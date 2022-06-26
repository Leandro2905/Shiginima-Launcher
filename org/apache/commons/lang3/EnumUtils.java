/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumUtils
/*     */ {
/*     */   private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
/*     */   private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
/*     */   private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
/*     */   private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
/*     */   
/*     */   public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
/*  59 */     Map<String, E> map = new LinkedHashMap<String, E>();
/*  60 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/*  61 */       map.put(enum_.name(), (E)enum_);
/*     */     }
/*  63 */     return map;
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
/*     */   public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
/*  76 */     return new ArrayList<E>(Arrays.asList(enumClass.getEnumConstants()));
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
/*     */   public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
/*  91 */     if (enumName == null) {
/*  92 */       return false;
/*     */     }
/*     */     try {
/*  95 */       Enum.valueOf(enumClass, enumName);
/*  96 */       return true;
/*  97 */     } catch (IllegalArgumentException ex) {
/*  98 */       return false;
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
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
/* 114 */     if (enumName == null) {
/* 115 */       return null;
/*     */     }
/*     */     try {
/* 118 */       return Enum.valueOf(enumClass, enumName);
/* 119 */     } catch (IllegalArgumentException ex) {
/* 120 */       return null;
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
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<? extends E> values) {
/* 143 */     checkBitVectorable(enumClass);
/* 144 */     Validate.notNull(values);
/* 145 */     long total = 0L;
/* 146 */     for (Enum enum_ : values) {
/* 147 */       Validate.isTrue((enum_ != null), "null elements not permitted", new Object[0]);
/* 148 */       total |= (1 << enum_.ordinal());
/*     */     } 
/* 150 */     return total;
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
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, Iterable<? extends E> values) {
/* 170 */     asEnum(enumClass);
/* 171 */     Validate.notNull(values);
/* 172 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 173 */     for (Enum enum_ : values) {
/* 174 */       Validate.isTrue((enum_ != null), "null elements not permitted", new Object[0]);
/* 175 */       condensed.add((E)enum_);
/*     */     } 
/* 177 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 178 */     for (Enum enum_ : condensed) {
/* 179 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | (1 << enum_.ordinal() % 64);
/*     */     }
/* 181 */     ArrayUtils.reverse(result);
/* 182 */     return result;
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
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values) {
/* 203 */     Validate.noNullElements(values);
/* 204 */     return generateBitVector(enumClass, Arrays.asList(values));
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
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, E... values) {
/* 224 */     asEnum(enumClass);
/* 225 */     Validate.noNullElements(values);
/* 226 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 227 */     Collections.addAll(condensed, values);
/* 228 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 229 */     for (Enum enum_ : condensed) {
/* 230 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | (1 << enum_.ordinal() % 64);
/*     */     }
/* 232 */     ArrayUtils.reverse(result);
/* 233 */     return result;
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
/* 250 */     checkBitVectorable(enumClass).getEnumConstants();
/* 251 */     return processBitVectors(enumClass, new long[] { value });
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> enumClass, long... values) {
/* 268 */     EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
/* 269 */     long[] lvalues = ArrayUtils.clone(Validate.<long[]>notNull(values));
/* 270 */     ArrayUtils.reverse(lvalues);
/* 271 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/* 272 */       int block = enum_.ordinal() / 64;
/* 273 */       if (block < lvalues.length && (lvalues[block] & (1 << enum_.ordinal() % 64)) != 0L) {
/* 274 */         results.add((E)enum_);
/*     */       }
/*     */     } 
/* 277 */     return results;
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
/*     */   private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
/* 290 */     Enum[] arrayOfEnum = asEnum(enumClass).getEnumConstants();
/* 291 */     Validate.isTrue((arrayOfEnum.length <= 64), "Cannot store %s %s values in %s bits", new Object[] { Integer.valueOf(arrayOfEnum.length), enumClass.getSimpleName(), Integer.valueOf(64) });
/*     */ 
/*     */     
/* 294 */     return enumClass;
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
/*     */   private static <E extends Enum<E>> Class<E> asEnum(Class<E> enumClass) {
/* 307 */     Validate.notNull(enumClass, "EnumClass must be defined.", new Object[0]);
/* 308 */     Validate.isTrue(enumClass.isEnum(), "%s does not seem to be an Enum type", new Object[] { enumClass });
/* 309 */     return enumClass;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\EnumUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */