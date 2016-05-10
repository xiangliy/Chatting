// the class let server know the client is not offline
package org.yexl.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class KeepAlive implements Runnable {

	@Override
	public void run() {
		try {
			ServerSocket serverSocket =new ServerSocket(2600);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
                String message = instream.readObject().toString();
                
                ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());
                outStream.writeObject("yes");
                outStream.flush();
               
                instream.close();
                outStream.close();
                socket.close();
            }
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }
	}

}
