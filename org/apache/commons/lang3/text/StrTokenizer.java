/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
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
/*      */ public class StrTokenizer
/*      */   implements ListIterator<String>, Cloneable
/*      */ {
/*   93 */   private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*   94 */     CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
/*   95 */     CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*   96 */     CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*   97 */     CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*   98 */     CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*   99 */     CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*  101 */   private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*  102 */     TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
/*  103 */     TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*  104 */     TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*  105 */     TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*  106 */     TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*  107 */     TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private char[] chars;
/*      */   
/*      */   private String[] tokens;
/*      */   
/*      */   private int tokenPos;
/*      */   
/*  118 */   private StrMatcher delimMatcher = StrMatcher.splitMatcher();
/*      */   
/*  120 */   private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
/*      */   
/*  122 */   private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
/*      */   
/*  124 */   private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean emptyAsNull = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreEmptyTokens = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getCSVClone() {
/*  139 */     return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
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
/*      */   public static StrTokenizer getCSVInstance() {
/*  152 */     return getCSVClone();
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
/*      */   public static StrTokenizer getCSVInstance(String input) {
/*  165 */     StrTokenizer tok = getCSVClone();
/*  166 */     tok.reset(input);
/*  167 */     return tok;
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
/*      */   public static StrTokenizer getCSVInstance(char[] input) {
/*  180 */     StrTokenizer tok = getCSVClone();
/*  181 */     tok.reset(input);
/*  182 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getTSVClone() {
/*  191 */     return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
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
/*      */   public static StrTokenizer getTSVInstance() {
/*  204 */     return getTSVClone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(String input) {
/*  215 */     StrTokenizer tok = getTSVClone();
/*  216 */     tok.reset(input);
/*  217 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(char[] input) {
/*  228 */     StrTokenizer tok = getTSVClone();
/*  229 */     tok.reset(input);
/*  230 */     return tok;
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
/*      */   public StrTokenizer() {
/*  242 */     this.chars = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input) {
/*  253 */     if (input != null) {
/*  254 */       this.chars = input.toCharArray();
/*      */     } else {
/*  256 */       this.chars = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, char delim) {
/*  267 */     this(input);
/*  268 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, String delim) {
/*  278 */     this(input);
/*  279 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, StrMatcher delim) {
/*  289 */     this(input);
/*  290 */     setDelimiterMatcher(delim);
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
/*      */   public StrTokenizer(String input, char delim, char quote) {
/*  302 */     this(input, delim);
/*  303 */     setQuoteChar(quote);
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
/*      */   public StrTokenizer(String input, StrMatcher delim, StrMatcher quote) {
/*  315 */     this(input, delim);
/*  316 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input) {
/*  327 */     this.chars = ArrayUtils.clone(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, char delim) {
/*  337 */     this(input);
/*  338 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, String delim) {
/*  348 */     this(input);
/*  349 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, StrMatcher delim) {
/*  359 */     this(input);
/*  360 */     setDelimiterMatcher(delim);
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
/*      */   public StrTokenizer(char[] input, char delim, char quote) {
/*  372 */     this(input, delim);
/*  373 */     setQuoteChar(quote);
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
/*      */   public StrTokenizer(char[] input, StrMatcher delim, StrMatcher quote) {
/*  385 */     this(input, delim);
/*  386 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  397 */     checkTokenized();
/*  398 */     return this.tokens.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextToken() {
/*  409 */     if (hasNext()) {
/*  410 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  412 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previousToken() {
/*  421 */     if (hasPrevious()) {
/*  422 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  424 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getTokenArray() {
/*  433 */     checkTokenized();
/*  434 */     return (String[])this.tokens.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTokenList() {
/*  443 */     checkTokenized();
/*  444 */     List<String> list = new ArrayList<String>(this.tokens.length);
/*  445 */     for (String element : this.tokens) {
/*  446 */       list.add(element);
/*      */     }
/*  448 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer reset() {
/*  459 */     this.tokenPos = 0;
/*  460 */     this.tokens = null;
/*  461 */     return this;
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
/*      */   public StrTokenizer reset(String input) {
/*  473 */     reset();
/*  474 */     if (input != null) {
/*  475 */       this.chars = input.toCharArray();
/*      */     } else {
/*  477 */       this.chars = null;
/*      */     } 
/*  479 */     return this;
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
/*      */   public StrTokenizer reset(char[] input) {
/*  491 */     reset();
/*  492 */     this.chars = ArrayUtils.clone(input);
/*  493 */     return this;
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
/*      */   public boolean hasNext() {
/*  505 */     checkTokenized();
/*  506 */     return (this.tokenPos < this.tokens.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String next() {
/*  517 */     if (hasNext()) {
/*  518 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  520 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextIndex() {
/*  530 */     return this.tokenPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrevious() {
/*  540 */     checkTokenized();
/*  541 */     return (this.tokenPos > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previous() {
/*  551 */     if (hasPrevious()) {
/*  552 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  554 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int previousIndex() {
/*  564 */     return this.tokenPos - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/*  574 */     throw new UnsupportedOperationException("remove() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void set(String obj) {
/*  584 */     throw new UnsupportedOperationException("set() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(String obj) {
/*  594 */     throw new UnsupportedOperationException("add() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkTokenized() {
/*  603 */     if (this.tokens == null) {
/*  604 */       if (this.chars == null) {
/*      */         
/*  606 */         List<String> split = tokenize(null, 0, 0);
/*  607 */         this.tokens = split.<String>toArray(new String[split.size()]);
/*      */       } else {
/*  609 */         List<String> split = tokenize(this.chars, 0, this.chars.length);
/*  610 */         this.tokens = split.<String>toArray(new String[split.size()]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> tokenize(char[] srcChars, int offset, int count) {
/*  636 */     if (srcChars == null || count == 0) {
/*  637 */       return Collections.emptyList();
/*      */     }
/*  639 */     StrBuilder buf = new StrBuilder();
/*  640 */     List<String> tokenList = new ArrayList<String>();
/*  641 */     int pos = offset;
/*      */ 
/*      */     
/*  644 */     while (pos >= 0 && pos < count) {
/*      */       
/*  646 */       pos = readNextToken(srcChars, pos, count, buf, tokenList);
/*      */ 
/*      */       
/*  649 */       if (pos >= count) {
/*  650 */         addToken(tokenList, "");
/*      */       }
/*      */     } 
/*  653 */     return tokenList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToken(List<String> list, String tok) {
/*  663 */     if (StringUtils.isEmpty(tok)) {
/*  664 */       if (isIgnoreEmptyTokens()) {
/*      */         return;
/*      */       }
/*  667 */       if (isEmptyTokenAsNull()) {
/*  668 */         tok = null;
/*      */       }
/*      */     } 
/*  671 */     list.add(tok);
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
/*      */   private int readNextToken(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList) {
/*  688 */     while (start < len) {
/*  689 */       int removeLen = Math.max(getIgnoredMatcher().isMatch(srcChars, start, start, len), getTrimmerMatcher().isMatch(srcChars, start, start, len));
/*      */ 
/*      */       
/*  692 */       if (removeLen == 0 || getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 || getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  697 */       start += removeLen;
/*      */     } 
/*      */ 
/*      */     
/*  701 */     if (start >= len) {
/*  702 */       addToken(tokenList, "");
/*  703 */       return -1;
/*      */     } 
/*      */ 
/*      */     
/*  707 */     int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
/*  708 */     if (delimLen > 0) {
/*  709 */       addToken(tokenList, "");
/*  710 */       return start + delimLen;
/*      */     } 
/*      */ 
/*      */     
/*  714 */     int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
/*  715 */     if (quoteLen > 0) {
/*  716 */       return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
/*      */     }
/*  718 */     return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
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
/*      */   private int readWithQuotes(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList, int quoteStart, int quoteLen) {
/*  739 */     workArea.clear();
/*  740 */     int pos = start;
/*  741 */     boolean quoting = (quoteLen > 0);
/*  742 */     int trimStart = 0;
/*      */     
/*  744 */     while (pos < len) {
/*      */ 
/*      */ 
/*      */       
/*  748 */       if (quoting) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  755 */         if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  756 */           if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
/*      */             
/*  758 */             workArea.append(srcChars, pos, quoteLen);
/*  759 */             pos += quoteLen * 2;
/*  760 */             trimStart = workArea.size();
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*  765 */           quoting = false;
/*  766 */           pos += quoteLen;
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  771 */         workArea.append(srcChars[pos++]);
/*  772 */         trimStart = workArea.size();
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  778 */       int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
/*  779 */       if (delimLen > 0) {
/*      */         
/*  781 */         addToken(tokenList, workArea.substring(0, trimStart));
/*  782 */         return pos + delimLen;
/*      */       } 
/*      */ 
/*      */       
/*  786 */       if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  787 */         quoting = true;
/*  788 */         pos += quoteLen;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  793 */       int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
/*  794 */       if (ignoredLen > 0) {
/*  795 */         pos += ignoredLen;
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/*  802 */       int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
/*  803 */       if (trimmedLen > 0) {
/*  804 */         workArea.append(srcChars, pos, trimmedLen);
/*  805 */         pos += trimmedLen;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  810 */       workArea.append(srcChars[pos++]);
/*  811 */       trimStart = workArea.size();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  816 */     addToken(tokenList, workArea.substring(0, trimStart));
/*  817 */     return -1;
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
/*      */   private boolean isQuote(char[] srcChars, int pos, int len, int quoteStart, int quoteLen) {
/*  832 */     for (int i = 0; i < quoteLen; i++) {
/*  833 */       if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
/*  834 */         return false;
/*      */       }
/*      */     } 
/*  837 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getDelimiterMatcher() {
/*  848 */     return this.delimMatcher;
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
/*      */   public StrTokenizer setDelimiterMatcher(StrMatcher delim) {
/*  860 */     if (delim == null) {
/*  861 */       this.delimMatcher = StrMatcher.noneMatcher();
/*      */     } else {
/*  863 */       this.delimMatcher = delim;
/*      */     } 
/*  865 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterChar(char delim) {
/*  875 */     return setDelimiterMatcher(StrMatcher.charMatcher(delim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterString(String delim) {
/*  885 */     return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
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
/*      */   public StrMatcher getQuoteMatcher() {
/*  900 */     return this.quoteMatcher;
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
/*      */   public StrTokenizer setQuoteMatcher(StrMatcher quote) {
/*  913 */     if (quote != null) {
/*  914 */       this.quoteMatcher = quote;
/*      */     }
/*  916 */     return this;
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
/*      */   public StrTokenizer setQuoteChar(char quote) {
/*  929 */     return setQuoteMatcher(StrMatcher.charMatcher(quote));
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
/*      */   public StrMatcher getIgnoredMatcher() {
/*  944 */     return this.ignoredMatcher;
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
/*      */   public StrTokenizer setIgnoredMatcher(StrMatcher ignored) {
/*  957 */     if (ignored != null) {
/*  958 */       this.ignoredMatcher = ignored;
/*      */     }
/*  960 */     return this;
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
/*      */   public StrTokenizer setIgnoredChar(char ignored) {
/*  973 */     return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
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
/*      */   public StrMatcher getTrimmerMatcher() {
/*  988 */     return this.trimmerMatcher;
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
/*      */   public StrTokenizer setTrimmerMatcher(StrMatcher trimmer) {
/* 1001 */     if (trimmer != null) {
/* 1002 */       this.trimmerMatcher = trimmer;
/*      */     }
/* 1004 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmptyTokenAsNull() {
/* 1015 */     return this.emptyAsNull;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setEmptyTokenAsNull(boolean emptyAsNull) {
/* 1026 */     this.emptyAsNull = emptyAsNull;
/* 1027 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIgnoreEmptyTokens() {
/* 1038 */     return this.ignoreEmptyTokens;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setIgnoreEmptyTokens(boolean ignoreEmptyTokens) {
/* 1049 */     this.ignoreEmptyTokens = ignoreEmptyTokens;
/* 1050 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContent() {
/* 1060 */     if (this.chars == null) {
/* 1061 */       return null;
/*      */     }
/* 1063 */     return new String(this.chars);
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
/*      */   public Object clone() {
/*      */     try {
/* 1077 */       return cloneReset();
/* 1078 */     } catch (CloneNotSupportedException ex) {
/* 1079 */       return null;
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
/*      */   Object cloneReset() throws CloneNotSupportedException {
/* 1092 */     StrTokenizer cloned = (StrTokenizer)super.clone();
/* 1093 */     if (cloned.chars != null) {
/* 1094 */       cloned.chars = (char[])cloned.chars.clone();
/*      */     }
/* 1096 */     cloned.reset();
/* 1097 */     return cloned;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1108 */     if (this.tokens == null) {
/* 1109 */       return "StrTokenizer[not tokenized yet]";
/*      */     }
/* 1111 */     return "StrTokenizer" + getTokenList();
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\StrTokenizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */