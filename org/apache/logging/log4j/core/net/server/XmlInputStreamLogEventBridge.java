/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
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
/*    */ public class XmlInputStreamLogEventBridge
/*    */   extends InputStreamLogEventBridge
/*    */ {
/*    */   private static final String EVENT_END = "</Event>";
/*    */   private static final String EVENT_START_NS_N = "<Event>";
/*    */   private static final String EVENT_START_NS_Y = "<Event ";
/*    */   
/*    */   public XmlInputStreamLogEventBridge() {
/* 35 */     this(1024, Charset.defaultCharset());
/*    */   }
/*    */   
/*    */   public XmlInputStreamLogEventBridge(int bufferSize, Charset charset) {
/* 39 */     super((ObjectMapper)new Log4jXmlObjectMapper(), bufferSize, charset, "</Event>");
/*    */   }
/*    */ 
/*    */   
/*    */   protected int[] getEventIndices(String text, int beginIndex) {
/* 44 */     int start = text.indexOf("<Event ", beginIndex);
/* 45 */     int startLen = "<Event ".length();
/* 46 */     if (start < 0) {
/* 47 */       start = text.indexOf("<Event>", beginIndex);
/* 48 */       startLen = "<Event>".length();
/*    */     } 
/* 50 */     int end = (start < 0) ? -1 : text.indexOf("</Event>", start + startLen);
/* 51 */     return new int[] { start, end };
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\XmlInputStreamLogEventBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */