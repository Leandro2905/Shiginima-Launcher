/*    */ package org.yaml.snakeyaml;
/*    */ 
/*    */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*    */ import org.yaml.snakeyaml.constructor.Constructor;
/*    */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*    */ public final class Loader
/*    */ {
/*    */   protected final BaseConstructor constructor;
/*    */   protected Resolver resolver;
/*    */   
/*    */   public Loader(BaseConstructor constructor) {
/* 31 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   public Loader() {
/* 35 */     this((BaseConstructor)new Constructor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\Loader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */