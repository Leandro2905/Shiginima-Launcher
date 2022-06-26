/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Shorts
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   public static final short MAX_POWER_OF_TWO = 16384;
/*     */   
/*     */   public static int hashCode(short value) {
/*  74 */     return value;
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
/*     */   public static short checkedCast(long value) {
/*  87 */     short result = (short)(int)value;
/*  88 */     if (result != value) {
/*     */       
/*  90 */       long l = value; throw new IllegalArgumentException((new StringBuilder(34)).append("Out of range: ").append(l).toString());
/*     */     } 
/*  92 */     return result;
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
/*     */   public static short saturatedCast(long value) {
/* 104 */     if (value > 32767L) {
/* 105 */       return Short.MAX_VALUE;
/*     */     }
/* 107 */     if (value < -32768L) {
/* 108 */       return Short.MIN_VALUE;
/*     */     }
/* 110 */     return (short)(int)value;
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
/*     */   public static int compare(short a, short b) {
/* 126 */     return a - b;
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
/*     */   public static boolean contains(short[] array, short target) {
/* 139 */     for (short value : array) {
/* 140 */       if (value == target) {
/* 141 */         return true;
/*     */       }
/*     */     } 
/* 144 */     return false;
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
/*     */   public static int indexOf(short[] array, short target) {
/* 157 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(short[] array, short target, int start, int end) {
/* 163 */     for (int i = start; i < end; i++) {
/* 164 */       if (array[i] == target) {
/* 165 */         return i;
/*     */       }
/*     */     } 
/* 168 */     return -1;
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
/*     */   public static int indexOf(short[] array, short[] target) {
/* 183 */     Preconditions.checkNotNull(array, "array");
/* 184 */     Preconditions.checkNotNull(target, "target");
/* 185 */     if (target.length == 0) {
/* 186 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 190 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 191 */       int j = 0; while (true) { if (j < target.length) {
/* 192 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 196 */         return i; }
/*     */     
/* 198 */     }  return -1;
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
/*     */   public static int lastIndexOf(short[] array, short target) {
/* 211 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(short[] array, short target, int start, int end) {
/* 217 */     for (int i = end - 1; i >= start; i--) {
/* 218 */       if (array[i] == target) {
/* 219 */         return i;
/*     */       }
/*     */     } 
/* 222 */     return -1;
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
/*     */   public static short min(short... array) {
/* 234 */     Preconditions.checkArgument((array.length > 0));
/* 235 */     short min = array[0];
/* 236 */     for (int i = 1; i < array.length; i++) {
/* 237 */       if (array[i] < min) {
/* 238 */         min = array[i];
/*     */       }
/*     */     } 
/* 241 */     return min;
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
/*     */   public static short max(short... array) {
/* 253 */     Preconditions.checkArgument((array.length > 0));
/* 254 */     short max = array[0];
/* 255 */     for (int i = 1; i < array.length; i++) {
/* 256 */       if (array[i] > max) {
/* 257 */         max = array[i];
/*     */       }
/*     */     } 
/* 260 */     return max;
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
/*     */   public static short[] concat(short[]... arrays) {
/* 273 */     int length = 0;
/* 274 */     for (short[] array : arrays) {
/* 275 */       length += array.length;
/*     */     }
/* 277 */     short[] result = new short[length];
/* 278 */     int pos = 0;
/* 279 */     for (short[] array : arrays) {
/* 280 */       System.arraycopy(array, 0, result, pos, array.length);
/* 281 */       pos += array.length;
/*     */     } 
/* 283 */     return result;
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
/*     */   @GwtIncompatible("doesn't work")
/*     */   public static byte[] toByteArray(short value) {
/* 300 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   @GwtIncompatible("doesn't work")
/*     */   public static short fromByteArray(byte[] bytes) {
/* 319 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", new Object[] { Integer.valueOf(bytes.length), Integer.valueOf(2) });
/*     */     
/* 321 */     return fromBytes(bytes[0], bytes[1]);
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
/*     */   public static short fromBytes(byte b1, byte b2) {
/* 333 */     return (short)(b1 << 8 | b2 & 0xFF);
/*     */   }
/*     */   
/*     */   private static final class ShortConverter
/*     */     extends Converter<String, Short> implements Serializable {
/* 338 */     static final ShortConverter INSTANCE = new ShortConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Short doForward(String value) {
/* 342 */       return Short.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Short value) {
/* 347 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 352 */       return "Shorts.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 356 */       return INSTANCE;
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
/*     */   public static Converter<String, Short> stringConverter() {
/* 369 */     return ShortConverter.INSTANCE;
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
/*     */   public static short[] ensureCapacity(short[] array, int minLength, int padding) {
/* 390 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 391 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 392 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static short[] copyOf(short[] original, int length) {
/* 399 */     short[] copy = new short[length];
/* 400 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 401 */     return copy;
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
/*     */   public static String join(String separator, short... array) {
/* 414 */     Preconditions.checkNotNull(separator);
/* 415 */     if (array.length == 0) {
/* 416 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 420 */     StringBuilder builder = new StringBuilder(array.length * 6);
/* 421 */     builder.append(array[0]);
/* 422 */     for (int i = 1; i < array.length; i++) {
/* 423 */       builder.append(separator).append(array[i]);
/*     */     }
/* 425 */     return builder.toString();
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
/*     */   public static Comparator<short[]> lexicographicalComparator() {
/* 445 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<short[]> {
/* 449 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(short[] left, short[] right) {
/* 453 */       int minLength = Math.min(left.length, right.length);
/* 454 */       for (int i = 0; i < minLength; i++) {
/* 455 */         int result = Shorts.compare(left[i], right[i]);
/* 456 */         if (result != 0) {
/* 457 */           return result;
/*     */         }
/*     */       } 
/* 460 */       return left.length - right.length;
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
/*     */   public static short[] toArray(Collection<? extends Number> collection) {
/* 480 */     if (collection instanceof ShortArrayAsList) {
/* 481 */       return ((ShortArrayAsList)collection).toShortArray();
/*     */     }
/*     */     
/* 484 */     Object[] boxedArray = collection.toArray();
/* 485 */     int len = boxedArray.length;
/* 486 */     short[] array = new short[len];
/* 487 */     for (int i = 0; i < len; i++)
/*     */     {
/* 489 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).shortValue();
/*     */     }
/* 491 */     return array;
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
/*     */   public static List<Short> asList(short... backingArray) {
/* 509 */     if (backingArray.length == 0) {
/* 510 */       return Collections.emptyList();
/*     */     }
/* 512 */     return new ShortArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
/*     */     final short[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ShortArrayAsList(short[] array) {
/* 523 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ShortArrayAsList(short[] array, int start, int end) {
/* 527 */       this.array = array;
/* 528 */       this.start = start;
/* 529 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 533 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 537 */       return false;
/*     */     }
/*     */     
/*     */     public Short get(int index) {
/* 541 */       Preconditions.checkElementIndex(index, size());
/* 542 */       return Short.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 547 */       return (target instanceof Short && Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 553 */       if (target instanceof Short) {
/* 554 */         int i = Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 555 */         if (i >= 0) {
/* 556 */           return i - this.start;
/*     */         }
/*     */       } 
/* 559 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 564 */       if (target instanceof Short) {
/* 565 */         int i = Shorts.lastIndexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 566 */         if (i >= 0) {
/* 567 */           return i - this.start;
/*     */         }
/*     */       } 
/* 570 */       return -1;
/*     */     }
/*     */     
/*     */     public Short set(int index, Short element) {
/* 574 */       Preconditions.checkElementIndex(index, size());
/* 575 */       short oldValue = this.array[this.start + index];
/*     */       
/* 577 */       this.array[this.start + index] = ((Short)Preconditions.checkNotNull(element)).shortValue();
/* 578 */       return Short.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Short> subList(int fromIndex, int toIndex) {
/* 582 */       int size = size();
/* 583 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 584 */       if (fromIndex == toIndex) {
/* 585 */         return Collections.emptyList();
/*     */       }
/* 587 */       return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 591 */       if (object == this) {
/* 592 */         return true;
/*     */       }
/* 594 */       if (object instanceof ShortArrayAsList) {
/* 595 */         ShortArrayAsList that = (ShortArrayAsList)object;
/* 596 */         int size = size();
/* 597 */         if (that.size() != size) {
/* 598 */           return false;
/*     */         }
/* 600 */         for (int i = 0; i < size; i++) {
/* 601 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 602 */             return false;
/*     */           }
/*     */         } 
/* 605 */         return true;
/*     */       } 
/* 607 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 611 */       int result = 1;
/* 612 */       for (int i = this.start; i < this.end; i++) {
/* 613 */         result = 31 * result + Shorts.hashCode(this.array[i]);
/*     */       }
/* 615 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 619 */       StringBuilder builder = new StringBuilder(size() * 6);
/* 620 */       builder.append('[').append(this.array[this.start]);
/* 621 */       for (int i = this.start + 1; i < this.end; i++) {
/* 622 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 624 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     short[] toShortArray() {
/* 629 */       int size = size();
/* 630 */       short[] result = new short[size];
/* 631 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 632 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Shorts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */