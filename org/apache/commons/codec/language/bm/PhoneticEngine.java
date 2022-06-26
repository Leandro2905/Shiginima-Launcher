/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PhoneticEngine
/*     */ {
/*     */   static final class PhonemeBuilder
/*     */   {
/*     */     private final Set<Rule.Phoneme> phonemes;
/*     */     
/*     */     public static PhonemeBuilder empty(Languages.LanguageSet languages) {
/*  72 */       return new PhonemeBuilder(new Rule.Phoneme("", languages));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private PhonemeBuilder(Rule.Phoneme phoneme) {
/*  78 */       this.phonemes = new LinkedHashSet<Rule.Phoneme>();
/*  79 */       this.phonemes.add(phoneme);
/*     */     }
/*     */     
/*     */     private PhonemeBuilder(Set<Rule.Phoneme> phonemes) {
/*  83 */       this.phonemes = phonemes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void append(CharSequence str) {
/*  92 */       for (Rule.Phoneme ph : this.phonemes) {
/*  93 */         ph.append(str);
/*     */       }
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
/*     */     public void apply(Rule.PhonemeExpr phonemeExpr, int maxPhonemes) {
/* 107 */       Set<Rule.Phoneme> newPhonemes = new LinkedHashSet<Rule.Phoneme>(maxPhonemes);
/*     */       
/* 109 */       label18: for (Rule.Phoneme left : this.phonemes) {
/* 110 */         for (Rule.Phoneme right : phonemeExpr.getPhonemes()) {
/* 111 */           Languages.LanguageSet languages = left.getLanguages().restrictTo(right.getLanguages());
/* 112 */           if (!languages.isEmpty()) {
/* 113 */             Rule.Phoneme join = new Rule.Phoneme(left, right, languages);
/* 114 */             if (newPhonemes.size() < maxPhonemes) {
/* 115 */               newPhonemes.add(join);
/* 116 */               if (newPhonemes.size() >= maxPhonemes) {
/*     */                 break label18;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 124 */       this.phonemes.clear();
/* 125 */       this.phonemes.addAll(newPhonemes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Rule.Phoneme> getPhonemes() {
/* 134 */       return this.phonemes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String makeString() {
/* 145 */       StringBuilder sb = new StringBuilder();
/*     */       
/* 147 */       for (Rule.Phoneme ph : this.phonemes) {
/* 148 */         if (sb.length() > 0) {
/* 149 */           sb.append("|");
/*     */         }
/* 151 */         sb.append(ph.getPhonemeText());
/*     */       } 
/*     */       
/* 154 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RulesApplication
/*     */   {
/*     */     private final Map<String, List<Rule>> finalRules;
/*     */ 
/*     */     
/*     */     private final CharSequence input;
/*     */ 
/*     */     
/*     */     private PhoneticEngine.PhonemeBuilder phonemeBuilder;
/*     */ 
/*     */     
/*     */     private int i;
/*     */ 
/*     */     
/*     */     private final int maxPhonemes;
/*     */ 
/*     */     
/*     */     private boolean found;
/*     */ 
/*     */     
/*     */     public RulesApplication(Map<String, List<Rule>> finalRules, CharSequence input, PhoneticEngine.PhonemeBuilder phonemeBuilder, int i, int maxPhonemes) {
/* 181 */       if (finalRules == null) {
/* 182 */         throw new NullPointerException("The finalRules argument must not be null");
/*     */       }
/* 184 */       this.finalRules = finalRules;
/* 185 */       this.phonemeBuilder = phonemeBuilder;
/* 186 */       this.input = input;
/* 187 */       this.i = i;
/* 188 */       this.maxPhonemes = maxPhonemes;
/*     */     }
/*     */     
/*     */     public int getI() {
/* 192 */       return this.i;
/*     */     }
/*     */     
/*     */     public PhoneticEngine.PhonemeBuilder getPhonemeBuilder() {
/* 196 */       return this.phonemeBuilder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RulesApplication invoke() {
/* 207 */       this.found = false;
/* 208 */       int patternLength = 1;
/* 209 */       List<Rule> rules = this.finalRules.get(this.input.subSequence(this.i, this.i + patternLength));
/* 210 */       if (rules != null) {
/* 211 */         for (Rule rule : rules) {
/* 212 */           String pattern = rule.getPattern();
/* 213 */           patternLength = pattern.length();
/* 214 */           if (rule.patternAndContextMatches(this.input, this.i)) {
/* 215 */             this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
/* 216 */             this.found = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 222 */       if (!this.found) {
/* 223 */         patternLength = 1;
/*     */       }
/*     */       
/* 226 */       this.i += patternLength;
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isFound() {
/* 231 */       return this.found;
/*     */     }
/*     */   }
/*     */   
/* 235 */   private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap<NameType, Set<String>>(NameType.class);
/*     */   
/*     */   static {
/* 238 */     NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "bar", "ben", "da", "de", "van", "von" }))));
/*     */ 
/*     */     
/* 241 */     NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von" }))));
/*     */ 
/*     */ 
/*     */     
/* 245 */     NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von" }))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MAX_PHONEMES = 20;
/*     */   
/*     */   private final Lang lang;
/*     */   private final NameType nameType;
/*     */   private final RuleType ruleType;
/*     */   private final boolean concat;
/*     */   private final int maxPhonemes;
/*     */   
/*     */   private static String join(Iterable<String> strings, String sep) {
/* 258 */     StringBuilder sb = new StringBuilder();
/* 259 */     Iterator<String> si = strings.iterator();
/* 260 */     if (si.hasNext()) {
/* 261 */       sb.append(si.next());
/*     */     }
/* 263 */     while (si.hasNext()) {
/* 264 */       sb.append(sep).append(si.next());
/*     */     }
/*     */     
/* 267 */     return sb.toString();
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
/*     */   public PhoneticEngine(NameType nameType, RuleType ruleType, boolean concat) {
/* 293 */     this(nameType, ruleType, concat, 20);
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
/*     */   public PhoneticEngine(NameType nameType, RuleType ruleType, boolean concat, int maxPhonemes) {
/* 311 */     if (ruleType == RuleType.RULES) {
/* 312 */       throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
/*     */     }
/* 314 */     this.nameType = nameType;
/* 315 */     this.ruleType = ruleType;
/* 316 */     this.concat = concat;
/* 317 */     this.lang = Lang.instance(nameType);
/* 318 */     this.maxPhonemes = maxPhonemes;
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
/*     */   private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, Map<String, List<Rule>> finalRules) {
/* 331 */     if (finalRules == null) {
/* 332 */       throw new NullPointerException("finalRules can not be null");
/*     */     }
/* 334 */     if (finalRules.isEmpty()) {
/* 335 */       return phonemeBuilder;
/*     */     }
/*     */     
/* 338 */     Map<Rule.Phoneme, Rule.Phoneme> phonemes = new TreeMap<Rule.Phoneme, Rule.Phoneme>(Rule.Phoneme.COMPARATOR);
/*     */ 
/*     */     
/* 341 */     for (Rule.Phoneme phoneme : phonemeBuilder.getPhonemes()) {
/* 342 */       PhonemeBuilder subBuilder = PhonemeBuilder.empty(phoneme.getLanguages());
/* 343 */       String phonemeText = phoneme.getPhonemeText().toString();
/*     */       int i;
/* 345 */       for (i = 0; i < phonemeText.length(); ) {
/* 346 */         RulesApplication rulesApplication = (new RulesApplication(finalRules, phonemeText, subBuilder, i, this.maxPhonemes)).invoke();
/*     */         
/* 348 */         boolean found = rulesApplication.isFound();
/* 349 */         subBuilder = rulesApplication.getPhonemeBuilder();
/*     */         
/* 351 */         if (!found)
/*     */         {
/* 353 */           subBuilder.append(phonemeText.subSequence(i, i + 1));
/*     */         }
/*     */         
/* 356 */         i = rulesApplication.getI();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 362 */       for (Rule.Phoneme newPhoneme : subBuilder.getPhonemes()) {
/* 363 */         if (phonemes.containsKey(newPhoneme)) {
/* 364 */           Rule.Phoneme oldPhoneme = phonemes.remove(newPhoneme);
/* 365 */           Rule.Phoneme mergedPhoneme = oldPhoneme.mergeWithLanguage(newPhoneme.getLanguages());
/* 366 */           phonemes.put(mergedPhoneme, mergedPhoneme); continue;
/*     */         } 
/* 368 */         phonemes.put(newPhoneme, newPhoneme);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 373 */     return new PhonemeBuilder(phonemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(String input) {
/* 384 */     Languages.LanguageSet languageSet = this.lang.guessLanguages(input);
/* 385 */     return encode(input, languageSet);
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
/*     */   public String encode(String input, Languages.LanguageSet languageSet) {
/* 399 */     Map<String, List<Rule>> rules = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
/*     */     
/* 401 */     Map<String, List<Rule>> finalRules1 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
/*     */     
/* 403 */     Map<String, List<Rule>> finalRules2 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
/*     */ 
/*     */ 
/*     */     
/* 407 */     input = input.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
/*     */     
/* 409 */     if (this.nameType == NameType.GENERIC) {
/* 410 */       if (input.length() >= 2 && input.substring(0, 2).equals("d'")) {
/* 411 */         String remainder = input.substring(2);
/* 412 */         String combined = "d" + remainder;
/* 413 */         return "(" + encode(remainder) + ")-(" + encode(combined) + ")";
/*     */       } 
/* 415 */       for (String l : NAME_PREFIXES.get(this.nameType)) {
/*     */         
/* 417 */         if (input.startsWith(l + " ")) {
/*     */           
/* 419 */           String remainder = input.substring(l.length() + 1);
/* 420 */           String combined = l + remainder;
/* 421 */           return "(" + encode(remainder) + ")-(" + encode(combined) + ")";
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 426 */     List<String> words = Arrays.asList(input.split("\\s+"));
/* 427 */     List<String> words2 = new ArrayList<String>();
/*     */ 
/*     */     
/* 430 */     switch (this.nameType) {
/*     */       case SEPHARDIC:
/* 432 */         for (String aWord : words) {
/* 433 */           String[] parts = aWord.split("'");
/* 434 */           String lastPart = parts[parts.length - 1];
/* 435 */           words2.add(lastPart);
/*     */         } 
/* 437 */         words2.removeAll(NAME_PREFIXES.get(this.nameType));
/*     */         break;
/*     */       case ASHKENAZI:
/* 440 */         words2.addAll(words);
/* 441 */         words2.removeAll(NAME_PREFIXES.get(this.nameType));
/*     */         break;
/*     */       case GENERIC:
/* 444 */         words2.addAll(words);
/*     */         break;
/*     */       default:
/* 447 */         throw new IllegalStateException("Unreachable case: " + this.nameType);
/*     */     } 
/*     */     
/* 450 */     if (this.concat) {
/*     */       
/* 452 */       input = join(words2, " ");
/* 453 */     } else if (words2.size() == 1) {
/*     */       
/* 455 */       input = words.iterator().next();
/*     */     } else {
/*     */       
/* 458 */       StringBuilder result = new StringBuilder();
/* 459 */       for (String word : words2) {
/* 460 */         result.append("-").append(encode(word));
/*     */       }
/*     */       
/* 463 */       return result.substring(1);
/*     */     } 
/*     */     
/* 466 */     PhonemeBuilder phonemeBuilder = PhonemeBuilder.empty(languageSet);
/*     */ 
/*     */     
/* 469 */     for (int i = 0; i < input.length(); ) {
/* 470 */       RulesApplication rulesApplication = (new RulesApplication(rules, input, phonemeBuilder, i, this.maxPhonemes)).invoke();
/*     */       
/* 472 */       i = rulesApplication.getI();
/* 473 */       phonemeBuilder = rulesApplication.getPhonemeBuilder();
/*     */     } 
/*     */ 
/*     */     
/* 477 */     phonemeBuilder = applyFinalRules(phonemeBuilder, finalRules1);
/*     */     
/* 479 */     phonemeBuilder = applyFinalRules(phonemeBuilder, finalRules2);
/*     */     
/* 481 */     return phonemeBuilder.makeString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lang getLang() {
/* 490 */     return this.lang;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameType getNameType() {
/* 499 */     return this.nameType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuleType getRuleType() {
/* 508 */     return this.ruleType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConcat() {
/* 517 */     return this.concat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPhonemes() {
/* 527 */     return this.maxPhonemes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\bm\PhoneticEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */