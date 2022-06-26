/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemUtils
/*     */ {
/*  48 */   private static final FileSystemUtils INSTANCE = new FileSystemUtils();
/*     */ 
/*     */   
/*     */   private static final int INIT_PROBLEM = -1;
/*     */ 
/*     */   
/*     */   private static final int OTHER = 0;
/*     */   
/*     */   private static final int WINDOWS = 1;
/*     */   
/*     */   private static final int UNIX = 2;
/*     */   
/*     */   private static final int POSIX_UNIX = 3;
/*     */   
/*     */   private static final int OS;
/*     */   
/*     */   private static final String DF;
/*     */ 
/*     */   
/*     */   static {
/*  68 */     int os = 0;
/*  69 */     String dfPath = "df";
/*     */     try {
/*  71 */       String osName = System.getProperty("os.name");
/*  72 */       if (osName == null) {
/*  73 */         throw new IOException("os.name not found");
/*     */       }
/*  75 */       osName = osName.toLowerCase(Locale.ENGLISH);
/*     */       
/*  77 */       if (osName.indexOf("windows") != -1) {
/*  78 */         os = 1;
/*  79 */       } else if (osName.indexOf("linux") != -1 || osName.indexOf("mpe/ix") != -1 || osName.indexOf("freebsd") != -1 || osName.indexOf("irix") != -1 || osName.indexOf("digital unix") != -1 || osName.indexOf("unix") != -1 || osName.indexOf("mac os x") != -1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  86 */         os = 2;
/*  87 */       } else if (osName.indexOf("sun os") != -1 || osName.indexOf("sunos") != -1 || osName.indexOf("solaris") != -1) {
/*     */ 
/*     */         
/*  90 */         os = 3;
/*  91 */         dfPath = "/usr/xpg4/bin/df";
/*  92 */       } else if (osName.indexOf("hp-ux") != -1 || osName.indexOf("aix") != -1) {
/*     */         
/*  94 */         os = 3;
/*     */       } else {
/*  96 */         os = 0;
/*     */       }
/*     */     
/*  99 */     } catch (Exception ex) {
/* 100 */       os = -1;
/*     */     } 
/* 102 */     OS = os;
/* 103 */     DF = dfPath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long freeSpace(String path) throws IOException {
/* 142 */     return INSTANCE.freeSpaceOS(path, OS, false, -1L);
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
/*     */   public static long freeSpaceKb(String path) throws IOException {
/* 171 */     return freeSpaceKb(path, -1L);
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
/*     */   public static long freeSpaceKb(String path, long timeout) throws IOException {
/* 200 */     return INSTANCE.freeSpaceOS(path, OS, true, timeout);
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
/*     */   public static long freeSpaceKb() throws IOException {
/* 216 */     return freeSpaceKb(-1L);
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
/*     */   public static long freeSpaceKb(long timeout) throws IOException {
/* 234 */     return freeSpaceKb((new File(".")).getAbsolutePath(), timeout);
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
/*     */   long freeSpaceOS(String path, int os, boolean kb, long timeout) throws IOException {
/* 259 */     if (path == null) {
/* 260 */       throw new IllegalArgumentException("Path must not be empty");
/*     */     }
/* 262 */     switch (os) {
/*     */       case 1:
/* 264 */         return kb ? (freeSpaceWindows(path, timeout) / 1024L) : freeSpaceWindows(path, timeout);
/*     */       case 2:
/* 266 */         return freeSpaceUnix(path, kb, false, timeout);
/*     */       case 3:
/* 268 */         return freeSpaceUnix(path, kb, true, timeout);
/*     */       case 0:
/* 270 */         throw new IllegalStateException("Unsupported operating system");
/*     */     } 
/* 272 */     throw new IllegalStateException("Exception caught when determining operating system");
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
/*     */   long freeSpaceWindows(String path, long timeout) throws IOException {
/* 288 */     path = FilenameUtils.normalize(path, false);
/* 289 */     if (path.length() > 0 && path.charAt(0) != '"') {
/* 290 */       path = "\"" + path + "\"";
/*     */     }
/*     */ 
/*     */     
/* 294 */     String[] cmdAttribs = { "cmd.exe", "/C", "dir /a /-c " + path };
/*     */ 
/*     */     
/* 297 */     List<String> lines = performCommand(cmdAttribs, 2147483647, timeout);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     for (int i = lines.size() - 1; i >= 0; i--) {
/* 304 */       String line = lines.get(i);
/* 305 */       if (line.length() > 0) {
/* 306 */         return parseDir(line, path);
/*     */       }
/*     */     } 
/*     */     
/* 310 */     throw new IOException("Command line 'dir /-c' did not return any info for path '" + path + "'");
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
/*     */   long parseDir(String line, String path) throws IOException {
/* 328 */     int bytesStart = 0;
/* 329 */     int bytesEnd = 0;
/* 330 */     int j = line.length() - 1;
/* 331 */     while (j >= 0) {
/* 332 */       char c = line.charAt(j);
/* 333 */       if (Character.isDigit(c)) {
/*     */ 
/*     */         
/* 336 */         bytesEnd = j + 1;
/*     */         break;
/*     */       } 
/* 339 */       j--;
/*     */     } 
/* 341 */     while (j >= 0) {
/* 342 */       char c = line.charAt(j);
/* 343 */       if (!Character.isDigit(c) && c != ',' && c != '.') {
/*     */ 
/*     */         
/* 346 */         bytesStart = j + 1;
/*     */         break;
/*     */       } 
/* 349 */       j--;
/*     */     } 
/* 351 */     if (j < 0) {
/* 352 */       throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
/* 359 */     for (int k = 0; k < buf.length(); k++) {
/* 360 */       if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
/* 361 */         buf.deleteCharAt(k--);
/*     */       }
/*     */     } 
/* 364 */     return parseBytes(buf.toString(), path);
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
/*     */   long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws IOException {
/* 380 */     if (path.length() == 0) {
/* 381 */       throw new IllegalArgumentException("Path must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 385 */     String flags = "-";
/* 386 */     if (kb) {
/* 387 */       flags = flags + "k";
/*     */     }
/* 389 */     if (posix) {
/* 390 */       flags = flags + "P";
/*     */     }
/* 392 */     (new String[3])[0] = DF; (new String[3])[1] = flags; (new String[3])[2] = path; (new String[2])[0] = DF; (new String[2])[1] = path; String[] cmdAttribs = (flags.length() > 1) ? new String[3] : new String[2];
/*     */ 
/*     */ 
/*     */     
/* 396 */     List<String> lines = performCommand(cmdAttribs, 3, timeout);
/* 397 */     if (lines.size() < 2)
/*     */     {
/* 399 */       throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + path + "'- response was " + lines);
/*     */     }
/*     */ 
/*     */     
/* 403 */     String line2 = lines.get(1);
/*     */ 
/*     */     
/* 406 */     StringTokenizer tok = new StringTokenizer(line2, " ");
/* 407 */     if (tok.countTokens() < 4) {
/*     */       
/* 409 */       if (tok.countTokens() == 1 && lines.size() >= 3) {
/* 410 */         String line3 = lines.get(2);
/* 411 */         tok = new StringTokenizer(line3, " ");
/*     */       } else {
/* 413 */         throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + path + "'- check path is valid");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 418 */       tok.nextToken();
/*     */     } 
/* 420 */     tok.nextToken();
/* 421 */     tok.nextToken();
/* 422 */     String freeSpace = tok.nextToken();
/* 423 */     return parseBytes(freeSpace, path);
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
/*     */   long parseBytes(String freeSpace, String path) throws IOException {
/*     */     try {
/* 437 */       long bytes = Long.parseLong(freeSpace);
/* 438 */       if (bytes < 0L) {
/* 439 */         throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + path + "'- check path is valid");
/*     */       }
/*     */ 
/*     */       
/* 443 */       return bytes;
/*     */     }
/* 445 */     catch (NumberFormatException ex) {
/* 446 */       throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + path + "'- check path is valid", ex);
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
/*     */   List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws IOException {
/* 472 */     List<String> lines = new ArrayList<String>(20);
/* 473 */     Process proc = null;
/* 474 */     InputStream in = null;
/* 475 */     OutputStream out = null;
/* 476 */     InputStream err = null;
/* 477 */     BufferedReader inr = null;
/*     */     
/*     */     try {
/* 480 */       Thread monitor = ThreadMonitor.start(timeout);
/*     */       
/* 482 */       proc = openProcess(cmdAttribs);
/* 483 */       in = proc.getInputStream();
/* 484 */       out = proc.getOutputStream();
/* 485 */       err = proc.getErrorStream();
/* 486 */       inr = new BufferedReader(new InputStreamReader(in));
/* 487 */       String line = inr.readLine();
/* 488 */       while (line != null && lines.size() < max) {
/* 489 */         line = line.toLowerCase(Locale.ENGLISH).trim();
/* 490 */         lines.add(line);
/* 491 */         line = inr.readLine();
/*     */       } 
/*     */       
/* 494 */       proc.waitFor();
/*     */       
/* 496 */       ThreadMonitor.stop(monitor);
/*     */       
/* 498 */       if (proc.exitValue() != 0)
/*     */       {
/* 500 */         throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command " + Arrays.asList(cmdAttribs));
/*     */       }
/*     */ 
/*     */       
/* 504 */       if (lines.isEmpty())
/*     */       {
/* 506 */         throw new IOException("Command line did not return any info for command " + Arrays.asList(cmdAttribs));
/*     */       }
/*     */ 
/*     */       
/* 510 */       return lines;
/*     */     }
/* 512 */     catch (InterruptedException ex) {
/* 513 */       throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 517 */       IOUtils.closeQuietly(in);
/* 518 */       IOUtils.closeQuietly(out);
/* 519 */       IOUtils.closeQuietly(err);
/* 520 */       IOUtils.closeQuietly(inr);
/* 521 */       if (proc != null) {
/* 522 */         proc.destroy();
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
/*     */   Process openProcess(String[] cmdAttribs) throws IOException {
/* 535 */     return Runtime.getRuntime().exec(cmdAttribs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\FileSystemUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */