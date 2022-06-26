/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @GwtCompatible
/*     */ public final class Booleans
/*     */ {
/*     */   public static int hashCode(boolean value) {
/*  60 */     return value ? 1231 : 1237;
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
/*     */   public static int compare(boolean a, boolean b) {
/*  77 */     return (a == b) ? 0 : (a ? 1 : -1);
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
/*     */   public static boolean contains(boolean[] array, boolean target) {
/*  95 */     for (boolean value : array) {
/*  96 */       if (value == target) {
/*  97 */         return true;
/*     */       }
/*     */     } 
/* 100 */     return false;
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
/*     */   public static int indexOf(boolean[] array, boolean target) {
/* 117 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(boolean[] array, boolean target, int start, int end) {
/* 123 */     for (int i = start; i < end; i++) {
/* 124 */       if (array[i] == target) {
/* 125 */         return i;
/*     */       }
/*     */     } 
/* 128 */     return -1;
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
/*     */   public static int indexOf(boolean[] array, boolean[] target) {
/* 143 */     Preconditions.checkNotNull(array, "array");
/* 144 */     Preconditions.checkNotNull(target, "target");
/* 145 */     if (target.length == 0) {
/* 146 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 150 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 151 */       int j = 0; while (true) { if (j < target.length) {
/* 152 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 156 */         return i; }
/*     */     
/* 158 */     }  return -1;
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
/*     */   public static int lastIndexOf(boolean[] array, boolean target) {
/* 171 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
/* 177 */     for (int i = end - 1; i >= start; i--) {
/* 178 */       if (array[i] == target) {
/* 179 */         return i;
/*     */       }
/*     */     } 
/* 182 */     return -1;
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
/*     */   public static boolean[] concat(boolean[]... arrays) {
/* 195 */     int length = 0;
/* 196 */     for (boolean[] array : arrays) {
/* 197 */       length += array.length;
/*     */     }
/* 199 */     boolean[] result = new boolean[length];
/* 200 */     int pos = 0;
/* 201 */     for (boolean[] array : arrays) {
/* 202 */       System.arraycopy(array, 0, result, pos, array.length);
/* 203 */       pos += array.length;
/*     */     } 
/* 205 */     return result;
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
/*     */   public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
/* 226 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 227 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 228 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] copyOf(boolean[] original, int length) {
/* 235 */     boolean[] copy = new boolean[length];
/* 236 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 237 */     return copy;
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
/*     */   public static String join(String separator, boolean... array) {
/* 250 */     Preconditions.checkNotNull(separator);
/* 251 */     if (array.length == 0) {
/* 252 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 256 */     StringBuilder builder = new StringBuilder(array.length * 7);
/* 257 */     builder.append(array[0]);
/* 258 */     for (int i = 1; i < array.length; i++) {
/* 259 */       builder.append(separator).append(array[i]);
/*     */     }
/* 261 */     return builder.toString();
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
/*     */   public static Comparator<boolean[]> lexicographicalComparator() {
/* 281 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<boolean[]> {
/* 285 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(boolean[] left, boolean[] right) {
/* 289 */       int minLength = Math.min(left.length, right.length);
/* 290 */       for (int i = 0; i < minLength; i++) {
/* 291 */         int result = Booleans.compare(left[i], right[i]);
/* 292 */         if (result != 0) {
/* 293 */           return result;
/*     */         }
/*     */       } 
/* 296 */       return left.length - right.length;
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
/*     */   public static boolean[] toArray(Collection<Boolean> collection) {
/* 318 */     if (collection instanceof BooleanArrayAsList) {
/* 319 */       return ((BooleanArrayAsList)collection).toBooleanArray();
/*     */     }
/*     */     
/* 322 */     Object[] boxedArray = collection.toArray();
/* 323 */     int len = boxedArray.length;
/* 324 */     boolean[] array = new boolean[len];
/* 325 */     for (int i = 0; i < len; i++)
/*     */     {
/* 327 */       array[i] = ((Boolean)Preconditions.checkNotNull(boxedArray[i])).booleanValue();
/*     */     }
/* 329 */     return array;
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
/*     */   public static List<Boolean> asList(boolean... backingArray) {
/* 347 */     if (backingArray.length == 0) {
/* 348 */       return Collections.emptyList();
/*     */     }
/* 350 */     return new BooleanArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
/*     */     final boolean[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BooleanArrayAsList(boolean[] array) {
/* 361 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     BooleanArrayAsList(boolean[] array, int start, int end) {
/* 365 */       this.array = array;
/* 366 */       this.start = start;
/* 367 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 371 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 375 */       return false;
/*     */     }
/*     */     
/*     */     public Boolean get(int index) {
/* 379 */       Preconditions.checkElementIndex(index, size());
/* 380 */       return Boolean.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 385 */       return (target instanceof Boolean && Booleans.indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 391 */       if (target instanceof Boolean) {
/* 392 */         int i = Booleans.indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 393 */         if (i >= 0) {
/* 394 */           return i - this.start;
/*     */         }
/*     */       } 
/* 397 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 402 */       if (target instanceof Boolean) {
/* 403 */         int i = Booleans.lastIndexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 404 */         if (i >= 0) {
/* 405 */           return i - this.start;
/*     */         }
/*     */       } 
/* 408 */       return -1;
/*     */     }
/*     */     
/*     */     public Boolean set(int index, Boolean element) {
/* 412 */       Preconditions.checkElementIndex(index, size());
/* 413 */       boolean oldValue = this.array[this.start + index];
/*     */       
/* 415 */       this.array[this.start + index] = ((Boolean)Preconditions.checkNotNull(element)).booleanValue();
/* 416 */       return Boolean.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Boolean> subList(int fromIndex, int toIndex) {
/* 420 */       int size = size();
/* 421 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 422 */       if (fromIndex == toIndex) {
/* 423 */         return Collections.emptyList();
/*     */       }
/* 425 */       return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 429 */       if (object == this) {
/* 430 */         return true;
/*     */       }
/* 432 */       if (object instanceof BooleanArrayAsList) {
/* 433 */         BooleanArrayAsList that = (BooleanArrayAsList)object;
/* 434 */         int size = size();
/* 435 */         if (that.size() != size) {
/* 436 */           return false;
/*     */         }
/* 438 */         for (int i = 0; i < size; i++) {
/* 439 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 440 */             return false;
/*     */           }
/*     */         } 
/* 443 */         return true;
/*     */       } 
/* 445 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 449 */       int result = 1;
/* 450 */       for (int i = this.start; i < this.end; i++) {
/* 451 */         result = 31 * result + Booleans.hashCode(this.array[i]);
/*     */       }
/* 453 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 457 */       StringBuilder builder = new StringBuilder(size() * 7);
/* 458 */       builder.append(this.array[this.start] ? "[true" : "[false");
/* 459 */       for (int i = this.start + 1; i < this.end; i++) {
/* 460 */         builder.append(this.array[i] ? ", true" : ", false");
/*     */       }
/* 462 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean[] toBooleanArray() {
/* 467 */       int size = size();
/* 468 */       boolean[] result = new boolean[size];
/* 469 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 470 */       return result;
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
/*     */   public static int countTrue(boolean... values) {
/* 483 */     int count = 0;
/* 484 */     for (boolean value : values) {
/* 485 */       if (value) {
/* 486 */         count++;
/*     */       }
/*     */     } 
/* 489 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Booleans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */