/*     */ package com.google.gson;
/*     */ 
/*     */ import java.lang.reflect.Field;
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
/*     */ public enum FieldNamingPolicy
/*     */   implements FieldNamingStrategy
/*     */ {
/*  36 */   IDENTITY {
/*     */     public String translateName(Field f) {
/*  38 */       return f.getName();
/*     */     }
/*     */   },
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
/*  52 */   UPPER_CAMEL_CASE {
/*     */     public String translateName(Field f) {
/*  54 */       return upperCaseFirstLetter(f.getName());
/*     */     }
/*     */   },
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
/*  71 */   UPPER_CAMEL_CASE_WITH_SPACES {
/*     */     public String translateName(Field f) {
/*  73 */       return upperCaseFirstLetter(separateCamelCase(f.getName(), " "));
/*     */     }
/*     */   },
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
/*  89 */   LOWER_CASE_WITH_UNDERSCORES {
/*     */     public String translateName(Field f) {
/*  91 */       return separateCamelCase(f.getName(), "_").toLowerCase();
/*     */     }
/*     */   },
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
/* 112 */   LOWER_CASE_WITH_DASHES {
/*     */     public String translateName(Field f) {
/* 114 */       return separateCamelCase(f.getName(), "-").toLowerCase();
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String separateCamelCase(String name, String separator) {
/* 123 */     StringBuilder translation = new StringBuilder();
/* 124 */     for (int i = 0; i < name.length(); i++) {
/* 125 */       char character = name.charAt(i);
/* 126 */       if (Character.isUpperCase(character) && translation.length() != 0) {
/* 127 */         translation.append(separator);
/*     */       }
/* 129 */       translation.append(character);
/*     */     } 
/* 131 */     return translation.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String upperCaseFirstLetter(String name) {
/* 138 */     StringBuilder fieldNameBuilder = new StringBuilder();
/* 139 */     int index = 0;
/* 140 */     char firstCharacter = name.charAt(index);
/*     */     
/* 142 */     while (index < name.length() - 1 && 
/* 143 */       !Character.isLetter(firstCharacter)) {
/*     */ 
/*     */ 
/*     */       
/* 147 */       fieldNameBuilder.append(firstCharacter);
/* 148 */       firstCharacter = name.charAt(++index);
/*     */     } 
/*     */     
/* 151 */     if (index == name.length()) {
/* 152 */       return fieldNameBuilder.toString();
/*     */     }
/*     */     
/* 155 */     if (!Character.isUpperCase(firstCharacter)) {
/* 156 */       String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
/* 157 */       return fieldNameBuilder.append(modifiedTarget).toString();
/*     */     } 
/* 159 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
/* 164 */     return (indexOfSubstring < srcString.length()) ? (firstCharacter + srcString.substring(indexOfSubstring)) : String.valueOf(firstCharacter);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\FieldNamingPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */