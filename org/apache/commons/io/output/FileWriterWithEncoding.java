/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileWriterWithEncoding
/*     */   extends Writer
/*     */ {
/*     */   private final Writer out;
/*     */   
/*     */   public FileWriterWithEncoding(String filename, String encoding) throws IOException {
/*  66 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, String encoding, boolean append) throws IOException {
/*  79 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(String filename, Charset encoding) throws IOException {
/*  91 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, Charset encoding, boolean append) throws IOException {
/* 104 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(String filename, CharsetEncoder encoding) throws IOException {
/* 116 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, CharsetEncoder encoding, boolean append) throws IOException {
/* 129 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, String encoding) throws IOException {
/* 141 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, String encoding, boolean append) throws IOException {
/* 155 */     this.out = initWriter(file, encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, Charset encoding) throws IOException {
/* 167 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
/* 181 */     this.out = initWriter(file, encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder encoding) throws IOException {
/* 193 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder encoding, boolean append) throws IOException {
/* 207 */     this.out = initWriter(file, encoding, append);
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
/*     */   private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
/* 223 */     if (file == null) {
/* 224 */       throw new NullPointerException("File is missing");
/*     */     }
/* 226 */     if (encoding == null) {
/* 227 */       throw new NullPointerException("Encoding is missing");
/*     */     }
/* 229 */     boolean fileExistedAlready = file.exists();
/* 230 */     OutputStream stream = null;
/* 231 */     Writer writer = null;
/*     */     try {
/* 233 */       stream = new FileOutputStream(file, append);
/* 234 */       if (encoding instanceof Charset) {
/* 235 */         writer = new OutputStreamWriter(stream, (Charset)encoding);
/* 236 */       } else if (encoding instanceof CharsetEncoder) {
/* 237 */         writer = new OutputStreamWriter(stream, (CharsetEncoder)encoding);
/*     */       } else {
/* 239 */         writer = new OutputStreamWriter(stream, (String)encoding);
/*     */       } 
/* 241 */     } catch (IOException ex) {
/* 242 */       IOUtils.closeQuietly(writer);
/* 243 */       IOUtils.closeQuietly(stream);
/* 244 */       if (!fileExistedAlready) {
/* 245 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 247 */       throw ex;
/* 248 */     } catch (RuntimeException ex) {
/* 249 */       IOUtils.closeQuietly(writer);
/* 250 */       IOUtils.closeQuietly(stream);
/* 251 */       if (!fileExistedAlready) {
/* 252 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 254 */       throw ex;
/*     */     } 
/* 256 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int idx) throws IOException {
/* 267 */     this.out.write(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] chr) throws IOException {
/* 277 */     this.out.write(chr);
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
/*     */   public void write(char[] chr, int st, int end) throws IOException {
/* 289 */     this.out.write(chr, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 299 */     this.out.write(str);
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
/*     */   public void write(String str, int st, int end) throws IOException {
/* 311 */     this.out.write(str, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 320 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 329 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\FileWriterWithEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */