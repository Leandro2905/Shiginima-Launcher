/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.util.Charsets;
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
/*    */ public abstract class AbstractStringLayout
/*    */   extends AbstractLayout<String>
/*    */ {
/*    */   private final Charset charset;
/*    */   
/*    */   protected AbstractStringLayout(Charset charset, byte[] header, byte[] footer) {
/* 35 */     super(header, footer);
/* 36 */     this.charset = (charset == null) ? Charsets.UTF_8 : charset;
/*    */   }
/*    */   
/*    */   protected AbstractStringLayout(Charset charset) {
/* 40 */     this(charset, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] toByteArray(LogEvent event) {
/* 52 */     return ((String)toSerializable(event)).getBytes(this.charset);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 60 */     return "text/plain";
/*    */   }
/*    */   
/*    */   protected Charset getCharset() {
/* 64 */     return this.charset;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\AbstractStringLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */