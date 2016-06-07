package manager;

import java.util.LinkedList;
import java.util.List;

import net.Hoster;

public class CommunikationManager{
	
   private static LinkedList<Hoster> Servers=new LinkedList<>();
	
   public static void host(int Port, String Name) throws Exception{
    Hoster Server=new Hoster(Port,Name,Servers.size()+1);
    Servers.add(Server);
   }
   
   public static void connect() throws Exception{
	   
   }
   
   public static List<Hoster> getServerList(){
	   return Servers;
   }
   
   public static Hoster getServerByID(int ID){
	   return Servers.get(ID);
   }
   
   public static Hoster getServerByName(String name){
	   for(Hoster Server: (Hoster[]) Servers.toArray()){
		   if(Server.getName().equals(name)){return Server;}
	   }
   return null;
   }

}
