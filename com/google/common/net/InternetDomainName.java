/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class InternetDomainName
/*     */ {
/*  79 */   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
/*     */   
/*  81 */   private static final Splitter DOT_SPLITTER = Splitter.on('.');
/*  82 */   private static final Joiner DOT_JOINER = Joiner.on('.');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DOT_REGEX = "\\.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_PARTS = 127;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 253;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 63;
/*     */ 
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
/*     */   private final ImmutableList<String> parts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int publicSuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternetDomainName(String name) {
/* 143 */     name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
/*     */     
/* 145 */     if (name.endsWith(".")) {
/* 146 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */     
/* 149 */     Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", new Object[] { name });
/*     */     
/* 151 */     this.name = name;
/*     */     
/* 153 */     this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
/* 154 */     Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", new Object[] { name });
/*     */     
/* 156 */     Preconditions.checkArgument(validateSyntax((List<String>)this.parts), "Not a valid domain name: '%s'", new Object[] { name });
/*     */     
/* 158 */     this.publicSuffixIndex = findPublicSuffix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findPublicSuffix() {
/* 168 */     int partsSize = this.parts.size();
/*     */     
/* 170 */     for (int i = 0; i < partsSize; i++) {
/* 171 */       String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
/*     */       
/* 173 */       if (PublicSuffixPatterns.EXACT.containsKey(ancestorName)) {
/* 174 */         return i;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
/* 181 */         return i + 1;
/*     */       }
/*     */       
/* 184 */       if (matchesWildcardPublicSuffix(ancestorName)) {
/* 185 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 189 */     return -1;
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
/*     */   public static InternetDomainName from(String domain) {
/* 213 */     return new InternetDomainName((String)Preconditions.checkNotNull(domain));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateSyntax(List<String> parts) {
/* 223 */     int lastIndex = parts.size() - 1;
/*     */ 
/*     */ 
/*     */     
/* 227 */     if (!validatePart(parts.get(lastIndex), true)) {
/* 228 */       return false;
/*     */     }
/*     */     
/* 231 */     for (int i = 0; i < lastIndex; i++) {
/* 232 */       String part = parts.get(i);
/* 233 */       if (!validatePart(part, false)) {
/* 234 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 238 */     return true;
/*     */   }
/*     */   
/* 241 */   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
/*     */   
/* 243 */   private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(DASH_MATCHER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePart(String part, boolean isFinalPart) {
/* 259 */     if (part.length() < 1 || part.length() > 63) {
/* 260 */       return false;
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
/* 273 */     String asciiChars = CharMatcher.ASCII.retainFrom(part);
/*     */     
/* 275 */     if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
/* 276 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 281 */     if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER.matches(part.charAt(part.length() - 1)))
/*     */     {
/* 283 */       return false;
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
/* 294 */     if (isFinalPart && CharMatcher.DIGIT.matches(part.charAt(0))) {
/* 295 */       return false;
/*     */     }
/*     */     
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<String> parts() {
/* 307 */     return this.parts;
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
/*     */   public boolean isPublicSuffix() {
/* 324 */     return (this.publicSuffixIndex == 0);
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
/*     */   public boolean hasPublicSuffix() {
/* 338 */     return (this.publicSuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName publicSuffix() {
/* 348 */     return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
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
/*     */   public boolean isUnderPublicSuffix() {
/* 371 */     return (this.publicSuffixIndex > 0);
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
/*     */   public boolean isTopPrivateDomain() {
/* 395 */     return (this.publicSuffixIndex == 1);
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
/*     */   public InternetDomainName topPrivateDomain() {
/* 421 */     if (isTopPrivateDomain()) {
/* 422 */       return this;
/*     */     }
/* 424 */     Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", new Object[] { this.name });
/* 425 */     return ancestor(this.publicSuffixIndex - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 432 */     return (this.parts.size() > 1);
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
/*     */   public InternetDomainName parent() {
/* 444 */     Preconditions.checkState(hasParent(), "Domain '%s' has no parent", new Object[] { this.name });
/* 445 */     return ancestor(1);
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
/*     */   private InternetDomainName ancestor(int levels) {
/* 457 */     return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
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
/*     */   public InternetDomainName child(String leftParts) {
/* 471 */     String str1 = String.valueOf(String.valueOf(Preconditions.checkNotNull(leftParts))), str2 = String.valueOf(String.valueOf(this.name)); return from((new StringBuilder(1 + str1.length() + str2.length())).append(str1).append(".").append(str2).toString());
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
/*     */   public static boolean isValid(String name) {
/*     */     try {
/* 498 */       from(name);
/* 499 */       return true;
/* 500 */     } catch (IllegalArgumentException e) {
/* 501 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesWildcardPublicSuffix(String domain) {
/* 510 */     String[] pieces = domain.split("\\.", 2);
/* 511 */     return (pieces.length == 2 && PublicSuffixPatterns.UNDER.containsKey(pieces[1]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 519 */     return this.name;
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
/*     */   public boolean equals(@Nullable Object object) {
/* 531 */     if (object == this) {
/* 532 */       return true;
/*     */     }
/*     */     
/* 535 */     if (object instanceof InternetDomainName) {
/* 536 */       InternetDomainName that = (InternetDomainName)object;
/* 537 */       return this.name.equals(that.name);
/*     */     } 
/*     */     
/* 540 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 545 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\net\InternetDomainName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */