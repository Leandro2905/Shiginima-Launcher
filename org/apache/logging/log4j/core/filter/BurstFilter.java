/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.DelayQueue;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "BurstFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class BurstFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private static final long NANOS_IN_SECONDS = 1000000000L;
/*     */   private static final int DEFAULT_RATE = 10;
/*     */   private static final int DEFAULT_RATE_MULTIPLE = 100;
/*     */   private static final int HASH_SHIFT = 32;
/*     */   private final Level level;
/*     */   private final long burstInterval;
/*  76 */   private final DelayQueue<LogDelay> history = new DelayQueue<LogDelay>();
/*     */   
/*  78 */   private final Queue<LogDelay> available = new ConcurrentLinkedQueue<LogDelay>();
/*     */ 
/*     */   
/*     */   private BurstFilter(Level level, float rate, long maxBurst, Filter.Result onMatch, Filter.Result onMismatch) {
/*  82 */     super(onMatch, onMismatch);
/*  83 */     this.level = level;
/*  84 */     this.burstInterval = (long)(1.0E9F * (float)maxBurst / rate);
/*  85 */     for (int i = 0; i < maxBurst; i++) {
/*  86 */       this.available.add(new LogDelay());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  93 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  99 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 105 */     return filter(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 110 */     return filter(event.getLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Filter.Result filter(Level level) {
/* 121 */     if (this.level.isMoreSpecificThan(level)) {
/* 122 */       LogDelay delay = this.history.poll();
/* 123 */       while (delay != null) {
/* 124 */         this.available.add(delay);
/* 125 */         delay = this.history.poll();
/*     */       } 
/* 127 */       delay = this.available.poll();
/* 128 */       if (delay != null) {
/* 129 */         delay.setDelay(this.burstInterval);
/* 130 */         this.history.add(delay);
/* 131 */         return this.onMatch;
/*     */       } 
/* 133 */       return this.onMismatch;
/*     */     } 
/* 135 */     return this.onMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAvailable() {
/* 144 */     return this.available.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 151 */     Iterator<LogDelay> iter = this.history.iterator();
/* 152 */     while (iter.hasNext()) {
/* 153 */       LogDelay delay = iter.next();
/* 154 */       this.history.remove(delay);
/* 155 */       this.available.add(delay);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class LogDelay
/*     */     implements Delayed
/*     */   {
/*     */     private long expireTime;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDelay(long delay) {
/* 175 */       this.expireTime = delay + System.nanoTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public long getDelay(TimeUnit timeUnit) {
/* 180 */       return timeUnit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Delayed delayed) {
/* 185 */       if (this.expireTime < ((LogDelay)delayed).expireTime)
/* 186 */         return -1; 
/* 187 */       if (this.expireTime > ((LogDelay)delayed).expireTime) {
/* 188 */         return 1;
/*     */       }
/* 190 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 195 */       if (this == o) {
/* 196 */         return true;
/*     */       }
/* 198 */       if (o == null || getClass() != o.getClass()) {
/* 199 */         return false;
/*     */       }
/*     */       
/* 202 */       LogDelay logDelay = (LogDelay)o;
/*     */       
/* 204 */       if (this.expireTime != logDelay.expireTime) {
/* 205 */         return false;
/*     */       }
/*     */       
/* 208 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 213 */       return (int)(this.expireTime ^ this.expireTime >>> 32L);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static BurstFilter createFilter(@PluginAttribute("level") Level level, @PluginAttribute("rate") Float rate, @PluginAttribute("maxBurst") Long maxBurst, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 233 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/* 234 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/* 235 */     Level actualLevel = (level == null) ? Level.WARN : level;
/* 236 */     float eventRate = (rate == null) ? 10.0F : rate.floatValue();
/* 237 */     if (eventRate <= 0.0F) {
/* 238 */       eventRate = 10.0F;
/*     */     }
/* 240 */     long max = (maxBurst == null) ? (long)(eventRate * 100.0F) : maxBurst.longValue();
/* 241 */     return new BurstFilter(actualLevel, eventRate, max, onMatch, onMismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\BurstFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */