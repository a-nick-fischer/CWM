package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import manager.IOManager;
import manager.PropertyManager;
import net.Hoster;

public class TerminalDialog{
	
	private static boolean isRunning;
	private static  LinkedList<String> CommandLog=new LinkedList<String>();
	
  public static void setRunning(boolean isRunning){
	TerminalDialog.isRunning=isRunning;
  }

  public static boolean isRunning(){
	  return isRunning;
  }

  
  public static void run() {
	setRunning(true);
	  while(isRunning){
		  
		 IOManager.print("\n>");
		  
		  while(IOManager.hasNextLine()){
			  
			  String line=IOManager.nextLine();
			  String[] words=line.split(" ");
			  CommandLog.add(line);
			  
			   switch(words[0]){
			   
			    case "exit":
			    try{
			      if(words.length==2){System.exit(Integer.parseInt(words[1]));}
			      else{System.exit(0);}
			    }catch(NumberFormatException e){System.exit(0);}
			    break;
			    
			    case "pm-set":
			    	if(line.length()!=3){break;}
			    	else{PropertyManager.setProperty(line.split(" ")[1], line.split(" ")[2]);}
			    break;
			    
			    case "pm-get":
			    	if(line.length()!=2){break;}
			    	else{/*new PrintWriter(Main.getOutputStream()).println(Main.getPropertyManager().getProperty(line.split(" ")[1]));*/ IOManager.println(PropertyManager.getProperty());}
			    break;
			   
			    case "pm-store":
			    if(line.split(" ").length==1){
				try {
					Main.getPropertyManager().getProperties().store(new PrintWriter(Main.getPropertyManager().getPropertyFile()),"");
				} catch (Exception e) {
					try {
						e.printStackTrace(new PrintWriter(new FileWriter(Main.getFileManager().getLogFile()),true));
						run();
					} catch (Exception e1) {
						e1.printStackTrace();
					    run();
					}
					run();
				}
				}else{
					try {
						Main.getPropertyManager().getProperties().store(new PrintWriter(new File(line.split(" ")[1])),"");
					} catch (Exception e) {
						try {
							e.printStackTrace(new PrintWriter(new FileWriter(Main.getFileManager().getLogFile()),true));
							run();
						} catch (Exception e1) {
							e1.printStackTrace();
						    run();
						}
						run();
					}
				}
			    break;
			    
			    case "pm-print":
				try {
					Main.getPropertyManager().getProperties().store(new PrintWriter(Main.getOutputStream()),"");
				} catch (Exception e) {
					try {
						e.printStackTrace(new PrintWriter(new FileWriter(Main.getFileManager().getLogFile()),true));
						run();
					} catch (Exception e1) {
						e1.printStackTrace();
					    run();
					}
					run();
				}
			    break;
			    
			    case "fm-install":
			    	if(!Main.isInstalled()){
			    		 Main.setup();
			    	}else{ new PrintWriter(Main.getOutputStream()).print("[CWM] Files are already installed!");;}
			    break;
			    
			    case "fm-is_installed":
			    	new PrintWriter(Main.getOutputStream()).print("[CWM] Main: "+Main.isInstalled()+"\n    FileManager: "+Main.getFileManager().isInstalled());
			    break;
			    
			    case "gm-start":
			    	if(Main.isInstalled()){
			    		Main.getGUIManager().start();
			    	}else{
			    		new PrintWriter(Main.getOutputStream()).print("[CWM] First install files!");
			    	}
			    break;
			    
			    case "cm-shutdown":
				try {
					if(line.split(" ").length==1){
						for(Hoster Server: (Hoster[]) Main.getCommunikationManager().getServerList().toArray()){
							Server.shutdown();
						}
					}
					else{
						Main.getCommunikationManager().getServerByName(line.split(" ")[1]).shutdown();
					}
					
				} catch (IOException e) {
					e.printStackTrace(new PrintWriter(Main.getErrorStream()));
				}
			    break;
			    
			    case "cm-broadcast":
			    	if(line.split(" ").length==1){
			    		new PrintWriter(Main.getOutputStream()).println("[CWM] Please speciffy a server-name");
			    		continue;
			    	}
			    	
			    	if(line.split(" ").length==2){
			    		new PrintWriter(Main.getOutputStream()).println("[CWM] Please speciffy a text to send");
			    		continue;
			    	}
			    	
			    		StringBuilder b=new StringBuilder();
			    		for(int i=2;i<line.split(" ").length;i++){
			    	      b.append(line.split(" ")[1]);
			    		}
			    		
			    		try {
							Main.getCommunikationManager().getServerByName(line.split(" ")[2]).broadcast(b.toString());
						} catch (IOException e) {
							e.printStackTrace(new PrintWriter(Main.getErrorStream()));
			    	}
			    break;
			    
			    case "cm-host":
			    	if(line.split(" ").length!=3){new PrintWriter(Main.getErrorStream()).println("[CWM] Invalid argument number");}
			    	
			    break;
			   }
		  }
	  }
  }
  
  public static String[] getLog(){
	  return (String[]) CommandLog.toArray();
  }

  
}
