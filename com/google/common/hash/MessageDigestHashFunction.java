/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MessageDigestHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final MessageDigest prototype;
/*     */   private final int bytes;
/*     */   private final boolean supportsClone;
/*     */   private final String toString;
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, String toString) {
/*  40 */     this.prototype = getMessageDigest(algorithmName);
/*  41 */     this.bytes = this.prototype.getDigestLength();
/*  42 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  43 */     this.supportsClone = supportsClone();
/*     */   }
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
/*  47 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  48 */     this.prototype = getMessageDigest(algorithmName);
/*  49 */     int maxLength = this.prototype.getDigestLength();
/*  50 */     Preconditions.checkArgument((bytes >= 4 && bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", new Object[] { Integer.valueOf(bytes), Integer.valueOf(maxLength) });
/*     */     
/*  52 */     this.bytes = bytes;
/*  53 */     this.supportsClone = supportsClone();
/*     */   }
/*     */   
/*     */   private boolean supportsClone() {
/*     */     try {
/*  58 */       this.prototype.clone();
/*  59 */       return true;
/*  60 */     } catch (CloneNotSupportedException e) {
/*  61 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int bits() {
/*  66 */     return this.bytes * 8;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  70 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static MessageDigest getMessageDigest(String algorithmName) {
/*     */     try {
/*  75 */       return MessageDigest.getInstance(algorithmName);
/*  76 */     } catch (NoSuchAlgorithmException e) {
/*  77 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Hasher newHasher() {
/*  82 */     if (this.supportsClone) {
/*     */       try {
/*  84 */         return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
/*  85 */       } catch (CloneNotSupportedException e) {}
/*     */     }
/*     */ 
/*     */     
/*  89 */     return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
/*     */   }
/*     */   
/*     */   private static final class SerializedForm
/*     */     implements Serializable {
/*     */     private final String algorithmName;
/*     */     private final int bytes;
/*     */     
/*     */     private SerializedForm(String algorithmName, int bytes, String toString) {
/*  98 */       this.algorithmName = algorithmName;
/*  99 */       this.bytes = bytes;
/* 100 */       this.toString = toString;
/*     */     }
/*     */     private final String toString; private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 104 */       return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 111 */     return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MessageDigestHasher
/*     */     extends AbstractByteHasher
/*     */   {
/*     */     private final MessageDigest digest;
/*     */     
/*     */     private final int bytes;
/*     */     private boolean done;
/*     */     
/*     */     private MessageDigestHasher(MessageDigest digest, int bytes) {
/* 124 */       this.digest = digest;
/* 125 */       this.bytes = bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 130 */       checkNotDone();
/* 131 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b) {
/* 136 */       checkNotDone();
/* 137 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 142 */       checkNotDone();
/* 143 */       this.digest.update(b, off, len);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 147 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 152 */       checkNotDone();
/* 153 */       this.done = true;
/* 154 */       return (this.bytes == this.digest.getDigestLength()) ? HashCode.fromBytesNoCopy(this.digest.digest()) : HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\MessageDigestHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */