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
	
	private static boolean running;
	
  public static void setRunning(boolean running){
	TerminalDialog.running=running;
  }

  public static boolean isRunning(){
	  return running;
  }
  
  
public static void switchInput(String line){
	String[] words = line.split(" ");
	  switch(words[0]){
	   
	    case "exit":
	    case "sys-exit":
	    try{
	      if(words.length==2){System.exit(Integer.parseInt(words[1]));}
	      else{System.exit(0);}
	    }catch(NumberFormatException e){System.exit(0);}
	    break;
	    
	    case "sys-call":
	    	if(words.length<2){IOManager.print(Colors.YELLOW+"\nInvalid argument number\n");break;}
	    	if(!words[1].contains(".") || !words[1].contains("(") || !words[1].contains(")")){IOManager.print(Colors.YELLOW+"\nInvalid syntax\n");break;}
	    	
	     
	    	String ClassName = words[1].substring(0, words[1].lastIndexOf("."));
	    	String Method = words[1].substring(words[1].lastIndexOf(".")+1);
	    	String MethodName = Method.substring(0, Method.indexOf("("));
	    	
	   if(words.length==3){
	    	Object[] Arguments = Method.substring(Method.indexOf("(")+1,Method.indexOf(")")).contains(",")? 
	    		                 Method.substring(Method.indexOf("(")+1,Method.indexOf(")")).split(",") : 
	    		                 new String[]{Method.substring(Method.indexOf("(")+1,Method.indexOf(")"))};
	    		                 
	    		                 
	       Class<?>[] ArgumentTypes=new Class<?>[Arguments.length];
	       String[] ArgumentTypeString = words[2].split("/");
	       
	       if(ArgumentTypeString.length != Arguments.length){IOManager.print(Colors.YELLOW+"\nYou must declare the type for each argument\n");break;}
	       
	         for(int j=0;j<ArgumentTypeString.length;j++){
	        	 try {
					ArgumentTypes[j] = Class.forName(ArgumentTypeString[j]);
				} catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
				}
	         }
	   
            
	    try{                
	        IOManager.print(Colors.RED+"RETURN VALUE: "+Class.forName(ClassName).getMethod(MethodName, ArgumentTypes).invoke(Class.forName(ClassName), Arguments));
	    } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
	  }
	  if(words.length==2){
	   try{
		   IOManager.print(Colors.RED+"RETURN VALUE: "+Class.forName(ClassName).getMethod(MethodName).invoke(Class.forName(ClassName)));
	   } catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
	  }
	    break;
	    
	    case "pm-set":
	    	PropertyManager.setProperty(words[1], words[2]);
	    break;
	   
	    case "pm-store":
	    if(words.length==1){
		try {
			PropertyManager.getProperties().store(new PrintStream(FileManager.getPropertyFile()),"");
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
		
		}else{
			try {
				PropertyManager.getProperties().store(new PrintStream(new File(words[1])),"");
			} catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
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
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
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
			if(words.length==1){
				for(Hoster Server: CommunikationManager.getServerList()){
					Server.shutdown();
				}
			}
			else{
				CommunikationManager.getServerByName(words[1]).shutdown();
			}
			
		} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
	    break;
	    
	    case "cm-broadcast":
	    	if(words.length==1){
	    		IOManager.print(Colors.YELLOW+"\nPlease speciffy a server-name\n");
	    		break;
	    	}
	    	
	    	if(words.length==2){
	    		IOManager.print(Colors.YELLOW+"\nPlease speciffy a text to send\n");
	    		break;
	    	}
	    	
	    		StringBuilder b=new StringBuilder("[SERVER] ");
	    		for(int i=2;i<words.length;i++){
	    	      b.append(words[i]);
	    		}
	    		
	    		try {
					CommunikationManager.getServerByName(words[1]).broadcast(b.toString());
	    		} catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
				}
	    break;
	    
	    case "cm-host":
	        if(words.length<3){
	        	IOManager.print(Colors.YELLOW+"\nInvalid argument number\n");
	        }else{
	            try{
	            	String name = words[1];
	            if(name.length()<4){IOManager.print(Colors.YELLOW+"\nName must be at least 4 characters long\n");break;}
	            if(name.length()>7){IOManager.print(Colors.YELLOW+"\nName must be at max. 7 characters long\n");break;}
	            	int port = Integer.parseInt(words[2]);
	            	CommunikationManager.host(port, name);
	            } catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
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
	    	  } catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
			  }
	    	}
	    break;
	    
	    case "cm-info":
	    	IOManager.print(Colors.BLUE+"\n     Name     ID     Clients     Port\n");
	    	
	    	for(Hoster H: CommunikationManager.getServerList()){
	    		
	        String Color = H.getServerSocket().isClosed()? Colors.RED : Colors.GREEN ; 
	         
	          IOManager.print(Color + "     "+H.getName() + "     "+H.getID()+"         "+H.getClients().size()+"        "+H.getServerSocket().getLocalPort()+"\n");
	    	}
	    	
	    	IOManager.print("\n");
	    break;
	    
	    case "sys-ansi-test":
	    	PrintStream IO = new PrintStream(IOManager.getOutputStream());
	    	IO.print("===================ANSI TEST==================\n\n");
	    	IO.print("TEXT:\n");
	    	IO.print(Colors.RED+"RED "+Colors.GREEN+"GREEN "+Colors.BLUE+"BLUE "+Colors.CYAN+"CYAN "+Colors.PURPLE+"PURPLE "+Colors.YELLOW+"YELLOW "+Colors.WHITE+"WHITE "+Colors.BLACK+"BLACK "+Colors.RESET+"\n\n");
	    	IO.print("BACKGROUND:\n");
	    	IO.print(Colors.RED_BACK+"RED "+Colors.GREEN_BACK+"GREEN "+Colors.BLUE_BACK+"BLUE "+Colors.CYAN_BACK+"CYAN "+Colors.PURPLE_BACK+"PURPLE "+Colors.YELLOW_BACK+"YELLOW "+Colors.WHITE_BACK+"WHITE "+Colors.BLACK_BACK+"BLACK "+Colors.RESET+"\n\n");
	    	IO.print("==============================================\n\n");
	    break;
	   }
	 }
  
  public static void run() {
	  setRunning(true);
	  
	   while(running){
		 IOManager.print("\n>");
		 switchInput(IOManager.nextLine());	    
	   }
    }
  }
