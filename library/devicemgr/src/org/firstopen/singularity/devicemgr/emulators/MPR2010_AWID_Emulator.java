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

package org.firstopen.singularity.devicemgr.emulators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;

public class MPR2010_AWID_Emulator implements Runnable, Shutdown {
    ServerSocket ss = null;

    static Log log = LogFactory.getLog(MPR2010_AWID_Emulator.class);

    EmulatorThread eThread = null;

    byte[] inboundBuffer = new byte[256];

    int threadCounter = 0;

    boolean stop = false;

    boolean isMultiProtocol = false;

    int[] iso18000_BTag = new int[] { 0x0D, 0x11, 0x01, 0x03, 0x08, 0x05, 0x07,
            0xa8, 0x02, 0x00, 0x10 };

    int[] epcClass0Tag = new int[] { 0x11, 0x17, 0x01, 0x03, 0x08, 0x05, 0x07,
            0xa8, 0x02, 0x00, 0x10, 0x00, 0x3e, 0x3a, 0x5e };

    int[] epcClass1Tag = new int[] { 0x11, 0x16, 0x01, 0x03, 0x08, 0x05, 0x07,
            0xa8, 0x02, 0x00, 0x10, 0x00, 0x3e, 0x3b, 0x4d };

    public final int[] stopCommand = new int[] { 0x00, 0x00 };

    public final int[] multiProtocolCommand = new int[] { 0x07, 0x14, 0x01,
            0x00, 0x00, 111, 65 };

    String port = "4000";

    Socket s = null;

    InputStream in = null;

    OutputStream out = null;

    /**
     * @param port
     */
    public MPR2010_AWID_Emulator(String port) {
        super();
        this.port = port;
    }

    public static void main(String args[]) {

        log.info("Shutdown manager registered...");
        ShutdownManager sdm = new ShutdownManager();
        Runtime.getRuntime().addShutdownHook(sdm);
        
        
        for (int i = 0; i < args.length; i++) {
            MPR2010_AWID_Emulator emulator = new MPR2010_AWID_Emulator(args[i]);
            ShutdownManager.addManagedObject(emulator);
            Thread thread = new Thread(emulator);
            thread.start();
        }// end for

    }// end main

    private void writeInitialGreeting(Socket s) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("iiAWID MPR-2010 V2.6e UHF MODULE");
        byte[] byteArray = sBuffer.toString().getBytes();
        s.getOutputStream().write(byteArray, 0, byteArray.length);
        s.getOutputStream().flush();
    }

     String intArrayToString(int[] array) {
        if (null == array)
            return null;

        StringBuffer s = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            s.append(array[i]);
            s.append(' ');
        }
        s.append('\n');
        return s.toString();
    }

    private class EmulatorThread implements java.lang.Runnable {

        Socket clientSocket = null;

        public EmulatorThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {

            InputStream tIn = null;

            OutputStream tOut = null;

            try {
                tIn = clientSocket.getInputStream();

                tOut = clientSocket.getOutputStream();

            } catch (IOException e) {
                log.error("cannot create Emulator Thread", e);
            }
            try {
                System.out.println("	EmulatorThread: counter = "
                        + threadCounter);
                threadCounter++;
                while (!stop) {
                    if (isMultiProtocol) {
                        tOut.write(compute(iso18000_BTag));
                        tOut.write(compute(epcClass0Tag));
                        tOut.write(compute(epcClass1Tag));
                    }
                    Thread.sleep(5); // 200 tag reads a second
                }

            } catch (java.net.SocketException x) {
                log.error("EmulatorThread: Socket Exception thrown", x);

            } catch (IOException e) {
                log.error(e);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            log.debug("EmulatorThread: thread safely closing");
        }// end run

    }

    public void close() {
        try {
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public byte[] compute(int[] buffer) {
        int count = buffer.length;
        int register = 0xffff;
        byte[] bArray = new byte[buffer.length + 2];
        for (int t = 0; t < buffer.length; t++) {
            bArray[t] = (byte) buffer[t];
        }

        while (count > 0) {
            int element = buffer[buffer.length - count];
            int t = ((int) ((register >>> 8) ^ element) & 0xff);
            register <<= 8;
            register ^= table[t];
            count--;
        }

        register ^= 0xFFFF;
        int upperByte = register >> 8;
        int lowerByte = register & 0x000000FF;
        bArray[buffer.length] = (byte) upperByte;
        bArray[buffer.length + 1] = (byte) lowerByte;
        return bArray;
    }

    public static final int[] table = new int[] { (int) 0x0000, (int) 0x1021,
            (int) 0x2042, (int) 0x3063, (int) 0x4084, (int) 0x50a5,
            (int) 0x60c6, (int) 0x70e7, (int) 0x8108, (int) 0x9129,
            (int) 0xa14a, (int) 0xb16b, (int) 0xc18c, (int) 0xd1ad,
            (int) 0xe1ce, (int) 0xf1ef, (int) 0x1231, (int) 0x0210,
            (int) 0x3273, (int) 0x2252, (int) 0x52b5, (int) 0x4294,
            (int) 0x72f7, (int) 0x62d6, (int) 0x9339, (int) 0x8318,
            (int) 0xb37b, (int) 0xa35a, (int) 0xd3bd, (int) 0xc39c,
            (int) 0xf3ff, (int) 0xe3de, (int) 0x2462, (int) 0x3443,
            (int) 0x0420, (int) 0x1401, (int) 0x64e6, (int) 0x74c7,
            (int) 0x44a4, (int) 0x5485, (int) 0xa56a, (int) 0xb54b,
            (int) 0x8528, (int) 0x9509, (int) 0xe5ee, (int) 0xf5cf,
            (int) 0xc5ac, (int) 0xd58d, (int) 0x3653, (int) 0x2672,
            (int) 0x1611, (int) 0x0630, (int) 0x76d7, (int) 0x66f6,
            (int) 0x5695, (int) 0x46b4, (int) 0xb75b, (int) 0xa77a,
            (int) 0x9719, (int) 0x8738, (int) 0xf7df, (int) 0xe7fe,
            (int) 0xd79d, (int) 0xc7bc, (int) 0x48c4, (int) 0x58e5,
            (int) 0x6886, (int) 0x78a7, (int) 0x0840, (int) 0x1861,
            (int) 0x2802, (int) 0x3823, (int) 0xc9cc, (int) 0xd9ed,
            (int) 0xe98e, (int) 0xf9af, (int) 0x8948, (int) 0x9969,
            (int) 0xa90a, (int) 0xb92b, (int) 0x5af5, (int) 0x4ad4,
            (int) 0x7ab7, (int) 0x6a96, (int) 0x1a71, (int) 0x0a50,
            (int) 0x3a33, (int) 0x2a12, (int) 0xdbfd, (int) 0xcbdc,
            (int) 0xfbbf, (int) 0xeb9e, (int) 0x9b79, (int) 0x8b58,
            (int) 0xbb3b, (int) 0xab1a, (int) 0x6ca6, (int) 0x7c87,
            (int) 0x4ce4, (int) 0x5cc5, (int) 0x2c22, (int) 0x3c03,
            (int) 0x0c60, (int) 0x1c41, (int) 0xedae, (int) 0xfd8f,
            (int) 0xcdec, (int) 0xddcd, (int) 0xad2a, (int) 0xbd0b,
            (int) 0x8d68, (int) 0x9d49, (int) 0x7e97, (int) 0x6eb6,
            (int) 0x5ed5, (int) 0x4ef4, (int) 0x3e13, (int) 0x2e32,
            (int) 0x1e51, (int) 0x0e70, (int) 0xff9f, (int) 0xefbe,
            (int) 0xdfdd, (int) 0xcffc, (int) 0xbf1b, (int) 0xaf3a,
            (int) 0x9f59, (int) 0x8f78, (int) 0x9188, (int) 0x81a9,
            (int) 0xb1ca, (int) 0xa1eb, (int) 0xd10c, (int) 0xc12d,
            (int) 0xf14e, (int) 0xe16f, (int) 0x1080, (int) 0x00a1,
            (int) 0x30c2, (int) 0x20e3, (int) 0x5004, (int) 0x4025,
            (int) 0x7046, (int) 0x6067, (int) 0x83b9, (int) 0x9398,
            (int) 0xa3fb, (int) 0xb3da, (int) 0xc33d, (int) 0xd31c,
            (int) 0xe37f, (int) 0xf35e, (int) 0x02b1, (int) 0x1290,
            (int) 0x22f3, (int) 0x32d2, (int) 0x4235, (int) 0x5214,
            (int) 0x6277, (int) 0x7256, (int) 0xb5ea, (int) 0xa5cb,
            (int) 0x95a8, (int) 0x8589, (int) 0xf56e, (int) 0xe54f,
            (int) 0xd52c, (int) 0xc50d, (int) 0x34e2, (int) 0x24c3,
            (int) 0x14a0, (int) 0x0481, (int) 0x7466, (int) 0x6447,
            (int) 0x5424, (int) 0x4405, (int) 0xa7db, (int) 0xb7fa,
            (int) 0x8799, (int) 0x97b8, (int) 0xe75f, (int) 0xf77e,
            (int) 0xc71d, (int) 0xd73c, (int) 0x26d3, (int) 0x36f2,
            (int) 0x0691, (int) 0x16b0, (int) 0x6657, (int) 0x7676,
            (int) 0x4615, (int) 0x5634, (int) 0xd94c, (int) 0xc96d,
            (int) 0xf90e, (int) 0xe92f, (int) 0x99c8, (int) 0x89e9,
            (int) 0xb98a, (int) 0xa9ab, (int) 0x5844, (int) 0x4865,
            (int) 0x7806, (int) 0x6827, (int) 0x18c0, (int) 0x08e1,
            (int) 0x3882, (int) 0x28a3, (int) 0xcb7d, (int) 0xdb5c,
            (int) 0xeb3f, (int) 0xfb1e, (int) 0x8bf9, (int) 0x9bd8,
            (int) 0xabbb, (int) 0xbb9a, (int) 0x4a75, (int) 0x5a54,
            (int) 0x6a37, (int) 0x7a16, (int) 0x0af1, (int) 0x1ad0,
            (int) 0x2ab3, (int) 0x3a92, (int) 0xfd2e, (int) 0xed0f,
            (int) 0xdd6c, (int) 0xcd4d, (int) 0xbdaa, (int) 0xad8b,
            (int) 0x9de8, (int) 0x8dc9, (int) 0x7c26, (int) 0x6c07,
            (int) 0x5c64, (int) 0x4c45, (int) 0x3ca2, (int) 0x2c83,
            (int) 0x1ce0, (int) 0x0cc1, (int) 0xef1f, (int) 0xff3e,
            (int) 0xcf5d, (int) 0xdf7c, (int) 0xaf9b, (int) 0xbfba,
            (int) 0x8fd9, (int) 0x9ff8, (int) 0x6e17, (int) 0x7e36,
            (int) 0x4e55, (int) 0x5e74, (int) 0x2e93, (int) 0x3eb2,
            (int) 0x0ed1, (int) 0x1ef0 };

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            int portInt = Integer.valueOf(port).intValue();
            ss = new ServerSocket(portInt);
        } catch (Exception x) {
            x.printStackTrace();
        }
        int bytesRead = 0;
        while (true) {
            System.out.println("Now listening for new connections on port: "
                    + port);

            try {
                s = ss.accept();

                in = s.getInputStream();
                out = s.getOutputStream();
                writeInitialGreeting(s);
                Thread t = null;
                stop = false;
                while (!stop) {

                    try {
                        System.out
                                .println("Now listening for new commands on existing socket = "
                                        + s);
                        bytesRead = in.read(inboundBuffer, 0, 256);
                        if (bytesRead == -1) {
                            System.out.println("bytesRead == -1");
                            stop = true;
                            break;
                        }
                        int[] command = new int[bytesRead];
                        for (int x = 0; x < bytesRead; x++) {
                            command[x] = inboundBuffer[x];
                        }
                        System.out.println("command = "
                                + intArrayToString(command));
                        if (Arrays.equals(command, stopCommand)) {
                            System.out.println("stop command received");
                            stop = true;
                            if (t != null)
                                t.join(); // wait for writer thread to close
                            out.write((byte) 0x00);
                        } else if (Arrays.equals(command, multiProtocolCommand)) {
                            System.out
                                    .println("multi ptotocol command received");
                            out.write((byte) 0x00);
                            stop = false;
                            isMultiProtocol = true;
                            eThread = new EmulatorThread(s);
                            t = new Thread(eThread);
                            t.start();
                        } else {
                            out.write((byte) 0xFF);
                            System.out.println("Command "
                                    + intArrayToString(command)
                                    + " not understood");
                        }

                    } catch (IOException e) {
                        stop = true;
                        log.error(e);
                    } catch (InterruptedException e) {
                        stop = true;
                        log.error(e);
                    }

                }// end read while

                // close out client i/o
                in.close();
                out.close();
                s.close();

            } catch (IOException e1) {
                log.error("cannot accept connections:", e1);
            }
        }// end accept while

    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.system.Shutdown#shutdown()
     */
    public boolean shutdown() {
        boolean complete = false;
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (s != null) {
                s.close();
            }
            complete = true;
        } catch (IOException e) {
           
            log.warn("unable to shutdown ", e);
        }
       
        return complete;
    }
}
