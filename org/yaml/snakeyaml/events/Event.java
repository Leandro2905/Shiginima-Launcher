/*    */ package org.yaml.snakeyaml.events;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public abstract class Event
/*    */ {
/*    */   private final Mark startMark;
/*    */   private final Mark endMark;
/*    */   
/*    */   public enum ID
/*    */   {
/* 26 */     Alias, DocumentEnd, DocumentStart, MappingEnd, MappingStart, Scalar, SequenceEnd, SequenceStart, StreamEnd, StreamStart;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Event(Mark startMark, Mark endMark) {
/* 33 */     this.startMark = startMark;
/* 34 */     this.endMark = endMark;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return "<" + getClass().getName() + "(" + getArguments() + ")>";
/*    */   }
/*    */   
/*    */   public Mark getStartMark() {
/* 42 */     return this.startMark;
/*    */   }
/*    */   
/*    */   public Mark getEndMark() {
/* 46 */     return this.endMark;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 53 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean is(ID paramID);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 63 */     if (obj instanceof Event) {
/* 64 */       return toString().equals(obj.toString());
/*    */     }
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 75 */     return toString().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\events\Event.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */