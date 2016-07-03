package manager;

import java.util.LinkedList;
import java.util.List;

import net.client.NIOConnection;
import net.client.SIOConnection;
import net.server.Hoster;

public class CommunikationManager{
	
   private static List<Hoster> Servers=new LinkedList<>();
	
   public static void host(int Port, String Name) throws Exception{
    Hoster Server=new Hoster(Port,Name,Servers.size()+1);
    Servers.add(Server);
   }
   
   public static void connect(String IP, int Port){
	   if(PropertyManager.getProperty("CONNECTION TYPE").equals("SIO")){
	   SIOConnection Con=new SIOConnection(IP,Port);
       ThreadManager.getMainExecutor().execute(Con::handleOut);
       Con.handleIn();
	   }
	   
	   if(PropertyManager.getProperty("CONNECTION TYPE").equals("NIO")){
	   NIOConnection Con=new NIOConnection(IP,Port);
	   Con.run();
	   }
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

}
