package com.refactored_networks.epc.epcnetwork;
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
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
/**
 * The general factory class for all EPC subclasses. Users should utilize this factory instead
 * of attempting to instantiate the subclasses directly since this factory ensures that the
 * subclasses are generated correctly. Especially as it relates to the sign related issues
 * of the various input forms.
 * 
 * @author michael
 *
 */
public class EPCFactory {

	protected Properties config;

	/**
	 * Create an instance of the factory with a default configuration. The Properties must
	 * contain any configuration options needed by any subclass. At this time this is used
	 * to communicate 64bit index lookup configurations to the CompanyPrefixIndexDB class. 
	 * The config must contain the following properties:
	 * <ul>
	 * <li>Property Name: "method"<br>
	 * Values: "ons", "xml"
	 * <li>Property Name: "filename"<br>
	 * Values: a path and filename for the XML version of the 64 bit index table. This property is only valid if
	 * the "method" property is set to "xml"
	 * <li>Property Name: "onsroot"<br>
	 * Values: a fully qualified domain-name that the root of all ONS related queries. This property is only
	 * valid if the "method" property is set to "ons"
	 * <li>Property Name: "nameserver"<br>
	 * Values: an IP address for the nameserver that the local DNS implementation should use instead of
	 * the default the system provides. If this propety is empty (i.e. ""), then the system default is used. 
	 * This property is only valid if the "method" property is set to "ons".
	 * </ul>
	 * 
	 * 
	 * @param inConfig
	 */
	public EPCFactory(Properties inConfig) {
		config = inConfig;
	}
	
	
	/**
	 * A static version of the getEPC(String, flags) method.
	 * 
	 * @param inEPC
	 * @param flags
	 * @param iConfig
	 * @return returns the correct subclass of the abstract EPC class
	 * @throws EPCParseException
	 */
	public static EPC getEPCStatic(String inEPC, int flags,Properties iConfig) throws EPCParseException {
		ParseUtils utils = new ParseUtils();
		if((flags & EPC.CANONICAL) != 0) {
			return(getEPCfromURNString(inEPC,iConfig));
		} else if((flags & EPC.HEX) != 0) {
			return(getEPCStatic(utils.HexStringToByteArray(inEPC),iConfig));
		} else if((flags & EPC.BINARY) != 0) {
			return(getEPCStatic(utils.BinaryStringToByteArray(inEPC),iConfig));
		} else {
			throw new EPCParseException("Error: input string of unknown format");
		}
	}
	
	/**
	 * A static version of the getEPC(String, Properties) method
	 * 
	 * @param inEPC
	 * @param iConfig
	 * @return returns the correct subclass of the abstract EPC class
	 * @throws EPCParseException
	 */
	public static EPC getEPCStatic(String inEPC,Properties iConfig) throws EPCParseException {
		if(inEPC.substring(0,4).equalsIgnoreCase("urn:")) {
			return(getEPCStatic(inEPC,EPC.CANONICAL,iConfig));
		} else if(ParseUtils.isHex(inEPC)) {
			return(getEPCStatic(inEPC,EPC.HEX,iConfig));
		} else if(ParseUtils.isBinary(inEPC)) {
			return(getEPCStatic(inEPC,EPC.BINARY,iConfig));
		} else {
			throw new EPCParseException("Error: input string of unknown format");
		}
	}
	/**
	 * This method will use heuristics to determine what format the input string is. 
	 * 
	 * @param inEPC
	 * @return returns the correct subclass of the abstract EPC class
	 * @throws EPCParseException
	 */
	public EPC getEPC(String inEPC) throws EPCParseException {
		return(getEPCStatic(inEPC,config));
	}
	
	/**
	 * This method takes a byte[] array as input that contains the EPC in binary form
	 * 
	 * @param inEPC
	 * @return returns the correct subclass of the abstract EPC class
	 * @throws EPCParseException
	 */
	public EPC getEPC(byte[] inEPC) throws EPCParseException {
		return(getEPCStatic(inEPC,config));
	}

	/**
	 * A static version of the getEPC(byte[]) method
	 * 
	 * @param inEPC
	 * @param iConfig
	 * @return returns the correct subclass of the abstract EPC class
	 * @throws EPCParseException
	 */
	public static EPC getEPCStatic(byte[] inEPC,Properties iConfig) throws EPCParseException {
		byte bHeader = inEPC[0];
		// 10 and 11
		int result=(bHeader >> 6) & 2;
		switch(result) {
			case 1: throw new EPCParseException("Two bit header value of 01 is reserved.");
			case 2: return((EPC) new SGTIN(inEPC,iConfig));
			case 3: throw new EPCParseException("Two bit header value of 11 is reserved.");
		} 
		// if we have gotten here then we've exhausted the two bit
		// header options
		if(bHeader < 8) {
			throw new EPCParseException("8 bit header values 0-7 are reserved and currently unassigned.");
		} else if((bHeader > 11) && (bHeader < 16)) {
			throw new EPCParseException("8 bit header values 12-15 are reserved 64bit schemes that are currently unassigned.");
		} else if((bHeader > 15) && (bHeader < 48)) {
			throw new EPCParseException("8 bit header values 16-47 are reserved schemes that are currently unassigned.");
		} else if((bHeader > 53) && (bHeader <64)) {
			throw new EPCParseException("8 bit header values 54-63 are reserved 96bit schemes that are currently unassigned.");
		}
		// the subclasses both handle 64 vs 96 bit-ness so the 
		// switch below just determines what the namespace is
		// and returns the correct subclass
		switch(bHeader) {
			 case 8:
			case 49:return((EPC) new SSCC(inEPC,iConfig));
			 case 9:
			case 50:return((EPC) new SGLN(inEPC,iConfig));
			case 10:
			case 51:return((EPC) new GRAI(inEPC,iConfig));
			case 11:
			case 52:return((EPC) new GIAI(inEPC,iConfig));
			case 48:return((EPC) new SGTIN(inEPC,iConfig));
			case 53:return((EPC) new GID(inEPC,iConfig));
   		   default:
				throw new EPCParseException("Something truly unholy just happened!");
		}
	}

	private static EPC getEPCfromURNString(String inEPC,Properties iConfig) throws EPCParseException {
		HashMap mIdTypes = new HashMap();
		mIdTypes.put("gid",new Integer(1));
		mIdTypes.put("sgtin",new Integer(2));
		mIdTypes.put("sscc",new Integer(3));
		mIdTypes.put("sgln",new Integer(4));
		mIdTypes.put("grai",new Integer(5));
		mIdTypes.put("giai",new Integer(6));
	
		// The parser
		StringTokenizer st = new StringTokenizer(inEPC, ":");
	
		// Is there anything to parse?
		if(!st.hasMoreTokens()) {
			throw new EPCParseException("Parse error. Not enough parse tokens.");
		}
		// If this isn't a URN, then throw an exception
		String sURN = st.nextToken();
		if(!sURN.equalsIgnoreCase("urn")) {
			throw new EPCParseException("Not a URN. Scheme is " + sURN);
		}
	
		// Are there more tokens?
		if(!st.hasMoreTokens()) {
			throw new EPCParseException("Parse error. Not enough parse tokens.");
		}
	
		// Is this an EPC? If not, then throw an exception
		String sNID = st.nextToken();
		if(!sNID.equalsIgnoreCase("epc")) {
			throw new EPCParseException("Not an EPC. NID is " + sNID);
		}
	
		// Anything left?
		if(!st.hasMoreTokens()) {
			throw new EPCParseException("Parse error. Not enough parse tokens.");
		}
	
		// Is this an 'id' type of an EPC? If not, throw an exception
		// We'll add the other types later....
		String sEPCType = st.nextToken();
		if(!sEPCType.equalsIgnoreCase("id")) {
			throw new EPCParseException("Not a pure identify form of an EPC. Type is " + sEPCType);
		}
	
		// Anything left?
		if(!st.hasMoreTokens()) {
			throw new EPCParseException("Parse error. Not enough parse tokens.");
		}
	
		// Finally! Is this a valid EPC id type? If so, then create
		// the appropriate object and return it.
		String sIdType = st.nextToken();
		int iSwitch = ((Integer) mIdTypes.get(sIdType.toLowerCase())).intValue();
		switch(iSwitch) {
			case 1:return((EPC) new GID(inEPC,EPC.CANONICAL,iConfig));
			case 2:return((EPC) new SGTIN(inEPC,EPC.CANONICAL,iConfig));
			case 3:return((EPC) new SSCC(inEPC,EPC.CANONICAL,iConfig));
			case 4:return((EPC) new SGLN(inEPC,EPC.CANONICAL,iConfig));
			case 5:return((EPC) new GRAI(inEPC,EPC.CANONICAL,iConfig));
			case 6:return((EPC) new GIAI(inEPC,EPC.CANONICAL,iConfig));
			default: 
				throw new EPCParseException("Not a pure identify form of an EPC. Type is " + sIdType);
		}
	}

}






