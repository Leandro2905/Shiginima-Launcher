/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.PrimitiveSink;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ByteSource
/*     */ {
/*     */   private static final int BUF_SIZE = 4096;
/*     */   
/*     */   public CharSource asCharSource(Charset charset) {
/*  73 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openBufferedStream() throws IOException {
/*  99 */     InputStream in = openStream();
/* 100 */     return (in instanceof BufferedInputStream) ? in : new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length) {
/* 112 */     return new SlicedByteSource(offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() throws IOException {
/* 123 */     Closer closer = Closer.create();
/*     */     try {
/* 125 */       InputStream in = closer.<InputStream>register(openStream());
/* 126 */       return (in.read() == -1);
/* 127 */     } catch (Throwable e) {
/* 128 */       throw closer.rethrow(e);
/*     */     } finally {
/* 130 */       closer.close();
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
/*     */   public long size() throws IOException {
/* 150 */     Closer closer = Closer.create();
/*     */     try {
/* 152 */       InputStream in = closer.<InputStream>register(openStream());
/* 153 */       return countBySkipping(in);
/* 154 */     } catch (IOException e) {
/*     */     
/*     */     } finally {
/* 157 */       closer.close();
/*     */     } 
/*     */     
/* 160 */     closer = Closer.create();
/*     */     try {
/* 162 */       InputStream in = closer.<InputStream>register(openStream());
/* 163 */       return countByReading(in);
/* 164 */     } catch (Throwable e) {
/* 165 */       throw closer.rethrow(e);
/*     */     } finally {
/* 167 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long countBySkipping(InputStream in) throws IOException {
/* 176 */     long count = 0L;
/*     */ 
/*     */     
/*     */     while (true) {
/* 180 */       long skipped = in.skip(Math.min(in.available(), 2147483647));
/* 181 */       if (skipped <= 0L) {
/* 182 */         if (in.read() == -1)
/* 183 */           return count; 
/* 184 */         if (count == 0L && in.available() == 0)
/*     */         {
/*     */           
/* 187 */           throw new IOException();
/*     */         }
/* 189 */         count++; continue;
/*     */       } 
/* 191 */       count += skipped;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 196 */   private static final byte[] countBuffer = new byte[4096];
/*     */   
/*     */   private long countByReading(InputStream in) throws IOException {
/* 199 */     long count = 0L;
/*     */     long read;
/* 201 */     while ((read = in.read(countBuffer)) != -1L) {
/* 202 */       count += read;
/*     */     }
/* 204 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(OutputStream output) throws IOException {
/* 215 */     Preconditions.checkNotNull(output);
/*     */     
/* 217 */     Closer closer = Closer.create();
/*     */     try {
/* 219 */       InputStream in = closer.<InputStream>register(openStream());
/* 220 */       return ByteStreams.copy(in, output);
/* 221 */     } catch (Throwable e) {
/* 222 */       throw closer.rethrow(e);
/*     */     } finally {
/* 224 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(ByteSink sink) throws IOException {
/* 235 */     Preconditions.checkNotNull(sink);
/*     */     
/* 237 */     Closer closer = Closer.create();
/*     */     try {
/* 239 */       InputStream in = closer.<InputStream>register(openStream());
/* 240 */       OutputStream out = closer.<OutputStream>register(sink.openStream());
/* 241 */       return ByteStreams.copy(in, out);
/* 242 */     } catch (Throwable e) {
/* 243 */       throw closer.rethrow(e);
/*     */     } finally {
/* 245 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 255 */     Closer closer = Closer.create();
/*     */     try {
/* 257 */       InputStream in = closer.<InputStream>register(openStream());
/* 258 */       return ByteStreams.toByteArray(in);
/* 259 */     } catch (Throwable e) {
/* 260 */       throw closer.rethrow(e);
/*     */     } finally {
/* 262 */       closer.close();
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
/*     */   @Beta
/*     */   public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 277 */     Preconditions.checkNotNull(processor);
/*     */     
/* 279 */     Closer closer = Closer.create();
/*     */     try {
/* 281 */       InputStream in = closer.<InputStream>register(openStream());
/* 282 */       return (T)ByteStreams.readBytes(in, (ByteProcessor)processor);
/* 283 */     } catch (Throwable e) {
/* 284 */       throw closer.rethrow(e);
/*     */     } finally {
/* 286 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash(HashFunction hashFunction) throws IOException {
/* 296 */     Hasher hasher = hashFunction.newHasher();
/* 297 */     copyTo(Funnels.asOutputStream((PrimitiveSink)hasher));
/* 298 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(ByteSource other) throws IOException {
/* 309 */     Preconditions.checkNotNull(other);
/*     */     
/* 311 */     byte[] buf1 = new byte[4096];
/* 312 */     byte[] buf2 = new byte[4096];
/*     */     
/* 314 */     Closer closer = Closer.create();
/*     */     try {
/* 316 */       InputStream in1 = closer.<InputStream>register(openStream());
/* 317 */       InputStream in2 = closer.<InputStream>register(other.openStream());
/*     */       while (true) {
/* 319 */         int read1 = ByteStreams.read(in1, buf1, 0, 4096);
/* 320 */         int read2 = ByteStreams.read(in2, buf2, 0, 4096);
/* 321 */         if (read1 != read2 || !Arrays.equals(buf1, buf2))
/* 322 */           return false; 
/* 323 */         if (read1 != 4096) {
/* 324 */           return true;
/*     */         }
/*     */       } 
/* 327 */     } catch (Throwable e) {
/* 328 */       throw closer.rethrow(e);
/*     */     } finally {
/* 330 */       closer.close();
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
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources) {
/* 346 */     return new ConcatenatedByteSource(sources);
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
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources) {
/* 368 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources) {
/* 384 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource wrap(byte[] b) {
/* 394 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource empty() {
/* 403 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     private final Charset charset;
/*     */ 
/*     */     
/*     */     private AsCharSource(Charset charset) {
/* 415 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 420 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 425 */       String str1 = String.valueOf(String.valueOf(ByteSource.this.toString())), str2 = String.valueOf(String.valueOf(this.charset)); return (new StringBuilder(15 + str1.length() + str2.length())).append(str1).append(".asCharSource(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class SlicedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final long offset;
/*     */     
/*     */     private final long length;
/*     */     
/*     */     private SlicedByteSource(long offset, long length) {
/* 438 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) });
/* 439 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) });
/* 440 */       this.offset = offset;
/* 441 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 446 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 451 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 455 */       if (this.offset > 0L) {
/*     */         try {
/* 457 */           ByteStreams.skipFully(in, this.offset);
/* 458 */         } catch (Throwable e) {
/* 459 */           Closer closer = Closer.create();
/* 460 */           closer.register(in);
/*     */           try {
/* 462 */             throw closer.rethrow(e);
/*     */           } finally {
/* 464 */             closer.close();
/*     */           } 
/*     */         } 
/*     */       }
/* 468 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 473 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) });
/* 474 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) });
/* 475 */       long maxLength = this.length - offset;
/* 476 */       return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 481 */       return (this.length == 0L || super.isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 486 */       String str = String.valueOf(String.valueOf(ByteSource.this.toString())); long l1 = this.offset, l2 = this.length; return (new StringBuilder(50 + str.length())).append(str).append(".slice(").append(l1).append(", ").append(l2).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteArrayByteSource
/*     */     extends ByteSource {
/*     */     protected final byte[] bytes;
/*     */     
/*     */     protected ByteArrayByteSource(byte[] bytes) {
/* 495 */       this.bytes = (byte[])Preconditions.checkNotNull(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() {
/* 500 */       return new ByteArrayInputStream(this.bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 505 */       return openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 510 */       return (this.bytes.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() {
/* 515 */       return this.bytes.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 520 */       return (byte[])this.bytes.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException {
/* 525 */       output.write(this.bytes);
/* 526 */       return this.bytes.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 531 */       processor.processBytes(this.bytes, 0, this.bytes.length);
/* 532 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException {
/* 537 */       return hashFunction.hashBytes(this.bytes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 544 */       String str = String.valueOf(String.valueOf(Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "..."))); return (new StringBuilder(17 + str.length())).append("ByteSource.wrap(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource
/*     */     extends ByteArrayByteSource
/*     */   {
/* 551 */     private static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     private EmptyByteSource() {
/* 554 */       super(new byte[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 559 */       Preconditions.checkNotNull(charset);
/* 560 */       return CharSource.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 565 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 570 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource
/*     */     extends ByteSource {
/*     */     private final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 579 */       this.sources = (Iterable<? extends ByteSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 584 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 589 */       for (ByteSource source : this.sources) {
/* 590 */         if (!source.isEmpty()) {
/* 591 */           return false;
/*     */         }
/*     */       } 
/* 594 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 599 */       long result = 0L;
/* 600 */       for (ByteSource source : this.sources) {
/* 601 */         result += source.size();
/*     */       }
/* 603 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 608 */       String str = String.valueOf(String.valueOf(this.sources)); return (new StringBuilder(19 + str.length())).append("ByteSource.concat(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */