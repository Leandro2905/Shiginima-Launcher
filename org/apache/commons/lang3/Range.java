/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Range<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Comparator<T> comparator;
/*     */   private final T minimum;
/*     */   private final T maximum;
/*     */   private transient int hashCode;
/*     */   private transient String toString;
/*     */   
/*     */   public static <T extends Comparable<T>> Range<T> is(T element) {
/*  76 */     return between(element, element, null);
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
/*     */   public static <T> Range<T> is(T element, Comparator<T> comparator) {
/*  94 */     return between(element, element, comparator);
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
/*     */   public static <T extends Comparable<T>> Range<T> between(T fromInclusive, T toInclusive) {
/* 114 */     return between(fromInclusive, toInclusive, null);
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
/*     */   public static <T> Range<T> between(T fromInclusive, T toInclusive, Comparator<T> comparator) {
/* 135 */     return new Range<T>(fromInclusive, toInclusive, comparator);
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
/*     */   private Range(T element1, T element2, Comparator<T> comp) {
/* 147 */     if (element1 == null || element2 == null) {
/* 148 */       throw new IllegalArgumentException("Elements in a range must not be null: element1=" + element1 + ", element2=" + element2);
/*     */     }
/*     */     
/* 151 */     if (comp == null) {
/* 152 */       this.comparator = ComparableComparator.INSTANCE;
/*     */     } else {
/* 154 */       this.comparator = comp;
/*     */     } 
/* 156 */     if (this.comparator.compare(element1, element2) < 1) {
/* 157 */       this.minimum = element1;
/* 158 */       this.maximum = element2;
/*     */     } else {
/* 160 */       this.minimum = element2;
/* 161 */       this.maximum = element1;
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
/*     */   public T getMinimum() {
/* 174 */     return this.minimum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getMaximum() {
/* 183 */     return this.maximum;
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
/*     */   public Comparator<T> getComparator() {
/* 195 */     return this.comparator;
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
/*     */   public boolean isNaturalOrdering() {
/* 207 */     return (this.comparator == ComparableComparator.INSTANCE);
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
/*     */   public boolean contains(T element) {
/* 220 */     if (element == null) {
/* 221 */       return false;
/*     */     }
/* 223 */     return (this.comparator.compare(element, this.minimum) > -1 && this.comparator.compare(element, this.maximum) < 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAfter(T element) {
/* 233 */     if (element == null) {
/* 234 */       return false;
/*     */     }
/* 236 */     return (this.comparator.compare(element, this.minimum) < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStartedBy(T element) {
/* 246 */     if (element == null) {
/* 247 */       return false;
/*     */     }
/* 249 */     return (this.comparator.compare(element, this.minimum) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEndedBy(T element) {
/* 259 */     if (element == null) {
/* 260 */       return false;
/*     */     }
/* 262 */     return (this.comparator.compare(element, this.maximum) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBefore(T element) {
/* 272 */     if (element == null) {
/* 273 */       return false;
/*     */     }
/* 275 */     return (this.comparator.compare(element, this.maximum) > 0);
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
/*     */   public int elementCompareTo(T element) {
/* 289 */     if (element == null)
/*     */     {
/* 291 */       throw new NullPointerException("Element is null");
/*     */     }
/* 293 */     if (isAfter(element))
/* 294 */       return -1; 
/* 295 */     if (isBefore(element)) {
/* 296 */       return 1;
/*     */     }
/* 298 */     return 0;
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
/*     */   public boolean containsRange(Range<T> otherRange) {
/* 315 */     if (otherRange == null) {
/* 316 */       return false;
/*     */     }
/* 318 */     return (contains(otherRange.minimum) && contains(otherRange.maximum));
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
/*     */   public boolean isAfterRange(Range<T> otherRange) {
/* 332 */     if (otherRange == null) {
/* 333 */       return false;
/*     */     }
/* 335 */     return isAfter(otherRange.maximum);
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
/*     */   public boolean isOverlappedBy(Range<T> otherRange) {
/* 351 */     if (otherRange == null) {
/* 352 */       return false;
/*     */     }
/* 354 */     return (otherRange.contains(this.minimum) || otherRange.contains(this.maximum) || contains(otherRange.minimum));
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
/*     */   public boolean isBeforeRange(Range<T> otherRange) {
/* 369 */     if (otherRange == null) {
/* 370 */       return false;
/*     */     }
/* 372 */     return isBefore(otherRange.minimum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<T> intersectionWith(Range<T> other) {
/* 383 */     if (!isOverlappedBy(other)) {
/* 384 */       throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[] { other }));
/*     */     }
/*     */     
/* 387 */     if (equals(other)) {
/* 388 */       return this;
/*     */     }
/* 390 */     T min = (getComparator().compare(this.minimum, other.minimum) < 0) ? other.minimum : this.minimum;
/* 391 */     T max = (getComparator().compare(this.maximum, other.maximum) < 0) ? this.maximum : other.maximum;
/* 392 */     return between(min, max, getComparator());
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
/*     */   public boolean equals(Object obj) {
/* 409 */     if (obj == this)
/* 410 */       return true; 
/* 411 */     if (obj == null || obj.getClass() != getClass()) {
/* 412 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 416 */     Range<T> range = (Range<T>)obj;
/* 417 */     return (this.minimum.equals(range.minimum) && this.maximum.equals(range.maximum));
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
/*     */   public int hashCode() {
/* 429 */     int result = this.hashCode;
/* 430 */     if (this.hashCode == 0) {
/* 431 */       result = 17;
/* 432 */       result = 37 * result + getClass().hashCode();
/* 433 */       result = 37 * result + this.minimum.hashCode();
/* 434 */       result = 37 * result + this.maximum.hashCode();
/* 435 */       this.hashCode = result;
/*     */     } 
/* 437 */     return result;
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
/*     */   public String toString() {
/* 449 */     String result = this.toString;
/* 450 */     if (result == null) {
/* 451 */       StringBuilder buf = new StringBuilder(32);
/* 452 */       buf.append('[');
/* 453 */       buf.append(this.minimum);
/* 454 */       buf.append("..");
/* 455 */       buf.append(this.maximum);
/* 456 */       buf.append(']');
/* 457 */       result = buf.toString();
/* 458 */       this.toString = result;
/*     */     } 
/* 460 */     return result;
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
/*     */   public String toString(String format) {
/* 476 */     return String.format(format, new Object[] { this.minimum, this.maximum, this.comparator });
/*     */   }
/*     */   
/*     */   private enum ComparableComparator
/*     */     implements Comparator
/*     */   {
/* 482 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Object obj1, Object obj2) {
/* 492 */       return ((Comparable<Object>)obj1).compareTo(obj2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\Range.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */