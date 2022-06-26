/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Longs
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;
/*     */   
/*     */   public static int hashCode(long value) {
/*  78 */     return (int)(value ^ value >>> 32L);
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
/*     */   public static int compare(long a, long b) {
/*  94 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
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
/*     */   public static boolean contains(long[] array, long target) {
/* 107 */     for (long value : array) {
/* 108 */       if (value == target) {
/* 109 */         return true;
/*     */       }
/*     */     } 
/* 112 */     return false;
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
/*     */   public static int indexOf(long[] array, long target) {
/* 125 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(long[] array, long target, int start, int end) {
/* 131 */     for (int i = start; i < end; i++) {
/* 132 */       if (array[i] == target) {
/* 133 */         return i;
/*     */       }
/*     */     } 
/* 136 */     return -1;
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
/*     */   public static int indexOf(long[] array, long[] target) {
/* 151 */     Preconditions.checkNotNull(array, "array");
/* 152 */     Preconditions.checkNotNull(target, "target");
/* 153 */     if (target.length == 0) {
/* 154 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 158 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 159 */       int j = 0; while (true) { if (j < target.length) {
/* 160 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 164 */         return i; }
/*     */     
/* 166 */     }  return -1;
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
/*     */   public static int lastIndexOf(long[] array, long target) {
/* 179 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(long[] array, long target, int start, int end) {
/* 185 */     for (int i = end - 1; i >= start; i--) {
/* 186 */       if (array[i] == target) {
/* 187 */         return i;
/*     */       }
/*     */     } 
/* 190 */     return -1;
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
/*     */   public static long min(long... array) {
/* 202 */     Preconditions.checkArgument((array.length > 0));
/* 203 */     long min = array[0];
/* 204 */     for (int i = 1; i < array.length; i++) {
/* 205 */       if (array[i] < min) {
/* 206 */         min = array[i];
/*     */       }
/*     */     } 
/* 209 */     return min;
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
/*     */   public static long max(long... array) {
/* 221 */     Preconditions.checkArgument((array.length > 0));
/* 222 */     long max = array[0];
/* 223 */     for (int i = 1; i < array.length; i++) {
/* 224 */       if (array[i] > max) {
/* 225 */         max = array[i];
/*     */       }
/*     */     } 
/* 228 */     return max;
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
/*     */   public static long[] concat(long[]... arrays) {
/* 241 */     int length = 0;
/* 242 */     for (long[] array : arrays) {
/* 243 */       length += array.length;
/*     */     }
/* 245 */     long[] result = new long[length];
/* 246 */     int pos = 0;
/* 247 */     for (long[] array : arrays) {
/* 248 */       System.arraycopy(array, 0, result, pos, array.length);
/* 249 */       pos += array.length;
/*     */     } 
/* 251 */     return result;
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
/*     */   public static byte[] toByteArray(long value) {
/* 268 */     byte[] result = new byte[8];
/* 269 */     for (int i = 7; i >= 0; i--) {
/* 270 */       result[i] = (byte)(int)(value & 0xFFL);
/* 271 */       value >>= 8L;
/*     */     } 
/* 273 */     return result;
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
/*     */   public static long fromByteArray(byte[] bytes) {
/* 290 */     Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", new Object[] { Integer.valueOf(bytes.length), Integer.valueOf(8) });
/*     */     
/* 292 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
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
/*     */   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
/* 305 */     return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Long tryParse(String string) {
/* 337 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 338 */       return null;
/*     */     }
/* 340 */     boolean negative = (string.charAt(0) == '-');
/* 341 */     int index = negative ? 1 : 0;
/* 342 */     if (index == string.length()) {
/* 343 */       return null;
/*     */     }
/* 345 */     int digit = string.charAt(index++) - 48;
/* 346 */     if (digit < 0 || digit > 9) {
/* 347 */       return null;
/*     */     }
/* 349 */     long accum = -digit;
/* 350 */     while (index < string.length()) {
/* 351 */       digit = string.charAt(index++) - 48;
/* 352 */       if (digit < 0 || digit > 9 || accum < -922337203685477580L) {
/* 353 */         return null;
/*     */       }
/* 355 */       accum *= 10L;
/* 356 */       if (accum < Long.MIN_VALUE + digit) {
/* 357 */         return null;
/*     */       }
/* 359 */       accum -= digit;
/*     */     } 
/*     */     
/* 362 */     if (negative)
/* 363 */       return Long.valueOf(accum); 
/* 364 */     if (accum == Long.MIN_VALUE) {
/* 365 */       return null;
/*     */     }
/* 367 */     return Long.valueOf(-accum);
/*     */   }
/*     */   
/*     */   private static final class LongConverter
/*     */     extends Converter<String, Long> implements Serializable {
/* 372 */     static final LongConverter INSTANCE = new LongConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Long doForward(String value) {
/* 376 */       return Long.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Long value) {
/* 381 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 386 */       return "Longs.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 390 */       return INSTANCE;
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
/*     */   public static Converter<String, Long> stringConverter() {
/* 403 */     return LongConverter.INSTANCE;
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
/*     */   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
/* 424 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 425 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 426 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long[] copyOf(long[] original, int length) {
/* 433 */     long[] copy = new long[length];
/* 434 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 435 */     return copy;
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
/*     */   public static String join(String separator, long... array) {
/* 448 */     Preconditions.checkNotNull(separator);
/* 449 */     if (array.length == 0) {
/* 450 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 454 */     StringBuilder builder = new StringBuilder(array.length * 10);
/* 455 */     builder.append(array[0]);
/* 456 */     for (int i = 1; i < array.length; i++) {
/* 457 */       builder.append(separator).append(array[i]);
/*     */     }
/* 459 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 479 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<long[]> {
/* 483 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 487 */       int minLength = Math.min(left.length, right.length);
/* 488 */       for (int i = 0; i < minLength; i++) {
/* 489 */         int result = Longs.compare(left[i], right[i]);
/* 490 */         if (result != 0) {
/* 491 */           return result;
/*     */         }
/*     */       } 
/* 494 */       return left.length - right.length;
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
/*     */   public static long[] toArray(Collection<? extends Number> collection) {
/* 514 */     if (collection instanceof LongArrayAsList) {
/* 515 */       return ((LongArrayAsList)collection).toLongArray();
/*     */     }
/*     */     
/* 518 */     Object[] boxedArray = collection.toArray();
/* 519 */     int len = boxedArray.length;
/* 520 */     long[] array = new long[len];
/* 521 */     for (int i = 0; i < len; i++)
/*     */     {
/* 523 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
/*     */     }
/* 525 */     return array;
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
/*     */   public static List<Long> asList(long... backingArray) {
/* 543 */     if (backingArray.length == 0) {
/* 544 */       return Collections.emptyList();
/*     */     }
/* 546 */     return new LongArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     final long[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongArrayAsList(long[] array) {
/* 557 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     LongArrayAsList(long[] array, int start, int end) {
/* 561 */       this.array = array;
/* 562 */       this.start = start;
/* 563 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 567 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 571 */       return false;
/*     */     }
/*     */     
/*     */     public Long get(int index) {
/* 575 */       Preconditions.checkElementIndex(index, size());
/* 576 */       return Long.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 581 */       return (target instanceof Long && Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 587 */       if (target instanceof Long) {
/* 588 */         int i = Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 589 */         if (i >= 0) {
/* 590 */           return i - this.start;
/*     */         }
/*     */       } 
/* 593 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 598 */       if (target instanceof Long) {
/* 599 */         int i = Longs.lastIndexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 600 */         if (i >= 0) {
/* 601 */           return i - this.start;
/*     */         }
/*     */       } 
/* 604 */       return -1;
/*     */     }
/*     */     
/*     */     public Long set(int index, Long element) {
/* 608 */       Preconditions.checkElementIndex(index, size());
/* 609 */       long oldValue = this.array[this.start + index];
/*     */       
/* 611 */       this.array[this.start + index] = ((Long)Preconditions.checkNotNull(element)).longValue();
/* 612 */       return Long.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 616 */       int size = size();
/* 617 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 618 */       if (fromIndex == toIndex) {
/* 619 */         return Collections.emptyList();
/*     */       }
/* 621 */       return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 625 */       if (object == this) {
/* 626 */         return true;
/*     */       }
/* 628 */       if (object instanceof LongArrayAsList) {
/* 629 */         LongArrayAsList that = (LongArrayAsList)object;
/* 630 */         int size = size();
/* 631 */         if (that.size() != size) {
/* 632 */           return false;
/*     */         }
/* 634 */         for (int i = 0; i < size; i++) {
/* 635 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 636 */             return false;
/*     */           }
/*     */         } 
/* 639 */         return true;
/*     */       } 
/* 641 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 645 */       int result = 1;
/* 646 */       for (int i = this.start; i < this.end; i++) {
/* 647 */         result = 31 * result + Longs.hashCode(this.array[i]);
/*     */       }
/* 649 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 653 */       StringBuilder builder = new StringBuilder(size() * 10);
/* 654 */       builder.append('[').append(this.array[this.start]);
/* 655 */       for (int i = this.start + 1; i < this.end; i++) {
/* 656 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 658 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     long[] toLongArray() {
/* 663 */       int size = size();
/* 664 */       long[] result = new long[size];
/* 665 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 666 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Longs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */