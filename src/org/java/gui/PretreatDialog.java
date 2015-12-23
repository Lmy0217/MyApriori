/*
 *    PretreatDialog.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.BorderFactory;

/**
 * Creates a panel that displays the attributes contained in a set of instances,
 * letting the user toggle whether each attribute is selected or not (eg: so
 * that unselected attributes can be removed before classification). <br>
 * Besides the All, None and Invert button one can also choose attributes which
 * names match a regular expression (Pattern button).
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
class PretreatPanel extends JPanel {

	/** for serialization */
	private static final long serialVersionUID = 627131485290359194L;

	/**
	 * A table model that looks at the names of attributes and maintains a list
	 * of attributes that have been "selected" and pretreat strings for
	 * attributes.
	 * 
	 * @author myluo
	 * @version $Revision: 1512 $
	 */
	class PretreatTableModel extends AbstractTableModel {

		/** for serialization */
		private static final long serialVersionUID = -4152987434024338064L;

		/** The instances who's attribute structure we are reporting */
		protected String[] m_Instances;

		/** The flag for whether the instance will be included */
		protected boolean[] m_Selected;

		/** The pretreat strings for attributes */
		protected String[] m_Pretreat;

		/**
		 * Creates the tablemodel with the given set of instances.
		 * 
		 * @param instances
		 *            the initial set of Instances
		 * @param selected
		 *            the initial of included attributes
		 * @param pretreat
		 *            the initial of pretreat strings
		 */
		public PretreatTableModel(String[] instances, boolean[] selected,
				String[] pretreat) {

			setInstances(instances, selected, pretreat);
		}

		/**
		 * Sets the tablemodel to look at a new set of instances.
		 * 
		 * @param instances
		 *            the new set of Instances.
		 * @param selected
		 *            the initial of included attributes
		 * @param pretreat
		 *            the initial of pretreat strings
		 */
		public void setInstances(String[] instances, boolean[] selected,
				String[] pretreat) {

			m_Instances = instances;
			m_Selected = new boolean[m_Instances.length];
			System.arraycopy(selected, 0, m_Selected, 0, m_Instances.length);
			m_Pretreat = new String[m_Instances.length];
			System.arraycopy(pretreat, 0, m_Pretreat, 0, m_Instances.length);
		}

		/**
		 * Gets the number of attributes.
		 * 
		 * @return the number of attributes.
		 */
		public int getRowCount() {

			return m_Selected.length;
		}

		/**
		 * Gets the number of columns: 4
		 * 
		 * @return 4
		 */
		public int getColumnCount() {

			return 4;
		}

		/**
		 * Gets a table cell
		 * 
		 * @param row
		 *            the row index
		 * @param column
		 *            the column index
		 * @return the value at row, column
		 */
		public Object getValueAt(int row, int column) {

			switch (column) {
			case 0:
				return new Integer(row + 1);
			case 1:
				return new Boolean(m_Selected[row]);
			case 2:
				return m_Instances[row];
			case 3:
				return m_Pretreat[row] != null ? m_Pretreat[row] : "";
			default:
				return null;
			}
		}

		/**
		 * Gets the name for a column.
		 * 
		 * @param column
		 *            the column index.
		 * @return the name of the column.
		 */
		public String getColumnName(int column) {

			switch (column) {
			case 0:
				return new String("序号");
			case 1:
				return new String("");
			case 2:
				return new String("属性");
			case 3:
				return new String("预处理");
			default:
				return null;
			}
		}

		/**
		 * Sets the value at a cell.
		 * 
		 * @param value
		 *            the new value.
		 * @param row
		 *            the row index.
		 * @param col
		 *            the column index.
		 */
		public void setValueAt(Object value, int row, int col) {

			if (col == 1) {
				m_Selected[row] = ((Boolean) value).booleanValue();
			} else if (col == 3) {
				m_Pretreat[row] = (String) value;
			}
		}

		/**
		 * Gets the class of elements in a column.
		 * 
		 * @param col
		 *            the column index.
		 * @return the class of elements in the column.
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int col) {

			return getValueAt(0, col).getClass();
		}

		/**
		 * Returns true if the column is the "selected" column.
		 * 
		 * @param row
		 *            ignored
		 * @param col
		 *            the column index.
		 * @return true if col == 1 or col == 3.
		 */
		public boolean isCellEditable(int row, int col) {

			if (col == 1 || col == 3) {
				return true;
			}
			return false;
		}

		/**
		 * Gets an array of selected indices.
		 * 
		 * @return the array of selected indices.
		 */
		public boolean[] getSelectedAttributes() {

			return m_Selected;
		}

		/**
		 * Gets an array of pretreat strings.
		 * 
		 * @return the array of pretreat strings.
		 */
		public String[] getPretreatString() {

			return m_Pretreat;
		}

		/**
		 * Sets the state of all attributes to selected.
		 */
		public void includeAll() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = true;
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

		/**
		 * Deselects all attributes.
		 */
		public void removeAll() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = false;
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

		/**
		 * Inverts the selected status of each attribute.
		 */
		public void invert() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = !m_Selected[i];
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

	} // PretreatTableModel

	/** Press to select all attributes */
	protected JButton m_IncludeAll = new JButton("全选");

	/** Press to deselect all attributes */
	protected JButton m_RemoveAll = new JButton("全不选");

	/** Press to invert the current selection */
	protected JButton m_Invert = new JButton("反选");

	/** The table displaying attribute names and selection status */
	protected JTable m_Table = new JTable();

	/** The table model containingn attribute names and selection status */
	protected PretreatTableModel m_Model;

	/**
	 * Creates the attribute selection panel with no initial instances.
	 */
	public PretreatPanel() {

		m_IncludeAll.setToolTipText("选择全部属性");
		m_IncludeAll.setEnabled(false);
		m_IncludeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.includeAll();
			}
		});
		m_RemoveAll.setToolTipText("不选全部属性");
		m_RemoveAll.setEnabled(false);
		m_RemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.removeAll();
			}
		});
		m_Invert.setToolTipText("反选当前选择");
		m_Invert.setEnabled(false);
		m_Invert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.invert();
			}
		});

		m_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_Table.setColumnSelectionAllowed(false);
		m_Table.setPreferredScrollableViewportSize(new Dimension(250, 150));

		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		p1.setLayout(new GridLayout(1, 4, 5, 5));
		p1.add(m_IncludeAll);
		p1.add(m_RemoveAll);
		p1.add(m_Invert);

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(new JScrollPane(m_Table), BorderLayout.CENTER);
	}

	/**
	 * Sets the instances who's attribute names will be displayed.
	 * 
	 * @param newInstances
	 *            the new set of instances
	 * @param selected
	 *            the array of included attributes
	 * @param pretreat
	 *            the array of pretreat strings
	 */
	public void setInstances(String[] newInstances, boolean[] selected,
			String[] pretreat) {

		if (m_Model == null) {
			m_Model = new PretreatTableModel(newInstances, selected, pretreat);
			m_Table.setModel(m_Model);
			TableColumnModel tcm = m_Table.getColumnModel();
			tcm.getColumn(0).setMaxWidth(60);
			tcm.getColumn(1).setMaxWidth(tcm.getColumn(1).getMinWidth());
			tcm.getColumn(2).setMinWidth(100);
			tcm.getColumn(3).setMinWidth(60);
		} else {
			m_Model.setInstances(newInstances, selected, pretreat);
			m_Table.clearSelection();
		}
		m_IncludeAll.setEnabled(true);
		m_RemoveAll.setEnabled(true);
		m_Invert.setEnabled(true);

		m_Table.doLayout();
		m_Table.revalidate();
		m_Table.repaint();
	}

	/**
	 * Gets an array of selected indices.
	 * 
	 * @return the array of selected indices.
	 */
	public boolean[] getSelectedAttributes() {

		return m_Model.getSelectedAttributes();
	}

	/**
	 * Gets an array of pretreat strings.
	 * 
	 * @return the array of pretreat strings.
	 */
	public String[] getPretreatString() {

		return m_Model.getPretreatString();
	}

	/**
	 * Gets the selection model used by the table.
	 * 
	 * @return a value of type 'ListSelectionModel'
	 */
	public ListSelectionModel getSelectionModel() {

		return m_Table.getSelectionModel();
	}

} // PretreatPanel

/**
 * Pretreat dialog.
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
public class PretreatDialog extends JDialog {

	/** for serialization */
	private static final long serialVersionUID = -4671522185385605849L;

	/** Saves the flag for whether the instance will be included */
	protected static boolean[] m_Selected;

	/** Saves the pretreat strings for attributes */
	protected static String[] m_Pretreat;

	/** Creates a pretreatpanel */
	protected PretreatPanel asp = new PretreatPanel();

	/**
	 * Creates the pretreatdialog with the given set of attributes.
	 * 
	 * @param JParent
	 *            the parent frame
	 * @param attributes
	 *            the given set of attributes
	 */
	public PretreatDialog(javax.swing.JFrame JParent, String[] attributes) {

		super(JParent, true);

		if (m_Selected == null) {
			m_Selected = new boolean[attributes.length];
			for (int i = 0; i < m_Selected.length; i++)
				m_Selected[i] = true;
		}
		if (m_Pretreat == null) {
			m_Pretreat = new String[attributes.length];
			for (int i = 0; i < m_Pretreat.length; i++)
				m_Pretreat[i] = "";
		}
		asp.setInstances(attributes, m_Selected, m_Pretreat);

		initGUI();
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				dispose();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * Initial GUI.
	 */
	protected void initGUI() {

		setTitle("预处理");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(asp, BorderLayout.CENTER);

		JButton m_Ok = new JButton("确定");
		m_Ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.arraycopy(asp.getSelectedAttributes(), 0, m_Selected, 0,
						m_Selected.length);
				System.arraycopy(asp.getPretreatString(), 0, m_Pretreat, 0,
						m_Pretreat.length);
				dispose();
			}
		});

		JButton m_Cancel = new JButton("取消");
		m_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel p2 = new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		p2.setLayout(new GridLayout(1, 4, 5, 5));
		p2.add(m_Ok);
		p2.add(m_Cancel);

		getContentPane().add(p2, BorderLayout.SOUTH);
	}

	/**
	 * Tests the pretreat panel from the command line.
	 * 
	 * @param args
	 *            must contain the name of an txt file to load.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + PretreatDialog.class.getName()
					+ " <dataset>\n");
			return;
		}

		final javax.swing.JFrame jf = new javax.swing.JFrame("Pretreat Dialog");

		final String[] i = new org.java.apriori.Apriori(
				new org.java.apriori.TxtReader(new java.io.File(args[0])))
		.attributesToStringArray();

		JButton button = new JButton("Test");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PretreatDialog(jf, i);
			}
		});

		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(button, BorderLayout.CENTER);
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				jf.dispose();
				System.exit(0);
			}
		});

		jf.pack();
		jf.setVisible(true);
	}

} // PretreatDialog
