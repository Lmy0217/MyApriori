/*
 *    OptionDialog.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Mining options setting dialog.
 * 
 * @author ykzhang
 * @version $Revision: 1512 $
 */
public class OptionDialog extends JDialog {

	/** for serialization */
	private static final long serialVersionUID = 6393927172003237817L;

	/** The text field for minimum support. */
	private JTextField m_minSupport;

	/** The text field for minimum confidence. */
	private JTextField m_minConfidence;

	/** The minimum support label. */
	private JLabel m_minSupportLabel;

	/** The minimum confidence label. */
	private JLabel m_minConflabel;

	/** The okay button. */
	private JButton m_Ok;

	/** The cancel button. */
	private JButton m_Cancel;

	/** The button panel. */
	private JPanel m_ButtonPanel;

	/** The option panel. */
	private JPanel m_OptionPanel;

	/** The double array for mining options. */
	protected static double[] m_Options;

	/**
	 * Creates option dialog.
	 * 
	 * @param jParent
	 *            the parent frame
	 */
	public OptionDialog(JFrame jParent) {

		super(jParent, true);

		if (m_Options == null)
			m_Options = new double[] { 0.2, 0.9 };

		initGUI();
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	/**
	 * Initial GUI.
	 */
	private void initGUI() {

		setTitle("参数设置");
		getContentPane().setLayout(new BorderLayout());

		m_minSupportLabel = new JLabel("最小支持度");
		m_minConflabel = new JLabel("最小置信度");

		m_minSupport = new JTextField("" + m_Options[0]);
		m_minSupport.setEditable(true);
		m_minSupport.setHorizontalAlignment(JTextField.LEFT);

		m_minConfidence = new JTextField("" + m_Options[1]);
		m_minConfidence.setEditable(true);
		m_minConfidence.setHorizontalAlignment(JTextField.LEFT);

		m_OptionPanel = new JPanel();
		m_OptionPanel.setLayout(new GridLayout(2, 2, 5, 5));
		m_OptionPanel.add(m_minSupportLabel);
		m_OptionPanel.add(m_minSupport);
		m_OptionPanel.add(m_minConflabel);
		m_OptionPanel.add(m_minConfidence);
		add(m_OptionPanel, BorderLayout.CENTER);

		m_Ok = new JButton("确定");
		m_Cancel = new JButton("取消");

		m_ButtonPanel = new JPanel();
		m_ButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		m_ButtonPanel.setLayout(new GridLayout(1, 4, 5, 5));
		m_ButtonPanel.add(m_Ok);
		m_ButtonPanel.add(m_Cancel);
		add(m_ButtonPanel, BorderLayout.SOUTH);

		m_minSupport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				m_minSupport.transferFocus();
			}
		});

		m_minConfidence.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				m_minConfidence.transferFocus();
			}
		});

		m_Cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
			}
		});

		m_Ok.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				options();
				dispose();
			}
		});

	}

	/**
	 * Sets mining options. Options included the minimum support and the minimum
	 * confidence.
	 */
	private void options() {

		try {
			m_Options[0] = Double.parseDouble(m_minSupport.getText());
			if (m_Options[0] < 0.0 || m_Options[0] > 1.0)
				m_Options[0] = 0.2;
		} catch (NumberFormatException e) {
			m_Options[0] = 0.2;
		}

		try {
			m_Options[1] = Double.parseDouble(m_minConfidence.getText());
			if (m_Options[1] < 0.0 || m_Options[1] > 1.0)
				m_Options[1] = 0.9;
		} catch (NumberFormatException e) {
			m_Options[1] = 0.9;
		}
	}

	/**
	 * Return the mining options.
	 * 
	 * @return the mining options
	 */
	public double[] getOptions() {

		return m_Options;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		final JFrame frame = new JFrame();

		JButton jb = new JButton("Test");
		jb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new OptionDialog(frame);
			}
		});

		frame.add(jb);
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

} // OptionDialog
