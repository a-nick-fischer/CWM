package manager;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

public class PropertyManager{
	
  private static Properties pt=new Properties();
  private static File PropertyFile;
  
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
	  PropertyFile=file;
	try {
		fr = new FileReader(file);
		pt.load(fr);
		fr.close();
	} catch (Exception e) {
    //TODO
	}
	  
  }
  
  public static void setup(File file){
   try {
    Scanner sc=new Scanner(file);
     if(!sc.hasNextLine()){
    	sc.close();
	  PrintWriter  pr = new PrintWriter(file);
	  
	  pt.setProperty("GUI","false");
	  pt.setProperty("LOG", "true");
	  pt.setProperty("LAF","Nimbus");
	  
	  pt.store(pr,"");
	  pr.close();
     }else{sc.close();}
    }catch (Exception e) {
     //TODO
     }
   }
  
  public static File getPropertyFile(){
	return PropertyFile;
	  
  }  
}
