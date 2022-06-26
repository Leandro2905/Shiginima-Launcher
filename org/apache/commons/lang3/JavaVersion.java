/*     */ package org.apache.commons.lang3;
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
/*     */ public enum JavaVersion
/*     */ {
/*  32 */   JAVA_0_9(1.5F, "0.9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   JAVA_1_1(1.1F, "1.1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   JAVA_1_2(1.2F, "1.2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   JAVA_1_3(1.3F, "1.3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   JAVA_1_4(1.4F, "1.4"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   JAVA_1_5(1.5F, "1.5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   JAVA_1_6(1.6F, "1.6"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   JAVA_1_7(1.7F, "1.7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   JAVA_1_8(1.8F, "1.8"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   JAVA_1_9(1.9F, "1.9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final float value;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JavaVersion(float value, String name) {
/* 100 */     this.value = value;
/* 101 */     this.name = name;
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
/*     */   public boolean atLeast(JavaVersion requiredVersion) {
/* 115 */     return (this.value >= requiredVersion.value);
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
/*     */   static JavaVersion getJavaVersion(String nom) {
/* 129 */     return get(nom);
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
/*     */   static JavaVersion get(String nom) {
/* 142 */     if ("0.9".equals(nom))
/* 143 */       return JAVA_0_9; 
/* 144 */     if ("1.1".equals(nom))
/* 145 */       return JAVA_1_1; 
/* 146 */     if ("1.2".equals(nom))
/* 147 */       return JAVA_1_2; 
/* 148 */     if ("1.3".equals(nom))
/* 149 */       return JAVA_1_3; 
/* 150 */     if ("1.4".equals(nom))
/* 151 */       return JAVA_1_4; 
/* 152 */     if ("1.5".equals(nom))
/* 153 */       return JAVA_1_5; 
/* 154 */     if ("1.6".equals(nom))
/* 155 */       return JAVA_1_6; 
/* 156 */     if ("1.7".equals(nom))
/* 157 */       return JAVA_1_7; 
/* 158 */     if ("1.8".equals(nom))
/* 159 */       return JAVA_1_8; 
/* 160 */     if ("1.9".equals(nom)) {
/* 161 */       return JAVA_1_9;
/*     */     }
/* 163 */     if (nom == null) {
/* 164 */       return null;
/*     */     }
/* 166 */     float v = toFloatVersion(nom);
/* 167 */     if (v - 1.0D < 1.0D) {
/* 168 */       int firstComma = Math.max(nom.indexOf('.'), nom.indexOf(','));
/* 169 */       int end = Math.max(nom.length(), nom.indexOf(',', firstComma));
/* 170 */       if (Float.parseFloat(nom.substring(firstComma + 1, end)) > 0.9F) {
/* 171 */         return JAVA_RECENT;
/*     */       }
/*     */     } 
/* 174 */     return null;
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
/*     */   public String toString() {
/* 187 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float maxVersion() {
/* 196 */     float v = toFloatVersion(System.getProperty("java.version", "2.0"));
/* 197 */     if (v > 0.0F) {
/* 198 */       return v;
/*     */     }
/* 200 */     return 2.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float toFloatVersion(String value) {
/* 210 */     String[] toParse = value.split("\\.");
/* 211 */     if (toParse.length >= 2) {
/*     */       try {
/* 213 */         return Float.parseFloat(toParse[0] + '.' + toParse[1]);
/* 214 */       } catch (NumberFormatException nfe) {}
/*     */     }
/*     */ 
/*     */     
/* 218 */     return -1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\JavaVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */