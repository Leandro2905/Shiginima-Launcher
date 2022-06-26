/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
/*    */ import java.io.Serializable;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ abstract class AbstractJacksonLayout
/*    */   extends AbstractStringLayout
/*    */ {
/*    */   protected static final String DEFAULT_EOL = "\r\n";
/*    */   protected static final String COMPACT_EOL = "";
/*    */   protected final String eol;
/*    */   protected final ObjectWriter objectWriter;
/*    */   protected final boolean compact;
/*    */   protected final boolean complete;
/*    */   
/*    */   protected AbstractJacksonLayout(ObjectWriter objectWriter, Charset charset, boolean compact, boolean complete) {
/* 37 */     super(charset);
/* 38 */     this.objectWriter = objectWriter;
/* 39 */     this.compact = compact;
/* 40 */     this.complete = complete;
/* 41 */     this.eol = compact ? "" : "\r\n";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toSerializable(LogEvent event) {
/*    */     try {
/* 53 */       return this.objectWriter.writeValueAsString(event);
/* 54 */     } catch (JsonProcessingException e) {
/*    */       
/* 56 */       LOGGER.error(e);
/* 57 */       return "";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\AbstractJacksonLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */