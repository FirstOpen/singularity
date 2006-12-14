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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * This is a util class for helper functions encountered in dealing with any
 * kind of writers.
 */

public class IOUtil {
	static Logger log = null;

	static {
		log = Logger.getLogger("IOUtil");
	}

	
	public static PrintWriter createPrintWriter(String filePath, String fileName)
			throws java.io.IOException {
		// log.debug("IOUtil.createPrintWriter starting");

		FileOutputStream fos = null;
		try {
			File file = new File(filePath, fileName);
			fos = new FileOutputStream(file);
			return new PrintWriter(fos);
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}

	}

	public static FileOutputStream createOutputStream(String filePath,
			String fileName) throws java.io.IOException {
		log.debug("IOUtil.createOutputStream starting filepath = '" + filePath
				+ "' id = '" + fileName + "'");
		FileOutputStream fos = null;
		try {
			File file = new File(filePath, fileName);
			fos = new FileOutputStream(file);
		} catch (Exception e) {
			log.debug("IOUtil.createPrintWriter caught exception: " + e);
			e.printStackTrace();
		}

		return fos;
	}// createPrintWriter


	@SuppressWarnings("unused")
	private static void createDirectory(String dir) {
		log.debug("IOUtil.createDirectory Starting");
		File f = new File(dir);
		f.mkdirs();
	}

    
}// class IOUtil
