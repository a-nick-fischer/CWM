package cwm.manager;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
	
	private static ExecutorService mainExecutor;
	private static HashMap<String,ExecutorService> executorMap = new HashMap<>();
	private static int Mode = -1;
	
  public static int getMainExecutorMode(){
	  return Mode;
  }
 
  public static void addTask(Runnable r){
	  mainExecutor.submit(r);
  }
 
  private static ExecutorService getExecutorTypeByMode(int Mode){
	  if(Mode==1){
		  return Executors.newSingleThreadExecutor();
	  }
	  else if(Mode==-1){
		  return Executors.newCachedThreadPool();
	  }
	  else if(Mode==-2){
		  return Executors.newWorkStealingPool();
	  }
	  else{
		  return Executors.newFixedThreadPool(Mode);
	  }
  }
 
  public static void initMainExecutor(int Mode){
	ThreadManager.Mode = Mode;
    mainExecutor = getExecutorTypeByMode(Mode);
  }

  public static ExecutorService getMainExecutor(){
	  return mainExecutor;
  }
 
  public static HashMap<String,ExecutorService> getAllExecutors(){
	  return executorMap;
  }
 
  public static ExecutorService requestExecutor(String Name,int Mode){
	  ExecutorService executor = getExecutorTypeByMode(Mode);
	  executorMap.put(Name, executor);
	  return executor;
  }

  public static ExecutorService getExecutorByName(String Name){
 	  return executorMap.get(Name);
  }
 
  public static void shutdownExecutor(String Name){
	  ExecutorService old = executorMap.remove(Name);
	  old.shutdownNow();
  }
}