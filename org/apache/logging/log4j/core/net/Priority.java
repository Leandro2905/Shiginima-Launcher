/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ 
/*    */ public class Priority
/*    */ {
/*    */   private final Facility facility;
/*    */   private final Severity severity;
/*    */   
/*    */   public Priority(Facility facility, Severity severity) {
/* 35 */     this.facility = facility;
/* 36 */     this.severity = severity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getPriority(Facility facility, Level level) {
/* 46 */     return (facility.getCode() << 3) + Severity.getSeverity(level).getCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Facility getFacility() {
/* 54 */     return this.facility;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Severity getSeverity() {
/* 62 */     return this.severity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 70 */     return this.facility.getCode() << 3 + this.severity.getCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return Integer.toString(getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\Priority.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */