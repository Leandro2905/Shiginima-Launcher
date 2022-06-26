/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
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
/*    */ public class LookupTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/* 47 */   private final HashMap<String, String> lookupMap = new HashMap<String, String>();
/* 48 */   private final HashSet<Character> prefixSet = new HashSet<Character>(); public LookupTranslator(CharSequence[]... lookup) {
/* 49 */     int _shortest = Integer.MAX_VALUE;
/* 50 */     int _longest = 0;
/* 51 */     if (lookup != null) {
/* 52 */       for (CharSequence[] seq : lookup) {
/* 53 */         this.lookupMap.put(seq[0].toString(), seq[1].toString());
/* 54 */         this.prefixSet.add(Character.valueOf(seq[0].charAt(0)));
/* 55 */         int sz = seq[0].length();
/* 56 */         if (sz < _shortest) {
/* 57 */           _shortest = sz;
/*    */         }
/* 59 */         if (sz > _longest) {
/* 60 */           _longest = sz;
/*    */         }
/*    */       } 
/*    */     }
/* 64 */     this.shortest = _shortest;
/* 65 */     this.longest = _longest;
/*    */   }
/*    */ 
/*    */   
/*    */   private final int shortest;
/*    */   
/*    */   private final int longest;
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 74 */     if (this.prefixSet.contains(Character.valueOf(input.charAt(index)))) {
/* 75 */       int max = this.longest;
/* 76 */       if (index + this.longest > input.length()) {
/* 77 */         max = input.length() - index;
/*    */       }
/*    */       
/* 80 */       for (int i = max; i >= this.shortest; i--) {
/* 81 */         CharSequence subSeq = input.subSequence(index, index + i);
/* 82 */         String result = this.lookupMap.get(subSeq.toString());
/*    */         
/* 84 */         if (result != null) {
/* 85 */           out.write(result);
/* 86 */           return i;
/*    */         } 
/*    */       } 
/*    */     } 
/* 90 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\translate\LookupTranslator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */