package org.yexl.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Set;

public class ServerMain {

	Boolean running = true;

	public boolean startListening(){
		try {
			running = true;
			ServerSocket serverSocket =new ServerSocket(2587);
            while (running) {
                Socket socket = serverSocket.accept();
                ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());

                String command = instream.readObject().toString();
                
                System.out.println(command);
                String nickName = null;
                
                String [] commandArray = command.split(" ");
                if(commandArray.length == 0){
                	//invalid commend
                	continue;
                }else if(commandArray.length == 2){
                	nickName = commandArray[1];
                }
                
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                
                switch(commandArray[0]){
	                case "signin":
	                	if(UserManager.getInstance().signin(nickName, socket.getRemoteSocketAddress().toString())){
	                		outstream.writeObject("success");
	                	}else{
	                		outstream.writeObject("fail");
	                	}
	                	break;
	                case "signout":
	                	if(UserManager.getInstance().signout(nickName)){
	                		outstream.writeObject("success");
	                	}else{
	                		outstream.writeObject("fail");
	                	}
	                	break;
	                case "clients":
	                	Set<String> clients = UserManager.getInstance().getAllNickNames();
	                	outstream.writeObject(clients);
	                	break;
	                case "getIp":
	                	String IP = UserManager.getInstance().getIP(nickName);
	                	outstream.writeObject(IP);
	                	break;
	                case "getIps":
	                	List<String> IPs = UserManager.getInstance().getIPs();
	                	outstream.writeObject(IPs);
	                	break;
	                default:
	                	System.out.println("wrong command");	
                }
                
                outstream.flush();
                instream.close();
                socket.close();
            }
            serverSocket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }
		return true;
	}
		
	public void terminate(){
		running = false;
	}

	public static void main(String[] args) {
		ServerMain server = new ServerMain();
		server.startListening();
	}
}
