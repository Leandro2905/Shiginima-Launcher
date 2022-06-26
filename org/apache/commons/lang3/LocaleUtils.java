/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleUtils
/*     */ {
/*  42 */   private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale toLocale(String str) {
/*  91 */     if (str == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     if (str.isEmpty()) {
/*  95 */       return new Locale("", "");
/*     */     }
/*  97 */     if (str.contains("#")) {
/*  98 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/* 100 */     int len = str.length();
/* 101 */     if (len < 2) {
/* 102 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/* 104 */     char ch0 = str.charAt(0);
/* 105 */     if (ch0 == '_') {
/* 106 */       if (len < 3) {
/* 107 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 109 */       char ch1 = str.charAt(1);
/* 110 */       char ch2 = str.charAt(2);
/* 111 */       if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
/* 112 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 114 */       if (len == 3) {
/* 115 */         return new Locale("", str.substring(1, 3));
/*     */       }
/* 117 */       if (len < 5) {
/* 118 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 120 */       if (str.charAt(3) != '_') {
/* 121 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 123 */       return new Locale("", str.substring(1, 3), str.substring(4));
/*     */     } 
/*     */     
/* 126 */     String[] split = str.split("_", -1);
/* 127 */     int occurrences = split.length - 1;
/* 128 */     switch (occurrences) {
/*     */       case 0:
/* 130 */         if (StringUtils.isAllLowerCase(str) && (len == 2 || len == 3)) {
/* 131 */           return new Locale(str);
/*     */         }
/* 133 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       
/*     */       case 1:
/* 136 */         if (StringUtils.isAllLowerCase(split[0]) && (split[0].length() == 2 || split[0].length() == 3) && split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))
/*     */         {
/*     */           
/* 139 */           return new Locale(split[0], split[1]);
/*     */         }
/* 141 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       
/*     */       case 2:
/* 144 */         if (StringUtils.isAllLowerCase(split[0]) && (split[0].length() == 2 || split[0].length() == 3) && (split[1].length() == 0 || (split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))) && split[2].length() > 0)
/*     */         {
/*     */ 
/*     */           
/* 148 */           return new Locale(split[0], split[1], split[2]);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 153 */     throw new IllegalArgumentException("Invalid locale format: " + str);
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
/*     */   public static List<Locale> localeLookupList(Locale locale) {
/* 171 */     return localeLookupList(locale, locale);
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
/*     */   public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
/* 193 */     List<Locale> list = new ArrayList<Locale>(4);
/* 194 */     if (locale != null) {
/* 195 */       list.add(locale);
/* 196 */       if (locale.getVariant().length() > 0) {
/* 197 */         list.add(new Locale(locale.getLanguage(), locale.getCountry()));
/*     */       }
/* 199 */       if (locale.getCountry().length() > 0) {
/* 200 */         list.add(new Locale(locale.getLanguage(), ""));
/*     */       }
/* 202 */       if (!list.contains(defaultLocale)) {
/* 203 */         list.add(defaultLocale);
/*     */       }
/*     */     } 
/* 206 */     return Collections.unmodifiableList(list);
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
/*     */   public static List<Locale> availableLocaleList() {
/* 220 */     return SyncAvoid.AVAILABLE_LOCALE_LIST;
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
/*     */   public static Set<Locale> availableLocaleSet() {
/* 234 */     return SyncAvoid.AVAILABLE_LOCALE_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailableLocale(Locale locale) {
/* 245 */     return availableLocaleList().contains(locale);
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
/*     */   public static List<Locale> languagesByCountry(String countryCode) {
/* 259 */     if (countryCode == null) {
/* 260 */       return Collections.emptyList();
/*     */     }
/* 262 */     List<Locale> langs = cLanguagesByCountry.get(countryCode);
/* 263 */     if (langs == null) {
/* 264 */       langs = new ArrayList<Locale>();
/* 265 */       List<Locale> locales = availableLocaleList();
/* 266 */       for (int i = 0; i < locales.size(); i++) {
/* 267 */         Locale locale = locales.get(i);
/* 268 */         if (countryCode.equals(locale.getCountry()) && locale.getVariant().isEmpty())
/*     */         {
/* 270 */           langs.add(locale);
/*     */         }
/*     */       } 
/* 273 */       langs = Collections.unmodifiableList(langs);
/* 274 */       cLanguagesByCountry.putIfAbsent(countryCode, langs);
/* 275 */       langs = cLanguagesByCountry.get(countryCode);
/*     */     } 
/* 277 */     return langs;
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
/*     */   public static List<Locale> countriesByLanguage(String languageCode) {
/* 291 */     if (languageCode == null) {
/* 292 */       return Collections.emptyList();
/*     */     }
/* 294 */     List<Locale> countries = cCountriesByLanguage.get(languageCode);
/* 295 */     if (countries == null) {
/* 296 */       countries = new ArrayList<Locale>();
/* 297 */       List<Locale> locales = availableLocaleList();
/* 298 */       for (int i = 0; i < locales.size(); i++) {
/* 299 */         Locale locale = locales.get(i);
/* 300 */         if (languageCode.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty())
/*     */         {
/*     */           
/* 303 */           countries.add(locale);
/*     */         }
/*     */       } 
/* 306 */       countries = Collections.unmodifiableList(countries);
/* 307 */       cCountriesByLanguage.putIfAbsent(languageCode, countries);
/* 308 */       countries = cCountriesByLanguage.get(languageCode);
/*     */     } 
/* 310 */     return countries;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SyncAvoid
/*     */   {
/*     */     private static final List<Locale> AVAILABLE_LOCALE_LIST;
/*     */     
/*     */     private static final Set<Locale> AVAILABLE_LOCALE_SET;
/*     */ 
/*     */     
/*     */     static {
/* 322 */       List<Locale> list = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
/* 323 */       AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
/* 324 */       AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<Locale>(list));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\LocaleUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */