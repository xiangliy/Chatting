package org.yexl.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserManager {
	// use a set to quickly verify whether a nickname exist or not, Set is faster than Map in search.
	private Set<String> nickNamesSet = new HashSet<String>();
	private CheckAlive checkAlive;
		
	// use a map to save <nickname, IP>. Mostly used in broadcast.
	private Map<String, String> nickNamesMap = new HashMap<String, String>();
	
	private UserManager(){
		checkAlive = new CheckAlive(nickNamesMap);
		Thread receiverThread = new Thread(checkAlive);  
        receiverThread.start(); 
	}
	
	// singleton
	private static class HolderClass {  
        private final static UserManager instance = new UserManager();  
	}  
	
	public static UserManager getInstance(){
		return HolderClass.instance;
	}

	public boolean signin(String nickname, String IPwithPort){
		if(nickNamesSet.contains(nickname)){
			return false;
		}
		
		String IP = IPwithPort.substring(1, IPwithPort.indexOf(':'));
		
		nickNamesSet.add(nickname);
		nickNamesMap.put(nickname, IP);
		
		checkAlive.setNickNamesMap(nickNamesMap);
		
		return true;
	}
	
	public boolean signout(String nickname){
		if(!nickNamesSet.contains(nickname)){
			return false;
		}
		
		nickNamesSet.remove(nickname);
		nickNamesMap.remove(nickname);
		
		return true;
	}
	
	public Set<String> getAllNickNames(){
		return nickNamesSet;
	}
	
	public List<String> getIPs(){
		List<String> l = new ArrayList<String>();
		l.addAll(nickNamesMap.values());
		
		return l;
	}
	
	public String getIP(String nickname){
		return nickNamesMap.get(nickname);
	}
}
