// manage the local user.
package org.yexl.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class NicknameManager {
	
	private NicknameManager(){}
	
	private static class HolderClass {  
        private final static NicknameManager instance = new NicknameManager();  
	}  
	
	public static NicknameManager getInstance(){
		return HolderClass.instance;
	}
	
	private String MyNickName = null;
	
	public String getMyNickName(){
		return MyNickName;
	}
	
	public boolean signin(String nickname){
		String result = null;
		try {
            Socket socket = new Socket("127.0.0.1", 2587);
            socket.setSoTimeout(60000);
            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());

            outStream.writeObject("signin " + nickname);
            outStream.flush();
            
            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
            result = instream.readObject().toString();
            
            outStream.close();
            instream.close();
            socket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }

		if(result.equals("success")){
			MyNickName = nickname;
			return true;
		}
		
		return false;
	}
	
	public boolean signout(String nickname){
		String result = null;
		try {
            Socket socket = new Socket("127.0.0.1", 2587);
            socket.setSoTimeout(60000);
            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());

            outStream.writeObject("signout " + nickname);
            outStream.flush();
            
            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
            result = instream.readObject().toString();
            
            outStream.close();
            instream.close();
            socket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }

		if(result.equals("success")){
			MyNickName = nickname;
			return true;
		}
		
		return false;
	}
	
	//get nickname list and show it in the listview
	@SuppressWarnings("unchecked")
	public Set<String> getNicknameList(){
		Set<String> result = null;
		try {
            Socket socket = new Socket("127.0.0.1", 2587);
            socket.setSoTimeout(60000);
            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());

            outStream.writeObject("clients");
            outStream.flush();
            
            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
            result = (Set<String>) instream.readObject();
            
            outStream.close();
            instream.close();
            socket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }

		return result;
	}
	
}
