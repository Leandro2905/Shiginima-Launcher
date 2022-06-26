/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.net.InetAddress;
/*     */ import java.text.ParseException;
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
/*     */ @Beta
/*     */ public final class HostSpecifier
/*     */ {
/*     */   private final String canonicalForm;
/*     */   
/*     */   private HostSpecifier(String canonicalForm) {
/*  57 */     this.canonicalForm = canonicalForm;
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
/*     */   public static HostSpecifier fromValid(String specifier) {
/*  78 */     HostAndPort parsedHost = HostAndPort.fromString(specifier);
/*  79 */     Preconditions.checkArgument(!parsedHost.hasPort());
/*  80 */     String host = parsedHost.getHostText();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     InetAddress addr = null;
/*     */     try {
/*  88 */       addr = InetAddresses.forString(host);
/*  89 */     } catch (IllegalArgumentException e) {}
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (addr != null) {
/*  94 */       return new HostSpecifier(InetAddresses.toUriString(addr));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     InternetDomainName domain = InternetDomainName.from(host);
/*     */     
/* 102 */     if (domain.hasPublicSuffix()) {
/* 103 */       return new HostSpecifier(domain.toString());
/*     */     }
/*     */     
/* 106 */     String.valueOf(host); throw new IllegalArgumentException((String.valueOf(host).length() != 0) ? "Domain name does not have a recognized public suffix: ".concat(String.valueOf(host)) : new String("Domain name does not have a recognized public suffix: "));
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
/*     */   public static HostSpecifier from(String specifier) throws ParseException {
/*     */     try {
/* 121 */       return fromValid(specifier);
/* 122 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       String.valueOf(specifier); ParseException parseException = new ParseException((String.valueOf(specifier).length() != 0) ? "Invalid host specifier: ".concat(String.valueOf(specifier)) : new String("Invalid host specifier: "), 0);
/*     */       
/* 129 */       parseException.initCause(e);
/* 130 */       throw parseException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String specifier) {
/*     */     try {
/* 141 */       fromValid(specifier);
/* 142 */       return true;
/* 143 */     } catch (IllegalArgumentException e) {
/* 144 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 150 */     if (this == other) {
/* 151 */       return true;
/*     */     }
/*     */     
/* 154 */     if (other instanceof HostSpecifier) {
/* 155 */       HostSpecifier that = (HostSpecifier)other;
/* 156 */       return this.canonicalForm.equals(that.canonicalForm);
/*     */     } 
/*     */     
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 164 */     return this.canonicalForm.hashCode();
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
/* 176 */     return this.canonicalForm;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\net\HostSpecifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */