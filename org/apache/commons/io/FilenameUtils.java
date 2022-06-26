/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Stack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FilenameUtils
/*      */ {
/*      */   public static final char EXTENSION_SEPARATOR = '.';
/*   95 */   public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char UNIX_SEPARATOR = '/';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char WINDOWS_SEPARATOR = '\\';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   private static final char SYSTEM_SEPARATOR = File.separatorChar;
/*      */ 
/*      */   
/*      */   private static final char OTHER_SEPARATOR;
/*      */ 
/*      */   
/*      */   static {
/*  117 */     if (isSystemWindows()) {
/*  118 */       OTHER_SEPARATOR = '/';
/*      */     } else {
/*  120 */       OTHER_SEPARATOR = '\\';
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
/*      */   static boolean isSystemWindows() {
/*  138 */     return (SYSTEM_SEPARATOR == '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isSeparator(char ch) {
/*  149 */     return (ch == '/' || ch == '\\');
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
/*      */   public static String normalize(String filename) {
/*  194 */     return doNormalize(filename, SYSTEM_SEPARATOR, true);
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
/*      */   public static String normalize(String filename, boolean unixSeparator) {
/*  241 */     char separator = unixSeparator ? '/' : '\\';
/*  242 */     return doNormalize(filename, separator, true);
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
/*      */   public static String normalizeNoEndSeparator(String filename) {
/*  288 */     return doNormalize(filename, SYSTEM_SEPARATOR, false);
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
/*      */   public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
/*  335 */     char separator = unixSeparator ? '/' : '\\';
/*  336 */     return doNormalize(filename, separator, false);
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
/*      */   private static String doNormalize(String filename, char separator, boolean keepSeparator) {
/*  348 */     if (filename == null) {
/*  349 */       return null;
/*      */     }
/*  351 */     int size = filename.length();
/*  352 */     if (size == 0) {
/*  353 */       return filename;
/*      */     }
/*  355 */     int prefix = getPrefixLength(filename);
/*  356 */     if (prefix < 0) {
/*  357 */       return null;
/*      */     }
/*      */     
/*  360 */     char[] array = new char[size + 2];
/*  361 */     filename.getChars(0, filename.length(), array, 0);
/*      */ 
/*      */     
/*  364 */     char otherSeparator = (separator == SYSTEM_SEPARATOR) ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
/*  365 */     for (int i = 0; i < array.length; i++) {
/*  366 */       if (array[i] == otherSeparator) {
/*  367 */         array[i] = separator;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  372 */     boolean lastIsDirectory = true;
/*  373 */     if (array[size - 1] != separator) {
/*  374 */       array[size++] = separator;
/*  375 */       lastIsDirectory = false;
/*      */     } 
/*      */     
/*      */     int j;
/*  379 */     for (j = prefix + 1; j < size; j++) {
/*  380 */       if (array[j] == separator && array[j - 1] == separator) {
/*  381 */         System.arraycopy(array, j, array, j - 1, size - j);
/*  382 */         size--;
/*  383 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  388 */     for (j = prefix + 1; j < size; j++) {
/*  389 */       if (array[j] == separator && array[j - 1] == '.' && (j == prefix + 1 || array[j - 2] == separator)) {
/*      */         
/*  391 */         if (j == size - 1) {
/*  392 */           lastIsDirectory = true;
/*      */         }
/*  394 */         System.arraycopy(array, j + 1, array, j - 1, size - j);
/*  395 */         size -= 2;
/*  396 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  402 */     for (j = prefix + 2; j < size; j++) {
/*  403 */       if (array[j] == separator && array[j - 1] == '.' && array[j - 2] == '.' && (j == prefix + 2 || array[j - 3] == separator)) {
/*      */         
/*  405 */         if (j == prefix + 2) {
/*  406 */           return null;
/*      */         }
/*  408 */         if (j == size - 1) {
/*  409 */           lastIsDirectory = true;
/*      */         }
/*      */         
/*  412 */         int k = j - 4; while (true) { if (k >= prefix) {
/*  413 */             if (array[k] == separator) {
/*      */               
/*  415 */               System.arraycopy(array, j + 1, array, k + 1, size - j);
/*  416 */               size -= j - k;
/*  417 */               j = k + 1; break;
/*      */             } 
/*      */             k--;
/*      */             continue;
/*      */           } 
/*  422 */           System.arraycopy(array, j + 1, array, prefix, size - j);
/*  423 */           size -= j + 1 - prefix;
/*  424 */           j = prefix + 1; break; }
/*      */       
/*      */       } 
/*      */     } 
/*  428 */     if (size <= 0) {
/*  429 */       return "";
/*      */     }
/*  431 */     if (size <= prefix) {
/*  432 */       return new String(array, 0, size);
/*      */     }
/*  434 */     if (lastIsDirectory && keepSeparator) {
/*  435 */       return new String(array, 0, size);
/*      */     }
/*  437 */     return new String(array, 0, size - 1);
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
/*      */   public static String concat(String basePath, String fullFilenameToAdd) {
/*  482 */     int prefix = getPrefixLength(fullFilenameToAdd);
/*  483 */     if (prefix < 0) {
/*  484 */       return null;
/*      */     }
/*  486 */     if (prefix > 0) {
/*  487 */       return normalize(fullFilenameToAdd);
/*      */     }
/*  489 */     if (basePath == null) {
/*  490 */       return null;
/*      */     }
/*  492 */     int len = basePath.length();
/*  493 */     if (len == 0) {
/*  494 */       return normalize(fullFilenameToAdd);
/*      */     }
/*  496 */     char ch = basePath.charAt(len - 1);
/*  497 */     if (isSeparator(ch)) {
/*  498 */       return normalize(basePath + fullFilenameToAdd);
/*      */     }
/*  500 */     return normalize(basePath + '/' + fullFilenameToAdd);
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
/*      */   public static boolean directoryContains(String canonicalParent, String canonicalChild) throws IOException {
/*  531 */     if (canonicalParent == null) {
/*  532 */       throw new IllegalArgumentException("Directory must not be null");
/*      */     }
/*      */     
/*  535 */     if (canonicalChild == null) {
/*  536 */       return false;
/*      */     }
/*      */     
/*  539 */     if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
/*  540 */       return false;
/*      */     }
/*      */     
/*  543 */     return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToUnix(String path) {
/*  554 */     if (path == null || path.indexOf('\\') == -1) {
/*  555 */       return path;
/*      */     }
/*  557 */     return path.replace('\\', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToWindows(String path) {
/*  567 */     if (path == null || path.indexOf('/') == -1) {
/*  568 */       return path;
/*      */     }
/*  570 */     return path.replace('/', '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToSystem(String path) {
/*  580 */     if (path == null) {
/*  581 */       return null;
/*      */     }
/*  583 */     if (isSystemWindows()) {
/*  584 */       return separatorsToWindows(path);
/*      */     }
/*  586 */     return separatorsToUnix(path);
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
/*      */   public static int getPrefixLength(String filename) {
/*  623 */     if (filename == null) {
/*  624 */       return -1;
/*      */     }
/*  626 */     int len = filename.length();
/*  627 */     if (len == 0) {
/*  628 */       return 0;
/*      */     }
/*  630 */     char ch0 = filename.charAt(0);
/*  631 */     if (ch0 == ':') {
/*  632 */       return -1;
/*      */     }
/*  634 */     if (len == 1) {
/*  635 */       if (ch0 == '~') {
/*  636 */         return 2;
/*      */       }
/*  638 */       return isSeparator(ch0) ? 1 : 0;
/*      */     } 
/*  640 */     if (ch0 == '~') {
/*  641 */       int posUnix = filename.indexOf('/', 1);
/*  642 */       int posWin = filename.indexOf('\\', 1);
/*  643 */       if (posUnix == -1 && posWin == -1) {
/*  644 */         return len + 1;
/*      */       }
/*  646 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/*  647 */       posWin = (posWin == -1) ? posUnix : posWin;
/*  648 */       return Math.min(posUnix, posWin) + 1;
/*      */     } 
/*  650 */     char ch1 = filename.charAt(1);
/*  651 */     if (ch1 == ':') {
/*  652 */       ch0 = Character.toUpperCase(ch0);
/*  653 */       if (ch0 >= 'A' && ch0 <= 'Z') {
/*  654 */         if (len == 2 || !isSeparator(filename.charAt(2))) {
/*  655 */           return 2;
/*      */         }
/*  657 */         return 3;
/*      */       } 
/*  659 */       return -1;
/*      */     } 
/*  661 */     if (isSeparator(ch0) && isSeparator(ch1)) {
/*  662 */       int posUnix = filename.indexOf('/', 2);
/*  663 */       int posWin = filename.indexOf('\\', 2);
/*  664 */       if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
/*  665 */         return -1;
/*      */       }
/*  667 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/*  668 */       posWin = (posWin == -1) ? posUnix : posWin;
/*  669 */       return Math.min(posUnix, posWin) + 1;
/*      */     } 
/*  671 */     return isSeparator(ch0) ? 1 : 0;
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
/*      */   public static int indexOfLastSeparator(String filename) {
/*  689 */     if (filename == null) {
/*  690 */       return -1;
/*      */     }
/*  692 */     int lastUnixPos = filename.lastIndexOf('/');
/*  693 */     int lastWindowsPos = filename.lastIndexOf('\\');
/*  694 */     return Math.max(lastUnixPos, lastWindowsPos);
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
/*      */   public static int indexOfExtension(String filename) {
/*  711 */     if (filename == null) {
/*  712 */       return -1;
/*      */     }
/*  714 */     int extensionPos = filename.lastIndexOf('.');
/*  715 */     int lastSeparator = indexOfLastSeparator(filename);
/*  716 */     return (lastSeparator > extensionPos) ? -1 : extensionPos;
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
/*      */   public static String getPrefix(String filename) {
/*  750 */     if (filename == null) {
/*  751 */       return null;
/*      */     }
/*  753 */     int len = getPrefixLength(filename);
/*  754 */     if (len < 0) {
/*  755 */       return null;
/*      */     }
/*  757 */     if (len > filename.length()) {
/*  758 */       return filename + '/';
/*      */     }
/*  760 */     return filename.substring(0, len);
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
/*      */   public static String getPath(String filename) {
/*  786 */     return doGetPath(filename, 1);
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
/*      */   public static String getPathNoEndSeparator(String filename) {
/*  813 */     return doGetPath(filename, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetPath(String filename, int separatorAdd) {
/*  824 */     if (filename == null) {
/*  825 */       return null;
/*      */     }
/*  827 */     int prefix = getPrefixLength(filename);
/*  828 */     if (prefix < 0) {
/*  829 */       return null;
/*      */     }
/*  831 */     int index = indexOfLastSeparator(filename);
/*  832 */     int endIndex = index + separatorAdd;
/*  833 */     if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
/*  834 */       return "";
/*      */     }
/*  836 */     return filename.substring(prefix, endIndex);
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
/*      */   public static String getFullPath(String filename) {
/*  865 */     return doGetFullPath(filename, true);
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
/*      */   public static String getFullPathNoEndSeparator(String filename) {
/*  895 */     return doGetFullPath(filename, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetFullPath(String filename, boolean includeSeparator) {
/*  906 */     if (filename == null) {
/*  907 */       return null;
/*      */     }
/*  909 */     int prefix = getPrefixLength(filename);
/*  910 */     if (prefix < 0) {
/*  911 */       return null;
/*      */     }
/*  913 */     if (prefix >= filename.length()) {
/*  914 */       if (includeSeparator) {
/*  915 */         return getPrefix(filename);
/*      */       }
/*  917 */       return filename;
/*      */     } 
/*      */     
/*  920 */     int index = indexOfLastSeparator(filename);
/*  921 */     if (index < 0) {
/*  922 */       return filename.substring(0, prefix);
/*      */     }
/*  924 */     int end = index + (includeSeparator ? 1 : 0);
/*  925 */     if (end == 0) {
/*  926 */       end++;
/*      */     }
/*  928 */     return filename.substring(0, end);
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
/*      */   public static String getName(String filename) {
/*  949 */     if (filename == null) {
/*  950 */       return null;
/*      */     }
/*  952 */     int index = indexOfLastSeparator(filename);
/*  953 */     return filename.substring(index + 1);
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
/*      */   public static String getBaseName(String filename) {
/*  974 */     return removeExtension(getName(filename));
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
/*      */   public static String getExtension(String filename) {
/*  996 */     if (filename == null) {
/*  997 */       return null;
/*      */     }
/*  999 */     int index = indexOfExtension(filename);
/* 1000 */     if (index == -1) {
/* 1001 */       return "";
/*      */     }
/* 1003 */     return filename.substring(index + 1);
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
/*      */   public static String removeExtension(String filename) {
/* 1026 */     if (filename == null) {
/* 1027 */       return null;
/*      */     }
/* 1029 */     int index = indexOfExtension(filename);
/* 1030 */     if (index == -1) {
/* 1031 */       return filename;
/*      */     }
/* 1033 */     return filename.substring(0, index);
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
/*      */   public static boolean equals(String filename1, String filename2) {
/* 1050 */     return equals(filename1, filename2, false, IOCase.SENSITIVE);
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
/*      */   public static boolean equalsOnSystem(String filename1, String filename2) {
/* 1065 */     return equals(filename1, filename2, false, IOCase.SYSTEM);
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
/*      */   public static boolean equalsNormalized(String filename1, String filename2) {
/* 1081 */     return equals(filename1, filename2, true, IOCase.SENSITIVE);
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
/*      */   public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
/* 1098 */     return equals(filename1, filename2, true, IOCase.SYSTEM);
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
/*      */   public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
/* 1116 */     if (filename1 == null || filename2 == null) {
/* 1117 */       return (filename1 == null && filename2 == null);
/*      */     }
/* 1119 */     if (normalized) {
/* 1120 */       filename1 = normalize(filename1);
/* 1121 */       filename2 = normalize(filename2);
/* 1122 */       if (filename1 == null || filename2 == null) {
/* 1123 */         throw new NullPointerException("Error normalizing one or both of the file names");
/*      */       }
/*      */     } 
/*      */     
/* 1127 */     if (caseSensitivity == null) {
/* 1128 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1130 */     return caseSensitivity.checkEquals(filename1, filename2);
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
/*      */   public static boolean isExtension(String filename, String extension) {
/* 1146 */     if (filename == null) {
/* 1147 */       return false;
/*      */     }
/* 1149 */     if (extension == null || extension.length() == 0) {
/* 1150 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1152 */     String fileExt = getExtension(filename);
/* 1153 */     return fileExt.equals(extension);
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
/*      */   public static boolean isExtension(String filename, String[] extensions) {
/* 1168 */     if (filename == null) {
/* 1169 */       return false;
/*      */     }
/* 1171 */     if (extensions == null || extensions.length == 0) {
/* 1172 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1174 */     String fileExt = getExtension(filename);
/* 1175 */     for (String extension : extensions) {
/* 1176 */       if (fileExt.equals(extension)) {
/* 1177 */         return true;
/*      */       }
/*      */     } 
/* 1180 */     return false;
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
/*      */   public static boolean isExtension(String filename, Collection<String> extensions) {
/* 1195 */     if (filename == null) {
/* 1196 */       return false;
/*      */     }
/* 1198 */     if (extensions == null || extensions.isEmpty()) {
/* 1199 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1201 */     String fileExt = getExtension(filename);
/* 1202 */     for (String extension : extensions) {
/* 1203 */       if (fileExt.equals(extension)) {
/* 1204 */         return true;
/*      */       }
/*      */     } 
/* 1207 */     return false;
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
/*      */   public static boolean wildcardMatch(String filename, String wildcardMatcher) {
/* 1234 */     return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
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
/*      */   public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
/* 1260 */     return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
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
/*      */   public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
/* 1278 */     if (filename == null && wildcardMatcher == null) {
/* 1279 */       return true;
/*      */     }
/* 1281 */     if (filename == null || wildcardMatcher == null) {
/* 1282 */       return false;
/*      */     }
/* 1284 */     if (caseSensitivity == null) {
/* 1285 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1287 */     String[] wcs = splitOnTokens(wildcardMatcher);
/* 1288 */     boolean anyChars = false;
/* 1289 */     int textIdx = 0;
/* 1290 */     int wcsIdx = 0;
/* 1291 */     Stack<int[]> backtrack = (Stack)new Stack<int>();
/*      */ 
/*      */     
/*      */     do {
/* 1295 */       if (backtrack.size() > 0) {
/* 1296 */         int[] array = backtrack.pop();
/* 1297 */         wcsIdx = array[0];
/* 1298 */         textIdx = array[1];
/* 1299 */         anyChars = true;
/*      */       } 
/*      */ 
/*      */       
/* 1303 */       while (wcsIdx < wcs.length) {
/*      */         
/* 1305 */         if (wcs[wcsIdx].equals("?")) {
/*      */           
/* 1307 */           textIdx++;
/* 1308 */           if (textIdx > filename.length()) {
/*      */             break;
/*      */           }
/* 1311 */           anyChars = false;
/*      */         }
/* 1313 */         else if (wcs[wcsIdx].equals("*")) {
/*      */           
/* 1315 */           anyChars = true;
/* 1316 */           if (wcsIdx == wcs.length - 1) {
/* 1317 */             textIdx = filename.length();
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 1322 */           if (anyChars) {
/*      */             
/* 1324 */             textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
/* 1325 */             if (textIdx == -1) {
/*      */               break;
/*      */             }
/*      */             
/* 1329 */             int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
/* 1330 */             if (repeat >= 0) {
/* 1331 */               backtrack.push(new int[] { wcsIdx, repeat });
/*      */             
/*      */             }
/*      */           }
/* 1335 */           else if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
/*      */             break;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1342 */           textIdx += wcs[wcsIdx].length();
/* 1343 */           anyChars = false;
/*      */         } 
/*      */         
/* 1346 */         wcsIdx++;
/*      */       } 
/*      */ 
/*      */       
/* 1350 */       if (wcsIdx == wcs.length && textIdx == filename.length()) {
/* 1351 */         return true;
/*      */       }
/*      */     }
/* 1354 */     while (backtrack.size() > 0);
/*      */     
/* 1356 */     return false;
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
/*      */   static String[] splitOnTokens(String text) {
/* 1371 */     if (text.indexOf('?') == -1 && text.indexOf('*') == -1) {
/* 1372 */       return new String[] { text };
/*      */     }
/*      */     
/* 1375 */     char[] array = text.toCharArray();
/* 1376 */     ArrayList<String> list = new ArrayList<String>();
/* 1377 */     StringBuilder buffer = new StringBuilder();
/* 1378 */     for (int i = 0; i < array.length; i++) {
/* 1379 */       if (array[i] == '?' || array[i] == '*') {
/* 1380 */         if (buffer.length() != 0) {
/* 1381 */           list.add(buffer.toString());
/* 1382 */           buffer.setLength(0);
/*      */         } 
/* 1384 */         if (array[i] == '?') {
/* 1385 */           list.add("?");
/* 1386 */         } else if (list.isEmpty() || (i > 0 && !((String)list.get(list.size() - 1)).equals("*"))) {
/*      */           
/* 1388 */           list.add("*");
/*      */         } 
/*      */       } else {
/* 1391 */         buffer.append(array[i]);
/*      */       } 
/*      */     } 
/* 1394 */     if (buffer.length() != 0) {
/* 1395 */       list.add(buffer.toString());
/*      */     }
/*      */     
/* 1398 */     return list.<String>toArray(new String[list.size()]);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\FilenameUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */