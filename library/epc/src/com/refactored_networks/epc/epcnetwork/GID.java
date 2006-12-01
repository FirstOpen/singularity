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

public class GID extends EPC {
	protected String sGeneralManagerNumber;
	protected String sObjectClass;
	protected String sSerialNum;
	protected BitSet bits;
	protected String sFilter;

	private ParseUtils utils = new ParseUtils();
	
	public GID(BitSet inEPC, Properties iConfig) throws EPCParseException {
		super(inEPC, iConfig);
		if(inEPC.length() == 96) {
			parse96bit(ParseUtils.BitsToBytes(inEPC));
		} else throw new EPCParseException("GID BitSet with incorrect length");
	}
	
	
	/**
	 * @param inEPC
	 * @param flags
	 * @param iConfig
	 * @throws EPCParseException
	 */
	public GID(String inEPC, int flags, Properties iConfig) throws EPCParseException {
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
		} 
	}

	

	public GID(byte[] inEPC,Properties config) throws EPCParseException {
		super(inEPC,config);
		// there is no 64bit representation of a GID
		if((inEPC[0] & 0xFF) == 53) {
                        parse96bit(inEPC);
                } else {
                        throw new EPCParseException("Binary EPC's header does not correspond to a GID");
                }

	}

	private void parse96bit(byte[] inEPC) throws EPCParseException {
                bits = new BitSet(((inEPC.length)*8)+1);
                bits.set(0);
                int j=0;
                for (int i=((inEPC.length)*8)-1;i>=0; i--) {
                        if(((inEPC[(inEPC.length-1) - (j/8)])&(1<<(j%8))) > 0) {                                bits.set(i+1);
                        } else {
                                bits.set(i+1,false);
                        }
                        j++;
                }

                long lHeader = ParseUtils.BitsToInt(bits.get(1,9),8);
                if(lHeader != 53) {
                        throw new EPCParseException("Attempting to parse an EPC with the wrong header value (" + lHeader + ")");
                }
                sGeneralManagerNumber = Long.toString(ParseUtils.BitsToInt(bits.get(9,37),28));
                sObjectClass = Long.toString(ParseUtils.BitsToInt(bits.get(37,61),24));
                sSerialNum = Long.toString(ParseUtils.BitsToInt(bits.get(61,97),36));
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
		if(!sIdType.equalsIgnoreCase("gid")) {
			throw new EPCParseException("Not an GID. Type is " + sIdType);
		}
		sGeneralManagerNumber = st.nextToken();
		if(sGeneralManagerNumber == null) {
			throw new EPCParseException("No General Manager Number found");
		}
		sObjectClass = st.nextToken();
		if(sObjectClass == null) {
			throw new EPCParseException("No Object Class Id found");
		}
		sSerialNum= st.nextToken();
		if(sSerialNum == null) {
			throw new EPCParseException("No serial number found");
		}
	}

	public String getSubNamespaceName() {
		return("gid");
	}

	public String getCanonicalEncoding() {
		return("urn:epc:id:gid:" + sGeneralManagerNumber + "." + sObjectClass + "." + sSerialNum);
	}
	public String getONSEncoding() {
		return(sObjectClass + "." + sGeneralManagerNumber + ".gid.id.");
	}

	public BitSet getBinaryEncodingBitSet(int flags) throws EPCParseException {
		if((flags & EPC.NINETYSIXBIT) != 0) {
			return(build96bit());
		} else if((flags & EPC.SIXTYFOURBIT) != 0 ) {
			throw new EPCParseException("GIDs have no 64bit representation!");
		}
		return(null);
	}

	

	private BitSet build96bit() throws EPCParseException {
		BitSet mybits = new BitSet(64);
		mybits.set(0);
		mybits.set(3);
		mybits.set(4);
		mybits.set(6);
		mybits.set(8);
				
		String bsGeneralManagerNumber;
		String bsObjectClass;
		String bsSerialNum;
		

		bsGeneralManagerNumber = utils.ZeroPad(Long.toString(Long.parseLong(sGeneralManagerNumber),2),28);
		for(int i=0;i<bsGeneralManagerNumber.length();i++) {
				if(bsGeneralManagerNumber.charAt(i) == '1') 
					mybits.set(9+i);
				else 
					mybits.set(9+i,false);
		}
		bsObjectClass = utils.ZeroPad(Long.toString(Long.parseLong(sObjectClass),2),24);
		for(int i=0;i<bsObjectClass.length();i++) {
				if(bsObjectClass.charAt(i) == '1') 
					mybits.set(37+i);
				else 
					mybits.set(37+i,false);
		}
		bsSerialNum = utils.ZeroPad(Long.toString(Long.parseLong(sSerialNum),2),36);
		for(int i=0;i<bsSerialNum.length();i++) {
				if(bsSerialNum.charAt(i) == '1') 
					mybits.set(61+i);
				else
					mybits.set(61+i,false);
		}
		return(mybits);
	}




	public byte[] getBinaryEncodingByteArray(int flags) throws EPCParseException {
		if((flags & EPC.NINETYSIXBIT) != 0) {
			return(ParseUtils.BitsToBytes(build96bit()));
		} else if((flags & EPC.SIXTYFOURBIT) != 0 ) {
			throw new EPCParseException("GID names have no 64bit representation!");
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
		} else throw new EPCParseException(
						"GIDs are only available in 96bit form:"
								+ flags);
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
	 * @return Returns the sGeneralManagerNumber.
	 */
	public String getgeneralManagerId(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sGeneralManagerNumber)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sGeneralManagerNumber)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sGeneralManagerNumber);
		}
		return(null);
	}
	

	/**
	 * @param generalManagerId The sGeneralManagerNumber to set.
	 */
	public void setgeneralManagerId(String generalManagerId, int flags) {
		if((flags & EPC.BINARY) != 0) {
			sGeneralManagerNumber  = Integer.toString(Integer.parseInt(generalManagerId,2));
		} else if((flags & EPC.HEX) != 0) {
			sGeneralManagerNumber  = Integer.toString(Integer.parseInt(generalManagerId,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sGeneralManagerNumber = generalManagerId;
		}
	}
	

	
	/**
	 * @return Returns the sObjectClass.
	 */
	public String getObjectClass(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sObjectClass)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sObjectClass)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sObjectClass);
		}
		return(null);
	}
	

	/**
	 * @param objectId The sObjectClass to set.
	 */
	public void setObjectClass(String objectId,int flags) {
		if((flags & EPC.BINARY) != 0) {
			sObjectClass  = Integer.toString(Integer.parseInt(objectId,2));
		} else if((flags & EPC.HEX) != 0) {
			sObjectClass  = Integer.toString(Integer.parseInt(objectId,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sObjectClass = objectId;
		}
	}
	

	/**
	 * @return Returns the sSerialNum.
	 */
	public String getSSerialNum(int flags) {
		if((flags & EPC.BINARY) != 0) {
			return (Integer.toBinaryString(Integer.parseInt(sSerialNum)));
		} else if((flags & EPC.HEX) != 0) {
			return (Integer.toHexString(Integer.parseInt(sSerialNum)));			
		} else if((flags & EPC.DECIMAL) != 0) {
			return(sSerialNum);
		}
		return(null);
	}
	

	/**
	 * @param serialNum The sSerialNum to set.
	 */
	public void setSSerialNum(String serialNum, int flags) {
		if((flags & EPC.BINARY) != 0) {
			sSerialNum  = Integer.toString(Integer.parseInt(serialNum,2));
		} else if((flags & EPC.HEX) != 0) {
			sSerialNum  = Integer.toString(Integer.parseInt(serialNum,16));
		} else if((flags & EPC.DECIMAL) != 0) {
			sSerialNum = serialNum;
		}
	}


	public String getHeaderString(int flags) throws EPCParseException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
	
