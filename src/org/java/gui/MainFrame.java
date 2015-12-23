/*
 *    MainFrame.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.java.apriori.Apriori;

/**
 * The main frame for data mining.
 * 
 * @author ykzhang
 * @version $Revision: 1512 $
 */
public class MainFrame extends JFrame {

	/** for serialization */
	private static final long serialVersionUID = 125289602539003688L;

	/**
	 * The data table.
	 * 
	 * @author ykzhang
	 * @version $Revision: 1512 $
	 */
	public class DataTable extends JTable {

		/** for serialization */
		private static final long serialVersionUID = 3750484369708144828L;

		/**
		 * Creates the datetable with the given set of tabledata and the column
		 * names.
		 * 
		 * @param tableData
		 *            the given set of data
		 * @param columnNames
		 *            the column names
		 */
		public DataTable(Object[][] tableData, Object[] columnNames) {

			super(tableData, columnNames);

		}

		/**
		 * Returns true if the column is the "selected" column.
		 * 
		 * @param row
		 *            ignored
		 * @param col
		 *            ignored
		 * @return false
		 */
		public boolean isCellEditable(int row, int col) {

			return false;
		}

	} // DataTable

	/** Apriori operations. */
	private Apriori m_Apriori;

	/** table data as an object array. */
	private Object[][] m_TableData;

	/** column names as an string array. */
	private String[] m_ColumnNames;

	/** column names as an string array. */
	private String[] m_AttributeNames;

	/** the main frame. */
	private JFrame m_Frame;

	/** the data scroll pane. */
	private JScrollPane m_DataScrollPane;

	/** the label for data. */
	private JLabel m_DataLabel;

	/** the table for data. */
	private JTable m_Table;

	/** the result scroll pane. */
	private JScrollPane m_ResultScrollPane;

	/** the table for result. */
	private JLabel m_ResultLabel;

	/** the test area for result. */
	private JTextArea m_Result;

	/** the menu bar. */
	private JMenuBar m_MenuBar;

	/** the menu data. */
	private JMenu m_Data;

	/** the menu set. */
	private JMenu m_Set;

	/** the menu analyze. */
	private JMenu m_Analyze;

	/** the menu about. */
	private JMenu m_Help;

	/** the menu item open. */
	private JMenuItem m_Open;

	/** the menu item pretreatment. */
	private JMenuItem m_Pretreatment;

	/** the menu item mining options. */
	private JMenuItem m_MiningOptions;

	/** the menu item largeItemSets. */
	private JMenuItem m_LargeItemSets;

	/** the menu item associationRules. */
	private JMenuItem m_AssociationRules;

	/** the menu item association analysis. */
	private JMenuItem m_AssociationAnalysis;

	/** the menu item version. */
	private JMenuItem m_About;

	/**
	 * Creates main frame.
	 */
	public MainFrame() {

		super("	数据挖掘");
		setWinStyle();
		initGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initial GUI.
	 */
	private void initGUI() {

		m_Frame = this;
		setSize(800, 700);

		setLayout(new GridLayout(2, 1, 0, 0));

		m_DataScrollPane = new JScrollPane();
		add(m_DataScrollPane);
		m_DataLabel = new JLabel("数据");
		m_DataLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		m_DataScrollPane.setColumnHeaderView(m_DataLabel);

		m_ResultScrollPane = new JScrollPane();
		add(m_ResultScrollPane);
		m_ResultLabel = new JLabel("结果");
		m_ResultLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		m_ResultScrollPane.setColumnHeaderView(m_ResultLabel);

		m_Result = new JTextArea();
		m_Result.setEditable(false);
		m_ResultScrollPane.setViewportView(m_Result);

		m_Data = new JMenu("数据");
		m_Set = new JMenu("设置");
		m_Analyze = new JMenu("挖掘");
		m_Help = new JMenu("帮助");

		m_Open = new JMenuItem("数据导入");
		m_Pretreatment = new JMenuItem("预处理");
		m_MiningOptions = new JMenuItem("挖掘参数");
		m_LargeItemSets = new JMenuItem("生成频繁集");
		m_AssociationRules = new JMenuItem("生成关联规则");
		m_AssociationAnalysis = new JMenuItem("数据挖掘");
		m_About = new JMenuItem("关于");

		m_MenuBar = new JMenuBar();
		m_Data.add(m_Open);
		m_Data.add(m_Pretreatment);
		m_Set.add(m_MiningOptions);
		m_Analyze.add(m_LargeItemSets);
		m_Analyze.add(m_AssociationRules);
		m_Analyze.add(m_AssociationAnalysis);
		m_Help.add(m_About);
		m_MenuBar.add(m_Data);
		m_MenuBar.add(m_Set);
		m_MenuBar.add(m_Analyze);
		m_MenuBar.add(m_Help);
		setJMenuBar(m_MenuBar);

		m_Pretreatment.setEnabled(false);
		m_MiningOptions.setEnabled(false);
		m_LargeItemSets.setEnabled(false);
		m_AssociationRules.setEnabled(false);
		m_AssociationAnalysis.setEnabled(false);

		m_Open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".txt", "txt");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(true);

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					clear();
					m_Apriori = new Apriori(new org.java.apriori.TxtReader(
							fileChooser.getSelectedFile()));
					m_AttributeNames = m_Apriori.attributesToStringArray();
					m_TableData = m_Apriori.instancesToObjectArray();
					m_ColumnNames = m_Apriori.attributesToStringArray();

					createTable(m_TableData, m_ColumnNames);

					m_Pretreatment.setEnabled(true);
					m_MiningOptions.setEnabled(true);
					m_LargeItemSets.setEnabled(true);
					m_AssociationRules.setEnabled(false);
					m_AssociationAnalysis.setEnabled(true);

					printInformation(m_Apriori.information());
				}
			}
		});

		m_Pretreatment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_AttributeNames != null) {
					new PretreatDialog(m_Frame, m_AttributeNames);
					m_Apriori.chooseAttributes(PretreatDialog.m_Selected);
					m_Apriori.setPretreatOptions(PretreatDialog.m_Pretreat);
					m_TableData = m_Apriori.pretreatInstancesToObjectArray();
					m_ColumnNames = m_Apriori.chooseAttributesToStringArray();
					createTable(m_TableData, m_ColumnNames);
					printInformation(m_Apriori.pretreatToString());
				}
			}
		});

		m_MiningOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Apriori.setOptions(new OptionDialog(m_Frame).getOptions());
				printInformation(m_Apriori.optionsToString());
			}
		});

		m_LargeItemSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Apriori.findLargeItemSets();
				m_AssociationRules.setEnabled(true);
				printInformation(m_Apriori.lsToString());
			}
		});

		m_AssociationRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Apriori.findAssociationsRules();
				printInformation(m_Apriori.ruleSetToString());
			}
		});

		m_AssociationAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Apriori.findLargeItemSets();
				m_Apriori.findAssociationsRules();
				printInformation(m_Apriori.toString());
			}
		});

		m_About.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String information = "\nVersion: 1.0\n"
						+ "Author: myluo, ykzhang\n"
						+ "Copyright (C) 2015 University of NanChang, JiangXi, China\n\n";
				JOptionPane.showMessageDialog(null, information, "关于",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
	}

	/**
	 * Clears at first.
	 */
	public void clear() {

		PretreatDialog.m_Selected = null;
		PretreatDialog.m_Pretreat = null;
		OptionDialog.m_Options = null;
	}

	/**
	 * Prints information with style.
	 * 
	 * @param information
	 *            the printed information
	 */
	public void printInformation(String information) {

		m_Result.append("====== " + new java.util.Date() + " ======\n");
		m_Result.append(information);
		m_Result.paintImmediately(m_Result.getBounds());
		m_Result.setCaretPosition(m_Result.getText().length());
	}

	/**
	 * Creates data table
	 * 
	 * @param tableData
	 *            the table data
	 * @param columnNames
	 *            the columns names
	 */
	private void createTable(Object[][] tableData, String[] columnNames) {

		m_Table = new DataTable(tableData, columnNames);
		TableColumnModel tcm = m_Table.getColumnModel();

		for (int i = 0; i < tcm.getColumnCount(); i++) {
			int width = (int) m_Table
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(m_Table,
							tcm.getColumn(i).getIdentifier(), false, false, -1,
							i).getPreferredSize().getWidth();
			int preferedWidth = (int) m_Table
					.getCellRenderer(0, i)
					.getTableCellRendererComponent(m_Table,
							m_Table.getValueAt(0, i), false, false, 0, i)
							.getPreferredSize().getWidth();
			width = Math.max(width, preferedWidth);
			m_Table.getColumnModel().getColumn(i).setMinWidth(width + 4);
		}

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.RIGHT);
		m_Table.setDefaultRenderer(Object.class, tcr);
		m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		m_Table.doLayout();
		m_DataScrollPane.setViewportView(m_Table);
	}

	/**
	 * Sets window style with system.
	 */
	private void setWinStyle() {

		try {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		MainFrame testFrame = new MainFrame();
		testFrame.setTitle("数据挖掘");
	}

} // MainFrame
