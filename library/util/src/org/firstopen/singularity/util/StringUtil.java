/*
 * Copyright 2005 i-Konect LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * A simple utility class that deals with String class
 */
public final class StringUtil {
	
	static Logger log = Logger.getLogger(StringUtil.class);
	/**
	 * Checks to see if the input string is either NULL or empty.
	 * 
	 * @param String
	 *            input string
	 * @return boolean true if the string is empty, false otherwise
	 */
	public static boolean isEmpty(String string) {
		if (string == null || string.length() == 0)
			return true;
		return false;
	}

	/**
	 * Checks to see if the input string is "true". This utility is useful when
	 * a string is used to represent a boolean ("true" or "false").<BR>
	 * NOTE: This routine is case in-sensitive.
	 * 
	 * @param String
	 *            input string
	 * @return boolean true if the input is "true", false otherwise
	 */
	public static boolean isTrue(String string) {
		if (null != string && string.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}

	/**
	 * Checks to see if the input string is "false". This utility is useful when
	 * a string is used to represent a boolean ("true" or "false").<BR>
	 * NOTE: This routine is case in-sensitive.
	 * 
	 * @param String
	 *            input string
	 * @return boolean true if the input is "false", false otherwise
	 */
	public static boolean isFalse(String string) {
		if (null != string && string.equalsIgnoreCase("false"))
			return true;
		else
			return false;
	}

	/**
	 * Convert an array object to string, the component of the array should have
	 * a public method toString()
	 * 
	 * @param Object[]
	 *            an input array of objects
	 * @return String the array converted into a string
	 */
	public static String arrayToString(Object[] array) {
		if (null == array)
			return null;

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < array.length; i++)
			s.append(array[i].toString());
		return s.toString();
	}

	public static String intArrayToString(int[] array,
			boolean whiteSpaceSeperator) {
		if (null == array)
			return null;

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			s.append(array[i]);
			if (whiteSpaceSeperator)
				s.append(' ');
		}
		return s.toString();
	}

	/**
	 * Convert a white space delimited string to an array of given type, the
	 * component of the array should have a public constructor which takes a
	 * single argument of type String.
	 * 
	 * @param String
	 *            input string
	 * @param Class
	 *            type of class that the output array must contain
	 * @return Object[] output array
	 * @exception Exception
	 *                if any of the array operations within fails
	 */
	public static Object[] stringToArray(String s, Class arrayType)
			throws Exception {
		StringTokenizer st = new StringTokenizer(s);
		int size = st.countTokens();
		Class componentType = arrayType.getComponentType();
		Object array = Array.newInstance(componentType, size);
		Constructor constructor = componentType
				.getConstructor(new Class[] { String.class });
		for (int i = 0; i < size; i++)
			Array.set(array, i, constructor.newInstance(new Object[] { st
					.nextToken() }));
		return (Object[]) array;
	}

	public static int[] stringToIntArray(String s) throws Exception {
		StringTokenizer st = new StringTokenizer(s);
		int size = st.countTokens();
		int[] iArray = new int[size];
		for (int i = 0; i < size; i++)
			iArray[i] = Integer.parseInt(st.nextToken(), 16);
		return iArray;
	}

	public static void main(String[] args) throws Exception {
		Object[] array = (Object[]) stringToArray(args[0], Integer[].class);
		log.debug(arrayToString(array));
	}
}
