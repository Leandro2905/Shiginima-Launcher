/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.io.Charsets;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LockableFileWriter
/*     */   extends Writer
/*     */ {
/*     */   private static final String LCK = ".lck";
/*     */   private final Writer out;
/*     */   private final File lockFile;
/*     */   
/*     */   public LockableFileWriter(String fileName) throws IOException {
/*  74 */     this(fileName, false, (String)null);
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
/*     */   public LockableFileWriter(String fileName, boolean append) throws IOException {
/*  86 */     this(fileName, append, (String)null);
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
/*     */   public LockableFileWriter(String fileName, boolean append, String lockDir) throws IOException {
/*  99 */     this(new File(fileName), append, lockDir);
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
/*     */   public LockableFileWriter(File file) throws IOException {
/* 111 */     this(file, false, (String)null);
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
/*     */   public LockableFileWriter(File file, boolean append) throws IOException {
/* 123 */     this(file, append, (String)null);
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
/*     */   public LockableFileWriter(File file, boolean append, String lockDir) throws IOException {
/* 136 */     this(file, Charset.defaultCharset(), append, lockDir);
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
/*     */   public LockableFileWriter(File file, Charset encoding) throws IOException {
/* 149 */     this(file, encoding, false, (String)null);
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
/*     */   public LockableFileWriter(File file, String encoding) throws IOException {
/* 164 */     this(file, encoding, false, (String)null);
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
/*     */   public LockableFileWriter(File file, Charset encoding, boolean append, String lockDir) throws IOException {
/* 182 */     file = file.getAbsoluteFile();
/* 183 */     if (file.getParentFile() != null) {
/* 184 */       FileUtils.forceMkdir(file.getParentFile());
/*     */     }
/* 186 */     if (file.isDirectory()) {
/* 187 */       throw new IOException("File specified is a directory");
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (lockDir == null) {
/* 192 */       lockDir = System.getProperty("java.io.tmpdir");
/*     */     }
/* 194 */     File lockDirFile = new File(lockDir);
/* 195 */     FileUtils.forceMkdir(lockDirFile);
/* 196 */     testLockDir(lockDirFile);
/* 197 */     this.lockFile = new File(lockDirFile, file.getName() + ".lck");
/*     */ 
/*     */     
/* 200 */     createLock();
/*     */ 
/*     */     
/* 203 */     this.out = initWriter(file, encoding, append);
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
/*     */   public LockableFileWriter(File file, String encoding, boolean append, String lockDir) throws IOException {
/* 221 */     this(file, Charsets.toCharset(encoding), append, lockDir);
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
/*     */   private void testLockDir(File lockDir) throws IOException {
/* 233 */     if (!lockDir.exists()) {
/* 234 */       throw new IOException("Could not find lockDir: " + lockDir.getAbsolutePath());
/*     */     }
/*     */     
/* 237 */     if (!lockDir.canWrite()) {
/* 238 */       throw new IOException("Could not write to lockDir: " + lockDir.getAbsolutePath());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createLock() throws IOException {
/* 249 */     synchronized (LockableFileWriter.class) {
/* 250 */       if (!this.lockFile.createNewFile()) {
/* 251 */         throw new IOException("Can't write file, lock " + this.lockFile.getAbsolutePath() + " exists");
/*     */       }
/*     */       
/* 254 */       this.lockFile.deleteOnExit();
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
/*     */   private Writer initWriter(File file, Charset encoding, boolean append) throws IOException {
/* 269 */     boolean fileExistedAlready = file.exists();
/* 270 */     OutputStream stream = null;
/* 271 */     Writer writer = null;
/*     */     try {
/* 273 */       stream = new FileOutputStream(file.getAbsolutePath(), append);
/* 274 */       writer = new OutputStreamWriter(stream, Charsets.toCharset(encoding));
/* 275 */     } catch (IOException ex) {
/* 276 */       IOUtils.closeQuietly(writer);
/* 277 */       IOUtils.closeQuietly(stream);
/* 278 */       FileUtils.deleteQuietly(this.lockFile);
/* 279 */       if (!fileExistedAlready) {
/* 280 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 282 */       throw ex;
/* 283 */     } catch (RuntimeException ex) {
/* 284 */       IOUtils.closeQuietly(writer);
/* 285 */       IOUtils.closeQuietly(stream);
/* 286 */       FileUtils.deleteQuietly(this.lockFile);
/* 287 */       if (!fileExistedAlready) {
/* 288 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 290 */       throw ex;
/*     */     } 
/* 292 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 304 */       this.out.close();
/*     */     } finally {
/* 306 */       this.lockFile.delete();
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
/*     */   public void write(int idx) throws IOException {
/* 318 */     this.out.write(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] chr) throws IOException {
/* 328 */     this.out.write(chr);
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
/* 340 */     this.out.write(chr, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 350 */     this.out.write(str);
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
/* 362 */     this.out.write(str, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 371 */     this.out.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\LockableFileWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */