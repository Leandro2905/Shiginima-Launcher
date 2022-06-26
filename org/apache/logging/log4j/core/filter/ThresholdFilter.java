/*    */ package org.apache.logging.log4j.core.filter;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.core.Filter;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.message.Message;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "ThresholdFilter", category = "Core", elementType = "filter", printObject = true)
/*    */ public final class ThresholdFilter
/*    */   extends AbstractFilter
/*    */ {
/*    */   private final Level level;
/*    */   
/*    */   private ThresholdFilter(Level level, Filter.Result onMatch, Filter.Result onMismatch) {
/* 42 */     super(onMatch, onMismatch);
/* 43 */     this.level = level;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 49 */     return filter(level);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 55 */     return filter(level);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 61 */     return filter(level);
/*    */   }
/*    */ 
/*    */   
/*    */   public Filter.Result filter(LogEvent event) {
/* 66 */     return filter(event.getLevel());
/*    */   }
/*    */   
/*    */   private Filter.Result filter(Level level) {
/* 70 */     return level.isMoreSpecificThan(this.level) ? this.onMatch : this.onMismatch;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return this.level.toString();
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
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static ThresholdFilter createFilter(@PluginAttribute("level") Level level, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 90 */     Level actualLevel = (level == null) ? Level.ERROR : level;
/* 91 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/* 92 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/* 93 */     return new ThresholdFilter(actualLevel, onMatch, onMismatch);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\ThresholdFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */