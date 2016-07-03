package net.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import main.Main;
import manager.IOManager;

public class NIOConnection implements Runnable{
	
  private SocketChannel Server;
  private Scanner Userin;
  private boolean running=false;
	
 public NIOConnection(String IP, int Port){
	 try {
		Server     = SocketChannel.open(new InetSocketAddress(IP,Port));
		Userin =new Scanner(Channels.newChannel(IOManager.getInputStream()));
		running=true;
	 } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
 }
 
 public void run(){
	 while(running){
		 try{
		 ByteBuffer sendbuffer = ByteBuffer.allocate(128);
		 ByteBuffer revbuffer = ByteBuffer.allocate(128);
		 
		 String line;
         if((line = Userin.nextLine())!=null){
        	 sendbuffer.asCharBuffer().put(line);
        	 Server.write(sendbuffer);
        	 sendbuffer.clear();
        	 
        	 IOManager.print(line);
         }
         
         if(Server.read(revbuffer) != -1){
        	 revbuffer.flip();
        	 IOManager.print("Reveived: "+revbuffer.asCharBuffer().toString());
        	 revbuffer.clear();
         }
        	 
			
		 } catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
		 }
		 
	 }
	 
	 try {
		Server.close();
	 } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
	 }
 }
 
 public boolean isRunning(){
	 return running;
 }
 
 public void setRunning(boolean running){
	 this.running=running;
 }
 
 
}
