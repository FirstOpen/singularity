/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

package org.firstopen.singularity.devicemgr.emulators;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/*
 * ITRF91501_Intermec_Emulator
 */
public class IF5_Intermec_Emulator implements IReaderEmulator {
	private InputStream readFromClientStream;

	private OutputStream writeToClientStream;
	
	Logger log = Logger.getLogger(this.getClass());
	
	public static void main(String args[]) {
		String port = args[0];
		Socket s = null;
		try {
			int portInt = Integer.valueOf(port).intValue();
			ServerSocket ss = new ServerSocket(portInt);
			EmulatorServer eServer = null;
			while (true) {
				s = ss.accept();
				if (eServer != null) {
					eServer.close();
				}
				eServer = new EmulatorServer(s, s.getInputStream(), s
						.getOutputStream());
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}

class EmulatorServer {
	
	Logger log = Logger.getLogger(this.getClass());
	
	static int counter = 0;

	InputStream in = null;

	OutputStream out = null;

	Socket s = null;

	public EmulatorServer(Socket s, InputStream in, OutputStream out) {
		counter = counter + 1;
		System.out.println("EmulatorServer constructor counter = " + counter);
		this.in = in;
		this.out = out;
		String epc1 = "7777777666666\n";
		String epc2 = "7777771666661\nOK\n";
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(epc1);
		sBuffer.append(epc2);
		sBuffer.append(StreamTokenizer.TT_EOF);
		byte[] charArray = sBuffer.toString().getBytes();

		try {
			byte[] buffer = new byte[256];
			while (true) {
				int bytesRead = in.read(buffer);
				log.debug("received String = " + new String(buffer));
				long endTime = System.currentTimeMillis() + 250;
				while (System.currentTimeMillis() < endTime) {
				}
				out.write(charArray, 0, charArray.length);
				out.flush();
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			close();
		}
	}

	public void close() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (s != null)
				s.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}
