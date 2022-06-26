/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import org.apache.commons.lang3.ArrayUtils;
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
/*    */ public class AggregateTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   private final CharSequenceTranslator[] translators;
/*    */   
/*    */   public AggregateTranslator(CharSequenceTranslator... translators) {
/* 41 */     this.translators = (CharSequenceTranslator[])ArrayUtils.clone((Object[])translators);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 51 */     for (CharSequenceTranslator translator : this.translators) {
/* 52 */       int consumed = translator.translate(input, index, out);
/* 53 */       if (consumed != 0) {
/* 54 */         return consumed;
/*    */       }
/*    */     } 
/* 57 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\translate\AggregateTranslator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */