/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOCase;
/*     */ import org.apache.commons.io.comparator.NameFileComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileAlterationObserver
/*     */   implements Serializable
/*     */ {
/* 124 */   private final List<FileAlterationListener> listeners = new CopyOnWriteArrayList<FileAlterationListener>();
/*     */ 
/*     */   
/*     */   private final FileEntry rootEntry;
/*     */   
/*     */   private final FileFilter fileFilter;
/*     */   
/*     */   private final Comparator<File> comparator;
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(String directoryName) {
/* 135 */     this(new File(directoryName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(String directoryName, FileFilter fileFilter) {
/* 145 */     this(new File(directoryName), fileFilter);
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
/*     */   public FileAlterationObserver(String directoryName, FileFilter fileFilter, IOCase caseSensitivity) {
/* 157 */     this(new File(directoryName), fileFilter, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(File directory) {
/* 166 */     this(directory, (FileFilter)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(File directory, FileFilter fileFilter) {
/* 176 */     this(directory, fileFilter, (IOCase)null);
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
/*     */   public FileAlterationObserver(File directory, FileFilter fileFilter, IOCase caseSensitivity) {
/* 188 */     this(new FileEntry(directory), fileFilter, caseSensitivity);
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
/*     */   protected FileAlterationObserver(FileEntry rootEntry, FileFilter fileFilter, IOCase caseSensitivity) {
/* 200 */     if (rootEntry == null) {
/* 201 */       throw new IllegalArgumentException("Root entry is missing");
/*     */     }
/* 203 */     if (rootEntry.getFile() == null) {
/* 204 */       throw new IllegalArgumentException("Root directory is missing");
/*     */     }
/* 206 */     this.rootEntry = rootEntry;
/* 207 */     this.fileFilter = fileFilter;
/* 208 */     if (caseSensitivity == null || caseSensitivity.equals(IOCase.SYSTEM)) {
/* 209 */       this.comparator = NameFileComparator.NAME_SYSTEM_COMPARATOR;
/* 210 */     } else if (caseSensitivity.equals(IOCase.INSENSITIVE)) {
/* 211 */       this.comparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
/*     */     } else {
/* 213 */       this.comparator = NameFileComparator.NAME_COMPARATOR;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDirectory() {
/* 223 */     return this.rootEntry.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileFilter getFileFilter() {
/* 233 */     return this.fileFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(FileAlterationListener listener) {
/* 242 */     if (listener != null) {
/* 243 */       this.listeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(FileAlterationListener listener) {
/* 253 */     if (listener != null) {
/* 254 */       while (this.listeners.remove(listener));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<FileAlterationListener> getListeners() {
/* 265 */     return this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() throws Exception {
/* 274 */     this.rootEntry.refresh(this.rootEntry.getFile());
/* 275 */     File[] files = listFiles(this.rootEntry.getFile());
/* 276 */     FileEntry[] children = (files.length > 0) ? new FileEntry[files.length] : FileEntry.EMPTY_ENTRIES;
/* 277 */     for (int i = 0; i < files.length; i++) {
/* 278 */       children[i] = createFileEntry(this.rootEntry, files[i]);
/*     */     }
/* 280 */     this.rootEntry.setChildren(children);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAndNotify() {
/* 297 */     for (FileAlterationListener listener : this.listeners) {
/* 298 */       listener.onStart(this);
/*     */     }
/*     */ 
/*     */     
/* 302 */     File rootFile = this.rootEntry.getFile();
/* 303 */     if (rootFile.exists()) {
/* 304 */       checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), listFiles(rootFile));
/* 305 */     } else if (this.rootEntry.isExists()) {
/* 306 */       checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     for (FileAlterationListener listener : this.listeners) {
/* 313 */       listener.onStop(this);
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
/*     */   private void checkAndNotify(FileEntry parent, FileEntry[] previous, File[] files) {
/* 325 */     int c = 0;
/* 326 */     FileEntry[] current = (files.length > 0) ? new FileEntry[files.length] : FileEntry.EMPTY_ENTRIES;
/* 327 */     for (FileEntry entry : previous) {
/* 328 */       while (c < files.length && this.comparator.compare(entry.getFile(), files[c]) > 0) {
/* 329 */         current[c] = createFileEntry(parent, files[c]);
/* 330 */         doCreate(current[c]);
/* 331 */         c++;
/*     */       } 
/* 333 */       if (c < files.length && this.comparator.compare(entry.getFile(), files[c]) == 0) {
/* 334 */         doMatch(entry, files[c]);
/* 335 */         checkAndNotify(entry, entry.getChildren(), listFiles(files[c]));
/* 336 */         current[c] = entry;
/* 337 */         c++;
/*     */       } else {
/* 339 */         checkAndNotify(entry, entry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
/* 340 */         doDelete(entry);
/*     */       } 
/*     */     } 
/* 343 */     for (; c < files.length; c++) {
/* 344 */       current[c] = createFileEntry(parent, files[c]);
/* 345 */       doCreate(current[c]);
/*     */     } 
/* 347 */     parent.setChildren(current);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileEntry createFileEntry(FileEntry parent, File file) {
/* 358 */     FileEntry entry = parent.newChildInstance(file);
/* 359 */     entry.refresh(file);
/* 360 */     File[] files = listFiles(file);
/* 361 */     FileEntry[] children = (files.length > 0) ? new FileEntry[files.length] : FileEntry.EMPTY_ENTRIES;
/* 362 */     for (int i = 0; i < files.length; i++) {
/* 363 */       children[i] = createFileEntry(entry, files[i]);
/*     */     }
/* 365 */     entry.setChildren(children);
/* 366 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doCreate(FileEntry entry) {
/* 375 */     for (FileAlterationListener listener : this.listeners) {
/* 376 */       if (entry.isDirectory()) {
/* 377 */         listener.onDirectoryCreate(entry.getFile()); continue;
/*     */       } 
/* 379 */       listener.onFileCreate(entry.getFile());
/*     */     } 
/*     */     
/* 382 */     FileEntry[] children = entry.getChildren();
/* 383 */     for (FileEntry aChildren : children) {
/* 384 */       doCreate(aChildren);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doMatch(FileEntry entry, File file) {
/* 395 */     if (entry.refresh(file)) {
/* 396 */       for (FileAlterationListener listener : this.listeners) {
/* 397 */         if (entry.isDirectory()) {
/* 398 */           listener.onDirectoryChange(file); continue;
/*     */         } 
/* 400 */         listener.onFileChange(file);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doDelete(FileEntry entry) {
/* 412 */     for (FileAlterationListener listener : this.listeners) {
/* 413 */       if (entry.isDirectory()) {
/* 414 */         listener.onDirectoryDelete(entry.getFile()); continue;
/*     */       } 
/* 416 */       listener.onFileDelete(entry.getFile());
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
/*     */   private File[] listFiles(File file) {
/* 429 */     File[] children = null;
/* 430 */     if (file.isDirectory()) {
/* 431 */       children = (this.fileFilter == null) ? file.listFiles() : file.listFiles(this.fileFilter);
/*     */     }
/* 433 */     if (children == null) {
/* 434 */       children = FileUtils.EMPTY_FILE_ARRAY;
/*     */     }
/* 436 */     if (this.comparator != null && children.length > 1) {
/* 437 */       Arrays.sort(children, this.comparator);
/*     */     }
/* 439 */     return children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 449 */     StringBuilder builder = new StringBuilder();
/* 450 */     builder.append(getClass().getSimpleName());
/* 451 */     builder.append("[file='");
/* 452 */     builder.append(getDirectory().getPath());
/* 453 */     builder.append('\'');
/* 454 */     if (this.fileFilter != null) {
/* 455 */       builder.append(", ");
/* 456 */       builder.append(this.fileFilter.toString());
/*     */     } 
/* 458 */     builder.append(", listeners=");
/* 459 */     builder.append(this.listeners.size());
/* 460 */     builder.append("]");
/* 461 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\monitor\FileAlterationObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */