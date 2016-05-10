// send message to other clients
package org.yexl.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MessageSender {
	
	private MessageSender(){}
	
	private static class HolderClass {  
        private final static MessageSender instance = new MessageSender();  
	}  
	
	public static MessageSender getInstance(){
		return HolderClass.instance;
	}
	
	public boolean sendOneMessage(String receiver, String message){
		boolean ret = false;
		try {
            Socket socket = new Socket("127.0.0.1", 2587);
            socket.setSoTimeout(60000);
            
            //get target client IP from server.
            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject("getIp " + receiver);
            outStream.flush();
            
            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
            String Ip = instream.readObject().toString();
            
            if(Ip == null){
            	ret =  false;
            }
            
            outStream.close();
            instream.close();
            socket.close();
            
            //send message to target
            Socket socketClient = new Socket(Ip, 2588);
            socketClient.setSoTimeout(60000);
            
            ObjectOutputStream outStreamClient = new ObjectOutputStream(socketClient.getOutputStream());
            outStreamClient.writeObject(NicknameManager.getInstance().getMyNickName() + ":" + message);
            
            outStreamClient.flush();
            outStreamClient.close();
            socketClient.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
            ret = false;
        }

		return ret;
	}
	
	public boolean broadcastMessage(String message){
		try {
            Socket socket = new Socket("127.0.0.1", 2587);
            socket.setSoTimeout(60000);
            
            //get Ip from server.
            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject("getIps");
            outStream.flush();
            
            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
            @SuppressWarnings("unchecked")
			List<String> Ips = (List<String>) instream.readObject();
            
            outStream.close();
            instream.close();
            socket.close();
            
            for(String Ip : Ips){
            	//send message
                Socket socketClient = new Socket(Ip, 2588);
                socketClient.setSoTimeout(60000);
                
                //get Ip from server.
                ObjectOutputStream outStreamClient = new ObjectOutputStream(socketClient.getOutputStream());
                outStreamClient.writeObject(NicknameManager.getInstance().getMyNickName() + ":" + message);
                
                outStreamClient.flush();
                outStreamClient.close();
                socketClient.close();
            }
            
        }catch (Exception e) {
            System.out.println("Exception:" + e);
            return false;
        }

		return true;
	}
}
