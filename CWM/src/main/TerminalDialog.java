package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import manager.CommunikationManager;
import manager.FileManager;
import manager.GUIManager;
import manager.IOManager;
import manager.PropertyManager;
import net.Hoster;

public class TerminalDialog{
	
	private static boolean isRunning;
	
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
			  
			  String line=IOManager.nextLine();
			  String[] words=line.split(" ");
			  
			   switch(words[0]){
			   
			    case "exit":
			    try{
			      if(words.length==2){System.exit(Integer.parseInt(words[1]));}
			      else{System.exit(0);}
			    }catch(NumberFormatException e){System.exit(0);}
			    break;
			    
			    case "pm-set":
			    	PropertyManager.setProperty(words[1], words[2]);
			    break;
			   
			    case "pm-store":
			    if(line.split(" ").length==1){
				try {
					PropertyManager.getProperties().store(new PrintStream(PropertyManager.getPropertyFile()),"");
				} catch (Exception e) {
					//TODO
				}
				
				}else{
					try {
						PropertyManager.getProperties().store(new PrintStream(new File(words[1])),"");
					} catch (Exception e) {
						//TODO
					}
				}
			    break;
			    
			    case "pm-print":
				try {
					PropertyManager.getProperties().store(IOManager.getOutputStream(),"");
				} catch (Exception e) {
					//TODO
				}
			    break;
			    
			    case "fm-install":
			    	if(!FileManager.isInstalled()){
			    		 Main.installAndLoad();
			    	}else{ IOManager.errprint("[CWM] Files are already installed!");;}
			    break;
			    
			    case "fm-is_installed":
			    	IOManager.print("[CWM] "+FileManager.isInstalled());
			    break;
			    
			    case "gm-start":
			    	GUIManager.start();
			    break;
			    
			    case "cm-shutdown":
				try {
					if(line.split(" ").length==1){
						for(Hoster Server: (Hoster[]) CommunikationManager.getServerList().toArray()){
							Server.shutdown();
						}
					}
					else{
						CommunikationManager.getServerByName(line.split(" ")[1]).shutdown();
					}
					
				} catch (Exception e) {
					//TODO
				}
			    break;
			    
			    case "cm-broadcast":
			    	if(words.length==1){
			    		IOManager.errprint("[CWM] Please speciffy a server-name");
			    		continue;
			    	}
			    	
			    	if(words.length==2){
			    		IOManager.errprint("[CWM] Please speciffy a text to send");
			    		continue;
			    	}
			    	
			    		StringBuilder b=new StringBuilder();
			    		for(int i=2;i<line.split(" ").length;i++){
			    	      b.append(line.split(" ")[1]);
			    		}
			    		
			    		try {
							CommunikationManager.getServerByName(words[2]).broadcast(b.toString());
						} catch (IOException e) {
							//TODO
						}
			    break;
			    
			    case "cm-host":
			        if(words.length<3){
			        	IOManager.errprint("[CWM] Invalid argument number");
			        }else{
			            try{
			            	String name = words[1];
			            	int port = Integer.parseInt(words[2]);
			            	CommunikationManager.host(port, name);
			            }catch(Exception e){
			            	//TODO
			            }
			        }
			    break;
			   }
			  
		  }
	  }
  } 
