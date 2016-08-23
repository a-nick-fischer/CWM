package cwm.net.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
	private int Port;
	private ServerSocket Server;
	private boolean running;
	private LinkedList<Client> Clients=new LinkedList<>();
	private ExecutorService ExecServ;
	private int Mode;
  private LinkedList<InetAddress> Banned=new LinkedList<>();
  private String BannedMSG = "You are banned on this Server!";
	
	public Hoster(int Port, String Name, int ID, int Mode){
	  try{
		  try{
		    this.Name=Name;
		    this.ID=ID;
		    this.Port=Port;
			  this.Mode=Mode;
		    Server=new ServerSocket(Port);
		    ExecServ = ThreadManager.requestExecutor(Name, Mode);
		    ExecServ.submit(() -> lookup());
		  }catch(java.net.BindException e){
			  IOManager.print(Colors.YELLOW+"\nPort "+Port+" is already in use, choose an other\n");
		      return;
		  }
	  }catch(Throwable e){
		   Main.handleError(e, Thread.currentThread(), "ERROR:");
	  }
	}
	
	public Hoster(int Port, String Name, int ID){
		this(Port,Name,ID,-1);
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
				Socket Conn = Server.accept();
				Client Client=new Client(Conn,ID,Conn.getInetAddress().getHostAddress());
				Clients.add(Client);
				if(isBanned(Client.getSocket().getInetAddress())){
					Client.getWriter().println(BannedMSG); Client.disconnect();continue;
				}
			  ExecServ.submit(Client);
			}catch (Throwable e) {
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
	
	public int getPort(){
		return Port;
	}
	
	public int getMode(){
		return Mode;
	}
	
	public List<Client> getClients(){
		return Clients;
	}
	
	public ServerSocket getServerSocket(){
		return Server;
	}
	
	public String getBannedMSG(){
		return BannedMSG;
	}
	
	public LinkedList<InetAddress> getBannedAdresses(){
		return Banned;
	}
	
	public void broadcast(Client Sender,String msg){
		for(Client Client: Clients){
			try{
				if(msg.isEmpty() || msg.contains("[SERVER]")){
					break;
				}
				if(msg.contains(" ")){
					if(msg.split(" ").length==0){
						break;
					}
				}
				if(Sender==Client){
					continue;
				}
				String ToSend = String.format("[%s] %s\r\n",Sender.getName(),msg);
        Client.getWriter().printf(ToSend);
        Client.getWriter().flush();
			}catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
		}
	}

	public void broadcast(String msg){
		for(Client Client: Clients){
			try {
				  String ToSend = String.format("[SERVER] %s\r\n",msg);
          Client.getWriter().printf(ToSend);
          Client.getWriter().flush();
			} catch (Throwable e) {
				Main.handleError(e,Thread.currentThread(),"ERROR:");
			}
	  }
  }
	
  public void shutdown(){
    setRunning(false);
    for(Client Client: Clients){
    	Client.disconnect();
    }
    ThreadManager.shutdownExecutor(Name);
    try {
			Server.close();
    }catch (Throwable e) {
			Main.handleError(e,Thread.currentThread(),"ERROR:");
		}
  }
    
  public void ban(InetAddress addr){
    Banned.add(addr);
    for(int i=0;i<Clients.size();i++){
    	if(Clients.get(i).getSocket().getInetAddress().equals(addr)){
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
    BannedMSG = msg;
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