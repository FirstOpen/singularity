package com.refactored_networks.epc.epcnetwork;
import java.util.BitSet;
import java.util.Properties;

/**
 * Copyright 2005 Refactored Networks, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

/**
 * The abstract base class that all EPC tags implement. Users <i>can</i> construct
 * a specific implementing subclass but this is not recommended due to the fluid
 * nature of some EPC coding standards. Users should use the EPCFactory class instead 
 * since it takes a raw EPC and correctly determines which subclass to create. Internally
 * the EPC is represented in a parsed form. Every encoding and encoding format
 * is calculated at the time of request. 
 * 
 * @author Michael Mealling
 * 
 */
public abstract class EPC {
	protected String sUnparsedStringEPC;
	protected byte[] bUnparsedByteEPC;
	protected BitSet bsUnparsedBitSetEPC;
	protected Properties config;
	protected int flags;

	/**
	 * The input or output is intended to be a String whose contents
	 * are in hexadecimal form and are interpreted case insensitive.
	 */
	public static final int HEX = 1;
	/**
	 * The input or output is intended to be a String whose contents
	 * are in decimal form and therefore must contain nothing but the
	 * digits 0,1,2,3,4,5,6,7,8,9,0
	 */
	public static final int DECIMAL = 2;
	/**
	 * The input or output is intended to be a String whose contents
	 * are in binary form and therefore must contain nothing but the
	 * digits 0 and 1.
	 */
	public static final int BINARY = 3;
	
	/**
	 * The input or output should be interpreted as a 64 bit tag
	 */
	public static final int SIXTYFOURBIT = 1 << 4;
	/**
	 * The input or output should be interprted as a 96 bit tag
	 */
	public static final int NINETYSIXBIT = 1 << 5;
	/**
	 * The input or output should be interpreted as an EPC in canonical form
	 */
	public static final int CANONICAL = 1 << 6;
	/**
	 * The input or output should be interpreted as an EPC in the form 
	 * needed by ONS. This form should constitute a valid series of "." 
	 * separated DNS labels. The root is <b>not</b> specified.
	 */
	public static final int ONS = 1 << 7;
	
	


	/**
	 * This constructor takes a byte array as input. Due to Java's insistance that bytes are signed 
	 * it can be fairly difficult to construct the byte array correctly. Either the String or BitSet
	 * based construcor are recommended.
	 * 
	 * @param inEPC An array of bytes interpeted as unsigned that represents the 
	 * sequence of bits divided arbitrary into 8 bit sequences. inEPC[0] represents
	 * the beginning of the binary EPC such that it should contain the header. If this
	 * constructor is called beware of how the byte array is created as Java's sign breakage
	 * can get in the way.
	 * @param iConfig A set of properties containing configuration values such as how the 64bit
	 * index table is used
	 * @throws EPCParseException
	 * 
	 * 
	 */
	public EPC(byte[] inEPC,Properties iConfig) throws EPCParseException {
		// Save it
		bUnparsedByteEPC = inEPC;
		config = iConfig;
	}

	/**
	 * This constructor takes a String who's expected format is specified by the flags
	 * parameter.
	 * 
	 * @param inEPC
	 * @param inflags
	 * @param iConfig
	 * @throws EPCParseException
	 */
	public EPC(String inEPC, int inflags, Properties iConfig) throws EPCParseException {
		// Save it
		sUnparsedStringEPC = inEPC;
		config = iConfig;
		flags = inflags;
	}

	/**
	 * This constructor takes a BitSet.
	 * 
	 * @param inEPC
	 * @param iConfig
	 * @throws EPCParseException
	 */
	public EPC(BitSet inEPC, Properties iConfig) throws EPCParseException {
		// Save it
		bsUnparsedBitSetEPC = inEPC;
		config = iConfig;
	}
	/**
	 * Returns the sub-namespace name for a particular subclass
	 * 
	 * @return A String containing the subnamespace name for this class
	 */
	public abstract String getSubNamespaceName();
	/**
	 * Returns the canonical (URN) encoding for the particular namespace
	 * 
	 * @return Returns the canonical (i.e. URN) encoding for this EPC
	 */
	public abstract String getCanonicalEncoding();
	/**
	 * If there is one, this returns a partial domain-name that is used by ONS
	 * to find information for this EPC. It does <b>not</b> contain the root 
	 * since that is a configuration option within the ONS client engine.
	 * 
	 * @return returns a "." seperated sequence of DNS labels
	 */
	public abstract String getONSEncoding();
	
	/**
	 * Compares two EPCs in their canonical form
	 * 
	 * @param inepc
	 * @return true if this EPC's canonical encoding equals the canonical encoding of the provided EPC
	 */
	public boolean equals(EPC inepc) {
		String sinEPC = inepc.getCanonicalEncoding();
		String smyEPC = this.getCanonicalEncoding();
		return(smyEPC.equalsIgnoreCase(sinEPC));
	}
	
	/**
	 * Compares two EPCs in binary form. The flags specifies what format that 
	 * comparison should take since the intent is to allow someone to compare
	 * particular bit sequences.
	 * 
	 * @param inepc
	 * @param flags
	 * @return returns true if the binary encodings of the two EPCs match according to the flags
	 * @throws EPCParseException 
	 */
	public boolean binaryEquals(EPC inepc, int flags) throws EPCParseException {
		BitSet bsinepc = inepc.getBinaryEncodingBitSet(flags);
		BitSet bsmyepc = this.getBinaryEncodingBitSet(flags);
		return(bsinepc.equals(bsmyepc));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return(this.getCanonicalEncoding());
	}
	
	/**
	 * 
	 * @return Returns the particular set of properties the subclass is currently working with
	 * 
	 */
	public Properties getConfig() {
		return config;
	}

	/**
	 * Set the config for this subclass
	 * 
	 * @param iConfig
	 */
	public void setConfig(Properties iConfig) {
		config = iConfig;
	}
	
	/**
	 * Returns a binary encoding according to the values found in the flags
	 * 
	 * @param flags
	 * @return A BitSet
	 * @throws EPCParseException 
	 */
	public abstract BitSet getBinaryEncodingBitSet(int flags) throws EPCParseException;
	/**
	 * Returns a binary encoding according to the values found in the flags
	 * 
	 * @param flags
	 * @return A byte array
	 * @throws EPCParseException 
	 */
	public abstract byte[] getBinaryEncodingByteArray(int flags) throws EPCParseException;
	/**
	 * Returns a binary encoding according to the values found in the flags
	 * 
	 * @param flags
	 * @return A String that may contain hex, binary or decimal digits
	 * @throws EPCParseException
	 */
	public abstract String getBinaryEncodingString(int flags) throws EPCParseException;
	
	public abstract String getHeaderString(int flags) throws EPCParseException;

}
	
