/*
 *    Pretreat.java
 *    Copyright (C) 2015 University of NanChang, JiangXi, China
 *
 */

package org.java.apriori;

/**
 * Pretreats data.
 * 
 * @author myluo
 * @version $Revision: 1512 $
 */
public class Pretreat {

	/**
	 * Pretreats a string with option.
	 * 
	 * @param str
	 *            the string pretreated
	 * @param option
	 *            the pretreat option
	 * @return the string from str after pretreatment
	 */
	public static String pretreat(String str, String option) {

		if (option == null || option.length() <= 1)
			return str;

		String preStr = str;
		switch (option.charAt(0)) {
		// TODO You could add any options you need.
		case '/':
			preStr = ""
					+ (Integer.parseInt(str)
							/ Integer.parseInt(option.substring(1)) * Integer
							.parseInt(option.substring(1)));
			break;
		}

		return preStr;
	}
}
