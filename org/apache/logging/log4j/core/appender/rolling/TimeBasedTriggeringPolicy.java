/*    */ package org.apache.logging.log4j.core.appender.rolling;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.util.Integers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
/*    */ public final class TimeBasedTriggeringPolicy
/*    */   implements TriggeringPolicy
/*    */ {
/*    */   private long nextRollover;
/*    */   private final int interval;
/*    */   private final boolean modulate;
/*    */   private RollingFileManager manager;
/*    */   
/*    */   private TimeBasedTriggeringPolicy(int interval, boolean modulate) {
/* 38 */     this.interval = interval;
/* 39 */     this.modulate = modulate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initialize(RollingFileManager manager) {
/* 48 */     this.manager = manager;
/*    */ 
/*    */     
/* 51 */     manager.getPatternProcessor().getNextTime(manager.getFileTime(), this.interval, this.modulate);
/*    */     
/* 53 */     this.nextRollover = manager.getPatternProcessor().getNextTime(manager.getFileTime(), this.interval, this.modulate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTriggeringEvent(LogEvent event) {
/* 63 */     if (this.manager.getFileSize() == 0L) {
/* 64 */       return false;
/*    */     }
/* 66 */     long now = event.getTimeMillis();
/* 67 */     if (now > this.nextRollover) {
/* 68 */       this.nextRollover = this.manager.getPatternProcessor().getNextTime(now, this.interval, this.modulate);
/* 69 */       return true;
/*    */     } 
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return "TimeBasedTriggeringPolicy";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") String interval, @PluginAttribute("modulate") String modulate) {
/* 89 */     int increment = Integers.parseInt(interval, 1);
/* 90 */     boolean mod = Boolean.parseBoolean(modulate);
/* 91 */     return new TimeBasedTriggeringPolicy(increment, mod);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\TimeBasedTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */