/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GzCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8102;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   
/*     */   public GzCompressAction(File source, File destination, boolean deleteSource) {
/*  57 */     if (source == null) {
/*  58 */       throw new NullPointerException("source");
/*     */     }
/*     */     
/*  61 */     if (destination == null) {
/*  62 */       throw new NullPointerException("destination");
/*     */     }
/*     */     
/*  65 */     this.source = source;
/*  66 */     this.destination = destination;
/*  67 */     this.deleteSource = deleteSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws IOException {
/*  78 */     return execute(this.source, this.destination, this.deleteSource);
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
/*     */   public static boolean execute(File source, File destination, boolean deleteSource) throws IOException {
/*  93 */     if (source.exists()) {
/*  94 */       FileInputStream fis = new FileInputStream(source);
/*  95 */       FileOutputStream fos = new FileOutputStream(destination);
/*  96 */       GZIPOutputStream gzos = new GZIPOutputStream(fos);
/*  97 */       BufferedOutputStream os = new BufferedOutputStream(gzos);
/*  98 */       byte[] inbuf = new byte[8102];
/*     */       
/*     */       int n;
/* 101 */       while ((n = fis.read(inbuf)) != -1) {
/* 102 */         os.write(inbuf, 0, n);
/*     */       }
/*     */       
/* 105 */       os.close();
/* 106 */       fis.close();
/*     */       
/* 108 */       if (deleteSource && !source.delete()) {
/* 109 */         LOGGER.warn("Unable to delete " + source.toString() + '.');
/*     */       }
/*     */       
/* 112 */       return true;
/*     */     } 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 126 */     LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\action\GzCompressAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */