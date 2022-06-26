/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class MemberUtils
/*     */ {
/*     */   private static final int ACCESS_TEST = 7;
/*  38 */   private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[] { byte.class, short.class, char.class, int.class, long.class, float.class, double.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean setAccessibleWorkaround(AccessibleObject o) {
/*  55 */     if (o == null || o.isAccessible()) {
/*  56 */       return false;
/*     */     }
/*  58 */     Member m = (Member)o;
/*  59 */     if (!o.isAccessible() && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
/*     */       try {
/*  61 */         o.setAccessible(true);
/*  62 */         return true;
/*  63 */       } catch (SecurityException e) {}
/*     */     }
/*     */ 
/*     */     
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPackageAccess(int modifiers) {
/*  76 */     return ((modifiers & 0x7) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAccessible(Member m) {
/*  85 */     return (m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
/* 101 */     float leftCost = getTotalTransformationCost(actual, left);
/* 102 */     float rightCost = getTotalTransformationCost(actual, right);
/* 103 */     return (leftCost < rightCost) ? -1 : ((rightCost < leftCost) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
/* 114 */     float totalCost = 0.0F;
/* 115 */     for (int i = 0; i < srcArgs.length; i++) {
/*     */       
/* 117 */       Class<?> srcClass = srcArgs[i];
/* 118 */       Class<?> destClass = destArgs[i];
/* 119 */       totalCost += getObjectTransformationCost(srcClass, destClass);
/*     */     } 
/* 121 */     return totalCost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
/* 133 */     if (destClass.isPrimitive()) {
/* 134 */       return getPrimitivePromotionCost(srcClass, destClass);
/*     */     }
/* 136 */     float cost = 0.0F;
/* 137 */     while (srcClass != null && !destClass.equals(srcClass)) {
/* 138 */       if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 144 */         cost += 0.25F;
/*     */         break;
/*     */       } 
/* 147 */       cost++;
/* 148 */       srcClass = srcClass.getSuperclass();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (srcClass == null) {
/* 155 */       cost += 1.5F;
/*     */     }
/* 157 */     return cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
/* 168 */     float cost = 0.0F;
/* 169 */     Class<?> cls = srcClass;
/* 170 */     if (!cls.isPrimitive()) {
/*     */       
/* 172 */       cost += 0.1F;
/* 173 */       cls = ClassUtils.wrapperToPrimitive(cls);
/*     */     } 
/* 175 */     for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
/* 176 */       if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
/* 177 */         cost += 0.1F;
/* 178 */         if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
/* 179 */           cls = ORDERED_PRIMITIVE_TYPES[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 183 */     return cost;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\reflect\MemberUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */