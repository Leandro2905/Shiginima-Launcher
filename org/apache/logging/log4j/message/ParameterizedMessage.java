/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class ParameterizedMessage
/*     */   implements Message
/*     */ {
/*     */   public static final String RECURSION_PREFIX = "[...";
/*     */   public static final String RECURSION_SUFFIX = "...]";
/*     */   public static final String ERROR_PREFIX = "[!!!";
/*     */   public static final String ERROR_SEPARATOR = "=>";
/*     */   public static final String ERROR_MSG_SEPARATOR = ":";
/*     */   public static final String ERROR_SUFFIX = "!!!]";
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   private static final int HASHVAL = 31;
/*     */   private static final char DELIM_START = '{';
/*     */   private static final char DELIM_STOP = '}';
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   private final String messagePattern;
/*     */   private final String[] stringArgs;
/*     */   private transient Object[] argArray;
/*     */   private transient String formattedMessage;
/*     */   private transient Throwable throwable;
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, String[] stringArgs, Throwable throwable) {
/*  85 */     this.messagePattern = messagePattern;
/*  86 */     this.stringArgs = stringArgs;
/*  87 */     this.throwable = throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object[] objectArgs, Throwable throwable) {
/*  98 */     this.messagePattern = messagePattern;
/*  99 */     this.throwable = throwable;
/* 100 */     this.stringArgs = parseArguments(objectArgs);
/*     */   }
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
/*     */   public ParameterizedMessage(String messagePattern, Object[] arguments) {
/* 116 */     this.messagePattern = messagePattern;
/* 117 */     this.stringArgs = parseArguments(arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg) {
/* 126 */     this(messagePattern, new Object[] { arg });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg1, Object arg2) {
/* 136 */     this(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   private String[] parseArguments(Object[] arguments) {
/*     */     String[] strArgs;
/* 140 */     if (arguments == null) {
/* 141 */       return null;
/*     */     }
/* 143 */     int argsCount = countArgumentPlaceholders(this.messagePattern);
/* 144 */     int resultArgCount = arguments.length;
/* 145 */     if (argsCount < arguments.length && this.throwable == null && arguments[arguments.length - 1] instanceof Throwable) {
/* 146 */       this.throwable = (Throwable)arguments[arguments.length - 1];
/* 147 */       resultArgCount--;
/*     */     } 
/* 149 */     this.argArray = new Object[resultArgCount];
/* 150 */     for (int i = 0; i < resultArgCount; i++) {
/* 151 */       this.argArray[i] = arguments[i];
/*     */     }
/*     */ 
/*     */     
/* 155 */     if (argsCount == 1 && this.throwable == null && arguments.length > 1) {
/*     */       
/* 157 */       strArgs = new String[1];
/* 158 */       strArgs[0] = deepToString(arguments);
/*     */     } else {
/* 160 */       strArgs = new String[resultArgCount];
/* 161 */       for (int j = 0; j < strArgs.length; j++) {
/* 162 */         strArgs[j] = deepToString(arguments[j]);
/*     */       }
/*     */     } 
/* 165 */     return strArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 174 */     if (this.formattedMessage == null) {
/* 175 */       this.formattedMessage = formatMessage(this.messagePattern, this.stringArgs);
/*     */     }
/* 177 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 186 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 195 */     if (this.argArray != null) {
/* 196 */       return this.argArray;
/*     */     }
/* 198 */     return (Object[])this.stringArgs;
/*     */   }
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
/*     */   public Throwable getThrowable() {
/* 212 */     return this.throwable;
/*     */   }
/*     */   
/*     */   protected String formatMessage(String msgPattern, String[] sArgs) {
/* 216 */     return format(msgPattern, (Object[])sArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 221 */     if (this == o) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (o == null || getClass() != o.getClass()) {
/* 225 */       return false;
/*     */     }
/*     */     
/* 228 */     ParameterizedMessage that = (ParameterizedMessage)o;
/*     */     
/* 230 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 231 */       return false;
/*     */     }
/* 233 */     if (!Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs)) {
/* 234 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 238 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 243 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 244 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 245 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(String messagePattern, Object[] arguments) {
/* 256 */     if (messagePattern == null || arguments == null || arguments.length == 0) {
/* 257 */       return messagePattern;
/*     */     }
/*     */     
/* 260 */     StringBuilder result = new StringBuilder();
/* 261 */     int escapeCounter = 0;
/* 262 */     int currentArgument = 0;
/* 263 */     for (int i = 0; i < messagePattern.length(); i++) {
/* 264 */       char curChar = messagePattern.charAt(i);
/* 265 */       if (curChar == '\\') {
/* 266 */         escapeCounter++;
/*     */       }
/* 268 */       else if (curChar == '{' && i < messagePattern.length() - 1 && messagePattern.charAt(i + 1) == '}') {
/*     */ 
/*     */         
/* 271 */         int escapedEscapes = escapeCounter / 2;
/* 272 */         for (int j = 0; j < escapedEscapes; j++) {
/* 273 */           result.append('\\');
/*     */         }
/*     */         
/* 276 */         if (escapeCounter % 2 == 1) {
/*     */ 
/*     */           
/* 279 */           result.append('{');
/* 280 */           result.append('}');
/*     */         } else {
/*     */           
/* 283 */           if (currentArgument < arguments.length) {
/* 284 */             result.append(arguments[currentArgument]);
/*     */           } else {
/* 286 */             result.append('{').append('}');
/*     */           } 
/* 288 */           currentArgument++;
/*     */         } 
/* 290 */         i++;
/* 291 */         escapeCounter = 0;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 296 */         if (escapeCounter > 0) {
/* 297 */           for (int j = 0; j < escapeCounter; j++) {
/* 298 */             result.append('\\');
/*     */           }
/* 300 */           escapeCounter = 0;
/*     */         } 
/* 302 */         result.append(curChar);
/*     */       } 
/*     */     } 
/* 305 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countArgumentPlaceholders(String messagePattern) {
/* 315 */     if (messagePattern == null) {
/* 316 */       return 0;
/*     */     }
/*     */     
/* 319 */     int delim = messagePattern.indexOf('{');
/*     */     
/* 321 */     if (delim == -1)
/*     */     {
/* 323 */       return 0;
/*     */     }
/* 325 */     int result = 0;
/* 326 */     boolean isEscaped = false;
/* 327 */     for (int i = 0; i < messagePattern.length(); i++) {
/* 328 */       char curChar = messagePattern.charAt(i);
/* 329 */       if (curChar == '\\') {
/* 330 */         isEscaped = !isEscaped;
/* 331 */       } else if (curChar == '{') {
/* 332 */         if (!isEscaped && i < messagePattern.length() - 1 && messagePattern.charAt(i + 1) == '}') {
/* 333 */           result++;
/* 334 */           i++;
/*     */         } 
/* 336 */         isEscaped = false;
/*     */       } else {
/* 338 */         isEscaped = false;
/*     */       } 
/*     */     } 
/* 341 */     return result;
/*     */   }
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
/*     */   public static String deepToString(Object o) {
/* 361 */     if (o == null) {
/* 362 */       return null;
/*     */     }
/* 364 */     if (o instanceof String) {
/* 365 */       return (String)o;
/*     */     }
/* 367 */     StringBuilder str = new StringBuilder();
/* 368 */     Set<String> dejaVu = new HashSet<String>();
/* 369 */     recursiveDeepToString(o, str, dejaVu);
/* 370 */     return str.toString();
/*     */   }
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
/*     */   private static void recursiveDeepToString(Object o, StringBuilder str, Set<String> dejaVu) {
/* 395 */     if (o == null) {
/* 396 */       str.append("null");
/*     */       return;
/*     */     } 
/* 399 */     if (o instanceof String) {
/* 400 */       str.append(o);
/*     */       
/*     */       return;
/*     */     } 
/* 404 */     Class<?> oClass = o.getClass();
/* 405 */     if (oClass.isArray()) {
/* 406 */       if (oClass == byte[].class) {
/* 407 */         str.append(Arrays.toString((byte[])o));
/* 408 */       } else if (oClass == short[].class) {
/* 409 */         str.append(Arrays.toString((short[])o));
/* 410 */       } else if (oClass == int[].class) {
/* 411 */         str.append(Arrays.toString((int[])o));
/* 412 */       } else if (oClass == long[].class) {
/* 413 */         str.append(Arrays.toString((long[])o));
/* 414 */       } else if (oClass == float[].class) {
/* 415 */         str.append(Arrays.toString((float[])o));
/* 416 */       } else if (oClass == double[].class) {
/* 417 */         str.append(Arrays.toString((double[])o));
/* 418 */       } else if (oClass == boolean[].class) {
/* 419 */         str.append(Arrays.toString((boolean[])o));
/* 420 */       } else if (oClass == char[].class) {
/* 421 */         str.append(Arrays.toString((char[])o));
/*     */       } else {
/*     */         
/* 424 */         String id = identityToString(o);
/* 425 */         if (dejaVu.contains(id)) {
/* 426 */           str.append("[...").append(id).append("...]");
/*     */         } else {
/* 428 */           dejaVu.add(id);
/* 429 */           Object[] oArray = (Object[])o;
/* 430 */           str.append('[');
/* 431 */           boolean first = true;
/* 432 */           for (Object current : oArray) {
/* 433 */             if (first) {
/* 434 */               first = false;
/*     */             } else {
/* 436 */               str.append(", ");
/*     */             } 
/* 438 */             recursiveDeepToString(current, str, new HashSet<String>(dejaVu));
/*     */           } 
/* 440 */           str.append(']');
/*     */         }
/*     */       
/*     */       } 
/* 444 */     } else if (o instanceof Map) {
/*     */       
/* 446 */       String id = identityToString(o);
/* 447 */       if (dejaVu.contains(id)) {
/* 448 */         str.append("[...").append(id).append("...]");
/*     */       } else {
/* 450 */         dejaVu.add(id);
/* 451 */         Map<?, ?> oMap = (Map<?, ?>)o;
/* 452 */         str.append('{');
/* 453 */         boolean isFirst = true;
/* 454 */         for (Map.Entry<?, ?> o1 : oMap.entrySet()) {
/* 455 */           Map.Entry<?, ?> current = o1;
/* 456 */           if (isFirst) {
/* 457 */             isFirst = false;
/*     */           } else {
/* 459 */             str.append(", ");
/*     */           } 
/* 461 */           Object key = current.getKey();
/* 462 */           Object value = current.getValue();
/* 463 */           recursiveDeepToString(key, str, new HashSet<String>(dejaVu));
/* 464 */           str.append('=');
/* 465 */           recursiveDeepToString(value, str, new HashSet<String>(dejaVu));
/*     */         } 
/* 467 */         str.append('}');
/*     */       } 
/* 469 */     } else if (o instanceof Collection) {
/*     */       
/* 471 */       String id = identityToString(o);
/* 472 */       if (dejaVu.contains(id)) {
/* 473 */         str.append("[...").append(id).append("...]");
/*     */       } else {
/* 475 */         dejaVu.add(id);
/* 476 */         Collection<?> oCol = (Collection)o;
/* 477 */         str.append('[');
/* 478 */         boolean isFirst = true;
/* 479 */         for (Object anOCol : oCol) {
/* 480 */           if (isFirst) {
/* 481 */             isFirst = false;
/*     */           } else {
/* 483 */             str.append(", ");
/*     */           } 
/* 485 */           recursiveDeepToString(anOCol, str, new HashSet<String>(dejaVu));
/*     */         } 
/* 487 */         str.append(']');
/*     */       } 
/* 489 */     } else if (o instanceof Date) {
/* 490 */       Date date = (Date)o;
/* 491 */       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
/*     */       
/* 493 */       str.append(format.format(date));
/*     */     } else {
/*     */       
/*     */       try {
/* 497 */         str.append(o.toString());
/* 498 */       } catch (Throwable t) {
/* 499 */         str.append("[!!!");
/* 500 */         str.append(identityToString(o));
/* 501 */         str.append("=>");
/* 502 */         String msg = t.getMessage();
/* 503 */         String className = t.getClass().getName();
/* 504 */         str.append(className);
/* 505 */         if (!className.equals(msg)) {
/* 506 */           str.append(":");
/* 507 */           str.append(msg);
/*     */         } 
/* 509 */         str.append("!!!]");
/*     */       } 
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
/*     */   public static String identityToString(Object obj) {
/* 533 */     if (obj == null) {
/* 534 */       return null;
/*     */     }
/* 536 */     return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 541 */     return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + Arrays.toString((Object[])this.stringArgs) + ", throwable=" + this.throwable + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\ParameterizedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */