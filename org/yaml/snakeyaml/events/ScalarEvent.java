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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ScalarEvent
/*    */   extends NodeEvent
/*    */ {
/*    */   private final String tag;
/*    */   private final Character style;
/*    */   private final String value;
/*    */   private final ImplicitTuple implicit;
/*    */   
/*    */   public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, Character style) {
/* 36 */     super(anchor, startMark, endMark);
/* 37 */     this.tag = tag;
/* 38 */     this.implicit = implicit;
/* 39 */     this.value = value;
/* 40 */     this.style = style;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTag() {
/* 50 */     return this.tag;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Character getStyle() {
/* 73 */     return this.style;
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
/*    */   public String getValue() {
/* 85 */     return this.value;
/*    */   }
/*    */   
/*    */   public ImplicitTuple getImplicit() {
/* 89 */     return this.implicit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 94 */     return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Event.ID id) {
/* 99 */     return (Event.ID.Scalar == id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\events\ScalarEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */