/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*    */ import org.apache.logging.log4j.core.util.Constants;
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
/*    */ @Plugin(name = "RootThrowablePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"rEx", "rThrowable", "rException"})
/*    */ public final class RootThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private RootThrowablePatternConverter(String[] options) {
/* 43 */     super("RootThrowable", "throwable", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RootThrowablePatternConverter newInstance(String[] options) {
/* 54 */     return new RootThrowablePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 62 */     ThrowableProxy proxy = null;
/* 63 */     if (event instanceof Log4jLogEvent) {
/* 64 */       proxy = ((Log4jLogEvent)event).getThrownProxy();
/*    */     }
/* 66 */     Throwable throwable = event.getThrown();
/* 67 */     if (throwable != null && this.options.anyLines()) {
/* 68 */       if (proxy == null) {
/* 69 */         super.format(event, toAppendTo);
/*    */         return;
/*    */       } 
/* 72 */       String trace = proxy.getCauseStackTraceAsString(this.options.getPackages());
/* 73 */       int len = toAppendTo.length();
/* 74 */       if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
/* 75 */         toAppendTo.append(' ');
/*    */       }
/* 77 */       if (!this.options.allLines() || !Constants.LINE_SEPARATOR.equals(this.options.getSeparator())) {
/* 78 */         StringBuilder sb = new StringBuilder();
/* 79 */         String[] array = trace.split(Constants.LINE_SEPARATOR);
/* 80 */         int limit = this.options.minLines(array.length) - 1;
/* 81 */         for (int i = 0; i <= limit; i++) {
/* 82 */           sb.append(array[i]);
/* 83 */           if (i < limit) {
/* 84 */             sb.append(this.options.getSeparator());
/*    */           }
/*    */         } 
/* 87 */         toAppendTo.append(sb.toString());
/*    */       } else {
/*    */         
/* 90 */         toAppendTo.append(trace);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\RootThrowablePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */