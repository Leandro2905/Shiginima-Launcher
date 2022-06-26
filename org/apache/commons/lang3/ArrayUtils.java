/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*      */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringStyle;
/*      */ import org.apache.commons.lang3.math.NumberUtils;
/*      */ import org.apache.commons.lang3.mutable.MutableInt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ArrayUtils
/*      */ {
/*   51 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*      */ 
/*      */ 
/*      */   
/*   55 */   public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*      */ 
/*      */ 
/*      */   
/*   59 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*      */ 
/*      */   
/*   63 */   public static final long[] EMPTY_LONG_ARRAY = new long[0];
/*      */ 
/*      */ 
/*      */   
/*   67 */   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
/*      */ 
/*      */ 
/*      */   
/*   71 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/*      */ 
/*      */ 
/*      */   
/*   75 */   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
/*      */ 
/*      */ 
/*      */   
/*   79 */   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
/*      */ 
/*      */ 
/*      */   
/*   83 */   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
/*      */ 
/*      */ 
/*      */   
/*   87 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */   
/*   91 */   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
/*      */ 
/*      */ 
/*      */   
/*   95 */   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
/*      */ 
/*      */ 
/*      */   
/*   99 */   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
/*      */ 
/*      */ 
/*      */   
/*  103 */   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
/*      */ 
/*      */ 
/*      */   
/*  107 */   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
/*      */ 
/*      */ 
/*      */   
/*  111 */   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/*      */ 
/*      */ 
/*      */   
/*  115 */   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
/*      */ 
/*      */ 
/*      */   
/*  119 */   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*      */ 
/*      */ 
/*      */   
/*  123 */   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Object array) {
/*  161 */     return toString(array, "{}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Object array, String stringIfNull) {
/*  177 */     if (array == null) {
/*  178 */       return stringIfNull;
/*      */     }
/*  180 */     return (new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE)).append(array).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hashCode(Object array) {
/*  192 */     return (new HashCodeBuilder()).append(array).toHashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static boolean isEquals(Object array1, Object array2) {
/*  209 */     return (new EqualsBuilder()).append(array1, array2).isEquals();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<Object, Object> toMap(Object[] array) {
/*  240 */     if (array == null) {
/*  241 */       return null;
/*      */     }
/*  243 */     Map<Object, Object> map = new HashMap<Object, Object>((int)(array.length * 1.5D));
/*  244 */     for (int i = 0; i < array.length; i++) {
/*  245 */       Object object = array[i];
/*  246 */       if (object instanceof Map.Entry) {
/*  247 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  248 */         map.put(entry.getKey(), entry.getValue());
/*  249 */       } else if (object instanceof Object[]) {
/*  250 */         Object[] entry = (Object[])object;
/*  251 */         if (entry.length < 2) {
/*  252 */           throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
/*      */         }
/*      */ 
/*      */         
/*  256 */         map.put(entry[0], entry[1]);
/*      */       } else {
/*  258 */         throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  263 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] toArray(T... items) {
/*  306 */     return items;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] clone(T[] array) {
/*  325 */     if (array == null) {
/*  326 */       return null;
/*      */     }
/*  328 */     return (T[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] clone(long[] array) {
/*  341 */     if (array == null) {
/*  342 */       return null;
/*      */     }
/*  344 */     return (long[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] clone(int[] array) {
/*  357 */     if (array == null) {
/*  358 */       return null;
/*      */     }
/*  360 */     return (int[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] clone(short[] array) {
/*  373 */     if (array == null) {
/*  374 */       return null;
/*      */     }
/*  376 */     return (short[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] clone(char[] array) {
/*  389 */     if (array == null) {
/*  390 */       return null;
/*      */     }
/*  392 */     return (char[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] clone(byte[] array) {
/*  405 */     if (array == null) {
/*  406 */       return null;
/*      */     }
/*  408 */     return (byte[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] clone(double[] array) {
/*  421 */     if (array == null) {
/*  422 */       return null;
/*      */     }
/*  424 */     return (double[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] clone(float[] array) {
/*  437 */     if (array == null) {
/*  438 */       return null;
/*      */     }
/*  440 */     return (float[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] clone(boolean[] array) {
/*  453 */     if (array == null) {
/*  454 */       return null;
/*      */     }
/*  456 */     return (boolean[])array.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] nullToEmpty(Object[] array) {
/*  475 */     if (isEmpty(array)) {
/*  476 */       return EMPTY_OBJECT_ARRAY;
/*      */     }
/*  478 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] nullToEmpty(Class<?>[] array) {
/*  495 */     if (isEmpty((Object[])array)) {
/*  496 */       return EMPTY_CLASS_ARRAY;
/*      */     }
/*  498 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] nullToEmpty(String[] array) {
/*  515 */     if (isEmpty((Object[])array)) {
/*  516 */       return EMPTY_STRING_ARRAY;
/*      */     }
/*  518 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] nullToEmpty(long[] array) {
/*  535 */     if (isEmpty(array)) {
/*  536 */       return EMPTY_LONG_ARRAY;
/*      */     }
/*  538 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] nullToEmpty(int[] array) {
/*  555 */     if (isEmpty(array)) {
/*  556 */       return EMPTY_INT_ARRAY;
/*      */     }
/*  558 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] nullToEmpty(short[] array) {
/*  575 */     if (isEmpty(array)) {
/*  576 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/*  578 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] nullToEmpty(char[] array) {
/*  595 */     if (isEmpty(array)) {
/*  596 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/*  598 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] nullToEmpty(byte[] array) {
/*  615 */     if (isEmpty(array)) {
/*  616 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*  618 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] nullToEmpty(double[] array) {
/*  635 */     if (isEmpty(array)) {
/*  636 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/*  638 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] nullToEmpty(float[] array) {
/*  655 */     if (isEmpty(array)) {
/*  656 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/*  658 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] nullToEmpty(boolean[] array) {
/*  675 */     if (isEmpty(array)) {
/*  676 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/*  678 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Long[] nullToEmpty(Long[] array) {
/*  695 */     if (isEmpty((Object[])array)) {
/*  696 */       return EMPTY_LONG_OBJECT_ARRAY;
/*      */     }
/*  698 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer[] nullToEmpty(Integer[] array) {
/*  715 */     if (isEmpty((Object[])array)) {
/*  716 */       return EMPTY_INTEGER_OBJECT_ARRAY;
/*      */     }
/*  718 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Short[] nullToEmpty(Short[] array) {
/*  735 */     if (isEmpty((Object[])array)) {
/*  736 */       return EMPTY_SHORT_OBJECT_ARRAY;
/*      */     }
/*  738 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Character[] nullToEmpty(Character[] array) {
/*  755 */     if (isEmpty((Object[])array)) {
/*  756 */       return EMPTY_CHARACTER_OBJECT_ARRAY;
/*      */     }
/*  758 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Byte[] nullToEmpty(Byte[] array) {
/*  775 */     if (isEmpty((Object[])array)) {
/*  776 */       return EMPTY_BYTE_OBJECT_ARRAY;
/*      */     }
/*  778 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Double[] nullToEmpty(Double[] array) {
/*  795 */     if (isEmpty((Object[])array)) {
/*  796 */       return EMPTY_DOUBLE_OBJECT_ARRAY;
/*      */     }
/*  798 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Float[] nullToEmpty(Float[] array) {
/*  815 */     if (isEmpty((Object[])array)) {
/*  816 */       return EMPTY_FLOAT_OBJECT_ARRAY;
/*      */     }
/*  818 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean[] nullToEmpty(Boolean[] array) {
/*  835 */     if (isEmpty((Object[])array)) {
/*  836 */       return EMPTY_BOOLEAN_OBJECT_ARRAY;
/*      */     }
/*  838 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
/*  873 */     if (array == null) {
/*  874 */       return null;
/*      */     }
/*  876 */     if (startIndexInclusive < 0) {
/*  877 */       startIndexInclusive = 0;
/*      */     }
/*  879 */     if (endIndexExclusive > array.length) {
/*  880 */       endIndexExclusive = array.length;
/*      */     }
/*  882 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  883 */     Class<?> type = array.getClass().getComponentType();
/*  884 */     if (newSize <= 0) {
/*      */       
/*  886 */       T[] emptyArray = (T[])Array.newInstance(type, 0);
/*  887 */       return emptyArray;
/*      */     } 
/*      */ 
/*      */     
/*  891 */     T[] subarray = (T[])Array.newInstance(type, newSize);
/*  892 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  893 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
/*  917 */     if (array == null) {
/*  918 */       return null;
/*      */     }
/*  920 */     if (startIndexInclusive < 0) {
/*  921 */       startIndexInclusive = 0;
/*      */     }
/*  923 */     if (endIndexExclusive > array.length) {
/*  924 */       endIndexExclusive = array.length;
/*      */     }
/*  926 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  927 */     if (newSize <= 0) {
/*  928 */       return EMPTY_LONG_ARRAY;
/*      */     }
/*      */     
/*  931 */     long[] subarray = new long[newSize];
/*  932 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  933 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
/*  957 */     if (array == null) {
/*  958 */       return null;
/*      */     }
/*  960 */     if (startIndexInclusive < 0) {
/*  961 */       startIndexInclusive = 0;
/*      */     }
/*  963 */     if (endIndexExclusive > array.length) {
/*  964 */       endIndexExclusive = array.length;
/*      */     }
/*  966 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  967 */     if (newSize <= 0) {
/*  968 */       return EMPTY_INT_ARRAY;
/*      */     }
/*      */     
/*  971 */     int[] subarray = new int[newSize];
/*  972 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  973 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
/*  997 */     if (array == null) {
/*  998 */       return null;
/*      */     }
/* 1000 */     if (startIndexInclusive < 0) {
/* 1001 */       startIndexInclusive = 0;
/*      */     }
/* 1003 */     if (endIndexExclusive > array.length) {
/* 1004 */       endIndexExclusive = array.length;
/*      */     }
/* 1006 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1007 */     if (newSize <= 0) {
/* 1008 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/*      */     
/* 1011 */     short[] subarray = new short[newSize];
/* 1012 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1013 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1037 */     if (array == null) {
/* 1038 */       return null;
/*      */     }
/* 1040 */     if (startIndexInclusive < 0) {
/* 1041 */       startIndexInclusive = 0;
/*      */     }
/* 1043 */     if (endIndexExclusive > array.length) {
/* 1044 */       endIndexExclusive = array.length;
/*      */     }
/* 1046 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1047 */     if (newSize <= 0) {
/* 1048 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/*      */     
/* 1051 */     char[] subarray = new char[newSize];
/* 1052 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1053 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1077 */     if (array == null) {
/* 1078 */       return null;
/*      */     }
/* 1080 */     if (startIndexInclusive < 0) {
/* 1081 */       startIndexInclusive = 0;
/*      */     }
/* 1083 */     if (endIndexExclusive > array.length) {
/* 1084 */       endIndexExclusive = array.length;
/*      */     }
/* 1086 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1087 */     if (newSize <= 0) {
/* 1088 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*      */     
/* 1091 */     byte[] subarray = new byte[newSize];
/* 1092 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1093 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1117 */     if (array == null) {
/* 1118 */       return null;
/*      */     }
/* 1120 */     if (startIndexInclusive < 0) {
/* 1121 */       startIndexInclusive = 0;
/*      */     }
/* 1123 */     if (endIndexExclusive > array.length) {
/* 1124 */       endIndexExclusive = array.length;
/*      */     }
/* 1126 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1127 */     if (newSize <= 0) {
/* 1128 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/*      */     
/* 1131 */     double[] subarray = new double[newSize];
/* 1132 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1133 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1157 */     if (array == null) {
/* 1158 */       return null;
/*      */     }
/* 1160 */     if (startIndexInclusive < 0) {
/* 1161 */       startIndexInclusive = 0;
/*      */     }
/* 1163 */     if (endIndexExclusive > array.length) {
/* 1164 */       endIndexExclusive = array.length;
/*      */     }
/* 1166 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1167 */     if (newSize <= 0) {
/* 1168 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/*      */     
/* 1171 */     float[] subarray = new float[newSize];
/* 1172 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1173 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1197 */     if (array == null) {
/* 1198 */       return null;
/*      */     }
/* 1200 */     if (startIndexInclusive < 0) {
/* 1201 */       startIndexInclusive = 0;
/*      */     }
/* 1203 */     if (endIndexExclusive > array.length) {
/* 1204 */       endIndexExclusive = array.length;
/*      */     }
/* 1206 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1207 */     if (newSize <= 0) {
/* 1208 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/*      */     
/* 1211 */     boolean[] subarray = new boolean[newSize];
/* 1212 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1213 */     return subarray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(Object[] array1, Object[] array2) {
/* 1230 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1233 */       return false;
/*      */     }
/* 1235 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(long[] array1, long[] array2) {
/* 1248 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1251 */       return false;
/*      */     }
/* 1253 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(int[] array1, int[] array2) {
/* 1266 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1269 */       return false;
/*      */     }
/* 1271 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(short[] array1, short[] array2) {
/* 1284 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1287 */       return false;
/*      */     }
/* 1289 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(char[] array1, char[] array2) {
/* 1302 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1305 */       return false;
/*      */     }
/* 1307 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(byte[] array1, byte[] array2) {
/* 1320 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1323 */       return false;
/*      */     }
/* 1325 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(double[] array1, double[] array2) {
/* 1338 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1341 */       return false;
/*      */     }
/* 1343 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(float[] array1, float[] array2) {
/* 1356 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1359 */       return false;
/*      */     }
/* 1361 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLength(boolean[] array1, boolean[] array2) {
/* 1374 */     if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
/*      */     {
/*      */       
/* 1377 */       return false;
/*      */     }
/* 1379 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getLength(Object array) {
/* 1404 */     if (array == null) {
/* 1405 */       return 0;
/*      */     }
/* 1407 */     return Array.getLength(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameType(Object array1, Object array2) {
/* 1420 */     if (array1 == null || array2 == null) {
/* 1421 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1423 */     return array1.getClass().getName().equals(array2.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(Object[] array) {
/* 1438 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1441 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(long[] array) {
/* 1452 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1455 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(int[] array) {
/* 1466 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1469 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(short[] array) {
/* 1480 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1483 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(char[] array) {
/* 1494 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1497 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(byte[] array) {
/* 1508 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1511 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(double[] array) {
/* 1522 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1525 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(float[] array) {
/* 1536 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1539 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(boolean[] array) {
/* 1550 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1553 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1576 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1579 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1580 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1582 */     while (j > i) {
/* 1583 */       boolean tmp = array[j];
/* 1584 */       array[j] = array[i];
/* 1585 */       array[i] = tmp;
/* 1586 */       j--;
/* 1587 */       i++;
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
/*      */   public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1611 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1614 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1615 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1617 */     while (j > i) {
/* 1618 */       byte tmp = array[j];
/* 1619 */       array[j] = array[i];
/* 1620 */       array[i] = tmp;
/* 1621 */       j--;
/* 1622 */       i++;
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
/*      */   public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1646 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1649 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1650 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1652 */     while (j > i) {
/* 1653 */       char tmp = array[j];
/* 1654 */       array[j] = array[i];
/* 1655 */       array[i] = tmp;
/* 1656 */       j--;
/* 1657 */       i++;
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
/*      */   public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1681 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1684 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1685 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1687 */     while (j > i) {
/* 1688 */       double tmp = array[j];
/* 1689 */       array[j] = array[i];
/* 1690 */       array[i] = tmp;
/* 1691 */       j--;
/* 1692 */       i++;
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
/*      */   public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1716 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1719 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1720 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1722 */     while (j > i) {
/* 1723 */       float tmp = array[j];
/* 1724 */       array[j] = array[i];
/* 1725 */       array[i] = tmp;
/* 1726 */       j--;
/* 1727 */       i++;
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
/*      */   public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1751 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1754 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1755 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1757 */     while (j > i) {
/* 1758 */       int tmp = array[j];
/* 1759 */       array[j] = array[i];
/* 1760 */       array[i] = tmp;
/* 1761 */       j--;
/* 1762 */       i++;
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
/*      */   public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1786 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1789 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1790 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1792 */     while (j > i) {
/* 1793 */       long tmp = array[j];
/* 1794 */       array[j] = array[i];
/* 1795 */       array[i] = tmp;
/* 1796 */       j--;
/* 1797 */       i++;
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
/*      */   public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1821 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1824 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1825 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1827 */     while (j > i) {
/* 1828 */       Object tmp = array[j];
/* 1829 */       array[j] = array[i];
/* 1830 */       array[i] = tmp;
/* 1831 */       j--;
/* 1832 */       i++;
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
/*      */   public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1856 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1859 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1860 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1862 */     while (j > i) {
/* 1863 */       short tmp = array[j];
/* 1864 */       array[j] = array[i];
/* 1865 */       array[i] = tmp;
/* 1866 */       j--;
/* 1867 */       i++;
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
/*      */   public static int indexOf(Object[] array, Object objectToFind) {
/* 1887 */     return indexOf(array, objectToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
/* 1905 */     if (array == null) {
/* 1906 */       return -1;
/*      */     }
/* 1908 */     if (startIndex < 0) {
/* 1909 */       startIndex = 0;
/*      */     }
/* 1911 */     if (objectToFind == null) {
/* 1912 */       for (int i = startIndex; i < array.length; i++) {
/* 1913 */         if (array[i] == null) {
/* 1914 */           return i;
/*      */         }
/*      */       } 
/* 1917 */     } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
/* 1918 */       for (int i = startIndex; i < array.length; i++) {
/* 1919 */         if (objectToFind.equals(array[i])) {
/* 1920 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1924 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind) {
/* 1938 */     return lastIndexOf(array, objectToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
/* 1956 */     if (array == null) {
/* 1957 */       return -1;
/*      */     }
/* 1959 */     if (startIndex < 0)
/* 1960 */       return -1; 
/* 1961 */     if (startIndex >= array.length) {
/* 1962 */       startIndex = array.length - 1;
/*      */     }
/* 1964 */     if (objectToFind == null) {
/* 1965 */       for (int i = startIndex; i >= 0; i--) {
/* 1966 */         if (array[i] == null) {
/* 1967 */           return i;
/*      */         }
/*      */       } 
/* 1970 */     } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
/* 1971 */       for (int i = startIndex; i >= 0; i--) {
/* 1972 */         if (objectToFind.equals(array[i])) {
/* 1973 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1977 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Object[] array, Object objectToFind) {
/* 1990 */     return (indexOf(array, objectToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(long[] array, long valueToFind) {
/* 2006 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(long[] array, long valueToFind, int startIndex) {
/* 2024 */     if (array == null) {
/* 2025 */       return -1;
/*      */     }
/* 2027 */     if (startIndex < 0) {
/* 2028 */       startIndex = 0;
/*      */     }
/* 2030 */     for (int i = startIndex; i < array.length; i++) {
/* 2031 */       if (valueToFind == array[i]) {
/* 2032 */         return i;
/*      */       }
/*      */     } 
/* 2035 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(long[] array, long valueToFind) {
/* 2049 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
/* 2067 */     if (array == null) {
/* 2068 */       return -1;
/*      */     }
/* 2070 */     if (startIndex < 0)
/* 2071 */       return -1; 
/* 2072 */     if (startIndex >= array.length) {
/* 2073 */       startIndex = array.length - 1;
/*      */     }
/* 2075 */     for (int i = startIndex; i >= 0; i--) {
/* 2076 */       if (valueToFind == array[i]) {
/* 2077 */         return i;
/*      */       }
/*      */     } 
/* 2080 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(long[] array, long valueToFind) {
/* 2093 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(int[] array, int valueToFind) {
/* 2109 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(int[] array, int valueToFind, int startIndex) {
/* 2127 */     if (array == null) {
/* 2128 */       return -1;
/*      */     }
/* 2130 */     if (startIndex < 0) {
/* 2131 */       startIndex = 0;
/*      */     }
/* 2133 */     for (int i = startIndex; i < array.length; i++) {
/* 2134 */       if (valueToFind == array[i]) {
/* 2135 */         return i;
/*      */       }
/*      */     } 
/* 2138 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(int[] array, int valueToFind) {
/* 2152 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
/* 2170 */     if (array == null) {
/* 2171 */       return -1;
/*      */     }
/* 2173 */     if (startIndex < 0)
/* 2174 */       return -1; 
/* 2175 */     if (startIndex >= array.length) {
/* 2176 */       startIndex = array.length - 1;
/*      */     }
/* 2178 */     for (int i = startIndex; i >= 0; i--) {
/* 2179 */       if (valueToFind == array[i]) {
/* 2180 */         return i;
/*      */       }
/*      */     } 
/* 2183 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(int[] array, int valueToFind) {
/* 2196 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(short[] array, short valueToFind) {
/* 2212 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(short[] array, short valueToFind, int startIndex) {
/* 2230 */     if (array == null) {
/* 2231 */       return -1;
/*      */     }
/* 2233 */     if (startIndex < 0) {
/* 2234 */       startIndex = 0;
/*      */     }
/* 2236 */     for (int i = startIndex; i < array.length; i++) {
/* 2237 */       if (valueToFind == array[i]) {
/* 2238 */         return i;
/*      */       }
/*      */     } 
/* 2241 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(short[] array, short valueToFind) {
/* 2255 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
/* 2273 */     if (array == null) {
/* 2274 */       return -1;
/*      */     }
/* 2276 */     if (startIndex < 0)
/* 2277 */       return -1; 
/* 2278 */     if (startIndex >= array.length) {
/* 2279 */       startIndex = array.length - 1;
/*      */     }
/* 2281 */     for (int i = startIndex; i >= 0; i--) {
/* 2282 */       if (valueToFind == array[i]) {
/* 2283 */         return i;
/*      */       }
/*      */     } 
/* 2286 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(short[] array, short valueToFind) {
/* 2299 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(char[] array, char valueToFind) {
/* 2316 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(char[] array, char valueToFind, int startIndex) {
/* 2335 */     if (array == null) {
/* 2336 */       return -1;
/*      */     }
/* 2338 */     if (startIndex < 0) {
/* 2339 */       startIndex = 0;
/*      */     }
/* 2341 */     for (int i = startIndex; i < array.length; i++) {
/* 2342 */       if (valueToFind == array[i]) {
/* 2343 */         return i;
/*      */       }
/*      */     } 
/* 2346 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(char[] array, char valueToFind) {
/* 2361 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
/* 2380 */     if (array == null) {
/* 2381 */       return -1;
/*      */     }
/* 2383 */     if (startIndex < 0)
/* 2384 */       return -1; 
/* 2385 */     if (startIndex >= array.length) {
/* 2386 */       startIndex = array.length - 1;
/*      */     }
/* 2388 */     for (int i = startIndex; i >= 0; i--) {
/* 2389 */       if (valueToFind == array[i]) {
/* 2390 */         return i;
/*      */       }
/*      */     } 
/* 2393 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(char[] array, char valueToFind) {
/* 2407 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(byte[] array, byte valueToFind) {
/* 2423 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
/* 2441 */     if (array == null) {
/* 2442 */       return -1;
/*      */     }
/* 2444 */     if (startIndex < 0) {
/* 2445 */       startIndex = 0;
/*      */     }
/* 2447 */     for (int i = startIndex; i < array.length; i++) {
/* 2448 */       if (valueToFind == array[i]) {
/* 2449 */         return i;
/*      */       }
/*      */     } 
/* 2452 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind) {
/* 2466 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
/* 2484 */     if (array == null) {
/* 2485 */       return -1;
/*      */     }
/* 2487 */     if (startIndex < 0)
/* 2488 */       return -1; 
/* 2489 */     if (startIndex >= array.length) {
/* 2490 */       startIndex = array.length - 1;
/*      */     }
/* 2492 */     for (int i = startIndex; i >= 0; i--) {
/* 2493 */       if (valueToFind == array[i]) {
/* 2494 */         return i;
/*      */       }
/*      */     } 
/* 2497 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(byte[] array, byte valueToFind) {
/* 2510 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(double[] array, double valueToFind) {
/* 2526 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(double[] array, double valueToFind, double tolerance) {
/* 2543 */     return indexOf(array, valueToFind, 0, tolerance);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex) {
/* 2561 */     if (isEmpty(array)) {
/* 2562 */       return -1;
/*      */     }
/* 2564 */     if (startIndex < 0) {
/* 2565 */       startIndex = 0;
/*      */     }
/* 2567 */     for (int i = startIndex; i < array.length; i++) {
/* 2568 */       if (valueToFind == array[i]) {
/* 2569 */         return i;
/*      */       }
/*      */     } 
/* 2572 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 2593 */     if (isEmpty(array)) {
/* 2594 */       return -1;
/*      */     }
/* 2596 */     if (startIndex < 0) {
/* 2597 */       startIndex = 0;
/*      */     }
/* 2599 */     double min = valueToFind - tolerance;
/* 2600 */     double max = valueToFind + tolerance;
/* 2601 */     for (int i = startIndex; i < array.length; i++) {
/* 2602 */       if (array[i] >= min && array[i] <= max) {
/* 2603 */         return i;
/*      */       }
/*      */     } 
/* 2606 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(double[] array, double valueToFind) {
/* 2620 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
/* 2637 */     return lastIndexOf(array, valueToFind, 2147483647, tolerance);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
/* 2655 */     if (isEmpty(array)) {
/* 2656 */       return -1;
/*      */     }
/* 2658 */     if (startIndex < 0)
/* 2659 */       return -1; 
/* 2660 */     if (startIndex >= array.length) {
/* 2661 */       startIndex = array.length - 1;
/*      */     }
/* 2663 */     for (int i = startIndex; i >= 0; i--) {
/* 2664 */       if (valueToFind == array[i]) {
/* 2665 */         return i;
/*      */       }
/*      */     } 
/* 2668 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 2689 */     if (isEmpty(array)) {
/* 2690 */       return -1;
/*      */     }
/* 2692 */     if (startIndex < 0)
/* 2693 */       return -1; 
/* 2694 */     if (startIndex >= array.length) {
/* 2695 */       startIndex = array.length - 1;
/*      */     }
/* 2697 */     double min = valueToFind - tolerance;
/* 2698 */     double max = valueToFind + tolerance;
/* 2699 */     for (int i = startIndex; i >= 0; i--) {
/* 2700 */       if (array[i] >= min && array[i] <= max) {
/* 2701 */         return i;
/*      */       }
/*      */     } 
/* 2704 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(double[] array, double valueToFind) {
/* 2717 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(double[] array, double valueToFind, double tolerance) {
/* 2734 */     return (indexOf(array, valueToFind, 0, tolerance) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(float[] array, float valueToFind) {
/* 2750 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(float[] array, float valueToFind, int startIndex) {
/* 2768 */     if (isEmpty(array)) {
/* 2769 */       return -1;
/*      */     }
/* 2771 */     if (startIndex < 0) {
/* 2772 */       startIndex = 0;
/*      */     }
/* 2774 */     for (int i = startIndex; i < array.length; i++) {
/* 2775 */       if (valueToFind == array[i]) {
/* 2776 */         return i;
/*      */       }
/*      */     } 
/* 2779 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(float[] array, float valueToFind) {
/* 2793 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
/* 2811 */     if (isEmpty(array)) {
/* 2812 */       return -1;
/*      */     }
/* 2814 */     if (startIndex < 0)
/* 2815 */       return -1; 
/* 2816 */     if (startIndex >= array.length) {
/* 2817 */       startIndex = array.length - 1;
/*      */     }
/* 2819 */     for (int i = startIndex; i >= 0; i--) {
/* 2820 */       if (valueToFind == array[i]) {
/* 2821 */         return i;
/*      */       }
/*      */     } 
/* 2824 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(float[] array, float valueToFind) {
/* 2837 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(boolean[] array, boolean valueToFind) {
/* 2853 */     return indexOf(array, valueToFind, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 2872 */     if (isEmpty(array)) {
/* 2873 */       return -1;
/*      */     }
/* 2875 */     if (startIndex < 0) {
/* 2876 */       startIndex = 0;
/*      */     }
/* 2878 */     for (int i = startIndex; i < array.length; i++) {
/* 2879 */       if (valueToFind == array[i]) {
/* 2880 */         return i;
/*      */       }
/*      */     } 
/* 2883 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind) {
/* 2898 */     return lastIndexOf(array, valueToFind, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 2916 */     if (isEmpty(array)) {
/* 2917 */       return -1;
/*      */     }
/* 2919 */     if (startIndex < 0)
/* 2920 */       return -1; 
/* 2921 */     if (startIndex >= array.length) {
/* 2922 */       startIndex = array.length - 1;
/*      */     }
/* 2924 */     for (int i = startIndex; i >= 0; i--) {
/* 2925 */       if (valueToFind == array[i]) {
/* 2926 */         return i;
/*      */       }
/*      */     } 
/* 2929 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(boolean[] array, boolean valueToFind) {
/* 2942 */     return (indexOf(array, valueToFind) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toPrimitive(Character[] array) {
/* 2960 */     if (array == null)
/* 2961 */       return null; 
/* 2962 */     if (array.length == 0) {
/* 2963 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 2965 */     char[] result = new char[array.length];
/* 2966 */     for (int i = 0; i < array.length; i++) {
/* 2967 */       result[i] = array[i].charValue();
/*      */     }
/* 2969 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toPrimitive(Character[] array, char valueForNull) {
/* 2982 */     if (array == null)
/* 2983 */       return null; 
/* 2984 */     if (array.length == 0) {
/* 2985 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 2987 */     char[] result = new char[array.length];
/* 2988 */     for (int i = 0; i < array.length; i++) {
/* 2989 */       Character b = array[i];
/* 2990 */       result[i] = (b == null) ? valueForNull : b.charValue();
/*      */     } 
/* 2992 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Character[] toObject(char[] array) {
/* 3004 */     if (array == null)
/* 3005 */       return null; 
/* 3006 */     if (array.length == 0) {
/* 3007 */       return EMPTY_CHARACTER_OBJECT_ARRAY;
/*      */     }
/* 3009 */     Character[] result = new Character[array.length];
/* 3010 */     for (int i = 0; i < array.length; i++) {
/* 3011 */       result[i] = Character.valueOf(array[i]);
/*      */     }
/* 3013 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] toPrimitive(Long[] array) {
/* 3028 */     if (array == null)
/* 3029 */       return null; 
/* 3030 */     if (array.length == 0) {
/* 3031 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 3033 */     long[] result = new long[array.length];
/* 3034 */     for (int i = 0; i < array.length; i++) {
/* 3035 */       result[i] = array[i].longValue();
/*      */     }
/* 3037 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] toPrimitive(Long[] array, long valueForNull) {
/* 3050 */     if (array == null)
/* 3051 */       return null; 
/* 3052 */     if (array.length == 0) {
/* 3053 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 3055 */     long[] result = new long[array.length];
/* 3056 */     for (int i = 0; i < array.length; i++) {
/* 3057 */       Long b = array[i];
/* 3058 */       result[i] = (b == null) ? valueForNull : b.longValue();
/*      */     } 
/* 3060 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Long[] toObject(long[] array) {
/* 3072 */     if (array == null)
/* 3073 */       return null; 
/* 3074 */     if (array.length == 0) {
/* 3075 */       return EMPTY_LONG_OBJECT_ARRAY;
/*      */     }
/* 3077 */     Long[] result = new Long[array.length];
/* 3078 */     for (int i = 0; i < array.length; i++) {
/* 3079 */       result[i] = Long.valueOf(array[i]);
/*      */     }
/* 3081 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] toPrimitive(Integer[] array) {
/* 3096 */     if (array == null)
/* 3097 */       return null; 
/* 3098 */     if (array.length == 0) {
/* 3099 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 3101 */     int[] result = new int[array.length];
/* 3102 */     for (int i = 0; i < array.length; i++) {
/* 3103 */       result[i] = array[i].intValue();
/*      */     }
/* 3105 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] toPrimitive(Integer[] array, int valueForNull) {
/* 3118 */     if (array == null)
/* 3119 */       return null; 
/* 3120 */     if (array.length == 0) {
/* 3121 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 3123 */     int[] result = new int[array.length];
/* 3124 */     for (int i = 0; i < array.length; i++) {
/* 3125 */       Integer b = array[i];
/* 3126 */       result[i] = (b == null) ? valueForNull : b.intValue();
/*      */     } 
/* 3128 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Integer[] toObject(int[] array) {
/* 3140 */     if (array == null)
/* 3141 */       return null; 
/* 3142 */     if (array.length == 0) {
/* 3143 */       return EMPTY_INTEGER_OBJECT_ARRAY;
/*      */     }
/* 3145 */     Integer[] result = new Integer[array.length];
/* 3146 */     for (int i = 0; i < array.length; i++) {
/* 3147 */       result[i] = Integer.valueOf(array[i]);
/*      */     }
/* 3149 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] toPrimitive(Short[] array) {
/* 3164 */     if (array == null)
/* 3165 */       return null; 
/* 3166 */     if (array.length == 0) {
/* 3167 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 3169 */     short[] result = new short[array.length];
/* 3170 */     for (int i = 0; i < array.length; i++) {
/* 3171 */       result[i] = array[i].shortValue();
/*      */     }
/* 3173 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] toPrimitive(Short[] array, short valueForNull) {
/* 3186 */     if (array == null)
/* 3187 */       return null; 
/* 3188 */     if (array.length == 0) {
/* 3189 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 3191 */     short[] result = new short[array.length];
/* 3192 */     for (int i = 0; i < array.length; i++) {
/* 3193 */       Short b = array[i];
/* 3194 */       result[i] = (b == null) ? valueForNull : b.shortValue();
/*      */     } 
/* 3196 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Short[] toObject(short[] array) {
/* 3208 */     if (array == null)
/* 3209 */       return null; 
/* 3210 */     if (array.length == 0) {
/* 3211 */       return EMPTY_SHORT_OBJECT_ARRAY;
/*      */     }
/* 3213 */     Short[] result = new Short[array.length];
/* 3214 */     for (int i = 0; i < array.length; i++) {
/* 3215 */       result[i] = Short.valueOf(array[i]);
/*      */     }
/* 3217 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toPrimitive(Byte[] array) {
/* 3232 */     if (array == null)
/* 3233 */       return null; 
/* 3234 */     if (array.length == 0) {
/* 3235 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 3237 */     byte[] result = new byte[array.length];
/* 3238 */     for (int i = 0; i < array.length; i++) {
/* 3239 */       result[i] = array[i].byteValue();
/*      */     }
/* 3241 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
/* 3254 */     if (array == null)
/* 3255 */       return null; 
/* 3256 */     if (array.length == 0) {
/* 3257 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 3259 */     byte[] result = new byte[array.length];
/* 3260 */     for (int i = 0; i < array.length; i++) {
/* 3261 */       Byte b = array[i];
/* 3262 */       result[i] = (b == null) ? valueForNull : b.byteValue();
/*      */     } 
/* 3264 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Byte[] toObject(byte[] array) {
/* 3276 */     if (array == null)
/* 3277 */       return null; 
/* 3278 */     if (array.length == 0) {
/* 3279 */       return EMPTY_BYTE_OBJECT_ARRAY;
/*      */     }
/* 3281 */     Byte[] result = new Byte[array.length];
/* 3282 */     for (int i = 0; i < array.length; i++) {
/* 3283 */       result[i] = Byte.valueOf(array[i]);
/*      */     }
/* 3285 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] toPrimitive(Double[] array) {
/* 3300 */     if (array == null)
/* 3301 */       return null; 
/* 3302 */     if (array.length == 0) {
/* 3303 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 3305 */     double[] result = new double[array.length];
/* 3306 */     for (int i = 0; i < array.length; i++) {
/* 3307 */       result[i] = array[i].doubleValue();
/*      */     }
/* 3309 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] toPrimitive(Double[] array, double valueForNull) {
/* 3322 */     if (array == null)
/* 3323 */       return null; 
/* 3324 */     if (array.length == 0) {
/* 3325 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 3327 */     double[] result = new double[array.length];
/* 3328 */     for (int i = 0; i < array.length; i++) {
/* 3329 */       Double b = array[i];
/* 3330 */       result[i] = (b == null) ? valueForNull : b.doubleValue();
/*      */     } 
/* 3332 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Double[] toObject(double[] array) {
/* 3344 */     if (array == null)
/* 3345 */       return null; 
/* 3346 */     if (array.length == 0) {
/* 3347 */       return EMPTY_DOUBLE_OBJECT_ARRAY;
/*      */     }
/* 3349 */     Double[] result = new Double[array.length];
/* 3350 */     for (int i = 0; i < array.length; i++) {
/* 3351 */       result[i] = Double.valueOf(array[i]);
/*      */     }
/* 3353 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] toPrimitive(Float[] array) {
/* 3368 */     if (array == null)
/* 3369 */       return null; 
/* 3370 */     if (array.length == 0) {
/* 3371 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 3373 */     float[] result = new float[array.length];
/* 3374 */     for (int i = 0; i < array.length; i++) {
/* 3375 */       result[i] = array[i].floatValue();
/*      */     }
/* 3377 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] toPrimitive(Float[] array, float valueForNull) {
/* 3390 */     if (array == null)
/* 3391 */       return null; 
/* 3392 */     if (array.length == 0) {
/* 3393 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 3395 */     float[] result = new float[array.length];
/* 3396 */     for (int i = 0; i < array.length; i++) {
/* 3397 */       Float b = array[i];
/* 3398 */       result[i] = (b == null) ? valueForNull : b.floatValue();
/*      */     } 
/* 3400 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Float[] toObject(float[] array) {
/* 3412 */     if (array == null)
/* 3413 */       return null; 
/* 3414 */     if (array.length == 0) {
/* 3415 */       return EMPTY_FLOAT_OBJECT_ARRAY;
/*      */     }
/* 3417 */     Float[] result = new Float[array.length];
/* 3418 */     for (int i = 0; i < array.length; i++) {
/* 3419 */       result[i] = Float.valueOf(array[i]);
/*      */     }
/* 3421 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] toPrimitive(Boolean[] array) {
/* 3436 */     if (array == null)
/* 3437 */       return null; 
/* 3438 */     if (array.length == 0) {
/* 3439 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/* 3441 */     boolean[] result = new boolean[array.length];
/* 3442 */     for (int i = 0; i < array.length; i++) {
/* 3443 */       result[i] = array[i].booleanValue();
/*      */     }
/* 3445 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
/* 3458 */     if (array == null)
/* 3459 */       return null; 
/* 3460 */     if (array.length == 0) {
/* 3461 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/* 3463 */     boolean[] result = new boolean[array.length];
/* 3464 */     for (int i = 0; i < array.length; i++) {
/* 3465 */       Boolean b = array[i];
/* 3466 */       result[i] = (b == null) ? valueForNull : b.booleanValue();
/*      */     } 
/* 3468 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean[] toObject(boolean[] array) {
/* 3480 */     if (array == null)
/* 3481 */       return null; 
/* 3482 */     if (array.length == 0) {
/* 3483 */       return EMPTY_BOOLEAN_OBJECT_ARRAY;
/*      */     }
/* 3485 */     Boolean[] result = new Boolean[array.length];
/* 3486 */     for (int i = 0; i < array.length; i++) {
/* 3487 */       result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/* 3489 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Object[] array) {
/* 3501 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(long[] array) {
/* 3512 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(int[] array) {
/* 3523 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(short[] array) {
/* 3534 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(char[] array) {
/* 3545 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(byte[] array) {
/* 3556 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(double[] array) {
/* 3567 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(float[] array) {
/* 3578 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(boolean[] array) {
/* 3589 */     return (array == null || array.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean isNotEmpty(T[] array) {
/* 3602 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(long[] array) {
/* 3613 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(int[] array) {
/* 3624 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(short[] array) {
/* 3635 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(char[] array) {
/* 3646 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(byte[] array) {
/* 3657 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(double[] array) {
/* 3668 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(float[] array) {
/* 3679 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(boolean[] array) {
/* 3690 */     return (array != null && array.length != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] addAll(T[] array1, T... array2) {
/* 3718 */     if (array1 == null)
/* 3719 */       return clone(array2); 
/* 3720 */     if (array2 == null) {
/* 3721 */       return clone(array1);
/*      */     }
/* 3723 */     Class<?> type1 = array1.getClass().getComponentType();
/*      */ 
/*      */     
/* 3726 */     T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
/* 3727 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*      */     try {
/* 3729 */       System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3730 */     } catch (ArrayStoreException ase) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3737 */       Class<?> type2 = array2.getClass().getComponentType();
/* 3738 */       if (!type1.isAssignableFrom(type2)) {
/* 3739 */         throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
/*      */       }
/*      */       
/* 3742 */       throw ase;
/*      */     } 
/* 3744 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] addAll(boolean[] array1, boolean... array2) {
/* 3765 */     if (array1 == null)
/* 3766 */       return clone(array2); 
/* 3767 */     if (array2 == null) {
/* 3768 */       return clone(array1);
/*      */     }
/* 3770 */     boolean[] joinedArray = new boolean[array1.length + array2.length];
/* 3771 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3772 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3773 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] addAll(char[] array1, char... array2) {
/* 3794 */     if (array1 == null)
/* 3795 */       return clone(array2); 
/* 3796 */     if (array2 == null) {
/* 3797 */       return clone(array1);
/*      */     }
/* 3799 */     char[] joinedArray = new char[array1.length + array2.length];
/* 3800 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3801 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3802 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] addAll(byte[] array1, byte... array2) {
/* 3823 */     if (array1 == null)
/* 3824 */       return clone(array2); 
/* 3825 */     if (array2 == null) {
/* 3826 */       return clone(array1);
/*      */     }
/* 3828 */     byte[] joinedArray = new byte[array1.length + array2.length];
/* 3829 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3830 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3831 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] addAll(short[] array1, short... array2) {
/* 3852 */     if (array1 == null)
/* 3853 */       return clone(array2); 
/* 3854 */     if (array2 == null) {
/* 3855 */       return clone(array1);
/*      */     }
/* 3857 */     short[] joinedArray = new short[array1.length + array2.length];
/* 3858 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3859 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3860 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] addAll(int[] array1, int... array2) {
/* 3881 */     if (array1 == null)
/* 3882 */       return clone(array2); 
/* 3883 */     if (array2 == null) {
/* 3884 */       return clone(array1);
/*      */     }
/* 3886 */     int[] joinedArray = new int[array1.length + array2.length];
/* 3887 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3888 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3889 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] addAll(long[] array1, long... array2) {
/* 3910 */     if (array1 == null)
/* 3911 */       return clone(array2); 
/* 3912 */     if (array2 == null) {
/* 3913 */       return clone(array1);
/*      */     }
/* 3915 */     long[] joinedArray = new long[array1.length + array2.length];
/* 3916 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3917 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3918 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] addAll(float[] array1, float... array2) {
/* 3939 */     if (array1 == null)
/* 3940 */       return clone(array2); 
/* 3941 */     if (array2 == null) {
/* 3942 */       return clone(array1);
/*      */     }
/* 3944 */     float[] joinedArray = new float[array1.length + array2.length];
/* 3945 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3946 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3947 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] addAll(double[] array1, double... array2) {
/* 3968 */     if (array1 == null)
/* 3969 */       return clone(array2); 
/* 3970 */     if (array2 == null) {
/* 3971 */       return clone(array1);
/*      */     }
/* 3973 */     double[] joinedArray = new double[array1.length + array2.length];
/* 3974 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 3975 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 3976 */     return joinedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] add(T[] array, T element) {
/*      */     Class<?> type;
/* 4010 */     if (array != null) {
/* 4011 */       type = array.getClass().getComponentType();
/* 4012 */     } else if (element != null) {
/* 4013 */       type = element.getClass();
/*      */     } else {
/* 4015 */       throw new IllegalArgumentException("Arguments cannot both be null");
/*      */     } 
/*      */ 
/*      */     
/* 4019 */     T[] newArray = (T[])copyArrayGrow1(array, type);
/* 4020 */     newArray[newArray.length - 1] = element;
/* 4021 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] add(boolean[] array, boolean element) {
/* 4046 */     boolean[] newArray = (boolean[])copyArrayGrow1(array, boolean.class);
/* 4047 */     newArray[newArray.length - 1] = element;
/* 4048 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] add(byte[] array, byte element) {
/* 4073 */     byte[] newArray = (byte[])copyArrayGrow1(array, byte.class);
/* 4074 */     newArray[newArray.length - 1] = element;
/* 4075 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] add(char[] array, char element) {
/* 4100 */     char[] newArray = (char[])copyArrayGrow1(array, char.class);
/* 4101 */     newArray[newArray.length - 1] = element;
/* 4102 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] add(double[] array, double element) {
/* 4127 */     double[] newArray = (double[])copyArrayGrow1(array, double.class);
/* 4128 */     newArray[newArray.length - 1] = element;
/* 4129 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] add(float[] array, float element) {
/* 4154 */     float[] newArray = (float[])copyArrayGrow1(array, float.class);
/* 4155 */     newArray[newArray.length - 1] = element;
/* 4156 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] add(int[] array, int element) {
/* 4181 */     int[] newArray = (int[])copyArrayGrow1(array, int.class);
/* 4182 */     newArray[newArray.length - 1] = element;
/* 4183 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] add(long[] array, long element) {
/* 4208 */     long[] newArray = (long[])copyArrayGrow1(array, long.class);
/* 4209 */     newArray[newArray.length - 1] = element;
/* 4210 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] add(short[] array, short element) {
/* 4235 */     short[] newArray = (short[])copyArrayGrow1(array, short.class);
/* 4236 */     newArray[newArray.length - 1] = element;
/* 4237 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
/* 4250 */     if (array != null) {
/* 4251 */       int arrayLength = Array.getLength(array);
/* 4252 */       Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
/* 4253 */       System.arraycopy(array, 0, newArray, 0, arrayLength);
/* 4254 */       return newArray;
/*      */     } 
/* 4256 */     return Array.newInstance(newArrayComponentType, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] add(T[] array, int index, T element) {
/* 4289 */     Class<?> clss = null;
/* 4290 */     if (array != null) {
/* 4291 */       clss = array.getClass().getComponentType();
/* 4292 */     } else if (element != null) {
/* 4293 */       clss = element.getClass();
/*      */     } else {
/* 4295 */       throw new IllegalArgumentException("Array and element cannot both be null");
/*      */     } 
/*      */     
/* 4298 */     T[] newArray = (T[])add(array, index, element, clss);
/* 4299 */     return newArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] add(boolean[] array, int index, boolean element) {
/* 4329 */     return (boolean[])add(array, index, Boolean.valueOf(element), boolean.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] add(char[] array, int index, char element) {
/* 4361 */     return (char[])add(array, index, Character.valueOf(element), char.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] add(byte[] array, int index, byte element) {
/* 4392 */     return (byte[])add(array, index, Byte.valueOf(element), byte.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] add(short[] array, int index, short element) {
/* 4423 */     return (short[])add(array, index, Short.valueOf(element), short.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] add(int[] array, int index, int element) {
/* 4454 */     return (int[])add(array, index, Integer.valueOf(element), int.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] add(long[] array, int index, long element) {
/* 4485 */     return (long[])add(array, index, Long.valueOf(element), long.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] add(float[] array, int index, float element) {
/* 4516 */     return (float[])add(array, index, Float.valueOf(element), float.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] add(double[] array, int index, double element) {
/* 4547 */     return (double[])add(array, index, Double.valueOf(element), double.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object add(Object array, int index, Object element, Class<?> clss) {
/* 4562 */     if (array == null) {
/* 4563 */       if (index != 0) {
/* 4564 */         throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
/*      */       }
/* 4566 */       Object joinedArray = Array.newInstance(clss, 1);
/* 4567 */       Array.set(joinedArray, 0, element);
/* 4568 */       return joinedArray;
/*      */     } 
/* 4570 */     int length = Array.getLength(array);
/* 4571 */     if (index > length || index < 0) {
/* 4572 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/* 4574 */     Object result = Array.newInstance(clss, length + 1);
/* 4575 */     System.arraycopy(array, 0, result, 0, index);
/* 4576 */     Array.set(result, index, element);
/* 4577 */     if (index < length) {
/* 4578 */       System.arraycopy(array, index, result, index + 1, length - index);
/*      */     }
/* 4580 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] remove(T[] array, int index) {
/* 4614 */     return (T[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] removeElement(T[] array, Object element) {
/* 4644 */     int index = indexOf((Object[])array, element);
/* 4645 */     if (index == -1) {
/* 4646 */       return clone(array);
/*      */     }
/* 4648 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] remove(boolean[] array, int index) {
/* 4680 */     return (boolean[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] removeElement(boolean[] array, boolean element) {
/* 4709 */     int index = indexOf(array, element);
/* 4710 */     if (index == -1) {
/* 4711 */       return clone(array);
/*      */     }
/* 4713 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] remove(byte[] array, int index) {
/* 4745 */     return (byte[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] removeElement(byte[] array, byte element) {
/* 4774 */     int index = indexOf(array, element);
/* 4775 */     if (index == -1) {
/* 4776 */       return clone(array);
/*      */     }
/* 4778 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] remove(char[] array, int index) {
/* 4810 */     return (char[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] removeElement(char[] array, char element) {
/* 4839 */     int index = indexOf(array, element);
/* 4840 */     if (index == -1) {
/* 4841 */       return clone(array);
/*      */     }
/* 4843 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] remove(double[] array, int index) {
/* 4875 */     return (double[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] removeElement(double[] array, double element) {
/* 4904 */     int index = indexOf(array, element);
/* 4905 */     if (index == -1) {
/* 4906 */       return clone(array);
/*      */     }
/* 4908 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] remove(float[] array, int index) {
/* 4940 */     return (float[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] removeElement(float[] array, float element) {
/* 4969 */     int index = indexOf(array, element);
/* 4970 */     if (index == -1) {
/* 4971 */       return clone(array);
/*      */     }
/* 4973 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] remove(int[] array, int index) {
/* 5005 */     return (int[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] removeElement(int[] array, int element) {
/* 5034 */     int index = indexOf(array, element);
/* 5035 */     if (index == -1) {
/* 5036 */       return clone(array);
/*      */     }
/* 5038 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] remove(long[] array, int index) {
/* 5070 */     return (long[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] removeElement(long[] array, long element) {
/* 5099 */     int index = indexOf(array, element);
/* 5100 */     if (index == -1) {
/* 5101 */       return clone(array);
/*      */     }
/* 5103 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] remove(short[] array, int index) {
/* 5135 */     return (short[])remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] removeElement(short[] array, short element) {
/* 5164 */     int index = indexOf(array, element);
/* 5165 */     if (index == -1) {
/* 5166 */       return clone(array);
/*      */     }
/* 5168 */     return remove(array, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object remove(Object array, int index) {
/* 5193 */     int length = getLength(array);
/* 5194 */     if (index < 0 || index >= length) {
/* 5195 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/*      */     
/* 5198 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
/* 5199 */     System.arraycopy(array, 0, result, 0, index);
/* 5200 */     if (index < length - 1) {
/* 5201 */       System.arraycopy(array, index + 1, result, index, length - index - 1);
/*      */     }
/*      */     
/* 5204 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] removeAll(T[] array, int... indices) {
/* 5235 */     return (T[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] removeElements(T[] array, T... values) {
/* 5267 */     if (isEmpty((Object[])array) || isEmpty((Object[])values)) {
/* 5268 */       return clone(array);
/*      */     }
/* 5270 */     HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(values.length);
/* 5271 */     for (T v : values) {
/* 5272 */       MutableInt count = occurrences.get(v);
/* 5273 */       if (count == null) {
/* 5274 */         occurrences.put(v, new MutableInt(1));
/*      */       } else {
/* 5276 */         count.increment();
/*      */       } 
/*      */     } 
/* 5279 */     BitSet toRemove = new BitSet();
/* 5280 */     for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
/* 5281 */       T v = e.getKey();
/* 5282 */       int found = 0;
/* 5283 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5284 */         found = indexOf((Object[])array, v, found);
/* 5285 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5288 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5293 */     T[] result = (T[])removeAll(array, toRemove);
/* 5294 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] removeAll(byte[] array, int... indices) {
/* 5327 */     return (byte[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] removeElements(byte[] array, byte... values) {
/* 5358 */     if (isEmpty(array) || isEmpty(values)) {
/* 5359 */       return clone(array);
/*      */     }
/* 5361 */     Map<Byte, MutableInt> occurrences = new HashMap<Byte, MutableInt>(values.length);
/* 5362 */     for (byte v : values) {
/* 5363 */       Byte boxed = Byte.valueOf(v);
/* 5364 */       MutableInt count = occurrences.get(boxed);
/* 5365 */       if (count == null) {
/* 5366 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5368 */         count.increment();
/*      */       } 
/*      */     } 
/* 5371 */     BitSet toRemove = new BitSet();
/* 5372 */     for (Map.Entry<Byte, MutableInt> e : occurrences.entrySet()) {
/* 5373 */       Byte v = e.getKey();
/* 5374 */       int found = 0;
/* 5375 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5376 */         found = indexOf(array, v.byteValue(), found);
/* 5377 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5380 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5383 */     return (byte[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] removeAll(short[] array, int... indices) {
/* 5416 */     return (short[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] removeElements(short[] array, short... values) {
/* 5447 */     if (isEmpty(array) || isEmpty(values)) {
/* 5448 */       return clone(array);
/*      */     }
/* 5450 */     HashMap<Short, MutableInt> occurrences = new HashMap<Short, MutableInt>(values.length);
/* 5451 */     for (short v : values) {
/* 5452 */       Short boxed = Short.valueOf(v);
/* 5453 */       MutableInt count = occurrences.get(boxed);
/* 5454 */       if (count == null) {
/* 5455 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5457 */         count.increment();
/*      */       } 
/*      */     } 
/* 5460 */     BitSet toRemove = new BitSet();
/* 5461 */     for (Map.Entry<Short, MutableInt> e : occurrences.entrySet()) {
/* 5462 */       Short v = e.getKey();
/* 5463 */       int found = 0;
/* 5464 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5465 */         found = indexOf(array, v.shortValue(), found);
/* 5466 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5469 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5472 */     return (short[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] removeAll(int[] array, int... indices) {
/* 5505 */     return (int[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] removeElements(int[] array, int... values) {
/* 5536 */     if (isEmpty(array) || isEmpty(values)) {
/* 5537 */       return clone(array);
/*      */     }
/* 5539 */     HashMap<Integer, MutableInt> occurrences = new HashMap<Integer, MutableInt>(values.length);
/* 5540 */     for (int v : values) {
/* 5541 */       Integer boxed = Integer.valueOf(v);
/* 5542 */       MutableInt count = occurrences.get(boxed);
/* 5543 */       if (count == null) {
/* 5544 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5546 */         count.increment();
/*      */       } 
/*      */     } 
/* 5549 */     BitSet toRemove = new BitSet();
/* 5550 */     for (Map.Entry<Integer, MutableInt> e : occurrences.entrySet()) {
/* 5551 */       Integer v = e.getKey();
/* 5552 */       int found = 0;
/* 5553 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5554 */         found = indexOf(array, v.intValue(), found);
/* 5555 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5558 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5561 */     return (int[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] removeAll(char[] array, int... indices) {
/* 5594 */     return (char[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] removeElements(char[] array, char... values) {
/* 5625 */     if (isEmpty(array) || isEmpty(values)) {
/* 5626 */       return clone(array);
/*      */     }
/* 5628 */     HashMap<Character, MutableInt> occurrences = new HashMap<Character, MutableInt>(values.length);
/* 5629 */     for (char v : values) {
/* 5630 */       Character boxed = Character.valueOf(v);
/* 5631 */       MutableInt count = occurrences.get(boxed);
/* 5632 */       if (count == null) {
/* 5633 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5635 */         count.increment();
/*      */       } 
/*      */     } 
/* 5638 */     BitSet toRemove = new BitSet();
/* 5639 */     for (Map.Entry<Character, MutableInt> e : occurrences.entrySet()) {
/* 5640 */       Character v = e.getKey();
/* 5641 */       int found = 0;
/* 5642 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5643 */         found = indexOf(array, v.charValue(), found);
/* 5644 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5647 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5650 */     return (char[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] removeAll(long[] array, int... indices) {
/* 5683 */     return (long[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] removeElements(long[] array, long... values) {
/* 5714 */     if (isEmpty(array) || isEmpty(values)) {
/* 5715 */       return clone(array);
/*      */     }
/* 5717 */     HashMap<Long, MutableInt> occurrences = new HashMap<Long, MutableInt>(values.length);
/* 5718 */     for (long v : values) {
/* 5719 */       Long boxed = Long.valueOf(v);
/* 5720 */       MutableInt count = occurrences.get(boxed);
/* 5721 */       if (count == null) {
/* 5722 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5724 */         count.increment();
/*      */       } 
/*      */     } 
/* 5727 */     BitSet toRemove = new BitSet();
/* 5728 */     for (Map.Entry<Long, MutableInt> e : occurrences.entrySet()) {
/* 5729 */       Long v = e.getKey();
/* 5730 */       int found = 0;
/* 5731 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5732 */         found = indexOf(array, v.longValue(), found);
/* 5733 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5736 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5739 */     return (long[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] removeAll(float[] array, int... indices) {
/* 5772 */     return (float[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] removeElements(float[] array, float... values) {
/* 5803 */     if (isEmpty(array) || isEmpty(values)) {
/* 5804 */       return clone(array);
/*      */     }
/* 5806 */     HashMap<Float, MutableInt> occurrences = new HashMap<Float, MutableInt>(values.length);
/* 5807 */     for (float v : values) {
/* 5808 */       Float boxed = Float.valueOf(v);
/* 5809 */       MutableInt count = occurrences.get(boxed);
/* 5810 */       if (count == null) {
/* 5811 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5813 */         count.increment();
/*      */       } 
/*      */     } 
/* 5816 */     BitSet toRemove = new BitSet();
/* 5817 */     for (Map.Entry<Float, MutableInt> e : occurrences.entrySet()) {
/* 5818 */       Float v = e.getKey();
/* 5819 */       int found = 0;
/* 5820 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5821 */         found = indexOf(array, v.floatValue(), found);
/* 5822 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5825 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5828 */     return (float[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] removeAll(double[] array, int... indices) {
/* 5861 */     return (double[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] removeElements(double[] array, double... values) {
/* 5892 */     if (isEmpty(array) || isEmpty(values)) {
/* 5893 */       return clone(array);
/*      */     }
/* 5895 */     HashMap<Double, MutableInt> occurrences = new HashMap<Double, MutableInt>(values.length);
/* 5896 */     for (double v : values) {
/* 5897 */       Double boxed = Double.valueOf(v);
/* 5898 */       MutableInt count = occurrences.get(boxed);
/* 5899 */       if (count == null) {
/* 5900 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5902 */         count.increment();
/*      */       } 
/*      */     } 
/* 5905 */     BitSet toRemove = new BitSet();
/* 5906 */     for (Map.Entry<Double, MutableInt> e : occurrences.entrySet()) {
/* 5907 */       Double v = e.getKey();
/* 5908 */       int found = 0;
/* 5909 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5910 */         found = indexOf(array, v.doubleValue(), found);
/* 5911 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5914 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 5917 */     return (double[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] removeAll(boolean[] array, int... indices) {
/* 5946 */     return (boolean[])removeAll(array, clone(indices));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] removeElements(boolean[] array, boolean... values) {
/* 5977 */     if (isEmpty(array) || isEmpty(values)) {
/* 5978 */       return clone(array);
/*      */     }
/* 5980 */     HashMap<Boolean, MutableInt> occurrences = new HashMap<Boolean, MutableInt>(2);
/* 5981 */     for (boolean v : values) {
/* 5982 */       Boolean boxed = Boolean.valueOf(v);
/* 5983 */       MutableInt count = occurrences.get(boxed);
/* 5984 */       if (count == null) {
/* 5985 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 5987 */         count.increment();
/*      */       } 
/*      */     } 
/* 5990 */     BitSet toRemove = new BitSet();
/* 5991 */     for (Map.Entry<Boolean, MutableInt> e : occurrences.entrySet()) {
/* 5992 */       Boolean v = e.getKey();
/* 5993 */       int found = 0;
/* 5994 */       for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
/* 5995 */         found = indexOf(array, v.booleanValue(), found);
/* 5996 */         if (found < 0) {
/*      */           break;
/*      */         }
/* 5999 */         toRemove.set(found++);
/*      */       } 
/*      */     } 
/* 6002 */     return (boolean[])removeAll(array, toRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object removeAll(Object array, int... indices) {
/* 6014 */     int length = getLength(array);
/* 6015 */     int diff = 0;
/*      */     
/* 6017 */     if (isNotEmpty(indices)) {
/* 6018 */       Arrays.sort(indices);
/*      */       
/* 6020 */       int i = indices.length;
/* 6021 */       int prevIndex = length;
/* 6022 */       while (--i >= 0) {
/* 6023 */         int index = indices[i];
/* 6024 */         if (index < 0 || index >= length) {
/* 6025 */           throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */         }
/* 6027 */         if (index >= prevIndex) {
/*      */           continue;
/*      */         }
/* 6030 */         diff++;
/* 6031 */         prevIndex = index;
/*      */       } 
/*      */     } 
/* 6034 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
/* 6035 */     if (diff < length) {
/* 6036 */       int end = length;
/* 6037 */       int dest = length - diff;
/* 6038 */       for (int i = indices.length - 1; i >= 0; i--) {
/* 6039 */         int index = indices[i];
/* 6040 */         if (end - index > 1) {
/* 6041 */           int cp = end - index - 1;
/* 6042 */           dest -= cp;
/* 6043 */           System.arraycopy(array, index + 1, result, dest, cp);
/*      */         } 
/*      */         
/* 6046 */         end = index;
/*      */       } 
/* 6048 */       if (end > 0) {
/* 6049 */         System.arraycopy(array, 0, result, 0, end);
/*      */       }
/*      */     } 
/* 6052 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object removeAll(Object array, BitSet indices) {
/* 6065 */     int srcLength = getLength(array);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6072 */     int removals = indices.cardinality();
/* 6073 */     Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
/* 6074 */     int srcIndex = 0;
/* 6075 */     int destIndex = 0;
/*      */     
/*      */     int set;
/* 6078 */     while ((set = indices.nextSetBit(srcIndex)) != -1) {
/* 6079 */       int i = set - srcIndex;
/* 6080 */       if (i > 0) {
/* 6081 */         System.arraycopy(array, srcIndex, result, destIndex, i);
/* 6082 */         destIndex += i;
/*      */       } 
/* 6084 */       srcIndex = indices.nextClearBit(set);
/*      */     } 
/* 6086 */     int count = srcLength - srcIndex;
/* 6087 */     if (count > 0) {
/* 6088 */       System.arraycopy(array, srcIndex, result, destIndex, count);
/*      */     }
/* 6090 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
/* 6103 */     return isSorted(array, new Comparator<T>()
/*      */         {
/*      */           public int compare(T o1, T o2) {
/* 6106 */             return o1.compareTo(o2);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
/* 6122 */     if (comparator == null) {
/* 6123 */       throw new IllegalArgumentException("Comparator should not be null.");
/*      */     }
/*      */     
/* 6126 */     if (array == null || array.length < 2) {
/* 6127 */       return true;
/*      */     }
/*      */     
/* 6130 */     T previous = array[0];
/* 6131 */     int n = array.length;
/* 6132 */     for (int i = 1; i < n; i++) {
/* 6133 */       T current = array[i];
/* 6134 */       if (comparator.compare(previous, current) > 0) {
/* 6135 */         return false;
/*      */       }
/*      */       
/* 6138 */       previous = current;
/*      */     } 
/* 6140 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(int[] array) {
/* 6151 */     if (array == null || array.length < 2) {
/* 6152 */       return true;
/*      */     }
/*      */     
/* 6155 */     int previous = array[0];
/* 6156 */     int n = array.length;
/* 6157 */     for (int i = 1; i < n; i++) {
/* 6158 */       int current = array[i];
/* 6159 */       if (NumberUtils.compare(previous, current) > 0) {
/* 6160 */         return false;
/*      */       }
/*      */       
/* 6163 */       previous = current;
/*      */     } 
/* 6165 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(long[] array) {
/* 6176 */     if (array == null || array.length < 2) {
/* 6177 */       return true;
/*      */     }
/*      */     
/* 6180 */     long previous = array[0];
/* 6181 */     int n = array.length;
/* 6182 */     for (int i = 1; i < n; i++) {
/* 6183 */       long current = array[i];
/* 6184 */       if (NumberUtils.compare(previous, current) > 0) {
/* 6185 */         return false;
/*      */       }
/*      */       
/* 6188 */       previous = current;
/*      */     } 
/* 6190 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(short[] array) {
/* 6201 */     if (array == null || array.length < 2) {
/* 6202 */       return true;
/*      */     }
/*      */     
/* 6205 */     short previous = array[0];
/* 6206 */     int n = array.length;
/* 6207 */     for (int i = 1; i < n; i++) {
/* 6208 */       short current = array[i];
/* 6209 */       if (NumberUtils.compare(previous, current) > 0) {
/* 6210 */         return false;
/*      */       }
/*      */       
/* 6213 */       previous = current;
/*      */     } 
/* 6215 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(double[] array) {
/* 6226 */     if (array == null || array.length < 2) {
/* 6227 */       return true;
/*      */     }
/*      */     
/* 6230 */     double previous = array[0];
/* 6231 */     int n = array.length;
/* 6232 */     for (int i = 1; i < n; i++) {
/* 6233 */       double current = array[i];
/* 6234 */       if (Double.compare(previous, current) > 0) {
/* 6235 */         return false;
/*      */       }
/*      */       
/* 6238 */       previous = current;
/*      */     } 
/* 6240 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(float[] array) {
/* 6251 */     if (array == null || array.length < 2) {
/* 6252 */       return true;
/*      */     }
/*      */     
/* 6255 */     float previous = array[0];
/* 6256 */     int n = array.length;
/* 6257 */     for (int i = 1; i < n; i++) {
/* 6258 */       float current = array[i];
/* 6259 */       if (Float.compare(previous, current) > 0) {
/* 6260 */         return false;
/*      */       }
/*      */       
/* 6263 */       previous = current;
/*      */     } 
/* 6265 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(byte[] array) {
/* 6276 */     if (array == null || array.length < 2) {
/* 6277 */       return true;
/*      */     }
/*      */     
/* 6280 */     byte previous = array[0];
/* 6281 */     int n = array.length;
/* 6282 */     for (int i = 1; i < n; i++) {
/* 6283 */       byte current = array[i];
/* 6284 */       if (NumberUtils.compare(previous, current) > 0) {
/* 6285 */         return false;
/*      */       }
/*      */       
/* 6288 */       previous = current;
/*      */     } 
/* 6290 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(char[] array) {
/* 6301 */     if (array == null || array.length < 2) {
/* 6302 */       return true;
/*      */     }
/*      */     
/* 6305 */     char previous = array[0];
/* 6306 */     int n = array.length;
/* 6307 */     for (int i = 1; i < n; i++) {
/* 6308 */       char current = array[i];
/* 6309 */       if (CharUtils.compare(previous, current) > 0) {
/* 6310 */         return false;
/*      */       }
/*      */       
/* 6313 */       previous = current;
/*      */     } 
/* 6315 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(boolean[] array) {
/* 6327 */     if (array == null || array.length < 2) {
/* 6328 */       return true;
/*      */     }
/*      */     
/* 6331 */     boolean previous = array[0];
/* 6332 */     int n = array.length;
/* 6333 */     for (int i = 1; i < n; i++) {
/* 6334 */       boolean current = array[i];
/* 6335 */       if (BooleanUtils.compare(previous, current) > 0) {
/* 6336 */         return false;
/*      */       }
/*      */       
/* 6339 */       previous = current;
/*      */     } 
/* 6341 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\ArrayUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */