/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStreamingHashFunction
/*     */   implements HashFunction
/*     */ {
/*     */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
/*  37 */     return newHasher().<T>putObject(instance, funnel).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  41 */     return newHasher().putUnencodedChars(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  45 */     return newHasher().putString(input, charset).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  49 */     return newHasher().putInt(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  53 */     return newHasher().putLong(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashBytes(byte[] input) {
/*  57 */     return newHasher().putBytes(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  61 */     return newHasher().putBytes(input, off, len).hash();
/*     */   }
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  65 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  66 */     return newHasher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class AbstractStreamingHasher
/*     */     extends AbstractHasher
/*     */   {
/*     */     private final ByteBuffer buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int chunkSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractStreamingHasher(int chunkSize) {
/*  95 */       this(chunkSize, chunkSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
/* 109 */       Preconditions.checkArgument((bufferSize % chunkSize == 0));
/*     */ 
/*     */       
/* 112 */       this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */       
/* 115 */       this.bufferSize = bufferSize;
/* 116 */       this.chunkSize = chunkSize;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 133 */       bb.position(bb.limit());
/* 134 */       bb.limit(this.chunkSize + 7);
/* 135 */       while (bb.position() < this.chunkSize) {
/* 136 */         bb.putLong(0L);
/*     */       }
/* 138 */       bb.limit(this.chunkSize);
/* 139 */       bb.flip();
/* 140 */       process(bb);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putBytes(byte[] bytes) {
/* 145 */       return putBytes(bytes, 0, bytes.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putBytes(byte[] bytes, int off, int len) {
/* 150 */       return putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
/*     */     }
/*     */ 
/*     */     
/*     */     private Hasher putBytes(ByteBuffer readBuffer) {
/* 155 */       if (readBuffer.remaining() <= this.buffer.remaining()) {
/* 156 */         this.buffer.put(readBuffer);
/* 157 */         munchIfFull();
/* 158 */         return this;
/*     */       } 
/*     */ 
/*     */       
/* 162 */       int bytesToCopy = this.bufferSize - this.buffer.position();
/* 163 */       for (int i = 0; i < bytesToCopy; i++) {
/* 164 */         this.buffer.put(readBuffer.get());
/*     */       }
/* 166 */       munch();
/*     */ 
/*     */       
/* 169 */       while (readBuffer.remaining() >= this.chunkSize) {
/* 170 */         process(readBuffer);
/*     */       }
/*     */ 
/*     */       
/* 174 */       this.buffer.put(readBuffer);
/* 175 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putUnencodedChars(CharSequence charSequence) {
/* 180 */       for (int i = 0; i < charSequence.length(); i++) {
/* 181 */         putChar(charSequence.charAt(i));
/*     */       }
/* 183 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putByte(byte b) {
/* 188 */       this.buffer.put(b);
/* 189 */       munchIfFull();
/* 190 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putShort(short s) {
/* 195 */       this.buffer.putShort(s);
/* 196 */       munchIfFull();
/* 197 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putChar(char c) {
/* 202 */       this.buffer.putChar(c);
/* 203 */       munchIfFull();
/* 204 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putInt(int i) {
/* 209 */       this.buffer.putInt(i);
/* 210 */       munchIfFull();
/* 211 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putLong(long l) {
/* 216 */       this.buffer.putLong(l);
/* 217 */       munchIfFull();
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 223 */       funnel.funnel(instance, this);
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final HashCode hash() {
/* 229 */       munch();
/* 230 */       this.buffer.flip();
/* 231 */       if (this.buffer.remaining() > 0) {
/* 232 */         processRemaining(this.buffer);
/*     */       }
/* 234 */       return makeHash();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void munchIfFull() {
/* 241 */       if (this.buffer.remaining() < 8)
/*     */       {
/* 243 */         munch();
/*     */       }
/*     */     }
/*     */     
/*     */     private void munch() {
/* 248 */       this.buffer.flip();
/* 249 */       while (this.buffer.remaining() >= this.chunkSize)
/*     */       {
/*     */         
/* 252 */         process(this.buffer);
/*     */       }
/* 254 */       this.buffer.compact();
/*     */     }
/*     */     
/*     */     protected abstract void process(ByteBuffer param1ByteBuffer);
/*     */     
/*     */     abstract HashCode makeHash();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\AbstractStreamingHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */