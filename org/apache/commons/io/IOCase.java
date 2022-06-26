/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public final class IOCase
/*     */   implements Serializable
/*     */ {
/*  42 */   public static final IOCase SENSITIVE = new IOCase("Sensitive", true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final IOCase INSENSITIVE = new IOCase("Insensitive", false);
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
/*  61 */   public static final IOCase SYSTEM = new IOCase("System", !FilenameUtils.isSystemWindows());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -6343169151696340687L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   private final transient boolean sensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOCase forName(String name) {
/*  81 */     if (SENSITIVE.name.equals(name)) {
/*  82 */       return SENSITIVE;
/*     */     }
/*  84 */     if (INSENSITIVE.name.equals(name)) {
/*  85 */       return INSENSITIVE;
/*     */     }
/*  87 */     if (SYSTEM.name.equals(name)) {
/*  88 */       return SYSTEM;
/*     */     }
/*  90 */     throw new IllegalArgumentException("Invalid IOCase name: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IOCase(String name, boolean sensitive) {
/* 101 */     this.name = name;
/* 102 */     this.sensitive = sensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 112 */     return forName(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 122 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/* 131 */     return this.sensitive;
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
/*     */   public int checkCompareTo(String str1, String str2) {
/* 147 */     if (str1 == null || str2 == null) {
/* 148 */       throw new NullPointerException("The strings must not be null");
/*     */     }
/* 150 */     return this.sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
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
/*     */   public boolean checkEquals(String str1, String str2) {
/* 165 */     if (str1 == null || str2 == null) {
/* 166 */       throw new NullPointerException("The strings must not be null");
/*     */     }
/* 168 */     return this.sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
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
/*     */   public boolean checkStartsWith(String str, String start) {
/* 183 */     return str.regionMatches(!this.sensitive, 0, start, 0, start.length());
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
/*     */   public boolean checkEndsWith(String str, String end) {
/* 198 */     int endLen = end.length();
/* 199 */     return str.regionMatches(!this.sensitive, str.length() - endLen, end, 0, endLen);
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
/*     */   public int checkIndexOf(String str, int strStartIndex, String search) {
/* 218 */     int endIndex = str.length() - search.length();
/* 219 */     if (endIndex >= strStartIndex) {
/* 220 */       for (int i = strStartIndex; i <= endIndex; i++) {
/* 221 */         if (checkRegionMatches(str, i, search)) {
/* 222 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/* 226 */     return -1;
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
/*     */   public boolean checkRegionMatches(String str, int strStartIndex, String search) {
/* 242 */     return str.regionMatches(!this.sensitive, strStartIndex, search, 0, search.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\IOCase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */