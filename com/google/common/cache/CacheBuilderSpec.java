/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class CacheBuilderSpec
/*     */ {
/*  90 */   private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
/*     */ 
/*     */   
/*  93 */   private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
/*     */ 
/*     */   
/*  96 */   private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK)).put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT)).put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
/*     */   
/*     */   @VisibleForTesting
/*     */   Integer initialCapacity;
/*     */   
/*     */   @VisibleForTesting
/*     */   Long maximumSize;
/*     */   
/*     */   @VisibleForTesting
/*     */   Long maximumWeight;
/*     */   @VisibleForTesting
/*     */   Integer concurrencyLevel;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength keyStrength;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength valueStrength;
/*     */   @VisibleForTesting
/*     */   Boolean recordStats;
/*     */   @VisibleForTesting
/*     */   long writeExpirationDuration;
/*     */   @VisibleForTesting
/*     */   TimeUnit writeExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long accessExpirationDuration;
/*     */   @VisibleForTesting
/*     */   TimeUnit accessExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long refreshDuration;
/*     */   @VisibleForTesting
/*     */   TimeUnit refreshTimeUnit;
/*     */   private final String specification;
/*     */   
/*     */   private CacheBuilderSpec(String specification) {
/* 129 */     this.specification = specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
/* 138 */     CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
/* 139 */     if (!cacheBuilderSpecification.isEmpty()) {
/* 140 */       for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
/* 141 */         ImmutableList<String> immutableList = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
/* 142 */         Preconditions.checkArgument(!immutableList.isEmpty(), "blank key-value pair");
/* 143 */         Preconditions.checkArgument((immutableList.size() <= 2), "key-value pair %s with more than one equals sign", new Object[] { keyValuePair });
/*     */ 
/*     */ 
/*     */         
/* 147 */         String key = immutableList.get(0);
/* 148 */         ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
/* 149 */         Preconditions.checkArgument((valueParser != null), "unknown key %s", new Object[] { key });
/*     */         
/* 151 */         String value = (immutableList.size() == 1) ? null : immutableList.get(1);
/* 152 */         valueParser.parse(spec, key, value);
/*     */       } 
/*     */     }
/*     */     
/* 156 */     return spec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheBuilderSpec disableCaching() {
/* 164 */     return parse("maximumSize=0");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CacheBuilder<Object, Object> toCacheBuilder() {
/* 171 */     CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
/* 172 */     if (this.initialCapacity != null) {
/* 173 */       builder.initialCapacity(this.initialCapacity.intValue());
/*     */     }
/* 175 */     if (this.maximumSize != null) {
/* 176 */       builder.maximumSize(this.maximumSize.longValue());
/*     */     }
/* 178 */     if (this.maximumWeight != null) {
/* 179 */       builder.maximumWeight(this.maximumWeight.longValue());
/*     */     }
/* 181 */     if (this.concurrencyLevel != null) {
/* 182 */       builder.concurrencyLevel(this.concurrencyLevel.intValue());
/*     */     }
/* 184 */     if (this.keyStrength != null) {
/* 185 */       switch (this.keyStrength) {
/*     */         case WEAK:
/* 187 */           builder.weakKeys();
/*     */           break;
/*     */         default:
/* 190 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 193 */     if (this.valueStrength != null) {
/* 194 */       switch (this.valueStrength) {
/*     */         case SOFT:
/* 196 */           builder.softValues();
/*     */           break;
/*     */         case WEAK:
/* 199 */           builder.weakValues();
/*     */           break;
/*     */         default:
/* 202 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 205 */     if (this.recordStats != null && this.recordStats.booleanValue()) {
/* 206 */       builder.recordStats();
/*     */     }
/* 208 */     if (this.writeExpirationTimeUnit != null) {
/* 209 */       builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
/*     */     }
/* 211 */     if (this.accessExpirationTimeUnit != null) {
/* 212 */       builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
/*     */     }
/* 214 */     if (this.refreshTimeUnit != null) {
/* 215 */       builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
/*     */     }
/*     */     
/* 218 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toParsableString() {
/* 228 */     return this.specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 237 */     return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 242 */     return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
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
/*     */   public boolean equals(@Nullable Object obj) {
/* 257 */     if (this == obj) {
/* 258 */       return true;
/*     */     }
/* 260 */     if (!(obj instanceof CacheBuilderSpec)) {
/* 261 */       return false;
/*     */     }
/* 263 */     CacheBuilderSpec that = (CacheBuilderSpec)obj;
/* 264 */     return (Objects.equal(this.initialCapacity, that.initialCapacity) && Objects.equal(this.maximumSize, that.maximumSize) && Objects.equal(this.maximumWeight, that.maximumWeight) && Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && Objects.equal(this.keyStrength, that.keyStrength) && Objects.equal(this.valueStrength, that.valueStrength) && Objects.equal(this.recordStats, that.recordStats) && Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(that.refreshDuration, that.refreshTimeUnit)));
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
/*     */   @Nullable
/*     */   private static Long durationInNanos(long duration, @Nullable TimeUnit unit) {
/* 284 */     return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
/*     */   }
/*     */   
/*     */   static abstract class IntegerParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseInteger(CacheBuilderSpec param1CacheBuilderSpec, int param1Int);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 293 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", new Object[] { key });
/*     */       try {
/* 295 */         parseInteger(spec, Integer.parseInt(value));
/* 296 */       } catch (NumberFormatException e) {
/* 297 */         throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class LongParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseLong(CacheBuilderSpec param1CacheBuilderSpec, long param1Long);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 309 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", new Object[] { key });
/*     */       try {
/* 311 */         parseLong(spec, Long.parseLong(value));
/* 312 */       } catch (NumberFormatException e) {
/* 313 */         throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InitialCapacityParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 323 */       Preconditions.checkArgument((spec.initialCapacity == null), "initial capacity was already set to ", new Object[] { spec.initialCapacity });
/*     */       
/* 325 */       spec.initialCapacity = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumSizeParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 333 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", new Object[] { spec.maximumSize });
/*     */       
/* 335 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", new Object[] { spec.maximumWeight });
/*     */       
/* 337 */       spec.maximumSize = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumWeightParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 345 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", new Object[] { spec.maximumWeight });
/*     */       
/* 347 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", new Object[] { spec.maximumSize });
/*     */       
/* 349 */       spec.maximumWeight = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConcurrencyLevelParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 357 */       Preconditions.checkArgument((spec.concurrencyLevel == null), "concurrency level was already set to ", new Object[] { spec.concurrencyLevel });
/*     */       
/* 359 */       spec.concurrencyLevel = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public KeyStrengthParser(LocalCache.Strength strength) {
/* 368 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 373 */       Preconditions.checkArgument((value == null), "key %s does not take values", new Object[] { key });
/* 374 */       Preconditions.checkArgument((spec.keyStrength == null), "%s was already set to %s", new Object[] { key, spec.keyStrength });
/* 375 */       spec.keyStrength = this.strength;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ValueStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public ValueStrengthParser(LocalCache.Strength strength) {
/* 384 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 389 */       Preconditions.checkArgument((value == null), "key %s does not take values", new Object[] { key });
/* 390 */       Preconditions.checkArgument((spec.valueStrength == null), "%s was already set to %s", new Object[] { key, spec.valueStrength });
/*     */ 
/*     */       
/* 393 */       spec.valueStrength = this.strength;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecordStatsParser
/*     */     implements ValueParser
/*     */   {
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 402 */       Preconditions.checkArgument((value == null), "recordStats does not take values");
/* 403 */       Preconditions.checkArgument((spec.recordStats == null), "recordStats already set");
/* 404 */       spec.recordStats = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class DurationParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseDuration(CacheBuilderSpec param1CacheBuilderSpec, long param1Long, TimeUnit param1TimeUnit);
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 417 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", new Object[] { key }); try {
/*     */         TimeUnit timeUnit;
/* 419 */         char lastChar = value.charAt(value.length() - 1);
/*     */         
/* 421 */         switch (lastChar) {
/*     */           case 'd':
/* 423 */             timeUnit = TimeUnit.DAYS;
/*     */             break;
/*     */           case 'h':
/* 426 */             timeUnit = TimeUnit.HOURS;
/*     */             break;
/*     */           case 'm':
/* 429 */             timeUnit = TimeUnit.MINUTES;
/*     */             break;
/*     */           case 's':
/* 432 */             timeUnit = TimeUnit.SECONDS;
/*     */             break;
/*     */           default:
/* 435 */             throw new IllegalArgumentException(String.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", new Object[] { key, value }));
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 440 */         long duration = Long.parseLong(value.substring(0, value.length() - 1));
/* 441 */         parseDuration(spec, duration, timeUnit);
/* 442 */       } catch (NumberFormatException e) {
/* 443 */         throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[] { key, value }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AccessDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 452 */       Preconditions.checkArgument((spec.accessExpirationTimeUnit == null), "expireAfterAccess already set");
/* 453 */       spec.accessExpirationDuration = duration;
/* 454 */       spec.accessExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class WriteDurationParser
/*     */     extends DurationParser {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 461 */       Preconditions.checkArgument((spec.writeExpirationTimeUnit == null), "expireAfterWrite already set");
/* 462 */       spec.writeExpirationDuration = duration;
/* 463 */       spec.writeExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RefreshDurationParser
/*     */     extends DurationParser {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 470 */       Preconditions.checkArgument((spec.refreshTimeUnit == null), "refreshAfterWrite already set");
/* 471 */       spec.refreshDuration = duration;
/* 472 */       spec.refreshTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface ValueParser {
/*     */     void parse(CacheBuilderSpec param1CacheBuilderSpec, String param1String1, @Nullable String param1String2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\cache\CacheBuilderSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */