/*
 *    Apriori.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.apriori;

import java.io.File;
import java.util.Vector;

import org.java.apriori.LargeItemSet.LargeItem;
import org.java.apriori.TxtReader.Attribute;

/**
 * This class contains Apriori operations.
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
public class Apriori {

	/** The reader of an TXT file. */
	private TxtReader m_TxtReader = null;

	/** The boolean array of choose attributes. */
	private boolean[] m_ChooseAttributes;

	/** The pretreat options. */
	private String[] m_PretreatOptions;

	/** The minimum support. */
	private double m_minSupport;

	/** The minimum confidence. */
	private double m_minConfidence;

	/** The set of all sets of itemsets L. */
	private Vector<LargeItemSet> m_Ls = null;

	/** The set of rules sorted. */
	private Vector<Object> m_sortedRuleSet = null;

	/**
	 * Creates an Apriori class.
	 */
	public Apriori() {

		m_Ls = new Vector<LargeItemSet>();
		m_sortedRuleSet = new Vector<Object>();
		setOptions(null);
	}

	/**
	 * Creates an Apriori class from the reader of an TXT file.
	 * 
	 * @param txtReader
	 *            the reader of an TXT file
	 */
	public Apriori(TxtReader txtReader) {

		this();
		m_TxtReader = txtReader;
		chooseAttributes(null);
	}

	/**
	 * Sets the mining options. The options contain the minimum support and the
	 * minimum confidence.
	 * 
	 * @param options
	 *            the mining options contain the minimum support and the minimum
	 *            confidence
	 */
	public void setOptions(double[] options) {

		if (options != null && options.length == 2) {
			m_minSupport = options[0];
			m_minConfidence = options[1];
		} else {
			m_minSupport = 0.2;
			m_minConfidence = 0.9;
		}
	}

	/**
	 * Sets the boolean array of choose attributes
	 * 
	 * @param chooseAttributes
	 *            the boolean array of choose attributes
	 */
	public void chooseAttributes(boolean[] chooseAttributes) {

		if (chooseAttributes != null
				&& chooseAttributes.length == m_TxtReader.numAttributes()) {
			m_ChooseAttributes = chooseAttributes;
		} else {
			m_ChooseAttributes = new boolean[m_TxtReader.numAttributes()];
			for (int i = 0; i < m_ChooseAttributes.length; i++)
				m_ChooseAttributes[i] = true;
		}
	}

	/**
	 * Sets the pretreat options.
	 * 
	 * @param pretreatOptions
	 *            the pretreat options
	 */
	public void setPretreatOptions(String[] pretreatOptions) {

		if (pretreatOptions != null
				&& pretreatOptions.length == m_TxtReader.numAttributes())
			m_PretreatOptions = pretreatOptions;
	}

	/**
	 * Method that finds all large itemsets for the given set of instances.
	 */
	public void findLargeItemSets() {

		int numSupport = (int) (m_minSupport * m_TxtReader.numInstances());

		m_Ls.removeAllElements();
		LargeItemSet largeItemSet = new LargeItemSet(pretreatAttributes(),
				m_ChooseAttributes, numSupport);

		if (largeItemSet.numLargeItem() > 0) {
			do {
				m_Ls.add(largeItemSet);
				largeItemSet = new LargeItemSet(largeItemSet,
						m_TxtReader.getInstances(), m_PretreatOptions);
			} while (largeItemSet.numLargeItem() > 0);
		}
	}

	/**
	 * Method that finds all association rules for the given set of large items.
	 */
	public void findAssociationsRules() {

		m_sortedRuleSet.removeAllElements();

		if (m_Ls.size() < 2)
			return;

		for (int i = 1; i < m_Ls.size(); i++) {
			Vector<LargeItem> largeItemSet = m_Ls.elementAt(i)
					.getLargeItemSet();
			for (int j = 0; j < largeItemSet.size(); j++) {
				LargeItem largeItem = largeItemSet.elementAt(j);

				for (int k = 0; k < i; k++) {
					Vector<LargeItem> kLargeItemSet = m_Ls.elementAt(k)
							.getLargeItemSet();
					for (int l = 0; l < kLargeItemSet.size(); l++) {
						LargeItem kLargeItem = kLargeItemSet.elementAt(l);

						if (largeItem.contains(kLargeItem)) {
							LargeItem splitLargeItem = largeItem
									.minus(kLargeItem);

							double ruleConfidence = (double) largeItem
									.getWeight() / kLargeItem.getWeight();
							
							if (ruleConfidence >= m_minConfidence) {

								if (numRules() == 0)
									m_sortedRuleSet.add(new Object[] {
											kLargeItem, splitLargeItem,
											ruleConfidence });
								else {
									int m = 0;
									for (; m < numRules(); m++) {
										if (((Double) (((Object[]) m_sortedRuleSet
												.elementAt(m))[2]))
												.doubleValue() <= ruleConfidence) {
											m_sortedRuleSet.add(m,
													new Object[] { kLargeItem,
															splitLargeItem,
															ruleConfidence });
											break;
										}
									}
									if (m == numRules())
										m_sortedRuleSet.add(new Object[] {
												kLargeItem, splitLargeItem,
												ruleConfidence });
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Returns the number of rules.
	 * 
	 * @return the number of rules
	 */
	public int numRules() {

		return m_sortedRuleSet.size();
	}

	/**
	 * Returns the number of choosed attributes.
	 * 
	 * @return the number of choosed attributes
	 */
	public int numChooseAttributes() {

		if (m_ChooseAttributes == null)
			return 0;

		int numChooseAttributes = 0;
		for (int i = 0; i < m_ChooseAttributes.length; i++) {
			if (m_ChooseAttributes[i])
				numChooseAttributes++;
		}

		return numChooseAttributes;
	}

	/**
	 * Returns the attributes as an string array.
	 * 
	 * @return the attributes as an string array
	 */
	public String[] attributesToStringArray() {

		String[] stringArray = new String[m_TxtReader.numAttributes()];

		for (int i = 0; i < m_TxtReader.numAttributes(); i++) {
			stringArray[i] = m_TxtReader.getAttributes().elementAt(i).getName();
		}

		return stringArray;
	}

	/**
	 * Returns the vector of attributes pretreated.
	 * 
	 * @return the vector of attributes pretreated
	 */
	public Vector<Attribute> pretreatAttributes() {

		if (m_PretreatOptions == null)
			return m_TxtReader.getAttributes();

		Vector<Attribute> pretreatAttributes = new Vector<Attribute>();

		for (int i = 0; i < m_TxtReader.numAttributes(); i++)
			pretreatAttributes.add(m_TxtReader.getAttributes().elementAt(i)
					.pretreat(m_PretreatOptions[i]));

		return pretreatAttributes;
	}

	/**
	 * Returns the choosed attributes as an string array.
	 * 
	 * @return the choosed attributes as an string array
	 */
	public String[] chooseAttributesToStringArray() {

		String[] stringArray = new String[numChooseAttributes()];

		for (int i = 0, j = 0; i < m_TxtReader.numAttributes(); i++) {
			if (m_ChooseAttributes[i])
				stringArray[j++] = m_TxtReader.getAttributes().elementAt(i)
						.getName();
		}

		return stringArray;
	}

	/**
	 * Returns the instances as an object array.
	 * 
	 * @return the instances as an object array
	 */
	public Object[][] instancesToObjectArray() {

		Object[][] objectArray = new Object[m_TxtReader.numInstances()][];

		for (int i = 0; i < m_TxtReader.numInstances(); i++) {
			objectArray[i] = (String[]) m_TxtReader.getInstances().elementAt(i);
		}

		return objectArray;
	}

	/**
	 * Returns the vector of instances pretreated.
	 * 
	 * @return the vector of instances pretreated
	 */
	public Vector<Object> pretreatInstances() {

		if (m_ChooseAttributes == null)
			return m_TxtReader.getInstances();

		Vector<Object> pretreatInstances = new Vector<Object>();
		String[] pretreatInstance = new String[numChooseAttributes()];

		for (int i = 0; i < m_TxtReader.numInstances(); i++) {
			String[] instance = (String[]) m_TxtReader.getInstances()
					.elementAt(i);

			for (int j = 0, k = 0; j < instance.length; j++) {
				if (m_ChooseAttributes[j])
					pretreatInstance[k++] = (m_PretreatOptions != null) ? Pretreat
							.pretreat(instance[j], m_PretreatOptions[j])
							: instance[j];
			}
			pretreatInstances.add(pretreatInstance);
			pretreatInstance = new String[numChooseAttributes()];
		}

		return pretreatInstances;
	}

	/**
	 * Returns the pretreated instances as an object array.
	 * 
	 * @return the pretreated instances as an object array
	 */
	public Object[][] pretreatInstancesToObjectArray() {

		if (m_ChooseAttributes == null)
			return instancesToObjectArray();

		Vector<Object> pretreatInstances = pretreatInstances();
		Object[][] objectArray = new Object[pretreatInstances.size()][numChooseAttributes()];

		for (int i = 0; i < pretreatInstances.size(); i++) {
			String[] pretreatInstance = (String[]) pretreatInstances
					.elementAt(i);
			for (int j = 0, k = 0; j < pretreatInstance.length; j++, k++)
				objectArray[i][k] = pretreatInstance[k];
		}

		return objectArray;
	}

	/**
	 * Returns the choosed attributes with pretreat options as an string.
	 * 
	 * @return the choosed attributes with pretreat options as an string
	 */
	public String pretreatToString() {

		StringBuilder sb = new StringBuilder();
		sb.append("属性 预处理：\n");

		String[] stringArray = attributesToStringArray();
		for (int i = 0; i < stringArray.length; i++) {
			if (m_ChooseAttributes[i]) {
				sb.append(stringArray[i]);
				if (m_PretreatOptions != null
						&& !m_PretreatOptions[i].equals(""))
					sb.append(" " + m_PretreatOptions[i]);
				else
					sb.append(" null");

				sb.append(", ");
			}
		}
		if(sb.length() >= 2)
			sb.delete(sb.length() - 2, sb.length());
		sb.append("\n\n");

		return sb.toString();
	}

	/**
	 * Returns a information of this Apriori.
	 * 
	 * @return a information of this Apriori
	 */
	public String information() {

		StringBuilder sb = new StringBuilder();

		sb.append("计划：\t" + "Apriori" + "\n");
		sb.append("关联：\t"
				+ m_TxtReader.getFile().getName().replaceAll("[.][^.]+$", "")
				+ "\n");
		sb.append("实例：\t" + m_TxtReader.numInstances() + "\n");
		sb.append("属性：\t");
		for (int i = 0; i < m_TxtReader.numAttributes(); i++) {
			if (m_ChooseAttributes == null || m_ChooseAttributes[i])
				sb.append(""
						+ m_TxtReader.getAttributes().elementAt(i).getName()
						+ ", ");
		}
		if (sb.length() >= 2)
			sb.delete(sb.length() - 2, sb.length());
		sb.append("\n\n");

		return sb.toString();
	}

	/**
	 * Returns a description of the options. The options contain the minimum
	 * support and the minimum confidence.
	 * 
	 * @return a description of the options
	 */
	public String optionsToString() {

		StringBuilder sb = new StringBuilder();

		sb.append("最小支持度: " + m_minSupport + "\n");
		sb.append("最小置信度: " + m_minConfidence + "\n\n");

		return sb.toString();
	}

	/**
	 * Returns a information of the class variable m_Ls.
	 * 
	 * @return a information of the class variable m_Ls
	 */
	public String lsInformationToString() {

		StringBuilder sb = new StringBuilder();
		sb.append("生成频繁项集：\n");

		for (int k = 0; k < m_Ls.size(); k++) {
			sb.append("频繁项集大小(");
			sb.append(k + 1);
			sb.append("): " + m_Ls.elementAt(k).numLargeItem() + "\n");
		}
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * Returns a description of the class variable m_Ls.
	 * 
	 * @return a description of the class variable m_Ls
	 */
	public String lsToString() {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < m_Ls.size(); i++) {
			sb.append("生成频繁项集(" + (i + 1) + "):\n");
			sb.append("{");
			for (int j = 0; j < m_Ls.elementAt(i).numLargeItem(); j++) {
				sb.append(largeItemToString(m_Ls.elementAt(i).getLargeItemSet()
						.elementAt(j)));
				if (j < m_Ls.elementAt(i).numLargeItem() - 1)
					sb.append(",");
			}
			sb.append(" }\n");
		}
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * Returns a description of the rule set.
	 * 
	 * @return a description of the rule set
	 */
	public String ruleSetToString() {

		StringBuilder sb = new StringBuilder();
		sb.append("生成关联规则：\n");

		for (int n = 0; n < m_sortedRuleSet.size(); n++) {
			Object[] objArray = (Object[]) m_sortedRuleSet.elementAt(n);
			sb.append(n + 1 + ".");

			sb.append(largeItemToString((LargeItem) objArray[0]));
			sb.append(" ==>");
			sb.append(largeItemToString((LargeItem) objArray[1]));

			sb.append("\tconf:(");
			sb.append(String.format("%.2f", objArray[2]));
			sb.append(")\n");
		}
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * Returns a description of the large item.
	 * 
	 * @param largeItem
	 *            the large item
	 * @return a description of the large item
	 */
	public String largeItemToString(LargeItem largeItem) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < largeItem.getValues().length; i++) {
			sb.append(" "
					+ m_TxtReader.getAttributes()
							.elementAt(largeItem.getIndexs()[i]).getName()
					+ "=" + largeItem.getValues()[i]);
		}
		sb.append(" " + largeItem.getWeight());

		return sb.toString();
	}

	/**
	 * Returns a description of this Apriori result.
	 * 
	 * @return a description of this Apriori result as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(information());
		sb.append(optionsToString());
		sb.append(lsInformationToString());
		sb.append(ruleSetToString());

		return sb.toString();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            should contain the name of an input file.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + Apriori.class.getName()
					+ " <dataset>\n");
			return;
		}

		Apriori apriori = new Apriori(new TxtReader(new File(args[0])));
		apriori.setOptions(null);
		apriori.chooseAttributes(null);
		apriori.setPretreatOptions(null);
		apriori.findLargeItemSets();
		apriori.findAssociationsRules();
		System.out.println(apriori);
	}

} // Apriori
