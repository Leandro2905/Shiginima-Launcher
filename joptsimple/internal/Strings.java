/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public final class Strings
/*     */ {
/*     */   public static final String EMPTY = "";
/*     */   public static final String SINGLE_QUOTE = "'";
/*  40 */   public static final String LINE_SEPARATOR = System.getProperty("line.separator");
/*     */   
/*     */   private Strings() {
/*  43 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String repeat(char ch, int count) {
/*  54 */     StringBuilder buffer = new StringBuilder();
/*     */     
/*  56 */     for (int i = 0; i < count; i++) {
/*  57 */       buffer.append(ch);
/*     */     }
/*  59 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNullOrEmpty(String target) {
/*  69 */     return (target == null || "".equals(target));
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
/*     */   public static String surround(String target, char begin, char end) {
/*  82 */     return begin + target + end;
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
/*     */   public static String join(String[] pieces, String separator) {
/*  94 */     return join(Arrays.asList(pieces), separator);
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
/*     */   public static String join(List<String> pieces, String separator) {
/* 106 */     StringBuilder buffer = new StringBuilder();
/*     */     
/* 108 */     for (Iterator<String> iter = pieces.iterator(); iter.hasNext(); ) {
/* 109 */       buffer.append(iter.next());
/*     */       
/* 111 */       if (iter.hasNext()) {
/* 112 */         buffer.append(separator);
/*     */       }
/*     */     } 
/* 115 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\Strings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */