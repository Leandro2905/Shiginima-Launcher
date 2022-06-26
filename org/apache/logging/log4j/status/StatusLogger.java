/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.simple.SimpleLogger;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StatusLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
/*     */   private static final String NOT_AVAIL = "?";
/*  55 */   private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
/*     */   
/*  57 */   private static final int MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
/*     */   
/*  59 */   private static final String DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
/*     */   
/*  61 */   private static final StatusLogger STATUS_LOGGER = new StatusLogger();
/*     */   
/*     */   private final SimpleLogger logger;
/*     */   
/*  65 */   private final Collection<StatusListener> listeners = new CopyOnWriteArrayList<StatusListener>();
/*  66 */   private final ReadWriteLock listenersLock = new ReentrantReadWriteLock();
/*     */   
/*  68 */   private final Queue<StatusData> messages = new BoundedQueue<StatusData>(MAX_ENTRIES);
/*  69 */   private final Lock msgLock = new ReentrantLock();
/*     */   
/*     */   private int listenersLevel;
/*     */   
/*     */   private StatusLogger() {
/*  74 */     this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", null, PROPS, System.err);
/*     */     
/*  76 */     this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StatusLogger getLogger() {
/*  84 */     return STATUS_LOGGER;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/*  88 */     this.logger.setLevel(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerListener(StatusListener listener) {
/*  96 */     this.listenersLock.writeLock().lock();
/*     */     try {
/*  98 */       this.listeners.add(listener);
/*  99 */       Level lvl = listener.getStatusLevel();
/* 100 */       if (this.listenersLevel < lvl.intLevel()) {
/* 101 */         this.listenersLevel = lvl.intLevel();
/*     */       }
/*     */     } finally {
/* 104 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(StatusListener listener) {
/* 113 */     closeSilently(listener);
/* 114 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 116 */       this.listeners.remove(listener);
/* 117 */       int lowest = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/* 118 */       for (StatusListener l : this.listeners) {
/* 119 */         int level = l.getStatusLevel().intLevel();
/* 120 */         if (lowest < level) {
/* 121 */           lowest = level;
/*     */         }
/*     */       } 
/* 124 */       this.listenersLevel = lowest;
/*     */     } finally {
/* 126 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<StatusListener> getListeners() {
/* 135 */     return this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 142 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 144 */       for (StatusListener listener : this.listeners) {
/* 145 */         closeSilently(listener);
/*     */       }
/*     */     } finally {
/* 148 */       this.listeners.clear();
/* 149 */       this.listenersLock.writeLock().unlock();
/*     */       
/* 151 */       clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeSilently(Closeable resource) {
/*     */     try {
/* 157 */       resource.close();
/* 158 */     } catch (IOException ignored) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<StatusData> getStatusData() {
/* 167 */     this.msgLock.lock();
/*     */     try {
/* 169 */       return new ArrayList<StatusData>(this.messages);
/*     */     } finally {
/* 171 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 179 */     this.msgLock.lock();
/*     */     try {
/* 181 */       this.messages.clear();
/*     */     } finally {
/* 183 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 189 */     return this.logger.getLevel();
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
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message msg, Throwable t) {
/* 202 */     StackTraceElement element = null;
/* 203 */     if (fqcn != null) {
/* 204 */       element = getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
/*     */     }
/* 206 */     StatusData data = new StatusData(element, level, msg, t);
/* 207 */     this.msgLock.lock();
/*     */     try {
/* 209 */       this.messages.add(data);
/*     */     } finally {
/* 211 */       this.msgLock.unlock();
/*     */     } 
/* 213 */     if (this.listeners.size() > 0) {
/* 214 */       for (StatusListener listener : this.listeners) {
/* 215 */         if (data.getLevel().isMoreSpecificThan(listener.getStatusLevel())) {
/* 216 */           listener.log(data);
/*     */         }
/*     */       } 
/*     */     } else {
/* 220 */       this.logger.logMessage(fqcn, level, marker, msg, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StackTraceElement getStackTraceElement(String fqcn, StackTraceElement[] stackTrace) {
/* 225 */     if (fqcn == null) {
/* 226 */       return null;
/*     */     }
/* 228 */     boolean next = false;
/* 229 */     for (StackTraceElement element : stackTrace) {
/* 230 */       String className = element.getClassName();
/* 231 */       if (next && !fqcn.equals(className)) {
/* 232 */         return element;
/*     */       }
/* 234 */       if (fqcn.equals(className)) {
/* 235 */         next = true;
/* 236 */       } else if ("?".equals(className)) {
/*     */         break;
/*     */       } 
/*     */     } 
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 245 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 250 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 255 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 260 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 265 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker) {
/* 270 */     if (this.listeners.size() > 0) {
/* 271 */       return (this.listenersLevel >= level.intLevel());
/*     */     }
/* 273 */     return this.logger.isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class BoundedQueue<E>
/*     */     extends ConcurrentLinkedQueue<E>
/*     */   {
/*     */     private static final long serialVersionUID = -3945953719763255337L;
/*     */     
/*     */     private final int size;
/*     */ 
/*     */     
/*     */     public BoundedQueue(int size) {
/* 287 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(E object) {
/* 292 */       while (StatusLogger.this.messages.size() > this.size) {
/* 293 */         StatusLogger.this.messages.poll();
/*     */       }
/* 295 */       return super.add(object);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\status\StatusLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */