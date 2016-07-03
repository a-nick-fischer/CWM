package manager;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
	
	private static ExecutorService mainExecutor;
	private static HashMap<String,ExecutorService> executorMap = new HashMap<>();
	private static int threadCount = -1;
	
 public static int getThreadCount(){
	 return threadCount;
 }
 
 public static void addTask(Runnable r){
	mainExecutor.submit(r);
 }
 
 public static void initMainExecutor(int threadCount){
	 ThreadManager.threadCount = threadCount;
	 
	 if(threadCount==1){
		 mainExecutor = Executors.newSingleThreadExecutor();
	 }
	 else if(threadCount==-1){
		 mainExecutor = Executors.newCachedThreadPool();
	 }
	 else if(threadCount==-2){
		 mainExecutor = Executors.newWorkStealingPool();
	 }
	 else{
		 mainExecutor = Executors.newFixedThreadPool(threadCount);
	 }
 }

 public static ExecutorService getMainExecutor(){
	 return mainExecutor;
 }
 
 public static HashMap<String,ExecutorService> getAllExecutors(){
	 return executorMap;
 }
 
 public static ExecutorService requestExecuter(String Name,int threadCount){
	 ExecutorService executer = null; //Beware of null-pointers
	 if(threadCount==1){
		 mainExecutor = Executors.newSingleThreadExecutor();
	 }
	 else if(threadCount==-1){
		 mainExecutor = Executors.newCachedThreadPool();
	 }
	 else if(threadCount==-2){
		 mainExecutor = Executors.newWorkStealingPool();
	 }
	 else{
		 mainExecutor = Executors.newFixedThreadPool(threadCount);
	 }
	 executorMap.put(Name, executer);
	 return executer;
	 
 }

 public static ExecutorService getExecuterByName(String Name){
	 return executorMap.get(Name);
 }
 
 public static void shutdownExecuter(String Name){
	 ExecutorService old = executorMap.remove(Name);
	 old.shutdownNow();
 }
 
 

}
