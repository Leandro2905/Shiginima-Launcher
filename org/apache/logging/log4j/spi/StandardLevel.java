/*    */ package org.apache.logging.log4j.spi;
/*    */ 
/*    */ import java.util.EnumSet;
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
/*    */ 
/*    */ public enum StandardLevel
/*    */ {
/* 30 */   OFF(0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   FATAL(100),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   ERROR(200),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   WARN(300),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   INFO(400),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   DEBUG(500),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   TRACE(600),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   ALL(2147483647);
/*    */   private final int intLevel;
/*    */   private static final EnumSet<StandardLevel> levelSet;
/*    */   
/*    */   static {
/* 70 */     levelSet = EnumSet.allOf(StandardLevel.class);
/*    */   }
/*    */   StandardLevel(int val) {
/* 73 */     this.intLevel = val;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int intLevel() {
/* 81 */     return this.intLevel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static StandardLevel getStandardLevel(int intLevel) {
/* 90 */     StandardLevel level = OFF;
/* 91 */     for (StandardLevel lvl : levelSet) {
/* 92 */       if (lvl.intLevel() > intLevel) {
/*    */         break;
/*    */       }
/* 95 */       level = lvl;
/*    */     } 
/* 97 */     return level;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\StandardLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */