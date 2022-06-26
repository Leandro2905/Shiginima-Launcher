/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtendedClassInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final boolean exact;
/*     */   private final String location;
/*     */   private final String version;
/*     */   
/*     */   public ExtendedClassInfo(boolean exact, String location, String version) {
/*  43 */     this.exact = exact;
/*  44 */     this.location = location;
/*  45 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  50 */     if (this == obj) {
/*  51 */       return true;
/*     */     }
/*  53 */     if (obj == null) {
/*  54 */       return false;
/*     */     }
/*  56 */     if (!(obj instanceof ExtendedClassInfo)) {
/*  57 */       return false;
/*     */     }
/*  59 */     ExtendedClassInfo other = (ExtendedClassInfo)obj;
/*  60 */     if (this.exact != other.exact) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (this.location == null) {
/*  64 */       if (other.location != null) {
/*  65 */         return false;
/*     */       }
/*  67 */     } else if (!this.location.equals(other.location)) {
/*  68 */       return false;
/*     */     } 
/*  70 */     if (this.version == null) {
/*  71 */       if (other.version != null) {
/*  72 */         return false;
/*     */       }
/*  74 */     } else if (!this.version.equals(other.version)) {
/*  75 */       return false;
/*     */     } 
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  81 */     return this.exact;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  85 */     return this.location;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  89 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     int prime = 31;
/*  95 */     int result = 1;
/*  96 */     result = 31 * result + (this.exact ? 1231 : 1237);
/*  97 */     result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
/*  98 */     result = 31 * result + ((this.version == null) ? 0 : this.version.hashCode());
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     StringBuilder sb = new StringBuilder();
/* 105 */     if (!this.exact) {
/* 106 */       sb.append('~');
/*     */     }
/* 108 */     sb.append('[');
/* 109 */     sb.append(this.location);
/* 110 */     sb.append(':');
/* 111 */     sb.append(this.version);
/* 112 */     sb.append(']');
/* 113 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\ExtendedClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */