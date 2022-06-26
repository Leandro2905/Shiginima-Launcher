/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lang
/*     */ {
/*     */   private static final class LangRule
/*     */   {
/*     */     private final boolean acceptOnMatch;
/*     */     private final Set<String> languages;
/*     */     private final Pattern pattern;
/*     */     
/*     */     private LangRule(Pattern pattern, Set<String> languages, boolean acceptOnMatch) {
/*  86 */       this.pattern = pattern;
/*  87 */       this.languages = languages;
/*  88 */       this.acceptOnMatch = acceptOnMatch;
/*     */     }
/*     */     
/*     */     public boolean matches(String txt) {
/*  92 */       return this.pattern.matcher(txt).find();
/*     */     }
/*     */   }
/*     */   
/*  96 */   private static final Map<NameType, Lang> Langs = new EnumMap<NameType, Lang>(NameType.class);
/*     */   
/*     */   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/%s_lang.txt";
/*     */   
/*     */   static {
/* 101 */     for (NameType s : NameType.values()) {
/* 102 */       Langs.put(s, loadFromResource(String.format("org/apache/commons/codec/language/bm/%s_lang.txt", new Object[] { s.getName() }), Languages.getInstance(s)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final Languages languages;
/*     */   
/*     */   private final List<LangRule> rules;
/*     */ 
/*     */   
/*     */   public static Lang instance(NameType nameType) {
/* 114 */     return Langs.get(nameType);
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
/*     */   public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
/* 130 */     List<LangRule> rules = new ArrayList<LangRule>();
/* 131 */     InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);
/*     */     
/* 133 */     if (lRulesIS == null) {
/* 134 */       throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/%s_lang.txt");
/*     */     }
/*     */     
/* 137 */     Scanner scanner = new Scanner(lRulesIS, "UTF-8");
/*     */     try {
/* 139 */       boolean inExtendedComment = false;
/* 140 */       while (scanner.hasNextLine()) {
/* 141 */         String rawLine = scanner.nextLine();
/* 142 */         String line = rawLine;
/* 143 */         if (inExtendedComment) {
/*     */           
/* 145 */           if (line.endsWith("*/"))
/* 146 */             inExtendedComment = false; 
/*     */           continue;
/*     */         } 
/* 149 */         if (line.startsWith("/*")) {
/* 150 */           inExtendedComment = true;
/*     */           continue;
/*     */         } 
/* 153 */         int cmtI = line.indexOf("//");
/* 154 */         if (cmtI >= 0) {
/* 155 */           line = line.substring(0, cmtI);
/*     */         }
/*     */ 
/*     */         
/* 159 */         line = line.trim();
/*     */         
/* 161 */         if (line.length() == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 166 */         String[] parts = line.split("\\s+");
/*     */         
/* 168 */         if (parts.length != 3) {
/* 169 */           throw new IllegalArgumentException("Malformed line '" + rawLine + "' in language resource '" + languageRulesResourceName + "'");
/*     */         }
/*     */ 
/*     */         
/* 173 */         Pattern pattern = Pattern.compile(parts[0]);
/* 174 */         String[] langs = parts[1].split("\\+");
/* 175 */         boolean accept = parts[2].equals("true");
/*     */         
/* 177 */         rules.add(new LangRule(pattern, new HashSet(Arrays.asList((Object[])langs)), accept));
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 182 */       scanner.close();
/*     */     } 
/* 184 */     return new Lang(rules, languages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Lang(List<LangRule> rules, Languages languages) {
/* 191 */     this.rules = Collections.unmodifiableList(rules);
/* 192 */     this.languages = languages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String guessLanguage(String text) {
/* 203 */     Languages.LanguageSet ls = guessLanguages(text);
/* 204 */     return ls.isSingleton() ? ls.getAny() : "any";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Languages.LanguageSet guessLanguages(String input) {
/* 215 */     String text = input.toLowerCase(Locale.ENGLISH);
/*     */     
/* 217 */     Set<String> langs = new HashSet<String>(this.languages.getLanguages());
/* 218 */     for (LangRule rule : this.rules) {
/* 219 */       if (rule.matches(text)) {
/* 220 */         if (rule.acceptOnMatch) {
/* 221 */           langs.retainAll(rule.languages); continue;
/*     */         } 
/* 223 */         langs.removeAll(rule.languages);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 228 */     Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
/* 229 */     return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\bm\Lang.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */