/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Chars
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   
/*     */   public static int hashCode(char value) {
/*  68 */     return value;
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
/*     */   public static char checkedCast(long value) {
/*  80 */     char result = (char)(int)value;
/*  81 */     if (result != value) {
/*     */       
/*  83 */       long l = value; throw new IllegalArgumentException((new StringBuilder(34)).append("Out of range: ").append(l).toString());
/*     */     } 
/*  85 */     return result;
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
/*     */   public static char saturatedCast(long value) {
/*  97 */     if (value > 65535L) {
/*  98 */       return Character.MAX_VALUE;
/*     */     }
/* 100 */     if (value < 0L) {
/* 101 */       return Character.MIN_VALUE;
/*     */     }
/* 103 */     return (char)(int)value;
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
/*     */   public static int compare(char a, char b) {
/* 119 */     return a - b;
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
/*     */   public static boolean contains(char[] array, char target) {
/* 132 */     for (char value : array) {
/* 133 */       if (value == target) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/* 137 */     return false;
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
/*     */   public static int indexOf(char[] array, char target) {
/* 150 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(char[] array, char target, int start, int end) {
/* 156 */     for (int i = start; i < end; i++) {
/* 157 */       if (array[i] == target) {
/* 158 */         return i;
/*     */       }
/*     */     } 
/* 161 */     return -1;
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
/*     */   public static int indexOf(char[] array, char[] target) {
/* 176 */     Preconditions.checkNotNull(array, "array");
/* 177 */     Preconditions.checkNotNull(target, "target");
/* 178 */     if (target.length == 0) {
/* 179 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 184 */       int j = 0; while (true) { if (j < target.length) {
/* 185 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 189 */         return i; }
/*     */     
/* 191 */     }  return -1;
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
/*     */   public static int lastIndexOf(char[] array, char target) {
/* 204 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(char[] array, char target, int start, int end) {
/* 210 */     for (int i = end - 1; i >= start; i--) {
/* 211 */       if (array[i] == target) {
/* 212 */         return i;
/*     */       }
/*     */     } 
/* 215 */     return -1;
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
/*     */   public static char min(char... array) {
/* 227 */     Preconditions.checkArgument((array.length > 0));
/* 228 */     char min = array[0];
/* 229 */     for (int i = 1; i < array.length; i++) {
/* 230 */       if (array[i] < min) {
/* 231 */         min = array[i];
/*     */       }
/*     */     } 
/* 234 */     return min;
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
/*     */   public static char max(char... array) {
/* 246 */     Preconditions.checkArgument((array.length > 0));
/* 247 */     char max = array[0];
/* 248 */     for (int i = 1; i < array.length; i++) {
/* 249 */       if (array[i] > max) {
/* 250 */         max = array[i];
/*     */       }
/*     */     } 
/* 253 */     return max;
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
/*     */   public static char[] concat(char[]... arrays) {
/* 266 */     int length = 0;
/* 267 */     for (char[] array : arrays) {
/* 268 */       length += array.length;
/*     */     }
/* 270 */     char[] result = new char[length];
/* 271 */     int pos = 0;
/* 272 */     for (char[] array : arrays) {
/* 273 */       System.arraycopy(array, 0, result, pos, array.length);
/* 274 */       pos += array.length;
/*     */     } 
/* 276 */     return result;
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
/*     */   public static byte[] toByteArray(char value) {
/* 292 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   public static char fromByteArray(byte[] bytes) {
/* 311 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", new Object[] { Integer.valueOf(bytes.length), Integer.valueOf(2) });
/*     */     
/* 313 */     return fromBytes(bytes[0], bytes[1]);
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
/*     */   public static char fromBytes(byte b1, byte b2) {
/* 325 */     return (char)(b1 << 8 | b2 & 0xFF);
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
/*     */   public static char[] ensureCapacity(char[] array, int minLength, int padding) {
/* 346 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 347 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 348 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] copyOf(char[] original, int length) {
/* 355 */     char[] copy = new char[length];
/* 356 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 357 */     return copy;
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
/*     */   public static String join(String separator, char... array) {
/* 370 */     Preconditions.checkNotNull(separator);
/* 371 */     int len = array.length;
/* 372 */     if (len == 0) {
/* 373 */       return "";
/*     */     }
/*     */     
/* 376 */     StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
/*     */     
/* 378 */     builder.append(array[0]);
/* 379 */     for (int i = 1; i < len; i++) {
/* 380 */       builder.append(separator).append(array[i]);
/*     */     }
/* 382 */     return builder.toString();
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
/*     */   public static Comparator<char[]> lexicographicalComparator() {
/* 402 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<char[]> {
/* 406 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(char[] left, char[] right) {
/* 410 */       int minLength = Math.min(left.length, right.length);
/* 411 */       for (int i = 0; i < minLength; i++) {
/* 412 */         int result = Chars.compare(left[i], right[i]);
/* 413 */         if (result != 0) {
/* 414 */           return result;
/*     */         }
/*     */       } 
/* 417 */       return left.length - right.length;
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
/*     */   public static char[] toArray(Collection<Character> collection) {
/* 436 */     if (collection instanceof CharArrayAsList) {
/* 437 */       return ((CharArrayAsList)collection).toCharArray();
/*     */     }
/*     */     
/* 440 */     Object[] boxedArray = collection.toArray();
/* 441 */     int len = boxedArray.length;
/* 442 */     char[] array = new char[len];
/* 443 */     for (int i = 0; i < len; i++)
/*     */     {
/* 445 */       array[i] = ((Character)Preconditions.checkNotNull(boxedArray[i])).charValue();
/*     */     }
/* 447 */     return array;
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
/*     */   public static List<Character> asList(char... backingArray) {
/* 465 */     if (backingArray.length == 0) {
/* 466 */       return Collections.emptyList();
/*     */     }
/* 468 */     return new CharArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
/*     */     final char[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     CharArrayAsList(char[] array) {
/* 479 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     CharArrayAsList(char[] array, int start, int end) {
/* 483 */       this.array = array;
/* 484 */       this.start = start;
/* 485 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 489 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 493 */       return false;
/*     */     }
/*     */     
/*     */     public Character get(int index) {
/* 497 */       Preconditions.checkElementIndex(index, size());
/* 498 */       return Character.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 503 */       return (target instanceof Character && Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 509 */       if (target instanceof Character) {
/* 510 */         int i = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 511 */         if (i >= 0) {
/* 512 */           return i - this.start;
/*     */         }
/*     */       } 
/* 515 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 520 */       if (target instanceof Character) {
/* 521 */         int i = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 522 */         if (i >= 0) {
/* 523 */           return i - this.start;
/*     */         }
/*     */       } 
/* 526 */       return -1;
/*     */     }
/*     */     
/*     */     public Character set(int index, Character element) {
/* 530 */       Preconditions.checkElementIndex(index, size());
/* 531 */       char oldValue = this.array[this.start + index];
/*     */       
/* 533 */       this.array[this.start + index] = ((Character)Preconditions.checkNotNull(element)).charValue();
/* 534 */       return Character.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Character> subList(int fromIndex, int toIndex) {
/* 538 */       int size = size();
/* 539 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 540 */       if (fromIndex == toIndex) {
/* 541 */         return Collections.emptyList();
/*     */       }
/* 543 */       return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 547 */       if (object == this) {
/* 548 */         return true;
/*     */       }
/* 550 */       if (object instanceof CharArrayAsList) {
/* 551 */         CharArrayAsList that = (CharArrayAsList)object;
/* 552 */         int size = size();
/* 553 */         if (that.size() != size) {
/* 554 */           return false;
/*     */         }
/* 556 */         for (int i = 0; i < size; i++) {
/* 557 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 558 */             return false;
/*     */           }
/*     */         } 
/* 561 */         return true;
/*     */       } 
/* 563 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 567 */       int result = 1;
/* 568 */       for (int i = this.start; i < this.end; i++) {
/* 569 */         result = 31 * result + Chars.hashCode(this.array[i]);
/*     */       }
/* 571 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 575 */       StringBuilder builder = new StringBuilder(size() * 3);
/* 576 */       builder.append('[').append(this.array[this.start]);
/* 577 */       for (int i = this.start + 1; i < this.end; i++) {
/* 578 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 580 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     char[] toCharArray() {
/* 585 */       int size = size();
/* 586 */       char[] result = new char[size];
/* 587 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 588 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Chars.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */