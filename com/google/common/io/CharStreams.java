/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Closeable;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
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
/*     */ public final class CharStreams
/*     */ {
/*     */   private static final int BUF_SIZE = 2048;
/*     */   
/*     */   public static long copy(Readable from, Appendable to) throws IOException {
/*  64 */     Preconditions.checkNotNull(from);
/*  65 */     Preconditions.checkNotNull(to);
/*  66 */     CharBuffer buf = CharBuffer.allocate(2048);
/*  67 */     long total = 0L;
/*  68 */     while (from.read(buf) != -1) {
/*  69 */       buf.flip();
/*  70 */       to.append(buf);
/*  71 */       total += buf.remaining();
/*  72 */       buf.clear();
/*     */     } 
/*  74 */     return total;
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
/*     */   public static String toString(Readable r) throws IOException {
/*  86 */     return toStringBuilder(r).toString();
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
/*     */   private static StringBuilder toStringBuilder(Readable r) throws IOException {
/*  98 */     StringBuilder sb = new StringBuilder();
/*  99 */     copy(r, sb);
/* 100 */     return sb;
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
/*     */   public static List<String> readLines(Readable r) throws IOException {
/* 117 */     List<String> result = new ArrayList<String>();
/* 118 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 120 */     while ((line = lineReader.readLine()) != null) {
/* 121 */       result.add(line);
/*     */     }
/* 123 */     return result;
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
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
/* 138 */     Preconditions.checkNotNull(readable);
/* 139 */     Preconditions.checkNotNull(processor);
/*     */     
/* 141 */     LineReader lineReader = new LineReader(readable); String line; do {
/*     */     
/* 143 */     } while ((line = lineReader.readLine()) != null && 
/* 144 */       processor.processLine(line));
/*     */ 
/*     */ 
/*     */     
/* 148 */     return processor.getResult();
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
/*     */   public static void skipFully(Reader reader, long n) throws IOException {
/* 163 */     Preconditions.checkNotNull(reader);
/* 164 */     while (n > 0L) {
/* 165 */       long amt = reader.skip(n);
/* 166 */       if (amt == 0L) {
/*     */         
/* 168 */         if (reader.read() == -1) {
/* 169 */           throw new EOFException();
/*     */         }
/* 171 */         n--; continue;
/*     */       } 
/* 173 */       n -= amt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Writer nullWriter() {
/* 184 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter
/*     */     extends Writer {
/* 189 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(int c) {}
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf) {
/* 197 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf, int off, int len) {
/* 202 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str) {
/* 207 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str, int off, int len) {
/* 212 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq) {
/* 217 */       Preconditions.checkNotNull(csq);
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq, int start, int end) {
/* 223 */       Preconditions.checkPositionIndexes(start, end, csq.length());
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(char c) {
/* 229 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 242 */       return "CharStreams.nullWriter()";
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
/*     */   public static Writer asWriter(Appendable target) {
/* 257 */     if (target instanceof Writer) {
/* 258 */       return (Writer)target;
/*     */     }
/* 260 */     return new AppendableWriter(target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Reader asReader(final Readable readable) {
/* 266 */     Preconditions.checkNotNull(readable);
/* 267 */     if (readable instanceof Reader) {
/* 268 */       return (Reader)readable;
/*     */     }
/* 270 */     return new Reader()
/*     */       {
/*     */         public int read(char[] cbuf, int off, int len) throws IOException {
/* 273 */           return read(CharBuffer.wrap(cbuf, off, len));
/*     */         }
/*     */ 
/*     */         
/*     */         public int read(CharBuffer target) throws IOException {
/* 278 */           return readable.read(target);
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 283 */           if (readable instanceof Closeable)
/* 284 */             ((Closeable)readable).close(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\CharStreams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */