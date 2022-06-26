/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.;
/*     */ import com.google.gson.internal.LazilyParsedNumber;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonPrimitive
/*     */   extends JsonElement
/*     */ {
/*  35 */   private static final Class<?>[] PRIMITIVE_TYPES = new Class[] { int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(Boolean bool) {
/*  47 */     setValue(bool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(Number number) {
/*  56 */     setValue(number);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(String string) {
/*  65 */     setValue(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(Character c) {
/*  75 */     setValue(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JsonPrimitive(Object primitive) {
/*  85 */     setValue(primitive);
/*     */   }
/*     */ 
/*     */   
/*     */   JsonPrimitive deepCopy() {
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   void setValue(Object primitive) {
/*  94 */     if (primitive instanceof Character) {
/*     */ 
/*     */       
/*  97 */       char c = ((Character)primitive).charValue();
/*  98 */       this.value = String.valueOf(c);
/*     */     } else {
/* 100 */       .Gson.Preconditions.checkArgument((primitive instanceof Number || isPrimitiveOrString(primitive)));
/*     */       
/* 102 */       this.value = primitive;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBoolean() {
/* 112 */     return this.value instanceof Boolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Boolean getAsBooleanWrapper() {
/* 122 */     return (Boolean)this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAsBoolean() {
/* 132 */     if (isBoolean()) {
/* 133 */       return getAsBooleanWrapper().booleanValue();
/*     */     }
/*     */     
/* 136 */     return Boolean.parseBoolean(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNumber() {
/* 146 */     return this.value instanceof Number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 157 */     return (this.value instanceof String) ? (Number)new LazilyParsedNumber((String)this.value) : (Number)this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isString() {
/* 166 */     return this.value instanceof String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsString() {
/* 176 */     if (isNumber())
/* 177 */       return getAsNumber().toString(); 
/* 178 */     if (isBoolean()) {
/* 179 */       return getAsBooleanWrapper().toString();
/*     */     }
/* 181 */     return (String)this.value;
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
/*     */   public double getAsDouble() {
/* 193 */     return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getAsBigDecimal() {
/* 204 */     return (this.value instanceof BigDecimal) ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getAsBigInteger() {
/* 215 */     return (this.value instanceof BigInteger) ? (BigInteger)this.value : new BigInteger(this.value.toString());
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
/*     */   public float getAsFloat() {
/* 227 */     return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 238 */     return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 249 */     return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 260 */     return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 265 */     return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
/*     */   }
/*     */ 
/*     */   
/*     */   public char getAsCharacter() {
/* 270 */     return getAsString().charAt(0);
/*     */   }
/*     */   
/*     */   private static boolean isPrimitiveOrString(Object target) {
/* 274 */     if (target instanceof String) {
/* 275 */       return true;
/*     */     }
/*     */     
/* 278 */     Class<?> classOfPrimitive = target.getClass();
/* 279 */     for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
/* 280 */       if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
/* 281 */         return true;
/*     */       }
/*     */     } 
/* 284 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 289 */     if (this.value == null) {
/* 290 */       return 31;
/*     */     }
/*     */     
/* 293 */     if (isIntegral(this)) {
/* 294 */       long value = getAsNumber().longValue();
/* 295 */       return (int)(value ^ value >>> 32L);
/*     */     } 
/* 297 */     if (this.value instanceof Number) {
/* 298 */       long value = Double.doubleToLongBits(getAsNumber().doubleValue());
/* 299 */       return (int)(value ^ value >>> 32L);
/*     */     } 
/* 301 */     return this.value.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 306 */     if (this == obj) {
/* 307 */       return true;
/*     */     }
/* 309 */     if (obj == null || getClass() != obj.getClass()) {
/* 310 */       return false;
/*     */     }
/* 312 */     JsonPrimitive other = (JsonPrimitive)obj;
/* 313 */     if (this.value == null) {
/* 314 */       return (other.value == null);
/*     */     }
/* 316 */     if (isIntegral(this) && isIntegral(other)) {
/* 317 */       return (getAsNumber().longValue() == other.getAsNumber().longValue());
/*     */     }
/* 319 */     if (this.value instanceof Number && other.value instanceof Number) {
/* 320 */       double a = getAsNumber().doubleValue();
/*     */ 
/*     */       
/* 323 */       double b = other.getAsNumber().doubleValue();
/* 324 */       return (a == b || (Double.isNaN(a) && Double.isNaN(b)));
/*     */     } 
/* 326 */     return this.value.equals(other.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isIntegral(JsonPrimitive primitive) {
/* 334 */     if (primitive.value instanceof Number) {
/* 335 */       Number number = (Number)primitive.value;
/* 336 */       return (number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte);
/*     */     } 
/*     */     
/* 339 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\JsonPrimitive.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */