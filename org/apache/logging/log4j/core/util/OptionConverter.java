/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public final class OptionConverter
/*     */ {
/*  31 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String DELIM_START = "${";
/*     */   
/*     */   private static final char DELIM_STOP = '}';
/*     */   
/*     */   private static final int DELIM_START_LEN = 2;
/*     */   
/*     */   private static final int DELIM_STOP_LEN = 1;
/*     */   
/*     */   private static final int ONE_K = 1024;
/*     */ 
/*     */   
/*     */   public static String[] concatenateArrays(String[] l, String[] r) {
/*  46 */     int len = l.length + r.length;
/*  47 */     String[] a = new String[len];
/*     */     
/*  49 */     System.arraycopy(l, 0, a, 0, l.length);
/*  50 */     System.arraycopy(r, 0, a, l.length, r.length);
/*     */     
/*  52 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertSpecialChars(String s) {
/*  57 */     int len = s.length();
/*  58 */     StringBuilder sbuf = new StringBuilder(len);
/*     */     
/*  60 */     int i = 0;
/*  61 */     while (i < len) {
/*  62 */       char c = s.charAt(i++);
/*  63 */       if (c == '\\') {
/*  64 */         c = s.charAt(i++);
/*  65 */         switch (c) {
/*     */           case 'n':
/*  67 */             c = '\n';
/*     */             break;
/*     */           case 'r':
/*  70 */             c = '\r';
/*     */             break;
/*     */           case 't':
/*  73 */             c = '\t';
/*     */             break;
/*     */           case 'f':
/*  76 */             c = '\f';
/*     */             break;
/*     */           case 'b':
/*  79 */             c = '\b';
/*     */             break;
/*     */           case '"':
/*  82 */             c = '"';
/*     */             break;
/*     */           case '\'':
/*  85 */             c = '\'';
/*     */             break;
/*     */           case '\\':
/*  88 */             c = '\\';
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  94 */       sbuf.append(c);
/*     */     } 
/*  96 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByKey(Properties props, String key, Class<?> superClass, Object defaultValue) {
/* 103 */     String className = findAndSubst(key, props);
/* 104 */     if (className == null) {
/* 105 */       LOGGER.error("Could not find value for key {}", new Object[] { key });
/* 106 */       return defaultValue;
/*     */     } 
/*     */     
/* 109 */     return instantiateByClassName(className.trim(), superClass, defaultValue);
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
/*     */   public static boolean toBoolean(String value, boolean defaultValue) {
/* 125 */     if (value == null) {
/* 126 */       return defaultValue;
/*     */     }
/* 128 */     String trimmedVal = value.trim();
/* 129 */     if ("true".equalsIgnoreCase(trimmedVal)) {
/* 130 */       return true;
/*     */     }
/* 132 */     if ("false".equalsIgnoreCase(trimmedVal)) {
/* 133 */       return false;
/*     */     }
/* 135 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(String value, int defaultValue) {
/* 145 */     if (value != null) {
/* 146 */       String s = value.trim();
/*     */       try {
/* 148 */         return Integer.parseInt(s);
/* 149 */       } catch (NumberFormatException e) {
/* 150 */         LOGGER.error("[{}] is not in proper int form.", new Object[] { s, e });
/*     */       } 
/*     */     } 
/* 153 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toFileSize(String value, long defaultValue) {
/* 163 */     if (value == null) {
/* 164 */       return defaultValue;
/*     */     }
/*     */     
/* 167 */     String str = value.trim().toUpperCase(Locale.ENGLISH);
/* 168 */     long multiplier = 1L;
/*     */     
/*     */     int index;
/* 171 */     if ((index = str.indexOf("KB")) != -1) {
/* 172 */       multiplier = 1024L;
/* 173 */       str = str.substring(0, index);
/* 174 */     } else if ((index = str.indexOf("MB")) != -1) {
/* 175 */       multiplier = 1048576L;
/* 176 */       str = str.substring(0, index);
/* 177 */     } else if ((index = str.indexOf("GB")) != -1) {
/* 178 */       multiplier = 1073741824L;
/* 179 */       str = str.substring(0, index);
/*     */     } 
/* 181 */     if (str != null) {
/*     */       try {
/* 183 */         return Long.parseLong(str) * multiplier;
/* 184 */       } catch (NumberFormatException e) {
/* 185 */         LOGGER.error("[{}] is not in proper int form.", new Object[] { str });
/* 186 */         LOGGER.error("[{}] not in expected format.", new Object[] { value, e });
/*     */       } 
/*     */     }
/* 189 */     return defaultValue;
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
/*     */   public static String findAndSubst(String key, Properties props) {
/* 201 */     String value = props.getProperty(key);
/* 202 */     if (value == null) {
/* 203 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 207 */       return substVars(value, props);
/* 208 */     } catch (IllegalArgumentException e) {
/* 209 */       LOGGER.error("Bad option value [{}].", new Object[] { value, e });
/* 210 */       return value;
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
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, Object defaultValue) {
/* 227 */     if (className != null) {
/*     */       try {
/* 229 */         Class<?> classObj = Loader.loadClass(className);
/* 230 */         if (!superClass.isAssignableFrom(classObj)) {
/* 231 */           LOGGER.error("A \"{}\" object is not assignable to a \"{}\" variable.", new Object[] { className, superClass.getName() });
/*     */           
/* 233 */           LOGGER.error("The class \"{}\" was loaded by [{}] whereas object of type [{}] was loaded by [{}].", new Object[] { superClass.getName(), superClass.getClassLoader(), classObj.getName() });
/*     */           
/* 235 */           return defaultValue;
/*     */         } 
/* 237 */         return classObj.newInstance();
/* 238 */       } catch (Exception e) {
/* 239 */         LOGGER.error("Could not instantiate class [{}].", new Object[] { className, e });
/*     */       } 
/*     */     }
/* 242 */     return defaultValue;
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
/*     */   public static String substVars(String val, Properties props) throws IllegalArgumentException {
/* 285 */     StringBuilder sbuf = new StringBuilder();
/*     */     
/* 287 */     int i = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 292 */       int j = val.indexOf("${", i);
/* 293 */       if (j == -1) {
/*     */         
/* 295 */         if (i == 0) {
/* 296 */           return val;
/*     */         }
/*     */         
/* 299 */         sbuf.append(val.substring(i, val.length()));
/* 300 */         return sbuf.toString();
/*     */       } 
/* 302 */       sbuf.append(val.substring(i, j));
/* 303 */       int k = val.indexOf('}', j);
/* 304 */       if (k == -1) {
/* 305 */         throw new IllegalArgumentException('"' + val + "\" has no closing brace. Opening brace at position " + j + '.');
/*     */       }
/*     */ 
/*     */       
/* 309 */       j += 2;
/* 310 */       String key = val.substring(j, k);
/*     */       
/* 312 */       String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
/*     */       
/* 314 */       if (replacement == null && props != null) {
/* 315 */         replacement = props.getProperty(key);
/*     */       }
/*     */       
/* 318 */       if (replacement != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 324 */         String recursiveReplacement = substVars(replacement, props);
/* 325 */         sbuf.append(recursiveReplacement);
/*     */       } 
/* 327 */       i = k + 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\OptionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */