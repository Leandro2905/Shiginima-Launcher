/*    */ package org.yaml.snakeyaml;
/*    */ 
/*    */ import org.yaml.snakeyaml.representer.Representer;
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
/*    */ public final class Dumper
/*    */ {
/*    */   protected final Representer representer;
/*    */   protected final DumperOptions options;
/*    */   
/*    */   public Dumper(Representer representer, DumperOptions options) {
/* 28 */     this.representer = representer;
/* 29 */     this.options = options;
/*    */   }
/*    */   
/*    */   public Dumper(DumperOptions options) {
/* 33 */     this(new Representer(), options);
/*    */   }
/*    */   
/*    */   public Dumper(Representer representer) {
/* 37 */     this(representer, new DumperOptions());
/*    */   }
/*    */   
/*    */   public Dumper() {
/* 41 */     this(new Representer(), new DumperOptions());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\Dumper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */