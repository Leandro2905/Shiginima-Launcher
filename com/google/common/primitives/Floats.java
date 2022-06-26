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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Floats
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   
/*     */   public static int hashCode(float value) {
/*  74 */     return Float.valueOf(value).hashCode();
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
/*     */   public static int compare(float a, float b) {
/*  92 */     return Float.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinite(float value) {
/* 103 */     return ((Float.NEGATIVE_INFINITY < value)) & ((value < Float.POSITIVE_INFINITY));
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
/*     */   public static boolean contains(float[] array, float target) {
/* 117 */     for (float value : array) {
/* 118 */       if (value == target) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/* 122 */     return false;
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
/*     */   public static int indexOf(float[] array, float target) {
/* 136 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(float[] array, float target, int start, int end) {
/* 142 */     for (int i = start; i < end; i++) {
/* 143 */       if (array[i] == target) {
/* 144 */         return i;
/*     */       }
/*     */     } 
/* 147 */     return -1;
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
/*     */   public static int indexOf(float[] array, float[] target) {
/* 165 */     Preconditions.checkNotNull(array, "array");
/* 166 */     Preconditions.checkNotNull(target, "target");
/* 167 */     if (target.length == 0) {
/* 168 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 172 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 173 */       int j = 0; while (true) { if (j < target.length) {
/* 174 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 178 */         return i; }
/*     */     
/* 180 */     }  return -1;
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
/*     */   public static int lastIndexOf(float[] array, float target) {
/* 194 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(float[] array, float target, int start, int end) {
/* 200 */     for (int i = end - 1; i >= start; i--) {
/* 201 */       if (array[i] == target) {
/* 202 */         return i;
/*     */       }
/*     */     } 
/* 205 */     return -1;
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
/*     */   public static float min(float... array) {
/* 218 */     Preconditions.checkArgument((array.length > 0));
/* 219 */     float min = array[0];
/* 220 */     for (int i = 1; i < array.length; i++) {
/* 221 */       min = Math.min(min, array[i]);
/*     */     }
/* 223 */     return min;
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
/*     */   public static float max(float... array) {
/* 236 */     Preconditions.checkArgument((array.length > 0));
/* 237 */     float max = array[0];
/* 238 */     for (int i = 1; i < array.length; i++) {
/* 239 */       max = Math.max(max, array[i]);
/*     */     }
/* 241 */     return max;
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
/*     */   public static float[] concat(float[]... arrays) {
/* 254 */     int length = 0;
/* 255 */     for (float[] array : arrays) {
/* 256 */       length += array.length;
/*     */     }
/* 258 */     float[] result = new float[length];
/* 259 */     int pos = 0;
/* 260 */     for (float[] array : arrays) {
/* 261 */       System.arraycopy(array, 0, result, pos, array.length);
/* 262 */       pos += array.length;
/*     */     } 
/* 264 */     return result;
/*     */   }
/*     */   
/*     */   private static final class FloatConverter
/*     */     extends Converter<String, Float> implements Serializable {
/* 269 */     static final FloatConverter INSTANCE = new FloatConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Float doForward(String value) {
/* 273 */       return Float.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Float value) {
/* 278 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 283 */       return "Floats.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 287 */       return INSTANCE;
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
/*     */   public static Converter<String, Float> stringConverter() {
/* 300 */     return FloatConverter.INSTANCE;
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
/*     */   public static float[] ensureCapacity(float[] array, int minLength, int padding) {
/* 321 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 322 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 323 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float[] copyOf(float[] original, int length) {
/* 330 */     float[] copy = new float[length];
/* 331 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 332 */     return copy;
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
/*     */   public static String join(String separator, float... array) {
/* 350 */     Preconditions.checkNotNull(separator);
/* 351 */     if (array.length == 0) {
/* 352 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 356 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 357 */     builder.append(array[0]);
/* 358 */     for (int i = 1; i < array.length; i++) {
/* 359 */       builder.append(separator).append(array[i]);
/*     */     }
/* 361 */     return builder.toString();
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
/*     */   public static Comparator<float[]> lexicographicalComparator() {
/* 381 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<float[]> {
/* 385 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(float[] left, float[] right) {
/* 389 */       int minLength = Math.min(left.length, right.length);
/* 390 */       for (int i = 0; i < minLength; i++) {
/* 391 */         int result = Float.compare(left[i], right[i]);
/* 392 */         if (result != 0) {
/* 393 */           return result;
/*     */         }
/*     */       } 
/* 396 */       return left.length - right.length;
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
/*     */   public static float[] toArray(Collection<? extends Number> collection) {
/* 416 */     if (collection instanceof FloatArrayAsList) {
/* 417 */       return ((FloatArrayAsList)collection).toFloatArray();
/*     */     }
/*     */     
/* 420 */     Object[] boxedArray = collection.toArray();
/* 421 */     int len = boxedArray.length;
/* 422 */     float[] array = new float[len];
/* 423 */     for (int i = 0; i < len; i++)
/*     */     {
/* 425 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
/*     */     }
/* 427 */     return array;
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
/*     */   public static List<Float> asList(float... backingArray) {
/* 448 */     if (backingArray.length == 0) {
/* 449 */       return Collections.emptyList();
/*     */     }
/* 451 */     return new FloatArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
/*     */     final float[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FloatArrayAsList(float[] array) {
/* 462 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     FloatArrayAsList(float[] array, int start, int end) {
/* 466 */       this.array = array;
/* 467 */       this.start = start;
/* 468 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 472 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 476 */       return false;
/*     */     }
/*     */     
/*     */     public Float get(int index) {
/* 480 */       Preconditions.checkElementIndex(index, size());
/* 481 */       return Float.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 486 */       return (target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 492 */       if (target instanceof Float) {
/* 493 */         int i = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 494 */         if (i >= 0) {
/* 495 */           return i - this.start;
/*     */         }
/*     */       } 
/* 498 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 503 */       if (target instanceof Float) {
/* 504 */         int i = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 505 */         if (i >= 0) {
/* 506 */           return i - this.start;
/*     */         }
/*     */       } 
/* 509 */       return -1;
/*     */     }
/*     */     
/*     */     public Float set(int index, Float element) {
/* 513 */       Preconditions.checkElementIndex(index, size());
/* 514 */       float oldValue = this.array[this.start + index];
/*     */       
/* 516 */       this.array[this.start + index] = ((Float)Preconditions.checkNotNull(element)).floatValue();
/* 517 */       return Float.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Float> subList(int fromIndex, int toIndex) {
/* 521 */       int size = size();
/* 522 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 523 */       if (fromIndex == toIndex) {
/* 524 */         return Collections.emptyList();
/*     */       }
/* 526 */       return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 530 */       if (object == this) {
/* 531 */         return true;
/*     */       }
/* 533 */       if (object instanceof FloatArrayAsList) {
/* 534 */         FloatArrayAsList that = (FloatArrayAsList)object;
/* 535 */         int size = size();
/* 536 */         if (that.size() != size) {
/* 537 */           return false;
/*     */         }
/* 539 */         for (int i = 0; i < size; i++) {
/* 540 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 541 */             return false;
/*     */           }
/*     */         } 
/* 544 */         return true;
/*     */       } 
/* 546 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 550 */       int result = 1;
/* 551 */       for (int i = this.start; i < this.end; i++) {
/* 552 */         result = 31 * result + Floats.hashCode(this.array[i]);
/*     */       }
/* 554 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 558 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 559 */       builder.append('[').append(this.array[this.start]);
/* 560 */       for (int i = this.start + 1; i < this.end; i++) {
/* 561 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 563 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     float[] toFloatArray() {
/* 568 */       int size = size();
/* 569 */       float[] result = new float[size];
/* 570 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 571 */       return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible("regular expressions")
/*     */   @Beta
/*     */   public static Float tryParse(String string) {
/* 600 */     if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 604 */         return Float.valueOf(Float.parseFloat(string));
/* 605 */       } catch (NumberFormatException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 610 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Floats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */