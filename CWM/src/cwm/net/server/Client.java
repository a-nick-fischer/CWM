package cwm.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cwm.main.Main;
import cwm.manager.CommunikationManager;

public class Client implements Runnable{

	private Socket client;
	private int ServerID;
	private BufferedReader sc;
	private boolean running=false;
	
	public Client(Socket client, int ServerID) throws IOException{
		this.setSocket(client);
		this.ServerID=ServerID;
		sc=new BufferedReader( new InputStreamReader(client.getInputStream()));
	}
	
	@Override
	public void run() {
		setRunning(true);
		try {
			CommunikationManager.getServerByID(ServerID).broadcast(client.getInetAddress().getHostAddress()+" joined the room!");
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
			disconnect();
		}
		while(isRunning()){
			    try{
			     String line;
			     if((line=sc.readLine())!=null){
				   CommunikationManager.getServerByID(ServerID).broadcast("["+client.getInetAddress().getHostAddress()+"] "+line);}
			    } catch (Throwable e) {
			    	disconnect();
					Main.handleError(e,Thread.currentThread(),"ERROR:");
				}
	    }
		disconnect();
	}

	public synchronized Socket getSocket() {
		return client;
	}

	public synchronized void setSocket(Socket client) {
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

	public synchronized void disconnect(){
		running = false;
		try{
		 if(sc!=null){sc.close();}
		 if(client!=null){client.close();}
		}catch(Exception e){
			Main.handleError(e, Thread.currentThread(), "ERROR:");
		}
	}
}
