/*      */ package org.apache.commons.lang3.math;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NumberUtils
/*      */ {
/*   35 */   public static final Long LONG_ZERO = Long.valueOf(0L);
/*      */   
/*   37 */   public static final Long LONG_ONE = Long.valueOf(1L);
/*      */   
/*   39 */   public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
/*      */   
/*   41 */   public static final Integer INTEGER_ZERO = Integer.valueOf(0);
/*      */   
/*   43 */   public static final Integer INTEGER_ONE = Integer.valueOf(1);
/*      */   
/*   45 */   public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
/*      */   
/*   47 */   public static final Short SHORT_ZERO = Short.valueOf((short)0);
/*      */   
/*   49 */   public static final Short SHORT_ONE = Short.valueOf((short)1);
/*      */   
/*   51 */   public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
/*      */   
/*   53 */   public static final Byte BYTE_ZERO = Byte.valueOf((byte)0);
/*      */   
/*   55 */   public static final Byte BYTE_ONE = Byte.valueOf((byte)1);
/*      */   
/*   57 */   public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte)-1);
/*      */   
/*   59 */   public static final Double DOUBLE_ZERO = Double.valueOf(0.0D);
/*      */   
/*   61 */   public static final Double DOUBLE_ONE = Double.valueOf(1.0D);
/*      */   
/*   63 */   public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0D);
/*      */   
/*   65 */   public static final Float FLOAT_ZERO = Float.valueOf(0.0F);
/*      */   
/*   67 */   public static final Float FLOAT_ONE = Float.valueOf(1.0F);
/*      */   
/*   69 */   public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInt(String str) {
/*  101 */     return toInt(str, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInt(String str, int defaultValue) {
/*  122 */     if (str == null) {
/*  123 */       return defaultValue;
/*      */     }
/*      */     try {
/*  126 */       return Integer.parseInt(str);
/*  127 */     } catch (NumberFormatException nfe) {
/*  128 */       return defaultValue;
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
/*      */   public static long toLong(String str) {
/*  150 */     return toLong(str, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long toLong(String str, long defaultValue) {
/*  171 */     if (str == null) {
/*  172 */       return defaultValue;
/*      */     }
/*      */     try {
/*  175 */       return Long.parseLong(str);
/*  176 */     } catch (NumberFormatException nfe) {
/*  177 */       return defaultValue;
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
/*      */   public static float toFloat(String str) {
/*  200 */     return toFloat(str, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float toFloat(String str, float defaultValue) {
/*  223 */     if (str == null) {
/*  224 */       return defaultValue;
/*      */     }
/*      */     try {
/*  227 */       return Float.parseFloat(str);
/*  228 */     } catch (NumberFormatException nfe) {
/*  229 */       return defaultValue;
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
/*      */   public static double toDouble(String str) {
/*  252 */     return toDouble(str, 0.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDouble(String str, double defaultValue) {
/*  275 */     if (str == null) {
/*  276 */       return defaultValue;
/*      */     }
/*      */     try {
/*  279 */       return Double.parseDouble(str);
/*  280 */     } catch (NumberFormatException nfe) {
/*  281 */       return defaultValue;
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
/*      */   public static byte toByte(String str) {
/*  304 */     return toByte(str, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte toByte(String str, byte defaultValue) {
/*  325 */     if (str == null) {
/*  326 */       return defaultValue;
/*      */     }
/*      */     try {
/*  329 */       return Byte.parseByte(str);
/*  330 */     } catch (NumberFormatException nfe) {
/*  331 */       return defaultValue;
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
/*      */   public static short toShort(String str) {
/*  353 */     return toShort(str, (short)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short toShort(String str, short defaultValue) {
/*  374 */     if (str == null) {
/*  375 */       return defaultValue;
/*      */     }
/*      */     try {
/*  378 */       return Short.parseShort(str);
/*  379 */     } catch (NumberFormatException nfe) {
/*  380 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Number createNumber(String str) throws NumberFormatException {
/*      */     String mant, dec, exp;
/*  452 */     if (str == null) {
/*  453 */       return null;
/*      */     }
/*  455 */     if (StringUtils.isBlank(str)) {
/*  456 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/*      */     
/*  459 */     String[] hex_prefixes = { "0x", "0X", "-0x", "-0X", "#", "-#" };
/*  460 */     int pfxLen = 0;
/*  461 */     for (String pfx : hex_prefixes) {
/*  462 */       if (str.startsWith(pfx)) {
/*  463 */         pfxLen += pfx.length();
/*      */         break;
/*      */       } 
/*      */     } 
/*  467 */     if (pfxLen > 0) {
/*  468 */       char firstSigDigit = Character.MIN_VALUE;
/*  469 */       for (int i = pfxLen; i < str.length(); ) {
/*  470 */         firstSigDigit = str.charAt(i);
/*  471 */         if (firstSigDigit == '0') {
/*  472 */           pfxLen++;
/*      */           
/*      */           i++;
/*      */         } 
/*      */       } 
/*  477 */       int hexDigits = str.length() - pfxLen;
/*  478 */       if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) {
/*  479 */         return createBigInteger(str);
/*      */       }
/*  481 */       if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) {
/*  482 */         return createLong(str);
/*      */       }
/*  484 */       return createInteger(str);
/*      */     } 
/*  486 */     char lastChar = str.charAt(str.length() - 1);
/*      */ 
/*      */ 
/*      */     
/*  490 */     int decPos = str.indexOf('.');
/*  491 */     int expPos = str.indexOf('e') + str.indexOf('E') + 1;
/*      */ 
/*      */ 
/*      */     
/*  495 */     int numDecimals = 0;
/*  496 */     if (decPos > -1) {
/*      */       
/*  498 */       if (expPos > -1) {
/*  499 */         if (expPos < decPos || expPos > str.length()) {
/*  500 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  502 */         dec = str.substring(decPos + 1, expPos);
/*      */       } else {
/*  504 */         dec = str.substring(decPos + 1);
/*      */       } 
/*  506 */       mant = getMantissa(str, decPos);
/*  507 */       numDecimals = dec.length();
/*      */     } else {
/*  509 */       if (expPos > -1) {
/*  510 */         if (expPos > str.length()) {
/*  511 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  513 */         mant = getMantissa(str, expPos);
/*      */       } else {
/*  515 */         mant = getMantissa(str);
/*      */       } 
/*  517 */       dec = null;
/*      */     } 
/*  519 */     if (!Character.isDigit(lastChar) && lastChar != '.') {
/*  520 */       if (expPos > -1 && expPos < str.length() - 1) {
/*  521 */         exp = str.substring(expPos + 1, str.length() - 1);
/*      */       } else {
/*  523 */         exp = null;
/*      */       } 
/*      */       
/*  526 */       String numeric = str.substring(0, str.length() - 1);
/*  527 */       boolean bool = (isAllZeros(mant) && isAllZeros(exp));
/*  528 */       switch (lastChar) {
/*      */         case 'L':
/*      */         case 'l':
/*  531 */           if (dec == null && exp == null && ((numeric.charAt(0) == '-' && isDigits(numeric.substring(1))) || isDigits(numeric))) {
/*      */             
/*      */             try {
/*      */               
/*  535 */               return createLong(numeric);
/*  536 */             } catch (NumberFormatException nfe) {
/*      */ 
/*      */               
/*  539 */               return createBigInteger(numeric);
/*      */             } 
/*      */           }
/*  542 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         case 'F':
/*      */         case 'f':
/*      */           try {
/*  546 */             Float f = createFloat(numeric);
/*  547 */             if (!f.isInfinite() && (f.floatValue() != 0.0F || bool))
/*      */             {
/*      */               
/*  550 */               return f;
/*      */             }
/*      */           }
/*  553 */           catch (NumberFormatException nfe) {}
/*      */ 
/*      */ 
/*      */         
/*      */         case 'D':
/*      */         case 'd':
/*      */           try {
/*  560 */             Double d = createDouble(numeric);
/*  561 */             if (!d.isInfinite() && (d.floatValue() != 0.0D || bool)) {
/*  562 */               return d;
/*      */             }
/*  564 */           } catch (NumberFormatException nfe) {}
/*      */ 
/*      */           
/*      */           try {
/*  568 */             return createBigDecimal(numeric);
/*  569 */           } catch (NumberFormatException e) {
/*      */             break;
/*      */           } 
/*      */       } 
/*      */       
/*  574 */       throw new NumberFormatException(str + " is not a valid number.");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  580 */     if (expPos > -1 && expPos < str.length() - 1) {
/*  581 */       exp = str.substring(expPos + 1, str.length());
/*      */     } else {
/*  583 */       exp = null;
/*      */     } 
/*  585 */     if (dec == null && exp == null) {
/*      */       
/*      */       try {
/*  588 */         return createInteger(str);
/*  589 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/*      */         try {
/*  593 */           return createLong(str);
/*  594 */         } catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */           
/*  597 */           return createBigInteger(str);
/*      */         } 
/*      */       } 
/*      */     }
/*  601 */     boolean allZeros = (isAllZeros(mant) && isAllZeros(exp));
/*      */     try {
/*  603 */       if (numDecimals <= 7) {
/*  604 */         Float f = createFloat(str);
/*  605 */         if (!f.isInfinite() && (f.floatValue() != 0.0F || allZeros)) {
/*  606 */           return f;
/*      */         }
/*      */       } 
/*  609 */     } catch (NumberFormatException nfe) {}
/*      */ 
/*      */     
/*      */     try {
/*  613 */       if (numDecimals <= 16) {
/*  614 */         Double d = createDouble(str);
/*  615 */         if (!d.isInfinite() && (d.doubleValue() != 0.0D || allZeros)) {
/*  616 */           return d;
/*      */         }
/*      */       } 
/*  619 */     } catch (NumberFormatException nfe) {}
/*      */ 
/*      */ 
/*      */     
/*  623 */     return createBigDecimal(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getMantissa(String str) {
/*  635 */     return getMantissa(str, str.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getMantissa(String str, int stopPos) {
/*  648 */     char firstChar = str.charAt(0);
/*  649 */     boolean hasSign = (firstChar == '-' || firstChar == '+');
/*      */     
/*  651 */     return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isAllZeros(String str) {
/*  663 */     if (str == null) {
/*  664 */       return true;
/*      */     }
/*  666 */     for (int i = str.length() - 1; i >= 0; i--) {
/*  667 */       if (str.charAt(i) != '0') {
/*  668 */         return false;
/*      */       }
/*      */     } 
/*  671 */     return (str.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Float createFloat(String str) {
/*  685 */     if (str == null) {
/*  686 */       return null;
/*      */     }
/*  688 */     return Float.valueOf(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Double createDouble(String str) {
/*  701 */     if (str == null) {
/*  702 */       return null;
/*      */     }
/*  704 */     return Double.valueOf(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer createInteger(String str) {
/*  719 */     if (str == null) {
/*  720 */       return null;
/*      */     }
/*      */     
/*  723 */     return Integer.decode(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Long createLong(String str) {
/*  738 */     if (str == null) {
/*  739 */       return null;
/*      */     }
/*  741 */     return Long.decode(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger createBigInteger(String str) {
/*  755 */     if (str == null) {
/*  756 */       return null;
/*      */     }
/*  758 */     int pos = 0;
/*  759 */     int radix = 10;
/*  760 */     boolean negate = false;
/*  761 */     if (str.startsWith("-")) {
/*  762 */       negate = true;
/*  763 */       pos = 1;
/*      */     } 
/*  765 */     if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
/*  766 */       radix = 16;
/*  767 */       pos += 2;
/*  768 */     } else if (str.startsWith("#", pos)) {
/*  769 */       radix = 16;
/*  770 */       pos++;
/*  771 */     } else if (str.startsWith("0", pos) && str.length() > pos + 1) {
/*  772 */       radix = 8;
/*  773 */       pos++;
/*      */     } 
/*      */     
/*  776 */     BigInteger value = new BigInteger(str.substring(pos), radix);
/*  777 */     return negate ? value.negate() : value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal createBigDecimal(String str) {
/*  790 */     if (str == null) {
/*  791 */       return null;
/*      */     }
/*      */     
/*  794 */     if (StringUtils.isBlank(str)) {
/*  795 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/*  797 */     if (str.trim().startsWith("--"))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  802 */       throw new NumberFormatException(str + " is not a valid number.");
/*      */     }
/*  804 */     return new BigDecimal(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long min(long... array) {
/*  820 */     validateArray(array);
/*      */ 
/*      */     
/*  823 */     long min = array[0];
/*  824 */     for (int i = 1; i < array.length; i++) {
/*  825 */       if (array[i] < min) {
/*  826 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  830 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int min(int... array) {
/*  844 */     validateArray(array);
/*      */ 
/*      */     
/*  847 */     int min = array[0];
/*  848 */     for (int j = 1; j < array.length; j++) {
/*  849 */       if (array[j] < min) {
/*  850 */         min = array[j];
/*      */       }
/*      */     } 
/*      */     
/*  854 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short min(short... array) {
/*  868 */     validateArray(array);
/*      */ 
/*      */     
/*  871 */     short min = array[0];
/*  872 */     for (int i = 1; i < array.length; i++) {
/*  873 */       if (array[i] < min) {
/*  874 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  878 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte min(byte... array) {
/*  892 */     validateArray(array);
/*      */ 
/*      */     
/*  895 */     byte min = array[0];
/*  896 */     for (int i = 1; i < array.length; i++) {
/*  897 */       if (array[i] < min) {
/*  898 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  902 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double min(double... array) {
/*  917 */     validateArray(array);
/*      */ 
/*      */     
/*  920 */     double min = array[0];
/*  921 */     for (int i = 1; i < array.length; i++) {
/*  922 */       if (Double.isNaN(array[i])) {
/*  923 */         return Double.NaN;
/*      */       }
/*  925 */       if (array[i] < min) {
/*  926 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  930 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float min(float... array) {
/*  945 */     validateArray(array);
/*      */ 
/*      */     
/*  948 */     float min = array[0];
/*  949 */     for (int i = 1; i < array.length; i++) {
/*  950 */       if (Float.isNaN(array[i])) {
/*  951 */         return Float.NaN;
/*      */       }
/*  953 */       if (array[i] < min) {
/*  954 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  958 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long max(long... array) {
/*  974 */     validateArray(array);
/*      */ 
/*      */     
/*  977 */     long max = array[0];
/*  978 */     for (int j = 1; j < array.length; j++) {
/*  979 */       if (array[j] > max) {
/*  980 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/*  984 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int max(int... array) {
/*  998 */     validateArray(array);
/*      */ 
/*      */     
/* 1001 */     int max = array[0];
/* 1002 */     for (int j = 1; j < array.length; j++) {
/* 1003 */       if (array[j] > max) {
/* 1004 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1008 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short max(short... array) {
/* 1022 */     validateArray(array);
/*      */ 
/*      */     
/* 1025 */     short max = array[0];
/* 1026 */     for (int i = 1; i < array.length; i++) {
/* 1027 */       if (array[i] > max) {
/* 1028 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1032 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte max(byte... array) {
/* 1046 */     validateArray(array);
/*      */ 
/*      */     
/* 1049 */     byte max = array[0];
/* 1050 */     for (int i = 1; i < array.length; i++) {
/* 1051 */       if (array[i] > max) {
/* 1052 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1056 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double max(double... array) {
/* 1071 */     validateArray(array);
/*      */ 
/*      */     
/* 1074 */     double max = array[0];
/* 1075 */     for (int j = 1; j < array.length; j++) {
/* 1076 */       if (Double.isNaN(array[j])) {
/* 1077 */         return Double.NaN;
/*      */       }
/* 1079 */       if (array[j] > max) {
/* 1080 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1084 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float max(float... array) {
/* 1099 */     validateArray(array);
/*      */ 
/*      */     
/* 1102 */     float max = array[0];
/* 1103 */     for (int j = 1; j < array.length; j++) {
/* 1104 */       if (Float.isNaN(array[j])) {
/* 1105 */         return Float.NaN;
/*      */       }
/* 1107 */       if (array[j] > max) {
/* 1108 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1112 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateArray(Object array) {
/* 1122 */     if (array == null) {
/* 1123 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1125 */     Validate.isTrue((Array.getLength(array) != 0), "Array cannot be empty.", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long min(long a, long b, long c) {
/* 1139 */     if (b < a) {
/* 1140 */       a = b;
/*      */     }
/* 1142 */     if (c < a) {
/* 1143 */       a = c;
/*      */     }
/* 1145 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int min(int a, int b, int c) {
/* 1157 */     if (b < a) {
/* 1158 */       a = b;
/*      */     }
/* 1160 */     if (c < a) {
/* 1161 */       a = c;
/*      */     }
/* 1163 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short min(short a, short b, short c) {
/* 1175 */     if (b < a) {
/* 1176 */       a = b;
/*      */     }
/* 1178 */     if (c < a) {
/* 1179 */       a = c;
/*      */     }
/* 1181 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte min(byte a, byte b, byte c) {
/* 1193 */     if (b < a) {
/* 1194 */       a = b;
/*      */     }
/* 1196 */     if (c < a) {
/* 1197 */       a = c;
/*      */     }
/* 1199 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double min(double a, double b, double c) {
/* 1215 */     return Math.min(Math.min(a, b), c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float min(float a, float b, float c) {
/* 1231 */     return Math.min(Math.min(a, b), c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long max(long a, long b, long c) {
/* 1245 */     if (b > a) {
/* 1246 */       a = b;
/*      */     }
/* 1248 */     if (c > a) {
/* 1249 */       a = c;
/*      */     }
/* 1251 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int max(int a, int b, int c) {
/* 1263 */     if (b > a) {
/* 1264 */       a = b;
/*      */     }
/* 1266 */     if (c > a) {
/* 1267 */       a = c;
/*      */     }
/* 1269 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short max(short a, short b, short c) {
/* 1281 */     if (b > a) {
/* 1282 */       a = b;
/*      */     }
/* 1284 */     if (c > a) {
/* 1285 */       a = c;
/*      */     }
/* 1287 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte max(byte a, byte b, byte c) {
/* 1299 */     if (b > a) {
/* 1300 */       a = b;
/*      */     }
/* 1302 */     if (c > a) {
/* 1303 */       a = c;
/*      */     }
/* 1305 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double max(double a, double b, double c) {
/* 1321 */     return Math.max(Math.max(a, b), c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float max(float a, float b, float c) {
/* 1337 */     return Math.max(Math.max(a, b), c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isDigits(String str) {
/* 1352 */     if (StringUtils.isEmpty(str)) {
/* 1353 */       return false;
/*      */     }
/* 1355 */     for (int i = 0; i < str.length(); i++) {
/* 1356 */       if (!Character.isDigit(str.charAt(i))) {
/* 1357 */         return false;
/*      */       }
/*      */     } 
/* 1360 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNumber(String str) {
/* 1383 */     if (StringUtils.isEmpty(str)) {
/* 1384 */       return false;
/*      */     }
/* 1386 */     char[] chars = str.toCharArray();
/* 1387 */     int sz = chars.length;
/* 1388 */     boolean hasExp = false;
/* 1389 */     boolean hasDecPoint = false;
/* 1390 */     boolean allowSigns = false;
/* 1391 */     boolean foundDigit = false;
/*      */     
/* 1393 */     int start = (chars[0] == '-') ? 1 : 0;
/* 1394 */     if (sz > start + 1 && chars[start] == '0') {
/* 1395 */       if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
/*      */ 
/*      */ 
/*      */         
/* 1399 */         int j = start + 2;
/* 1400 */         if (j == sz) {
/* 1401 */           return false;
/*      */         }
/*      */         
/* 1404 */         for (; j < chars.length; j++) {
/* 1405 */           if ((chars[j] < '0' || chars[j] > '9') && (chars[j] < 'a' || chars[j] > 'f') && (chars[j] < 'A' || chars[j] > 'F'))
/*      */           {
/*      */             
/* 1408 */             return false;
/*      */           }
/*      */         } 
/* 1411 */         return true;
/* 1412 */       }  if (Character.isDigit(chars[start + 1])) {
/*      */         
/* 1414 */         int j = start + 1;
/* 1415 */         for (; j < chars.length; j++) {
/* 1416 */           if (chars[j] < '0' || chars[j] > '7') {
/* 1417 */             return false;
/*      */           }
/*      */         } 
/* 1420 */         return true;
/*      */       } 
/*      */     } 
/* 1423 */     sz--;
/*      */     
/* 1425 */     int i = start;
/*      */ 
/*      */     
/* 1428 */     while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
/* 1429 */       if (chars[i] >= '0' && chars[i] <= '9') {
/* 1430 */         foundDigit = true;
/* 1431 */         allowSigns = false;
/*      */       }
/* 1433 */       else if (chars[i] == '.') {
/* 1434 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1436 */           return false;
/*      */         }
/* 1438 */         hasDecPoint = true;
/* 1439 */       } else if (chars[i] == 'e' || chars[i] == 'E') {
/*      */         
/* 1441 */         if (hasExp)
/*      */         {
/* 1443 */           return false;
/*      */         }
/* 1445 */         if (!foundDigit) {
/* 1446 */           return false;
/*      */         }
/* 1448 */         hasExp = true;
/* 1449 */         allowSigns = true;
/* 1450 */       } else if (chars[i] == '+' || chars[i] == '-') {
/* 1451 */         if (!allowSigns) {
/* 1452 */           return false;
/*      */         }
/* 1454 */         allowSigns = false;
/* 1455 */         foundDigit = false;
/*      */       } else {
/* 1457 */         return false;
/*      */       } 
/* 1459 */       i++;
/*      */     } 
/* 1461 */     if (i < chars.length) {
/* 1462 */       if (chars[i] >= '0' && chars[i] <= '9')
/*      */       {
/* 1464 */         return true;
/*      */       }
/* 1466 */       if (chars[i] == 'e' || chars[i] == 'E')
/*      */       {
/* 1468 */         return false;
/*      */       }
/* 1470 */       if (chars[i] == '.') {
/* 1471 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1473 */           return false;
/*      */         }
/*      */         
/* 1476 */         return foundDigit;
/*      */       } 
/* 1478 */       if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1483 */         return foundDigit;
/*      */       }
/* 1485 */       if (chars[i] == 'l' || chars[i] == 'L')
/*      */       {
/*      */         
/* 1488 */         return (foundDigit && !hasExp && !hasDecPoint);
/*      */       }
/*      */       
/* 1491 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1495 */     return (!allowSigns && foundDigit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isParsable(String str) {
/* 1516 */     if (StringUtils.endsWith(str, ".")) {
/* 1517 */       return false;
/*      */     }
/* 1519 */     if (StringUtils.startsWith(str, "-")) {
/* 1520 */       return isDigits(StringUtils.replaceOnce(str.substring(1), ".", ""));
/*      */     }
/* 1522 */     return isDigits(StringUtils.replaceOnce(str, ".", ""));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int compare(int x, int y) {
/* 1537 */     if (x == y) {
/* 1538 */       return 0;
/*      */     }
/* 1540 */     if (x < y) {
/* 1541 */       return -1;
/*      */     }
/* 1543 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int compare(long x, long y) {
/* 1558 */     if (x == y) {
/* 1559 */       return 0;
/*      */     }
/* 1561 */     if (x < y) {
/* 1562 */       return -1;
/*      */     }
/* 1564 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int compare(short x, short y) {
/* 1579 */     if (x == y) {
/* 1580 */       return 0;
/*      */     }
/* 1582 */     if (x < y) {
/* 1583 */       return -1;
/*      */     }
/* 1585 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int compare(byte x, byte y) {
/* 1600 */     return x - y;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\math\NumberUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */