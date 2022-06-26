/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import org.apache.commons.lang3.math.NumberUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BooleanUtils
/*      */ {
/*      */   public static Boolean negate(Boolean bool) {
/*   64 */     if (bool == null) {
/*   65 */       return null;
/*      */     }
/*   67 */     return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isTrue(Boolean bool) {
/*   87 */     return Boolean.TRUE.equals(bool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotTrue(Boolean bool) {
/*  105 */     return !isTrue(bool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFalse(Boolean bool) {
/*  123 */     return Boolean.FALSE.equals(bool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotFalse(Boolean bool) {
/*  141 */     return !isFalse(bool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(Boolean bool) {
/*  159 */     return (bool != null && bool.booleanValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBooleanDefaultIfNull(Boolean bool, boolean valueIfNull) {
/*  176 */     if (bool == null) {
/*  177 */       return valueIfNull;
/*      */     }
/*  179 */     return bool.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(int value) {
/*  199 */     return (value != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(int value) {
/*  217 */     return (value == 0) ? Boolean.FALSE : Boolean.TRUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(Integer value) {
/*  239 */     if (value == null) {
/*  240 */       return null;
/*      */     }
/*  242 */     return (value.intValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(int value, int trueValue, int falseValue) {
/*  262 */     if (value == trueValue) {
/*  263 */       return true;
/*      */     }
/*  265 */     if (value == falseValue) {
/*  266 */       return false;
/*      */     }
/*      */     
/*  269 */     throw new IllegalArgumentException("The Integer did not match either specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(Integer value, Integer trueValue, Integer falseValue) {
/*  290 */     if (value == null) {
/*  291 */       if (trueValue == null) {
/*  292 */         return true;
/*      */       }
/*  294 */       if (falseValue == null)
/*  295 */         return false; 
/*      */     } else {
/*  297 */       if (value.equals(trueValue))
/*  298 */         return true; 
/*  299 */       if (value.equals(falseValue)) {
/*  300 */         return false;
/*      */       }
/*      */     } 
/*  303 */     throw new IllegalArgumentException("The Integer did not match either specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(int value, int trueValue, int falseValue, int nullValue) {
/*  325 */     if (value == trueValue) {
/*  326 */       return Boolean.TRUE;
/*      */     }
/*  328 */     if (value == falseValue) {
/*  329 */       return Boolean.FALSE;
/*      */     }
/*  331 */     if (value == nullValue) {
/*  332 */       return null;
/*      */     }
/*      */     
/*  335 */     throw new IllegalArgumentException("The Integer did not match any specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(Integer value, Integer trueValue, Integer falseValue, Integer nullValue) {
/*  357 */     if (value == null) {
/*  358 */       if (trueValue == null) {
/*  359 */         return Boolean.TRUE;
/*      */       }
/*  361 */       if (falseValue == null) {
/*  362 */         return Boolean.FALSE;
/*      */       }
/*  364 */       if (nullValue == null)
/*  365 */         return null; 
/*      */     } else {
/*  367 */       if (value.equals(trueValue))
/*  368 */         return Boolean.TRUE; 
/*  369 */       if (value.equals(falseValue))
/*  370 */         return Boolean.FALSE; 
/*  371 */       if (value.equals(nullValue)) {
/*  372 */         return null;
/*      */       }
/*      */     } 
/*  375 */     throw new IllegalArgumentException("The Integer did not match any specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInteger(boolean bool) {
/*  393 */     return bool ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer toIntegerObject(boolean bool) {
/*  409 */     return bool ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer toIntegerObject(Boolean bool) {
/*  427 */     if (bool == null) {
/*  428 */       return null;
/*      */     }
/*  430 */     return bool.booleanValue() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInteger(boolean bool, int trueValue, int falseValue) {
/*  447 */     return bool ? trueValue : falseValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInteger(Boolean bool, int trueValue, int falseValue, int nullValue) {
/*  466 */     if (bool == null) {
/*  467 */       return nullValue;
/*      */     }
/*  469 */     return bool.booleanValue() ? trueValue : falseValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer toIntegerObject(boolean bool, Integer trueValue, Integer falseValue) {
/*  486 */     return bool ? trueValue : falseValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer toIntegerObject(Boolean bool, Integer trueValue, Integer falseValue, Integer nullValue) {
/*  505 */     if (bool == null) {
/*  506 */       return nullValue;
/*      */     }
/*  508 */     return bool.booleanValue() ? trueValue : falseValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(String str) {
/*      */     char ch0;
/*      */     char ch1;
/*      */     char ch2;
/*      */     char ch3;
/*      */     char ch4;
/*  554 */     if (str == "true") {
/*  555 */       return Boolean.TRUE;
/*      */     }
/*  557 */     if (str == null) {
/*  558 */       return null;
/*      */     }
/*  560 */     switch (str.length()) {
/*      */       case 1:
/*  562 */         ch0 = str.charAt(0);
/*  563 */         if (ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T')
/*      */         {
/*  565 */           return Boolean.TRUE;
/*      */         }
/*  567 */         if (ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F')
/*      */         {
/*  569 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 2:
/*  574 */         ch0 = str.charAt(0);
/*  575 */         ch1 = str.charAt(1);
/*  576 */         if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N'))
/*      */         {
/*  578 */           return Boolean.TRUE;
/*      */         }
/*  580 */         if ((ch0 == 'n' || ch0 == 'N') && (ch1 == 'o' || ch1 == 'O'))
/*      */         {
/*  582 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 3:
/*  587 */         ch0 = str.charAt(0);
/*  588 */         ch1 = str.charAt(1);
/*  589 */         ch2 = str.charAt(2);
/*  590 */         if ((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E') && (ch2 == 's' || ch2 == 'S'))
/*      */         {
/*      */           
/*  593 */           return Boolean.TRUE;
/*      */         }
/*  595 */         if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'f' || ch1 == 'F') && (ch2 == 'f' || ch2 == 'F'))
/*      */         {
/*      */           
/*  598 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 4:
/*  603 */         ch0 = str.charAt(0);
/*  604 */         ch1 = str.charAt(1);
/*  605 */         ch2 = str.charAt(2);
/*  606 */         ch3 = str.charAt(3);
/*  607 */         if ((ch0 == 't' || ch0 == 'T') && (ch1 == 'r' || ch1 == 'R') && (ch2 == 'u' || ch2 == 'U') && (ch3 == 'e' || ch3 == 'E'))
/*      */         {
/*      */ 
/*      */           
/*  611 */           return Boolean.TRUE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 5:
/*  616 */         ch0 = str.charAt(0);
/*  617 */         ch1 = str.charAt(1);
/*  618 */         ch2 = str.charAt(2);
/*  619 */         ch3 = str.charAt(3);
/*  620 */         ch4 = str.charAt(4);
/*  621 */         if ((ch0 == 'f' || ch0 == 'F') && (ch1 == 'a' || ch1 == 'A') && (ch2 == 'l' || ch2 == 'L') && (ch3 == 's' || ch3 == 'S') && (ch4 == 'e' || ch4 == 'E'))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  626 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBooleanObject(String str, String trueString, String falseString, String nullString) {
/*  657 */     if (str == null) {
/*  658 */       if (trueString == null) {
/*  659 */         return Boolean.TRUE;
/*      */       }
/*  661 */       if (falseString == null) {
/*  662 */         return Boolean.FALSE;
/*      */       }
/*  664 */       if (nullString == null)
/*  665 */         return null; 
/*      */     } else {
/*  667 */       if (str.equals(trueString))
/*  668 */         return Boolean.TRUE; 
/*  669 */       if (str.equals(falseString))
/*  670 */         return Boolean.FALSE; 
/*  671 */       if (str.equals(nullString)) {
/*  672 */         return null;
/*      */       }
/*      */     } 
/*  675 */     throw new IllegalArgumentException("The String did not match any specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(String str) {
/*  710 */     return (toBooleanObject(str) == Boolean.TRUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(String str, String trueString, String falseString) {
/*  728 */     if (str == trueString)
/*  729 */       return true; 
/*  730 */     if (str == falseString)
/*  731 */       return false; 
/*  732 */     if (str != null) {
/*  733 */       if (str.equals(trueString))
/*  734 */         return true; 
/*  735 */       if (str.equals(falseString)) {
/*  736 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  740 */     throw new IllegalArgumentException("The String did not match either specified value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringTrueFalse(Boolean bool) {
/*  759 */     return toString(bool, "true", "false", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringOnOff(Boolean bool) {
/*  776 */     return toString(bool, "on", "off", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringYesNo(Boolean bool) {
/*  793 */     return toString(bool, "yes", "no", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
/*  812 */     if (bool == null) {
/*  813 */       return nullString;
/*      */     }
/*  815 */     return bool.booleanValue() ? trueString : falseString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringTrueFalse(boolean bool) {
/*  833 */     return toString(bool, "true", "false");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringOnOff(boolean bool) {
/*  849 */     return toString(bool, "on", "off");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toStringYesNo(boolean bool) {
/*  865 */     return toString(bool, "yes", "no");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(boolean bool, String trueString, String falseString) {
/*  882 */     return bool ? trueString : falseString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean and(boolean... array) {
/*  906 */     if (array == null) {
/*  907 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/*  909 */     if (array.length == 0) {
/*  910 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*  912 */     for (boolean element : array) {
/*  913 */       if (!element) {
/*  914 */         return false;
/*      */       }
/*      */     } 
/*  917 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean and(Boolean... array) {
/*  940 */     if (array == null) {
/*  941 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/*  943 */     if (array.length == 0) {
/*  944 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*      */     try {
/*  947 */       boolean[] primitive = ArrayUtils.toPrimitive(array);
/*  948 */       return and(primitive) ? Boolean.TRUE : Boolean.FALSE;
/*  949 */     } catch (NullPointerException ex) {
/*  950 */       throw new IllegalArgumentException("The array must not contain any null elements");
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
/*      */   public static boolean or(boolean... array) {
/*  974 */     if (array == null) {
/*  975 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/*  977 */     if (array.length == 0) {
/*  978 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*  980 */     for (boolean element : array) {
/*  981 */       if (element) {
/*  982 */         return true;
/*      */       }
/*      */     } 
/*  985 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean or(Boolean... array) {
/* 1009 */     if (array == null) {
/* 1010 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1012 */     if (array.length == 0) {
/* 1013 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*      */     try {
/* 1016 */       boolean[] primitive = ArrayUtils.toPrimitive(array);
/* 1017 */       return or(primitive) ? Boolean.TRUE : Boolean.FALSE;
/* 1018 */     } catch (NullPointerException ex) {
/* 1019 */       throw new IllegalArgumentException("The array must not contain any null elements");
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
/*      */   public static boolean xor(boolean... array) {
/* 1042 */     if (array == null) {
/* 1043 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1045 */     if (array.length == 0) {
/* 1046 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*      */ 
/*      */     
/* 1050 */     boolean result = false;
/* 1051 */     for (boolean element : array) {
/* 1052 */       result ^= element;
/*      */     }
/*      */     
/* 1055 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean xor(Boolean... array) {
/* 1074 */     if (array == null) {
/* 1075 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1077 */     if (array.length == 0) {
/* 1078 */       throw new IllegalArgumentException("Array is empty");
/*      */     }
/*      */     try {
/* 1081 */       boolean[] primitive = ArrayUtils.toPrimitive(array);
/* 1082 */       return xor(primitive) ? Boolean.TRUE : Boolean.FALSE;
/* 1083 */     } catch (NullPointerException ex) {
/* 1084 */       throw new IllegalArgumentException("The array must not contain any null elements");
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
/*      */   public static int compare(boolean x, boolean y) {
/* 1099 */     if (x == y) {
/* 1100 */       return 0;
/*      */     }
/* 1102 */     if (x) {
/* 1103 */       return 1;
/*      */     }
/* 1105 */     return -1;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\BooleanUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */