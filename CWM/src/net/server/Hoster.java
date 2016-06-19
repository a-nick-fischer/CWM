package net.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hoster {
	
	private String Name;
	private int ID;
	private ServerSocket Server;
	private boolean running;
	private LinkedList<Client> Clients=new LinkedList<>();
	private ExecutorService ExecServ;
   
	public Hoster(int Port, String Name, int ID) throws Exception{
		this.Name=Name;
		this.ID=ID;
		Server=new ServerSocket(Port);
		ExecServ = Executors.newWorkStealingPool();
		ExecServ.submit(() -> lookup());
	}
	
	private void lookup(){
		running=true;
		while(running){
			try {
				Client Client=new Client(Server.accept(),ID);
				Clients.add(Client);
			    ExecServ.submit(Client);
				
			} catch (Exception e) {
				//TODO
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
		for(Client Client: Clients){
			new PrintStream(Client.getSocket().getOutputStream()).println(msg);
		}
	}

    public void shutdown() throws IOException{
    	running=false;
    	ExecServ.shutdown();
    	
    	for(Client Client: Clients){
    		Client.getSocket().close();
    	}
    	Server.close();
    }
    
}
