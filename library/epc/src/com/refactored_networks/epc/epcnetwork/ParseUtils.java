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
import java.math.BigInteger;
import java.util.BitSet;

/**
 * @author Copyright 2005, Refactored Networks, LLC
 *
 */
public class ParseUtils {
	
	public byte[] BigIntegerToByteArray(BigInteger iEPC, int numbits) {
		/*
		 * You can't simply do BigInteger.toByteArray since it introduces
		 * a signage byte when headers are certain values (64bit SGTIN to 
		 * be exact) 
		 * 
		 * This version is for when something wants to specify the number of bits to 
		 * expect
		*/
		int numbytes = numbits/8;
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
		if(outBytes.length == numbytes) {
			return(outBytes);
		} else {
			byte[] lastBytes = new byte[numbytes];
			int difference = numbytes - outBytes.length;
			for(int i=0; i<numbytes;i++) {
				if(i<difference) lastBytes[i]=0;
				else lastBytes[i]=outBytes[i-difference];
			}
			return(lastBytes);
		}
	}
	
	public byte[] BigIntegerToByteArray(BigInteger iEPC) {
		/*
		 * You can't simply do BigInteger.toByteArray since it introduces
		 * a signage byte when headers are certain values (64bit SGTIN to 
		 * be exact) 
		*/
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
		return(outBytes);
	}
	
	public byte[] HexStringToByteArray(String inEPC) {
		int numbits = inEPC.length() * 4;
		return(BigIntegerToByteArray(new BigInteger(inEPC,16),numbits));
	}

	public byte[] DecimalStringToByteArray(String inEPC) {
		return(BigIntegerToByteArray(new BigInteger(inEPC)));
	}

	public byte[] BinaryStringToByteArray(String inEPC) {
		return(BigIntegerToByteArray(new BigInteger(inEPC,2)));
	}

	public BitSet BinaryStringToBitSet(String bitstring) {
		int length = bitstring.length();
		BitSet bits = new BitSet(length);
		for(int i=0;i<length;i++) {
			if(bitstring.charAt(i) == '1') {
				bits.set(i);
			}
		}
		return(bits);
	}
	
	public String ZeroPad(String in, int length) {
		int toAdd = length-in.length();
		String result = new String();
		for(int i=0;i<toAdd;i++) {
			result = result + "0";
		}
		result = result + in;
		return(result);
	}

	public static long BitsToInt(BitSet bits, int length) {
		int j=0;
		byte[] bytes = new byte[length/8+1];
		long result=0;
        	for (int i=(length-1); i>=0; i--) {
            		if (bits.get(i)) {
                		bytes[bytes.length-j/8-1] |= 1<<(j%8);
            		}
			j++;
        	}
		// copy, then shift the whole thing, not shift then add
		// doing an add brings the whole sign thing into play and
		// that's a Bad Thing (tm)
		for(int i=0; i<bytes.length-1; i++) {
			result  |= bytes[i] & 0xFF;
			result <<=8;
		}
		result  |= bytes[bytes.length-1] & 0xFF;
		return(result);
    }
	
	public static byte[] BitsToBytes(BitSet bits) {
		return(BitsToBytes(bits,bits.length()));
	}
	
	public static byte[] BitsToBytes(BitSet bits, int length) {
		int j=0;
		byte[] bytes = new byte[length/8+1];
		long result=0;
        	for (int i=(length-1); i>=0; i--) {
            		if (bits.get(i)) {
                		bytes[bytes.length-j/8-1] |= 1<<(j%8);
            		}
			j++;
        	}
		
		return(bytes);
    }
	
	public static void printBitSet(BitSet iBits, int length) {
		System.out.println("--------------------------------");
		System.out.println("BitSet length is " + iBits.length());
		System.out.println("length is " + length);
        	for (int i=0; i<length; i++) {
			if(((i-1)%8) == 0) { System.out.print(" "); }
            		if (iBits.get(i)) { System.out.print("1"); }
			else { System.out.print("0"); }
		}
		System.out.println("\n--------------------------------");
    }

	public static void printByteArray(byte[] bytes) {
		System.out.println("----------------------------------");
		System.out.println("byte[] length is " + bytes.length * 8);
        	for (int i=0; i<bytes.length; i++) {
			if(((i-1)%8) == 0) { System.out.print(" "); }
				for(int j=0;j<8;j++) {
					if ((bytes[i]&(1<<j)) == 1) { System.out.print("1"); }
					else { System.out.print("0"); }
				}
		}
		System.out.println("\n--------------------------------");
	}
	

	public static boolean isBinary(String in) {
		for(int i=0;i<in.length();i++) {
			if((in.charAt(i) == '0') || (in.charAt(i) == '1')) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isHex(String in) {
		String hexdigits = "ABCDEFabcdef0123456789 ";
		for(int i=0;i<in.length();i++) 
			if(hexdigits.indexOf(in.charAt(i)) == -1) {
				return false;
			}
		return true;
	}
}
