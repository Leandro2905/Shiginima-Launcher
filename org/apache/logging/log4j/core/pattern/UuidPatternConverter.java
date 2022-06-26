/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.util.UuidUtil;
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
/*    */ @Plugin(name = "UuidPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"u", "uuid"})
/*    */ public final class UuidPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final boolean isRandom;
/*    */   
/*    */   private UuidPatternConverter(boolean isRandom) {
/* 38 */     super("u", "uuid");
/* 39 */     this.isRandom = isRandom;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static UuidPatternConverter newInstance(String[] options) {
/* 49 */     if (options.length == 0) {
/* 50 */       return new UuidPatternConverter(false);
/*    */     }
/*    */     
/* 53 */     if (options.length > 1 || (!options[0].equalsIgnoreCase("RANDOM") && !options[0].equalsIgnoreCase("Time"))) {
/* 54 */       LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\"");
/*    */     }
/* 56 */     return new UuidPatternConverter(options[0].equalsIgnoreCase("RANDOM"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 64 */     UUID uuid = this.isRandom ? UUID.randomUUID() : UuidUtil.getTimeBasedUuid();
/* 65 */     toAppendTo.append(uuid.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\UuidPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */