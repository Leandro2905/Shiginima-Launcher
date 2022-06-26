/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public final class SetUtils
/*    */ {
/*    */   public static String[] prefixSet(Set<String> set, String prefix) {
/* 31 */     Set<String> prefixSet = new HashSet<String>();
/* 32 */     for (String str : set) {
/* 33 */       if (str.startsWith(prefix)) {
/* 34 */         prefixSet.add(str);
/*    */       }
/*    */     } 
/* 37 */     return prefixSet.<String>toArray(new String[prefixSet.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\SetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */