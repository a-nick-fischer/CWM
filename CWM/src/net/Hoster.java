package net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hoster {
	
	private String Name;
	private int ID;
	private ServerSocket Server;
	private boolean isRunning;
	private LinkedList<Client> Clients=new LinkedList<>();
	private ExecutorService ExecServ;
   
	public Hoster(int Port, String Name, int ID) throws Exception{
		this.Name=Name;
		this.ID=ID;
		Server=new ServerSocket(Port);
		ExecServ= Executors.newCachedThreadPool();
		ExecServ.submit(() -> lookup());
	}
	
	private void lookup(){
		while(isRunning){
			try {
				Client Client=new Client(Server.accept(),ID);
				Clients.add(Client);
			    ExecServ.submit(Client);
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return Name;
	}
	
	public void broadcast(String msg) throws IOException{
		for(Socket Client: (Socket[]) Clients.toArray()){
			new PrintWriter(Client.getOutputStream()).println(msg);
		}
	}

    public void shutdown() throws IOException{
    	isRunning=false;
    	ExecServ.shutdown();
    	
    	for(Client Client: (Client[]) Clients.toArray()){
    		Client.getSocket().close();
    	}
    	Server.close();
    }
    
}
