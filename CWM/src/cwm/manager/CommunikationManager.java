package cwm.manager;

import java.util.LinkedList;
import java.util.List;
import cwm.net.client.Connection;
import cwm.net.server.Hoster;

public class CommunikationManager{
	
   private static List<Hoster> Servers=new LinkedList<>();
	
   public static void host(int Port, String Name){
    Hoster Server=new Hoster(Port,Name,Servers.size()+1);
    Servers.add(Server);
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

}
