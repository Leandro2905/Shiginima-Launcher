/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DaitchMokotoffSoundex
/*     */   implements StringEncoder
/*     */ {
/*     */   private static final String COMMENT = "//";
/*     */   private static final String DOUBLE_QUOTE = "\"";
/*     */   private static final String MULTILINE_COMMENT_END = "*/";
/*     */   private static final String MULTILINE_COMMENT_START = "/*";
/*     */   private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
/*     */   private static final int MAX_LENGTH = 6;
/*     */   
/*     */   private static final class Branch
/*     */   {
/*  83 */     private final StringBuilder builder = new StringBuilder();
/*  84 */     private String lastReplacement = null;
/*  85 */     private String cachedString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Branch createBranch() {
/*  94 */       Branch branch = new Branch();
/*  95 */       branch.builder.append(toString());
/*  96 */       branch.lastReplacement = this.lastReplacement;
/*  97 */       return branch;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 102 */       if (this == other) {
/* 103 */         return true;
/*     */       }
/* 105 */       if (!(other instanceof Branch)) {
/* 106 */         return false;
/*     */       }
/*     */       
/* 109 */       return toString().equals(((Branch)other).toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void finish() {
/* 116 */       while (this.builder.length() < 6) {
/* 117 */         this.builder.append('0');
/* 118 */         this.cachedString = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 124 */       return toString().hashCode();
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
/*     */     public void processNextReplacement(String replacement, boolean forceAppend) {
/* 136 */       boolean append = (this.lastReplacement == null || !this.lastReplacement.endsWith(replacement) || forceAppend);
/*     */       
/* 138 */       if (append && this.builder.length() < 6) {
/* 139 */         this.builder.append(replacement);
/*     */         
/* 141 */         if (this.builder.length() > 6) {
/* 142 */           this.builder.delete(6, this.builder.length());
/*     */         }
/* 144 */         this.cachedString = null;
/*     */       } 
/*     */       
/* 147 */       this.lastReplacement = replacement;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 152 */       if (this.cachedString == null) {
/* 153 */         this.cachedString = this.builder.toString();
/*     */       }
/* 155 */       return this.cachedString;
/*     */     }
/*     */ 
/*     */     
/*     */     private Branch() {}
/*     */   }
/*     */   
/*     */   private static final class Rule
/*     */   {
/*     */     private final String pattern;
/*     */     private final String[] replacementAtStart;
/*     */     private final String[] replacementBeforeVowel;
/*     */     private final String[] replacementDefault;
/*     */     
/*     */     protected Rule(String pattern, String replacementAtStart, String replacementBeforeVowel, String replacementDefault) {
/* 170 */       this.pattern = pattern;
/* 171 */       this.replacementAtStart = replacementAtStart.split("\\|");
/* 172 */       this.replacementBeforeVowel = replacementBeforeVowel.split("\\|");
/* 173 */       this.replacementDefault = replacementDefault.split("\\|");
/*     */     }
/*     */     
/*     */     public int getPatternLength() {
/* 177 */       return this.pattern.length();
/*     */     }
/*     */     
/*     */     public String[] getReplacements(String context, boolean atStart) {
/* 181 */       if (atStart) {
/* 182 */         return this.replacementAtStart;
/*     */       }
/*     */       
/* 185 */       int nextIndex = getPatternLength();
/* 186 */       boolean nextCharIsVowel = (nextIndex < context.length()) ? isVowel(context.charAt(nextIndex)) : false;
/* 187 */       if (nextCharIsVowel) {
/* 188 */         return this.replacementBeforeVowel;
/*     */       }
/*     */       
/* 191 */       return this.replacementDefault;
/*     */     }
/*     */     
/*     */     private boolean isVowel(char ch) {
/* 195 */       return (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u');
/*     */     }
/*     */     
/*     */     public boolean matches(String context) {
/* 199 */       return context.startsWith(this.pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 204 */       return String.format("%s=(%s,%s,%s)", new Object[] { this.pattern, Arrays.asList(this.replacementAtStart), Arrays.asList(this.replacementBeforeVowel), Arrays.asList(this.replacementDefault) });
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
/* 223 */   private static final Map<Character, List<Rule>> RULES = new HashMap<Character, List<Rule>>();
/*     */ 
/*     */   
/* 226 */   private static final Map<Character, Character> FOLDINGS = new HashMap<Character, Character>(); private final boolean folding;
/*     */   
/*     */   static {
/* 229 */     InputStream rulesIS = DaitchMokotoffSoundex.class.getClassLoader().getResourceAsStream("org/apache/commons/codec/language/dmrules.txt");
/* 230 */     if (rulesIS == null) {
/* 231 */       throw new IllegalArgumentException("Unable to load resource: org/apache/commons/codec/language/dmrules.txt");
/*     */     }
/*     */     
/* 234 */     Scanner scanner = new Scanner(rulesIS, "UTF-8");
/* 235 */     parseRules(scanner, "org/apache/commons/codec/language/dmrules.txt", RULES, FOLDINGS);
/* 236 */     scanner.close();
/*     */ 
/*     */     
/* 239 */     for (Map.Entry<Character, List<Rule>> rule : RULES.entrySet()) {
/* 240 */       List<Rule> ruleList = rule.getValue();
/* 241 */       Collections.sort(ruleList, new Comparator<Rule>()
/*     */           {
/*     */             public int compare(DaitchMokotoffSoundex.Rule rule1, DaitchMokotoffSoundex.Rule rule2) {
/* 244 */               return rule2.getPatternLength() - rule1.getPatternLength();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void parseRules(Scanner scanner, String location, Map<Character, List<Rule>> ruleMapping, Map<Character, Character> asciiFoldings) {
/* 252 */     int currentLine = 0;
/* 253 */     boolean inMultilineComment = false;
/*     */     
/* 255 */     while (scanner.hasNextLine()) {
/* 256 */       currentLine++;
/* 257 */       String rawLine = scanner.nextLine();
/* 258 */       String line = rawLine;
/*     */       
/* 260 */       if (inMultilineComment) {
/* 261 */         if (line.endsWith("*/")) {
/* 262 */           inMultilineComment = false;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 267 */       if (line.startsWith("/*")) {
/* 268 */         inMultilineComment = true;
/*     */         continue;
/*     */       } 
/* 271 */       int cmtI = line.indexOf("//");
/* 272 */       if (cmtI >= 0) {
/* 273 */         line = line.substring(0, cmtI);
/*     */       }
/*     */ 
/*     */       
/* 277 */       line = line.trim();
/*     */       
/* 279 */       if (line.length() == 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 283 */       if (line.contains("=")) {
/*     */         
/* 285 */         String[] arrayOfString = line.split("=");
/* 286 */         if (arrayOfString.length != 2) {
/* 287 */           throw new IllegalArgumentException("Malformed folding statement split into " + arrayOfString.length + " parts: " + rawLine + " in " + location);
/*     */         }
/*     */         
/* 290 */         String leftCharacter = arrayOfString[0];
/* 291 */         String rightCharacter = arrayOfString[1];
/*     */         
/* 293 */         if (leftCharacter.length() != 1 || rightCharacter.length() != 1) {
/* 294 */           throw new IllegalArgumentException("Malformed folding statement - patterns are not single characters: " + rawLine + " in " + location);
/*     */         }
/*     */ 
/*     */         
/* 298 */         asciiFoldings.put(Character.valueOf(leftCharacter.charAt(0)), Character.valueOf(rightCharacter.charAt(0)));
/*     */         
/*     */         continue;
/*     */       } 
/* 302 */       String[] parts = line.split("\\s+");
/* 303 */       if (parts.length != 4) {
/* 304 */         throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
/*     */       }
/*     */       
/*     */       try {
/* 308 */         String pattern = stripQuotes(parts[0]);
/* 309 */         String replacement1 = stripQuotes(parts[1]);
/* 310 */         String replacement2 = stripQuotes(parts[2]);
/* 311 */         String replacement3 = stripQuotes(parts[3]);
/*     */         
/* 313 */         Rule r = new Rule(pattern, replacement1, replacement2, replacement3);
/* 314 */         char patternKey = r.pattern.charAt(0);
/* 315 */         List<Rule> rules = ruleMapping.get(Character.valueOf(patternKey));
/* 316 */         if (rules == null) {
/* 317 */           rules = new ArrayList<Rule>();
/* 318 */           ruleMapping.put(Character.valueOf(patternKey), rules);
/*     */         } 
/* 320 */         rules.add(r);
/* 321 */       } catch (IllegalArgumentException e) {
/* 322 */         throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String stripQuotes(String str) {
/* 332 */     if (str.startsWith("\"")) {
/* 333 */       str = str.substring(1);
/*     */     }
/*     */     
/* 336 */     if (str.endsWith("\"")) {
/* 337 */       str = str.substring(0, str.length() - 1);
/*     */     }
/*     */     
/* 340 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DaitchMokotoffSoundex() {
/* 350 */     this(true);
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
/*     */   public DaitchMokotoffSoundex(boolean folding) {
/* 364 */     this.folding = folding;
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
/*     */   private String cleanup(String input) {
/* 378 */     StringBuilder sb = new StringBuilder();
/* 379 */     for (char ch : input.toCharArray()) {
/* 380 */       if (!Character.isWhitespace(ch)) {
/*     */ 
/*     */ 
/*     */         
/* 384 */         ch = Character.toLowerCase(ch);
/* 385 */         if (this.folding && FOLDINGS.containsKey(Character.valueOf(ch))) {
/* 386 */           ch = ((Character)FOLDINGS.get(Character.valueOf(ch))).charValue();
/*     */         }
/* 388 */         sb.append(ch);
/*     */       } 
/* 390 */     }  return sb.toString();
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 413 */     if (!(obj instanceof String)) {
/* 414 */       throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
/*     */     }
/*     */     
/* 417 */     return encode((String)obj);
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
/*     */   public String encode(String source) {
/* 433 */     if (source == null) {
/* 434 */       return null;
/*     */     }
/* 436 */     return soundex(source, false)[0];
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
/*     */   public String soundex(String source) {
/* 463 */     String[] branches = soundex(source, true);
/* 464 */     StringBuilder sb = new StringBuilder();
/* 465 */     int index = 0;
/* 466 */     for (String branch : branches) {
/* 467 */       sb.append(branch);
/* 468 */       if (++index < branches.length) {
/* 469 */         sb.append('|');
/*     */       }
/*     */     } 
/* 472 */     return sb.toString();
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
/*     */   private String[] soundex(String source, boolean branching) {
/* 486 */     if (source == null) {
/* 487 */       return null;
/*     */     }
/*     */     
/* 490 */     String input = cleanup(source);
/*     */     
/* 492 */     Set<Branch> currentBranches = new LinkedHashSet<Branch>();
/* 493 */     currentBranches.add(new Branch());
/*     */     
/* 495 */     char lastChar = Character.MIN_VALUE;
/* 496 */     for (int index = 0; index < input.length(); index++) {
/* 497 */       char ch = input.charAt(index);
/*     */ 
/*     */       
/* 500 */       if (!Character.isWhitespace(ch)) {
/*     */ 
/*     */ 
/*     */         
/* 504 */         String inputContext = input.substring(index);
/* 505 */         List<Rule> rules = RULES.get(Character.valueOf(ch));
/* 506 */         if (rules != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 512 */           List<Branch> nextBranches = branching ? new ArrayList<Branch>() : Collections.EMPTY_LIST;
/*     */           
/* 514 */           for (Rule rule : rules) {
/* 515 */             if (rule.matches(inputContext)) {
/* 516 */               if (branching) {
/* 517 */                 nextBranches.clear();
/*     */               }
/* 519 */               String[] replacements = rule.getReplacements(inputContext, (lastChar == '\000'));
/* 520 */               boolean branchingRequired = (replacements.length > 1 && branching);
/*     */               
/* 522 */               for (Branch branch : currentBranches) {
/* 523 */                 String[] arr$; int len$; int i$; for (arr$ = replacements, len$ = arr$.length, i$ = 0; i$ < len$; ) { String nextReplacement = arr$[i$];
/*     */                   
/* 525 */                   Branch nextBranch = branchingRequired ? branch.createBranch() : branch;
/*     */ 
/*     */                   
/* 528 */                   boolean force = ((lastChar == 'm' && ch == 'n') || (lastChar == 'n' && ch == 'm'));
/*     */                   
/* 530 */                   nextBranch.processNextReplacement(nextReplacement, force);
/*     */                   
/* 532 */                   if (branching) {
/* 533 */                     nextBranches.add(nextBranch);
/*     */                     
/*     */                     i$++;
/*     */                   }  }
/*     */               
/*     */               } 
/*     */               
/* 540 */               if (branching) {
/* 541 */                 currentBranches.clear();
/* 542 */                 currentBranches.addAll(nextBranches);
/*     */               } 
/* 544 */               index += rule.getPatternLength() - 1;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 549 */           lastChar = ch;
/*     */         } 
/*     */       } 
/* 552 */     }  String[] result = new String[currentBranches.size()];
/* 553 */     int i = 0;
/* 554 */     for (Branch branch : currentBranches) {
/* 555 */       branch.finish();
/* 556 */       result[i++] = branch.toString();
/*     */     } 
/*     */     
/* 559 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\DaitchMokotoffSoundex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */