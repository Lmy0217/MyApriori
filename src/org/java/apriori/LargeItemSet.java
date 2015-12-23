/*
 *    LargeItemSet.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.apriori;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

import org.java.apriori.TxtReader.Attribute;

/**
 * Find the set of large items.
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
public class LargeItemSet {

	/** The set of large items. */
	private Vector<LargeItem> m_LargeItemSet;

	/** The minimum weight. */
	private int m_minWeight;

	/**
	 * The large item.
	 * 
	 * @author myluo
	 * @version $Revision: 1512 $
	 */
	class LargeItem implements Serializable, Comparable<LargeItem> {

		/** for serialization */
		private static final long serialVersionUID = 8947607543530804751L;

		/** The large item's values. */
		private String[] m_Values;

		/** The values's indexs of all attributes. */
		private int[] m_Indexs;

		/** The large item's weight in all instances. */
		private int m_Weight;

		/**
		 * Creates a large item.
		 */
		public LargeItem() {

		}

		/**
		 * Creates a large item with values and indexs.
		 * 
		 * @param values
		 *            the large item's values
		 * @param indexs
		 *            the large item's indexs
		 */
		public LargeItem(String[] values, int[] indexs) {

			this();
			m_Values = values;
			m_Indexs = indexs;
		}

		/**
		 * Creates a large item with values, indexs and weight.
		 * 
		 * @param values
		 *            the large item's values
		 * @param indexs
		 *            the large item's indexs
		 * @param weight
		 *            the large item's weight
		 */
		public LargeItem(String[] values, int[] indexs, int weight) {

			this(values, indexs);
			setWeight(weight);
		}

		/**
		 * Returns the number of large item values.
		 * 
		 * @return the number of large item values
		 */
		public int numValuesInItem() {

			return m_Values.length;
		}

		/**
		 * Returns the large item's values as a string array.
		 * 
		 * @return the large item's values as a string array
		 */
		public String[] getValues() {

			return m_Values;
		}

		/**
		 * Returns the values's indexs as a int array.
		 * 
		 * @return the values's indexs as a int array
		 */
		public int[] getIndexs() {

			return m_Indexs;
		}

		/**
		 * Sets the large item's weight.
		 * 
		 * @param weight
		 *            the large item's weight
		 */
		public void setWeight(int weight) {

			m_Weight = weight;
		}

		/**
		 * Returns the large item's weight.
		 * 
		 * @return the large item's weight
		 */
		public int getWeight() {

			return m_Weight;
		}

		/**
		 * Returns true if and only if this large item contains largeItem.
		 * 
		 * @param largeItem
		 *            the large item to search for
		 * @return true if this large item contains largeItem, false otherwise
		 */
		public boolean contains(LargeItem largeItem) {

			int count = 0;
			for (int i = 0, j = 0; i < largeItem.getIndexs().length; i++) {
				for (; j < m_Indexs.length; j++) {

					if (largeItem.getIndexs()[i] == m_Indexs[j]
							&& largeItem.getValues()[i].equals(m_Values[j])) {
						count++;
						break;
					}
				}
			}

			if (count == largeItem.getValues().length)
				return true;
			else
				return false;
		}

		/**
		 * Returns the large item minus some items in this large item that
		 * include in this large item and the given large item.
		 * 
		 * @param large
		 *            item the large item to minus
		 * @return the large item that this large item minus the given large
		 *         item
		 */
		public LargeItem minus(LargeItem largeItem) {

			String[] newValues = new String[m_Values.length
			                                - largeItem.getValues().length];
			int[] newIndexs = new int[m_Indexs.length
			                          - largeItem.getIndexs().length];
			int index = 0;

			for (int i = 0, j = 0; i < m_Indexs.length; i++) {
				for (; j < largeItem.getIndexs().length; j++) {

					if (largeItem.getIndexs()[j] == m_Indexs[i]
							&& largeItem.getValues()[j].equals(m_Values[i])) {
						break;
					} else if (largeItem.getIndexs()[j] > m_Indexs[i]) {
						newValues[index] = new String(m_Values[i]);
						newIndexs[index] = m_Indexs[i];
						index++;
						break;
					}
				}
			}

			while (index < newValues.length) {
				newValues[index] = new String(m_Values[m_Values.length
				                                       - newValues.length + index]);
				newIndexs[index] = m_Indexs[m_Values.length - newValues.length
				                            + index];
				index++;
			}

			return new LargeItem(newValues, newIndexs, m_Weight);
		}

		/**
		 * Compares two large item.
		 * 
		 * @param largeItem
		 *            the large item to be compared
		 * @return the value 0 if the argument largeItem's items count is not
		 *         equal to this largeItem's items count or two largeItem's
		 *         items is equivalent except the last item; the value 1 if the
		 *         argument largeItem's last item's index is greater than this
		 *         largeItem's last item's index; the value -1 if the argument
		 *         largeItem's last item's index is less than this largeItem's
		 *         last item's index;
		 */
		public int compareTo(LargeItem largeItem) {

			if (m_Values.length != largeItem.getValues().length
					|| m_Values.length <= 0)
				return 0;

			if (m_Indexs[m_Indexs.length - 1] == largeItem.getIndexs()[largeItem
			                                                           .getIndexs().length - 1])
				return 0;

			for (int i = 0; i < m_Values.length - 1; i++) {
				if (!(m_Indexs[i] == largeItem.getIndexs()[i] && m_Values[i]
						.equals(largeItem.getValues()[i])))
					return 0;
			}

			return (m_Indexs[m_Indexs.length - 1] < largeItem.getIndexs()[largeItem
			                                                              .getIndexs().length - 1]) ? 1 : -1;
		}

		/**
		 * Returns a description of this large item.
		 * 
		 * @return a description of this large item
		 */
		public String toString() {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < m_Values.length; i++) {
				sb.append(" " + m_Indexs[i] + "=" + m_Values[i]);
			}
			sb.append(" " + m_Weight);

			return sb.toString();
		}

	}

	/**
	 * Creates a set of large item.
	 */
	public LargeItemSet() {

		m_LargeItemSet = new Vector<LargeItem>();
	}

	/**
	 * Creates a set of 1-largeItems from the attributes and the option.
	 * 
	 * @param pretreatAttributes
	 *            the vector of attributes after pretreatment
	 * @param chooseAttributes
	 *            the boolean array of choose attributes
	 * @param option
	 *            the option is the minimum weight
	 */
	public LargeItemSet(Vector<Attribute> pretreatAttributes,
			boolean[] chooseAttributes, int option) {

		this();
		setOption(option);
		findOneLargeItemSet(pretreatAttributes, chooseAttributes);
	}

	/**
	 * Creates a set of k-largeItems from the set of (k-1)-largeItems and the
	 * instances pretreated.
	 * 
	 * @param largeItemSet
	 *            the set of (k-1)-largeItems
	 * @param instances
	 *            the vector for instances
	 * @param pretreatOptions
	 *            the pretreat options
	 */
	public LargeItemSet(LargeItemSet largeItemSet, Vector<Object> instances,
			String[] pretreatOptions) {

		this();
		setOption(largeItemSet.getOption());
		findKLargeItemSet(largeItemSet, instances, pretreatOptions);
	}

	/**
	 * Sets the option. The option is the minimum weight.
	 * 
	 * @param option
	 *            the option is the minimum weight
	 */
	public void setOption(int option) {

		m_minWeight = option;
	}

	/**
	 * Gets the option. The option is the minimum weight.
	 * 
	 * @return option the option is the minimum weight
	 */
	public int getOption() {

		return m_minWeight;
	}

	/**
	 * Returns the number of large items.
	 * 
	 * @return the number of large items.
	 */
	public int numLargeItem() {

		return m_LargeItemSet.size();
	}

	/**
	 * Returns the set of large items.
	 * 
	 * @return the set of large items
	 */
	public Vector<LargeItem> getLargeItemSet() {

		return m_LargeItemSet;
	}

	/**
	 * Method that finds the set of 1-largeItems for the given set of attributes
	 * pretreated.
	 * 
	 * @param pretreatAttributes
	 *            the vector of attributes pretreated
	 * @param chooseAttributes
	 *            the boolean array of choose attributes
	 */
	private void findOneLargeItemSet(Vector<Attribute> pretreatAttributes,
			boolean[] chooseAttributes) {

		for (int i = 0; i < pretreatAttributes.size(); i++) {
			if (chooseAttributes != null && !chooseAttributes[i])
				continue;

			Attribute attribute = pretreatAttributes.elementAt(i);
			for (int j = 0; j < attribute.numValues(); j++) {

				if (attribute.getWeights().elementAt(j) > m_minWeight) {
					m_LargeItemSet.add(new LargeItem(new String[] { attribute
							.getValues().elementAt(j) }, new int[] { i },
							attribute.getWeights().elementAt(j).intValue()));
				}
			}
		}
	}

	/**
	 * Method that finds the set of k-largeItems for the given set of
	 * (k-1)-largeItems and the instances pretreated.
	 * 
	 * @param largeItemSet
	 *            the set of (k-1)-largeItems
	 * @param instances
	 *            the vector for instances
	 * @param pretreatOptions
	 *            the pretreat options
	 */
	private void findKLargeItemSet(LargeItemSet largeItemSet,
			Vector<Object> instances, String[] pretreatOptions) {

		Vector<LargeItem> itemSet = largeItemSet.getLargeItemSet();
		int temp;

		for (int i = 0; i < largeItemSet.numLargeItem(); i++) {
			for (int j = i + 1; j < largeItemSet.numLargeItem(); j++) {

				if ((temp = itemSet.elementAt(i)
						.compareTo(itemSet.elementAt(j))) != 0) {
					String[] proValues = itemSet.elementAt(i).getValues();
					int[] proIndexs = itemSet.elementAt(i).getIndexs();

					String[] values = new String[proValues.length + 1];
					int[] indexs = new int[proIndexs.length + 1];

					System.arraycopy(proValues, 0, values, 0, proValues.length);
					System.arraycopy(proIndexs, 0, indexs, 0, proIndexs.length);

					if (temp == 1) {
						System.arraycopy(itemSet.elementAt(j).getValues(),
								proValues.length - 1, values,
								values.length - 1, 1);
						indexs[indexs.length - 1] = itemSet.elementAt(j)
								.getIndexs()[proIndexs.length - 1];
					} else if (temp == -1) {
						System.arraycopy(itemSet.elementAt(j).getValues(),
								proValues.length - 1, values,
								values.length - 2, 1);
						indexs[indexs.length - 2] = itemSet.elementAt(j)
								.getIndexs()[proIndexs.length - 1];
						System.arraycopy(proValues, proValues.length - 1,
								values, indexs.length - 1, 1);
						indexs[indexs.length - 1] = proIndexs[proIndexs.length - 1];
					}

					int weight = weight(values, indexs, instances,
							pretreatOptions);
					if (weight > m_minWeight)
						m_LargeItemSet
						.add(new LargeItem(values, indexs, weight));
				}
			}
		}
	}

	/**
	 * Computes the weight for the given items in instances pretreated.
	 * 
	 * @param values
	 *            the item's values
	 * @param indexs
	 *            the values's indexs of all attributes
	 * @param instances
	 *            the vector for instances
	 * @param pretreatOptions
	 *            the pretreat options
	 * @return the weight for the given items in instances pretreated
	 */
	private int weight(String[] values, int[] indexs, Vector<Object> instances,
			String[] pretreatOptions) {

		int weight = 0;
		for (Object obj : instances) {
			String[] stringArray = (String[]) obj;
			int count = 0;
			for (int i = 0; i < values.length; i++) {
				if (((pretreatOptions != null) ? Pretreat.pretreat(
						stringArray[indexs[i]], pretreatOptions[indexs[i]])
						: stringArray[indexs[i]]).equals(values[i])) {
					count++;
				} else {
					break;
				}
			}
			if (count == values.length)
				weight++;
		}

		return weight;
	}

	/**
	 * Returns a description of this large item set.
	 * 
	 * @return a description of this large item set
	 */
	public String toString() {

		return m_LargeItemSet.toString();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            should contain the name of an input file.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + LargeItemSet.class.getName()
					+ " <dataset>\n");
			return;
		}

		TxtReader txtReader = new TxtReader(new File(args[0]));
		LargeItemSet largeItemSet = new LargeItemSet(txtReader.getAttributes(),
				null, (int) (0.1 * txtReader.numInstances()));
		if (largeItemSet.numLargeItem() > 0) {
			do {
				System.out.println(largeItemSet);
				largeItemSet = new LargeItemSet(largeItemSet,
						txtReader.getInstances(), null);
			} while (largeItemSet.numLargeItem() > 0);
		}
	}

}
