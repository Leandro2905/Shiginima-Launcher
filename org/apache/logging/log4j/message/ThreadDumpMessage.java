/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadDumpMessage
/*     */   implements Message
/*     */ {
/*     */   private static final long serialVersionUID = -1103400781608841088L;
/*     */   private static final ThreadInfoFactory FACTORY;
/*     */   private volatile Map<ThreadInformation, StackTraceElement[]> threads;
/*     */   private final String title;
/*     */   private String formattedMessage;
/*     */   
/*     */   static {
/*  47 */     Method[] methods = ThreadInfo.class.getMethods();
/*  48 */     boolean basic = true;
/*  49 */     for (Method method : methods) {
/*  50 */       if (method.getName().equals("getLockInfo")) {
/*  51 */         basic = false;
/*     */         break;
/*     */       } 
/*     */     } 
/*  55 */     FACTORY = basic ? new BasicThreadInfoFactory() : new ExtendedThreadInfoFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadDumpMessage(String title) {
/*  63 */     this.title = (title == null) ? "" : title;
/*  64 */     this.threads = FACTORY.createThreadInfo();
/*     */   }
/*     */   
/*     */   private ThreadDumpMessage(String formattedMsg, String title) {
/*  68 */     this.formattedMessage = formattedMsg;
/*  69 */     this.title = (title == null) ? "" : title;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  74 */     StringBuilder sb = new StringBuilder("ThreadDumpMessage[");
/*  75 */     if (this.title.length() > 0) {
/*  76 */       sb.append("Title=\"").append(this.title).append('"');
/*     */     }
/*  78 */     sb.append(']');
/*  79 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  88 */     if (this.formattedMessage != null) {
/*  89 */       return this.formattedMessage;
/*     */     }
/*  91 */     StringBuilder sb = new StringBuilder(this.title);
/*  92 */     if (this.title.length() > 0) {
/*  93 */       sb.append('\n');
/*     */     }
/*  95 */     for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
/*  96 */       ThreadInformation info = entry.getKey();
/*  97 */       info.printThreadInfo(sb);
/*  98 */       info.printStack(sb, entry.getValue());
/*  99 */       sb.append('\n');
/*     */     } 
/* 101 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 110 */     return (this.title == null) ? "" : this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 128 */     return new ThreadDumpMessageProxy(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 133 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ThreadDumpMessageProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3476620450287648269L;
/*     */     
/*     */     private final String formattedMsg;
/*     */     private final String title;
/*     */     
/*     */     public ThreadDumpMessageProxy(ThreadDumpMessage msg) {
/* 146 */       this.formattedMsg = msg.getFormattedMessage();
/* 147 */       this.title = msg.title;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 155 */       return new ThreadDumpMessage(this.formattedMsg, this.title);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface ThreadInfoFactory
/*     */   {
/*     */     Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BasicThreadInfoFactory
/*     */     implements ThreadInfoFactory
/*     */   {
/*     */     private BasicThreadInfoFactory() {}
/*     */     
/*     */     public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 172 */       Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
/* 173 */       Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<ThreadInformation, StackTraceElement>(map.size());
/*     */       
/* 175 */       for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
/* 176 */         threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
/*     */       }
/* 178 */       return threads;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ExtendedThreadInfoFactory
/*     */     implements ThreadInfoFactory
/*     */   {
/*     */     private ExtendedThreadInfoFactory() {}
/*     */     
/*     */     public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 188 */       ThreadMXBean bean = ManagementFactory.getThreadMXBean();
/* 189 */       ThreadInfo[] array = bean.dumpAllThreads(true, true);
/*     */       
/* 191 */       Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<ThreadInformation, StackTraceElement>(array.length);
/*     */       
/* 193 */       for (ThreadInfo info : array) {
/* 194 */         threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
/*     */       }
/* 196 */       return threads;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 207 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\ThreadDumpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */