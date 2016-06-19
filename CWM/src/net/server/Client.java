package net.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import manager.CommunikationManager;

public class Client implements Runnable{

	private Socket client;
	private int ServerID;
	private Scanner sc;
	private boolean running=false;
	
	public Client(Socket client, int ServerID) throws IOException{
		this.setSocket(client);
		this.ServerID=ServerID;
		sc=new Scanner(client.getInputStream());
	}
	
	@Override
	public void run() {
		running=true;
		try {
			CommunikationManager.getServerByID(ServerID).broadcast(client.getInetAddress()+" joined the room!");
			System.out.print("IP: "+client.getInetAddress()+"\nServerID: "+ServerID+"\nServerName: "+CommunikationManager.getServerByID(ServerID).getName());
		} catch (Exception e1) {
		   e1.printStackTrace();
		}
		while(running){
			     StringBuilder line= new StringBuilder(sc.nextLine());
			     line.insert(0, "["+client.getInetAddress()+"] ");
			    try {
					CommunikationManager.getServerByID(ServerID).broadcast(line.toString());
				} catch (Exception e) {
					//TODO
				}}
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
