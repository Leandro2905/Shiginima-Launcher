/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ import org.apache.commons.lang3.SystemUtils;
/*      */ import org.apache.commons.lang3.builder.Builder;
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
/*      */ public class StrBuilder
/*      */   implements CharSequence, Appendable, Serializable, Builder<String>
/*      */ {
/*      */   static final int CAPACITY = 32;
/*      */   private static final long serialVersionUID = 7628716375283629643L;
/*      */   protected char[] buffer;
/*      */   protected int size;
/*      */   private String newLine;
/*      */   private String nullText;
/*      */   
/*      */   public StrBuilder() {
/*  105 */     this(32);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder(int initialCapacity) {
/*  115 */     if (initialCapacity <= 0) {
/*  116 */       initialCapacity = 32;
/*      */     }
/*  118 */     this.buffer = new char[initialCapacity];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder(String str) {
/*  129 */     if (str == null) {
/*  130 */       this.buffer = new char[32];
/*      */     } else {
/*  132 */       this.buffer = new char[str.length() + 32];
/*  133 */       append(str);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNewLineText() {
/*  144 */     return this.newLine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder setNewLineText(String newLine) {
/*  154 */     this.newLine = newLine;
/*  155 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNullText() {
/*  165 */     return this.nullText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder setNullText(String nullText) {
/*  175 */     if (nullText != null && nullText.isEmpty()) {
/*  176 */       nullText = null;
/*      */     }
/*  178 */     this.nullText = nullText;
/*  179 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  190 */     return this.size;
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
/*      */   public StrBuilder setLength(int length) {
/*  202 */     if (length < 0) {
/*  203 */       throw new StringIndexOutOfBoundsException(length);
/*      */     }
/*  205 */     if (length < this.size) {
/*  206 */       this.size = length;
/*  207 */     } else if (length > this.size) {
/*  208 */       ensureCapacity(length);
/*  209 */       int oldEnd = this.size;
/*  210 */       int newEnd = length;
/*  211 */       this.size = length;
/*  212 */       for (int i = oldEnd; i < newEnd; i++) {
/*  213 */         this.buffer[i] = Character.MIN_VALUE;
/*      */       }
/*      */     } 
/*  216 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  226 */     return this.buffer.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder ensureCapacity(int capacity) {
/*  236 */     if (capacity > this.buffer.length) {
/*  237 */       char[] old = this.buffer;
/*  238 */       this.buffer = new char[capacity * 2];
/*  239 */       System.arraycopy(old, 0, this.buffer, 0, this.size);
/*      */     } 
/*  241 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder minimizeCapacity() {
/*  250 */     if (this.buffer.length > length()) {
/*  251 */       char[] old = this.buffer;
/*  252 */       this.buffer = new char[length()];
/*  253 */       System.arraycopy(old, 0, this.buffer, 0, this.size);
/*      */     } 
/*  255 */     return this;
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
/*      */   public int size() {
/*  268 */     return this.size;
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
/*      */   public boolean isEmpty() {
/*  280 */     return (this.size == 0);
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
/*      */   public StrBuilder clear() {
/*  295 */     this.size = 0;
/*  296 */     return this;
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
/*      */   public char charAt(int index) {
/*  311 */     if (index < 0 || index >= length()) {
/*  312 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  314 */     return this.buffer[index];
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
/*      */   public StrBuilder setCharAt(int index, char ch) {
/*  328 */     if (index < 0 || index >= length()) {
/*  329 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  331 */     this.buffer[index] = ch;
/*  332 */     return this;
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
/*      */   public StrBuilder deleteCharAt(int index) {
/*  345 */     if (index < 0 || index >= this.size) {
/*  346 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  348 */     deleteImpl(index, index + 1, 1);
/*  349 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] toCharArray() {
/*  359 */     if (this.size == 0) {
/*  360 */       return ArrayUtils.EMPTY_CHAR_ARRAY;
/*      */     }
/*  362 */     char[] chars = new char[this.size];
/*  363 */     System.arraycopy(this.buffer, 0, chars, 0, this.size);
/*  364 */     return chars;
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
/*      */   public char[] toCharArray(int startIndex, int endIndex) {
/*  378 */     endIndex = validateRange(startIndex, endIndex);
/*  379 */     int len = endIndex - startIndex;
/*  380 */     if (len == 0) {
/*  381 */       return ArrayUtils.EMPTY_CHAR_ARRAY;
/*      */     }
/*  383 */     char[] chars = new char[len];
/*  384 */     System.arraycopy(this.buffer, startIndex, chars, 0, len);
/*  385 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] getChars(char[] destination) {
/*  395 */     int len = length();
/*  396 */     if (destination == null || destination.length < len) {
/*  397 */       destination = new char[len];
/*      */     }
/*  399 */     System.arraycopy(this.buffer, 0, destination, 0, len);
/*  400 */     return destination;
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
/*      */   public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
/*  414 */     if (startIndex < 0) {
/*  415 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/*  417 */     if (endIndex < 0 || endIndex > length()) {
/*  418 */       throw new StringIndexOutOfBoundsException(endIndex);
/*      */     }
/*  420 */     if (startIndex > endIndex) {
/*  421 */       throw new StringIndexOutOfBoundsException("end < start");
/*      */     }
/*  423 */     System.arraycopy(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
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
/*      */   public int readFrom(Readable readable) throws IOException {
/*  439 */     int oldSize = this.size;
/*  440 */     if (readable instanceof Reader) {
/*  441 */       Reader r = (Reader)readable;
/*  442 */       ensureCapacity(this.size + 1);
/*      */       int read;
/*  444 */       while ((read = r.read(this.buffer, this.size, this.buffer.length - this.size)) != -1) {
/*  445 */         this.size += read;
/*  446 */         ensureCapacity(this.size + 1);
/*      */       } 
/*  448 */     } else if (readable instanceof CharBuffer) {
/*  449 */       CharBuffer cb = (CharBuffer)readable;
/*  450 */       int remaining = cb.remaining();
/*  451 */       ensureCapacity(this.size + remaining);
/*  452 */       cb.get(this.buffer, this.size, remaining);
/*  453 */       this.size += remaining;
/*      */     } else {
/*      */       while (true) {
/*  456 */         ensureCapacity(this.size + 1);
/*  457 */         CharBuffer buf = CharBuffer.wrap(this.buffer, this.size, this.buffer.length - this.size);
/*  458 */         int read = readable.read(buf);
/*  459 */         if (read == -1) {
/*      */           break;
/*      */         }
/*  462 */         this.size += read;
/*      */       } 
/*      */     } 
/*  465 */     return this.size - oldSize;
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
/*      */   public StrBuilder appendNewLine() {
/*  479 */     if (this.newLine == null) {
/*  480 */       append(SystemUtils.LINE_SEPARATOR);
/*  481 */       return this;
/*      */     } 
/*  483 */     return append(this.newLine);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendNull() {
/*  492 */     if (this.nullText == null) {
/*  493 */       return this;
/*      */     }
/*  495 */     return append(this.nullText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(Object obj) {
/*  506 */     if (obj == null) {
/*  507 */       return appendNull();
/*      */     }
/*  509 */     if (obj instanceof CharSequence) {
/*  510 */       return append((CharSequence)obj);
/*      */     }
/*  512 */     return append(obj.toString());
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
/*      */   public StrBuilder append(CharSequence seq) {
/*  525 */     if (seq == null) {
/*  526 */       return appendNull();
/*      */     }
/*  528 */     if (seq instanceof StrBuilder) {
/*  529 */       return append((StrBuilder)seq);
/*      */     }
/*  531 */     if (seq instanceof StringBuilder) {
/*  532 */       return append((StringBuilder)seq);
/*      */     }
/*  534 */     if (seq instanceof StringBuffer) {
/*  535 */       return append((StringBuffer)seq);
/*      */     }
/*  537 */     if (seq instanceof CharBuffer) {
/*  538 */       return append((CharBuffer)seq);
/*      */     }
/*  540 */     return append(seq.toString());
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
/*      */   public StrBuilder append(CharSequence seq, int startIndex, int length) {
/*  555 */     if (seq == null) {
/*  556 */       return appendNull();
/*      */     }
/*  558 */     return append(seq.toString(), startIndex, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(String str) {
/*  569 */     if (str == null) {
/*  570 */       return appendNull();
/*      */     }
/*  572 */     int strLen = str.length();
/*  573 */     if (strLen > 0) {
/*  574 */       int len = length();
/*  575 */       ensureCapacity(len + strLen);
/*  576 */       str.getChars(0, strLen, this.buffer, len);
/*  577 */       this.size += strLen;
/*      */     } 
/*  579 */     return this;
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
/*      */   public StrBuilder append(String str, int startIndex, int length) {
/*  593 */     if (str == null) {
/*  594 */       return appendNull();
/*      */     }
/*  596 */     if (startIndex < 0 || startIndex > str.length()) {
/*  597 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  599 */     if (length < 0 || startIndex + length > str.length()) {
/*  600 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  602 */     if (length > 0) {
/*  603 */       int len = length();
/*  604 */       ensureCapacity(len + length);
/*  605 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  606 */       this.size += length;
/*      */     } 
/*  608 */     return this;
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
/*      */   public StrBuilder append(String format, Object... objs) {
/*  621 */     return append(String.format(format, objs));
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
/*      */   public StrBuilder append(CharBuffer buf) {
/*  633 */     if (buf == null) {
/*  634 */       return appendNull();
/*      */     }
/*  636 */     if (buf.hasArray()) {
/*  637 */       int length = buf.remaining();
/*  638 */       int len = length();
/*  639 */       ensureCapacity(len + length);
/*  640 */       System.arraycopy(buf.array(), buf.arrayOffset() + buf.position(), this.buffer, len, length);
/*  641 */       this.size += length;
/*      */     } else {
/*  643 */       append(buf.toString());
/*      */     } 
/*  645 */     return this;
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
/*      */   public StrBuilder append(CharBuffer buf, int startIndex, int length) {
/*  659 */     if (buf == null) {
/*  660 */       return appendNull();
/*      */     }
/*  662 */     if (buf.hasArray()) {
/*  663 */       int totalLength = buf.remaining();
/*  664 */       if (startIndex < 0 || startIndex > totalLength) {
/*  665 */         throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */       }
/*  667 */       if (length < 0 || startIndex + length > totalLength) {
/*  668 */         throw new StringIndexOutOfBoundsException("length must be valid");
/*      */       }
/*  670 */       int len = length();
/*  671 */       ensureCapacity(len + length);
/*  672 */       System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, this.buffer, len, length);
/*  673 */       this.size += length;
/*      */     } else {
/*  675 */       append(buf.toString(), startIndex, length);
/*      */     } 
/*  677 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(StringBuffer str) {
/*  688 */     if (str == null) {
/*  689 */       return appendNull();
/*      */     }
/*  691 */     int strLen = str.length();
/*  692 */     if (strLen > 0) {
/*  693 */       int len = length();
/*  694 */       ensureCapacity(len + strLen);
/*  695 */       str.getChars(0, strLen, this.buffer, len);
/*  696 */       this.size += strLen;
/*      */     } 
/*  698 */     return this;
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
/*      */   public StrBuilder append(StringBuffer str, int startIndex, int length) {
/*  711 */     if (str == null) {
/*  712 */       return appendNull();
/*      */     }
/*  714 */     if (startIndex < 0 || startIndex > str.length()) {
/*  715 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  717 */     if (length < 0 || startIndex + length > str.length()) {
/*  718 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  720 */     if (length > 0) {
/*  721 */       int len = length();
/*  722 */       ensureCapacity(len + length);
/*  723 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  724 */       this.size += length;
/*      */     } 
/*  726 */     return this;
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
/*      */   public StrBuilder append(StringBuilder str) {
/*  738 */     if (str == null) {
/*  739 */       return appendNull();
/*      */     }
/*  741 */     int strLen = str.length();
/*  742 */     if (strLen > 0) {
/*  743 */       int len = length();
/*  744 */       ensureCapacity(len + strLen);
/*  745 */       str.getChars(0, strLen, this.buffer, len);
/*  746 */       this.size += strLen;
/*      */     } 
/*  748 */     return this;
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
/*      */   public StrBuilder append(StringBuilder str, int startIndex, int length) {
/*  762 */     if (str == null) {
/*  763 */       return appendNull();
/*      */     }
/*  765 */     if (startIndex < 0 || startIndex > str.length()) {
/*  766 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  768 */     if (length < 0 || startIndex + length > str.length()) {
/*  769 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  771 */     if (length > 0) {
/*  772 */       int len = length();
/*  773 */       ensureCapacity(len + length);
/*  774 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  775 */       this.size += length;
/*      */     } 
/*  777 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(StrBuilder str) {
/*  788 */     if (str == null) {
/*  789 */       return appendNull();
/*      */     }
/*  791 */     int strLen = str.length();
/*  792 */     if (strLen > 0) {
/*  793 */       int len = length();
/*  794 */       ensureCapacity(len + strLen);
/*  795 */       System.arraycopy(str.buffer, 0, this.buffer, len, strLen);
/*  796 */       this.size += strLen;
/*      */     } 
/*  798 */     return this;
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
/*      */   public StrBuilder append(StrBuilder str, int startIndex, int length) {
/*  811 */     if (str == null) {
/*  812 */       return appendNull();
/*      */     }
/*  814 */     if (startIndex < 0 || startIndex > str.length()) {
/*  815 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  817 */     if (length < 0 || startIndex + length > str.length()) {
/*  818 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  820 */     if (length > 0) {
/*  821 */       int len = length();
/*  822 */       ensureCapacity(len + length);
/*  823 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  824 */       this.size += length;
/*      */     } 
/*  826 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(char[] chars) {
/*  837 */     if (chars == null) {
/*  838 */       return appendNull();
/*      */     }
/*  840 */     int strLen = chars.length;
/*  841 */     if (strLen > 0) {
/*  842 */       int len = length();
/*  843 */       ensureCapacity(len + strLen);
/*  844 */       System.arraycopy(chars, 0, this.buffer, len, strLen);
/*  845 */       this.size += strLen;
/*      */     } 
/*  847 */     return this;
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
/*      */   public StrBuilder append(char[] chars, int startIndex, int length) {
/*  860 */     if (chars == null) {
/*  861 */       return appendNull();
/*      */     }
/*  863 */     if (startIndex < 0 || startIndex > chars.length) {
/*  864 */       throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length);
/*      */     }
/*  866 */     if (length < 0 || startIndex + length > chars.length) {
/*  867 */       throw new StringIndexOutOfBoundsException("Invalid length: " + length);
/*      */     }
/*  869 */     if (length > 0) {
/*  870 */       int len = length();
/*  871 */       ensureCapacity(len + length);
/*  872 */       System.arraycopy(chars, startIndex, this.buffer, len, length);
/*  873 */       this.size += length;
/*      */     } 
/*  875 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(boolean value) {
/*  885 */     if (value) {
/*  886 */       ensureCapacity(this.size + 4);
/*  887 */       this.buffer[this.size++] = 't';
/*  888 */       this.buffer[this.size++] = 'r';
/*  889 */       this.buffer[this.size++] = 'u';
/*  890 */       this.buffer[this.size++] = 'e';
/*      */     } else {
/*  892 */       ensureCapacity(this.size + 5);
/*  893 */       this.buffer[this.size++] = 'f';
/*  894 */       this.buffer[this.size++] = 'a';
/*  895 */       this.buffer[this.size++] = 'l';
/*  896 */       this.buffer[this.size++] = 's';
/*  897 */       this.buffer[this.size++] = 'e';
/*      */     } 
/*  899 */     return this;
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
/*      */   public StrBuilder append(char ch) {
/*  911 */     int len = length();
/*  912 */     ensureCapacity(len + 1);
/*  913 */     this.buffer[this.size++] = ch;
/*  914 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(int value) {
/*  924 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(long value) {
/*  934 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(float value) {
/*  944 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(double value) {
/*  954 */     return append(String.valueOf(value));
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
/*      */   public StrBuilder appendln(Object obj) {
/*  967 */     return append(obj).appendNewLine();
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
/*      */   public StrBuilder appendln(String str) {
/*  979 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(String str, int startIndex, int length) {
/*  993 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(String format, Object... objs) {
/* 1006 */     return append(format, objs).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuffer str) {
/* 1018 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuilder str) {
/* 1030 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuilder str, int startIndex, int length) {
/* 1044 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuffer str, int startIndex, int length) {
/* 1058 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(StrBuilder str) {
/* 1070 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StrBuilder str, int startIndex, int length) {
/* 1084 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(char[] chars) {
/* 1096 */     return append(chars).appendNewLine();
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
/*      */   public StrBuilder appendln(char[] chars, int startIndex, int length) {
/* 1110 */     return append(chars, startIndex, length).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(boolean value) {
/* 1121 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(char ch) {
/* 1132 */     return append(ch).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(int value) {
/* 1143 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(long value) {
/* 1154 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(float value) {
/* 1165 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(double value) {
/* 1176 */     return append(value).appendNewLine();
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
/*      */   public <T> StrBuilder appendAll(T... array) {
/* 1191 */     if (array != null && array.length > 0) {
/* 1192 */       for (T element : array) {
/* 1193 */         append(element);
/*      */       }
/*      */     }
/* 1196 */     return this;
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
/*      */   public StrBuilder appendAll(Iterable<?> iterable) {
/* 1209 */     if (iterable != null) {
/* 1210 */       for (Object o : iterable) {
/* 1211 */         append(o);
/*      */       }
/*      */     }
/* 1214 */     return this;
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
/*      */   public StrBuilder appendAll(Iterator<?> it) {
/* 1227 */     if (it != null) {
/* 1228 */       while (it.hasNext()) {
/* 1229 */         append(it.next());
/*      */       }
/*      */     }
/* 1232 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Object[] array, String separator) {
/* 1247 */     if (array != null && array.length > 0) {
/*      */       
/* 1249 */       String sep = ObjectUtils.toString(separator);
/* 1250 */       append(array[0]);
/* 1251 */       for (int i = 1; i < array.length; i++) {
/* 1252 */         append(sep);
/* 1253 */         append(array[i]);
/*      */       } 
/*      */     } 
/* 1256 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Iterable<?> iterable, String separator) {
/* 1270 */     if (iterable != null) {
/*      */       
/* 1272 */       String sep = ObjectUtils.toString(separator);
/* 1273 */       Iterator<?> it = iterable.iterator();
/* 1274 */       while (it.hasNext()) {
/* 1275 */         append(it.next());
/* 1276 */         if (it.hasNext()) {
/* 1277 */           append(sep);
/*      */         }
/*      */       } 
/*      */     } 
/* 1281 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Iterator<?> it, String separator) {
/* 1295 */     if (it != null) {
/*      */       
/* 1297 */       String sep = ObjectUtils.toString(separator);
/* 1298 */       while (it.hasNext()) {
/* 1299 */         append(it.next());
/* 1300 */         if (it.hasNext()) {
/* 1301 */           append(sep);
/*      */         }
/*      */       } 
/*      */     } 
/* 1305 */     return this;
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
/*      */   
/*      */   public StrBuilder appendSeparator(String separator) {
/* 1330 */     return appendSeparator(separator, (String)null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendSeparator(String standard, String defaultIfEmpty) {
/* 1361 */     String str = isEmpty() ? defaultIfEmpty : standard;
/* 1362 */     if (str != null) {
/* 1363 */       append(str);
/*      */     }
/* 1365 */     return this;
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
/*      */   public StrBuilder appendSeparator(char separator) {
/* 1388 */     if (size() > 0) {
/* 1389 */       append(separator);
/*      */     }
/* 1391 */     return this;
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
/*      */   public StrBuilder appendSeparator(char standard, char defaultIfEmpty) {
/* 1406 */     if (size() > 0) {
/* 1407 */       append(standard);
/*      */     } else {
/* 1409 */       append(defaultIfEmpty);
/*      */     } 
/* 1411 */     return this;
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
/*      */   
/*      */   public StrBuilder appendSeparator(String separator, int loopIndex) {
/* 1436 */     if (separator != null && loopIndex > 0) {
/* 1437 */       append(separator);
/*      */     }
/* 1439 */     return this;
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
/*      */   
/*      */   public StrBuilder appendSeparator(char separator, int loopIndex) {
/* 1464 */     if (loopIndex > 0) {
/* 1465 */       append(separator);
/*      */     }
/* 1467 */     return this;
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
/*      */   public StrBuilder appendPadding(int length, char padChar) {
/* 1479 */     if (length >= 0) {
/* 1480 */       ensureCapacity(this.size + length);
/* 1481 */       for (int i = 0; i < length; i++) {
/* 1482 */         this.buffer[this.size++] = padChar;
/*      */       }
/*      */     } 
/* 1485 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
/* 1501 */     if (width > 0) {
/* 1502 */       ensureCapacity(this.size + width);
/* 1503 */       String str = (obj == null) ? getNullText() : obj.toString();
/* 1504 */       if (str == null) {
/* 1505 */         str = "";
/*      */       }
/* 1507 */       int strLen = str.length();
/* 1508 */       if (strLen >= width) {
/* 1509 */         str.getChars(strLen - width, strLen, this.buffer, this.size);
/*      */       } else {
/* 1511 */         int padLen = width - strLen;
/* 1512 */         for (int i = 0; i < padLen; i++) {
/* 1513 */           this.buffer[this.size + i] = padChar;
/*      */         }
/* 1515 */         str.getChars(0, strLen, this.buffer, this.size + padLen);
/*      */       } 
/* 1517 */       this.size += width;
/*      */     } 
/* 1519 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
/* 1533 */     return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
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
/*      */   public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
/* 1548 */     if (width > 0) {
/* 1549 */       ensureCapacity(this.size + width);
/* 1550 */       String str = (obj == null) ? getNullText() : obj.toString();
/* 1551 */       if (str == null) {
/* 1552 */         str = "";
/*      */       }
/* 1554 */       int strLen = str.length();
/* 1555 */       if (strLen >= width) {
/* 1556 */         str.getChars(0, width, this.buffer, this.size);
/*      */       } else {
/* 1558 */         int padLen = width - strLen;
/* 1559 */         str.getChars(0, strLen, this.buffer, this.size);
/* 1560 */         for (int i = 0; i < padLen; i++) {
/* 1561 */           this.buffer[this.size + strLen + i] = padChar;
/*      */         }
/*      */       } 
/* 1564 */       this.size += width;
/*      */     } 
/* 1566 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
/* 1580 */     return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
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
/*      */   public StrBuilder insert(int index, Object obj) {
/* 1594 */     if (obj == null) {
/* 1595 */       return insert(index, this.nullText);
/*      */     }
/* 1597 */     return insert(index, obj.toString());
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
/*      */   public StrBuilder insert(int index, String str) {
/* 1610 */     validateIndex(index);
/* 1611 */     if (str == null) {
/* 1612 */       str = this.nullText;
/*      */     }
/* 1614 */     if (str != null) {
/* 1615 */       int strLen = str.length();
/* 1616 */       if (strLen > 0) {
/* 1617 */         int newSize = this.size + strLen;
/* 1618 */         ensureCapacity(newSize);
/* 1619 */         System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
/* 1620 */         this.size = newSize;
/* 1621 */         str.getChars(0, strLen, this.buffer, index);
/*      */       } 
/*      */     } 
/* 1624 */     return this;
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
/*      */   public StrBuilder insert(int index, char[] chars) {
/* 1637 */     validateIndex(index);
/* 1638 */     if (chars == null) {
/* 1639 */       return insert(index, this.nullText);
/*      */     }
/* 1641 */     int len = chars.length;
/* 1642 */     if (len > 0) {
/* 1643 */       ensureCapacity(this.size + len);
/* 1644 */       System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
/* 1645 */       System.arraycopy(chars, 0, this.buffer, index, len);
/* 1646 */       this.size += len;
/*      */     } 
/* 1648 */     return this;
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
/*      */   public StrBuilder insert(int index, char[] chars, int offset, int length) {
/* 1663 */     validateIndex(index);
/* 1664 */     if (chars == null) {
/* 1665 */       return insert(index, this.nullText);
/*      */     }
/* 1667 */     if (offset < 0 || offset > chars.length) {
/* 1668 */       throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
/*      */     }
/* 1670 */     if (length < 0 || offset + length > chars.length) {
/* 1671 */       throw new StringIndexOutOfBoundsException("Invalid length: " + length);
/*      */     }
/* 1673 */     if (length > 0) {
/* 1674 */       ensureCapacity(this.size + length);
/* 1675 */       System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
/* 1676 */       System.arraycopy(chars, offset, this.buffer, index, length);
/* 1677 */       this.size += length;
/*      */     } 
/* 1679 */     return this;
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
/*      */   public StrBuilder insert(int index, boolean value) {
/* 1691 */     validateIndex(index);
/* 1692 */     if (value) {
/* 1693 */       ensureCapacity(this.size + 4);
/* 1694 */       System.arraycopy(this.buffer, index, this.buffer, index + 4, this.size - index);
/* 1695 */       this.buffer[index++] = 't';
/* 1696 */       this.buffer[index++] = 'r';
/* 1697 */       this.buffer[index++] = 'u';
/* 1698 */       this.buffer[index] = 'e';
/* 1699 */       this.size += 4;
/*      */     } else {
/* 1701 */       ensureCapacity(this.size + 5);
/* 1702 */       System.arraycopy(this.buffer, index, this.buffer, index + 5, this.size - index);
/* 1703 */       this.buffer[index++] = 'f';
/* 1704 */       this.buffer[index++] = 'a';
/* 1705 */       this.buffer[index++] = 'l';
/* 1706 */       this.buffer[index++] = 's';
/* 1707 */       this.buffer[index] = 'e';
/* 1708 */       this.size += 5;
/*      */     } 
/* 1710 */     return this;
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
/*      */   public StrBuilder insert(int index, char value) {
/* 1722 */     validateIndex(index);
/* 1723 */     ensureCapacity(this.size + 1);
/* 1724 */     System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
/* 1725 */     this.buffer[index] = value;
/* 1726 */     this.size++;
/* 1727 */     return this;
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
/*      */   public StrBuilder insert(int index, int value) {
/* 1739 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, long value) {
/* 1751 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, float value) {
/* 1763 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, double value) {
/* 1775 */     return insert(index, String.valueOf(value));
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
/*      */   private void deleteImpl(int startIndex, int endIndex, int len) {
/* 1788 */     System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
/* 1789 */     this.size -= len;
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
/*      */   public StrBuilder delete(int startIndex, int endIndex) {
/* 1802 */     endIndex = validateRange(startIndex, endIndex);
/* 1803 */     int len = endIndex - startIndex;
/* 1804 */     if (len > 0) {
/* 1805 */       deleteImpl(startIndex, endIndex, len);
/*      */     }
/* 1807 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteAll(char ch) {
/* 1818 */     for (int i = 0; i < this.size; i++) {
/* 1819 */       if (this.buffer[i] == ch) {
/* 1820 */         int start = i; do {  }
/* 1821 */         while (++i < this.size && 
/* 1822 */           this.buffer[i] == ch);
/*      */ 
/*      */ 
/*      */         
/* 1826 */         int len = i - start;
/* 1827 */         deleteImpl(start, i, len);
/* 1828 */         i -= len;
/*      */       } 
/*      */     } 
/* 1831 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteFirst(char ch) {
/* 1841 */     for (int i = 0; i < this.size; i++) {
/* 1842 */       if (this.buffer[i] == ch) {
/* 1843 */         deleteImpl(i, i + 1, 1);
/*      */         break;
/*      */       } 
/*      */     } 
/* 1847 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteAll(String str) {
/* 1858 */     int len = (str == null) ? 0 : str.length();
/* 1859 */     if (len > 0) {
/* 1860 */       int index = indexOf(str, 0);
/* 1861 */       while (index >= 0) {
/* 1862 */         deleteImpl(index, index + len, len);
/* 1863 */         index = indexOf(str, index);
/*      */       } 
/*      */     } 
/* 1866 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteFirst(String str) {
/* 1876 */     int len = (str == null) ? 0 : str.length();
/* 1877 */     if (len > 0) {
/* 1878 */       int index = indexOf(str, 0);
/* 1879 */       if (index >= 0) {
/* 1880 */         deleteImpl(index, index + len, len);
/*      */       }
/*      */     } 
/* 1883 */     return this;
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
/*      */   public StrBuilder deleteAll(StrMatcher matcher) {
/* 1898 */     return replace(matcher, null, 0, this.size, -1);
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
/*      */   public StrBuilder deleteFirst(StrMatcher matcher) {
/* 1912 */     return replace(matcher, null, 0, this.size, 1);
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
/*      */   private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
/* 1927 */     int newSize = this.size - removeLen + insertLen;
/* 1928 */     if (insertLen != removeLen) {
/* 1929 */       ensureCapacity(newSize);
/* 1930 */       System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
/* 1931 */       this.size = newSize;
/*      */     } 
/* 1933 */     if (insertLen > 0) {
/* 1934 */       insertStr.getChars(0, insertLen, this.buffer, startIndex);
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
/*      */   public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
/* 1950 */     endIndex = validateRange(startIndex, endIndex);
/* 1951 */     int insertLen = (replaceStr == null) ? 0 : replaceStr.length();
/* 1952 */     replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
/* 1953 */     return this;
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
/*      */   public StrBuilder replaceAll(char search, char replace) {
/* 1966 */     if (search != replace) {
/* 1967 */       for (int i = 0; i < this.size; i++) {
/* 1968 */         if (this.buffer[i] == search) {
/* 1969 */           this.buffer[i] = replace;
/*      */         }
/*      */       } 
/*      */     }
/* 1973 */     return this;
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
/*      */   public StrBuilder replaceFirst(char search, char replace) {
/* 1985 */     if (search != replace) {
/* 1986 */       for (int i = 0; i < this.size; i++) {
/* 1987 */         if (this.buffer[i] == search) {
/* 1988 */           this.buffer[i] = replace;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1993 */     return this;
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
/*      */   public StrBuilder replaceAll(String searchStr, String replaceStr) {
/* 2005 */     int searchLen = (searchStr == null) ? 0 : searchStr.length();
/* 2006 */     if (searchLen > 0) {
/* 2007 */       int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
/* 2008 */       int index = indexOf(searchStr, 0);
/* 2009 */       while (index >= 0) {
/* 2010 */         replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
/* 2011 */         index = indexOf(searchStr, index + replaceLen);
/*      */       } 
/*      */     } 
/* 2014 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder replaceFirst(String searchStr, String replaceStr) {
/* 2025 */     int searchLen = (searchStr == null) ? 0 : searchStr.length();
/* 2026 */     if (searchLen > 0) {
/* 2027 */       int index = indexOf(searchStr, 0);
/* 2028 */       if (index >= 0) {
/* 2029 */         int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
/* 2030 */         replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
/*      */       } 
/*      */     } 
/* 2033 */     return this;
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
/*      */   public StrBuilder replaceAll(StrMatcher matcher, String replaceStr) {
/* 2049 */     return replace(matcher, replaceStr, 0, this.size, -1);
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
/*      */   public StrBuilder replaceFirst(StrMatcher matcher, String replaceStr) {
/* 2064 */     return replace(matcher, replaceStr, 0, this.size, 1);
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
/*      */   public StrBuilder replace(StrMatcher matcher, String replaceStr, int startIndex, int endIndex, int replaceCount) {
/* 2087 */     endIndex = validateRange(startIndex, endIndex);
/* 2088 */     return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
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
/*      */   private StrBuilder replaceImpl(StrMatcher matcher, String replaceStr, int from, int to, int replaceCount) {
/* 2109 */     if (matcher == null || this.size == 0) {
/* 2110 */       return this;
/*      */     }
/* 2112 */     int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
/* 2113 */     char[] buf = this.buffer;
/* 2114 */     for (int i = from; i < to && replaceCount != 0; i++) {
/* 2115 */       int removeLen = matcher.isMatch(buf, i, from, to);
/* 2116 */       if (removeLen > 0) {
/* 2117 */         replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
/* 2118 */         to = to - removeLen + replaceLen;
/* 2119 */         i = i + replaceLen - 1;
/* 2120 */         if (replaceCount > 0) {
/* 2121 */           replaceCount--;
/*      */         }
/*      */       } 
/*      */     } 
/* 2125 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder reverse() {
/* 2135 */     if (this.size == 0) {
/* 2136 */       return this;
/*      */     }
/*      */     
/* 2139 */     int half = this.size / 2;
/* 2140 */     char[] buf = this.buffer;
/* 2141 */     for (int leftIdx = 0, rightIdx = this.size - 1; leftIdx < half; leftIdx++, rightIdx--) {
/* 2142 */       char swap = buf[leftIdx];
/* 2143 */       buf[leftIdx] = buf[rightIdx];
/* 2144 */       buf[rightIdx] = swap;
/*      */     } 
/* 2146 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder trim() {
/* 2157 */     if (this.size == 0) {
/* 2158 */       return this;
/*      */     }
/* 2160 */     int len = this.size;
/* 2161 */     char[] buf = this.buffer;
/* 2162 */     int pos = 0;
/* 2163 */     while (pos < len && buf[pos] <= ' ') {
/* 2164 */       pos++;
/*      */     }
/* 2166 */     while (pos < len && buf[len - 1] <= ' ') {
/* 2167 */       len--;
/*      */     }
/* 2169 */     if (len < this.size) {
/* 2170 */       delete(len, this.size);
/*      */     }
/* 2172 */     if (pos > 0) {
/* 2173 */       delete(0, pos);
/*      */     }
/* 2175 */     return this;
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
/*      */   public boolean startsWith(String str) {
/* 2188 */     if (str == null) {
/* 2189 */       return false;
/*      */     }
/* 2191 */     int len = str.length();
/* 2192 */     if (len == 0) {
/* 2193 */       return true;
/*      */     }
/* 2195 */     if (len > this.size) {
/* 2196 */       return false;
/*      */     }
/* 2198 */     for (int i = 0; i < len; i++) {
/* 2199 */       if (this.buffer[i] != str.charAt(i)) {
/* 2200 */         return false;
/*      */       }
/*      */     } 
/* 2203 */     return true;
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
/*      */   public boolean endsWith(String str) {
/* 2215 */     if (str == null) {
/* 2216 */       return false;
/*      */     }
/* 2218 */     int len = str.length();
/* 2219 */     if (len == 0) {
/* 2220 */       return true;
/*      */     }
/* 2222 */     if (len > this.size) {
/* 2223 */       return false;
/*      */     }
/* 2225 */     int pos = this.size - len;
/* 2226 */     for (int i = 0; i < len; i++, pos++) {
/* 2227 */       if (this.buffer[pos] != str.charAt(i)) {
/* 2228 */         return false;
/*      */       }
/*      */     } 
/* 2231 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSequence subSequence(int startIndex, int endIndex) {
/* 2241 */     if (startIndex < 0) {
/* 2242 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/* 2244 */     if (endIndex > this.size) {
/* 2245 */       throw new StringIndexOutOfBoundsException(endIndex);
/*      */     }
/* 2247 */     if (startIndex > endIndex) {
/* 2248 */       throw new StringIndexOutOfBoundsException(endIndex - startIndex);
/*      */     }
/* 2250 */     return substring(startIndex, endIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String substring(int start) {
/* 2261 */     return substring(start, this.size);
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
/*      */   public String substring(int startIndex, int endIndex) {
/* 2278 */     endIndex = validateRange(startIndex, endIndex);
/* 2279 */     return new String(this.buffer, startIndex, endIndex - startIndex);
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
/*      */   public String leftString(int length) {
/* 2295 */     if (length <= 0)
/* 2296 */       return ""; 
/* 2297 */     if (length >= this.size) {
/* 2298 */       return new String(this.buffer, 0, this.size);
/*      */     }
/* 2300 */     return new String(this.buffer, 0, length);
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
/*      */   public String rightString(int length) {
/* 2317 */     if (length <= 0)
/* 2318 */       return ""; 
/* 2319 */     if (length >= this.size) {
/* 2320 */       return new String(this.buffer, 0, this.size);
/*      */     }
/* 2322 */     return new String(this.buffer, this.size - length, length);
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
/*      */   public String midString(int index, int length) {
/* 2343 */     if (index < 0) {
/* 2344 */       index = 0;
/*      */     }
/* 2346 */     if (length <= 0 || index >= this.size) {
/* 2347 */       return "";
/*      */     }
/* 2349 */     if (this.size <= index + length) {
/* 2350 */       return new String(this.buffer, index, this.size - index);
/*      */     }
/* 2352 */     return new String(this.buffer, index, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(char ch) {
/* 2363 */     char[] thisBuf = this.buffer;
/* 2364 */     for (int i = 0; i < this.size; i++) {
/* 2365 */       if (thisBuf[i] == ch) {
/* 2366 */         return true;
/*      */       }
/*      */     } 
/* 2369 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(String str) {
/* 2379 */     return (indexOf(str, 0) >= 0);
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
/*      */   public boolean contains(StrMatcher matcher) {
/* 2394 */     return (indexOf(matcher, 0) >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char ch) {
/* 2405 */     return indexOf(ch, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char ch, int startIndex) {
/* 2416 */     startIndex = (startIndex < 0) ? 0 : startIndex;
/* 2417 */     if (startIndex >= this.size) {
/* 2418 */       return -1;
/*      */     }
/* 2420 */     char[] thisBuf = this.buffer;
/* 2421 */     for (int i = startIndex; i < this.size; i++) {
/* 2422 */       if (thisBuf[i] == ch) {
/* 2423 */         return i;
/*      */       }
/*      */     } 
/* 2426 */     return -1;
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
/*      */   public int indexOf(String str) {
/* 2438 */     return indexOf(str, 0);
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
/*      */   public int indexOf(String str, int startIndex) {
/* 2452 */     startIndex = (startIndex < 0) ? 0 : startIndex;
/* 2453 */     if (str == null || startIndex >= this.size) {
/* 2454 */       return -1;
/*      */     }
/* 2456 */     int strLen = str.length();
/* 2457 */     if (strLen == 1) {
/* 2458 */       return indexOf(str.charAt(0), startIndex);
/*      */     }
/* 2460 */     if (strLen == 0) {
/* 2461 */       return startIndex;
/*      */     }
/* 2463 */     if (strLen > this.size) {
/* 2464 */       return -1;
/*      */     }
/* 2466 */     char[] thisBuf = this.buffer;
/* 2467 */     int len = this.size - strLen + 1;
/*      */     
/* 2469 */     for (int i = startIndex; i < len; i++) {
/* 2470 */       int j = 0; while (true) { if (j < strLen) {
/* 2471 */           if (str.charAt(j) != thisBuf[i + j])
/*      */             break;  j++;
/*      */           continue;
/*      */         } 
/* 2475 */         return i; }
/*      */     
/* 2477 */     }  return -1;
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
/*      */   public int indexOf(StrMatcher matcher) {
/* 2491 */     return indexOf(matcher, 0);
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
/*      */   public int indexOf(StrMatcher matcher, int startIndex) {
/* 2507 */     startIndex = (startIndex < 0) ? 0 : startIndex;
/* 2508 */     if (matcher == null || startIndex >= this.size) {
/* 2509 */       return -1;
/*      */     }
/* 2511 */     int len = this.size;
/* 2512 */     char[] buf = this.buffer;
/* 2513 */     for (int i = startIndex; i < len; i++) {
/* 2514 */       if (matcher.isMatch(buf, i, startIndex, len) > 0) {
/* 2515 */         return i;
/*      */       }
/*      */     } 
/* 2518 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(char ch) {
/* 2529 */     return lastIndexOf(ch, this.size - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(char ch, int startIndex) {
/* 2540 */     startIndex = (startIndex >= this.size) ? (this.size - 1) : startIndex;
/* 2541 */     if (startIndex < 0) {
/* 2542 */       return -1;
/*      */     }
/* 2544 */     for (int i = startIndex; i >= 0; i--) {
/* 2545 */       if (this.buffer[i] == ch) {
/* 2546 */         return i;
/*      */       }
/*      */     } 
/* 2549 */     return -1;
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
/*      */   public int lastIndexOf(String str) {
/* 2561 */     return lastIndexOf(str, this.size - 1);
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
/*      */   public int lastIndexOf(String str, int startIndex) {
/* 2575 */     startIndex = (startIndex >= this.size) ? (this.size - 1) : startIndex;
/* 2576 */     if (str == null || startIndex < 0) {
/* 2577 */       return -1;
/*      */     }
/* 2579 */     int strLen = str.length();
/* 2580 */     if (strLen > 0 && strLen <= this.size) {
/* 2581 */       if (strLen == 1) {
/* 2582 */         return lastIndexOf(str.charAt(0), startIndex);
/*      */       }
/*      */ 
/*      */       
/* 2586 */       for (int i = startIndex - strLen + 1; i >= 0; i--) {
/* 2587 */         int j = 0; while (true) { if (j < strLen) {
/* 2588 */             if (str.charAt(j) != this.buffer[i + j])
/*      */               break;  j++;
/*      */             continue;
/*      */           } 
/* 2592 */           return i; }
/*      */       
/*      */       } 
/* 2595 */     } else if (strLen == 0) {
/* 2596 */       return startIndex;
/*      */     } 
/* 2598 */     return -1;
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
/*      */   public int lastIndexOf(StrMatcher matcher) {
/* 2612 */     return lastIndexOf(matcher, this.size);
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
/*      */   public int lastIndexOf(StrMatcher matcher, int startIndex) {
/* 2628 */     startIndex = (startIndex >= this.size) ? (this.size - 1) : startIndex;
/* 2629 */     if (matcher == null || startIndex < 0) {
/* 2630 */       return -1;
/*      */     }
/* 2632 */     char[] buf = this.buffer;
/* 2633 */     int endIndex = startIndex + 1;
/* 2634 */     for (int i = startIndex; i >= 0; i--) {
/* 2635 */       if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
/* 2636 */         return i;
/*      */       }
/*      */     } 
/* 2639 */     return -1;
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
/*      */   public StrTokenizer asTokenizer() {
/* 2676 */     return new StrBuilderTokenizer();
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
/*      */   public Reader asReader() {
/* 2700 */     return new StrBuilderReader();
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
/*      */   
/*      */   public Writer asWriter() {
/* 2725 */     return new StrBuilderWriter();
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
/*      */   public void appendTo(Appendable appendable) throws IOException {
/* 2741 */     if (appendable instanceof Writer) {
/* 2742 */       ((Writer)appendable).write(this.buffer, 0, this.size);
/* 2743 */     } else if (appendable instanceof StringBuilder) {
/* 2744 */       ((StringBuilder)appendable).append(this.buffer, 0, this.size);
/* 2745 */     } else if (appendable instanceof StringBuffer) {
/* 2746 */       ((StringBuffer)appendable).append(this.buffer, 0, this.size);
/* 2747 */     } else if (appendable instanceof CharBuffer) {
/* 2748 */       ((CharBuffer)appendable).put(this.buffer, 0, this.size);
/*      */     } else {
/* 2750 */       appendable.append(this);
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
/*      */   public boolean equalsIgnoreCase(StrBuilder other) {
/* 2793 */     if (this == other) {
/* 2794 */       return true;
/*      */     }
/* 2796 */     if (this.size != other.size) {
/* 2797 */       return false;
/*      */     }
/* 2799 */     char[] thisBuf = this.buffer;
/* 2800 */     char[] otherBuf = other.buffer;
/* 2801 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2802 */       char c1 = thisBuf[i];
/* 2803 */       char c2 = otherBuf[i];
/* 2804 */       if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
/* 2805 */         return false;
/*      */       }
/*      */     } 
/* 2808 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(StrBuilder other) {
/* 2819 */     if (this == other) {
/* 2820 */       return true;
/*      */     }
/* 2822 */     if (this.size != other.size) {
/* 2823 */       return false;
/*      */     }
/* 2825 */     char[] thisBuf = this.buffer;
/* 2826 */     char[] otherBuf = other.buffer;
/* 2827 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2828 */       if (thisBuf[i] != otherBuf[i]) {
/* 2829 */         return false;
/*      */       }
/*      */     } 
/* 2832 */     return true;
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
/*      */   public boolean equals(Object obj) {
/* 2844 */     if (obj instanceof StrBuilder) {
/* 2845 */       return equals((StrBuilder)obj);
/*      */     }
/* 2847 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 2857 */     char[] buf = this.buffer;
/* 2858 */     int hash = 0;
/* 2859 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2860 */       hash = 31 * hash + buf[i];
/*      */     }
/* 2862 */     return hash;
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
/*      */   public String toString() {
/* 2877 */     return new String(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer toStringBuffer() {
/* 2887 */     return (new StringBuffer(this.size)).append(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder toStringBuilder() {
/* 2898 */     return (new StringBuilder(this.size)).append(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String build() {
/* 2909 */     return toString();
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
/*      */   protected int validateRange(int startIndex, int endIndex) {
/* 2923 */     if (startIndex < 0) {
/* 2924 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/* 2926 */     if (endIndex > this.size) {
/* 2927 */       endIndex = this.size;
/*      */     }
/* 2929 */     if (startIndex > endIndex) {
/* 2930 */       throw new StringIndexOutOfBoundsException("end < start");
/*      */     }
/* 2932 */     return endIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void validateIndex(int index) {
/* 2942 */     if (index < 0 || index > this.size) {
/* 2943 */       throw new StringIndexOutOfBoundsException(index);
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
/*      */   class StrBuilderTokenizer
/*      */     extends StrTokenizer
/*      */   {
/*      */     protected List<String> tokenize(char[] chars, int offset, int count) {
/* 2963 */       if (chars == null) {
/* 2964 */         return super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size());
/*      */       }
/* 2966 */       return super.tokenize(chars, offset, count);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getContent() {
/* 2972 */       String str = super.getContent();
/* 2973 */       if (str == null) {
/* 2974 */         return StrBuilder.this.toString();
/*      */       }
/* 2976 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class StrBuilderReader
/*      */     extends Reader
/*      */   {
/*      */     private int pos;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int mark;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() {
/* 3006 */       if (!ready()) {
/* 3007 */         return -1;
/*      */       }
/* 3009 */       return StrBuilder.this.charAt(this.pos++);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int read(char[] b, int off, int len) {
/* 3015 */       if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0)
/*      */       {
/* 3017 */         throw new IndexOutOfBoundsException();
/*      */       }
/* 3019 */       if (len == 0) {
/* 3020 */         return 0;
/*      */       }
/* 3022 */       if (this.pos >= StrBuilder.this.size()) {
/* 3023 */         return -1;
/*      */       }
/* 3025 */       if (this.pos + len > StrBuilder.this.size()) {
/* 3026 */         len = StrBuilder.this.size() - this.pos;
/*      */       }
/* 3028 */       StrBuilder.this.getChars(this.pos, this.pos + len, b, off);
/* 3029 */       this.pos += len;
/* 3030 */       return len;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long skip(long n) {
/* 3036 */       if (this.pos + n > StrBuilder.this.size()) {
/* 3037 */         n = (StrBuilder.this.size() - this.pos);
/*      */       }
/* 3039 */       if (n < 0L) {
/* 3040 */         return 0L;
/*      */       }
/* 3042 */       this.pos = (int)(this.pos + n);
/* 3043 */       return n;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean ready() {
/* 3049 */       return (this.pos < StrBuilder.this.size());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean markSupported() {
/* 3055 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mark(int readAheadLimit) {
/* 3061 */       this.mark = this.pos;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void reset() {
/* 3067 */       this.pos = this.mark;
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
/*      */   class StrBuilderWriter
/*      */     extends Writer
/*      */   {
/*      */     public void close() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void flush() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(int c) {
/* 3099 */       StrBuilder.this.append((char)c);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(char[] cbuf) {
/* 3105 */       StrBuilder.this.append(cbuf);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(char[] cbuf, int off, int len) {
/* 3111 */       StrBuilder.this.append(cbuf, off, len);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(String str) {
/* 3117 */       StrBuilder.this.append(str);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(String str, int off, int len) {
/* 3123 */       StrBuilder.this.append(str, off, len);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\StrBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */