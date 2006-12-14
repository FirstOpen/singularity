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
import java.util.BitSet;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * <b>A Global Internal Asset Identifier<b><br>
 * GIAIs consiste of a Manager Id and an Individual Asset Reference. Instances of this
 * class should be retrieved through the EPCFactory class. 
 * 
 * @author michael
 *
 */
public class GIAI extends EPC {
	protected String sManagerId;
	protected String sIndividualAssetReference;
	protected BitSet bits;
	protected String sFilter;

	private ParseUtils utils = new ParseUtils();
	
	public GIAI(BitSet inEPC, Properties iConfig) throws EPCParseException {
		super(inEPC, iConfig);
		if(inEPC.length() == 64) {
			parse64bit(ParseUtils.BitsToBytes(inEPC));
		} else if(inEPC.length() == 96) {
			parse96bit(ParseUtils.BitsToBytes(inEPC));
		}
	}
	
	
	/**
	 * @param inEPC
	 * @param flags
	 * @param iConfig
	 * @throws EPCParseException
	 */
	public GIAI(String inEPC, int flags, Properties iConfig) throws EPCParseException {
		super(inEPC, flags, iConfig);
		if((flags & EPC.CANONICAL) != 0) {
			parseCanonical(inEPC);
		} else if((flags & EPC.NINETYSIXBIT) != 0) {
			if((flags & EPC.BINARY) != 0) {
				parse96bit(utils.BinaryStringToByteArray(inEPC));
			} else if((flags & EPC.DECIMAL) !=0 ) {
				parse96bit(utils.DecimalStringToByteArray(inEPC));
			} else if((flags & EPC.HEX) != 0) {
				parse96bit(utils.HexStringToByteArray(inEPC));
			} else throw new EPCParseException("Unable to interpret flags in a meaningful way:" + flags);
		} else if((flags & EPC.SIXTYFOURBIT) != 0) {
			if((flags & EPC.BINARY) != 0) {
				parse64bit(utils.BinaryStringToByteArray(inEPC));
			} else if((flags & EPC.DECIMAL) !=0 ) {
				parse64bit(utils.DecimalStringToByteArray(inEPC));
			} else if((flags & EPC.HEX) != 0) {
				parse64bit(utils.HexStringToByteArray(inEPC));
			} else throw new EPCParseException("Unable to interpret flags in a meaningful way:" + flags);
		}
	}

	
	public GIAI(byte[] inEPC,Properties config) throws EPCParseException {
		super(inEPC,config);

		switch((inEPC[0] & 0xFF)) {
			case 11: parse64bit(inEPC);
				 break;
			case 52: parse96bit(inEPC);
				 break;
			default:
                        	throw new EPCParseException("Binary EPC's header does not correspond to an GIAI");
                }
	}



	private void parse64bit(byte[] inEPC) throws EPCParseException {
		bits = new BitSet(((inEPC.length)*8)+1);
		bits.set(0);
		int j=0;
        	for (int i=((inEPC.length)*8)-1;i>=0; i--) {
			if(((inEPC[(inEPC.length-1) - (j/8)])&(1<<(j%8))) > 0) {
				bits.set(i+1);
			} else {
				bits.set(i+1,false);
			}
			j++;
        	}

		long lHeader = ParseUtils.BitsToInt(bits.get(1,9),8);
		long lFilter = ParseUtils.BitsToInt(bits.get(9,12),3);
		sFilter = Long.toString(lFilter);
		long lIndex = ParseUtils.BitsToInt(bits.get(12,26),14);
				try {
			CompanyPrefixIndexDB indexDB = new CompanyPrefixIndexDB(config);
			sManagerId = indexDB.Lookup(Long.toString(lIndex));
		} catch(NoSuchIndexException noindex) {
			throw new EPCParseException("The index value was not found in " + config.getProperty("method"));
		} catch(Exception e) {
			throw new EPCParseException(e.getMessage());
		}
		sIndividualAssetReference = Long.toString(ParseUtils.BitsToInt(bits.get(26,65),39));

	}


	private void parse96bit(byte[] inEPC) throws EPCParseException {
		bits = new BitSet(((inEPC.length)*8)+1);
		bits.set(0);
		int j=0;
        	for (int i=((inEPC.length)*8)-1;i>=0; i--) {
			if(((inEPC[(inEPC.length-1) - (j/8)])&(1<<(j%8))) > 0) {
				bits.set(i+1);
			} else {
				bits.set(i+1,false);
			}
			j++;
        	}

		long lHeader = ParseUtils.BitsToInt(bits.get(1,9),8);
		long lFilter = ParseUtils.BitsToInt(bits.get(9,12),3);
		long lPartition = ParseUtils.BitsToInt(bits.get(12,15),3);

		switch((int) lPartition) {
			case 0:
				sManagerId = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(15,55),40));
				sIndividualAssetReference = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(55,97),42));
				break;
			case 1:
				sManagerId = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(15,52),37));
				sIndividualAssetReference = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(52,97),45));
				break;
			case 2:
				sManagerId = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(15,49),34));
				sIndividualAssetReference = 
				   Long.toString(ParseUtils.BitsToInt(bits.get(49,97),48));
				break;
			case 3:
				sManagerId = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(15,45),30));
				sIndividualAssetReference = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(45,97),52));
				break;
			case 4:
				sManagerId = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(15,42),27));
				sIndividualAssetReference = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(42,97),55));
				break;
			case 5:
				sManagerId = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(15,39),24));
				sIndividualAssetReference = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(39,97),58));
				break;
			case 6:
				sManagerId = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(15,35),20));
				sIndividualAssetReference = 
				    Long.toString(ParseUtils.BitsToInt(bits.get(35,97),62));
				break;
		       default: 
				throw new EPCParseException("partition value invalid (" + lPartition + ")");
		}
	}


	private void parseCanonical(String inEPC) throws EPCParseException {
		// The parser
		StringTokenizer st = new StringTokenizer(inEPC, ":.");

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

		// Finally! Is this a valid EPC type? If so, then save
		// that and end since any further parsing is handled by
		// the subclass.
		String sIdType = st.nextToken();
		if(!sIdType.equalsIgnoreCase("giai")) {
			throw new EPCParseException("Not an GIAI. Type is " + sIdType);
		}
		sManagerId = st.nextToken();
		if(sManagerId == null) {
			throw new EPCParseException("No manager id found");
		}
		sIndividualAssetReference= st.nextToken();
		if(sIndividualAssetReference == null) {
			throw new EPCParseException("No Individual Asset Reference found");
		}
	}

	/* (non-Javadoc)
	 * @see com.refactored_networks.epc.epcnetwork.EPC#getSubNamespaceName()
	 */
	public String getSubNamespaceName() {
		return("giai");
	}

	/* (non-Javadoc)
	 * @see com.refactored_networks.epc.epcnetwork.EPC#getCanonicalEncoding()
	 */
	public String getCanonicalEncoding() {
		return("urn:epc:id:giai:" + sManagerId + "." + sIndividualAssetReference);
	}
	/* (non-Javadoc)
	 * @see com.refactored_networks.epc.epcnetwork.EPC#getONSEncoding()
	 */
	public String getONSEncoding() {
		return(sIndividualAssetReference + "." + sManagerId + ".giai.id.");
	}


	/* (non-Javadoc)
	 * @see com.refactored_networks.epc.epcnetwork.EPC#getBinaryEncodingBitSet(int)
	 */
	public BitSet getBinaryEncodingBitSet(int flags) throws EPCParseException {
		if((flags & EPC.NINETYSIXBIT) != 0) {
			return(build96bit());
		} else if((flags & EPC.SIXTYFOURBIT) != 0 ) {
			return(build64bit());
		}
		return(null);
	}

	private BitSet build64bit() throws EPCParseException {
		BitSet mybits = new BitSet(64);
		String sIndex;
		mybits.set(0);
		mybits.set(1);
		
		String bsFilter = utils.ZeroPad(Integer.toString(Integer.parseInt(sFilter),2),3);
		for(int i=0;i<bsFilter.length();i++) 
			if(bsFilter.charAt(i) == '1') {
				mybits.set(i+3);
			} else {
				mybits.set(i+3,false);
			}
		try {
			CompanyPrefixIndexDB indexDB = new CompanyPrefixIndexDB(config);
			sIndex = indexDB.ReverseLookup(sManagerId);
		} catch(NoSuchIndexException noindex) {
			throw new EPCParseException("The index value was not found in " + config.getProperty("method"));
		} catch(Exception e) {
			throw new EPCParseException(e.getMessage());
		}
		
		String bsIndex = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndex),2),14);
		for(int i=0;i<bsIndex.length();i++) 
			if(bsFilter.charAt(i) == '1') 
				mybits.set(i+6);
			else 
				mybits.set(i+6,false);
		String bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),20);
		for(int i=0;i<bsIndividualAssetReference.length();i++) 
			if(bsIndividualAssetReference.charAt(i) == '1') 
				mybits.set(i+6);
			else 
				mybits.set(i+6,false);
		
		return(mybits);
	}


	private BitSet build96bit() throws EPCParseException {
		BitSet mybits = new BitSet(64);
		mybits.set(0);
		mybits.set(3);
		mybits.set(4);
		String bsFilter = utils.ZeroPad(Integer.toString(Integer.parseInt(sFilter),2),3);
		for(int i=0;i<bsFilter.length();i++) 
			if(bsFilter.charAt(i) == '1') {
				mybits.set(i+9);
			} else {
				mybits.set(i+9,false);
			}
		
		String bsPartition;
		String bsManagerId;
		String bsIndividualAssetReference;
		String bsSerialNum;
		
		switch(sManagerId.length()) {
		case 6:
			bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),20);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),24);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(35+i);
				else 
					mybits.set(35+i,false);
			}
			 
			break;
		case 7:
			bsPartition = utils.ZeroPad(Integer.toString(5,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),24);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),20);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(39+i);
				else 
					mybits.set(39+i,false);
			}
			 
			break;
		case 8:
			bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),27);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),17);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(42+i);
				else 
					mybits.set(42+i,false);
			}
			
			break;
		case 9:
			bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),30);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),14);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(45+i);
				else 
					mybits.set(45+i,false);
			}
			 
			break;
		case 10:
			bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),34);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),10);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(49+i);
				else 
					mybits.set(49+i,false);
			}
			 
			break;
		case 11:bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
		for(int i=0;i<bsPartition.length();i++) 
			if(bsPartition.charAt(i) == '1') {
				mybits.set(i+12);
			} else {
				mybits.set(i+12,false);
			}
		 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),37);
		for(int i=0;i<bsManagerId.length();i++) {
			if(bsManagerId.charAt(i) == '1') 
				mybits.set(15+i);
			else 
				mybits.set(15+i,false);
		}
		 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),7);
		for(int i=0;i<bsIndividualAssetReference.length();i++) {
			if(bsIndividualAssetReference.charAt(i) == '1') 
				mybits.set(52+i);
			else 
				mybits.set(52+i,false);
		}
		 
			break;
		case 12:
			bsPartition = utils.ZeroPad(Integer.toString(6,2),3);
			for(int i=0;i<bsPartition.length();i++) 
				if(bsPartition.charAt(i) == '1') {
					mybits.set(i+12);
				} else {
					mybits.set(i+12,false);
				}
			 bsManagerId = utils.ZeroPad(Integer.toString(Integer.parseInt(sManagerId),2),40);
			for(int i=0;i<bsManagerId.length();i++) {
				if(bsManagerId.charAt(i) == '1') 
					mybits.set(15+i);
				else 
					mybits.set(15+i,false);
			}
			 bsIndividualAssetReference = utils.ZeroPad(Integer.toString(Integer.parseInt(sIndividualAssetReference),2),4);
			for(int i=0;i<bsIndividualAssetReference.length();i++) {
				if(bsIndividualAssetReference.charAt(i) == '1') 
					mybits.set(55+i);
				else 
					mybits.set(25+i,false);
			}
			
			break;
			
		default: throw new EPCParseException("Error! the internally stored ManagerId is incorrect!");
		}
		
		
		return(mybits);
	}




	public byte[] getBinaryEncodingByteArray(int flags) throws EPCParseException {
		if((flags & EPC.NINETYSIXBIT) != 0) {
			return(ParseUtils.BitsToBytes(build96bit()));
		} else if((flags & EPC.SIXTYFOURBIT) != 0 ) {
			return(ParseUtils.BitsToBytes(build64bit()));
		}
		return(null);
	}

	public String getBinaryEncodingString(int flags) throws EPCParseException {
		StringBuffer buffer = new StringBuffer();
		if((flags & EPC.NINETYSIXBIT) != 0) {
			BitSet bits = build96bit();
			byte[] mybits;
			switch (flags & 3) {
			case EPC.BINARY:
				for (int i=1; i<97; i++) {
					if (bits.get(i)) { buffer.append("1"); }
					else { buffer.append("0"); }
				}
				return(buffer.toString());
			case EPC.DECIMAL:
				BitSet bsByte;
				byte bbyte;
				for(int i=0;i<12;i++) {
					bsByte = bits.get((i*8) + 1,(i*8) + 9);
					bbyte=0; int x=7;
					for(int j=0;j<8;j++) {
						if(bsByte.get(j)) {
							bbyte |= 1<<x;
						}
						x--;
					}
					buffer.append(Byte.toString(bbyte));
				}
				return(buffer.toString());
			case EPC.HEX:
				BitSet nibble;
				byte bnibble;
				for(int i=0;i<24; i++) {
					nibble = bits.get((i*4) +1, ((i*4) + 5));
					bnibble=0; int x=3;
					for(int j=0;j<4;j++) {
						if(nibble.get(j)) {
							bnibble |= 1<<x;
						}
						x--;
					}
					buffer.append(Integer.toString(bnibble,16));
				} 
				return(buffer.toString());
			default: throw new EPCParseException("Unable to interpret flags in a meaningful way:" + flags);
			}
		} else if((flags & EPC.SIXTYFOURBIT) != 0) {
			BitSet bits = build64bit();
			byte[] mybits;
			switch (flags & 3) {
			case EPC.BINARY:
				for (int i = 1; i < 65; i++) {
					if (bits.get(i)) {
						buffer.append("1");
					} else {
						buffer.append("0");
					}
				}
				return (buffer.toString());

			case EPC.DECIMAL:
				BitSet bsByte;
				byte bbyte;
				for(int i=0;i<8;i++) {
					bsByte = bits.get((i*8) + 1,(i*8) + 9);
					bbyte=0; int x=7;
					for(int j=0;j<8;j++) {
						if(bsByte.get(j)) {
							bbyte |= 1<<x;
						}
						x--;
					}
					buffer.append(Byte.toString(bbyte));
				}
				return(buffer.toString());
			case EPC.HEX:
				BitSet nibble;
				byte bnibble;
				for(int i=0;i<16; i++) {
					nibble = bits.get((i*4) +1, ((i*4) + 5));
					bnibble=0; int x=3;
					for(int j=0;j<4;j++) {
						if(nibble.get(j)) {
							bnibble |= 1<<x;
						}
						x--;
					}
					buffer.append(Integer.toString(bnibble,16));
				} /*
				mybits = ParseUtils.BitsToBytes(bits.get(1,97));
				bigint = new BigInteger(mybits);
				return(bigint.toString(16));
				*/
				return(buffer.toString());
			default:
				throw new EPCParseException(
						"Unable to interpret flags in a meaningful way:"
								+ flags);
			}
		} else throw new EPCParseException("Unable to interpret flags in a meaningful way:" + flags);
	}
	/**
	 * @return Returns the sFilter.
	 */
	public String getFilter(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sFilter)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sFilter)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sFilter);
		}
		return(null);
	}
	

	/**
	 * @param filter The sFilter to set.
	 */
	public void setFilter(String filter, int flags) {
		if((flags & EPC.BINARY) != 0) {
			sFilter  = Integer.toString(Integer.parseInt(filter,2));
		} else if((flags & EPC.HEX) != 0) {
			sFilter  = Integer.toString(Integer.parseInt(filter,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sFilter = filter;
		}
	}
	

	/**
	 * @return Returns the sManagerId.
	 */
	public String getManagerId(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sManagerId)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sManagerId)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sManagerId);
		}
		return(null);
	}
	

	/**
	 * @param managerId The sManagerId to set.
	 */
	public void setManagerId(String managerId, int flags) {
		if((flags & EPC.BINARY) != 0) {
			sManagerId  = Integer.toString(Integer.parseInt(managerId,2));
		} else if((flags & EPC.HEX) != 0) {
			sManagerId  = Integer.toString(Integer.parseInt(managerId,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sManagerId = managerId;
		}
	}
	
	/**
	 * @return Returns the Index for a 64bit tag, not a ManagerId.
	 * @throws EPCParseException 
	 */
	public String getIndex(int flags) throws EPCParseException {
		String result = null;
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sManagerId)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sManagerId)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sManagerId);
		}
		try {
			CompanyPrefixIndexDB indexDB = new CompanyPrefixIndexDB(config);
			result = indexDB.ReverseLookup(sManagerId);
		} catch(NoSuchIndexException noindex) {
			throw new EPCParseException("The index value was not found in " + config.getProperty("method"));
		} catch(Exception e) {
			throw new EPCParseException(e.getMessage());
		}
		return(result);
	}
	

	/**
	 * @param in_index The index to set for a 64bit tag
	 * @throws EPCParseException 
	 */
	public void setIndex(String in_index, int flags) throws EPCParseException {
		String index;
		if((flags & EPC.BINARY) != 0) {
			index  = Integer.toString(Integer.parseInt(in_index,2));
		} else if((flags & EPC.HEX) != 0) {
			index  = Integer.toString(Integer.parseInt(in_index,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			index = in_index;
		} else throw new EPCParseException("Unable to interpret flags in a meaningful way:" + flags);
		try {
			CompanyPrefixIndexDB indexDB = new CompanyPrefixIndexDB(config);
			sManagerId = indexDB.Lookup(index);
		} catch(NoSuchIndexException noindex) {
			throw new EPCParseException("The index value was not found in " + config.getProperty("method"));
		} catch(Exception e) {
			throw new EPCParseException(e.getMessage());
		}
	}
	

	
	/**
	 * @return Returns the sObjectClass.
	 */
	public String getIndividualAssetReference(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sIndividualAssetReference)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sIndividualAssetReference)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sIndividualAssetReference);
		}
		return(null);
	}
	

	/**
	 * @param assetReference The sObjectClass to set.
	 */
	public void setIndividualAssetReference(String assetReference,int flags) {
		if((flags & EPC.BINARY) != 0) {
			sIndividualAssetReference  = Integer.toString(Integer.parseInt(assetReference,2));
		} else if((flags & EPC.HEX) != 0) {
			sIndividualAssetReference  = Integer.toString(Integer.parseInt(assetReference,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sIndividualAssetReference = assetReference;
		}
	}


	public String getHeaderString(int flags) throws EPCParseException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
