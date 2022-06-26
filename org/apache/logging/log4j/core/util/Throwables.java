/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public final class Throwables
/*     */ {
/*     */   private static final Method ADD_SUPPRESSED;
/*     */   private static final Method GET_SUPPRESSED;
/*     */   
/*     */   static {
/*  42 */     Method getSuppressed = null, addSuppressed = null;
/*  43 */     Method[] methods = Throwable.class.getMethods();
/*  44 */     for (Method method : methods) {
/*  45 */       if (method.getName().equals("getSuppressed")) {
/*  46 */         getSuppressed = method;
/*  47 */       } else if (method.getName().equals("addSuppressed")) {
/*  48 */         addSuppressed = method;
/*     */       } 
/*     */     } 
/*  51 */     GET_SUPPRESSED = getSuppressed;
/*  52 */     ADD_SUPPRESSED = addSuppressed;
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
/*     */   @Deprecated
/*     */   public static void addSuppressed(Throwable throwable, Throwable suppressedThrowable) {
/*  66 */     if (ADD_SUPPRESSED != null) {
/*     */       try {
/*  68 */         ADD_SUPPRESSED.invoke(throwable, new Object[] { suppressedThrowable });
/*  69 */       } catch (IllegalAccessException e) {
/*     */         
/*  71 */         StatusLogger.getLogger().error(e);
/*  72 */       } catch (IllegalArgumentException e) {
/*     */         
/*  74 */         StatusLogger.getLogger().error(e);
/*  75 */       } catch (InvocationTargetException e) {
/*     */         
/*  77 */         StatusLogger.getLogger().error(e);
/*     */       } 
/*     */     }
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
/*     */   @Deprecated
/*     */   public static Throwable[] getSuppressed(Throwable throwable) {
/*  94 */     if (GET_SUPPRESSED != null) {
/*     */       try {
/*  96 */         return (Throwable[])GET_SUPPRESSED.invoke(throwable, new Object[0]);
/*  97 */       } catch (Exception e) {
/*     */         
/*  99 */         StatusLogger.getLogger().error(e);
/* 100 */         return null;
/*     */       } 
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGetSuppressedAvailable() {
/* 112 */     return (GET_SUPPRESSED != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> toStringList(Throwable throwable) {
/* 122 */     StringWriter sw = new StringWriter();
/* 123 */     PrintWriter pw = new PrintWriter(sw);
/*     */     try {
/* 125 */       throwable.printStackTrace(pw);
/* 126 */     } catch (RuntimeException ex) {}
/*     */ 
/*     */     
/* 129 */     pw.flush();
/* 130 */     List<String> lines = new ArrayList<String>();
/* 131 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/*     */     try {
/* 133 */       String line = reader.readLine();
/* 134 */       while (line != null) {
/* 135 */         lines.add(line);
/* 136 */         line = reader.readLine();
/*     */       } 
/* 138 */     } catch (IOException ex) {
/* 139 */       if (ex instanceof java.io.InterruptedIOException) {
/* 140 */         Thread.currentThread().interrupt();
/*     */       }
/* 142 */       lines.add(ex.toString());
/*     */     } finally {
/* 144 */       Closer.closeSilently(reader);
/*     */     } 
/* 146 */     return lines;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Throwables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */