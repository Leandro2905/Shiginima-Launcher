/*     */ package com.google.gson;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonArray
/*     */   extends JsonElement
/*     */   implements Iterable<JsonElement>
/*     */ {
/*  40 */   private final List<JsonElement> elements = new ArrayList<JsonElement>();
/*     */ 
/*     */ 
/*     */   
/*     */   JsonArray deepCopy() {
/*  45 */     JsonArray result = new JsonArray();
/*  46 */     for (JsonElement element : this.elements) {
/*  47 */       result.add(element.deepCopy());
/*     */     }
/*  49 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(JsonElement element) {
/*  58 */     if (element == null) {
/*  59 */       element = JsonNull.INSTANCE;
/*     */     }
/*  61 */     this.elements.add(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(JsonArray array) {
/*  70 */     this.elements.addAll(array.elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  79 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JsonElement> iterator() {
/*  89 */     return this.elements.iterator();
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
/*     */   public JsonElement get(int i) {
/* 101 */     return this.elements.get(i);
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
/*     */   public Number getAsNumber() {
/* 114 */     if (this.elements.size() == 1) {
/* 115 */       return ((JsonElement)this.elements.get(0)).getAsNumber();
/*     */     }
/* 117 */     throw new IllegalStateException();
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
/*     */   public String getAsString() {
/* 130 */     if (this.elements.size() == 1) {
/* 131 */       return ((JsonElement)this.elements.get(0)).getAsString();
/*     */     }
/* 133 */     throw new IllegalStateException();
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
/*     */   public double getAsDouble() {
/* 146 */     if (this.elements.size() == 1) {
/* 147 */       return ((JsonElement)this.elements.get(0)).getAsDouble();
/*     */     }
/* 149 */     throw new IllegalStateException();
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
/*     */   public BigDecimal getAsBigDecimal() {
/* 163 */     if (this.elements.size() == 1) {
/* 164 */       return ((JsonElement)this.elements.get(0)).getAsBigDecimal();
/*     */     }
/* 166 */     throw new IllegalStateException();
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
/*     */   public BigInteger getAsBigInteger() {
/* 180 */     if (this.elements.size() == 1) {
/* 181 */       return ((JsonElement)this.elements.get(0)).getAsBigInteger();
/*     */     }
/* 183 */     throw new IllegalStateException();
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
/*     */   public float getAsFloat() {
/* 196 */     if (this.elements.size() == 1) {
/* 197 */       return ((JsonElement)this.elements.get(0)).getAsFloat();
/*     */     }
/* 199 */     throw new IllegalStateException();
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
/*     */   public long getAsLong() {
/* 212 */     if (this.elements.size() == 1) {
/* 213 */       return ((JsonElement)this.elements.get(0)).getAsLong();
/*     */     }
/* 215 */     throw new IllegalStateException();
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
/*     */   public int getAsInt() {
/* 228 */     if (this.elements.size() == 1) {
/* 229 */       return ((JsonElement)this.elements.get(0)).getAsInt();
/*     */     }
/* 231 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 236 */     if (this.elements.size() == 1) {
/* 237 */       return ((JsonElement)this.elements.get(0)).getAsByte();
/*     */     }
/* 239 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public char getAsCharacter() {
/* 244 */     if (this.elements.size() == 1) {
/* 245 */       return ((JsonElement)this.elements.get(0)).getAsCharacter();
/*     */     }
/* 247 */     throw new IllegalStateException();
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
/*     */   public short getAsShort() {
/* 260 */     if (this.elements.size() == 1) {
/* 261 */       return ((JsonElement)this.elements.get(0)).getAsShort();
/*     */     }
/* 263 */     throw new IllegalStateException();
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
/*     */   public boolean getAsBoolean() {
/* 276 */     if (this.elements.size() == 1) {
/* 277 */       return ((JsonElement)this.elements.get(0)).getAsBoolean();
/*     */     }
/* 279 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 284 */     return (o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 289 */     return this.elements.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\JsonArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */