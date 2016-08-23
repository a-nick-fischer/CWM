package cwm.cmd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import cwm.main.Main;
import cwm.manager.CommunikationManager;
import cwm.manager.FileManager;
import cwm.manager.GUIManager;
import cwm.manager.IOManager;
import cwm.manager.PropertyManager;
import cwm.net.server.Hoster;

public class CmdMethods {
	
  public static void SYS_EXIT(List<String> args){
      try{
         if(args.size()==2){System.exit(Integer.parseInt(args.get(1)));}
         else{System.exit(0);}
      }catch(NumberFormatException e){System.exit(0);}
  }
  
  
  public static void SYS_CALL(List<String> args){
      try{
        if(!args.get(1).contains(".") || !args.get(1).contains("(") || !args.get(1).contains(")")){
				  	IOManager.print(Colors.YELLOW+"\nInvalid syntax\n");return;
				}
      	String ClassName = args.get(1).substring(0, args.get(1).lastIndexOf("."));
      	String Method = args.get(1).substring(args.get(1).lastIndexOf(".")+1);
      	String MethodName = Method.substring(0, Method.indexOf("("));
        if(args.size()==3){
	          Object[] Arguments = Method.substring(Method.indexOf("(")+1,Method.indexOf(")")).contains(",")? 
		                                              Method.substring(Method.indexOf("(")+1,Method.indexOf(")")).split(",") : 
		                                              new String[]{Method.substring(Method.indexOf("(")+1,Method.indexOf(")"))};           
		                 
            Class<?>[] ArgumentTypes=new Class<?>[Arguments.length];
            String[] ArgumentTypeString = args.get(2).split("/");
            if(ArgumentTypeString.length != Arguments.length){
						     IOManager.print(Colors.YELLOW+"\nYou must declare the type for each argument\n");return;
					  }
            for(int j=0;j<ArgumentTypeString.length;j++){
			          ArgumentTypes[j] = Class.forName(ArgumentTypeString[j]);
            }    
            IOManager.print(Colors.RED+"\nRETURN VALUE: "+Class.forName(ClassName)
						                                                   .getMethod(MethodName, ArgumentTypes)
		  																												 .invoke(Class.forName(ClassName), Arguments));
			}else
      if(args.size()==2){
           IOManager.print(Colors.RED+"RETURN VALUE: "+Class.forName(ClassName).getMethod(MethodName).invoke(Class.forName(ClassName)));
      }
    }catch(Throwable e){
	     Main.handleError(e, Thread.currentThread(), "ERROR:");
    }
 }
  
  
  public static void SYS_HELP(List<String> args){
    IOManager.print(Colors.RESET+"==================== HELP ====================\n\n"
	                 +Colors.YELLOW+"Commands:\n"+Colors.RESET
                   +Colors.BLUE+" sys-exit <OPTIONAL EXIT VALUE> :"+Colors.RESET+" exits the app\n"
	                 +Colors.BLUE+" exit:"+Colors.RESET+" exits the app\n"
	                 +Colors.BLUE+" sys-help:"+Colors.RESET+" displays this help page\n"
	                 +Colors.BLUE+" help:"+Colors.RESET+" displays this help page\n"
	                 +Colors.BLUE+" sys-ansi-test:"+Colors.RESET+" test of colored printing\n"
	                 +Colors.BLUE+" sys-runtime-error-test <MSG>:"+Colors.RESET+" test of runtime error handling\n"
	                 +Colors.BLUE+" sys-checked-error-test <MSG>:"+Colors.RESET+" test of checked error handling\n"
	                 +Colors.BLUE+" sys-update <VERSION>:"+Colors.RESET+" updater, in deverlopment\n"
	                 +Colors.BLUE+" sys-call <METHOD> <DATATYPES>:"+Colors.RESET+" debug function, do not use!\n\n"
                   +Colors.BLUE+" pm-set <KEY> <VALUE>:"+Colors.RESET+" sets an property\n"
                   +Colors.BLUE+" pm-get:"+Colors.RESET+" prints all propertys from prog.conf to screen\n"
                   +Colors.BLUE+" pm-store <OPTIONAL DIST FILE>:"+Colors.RESET+" stores all propertys to a file\n"
                   +Colors.BLUE+" pm-load <OPTIONAL DIST FILE>:"+Colors.RESET+" loads all propertys from a file\n\n"
                   +Colors.BLUE+" fm-install:"+Colors.RESET+" installs and repairs all files\n"
                   +Colors.BLUE+" fm-installed:"+Colors.RESET+" checks if all files are installed\n"
                   +Colors.BLUE+" fm-delete-all:"+Colors.RESET+" deletes all cwm-files\n\n"
                   +Colors.BLUE+" gm-start:"+Colors.RESET+" starts the GUI, in deverlopment\n\n"
                   +Colors.BLUE+" cm-host <SERVERNAME> <PORT>:"+Colors.RESET+" hosts an chat-server\n"
                   +Colors.BLUE+" cm-connect <IP>:<PORT>:"+Colors.RESET+" connects to a chat server\n"
                   +Colors.BLUE+" cm-shutdown <OPTIONAL SERVERNAME>:"+Colors.RESET+" shuts all or the given server down\n"
                   +Colors.BLUE+" cm-broadcast <SERVERNAME> <MSG>:"+Colors.RESET+" broadcasts the message to the server\n"
                   +Colors.BLUE+" cm-ban <SERVERNAME> <IP TO BAN>:"+Colors.RESET+" bans a speciffic ip from the server\n"
                   +Colors.BLUE+" cm-disban <SERVERNAME> <IP TO BAN>:"+Colors.RESET+" disbans a speciffic ip from the server\n"
                   +Colors.BLUE+" cm-info:"+Colors.RESET+" displays information about all hosted servers\n"
                   +Colors.BLUE+" cm-save-server <SERVERNAME>:"+Colors.RESET+" saves server properties to file\n"
                   +Colors.BLUE+" cm-load-server <SERVERNAME>:"+Colors.RESET+" loads server properties from file\n"
                   +"=============================================="
	                );
  }
  
  
  public static void SYS_ANSI_TEST(List<String> args){
	  PrintStream IO = new PrintStream(IOManager.getOutputStream());
	  IO.print("================== ANSI TEST =================\n\n");
  	IO.print("TEXT:\n");
	  IO.print(Colors.RED+"RED "+Colors.GREEN+"GREEN "+Colors.BLUE+"BLUE "+Colors.CYAN+"CYAN "+Colors.PURPLE+"PURPLE "+Colors.YELLOW+"YELLOW "+Colors.WHITE+"WHITE "+Colors.BLACK+"BLACK "+Colors.RESET+"\n\n");
  	IO.print("BACKGROUND:\n");
  	IO.print(Colors.RED_BACK+"RED "+Colors.GREEN_BACK+"GREEN "+Colors.BLUE_BACK+"BLUE "+Colors.CYAN_BACK+"CYAN "+Colors.PURPLE_BACK+"PURPLE "+Colors.YELLOW_BACK+"YELLOW "+Colors.WHITE_BACK+"WHITE "+Colors.BLACK_BACK+"BLACK "+Colors.RESET+"\n\n");
  	IO.print("==============================================\n\n");
	}
  
  
  public static void SYS_RUNTIME_ERROR_TEST(List<String> args){
	  if(args.size()>=2){throw new RuntimeException(args.get(1));}
    else throw new RuntimeException();
  }
  
  
  public static void SYS_CHECKED_ERROR_TEST(List<String> args){
	  try{
    	  if(args.size()>=2){throw new Exception(args.get(1));}
    	  else throw new Exception();
    }catch(Exception e){
      	Main.handleError(e, Thread.currentThread(), "ERROR:");
    }
  }
  
  
  public static void SYS_UPDATE(List<String> args){
	  IOManager.print(Colors.YELLOW+"\nThe updater is under deverlopment\n");
//	  try{
//	    	 URL url = new URL ("ftp://user:cwm@81.217.188.227/update/"+args.get(1)+".zip");
//	    	 URLConnection urlc = url.openConnection();
//	    	 @SuppressWarnings("resource")
//			Scanner sc = new Scanner(urlc.getInputStream());
//	    	 File out = new File(args.get(1)+".zip");
//	    	 out.createNewFile();
//	    	 @SuppressWarnings("resource")
//			PrintWriter pw = new PrintWriter(out);
//	    	 
//	    	 while(sc.hasNextLine()){
//	    		 pw.println(sc.nextLine());
//	    	 }
//	    	 
//	    	}catch(Exception e){
//	    		Main.handleError(e, Thread.currentThread(), "ERROR:");
//	    	}
  }
  
  
  public static void PM_SET(List<String> args){
	  PropertyManager.setProperty(args.get(1), args.get(2));
  }
  
  
  public static void PM_GET(List<String> args){
	  try {
			PropertyManager.getProperties().store(IOManager.getOutputStream(),"");
			  if(PropertyManager.getProperty("LOG").equals("true") && FileManager.isInstalled()){
		  	PropertyManager.getProperties().store(new PrintStream(new FileOutputStream(FileManager.getFileByName("Cwm.log"))), "");
			}
		}catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
  }
 
  
  public static void PM_STORE(List<String> args){
	  if(args.size()==1){
	    try {
		    PropertyManager.getProperties().store(new PrintStream(FileManager.getFileByName("Prog.conf")),"");
		  }catch (Throwable e) {
		  	Main.handleError(e,Thread.currentThread(),"ERROR:");
		  }
		}else{
			try {
			  PropertyManager.getProperties().store(new PrintStream(new File(args.get(1))),"");
			}catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
		}
  }
  
  
  public static void PM_LOAD(List<String> args){
	  if(args.size()==1){
	    PropertyManager.load(FileManager.getFileByName("Prog.conf"));
	  }else{
	    PropertyManager.load(new File(args.get(1)));
	  }
  }
  
  
  public static void FM_INSTALL(List<String> args){
  	if(!FileManager.isInstalled()){
 		  FileManager.install();
 		  PropertyManager.setup(FileManager.getFileByName("Prog.conf"));
 	  }else{ 
 	  	IOManager.print(Colors.YELLOW+"\nFiles are already installed!\n");
		}
  }
 
  
  public static void FM_INSTALLED(List<String> args){
	  IOManager.print(FileManager.isInstalled()? 
		Colors.GREEN+"\ntrue\n" :
	  Colors.RED+"\nfalse\n");
  }

  
  public static void FM_DELETE_ALL(List<String> args){
	  FileManager.deleteAll();
  }
  
  
  public static void GM_START(List<String> args){
	  GUIManager.start();
  }
  
  
  public static void CM_HOST(List<String> args){
    try{
       String name = args.get(1);
       if(name.length()<4){IOManager.print(Colors.YELLOW+"\nName must be at least 4 characters long\n");return;}else
       if(name.length()>7){IOManager.print(Colors.YELLOW+"\nName must be at max. 7 characters long\n");return;}
       int port = Integer.parseInt(args.get(2));
       if(args.size()==3){
         CommunikationManager.host(port, name);
       }else{
         int mode = -1;
         try{
           mode=Integer.parseInt(args.get(3));
         }catch(NumberFormatException e){
    	     IOManager.print(Colors.YELLOW+"\nEnter a number as third argument\n");
    	     Main.run();
         }
         CommunikationManager.host(port, name,mode);
     }
    }catch (Throwable e) {
	    Main.handleError(e,Thread.currentThread(),"ERROR:");
    }
  }
 
  
  public static void CM_CONNECT(List<String> args){
	  if(args.size()<2){
  	  IOManager.print(Colors.YELLOW+"\nInvalid argument number\n");
  	}else{
  	  try{
  		  String IP=args.get(1).split(":")[0];
  		  int Port=Integer.parseInt(args.get(1).split(":")[1]);
  	  	CommunikationManager.connect(IP,Port);
  	  }catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
  	  }
  	}
  }
 
  
  public static void CM_SHUTDOWN(List<String> args){
	  try{
			 if(args.size()==1){
			   for(Hoster Server: CommunikationManager.getServerList()){
					 Server.shutdown();
				 }
			 }else{
			   CommunikationManager.getServerByName(args.get(1)).shutdown();
			 }
			
		}catch (Throwable e) {
		  Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
  }

  
  public static void CM_BROADCAST(List<String> args){
    StringBuilder b=new StringBuilder("");
    for(int i=2;i<args.size();i++){
  	  b.append(" "+args.get(i));
  	}
  	try{
		  CommunikationManager.getServerByName(args.get(1)).broadcast(b.toString());
  	}catch (Throwable e) {
		  Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
  }
 
  
  public static void CM_BAN(List<String> args){
	  try{
	     CommunikationManager.getServerByName(args.get(1)).ban(InetAddress.getByName(args.get(2)));
	  }catch (Exception e) {
	     Main.handleError(e, Thread.currentThread(), "ERROR:");
	  }
  }
 
  
  public static void CM_DISBAN(List<String> args){
	  try{
			CommunikationManager.getServerByName(args.get(1)).disban(InetAddress.getByName(args.get(2)));
		}catch (Exception e) {
			Main.handleError(e, Thread.currentThread(), "ERROR:");
		}
  }

  
  public static void CM_INFO(List<String> args){
	  IOManager.print(Colors.BLUE+"\n     Name     ID     Clients     Port\n");
  	for(Hoster H: CommunikationManager.getServerList()){
      String Color = H.getServerSocket().isClosed()? Colors.RED : Colors.GREEN ; 
        IOManager.print(Color + "     "+
				                H.getName() + "     "+
												H.getID()+"         "+
												H.getClients().size()+"        "+
												H.getServerSocket().getLocalPort()+"\n"
											 );
  	}
  	IOManager.print("\n");
  }

  
  public static void CM_SAVE_SERVER(List<String> args){
	  Hoster h = null;
	  if((h=CommunikationManager.getServerByName(args.get(1)))!=null){
	    PropertyManager.saveServer(h);
	  }else{
		  IOManager.print(Colors.YELLOW+"\nServer not found!\n");
	  }
  }

  
  public static void CM_LOAD_SERVER(List<String> args){
	  String Path = FileManager.getFileByName("Server")+File.separator+args.get(1)+".conf";
	  File f = new File(Path);
	  if(f.isFile()){
		  PropertyManager.loadServer(f);
	  }else{
		  IOManager.print(Colors.YELLOW+"\nServer not found!\n");
	  }
  }
  
  
  public static void MY_NAME_IS(List<String> args){
	  try{
		  IOManager.print(Colors.YELLOW+"          Bond, ");
		  InputStream defaultSound =  new CmdMethods().getClass().getResourceAsStream("/cwm/data/sound/JB.wav");
		  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(defaultSound));
		  Clip clip = AudioSystem.getClip();
		  clip.open(audioInputStream);
		  clip.start();
		  Thread.sleep(1000);
		  IOManager.print(Colors.YELLOW+"James Bond\n");
	  }catch (Exception e) {
		  Main.handleError(e, Thread.currentThread(), "ERROR:");
	  }
  }
}
