/*
 *    TxtReader.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.Vector;

/**
 * Reads data from an TXT file.
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
public class TxtReader {

	/** The source file. */
	private File m_file = null;

	/** The reader for the source file. */
	private Reader m_sourceReader = null;

	/** The tokenizer for reading the stream. */
	private StreamTokenizer m_Tokenizer = null;

	/** The vector for attributes. */
	private Vector<Attribute> attributes = null;

	/** The vector for instances. */
	private Vector<Object> instances = null;

	/**
	 * Class for handling an attribute. Once an attribute has been created, it
	 * can't be changed.
	 * 
	 * @author myluo
	 * @version $Revision: 1512 $
	 */
	public class Attribute implements Serializable {

		/** for serialization */
		private static final long serialVersionUID = 4581950157712585216L;

		/** The attribute's name. */
		private String m_Name;

		/** The attribute's type. */
		private int m_Type;

		/** The attribute's values. */
		private Vector<String> m_Values = null;

		/** The value' weights. */
		private Vector<Integer> m_Weights = null;

		/**
		 * Constructor for a attribute.
		 */
		public Attribute() {

			m_Values = new Vector<String>();
			m_Weights = new Vector<Integer>();
		}

		/**
		 * Constructor for a attribute.
		 * 
		 * @param attributeName
		 *            the name for the attribute
		 */
		public Attribute(String attributeName) {

			this();
			m_Name = attributeName;
		}

		/**
		 * Returns the attribute's name.
		 * 
		 * @return the attribute's name as a string
		 */
		public String getName() {

			return m_Name;
		}

		/**
		 * sets the attribute's type
		 * 
		 * @param type
		 *            the attribute's type
		 */
		public void setType(int type) {

			m_Type = type;
		}

		/**
		 * Returns the attribute's type as an integer.
		 * 
		 * @return the attribute's type
		 */
		public int getType() {

			return m_Type;
		}

		/**
		 * Returns the number of attribute values.
		 * 
		 * @return the number of attribute values
		 */
		public int numValues() {

			return m_Values.size();
		}

		/**
		 * Adds an attribute value.
		 * 
		 * @param value
		 *            the attribute value
		 */
		private void append(String value) {

			append(value, 1);
		}

		/**
		 * Adds some attribute value.
		 * 
		 * @param value
		 *            the attribute value
		 * @param number
		 *            the number of added
		 */
		private void append(String value, int number) {

			int index = m_Values.indexOf(value);
			if (index != -1) {
				m_Weights.setElementAt(m_Weights.elementAt(index) + number,
						index);
			} else {
				m_Values.addElement(value);
				m_Weights.addElement(new Integer(number));
			}
		}

		/**
		 * Returns a description of this attribute in TXT format after
		 * pretreatment.
		 * 
		 * @param option
		 *            the pretreat option
		 * @return a description of this attribute in TXT format after
		 *         pretreatment
		 */
		public Attribute pretreat(String option) {

			if (option == null)
				return this;

			Attribute attribute = new Attribute(m_Name);
			for (int i = 0; i < numValues(); i++) {
				attribute.append(
						Pretreat.pretreat(m_Values.elementAt(i), option),
						m_Weights.elementAt(i));
			}

			return attribute;
		}

		/**
		 * Returns the vector of values.
		 * 
		 * @return the vector of values
		 */
		public Vector<String> getValues() {

			return m_Values;
		}

		/**
		 * Returns the vector of weights.
		 * 
		 * @return the vector of weights
		 */
		public Vector<Integer> getWeights() {

			return m_Weights;
		}

		/**
		 * Returns a description of this attribute in TXT format.
		 * 
		 * @return a description of this attribute as a string
		 */
		public String toString() {

			StringBuilder sb = new StringBuilder();

			sb.append("@attribute " + getName() + " {");
			for (int i = 0; i < numValues(); i++) {
				sb.append(" " + m_Values.elementAt(i));
				if (i < numValues() - 1)
					sb.append(",");
			}
			sb.append(" }\n");

			return sb.toString();
		}
	}

	/**
	 * Reads the data completely from the reader.
	 */
	public TxtReader() {

	}

	/**
	 * Reads the data completely from the reader.
	 * 
	 * @param file
	 *            the reader for the source file
	 */
	public TxtReader(File file) {

		this();
		setSource(file);
		initTokenizer();
		readAttributes();
		readInstances();
	}

	/**
	 * sets the source File
	 * 
	 * @param file
	 *            the source file
	 */
	public void setSource(File file) {

		try {
			m_sourceReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			m_file = file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the StreamTokenizer used for reading the TXT file.
	 */
	private void initTokenizer() {

		if (m_sourceReader == null)
			return;

		m_Tokenizer = new StreamTokenizer(m_sourceReader);
		m_Tokenizer.resetSyntax();
		m_Tokenizer.whitespaceChars(0, ' ');
		m_Tokenizer.wordChars(' ' + 1, '\u00FF');
		m_Tokenizer.whitespaceChars(',', ',');
		m_Tokenizer.commentChar('%');
		m_Tokenizer.quoteChar('"');
		m_Tokenizer.quoteChar('\'');
		m_Tokenizer.ordinaryChar('{');
		m_Tokenizer.ordinaryChar('}');
		m_Tokenizer.eolIsSignificant(true);
	}

	/**
	 * Reads and stores attributes of an TXT file.
	 */
	private void readAttributes() {

		if (m_Tokenizer == null)
			return;

		attributes = new Vector<Attribute>();

		try {
			while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
				attributes.add(new Attribute(m_Tokenizer.sval));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads and stores instances of an TXT file.
	 */
	private void readInstances() {

		if (m_Tokenizer == null)
			return;

		instances = new Vector<Object>();
		String[] instance = new String[numAttributes()];
		int index = -1;

		try {
			while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				if (m_Tokenizer.ttype != StreamTokenizer.TT_EOL
						&& ++index < numAttributes()) {

					if (m_Tokenizer.ttype == StreamTokenizer.TT_WORD)
						instance[index] = m_Tokenizer.sval;
					else if (m_Tokenizer.ttype == StreamTokenizer.TT_NUMBER)
						instance[index] = "" + (int) m_Tokenizer.nval;

					attributes.elementAt(index).append(instance[index]);
				} else {
					if (index + 1 >= numAttributes())
						instances.add(instance);
					instance = new String[numAttributes()];
					index = -1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the number of attributes.
	 * 
	 * @return the number of attributes
	 */
	public int numAttributes() {

		return attributes.size();
	}

	/**
	 * Returns the number of instances.
	 * 
	 * @return the number of instances
	 */
	public int numInstances() {

		return instances.size();
	}

	/**
	 * Returns the source file.
	 * 
	 * @return the source file
	 */
	public File getFile() {

		return m_file;
	}

	/**
	 * Returns the vector of attributes.
	 * 
	 * @return the vector of attributes
	 */
	public Vector<Attribute> getAttributes() {

		return attributes;
	}

	/**
	 * Returns the vector of instances.
	 * 
	 * @return the vector of instances
	 */
	public Vector<Object> getInstances() {

		return instances;
	}

	/**
	 * Returns a description of this txtReader in TXT format.
	 * 
	 * @return a description of this txtReader as a string
	 */
	public String toString() {

		if (attributes == null || instances == null)
			return null;

		StringBuilder sb = new StringBuilder("");

		for (Attribute attribute : attributes)
			sb.append(attribute);
		sb.append("\n");

		sb.append("@data\n\n");
		for (Object instance : instances) {
			for (String str : (String[]) instance)
				sb.append(" " + str);
			sb.append("\n");
		}

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
			System.out.println("\nUsage: " + TxtReader.class.getName()
					+ " <dataset>\n");
			return;
		}

		System.out.println(new TxtReader(new File(args[0])));
	}
}
