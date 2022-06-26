/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ public class LazyStringBuilder
/*     */   implements Appendable, CharSequence
/*     */ {
/*     */   private final List<CharSequence> list;
/*     */   private String cache;
/*     */   
/*     */   private void invalidateCache() {
/*  29 */     this.cache = null;
/*     */   }
/*     */   
/*     */   public LazyStringBuilder() {
/*  33 */     this.list = new ArrayList<>(20);
/*     */   }
/*     */   
/*     */   public LazyStringBuilder append(LazyStringBuilder lsb) {
/*  37 */     this.list.addAll(lsb.list);
/*  38 */     invalidateCache();
/*  39 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LazyStringBuilder append(CharSequence csq) {
/*  44 */     assert csq != null;
/*  45 */     this.list.add(csq);
/*  46 */     invalidateCache();
/*  47 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LazyStringBuilder append(CharSequence csq, int start, int end) {
/*  52 */     CharSequence subsequence = csq.subSequence(start, end);
/*  53 */     this.list.add(subsequence);
/*  54 */     invalidateCache();
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LazyStringBuilder append(char c) {
/*  60 */     this.list.add(Character.toString(c));
/*  61 */     invalidateCache();
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/*  67 */     if (this.cache != null) {
/*  68 */       return this.cache.length();
/*     */     }
/*  70 */     int length = 0;
/*  71 */     for (CharSequence csq : this.list) {
/*  72 */       length += csq.length();
/*     */     }
/*  74 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  79 */     if (this.cache != null) {
/*  80 */       return this.cache.charAt(index);
/*     */     }
/*  82 */     for (CharSequence csq : this.list) {
/*  83 */       if (index < csq.length()) {
/*  84 */         return csq.charAt(index);
/*     */       }
/*  86 */       index -= csq.length();
/*     */     } 
/*     */     
/*  89 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/*  94 */     return toString().subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     if (this.cache == null) {
/* 100 */       StringBuilder sb = new StringBuilder(length());
/* 101 */       for (CharSequence csq : this.list) {
/* 102 */         sb.append(csq);
/*     */       }
/* 104 */       this.cache = sb.toString();
/*     */     } 
/* 106 */     return this.cache;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\LazyStringBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */