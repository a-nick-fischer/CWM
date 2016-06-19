package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import manager.CommunikationManager;
import manager.FileManager;
import manager.GUIManager;
import manager.IOManager;
import manager.PropertyManager;
import net.server.Hoster;

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
			    if(words.length==1){
				try {
					PropertyManager.getProperties().store(new PrintStream(FileManager.getPropertyFile()),"");
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
			    
			    case "pm-load":
			    if(words.length==1){
			    	PropertyManager.load(FileManager.getPropertyFile());
			    }else{
			    	PropertyManager.load(new File(words[1]));
			    }
			    break;
			    
			    case "pm-get":
				try {
					PropertyManager.getProperties().store(IOManager.getOutputStream(),"");
					PropertyManager.getProperties().store(new PrintStream(new FileOutputStream(FileManager.getLogFile())), "");
				} catch (Exception e) {
					//TODO
				}
			    break;
			    
			    case "fm-install":
			    	if(!FileManager.isInstalled()){
			    		 Main.installAndLoad();
			    	}else{ IOManager.print(Colors.YELLOW+"\nFiles are already installed!\n");}
			    break;
			    
			    case "fm-installed":
			    	IOManager.print(FileManager.isInstalled()? Colors.GREEN+"\n"+FileManager.isInstalled()+"\n" : Colors.RED+"\n"+FileManager.isInstalled()+"\n");
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
			    		IOManager.print(Colors.YELLOW+"\nPlease speciffy a server-name\n");
			    		continue;
			    	}
			    	
			    	if(words.length==2){
			    		IOManager.print(Colors.YELLOW+"\nPlease speciffy a text to send\n");
			    		continue;
			    	}
			    	
			    		StringBuilder b=new StringBuilder();
			    		for(int i=3;i<line.split(" ").length;i++){
			    	      b.append(line.split(" ")[1]);
			    		}
			    		
			    		try {
			    			 System.out.println(b.toString());
							CommunikationManager.getServerByName(words[1]).broadcast(b.toString());
						} catch (Exception e) {
							//TODO
						}
			    break;
			    
			    case "cm-host":
			        if(words.length<3){
			        	IOManager.print(Colors.YELLOW+"\nInvalid argument number\n");
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
			    
			    case "cm-connect":
			    	if(words.length<2){
			    		IOManager.print(Colors.YELLOW+"\nInvalid argument number\n");
			    	}
			    	else{
			    	  try{
			    		String IP=words[1].split(":")[0];
			    		int Port=Integer.parseInt(words[1].split(":")[1]);
			    		CommunikationManager.connect(IP,Port);
			    	  }catch(Exception e){
			    		  //TODO
			    	  }
			    	}
			    break;
			    
			    case "cm-info":
			    	
			    break;
			   }
			  
		  }
	  }
  } 
