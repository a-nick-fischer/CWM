package cwm.manager;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cwm.main.Main;
import cwm.net.server.Hoster;

public class PropertyManager{
	
  private static Properties pt=new Properties();
  private static List<Properties> ServerList = new LinkedList<>();
 
  public static void loadDefaults(){
	  pt.setProperty("GUI","false");
	  pt.setProperty("LOG", "true");
	  pt.setProperty("ANSI", "false");
	  pt.setProperty("MAIN_EXECUTOR_TYPE", "-2");
  }
  
  public static Properties getProperties(){
	  return pt;  
  } 

  public static void setProperties(Properties pt){
	  PropertyManager.pt=pt;
  }
  
  public static String getProperty(String key){
  	return pt.getProperty(key);  
  }

  public static void setProperty(String key,String value){
	  pt.setProperty(key, value);
  }
  
  public static void load(File file){
	  FileReader fr;
	  try {
	  	fr = new FileReader(file);
		  pt.load(fr);
		  fr.close();
	  }catch (Throwable e) {
		  Main.handleError(e,Thread.currentThread(),"ERROR:");
  	}
  }
  
  public static void setup(File file){
    try {
	    PrintWriter  pr = new PrintWriter(file);
	    pt.store(pr," ");
	    pr.close();
    }catch (Throwable e) {
		  Main.handleError(e,Thread.currentThread(),"ERROR:");
	  }
  }
 
  public static void saveServer(Hoster h){
    try{
	    File ServerFile = new File(FileManager.getFileByName("Server").getAbsolutePath()+File.separator+h.getName()+".conf");
      ServerFile.createNewFile();
      Properties ServerProps = new Properties();
      ServerProps.setProperty("NAME", h.getName());
      ServerProps.setProperty("PORT", String.valueOf(h.getPort()));
      ServerProps.setProperty("AUTOSTART", "false");
      ServerProps.setProperty("MODE",String.valueOf(h.getMode()));
      ServerProps.setProperty("TYPE","TCP");
      ServerProps.setProperty("BANNED_MSG", h.getBannedMSG());
      ServerProps.setProperty("BANNED", h.getBannedAdresses().toString());
      ServerList.add(ServerProps);
      ServerProps.store(new PrintWriter(ServerFile), "");
    }catch(Throwable e){
    	Main.handleError(e, Thread.currentThread(), "ERROR:");
    }
  }
  
  public static Properties loadServer(File f){
	  try{
		  Properties ServerProps = new Properties();
		  ServerProps.load(new FileReader(f));
		  ServerList.add(ServerProps);
			return ServerProps;
	  }catch(Throwable e){
	    	Main.handleError(e, Thread.currentThread(), "ERROR:");
	  }
		return null;
  }

  public static List<Properties> getServerPropertiesList(){
	  return ServerList;
  }
}
