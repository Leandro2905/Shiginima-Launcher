/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   @Nullable
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   @Nullable
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   private transient GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  46 */     C c1 = range.hasLowerBound() ? (C)range.lowerEndpoint() : null;
/*  47 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */ 
/*     */     
/*  50 */     C c2 = range.hasUpperBound() ? (C)range.upperEndpoint() : null;
/*  51 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  52 */     return new GeneralRange<T>(Ordering.natural(), range.hasLowerBound(), (T)c1, lowerBoundType, range.hasUpperBound(), (T)c2, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
/*  60 */     return new GeneralRange<T>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
/*  69 */     return new GeneralRange<T>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
/*  78 */     return new GeneralRange<T>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType) {
/*  87 */     return new GeneralRange<T>(comparator, true, lower, lowerType, true, upper, upperType);
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
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType) {
/* 103 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
/* 104 */     this.hasLowerBound = hasLowerBound;
/* 105 */     this.hasUpperBound = hasUpperBound;
/* 106 */     this.lowerEndpoint = lowerEndpoint;
/* 107 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 108 */     this.upperEndpoint = upperEndpoint;
/* 109 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */     
/* 111 */     if (hasLowerBound) {
/* 112 */       comparator.compare(lowerEndpoint, lowerEndpoint);
/*     */     }
/* 114 */     if (hasUpperBound) {
/* 115 */       comparator.compare(upperEndpoint, upperEndpoint);
/*     */     }
/* 117 */     if (hasLowerBound && hasUpperBound) {
/* 118 */       int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
/*     */       
/* 120 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", new Object[] { lowerEndpoint, upperEndpoint });
/*     */       
/* 122 */       if (cmp == 0) {
/* 123 */         Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN)) | ((upperBoundType != BoundType.OPEN)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Comparator<? super T> comparator() {
/* 129 */     return this.comparator;
/*     */   }
/*     */   
/*     */   boolean hasLowerBound() {
/* 133 */     return this.hasLowerBound;
/*     */   }
/*     */   
/*     */   boolean hasUpperBound() {
/* 137 */     return this.hasUpperBound;
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/* 141 */     return ((hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint())));
/*     */   }
/*     */ 
/*     */   
/*     */   boolean tooLow(@Nullable T t) {
/* 146 */     if (!hasLowerBound()) {
/* 147 */       return false;
/*     */     }
/* 149 */     T lbound = getLowerEndpoint();
/* 150 */     int cmp = this.comparator.compare(t, lbound);
/* 151 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((getLowerBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean tooHigh(@Nullable T t) {
/* 155 */     if (!hasUpperBound()) {
/* 156 */       return false;
/*     */     }
/* 158 */     T ubound = getUpperEndpoint();
/* 159 */     int cmp = this.comparator.compare(t, ubound);
/* 160 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((getUpperBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean contains(@Nullable T t) {
/* 164 */     return (!tooLow(t) && !tooHigh(t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 171 */     Preconditions.checkNotNull(other);
/* 172 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 174 */     boolean hasLowBound = this.hasLowerBound;
/*     */     
/* 176 */     T lowEnd = getLowerEndpoint();
/* 177 */     BoundType lowType = getLowerBoundType();
/* 178 */     if (!hasLowerBound()) {
/* 179 */       hasLowBound = other.hasLowerBound;
/* 180 */       lowEnd = other.getLowerEndpoint();
/* 181 */       lowType = other.getLowerBoundType();
/* 182 */     } else if (other.hasLowerBound()) {
/* 183 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 184 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 185 */         lowEnd = other.getLowerEndpoint();
/* 186 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     boolean hasUpBound = this.hasUpperBound;
/*     */     
/* 192 */     T upEnd = getUpperEndpoint();
/* 193 */     BoundType upType = getUpperBoundType();
/* 194 */     if (!hasUpperBound()) {
/* 195 */       hasUpBound = other.hasUpperBound;
/* 196 */       upEnd = other.getUpperEndpoint();
/* 197 */       upType = other.getUpperBoundType();
/* 198 */     } else if (other.hasUpperBound()) {
/* 199 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 200 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 201 */         upEnd = other.getUpperEndpoint();
/* 202 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 206 */     if (hasLowBound && hasUpBound) {
/* 207 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 208 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 210 */         lowEnd = upEnd;
/* 211 */         lowType = BoundType.OPEN;
/* 212 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 221 */     if (obj instanceof GeneralRange) {
/* 222 */       GeneralRange<?> r = (GeneralRange)obj;
/* 223 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && getLowerBoundType().equals(r.getLowerBoundType()) && getUpperBoundType().equals(r.getUpperBoundType()) && Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return Objects.hashCode(new Object[] { this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 244 */     GeneralRange<T> result = this.reverse;
/* 245 */     if (result == null) {
/* 246 */       result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/*     */ 
/*     */       
/* 249 */       result.reverse = this;
/* 250 */       return this.reverse = result;
/*     */     } 
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return this.comparator + ":" + ((this.lowerBoundType == BoundType.CLOSED) ? 91 : 40) + (this.hasLowerBound ? (String)this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? (String)this.upperEndpoint : "∞") + ((this.upperBoundType == BoundType.CLOSED) ? 93 : 41);
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
/*     */   T getLowerEndpoint() {
/* 269 */     return this.lowerEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getLowerBoundType() {
/* 273 */     return this.lowerBoundType;
/*     */   }
/*     */   
/*     */   T getUpperEndpoint() {
/* 277 */     return this.upperEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getUpperBoundType() {
/* 281 */     return this.upperBoundType;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\GeneralRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */