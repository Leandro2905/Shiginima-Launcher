/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.base.Ticker;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class CacheBuilder<K, V>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*     */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*     */   
/* 158 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*     */       {
/*     */         public void recordHits(int count) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void recordMisses(int count) {}
/*     */ 
/*     */         
/*     */         public void recordLoadSuccess(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordLoadException(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordEviction() {}
/*     */ 
/*     */         
/*     */         public CacheStats snapshot() {
/* 177 */           return CacheBuilder.EMPTY_STATS;
/*     */         }
/*     */       });
/* 180 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*     */   
/* 182 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*     */     {
/*     */       public AbstractCache.StatsCounter get()
/*     */       {
/* 186 */         return new AbstractCache.SimpleStatsCounter();
/*     */       }
/*     */     };
/*     */   
/*     */   enum NullListener implements RemovalListener<Object, Object> {
/* 191 */     INSTANCE;
/*     */     
/*     */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*     */   }
/*     */   
/*     */   enum OneWeigher
/*     */     implements Weigher<Object, Object> {
/* 198 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int weigh(Object key, Object value) {
/* 202 */       return 1;
/*     */     }
/*     */   }
/*     */   
/* 206 */   static final Ticker NULL_TICKER = new Ticker()
/*     */     {
/*     */       public long read() {
/* 209 */         return 0L;
/*     */       }
/*     */     };
/*     */   
/* 213 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*     */   
/*     */   static final int UNSET_INT = -1;
/*     */   
/*     */   boolean strictParsing = true;
/*     */   
/* 219 */   int initialCapacity = -1;
/* 220 */   int concurrencyLevel = -1;
/* 221 */   long maximumSize = -1L;
/* 222 */   long maximumWeight = -1L;
/*     */   
/*     */   Weigher<? super K, ? super V> weigher;
/*     */   
/*     */   LocalCache.Strength keyStrength;
/*     */   LocalCache.Strength valueStrength;
/* 228 */   long expireAfterWriteNanos = -1L;
/* 229 */   long expireAfterAccessNanos = -1L;
/* 230 */   long refreshNanos = -1L;
/*     */   
/*     */   Equivalence<Object> keyEquivalence;
/*     */   
/*     */   Equivalence<Object> valueEquivalence;
/*     */   
/*     */   RemovalListener<? super K, ? super V> removalListener;
/*     */   Ticker ticker;
/* 238 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheBuilder<Object, Object> newBuilder() {
/* 248 */     return new CacheBuilder<Object, Object>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported")
/*     */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
/* 259 */     return spec.toCacheBuilder().lenientParsing();
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
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported")
/*     */   public static CacheBuilder<Object, Object> from(String spec) {
/* 273 */     return from(CacheBuilderSpec.parse(spec));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> lenientParsing() {
/* 281 */     this.strictParsing = false;
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/* 293 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", new Object[] { this.keyEquivalence });
/* 294 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 295 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 299 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/* 311 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", new Object[] { this.valueEquivalence });
/*     */     
/* 313 */     this.valueEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getValueEquivalence() {
/* 318 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*     */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/* 332 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", new Object[] { Integer.valueOf(this.initialCapacity) });
/*     */     
/* 334 */     Preconditions.checkArgument((initialCapacity >= 0));
/* 335 */     this.initialCapacity = initialCapacity;
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 340 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/* 374 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", new Object[] { Integer.valueOf(this.concurrencyLevel) });
/*     */     
/* 376 */     Preconditions.checkArgument((concurrencyLevel > 0));
/* 377 */     this.concurrencyLevel = concurrencyLevel;
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 382 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*     */   public CacheBuilder<K, V> maximumSize(long size) {
/* 401 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     
/* 403 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", new Object[] { Long.valueOf(this.maximumWeight) });
/*     */     
/* 405 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/* 406 */     Preconditions.checkArgument((size >= 0L), "maximum size must not be negative");
/* 407 */     this.maximumSize = size;
/* 408 */     return this;
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
/*     */   @GwtIncompatible("To be supported")
/*     */   public CacheBuilder<K, V> maximumWeight(long weight) {
/* 437 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", new Object[] { Long.valueOf(this.maximumWeight) });
/*     */     
/* 439 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     
/* 441 */     this.maximumWeight = weight;
/* 442 */     Preconditions.checkArgument((weight >= 0L), "maximum weight must not be negative");
/* 443 */     return this;
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
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/* 477 */     Preconditions.checkState((this.weigher == null));
/* 478 */     if (this.strictParsing) {
/* 479 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 485 */     CacheBuilder<K1, V1> me = this;
/* 486 */     me.weigher = (Weigher<? super K, ? super V>)Preconditions.checkNotNull(weigher);
/* 487 */     return me;
/*     */   }
/*     */   
/*     */   long getMaximumWeight() {
/* 491 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/* 492 */       return 0L;
/*     */     }
/* 494 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
/* 500 */     return (Weigher<K1, V1>)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public CacheBuilder<K, V> weakKeys() {
/* 518 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/* 522 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", new Object[] { this.keyStrength });
/* 523 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 524 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getKeyStrength() {
/* 528 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public CacheBuilder<K, V> weakValues() {
/* 549 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*     */   @GwtIncompatible("java.lang.ref.SoftReference")
/*     */   public CacheBuilder<K, V> softValues() {
/* 573 */     return setValueStrength(LocalCache.Strength.SOFT);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/* 577 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", new Object[] { this.valueStrength });
/* 578 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 579 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getValueStrength() {
/* 583 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*     */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/* 606 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterWriteNanos) });
/*     */     
/* 608 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 609 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/* 610 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterWriteNanos() {
/* 614 */     return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
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
/*     */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/* 640 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterAccessNanos) });
/*     */     
/* 642 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 643 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/* 644 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterAccessNanos() {
/* 648 */     return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
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
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported (synchronously).")
/*     */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/* 680 */     Preconditions.checkNotNull(unit);
/* 681 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", new Object[] { Long.valueOf(this.refreshNanos) });
/* 682 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 683 */     this.refreshNanos = unit.toNanos(duration);
/* 684 */     return this;
/*     */   }
/*     */   
/*     */   long getRefreshNanos() {
/* 688 */     return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
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
/*     */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/* 701 */     Preconditions.checkState((this.ticker == null));
/* 702 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/* 703 */     return this;
/*     */   }
/*     */   
/*     */   Ticker getTicker(boolean recordsTime) {
/* 707 */     if (this.ticker != null) {
/* 708 */       return this.ticker;
/*     */     }
/* 710 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*     */   @CheckReturnValue
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/* 737 */     Preconditions.checkState((this.removalListener == null));
/*     */ 
/*     */ 
/*     */     
/* 741 */     CacheBuilder<K1, V1> me = this;
/* 742 */     me.removalListener = (RemovalListener<? super K, ? super V>)Preconditions.checkNotNull(listener);
/* 743 */     return me;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
/* 749 */     return (RemovalListener<K1, V1>)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*     */   public CacheBuilder<K, V> recordStats() {
/* 762 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/* 763 */     return this;
/*     */   }
/*     */   
/*     */   boolean isRecordingStats() {
/* 767 */     return (this.statsCounterSupplier == CACHE_STATS_COUNTER);
/*     */   }
/*     */   
/*     */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/* 771 */     return this.statsCounterSupplier;
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
/*     */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/* 788 */     checkWeightWithWeigher();
/* 789 */     return new LocalCache.LocalLoadingCache<K1, V1>(this, loader);
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
/*     */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/* 805 */     checkWeightWithWeigher();
/* 806 */     checkNonLoadingCache();
/* 807 */     return new LocalCache.LocalManualCache<K1, V1>(this);
/*     */   }
/*     */   
/*     */   private void checkNonLoadingCache() {
/* 811 */     Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache");
/*     */   }
/*     */   
/*     */   private void checkWeightWithWeigher() {
/* 815 */     if (this.weigher == null) {
/* 816 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*     */     }
/* 818 */     else if (this.strictParsing) {
/* 819 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*     */     }
/* 821 */     else if (this.maximumWeight == -1L) {
/* 822 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
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
/*     */   public String toString() {
/* 834 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 835 */     if (this.initialCapacity != -1) {
/* 836 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 838 */     if (this.concurrencyLevel != -1) {
/* 839 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 841 */     if (this.maximumSize != -1L) {
/* 842 */       s.add("maximumSize", this.maximumSize);
/*     */     }
/* 844 */     if (this.maximumWeight != -1L) {
/* 845 */       s.add("maximumWeight", this.maximumWeight);
/*     */     }
/* 847 */     if (this.expireAfterWriteNanos != -1L) {
/* 848 */       long l = this.expireAfterWriteNanos; s.add("expireAfterWrite", (new StringBuilder(22)).append(l).append("ns").toString());
/*     */     } 
/* 850 */     if (this.expireAfterAccessNanos != -1L) {
/* 851 */       long l = this.expireAfterAccessNanos; s.add("expireAfterAccess", (new StringBuilder(22)).append(l).append("ns").toString());
/*     */     } 
/* 853 */     if (this.keyStrength != null) {
/* 854 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 856 */     if (this.valueStrength != null) {
/* 857 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 859 */     if (this.keyEquivalence != null) {
/* 860 */       s.addValue("keyEquivalence");
/*     */     }
/* 862 */     if (this.valueEquivalence != null) {
/* 863 */       s.addValue("valueEquivalence");
/*     */     }
/* 865 */     if (this.removalListener != null) {
/* 866 */       s.addValue("removalListener");
/*     */     }
/* 868 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */