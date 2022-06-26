/*     */ package com.google.thirdparty.publicsuffix;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
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
/*     */ @GwtCompatible
/*     */ class TrieParser
/*     */ {
/*  32 */   private static final Joiner PREFIX_JOINER = Joiner.on("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
/*  39 */     ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
/*  40 */     int encodedLen = encoded.length();
/*  41 */     int idx = 0;
/*  42 */     while (idx < encodedLen) {
/*  43 */       idx += doParseTrieToBuilder(Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  48 */     return builder.build();
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
/*     */   private static int doParseTrieToBuilder(List<CharSequence> stack, CharSequence encoded, ImmutableMap.Builder<String, PublicSuffixType> builder) {
/*  65 */     int encodedLen = encoded.length();
/*  66 */     int idx = 0;
/*  67 */     char c = Character.MIN_VALUE;
/*     */ 
/*     */     
/*  70 */     for (; idx < encodedLen; idx++) {
/*  71 */       c = encoded.charAt(idx);
/*  72 */       if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  77 */     stack.add(0, reverse(encoded.subSequence(0, idx)));
/*     */     
/*  79 */     if (c == '!' || c == '?' || c == ':' || c == ',') {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       String domain = PREFIX_JOINER.join(stack);
/*  85 */       if (domain.length() > 0) {
/*  86 */         builder.put(domain, PublicSuffixType.fromCode(c));
/*     */       }
/*     */     } 
/*  89 */     idx++;
/*     */     
/*  91 */     if (c != '?' && c != ',') {
/*  92 */       while (idx < encodedLen) {
/*     */         
/*  94 */         idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
/*  95 */         if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
/*     */           
/*  97 */           idx++;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 102 */     stack.remove(0);
/* 103 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CharSequence reverse(CharSequence s) {
/* 112 */     int length = s.length();
/* 113 */     if (length <= 1) {
/* 114 */       return s;
/*     */     }
/*     */     
/* 117 */     char[] buffer = new char[length];
/* 118 */     buffer[0] = s.charAt(length - 1);
/*     */     
/* 120 */     for (int i = 1; i < length; i++) {
/* 121 */       buffer[i] = s.charAt(length - 1 - i);
/* 122 */       if (Character.isSurrogatePair(buffer[i], buffer[i - 1])) {
/* 123 */         swap(buffer, i - 1, i);
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return new String(buffer);
/*     */   }
/*     */   
/*     */   private static void swap(char[] buffer, int f, int s) {
/* 131 */     char tmp = buffer[f];
/* 132 */     buffer[f] = buffer[s];
/* 133 */     buffer[s] = tmp;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\thirdparty\publicsuffix\TrieParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */