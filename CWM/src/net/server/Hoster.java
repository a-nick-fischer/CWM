package net.server;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import main.Main;
import manager.ThreadManager;

public class Hoster {
	
	private String Name;
	private int ID;
	private ServerSocket Server;
	private boolean running;
	private LinkedList<Client> Clients=new LinkedList<>();
	private ExecutorService ExecServ;
	
    private LinkedList<InetAddress> Banned=new LinkedList<>();
    private String BannedMSG = "You are banned on this Server!";
	
	public Hoster(int Port, String Name, int ID) throws Exception{
		this.Name=Name;
		this.ID=ID;
		Server=new ServerSocket(Port);
		ExecServ = ThreadManager.requestExecuter(Name, -1);
		ExecServ.submit(() -> lookup());
	}
	
	private void lookup(){
		running=true;
		loop:
		while(running){
			try {
				Client Client=new Client(Server.accept(),ID);
				Clients.add(Client);
				 
				  for(int i=0;i<Banned.size();i++){
					  if(Client.getSocket().getInetAddress().equals(Banned.get(i))){
						  new PrintStream(Client.getSocket().getOutputStream()).println(BannedMSG);
						  continue loop;
					  }
				  }
				
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
	 ExecServ.submit(() -> {
		for(Client Client: Clients){
			try {
				new PrintStream(Client.getSocket().getOutputStream()).print("\n"+msg+"\n");
			} catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
		}
	 });
	}

    public void shutdown(){
    	running=false;
    	ThreadManager.shutdownExecuter(Name);
    	
    	for(Client Client: Clients){
    		Client.setRunning(false);
    	}
      
    	try {
			Server.close();
    	} catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
    }
    
    public void ban(InetAddress addr){
    	Banned.add(addr);
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
}
