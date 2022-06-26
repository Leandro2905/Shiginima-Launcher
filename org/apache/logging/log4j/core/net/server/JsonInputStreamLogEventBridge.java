/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
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
/*    */ public class JsonInputStreamLogEventBridge
/*    */   extends InputStreamLogEventBridge
/*    */ {
/* 30 */   private static final int[] END_PAIR = new int[] { -1, -1 };
/*    */   private static final char EVENT_END_MARKER = '}';
/*    */   private static final char EVENT_START_MARKER = '{';
/*    */   private static final char JSON_ESC = '\\';
/*    */   private static final char JSON_STR_DELIM = '"';
/*    */   
/*    */   public JsonInputStreamLogEventBridge() {
/* 37 */     this(1024, Charset.defaultCharset());
/*    */   }
/*    */   
/*    */   public JsonInputStreamLogEventBridge(int bufferSize, Charset charset) {
/* 41 */     super((ObjectMapper)new Log4jJsonObjectMapper(), bufferSize, charset, String.valueOf('}'));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int[] getEventIndices(String text, int beginIndex) {
/* 47 */     int start = text.indexOf('{', beginIndex);
/* 48 */     if (start == -1) {
/* 49 */       return END_PAIR;
/*    */     }
/* 51 */     char[] charArray = text.toCharArray();
/* 52 */     int stack = 0;
/* 53 */     boolean inStr = false;
/* 54 */     boolean inEsc = false;
/* 55 */     for (int i = start; i < charArray.length; i++) {
/* 56 */       char c = charArray[i];
/* 57 */       if (!inEsc) {
/* 58 */         inEsc = false;
/* 59 */         switch (c) {
/*    */           case '{':
/* 61 */             if (!inStr) {
/* 62 */               stack++;
/*    */             }
/*    */             break;
/*    */           case '}':
/* 66 */             if (!inStr) {
/* 67 */               stack--;
/*    */             }
/*    */             break;
/*    */           case '"':
/* 71 */             inStr = !inStr;
/*    */             break;
/*    */           case '\\':
/* 74 */             inEsc = true;
/*    */             break;
/*    */         } 
/* 77 */         if (stack == 0) {
/* 78 */           return new int[] { start, i };
/*    */         }
/*    */       } 
/*    */     } 
/* 82 */     return END_PAIR;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\JsonInputStreamLogEventBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */