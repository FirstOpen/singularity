/**
 * 
 */
package org.firstopen.singularity.devicemgr.emulators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;

/**
 * @author TomRose
 * 
 */
public class IPicoEmulator implements Runnable, Shutdown {
	private final char CR = '\r';

	private final char LF = '\n';

	private Log log = LogFactory.getLog(this.getClass());

	private boolean continueWriting;

	private String defaultPort = "3002";
	
	private Thread emulatorThread = null;

	private int msecPerRead = 50;
	
	private ServerSocket ss = null;

	private String messageSeed = "aa004497fc0000220404051012062510473600ff";
	/**
	 * 
	 */
	public IPicoEmulator() {

	}

	public static void main(String[] args) {
		IPicoEmulator iPicoEmulator = new IPicoEmulator();
		
		for (int i = 0; i < args.length; i++) {
			switch (i) {

			case 0:
				iPicoEmulator.defaultPort = args[i];
				break;
			case 1:
				iPicoEmulator.msecPerRead = Integer.parseInt(args[i]);
				break;
			case 2:
				iPicoEmulator.messageSeed = args[i];
				break;

			}//end switch
		}// end for
		
		ShutdownManager sdm = new ShutdownManager();
		Runtime.getRuntime().addShutdownHook(sdm);
		ShutdownManager.addManagedObject(iPicoEmulator);

		iPicoEmulator.emulatorThread = new Thread(iPicoEmulator);
		iPicoEmulator.emulatorThread.start();
	}
	
	protected String calcLRC(byte[] buf) {
		int buffer = 0;
		for (int i = 2; i < buf.length; i++) {
			buffer = buf[i] + buffer;
		}
		buffer = (0x000000FF & (buffer));

		return (Integer.toHexString(buffer));
	}
	public void run() {

		StringBuffer message = new StringBuffer();
		String sLRC = calcLRC(messageSeed.getBytes());
		message.append(messageSeed);
		message.append(sLRC);
		message.append(CR);
		message.append(LF);
		message.append(message);
		
		byte[] bMessage = message.toString().getBytes();
		try {
			 ss = new ServerSocket(Integer.parseInt(defaultPort));
			continueWriting = true;
		
			while (continueWriting) {
				log.info("emulator listening on port " + defaultPort);
				Socket s = ss.accept();
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				while (continueWriting) {
					log.debug("writing message");
					out.write(bMessage);
					Thread.sleep(msecPerRead);
				}
			}
		} catch (IOException e) {
			log.error("unable to start emulator", e);
		} catch (InterruptedException e) {
			log.error("unable throttle emulator, sleep fialed", e);
		}

	}

	public boolean shutdown() {
		continueWriting = false;
		boolean status = false;
		try {
			log.info("waiting for listener to shutdown");
			emulatorThread.join(2000);
		    status = true;
		} catch (InterruptedException e) {
			log.error("join timeedout attempting socket close...", e);
			if (emulatorThread.isAlive()){
				try {
					ss.close();
					status = true;
				} catch (IOException e1) {
					log.error("unable to shutdown emulator", e1);
				}
			}
		
			
		}
		System.out.println("Shutdown Complete.");
		
		return status;
	}

}// end class IPicoEmulator
