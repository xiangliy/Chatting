// check whether client online or not.
package org.yexl.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class CheckAlive implements Runnable{
	
	private Map<String, String> nickNamesMap;
	
	public CheckAlive(Map<String, String> nickNamesMap){
		this.nickNamesMap = nickNamesMap;
	}

	public void setNickNamesMap(Map<String, String> nickNamesMap) {
		this.nickNamesMap = nickNamesMap;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			for (Map.Entry<String, String> entry : nickNamesMap.entrySet())
			{
				try {
					Socket socket = new Socket(entry.getValue(), 2600);
		            socket.setSoTimeout(10000);
		            ObjectOutputStream outStream =new ObjectOutputStream(socket.getOutputStream());
	
		            outStream.writeObject("are you alive");
		            outStream.flush();
		            
		            ObjectInputStream instream =new ObjectInputStream(socket.getInputStream());
		            String result = instream.readObject().toString();
		            
		            if(!result.equals("yes")){
		            	nickNamesMap.remove(entry.getKey());
		            }
		            
		            outStream.close();
		            instream.close();
		            socket.close();
		        }catch (Exception e) {
		            System.out.println("Exception:" + e);
		        }
			}
		}
	}
	
}
