package manager;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import main.Main;

public class FileManager{
	
	private static File PropertyFile=new File("JTT\\Prog.conf");
	private static File SelfFile;
	private static File Folder=new File("JTT");
	private static File LogFile=new File("JTT\\Err.log");
	private static File PluginFolder=new File("JTT\\Plugins");
	
 public static void setup(){  
	    try {
			SelfFile=new File(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),"UTF-8"));
			if(!Folder.exists()){Folder.mkdir();}
			if(!PluginFolder.exists()){PluginFolder.mkdir();}
			if(!PropertyFile.exists()){PropertyFile.createNewFile();}
			if(!LogFile.exists()){LogFile.createNewFile();}
			Main.setInstalled(true);
			
		} catch (Exception e){
        //TODO
		}
 }
 
 public static void deleteAll(){
	 PropertyFile.delete();
	 LogFile.delete();
	 try {
		Files.move(SelfFile.toPath(),new File(Folder.getParent()+SelfFile.getName()).toPath());
		Main.setInstalled(false);
	} catch (IOException e) {
		//TODO
	}
 }
 
 public static File getLogFile(){
	return LogFile; 
 }
 
 public static File getDataFolder(){
	 return Folder;
 }

 public static File getJarFile(){
	 return SelfFile;
 }
 
 public static File getPropertyFile(){
	return PropertyFile; 
 }
 
 public static boolean isInstalled(){
	 if(Folder.exists() && LogFile.exists() && PluginFolder.exists() && PropertyFile.exists()){
		 return true;
	 }
	 else return false;
 }
 
}
