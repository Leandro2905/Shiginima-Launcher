/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.io.IOCase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileFilterUtils
/*     */ {
/*     */   public static File[] filter(IOFileFilter filter, File... files) {
/*  77 */     if (filter == null) {
/*  78 */       throw new IllegalArgumentException("file filter is null");
/*     */     }
/*  80 */     if (files == null) {
/*  81 */       return new File[0];
/*     */     }
/*  83 */     List<File> acceptedFiles = new ArrayList<File>();
/*  84 */     for (File file : files) {
/*  85 */       if (file == null) {
/*  86 */         throw new IllegalArgumentException("file array contains null");
/*     */       }
/*  88 */       if (filter.accept(file)) {
/*  89 */         acceptedFiles.add(file);
/*     */       }
/*     */     } 
/*  92 */     return acceptedFiles.<File>toArray(new File[acceptedFiles.size()]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File[] filter(IOFileFilter filter, Iterable<File> files) {
/* 122 */     List<File> acceptedFiles = filterList(filter, files);
/* 123 */     return acceptedFiles.<File>toArray(new File[acceptedFiles.size()]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<File> filterList(IOFileFilter filter, Iterable<File> files) {
/* 152 */     return filter(filter, files, new ArrayList<File>());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<File> filterList(IOFileFilter filter, File... files) {
/* 181 */     File[] acceptedFiles = filter(filter, files);
/* 182 */     return Arrays.asList(acceptedFiles);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<File> filterSet(IOFileFilter filter, File... files) {
/* 212 */     File[] acceptedFiles = filter(filter, files);
/* 213 */     return new HashSet<File>(Arrays.asList(acceptedFiles));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<File> filterSet(IOFileFilter filter, Iterable<File> files) {
/* 243 */     return filter(filter, files, new HashSet<File>());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends java.util.Collection<File>> T filter(IOFileFilter filter, Iterable<File> files, T acceptedFiles) {
/* 270 */     if (filter == null) {
/* 271 */       throw new IllegalArgumentException("file filter is null");
/*     */     }
/* 273 */     if (files != null) {
/* 274 */       for (File file : files) {
/* 275 */         if (file == null) {
/* 276 */           throw new IllegalArgumentException("file collection contains null");
/*     */         }
/* 278 */         if (filter.accept(file)) {
/* 279 */           acceptedFiles.add(file);
/*     */         }
/*     */       } 
/*     */     }
/* 283 */     return acceptedFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter prefixFileFilter(String prefix) {
/* 294 */     return new PrefixFileFilter(prefix);
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
/*     */   public static IOFileFilter prefixFileFilter(String prefix, IOCase caseSensitivity) {
/* 307 */     return new PrefixFileFilter(prefix, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter suffixFileFilter(String suffix) {
/* 318 */     return new SuffixFileFilter(suffix);
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
/*     */   public static IOFileFilter suffixFileFilter(String suffix, IOCase caseSensitivity) {
/* 331 */     return new SuffixFileFilter(suffix, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter nameFileFilter(String name) {
/* 342 */     return new NameFileFilter(name);
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
/*     */   public static IOFileFilter nameFileFilter(String name, IOCase caseSensitivity) {
/* 355 */     return new NameFileFilter(name, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter directoryFileFilter() {
/* 365 */     return DirectoryFileFilter.DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter fileFileFilter() {
/* 375 */     return FileFileFilter.FILE;
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
/*     */   @Deprecated
/*     */   public static IOFileFilter andFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/* 391 */     return new AndFileFilter(filter1, filter2);
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
/*     */   @Deprecated
/*     */   public static IOFileFilter orFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/* 406 */     return new OrFileFilter(filter1, filter2);
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
/*     */   public static IOFileFilter and(IOFileFilter... filters) {
/* 421 */     return new AndFileFilter(toList(filters));
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
/*     */   public static IOFileFilter or(IOFileFilter... filters) {
/* 436 */     return new OrFileFilter(toList(filters));
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
/*     */   public static List<IOFileFilter> toList(IOFileFilter... filters) {
/* 449 */     if (filters == null) {
/* 450 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/* 452 */     List<IOFileFilter> list = new ArrayList<IOFileFilter>(filters.length);
/* 453 */     for (int i = 0; i < filters.length; i++) {
/* 454 */       if (filters[i] == null) {
/* 455 */         throw new IllegalArgumentException("The filter[" + i + "] is null");
/*     */       }
/* 457 */       list.add(filters[i]);
/*     */     } 
/* 459 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter notFileFilter(IOFileFilter filter) {
/* 470 */     return new NotFileFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter trueFileFilter() {
/* 481 */     return TrueFileFilter.TRUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter falseFileFilter() {
/* 491 */     return FalseFileFilter.FALSE;
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
/*     */   public static IOFileFilter asFileFilter(FileFilter filter) {
/* 504 */     return new DelegateFileFilter(filter);
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
/*     */   public static IOFileFilter asFileFilter(FilenameFilter filter) {
/* 516 */     return new DelegateFileFilter(filter);
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
/*     */   public static IOFileFilter ageFileFilter(long cutoff) {
/* 530 */     return new AgeFileFilter(cutoff);
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
/*     */   public static IOFileFilter ageFileFilter(long cutoff, boolean acceptOlder) {
/* 543 */     return new AgeFileFilter(cutoff, acceptOlder);
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
/*     */   public static IOFileFilter ageFileFilter(Date cutoffDate) {
/* 556 */     return new AgeFileFilter(cutoffDate);
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
/*     */   public static IOFileFilter ageFileFilter(Date cutoffDate, boolean acceptOlder) {
/* 569 */     return new AgeFileFilter(cutoffDate, acceptOlder);
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
/*     */   public static IOFileFilter ageFileFilter(File cutoffReference) {
/* 583 */     return new AgeFileFilter(cutoffReference);
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
/*     */   public static IOFileFilter ageFileFilter(File cutoffReference, boolean acceptOlder) {
/* 597 */     return new AgeFileFilter(cutoffReference, acceptOlder);
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
/*     */   public static IOFileFilter sizeFileFilter(long threshold) {
/* 610 */     return new SizeFileFilter(threshold);
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
/*     */   public static IOFileFilter sizeFileFilter(long threshold, boolean acceptLarger) {
/* 623 */     return new SizeFileFilter(threshold, acceptLarger);
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
/*     */   public static IOFileFilter sizeRangeFileFilter(long minSizeInclusive, long maxSizeInclusive) {
/* 637 */     IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
/* 638 */     IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
/* 639 */     return new AndFileFilter(minimumFilter, maximumFilter);
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
/*     */   public static IOFileFilter magicNumberFileFilter(String magicNumber) {
/* 658 */     return new MagicNumberFileFilter(magicNumber);
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
/*     */   
/*     */   public static IOFileFilter magicNumberFileFilter(String magicNumber, long offset) {
/* 679 */     return new MagicNumberFileFilter(magicNumber, offset);
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
/*     */   public static IOFileFilter magicNumberFileFilter(byte[] magicNumber) {
/* 698 */     return new MagicNumberFileFilter(magicNumber);
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
/*     */   
/*     */   public static IOFileFilter magicNumberFileFilter(byte[] magicNumber, long offset) {
/* 719 */     return new MagicNumberFileFilter(magicNumber, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 724 */   private static final IOFileFilter cvsFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter("CVS") }));
/*     */ 
/*     */ 
/*     */   
/* 728 */   private static final IOFileFilter svnFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter(".svn") }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter makeCVSAware(IOFileFilter filter) {
/* 741 */     if (filter == null) {
/* 742 */       return cvsFilter;
/*     */     }
/* 744 */     return and(new IOFileFilter[] { filter, cvsFilter });
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
/*     */   public static IOFileFilter makeSVNAware(IOFileFilter filter) {
/* 758 */     if (filter == null) {
/* 759 */       return svnFilter;
/*     */     }
/* 761 */     return and(new IOFileFilter[] { filter, svnFilter });
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
/*     */   public static IOFileFilter makeDirectoryOnly(IOFileFilter filter) {
/* 775 */     if (filter == null) {
/* 776 */       return DirectoryFileFilter.DIRECTORY;
/*     */     }
/* 778 */     return new AndFileFilter(DirectoryFileFilter.DIRECTORY, filter);
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
/*     */   public static IOFileFilter makeFileOnly(IOFileFilter filter) {
/* 790 */     if (filter == null) {
/* 791 */       return FileFileFilter.FILE;
/*     */     }
/* 793 */     return new AndFileFilter(FileFileFilter.FILE, filter);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\FileFilterUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */