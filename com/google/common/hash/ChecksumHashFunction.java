/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Supplier;
/*    */ import java.io.Serializable;
/*    */ import java.util.zip.Checksum;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ChecksumHashFunction
/*    */   extends AbstractStreamingHashFunction
/*    */   implements Serializable
/*    */ {
/*    */   private final Supplier<? extends Checksum> checksumSupplier;
/*    */   private final int bits;
/*    */   private final String toString;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ChecksumHashFunction(Supplier<? extends Checksum> checksumSupplier, int bits, String toString) {
/* 37 */     this.checksumSupplier = (Supplier<? extends Checksum>)Preconditions.checkNotNull(checksumSupplier);
/* 38 */     Preconditions.checkArgument((bits == 32 || bits == 64), "bits (%s) must be either 32 or 64", new Object[] { Integer.valueOf(bits) });
/* 39 */     this.bits = bits;
/* 40 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*    */   }
/*    */ 
/*    */   
/*    */   public int bits() {
/* 45 */     return this.bits;
/*    */   }
/*    */ 
/*    */   
/*    */   public Hasher newHasher() {
/* 50 */     return new ChecksumHasher((Checksum)this.checksumSupplier.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return this.toString;
/*    */   }
/*    */ 
/*    */   
/*    */   private final class ChecksumHasher
/*    */     extends AbstractByteHasher
/*    */   {
/*    */     private final Checksum checksum;
/*    */ 
/*    */     
/*    */     private ChecksumHasher(Checksum checksum) {
/* 66 */       this.checksum = (Checksum)Preconditions.checkNotNull(checksum);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte b) {
/* 71 */       this.checksum.update(b);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte[] bytes, int off, int len) {
/* 76 */       this.checksum.update(bytes, off, len);
/*    */     }
/*    */ 
/*    */     
/*    */     public HashCode hash() {
/* 81 */       long value = this.checksum.getValue();
/* 82 */       if (ChecksumHashFunction.this.bits == 32)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 88 */         return HashCode.fromInt((int)value);
/*    */       }
/* 90 */       return HashCode.fromLong(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\ChecksumHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */