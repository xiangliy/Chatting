// A thread for listing message from other clients
package org.yexl.client;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.TextField;

public class MessageReceiver implements Runnable{
	
	private TextField messagelbl;
	Boolean running = true;
	
	MessageReceiver(TextField receivedMessage){
		messagelbl = receivedMessage;
	}
	
	public void terminate(){
		running = false;
	}

	@Override
	public void run() {
		try {
			running = true;
			ServerSocket serverSocket =new ServerSocket(2588);
            while (running) {
                Socket socket = serverSocket.accept();
                ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());

                String message = instream.readObject().toString();
                
                messagelbl.setText(message);
                
                instream.close();
                socket.close();
            }
            serverSocket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }
	}
}
