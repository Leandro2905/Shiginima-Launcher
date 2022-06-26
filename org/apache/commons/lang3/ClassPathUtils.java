/*     */ package org.apache.commons.lang3;
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
/*     */ public class ClassPathUtils
/*     */ {
/*     */   public static String toFullyQualifiedName(Class<?> context, String resourceName) {
/*  59 */     Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
/*  60 */     Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
/*  61 */     return toFullyQualifiedName(context.getPackage(), resourceName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedName(Package context, String resourceName) {
/*  81 */     Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
/*  82 */     Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
/*  83 */     StringBuilder sb = new StringBuilder();
/*  84 */     sb.append(context.getName());
/*  85 */     sb.append(".");
/*  86 */     sb.append(resourceName);
/*  87 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedPath(Class<?> context, String resourceName) {
/* 107 */     Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
/* 108 */     Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
/* 109 */     return toFullyQualifiedPath(context.getPackage(), resourceName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedPath(Package context, String resourceName) {
/* 130 */     Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
/* 131 */     Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
/* 132 */     StringBuilder sb = new StringBuilder();
/* 133 */     sb.append(context.getName().replace('.', '/'));
/* 134 */     sb.append("/");
/* 135 */     sb.append(resourceName);
/* 136 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\ClassPathUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */