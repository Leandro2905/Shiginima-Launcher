/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*  83 */     Preconditions.checkNotNull(file);
/*  84 */     Preconditions.checkNotNull(charset);
/*  85 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/* 100 */     Preconditions.checkNotNull(file);
/* 101 */     Preconditions.checkNotNull(charset);
/* 102 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource asByteSource(File file) {
/* 112 */     return new FileByteSource(file);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource
/*     */     extends ByteSource {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 120 */       this.file = (File)Preconditions.checkNotNull(file);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileInputStream openStream() throws IOException {
/* 125 */       return new FileInputStream(this.file);
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 130 */       if (!this.file.isFile()) {
/* 131 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 133 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 138 */       Closer closer = Closer.create();
/*     */       try {
/* 140 */         FileInputStream in = closer.<FileInputStream>register(openStream());
/* 141 */         return Files.readFile(in, in.getChannel().size());
/* 142 */       } catch (Throwable e) {
/* 143 */         throw closer.rethrow(e);
/*     */       } finally {
/* 145 */         closer.close();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 151 */       String str = String.valueOf(String.valueOf(this.file)); return (new StringBuilder(20 + str.length())).append("Files.asByteSource(").append(str).append(")").toString();
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
/*     */   static byte[] readFile(InputStream in, long expectedSize) throws IOException {
/* 163 */     if (expectedSize > 2147483647L) {
/* 164 */       long l = expectedSize; throw new OutOfMemoryError((new StringBuilder(68)).append("file is too large to fit in a byte array: ").append(l).append(" bytes").toString());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     return (expectedSize == 0L) ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int)expectedSize);
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
/* 185 */     return new FileByteSink(file, modes);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink
/*     */     extends ByteSink {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 194 */       this.file = (File)Preconditions.checkNotNull(file);
/* 195 */       this.modes = ImmutableSet.copyOf((Object[])modes);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileOutputStream openStream() throws IOException {
/* 200 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 205 */       String str1 = String.valueOf(String.valueOf(this.file)), str2 = String.valueOf(String.valueOf(this.modes)); return (new StringBuilder(20 + str1.length() + str2.length())).append("Files.asByteSink(").append(str1).append(", ").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(File file, Charset charset) {
/* 216 */     return asByteSource(file).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
/* 231 */     return asByteSink(file, modes).asCharSink(charset);
/*     */   }
/*     */   
/*     */   private static FileWriteMode[] modes(boolean append) {
/* 235 */     (new FileWriteMode[1])[0] = FileWriteMode.APPEND; return append ? new FileWriteMode[1] : new FileWriteMode[0];
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
/*     */   public static byte[] toByteArray(File file) throws IOException {
/* 250 */     return asByteSource(file).read();
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
/*     */   public static String toString(File file, Charset charset) throws IOException {
/* 264 */     return asCharSource(file, charset).read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void write(byte[] from, File to) throws IOException {
/* 275 */     asByteSink(to, new FileWriteMode[0]).write(from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(File from, OutputStream to) throws IOException {
/* 286 */     asByteSource(from).copyTo(to);
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
/*     */   public static void copy(File from, File to) throws IOException {
/* 303 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
/*     */     
/* 305 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   public static void write(CharSequence from, File to, Charset charset) throws IOException {
/* 320 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   public static void append(CharSequence from, File to, Charset charset) throws IOException {
/* 335 */     write(from, to, charset, true);
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
/*     */   private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
/* 351 */     asCharSink(to, charset, modes(append)).write(from);
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
/*     */   public static void copy(File from, Charset charset, Appendable to) throws IOException {
/* 366 */     asCharSource(from, charset).copyTo(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(File file1, File file2) throws IOException {
/* 375 */     Preconditions.checkNotNull(file1);
/* 376 */     Preconditions.checkNotNull(file2);
/* 377 */     if (file1 == file2 || file1.equals(file2)) {
/* 378 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     long len1 = file1.length();
/* 387 */     long len2 = file2.length();
/* 388 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 389 */       return false;
/*     */     }
/* 391 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */   public static File createTempDir() {
/* 414 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/* 415 */     long l = System.currentTimeMillis(); String baseName = (new StringBuilder(21)).append(l).append("-").toString();
/*     */     
/* 417 */     for (int counter = 0; counter < 10000; counter++) {
/* 418 */       String str = String.valueOf(String.valueOf(baseName)); int i = counter; File tempDir = new File(baseDir, (new StringBuilder(11 + str.length())).append(str).append(i).toString());
/* 419 */       if (tempDir.mkdir()) {
/* 420 */         return tempDir;
/*     */       }
/*     */     } 
/* 423 */     String str1 = String.valueOf(String.valueOf("Failed to create directory within 10000 attempts (tried ")), str2 = String.valueOf(String.valueOf(baseName)), str3 = String.valueOf(String.valueOf(baseName)); char c = '✏'; throw new IllegalStateException((new StringBuilder(17 + str1.length() + str2.length() + str3.length())).append(str1).append(str2).append("0 to ").append(str3).append(c).append(")").toString());
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
/*     */   public static void touch(File file) throws IOException {
/* 436 */     Preconditions.checkNotNull(file);
/* 437 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
/*     */       
/* 439 */       String str = String.valueOf(String.valueOf(file)); throw new IOException((new StringBuilder(38 + str.length())).append("Unable to update modification time of ").append(str).toString());
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
/*     */   public static void createParentDirs(File file) throws IOException {
/* 454 */     Preconditions.checkNotNull(file);
/* 455 */     File parent = file.getCanonicalFile().getParentFile();
/* 456 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     parent.mkdirs();
/* 467 */     if (!parent.isDirectory()) {
/* 468 */       String str = String.valueOf(String.valueOf(file)); throw new IOException((new StringBuilder(39 + str.length())).append("Unable to create parent directories of ").append(str).toString());
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
/*     */   
/*     */   public static void move(File from, File to) throws IOException {
/* 484 */     Preconditions.checkNotNull(from);
/* 485 */     Preconditions.checkNotNull(to);
/* 486 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
/*     */ 
/*     */     
/* 489 */     if (!from.renameTo(to)) {
/* 490 */       copy(from, to);
/* 491 */       if (!from.delete()) {
/* 492 */         if (!to.delete()) {
/* 493 */           String str1 = String.valueOf(String.valueOf(to)); throw new IOException((new StringBuilder(17 + str1.length())).append("Unable to delete ").append(str1).toString());
/*     */         } 
/* 495 */         String str = String.valueOf(String.valueOf(from)); throw new IOException((new StringBuilder(17 + str.length())).append("Unable to delete ").append(str).toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readFirstLine(File file, Charset charset) throws IOException {
/* 513 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 535 */     return readLines(file, charset, new LineProcessor<List<String>>() {
/* 536 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 540 */             this.result.add(line);
/* 541 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 546 */             return this.result;
/*     */           }
/*     */         });
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
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
/* 564 */     return asCharSource(file, charset).readLines(callback);
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
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
/* 580 */     return asByteSource(file).read(processor);
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
/*     */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
/* 594 */     return asByteSource(file).hash(hashFunction);
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
/*     */   public static MappedByteBuffer map(File file) throws IOException {
/* 614 */     Preconditions.checkNotNull(file);
/* 615 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/* 638 */     Preconditions.checkNotNull(file);
/* 639 */     Preconditions.checkNotNull(mode);
/* 640 */     if (!file.exists()) {
/* 641 */       throw new FileNotFoundException(file.toString());
/*     */     }
/* 643 */     return map(file, mode, file.length());
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
/* 669 */     Preconditions.checkNotNull(file);
/* 670 */     Preconditions.checkNotNull(mode);
/*     */     
/* 672 */     Closer closer = Closer.create();
/*     */     try {
/* 674 */       RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/*     */       
/* 676 */       return map(raf, mode, size);
/* 677 */     } catch (Throwable e) {
/* 678 */       throw closer.rethrow(e);
/*     */     } finally {
/* 680 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
/* 686 */     Closer closer = Closer.create();
/*     */     try {
/* 688 */       FileChannel channel = closer.<FileChannel>register(raf.getChannel());
/* 689 */       return channel.map(mode, 0L, size);
/* 690 */     } catch (Throwable e) {
/* 691 */       throw closer.rethrow(e);
/*     */     } finally {
/* 693 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simplifyPath(String pathname) {
/* 719 */     Preconditions.checkNotNull(pathname);
/* 720 */     if (pathname.length() == 0) {
/* 721 */       return ".";
/*     */     }
/*     */ 
/*     */     
/* 725 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/*     */     
/* 727 */     List<String> path = new ArrayList<String>();
/*     */ 
/*     */     
/* 730 */     for (String component : components) {
/* 731 */       if (component.equals("."))
/*     */         continue; 
/* 733 */       if (component.equals("..")) {
/* 734 */         if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/* 735 */           path.remove(path.size() - 1); continue;
/*     */         } 
/* 737 */         path.add("..");
/*     */         continue;
/*     */       } 
/* 740 */       path.add(component);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 745 */     String result = Joiner.on('/').join(path);
/* 746 */     if (pathname.charAt(0) == '/') {
/* 747 */       String.valueOf(result); result = (String.valueOf(result).length() != 0) ? "/".concat(String.valueOf(result)) : new String("/");
/*     */     } 
/*     */     
/* 750 */     while (result.startsWith("/../")) {
/* 751 */       result = result.substring(3);
/*     */     }
/* 753 */     if (result.equals("/..")) {
/* 754 */       result = "/";
/* 755 */     } else if ("".equals(result)) {
/* 756 */       result = ".";
/*     */     } 
/*     */     
/* 759 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFileExtension(String fullName) {
/* 770 */     Preconditions.checkNotNull(fullName);
/* 771 */     String fileName = (new File(fullName)).getName();
/* 772 */     int dotIndex = fileName.lastIndexOf('.');
/* 773 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
/*     */   public static String getNameWithoutExtension(String file) {
/* 787 */     Preconditions.checkNotNull(file);
/* 788 */     String fileName = (new File(file)).getName();
/* 789 */     int dotIndex = fileName.lastIndexOf('.');
/* 790 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static TreeTraverser<File> fileTreeTraverser() {
/* 804 */     return FILE_TREE_TRAVERSER;
/*     */   }
/*     */   
/* 807 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>()
/*     */     {
/*     */       public Iterable<File> children(File file)
/*     */       {
/* 811 */         if (file.isDirectory()) {
/* 812 */           File[] files = file.listFiles();
/* 813 */           if (files != null) {
/* 814 */             return Collections.unmodifiableList(Arrays.asList(files));
/*     */           }
/*     */         } 
/*     */         
/* 818 */         return Collections.emptyList();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 823 */         return "Files.fileTreeTraverser()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isDirectory() {
/* 833 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isFile() {
/* 842 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private enum FilePredicate implements Predicate<File> {
/* 846 */     IS_DIRECTORY
/*     */     {
/*     */       public boolean apply(File file) {
/* 849 */         return file.isDirectory();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 854 */         return "Files.isDirectory()";
/*     */       }
/*     */     },
/*     */     
/* 858 */     IS_FILE
/*     */     {
/*     */       public boolean apply(File file) {
/* 861 */         return file.isFile();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 866 */         return "Files.isFile()";
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\Files.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */