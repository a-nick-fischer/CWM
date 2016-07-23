package cwm.net.server;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cwm.cmd.Colors;
import cwm.main.Main;
import cwm.manager.IOManager;
import cwm.manager.ThreadManager;

public class Hoster {
	
	private String Name;
	private int ID;
	private ServerSocket Server;
	private boolean running;
	private LinkedList<Client> Clients=new LinkedList<>();
	private ExecutorService ExecServ;
	
    private LinkedList<InetAddress> Banned=new LinkedList<>();
    private String BannedMSG = "You are banned on this Server!";
	
	public Hoster(int Port, String Name, int ID){
	   try{
		try{
		  this.Name=Name;
		  this.ID=ID;
		  Server=new ServerSocket(Port);
		  ExecServ = ThreadManager.requestExecuter(Name, -1);
		  ExecServ.submit(() -> lookup());
		}catch(java.net.BindException e){
			IOManager.print(Colors.YELLOW+"\nPort "+Port+" is already in use, choose an other\n");
			Main.run();
		}
	   }catch(Throwable e){
		   Main.handleError(e, Thread.currentThread(), "ERROR:");
	   }
	}
	
	public synchronized void setRunning(boolean running){
		this.running=running;
	}
	
	public synchronized boolean isRunning(){
		return running;
	}
	
    private void lookup(){
		setRunning(true);
		while(isRunning()){
			try {
				Client Client=new Client(Server.accept(),ID);
				Clients.add(Client);
				if(isBanned(Client.getSocket().getInetAddress())){new PrintStream(Client.getSocket().getOutputStream()).println(BannedMSG);continue;}
			    ExecServ.submit(Client);
				
			} catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}			
		}
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return Name;
	}
	
	public List<Client> getClients(){
		return Clients;
	}
	
	public ServerSocket getServerSocket(){
		return Server;
	}
	
	public void broadcast(String msg){
	 String msgToSend = msg.replaceAll("\\[SERVER\\]", "");
	 
	 
	 ExecServ.submit(() -> {
		for(Client Client: Clients){
			try {
				PrintStream ps = new PrintStream(Client.getSocket().getOutputStream());
				ps.print(msgToSend);
				ps.close();
			} catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
		}
	 });
	}

    public void shutdown(){
    	setRunning(false);
    	for(Client Client: Clients){
    		Client.disconnect();
    	}
    	ThreadManager.shutdownExecuter(Name);
      
    	try {
			Server.close();
    	} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
    }
    
    public void ban(InetAddress addr){
    	Banned.add(addr);
    	 for(int i=0;i<Clients.size();i++){
    		 if(Clients.get(i).getSocket().getInetAddress()==addr){
    			Clients.get(i).disconnect();
    			 return;
    		 }
    	 }
    }
    
    public void disban(InetAddress addr){
    	Banned.remove(addr);
    }

    public boolean isBanned(InetAddress addr){
		return Banned.contains(addr);
    }

    public void setBannedMessage(String msg){
    	BannedMSG=msg;
    }

    public void kick(InetAddress addr){
    	for(int i=0;i<Clients.size();i++){
   		 if(Clients.get(i).getSocket().getInetAddress()==addr){
   			Clients.get(i).disconnect();
   			return;
   		 }
   	 }
    }
}
