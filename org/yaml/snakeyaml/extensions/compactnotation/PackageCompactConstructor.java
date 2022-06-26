/*    */ package org.yaml.snakeyaml.extensions.compactnotation;
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
/*    */ public class PackageCompactConstructor
/*    */   extends CompactConstructor
/*    */ {
/*    */   private String packageName;
/*    */   
/*    */   public PackageCompactConstructor(String packageName) {
/* 22 */     this.packageName = packageName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 27 */     if (name.indexOf('.') < 0) {
/*    */       try {
/* 29 */         Class<?> clazz = Class.forName(this.packageName + "." + name);
/* 30 */         return clazz;
/* 31 */       } catch (ClassNotFoundException e) {}
/*    */     }
/*    */ 
/*    */     
/* 35 */     return super.getClassForName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\extensions\compactnotation\PackageCompactConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */