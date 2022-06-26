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
/*     */ import java.util.regex.Pattern;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Doubles
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   
/*     */   public static int hashCode(double value) {
/*  74 */     return Double.valueOf(value).hashCode();
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
/*     */   public static int compare(double a, double b) {
/*  96 */     return Double.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinite(double value) {
/* 107 */     return ((Double.NEGATIVE_INFINITY < value)) & ((value < Double.POSITIVE_INFINITY));
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
/*     */   public static boolean contains(double[] array, double target) {
/* 121 */     for (double value : array) {
/* 122 */       if (value == target) {
/* 123 */         return true;
/*     */       }
/*     */     } 
/* 126 */     return false;
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
/*     */   public static int indexOf(double[] array, double target) {
/* 140 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(double[] array, double target, int start, int end) {
/* 146 */     for (int i = start; i < end; i++) {
/* 147 */       if (array[i] == target) {
/* 148 */         return i;
/*     */       }
/*     */     } 
/* 151 */     return -1;
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
/*     */   public static int indexOf(double[] array, double[] target) {
/* 169 */     Preconditions.checkNotNull(array, "array");
/* 170 */     Preconditions.checkNotNull(target, "target");
/* 171 */     if (target.length == 0) {
/* 172 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 176 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 177 */       int j = 0; while (true) { if (j < target.length) {
/* 178 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 182 */         return i; }
/*     */     
/* 184 */     }  return -1;
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
/*     */   public static int lastIndexOf(double[] array, double target) {
/* 198 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(double[] array, double target, int start, int end) {
/* 204 */     for (int i = end - 1; i >= start; i--) {
/* 205 */       if (array[i] == target) {
/* 206 */         return i;
/*     */       }
/*     */     } 
/* 209 */     return -1;
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
/*     */   public static double min(double... array) {
/* 222 */     Preconditions.checkArgument((array.length > 0));
/* 223 */     double min = array[0];
/* 224 */     for (int i = 1; i < array.length; i++) {
/* 225 */       min = Math.min(min, array[i]);
/*     */     }
/* 227 */     return min;
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
/*     */   public static double max(double... array) {
/* 240 */     Preconditions.checkArgument((array.length > 0));
/* 241 */     double max = array[0];
/* 242 */     for (int i = 1; i < array.length; i++) {
/* 243 */       max = Math.max(max, array[i]);
/*     */     }
/* 245 */     return max;
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
/*     */   public static double[] concat(double[]... arrays) {
/* 258 */     int length = 0;
/* 259 */     for (double[] array : arrays) {
/* 260 */       length += array.length;
/*     */     }
/* 262 */     double[] result = new double[length];
/* 263 */     int pos = 0;
/* 264 */     for (double[] array : arrays) {
/* 265 */       System.arraycopy(array, 0, result, pos, array.length);
/* 266 */       pos += array.length;
/*     */     } 
/* 268 */     return result;
/*     */   }
/*     */   
/*     */   private static final class DoubleConverter
/*     */     extends Converter<String, Double> implements Serializable {
/* 273 */     static final DoubleConverter INSTANCE = new DoubleConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Double doForward(String value) {
/* 277 */       return Double.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Double value) {
/* 282 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 287 */       return "Doubles.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 291 */       return INSTANCE;
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
/*     */   public static Converter<String, Double> stringConverter() {
/* 304 */     return DoubleConverter.INSTANCE;
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
/*     */   public static double[] ensureCapacity(double[] array, int minLength, int padding) {
/* 325 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 326 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 327 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double[] copyOf(double[] original, int length) {
/* 334 */     double[] copy = new double[length];
/* 335 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 336 */     return copy;
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
/*     */   public static String join(String separator, double... array) {
/* 354 */     Preconditions.checkNotNull(separator);
/* 355 */     if (array.length == 0) {
/* 356 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 360 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 361 */     builder.append(array[0]);
/* 362 */     for (int i = 1; i < array.length; i++) {
/* 363 */       builder.append(separator).append(array[i]);
/*     */     }
/* 365 */     return builder.toString();
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
/*     */   public static Comparator<double[]> lexicographicalComparator() {
/* 385 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<double[]> {
/* 389 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(double[] left, double[] right) {
/* 393 */       int minLength = Math.min(left.length, right.length);
/* 394 */       for (int i = 0; i < minLength; i++) {
/* 395 */         int result = Double.compare(left[i], right[i]);
/* 396 */         if (result != 0) {
/* 397 */           return result;
/*     */         }
/*     */       } 
/* 400 */       return left.length - right.length;
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
/*     */   public static double[] toArray(Collection<? extends Number> collection) {
/* 420 */     if (collection instanceof DoubleArrayAsList) {
/* 421 */       return ((DoubleArrayAsList)collection).toDoubleArray();
/*     */     }
/*     */     
/* 424 */     Object[] boxedArray = collection.toArray();
/* 425 */     int len = boxedArray.length;
/* 426 */     double[] array = new double[len];
/* 427 */     for (int i = 0; i < len; i++)
/*     */     {
/* 429 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
/*     */     }
/* 431 */     return array;
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
/*     */   public static List<Double> asList(double... backingArray) {
/* 452 */     if (backingArray.length == 0) {
/* 453 */       return Collections.emptyList();
/*     */     }
/* 455 */     return new DoubleArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     final double[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     DoubleArrayAsList(double[] array) {
/* 466 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     DoubleArrayAsList(double[] array, int start, int end) {
/* 470 */       this.array = array;
/* 471 */       this.start = start;
/* 472 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 476 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 480 */       return false;
/*     */     }
/*     */     
/*     */     public Double get(int index) {
/* 484 */       Preconditions.checkElementIndex(index, size());
/* 485 */       return Double.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 490 */       return (target instanceof Double && Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 496 */       if (target instanceof Double) {
/* 497 */         int i = Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 498 */         if (i >= 0) {
/* 499 */           return i - this.start;
/*     */         }
/*     */       } 
/* 502 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 507 */       if (target instanceof Double) {
/* 508 */         int i = Doubles.lastIndexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 509 */         if (i >= 0) {
/* 510 */           return i - this.start;
/*     */         }
/*     */       } 
/* 513 */       return -1;
/*     */     }
/*     */     
/*     */     public Double set(int index, Double element) {
/* 517 */       Preconditions.checkElementIndex(index, size());
/* 518 */       double oldValue = this.array[this.start + index];
/*     */       
/* 520 */       this.array[this.start + index] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
/* 521 */       return Double.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 525 */       int size = size();
/* 526 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 527 */       if (fromIndex == toIndex) {
/* 528 */         return Collections.emptyList();
/*     */       }
/* 530 */       return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 534 */       if (object == this) {
/* 535 */         return true;
/*     */       }
/* 537 */       if (object instanceof DoubleArrayAsList) {
/* 538 */         DoubleArrayAsList that = (DoubleArrayAsList)object;
/* 539 */         int size = size();
/* 540 */         if (that.size() != size) {
/* 541 */           return false;
/*     */         }
/* 543 */         for (int i = 0; i < size; i++) {
/* 544 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 545 */             return false;
/*     */           }
/*     */         } 
/* 548 */         return true;
/*     */       } 
/* 550 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 554 */       int result = 1;
/* 555 */       for (int i = this.start; i < this.end; i++) {
/* 556 */         result = 31 * result + Doubles.hashCode(this.array[i]);
/*     */       }
/* 558 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 562 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 563 */       builder.append('[').append(this.array[this.start]);
/* 564 */       for (int i = this.start + 1; i < this.end; i++) {
/* 565 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 567 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     double[] toDoubleArray() {
/* 572 */       int size = size();
/* 573 */       double[] result = new double[size];
/* 574 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 575 */       return result;
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
/*     */   @GwtIncompatible("regular expressions")
/* 588 */   static final Pattern FLOATING_POINT_PATTERN = fpPattern();
/*     */   
/*     */   @GwtIncompatible("regular expressions")
/*     */   private static Pattern fpPattern() {
/* 592 */     String decimal = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
/* 593 */     String completeDec = String.valueOf(decimal).concat("(?:[eE][+-]?\\d++)?[fFdD]?");
/* 594 */     String hex = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
/* 595 */     String str1 = String.valueOf(String.valueOf(hex)), completeHex = (new StringBuilder(25 + str1.length())).append("0[xX]").append(str1).append("[pP][+-]?\\d++[fFdD]?").toString();
/* 596 */     String str2 = String.valueOf(String.valueOf(completeDec)), str3 = String.valueOf(String.valueOf(completeHex)), fpPattern = (new StringBuilder(23 + str2.length() + str3.length())).append("[+-]?(?:NaN|Infinity|").append(str2).append("|").append(str3).append(")").toString();
/* 597 */     return Pattern.compile(fpPattern);
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
/*     */   @Nullable
/*     */   @GwtIncompatible("regular expressions")
/*     */   @Beta
/*     */   public static Double tryParse(String string) {
/* 623 */     if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 627 */         return Double.valueOf(Double.parseDouble(string));
/* 628 */       } catch (NumberFormatException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 633 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Doubles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */