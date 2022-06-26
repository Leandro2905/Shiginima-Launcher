/*     */ package org.jivesoftware.smack.util.dns;
/*     */ 
/*     */ import org.jivesoftware.smack.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HostAddress
/*     */ {
/*     */   private final String fqdn;
/*     */   private final int port;
/*     */   private Exception exception;
/*     */   
/*     */   public HostAddress(String fqdn) {
/*  35 */     this(fqdn, 5222);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostAddress(String fqdn, int port) {
/*  46 */     Objects.requireNonNull(fqdn, "FQDN is null");
/*  47 */     if (port < 0 || port > 65535) {
/*  48 */       throw new IllegalArgumentException("Port must be a 16-bit unsiged integer (i.e. between 0-65535. Port was: " + port);
/*     */     }
/*  50 */     if (fqdn.charAt(fqdn.length() - 1) == '.') {
/*  51 */       this.fqdn = fqdn.substring(0, fqdn.length() - 1);
/*     */     } else {
/*     */       
/*  54 */       this.fqdn = fqdn;
/*     */     } 
/*  56 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getFQDN() {
/*  60 */     return this.fqdn;
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  64 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setException(Exception e) {
/*  68 */     this.exception = e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getException() {
/*  79 */     return this.exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return this.fqdn + ":" + this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  89 */     if (this == o) {
/*  90 */       return true;
/*     */     }
/*  92 */     if (!(o instanceof HostAddress)) {
/*  93 */       return false;
/*     */     }
/*     */     
/*  96 */     HostAddress address = (HostAddress)o;
/*     */     
/*  98 */     if (!this.fqdn.equals(address.fqdn)) {
/*  99 */       return false;
/*     */     }
/* 101 */     return (this.port == address.port);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 106 */     int result = 1;
/* 107 */     result = 37 * result + this.fqdn.hashCode();
/* 108 */     return result * 37 + this.port;
/*     */   }
/*     */   
/*     */   public String getErrorMessage() {
/* 112 */     if (this.exception == null) {
/* 113 */       return "No error logged";
/*     */     }
/* 115 */     return "'" + toString() + "' failed because " + this.exception.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\dns\HostAddress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */