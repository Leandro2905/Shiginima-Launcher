/*     */ package org.apache.logging.log4j.core.jackson;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/*     */ import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JsonPropertyOrder({"key", "value"})
/*     */ final class MapEntry
/*     */ {
/*     */   @JsonProperty
/*     */   @JacksonXmlProperty(isAttribute = true)
/*     */   private String key;
/*     */   @JsonProperty
/*     */   @JacksonXmlProperty(isAttribute = true)
/*     */   private String value;
/*     */   
/*     */   @JsonCreator
/*     */   public MapEntry(@JsonProperty("key") String key, @JsonProperty("value") String value) {
/*  48 */     setKey(key);
/*  49 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  54 */     if (this == obj) {
/*  55 */       return true;
/*     */     }
/*  57 */     if (obj == null) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (!(obj instanceof MapEntry)) {
/*  61 */       return false;
/*     */     }
/*  63 */     MapEntry other = (MapEntry)obj;
/*  64 */     if (getKey() == null) {
/*  65 */       if (other.getKey() != null) {
/*  66 */         return false;
/*     */       }
/*  68 */     } else if (!getKey().equals(other.getKey())) {
/*  69 */       return false;
/*     */     } 
/*  71 */     if (getValue() == null) {
/*  72 */       if (other.getValue() != null) {
/*  73 */         return false;
/*     */       }
/*  75 */     } else if (!getValue().equals(other.getValue())) {
/*  76 */       return false;
/*     */     } 
/*  78 */     return true;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  82 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  86 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     int prime = 31;
/*  92 */     int result = 1;
/*  93 */     result = 31 * result + ((getKey() == null) ? 0 : getKey().hashCode());
/*  94 */     result = 31 * result + ((getValue() == null) ? 0 : getValue().hashCode());
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  99 */     this.key = key;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 103 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return "" + getKey() + "=" + getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\MapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */