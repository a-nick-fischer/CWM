package cwm.manager;

import java.io.File;
import java.util.LinkedList;

import cwm.main.Main;

public class FileManager{
	
	private static LinkedList<File> Files = new LinkedList<>();
	private static File PropertyFile=new File("CWMessenger\\Prog.conf");
	private static File Folder      = new File("CWMessenger");
	private static File LogFile     = new File("CWMessenger\\Cwm.log");
	private static File PluginFolder=new File("CWMessenger\\Plugins");
	private static File ServerFolder=new File("CWMessenger\\Server");
	
 public static void init(){
	 Files.add(Folder);
	 Files.add(ServerFolder);
	 Files.add(LogFile);
	 Files.add(PropertyFile);
 }
	
 public static void install(){
	    try {
			if(!Folder.exists()){Folder.mkdir();}
			if(!PluginFolder.exists()){PluginFolder.mkdir();}
			if(!ServerFolder.exists()){ServerFolder.mkdir();}
			if(!PropertyFile.exists()){PropertyFile.createNewFile();}
			if(!LogFile.exists()){LogFile.createNewFile();}
			
	    } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
 }
 
 public static void deleteAll(){
	 Folder.delete();
 }
 
 public static File getFileByName(String Name){
	 for(File e: Files){
		 if(e.getName().equals(Name)){
			 return e;
		 }
	 }
	 return null;
 }
 
 public static void setFileByName(String Name, File f){
	 for(File e: Files){
		 if(e.getName().equals(Name)){
			 Files.set(Files.indexOf(e), f);
		 }
	 }
 }
 
 public static boolean isInstalled(){
	 if(Folder.exists() && LogFile.exists() && PluginFolder.exists() && PropertyFile.exists() && ServerFolder.exists()){
		 return true;
	 }
	 else return false;
 }
}
