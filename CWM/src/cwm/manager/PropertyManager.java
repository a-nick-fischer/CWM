package cwm.manager;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

import cwm.main.Main;

public class PropertyManager{
	
  private static Properties pt=new Properties();
 
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
	} catch (Throwable e) {
		Main.handleError(e,Thread.currentThread(),"ERROR:");
	}
	  
  }
  
  public static void setup(File file){
   try {
    Scanner sc=new Scanner(file);
     if(!sc.hasNextLine()){
    	sc.close();
	  PrintWriter  pr = new PrintWriter(file);
	  pt.store(pr," ");
	  pr.close();
     }else{sc.close();}
   } catch (Throwable e) {
		Main.handleError(e,Thread.currentThread(),"ERROR:");
	}
   }
}
