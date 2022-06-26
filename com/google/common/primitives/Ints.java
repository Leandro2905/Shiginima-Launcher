/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Ints
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   public static final int MAX_POWER_OF_TWO = 1073741824;
/*     */   
/*     */   public static int hashCode(int value) {
/*  76 */     return value;
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
/*     */   public static int checkedCast(long value) {
/*  88 */     int result = (int)value;
/*  89 */     if (result != value) {
/*     */       
/*  91 */       long l = value; throw new IllegalArgumentException((new StringBuilder(34)).append("Out of range: ").append(l).toString());
/*     */     } 
/*  93 */     return result;
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
/*     */   public static int saturatedCast(long value) {
/* 105 */     if (value > 2147483647L) {
/* 106 */       return Integer.MAX_VALUE;
/*     */     }
/* 108 */     if (value < -2147483648L) {
/* 109 */       return Integer.MIN_VALUE;
/*     */     }
/* 111 */     return (int)value;
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
/*     */   public static int compare(int a, int b) {
/* 127 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
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
/*     */   public static boolean contains(int[] array, int target) {
/* 140 */     for (int value : array) {
/* 141 */       if (value == target) {
/* 142 */         return true;
/*     */       }
/*     */     } 
/* 145 */     return false;
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
/*     */   public static int indexOf(int[] array, int target) {
/* 158 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(int[] array, int target, int start, int end) {
/* 164 */     for (int i = start; i < end; i++) {
/* 165 */       if (array[i] == target) {
/* 166 */         return i;
/*     */       }
/*     */     } 
/* 169 */     return -1;
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
/*     */   public static int indexOf(int[] array, int[] target) {
/* 184 */     Preconditions.checkNotNull(array, "array");
/* 185 */     Preconditions.checkNotNull(target, "target");
/* 186 */     if (target.length == 0) {
/* 187 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 191 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 192 */       int j = 0; while (true) { if (j < target.length) {
/* 193 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 197 */         return i; }
/*     */     
/* 199 */     }  return -1;
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
/*     */   public static int lastIndexOf(int[] array, int target) {
/* 212 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(int[] array, int target, int start, int end) {
/* 218 */     for (int i = end - 1; i >= start; i--) {
/* 219 */       if (array[i] == target) {
/* 220 */         return i;
/*     */       }
/*     */     } 
/* 223 */     return -1;
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
/*     */   public static int min(int... array) {
/* 235 */     Preconditions.checkArgument((array.length > 0));
/* 236 */     int min = array[0];
/* 237 */     for (int i = 1; i < array.length; i++) {
/* 238 */       if (array[i] < min) {
/* 239 */         min = array[i];
/*     */       }
/*     */     } 
/* 242 */     return min;
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
/*     */   public static int max(int... array) {
/* 254 */     Preconditions.checkArgument((array.length > 0));
/* 255 */     int max = array[0];
/* 256 */     for (int i = 1; i < array.length; i++) {
/* 257 */       if (array[i] > max) {
/* 258 */         max = array[i];
/*     */       }
/*     */     } 
/* 261 */     return max;
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
/*     */   public static int[] concat(int[]... arrays) {
/* 274 */     int length = 0;
/* 275 */     for (int[] array : arrays) {
/* 276 */       length += array.length;
/*     */     }
/* 278 */     int[] result = new int[length];
/* 279 */     int pos = 0;
/* 280 */     for (int[] array : arrays) {
/* 281 */       System.arraycopy(array, 0, result, pos, array.length);
/* 282 */       pos += array.length;
/*     */     } 
/* 284 */     return result;
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
/*     */   @GwtIncompatible("doesn't work")
/*     */   public static byte[] toByteArray(int value) {
/* 300 */     return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
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
/*     */   @GwtIncompatible("doesn't work")
/*     */   public static int fromByteArray(byte[] bytes) {
/* 321 */     Preconditions.checkArgument((bytes.length >= 4), "array too small: %s < %s", new Object[] { Integer.valueOf(bytes.length), Integer.valueOf(4) });
/*     */     
/* 323 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("doesn't work")
/*     */   public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
/* 335 */     return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
/*     */   }
/*     */   
/*     */   private static final class IntConverter
/*     */     extends Converter<String, Integer> implements Serializable {
/* 340 */     static final IntConverter INSTANCE = new IntConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Integer doForward(String value) {
/* 344 */       return Integer.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Integer value) {
/* 349 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 354 */       return "Ints.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 358 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Integer> stringConverter() {
/* 371 */     return IntConverter.INSTANCE;
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
/*     */   public static int[] ensureCapacity(int[] array, int minLength, int padding) {
/* 392 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 393 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 394 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] copyOf(int[] original, int length) {
/* 401 */     int[] copy = new int[length];
/* 402 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 403 */     return copy;
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
/*     */   public static String join(String separator, int... array) {
/* 416 */     Preconditions.checkNotNull(separator);
/* 417 */     if (array.length == 0) {
/* 418 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 422 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 423 */     builder.append(array[0]);
/* 424 */     for (int i = 1; i < array.length; i++) {
/* 425 */       builder.append(separator).append(array[i]);
/*     */     }
/* 427 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 446 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<int[]> {
/* 450 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 454 */       int minLength = Math.min(left.length, right.length);
/* 455 */       for (int i = 0; i < minLength; i++) {
/* 456 */         int result = Ints.compare(left[i], right[i]);
/* 457 */         if (result != 0) {
/* 458 */           return result;
/*     */         }
/*     */       } 
/* 461 */       return left.length - right.length;
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
/*     */   public static int[] toArray(Collection<? extends Number> collection) {
/* 481 */     if (collection instanceof IntArrayAsList) {
/* 482 */       return ((IntArrayAsList)collection).toIntArray();
/*     */     }
/*     */     
/* 485 */     Object[] boxedArray = collection.toArray();
/* 486 */     int len = boxedArray.length;
/* 487 */     int[] array = new int[len];
/* 488 */     for (int i = 0; i < len; i++)
/*     */     {
/* 490 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
/*     */     }
/* 492 */     return array;
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
/*     */   public static List<Integer> asList(int... backingArray) {
/* 510 */     if (backingArray.length == 0) {
/* 511 */       return Collections.emptyList();
/*     */     }
/* 513 */     return new IntArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     final int[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IntArrayAsList(int[] array) {
/* 524 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     IntArrayAsList(int[] array, int start, int end) {
/* 528 */       this.array = array;
/* 529 */       this.start = start;
/* 530 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 534 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 538 */       return false;
/*     */     }
/*     */     
/*     */     public Integer get(int index) {
/* 542 */       Preconditions.checkElementIndex(index, size());
/* 543 */       return Integer.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 548 */       return (target instanceof Integer && Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 554 */       if (target instanceof Integer) {
/* 555 */         int i = Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 556 */         if (i >= 0) {
/* 557 */           return i - this.start;
/*     */         }
/*     */       } 
/* 560 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 565 */       if (target instanceof Integer) {
/* 566 */         int i = Ints.lastIndexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 567 */         if (i >= 0) {
/* 568 */           return i - this.start;
/*     */         }
/*     */       } 
/* 571 */       return -1;
/*     */     }
/*     */     
/*     */     public Integer set(int index, Integer element) {
/* 575 */       Preconditions.checkElementIndex(index, size());
/* 576 */       int oldValue = this.array[this.start + index];
/*     */       
/* 578 */       this.array[this.start + index] = ((Integer)Preconditions.checkNotNull(element)).intValue();
/* 579 */       return Integer.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 583 */       int size = size();
/* 584 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 585 */       if (fromIndex == toIndex) {
/* 586 */         return Collections.emptyList();
/*     */       }
/* 588 */       return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 592 */       if (object == this) {
/* 593 */         return true;
/*     */       }
/* 595 */       if (object instanceof IntArrayAsList) {
/* 596 */         IntArrayAsList that = (IntArrayAsList)object;
/* 597 */         int size = size();
/* 598 */         if (that.size() != size) {
/* 599 */           return false;
/*     */         }
/* 601 */         for (int i = 0; i < size; i++) {
/* 602 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 603 */             return false;
/*     */           }
/*     */         } 
/* 606 */         return true;
/*     */       } 
/* 608 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 612 */       int result = 1;
/* 613 */       for (int i = this.start; i < this.end; i++) {
/* 614 */         result = 31 * result + Ints.hashCode(this.array[i]);
/*     */       }
/* 616 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 620 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 621 */       builder.append('[').append(this.array[this.start]);
/* 622 */       for (int i = this.start + 1; i < this.end; i++) {
/* 623 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 625 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     int[] toIntArray() {
/* 630 */       int size = size();
/* 631 */       int[] result = new int[size];
/* 632 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 633 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 639 */   private static final byte[] asciiDigits = new byte[128];
/*     */   
/*     */   static {
/* 642 */     Arrays.fill(asciiDigits, (byte)-1); int i;
/* 643 */     for (i = 0; i <= 9; i++) {
/* 644 */       asciiDigits[48 + i] = (byte)i;
/*     */     }
/* 646 */     for (i = 0; i <= 26; i++) {
/* 647 */       asciiDigits[65 + i] = (byte)(10 + i);
/* 648 */       asciiDigits[97 + i] = (byte)(10 + i);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int digit(char c) {
/* 653 */     return (c < '') ? asciiDigits[c] : -1;
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
/*     */   @CheckForNull
/*     */   @Beta
/*     */   public static Integer tryParse(String string) {
/* 679 */     return tryParse(string, 10);
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
/*     */   @CheckForNull
/*     */   static Integer tryParse(String string, int radix) {
/* 706 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 707 */       return null;
/*     */     }
/* 709 */     if (radix < 2 || radix > 36) {
/* 710 */       int i = radix; throw new IllegalArgumentException((new StringBuilder(65)).append("radix must be between MIN_RADIX and MAX_RADIX but was ").append(i).toString());
/*     */     } 
/*     */     
/* 713 */     boolean negative = (string.charAt(0) == '-');
/* 714 */     int index = negative ? 1 : 0;
/* 715 */     if (index == string.length()) {
/* 716 */       return null;
/*     */     }
/* 718 */     int digit = digit(string.charAt(index++));
/* 719 */     if (digit < 0 || digit >= radix) {
/* 720 */       return null;
/*     */     }
/* 722 */     int accum = -digit;
/*     */     
/* 724 */     int cap = Integer.MIN_VALUE / radix;
/*     */     
/* 726 */     while (index < string.length()) {
/* 727 */       digit = digit(string.charAt(index++));
/* 728 */       if (digit < 0 || digit >= radix || accum < cap) {
/* 729 */         return null;
/*     */       }
/* 731 */       accum *= radix;
/* 732 */       if (accum < Integer.MIN_VALUE + digit) {
/* 733 */         return null;
/*     */       }
/* 735 */       accum -= digit;
/*     */     } 
/*     */     
/* 738 */     if (negative)
/* 739 */       return Integer.valueOf(accum); 
/* 740 */     if (accum == Integer.MIN_VALUE) {
/* 741 */       return null;
/*     */     }
/* 743 */     return Integer.valueOf(-accum);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Ints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */