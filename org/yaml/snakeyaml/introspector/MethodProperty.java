/*    */ package org.yaml.snakeyaml.introspector;
/*    */ 
/*    */ import java.beans.PropertyDescriptor;
/*    */ import org.yaml.snakeyaml.error.YAMLException;
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
/*    */ public class MethodProperty
/*    */   extends GenericProperty
/*    */ {
/*    */   private final PropertyDescriptor property;
/*    */   private final boolean readable;
/*    */   private final boolean writable;
/*    */   
/*    */   public MethodProperty(PropertyDescriptor property) {
/* 38 */     super(property.getName(), property.getPropertyType(), (property.getReadMethod() == null) ? null : property.getReadMethod().getGenericReturnType());
/*    */ 
/*    */     
/* 41 */     this.property = property;
/* 42 */     this.readable = (property.getReadMethod() != null);
/* 43 */     this.writable = (property.getWriteMethod() != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(Object object, Object value) throws Exception {
/* 48 */     this.property.getWriteMethod().invoke(object, new Object[] { value });
/*    */   }
/*    */ 
/*    */   
/*    */   public Object get(Object object) {
/*    */     try {
/* 54 */       this.property.getReadMethod().setAccessible(true);
/* 55 */       return this.property.getReadMethod().invoke(object, new Object[0]);
/* 56 */     } catch (Exception e) {
/* 57 */       throw new YAMLException("Unable to find getter for property '" + this.property.getName() + "' on object " + object + ":" + e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isWritable() {
/* 64 */     return this.writable;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadable() {
/* 69 */     return this.readable;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\introspector\MethodProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */