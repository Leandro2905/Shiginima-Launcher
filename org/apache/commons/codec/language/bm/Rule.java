/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
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
/*     */ 
/*     */ public class Rule
/*     */ {
/*     */   public static final class Phoneme
/*     */     implements PhonemeExpr
/*     */   {
/*  85 */     public static final Comparator<Phoneme> COMPARATOR = new Comparator<Phoneme>()
/*     */       {
/*     */         public int compare(Rule.Phoneme o1, Rule.Phoneme o2) {
/*  88 */           for (int i = 0; i < o1.phonemeText.length(); i++) {
/*  89 */             if (i >= o2.phonemeText.length()) {
/*  90 */               return 1;
/*     */             }
/*  92 */             int c = o1.phonemeText.charAt(i) - o2.phonemeText.charAt(i);
/*  93 */             if (c != 0) {
/*  94 */               return c;
/*     */             }
/*     */           } 
/*     */           
/*  98 */           if (o1.phonemeText.length() < o2.phonemeText.length()) {
/*  99 */             return -1;
/*     */           }
/*     */           
/* 102 */           return 0;
/*     */         }
/*     */       };
/*     */     
/*     */     private final StringBuilder phonemeText;
/*     */     private final Languages.LanguageSet languages;
/*     */     
/*     */     public Phoneme(CharSequence phonemeText, Languages.LanguageSet languages) {
/* 110 */       this.phonemeText = new StringBuilder(phonemeText);
/* 111 */       this.languages = languages;
/*     */     }
/*     */     
/*     */     public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight) {
/* 115 */       this(phonemeLeft.phonemeText, phonemeLeft.languages);
/* 116 */       this.phonemeText.append(phonemeRight.phonemeText);
/*     */     }
/*     */     
/*     */     public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight, Languages.LanguageSet languages) {
/* 120 */       this(phonemeLeft.phonemeText, languages);
/* 121 */       this.phonemeText.append(phonemeRight.phonemeText);
/*     */     }
/*     */     
/*     */     public Phoneme append(CharSequence str) {
/* 125 */       this.phonemeText.append(str);
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Languages.LanguageSet getLanguages() {
/* 130 */       return this.languages;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<Phoneme> getPhonemes() {
/* 135 */       return Collections.singleton(this);
/*     */     }
/*     */     
/*     */     public CharSequence getPhonemeText() {
/* 139 */       return this.phonemeText;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Phoneme join(Phoneme right) {
/* 151 */       return new Phoneme(this.phonemeText.toString() + right.phonemeText.toString(), this.languages.restrictTo(right.languages));
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
/*     */     public Phoneme mergeWithLanguage(Languages.LanguageSet lang) {
/* 163 */       return new Phoneme(this.phonemeText.toString(), this.languages.merge(lang));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 168 */       return this.phonemeText.toString() + "[" + this.languages + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class PhonemeList
/*     */     implements PhonemeExpr
/*     */   {
/*     */     private final List<Rule.Phoneme> phonemes;
/*     */ 
/*     */     
/*     */     public PhonemeList(List<Rule.Phoneme> phonemes) {
/* 180 */       this.phonemes = phonemes;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Rule.Phoneme> getPhonemes() {
/* 185 */       return this.phonemes;
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
/* 196 */   public static final RPattern ALL_STRINGS_RMATCHER = new RPattern()
/*     */     {
/*     */       public boolean isMatch(CharSequence input) {
/* 199 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static final String ALL = "ALL";
/*     */   
/*     */   private static final String DOUBLE_QUOTE = "\"";
/*     */   
/*     */   private static final String HASH_INCLUDE = "#include";
/* 209 */   private static final Map<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>> RULES = new EnumMap<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>>(NameType.class); private final RPattern lContext;
/*     */   private final String pattern;
/*     */   
/*     */   static {
/* 213 */     for (NameType s : NameType.values()) {
/* 214 */       Map<RuleType, Map<String, Map<String, List<Rule>>>> rts = new EnumMap<RuleType, Map<String, Map<String, List<Rule>>>>(RuleType.class);
/*     */ 
/*     */       
/* 217 */       for (RuleType rt : RuleType.values()) {
/* 218 */         Map<String, Map<String, List<Rule>>> rs = new HashMap<String, Map<String, List<Rule>>>();
/*     */         
/* 220 */         Languages ls = Languages.getInstance(s);
/* 221 */         for (String l : ls.getLanguages()) {
/*     */           try {
/* 223 */             rs.put(l, parseRules(createScanner(s, rt, l), createResourceName(s, rt, l)));
/* 224 */           } catch (IllegalStateException e) {
/* 225 */             throw new IllegalStateException("Problem processing " + createResourceName(s, rt, l), e);
/*     */           } 
/*     */         } 
/* 228 */         if (!rt.equals(RuleType.RULES)) {
/* 229 */           rs.put("common", parseRules(createScanner(s, rt, "common"), createResourceName(s, rt, "common")));
/*     */         }
/*     */         
/* 232 */         rts.put(rt, Collections.unmodifiableMap(rs));
/*     */       } 
/*     */       
/* 235 */       RULES.put(s, Collections.unmodifiableMap(rts));
/*     */     } 
/*     */   }
/*     */   private final PhonemeExpr phoneme; private final RPattern rContext;
/*     */   private static boolean contains(CharSequence chars, char input) {
/* 240 */     for (int i = 0; i < chars.length(); i++) {
/* 241 */       if (chars.charAt(i) == input) {
/* 242 */         return true;
/*     */       }
/*     */     } 
/* 245 */     return false;
/*     */   }
/*     */   
/*     */   private static String createResourceName(NameType nameType, RuleType rt, String lang) {
/* 249 */     return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", new Object[] { nameType.getName(), rt.getName(), lang });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Scanner createScanner(NameType nameType, RuleType rt, String lang) {
/* 254 */     String resName = createResourceName(nameType, rt, lang);
/* 255 */     InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
/*     */     
/* 257 */     if (rulesIS == null) {
/* 258 */       throw new IllegalArgumentException("Unable to load resource: " + resName);
/*     */     }
/*     */     
/* 261 */     return new Scanner(rulesIS, "UTF-8");
/*     */   }
/*     */   
/*     */   private static Scanner createScanner(String lang) {
/* 265 */     String resName = String.format("org/apache/commons/codec/language/bm/%s.txt", new Object[] { lang });
/* 266 */     InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
/*     */     
/* 268 */     if (rulesIS == null) {
/* 269 */       throw new IllegalArgumentException("Unable to load resource: " + resName);
/*     */     }
/*     */     
/* 272 */     return new Scanner(rulesIS, "UTF-8");
/*     */   }
/*     */   
/*     */   private static boolean endsWith(CharSequence input, CharSequence suffix) {
/* 276 */     if (suffix.length() > input.length()) {
/* 277 */       return false;
/*     */     }
/* 279 */     for (int i = input.length() - 1, j = suffix.length() - 1; j >= 0; i--, j--) {
/* 280 */       if (input.charAt(i) != suffix.charAt(j)) {
/* 281 */         return false;
/*     */       }
/*     */     } 
/* 284 */     return true;
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
/*     */   public static List<Rule> getInstance(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
/* 300 */     Map<String, List<Rule>> ruleMap = getInstanceMap(nameType, rt, langs);
/* 301 */     List<Rule> allRules = new ArrayList<Rule>();
/* 302 */     for (List<Rule> rules : ruleMap.values()) {
/* 303 */       allRules.addAll(rules);
/*     */     }
/* 305 */     return allRules;
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
/*     */   public static List<Rule> getInstance(NameType nameType, RuleType rt, String lang) {
/* 320 */     return getInstance(nameType, rt, Languages.LanguageSet.from(new HashSet<String>(Arrays.asList(new String[] { lang }))));
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
/*     */   public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
/* 337 */     return langs.isSingleton() ? getInstanceMap(nameType, rt, langs.getAny()) : getInstanceMap(nameType, rt, "any");
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
/*     */   public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, String lang) {
/* 355 */     Map<String, List<Rule>> rules = (Map<String, List<Rule>>)((Map)((Map)RULES.get(nameType)).get(rt)).get(lang);
/*     */     
/* 357 */     if (rules == null) {
/* 358 */       throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", new Object[] { nameType.getName(), rt.getName(), lang }));
/*     */     }
/*     */ 
/*     */     
/* 362 */     return rules;
/*     */   }
/*     */   
/*     */   private static Phoneme parsePhoneme(String ph) {
/* 366 */     int open = ph.indexOf("[");
/* 367 */     if (open >= 0) {
/* 368 */       if (!ph.endsWith("]")) {
/* 369 */         throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
/*     */       }
/* 371 */       String before = ph.substring(0, open);
/* 372 */       String in = ph.substring(open + 1, ph.length() - 1);
/* 373 */       Set<String> langs = new HashSet<String>(Arrays.asList(in.split("[+]")));
/*     */       
/* 375 */       return new Phoneme(before, Languages.LanguageSet.from(langs));
/*     */     } 
/* 377 */     return new Phoneme(ph, Languages.ANY_LANGUAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   private static PhonemeExpr parsePhonemeExpr(String ph) {
/* 382 */     if (ph.startsWith("(")) {
/* 383 */       if (!ph.endsWith(")")) {
/* 384 */         throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
/*     */       }
/*     */       
/* 387 */       List<Phoneme> phs = new ArrayList<Phoneme>();
/* 388 */       String body = ph.substring(1, ph.length() - 1);
/* 389 */       for (String part : body.split("[|]")) {
/* 390 */         phs.add(parsePhoneme(part));
/*     */       }
/* 392 */       if (body.startsWith("|") || body.endsWith("|")) {
/* 393 */         phs.add(new Phoneme("", Languages.ANY_LANGUAGE));
/*     */       }
/*     */       
/* 396 */       return new PhonemeList(phs);
/*     */     } 
/* 398 */     return parsePhoneme(ph);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, List<Rule>> parseRules(Scanner scanner, final String location) {
/* 403 */     Map<String, List<Rule>> lines = new HashMap<String, List<Rule>>();
/* 404 */     int currentLine = 0;
/*     */     
/* 406 */     boolean inMultilineComment = false;
/* 407 */     while (scanner.hasNextLine()) {
/* 408 */       currentLine++;
/* 409 */       String rawLine = scanner.nextLine();
/* 410 */       String line = rawLine;
/*     */       
/* 412 */       if (inMultilineComment) {
/* 413 */         if (line.endsWith("*/"))
/* 414 */           inMultilineComment = false; 
/*     */         continue;
/*     */       } 
/* 417 */       if (line.startsWith("/*")) {
/* 418 */         inMultilineComment = true;
/*     */         continue;
/*     */       } 
/* 421 */       int cmtI = line.indexOf("//");
/* 422 */       if (cmtI >= 0) {
/* 423 */         line = line.substring(0, cmtI);
/*     */       }
/*     */ 
/*     */       
/* 427 */       line = line.trim();
/*     */       
/* 429 */       if (line.length() == 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 433 */       if (line.startsWith("#include")) {
/*     */         
/* 435 */         String incl = line.substring("#include".length()).trim();
/* 436 */         if (incl.contains(" ")) {
/* 437 */           throw new IllegalArgumentException("Malformed import statement '" + rawLine + "' in " + location);
/*     */         }
/*     */         
/* 440 */         lines.putAll(parseRules(createScanner(incl), location + "->" + incl));
/*     */         
/*     */         continue;
/*     */       } 
/* 444 */       String[] parts = line.split("\\s+");
/* 445 */       if (parts.length != 4) {
/* 446 */         throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
/*     */       }
/*     */       
/*     */       try {
/* 450 */         final String pat = stripQuotes(parts[0]);
/* 451 */         final String lCon = stripQuotes(parts[1]);
/* 452 */         final String rCon = stripQuotes(parts[2]);
/* 453 */         PhonemeExpr ph = parsePhonemeExpr(stripQuotes(parts[3]));
/* 454 */         final int cLine = currentLine;
/* 455 */         Rule r = new Rule(pat, lCon, rCon, ph) {
/* 456 */             private final int myLine = cLine;
/* 457 */             private final String loc = location;
/*     */ 
/*     */             
/*     */             public String toString() {
/* 461 */               StringBuilder sb = new StringBuilder();
/* 462 */               sb.append("Rule");
/* 463 */               sb.append("{line=").append(this.myLine);
/* 464 */               sb.append(", loc='").append(this.loc).append('\'');
/* 465 */               sb.append(", pat='").append(pat).append('\'');
/* 466 */               sb.append(", lcon='").append(lCon).append('\'');
/* 467 */               sb.append(", rcon='").append(rCon).append('\'');
/* 468 */               sb.append('}');
/* 469 */               return sb.toString();
/*     */             }
/*     */           };
/* 472 */         String patternKey = r.pattern.substring(0, 1);
/* 473 */         List<Rule> rules = lines.get(patternKey);
/* 474 */         if (rules == null) {
/* 475 */           rules = new ArrayList<Rule>();
/* 476 */           lines.put(patternKey, rules);
/*     */         } 
/* 478 */         rules.add(r);
/* 479 */       } catch (IllegalArgumentException e) {
/* 480 */         throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     return lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RPattern pattern(final String regex) {
/* 500 */     boolean startsWith = regex.startsWith("^");
/* 501 */     boolean endsWith = regex.endsWith("$");
/* 502 */     final String content = regex.substring(startsWith ? 1 : 0, endsWith ? (regex.length() - 1) : regex.length());
/* 503 */     boolean boxes = content.contains("[");
/*     */     
/* 505 */     if (!boxes) {
/* 506 */       if (startsWith && endsWith) {
/*     */         
/* 508 */         if (content.length() == 0)
/*     */         {
/* 510 */           return new RPattern()
/*     */             {
/*     */               public boolean isMatch(CharSequence input) {
/* 513 */                 return (input.length() == 0);
/*     */               }
/*     */             };
/*     */         }
/* 517 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 520 */               return input.equals(content);
/*     */             }
/*     */           };
/*     */       } 
/* 524 */       if ((startsWith || endsWith) && content.length() == 0)
/*     */       {
/* 526 */         return ALL_STRINGS_RMATCHER; } 
/* 527 */       if (startsWith)
/*     */       {
/* 529 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 532 */               return Rule.startsWith(input, content);
/*     */             }
/*     */           }; } 
/* 535 */       if (endsWith)
/*     */       {
/* 537 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 540 */               return Rule.endsWith(input, content);
/*     */             }
/*     */           };
/*     */       }
/*     */     } else {
/* 545 */       boolean startsWithBox = content.startsWith("[");
/* 546 */       boolean endsWithBox = content.endsWith("]");
/*     */       
/* 548 */       if (startsWithBox && endsWithBox) {
/* 549 */         String boxContent = content.substring(1, content.length() - 1);
/* 550 */         if (!boxContent.contains("[")) {
/*     */           
/* 552 */           boolean negate = boxContent.startsWith("^");
/* 553 */           if (negate) {
/* 554 */             boxContent = boxContent.substring(1);
/*     */           }
/* 556 */           final String bContent = boxContent;
/* 557 */           final boolean shouldMatch = !negate;
/*     */           
/* 559 */           if (startsWith && endsWith)
/*     */           {
/* 561 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 564 */                   return (input.length() == 1 && Rule.contains(bContent, input.charAt(0)) == shouldMatch);
/*     */                 }
/*     */               }; } 
/* 567 */           if (startsWith)
/*     */           {
/* 569 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 572 */                   return (input.length() > 0 && Rule.contains(bContent, input.charAt(0)) == shouldMatch);
/*     */                 }
/*     */               }; } 
/* 575 */           if (endsWith)
/*     */           {
/* 577 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 580 */                   return (input.length() > 0 && Rule.contains(bContent, input.charAt(input.length() - 1)) == shouldMatch);
/*     */                 }
/*     */               };
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 589 */     return new RPattern() {
/* 590 */         Pattern pattern = Pattern.compile(regex);
/*     */ 
/*     */         
/*     */         public boolean isMatch(CharSequence input) {
/* 594 */           Matcher matcher = this.pattern.matcher(input);
/* 595 */           return matcher.find();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean startsWith(CharSequence input, CharSequence prefix) {
/* 601 */     if (prefix.length() > input.length()) {
/* 602 */       return false;
/*     */     }
/* 604 */     for (int i = 0; i < prefix.length(); i++) {
/* 605 */       if (input.charAt(i) != prefix.charAt(i)) {
/* 606 */         return false;
/*     */       }
/*     */     } 
/* 609 */     return true;
/*     */   }
/*     */   
/*     */   private static String stripQuotes(String str) {
/* 613 */     if (str.startsWith("\"")) {
/* 614 */       str = str.substring(1);
/*     */     }
/*     */     
/* 617 */     if (str.endsWith("\"")) {
/* 618 */       str = str.substring(0, str.length() - 1);
/*     */     }
/*     */     
/* 621 */     return str;
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
/*     */   public Rule(String pattern, String lContext, String rContext, PhonemeExpr phoneme) {
/* 645 */     this.pattern = pattern;
/* 646 */     this.lContext = pattern(lContext + "$");
/* 647 */     this.rContext = pattern("^" + rContext);
/* 648 */     this.phoneme = phoneme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RPattern getLContext() {
/* 657 */     return this.lContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 666 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PhonemeExpr getPhoneme() {
/* 675 */     return this.phoneme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RPattern getRContext() {
/* 684 */     return this.rContext;
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
/*     */   public boolean patternAndContextMatches(CharSequence input, int i) {
/* 699 */     if (i < 0) {
/* 700 */       throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
/*     */     }
/*     */     
/* 703 */     int patternLength = this.pattern.length();
/* 704 */     int ipl = i + patternLength;
/*     */     
/* 706 */     if (ipl > input.length())
/*     */     {
/* 708 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 713 */     if (!input.subSequence(i, ipl).equals(this.pattern))
/* 714 */       return false; 
/* 715 */     if (!this.rContext.isMatch(input.subSequence(ipl, input.length()))) {
/* 716 */       return false;
/*     */     }
/* 718 */     return this.lContext.isMatch(input.subSequence(0, i));
/*     */   }
/*     */   
/*     */   public static interface RPattern {
/*     */     boolean isMatch(CharSequence param1CharSequence);
/*     */   }
/*     */   
/*     */   public static interface PhonemeExpr {
/*     */     Iterable<Rule.Phoneme> getPhonemes();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\bm\Rule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */