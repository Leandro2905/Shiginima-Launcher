/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompareToBuilder
/*      */   implements Builder<Integer>
/*      */ {
/*  112 */   private int comparison = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs) {
/*  143 */     return reflectionCompare(lhs, rhs, false, null, new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients) {
/*  175 */     return reflectionCompare(lhs, rhs, compareTransients, null, new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs, Collection<String> excludeFields) {
/*  208 */     return reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs, String... excludeFields) {
/*  241 */     return reflectionCompare(lhs, rhs, false, null, excludeFields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients, Class<?> reflectUpToClass, String... excludeFields) {
/*  283 */     if (lhs == rhs) {
/*  284 */       return 0;
/*      */     }
/*  286 */     if (lhs == null || rhs == null) {
/*  287 */       throw new NullPointerException();
/*      */     }
/*  289 */     Class<?> lhsClazz = lhs.getClass();
/*  290 */     if (!lhsClazz.isInstance(rhs)) {
/*  291 */       throw new ClassCastException();
/*      */     }
/*  293 */     CompareToBuilder compareToBuilder = new CompareToBuilder();
/*  294 */     reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
/*  295 */     while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
/*  296 */       lhsClazz = lhsClazz.getSuperclass();
/*  297 */       reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
/*      */     } 
/*  299 */     return compareToBuilder.toComparison();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, CompareToBuilder builder, boolean useTransients, String[] excludeFields) {
/*  321 */     Field[] fields = clazz.getDeclaredFields();
/*  322 */     AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/*  323 */     for (int i = 0; i < fields.length && builder.comparison == 0; i++) {
/*  324 */       Field f = fields[i];
/*  325 */       if (!ArrayUtils.contains((Object[])excludeFields, f.getName()) && f.getName().indexOf('$') == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers())) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  330 */           builder.append(f.get(lhs), f.get(rhs));
/*  331 */         } catch (IllegalAccessException e) {
/*      */ 
/*      */           
/*  334 */           throw new InternalError("Unexpected IllegalAccessException");
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
/*      */   public CompareToBuilder appendSuper(int superCompareTo) {
/*  350 */     if (this.comparison != 0) {
/*  351 */       return this;
/*      */     }
/*  353 */     this.comparison = superCompareTo;
/*  354 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(Object lhs, Object rhs) {
/*  378 */     return append(lhs, rhs, (Comparator<?>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(Object lhs, Object rhs, Comparator<?> comparator) {
/*  407 */     if (this.comparison != 0) {
/*  408 */       return this;
/*      */     }
/*  410 */     if (lhs == rhs) {
/*  411 */       return this;
/*      */     }
/*  413 */     if (lhs == null) {
/*  414 */       this.comparison = -1;
/*  415 */       return this;
/*      */     } 
/*  417 */     if (rhs == null) {
/*  418 */       this.comparison = 1;
/*  419 */       return this;
/*      */     } 
/*  421 */     if (lhs.getClass().isArray()) {
/*      */ 
/*      */ 
/*      */       
/*  425 */       if (lhs instanceof long[]) {
/*  426 */         append((long[])lhs, (long[])rhs);
/*  427 */       } else if (lhs instanceof int[]) {
/*  428 */         append((int[])lhs, (int[])rhs);
/*  429 */       } else if (lhs instanceof short[]) {
/*  430 */         append((short[])lhs, (short[])rhs);
/*  431 */       } else if (lhs instanceof char[]) {
/*  432 */         append((char[])lhs, (char[])rhs);
/*  433 */       } else if (lhs instanceof byte[]) {
/*  434 */         append((byte[])lhs, (byte[])rhs);
/*  435 */       } else if (lhs instanceof double[]) {
/*  436 */         append((double[])lhs, (double[])rhs);
/*  437 */       } else if (lhs instanceof float[]) {
/*  438 */         append((float[])lhs, (float[])rhs);
/*  439 */       } else if (lhs instanceof boolean[]) {
/*  440 */         append((boolean[])lhs, (boolean[])rhs);
/*      */       }
/*      */       else {
/*      */         
/*  444 */         append((Object[])lhs, (Object[])rhs, comparator);
/*      */       }
/*      */     
/*      */     }
/*  448 */     else if (comparator == null) {
/*      */       
/*  450 */       Comparable<Object> comparable = (Comparable<Object>)lhs;
/*  451 */       this.comparison = comparable.compareTo(rhs);
/*      */     } else {
/*      */       
/*  454 */       Comparator<Object> comparator2 = (Comparator)comparator;
/*  455 */       this.comparison = comparator2.compare(lhs, rhs);
/*      */     } 
/*      */     
/*  458 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(long lhs, long rhs) {
/*  471 */     if (this.comparison != 0) {
/*  472 */       return this;
/*      */     }
/*  474 */     this.comparison = (lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0);
/*  475 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(int lhs, int rhs) {
/*  487 */     if (this.comparison != 0) {
/*  488 */       return this;
/*      */     }
/*  490 */     this.comparison = (lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0);
/*  491 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(short lhs, short rhs) {
/*  503 */     if (this.comparison != 0) {
/*  504 */       return this;
/*      */     }
/*  506 */     this.comparison = (lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0);
/*  507 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(char lhs, char rhs) {
/*  519 */     if (this.comparison != 0) {
/*  520 */       return this;
/*      */     }
/*  522 */     this.comparison = (lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0);
/*  523 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(byte lhs, byte rhs) {
/*  535 */     if (this.comparison != 0) {
/*  536 */       return this;
/*      */     }
/*  538 */     this.comparison = (lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0);
/*  539 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(double lhs, double rhs) {
/*  556 */     if (this.comparison != 0) {
/*  557 */       return this;
/*      */     }
/*  559 */     this.comparison = Double.compare(lhs, rhs);
/*  560 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(float lhs, float rhs) {
/*  577 */     if (this.comparison != 0) {
/*  578 */       return this;
/*      */     }
/*  580 */     this.comparison = Float.compare(lhs, rhs);
/*  581 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(boolean lhs, boolean rhs) {
/*  593 */     if (this.comparison != 0) {
/*  594 */       return this;
/*      */     }
/*  596 */     if (lhs == rhs) {
/*  597 */       return this;
/*      */     }
/*  599 */     if (!lhs) {
/*  600 */       this.comparison = -1;
/*      */     } else {
/*  602 */       this.comparison = 1;
/*      */     } 
/*  604 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(Object[] lhs, Object[] rhs) {
/*  629 */     return append(lhs, rhs, (Comparator<?>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator<?> comparator) {
/*  656 */     if (this.comparison != 0) {
/*  657 */       return this;
/*      */     }
/*  659 */     if (lhs == rhs) {
/*  660 */       return this;
/*      */     }
/*  662 */     if (lhs == null) {
/*  663 */       this.comparison = -1;
/*  664 */       return this;
/*      */     } 
/*  666 */     if (rhs == null) {
/*  667 */       this.comparison = 1;
/*  668 */       return this;
/*      */     } 
/*  670 */     if (lhs.length != rhs.length) {
/*  671 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  672 */       return this;
/*      */     } 
/*  674 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  675 */       append(lhs[i], rhs[i], comparator);
/*      */     }
/*  677 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(long[] lhs, long[] rhs) {
/*  696 */     if (this.comparison != 0) {
/*  697 */       return this;
/*      */     }
/*  699 */     if (lhs == rhs) {
/*  700 */       return this;
/*      */     }
/*  702 */     if (lhs == null) {
/*  703 */       this.comparison = -1;
/*  704 */       return this;
/*      */     } 
/*  706 */     if (rhs == null) {
/*  707 */       this.comparison = 1;
/*  708 */       return this;
/*      */     } 
/*  710 */     if (lhs.length != rhs.length) {
/*  711 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  712 */       return this;
/*      */     } 
/*  714 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  715 */       append(lhs[i], rhs[i]);
/*      */     }
/*  717 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(int[] lhs, int[] rhs) {
/*  736 */     if (this.comparison != 0) {
/*  737 */       return this;
/*      */     }
/*  739 */     if (lhs == rhs) {
/*  740 */       return this;
/*      */     }
/*  742 */     if (lhs == null) {
/*  743 */       this.comparison = -1;
/*  744 */       return this;
/*      */     } 
/*  746 */     if (rhs == null) {
/*  747 */       this.comparison = 1;
/*  748 */       return this;
/*      */     } 
/*  750 */     if (lhs.length != rhs.length) {
/*  751 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  752 */       return this;
/*      */     } 
/*  754 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  755 */       append(lhs[i], rhs[i]);
/*      */     }
/*  757 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(short[] lhs, short[] rhs) {
/*  776 */     if (this.comparison != 0) {
/*  777 */       return this;
/*      */     }
/*  779 */     if (lhs == rhs) {
/*  780 */       return this;
/*      */     }
/*  782 */     if (lhs == null) {
/*  783 */       this.comparison = -1;
/*  784 */       return this;
/*      */     } 
/*  786 */     if (rhs == null) {
/*  787 */       this.comparison = 1;
/*  788 */       return this;
/*      */     } 
/*  790 */     if (lhs.length != rhs.length) {
/*  791 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  792 */       return this;
/*      */     } 
/*  794 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  795 */       append(lhs[i], rhs[i]);
/*      */     }
/*  797 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(char[] lhs, char[] rhs) {
/*  816 */     if (this.comparison != 0) {
/*  817 */       return this;
/*      */     }
/*  819 */     if (lhs == rhs) {
/*  820 */       return this;
/*      */     }
/*  822 */     if (lhs == null) {
/*  823 */       this.comparison = -1;
/*  824 */       return this;
/*      */     } 
/*  826 */     if (rhs == null) {
/*  827 */       this.comparison = 1;
/*  828 */       return this;
/*      */     } 
/*  830 */     if (lhs.length != rhs.length) {
/*  831 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  832 */       return this;
/*      */     } 
/*  834 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  835 */       append(lhs[i], rhs[i]);
/*      */     }
/*  837 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(byte[] lhs, byte[] rhs) {
/*  856 */     if (this.comparison != 0) {
/*  857 */       return this;
/*      */     }
/*  859 */     if (lhs == rhs) {
/*  860 */       return this;
/*      */     }
/*  862 */     if (lhs == null) {
/*  863 */       this.comparison = -1;
/*  864 */       return this;
/*      */     } 
/*  866 */     if (rhs == null) {
/*  867 */       this.comparison = 1;
/*  868 */       return this;
/*      */     } 
/*  870 */     if (lhs.length != rhs.length) {
/*  871 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  872 */       return this;
/*      */     } 
/*  874 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  875 */       append(lhs[i], rhs[i]);
/*      */     }
/*  877 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(double[] lhs, double[] rhs) {
/*  896 */     if (this.comparison != 0) {
/*  897 */       return this;
/*      */     }
/*  899 */     if (lhs == rhs) {
/*  900 */       return this;
/*      */     }
/*  902 */     if (lhs == null) {
/*  903 */       this.comparison = -1;
/*  904 */       return this;
/*      */     } 
/*  906 */     if (rhs == null) {
/*  907 */       this.comparison = 1;
/*  908 */       return this;
/*      */     } 
/*  910 */     if (lhs.length != rhs.length) {
/*  911 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  912 */       return this;
/*      */     } 
/*  914 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  915 */       append(lhs[i], rhs[i]);
/*      */     }
/*  917 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(float[] lhs, float[] rhs) {
/*  936 */     if (this.comparison != 0) {
/*  937 */       return this;
/*      */     }
/*  939 */     if (lhs == rhs) {
/*  940 */       return this;
/*      */     }
/*  942 */     if (lhs == null) {
/*  943 */       this.comparison = -1;
/*  944 */       return this;
/*      */     } 
/*  946 */     if (rhs == null) {
/*  947 */       this.comparison = 1;
/*  948 */       return this;
/*      */     } 
/*  950 */     if (lhs.length != rhs.length) {
/*  951 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  952 */       return this;
/*      */     } 
/*  954 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  955 */       append(lhs[i], rhs[i]);
/*      */     }
/*  957 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
/*  976 */     if (this.comparison != 0) {
/*  977 */       return this;
/*      */     }
/*  979 */     if (lhs == rhs) {
/*  980 */       return this;
/*      */     }
/*  982 */     if (lhs == null) {
/*  983 */       this.comparison = -1;
/*  984 */       return this;
/*      */     } 
/*  986 */     if (rhs == null) {
/*  987 */       this.comparison = 1;
/*  988 */       return this;
/*      */     } 
/*  990 */     if (lhs.length != rhs.length) {
/*  991 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  992 */       return this;
/*      */     } 
/*  994 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  995 */       append(lhs[i], rhs[i]);
/*      */     }
/*  997 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int toComparison() {
/* 1011 */     return this.comparison;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer build() {
/* 1026 */     return Integer.valueOf(toComparison());
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\CompareToBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */