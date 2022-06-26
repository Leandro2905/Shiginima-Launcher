/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.text.Normalizer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   public static final String SPACE = " ";
/*      */   public static final String EMPTY = "";
/*      */   public static final String LF = "\n";
/*      */   public static final String CR = "\r";
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */   private static final int PAD_LIMIT = 8192;
/*      */   
/*      */   public static boolean isEmpty(CharSequence cs) {
/*  209 */     return (cs == null || cs.length() == 0);
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
/*      */   public static boolean isNotEmpty(CharSequence cs) {
/*  228 */     return !isEmpty(cs);
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
/*      */   public static boolean isAnyEmpty(CharSequence... css) {
/*  249 */     if (ArrayUtils.isEmpty((Object[])css)) {
/*  250 */       return true;
/*      */     }
/*  252 */     for (CharSequence cs : css) {
/*  253 */       if (isEmpty(cs)) {
/*  254 */         return true;
/*      */       }
/*      */     } 
/*  257 */     return false;
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
/*      */   public static boolean isNoneEmpty(CharSequence... css) {
/*  278 */     return !isAnyEmpty(css);
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
/*      */   public static boolean isBlank(CharSequence cs) {
/*      */     int strLen;
/*  298 */     if (cs == null || (strLen = cs.length()) == 0) {
/*  299 */       return true;
/*      */     }
/*  301 */     for (int i = 0; i < strLen; i++) {
/*  302 */       if (!Character.isWhitespace(cs.charAt(i))) {
/*  303 */         return false;
/*      */       }
/*      */     } 
/*  306 */     return true;
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
/*      */   public static boolean isNotBlank(CharSequence cs) {
/*  327 */     return !isBlank(cs);
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
/*      */   public static boolean isAnyBlank(CharSequence... css) {
/*  349 */     if (ArrayUtils.isEmpty((Object[])css)) {
/*  350 */       return true;
/*      */     }
/*  352 */     for (CharSequence cs : css) {
/*  353 */       if (isBlank(cs)) {
/*  354 */         return true;
/*      */       }
/*      */     } 
/*  357 */     return false;
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
/*      */   public static boolean isNoneBlank(CharSequence... css) {
/*  379 */     return !isAnyBlank(css);
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
/*      */   public static String trim(String str) {
/*  408 */     return (str == null) ? null : str.trim();
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
/*      */   public static String trimToNull(String str) {
/*  434 */     String ts = trim(str);
/*  435 */     return isEmpty(ts) ? null : ts;
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
/*      */   public static String trimToEmpty(String str) {
/*  460 */     return (str == null) ? "" : str.trim();
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
/*      */   public static String strip(String str) {
/*  488 */     return strip(str, null);
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
/*      */   public static String stripToNull(String str) {
/*  515 */     if (str == null) {
/*  516 */       return null;
/*      */     }
/*  518 */     str = strip(str, null);
/*  519 */     return str.isEmpty() ? null : str;
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
/*      */   public static String stripToEmpty(String str) {
/*  545 */     return (str == null) ? "" : strip(str, null);
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
/*      */   public static String strip(String str, String stripChars) {
/*  575 */     if (isEmpty(str)) {
/*  576 */       return str;
/*      */     }
/*  578 */     str = stripStart(str, stripChars);
/*  579 */     return stripEnd(str, stripChars);
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
/*      */   public static String stripStart(String str, String stripChars) {
/*      */     int strLen;
/*  608 */     if (str == null || (strLen = str.length()) == 0) {
/*  609 */       return str;
/*      */     }
/*  611 */     int start = 0;
/*  612 */     if (stripChars == null) {
/*  613 */       while (start != strLen && Character.isWhitespace(str.charAt(start)))
/*  614 */         start++; 
/*      */     } else {
/*  616 */       if (stripChars.isEmpty()) {
/*  617 */         return str;
/*      */       }
/*  619 */       while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
/*  620 */         start++;
/*      */       }
/*      */     } 
/*  623 */     return str.substring(start);
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
/*      */   public static String stripEnd(String str, String stripChars) {
/*      */     int end;
/*  653 */     if (str == null || (end = str.length()) == 0) {
/*  654 */       return str;
/*      */     }
/*      */     
/*  657 */     if (stripChars == null) {
/*  658 */       while (end != 0 && Character.isWhitespace(str.charAt(end - 1)))
/*  659 */         end--; 
/*      */     } else {
/*  661 */       if (stripChars.isEmpty()) {
/*  662 */         return str;
/*      */       }
/*  664 */       while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
/*  665 */         end--;
/*      */       }
/*      */     } 
/*  668 */     return str.substring(0, end);
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
/*      */   public static String[] stripAll(String... strs) {
/*  693 */     return stripAll(strs, null);
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
/*      */   public static String[] stripAll(String[] strs, String stripChars) {
/*      */     int strsLen;
/*  723 */     if (strs == null || (strsLen = strs.length) == 0) {
/*  724 */       return strs;
/*      */     }
/*  726 */     String[] newArr = new String[strsLen];
/*  727 */     for (int i = 0; i < strsLen; i++) {
/*  728 */       newArr[i] = strip(strs[i], stripChars);
/*      */     }
/*  730 */     return newArr;
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
/*      */   public static String stripAccents(String input) {
/*  752 */     if (input == null) {
/*  753 */       return null;
/*      */     }
/*  755 */     Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
/*  756 */     String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
/*      */     
/*  758 */     return pattern.matcher(decomposed).replaceAll("");
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
/*      */   public static boolean equals(CharSequence cs1, CharSequence cs2) {
/*  785 */     if (cs1 == cs2) {
/*  786 */       return true;
/*      */     }
/*  788 */     if (cs1 == null || cs2 == null) {
/*  789 */       return false;
/*      */     }
/*  791 */     if (cs1 instanceof String && cs2 instanceof String) {
/*  792 */       return cs1.equals(cs2);
/*      */     }
/*  794 */     return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
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
/*      */   public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
/*  819 */     if (str1 == null || str2 == null)
/*  820 */       return (str1 == str2); 
/*  821 */     if (str1 == str2)
/*  822 */       return true; 
/*  823 */     if (str1.length() != str2.length()) {
/*  824 */       return false;
/*      */     }
/*  826 */     return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
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
/*      */   public static int indexOf(CharSequence seq, int searchChar) {
/*  853 */     if (isEmpty(seq)) {
/*  854 */       return -1;
/*      */     }
/*  856 */     return CharSequenceUtils.indexOf(seq, searchChar, 0);
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
/*      */   public static int indexOf(CharSequence seq, int searchChar, int startPos) {
/*  886 */     if (isEmpty(seq)) {
/*  887 */       return -1;
/*      */     }
/*  889 */     return CharSequenceUtils.indexOf(seq, searchChar, startPos);
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq) {
/*  917 */     if (seq == null || searchSeq == null) {
/*  918 */       return -1;
/*      */     }
/*  920 */     return CharSequenceUtils.indexOf(seq, searchSeq, 0);
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/*  957 */     if (seq == null || searchSeq == null) {
/*  958 */       return -1;
/*      */     }
/*  960 */     return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
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
/*      */   public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/*  998 */     return ordinalIndexOf(str, searchStr, ordinal, false);
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
/*      */   private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
/* 1016 */     if (str == null || searchStr == null || ordinal <= 0) {
/* 1017 */       return -1;
/*      */     }
/* 1019 */     if (searchStr.length() == 0) {
/* 1020 */       return lastIndex ? str.length() : 0;
/*      */     }
/* 1022 */     int found = 0;
/* 1023 */     int index = lastIndex ? str.length() : -1;
/*      */     while (true) {
/* 1025 */       if (lastIndex) {
/* 1026 */         index = CharSequenceUtils.lastIndexOf(str, searchStr, index - searchStr.length());
/*      */       } else {
/* 1028 */         index = CharSequenceUtils.indexOf(str, searchStr, index + searchStr.length());
/*      */       } 
/* 1030 */       if (index < 0) {
/* 1031 */         return index;
/*      */       }
/* 1033 */       found++;
/* 1034 */       if (found >= ordinal) {
/* 1035 */         return index;
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1064 */     return indexOfIgnoreCase(str, searchStr, 0);
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 1100 */     if (str == null || searchStr == null) {
/* 1101 */       return -1;
/*      */     }
/* 1103 */     if (startPos < 0) {
/* 1104 */       startPos = 0;
/*      */     }
/* 1106 */     int endLimit = str.length() - searchStr.length() + 1;
/* 1107 */     if (startPos > endLimit) {
/* 1108 */       return -1;
/*      */     }
/* 1110 */     if (searchStr.length() == 0) {
/* 1111 */       return startPos;
/*      */     }
/* 1113 */     for (int i = startPos; i < endLimit; i++) {
/* 1114 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
/* 1115 */         return i;
/*      */       }
/*      */     } 
/* 1118 */     return -1;
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
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar) {
/* 1144 */     if (isEmpty(seq)) {
/* 1145 */       return -1;
/*      */     }
/* 1147 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
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
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
/* 1182 */     if (isEmpty(seq)) {
/* 1183 */       return -1;
/*      */     }
/* 1185 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
/* 1212 */     if (seq == null || searchSeq == null) {
/* 1213 */       return -1;
/*      */     }
/* 1215 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
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
/*      */   public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/* 1253 */     return ordinalIndexOf(str, searchStr, ordinal, true);
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/* 1293 */     if (seq == null || searchSeq == null) {
/* 1294 */       return -1;
/*      */     }
/* 1296 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1323 */     if (str == null || searchStr == null) {
/* 1324 */       return -1;
/*      */     }
/* 1326 */     return lastIndexOfIgnoreCase(str, searchStr, str.length());
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 1362 */     if (str == null || searchStr == null) {
/* 1363 */       return -1;
/*      */     }
/* 1365 */     if (startPos > str.length() - searchStr.length()) {
/* 1366 */       startPos = str.length() - searchStr.length();
/*      */     }
/* 1368 */     if (startPos < 0) {
/* 1369 */       return -1;
/*      */     }
/* 1371 */     if (searchStr.length() == 0) {
/* 1372 */       return startPos;
/*      */     }
/*      */     
/* 1375 */     for (int i = startPos; i >= 0; i--) {
/* 1376 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
/* 1377 */         return i;
/*      */       }
/*      */     } 
/* 1380 */     return -1;
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
/*      */   public static boolean contains(CharSequence seq, int searchChar) {
/* 1406 */     if (isEmpty(seq)) {
/* 1407 */       return false;
/*      */     }
/* 1409 */     return (CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0);
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
/*      */   public static boolean contains(CharSequence seq, CharSequence searchSeq) {
/* 1435 */     if (seq == null || searchSeq == null) {
/* 1436 */       return false;
/*      */     }
/* 1438 */     return (CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0);
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
/*      */   public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1466 */     if (str == null || searchStr == null) {
/* 1467 */       return false;
/*      */     }
/* 1469 */     int len = searchStr.length();
/* 1470 */     int max = str.length() - len;
/* 1471 */     for (int i = 0; i <= max; i++) {
/* 1472 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
/* 1473 */         return true;
/*      */       }
/*      */     } 
/* 1476 */     return false;
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
/*      */   public static boolean containsWhitespace(CharSequence seq) {
/* 1489 */     if (isEmpty(seq)) {
/* 1490 */       return false;
/*      */     }
/* 1492 */     int strLen = seq.length();
/* 1493 */     for (int i = 0; i < strLen; i++) {
/* 1494 */       if (Character.isWhitespace(seq.charAt(i))) {
/* 1495 */         return true;
/*      */       }
/*      */     } 
/* 1498 */     return false;
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
/*      */   public static int indexOfAny(CharSequence cs, char... searchChars) {
/* 1527 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1528 */       return -1;
/*      */     }
/* 1530 */     int csLen = cs.length();
/* 1531 */     int csLast = csLen - 1;
/* 1532 */     int searchLen = searchChars.length;
/* 1533 */     int searchLast = searchLen - 1;
/* 1534 */     for (int i = 0; i < csLen; i++) {
/* 1535 */       char ch = cs.charAt(i);
/* 1536 */       for (int j = 0; j < searchLen; j++) {
/* 1537 */         if (searchChars[j] == ch) {
/* 1538 */           if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
/*      */             
/* 1540 */             if (searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1541 */               return i;
/*      */             }
/*      */           } else {
/* 1544 */             return i;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1549 */     return -1;
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
/*      */   public static int indexOfAny(CharSequence cs, String searchChars) {
/* 1576 */     if (isEmpty(cs) || isEmpty(searchChars)) {
/* 1577 */       return -1;
/*      */     }
/* 1579 */     return indexOfAny(cs, searchChars.toCharArray());
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
/*      */   public static boolean containsAny(CharSequence cs, char... searchChars) {
/* 1609 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1610 */       return false;
/*      */     }
/* 1612 */     int csLength = cs.length();
/* 1613 */     int searchLength = searchChars.length;
/* 1614 */     int csLast = csLength - 1;
/* 1615 */     int searchLast = searchLength - 1;
/* 1616 */     for (int i = 0; i < csLength; i++) {
/* 1617 */       char ch = cs.charAt(i);
/* 1618 */       for (int j = 0; j < searchLength; j++) {
/* 1619 */         if (searchChars[j] == ch) {
/* 1620 */           if (Character.isHighSurrogate(ch)) {
/* 1621 */             if (j == searchLast)
/*      */             {
/* 1623 */               return true;
/*      */             }
/* 1625 */             if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1626 */               return true;
/*      */             }
/*      */           } else {
/*      */             
/* 1630 */             return true;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1635 */     return false;
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
/* 1667 */     if (searchChars == null) {
/* 1668 */       return false;
/*      */     }
/* 1670 */     return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
/* 1697 */     if (isEmpty(cs) || ArrayUtils.isEmpty((Object[])searchCharSequences)) {
/* 1698 */       return false;
/*      */     }
/* 1700 */     for (CharSequence searchCharSequence : searchCharSequences) {
/* 1701 */       if (contains(cs, searchCharSequence)) {
/* 1702 */         return true;
/*      */       }
/*      */     } 
/* 1705 */     return false;
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
/*      */   public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
/* 1735 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1736 */       return -1;
/*      */     }
/* 1738 */     int csLen = cs.length();
/* 1739 */     int csLast = csLen - 1;
/* 1740 */     int searchLen = searchChars.length;
/* 1741 */     int searchLast = searchLen - 1;
/*      */     
/* 1743 */     for (int i = 0; i < csLen; i++) {
/* 1744 */       char ch = cs.charAt(i);
/* 1745 */       int j = 0; while (true) { if (j < searchLen) {
/* 1746 */           if (searchChars[j] == ch && (
/* 1747 */             i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch) || 
/* 1748 */             searchChars[j + 1] == cs.charAt(i + 1))) {
/*      */             break;
/*      */           }
/*      */           
/*      */           j++;
/*      */           
/*      */           continue;
/*      */         } 
/* 1756 */         return i; }
/*      */     
/* 1758 */     }  return -1;
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
/*      */   public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
/* 1785 */     if (isEmpty(seq) || isEmpty(searchChars)) {
/* 1786 */       return -1;
/*      */     }
/* 1788 */     int strLen = seq.length();
/* 1789 */     for (int i = 0; i < strLen; i++) {
/* 1790 */       char ch = seq.charAt(i);
/* 1791 */       boolean chFound = (CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0);
/* 1792 */       if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
/* 1793 */         char ch2 = seq.charAt(i + 1);
/* 1794 */         if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
/* 1795 */           return i;
/*      */         }
/*      */       }
/* 1798 */       else if (!chFound) {
/* 1799 */         return i;
/*      */       } 
/*      */     } 
/*      */     
/* 1803 */     return -1;
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
/*      */   public static boolean containsOnly(CharSequence cs, char... valid) {
/* 1832 */     if (valid == null || cs == null) {
/* 1833 */       return false;
/*      */     }
/* 1835 */     if (cs.length() == 0) {
/* 1836 */       return true;
/*      */     }
/* 1838 */     if (valid.length == 0) {
/* 1839 */       return false;
/*      */     }
/* 1841 */     return (indexOfAnyBut(cs, valid) == -1);
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
/*      */   public static boolean containsOnly(CharSequence cs, String validChars) {
/* 1868 */     if (cs == null || validChars == null) {
/* 1869 */       return false;
/*      */     }
/* 1871 */     return containsOnly(cs, validChars.toCharArray());
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
/*      */   public static boolean containsNone(CharSequence cs, char... searchChars) {
/* 1900 */     if (cs == null || searchChars == null) {
/* 1901 */       return true;
/*      */     }
/* 1903 */     int csLen = cs.length();
/* 1904 */     int csLast = csLen - 1;
/* 1905 */     int searchLen = searchChars.length;
/* 1906 */     int searchLast = searchLen - 1;
/* 1907 */     for (int i = 0; i < csLen; i++) {
/* 1908 */       char ch = cs.charAt(i);
/* 1909 */       for (int j = 0; j < searchLen; j++) {
/* 1910 */         if (searchChars[j] == ch) {
/* 1911 */           if (Character.isHighSurrogate(ch)) {
/* 1912 */             if (j == searchLast)
/*      */             {
/* 1914 */               return false;
/*      */             }
/* 1916 */             if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1917 */               return false;
/*      */             }
/*      */           } else {
/*      */             
/* 1921 */             return false;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1926 */     return true;
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
/*      */   public static boolean containsNone(CharSequence cs, String invalidChars) {
/* 1953 */     if (cs == null || invalidChars == null) {
/* 1954 */       return true;
/*      */     }
/* 1956 */     return containsNone(cs, invalidChars.toCharArray());
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
/*      */   public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 1989 */     if (str == null || searchStrs == null) {
/* 1990 */       return -1;
/*      */     }
/* 1992 */     int sz = searchStrs.length;
/*      */ 
/*      */     
/* 1995 */     int ret = Integer.MAX_VALUE;
/*      */     
/* 1997 */     int tmp = 0;
/* 1998 */     for (int i = 0; i < sz; i++) {
/* 1999 */       CharSequence search = searchStrs[i];
/* 2000 */       if (search != null) {
/*      */ 
/*      */         
/* 2003 */         tmp = CharSequenceUtils.indexOf(str, search, 0);
/* 2004 */         if (tmp != -1)
/*      */         {
/*      */ 
/*      */           
/* 2008 */           if (tmp < ret)
/* 2009 */             ret = tmp; 
/*      */         }
/*      */       } 
/*      */     } 
/* 2013 */     return (ret == Integer.MAX_VALUE) ? -1 : ret;
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
/*      */   public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 2043 */     if (str == null || searchStrs == null) {
/* 2044 */       return -1;
/*      */     }
/* 2046 */     int sz = searchStrs.length;
/* 2047 */     int ret = -1;
/* 2048 */     int tmp = 0;
/* 2049 */     for (int i = 0; i < sz; i++) {
/* 2050 */       CharSequence search = searchStrs[i];
/* 2051 */       if (search != null) {
/*      */ 
/*      */         
/* 2054 */         tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
/* 2055 */         if (tmp > ret)
/* 2056 */           ret = tmp; 
/*      */       } 
/*      */     } 
/* 2059 */     return ret;
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
/*      */   public static String substring(String str, int start) {
/* 2089 */     if (str == null) {
/* 2090 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2094 */     if (start < 0) {
/* 2095 */       start = str.length() + start;
/*      */     }
/*      */     
/* 2098 */     if (start < 0) {
/* 2099 */       start = 0;
/*      */     }
/* 2101 */     if (start > str.length()) {
/* 2102 */       return "";
/*      */     }
/*      */     
/* 2105 */     return str.substring(start);
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
/*      */   public static String substring(String str, int start, int end) {
/* 2144 */     if (str == null) {
/* 2145 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2149 */     if (end < 0) {
/* 2150 */       end = str.length() + end;
/*      */     }
/* 2152 */     if (start < 0) {
/* 2153 */       start = str.length() + start;
/*      */     }
/*      */ 
/*      */     
/* 2157 */     if (end > str.length()) {
/* 2158 */       end = str.length();
/*      */     }
/*      */ 
/*      */     
/* 2162 */     if (start > end) {
/* 2163 */       return "";
/*      */     }
/*      */     
/* 2166 */     if (start < 0) {
/* 2167 */       start = 0;
/*      */     }
/* 2169 */     if (end < 0) {
/* 2170 */       end = 0;
/*      */     }
/*      */     
/* 2173 */     return str.substring(start, end);
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
/*      */   public static String left(String str, int len) {
/* 2199 */     if (str == null) {
/* 2200 */       return null;
/*      */     }
/* 2202 */     if (len < 0) {
/* 2203 */       return "";
/*      */     }
/* 2205 */     if (str.length() <= len) {
/* 2206 */       return str;
/*      */     }
/* 2208 */     return str.substring(0, len);
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
/*      */   public static String right(String str, int len) {
/* 2232 */     if (str == null) {
/* 2233 */       return null;
/*      */     }
/* 2235 */     if (len < 0) {
/* 2236 */       return "";
/*      */     }
/* 2238 */     if (str.length() <= len) {
/* 2239 */       return str;
/*      */     }
/* 2241 */     return str.substring(str.length() - len);
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
/*      */   public static String mid(String str, int pos, int len) {
/* 2270 */     if (str == null) {
/* 2271 */       return null;
/*      */     }
/* 2273 */     if (len < 0 || pos > str.length()) {
/* 2274 */       return "";
/*      */     }
/* 2276 */     if (pos < 0) {
/* 2277 */       pos = 0;
/*      */     }
/* 2279 */     if (str.length() <= pos + len) {
/* 2280 */       return str.substring(pos);
/*      */     }
/* 2282 */     return str.substring(pos, pos + len);
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
/*      */   public static String substringBefore(String str, String separator) {
/* 2315 */     if (isEmpty(str) || separator == null) {
/* 2316 */       return str;
/*      */     }
/* 2318 */     if (separator.isEmpty()) {
/* 2319 */       return "";
/*      */     }
/* 2321 */     int pos = str.indexOf(separator);
/* 2322 */     if (pos == -1) {
/* 2323 */       return str;
/*      */     }
/* 2325 */     return str.substring(0, pos);
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
/*      */   public static String substringAfter(String str, String separator) {
/* 2357 */     if (isEmpty(str)) {
/* 2358 */       return str;
/*      */     }
/* 2360 */     if (separator == null) {
/* 2361 */       return "";
/*      */     }
/* 2363 */     int pos = str.indexOf(separator);
/* 2364 */     if (pos == -1) {
/* 2365 */       return "";
/*      */     }
/* 2367 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringBeforeLast(String str, String separator) {
/* 2398 */     if (isEmpty(str) || isEmpty(separator)) {
/* 2399 */       return str;
/*      */     }
/* 2401 */     int pos = str.lastIndexOf(separator);
/* 2402 */     if (pos == -1) {
/* 2403 */       return str;
/*      */     }
/* 2405 */     return str.substring(0, pos);
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
/*      */   public static String substringAfterLast(String str, String separator) {
/* 2438 */     if (isEmpty(str)) {
/* 2439 */       return str;
/*      */     }
/* 2441 */     if (isEmpty(separator)) {
/* 2442 */       return "";
/*      */     }
/* 2444 */     int pos = str.lastIndexOf(separator);
/* 2445 */     if (pos == -1 || pos == str.length() - separator.length()) {
/* 2446 */       return "";
/*      */     }
/* 2448 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringBetween(String str, String tag) {
/* 2475 */     return substringBetween(str, tag, tag);
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
/*      */   public static String substringBetween(String str, String open, String close) {
/* 2506 */     if (str == null || open == null || close == null) {
/* 2507 */       return null;
/*      */     }
/* 2509 */     int start = str.indexOf(open);
/* 2510 */     if (start != -1) {
/* 2511 */       int end = str.indexOf(close, start + open.length());
/* 2512 */       if (end != -1) {
/* 2513 */         return str.substring(start + open.length(), end);
/*      */       }
/*      */     } 
/* 2516 */     return null;
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
/*      */   public static String[] substringsBetween(String str, String open, String close) {
/* 2542 */     if (str == null || isEmpty(open) || isEmpty(close)) {
/* 2543 */       return null;
/*      */     }
/* 2545 */     int strLen = str.length();
/* 2546 */     if (strLen == 0) {
/* 2547 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 2549 */     int closeLen = close.length();
/* 2550 */     int openLen = open.length();
/* 2551 */     List<String> list = new ArrayList<String>();
/* 2552 */     int pos = 0;
/* 2553 */     while (pos < strLen - closeLen) {
/* 2554 */       int start = str.indexOf(open, pos);
/* 2555 */       if (start < 0) {
/*      */         break;
/*      */       }
/* 2558 */       start += openLen;
/* 2559 */       int end = str.indexOf(close, start);
/* 2560 */       if (end < 0) {
/*      */         break;
/*      */       }
/* 2563 */       list.add(str.substring(start, end));
/* 2564 */       pos = end + closeLen;
/*      */     } 
/* 2566 */     if (list.isEmpty()) {
/* 2567 */       return null;
/*      */     }
/* 2569 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] split(String str) {
/* 2600 */     return split(str, null, -1);
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
/*      */   public static String[] split(String str, char separatorChar) {
/* 2628 */     return splitWorker(str, separatorChar, false);
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
/*      */   public static String[] split(String str, String separatorChars) {
/* 2657 */     return splitWorker(str, separatorChars, -1, false);
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
/*      */   public static String[] split(String str, String separatorChars, int max) {
/* 2691 */     return splitWorker(str, separatorChars, max, false);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator) {
/* 2718 */     return splitByWholeSeparatorWorker(str, separator, -1, false);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator, int max) {
/* 2749 */     return splitByWholeSeparatorWorker(str, separator, max, false);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
/* 2778 */     return splitByWholeSeparatorWorker(str, separator, -1, true);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
/* 2811 */     return splitByWholeSeparatorWorker(str, separator, max, true);
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
/*      */   private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
/* 2830 */     if (str == null) {
/* 2831 */       return null;
/*      */     }
/*      */     
/* 2834 */     int len = str.length();
/*      */     
/* 2836 */     if (len == 0) {
/* 2837 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 2840 */     if (separator == null || "".equals(separator))
/*      */     {
/* 2842 */       return splitWorker(str, null, max, preserveAllTokens);
/*      */     }
/*      */     
/* 2845 */     int separatorLength = separator.length();
/*      */     
/* 2847 */     ArrayList<String> substrings = new ArrayList<String>();
/* 2848 */     int numberOfSubstrings = 0;
/* 2849 */     int beg = 0;
/* 2850 */     int end = 0;
/* 2851 */     while (end < len) {
/* 2852 */       end = str.indexOf(separator, beg);
/*      */       
/* 2854 */       if (end > -1) {
/* 2855 */         if (end > beg) {
/* 2856 */           numberOfSubstrings++;
/*      */           
/* 2858 */           if (numberOfSubstrings == max) {
/* 2859 */             end = len;
/* 2860 */             substrings.add(str.substring(beg));
/*      */             
/*      */             continue;
/*      */           } 
/* 2864 */           substrings.add(str.substring(beg, end));
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2869 */           beg = end + separatorLength;
/*      */           
/*      */           continue;
/*      */         } 
/* 2873 */         if (preserveAllTokens) {
/* 2874 */           numberOfSubstrings++;
/* 2875 */           if (numberOfSubstrings == max) {
/* 2876 */             end = len;
/* 2877 */             substrings.add(str.substring(beg));
/*      */           } else {
/* 2879 */             substrings.add("");
/*      */           } 
/*      */         } 
/* 2882 */         beg = end + separatorLength;
/*      */         
/*      */         continue;
/*      */       } 
/* 2886 */       substrings.add(str.substring(beg));
/* 2887 */       end = len;
/*      */     } 
/*      */ 
/*      */     
/* 2891 */     return substrings.<String>toArray(new String[substrings.size()]);
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
/*      */   public static String[] splitPreserveAllTokens(String str) {
/* 2920 */     return splitWorker(str, null, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, char separatorChar) {
/* 2956 */     return splitWorker(str, separatorChar, true);
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
/*      */   private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
/* 2974 */     if (str == null) {
/* 2975 */       return null;
/*      */     }
/* 2977 */     int len = str.length();
/* 2978 */     if (len == 0) {
/* 2979 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 2981 */     List<String> list = new ArrayList<String>();
/* 2982 */     int i = 0, start = 0;
/* 2983 */     boolean match = false;
/* 2984 */     boolean lastMatch = false;
/* 2985 */     while (i < len) {
/* 2986 */       if (str.charAt(i) == separatorChar) {
/* 2987 */         if (match || preserveAllTokens) {
/* 2988 */           list.add(str.substring(start, i));
/* 2989 */           match = false;
/* 2990 */           lastMatch = true;
/*      */         } 
/* 2992 */         start = ++i;
/*      */         continue;
/*      */       } 
/* 2995 */       lastMatch = false;
/* 2996 */       match = true;
/* 2997 */       i++;
/*      */     } 
/* 2999 */     if (match || (preserveAllTokens && lastMatch)) {
/* 3000 */       list.add(str.substring(start, i));
/*      */     }
/* 3002 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars) {
/* 3039 */     return splitWorker(str, separatorChars, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
/* 3079 */     return splitWorker(str, separatorChars, max, true);
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
/*      */   private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
/* 3101 */     if (str == null) {
/* 3102 */       return null;
/*      */     }
/* 3104 */     int len = str.length();
/* 3105 */     if (len == 0) {
/* 3106 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 3108 */     List<String> list = new ArrayList<String>();
/* 3109 */     int sizePlus1 = 1;
/* 3110 */     int i = 0, start = 0;
/* 3111 */     boolean match = false;
/* 3112 */     boolean lastMatch = false;
/* 3113 */     if (separatorChars == null) {
/*      */       
/* 3115 */       while (i < len) {
/* 3116 */         if (Character.isWhitespace(str.charAt(i))) {
/* 3117 */           if (match || preserveAllTokens) {
/* 3118 */             lastMatch = true;
/* 3119 */             if (sizePlus1++ == max) {
/* 3120 */               i = len;
/* 3121 */               lastMatch = false;
/*      */             } 
/* 3123 */             list.add(str.substring(start, i));
/* 3124 */             match = false;
/*      */           } 
/* 3126 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3129 */         lastMatch = false;
/* 3130 */         match = true;
/* 3131 */         i++;
/*      */       } 
/* 3133 */     } else if (separatorChars.length() == 1) {
/*      */       
/* 3135 */       char sep = separatorChars.charAt(0);
/* 3136 */       while (i < len) {
/* 3137 */         if (str.charAt(i) == sep) {
/* 3138 */           if (match || preserveAllTokens) {
/* 3139 */             lastMatch = true;
/* 3140 */             if (sizePlus1++ == max) {
/* 3141 */               i = len;
/* 3142 */               lastMatch = false;
/*      */             } 
/* 3144 */             list.add(str.substring(start, i));
/* 3145 */             match = false;
/*      */           } 
/* 3147 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3150 */         lastMatch = false;
/* 3151 */         match = true;
/* 3152 */         i++;
/*      */       } 
/*      */     } else {
/*      */       
/* 3156 */       while (i < len) {
/* 3157 */         if (separatorChars.indexOf(str.charAt(i)) >= 0) {
/* 3158 */           if (match || preserveAllTokens) {
/* 3159 */             lastMatch = true;
/* 3160 */             if (sizePlus1++ == max) {
/* 3161 */               i = len;
/* 3162 */               lastMatch = false;
/*      */             } 
/* 3164 */             list.add(str.substring(start, i));
/* 3165 */             match = false;
/*      */           } 
/* 3167 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3170 */         lastMatch = false;
/* 3171 */         match = true;
/* 3172 */         i++;
/*      */       } 
/*      */     } 
/* 3175 */     if (match || (preserveAllTokens && lastMatch)) {
/* 3176 */       list.add(str.substring(start, i));
/*      */     }
/* 3178 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] splitByCharacterType(String str) {
/* 3201 */     return splitByCharacterType(str, false);
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
/*      */   public static String[] splitByCharacterTypeCamelCase(String str) {
/* 3229 */     return splitByCharacterType(str, true);
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
/*      */   private static String[] splitByCharacterType(String str, boolean camelCase) {
/* 3247 */     if (str == null) {
/* 3248 */       return null;
/*      */     }
/* 3250 */     if (str.isEmpty()) {
/* 3251 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 3253 */     char[] c = str.toCharArray();
/* 3254 */     List<String> list = new ArrayList<String>();
/* 3255 */     int tokenStart = 0;
/* 3256 */     int currentType = Character.getType(c[tokenStart]);
/* 3257 */     for (int pos = tokenStart + 1; pos < c.length; pos++) {
/* 3258 */       int type = Character.getType(c[pos]);
/* 3259 */       if (type != currentType) {
/*      */ 
/*      */         
/* 3262 */         if (camelCase && type == 2 && currentType == 1) {
/* 3263 */           int newTokenStart = pos - 1;
/* 3264 */           if (newTokenStart != tokenStart) {
/* 3265 */             list.add(new String(c, tokenStart, newTokenStart - tokenStart));
/* 3266 */             tokenStart = newTokenStart;
/*      */           } 
/*      */         } else {
/* 3269 */           list.add(new String(c, tokenStart, pos - tokenStart));
/* 3270 */           tokenStart = pos;
/*      */         } 
/* 3272 */         currentType = type;
/*      */       } 
/* 3274 */     }  list.add(new String(c, tokenStart, c.length - tokenStart));
/* 3275 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static <T> String join(T... elements) {
/* 3303 */     return join((Object[])elements, (String)null);
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
/*      */   public static String join(Object[] array, char separator) {
/* 3329 */     if (array == null) {
/* 3330 */       return null;
/*      */     }
/* 3332 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(long[] array, char separator) {
/* 3361 */     if (array == null) {
/* 3362 */       return null;
/*      */     }
/* 3364 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(int[] array, char separator) {
/* 3393 */     if (array == null) {
/* 3394 */       return null;
/*      */     }
/* 3396 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(short[] array, char separator) {
/* 3425 */     if (array == null) {
/* 3426 */       return null;
/*      */     }
/* 3428 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(byte[] array, char separator) {
/* 3457 */     if (array == null) {
/* 3458 */       return null;
/*      */     }
/* 3460 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(char[] array, char separator) {
/* 3489 */     if (array == null) {
/* 3490 */       return null;
/*      */     }
/* 3492 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(float[] array, char separator) {
/* 3521 */     if (array == null) {
/* 3522 */       return null;
/*      */     }
/* 3524 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(double[] array, char separator) {
/* 3553 */     if (array == null) {
/* 3554 */       return null;
/*      */     }
/* 3556 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(Object[] array, char separator, int startIndex, int endIndex) {
/* 3587 */     if (array == null) {
/* 3588 */       return null;
/*      */     }
/* 3590 */     int noOfItems = endIndex - startIndex;
/* 3591 */     if (noOfItems <= 0) {
/* 3592 */       return "";
/*      */     }
/* 3594 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3595 */     for (int i = startIndex; i < endIndex; i++) {
/* 3596 */       if (i > startIndex) {
/* 3597 */         buf.append(separator);
/*      */       }
/* 3599 */       if (array[i] != null) {
/* 3600 */         buf.append(array[i]);
/*      */       }
/*      */     } 
/* 3603 */     return buf.toString();
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
/*      */   public static String join(long[] array, char separator, int startIndex, int endIndex) {
/* 3638 */     if (array == null) {
/* 3639 */       return null;
/*      */     }
/* 3641 */     int noOfItems = endIndex - startIndex;
/* 3642 */     if (noOfItems <= 0) {
/* 3643 */       return "";
/*      */     }
/* 3645 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3646 */     for (int i = startIndex; i < endIndex; i++) {
/* 3647 */       if (i > startIndex) {
/* 3648 */         buf.append(separator);
/*      */       }
/* 3650 */       buf.append(array[i]);
/*      */     } 
/* 3652 */     return buf.toString();
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
/*      */   public static String join(int[] array, char separator, int startIndex, int endIndex) {
/* 3687 */     if (array == null) {
/* 3688 */       return null;
/*      */     }
/* 3690 */     int noOfItems = endIndex - startIndex;
/* 3691 */     if (noOfItems <= 0) {
/* 3692 */       return "";
/*      */     }
/* 3694 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3695 */     for (int i = startIndex; i < endIndex; i++) {
/* 3696 */       if (i > startIndex) {
/* 3697 */         buf.append(separator);
/*      */       }
/* 3699 */       buf.append(array[i]);
/*      */     } 
/* 3701 */     return buf.toString();
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
/*      */   public static String join(byte[] array, char separator, int startIndex, int endIndex) {
/* 3736 */     if (array == null) {
/* 3737 */       return null;
/*      */     }
/* 3739 */     int noOfItems = endIndex - startIndex;
/* 3740 */     if (noOfItems <= 0) {
/* 3741 */       return "";
/*      */     }
/* 3743 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3744 */     for (int i = startIndex; i < endIndex; i++) {
/* 3745 */       if (i > startIndex) {
/* 3746 */         buf.append(separator);
/*      */       }
/* 3748 */       buf.append(array[i]);
/*      */     } 
/* 3750 */     return buf.toString();
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
/*      */   public static String join(short[] array, char separator, int startIndex, int endIndex) {
/* 3785 */     if (array == null) {
/* 3786 */       return null;
/*      */     }
/* 3788 */     int noOfItems = endIndex - startIndex;
/* 3789 */     if (noOfItems <= 0) {
/* 3790 */       return "";
/*      */     }
/* 3792 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3793 */     for (int i = startIndex; i < endIndex; i++) {
/* 3794 */       if (i > startIndex) {
/* 3795 */         buf.append(separator);
/*      */       }
/* 3797 */       buf.append(array[i]);
/*      */     } 
/* 3799 */     return buf.toString();
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
/*      */   public static String join(char[] array, char separator, int startIndex, int endIndex) {
/* 3834 */     if (array == null) {
/* 3835 */       return null;
/*      */     }
/* 3837 */     int noOfItems = endIndex - startIndex;
/* 3838 */     if (noOfItems <= 0) {
/* 3839 */       return "";
/*      */     }
/* 3841 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3842 */     for (int i = startIndex; i < endIndex; i++) {
/* 3843 */       if (i > startIndex) {
/* 3844 */         buf.append(separator);
/*      */       }
/* 3846 */       buf.append(array[i]);
/*      */     } 
/* 3848 */     return buf.toString();
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
/*      */   public static String join(double[] array, char separator, int startIndex, int endIndex) {
/* 3883 */     if (array == null) {
/* 3884 */       return null;
/*      */     }
/* 3886 */     int noOfItems = endIndex - startIndex;
/* 3887 */     if (noOfItems <= 0) {
/* 3888 */       return "";
/*      */     }
/* 3890 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3891 */     for (int i = startIndex; i < endIndex; i++) {
/* 3892 */       if (i > startIndex) {
/* 3893 */         buf.append(separator);
/*      */       }
/* 3895 */       buf.append(array[i]);
/*      */     } 
/* 3897 */     return buf.toString();
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
/*      */   public static String join(float[] array, char separator, int startIndex, int endIndex) {
/* 3932 */     if (array == null) {
/* 3933 */       return null;
/*      */     }
/* 3935 */     int noOfItems = endIndex - startIndex;
/* 3936 */     if (noOfItems <= 0) {
/* 3937 */       return "";
/*      */     }
/* 3939 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3940 */     for (int i = startIndex; i < endIndex; i++) {
/* 3941 */       if (i > startIndex) {
/* 3942 */         buf.append(separator);
/*      */       }
/* 3944 */       buf.append(array[i]);
/*      */     } 
/* 3946 */     return buf.toString();
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
/*      */   public static String join(Object[] array, String separator) {
/* 3974 */     if (array == null) {
/* 3975 */       return null;
/*      */     }
/* 3977 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(Object[] array, String separator, int startIndex, int endIndex) {
/* 4016 */     if (array == null) {
/* 4017 */       return null;
/*      */     }
/* 4019 */     if (separator == null) {
/* 4020 */       separator = "";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4025 */     int noOfItems = endIndex - startIndex;
/* 4026 */     if (noOfItems <= 0) {
/* 4027 */       return "";
/*      */     }
/*      */     
/* 4030 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/*      */     
/* 4032 */     for (int i = startIndex; i < endIndex; i++) {
/* 4033 */       if (i > startIndex) {
/* 4034 */         buf.append(separator);
/*      */       }
/* 4036 */       if (array[i] != null) {
/* 4037 */         buf.append(array[i]);
/*      */       }
/*      */     } 
/* 4040 */     return buf.toString();
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
/*      */   public static String join(Iterator<?> iterator, char separator) {
/* 4060 */     if (iterator == null) {
/* 4061 */       return null;
/*      */     }
/* 4063 */     if (!iterator.hasNext()) {
/* 4064 */       return "";
/*      */     }
/* 4066 */     Object first = iterator.next();
/* 4067 */     if (!iterator.hasNext()) {
/*      */ 
/*      */       
/* 4070 */       String result = ObjectUtils.toString(first);
/* 4071 */       return result;
/*      */     } 
/*      */ 
/*      */     
/* 4075 */     StringBuilder buf = new StringBuilder(256);
/* 4076 */     if (first != null) {
/* 4077 */       buf.append(first);
/*      */     }
/*      */     
/* 4080 */     while (iterator.hasNext()) {
/* 4081 */       buf.append(separator);
/* 4082 */       Object obj = iterator.next();
/* 4083 */       if (obj != null) {
/* 4084 */         buf.append(obj);
/*      */       }
/*      */     } 
/*      */     
/* 4088 */     return buf.toString();
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
/*      */   public static String join(Iterator<?> iterator, String separator) {
/* 4107 */     if (iterator == null) {
/* 4108 */       return null;
/*      */     }
/* 4110 */     if (!iterator.hasNext()) {
/* 4111 */       return "";
/*      */     }
/* 4113 */     Object first = iterator.next();
/* 4114 */     if (!iterator.hasNext()) {
/*      */       
/* 4116 */       String result = ObjectUtils.toString(first);
/* 4117 */       return result;
/*      */     } 
/*      */ 
/*      */     
/* 4121 */     StringBuilder buf = new StringBuilder(256);
/* 4122 */     if (first != null) {
/* 4123 */       buf.append(first);
/*      */     }
/*      */     
/* 4126 */     while (iterator.hasNext()) {
/* 4127 */       if (separator != null) {
/* 4128 */         buf.append(separator);
/*      */       }
/* 4130 */       Object obj = iterator.next();
/* 4131 */       if (obj != null) {
/* 4132 */         buf.append(obj);
/*      */       }
/*      */     } 
/* 4135 */     return buf.toString();
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
/*      */   public static String join(Iterable<?> iterable, char separator) {
/* 4153 */     if (iterable == null) {
/* 4154 */       return null;
/*      */     }
/* 4156 */     return join(iterable.iterator(), separator);
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
/*      */   public static String join(Iterable<?> iterable, String separator) {
/* 4174 */     if (iterable == null) {
/* 4175 */       return null;
/*      */     }
/* 4177 */     return join(iterable.iterator(), separator);
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
/*      */   public static String deleteWhitespace(String str) {
/* 4197 */     if (isEmpty(str)) {
/* 4198 */       return str;
/*      */     }
/* 4200 */     int sz = str.length();
/* 4201 */     char[] chs = new char[sz];
/* 4202 */     int count = 0;
/* 4203 */     for (int i = 0; i < sz; i++) {
/* 4204 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 4205 */         chs[count++] = str.charAt(i);
/*      */       }
/*      */     } 
/* 4208 */     if (count == sz) {
/* 4209 */       return str;
/*      */     }
/* 4211 */     return new String(chs, 0, count);
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
/*      */   public static String removeStart(String str, String remove) {
/* 4241 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4242 */       return str;
/*      */     }
/* 4244 */     if (str.startsWith(remove)) {
/* 4245 */       return str.substring(remove.length());
/*      */     }
/* 4247 */     return str;
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
/*      */   public static String removeStartIgnoreCase(String str, String remove) {
/* 4276 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4277 */       return str;
/*      */     }
/* 4279 */     if (startsWithIgnoreCase(str, remove)) {
/* 4280 */       return str.substring(remove.length());
/*      */     }
/* 4282 */     return str;
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
/*      */   public static String removeEnd(String str, String remove) {
/* 4310 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4311 */       return str;
/*      */     }
/* 4313 */     if (str.endsWith(remove)) {
/* 4314 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 4316 */     return str;
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
/*      */   public static String removeEndIgnoreCase(String str, String remove) {
/* 4346 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4347 */       return str;
/*      */     }
/* 4349 */     if (endsWithIgnoreCase(str, remove)) {
/* 4350 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 4352 */     return str;
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
/*      */   public static String remove(String str, String remove) {
/* 4379 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4380 */       return str;
/*      */     }
/* 4382 */     return replace(str, remove, "", -1);
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
/*      */   public static String remove(String str, char remove) {
/* 4405 */     if (isEmpty(str) || str.indexOf(remove) == -1) {
/* 4406 */       return str;
/*      */     }
/* 4408 */     char[] chars = str.toCharArray();
/* 4409 */     int pos = 0;
/* 4410 */     for (int i = 0; i < chars.length; i++) {
/* 4411 */       if (chars[i] != remove) {
/* 4412 */         chars[pos++] = chars[i];
/*      */       }
/*      */     } 
/* 4415 */     return new String(chars, 0, pos);
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
/*      */   public static String replaceOnce(String text, String searchString, String replacement) {
/* 4444 */     return replace(text, searchString, replacement, 1);
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
/*      */   public static String replacePattern(String source, String regex, String replacement) {
/* 4468 */     return Pattern.compile(regex, 32).matcher(source).replaceAll(replacement);
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
/*      */   public static String removePattern(String source, String regex) {
/* 4484 */     return replacePattern(source, regex, "");
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
/*      */   public static String replace(String text, String searchString, String replacement) {
/* 4511 */     return replace(text, searchString, replacement, -1);
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
/*      */   public static String replace(String text, String searchString, String replacement, int max) {
/* 4543 */     if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
/* 4544 */       return text;
/*      */     }
/* 4546 */     int start = 0;
/* 4547 */     int end = text.indexOf(searchString, start);
/* 4548 */     if (end == -1) {
/* 4549 */       return text;
/*      */     }
/* 4551 */     int replLength = searchString.length();
/* 4552 */     int increase = replacement.length() - replLength;
/* 4553 */     increase = (increase < 0) ? 0 : increase;
/* 4554 */     increase *= (max < 0) ? 16 : ((max > 64) ? 64 : max);
/* 4555 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/* 4556 */     while (end != -1) {
/* 4557 */       buf.append(text.substring(start, end)).append(replacement);
/* 4558 */       start = end + replLength;
/* 4559 */       if (--max == 0) {
/*      */         break;
/*      */       }
/* 4562 */       end = text.indexOf(searchString, start);
/*      */     } 
/* 4564 */     buf.append(text.substring(start));
/* 4565 */     return buf.toString();
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
/*      */   public static String replaceEach(String text, String[] searchList, String[] replacementList) {
/* 4608 */     return replaceEach(text, searchList, replacementList, false, 0);
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
/*      */   public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
/* 4656 */     int timeToLive = (searchList == null) ? 0 : searchList.length;
/* 4657 */     return replaceEach(text, searchList, replacementList, true, timeToLive);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
/* 4716 */     if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0)
/*      */     {
/* 4718 */       return text;
/*      */     }
/*      */ 
/*      */     
/* 4722 */     if (timeToLive < 0) {
/* 4723 */       throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
/*      */     }
/*      */ 
/*      */     
/* 4727 */     int searchLength = searchList.length;
/* 4728 */     int replacementLength = replacementList.length;
/*      */ 
/*      */     
/* 4731 */     if (searchLength != replacementLength) {
/* 4732 */       throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4739 */     boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
/*      */ 
/*      */     
/* 4742 */     int textIndex = -1;
/* 4743 */     int replaceIndex = -1;
/* 4744 */     int tempIndex = -1;
/*      */ 
/*      */ 
/*      */     
/* 4748 */     for (int i = 0; i < searchLength; i++) {
/* 4749 */       if (!noMoreMatchesForReplIndex[i] && searchList[i] != null && !searchList[i].isEmpty() && replacementList[i] != null) {
/*      */ 
/*      */ 
/*      */         
/* 4753 */         tempIndex = text.indexOf(searchList[i]);
/*      */ 
/*      */         
/* 4756 */         if (tempIndex == -1) {
/* 4757 */           noMoreMatchesForReplIndex[i] = true;
/*      */         }
/* 4759 */         else if (textIndex == -1 || tempIndex < textIndex) {
/* 4760 */           textIndex = tempIndex;
/* 4761 */           replaceIndex = i;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4768 */     if (textIndex == -1) {
/* 4769 */       return text;
/*      */     }
/*      */     
/* 4772 */     int start = 0;
/*      */ 
/*      */     
/* 4775 */     int increase = 0;
/*      */ 
/*      */     
/* 4778 */     for (int j = 0; j < searchList.length; j++) {
/* 4779 */       if (searchList[j] != null && replacementList[j] != null) {
/*      */ 
/*      */         
/* 4782 */         int greater = replacementList[j].length() - searchList[j].length();
/* 4783 */         if (greater > 0) {
/* 4784 */           increase += 3 * greater;
/*      */         }
/*      */       } 
/*      */     } 
/* 4788 */     increase = Math.min(increase, text.length() / 5);
/*      */     
/* 4790 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/*      */     
/* 4792 */     while (textIndex != -1) {
/*      */       int m;
/* 4794 */       for (m = start; m < textIndex; m++) {
/* 4795 */         buf.append(text.charAt(m));
/*      */       }
/* 4797 */       buf.append(replacementList[replaceIndex]);
/*      */       
/* 4799 */       start = textIndex + searchList[replaceIndex].length();
/*      */       
/* 4801 */       textIndex = -1;
/* 4802 */       replaceIndex = -1;
/* 4803 */       tempIndex = -1;
/*      */ 
/*      */       
/* 4806 */       for (m = 0; m < searchLength; m++) {
/* 4807 */         if (!noMoreMatchesForReplIndex[m] && searchList[m] != null && !searchList[m].isEmpty() && replacementList[m] != null) {
/*      */ 
/*      */ 
/*      */           
/* 4811 */           tempIndex = text.indexOf(searchList[m], start);
/*      */ 
/*      */           
/* 4814 */           if (tempIndex == -1) {
/* 4815 */             noMoreMatchesForReplIndex[m] = true;
/*      */           }
/* 4817 */           else if (textIndex == -1 || tempIndex < textIndex) {
/* 4818 */             textIndex = tempIndex;
/* 4819 */             replaceIndex = m;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 4826 */     int textLength = text.length();
/* 4827 */     for (int k = start; k < textLength; k++) {
/* 4828 */       buf.append(text.charAt(k));
/*      */     }
/* 4830 */     String result = buf.toString();
/* 4831 */     if (!repeat) {
/* 4832 */       return result;
/*      */     }
/*      */     
/* 4835 */     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
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
/*      */   public static String replaceChars(String str, char searchChar, char replaceChar) {
/* 4861 */     if (str == null) {
/* 4862 */       return null;
/*      */     }
/* 4864 */     return str.replace(searchChar, replaceChar);
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
/*      */   public static String replaceChars(String str, String searchChars, String replaceChars) {
/* 4904 */     if (isEmpty(str) || isEmpty(searchChars)) {
/* 4905 */       return str;
/*      */     }
/* 4907 */     if (replaceChars == null) {
/* 4908 */       replaceChars = "";
/*      */     }
/* 4910 */     boolean modified = false;
/* 4911 */     int replaceCharsLength = replaceChars.length();
/* 4912 */     int strLength = str.length();
/* 4913 */     StringBuilder buf = new StringBuilder(strLength);
/* 4914 */     for (int i = 0; i < strLength; i++) {
/* 4915 */       char ch = str.charAt(i);
/* 4916 */       int index = searchChars.indexOf(ch);
/* 4917 */       if (index >= 0) {
/* 4918 */         modified = true;
/* 4919 */         if (index < replaceCharsLength) {
/* 4920 */           buf.append(replaceChars.charAt(index));
/*      */         }
/*      */       } else {
/* 4923 */         buf.append(ch);
/*      */       } 
/*      */     } 
/* 4926 */     if (modified) {
/* 4927 */       return buf.toString();
/*      */     }
/* 4929 */     return str;
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
/*      */   public static String overlay(String str, String overlay, int start, int end) {
/* 4964 */     if (str == null) {
/* 4965 */       return null;
/*      */     }
/* 4967 */     if (overlay == null) {
/* 4968 */       overlay = "";
/*      */     }
/* 4970 */     int len = str.length();
/* 4971 */     if (start < 0) {
/* 4972 */       start = 0;
/*      */     }
/* 4974 */     if (start > len) {
/* 4975 */       start = len;
/*      */     }
/* 4977 */     if (end < 0) {
/* 4978 */       end = 0;
/*      */     }
/* 4980 */     if (end > len) {
/* 4981 */       end = len;
/*      */     }
/* 4983 */     if (start > end) {
/* 4984 */       int temp = start;
/* 4985 */       start = end;
/* 4986 */       end = temp;
/*      */     } 
/* 4988 */     return (new StringBuilder(len + start - end + overlay.length() + 1)).append(str.substring(0, start)).append(overlay).append(str.substring(end)).toString();
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
/*      */   public static String chomp(String str) {
/* 5023 */     if (isEmpty(str)) {
/* 5024 */       return str;
/*      */     }
/*      */     
/* 5027 */     if (str.length() == 1) {
/* 5028 */       char ch = str.charAt(0);
/* 5029 */       if (ch == '\r' || ch == '\n') {
/* 5030 */         return "";
/*      */       }
/* 5032 */       return str;
/*      */     } 
/*      */     
/* 5035 */     int lastIdx = str.length() - 1;
/* 5036 */     char last = str.charAt(lastIdx);
/*      */     
/* 5038 */     if (last == '\n') {
/* 5039 */       if (str.charAt(lastIdx - 1) == '\r') {
/* 5040 */         lastIdx--;
/*      */       }
/* 5042 */     } else if (last != '\r') {
/* 5043 */       lastIdx++;
/*      */     } 
/* 5045 */     return str.substring(0, lastIdx);
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
/*      */   @Deprecated
/*      */   public static String chomp(String str, String separator) {
/* 5077 */     return removeEnd(str, separator);
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
/*      */   public static String chop(String str) {
/* 5106 */     if (str == null) {
/* 5107 */       return null;
/*      */     }
/* 5109 */     int strLen = str.length();
/* 5110 */     if (strLen < 2) {
/* 5111 */       return "";
/*      */     }
/* 5113 */     int lastIdx = strLen - 1;
/* 5114 */     String ret = str.substring(0, lastIdx);
/* 5115 */     char last = str.charAt(lastIdx);
/* 5116 */     if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
/* 5117 */       return ret.substring(0, lastIdx - 1);
/*      */     }
/* 5119 */     return ret;
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
/*      */   public static String repeat(String str, int repeat) {
/*      */     char ch0, ch1, output2[];
/*      */     int i;
/* 5148 */     if (str == null) {
/* 5149 */       return null;
/*      */     }
/* 5151 */     if (repeat <= 0) {
/* 5152 */       return "";
/*      */     }
/* 5154 */     int inputLength = str.length();
/* 5155 */     if (repeat == 1 || inputLength == 0) {
/* 5156 */       return str;
/*      */     }
/* 5158 */     if (inputLength == 1 && repeat <= 8192) {
/* 5159 */       return repeat(str.charAt(0), repeat);
/*      */     }
/*      */     
/* 5162 */     int outputLength = inputLength * repeat;
/* 5163 */     switch (inputLength) {
/*      */       case 1:
/* 5165 */         return repeat(str.charAt(0), repeat);
/*      */       case 2:
/* 5167 */         ch0 = str.charAt(0);
/* 5168 */         ch1 = str.charAt(1);
/* 5169 */         output2 = new char[outputLength];
/* 5170 */         for (i = repeat * 2 - 2; i >= 0; i--, i--) {
/* 5171 */           output2[i] = ch0;
/* 5172 */           output2[i + 1] = ch1;
/*      */         } 
/* 5174 */         return new String(output2);
/*      */     } 
/* 5176 */     StringBuilder buf = new StringBuilder(outputLength);
/* 5177 */     for (int j = 0; j < repeat; j++) {
/* 5178 */       buf.append(str);
/*      */     }
/* 5180 */     return buf.toString();
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
/*      */   public static String repeat(String str, String separator, int repeat) {
/* 5205 */     if (str == null || separator == null) {
/* 5206 */       return repeat(str, repeat);
/*      */     }
/*      */     
/* 5209 */     String result = repeat(str + separator, repeat);
/* 5210 */     return removeEnd(result, separator);
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
/*      */   public static String repeat(char ch, int repeat) {
/* 5236 */     char[] buf = new char[repeat];
/* 5237 */     for (int i = repeat - 1; i >= 0; i--) {
/* 5238 */       buf[i] = ch;
/*      */     }
/* 5240 */     return new String(buf);
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
/*      */   public static String rightPad(String str, int size) {
/* 5263 */     return rightPad(str, size, ' ');
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
/*      */   public static String rightPad(String str, int size, char padChar) {
/* 5288 */     if (str == null) {
/* 5289 */       return null;
/*      */     }
/* 5291 */     int pads = size - str.length();
/* 5292 */     if (pads <= 0) {
/* 5293 */       return str;
/*      */     }
/* 5295 */     if (pads > 8192) {
/* 5296 */       return rightPad(str, size, String.valueOf(padChar));
/*      */     }
/* 5298 */     return str.concat(repeat(padChar, pads));
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
/*      */   public static String rightPad(String str, int size, String padStr) {
/* 5325 */     if (str == null) {
/* 5326 */       return null;
/*      */     }
/* 5328 */     if (isEmpty(padStr)) {
/* 5329 */       padStr = " ";
/*      */     }
/* 5331 */     int padLen = padStr.length();
/* 5332 */     int strLen = str.length();
/* 5333 */     int pads = size - strLen;
/* 5334 */     if (pads <= 0) {
/* 5335 */       return str;
/*      */     }
/* 5337 */     if (padLen == 1 && pads <= 8192) {
/* 5338 */       return rightPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 5341 */     if (pads == padLen)
/* 5342 */       return str.concat(padStr); 
/* 5343 */     if (pads < padLen) {
/* 5344 */       return str.concat(padStr.substring(0, pads));
/*      */     }
/* 5346 */     char[] padding = new char[pads];
/* 5347 */     char[] padChars = padStr.toCharArray();
/* 5348 */     for (int i = 0; i < pads; i++) {
/* 5349 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 5351 */     return str.concat(new String(padding));
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
/*      */   public static String leftPad(String str, int size) {
/* 5375 */     return leftPad(str, size, ' ');
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
/*      */   public static String leftPad(String str, int size, char padChar) {
/* 5400 */     if (str == null) {
/* 5401 */       return null;
/*      */     }
/* 5403 */     int pads = size - str.length();
/* 5404 */     if (pads <= 0) {
/* 5405 */       return str;
/*      */     }
/* 5407 */     if (pads > 8192) {
/* 5408 */       return leftPad(str, size, String.valueOf(padChar));
/*      */     }
/* 5410 */     return repeat(padChar, pads).concat(str);
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
/*      */   public static String leftPad(String str, int size, String padStr) {
/* 5437 */     if (str == null) {
/* 5438 */       return null;
/*      */     }
/* 5440 */     if (isEmpty(padStr)) {
/* 5441 */       padStr = " ";
/*      */     }
/* 5443 */     int padLen = padStr.length();
/* 5444 */     int strLen = str.length();
/* 5445 */     int pads = size - strLen;
/* 5446 */     if (pads <= 0) {
/* 5447 */       return str;
/*      */     }
/* 5449 */     if (padLen == 1 && pads <= 8192) {
/* 5450 */       return leftPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 5453 */     if (pads == padLen)
/* 5454 */       return padStr.concat(str); 
/* 5455 */     if (pads < padLen) {
/* 5456 */       return padStr.substring(0, pads).concat(str);
/*      */     }
/* 5458 */     char[] padding = new char[pads];
/* 5459 */     char[] padChars = padStr.toCharArray();
/* 5460 */     for (int i = 0; i < pads; i++) {
/* 5461 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 5463 */     return (new String(padding)).concat(str);
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
/*      */   public static int length(CharSequence cs) {
/* 5479 */     return (cs == null) ? 0 : cs.length();
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
/*      */   public static String center(String str, int size) {
/* 5508 */     return center(str, size, ' ');
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
/*      */   public static String center(String str, int size, char padChar) {
/* 5536 */     if (str == null || size <= 0) {
/* 5537 */       return str;
/*      */     }
/* 5539 */     int strLen = str.length();
/* 5540 */     int pads = size - strLen;
/* 5541 */     if (pads <= 0) {
/* 5542 */       return str;
/*      */     }
/* 5544 */     str = leftPad(str, strLen + pads / 2, padChar);
/* 5545 */     str = rightPad(str, size, padChar);
/* 5546 */     return str;
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
/*      */   public static String center(String str, int size, String padStr) {
/* 5576 */     if (str == null || size <= 0) {
/* 5577 */       return str;
/*      */     }
/* 5579 */     if (isEmpty(padStr)) {
/* 5580 */       padStr = " ";
/*      */     }
/* 5582 */     int strLen = str.length();
/* 5583 */     int pads = size - strLen;
/* 5584 */     if (pads <= 0) {
/* 5585 */       return str;
/*      */     }
/* 5587 */     str = leftPad(str, strLen + pads / 2, padStr);
/* 5588 */     str = rightPad(str, size, padStr);
/* 5589 */     return str;
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
/*      */   public static String upperCase(String str) {
/* 5614 */     if (str == null) {
/* 5615 */       return null;
/*      */     }
/* 5617 */     return str.toUpperCase();
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
/*      */   public static String upperCase(String str, Locale locale) {
/* 5637 */     if (str == null) {
/* 5638 */       return null;
/*      */     }
/* 5640 */     return str.toUpperCase(locale);
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
/*      */   public static String lowerCase(String str) {
/* 5663 */     if (str == null) {
/* 5664 */       return null;
/*      */     }
/* 5666 */     return str.toLowerCase();
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
/*      */   public static String lowerCase(String str, Locale locale) {
/* 5686 */     if (str == null) {
/* 5687 */       return null;
/*      */     }
/* 5689 */     return str.toLowerCase(locale);
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
/*      */   public static String capitalize(String str) {
/*      */     int strLen;
/* 5714 */     if (str == null || (strLen = str.length()) == 0) {
/* 5715 */       return str;
/*      */     }
/*      */     
/* 5718 */     char firstChar = str.charAt(0);
/* 5719 */     if (Character.isTitleCase(firstChar))
/*      */     {
/* 5721 */       return str;
/*      */     }
/*      */     
/* 5724 */     return (new StringBuilder(strLen)).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
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
/*      */   public static String uncapitalize(String str) {
/*      */     int strLen;
/* 5752 */     if (str == null || (strLen = str.length()) == 0) {
/* 5753 */       return str;
/*      */     }
/*      */     
/* 5756 */     char firstChar = str.charAt(0);
/* 5757 */     if (Character.isLowerCase(firstChar))
/*      */     {
/* 5759 */       return str;
/*      */     }
/*      */     
/* 5762 */     return (new StringBuilder(strLen)).append(Character.toLowerCase(firstChar)).append(str.substring(1)).toString();
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
/*      */   public static String swapCase(String str) {
/* 5796 */     if (isEmpty(str)) {
/* 5797 */       return str;
/*      */     }
/*      */     
/* 5800 */     char[] buffer = str.toCharArray();
/*      */     
/* 5802 */     for (int i = 0; i < buffer.length; i++) {
/* 5803 */       char ch = buffer[i];
/* 5804 */       if (Character.isUpperCase(ch)) {
/* 5805 */         buffer[i] = Character.toLowerCase(ch);
/* 5806 */       } else if (Character.isTitleCase(ch)) {
/* 5807 */         buffer[i] = Character.toLowerCase(ch);
/* 5808 */       } else if (Character.isLowerCase(ch)) {
/* 5809 */         buffer[i] = Character.toUpperCase(ch);
/*      */       } 
/*      */     } 
/* 5812 */     return new String(buffer);
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
/*      */   public static int countMatches(CharSequence str, CharSequence sub) {
/* 5838 */     if (isEmpty(str) || isEmpty(sub)) {
/* 5839 */       return 0;
/*      */     }
/* 5841 */     int count = 0;
/* 5842 */     int idx = 0;
/* 5843 */     while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1) {
/* 5844 */       count++;
/* 5845 */       idx += sub.length();
/*      */     } 
/* 5847 */     return count;
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
/*      */   public static int countMatches(CharSequence str, char ch) {
/* 5870 */     if (isEmpty(str)) {
/* 5871 */       return 0;
/*      */     }
/* 5873 */     int count = 0;
/*      */     
/* 5875 */     for (int i = 0; i < str.length(); i++) {
/* 5876 */       if (ch == str.charAt(i)) {
/* 5877 */         count++;
/*      */       }
/*      */     } 
/* 5880 */     return count;
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
/*      */   public static boolean isAlpha(CharSequence cs) {
/* 5906 */     if (isEmpty(cs)) {
/* 5907 */       return false;
/*      */     }
/* 5909 */     int sz = cs.length();
/* 5910 */     for (int i = 0; i < sz; i++) {
/* 5911 */       if (!Character.isLetter(cs.charAt(i))) {
/* 5912 */         return false;
/*      */       }
/*      */     } 
/* 5915 */     return true;
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
/*      */   public static boolean isAlphaSpace(CharSequence cs) {
/* 5941 */     if (cs == null) {
/* 5942 */       return false;
/*      */     }
/* 5944 */     int sz = cs.length();
/* 5945 */     for (int i = 0; i < sz; i++) {
/* 5946 */       if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 5947 */         return false;
/*      */       }
/*      */     } 
/* 5950 */     return true;
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
/*      */   public static boolean isAlphanumeric(CharSequence cs) {
/* 5976 */     if (isEmpty(cs)) {
/* 5977 */       return false;
/*      */     }
/* 5979 */     int sz = cs.length();
/* 5980 */     for (int i = 0; i < sz; i++) {
/* 5981 */       if (!Character.isLetterOrDigit(cs.charAt(i))) {
/* 5982 */         return false;
/*      */       }
/*      */     } 
/* 5985 */     return true;
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
/*      */   public static boolean isAlphanumericSpace(CharSequence cs) {
/* 6011 */     if (cs == null) {
/* 6012 */       return false;
/*      */     }
/* 6014 */     int sz = cs.length();
/* 6015 */     for (int i = 0; i < sz; i++) {
/* 6016 */       if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 6017 */         return false;
/*      */       }
/*      */     } 
/* 6020 */     return true;
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
/*      */   public static boolean isAsciiPrintable(CharSequence cs) {
/* 6050 */     if (cs == null) {
/* 6051 */       return false;
/*      */     }
/* 6053 */     int sz = cs.length();
/* 6054 */     for (int i = 0; i < sz; i++) {
/* 6055 */       if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
/* 6056 */         return false;
/*      */       }
/*      */     } 
/* 6059 */     return true;
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
/*      */   public static boolean isNumeric(CharSequence cs) {
/* 6094 */     if (isEmpty(cs)) {
/* 6095 */       return false;
/*      */     }
/* 6097 */     int sz = cs.length();
/* 6098 */     for (int i = 0; i < sz; i++) {
/* 6099 */       if (!Character.isDigit(cs.charAt(i))) {
/* 6100 */         return false;
/*      */       }
/*      */     } 
/* 6103 */     return true;
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
/*      */   public static boolean isNumericSpace(CharSequence cs) {
/* 6133 */     if (cs == null) {
/* 6134 */       return false;
/*      */     }
/* 6136 */     int sz = cs.length();
/* 6137 */     for (int i = 0; i < sz; i++) {
/* 6138 */       if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 6139 */         return false;
/*      */       }
/*      */     } 
/* 6142 */     return true;
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
/*      */   public static boolean isWhitespace(CharSequence cs) {
/* 6166 */     if (cs == null) {
/* 6167 */       return false;
/*      */     }
/* 6169 */     int sz = cs.length();
/* 6170 */     for (int i = 0; i < sz; i++) {
/* 6171 */       if (!Character.isWhitespace(cs.charAt(i))) {
/* 6172 */         return false;
/*      */       }
/*      */     } 
/* 6175 */     return true;
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
/*      */   public static boolean isAllLowerCase(CharSequence cs) {
/* 6201 */     if (cs == null || isEmpty(cs)) {
/* 6202 */       return false;
/*      */     }
/* 6204 */     int sz = cs.length();
/* 6205 */     for (int i = 0; i < sz; i++) {
/* 6206 */       if (!Character.isLowerCase(cs.charAt(i))) {
/* 6207 */         return false;
/*      */       }
/*      */     } 
/* 6210 */     return true;
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
/*      */   public static boolean isAllUpperCase(CharSequence cs) {
/* 6236 */     if (cs == null || isEmpty(cs)) {
/* 6237 */       return false;
/*      */     }
/* 6239 */     int sz = cs.length();
/* 6240 */     for (int i = 0; i < sz; i++) {
/* 6241 */       if (!Character.isUpperCase(cs.charAt(i))) {
/* 6242 */         return false;
/*      */       }
/*      */     } 
/* 6245 */     return true;
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
/*      */   public static String defaultString(String str) {
/* 6267 */     return (str == null) ? "" : str;
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
/*      */   public static String defaultString(String str, String defaultStr) {
/* 6288 */     return (str == null) ? defaultStr : str;
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
/*      */   public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
/* 6310 */     return isBlank((CharSequence)str) ? defaultStr : str;
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
/*      */   public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
/* 6332 */     return isEmpty((CharSequence)str) ? defaultStr : str;
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
/*      */   public static String reverse(String str) {
/* 6352 */     if (str == null) {
/* 6353 */       return null;
/*      */     }
/* 6355 */     return (new StringBuilder(str)).reverse().toString();
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
/*      */   public static String reverseDelimited(String str, char separatorChar) {
/* 6378 */     if (str == null) {
/* 6379 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 6383 */     String[] strs = split(str, separatorChar);
/* 6384 */     ArrayUtils.reverse((Object[])strs);
/* 6385 */     return join((Object[])strs, separatorChar);
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
/*      */   public static String abbreviate(String str, int maxWidth) {
/* 6422 */     return abbreviate(str, 0, maxWidth);
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
/*      */   public static String abbreviate(String str, int offset, int maxWidth) {
/* 6461 */     if (str == null) {
/* 6462 */       return null;
/*      */     }
/* 6464 */     if (maxWidth < 4) {
/* 6465 */       throw new IllegalArgumentException("Minimum abbreviation width is 4");
/*      */     }
/* 6467 */     if (str.length() <= maxWidth) {
/* 6468 */       return str;
/*      */     }
/* 6470 */     if (offset > str.length()) {
/* 6471 */       offset = str.length();
/*      */     }
/* 6473 */     if (str.length() - offset < maxWidth - 3) {
/* 6474 */       offset = str.length() - maxWidth - 3;
/*      */     }
/* 6476 */     String abrevMarker = "...";
/* 6477 */     if (offset <= 4) {
/* 6478 */       return str.substring(0, maxWidth - 3) + "...";
/*      */     }
/* 6480 */     if (maxWidth < 7) {
/* 6481 */       throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
/*      */     }
/* 6483 */     if (offset + maxWidth - 3 < str.length()) {
/* 6484 */       return "..." + abbreviate(str.substring(offset), maxWidth - 3);
/*      */     }
/* 6486 */     return "..." + str.substring(str.length() - maxWidth - 3);
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
/*      */   public static String abbreviateMiddle(String str, String middle, int length) {
/* 6519 */     if (isEmpty(str) || isEmpty(middle)) {
/* 6520 */       return str;
/*      */     }
/*      */     
/* 6523 */     if (length >= str.length() || length < middle.length() + 2) {
/* 6524 */       return str;
/*      */     }
/*      */     
/* 6527 */     int targetSting = length - middle.length();
/* 6528 */     int startOffset = targetSting / 2 + targetSting % 2;
/* 6529 */     int endOffset = str.length() - targetSting / 2;
/*      */     
/* 6531 */     StringBuilder builder = new StringBuilder(length);
/* 6532 */     builder.append(str.substring(0, startOffset));
/* 6533 */     builder.append(middle);
/* 6534 */     builder.append(str.substring(endOffset));
/*      */     
/* 6536 */     return builder.toString();
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
/*      */   public static String difference(String str1, String str2) {
/* 6570 */     if (str1 == null) {
/* 6571 */       return str2;
/*      */     }
/* 6573 */     if (str2 == null) {
/* 6574 */       return str1;
/*      */     }
/* 6576 */     int at = indexOfDifference(str1, str2);
/* 6577 */     if (at == -1) {
/* 6578 */       return "";
/*      */     }
/* 6580 */     return str2.substring(at);
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
/*      */   public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
/* 6609 */     if (cs1 == cs2) {
/* 6610 */       return -1;
/*      */     }
/* 6612 */     if (cs1 == null || cs2 == null) {
/* 6613 */       return 0;
/*      */     }
/*      */     int i;
/* 6616 */     for (i = 0; i < cs1.length() && i < cs2.length() && 
/* 6617 */       cs1.charAt(i) == cs2.charAt(i); i++);
/*      */ 
/*      */ 
/*      */     
/* 6621 */     if (i < cs2.length() || i < cs1.length()) {
/* 6622 */       return i;
/*      */     }
/* 6624 */     return -1;
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
/*      */   public static int indexOfDifference(CharSequence... css) {
/* 6660 */     if (css == null || css.length <= 1) {
/* 6661 */       return -1;
/*      */     }
/* 6663 */     boolean anyStringNull = false;
/* 6664 */     boolean allStringsNull = true;
/* 6665 */     int arrayLen = css.length;
/* 6666 */     int shortestStrLen = Integer.MAX_VALUE;
/* 6667 */     int longestStrLen = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6672 */     for (int i = 0; i < arrayLen; i++) {
/* 6673 */       if (css[i] == null) {
/* 6674 */         anyStringNull = true;
/* 6675 */         shortestStrLen = 0;
/*      */       } else {
/* 6677 */         allStringsNull = false;
/* 6678 */         shortestStrLen = Math.min(css[i].length(), shortestStrLen);
/* 6679 */         longestStrLen = Math.max(css[i].length(), longestStrLen);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 6684 */     if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
/* 6685 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 6689 */     if (shortestStrLen == 0) {
/* 6690 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 6694 */     int firstDiff = -1;
/* 6695 */     for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
/* 6696 */       char comparisonChar = css[0].charAt(stringPos);
/* 6697 */       for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
/* 6698 */         if (css[arrayPos].charAt(stringPos) != comparisonChar) {
/* 6699 */           firstDiff = stringPos;
/*      */           break;
/*      */         } 
/*      */       } 
/* 6703 */       if (firstDiff != -1) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 6708 */     if (firstDiff == -1 && shortestStrLen != longestStrLen)
/*      */     {
/*      */ 
/*      */       
/* 6712 */       return shortestStrLen;
/*      */     }
/* 6714 */     return firstDiff;
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
/*      */   public static String getCommonPrefix(String... strs) {
/* 6751 */     if (strs == null || strs.length == 0) {
/* 6752 */       return "";
/*      */     }
/* 6754 */     int smallestIndexOfDiff = indexOfDifference((CharSequence[])strs);
/* 6755 */     if (smallestIndexOfDiff == -1) {
/*      */       
/* 6757 */       if (strs[0] == null) {
/* 6758 */         return "";
/*      */       }
/* 6760 */       return strs[0];
/* 6761 */     }  if (smallestIndexOfDiff == 0)
/*      */     {
/* 6763 */       return "";
/*      */     }
/*      */     
/* 6766 */     return strs[0].substring(0, smallestIndexOfDiff);
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
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
/* 6809 */     if (s == null || t == null) {
/* 6810 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6830 */     int n = s.length();
/* 6831 */     int m = t.length();
/*      */     
/* 6833 */     if (n == 0)
/* 6834 */       return m; 
/* 6835 */     if (m == 0) {
/* 6836 */       return n;
/*      */     }
/*      */     
/* 6839 */     if (n > m) {
/*      */       
/* 6841 */       CharSequence tmp = s;
/* 6842 */       s = t;
/* 6843 */       t = tmp;
/* 6844 */       n = m;
/* 6845 */       m = t.length();
/*      */     } 
/*      */     
/* 6848 */     int[] p = new int[n + 1];
/* 6849 */     int[] d = new int[n + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6860 */     for (i = 0; i <= n; i++) {
/* 6861 */       p[i] = i;
/*      */     }
/*      */     
/* 6864 */     for (int j = 1; j <= m; j++) {
/* 6865 */       char t_j = t.charAt(j - 1);
/* 6866 */       d[0] = j;
/*      */       
/* 6868 */       for (i = 1; i <= n; i++) {
/* 6869 */         int cost = (s.charAt(i - 1) == t_j) ? 0 : 1;
/*      */         
/* 6871 */         d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
/*      */       } 
/*      */ 
/*      */       
/* 6875 */       int[] _d = p;
/* 6876 */       p = d;
/* 6877 */       d = _d;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 6882 */     return p[n];
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
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
/* 6918 */     if (s == null || t == null) {
/* 6919 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/* 6921 */     if (threshold < 0) {
/* 6922 */       throw new IllegalArgumentException("Threshold must not be negative");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6969 */     int n = s.length();
/* 6970 */     int m = t.length();
/*      */ 
/*      */     
/* 6973 */     if (n == 0)
/* 6974 */       return (m <= threshold) ? m : -1; 
/* 6975 */     if (m == 0) {
/* 6976 */       return (n <= threshold) ? n : -1;
/*      */     }
/*      */     
/* 6979 */     if (n > m) {
/*      */       
/* 6981 */       CharSequence tmp = s;
/* 6982 */       s = t;
/* 6983 */       t = tmp;
/* 6984 */       n = m;
/* 6985 */       m = t.length();
/*      */     } 
/*      */     
/* 6988 */     int[] p = new int[n + 1];
/* 6989 */     int[] d = new int[n + 1];
/*      */ 
/*      */ 
/*      */     
/* 6993 */     int boundary = Math.min(n, threshold) + 1;
/* 6994 */     for (int i = 0; i < boundary; i++) {
/* 6995 */       p[i] = i;
/*      */     }
/*      */ 
/*      */     
/* 6999 */     Arrays.fill(p, boundary, p.length, 2147483647);
/* 7000 */     Arrays.fill(d, 2147483647);
/*      */ 
/*      */     
/* 7003 */     for (int j = 1; j <= m; j++) {
/* 7004 */       char t_j = t.charAt(j - 1);
/* 7005 */       d[0] = j;
/*      */ 
/*      */       
/* 7008 */       int min = Math.max(1, j - threshold);
/* 7009 */       int max = (j > Integer.MAX_VALUE - threshold) ? n : Math.min(n, j + threshold);
/*      */ 
/*      */       
/* 7012 */       if (min > max) {
/* 7013 */         return -1;
/*      */       }
/*      */ 
/*      */       
/* 7017 */       if (min > 1) {
/* 7018 */         d[min - 1] = Integer.MAX_VALUE;
/*      */       }
/*      */ 
/*      */       
/* 7022 */       for (int k = min; k <= max; k++) {
/* 7023 */         if (s.charAt(k - 1) == t_j) {
/*      */           
/* 7025 */           d[k] = p[k - 1];
/*      */         } else {
/*      */           
/* 7028 */           d[k] = 1 + Math.min(Math.min(d[k - 1], p[k]), p[k - 1]);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 7033 */       int[] _d = p;
/* 7034 */       p = d;
/* 7035 */       d = _d;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 7040 */     if (p[n] <= threshold) {
/* 7041 */       return p[n];
/*      */     }
/* 7043 */     return -1;
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
/*      */   public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
/* 7079 */     double DEFAULT_SCALING_FACTOR = 0.1D;
/*      */     
/* 7081 */     if (first == null || second == null) {
/* 7082 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */     
/* 7085 */     double jaro = score(first, second);
/* 7086 */     int cl = commonPrefixLength(first, second);
/* 7087 */     double matchScore = Math.round((jaro + 0.1D * cl * (1.0D - jaro)) * 100.0D) / 100.0D;
/*      */     
/* 7089 */     return matchScore;
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
/*      */   private static double score(CharSequence first, CharSequence second) {
/*      */     String shorter, longer;
/* 7103 */     if (first.length() > second.length()) {
/* 7104 */       longer = first.toString().toLowerCase();
/* 7105 */       shorter = second.toString().toLowerCase();
/*      */     } else {
/* 7107 */       longer = second.toString().toLowerCase();
/* 7108 */       shorter = first.toString().toLowerCase();
/*      */     } 
/*      */ 
/*      */     
/* 7112 */     int halflength = shorter.length() / 2 + 1;
/*      */ 
/*      */ 
/*      */     
/* 7116 */     String m1 = getSetOfMatchingCharacterWithin(shorter, longer, halflength);
/* 7117 */     String m2 = getSetOfMatchingCharacterWithin(longer, shorter, halflength);
/*      */ 
/*      */ 
/*      */     
/* 7121 */     if (m1.length() == 0 || m2.length() == 0) {
/* 7122 */       return 0.0D;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 7127 */     if (m1.length() != m2.length()) {
/* 7128 */       return 0.0D;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 7133 */     int transpositions = transpositions(m1, m2);
/*      */ 
/*      */     
/* 7136 */     double dist = (m1.length() / shorter.length() + m2.length() / longer.length() + (m1.length() - transpositions) / m1.length()) / 3.0D;
/*      */ 
/*      */ 
/*      */     
/* 7140 */     return dist;
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
/*      */   public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
/* 7170 */     if (term == null || query == null)
/* 7171 */       throw new IllegalArgumentException("Strings must not be null"); 
/* 7172 */     if (locale == null) {
/* 7173 */       throw new IllegalArgumentException("Locale must not be null");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7180 */     String termLowerCase = term.toString().toLowerCase(locale);
/* 7181 */     String queryLowerCase = query.toString().toLowerCase(locale);
/*      */ 
/*      */     
/* 7184 */     int score = 0;
/*      */ 
/*      */ 
/*      */     
/* 7188 */     int termIndex = 0;
/*      */ 
/*      */     
/* 7191 */     int previousMatchingCharacterIndex = Integer.MIN_VALUE;
/*      */     
/* 7193 */     for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
/* 7194 */       char queryChar = queryLowerCase.charAt(queryIndex);
/*      */       
/* 7196 */       boolean termCharacterMatchFound = false;
/* 7197 */       for (; termIndex < termLowerCase.length() && !termCharacterMatchFound; termIndex++) {
/* 7198 */         char termChar = termLowerCase.charAt(termIndex);
/*      */         
/* 7200 */         if (queryChar == termChar) {
/*      */           
/* 7202 */           score++;
/*      */ 
/*      */ 
/*      */           
/* 7206 */           if (previousMatchingCharacterIndex + 1 == termIndex) {
/* 7207 */             score += 2;
/*      */           }
/*      */           
/* 7210 */           previousMatchingCharacterIndex = termIndex;
/*      */ 
/*      */ 
/*      */           
/* 7214 */           termCharacterMatchFound = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 7219 */     return score;
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
/*      */   private static String getSetOfMatchingCharacterWithin(CharSequence first, CharSequence second, int limit) {
/* 7234 */     StringBuilder common = new StringBuilder();
/* 7235 */     StringBuilder copy = new StringBuilder(second);
/*      */     
/* 7237 */     for (int i = 0; i < first.length(); i++) {
/* 7238 */       char ch = first.charAt(i);
/* 7239 */       boolean found = false;
/*      */ 
/*      */       
/* 7242 */       for (int j = Math.max(0, i - limit); !found && j < Math.min(i + limit, second.length()); j++) {
/* 7243 */         if (copy.charAt(j) == ch) {
/* 7244 */           found = true;
/* 7245 */           common.append(ch);
/* 7246 */           copy.setCharAt(j, '*');
/*      */         } 
/*      */       } 
/*      */     } 
/* 7250 */     return common.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int transpositions(CharSequence first, CharSequence second) {
/* 7260 */     int transpositions = 0;
/* 7261 */     for (int i = 0; i < first.length(); i++) {
/* 7262 */       if (first.charAt(i) != second.charAt(i)) {
/* 7263 */         transpositions++;
/*      */       }
/*      */     } 
/* 7266 */     return transpositions / 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int commonPrefixLength(CharSequence first, CharSequence second) {
/* 7277 */     int result = getCommonPrefix(new String[] { first.toString(), second.toString() }).length();
/*      */ 
/*      */     
/* 7280 */     return (result > 4) ? 4 : result;
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
/*      */   public static boolean startsWith(CharSequence str, CharSequence prefix) {
/* 7309 */     return startsWith(str, prefix, false);
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
/*      */   public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
/* 7335 */     return startsWith(str, prefix, true);
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
/*      */   private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
/* 7350 */     if (str == null || prefix == null) {
/* 7351 */       return (str == null && prefix == null);
/*      */     }
/* 7353 */     if (prefix.length() > str.length()) {
/* 7354 */       return false;
/*      */     }
/* 7356 */     return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
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
/*      */   public static boolean startsWithAny(CharSequence string, CharSequence... searchStrings) {
/* 7379 */     if (isEmpty(string) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 7380 */       return false;
/*      */     }
/* 7382 */     for (CharSequence searchString : searchStrings) {
/* 7383 */       if (startsWith(string, searchString)) {
/* 7384 */         return true;
/*      */       }
/*      */     } 
/* 7387 */     return false;
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
/*      */   public static boolean endsWith(CharSequence str, CharSequence suffix) {
/* 7417 */     return endsWith(str, suffix, false);
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
/*      */   public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
/* 7444 */     return endsWith(str, suffix, true);
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
/*      */   private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
/* 7459 */     if (str == null || suffix == null) {
/* 7460 */       return (str == null && suffix == null);
/*      */     }
/* 7462 */     if (suffix.length() > str.length()) {
/* 7463 */       return false;
/*      */     }
/* 7465 */     int strOffset = str.length() - suffix.length();
/* 7466 */     return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
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
/*      */   public static String normalizeSpace(String str) {
/* 7513 */     if (isEmpty(str)) {
/* 7514 */       return str;
/*      */     }
/* 7516 */     int size = str.length();
/* 7517 */     char[] newChars = new char[size];
/* 7518 */     int count = 0;
/* 7519 */     int whitespacesCount = 0;
/* 7520 */     boolean startWhitespaces = true;
/* 7521 */     for (int i = 0; i < size; i++) {
/* 7522 */       char actualChar = str.charAt(i);
/* 7523 */       boolean isWhitespace = Character.isWhitespace(actualChar);
/* 7524 */       if (!isWhitespace) {
/* 7525 */         startWhitespaces = false;
/* 7526 */         newChars[count++] = (actualChar == ' ') ? ' ' : actualChar;
/* 7527 */         whitespacesCount = 0;
/*      */       } else {
/* 7529 */         if (whitespacesCount == 0 && !startWhitespaces) {
/* 7530 */           newChars[count++] = " ".charAt(0);
/*      */         }
/* 7532 */         whitespacesCount++;
/*      */       } 
/*      */     } 
/* 7535 */     if (startWhitespaces) {
/* 7536 */       return "";
/*      */     }
/* 7538 */     return new String(newChars, 0, count - ((whitespacesCount > 0) ? 1 : 0));
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
/*      */   public static boolean endsWithAny(CharSequence string, CharSequence... searchStrings) {
/* 7560 */     if (isEmpty(string) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 7561 */       return false;
/*      */     }
/* 7563 */     for (CharSequence searchString : searchStrings) {
/* 7564 */       if (endsWith(string, searchString)) {
/* 7565 */         return true;
/*      */       }
/*      */     } 
/* 7568 */     return false;
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
/*      */   private static String appendIfMissing(String str, CharSequence suffix, boolean ignoreCase, CharSequence... suffixes) {
/* 7583 */     if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
/* 7584 */       return str;
/*      */     }
/* 7586 */     if (suffixes != null && suffixes.length > 0) {
/* 7587 */       for (CharSequence s : suffixes) {
/* 7588 */         if (endsWith(str, s, ignoreCase)) {
/* 7589 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/* 7593 */     return str + suffix.toString();
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
/*      */   public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
/* 7631 */     return appendIfMissing(str, suffix, false, suffixes);
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
/*      */   public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
/* 7669 */     return appendIfMissing(str, suffix, true, suffixes);
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
/*      */   private static String prependIfMissing(String str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
/* 7684 */     if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
/* 7685 */       return str;
/*      */     }
/* 7687 */     if (prefixes != null && prefixes.length > 0) {
/* 7688 */       for (CharSequence p : prefixes) {
/* 7689 */         if (startsWith(str, p, ignoreCase)) {
/* 7690 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/* 7694 */     return prefix.toString() + str;
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
/*      */   public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
/* 7732 */     return prependIfMissing(str, prefix, false, prefixes);
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
/*      */   public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
/* 7770 */     return prependIfMissing(str, prefix, true, prefixes);
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
/*      */   @Deprecated
/*      */   public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
/* 7790 */     return (charsetName != null) ? new String(bytes, charsetName) : new String(bytes, Charset.defaultCharset());
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
/*      */   public static String toEncodedString(byte[] bytes, Charset charset) {
/* 7807 */     return new String(bytes, (charset != null) ? charset : Charset.defaultCharset());
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
/*      */   public static String wrap(String str, char wrapWith) {
/* 7833 */     if (isEmpty(str) || wrapWith == '\000') {
/* 7834 */       return str;
/*      */     }
/*      */     
/* 7837 */     return wrapWith + str + wrapWith;
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
/*      */   public static String wrap(String str, String wrapWith) {
/* 7871 */     if (isEmpty(str) || isEmpty(wrapWith)) {
/* 7872 */       return str;
/*      */     }
/*      */     
/* 7875 */     return wrapWith.concat(str).concat(wrapWith);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\StringUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */