package net;

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
		try {
			CommunikationManager.getServerByID(ServerID).broadcast(client.getInetAddress()+" joined the room!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(running){
			if(sc.hasNextLine()){
			     StringBuilder line= new StringBuilder(sc.nextLine());
			     line.insert(0, "["+client.getInetAddress()+"] ");
			    try {
					CommunikationManager.getServerByID(ServerID).broadcast(line.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}}
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
