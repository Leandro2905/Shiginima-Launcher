/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ThrowablePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"ex", "throwable", "exception"})
/*     */ public class ThrowablePatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private String rawOption;
/*     */   protected final ThrowableFormatOptions options;
/*     */   
/*     */   protected ThrowablePatternConverter(String name, String style, String[] options) {
/*  52 */     super(name, style);
/*  53 */     this.options = ThrowableFormatOptions.newInstance(options);
/*  54 */     if (options != null && options.length > 0) {
/*  55 */       this.rawOption = options[0];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThrowablePatternConverter newInstance(String[] options) {
/*  67 */     return new ThrowablePatternConverter("Throwable", "throwable", options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder buffer) {
/*  75 */     Throwable t = event.getThrown();
/*     */     
/*  77 */     if (isSubShortOption()) {
/*  78 */       formatSubShortOption(t, buffer);
/*     */     }
/*  80 */     else if (t != null && this.options.anyLines()) {
/*  81 */       formatOption(t, buffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSubShortOption() {
/*  86 */     return ("short.message".equalsIgnoreCase(this.rawOption) || "short.localizedMessage".equalsIgnoreCase(this.rawOption) || "short.fileName".equalsIgnoreCase(this.rawOption) || "short.lineNumber".equalsIgnoreCase(this.rawOption) || "short.methodName".equalsIgnoreCase(this.rawOption) || "short.className".equalsIgnoreCase(this.rawOption));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatSubShortOption(Throwable t, StringBuilder buffer) {
/*  96 */     StackTraceElement throwingMethod = null;
/*     */ 
/*     */     
/*  99 */     if (t != null) {
/* 100 */       StackTraceElement[] trace = t.getStackTrace();
/* 101 */       if (trace != null && trace.length > 0) {
/* 102 */         throwingMethod = trace[0];
/*     */       }
/*     */     } 
/*     */     
/* 106 */     if (t != null && throwingMethod != null) {
/* 107 */       String toAppend = "";
/*     */       
/* 109 */       if ("short.className".equalsIgnoreCase(this.rawOption)) {
/* 110 */         toAppend = throwingMethod.getClassName();
/*     */       }
/* 112 */       else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
/* 113 */         toAppend = throwingMethod.getMethodName();
/*     */       }
/* 115 */       else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
/* 116 */         toAppend = String.valueOf(throwingMethod.getLineNumber());
/*     */       }
/* 118 */       else if ("short.message".equalsIgnoreCase(this.rawOption)) {
/* 119 */         toAppend = t.getMessage();
/*     */       }
/* 121 */       else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
/* 122 */         toAppend = t.getLocalizedMessage();
/*     */       }
/* 124 */       else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
/* 125 */         toAppend = throwingMethod.getFileName();
/*     */       } 
/*     */       
/* 128 */       int len = buffer.length();
/* 129 */       if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
/* 130 */         buffer.append(' ');
/*     */       }
/* 132 */       buffer.append(toAppend);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatOption(Throwable throwable, StringBuilder buffer) {
/* 137 */     StringWriter w = new StringWriter();
/*     */     
/* 139 */     throwable.printStackTrace(new PrintWriter(w));
/* 140 */     int len = buffer.length();
/* 141 */     if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
/* 142 */       buffer.append(' ');
/*     */     }
/* 144 */     if (!this.options.allLines() || !Constants.LINE_SEPARATOR.equals(this.options.getSeparator())) {
/* 145 */       StringBuilder sb = new StringBuilder();
/* 146 */       String[] array = w.toString().split(Constants.LINE_SEPARATOR);
/* 147 */       int limit = this.options.minLines(array.length) - 1;
/* 148 */       for (int i = 0; i <= limit; i++) {
/* 149 */         sb.append(array[i]);
/* 150 */         if (i < limit) {
/* 151 */           sb.append(this.options.getSeparator());
/*     */         }
/*     */       } 
/* 154 */       buffer.append(sb.toString());
/*     */     } else {
/*     */       
/* 157 */       buffer.append(w.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\ThrowablePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */