/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class GwtWorkarounds
/*     */ {
/*     */   @GwtIncompatible("Reader")
/*     */   static CharInput asCharInput(final Reader reader) {
/*  53 */     Preconditions.checkNotNull(reader);
/*  54 */     return new CharInput()
/*     */       {
/*     */         public int read() throws IOException {
/*  57 */           return reader.read();
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/*  62 */           reader.close();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharInput asCharInput(final CharSequence chars) {
/*  71 */     Preconditions.checkNotNull(chars);
/*  72 */     return new CharInput() {
/*  73 */         int index = 0;
/*     */ 
/*     */         
/*     */         public int read() {
/*  77 */           if (this.index < chars.length()) {
/*  78 */             return chars.charAt(this.index++);
/*     */           }
/*  80 */           return -1;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void close() {
/*  86 */           this.index = chars.length();
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible("InputStream")
/*     */   static InputStream asInputStream(final ByteInput input) {
/* 104 */     Preconditions.checkNotNull(input);
/* 105 */     return new InputStream()
/*     */       {
/*     */         public int read() throws IOException {
/* 108 */           return input.read();
/*     */         }
/*     */ 
/*     */         
/*     */         public int read(byte[] b, int off, int len) throws IOException {
/* 113 */           Preconditions.checkNotNull(b);
/* 114 */           Preconditions.checkPositionIndexes(off, off + len, b.length);
/* 115 */           if (len == 0) {
/* 116 */             return 0;
/*     */           }
/* 118 */           int firstByte = read();
/* 119 */           if (firstByte == -1) {
/* 120 */             return -1;
/*     */           }
/* 122 */           b[off] = (byte)firstByte;
/* 123 */           for (int dst = 1; dst < len; dst++) {
/* 124 */             int readByte = read();
/* 125 */             if (readByte == -1) {
/* 126 */               return dst;
/*     */             }
/* 128 */             b[off + dst] = (byte)readByte;
/*     */           } 
/* 130 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 135 */           input.close();
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible("OutputStream")
/*     */   static OutputStream asOutputStream(final ByteOutput output) {
/* 154 */     Preconditions.checkNotNull(output);
/* 155 */     return new OutputStream()
/*     */       {
/*     */         public void write(int b) throws IOException {
/* 158 */           output.write((byte)b);
/*     */         }
/*     */ 
/*     */         
/*     */         public void flush() throws IOException {
/* 163 */           output.flush();
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 168 */           output.close();
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible("Writer")
/*     */   static CharOutput asCharOutput(final Writer writer) {
/* 187 */     Preconditions.checkNotNull(writer);
/* 188 */     return new CharOutput()
/*     */       {
/*     */         public void write(char c) throws IOException {
/* 191 */           writer.append(c);
/*     */         }
/*     */ 
/*     */         
/*     */         public void flush() throws IOException {
/* 196 */           writer.flush();
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 201 */           writer.close();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharOutput stringBuilderOutput(int initialSize) {
/* 211 */     final StringBuilder builder = new StringBuilder(initialSize);
/* 212 */     return new CharOutput()
/*     */       {
/*     */         public void write(char c)
/*     */         {
/* 216 */           builder.append(c);
/*     */         }
/*     */ 
/*     */         
/*     */         public void flush() {}
/*     */ 
/*     */         
/*     */         public void close() {}
/*     */ 
/*     */         
/*     */         public String toString() {
/* 227 */           return builder.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static interface CharInput {
/*     */     int read() throws IOException;
/*     */     
/*     */     void close() throws IOException;
/*     */   }
/*     */   
/*     */   static interface ByteInput {
/*     */     int read() throws IOException;
/*     */     
/*     */     void close() throws IOException;
/*     */   }
/*     */   
/*     */   static interface ByteOutput {
/*     */     void write(byte param1Byte) throws IOException;
/*     */     
/*     */     void flush() throws IOException;
/*     */     
/*     */     void close() throws IOException;
/*     */   }
/*     */   
/*     */   static interface CharOutput {
/*     */     void write(char param1Char) throws IOException;
/*     */     
/*     */     void flush() throws IOException;
/*     */     
/*     */     void close() throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\GwtWorkarounds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */