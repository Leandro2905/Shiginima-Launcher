/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy) {
/* 110 */     this(strategy, false, CharMatcher.NONE, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 115 */     this.strategy = strategy;
/* 116 */     this.omitEmptyStrings = omitEmptyStrings;
/* 117 */     this.trimmer = trimmer;
/* 118 */     this.limit = limit;
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
/*     */   public static Splitter on(char separator) {
/* 130 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(final CharMatcher separatorMatcher) {
/* 144 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 146 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 149 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 int separatorStart(int start) {
/* 151 */                   return separatorMatcher.indexIn(this.toSplit, start);
/*     */                 }
/*     */                 
/*     */                 int separatorEnd(int separatorPosition) {
/* 155 */                   return separatorPosition + 1;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public static Splitter on(final String separator) {
/* 171 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/*     */ 
/*     */     
/* 174 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 177 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 public int separatorStart(int start) {
/* 179 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 182 */                   int p = start, last = this.toSplit.length() - separatorLength;
/* 183 */                   for (; p <= last; p++) {
/* 184 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 185 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 189 */                       return p; }
/*     */                   
/* 191 */                   }  return -1;
/*     */                 }
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 195 */                   return separatorPosition + separator.length();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible("java.util.regex")
/*     */   public static Splitter on(final Pattern separatorPattern) {
/* 216 */     Preconditions.checkNotNull(separatorPattern);
/* 217 */     Preconditions.checkArgument(!separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", new Object[] { separatorPattern });
/*     */ 
/*     */     
/* 220 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 223 */             final Matcher matcher = separatorPattern.matcher(toSplit);
/* 224 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 public int separatorStart(int start) {
/* 226 */                   return matcher.find(start) ? matcher.start() : -1;
/*     */                 }
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 230 */                   return matcher.end();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible("java.util.regex")
/*     */   public static Splitter onPattern(String separatorPattern) {
/* 254 */     return on(Pattern.compile(separatorPattern));
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
/*     */   public static Splitter fixedLength(final int length) {
/* 277 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 279 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 282 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 public int separatorStart(int start) {
/* 284 */                   int nextChunkStart = start + length;
/* 285 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 289 */                   return separatorPosition;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @CheckReturnValue
/*     */   public Splitter omitEmptyStrings() {
/* 316 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   @CheckReturnValue
/*     */   public Splitter limit(int limit) {
/* 340 */     Preconditions.checkArgument((limit > 0), "must be greater than zero: %s", new Object[] { Integer.valueOf(limit) });
/* 341 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
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
/*     */   @CheckReturnValue
/*     */   public Splitter trimResults() {
/* 356 */     return trimResults(CharMatcher.WHITESPACE);
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
/*     */   @CheckReturnValue
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 373 */     Preconditions.checkNotNull(trimmer);
/* 374 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 386 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 388 */     return new Iterable<String>() {
/*     */         public Iterator<String> iterator() {
/* 390 */           return Splitter.this.splittingIterator(sequence);
/*     */         }
/*     */         public String toString() {
/* 393 */           return Joiner.on(", ").appendTo((new StringBuilder()).append('['), this).append(']').toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Iterator<String> splittingIterator(CharSequence sequence) {
/* 402 */     return this.strategy.iterator(this, sequence);
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
/*     */   @Beta
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 416 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 418 */     Iterator<String> iterator = splittingIterator(sequence);
/* 419 */     List<String> result = new ArrayList<String>();
/*     */     
/* 421 */     while (iterator.hasNext()) {
/* 422 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 425 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(String separator) {
/* 437 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(char separator) {
/* 449 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) {
/* 462 */     return new MapSplitter(this, keyValueSplitter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */ 
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */ 
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 480 */       this.outerSplitter = outerSplitter;
/* 481 */       this.entrySplitter = Preconditions.<Splitter>checkNotNull(entrySplitter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 500 */       Map<String, String> map = new LinkedHashMap<String, String>();
/* 501 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 502 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 504 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/* 505 */         String key = entryFields.next();
/* 506 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", new Object[] { key });
/*     */         
/* 508 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/* 509 */         String value = entryFields.next();
/* 510 */         map.put(key, value);
/*     */         
/* 512 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/*     */       } 
/* 514 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */ 
/*     */     
/*     */     final CharMatcher trimmer;
/*     */ 
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */ 
/*     */     
/* 540 */     int offset = 0;
/*     */     int limit;
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 544 */       this.trimmer = splitter.trimmer;
/* 545 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 546 */       this.limit = splitter.limit;
/* 547 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String computeNext() {
/* 557 */       int nextStart = this.offset;
/* 558 */       while (this.offset != -1) {
/* 559 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 562 */         int separatorPosition = separatorStart(this.offset);
/* 563 */         if (separatorPosition == -1) {
/* 564 */           end = this.toSplit.length();
/* 565 */           this.offset = -1;
/*     */         } else {
/* 567 */           end = separatorPosition;
/* 568 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 570 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 578 */           this.offset++;
/* 579 */           if (this.offset >= this.toSplit.length()) {
/* 580 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 585 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 586 */           start++;
/*     */         }
/* 588 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 589 */           end--;
/*     */         }
/*     */         
/* 592 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 594 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 598 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 602 */           end = this.toSplit.length();
/* 603 */           this.offset = -1;
/*     */           
/* 605 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 606 */             end--;
/*     */           }
/*     */         } else {
/* 609 */           this.limit--;
/*     */         } 
/*     */         
/* 612 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 614 */       return endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Splitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */