package net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import main.Main;
import manager.CommunikationManager;

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
		running=true;
		try {
			CommunikationManager.getServerByID(ServerID).broadcast(client.getInetAddress().getHostAddress()+" joined the room!");
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
		while(running){
			    try{
			     StringBuilder line= new StringBuilder(sc.readLine());
			     line.insert(0, "["+client.getInetAddress()+"] ");
					CommunikationManager.getServerByID(ServerID).broadcast(line.toString());
			    } catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
				}
	    }
		try {
			sc.close();
			client.close();
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
	}

	public Socket getSocket() {
		return client;
	}

	public void setSocket(Socket client) {
		this.client = client;
	}
	

	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean running){
		this.running=running;
	}
	
	public int getServerID(){
		return ServerID;
	}

}
