package net.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import main.Main;
import manager.IOManager;

public class SIOConnection{
	
  private Socket Server;
  private PrintStream toServer;
  private BufferedReader fromServer;
  
  private boolean running=false;
	
 public SIOConnection(String IP, int Port){
	 try {
		 
		Server     = new Socket(IP,Port);
		toServer   = new PrintStream(Server.getOutputStream(),true);
		fromServer = new BufferedReader(new InputStreamReader(Server.getInputStream()));
		
		running=true;
		
	 } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
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
   while(running){
	 try {
	  String rev;
	  if((rev = fromServer.readLine()) != null){
		IOManager.print(rev);
	  }
	 } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
	 }
   }
 }
 
 
 
 public boolean isRunning(){
	 return running;
 }
 
 public void setRunning(boolean running){
	 this.running=running;
 }
 
 
}
