/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ class AppendableWriter
/*     */   extends Writer
/*     */ {
/*     */   private final Appendable target;
/*     */   private boolean closed;
/*     */   
/*     */   AppendableWriter(Appendable target) {
/*  47 */     this.target = (Appendable)Preconditions.checkNotNull(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  56 */     checkNotClosed();
/*     */ 
/*     */     
/*  59 */     this.target.append(new String(cbuf, off, len));
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  63 */     checkNotClosed();
/*  64 */     if (this.target instanceof Flushable) {
/*  65 */       ((Flushable)this.target).flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  70 */     this.closed = true;
/*  71 */     if (this.target instanceof Closeable) {
/*  72 */       ((Closeable)this.target).close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*  82 */     checkNotClosed();
/*  83 */     this.target.append((char)c);
/*     */   }
/*     */   
/*     */   public void write(@Nullable String str) throws IOException {
/*  87 */     checkNotClosed();
/*  88 */     this.target.append(str);
/*     */   }
/*     */   
/*     */   public void write(@Nullable String str, int off, int len) throws IOException {
/*  92 */     checkNotClosed();
/*     */     
/*  94 */     this.target.append(str, off, off + len);
/*     */   }
/*     */   
/*     */   public Writer append(char c) throws IOException {
/*  98 */     checkNotClosed();
/*  99 */     this.target.append(c);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Writer append(@Nullable CharSequence charSeq) throws IOException {
/* 104 */     checkNotClosed();
/* 105 */     this.target.append(charSeq);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(@Nullable CharSequence charSeq, int start, int end) throws IOException {
/* 111 */     checkNotClosed();
/* 112 */     this.target.append(charSeq, start, end);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   private void checkNotClosed() throws IOException {
/* 117 */     if (this.closed)
/* 118 */       throw new IOException("Cannot write to a closed writer."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\AppendableWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */