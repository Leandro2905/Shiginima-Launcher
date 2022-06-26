/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ public class OctalUnescaper
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 40 */     int remaining = input.length() - index - 1;
/* 41 */     StringBuilder builder = new StringBuilder();
/* 42 */     if (input.charAt(index) == '\\' && remaining > 0 && isOctalDigit(input.charAt(index + 1))) {
/* 43 */       int next = index + 1;
/* 44 */       int next2 = index + 2;
/* 45 */       int next3 = index + 3;
/*    */ 
/*    */       
/* 48 */       builder.append(input.charAt(next));
/*    */       
/* 50 */       if (remaining > 1 && isOctalDigit(input.charAt(next2))) {
/* 51 */         builder.append(input.charAt(next2));
/* 52 */         if (remaining > 2 && isZeroToThree(input.charAt(next)) && isOctalDigit(input.charAt(next3))) {
/* 53 */           builder.append(input.charAt(next3));
/*    */         }
/*    */       } 
/*    */       
/* 57 */       out.write(Integer.parseInt(builder.toString(), 8));
/* 58 */       return 1 + builder.length();
/*    */     } 
/* 60 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isOctalDigit(char ch) {
/* 69 */     return (ch >= '0' && ch <= '7');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isZeroToThree(char ch) {
/* 78 */     return (ch >= '0' && ch <= '3');
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\translate\OctalUnescaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */