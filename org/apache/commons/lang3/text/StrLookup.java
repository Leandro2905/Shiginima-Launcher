/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public abstract class StrLookup<V>
/*     */ {
/*  44 */   private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<String>(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrLookup<?> noneLookup() {
/*  53 */     return NONE_LOOKUP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties copyProperties(Properties input) {
/*  63 */     if (input == null) {
/*  64 */       return null;
/*     */     }
/*     */     
/*  67 */     Properties output = new Properties();
/*     */     
/*  69 */     Enumeration<String> propertyNames = (Enumeration)input.propertyNames();
/*     */     
/*  71 */     while (propertyNames.hasMoreElements()) {
/*  72 */       String propertyName = propertyNames.nextElement();
/*  73 */       output.setProperty(propertyName, input.getProperty(propertyName));
/*     */     } 
/*     */     
/*  76 */     return output;
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
/*     */   public static StrLookup<String> systemPropertiesLookup() {
/*  91 */     Properties systemProperties = null;
/*     */     
/*     */     try {
/*  94 */       systemProperties = System.getProperties();
/*  95 */     } catch (SecurityException ex) {}
/*     */ 
/*     */ 
/*     */     
/*  99 */     Properties properties = copyProperties(systemProperties);
/*     */     
/* 101 */     Map<String, String> propertiesMap = properties;
/*     */     
/* 103 */     return new MapStrLookup<String>(propertiesMap);
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
/*     */   public static <V> StrLookup<V> mapLookup(Map<String, V> map) {
/* 117 */     return new MapStrLookup<V>(map);
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
/*     */   public abstract String lookup(String paramString);
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
/*     */   static class MapStrLookup<V>
/*     */     extends StrLookup<V>
/*     */   {
/*     */     private final Map<String, V> map;
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
/*     */     MapStrLookup(Map<String, V> map) {
/* 168 */       this.map = map;
/*     */     }
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
/*     */     public String lookup(String key) {
/* 182 */       if (this.map == null) {
/* 183 */         return null;
/*     */       }
/* 185 */       Object obj = this.map.get(key);
/* 186 */       if (obj == null) {
/* 187 */         return null;
/*     */       }
/* 189 */       return obj.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\StrLookup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */