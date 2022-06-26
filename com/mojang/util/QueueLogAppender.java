/*    */ package com.mojang.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.locks.ReadWriteLock;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ import org.apache.logging.log4j.core.Filter;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ 
/*    */ 
/*    */ @Plugin(name = "Queue", category = "Core", elementType = "appender", printObject = true)
/*    */ public class QueueLogAppender
/*    */   extends AbstractAppender
/*    */ {
/*    */   private static final int MAX_CAPACITY = 250;
/* 25 */   private static final Map<String, BlockingQueue<String>> QUEUES = new HashMap<>();
/* 26 */   private static final ReadWriteLock QUEUE_LOCK = new ReentrantReadWriteLock();
/*    */   private final BlockingQueue<String> queue;
/*    */   
/*    */   public QueueLogAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, BlockingQueue<String> queue) {
/* 30 */     super(name, filter, layout, ignoreExceptions);
/* 31 */     this.queue = queue;
/*    */   }
/*    */   
/*    */   public void append(LogEvent event) {
/* 35 */     if (this.queue.size() >= 250) {
/* 36 */       this.queue.clear();
/*    */     }
/* 38 */     this.queue.add(getLayout().toSerializable(event).toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static QueueLogAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout2, @PluginElement("Filters") Filter filter) {
/* 47 */     boolean ignoreExceptions = Boolean.parseBoolean(ignore);
/* 48 */     if (name == null) {
/* 49 */       LOGGER.error("No name provided for QueueLogAppender");
/* 50 */       return null;
/*    */     } 
/* 52 */     QUEUE_LOCK.writeLock().lock();
/* 53 */     BlockingQueue<String> queue = QUEUES.get(name);
/* 54 */     if (queue == null) {
/* 55 */       queue = new LinkedBlockingQueue<>();
/* 56 */       QUEUES.put(name, queue);
/*    */     } 
/* 58 */     QUEUE_LOCK.writeLock().unlock();
/*    */     
/* 60 */     if (layout2 != null) return new QueueLogAppender(name, filter, layout2, ignoreExceptions, queue);
/*    */ 
/*    */ 
/*    */     
/* 64 */     return new QueueLogAppender(name, filter, layout2, ignoreExceptions, queue);
/*    */   }
/*    */   
/*    */   public static String getNextLogEvent(String queueName) {
/* 68 */     QUEUE_LOCK.readLock().lock();
/* 69 */     BlockingQueue<String> queue = QUEUES.get(queueName);
/* 70 */     QUEUE_LOCK.readLock().unlock();
/* 71 */     if (queue == null) return null; 
/*    */     try {
/* 73 */       return queue.take();
/* 74 */     } catch (InterruptedException interruptedException) {
/*    */ 
/*    */       
/* 77 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojan\\util\QueueLogAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */