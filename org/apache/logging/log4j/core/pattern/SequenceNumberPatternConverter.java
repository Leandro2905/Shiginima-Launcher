/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "SequenceNumberPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"sn", "sequenceNumber"})
/*    */ public final class SequenceNumberPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 32 */   private static final AtomicLong SEQUENCE = new AtomicLong();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SequenceNumberPatternConverter() {
/* 44 */     super("Sequence Number", "sn");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SequenceNumberPatternConverter newInstance(String[] options) {
/* 55 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 63 */     toAppendTo.append(Long.toString(SEQUENCE.incrementAndGet()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\SequenceNumberPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */