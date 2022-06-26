/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.CheckedInputStream;
/*      */ import java.util.zip.Checksum;
/*      */ import org.apache.commons.io.filefilter.DirectoryFileFilter;
/*      */ import org.apache.commons.io.filefilter.FalseFileFilter;
/*      */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*      */ import org.apache.commons.io.filefilter.IOFileFilter;
/*      */ import org.apache.commons.io.filefilter.SuffixFileFilter;
/*      */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*      */ import org.apache.commons.io.output.NullOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FileUtils
/*      */ {
/*      */   public static final long ONE_KB = 1024L;
/*   95 */   public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_MB = 1048576L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_GB = 1073741824L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  124 */   public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_TB = 1099511627776L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  136 */   public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_PB = 1125899906842624L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  148 */   public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_EB = 1152921504606846976L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  160 */   public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  165 */   public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  170 */   public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  175 */   public static final File[] EMPTY_FILE_ARRAY = new File[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getFile(File directory, String... names) {
/*  192 */     if (directory == null) {
/*  193 */       throw new NullPointerException("directorydirectory must not be null");
/*      */     }
/*  195 */     if (names == null) {
/*  196 */       throw new NullPointerException("names must not be null");
/*      */     }
/*  198 */     File file = directory;
/*  199 */     for (String name : names) {
/*  200 */       file = new File(file, name);
/*      */     }
/*  202 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getFile(String... names) {
/*  213 */     if (names == null) {
/*  214 */       throw new NullPointerException("names must not be null");
/*      */     }
/*  216 */     File file = null;
/*  217 */     for (String name : names) {
/*  218 */       if (file == null) {
/*  219 */         file = new File(name);
/*      */       } else {
/*  221 */         file = new File(file, name);
/*      */       } 
/*      */     } 
/*  224 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getTempDirectoryPath() {
/*  235 */     return System.getProperty("java.io.tmpdir");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getTempDirectory() {
/*  246 */     return new File(getTempDirectoryPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getUserDirectoryPath() {
/*  257 */     return System.getProperty("user.home");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getUserDirectory() {
/*  268 */     return new File(getUserDirectoryPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FileInputStream openInputStream(File file) throws IOException {
/*  291 */     if (file.exists()) {
/*  292 */       if (file.isDirectory()) {
/*  293 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  295 */       if (!file.canRead()) {
/*  296 */         throw new IOException("File '" + file + "' cannot be read");
/*      */       }
/*      */     } else {
/*  299 */       throw new FileNotFoundException("File '" + file + "' does not exist");
/*      */     } 
/*  301 */     return new FileInputStream(file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FileOutputStream openOutputStream(File file) throws IOException {
/*  326 */     return openOutputStream(file, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
/*  352 */     if (file.exists()) {
/*  353 */       if (file.isDirectory()) {
/*  354 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  356 */       if (!file.canWrite()) {
/*  357 */         throw new IOException("File '" + file + "' cannot be written to");
/*      */       }
/*      */     } else {
/*  360 */       File parent = file.getParentFile();
/*  361 */       if (parent != null && 
/*  362 */         !parent.mkdirs() && !parent.isDirectory()) {
/*  363 */         throw new IOException("Directory '" + parent + "' could not be created");
/*      */       }
/*      */     } 
/*      */     
/*  367 */     return new FileOutputStream(file, append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String byteCountToDisplaySize(BigInteger size) {
/*      */     String displaySize;
/*  391 */     if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  392 */       displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
/*  393 */     } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  394 */       displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
/*  395 */     } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  396 */       displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
/*  397 */     } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  398 */       displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
/*  399 */     } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  400 */       displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
/*  401 */     } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  402 */       displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
/*      */     } else {
/*  404 */       displaySize = String.valueOf(size) + " bytes";
/*      */     } 
/*  406 */     return displaySize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String byteCountToDisplaySize(long size) {
/*  426 */     return byteCountToDisplaySize(BigInteger.valueOf(size));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void touch(File file) throws IOException {
/*  443 */     if (!file.exists()) {
/*  444 */       OutputStream out = openOutputStream(file);
/*  445 */       IOUtils.closeQuietly(out);
/*      */     } 
/*  447 */     boolean success = file.setLastModified(System.currentTimeMillis());
/*  448 */     if (!success) {
/*  449 */       throw new IOException("Unable to set the last modification time for " + file);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File[] convertFileCollectionToFileArray(Collection<File> files) {
/*  463 */     return files.<File>toArray(new File[files.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void innerListFiles(Collection<File> files, File directory, IOFileFilter filter, boolean includeSubDirectories) {
/*  478 */     File[] found = directory.listFiles((FileFilter)filter);
/*      */     
/*  480 */     if (found != null) {
/*  481 */       for (File file : found) {
/*  482 */         if (file.isDirectory()) {
/*  483 */           if (includeSubDirectories) {
/*  484 */             files.add(file);
/*      */           }
/*  486 */           innerListFiles(files, file, filter, includeSubDirectories);
/*      */         } else {
/*  488 */           files.add(file);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  521 */     validateListFilesParameters(directory, fileFilter);
/*      */     
/*  523 */     IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
/*  524 */     IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
/*      */ 
/*      */     
/*  527 */     Collection<File> files = new LinkedList<File>();
/*  528 */     innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }, ), false);
/*      */     
/*  530 */     return files;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateListFilesParameters(File directory, IOFileFilter fileFilter) {
/*  544 */     if (!directory.isDirectory()) {
/*  545 */       throw new IllegalArgumentException("Parameter 'directory' is not a directory");
/*      */     }
/*  547 */     if (fileFilter == null) {
/*  548 */       throw new NullPointerException("Parameter 'fileFilter' is null");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter fileFilter) {
/*  559 */     return FileFilterUtils.and(new IOFileFilter[] { fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter dirFilter) {
/*  569 */     return (dirFilter == null) ? FalseFileFilter.INSTANCE : FileFilterUtils.and(new IOFileFilter[] { dirFilter, DirectoryFileFilter.INSTANCE });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  593 */     validateListFilesParameters(directory, fileFilter);
/*      */     
/*  595 */     IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
/*  596 */     IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
/*      */ 
/*      */     
/*  599 */     Collection<File> files = new LinkedList<File>();
/*  600 */     if (directory.isDirectory()) {
/*  601 */       files.add(directory);
/*      */     }
/*  603 */     innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }, ), true);
/*      */     
/*  605 */     return files;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<File> iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  628 */     return listFiles(directory, fileFilter, dirFilter).iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<File> iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  652 */     return listFilesAndDirs(directory, fileFilter, dirFilter).iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] toSuffixes(String[] extensions) {
/*  664 */     String[] suffixes = new String[extensions.length];
/*  665 */     for (int i = 0; i < extensions.length; i++) {
/*  666 */       suffixes[i] = "." + extensions[i];
/*      */     }
/*  668 */     return suffixes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive) {
/*      */     SuffixFileFilter suffixFileFilter;
/*  685 */     if (extensions == null) {
/*  686 */       IOFileFilter filter = TrueFileFilter.INSTANCE;
/*      */     } else {
/*  688 */       String[] suffixes = toSuffixes(extensions);
/*  689 */       suffixFileFilter = new SuffixFileFilter(suffixes);
/*      */     } 
/*  691 */     return listFiles(directory, (IOFileFilter)suffixFileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<File> iterateFiles(File directory, String[] extensions, boolean recursive) {
/*  710 */     return listFiles(directory, extensions, recursive).iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(File file1, File file2) throws IOException {
/*  730 */     boolean file1Exists = file1.exists();
/*  731 */     if (file1Exists != file2.exists()) {
/*  732 */       return false;
/*      */     }
/*      */     
/*  735 */     if (!file1Exists)
/*      */     {
/*  737 */       return true;
/*      */     }
/*      */     
/*  740 */     if (file1.isDirectory() || file2.isDirectory())
/*      */     {
/*  742 */       throw new IOException("Can't compare directories, only files");
/*      */     }
/*      */     
/*  745 */     if (file1.length() != file2.length())
/*      */     {
/*  747 */       return false;
/*      */     }
/*      */     
/*  750 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  752 */       return true;
/*      */     }
/*      */     
/*  755 */     InputStream input1 = null;
/*  756 */     InputStream input2 = null;
/*      */     try {
/*  758 */       input1 = new FileInputStream(file1);
/*  759 */       input2 = new FileInputStream(file2);
/*  760 */       return IOUtils.contentEquals(input1, input2);
/*      */     } finally {
/*      */       
/*  763 */       IOUtils.closeQuietly(input1);
/*  764 */       IOUtils.closeQuietly(input2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
/*  787 */     boolean file1Exists = file1.exists();
/*  788 */     if (file1Exists != file2.exists()) {
/*  789 */       return false;
/*      */     }
/*      */     
/*  792 */     if (!file1Exists)
/*      */     {
/*  794 */       return true;
/*      */     }
/*      */     
/*  797 */     if (file1.isDirectory() || file2.isDirectory())
/*      */     {
/*  799 */       throw new IOException("Can't compare directories, only files");
/*      */     }
/*      */     
/*  802 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  804 */       return true;
/*      */     }
/*      */     
/*  807 */     Reader input1 = null;
/*  808 */     Reader input2 = null;
/*      */     try {
/*  810 */       if (charsetName == null) {
/*  811 */         input1 = new InputStreamReader(new FileInputStream(file1));
/*  812 */         input2 = new InputStreamReader(new FileInputStream(file2));
/*      */       } else {
/*  814 */         input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
/*  815 */         input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
/*      */       } 
/*  817 */       return IOUtils.contentEqualsIgnoreEOL(input1, input2);
/*      */     } finally {
/*      */       
/*  820 */       IOUtils.closeQuietly(input1);
/*  821 */       IOUtils.closeQuietly(input2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File toFile(URL url) {
/*  841 */     if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
/*  842 */       return null;
/*      */     }
/*  844 */     String filename = url.getFile().replace('/', File.separatorChar);
/*  845 */     filename = decodeUrl(filename);
/*  846 */     return new File(filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String decodeUrl(String url) {
/*  865 */     String decoded = url;
/*  866 */     if (url != null && url.indexOf('%') >= 0) {
/*  867 */       int n = url.length();
/*  868 */       StringBuffer buffer = new StringBuffer();
/*  869 */       ByteBuffer bytes = ByteBuffer.allocate(n);
/*  870 */       for (int i = 0; i < n; ) {
/*  871 */         if (url.charAt(i) == '%') {
/*      */           try {
/*      */             do {
/*  874 */               byte octet = (byte)Integer.parseInt(url.substring(i + 1, i + 3), 16);
/*  875 */               bytes.put(octet);
/*  876 */               i += 3;
/*  877 */             } while (i < n && url.charAt(i) == '%');
/*      */             continue;
/*  879 */           } catch (RuntimeException e) {
/*      */ 
/*      */           
/*      */           } finally {
/*  883 */             if (bytes.position() > 0) {
/*  884 */               bytes.flip();
/*  885 */               buffer.append(UTF8.decode(bytes).toString());
/*  886 */               bytes.clear();
/*      */             } 
/*      */           } 
/*      */         }
/*  890 */         buffer.append(url.charAt(i++));
/*      */       } 
/*  892 */       decoded = buffer.toString();
/*      */     } 
/*  894 */     return decoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File[] toFiles(URL[] urls) {
/*  917 */     if (urls == null || urls.length == 0) {
/*  918 */       return EMPTY_FILE_ARRAY;
/*      */     }
/*  920 */     File[] files = new File[urls.length];
/*  921 */     for (int i = 0; i < urls.length; i++) {
/*  922 */       URL url = urls[i];
/*  923 */       if (url != null) {
/*  924 */         if (!url.getProtocol().equals("file")) {
/*  925 */           throw new IllegalArgumentException("URL could not be converted to a File: " + url);
/*      */         }
/*      */         
/*  928 */         files[i] = toFile(url);
/*      */       } 
/*      */     } 
/*  931 */     return files;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static URL[] toURLs(File[] files) throws IOException {
/*  945 */     URL[] urls = new URL[files.length];
/*      */     
/*  947 */     for (int i = 0; i < urls.length; i++) {
/*  948 */       urls[i] = files[i].toURI().toURL();
/*      */     }
/*      */     
/*  951 */     return urls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
/*  977 */     copyFileToDirectory(srcFile, destDir, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
/* 1006 */     if (destDir == null) {
/* 1007 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1009 */     if (destDir.exists() && !destDir.isDirectory()) {
/* 1010 */       throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 1012 */     File destFile = new File(destDir, srcFile.getName());
/* 1013 */     copyFile(srcFile, destFile, preserveFileDate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyFile(File srcFile, File destFile) throws IOException {
/* 1038 */     copyFile(srcFile, destFile, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/* 1067 */     if (srcFile == null) {
/* 1068 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1070 */     if (destFile == null) {
/* 1071 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1073 */     if (!srcFile.exists()) {
/* 1074 */       throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
/*      */     }
/* 1076 */     if (srcFile.isDirectory()) {
/* 1077 */       throw new IOException("Source '" + srcFile + "' exists but is a directory");
/*      */     }
/* 1079 */     if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
/* 1080 */       throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
/*      */     }
/* 1082 */     File parentFile = destFile.getParentFile();
/* 1083 */     if (parentFile != null && 
/* 1084 */       !parentFile.mkdirs() && !parentFile.isDirectory()) {
/* 1085 */       throw new IOException("Destination '" + parentFile + "' directory cannot be created");
/*      */     }
/*      */     
/* 1088 */     if (destFile.exists() && !destFile.canWrite()) {
/* 1089 */       throw new IOException("Destination '" + destFile + "' exists but is read-only");
/*      */     }
/* 1091 */     doCopyFile(srcFile, destFile, preserveFileDate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyFile(File input, OutputStream output) throws IOException {
/* 1112 */     FileInputStream fis = new FileInputStream(input);
/*      */     try {
/* 1114 */       return IOUtils.copyLarge(fis, output);
/*      */     } finally {
/* 1116 */       fis.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/* 1129 */     if (destFile.exists() && destFile.isDirectory()) {
/* 1130 */       throw new IOException("Destination '" + destFile + "' exists but is a directory");
/*      */     }
/*      */     
/* 1133 */     FileInputStream fis = null;
/* 1134 */     FileOutputStream fos = null;
/* 1135 */     FileChannel input = null;
/* 1136 */     FileChannel output = null;
/*      */     try {
/* 1138 */       fis = new FileInputStream(srcFile);
/* 1139 */       fos = new FileOutputStream(destFile);
/* 1140 */       input = fis.getChannel();
/* 1141 */       output = fos.getChannel();
/* 1142 */       long size = input.size();
/* 1143 */       long pos = 0L;
/* 1144 */       long count = 0L;
/* 1145 */       while (pos < size) {
/* 1146 */         count = (size - pos > 31457280L) ? 31457280L : (size - pos);
/* 1147 */         pos += output.transferFrom(input, pos, count);
/*      */       } 
/*      */     } finally {
/* 1150 */       IOUtils.closeQuietly(output);
/* 1151 */       IOUtils.closeQuietly(fos);
/* 1152 */       IOUtils.closeQuietly(input);
/* 1153 */       IOUtils.closeQuietly(fis);
/*      */     } 
/*      */     
/* 1156 */     if (srcFile.length() != destFile.length()) {
/* 1157 */       throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
/*      */     }
/*      */     
/* 1160 */     if (preserveFileDate) {
/* 1161 */       destFile.setLastModified(srcFile.lastModified());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
/* 1190 */     if (srcDir == null) {
/* 1191 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1193 */     if (srcDir.exists() && !srcDir.isDirectory()) {
/* 1194 */       throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
/*      */     }
/* 1196 */     if (destDir == null) {
/* 1197 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1199 */     if (destDir.exists() && !destDir.isDirectory()) {
/* 1200 */       throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 1202 */     copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir) throws IOException {
/* 1230 */     copyDirectory(srcDir, destDir, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
/* 1261 */     copyDirectory(srcDir, destDir, null, preserveFileDate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
/* 1310 */     copyDirectory(srcDir, destDir, filter, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
/* 1361 */     if (srcDir == null) {
/* 1362 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1364 */     if (destDir == null) {
/* 1365 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1367 */     if (!srcDir.exists()) {
/* 1368 */       throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
/*      */     }
/* 1370 */     if (!srcDir.isDirectory()) {
/* 1371 */       throw new IOException("Source '" + srcDir + "' exists but is not a directory");
/*      */     }
/* 1373 */     if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
/* 1374 */       throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
/*      */     }
/*      */ 
/*      */     
/* 1378 */     List<String> exclusionList = null;
/* 1379 */     if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
/* 1380 */       File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/* 1381 */       if (srcFiles != null && srcFiles.length > 0) {
/* 1382 */         exclusionList = new ArrayList<String>(srcFiles.length);
/* 1383 */         for (File srcFile : srcFiles) {
/* 1384 */           File copiedFile = new File(destDir, srcFile.getName());
/* 1385 */           exclusionList.add(copiedFile.getCanonicalPath());
/*      */         } 
/*      */       } 
/*      */     } 
/* 1389 */     doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
/* 1406 */     File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/* 1407 */     if (srcFiles == null) {
/* 1408 */       throw new IOException("Failed to list contents of " + srcDir);
/*      */     }
/* 1410 */     if (destDir.exists()) {
/* 1411 */       if (!destDir.isDirectory()) {
/* 1412 */         throw new IOException("Destination '" + destDir + "' exists but is not a directory");
/*      */       }
/*      */     }
/* 1415 */     else if (!destDir.mkdirs() && !destDir.isDirectory()) {
/* 1416 */       throw new IOException("Destination '" + destDir + "' directory cannot be created");
/*      */     } 
/*      */     
/* 1419 */     if (!destDir.canWrite()) {
/* 1420 */       throw new IOException("Destination '" + destDir + "' cannot be written to");
/*      */     }
/* 1422 */     for (File srcFile : srcFiles) {
/* 1423 */       File dstFile = new File(destDir, srcFile.getName());
/* 1424 */       if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
/* 1425 */         if (srcFile.isDirectory()) {
/* 1426 */           doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
/*      */         } else {
/* 1428 */           doCopyFile(srcFile, dstFile, preserveFileDate);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1434 */     if (preserveFileDate) {
/* 1435 */       destDir.setLastModified(srcDir.lastModified());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyURLToFile(URL source, File destination) throws IOException {
/* 1460 */     InputStream input = source.openStream();
/* 1461 */     copyInputStreamToFile(input, destination);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout) throws IOException {
/* 1486 */     URLConnection connection = source.openConnection();
/* 1487 */     connection.setConnectTimeout(connectionTimeout);
/* 1488 */     connection.setReadTimeout(readTimeout);
/* 1489 */     InputStream input = connection.getInputStream();
/* 1490 */     copyInputStreamToFile(input, destination);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
/*      */     try {
/* 1510 */       FileOutputStream output = openOutputStream(destination);
/*      */       try {
/* 1512 */         IOUtils.copy(source, output);
/* 1513 */         output.close();
/*      */       } finally {
/* 1515 */         IOUtils.closeQuietly(output);
/*      */       } 
/*      */     } finally {
/* 1518 */       IOUtils.closeQuietly(source);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void deleteDirectory(File directory) throws IOException {
/* 1530 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/* 1534 */     if (!isSymlink(directory)) {
/* 1535 */       cleanDirectory(directory);
/*      */     }
/*      */     
/* 1538 */     if (!directory.delete()) {
/* 1539 */       String message = "Unable to delete directory " + directory + ".";
/*      */       
/* 1541 */       throw new IOException(message);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean deleteQuietly(File file) {
/* 1561 */     if (file == null) {
/* 1562 */       return false;
/*      */     }
/*      */     try {
/* 1565 */       if (file.isDirectory()) {
/* 1566 */         cleanDirectory(file);
/*      */       }
/* 1568 */     } catch (Exception ignored) {}
/*      */ 
/*      */     
/*      */     try {
/* 1572 */       return file.delete();
/* 1573 */     } catch (Exception ignored) {
/* 1574 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean directoryContains(File directory, File child) throws IOException {
/* 1605 */     if (directory == null) {
/* 1606 */       throw new IllegalArgumentException("Directory must not be null");
/*      */     }
/*      */     
/* 1609 */     if (!directory.isDirectory()) {
/* 1610 */       throw new IllegalArgumentException("Not a directory: " + directory);
/*      */     }
/*      */     
/* 1613 */     if (child == null) {
/* 1614 */       return false;
/*      */     }
/*      */     
/* 1617 */     if (!directory.exists() || !child.exists()) {
/* 1618 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1622 */     String canonicalParent = directory.getCanonicalPath();
/* 1623 */     String canonicalChild = child.getCanonicalPath();
/*      */     
/* 1625 */     return FilenameUtils.directoryContains(canonicalParent, canonicalChild);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void cleanDirectory(File directory) throws IOException {
/* 1635 */     if (!directory.exists()) {
/* 1636 */       String message = directory + " does not exist";
/* 1637 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 1640 */     if (!directory.isDirectory()) {
/* 1641 */       String message = directory + " is not a directory";
/* 1642 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 1645 */     File[] files = directory.listFiles();
/* 1646 */     if (files == null) {
/* 1647 */       throw new IOException("Failed to list contents of " + directory);
/*      */     }
/*      */     
/* 1650 */     IOException exception = null;
/* 1651 */     for (File file : files) {
/*      */       try {
/* 1653 */         forceDelete(file);
/* 1654 */       } catch (IOException ioe) {
/* 1655 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/* 1659 */     if (null != exception) {
/* 1660 */       throw exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean waitFor(File file, int seconds) {
/* 1677 */     int timeout = 0;
/* 1678 */     int tick = 0;
/* 1679 */     while (!file.exists()) {
/* 1680 */       if (tick++ >= 10) {
/* 1681 */         tick = 0;
/* 1682 */         if (timeout++ > seconds) {
/* 1683 */           return false;
/*      */         }
/*      */       } 
/*      */       try {
/* 1687 */         Thread.sleep(100L);
/* 1688 */       } catch (InterruptedException ignore) {
/*      */       
/* 1690 */       } catch (Exception ex) {
/*      */         break;
/*      */       } 
/*      */     } 
/* 1694 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String readFileToString(File file, Charset encoding) throws IOException {
/* 1709 */     InputStream in = null;
/*      */     try {
/* 1711 */       in = openInputStream(file);
/* 1712 */       return IOUtils.toString(in, Charsets.toCharset(encoding));
/*      */     } finally {
/* 1714 */       IOUtils.closeQuietly(in);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String readFileToString(File file, String encoding) throws IOException {
/* 1734 */     return readFileToString(file, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String readFileToString(File file) throws IOException {
/* 1748 */     return readFileToString(file, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] readFileToByteArray(File file) throws IOException {
/* 1761 */     InputStream in = null;
/*      */     try {
/* 1763 */       in = openInputStream(file);
/* 1764 */       return IOUtils.toByteArray(in, file.length());
/*      */     } finally {
/* 1766 */       IOUtils.closeQuietly(in);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(File file, Charset encoding) throws IOException {
/* 1781 */     InputStream in = null;
/*      */     try {
/* 1783 */       in = openInputStream(file);
/* 1784 */       return IOUtils.readLines(in, Charsets.toCharset(encoding));
/*      */     } finally {
/* 1786 */       IOUtils.closeQuietly(in);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(File file, String encoding) throws IOException {
/* 1806 */     return readLines(file, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(File file) throws IOException {
/* 1819 */     return readLines(file, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(File file, String encoding) throws IOException {
/* 1854 */     InputStream in = null;
/*      */     try {
/* 1856 */       in = openInputStream(file);
/* 1857 */       return IOUtils.lineIterator(in, encoding);
/* 1858 */     } catch (IOException ex) {
/* 1859 */       IOUtils.closeQuietly(in);
/* 1860 */       throw ex;
/* 1861 */     } catch (RuntimeException ex) {
/* 1862 */       IOUtils.closeQuietly(in);
/* 1863 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(File file) throws IOException {
/* 1877 */     return lineIterator(file, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, Charset encoding) throws IOException {
/* 1895 */     writeStringToFile(file, data, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, String encoding) throws IOException {
/* 1911 */     writeStringToFile(file, data, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
/* 1926 */     OutputStream out = null;
/*      */     try {
/* 1928 */       out = openOutputStream(file, append);
/* 1929 */       IOUtils.write(data, out, encoding);
/* 1930 */       out.close();
/*      */     } finally {
/* 1932 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, String encoding, boolean append) throws IOException {
/* 1951 */     writeStringToFile(file, data, Charsets.toCharset(encoding), append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data) throws IOException {
/* 1962 */     writeStringToFile(file, data, Charset.defaultCharset(), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, boolean append) throws IOException {
/* 1976 */     writeStringToFile(file, data, Charset.defaultCharset(), append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data) throws IOException {
/* 1988 */     write(file, data, Charset.defaultCharset(), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data, boolean append) throws IOException {
/* 2002 */     write(file, data, Charset.defaultCharset(), append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data, Charset encoding) throws IOException {
/* 2015 */     write(file, data, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data, String encoding) throws IOException {
/* 2029 */     write(file, data, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data, Charset encoding, boolean append) throws IOException {
/* 2044 */     String str = (data == null) ? null : data.toString();
/* 2045 */     writeStringToFile(file, str, encoding, append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(File file, CharSequence data, String encoding, boolean append) throws IOException {
/* 2063 */     write(file, data, Charsets.toCharset(encoding), append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
/* 2078 */     writeByteArrayToFile(file, data, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
/* 2092 */     OutputStream out = null;
/*      */     try {
/* 2094 */       out = openOutputStream(file, append);
/* 2095 */       out.write(data);
/* 2096 */       out.close();
/*      */     } finally {
/* 2098 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines) throws IOException {
/* 2118 */     writeLines(file, encoding, lines, null, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, boolean append) throws IOException {
/* 2136 */     writeLines(file, encoding, lines, null, append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, Collection<?> lines) throws IOException {
/* 2150 */     writeLines(file, null, lines, null, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
/* 2166 */     writeLines(file, null, lines, null, append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding) throws IOException {
/* 2187 */     writeLines(file, encoding, lines, lineEnding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 2207 */     FileOutputStream out = null;
/*      */     try {
/* 2209 */       out = openOutputStream(file, append);
/* 2210 */       BufferedOutputStream buffer = new BufferedOutputStream(out);
/* 2211 */       IOUtils.writeLines(lines, lineEnding, buffer, encoding);
/* 2212 */       buffer.flush();
/* 2213 */       out.close();
/*      */     } finally {
/* 2215 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
/* 2231 */     writeLines(file, null, lines, lineEnding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 2249 */     writeLines(file, null, lines, lineEnding, append);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void forceDelete(File file) throws IOException {
/* 2269 */     if (file.isDirectory()) {
/* 2270 */       deleteDirectory(file);
/*      */     } else {
/* 2272 */       boolean filePresent = file.exists();
/* 2273 */       if (!file.delete()) {
/* 2274 */         if (!filePresent) {
/* 2275 */           throw new FileNotFoundException("File does not exist: " + file);
/*      */         }
/* 2277 */         String message = "Unable to delete file: " + file;
/*      */         
/* 2279 */         throw new IOException(message);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void forceDeleteOnExit(File file) throws IOException {
/* 2293 */     if (file.isDirectory()) {
/* 2294 */       deleteDirectoryOnExit(file);
/*      */     } else {
/* 2296 */       file.deleteOnExit();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void deleteDirectoryOnExit(File directory) throws IOException {
/* 2308 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/* 2312 */     directory.deleteOnExit();
/* 2313 */     if (!isSymlink(directory)) {
/* 2314 */       cleanDirectoryOnExit(directory);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void cleanDirectoryOnExit(File directory) throws IOException {
/* 2326 */     if (!directory.exists()) {
/* 2327 */       String message = directory + " does not exist";
/* 2328 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2331 */     if (!directory.isDirectory()) {
/* 2332 */       String message = directory + " is not a directory";
/* 2333 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2336 */     File[] files = directory.listFiles();
/* 2337 */     if (files == null) {
/* 2338 */       throw new IOException("Failed to list contents of " + directory);
/*      */     }
/*      */     
/* 2341 */     IOException exception = null;
/* 2342 */     for (File file : files) {
/*      */       try {
/* 2344 */         forceDeleteOnExit(file);
/* 2345 */       } catch (IOException ioe) {
/* 2346 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/* 2350 */     if (null != exception) {
/* 2351 */       throw exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void forceMkdir(File directory) throws IOException {
/* 2367 */     if (directory.exists()) {
/* 2368 */       if (!directory.isDirectory()) {
/* 2369 */         String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2374 */         throw new IOException(message);
/*      */       }
/*      */     
/* 2377 */     } else if (!directory.mkdirs()) {
/*      */ 
/*      */       
/* 2380 */       if (!directory.isDirectory()) {
/*      */         
/* 2382 */         String message = "Unable to create directory " + directory;
/*      */         
/* 2384 */         throw new IOException(message);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long sizeOf(File file) {
/* 2411 */     if (!file.exists()) {
/* 2412 */       String message = file + " does not exist";
/* 2413 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2416 */     if (file.isDirectory()) {
/* 2417 */       return sizeOfDirectory(file);
/*      */     }
/* 2419 */     return file.length();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger sizeOfAsBigInteger(File file) {
/* 2444 */     if (!file.exists()) {
/* 2445 */       String message = file + " does not exist";
/* 2446 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2449 */     if (file.isDirectory()) {
/* 2450 */       return sizeOfDirectoryAsBigInteger(file);
/*      */     }
/* 2452 */     return BigInteger.valueOf(file.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long sizeOfDirectory(File directory) {
/* 2468 */     checkDirectory(directory);
/*      */     
/* 2470 */     File[] files = directory.listFiles();
/* 2471 */     if (files == null) {
/* 2472 */       return 0L;
/*      */     }
/* 2474 */     long size = 0L;
/*      */     
/* 2476 */     for (File file : files) {
/*      */       try {
/* 2478 */         if (!isSymlink(file)) {
/* 2479 */           size += sizeOf(file);
/* 2480 */           if (size < 0L) {
/*      */             break;
/*      */           }
/*      */         } 
/* 2484 */       } catch (IOException ioe) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2489 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
/* 2503 */     checkDirectory(directory);
/*      */     
/* 2505 */     File[] files = directory.listFiles();
/* 2506 */     if (files == null) {
/* 2507 */       return BigInteger.ZERO;
/*      */     }
/* 2509 */     BigInteger size = BigInteger.ZERO;
/*      */     
/* 2511 */     for (File file : files) {
/*      */       try {
/* 2513 */         if (!isSymlink(file)) {
/* 2514 */           size = size.add(BigInteger.valueOf(sizeOf(file)));
/*      */         }
/* 2516 */       } catch (IOException ioe) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2521 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkDirectory(File directory) {
/* 2531 */     if (!directory.exists()) {
/* 2532 */       throw new IllegalArgumentException(directory + " does not exist");
/*      */     }
/* 2534 */     if (!directory.isDirectory()) {
/* 2535 */       throw new IllegalArgumentException(directory + " is not a directory");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileNewer(File file, File reference) {
/* 2554 */     if (reference == null) {
/* 2555 */       throw new IllegalArgumentException("No specified reference file");
/*      */     }
/* 2557 */     if (!reference.exists()) {
/* 2558 */       throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
/*      */     }
/*      */     
/* 2561 */     return isFileNewer(file, reference.lastModified());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileNewer(File file, Date date) {
/* 2577 */     if (date == null) {
/* 2578 */       throw new IllegalArgumentException("No specified date");
/*      */     }
/* 2580 */     return isFileNewer(file, date.getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileNewer(File file, long timeMillis) {
/* 2596 */     if (file == null) {
/* 2597 */       throw new IllegalArgumentException("No specified file");
/*      */     }
/* 2599 */     if (!file.exists()) {
/* 2600 */       return false;
/*      */     }
/* 2602 */     return (file.lastModified() > timeMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileOlder(File file, File reference) {
/* 2621 */     if (reference == null) {
/* 2622 */       throw new IllegalArgumentException("No specified reference file");
/*      */     }
/* 2624 */     if (!reference.exists()) {
/* 2625 */       throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
/*      */     }
/*      */     
/* 2628 */     return isFileOlder(file, reference.lastModified());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileOlder(File file, Date date) {
/* 2644 */     if (date == null) {
/* 2645 */       throw new IllegalArgumentException("No specified date");
/*      */     }
/* 2647 */     return isFileOlder(file, date.getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileOlder(File file, long timeMillis) {
/* 2663 */     if (file == null) {
/* 2664 */       throw new IllegalArgumentException("No specified file");
/*      */     }
/* 2666 */     if (!file.exists()) {
/* 2667 */       return false;
/*      */     }
/* 2669 */     return (file.lastModified() < timeMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long checksumCRC32(File file) throws IOException {
/* 2685 */     CRC32 crc = new CRC32();
/* 2686 */     checksum(file, crc);
/* 2687 */     return crc.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Checksum checksum(File file, Checksum checksum) throws IOException {
/* 2708 */     if (file.isDirectory()) {
/* 2709 */       throw new IllegalArgumentException("Checksums can't be computed on directories");
/*      */     }
/* 2711 */     InputStream in = null;
/*      */     try {
/* 2713 */       in = new CheckedInputStream(new FileInputStream(file), checksum);
/* 2714 */       IOUtils.copy(in, (OutputStream)new NullOutputStream());
/*      */     } finally {
/* 2716 */       IOUtils.closeQuietly(in);
/*      */     } 
/* 2718 */     return checksum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveDirectory(File srcDir, File destDir) throws IOException {
/* 2735 */     if (srcDir == null) {
/* 2736 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2738 */     if (destDir == null) {
/* 2739 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 2741 */     if (!srcDir.exists()) {
/* 2742 */       throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
/*      */     }
/* 2744 */     if (!srcDir.isDirectory()) {
/* 2745 */       throw new IOException("Source '" + srcDir + "' is not a directory");
/*      */     }
/* 2747 */     if (destDir.exists()) {
/* 2748 */       throw new FileExistsException("Destination '" + destDir + "' already exists");
/*      */     }
/* 2750 */     boolean rename = srcDir.renameTo(destDir);
/* 2751 */     if (!rename) {
/* 2752 */       if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
/* 2753 */         throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
/*      */       }
/* 2755 */       copyDirectory(srcDir, destDir);
/* 2756 */       deleteDirectory(srcDir);
/* 2757 */       if (srcDir.exists()) {
/* 2758 */         throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 2778 */     if (src == null) {
/* 2779 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2781 */     if (destDir == null) {
/* 2782 */       throw new NullPointerException("Destination directory must not be null");
/*      */     }
/* 2784 */     if (!destDir.exists() && createDestDir) {
/* 2785 */       destDir.mkdirs();
/*      */     }
/* 2787 */     if (!destDir.exists()) {
/* 2788 */       throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
/*      */     }
/*      */     
/* 2791 */     if (!destDir.isDirectory()) {
/* 2792 */       throw new IOException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 2794 */     moveDirectory(src, new File(destDir, src.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveFile(File srcFile, File destFile) throws IOException {
/* 2812 */     if (srcFile == null) {
/* 2813 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2815 */     if (destFile == null) {
/* 2816 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 2818 */     if (!srcFile.exists()) {
/* 2819 */       throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
/*      */     }
/* 2821 */     if (srcFile.isDirectory()) {
/* 2822 */       throw new IOException("Source '" + srcFile + "' is a directory");
/*      */     }
/* 2824 */     if (destFile.exists()) {
/* 2825 */       throw new FileExistsException("Destination '" + destFile + "' already exists");
/*      */     }
/* 2827 */     if (destFile.isDirectory()) {
/* 2828 */       throw new IOException("Destination '" + destFile + "' is a directory");
/*      */     }
/* 2830 */     boolean rename = srcFile.renameTo(destFile);
/* 2831 */     if (!rename) {
/* 2832 */       copyFile(srcFile, destFile);
/* 2833 */       if (!srcFile.delete()) {
/* 2834 */         deleteQuietly(destFile);
/* 2835 */         throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
/* 2855 */     if (srcFile == null) {
/* 2856 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2858 */     if (destDir == null) {
/* 2859 */       throw new NullPointerException("Destination directory must not be null");
/*      */     }
/* 2861 */     if (!destDir.exists() && createDestDir) {
/* 2862 */       destDir.mkdirs();
/*      */     }
/* 2864 */     if (!destDir.exists()) {
/* 2865 */       throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
/*      */     }
/*      */     
/* 2868 */     if (!destDir.isDirectory()) {
/* 2869 */       throw new IOException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 2871 */     moveFile(srcFile, new File(destDir, srcFile.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 2890 */     if (src == null) {
/* 2891 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2893 */     if (destDir == null) {
/* 2894 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 2896 */     if (!src.exists()) {
/* 2897 */       throw new FileNotFoundException("Source '" + src + "' does not exist");
/*      */     }
/* 2899 */     if (src.isDirectory()) {
/* 2900 */       moveDirectoryToDirectory(src, destDir, createDestDir);
/*      */     } else {
/* 2902 */       moveFileToDirectory(src, destDir, createDestDir);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSymlink(File file) throws IOException {
/* 2921 */     if (file == null) {
/* 2922 */       throw new NullPointerException("File must not be null");
/*      */     }
/* 2924 */     if (FilenameUtils.isSystemWindows()) {
/* 2925 */       return false;
/*      */     }
/* 2927 */     File fileInCanonicalDir = null;
/* 2928 */     if (file.getParent() == null) {
/* 2929 */       fileInCanonicalDir = file;
/*      */     } else {
/* 2931 */       File canonicalDir = file.getParentFile().getCanonicalFile();
/* 2932 */       fileInCanonicalDir = new File(canonicalDir, file.getName());
/*      */     } 
/*      */     
/* 2935 */     if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
/* 2936 */       return false;
/*      */     }
/* 2938 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */