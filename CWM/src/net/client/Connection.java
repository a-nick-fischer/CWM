package net.client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import manager.IOManager;

public class Connection{
	
  private Socket Server;
  private PrintStream toServer;
  private Scanner fromServer;
  
  private boolean running=false;
	
 public Connection(String IP, int Port){
	 try {
		 
		Server     = new Socket(IP,Port);
		toServer   = new PrintStream(Server.getOutputStream());
		fromServer = new Scanner(Server.getInputStream());
		
		running=true;
		
	} catch (Exception e) {
		//TODO
	}
 }

 public void handleOut(){
	 while(running){
		   IOManager.print(">>>");
			 String line=IOManager.nextLine();
			 if(line.equals("#break")){toServer.println("-----DISCONNECTED-----");running=false;}
			 toServer.print(line);
	 }
 }
 
 public void handleIn(){
	 IOManager.print(fromServer.nextLine());
 }
 
 
 
 public boolean isRunning(){
	 return running;
 }
 
 public void setRunning(boolean running){
	 this.running=running;
 }
 
 
}
