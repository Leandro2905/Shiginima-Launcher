/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class ArrayBasedEscaperMap
/*    */ {
/*    */   private final char[][] replacementArray;
/*    */   
/*    */   public static ArrayBasedEscaperMap create(Map<Character, String> replacements) {
/* 56 */     return new ArrayBasedEscaperMap(createReplacementArray(replacements));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ArrayBasedEscaperMap(char[][] replacementArray) {
/* 64 */     this.replacementArray = replacementArray;
/*    */   }
/*    */ 
/*    */   
/*    */   char[][] getReplacementArray() {
/* 69 */     return this.replacementArray;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   static char[][] createReplacementArray(Map<Character, String> map) {
/* 77 */     Preconditions.checkNotNull(map);
/* 78 */     if (map.isEmpty()) {
/* 79 */       return EMPTY_REPLACEMENT_ARRAY;
/*    */     }
/* 81 */     char max = ((Character)Collections.<Character>max(map.keySet())).charValue();
/* 82 */     char[][] replacements = new char[max + 1][];
/* 83 */     for (Iterator<Character> i$ = map.keySet().iterator(); i$.hasNext(); ) { char c = ((Character)i$.next()).charValue();
/* 84 */       replacements[c] = ((String)map.get(Character.valueOf(c))).toCharArray(); }
/*    */     
/* 86 */     return replacements;
/*    */   }
/*    */ 
/*    */   
/* 90 */   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\escape\ArrayBasedEscaperMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */