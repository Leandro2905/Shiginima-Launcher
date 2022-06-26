/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileRenameAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean renameEmptyFiles;
/*     */   
/*     */   public FileRenameAction(File src, File dst, boolean renameEmptyFiles) {
/*  53 */     this.source = src;
/*  54 */     this.destination = dst;
/*  55 */     this.renameEmptyFiles = renameEmptyFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() {
/*  65 */     return execute(this.source, this.destination, this.renameEmptyFiles);
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
/*     */   public static boolean execute(File source, File destination, boolean renameEmptyFiles) {
/*  77 */     if (renameEmptyFiles || source.length() > 0L) {
/*  78 */       File parent = destination.getParentFile();
/*  79 */       if (parent != null && !parent.exists() && 
/*  80 */         !parent.mkdirs()) {
/*  81 */         LOGGER.error("Unable to create directory {}", new Object[] { parent.getAbsolutePath() });
/*  82 */         return false;
/*     */       } 
/*     */       
/*     */       try {
/*  86 */         if (!source.renameTo(destination)) {
/*     */           try {
/*  88 */             copyFile(source, destination);
/*  89 */             return source.delete();
/*  90 */           } catch (IOException iex) {
/*  91 */             LOGGER.error("Unable to rename file {} to {} - {}", new Object[] { source.getAbsolutePath(), destination.getAbsolutePath(), iex.getMessage() });
/*     */           } 
/*     */         }
/*     */         
/*  95 */         return true;
/*  96 */       } catch (Exception ex) {
/*     */         try {
/*  98 */           copyFile(source, destination);
/*  99 */           return source.delete();
/* 100 */         } catch (IOException iex) {
/* 101 */           LOGGER.error("Unable to rename file {} to {} - {}", new Object[] { source.getAbsolutePath(), destination.getAbsolutePath(), iex.getMessage() });
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/*     */       try {
/* 107 */         source.delete();
/* 108 */       } catch (Exception ex) {
/* 109 */         LOGGER.error("Unable to delete empty file " + source.getAbsolutePath());
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   private static void copyFile(File source, File destination) throws IOException {
/* 117 */     if (!destination.exists()) {
/* 118 */       destination.createNewFile();
/*     */     }
/*     */     
/* 121 */     FileChannel srcChannel = null;
/* 122 */     FileChannel destChannel = null;
/* 123 */     FileInputStream srcStream = null;
/* 124 */     FileOutputStream destStream = null;
/*     */     try {
/* 126 */       srcStream = new FileInputStream(source);
/* 127 */       destStream = new FileOutputStream(destination);
/* 128 */       srcChannel = srcStream.getChannel();
/* 129 */       destChannel = destStream.getChannel();
/* 130 */       destChannel.transferFrom(srcChannel, 0L, srcChannel.size());
/*     */     } finally {
/* 132 */       if (srcChannel != null) {
/* 133 */         srcChannel.close();
/*     */       }
/* 135 */       if (srcStream != null) {
/* 136 */         srcStream.close();
/*     */       }
/* 138 */       if (destChannel != null) {
/* 139 */         destChannel.close();
/*     */       }
/* 141 */       if (destStream != null) {
/* 142 */         destStream.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return FileRenameAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", renameEmptyFiles=" + this.renameEmptyFiles + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\action\FileRenameAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */