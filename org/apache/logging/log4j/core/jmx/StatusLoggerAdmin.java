/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcasterSupport;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.status.StatusData;
/*     */ import org.apache.logging.log4j.status.StatusListener;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class StatusLoggerAdmin
/*     */   extends NotificationBroadcasterSupport
/*     */   implements StatusListener, StatusLoggerAdminMBean
/*     */ {
/*  39 */   private final AtomicLong sequenceNo = new AtomicLong();
/*     */   private final ObjectName objectName;
/*     */   private final String contextName;
/*  42 */   private Level level = Level.WARN;
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
/*     */   public StatusLoggerAdmin(String contextName, Executor executor) {
/*  58 */     super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
/*  59 */     this.contextName = contextName;
/*     */     try {
/*  61 */       String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { Server.escape(contextName) });
/*  62 */       this.objectName = new ObjectName(mbeanName);
/*  63 */     } catch (Exception e) {
/*  64 */       throw new IllegalStateException(e);
/*     */     } 
/*  66 */     StatusLogger.getLogger().registerListener(this);
/*     */   }
/*     */   
/*     */   private static MBeanNotificationInfo createNotificationInfo() {
/*  70 */     String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
/*     */     
/*  72 */     String name = Notification.class.getName();
/*  73 */     String description = "StatusLogger has logged an event";
/*  74 */     return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getStatusDataHistory() {
/*  79 */     List<StatusData> data = getStatusData();
/*  80 */     String[] result = new String[data.size()];
/*  81 */     for (int i = 0; i < result.length; i++) {
/*  82 */       result[i] = ((StatusData)data.get(i)).getFormattedStatus();
/*     */     }
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StatusData> getStatusData() {
/*  89 */     return StatusLogger.getLogger().getStatusData();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevel() {
/*  94 */     return this.level.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getStatusLevel() {
/*  99 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(String level) {
/* 104 */     this.level = Level.toLevel(level, Level.ERROR);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextName() {
/* 109 */     return this.contextName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(StatusData data) {
/* 121 */     Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", getObjectName(), nextSeqNo(), now(), data.getFormattedStatus());
/*     */     
/* 123 */     sendNotification(notifMsg);
/*     */     
/* 125 */     Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", getObjectName(), nextSeqNo(), now());
/* 126 */     notifData.setUserData(data);
/* 127 */     sendNotification(notifData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName() {
/* 138 */     return this.objectName;
/*     */   }
/*     */   
/*     */   private long nextSeqNo() {
/* 142 */     return this.sequenceNo.getAndIncrement();
/*     */   }
/*     */   
/*     */   private long now() {
/* 146 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {}
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\StatusLoggerAdmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */