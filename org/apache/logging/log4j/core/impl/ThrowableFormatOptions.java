/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ public final class ThrowableFormatOptions
/*     */ {
/*     */   private static final int DEFAULT_LINES = 2147483647;
/*  36 */   protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FULL = "full";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NONE = "none";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SHORT = "short";
/*     */ 
/*     */ 
/*     */   
/*     */   private final int lines;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String separator;
/*     */ 
/*     */   
/*     */   private final List<String> packages;
/*     */ 
/*     */   
/*     */   public static final String CLASS_NAME = "short.className";
/*     */ 
/*     */   
/*     */   public static final String METHOD_NAME = "short.methodName";
/*     */ 
/*     */   
/*     */   public static final String LINE_NUMBER = "short.lineNumber";
/*     */ 
/*     */   
/*     */   public static final String FILE_NAME = "short.fileName";
/*     */ 
/*     */   
/*     */   public static final String MESSAGE = "short.message";
/*     */ 
/*     */   
/*     */   public static final String LOCALIZED_MESSAGE = "short.localizedMessage";
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(int lines, String separator, List<String> packages) {
/*  82 */     this.lines = lines;
/*  83 */     this.separator = (separator == null) ? Constants.LINE_SEPARATOR : separator;
/*  84 */     this.packages = packages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(List<String> packages) {
/*  92 */     this(2147483647, null, packages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions() {
/*  99 */     this(2147483647, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLines() {
/* 107 */     return this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 115 */     return this.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getPackages() {
/* 123 */     return this.packages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean allLines() {
/* 131 */     return (this.lines == Integer.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean anyLines() {
/* 139 */     return (this.lines > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int minLines(int maxLines) {
/* 148 */     return (this.lines > maxLines) ? maxLines : this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPackages() {
/* 156 */     return (this.packages != null && !this.packages.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     StringBuilder s = new StringBuilder();
/* 165 */     s.append('{').append(allLines() ? "full" : ((this.lines == 2) ? "short" : (anyLines() ? String.valueOf(this.lines) : "none"))).append('}');
/* 166 */     s.append("{separator(").append(this.separator).append(")}");
/* 167 */     if (hasPackages()) {
/* 168 */       s.append("{filters(");
/* 169 */       for (String p : this.packages) {
/* 170 */         s.append(p).append(',');
/*     */       }
/* 172 */       s.deleteCharAt(s.length() - 1);
/* 173 */       s.append(")}");
/*     */     } 
/* 175 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThrowableFormatOptions newInstance(String[] options) {
/* 183 */     if (options == null || options.length == 0) {
/* 184 */       return DEFAULT;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     if (options.length == 1 && options[0] != null && options[0].length() > 0) {
/* 193 */       String[] opts = options[0].split(Patterns.COMMA_SEPARATOR, 2);
/* 194 */       String first = opts[0].trim();
/* 195 */       Scanner scanner = new Scanner(first);
/* 196 */       if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first.equalsIgnoreCase("none") || scanner.hasNextInt())) {
/* 197 */         options = new String[] { first, opts[1].trim() };
/*     */       }
/* 199 */       scanner.close();
/*     */     } 
/*     */     
/* 202 */     int lines = DEFAULT.lines;
/* 203 */     String separator = DEFAULT.separator;
/* 204 */     List<String> packages = DEFAULT.packages;
/* 205 */     for (String rawOption : options) {
/* 206 */       if (rawOption != null) {
/* 207 */         String option = rawOption.trim();
/* 208 */         if (!option.isEmpty())
/*     */         {
/* 210 */           if (option.startsWith("separator(") && option.endsWith(")")) {
/* 211 */             separator = option.substring("separator(".length(), option.length() - 1);
/* 212 */           } else if (option.startsWith("filters(") && option.endsWith(")")) {
/* 213 */             String filterStr = option.substring("filters(".length(), option.length() - 1);
/* 214 */             if (filterStr.length() > 0) {
/* 215 */               String[] array = filterStr.split(Patterns.COMMA_SEPARATOR);
/* 216 */               if (array.length > 0) {
/* 217 */                 packages = new ArrayList<String>(array.length);
/* 218 */                 for (String token : array) {
/* 219 */                   token = token.trim();
/* 220 */                   if (token.length() > 0) {
/* 221 */                     packages.add(token);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/* 226 */           } else if (option.equalsIgnoreCase("none")) {
/* 227 */             lines = 0;
/* 228 */           } else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option.equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option.equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option.equalsIgnoreCase("short.localizedMessage")) {
/*     */ 
/*     */ 
/*     */             
/* 232 */             lines = 2;
/* 233 */           } else if (!option.equalsIgnoreCase("full")) {
/* 234 */             lines = Integer.parseInt(option);
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 238 */     return new ThrowableFormatOptions(lines, separator, packages);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\ThrowableFormatOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */