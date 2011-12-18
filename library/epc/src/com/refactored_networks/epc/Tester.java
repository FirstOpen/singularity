package com.refactored_networks.epc;
import java.math.BigInteger;
import java.util.Properties;

import com.refactored_networks.epc.epcnetwork.EPC;
import com.refactored_networks.epc.epcnetwork.EPCFactory;

/**
 * @author Michael Mealling
 *
 *Copyright 2005 Refactored Networks, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
public abstract class Tester {
	public static void main(String[] args) {
		String sEPC="";
	   try {
		   if(args.length < 1) {
			 sEPC="urn:epc:id:sgtin:0037000.030241.1041970";
		   }
		   if(args.length > 1) {
			   for(int i=0;i<args.length;i++) {
				   sEPC += args[i];
			   }
		   } else { sEPC = args[0]; }
		Properties config = new Properties();
		config.setProperty("method","xml");
		config.setProperty("filename","test-index-table.xml");
		BigInteger iEPC = new BigInteger(sEPC,16);

		System.out.println("iEPC as hex is " + iEPC.toString(16));
		String binaryString = iEPC.toString(2);
		System.out.println("iEPC in binary is " + binaryString);
		System.out.println("length of binary string is " + binaryString.length());
		
		EPCFactory factory = new EPCFactory(config);
		EPC epc2 = factory.getEPC(sEPC);
		
		

		// This is here to get rid of the signage byte that BigInteger
		// introduces if the first bit of the header is 1 (i.e. a 
		// 64bit SGTIN
		byte inBytes[] = iEPC.toByteArray();
		byte outBytes[];
		if(inBytes[0]==0) {
			outBytes = new byte[inBytes.length - 1];
			for(int i=0;i<(inBytes.length -1); i++) {
				outBytes[i] = inBytes[i+1];
			}
		} else {
			outBytes = inBytes;
		}
		EPC epc = factory.getEPC(outBytes);
		
		System.out.println("epc Canonical encoding is " + epc.getCanonicalEncoding());
		System.out.println("epc 2Canonical encoding is " + epc2.getCanonicalEncoding());
		System.out.println("epc ONS encoding is " + epc.getONSEncoding());
		System.out.println("epc2 ONS encoding is " + epc2.getONSEncoding());
		//System.out.println("64bit binary encoding as string is: " + epc.getBinaryEncodingString(EPC.SIXTYFOURBIT | EPC.BINARY));
		System.out.println("96bit binary encoding as string is: " + epc.getBinaryEncodingString(EPC.NINETYSIXBIT | EPC.BINARY));
		//System.out.println("64bit hex encoding as string is: " + epc.getBinaryEncodingString(EPC.SIXTYFOURBIT | EPC.HEX));
		System.out.println("96bit hex encoding as string is: " + epc.getBinaryEncodingString(EPC.NINETYSIXBIT | EPC.HEX));
		
		//System.out.println("epc2 64bit binary encoding as string is: " + epc2.getBinaryEncodingString(EPC.SIXTYFOURBIT | EPC.BINARY));
		System.out.println("epc2 96bit binary encoding as string is: " + epc2.getBinaryEncodingString(EPC.NINETYSIXBIT | EPC.BINARY));
		//System.out.println("epc2 64bit hex encoding as string is: " + epc2.getBinaryEncodingString(EPC.SIXTYFOURBIT | EPC.HEX));
		System.out.println("epc2 96bit hex encoding as string is: " + epc2.getBinaryEncodingString(EPC.NINETYSIXBIT | EPC.HEX));

	   } catch (Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	   }
	}
}
	
