package cwm.net.client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import cwm.cmd.Colors;
import cwm.main.Main;
import cwm.manager.IOManager;

public class Connection{
	
  private Socket Server;
  private PrintStream toServer;
  private Scanner fromServer;
  
  private boolean running=false;
	
 public Connection(String IP, int Port){
	try{
	 try {
		 
		Server     = new Socket(IP,Port);
		toServer   = new PrintStream(Server.getOutputStream(),true);
		fromServer = new Scanner(Server.getInputStream());
		running = true;
		
	 }catch(java.net.ConnectException e){
		 IOManager.print(Colors.YELLOW+"\nCould not connect to server! \n");
		 Main.run();
	 }
    }catch (Throwable e) {
		Main.handleError(e,Thread.currentThread(),"ERROR:");
	}
 }

 public void handleOut(){
	 while(isRunning()){
		   IOManager.print(">>>");
			 String line=IOManager.nextLine();
			 if(line.equals("/exit")){disconnect();continue;}
			 toServer.print(line+"\r\n");
			 toServer.flush();
	 }
 }
 
 public void handleIn(){
  try {
   while(isRunning()){
		IOManager.print(fromServer.nextLine()+"\n>>>");
   }
  }catch (Throwable e) {
	Main.handleError(e,Thread.currentThread(),"ERROR:");
  }
 }
 
 
 
 public synchronized boolean isRunning(){
	 return running;
 }
 
 public synchronized void setRunning(boolean running){
	 this.running=running;
 }
 
 public void disconnect(){
	 setRunning(false);
	 try{
	  toServer.print("/exit\r\n");
	  if(fromServer!=null){fromServer.close();}
	  if(toServer!=null){toServer.close();}
	  if(Server!=null && Server.isClosed()==false){Server.close();}
	 }catch(Throwable e){
		 Main.handleError(e, Thread.currentThread(), "ERROR:");
	 }
 }
}
