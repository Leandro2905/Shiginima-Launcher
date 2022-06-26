/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ public class TlsSyslogFrame
/*    */ {
/*    */   public static final char SPACE = ' ';
/*    */   private String message;
/*    */   private int messageLengthInBytes;
/*    */   
/*    */   public TlsSyslogFrame(String message) {
/* 31 */     setMessage(message);
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 35 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 39 */     this.message = message;
/* 40 */     setLengthInBytes();
/*    */   }
/*    */   
/*    */   private void setLengthInBytes() {
/* 44 */     this.messageLengthInBytes = this.message.length();
/*    */   }
/*    */   
/*    */   public byte[] getBytes() {
/* 48 */     String frame = toString();
/* 49 */     return frame.getBytes(Charset.defaultCharset());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     String length = Integer.toString(this.messageLengthInBytes);
/* 55 */     return length + ' ' + this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object frame) {
/* 60 */     return super.equals(frame);
/*    */   }
/*    */   
/*    */   public boolean equals(TlsSyslogFrame frame) {
/* 64 */     return (isLengthEquals(frame) && isMessageEquals(frame));
/*    */   }
/*    */   
/*    */   private boolean isLengthEquals(TlsSyslogFrame frame) {
/* 68 */     return (this.messageLengthInBytes == frame.messageLengthInBytes);
/*    */   }
/*    */   
/*    */   private boolean isMessageEquals(TlsSyslogFrame frame) {
/* 72 */     return this.message.equals(frame.message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\TlsSyslogFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */