/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class CharMatcher
/*      */   implements Predicate<Character>
/*      */ {
/*   67 */   public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher()
/*      */     {
/*      */       public boolean matches(char c) {
/*   70 */         switch (c) {
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\013':
/*      */           case '\f':
/*      */           case '\r':
/*      */           case ' ':
/*      */           case '':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case '　':
/*   83 */             return true;
/*      */           case ' ':
/*   85 */             return false;
/*      */         } 
/*   87 */         return (c >= ' ' && c <= ' ');
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*   93 */         return "CharMatcher.BREAKING_WHITESPACE";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  100 */   public static final CharMatcher ASCII = inRange(false, '', "CharMatcher.ASCII"); private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
/*      */   private static final String NINES;
/*      */   
/*      */   private static class RangesMatcher extends CharMatcher { private final char[] rangeStarts;
/*      */     private final char[] rangeEnds;
/*      */     
/*      */     RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
/*  107 */       super(description);
/*  108 */       this.rangeStarts = rangeStarts;
/*  109 */       this.rangeEnds = rangeEnds;
/*  110 */       Preconditions.checkArgument((rangeStarts.length == rangeEnds.length));
/*  111 */       for (int i = 0; i < rangeStarts.length; i++) {
/*  112 */         Preconditions.checkArgument((rangeStarts[i] <= rangeEnds[i]));
/*  113 */         if (i + 1 < rangeStarts.length) {
/*  114 */           Preconditions.checkArgument((rangeEnds[i] < rangeStarts[i + 1]));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  121 */       int index = Arrays.binarySearch(this.rangeStarts, c);
/*  122 */       if (index >= 0) {
/*  123 */         return true;
/*      */       }
/*  125 */       index = (index ^ 0xFFFFFFFF) - 1;
/*  126 */       return (index >= 0 && c <= this.rangeEnds[index]);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  138 */     StringBuilder builder = new StringBuilder("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length());
/*  139 */     for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
/*  140 */       builder.append((char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + 9));
/*      */     }
/*  142 */     NINES = builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   public static final CharMatcher DIGIT = new RangesMatcher("CharMatcher.DIGIT", "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray(), NINES.toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final CharMatcher JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
/*      */       public boolean matches(char c) {
/*  160 */         return Character.isDigit(c);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   public static final CharMatcher JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
/*      */       public boolean matches(char c) {
/*  171 */         return Character.isLetter(c);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  179 */   public static final CharMatcher JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT")
/*      */     {
/*      */       public boolean matches(char c) {
/*  182 */         return Character.isLetterOrDigit(c);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  190 */   public static final CharMatcher JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE")
/*      */     {
/*      */       public boolean matches(char c) {
/*  193 */         return Character.isUpperCase(c);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  201 */   public static final CharMatcher JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE")
/*      */     {
/*      */       public boolean matches(char c) {
/*  204 */         return Character.isLowerCase(c);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  212 */   public static final CharMatcher JAVA_ISO_CONTROL = inRange(false, '\037').or(inRange('', '')).withToString("CharMatcher.JAVA_ISO_CONTROL");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  222 */   public static final CharMatcher INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String showCharacter(char c) {
/*  229 */     String hex = "0123456789ABCDEF";
/*  230 */     char[] tmp = { '\\', 'u', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };
/*  231 */     for (int i = 0; i < 4; i++) {
/*  232 */       tmp[5 - i] = hex.charAt(c & 0xF);
/*  233 */       c = (char)(c >> 4);
/*      */     } 
/*  235 */     return String.copyValueOf(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  247 */   public static final CharMatcher SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  252 */   public static final CharMatcher ANY = new FastMatcher("CharMatcher.ANY")
/*      */     {
/*      */       public boolean matches(char c) {
/*  255 */         return true;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence) {
/*  259 */         return (sequence.length() == 0) ? -1 : 0;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence, int start) {
/*  263 */         int length = sequence.length();
/*  264 */         Preconditions.checkPositionIndex(start, length);
/*  265 */         return (start == length) ? -1 : start;
/*      */       }
/*      */       
/*      */       public int lastIndexIn(CharSequence sequence) {
/*  269 */         return sequence.length() - 1;
/*      */       }
/*      */       
/*      */       public boolean matchesAllOf(CharSequence sequence) {
/*  273 */         Preconditions.checkNotNull(sequence);
/*  274 */         return true;
/*      */       }
/*      */       
/*      */       public boolean matchesNoneOf(CharSequence sequence) {
/*  278 */         return (sequence.length() == 0);
/*      */       }
/*      */       
/*      */       public String removeFrom(CharSequence sequence) {
/*  282 */         Preconditions.checkNotNull(sequence);
/*  283 */         return "";
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, char replacement) {
/*  287 */         char[] array = new char[sequence.length()];
/*  288 */         Arrays.fill(array, replacement);
/*  289 */         return new String(array);
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  293 */         StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
/*  294 */         for (int i = 0; i < sequence.length(); i++) {
/*  295 */           retval.append(replacement);
/*      */         }
/*  297 */         return retval.toString();
/*      */       }
/*      */       
/*      */       public String collapseFrom(CharSequence sequence, char replacement) {
/*  301 */         return (sequence.length() == 0) ? "" : String.valueOf(replacement);
/*      */       }
/*      */       
/*      */       public String trimFrom(CharSequence sequence) {
/*  305 */         Preconditions.checkNotNull(sequence);
/*  306 */         return "";
/*      */       }
/*      */       
/*      */       public int countIn(CharSequence sequence) {
/*  310 */         return sequence.length();
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other) {
/*  314 */         return Preconditions.<CharMatcher>checkNotNull(other);
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other) {
/*  318 */         Preconditions.checkNotNull(other);
/*  319 */         return this;
/*      */       }
/*      */       
/*      */       public CharMatcher negate() {
/*  323 */         return NONE;
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*  328 */   public static final CharMatcher NONE = new FastMatcher("CharMatcher.NONE")
/*      */     {
/*      */       public boolean matches(char c) {
/*  331 */         return false;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence) {
/*  335 */         Preconditions.checkNotNull(sequence);
/*  336 */         return -1;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence, int start) {
/*  340 */         int length = sequence.length();
/*  341 */         Preconditions.checkPositionIndex(start, length);
/*  342 */         return -1;
/*      */       }
/*      */       
/*      */       public int lastIndexIn(CharSequence sequence) {
/*  346 */         Preconditions.checkNotNull(sequence);
/*  347 */         return -1;
/*      */       }
/*      */       
/*      */       public boolean matchesAllOf(CharSequence sequence) {
/*  351 */         return (sequence.length() == 0);
/*      */       }
/*      */       
/*      */       public boolean matchesNoneOf(CharSequence sequence) {
/*  355 */         Preconditions.checkNotNull(sequence);
/*  356 */         return true;
/*      */       }
/*      */       
/*      */       public String removeFrom(CharSequence sequence) {
/*  360 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, char replacement) {
/*  364 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  368 */         Preconditions.checkNotNull(replacement);
/*  369 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String collapseFrom(CharSequence sequence, char replacement) {
/*  373 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String trimFrom(CharSequence sequence) {
/*  377 */         return sequence.toString();
/*      */       }
/*      */ 
/*      */       
/*      */       public String trimLeadingFrom(CharSequence sequence) {
/*  382 */         return sequence.toString();
/*      */       }
/*      */ 
/*      */       
/*      */       public String trimTrailingFrom(CharSequence sequence) {
/*  387 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public int countIn(CharSequence sequence) {
/*  391 */         Preconditions.checkNotNull(sequence);
/*  392 */         return 0;
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other) {
/*  396 */         Preconditions.checkNotNull(other);
/*  397 */         return this;
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other) {
/*  401 */         return Preconditions.<CharMatcher>checkNotNull(other);
/*      */       }
/*      */       
/*      */       public CharMatcher negate() {
/*  405 */         return ANY;
/*      */       }
/*      */     };
/*      */   
/*      */   final String description;
/*      */   private static final int DISTINCT_CHARS = 65536;
/*      */   static final String WHITESPACE_TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */   static final int WHITESPACE_MULTIPLIER = 1682554634;
/*      */   
/*      */   public static CharMatcher is(final char match) {
/*  415 */     String str1 = String.valueOf(String.valueOf(showCharacter(match))), description = (new StringBuilder(18 + str1.length())).append("CharMatcher.is('").append(str1).append("')").toString();
/*  416 */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) {
/*  418 */           return (c == match);
/*      */         }
/*      */         
/*      */         public String replaceFrom(CharSequence sequence, char replacement) {
/*  422 */           return sequence.toString().replace(match, replacement);
/*      */         }
/*      */         
/*      */         public CharMatcher and(CharMatcher other) {
/*  426 */           return other.matches(match) ? this : NONE;
/*      */         }
/*      */         
/*      */         public CharMatcher or(CharMatcher other) {
/*  430 */           return other.matches(match) ? other : super.or(other);
/*      */         }
/*      */         
/*      */         public CharMatcher negate() {
/*  434 */           return isNot(match);
/*      */         }
/*      */ 
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*  440 */           table.set(match);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher isNot(final char match) {
/*  451 */     String str1 = String.valueOf(String.valueOf(showCharacter(match))), description = (new StringBuilder(21 + str1.length())).append("CharMatcher.isNot('").append(str1).append("')").toString();
/*  452 */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) {
/*  454 */           return (c != match);
/*      */         }
/*      */         
/*      */         public CharMatcher and(CharMatcher other) {
/*  458 */           return other.matches(match) ? super.and(other) : other;
/*      */         }
/*      */         
/*      */         public CharMatcher or(CharMatcher other) {
/*  462 */           return other.matches(match) ? ANY : this;
/*      */         }
/*      */ 
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*  468 */           table.set(0, match);
/*  469 */           table.set(match + 1, 65536);
/*      */         }
/*      */         
/*      */         public CharMatcher negate() {
/*  473 */           return is(match);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher anyOf(CharSequence sequence) {
/*  483 */     switch (sequence.length()) {
/*      */       case 0:
/*  485 */         return NONE;
/*      */       case 1:
/*  487 */         return is(sequence.charAt(0));
/*      */       case 2:
/*  489 */         return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  494 */     final char[] chars = sequence.toString().toCharArray();
/*  495 */     Arrays.sort(chars);
/*  496 */     StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/*  497 */     for (char c : chars) {
/*  498 */       description.append(showCharacter(c));
/*      */     }
/*  500 */     description.append("\")");
/*  501 */     return new CharMatcher(description.toString()) {
/*      */         public boolean matches(char c) {
/*  503 */           return (Arrays.binarySearch(chars, c) >= 0);
/*      */         }
/*      */ 
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*  509 */           for (char c : chars) {
/*  510 */             table.set(c);
/*      */           }
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static CharMatcher isEither(final char match1, final char match2) {
/*  519 */     String str1 = String.valueOf(String.valueOf(showCharacter(match1))), str2 = String.valueOf(String.valueOf(showCharacter(match2))), description = (new StringBuilder(21 + str1.length() + str2.length())).append("CharMatcher.anyOf(\"").append(str1).append(str2).append("\")").toString();
/*      */     
/*  521 */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) {
/*  523 */           return (c == match1 || c == match2);
/*      */         }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*  528 */           table.set(match1);
/*  529 */           table.set(match2);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher noneOf(CharSequence sequence) {
/*  539 */     return anyOf(sequence).negate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive) {
/*  550 */     Preconditions.checkArgument((endInclusive >= startInclusive));
/*  551 */     String str1 = String.valueOf(String.valueOf(showCharacter(startInclusive))), str2 = String.valueOf(String.valueOf(showCharacter(endInclusive))), description = (new StringBuilder(27 + str1.length() + str2.length())).append("CharMatcher.inRange('").append(str1).append("', '").append(str2).append("')").toString();
/*      */ 
/*      */     
/*  554 */     return inRange(startInclusive, endInclusive, description);
/*      */   }
/*      */ 
/*      */   
/*      */   static CharMatcher inRange(final char startInclusive, final char endInclusive, String description) {
/*  559 */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) {
/*  561 */           return (startInclusive <= c && c <= endInclusive);
/*      */         }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*  566 */           table.set(startInclusive, endInclusive + 1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
/*  576 */     Preconditions.checkNotNull(predicate);
/*  577 */     if (predicate instanceof CharMatcher) {
/*  578 */       return (CharMatcher)predicate;
/*      */     }
/*  580 */     String str1 = String.valueOf(String.valueOf(predicate)), description = (new StringBuilder(26 + str1.length())).append("CharMatcher.forPredicate(").append(str1).append(")").toString();
/*  581 */     return new CharMatcher(description) {
/*      */         public boolean matches(char c) {
/*  583 */           return predicate.apply(Character.valueOf(c));
/*      */         }
/*      */         
/*      */         public boolean apply(Character character) {
/*  587 */           return predicate.apply(Preconditions.checkNotNull(character));
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CharMatcher(String description) {
/*  601 */     this.description = description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CharMatcher() {
/*  609 */     this.description = super.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher negate() {
/*  623 */     return new NegatedMatcher(this);
/*      */   }
/*      */   
/*      */   private static class NegatedMatcher extends CharMatcher {
/*      */     final CharMatcher original;
/*      */     
/*      */     NegatedMatcher(String toString, CharMatcher original) {
/*  630 */       super(toString);
/*  631 */       this.original = original;
/*      */     }
/*      */     
/*      */     NegatedMatcher(CharMatcher original) {
/*  635 */       this((new StringBuilder(9 + str.length())).append(str).append(".negate()").toString(), original);
/*      */     }
/*      */     
/*      */     public boolean matches(char c) {
/*  639 */       return !this.original.matches(c);
/*      */     }
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/*  643 */       return this.original.matchesNoneOf(sequence);
/*      */     }
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/*  647 */       return this.original.matchesAllOf(sequence);
/*      */     }
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/*  651 */       return sequence.length() - this.original.countIn(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*  657 */       BitSet tmp = new BitSet();
/*  658 */       this.original.setBits(tmp);
/*  659 */       tmp.flip(0, 65536);
/*  660 */       table.or(tmp);
/*      */     }
/*      */     
/*      */     public CharMatcher negate() {
/*  664 */       return this.original;
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher withToString(String description) {
/*  669 */       return new NegatedMatcher(description, this.original);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher and(CharMatcher other) {
/*  677 */     return new And(this, Preconditions.<CharMatcher>checkNotNull(other));
/*      */   }
/*      */   
/*      */   private static class And extends CharMatcher {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     And(CharMatcher a, CharMatcher b) {
/*  685 */       this(a, b, (new StringBuilder(19 + str1.length() + str2.length())).append("CharMatcher.and(").append(str1).append(", ").append(str2).append(")").toString());
/*      */     }
/*      */     
/*      */     And(CharMatcher a, CharMatcher b, String description) {
/*  689 */       super(description);
/*  690 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/*  691 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  696 */       return (this.first.matches(c) && this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*  702 */       BitSet tmp1 = new BitSet();
/*  703 */       this.first.setBits(tmp1);
/*  704 */       BitSet tmp2 = new BitSet();
/*  705 */       this.second.setBits(tmp2);
/*  706 */       tmp1.and(tmp2);
/*  707 */       table.or(tmp1);
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher withToString(String description) {
/*  712 */       return new And(this.first, this.second, description);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher or(CharMatcher other) {
/*  720 */     return new Or(this, Preconditions.<CharMatcher>checkNotNull(other));
/*      */   }
/*      */   
/*      */   private static class Or extends CharMatcher {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b, String description) {
/*  728 */       super(description);
/*  729 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/*  730 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b) {
/*  734 */       this(a, b, (new StringBuilder(18 + str1.length() + str2.length())).append("CharMatcher.or(").append(str1).append(", ").append(str2).append(")").toString());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*  740 */       this.first.setBits(table);
/*  741 */       this.second.setBits(table);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  746 */       return (this.first.matches(c) || this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher withToString(String description) {
/*  751 */       return new Or(this.first, this.second, description);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher precomputed() {
/*  765 */     return Platform.precomputeCharMatcher(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CharMatcher withToString(String description) {
/*  775 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   CharMatcher precomputedInternal() {
/*  792 */     BitSet table = new BitSet();
/*  793 */     setBits(table);
/*  794 */     int totalCharacters = table.cardinality();
/*  795 */     if (totalCharacters * 2 <= 65536) {
/*  796 */       return precomputedPositive(totalCharacters, table, this.description);
/*      */     }
/*      */     
/*  799 */     table.flip(0, 65536);
/*  800 */     int negatedCharacters = 65536 - totalCharacters;
/*  801 */     String suffix = ".negate()";
/*  802 */     String.valueOf(suffix); String negatedDescription = this.description.endsWith(suffix) ? this.description.substring(0, this.description.length() - suffix.length()) : ((String.valueOf(suffix).length() != 0) ? String.valueOf(this.description).concat(String.valueOf(suffix)) : new String(String.valueOf(this.description)));
/*      */ 
/*      */     
/*  805 */     return new NegatedFastMatcher(toString(), precomputedPositive(negatedCharacters, table, negatedDescription));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class FastMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     FastMatcher() {}
/*      */ 
/*      */ 
/*      */     
/*      */     FastMatcher(String description) {
/*  819 */       super(description);
/*      */     }
/*      */ 
/*      */     
/*      */     public final CharMatcher precomputed() {
/*  824 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/*  829 */       return new CharMatcher.NegatedFastMatcher(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class NegatedFastMatcher extends NegatedMatcher {
/*      */     NegatedFastMatcher(CharMatcher original) {
/*  835 */       super(original);
/*      */     }
/*      */     
/*      */     NegatedFastMatcher(String toString, CharMatcher original) {
/*  839 */       super(toString, original);
/*      */     }
/*      */ 
/*      */     
/*      */     public final CharMatcher precomputed() {
/*  844 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher withToString(String description) {
/*  849 */       return new NegatedFastMatcher(description, this.original);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
/*      */     char c1;
/*      */     char c2;
/*  861 */     switch (totalCharacters) {
/*      */       case 0:
/*  863 */         return NONE;
/*      */       case 1:
/*  865 */         return is((char)table.nextSetBit(0));
/*      */       case 2:
/*  867 */         c1 = (char)table.nextSetBit(0);
/*  868 */         c2 = (char)table.nextSetBit(c1 + 1);
/*  869 */         return isEither(c1, c2);
/*      */     } 
/*  871 */     return isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("SmallCharMatcher")
/*      */   private static boolean isSmall(int totalCharacters, int tableLength) {
/*  879 */     return (totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16);
/*      */   }
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   private static class BitSetMatcher
/*      */     extends FastMatcher
/*      */   {
/*      */     private final BitSet table;
/*      */     
/*      */     private BitSetMatcher(BitSet table, String description) {
/*  889 */       super(description);
/*  890 */       if (table.length() + 64 < table.size()) {
/*  891 */         table = (BitSet)table.clone();
/*      */       }
/*      */       
/*  894 */       this.table = table;
/*      */     }
/*      */     
/*      */     public boolean matches(char c) {
/*  898 */       return this.table.get(c);
/*      */     }
/*      */ 
/*      */     
/*      */     void setBits(BitSet bitSet) {
/*  903 */       bitSet.or(this.table);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   void setBits(BitSet table) {
/*  912 */     for (int c = 65535; c >= 0; c--) {
/*  913 */       if (matches((char)c)) {
/*  914 */         table.set(c);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAnyOf(CharSequence sequence) {
/*  933 */     return !matchesNoneOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAllOf(CharSequence sequence) {
/*  947 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  948 */       if (!matches(sequence.charAt(i))) {
/*  949 */         return false;
/*      */       }
/*      */     } 
/*  952 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesNoneOf(CharSequence sequence) {
/*  967 */     return (indexIn(sequence) == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence) {
/*  981 */     int length = sequence.length();
/*  982 */     for (int i = 0; i < length; i++) {
/*  983 */       if (matches(sequence.charAt(i))) {
/*  984 */         return i;
/*      */       }
/*      */     } 
/*  987 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence, int start) {
/* 1006 */     int length = sequence.length();
/* 1007 */     Preconditions.checkPositionIndex(start, length);
/* 1008 */     for (int i = start; i < length; i++) {
/* 1009 */       if (matches(sequence.charAt(i))) {
/* 1010 */         return i;
/*      */       }
/*      */     } 
/* 1013 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexIn(CharSequence sequence) {
/* 1027 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/* 1028 */       if (matches(sequence.charAt(i))) {
/* 1029 */         return i;
/*      */       }
/*      */     } 
/* 1032 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int countIn(CharSequence sequence) {
/* 1039 */     int count = 0;
/* 1040 */     for (int i = 0; i < sequence.length(); i++) {
/* 1041 */       if (matches(sequence.charAt(i))) {
/* 1042 */         count++;
/*      */       }
/*      */     } 
/* 1045 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String removeFrom(CharSequence sequence) {
/* 1058 */     String string = sequence.toString();
/* 1059 */     int pos = indexIn(string);
/* 1060 */     if (pos == -1) {
/* 1061 */       return string;
/*      */     }
/*      */     
/* 1064 */     char[] chars = string.toCharArray();
/* 1065 */     int spread = 1;
/*      */ 
/*      */     
/*      */     while (true) {
/* 1069 */       pos++;
/*      */       
/* 1071 */       while (pos != chars.length) {
/*      */ 
/*      */         
/* 1074 */         if (matches(chars[pos]))
/*      */         
/*      */         { 
/*      */ 
/*      */ 
/*      */           
/* 1080 */           spread++; continue; }  chars[pos - spread] = chars[pos]; pos++;
/*      */       }  break;
/* 1082 */     }  return new String(chars, 0, pos - spread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String retainFrom(CharSequence sequence) {
/* 1095 */     return negate().removeFrom(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String replaceFrom(CharSequence sequence, char replacement) {
/* 1117 */     String string = sequence.toString();
/* 1118 */     int pos = indexIn(string);
/* 1119 */     if (pos == -1) {
/* 1120 */       return string;
/*      */     }
/* 1122 */     char[] chars = string.toCharArray();
/* 1123 */     chars[pos] = replacement;
/* 1124 */     for (int i = pos + 1; i < chars.length; i++) {
/* 1125 */       if (matches(chars[i])) {
/* 1126 */         chars[i] = replacement;
/*      */       }
/*      */     } 
/* 1129 */     return new String(chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1150 */     int replacementLen = replacement.length();
/* 1151 */     if (replacementLen == 0) {
/* 1152 */       return removeFrom(sequence);
/*      */     }
/* 1154 */     if (replacementLen == 1) {
/* 1155 */       return replaceFrom(sequence, replacement.charAt(0));
/*      */     }
/*      */     
/* 1158 */     String string = sequence.toString();
/* 1159 */     int pos = indexIn(string);
/* 1160 */     if (pos == -1) {
/* 1161 */       return string;
/*      */     }
/*      */     
/* 1164 */     int len = string.length();
/* 1165 */     StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */     
/* 1167 */     int oldpos = 0;
/*      */     do {
/* 1169 */       buf.append(string, oldpos, pos);
/* 1170 */       buf.append(replacement);
/* 1171 */       oldpos = pos + 1;
/* 1172 */       pos = indexIn(string, oldpos);
/* 1173 */     } while (pos != -1);
/*      */     
/* 1175 */     buf.append(string, oldpos, len);
/* 1176 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimFrom(CharSequence sequence) {
/* 1195 */     int len = sequence.length();
/*      */     
/*      */     int first;
/*      */     
/* 1199 */     for (first = 0; first < len && 
/* 1200 */       matches(sequence.charAt(first)); first++);
/*      */     
/*      */     int last;
/*      */     
/* 1204 */     for (last = len - 1; last > first && 
/* 1205 */       matches(sequence.charAt(last)); last--);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1210 */     return sequence.subSequence(first, last + 1).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimLeadingFrom(CharSequence sequence) {
/* 1223 */     int len = sequence.length();
/* 1224 */     for (int first = 0; first < len; first++) {
/* 1225 */       if (!matches(sequence.charAt(first))) {
/* 1226 */         return sequence.subSequence(first, len).toString();
/*      */       }
/*      */     } 
/* 1229 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimTrailingFrom(CharSequence sequence) {
/* 1242 */     int len = sequence.length();
/* 1243 */     for (int last = len - 1; last >= 0; last--) {
/* 1244 */       if (!matches(sequence.charAt(last))) {
/* 1245 */         return sequence.subSequence(0, last + 1).toString();
/*      */       }
/*      */     } 
/* 1248 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String collapseFrom(CharSequence sequence, char replacement) {
/* 1272 */     int len = sequence.length();
/* 1273 */     for (int i = 0; i < len; i++) {
/* 1274 */       char c = sequence.charAt(i);
/* 1275 */       if (matches(c)) {
/* 1276 */         if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
/*      */ 
/*      */           
/* 1279 */           i++;
/*      */         } else {
/* 1281 */           StringBuilder builder = (new StringBuilder(len)).append(sequence.subSequence(0, i)).append(replacement);
/*      */ 
/*      */           
/* 1284 */           return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1289 */     return sequence.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
/* 1300 */     int len = sequence.length();
/*      */     
/*      */     int first;
/*      */     
/* 1304 */     for (first = 0; first < len && matches(sequence.charAt(first)); first++); int last;
/* 1305 */     for (last = len - 1; last > first && matches(sequence.charAt(last)); last--);
/*      */     
/* 1307 */     return (first == 0 && last == len - 1) ? collapseFrom(sequence, replacement) : finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
/* 1318 */     for (int i = start; i < end; i++) {
/* 1319 */       char c = sequence.charAt(i);
/* 1320 */       if (matches(c)) {
/* 1321 */         if (!inMatchingGroup) {
/* 1322 */           builder.append(replacement);
/* 1323 */           inMatchingGroup = true;
/*      */         } 
/*      */       } else {
/* 1326 */         builder.append(c);
/* 1327 */         inMatchingGroup = false;
/*      */       } 
/*      */     } 
/* 1330 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean apply(Character character) {
/* 1340 */     return matches(character.charValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1349 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1358 */   static final int WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1371 */   public static final CharMatcher WHITESPACE = new FastMatcher("WHITESPACE")
/*      */     {
/*      */       public boolean matches(char c) {
/* 1374 */         return (" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> WHITESPACE_SHIFT) == c);
/*      */       }
/*      */ 
/*      */       
/*      */       @GwtIncompatible("java.util.BitSet")
/*      */       void setBits(BitSet table) {
/* 1380 */         for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++)
/* 1381 */           table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i)); 
/*      */       }
/*      */     };
/*      */   
/*      */   public abstract boolean matches(char paramChar);
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\CharMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */