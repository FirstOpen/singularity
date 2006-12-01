/*
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

package org.firstopen.singularity.devicemgr.interrogator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.InfrastructureException;

public class MPR2010_AWID_IO implements Interrogator, Shutdown {

    private Socket aSocket;

    private InputStream readFromInterrogatorStream;

    private OutputStream writeToInterrogatorStream;

    private boolean continueReading = false;

    private DeviceProfile deviceProfile = null;

    private InterrogatorIO interrogatorIO;

    private Thread readerThread;

    private Logger log = null;

    public MPR2010_AWID_IO(DeviceProfile deviceProfile) {
        log = Logger.getLogger(this.getClass());
        setDeviceProfile(deviceProfile);
        /*
         * register with shutdown manager
         */
        ShutdownManager.addManagedObject(this);
    }

    private void flushInitialGreeting() throws Exception {
        log.debug("flushInitialGreeting()");
        StringBuffer greeting = new StringBuffer();
        while (true) {
            greeting.append((char) readFromInterrogatorStream.read());
            if (greeting.toString().lastIndexOf("MODULE") != -1) break;
        }
        log.debug("flishInitialGreeting = " + greeting.toString());
    }

    public void run() {
        try {
            connect();
            log.debug("starting deviceProfileName = " + deviceProfile.getDeviceProfileID());
            ReadFromInterrogatorThread rThread = new ReadFromInterrogatorThread();
            readerThread = new Thread(rThread);
            // write_ISO_18000_ID_ToInterrogator();
            write_MULTI_PROTOCOL_ID_ToInterrogator();
            readerThread.start();

        } catch (Exception x) {
            log.error("unable to start thread", x);
        }
    }

    private void write_MULTI_PROTOCOL_ID_ToInterrogator() {
        log.debug("write_MULTI_PROTOCOL_ID_ToInterrogator()");
        try {
            int[] iCommand = { 0x07, 0x14, 0x01, 0x00, 0x00 };
            writeToInterrogatorStream.write(compute(iCommand));
            writeToInterrogatorStream.flush();
            if (readFromInterrogatorStream.read() != 0) {
                throw new Exception("Command is not valid!");
            }
        } catch (Exception x) {
            log.error(x);
        }

    }

    private void write_ISO_18000_ID_ToInterrogator() {
        log.debug("write_ISO_18000_ID_ToInterrogator()");
        try {
            int[] iCommand = { 18, 17, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 43,
                    240 };
            writeToInterrogatorStream.write(compute(iCommand));
            writeToInterrogatorStream.flush();
            if (readFromInterrogatorStream.read() != 0) {
                log.error("Command is not Valid!");
                return;
            }
        } catch (Exception x) {
            log.error("unable to Write ISO 18000 ID to Interrorgator", x);
        }
    }

    private void write_STOP_ToInterrogator() throws IOException {

        byte[] iCommand = new byte[] { 0x00, 0x00 };
        writeToInterrogatorStream.write(iCommand);
        writeToInterrogatorStream.flush();
        if (readFromInterrogatorStream.read() != 0) {
            log.error("Command is not valid!");
        }

    }

    private void close() throws IOException {

        log.debug("close()");
        write_STOP_ToInterrogator();
        readFromInterrogatorStream.close();
        writeToInterrogatorStream.close();
        aSocket.close();

    }

    private void printIntArray(int[] intArray) {
        StringBuffer buffer = new StringBuffer();
        for (int z = 0; z < intArray.length; z++) {
            buffer.append(Integer.toHexString(intArray[z]));
            buffer.append(' ');
        }
        log.debug("tagId = " + buffer.toString());
    }

    @SuppressWarnings("unused")
    private void printByteArray(byte[] byteArray) {
        StringBuffer buffer = new StringBuffer();
        for (int z = 0; z < byteArray.length; z++) {
            buffer.append(Integer.toHexString(byteArray[z]));
            buffer.append(' ');
        }
        log.debug("command = " + buffer.toString());
    }

    class ReadFromInterrogatorThread implements java.lang.Runnable {
        ReadFromInterrogatorThread() {
        }

        public void run() {
            log.debug("readFromInterrogator() ");
            try {
                int[] tagIds = null;
                int tagByteQuantity = 0;

                while (continueReading) {
                    int tagType = readFromInterrogatorStream.read();
                    if (tagType == 0x0D) // 8 byte tagId
                    tagByteQuantity = 8;
                    else if (tagType == 0x11) // 12 byte tagId
                    tagByteQuantity = 12;
                    else {
                        log
                                .error("tagByteQuantity is not a valid identifier = "
                                        + tagByteQuantity);
                        return;
                    }

                    tagIds = new int[tagByteQuantity];

                    readFromInterrogatorStream.read();
                    readFromInterrogatorStream.read();
                    for (int t = 0; t < tagByteQuantity; t++) {
                        tagIds[t] = readFromInterrogatorStream.read();
                    }
                    readFromInterrogatorStream.read();
                    readFromInterrogatorStream.read();

                    printIntArray(tagIds);

                    if (interrogatorIO == null)
                        throw new Exception(
                                "Device Manager has not been assigned");

                    /*
                     * TODO: find the appropriate sensor and reader for this
                     * event. Current it is just the first.
                     */
                    Set<Sensor> sensors = deviceProfile.getSensorSet();
                    
                    Set<Reader> readers = deviceProfile.getReaderSet();
                    
                    Sensor sensor = sensors.iterator().next();
                    Reader reader = readers.iterator().next();
                            
                    ReaderEvent event = new ReaderEvent(sensor);
                    
                    event.setReaderName(reader.getName());
                    
                    ArrayList<Tag> tagList = new ArrayList<Tag>();

                    Tag tag = new Tag();
                    tag.setIntvalue(tagIds);
                    tag.increment();
                    tagList.add(tag);
                    event.setTagIds(tagList);
                    interrogatorIO.sendEvent(event);

                }

            } catch (Exception x) {
                log.error("unable to start interrogator thread", x);
            }
        }
    }

    private static final int[] table = new int[] { (int) 0x0000, (int) 0x1021,
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

    private byte[] compute(int[] buffer) {
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

        /*
         * for(int t = 0; t < bArray.length; t++) { log.debug("byte =
         * "+bArray[t]); }
         */

        return bArray;
    }

    private void connect() {
        aSocket = null;
        try {

            log.debug("Constructor()  deviceProfileName = " + deviceProfile.getDeviceProfileID());
            int portInt = Integer.valueOf(deviceProfile.getPort()).intValue();
            aSocket = new Socket(deviceProfile.getIpAddress(), portInt);

            // aSocket.setSoTimewriteToInterrogatorStream.4000);
            readFromInterrogatorStream = aSocket.getInputStream();
            writeToInterrogatorStream = aSocket.getOutputStream();
            flushInitialGreeting();

        } catch (java.net.NoRouteToHostException x) {
            log.error("No route to host exception.  "
                    + deviceProfile.getIpAddress());
        } catch (Exception x) {
            log.error("unable to conectio to reader", x);
        }
    }

    public synchronized void on() {
        if (continueReading == false) {
            continueReading = true;
            this.run();
        }
    }

    /**
     * Must complete shutdown before starting again.
     * @throws IOException 
     */
    public synchronized void off() {
        if (continueReading == true) {
            continueReading = false;
            // currentThread.join();

            try {
                close();
            } catch (IOException e) {
                // TODO: Clean up for restart
                log.error("unable to turn off device", e);
                throw new InfrastructureException("unable to turn off device");
            }

        }// end if
    }

    /**
     * @return Returns the deviceProfile.
     */
    public DeviceProfile getDeviceProfile() {
        return deviceProfile;
    }

    /**
     * @param deviceProfile
     *            The deviceProfile to set.
     */
    public void setDeviceProfile(DeviceProfile deviceProfile) {
        this.deviceProfile = deviceProfile;
    }

    /**
     * @return Returns the interrogatorIO.
     */
    public InterrogatorIO getInterrogatorIO() {
        return interrogatorIO;
    }

    /**
     * 
     */
    public void setInterrogatorIO(InterrogatorIO interrogatorIO) {
        this.interrogatorIO = interrogatorIO;

    }

    public boolean shutdown() {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryRep()
     */
    public void queryRep() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#ack()
     */
    public void ack() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#query()
     */
    public void query() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryAdjust()
     */
    public void queryAdjust() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#select()
     */
    public void select() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#nak()
     */
    public void nak() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#reqRN()
     */
    public void reqRN() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#read()
     */
    public void read() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#write()
     */
    public void write() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#kill()
     */
    public void kill() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#lock()
     */
    public void lock() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#access()
     */
    public void access() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockWrite()
     */
    public void blockWrite() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockErase()
     */
    public void blockErase() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#getDescription()
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     */
    public MPR2010_AWID_IO() {
        super();
        // TODO Auto-generated constructor stub
    }

  
}
