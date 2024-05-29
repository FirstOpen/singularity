/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.devicemgr.interrogator;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.system.Tag;

/**
 * @author TomRose
 * @version $Id: IPSADC_IPICO_TTO_IO.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public class IPSADC_IPICO_TTO_IO extends IPSADC_IPICO_IO {

	
	private Log log = LogFactory.getLog(IPSADC_IPICO_TTO_IO.class);
	
	
	/**
	 * 
	 */
	public IPSADC_IPICO_TTO_IO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param deviceProfile
	 */
	public IPSADC_IPICO_TTO_IO(DeviceProfile deviceProfile) {
		super(deviceProfile);
	
		// TODO Auto-generated constructor stub
	}

	/* Byte Description Info - Hexadecimal ASCII delimited records
	 * 0 		Header character
	 * 1 		Frame header, 1 Header character 
	 * 2-3 		Reader ID 0-255 in ASCII hex
	 * 4-15 	Tag ID MS digit first 
	 * 16-19 	I and Q channel counter Binary counters 0-255 in ASCII hex
	 * 20-33	Date/Time Date and time with 10ms resolution. 390ms/10 = 39 =(27 =
	 * 			0x32 + 0x37) and the month 12 is 0x31+ 0x32. 
	 * 34-39    Tampered
	 * 40-41 	LRC Checksum on bytes 2 to 35 
	 * 42-43 	End of packet (CR, LF) 0x0d, 0x0a
	 * 
	 * (i.e. ASCII converison of byte array =
	 * 
	 * 
	 * aa 00 4497fc000022 0002 0510101630132d 32 00 ff 63<CR><LF>
	 * 
	 * Instantaneous ID hits and IDs in transit are spooled in small packets, one for each ID. These can be
    * considered as stand alone data-grams similar to the UDP protocol. Table 1 depicts the content of these
    * data-grams for the hexadecimal ASCII format. This format converts the binary data to ASCII but with a
    * hexadecimal base (e.g. 0b00101111 is converted to �2f�). All ASCII characters are lower case. In addition,
    * the ID CRC bytes are stripped off. A simple 8-bit LRC is appended to the packet and is a modulo 28 addition
    * of all the data in the packet but not the Frame header bytes.
    * 
    * Example:  
    * ASCII spooled ID, �aa400000000123450a2a01123018455927a7<CR><LF>� (38 bytes) Reader ID 0x40 (64d), tag ID 0x000000012345, I counter 0x0a (10d), Q counter 0x2a (42d), date (20)01-12-
    * 30, 18:45:59.39, checksum 0xa7. (Note the millennium and century value is discarded in date stamp)
    * ASCII LRC checksum calculation:
    * �4�+�0� + �.. �2� + �7�
	 * Calculations are done in hexadecimal on ASCII data, (i.e ASCII character �a� is 0x61)
	 * 0x34 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x31 + 0x32 + 0x33 + 0x34 +
	 * 0x35 + 0x30 + 0x61 + 0x32 + 0x61 + 0x30 + 0x31 + 0x31 + 0x32 + 0x33 + 0x30 + 0x31 + 0x38 +
	 * 0x34 + 0x35 + 0x35 + 0x39 + 0x32 + 0x37 = 0x06a7,
	 * discard carry thus LRC = 0xa7 or ASCII 0x61, 0x37
	 * 
	 * 
	 * @throws InterrogatorException
	 * @throws Exception
	 * @throws Exception
	 */
	public ArrayList<Tag> decodeData(ArrayList<byte[]> messageList)
			throws InterrogatorException {

		ArrayList<Tag> tagList = new ArrayList<Tag>();
		
		for (int j = 0; j < messageList.size(); j++) {

			byte[] messageBuffer = messageList.get(j);

			if (messageBuffer[0] != 97)
				throw new InterrogatorException("invalid message from reader");
			int iTemp = 0;
			char cstring[] = new char[messageBuffer.length - 2];

			for (int i = 0; i < messageBuffer.length - 2; i++) {
				iTemp = 0;
				iTemp = (0x000000FF & ((int) messageBuffer[i]));
				cstring[i] = (char) iTemp;
			}

			String readBuffer = new String(cstring);
			log.debug("readbuffer : " + readBuffer);

			String lrc = readBuffer.substring(RECORD_LENGTH-4, RECORD_LENGTH-2);
			int iLRC = new BigInteger(lrc, 16).intValue();

			iTemp = 0;
			for (int i = 2; i <= RECORD_LENGTH-5; i++) {
				iTemp = messageBuffer[i] + iTemp;
			}
			iTemp = (0x000000FF & (iTemp));
			if (iLRC != iTemp)
				throw new InterrogatorException(
						"invalid message, checksum does not match");

			String header = readBuffer.substring(0, 2);
			
			String readerID = readBuffer.substring(2, 4);
			int iReaderID = new BigInteger(readerID, 16).intValue();

			String tagID = readBuffer.substring(4, 16);
			int iTagID = new BigInteger(tagID, 16).intValue();

			String iiChannelCounter = readBuffer.substring(16, 18);
			int iIChannelCounter = new BigInteger(iiChannelCounter, 16)
					.intValue();

			String qChannelCounter = readBuffer.substring(18, 20);
			int iQChannelCounter = new BigInteger(qChannelCounter, 16)
					.intValue();

			
			
			String dateTime = readBuffer.substring(20, 34);
			int iDateTime = new BigInteger(dateTime, 16).intValue();

			String hour = dateTime.substring(6, 8);
			String minutes = dateTime.substring(8, 10);
			String seconds = dateTime.substring(10, 12);
			String msec = dateTime.substring(12);
			String ftime = hour + ":" + minutes + ":" + seconds + "." + msec + "0";
			
			String page = readBuffer.substring(36, 38);
			int iPage = new BigInteger(page, 16).intValue();

			String tamper = readBuffer.substring(38, 40);
			int iTamper = new BigInteger(tamper, 16).intValue();

			
			String packetEnd = readBuffer.substring(RECORD_LENGTH-2);

			log.debug("header = " + header);
			log.debug("readerID = " + readerID + "= " + iReaderID);
			log.debug("tagID = " + tagID + "= " + iTagID);
			log.debug("IChannel = " + iIChannelCounter );
			log.debug("QChannel = " + iQChannelCounter );
			log.debug("dateTime = " + dateTime.substring(0, 6) + ftime + "=" + iDateTime);
			log.debug("tamper = " + tamper + "=" + iTamper);
			log.debug("lrc = " + lrc + "= " + iLRC);
			log.debug("packetEnd = " + packetEnd);

			/*
			 * ignore message unless its a tagID message.
			 * @see page 51 of iPico Serial Protocol Manual v1.00 September 2005
			 * 
			 */
			if (iPage == 0) {
				Tag currentTag = new Tag();
				currentTag.setValue(tagID);
				currentTag.setBinvalue(tamper.getBytes());
				tagList.add(currentTag);
				currentTag.setCount(iIChannelCounter + iQChannelCounter);
			}


			
			/*
			 * TODO: uncomment if physical readerID is perfered, need
			 * to make this part of the configuration to allow 
			 * physical IDs as well as assined IDs for the physical 
			 * compoents. 
			 *deviceProfile.setDeviceProfileID(readerID);
			 */
			
		}// end for all messages
		return tagList;
	
	}
	
  protected void setupCommands(){
	    super.setupCommands();
		RECORD_LENGTH = 44;
		
	}
}
