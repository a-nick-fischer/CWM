package cwm.net.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import cwm.cmd.Colors;
import cwm.main.Main;
import cwm.manager.IOManager;

public class Connection{
	
  private Socket Server;
  private PrintStream toServer;
  private BufferedReader fromServer;
  
  private boolean running=false;
	
 public Connection(String IP, int Port){
	try{
	 try {
		 
		Server     = new Socket(IP,Port);
		toServer   = new PrintStream(Server.getOutputStream(),true);
		fromServer = new BufferedReader(new InputStreamReader(Server.getInputStream()));
		
		running=true;
	 }catch(java.net.ConnectException e){
		 IOManager.print(Colors.YELLOW+"\nCould not connect to server! \n");
		 Main.run();
	 }
	 } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
	 }
 }

 public void handleOut(){
	 while(running){
		   IOManager.print(">>>");
			 String line=IOManager.nextLine();
			 if(line.equals("#break")){toServer.println("-----DISCONNECTED-----");IOManager.print("-----DISCONNECTED-----");running=false;}
			 toServer.print(line);
	 }
 }
 
 public void handleIn(){
   while(running){
	 try {
	  String rev;
	  if((rev = fromServer.readLine()) != null){
		IOManager.print("\n"+rev+"\n");
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
