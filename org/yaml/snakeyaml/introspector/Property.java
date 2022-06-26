/*    */ package org.yaml.snakeyaml.introspector;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Property
/*    */   implements Comparable<Property>
/*    */ {
/*    */   private final String name;
/*    */   private final Class<?> type;
/*    */   
/*    */   public Property(String name, Class<?> type) {
/* 37 */     this.name = name;
/* 38 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Class<?> getType() {
/* 42 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return getName() + " of " + getType();
/*    */   }
/*    */   
/*    */   public int compareTo(Property o) {
/* 57 */     return this.name.compareTo(o.name);
/*    */   }
/*    */   
/*    */   public boolean isWritable() {
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isReadable() {
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return this.name.hashCode() + this.type.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 79 */     if (other instanceof Property) {
/* 80 */       Property p = (Property)other;
/* 81 */       return (this.name.equals(p.getName()) && this.type.equals(p.getType()));
/*    */     } 
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   public abstract Class<?>[] getActualTypeArguments();
/*    */   
/*    */   public abstract void set(Object paramObject1, Object paramObject2) throws Exception;
/*    */   
/*    */   public abstract Object get(Object paramObject);
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\introspector\Property.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */