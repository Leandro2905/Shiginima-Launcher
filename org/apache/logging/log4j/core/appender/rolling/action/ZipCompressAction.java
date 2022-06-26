/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZipCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8102;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   private final int level;
/*     */   
/*     */   public ZipCompressAction(File source, File destination, boolean deleteSource, int level) {
/*  64 */     if (source == null) {
/*  65 */       throw new NullPointerException("source");
/*     */     }
/*     */     
/*  68 */     if (destination == null) {
/*  69 */       throw new NullPointerException("destination");
/*     */     }
/*     */     
/*  72 */     this.source = source;
/*  73 */     this.destination = destination;
/*  74 */     this.deleteSource = deleteSource;
/*  75 */     this.level = level;
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
/*  86 */     return execute(this.source, this.destination, this.deleteSource, this.level);
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
/*     */   public static boolean execute(File source, File destination, boolean deleteSource, int level) throws IOException {
/* 102 */     if (source.exists()) {
/* 103 */       FileInputStream fis = new FileInputStream(source);
/* 104 */       FileOutputStream fos = new FileOutputStream(destination);
/* 105 */       ZipOutputStream zos = new ZipOutputStream(fos);
/* 106 */       zos.setLevel(level);
/*     */       
/* 108 */       ZipEntry zipEntry = new ZipEntry(source.getName());
/* 109 */       zos.putNextEntry(zipEntry);
/*     */       
/* 111 */       byte[] inbuf = new byte[8102];
/*     */       
/*     */       int n;
/* 114 */       while ((n = fis.read(inbuf)) != -1) {
/* 115 */         zos.write(inbuf, 0, n);
/*     */       }
/*     */       
/* 118 */       zos.close();
/* 119 */       fis.close();
/*     */       
/* 121 */       if (deleteSource && !source.delete()) {
/* 122 */         LOGGER.warn("Unable to delete " + source.toString() + '.');
/*     */       }
/*     */       
/* 125 */       return true;
/*     */     } 
/*     */     
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 138 */     LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return ZipCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", level=" + this.level + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\action\ZipCompressAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */