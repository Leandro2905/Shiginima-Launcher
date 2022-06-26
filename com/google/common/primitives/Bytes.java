/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @GwtCompatible
/*     */ public final class Bytes
/*     */ {
/*     */   public static int hashCode(byte value) {
/*  62 */     return value;
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
/*     */   public static boolean contains(byte[] array, byte target) {
/*  75 */     for (byte value : array) {
/*  76 */       if (value == target) {
/*  77 */         return true;
/*     */       }
/*     */     } 
/*  80 */     return false;
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
/*     */   public static int indexOf(byte[] array, byte target) {
/*  93 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(byte[] array, byte target, int start, int end) {
/*  99 */     for (int i = start; i < end; i++) {
/* 100 */       if (array[i] == target) {
/* 101 */         return i;
/*     */       }
/*     */     } 
/* 104 */     return -1;
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
/*     */   public static int indexOf(byte[] array, byte[] target) {
/* 119 */     Preconditions.checkNotNull(array, "array");
/* 120 */     Preconditions.checkNotNull(target, "target");
/* 121 */     if (target.length == 0) {
/* 122 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 126 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 127 */       int j = 0; while (true) { if (j < target.length) {
/* 128 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 132 */         return i; }
/*     */     
/* 134 */     }  return -1;
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
/*     */   public static int lastIndexOf(byte[] array, byte target) {
/* 147 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(byte[] array, byte target, int start, int end) {
/* 153 */     for (int i = end - 1; i >= start; i--) {
/* 154 */       if (array[i] == target) {
/* 155 */         return i;
/*     */       }
/*     */     } 
/* 158 */     return -1;
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
/*     */   public static byte[] concat(byte[]... arrays) {
/* 171 */     int length = 0;
/* 172 */     for (byte[] array : arrays) {
/* 173 */       length += array.length;
/*     */     }
/* 175 */     byte[] result = new byte[length];
/* 176 */     int pos = 0;
/* 177 */     for (byte[] array : arrays) {
/* 178 */       System.arraycopy(array, 0, result, pos, array.length);
/* 179 */       pos += array.length;
/*     */     } 
/* 181 */     return result;
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
/*     */   public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
/* 202 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
/* 203 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
/* 204 */     return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] copyOf(byte[] original, int length) {
/* 211 */     byte[] copy = new byte[length];
/* 212 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
/* 213 */     return copy;
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
/*     */   public static byte[] toArray(Collection<? extends Number> collection) {
/* 232 */     if (collection instanceof ByteArrayAsList) {
/* 233 */       return ((ByteArrayAsList)collection).toByteArray();
/*     */     }
/*     */     
/* 236 */     Object[] boxedArray = collection.toArray();
/* 237 */     int len = boxedArray.length;
/* 238 */     byte[] array = new byte[len];
/* 239 */     for (int i = 0; i < len; i++)
/*     */     {
/* 241 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue();
/*     */     }
/* 243 */     return array;
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
/*     */   public static List<Byte> asList(byte... backingArray) {
/* 261 */     if (backingArray.length == 0) {
/* 262 */       return Collections.emptyList();
/*     */     }
/* 264 */     return new ByteArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
/*     */     final byte[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ByteArrayAsList(byte[] array) {
/* 275 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ByteArrayAsList(byte[] array, int start, int end) {
/* 279 */       this.array = array;
/* 280 */       this.start = start;
/* 281 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size() {
/* 285 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 289 */       return false;
/*     */     }
/*     */     
/*     */     public Byte get(int index) {
/* 293 */       Preconditions.checkElementIndex(index, size());
/* 294 */       return Byte.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 299 */       return (target instanceof Byte && Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 305 */       if (target instanceof Byte) {
/* 306 */         int i = Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 307 */         if (i >= 0) {
/* 308 */           return i - this.start;
/*     */         }
/*     */       } 
/* 311 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 316 */       if (target instanceof Byte) {
/* 317 */         int i = Bytes.lastIndexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 318 */         if (i >= 0) {
/* 319 */           return i - this.start;
/*     */         }
/*     */       } 
/* 322 */       return -1;
/*     */     }
/*     */     
/*     */     public Byte set(int index, Byte element) {
/* 326 */       Preconditions.checkElementIndex(index, size());
/* 327 */       byte oldValue = this.array[this.start + index];
/*     */       
/* 329 */       this.array[this.start + index] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
/* 330 */       return Byte.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Byte> subList(int fromIndex, int toIndex) {
/* 334 */       int size = size();
/* 335 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 336 */       if (fromIndex == toIndex) {
/* 337 */         return Collections.emptyList();
/*     */       }
/* 339 */       return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 343 */       if (object == this) {
/* 344 */         return true;
/*     */       }
/* 346 */       if (object instanceof ByteArrayAsList) {
/* 347 */         ByteArrayAsList that = (ByteArrayAsList)object;
/* 348 */         int size = size();
/* 349 */         if (that.size() != size) {
/* 350 */           return false;
/*     */         }
/* 352 */         for (int i = 0; i < size; i++) {
/* 353 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 354 */             return false;
/*     */           }
/*     */         } 
/* 357 */         return true;
/*     */       } 
/* 359 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 363 */       int result = 1;
/* 364 */       for (int i = this.start; i < this.end; i++) {
/* 365 */         result = 31 * result + Bytes.hashCode(this.array[i]);
/*     */       }
/* 367 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 371 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 372 */       builder.append('[').append(this.array[this.start]);
/* 373 */       for (int i = this.start + 1; i < this.end; i++) {
/* 374 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 376 */       return builder.append(']').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     byte[] toByteArray() {
/* 381 */       int size = size();
/* 382 */       byte[] result = new byte[size];
/* 383 */       System.arraycopy(this.array, this.start, result, 0, size);
/* 384 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\Bytes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */