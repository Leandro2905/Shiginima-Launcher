/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Scanner;
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
/*     */ public class Languages
/*     */ {
/*     */   public static final String ANY = "any";
/*     */   
/*     */   public static abstract class LanguageSet
/*     */   {
/*     */     public static LanguageSet from(Set<String> langs) {
/*  64 */       return langs.isEmpty() ? Languages.NO_LANGUAGES : new Languages.SomeLanguages(langs);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract boolean contains(String param1String);
/*     */     
/*     */     public abstract String getAny();
/*     */     
/*     */     public abstract boolean isEmpty();
/*     */     
/*     */     public abstract boolean isSingleton();
/*     */     
/*     */     public abstract LanguageSet restrictTo(LanguageSet param1LanguageSet);
/*     */     
/*     */     abstract LanguageSet merge(LanguageSet param1LanguageSet);
/*     */   }
/*     */   
/*     */   public static final class SomeLanguages
/*     */     extends LanguageSet
/*     */   {
/*     */     private final Set<String> languages;
/*     */     
/*     */     private SomeLanguages(Set<String> languages) {
/*  87 */       this.languages = Collections.unmodifiableSet(languages);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(String language) {
/*  92 */       return this.languages.contains(language);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAny() {
/*  97 */       return this.languages.iterator().next();
/*     */     }
/*     */     
/*     */     public Set<String> getLanguages() {
/* 101 */       return this.languages;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 106 */       return this.languages.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSingleton() {
/* 111 */       return (this.languages.size() == 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 116 */       if (other == Languages.NO_LANGUAGES)
/* 117 */         return other; 
/* 118 */       if (other == Languages.ANY_LANGUAGE) {
/* 119 */         return this;
/*     */       }
/* 121 */       SomeLanguages sl = (SomeLanguages)other;
/* 122 */       Set<String> ls = new HashSet<String>(Math.min(this.languages.size(), sl.languages.size()));
/* 123 */       for (String lang : this.languages) {
/* 124 */         if (sl.languages.contains(lang)) {
/* 125 */           ls.add(lang);
/*     */         }
/*     */       } 
/* 128 */       return from(ls);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Languages.LanguageSet merge(Languages.LanguageSet other) {
/* 134 */       if (other == Languages.NO_LANGUAGES)
/* 135 */         return this; 
/* 136 */       if (other == Languages.ANY_LANGUAGE) {
/* 137 */         return other;
/*     */       }
/* 139 */       SomeLanguages sl = (SomeLanguages)other;
/* 140 */       Set<String> ls = new HashSet<String>(this.languages);
/* 141 */       for (String lang : sl.languages) {
/* 142 */         ls.add(lang);
/*     */       }
/* 144 */       return from(ls);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 150 */       return "Languages(" + this.languages.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private static final Map<NameType, Languages> LANGUAGES = new EnumMap<NameType, Languages>(NameType.class); private final Set<String> languages;
/*     */   
/*     */   static {
/* 160 */     for (NameType s : NameType.values()) {
/* 161 */       LANGUAGES.put(s, getInstance(langResourceName(s)));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Languages getInstance(NameType nameType) {
/* 166 */     return LANGUAGES.get(nameType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Languages getInstance(String languagesResourceName) {
/* 171 */     Set<String> ls = new HashSet<String>();
/* 172 */     InputStream langIS = Languages.class.getClassLoader().getResourceAsStream(languagesResourceName);
/*     */     
/* 174 */     if (langIS == null) {
/* 175 */       throw new IllegalArgumentException("Unable to resolve required resource: " + languagesResourceName);
/*     */     }
/*     */     
/* 178 */     Scanner lsScanner = new Scanner(langIS, "UTF-8");
/*     */     try {
/* 180 */       boolean inExtendedComment = false;
/* 181 */       while (lsScanner.hasNextLine()) {
/* 182 */         String line = lsScanner.nextLine().trim();
/* 183 */         if (inExtendedComment) {
/* 184 */           if (line.endsWith("*/"))
/* 185 */             inExtendedComment = false; 
/*     */           continue;
/*     */         } 
/* 188 */         if (line.startsWith("/*")) {
/* 189 */           inExtendedComment = true; continue;
/* 190 */         }  if (line.length() > 0) {
/* 191 */           ls.add(line);
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 196 */       lsScanner.close();
/*     */     } 
/*     */     
/* 199 */     return new Languages(Collections.unmodifiableSet(ls));
/*     */   }
/*     */   
/*     */   private static String langResourceName(NameType nameType) {
/* 203 */     return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", new Object[] { nameType.getName() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public static final LanguageSet NO_LANGUAGES = new LanguageSet()
/*     */     {
/*     */       public boolean contains(String language) {
/* 214 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getAny() {
/* 219 */         throw new NoSuchElementException("Can't fetch any language from the empty language set.");
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 224 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isSingleton() {
/* 229 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 234 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet merge(Languages.LanguageSet other) {
/* 239 */         return other;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 244 */         return "NO_LANGUAGES";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 251 */   public static final LanguageSet ANY_LANGUAGE = new LanguageSet()
/*     */     {
/*     */       public boolean contains(String language) {
/* 254 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getAny() {
/* 259 */         throw new NoSuchElementException("Can't fetch any language from the any language set.");
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 264 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isSingleton() {
/* 269 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 274 */         return other;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet merge(Languages.LanguageSet other) {
/* 279 */         return other;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 284 */         return "ANY_LANGUAGE";
/*     */       }
/*     */     };
/*     */   
/*     */   private Languages(Set<String> languages) {
/* 289 */     this.languages = languages;
/*     */   }
/*     */   
/*     */   public Set<String> getLanguages() {
/* 293 */     return this.languages;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\bm\Languages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */