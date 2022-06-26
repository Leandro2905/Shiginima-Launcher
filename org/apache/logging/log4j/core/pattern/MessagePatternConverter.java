/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.message.MultiformatMessage;
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
/*    */ @Plugin(name = "MessagePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"m", "msg", "message"})
/*    */ public final class MessagePatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final String[] formats;
/*    */   private final Configuration config;
/*    */   
/*    */   private MessagePatternConverter(Configuration config, String[] options) {
/* 41 */     super("Message", "message");
/* 42 */     this.formats = options;
/* 43 */     this.config = config;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MessagePatternConverter newInstance(Configuration config, String[] options) {
/* 54 */     return new MessagePatternConverter(config, options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 62 */     Message msg = event.getMessage();
/* 63 */     if (msg != null) {
/*    */       String result;
/* 65 */       if (msg instanceof MultiformatMessage) {
/* 66 */         result = ((MultiformatMessage)msg).getFormattedMessage(this.formats);
/*    */       } else {
/* 68 */         result = msg.getFormattedMessage();
/*    */       } 
/* 70 */       if (result != null) {
/* 71 */         toAppendTo.append((this.config != null && result.contains("${")) ? this.config.getStrSubstitutor().replace(event, result) : result);
/*    */       } else {
/*    */         
/* 74 */         toAppendTo.append("null");
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\MessagePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */