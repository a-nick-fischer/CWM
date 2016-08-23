package cwm.manager;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cwm.main.Main;
import cwm.net.client.Connection;
import cwm.net.server.Hoster;

public class CommunikationManager{
	
   private static List<Hoster> Servers=new LinkedList<>();
	
   public static void hostAutostartServer(){
	   File[] ServerFiles = FileManager.getFileByName("Server").listFiles();
	   for(int i=0;i<ServerFiles.length;i++){
		   Properties ServerProps = PropertyManager.loadServer(ServerFiles[i]);
		   if(ServerProps.getProperty("AUTOSTART").equals("true")){
			   host(ServerProps);
		   }
	   }
   }
   
   public static void host(int Port, String Name){
	   if(portIsAvaible(Port)==false){return;}
         Hoster Server=new Hoster(Port,Name,Servers.size()+1);
       Servers.add(Server);
   }
   
   public static void host(int Port,String Name,int ExecutorType){
	   if(portIsAvaible(Port)==false){return;}
	   Hoster Server = new Hoster(Port,Name,Servers.size()+1,ExecutorType);
	   Servers.add(Server);
   }
   
   public static void host(Properties p){
	   try{
		 if(portIsAvaible(Integer.parseInt(p.getProperty("PORT")))==false){return;}
	     Hoster Server = new Hoster(Integer.parseInt(p.getProperty("PORT")),
			                            p.getProperty("NAME"),
			                            Integer.parseInt(p.getProperty("MODE"))
																  );
	     Server.setBannedMessage(p.getProperty("BANNED_MSG"));
	     List<InetAddress> Banned = new LinkedList<>();
	     String[] s = null;
	     if(p.getProperty("BANNED").equals("[]") == false && p.getProperty("BANNED").isEmpty() == false){
	       s = p.getProperty("BANNED").replaceAll("[", "").replaceAll("]", "").split(",");
	     }else{
	       s = new String[0];
	     }
	     for(String str: s){
		     Banned.add(InetAddress.getByName(str));
	     }
	     Servers.add(Server);
	   }catch(Throwable e){
	     Main.handleError(e, Thread.currentThread(), "ERROR:");
	   }
	 }
   
   public static void connect(String IP, int Port){
	   Connection Con=new Connection(IP,Port);
     ThreadManager.addTask(() -> Con.handleOut());
     Con.handleIn();
   }
   
   public static List<Hoster> getServerList(){
	   return Servers;
   }
   
   public static Hoster getServerByID(int ID){
	   return Servers.get(ID-1);
   }
   
   public static Hoster getServerByName(String name){
	   for(Hoster Server: Servers){
		   if(Server.getName().equals(name)){return Server;}
	   }
     return null;
   }

   public static boolean portIsAvaible(int port){
	   if (port < 0 || port > 65535) {
	       return false;
	   }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (Throwable e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (Throwable e) {
	                Main.handleError(e, Thread.currentThread(), "ERROR:");
	            }
	        }
	    }

	    return false;
   }
}
