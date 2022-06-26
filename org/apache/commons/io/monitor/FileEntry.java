/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileEntry
/*     */   implements Serializable
/*     */ {
/*  44 */   static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];
/*     */   
/*     */   private final FileEntry parent;
/*     */   
/*     */   private FileEntry[] children;
/*     */   
/*     */   private final File file;
/*     */   
/*     */   private String name;
/*     */   
/*     */   private boolean exists;
/*     */   
/*     */   private boolean directory;
/*     */   private long lastModified;
/*     */   private long length;
/*     */   
/*     */   public FileEntry(File file) {
/*  61 */     this((FileEntry)null, file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry(FileEntry parent, File file) {
/*  71 */     if (file == null) {
/*  72 */       throw new IllegalArgumentException("File is missing");
/*     */     }
/*  74 */     this.file = file;
/*  75 */     this.parent = parent;
/*  76 */     this.name = file.getName();
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
/*     */   public boolean refresh(File file) {
/*  96 */     boolean origExists = this.exists;
/*  97 */     long origLastModified = this.lastModified;
/*  98 */     boolean origDirectory = this.directory;
/*  99 */     long origLength = this.length;
/*     */ 
/*     */     
/* 102 */     this.name = file.getName();
/* 103 */     this.exists = file.exists();
/* 104 */     this.directory = this.exists ? file.isDirectory() : false;
/* 105 */     this.lastModified = this.exists ? file.lastModified() : 0L;
/* 106 */     this.length = (this.exists && !this.directory) ? file.length() : 0L;
/*     */ 
/*     */     
/* 109 */     return (this.exists != origExists || this.lastModified != origLastModified || this.directory != origDirectory || this.length != origLength);
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
/*     */   public FileEntry newChildInstance(File file) {
/* 125 */     return new FileEntry(this, file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry getParent() {
/* 134 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLevel() {
/* 143 */     return (this.parent == null) ? 0 : (this.parent.getLevel() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry[] getChildren() {
/* 154 */     return (this.children != null) ? this.children : EMPTY_ENTRIES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChildren(FileEntry[] children) {
/* 163 */     this.children = children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 172 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 181 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 190 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 200 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModified(long lastModified) {
/* 210 */     this.lastModified = lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLength() {
/* 219 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLength(long length) {
/* 228 */     this.length = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 238 */     return this.exists;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExists(boolean exists) {
/* 248 */     this.exists = exists;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 257 */     return this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean directory) {
/* 266 */     this.directory = directory;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\monitor\FileEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */