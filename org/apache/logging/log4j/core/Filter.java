/*    */ package org.apache.logging.log4j.core;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.util.EnglishEnums;
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
/*    */ public interface Filter
/*    */   extends LifeCycle
/*    */ {
/*    */   Result getOnMismatch();
/*    */   
/*    */   Result getOnMatch();
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
/*    */   
/*    */   Result filter(LogEvent paramLogEvent);
/*    */   
/*    */   public enum Result
/*    */   {
/* 42 */     ACCEPT,
/*    */ 
/*    */ 
/*    */     
/* 46 */     NEUTRAL,
/*    */ 
/*    */ 
/*    */     
/* 50 */     DENY;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Result toResult(String name) {
/* 59 */       return toResult(name, null);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Result toResult(String name, Result defaultResult) {
/* 70 */       return (Result)EnglishEnums.valueOf(Result.class, name, defaultResult);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\Filter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */