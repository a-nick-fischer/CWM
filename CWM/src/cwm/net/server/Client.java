package cwm.net.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import cwm.main.Main;
import cwm.manager.CommunikationManager;

public class Client implements Runnable{

	private Socket client;
	private int ServerID;
	private String Name;
	private Scanner sc;
	private PrintStream ps;
	private boolean running;
	
	public Client(Socket client, int ServerID, String Name) throws IOException{
		this.setSocket(client);
		this.ServerID=ServerID;
		this.Name=Name;
		ps=new PrintStream(client.getOutputStream(),true);
		sc=new Scanner(client.getInputStream());
	}
	
	@Override 
	public void run() {
		setRunning(true);
		try {
			CommunikationManager.getServerByID(ServerID).broadcast(Name+" joined the room!");
		}catch (Throwable e) {
			disconnect();
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
		while(isRunning()){
		  try{
			  if(sc.hasNextLine()){
			    String line = sc.nextLine();
			    if(line.equals("/exit")){disconnect();}
			    if(line.equals("/list")){
			      List<Client> Clients = CommunikationManager.getServerByID(ServerID).getClients();
			    	ps.printf("[SERVER] There are %s users on this server\r\n",Clients.size());
			    	for(int i=0;i<Clients.size();i++){
			    		ps.printf("%s: %s\r\n",i,Clients.get(i).getName());
			    	}
			    }else{
						CommunikationManager.getServerByID(ServerID).broadcast(this,line);
					}
				}
			}catch (Throwable e) {
			  disconnect();
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
	  }
	}

	public Socket getSocket() {
		return client;
	}
  
	public void setSocket(Socket client) {
		this.client = client;
	}
	
	public synchronized boolean isRunning(){
		return running;
	}
	
	public synchronized void setRunning(boolean running){
		this.running=running;
	}
	
	public int getServerID(){
		return ServerID;
	}

	public String getName(){
		return Name;
	}
	
	public PrintStream getWriter(){
		return ps;
	}
	
	public synchronized void disconnect(){
		CommunikationManager.getServerByID(ServerID).broadcast(Name+" exited!");
		setRunning(false);
		CommunikationManager.getServerByID(ServerID).getClients().remove(this);
		try{
		  if(sc!=null){sc.close();}
		  if(ps!=null){ps.close();}
		  if(client!=null){
			if(client.isClosed()==false){
				client.close();
			}
		  }
		}catch(Exception e){
			Main.handleError(e, Thread.currentThread(), "ERROR:");
		}
	}
}
