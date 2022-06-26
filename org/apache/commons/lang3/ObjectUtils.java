/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.lang3.exception.CloneFailedException;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ import org.apache.commons.lang3.text.StrBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectUtils
/*     */ {
/*  63 */   public static final Null NULL = new Null();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T defaultIfNull(T object, T defaultValue) {
/*  96 */     return (object != null) ? object : defaultValue;
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
/*     */   public static <T> T firstNonNull(T... values) {
/* 122 */     if (values != null) {
/* 123 */       for (T val : values) {
/* 124 */         if (val != null) {
/* 125 */           return val;
/*     */         }
/*     */       } 
/*     */     }
/* 129 */     return null;
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
/*     */   @Deprecated
/*     */   public static boolean equals(Object object1, Object object2) {
/* 157 */     if (object1 == object2) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (object1 == null || object2 == null) {
/* 161 */       return false;
/*     */     }
/* 163 */     return object1.equals(object2);
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
/*     */   public static boolean notEqual(Object object1, Object object2) {
/* 186 */     return !equals(object1, object2);
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
/*     */   @Deprecated
/*     */   public static int hashCode(Object obj) {
/* 207 */     return (obj == null) ? 0 : obj.hashCode();
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
/*     */   @Deprecated
/*     */   public static int hashCodeMulti(Object... objects) {
/* 234 */     int hash = 1;
/* 235 */     if (objects != null) {
/* 236 */       for (Object object : objects) {
/* 237 */         int tmpHash = hashCode(object);
/* 238 */         hash = hash * 31 + tmpHash;
/*     */       } 
/*     */     }
/* 241 */     return hash;
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
/*     */   public static String identityToString(Object object) {
/* 263 */     if (object == null) {
/* 264 */       return null;
/*     */     }
/* 266 */     StringBuilder builder = new StringBuilder();
/* 267 */     identityToString(builder, object);
/* 268 */     return builder.toString();
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
/*     */   public static void identityToString(Appendable appendable, Object object) throws IOException {
/* 288 */     if (object == null) {
/* 289 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*     */     }
/* 291 */     appendable.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
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
/*     */   public static void identityToString(StrBuilder builder, Object object) {
/* 312 */     if (object == null) {
/* 313 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*     */     }
/* 315 */     builder.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
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
/*     */   public static void identityToString(StringBuffer buffer, Object object) {
/* 336 */     if (object == null) {
/* 337 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*     */     }
/* 339 */     buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
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
/*     */   public static void identityToString(StringBuilder builder, Object object) {
/* 360 */     if (object == null) {
/* 361 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*     */     }
/* 363 */     builder.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
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
/*     */   @Deprecated
/*     */   public static String toString(Object obj) {
/* 392 */     return (obj == null) ? "" : obj.toString();
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
/*     */   @Deprecated
/*     */   public static String toString(Object obj, String nullStr) {
/* 418 */     return (obj == null) ? nullStr : obj.toString();
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
/*     */   public static <T extends Comparable<? super T>> T min(T... values) {
/* 437 */     T result = null;
/* 438 */     if (values != null) {
/* 439 */       for (T value : values) {
/* 440 */         if (compare(value, result, true) < 0) {
/* 441 */           result = value;
/*     */         }
/*     */       } 
/*     */     }
/* 445 */     return result;
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
/*     */   public static <T extends Comparable<? super T>> T max(T... values) {
/* 462 */     T result = null;
/* 463 */     if (values != null) {
/* 464 */       for (T value : values) {
/* 465 */         if (compare(value, result, false) > 0) {
/* 466 */           result = value;
/*     */         }
/*     */       } 
/*     */     }
/* 470 */     return result;
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
/*     */   public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
/* 484 */     return compare(c1, c2, false);
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
/*     */   public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
/* 501 */     if (c1 == c2)
/* 502 */       return 0; 
/* 503 */     if (c1 == null)
/* 504 */       return nullGreater ? 1 : -1; 
/* 505 */     if (c2 == null) {
/* 506 */       return nullGreater ? -1 : 1;
/*     */     }
/* 508 */     return c1.compareTo(c2);
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
/*     */   public static <T extends Comparable<? super T>> T median(T... items) {
/* 522 */     Validate.notEmpty(items);
/* 523 */     Validate.noNullElements(items);
/* 524 */     TreeSet<T> sort = new TreeSet<T>();
/* 525 */     Collections.addAll(sort, items);
/*     */ 
/*     */     
/* 528 */     return (T)sort.toArray()[(sort.size() - 1) / 2];
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
/*     */   public static <T> T median(Comparator<T> comparator, T... items) {
/* 544 */     Validate.notEmpty(items, "null/empty items", new Object[0]);
/* 545 */     Validate.noNullElements(items);
/* 546 */     Validate.notNull(comparator, "null comparator", new Object[0]);
/* 547 */     TreeSet<T> sort = new TreeSet<T>(comparator);
/* 548 */     Collections.addAll(sort, items);
/*     */ 
/*     */     
/* 551 */     T result = (T)sort.toArray()[(sort.size() - 1) / 2];
/* 552 */     return result;
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
/*     */   public static <T> T mode(T... items) {
/* 566 */     if (ArrayUtils.isNotEmpty(items)) {
/* 567 */       HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(items.length);
/* 568 */       for (T t : items) {
/* 569 */         MutableInt count = occurrences.get(t);
/* 570 */         if (count == null) {
/* 571 */           occurrences.put(t, new MutableInt(1));
/*     */         } else {
/* 573 */           count.increment();
/*     */         } 
/*     */       } 
/* 576 */       T result = null;
/* 577 */       int max = 0;
/* 578 */       for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
/* 579 */         int cmp = ((MutableInt)e.getValue()).intValue();
/* 580 */         if (cmp == max) {
/* 581 */           result = null; continue;
/* 582 */         }  if (cmp > max) {
/* 583 */           max = cmp;
/* 584 */           result = e.getKey();
/*     */         } 
/*     */       } 
/* 587 */       return result;
/*     */     } 
/* 589 */     return null;
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
/*     */   public static <T> T clone(T obj) {
/* 604 */     if (obj instanceof Cloneable) {
/*     */       Object result;
/* 606 */       if (obj.getClass().isArray()) {
/* 607 */         Class<?> componentType = obj.getClass().getComponentType();
/* 608 */         if (!componentType.isPrimitive()) {
/* 609 */           result = ((Object[])obj).clone();
/*     */         } else {
/* 611 */           int length = Array.getLength(obj);
/* 612 */           result = Array.newInstance(componentType, length);
/* 613 */           while (length-- > 0) {
/* 614 */             Array.set(result, length, Array.get(obj, length));
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         try {
/* 619 */           Method clone = obj.getClass().getMethod("clone", new Class[0]);
/* 620 */           result = clone.invoke(obj, new Object[0]);
/* 621 */         } catch (NoSuchMethodException e) {
/* 622 */           throw new CloneFailedException("Cloneable type " + obj.getClass().getName() + " has no clone method", e);
/*     */         
/*     */         }
/* 625 */         catch (IllegalAccessException e) {
/* 626 */           throw new CloneFailedException("Cannot clone Cloneable type " + obj.getClass().getName(), e);
/*     */         }
/* 628 */         catch (InvocationTargetException e) {
/* 629 */           throw new CloneFailedException("Exception cloning Cloneable type " + obj.getClass().getName(), e.getCause());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 634 */       T checked = (T)result;
/* 635 */       return checked;
/*     */     } 
/*     */     
/* 638 */     return null;
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
/*     */   public static <T> T cloneIfPossible(T obj) {
/* 658 */     T clone = clone(obj);
/* 659 */     return (clone == null) ? obj : clone;
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
/*     */   public static class Null
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 7092611880189329093L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object readResolve() {
/* 698 */       return ObjectUtils.NULL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean CONST(boolean v) {
/* 741 */     return v;
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
/*     */   public static byte CONST(byte v) {
/* 760 */     return v;
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
/*     */   public static byte CONST_BYTE(int v) throws IllegalArgumentException {
/* 783 */     if (v < -128 || v > 127) {
/* 784 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + v + "]");
/*     */     }
/* 786 */     return (byte)v;
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
/*     */   public static char CONST(char v) {
/* 806 */     return v;
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
/*     */   public static short CONST(short v) {
/* 825 */     return v;
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
/*     */   public static short CONST_SHORT(int v) throws IllegalArgumentException {
/* 848 */     if (v < -32768 || v > 32767) {
/* 849 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + v + "]");
/*     */     }
/* 851 */     return (short)v;
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
/*     */   public static int CONST(int v) {
/* 872 */     return v;
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
/*     */   public static long CONST(long v) {
/* 891 */     return v;
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
/*     */   public static float CONST(float v) {
/* 910 */     return v;
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
/*     */   public static double CONST(double v) {
/* 929 */     return v;
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
/*     */   public static <T> T CONST(T v) {
/* 949 */     return v;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\ObjectUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */