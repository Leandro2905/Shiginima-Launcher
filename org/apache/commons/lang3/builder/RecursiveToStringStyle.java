/*    */ package org.apache.commons.lang3.builder;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.apache.commons.lang3.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecursiveToStringStyle
/*    */   extends ToStringStyle
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/* 72 */     if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && accept(value.getClass())) {
/*    */ 
/*    */       
/* 75 */       buffer.append(ReflectionToStringBuilder.toString(value, this));
/*    */     } else {
/* 77 */       super.appendDetail(buffer, fieldName, value);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
/* 83 */     appendClassName(buffer, coll);
/* 84 */     appendIdentityHashCode(buffer, coll);
/* 85 */     appendDetail(buffer, fieldName, coll.toArray());
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
/*    */   protected boolean accept(Class<?> clazz) {
/* 98 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\RecursiveToStringStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */