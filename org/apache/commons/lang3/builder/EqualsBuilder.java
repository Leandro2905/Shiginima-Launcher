/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EqualsBuilder
/*     */   implements Builder<Boolean>
/*     */ {
/*  93 */   private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<Set<Pair<IDKey, IDKey>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<Pair<IDKey, IDKey>> getRegistry() {
/* 122 */     return REGISTRY.get();
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
/*     */   static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
/* 136 */     IDKey left = new IDKey(lhs);
/* 137 */     IDKey right = new IDKey(rhs);
/* 138 */     return Pair.of(left, right);
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
/*     */   static boolean isRegistered(Object lhs, Object rhs) {
/* 155 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 156 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 157 */     Pair<IDKey, IDKey> swappedPair = Pair.of(pair.getLeft(), pair.getRight());
/*     */     
/* 159 */     return (registry != null && (registry.contains(pair) || registry.contains(swappedPair)));
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
/*     */   static void register(Object lhs, Object rhs) {
/* 173 */     synchronized (EqualsBuilder.class) {
/* 174 */       if (getRegistry() == null) {
/* 175 */         REGISTRY.set(new HashSet<Pair<IDKey, IDKey>>());
/*     */       }
/*     */     } 
/*     */     
/* 179 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 180 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 181 */     registry.add(pair);
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
/*     */   static void unregister(Object lhs, Object rhs) {
/* 197 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 198 */     if (registry != null) {
/* 199 */       Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 200 */       registry.remove(pair);
/* 201 */       synchronized (EqualsBuilder.class) {
/*     */         
/* 203 */         registry = getRegistry();
/* 204 */         if (registry != null && registry.isEmpty()) {
/* 205 */           REGISTRY.remove();
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEquals = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
/* 250 */     return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
/* 274 */     return reflectionEquals(lhs, rhs, false, null, excludeFields);
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
/* 299 */     return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
/*     */     Class<?> testClass;
/* 331 */     if (lhs == rhs) {
/* 332 */       return true;
/*     */     }
/* 334 */     if (lhs == null || rhs == null) {
/* 335 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     Class<?> lhsClass = lhs.getClass();
/* 342 */     Class<?> rhsClass = rhs.getClass();
/*     */     
/* 344 */     if (lhsClass.isInstance(rhs)) {
/* 345 */       testClass = lhsClass;
/* 346 */       if (!rhsClass.isInstance(lhs))
/*     */       {
/* 348 */         testClass = rhsClass;
/*     */       }
/* 350 */     } else if (rhsClass.isInstance(lhs)) {
/* 351 */       testClass = rhsClass;
/* 352 */       if (!lhsClass.isInstance(rhs))
/*     */       {
/* 354 */         testClass = lhsClass;
/*     */       }
/*     */     } else {
/*     */       
/* 358 */       return false;
/*     */     } 
/* 360 */     EqualsBuilder equalsBuilder = new EqualsBuilder();
/*     */     try {
/* 362 */       if (testClass.isArray()) {
/* 363 */         equalsBuilder.append(lhs, rhs);
/*     */       } else {
/* 365 */         reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
/* 366 */         while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
/* 367 */           testClass = testClass.getSuperclass();
/* 368 */           reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
/*     */         } 
/*     */       } 
/* 371 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 377 */       return false;
/*     */     } 
/* 379 */     return equalsBuilder.isEquals();
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
/*     */   private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
/* 401 */     if (isRegistered(lhs, rhs)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 406 */       register(lhs, rhs);
/* 407 */       Field[] fields = clazz.getDeclaredFields();
/* 408 */       AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 409 */       for (int i = 0; i < fields.length && builder.isEquals; i++) {
/* 410 */         Field f = fields[i];
/* 411 */         if (!ArrayUtils.contains((Object[])excludeFields, f.getName()) && f.getName().indexOf('$') == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers())) {
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 416 */             builder.append(f.get(lhs), f.get(rhs));
/* 417 */           } catch (IllegalAccessException e) {
/*     */ 
/*     */             
/* 420 */             throw new InternalError("Unexpected IllegalAccessException");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } finally {
/* 425 */       unregister(lhs, rhs);
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
/*     */   public EqualsBuilder appendSuper(boolean superEquals) {
/* 439 */     if (!this.isEquals) {
/* 440 */       return this;
/*     */     }
/* 442 */     this.isEquals = superEquals;
/* 443 */     return this;
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
/*     */   public EqualsBuilder append(Object lhs, Object rhs) {
/* 457 */     if (!this.isEquals) {
/* 458 */       return this;
/*     */     }
/* 460 */     if (lhs == rhs) {
/* 461 */       return this;
/*     */     }
/* 463 */     if (lhs == null || rhs == null) {
/* 464 */       setEquals(false);
/* 465 */       return this;
/*     */     } 
/* 467 */     Class<?> lhsClass = lhs.getClass();
/* 468 */     if (!lhsClass.isArray()) {
/*     */       
/* 470 */       this.isEquals = lhs.equals(rhs);
/* 471 */     } else if (lhs.getClass() != rhs.getClass()) {
/*     */       
/* 473 */       setEquals(false);
/*     */ 
/*     */     
/*     */     }
/* 477 */     else if (lhs instanceof long[]) {
/* 478 */       append((long[])lhs, (long[])rhs);
/* 479 */     } else if (lhs instanceof int[]) {
/* 480 */       append((int[])lhs, (int[])rhs);
/* 481 */     } else if (lhs instanceof short[]) {
/* 482 */       append((short[])lhs, (short[])rhs);
/* 483 */     } else if (lhs instanceof char[]) {
/* 484 */       append((char[])lhs, (char[])rhs);
/* 485 */     } else if (lhs instanceof byte[]) {
/* 486 */       append((byte[])lhs, (byte[])rhs);
/* 487 */     } else if (lhs instanceof double[]) {
/* 488 */       append((double[])lhs, (double[])rhs);
/* 489 */     } else if (lhs instanceof float[]) {
/* 490 */       append((float[])lhs, (float[])rhs);
/* 491 */     } else if (lhs instanceof boolean[]) {
/* 492 */       append((boolean[])lhs, (boolean[])rhs);
/*     */     } else {
/*     */       
/* 495 */       append((Object[])lhs, (Object[])rhs);
/*     */     } 
/* 497 */     return this;
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
/*     */   public EqualsBuilder append(long lhs, long rhs) {
/* 512 */     if (!this.isEquals) {
/* 513 */       return this;
/*     */     }
/* 515 */     this.isEquals = (lhs == rhs);
/* 516 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(int lhs, int rhs) {
/* 527 */     if (!this.isEquals) {
/* 528 */       return this;
/*     */     }
/* 530 */     this.isEquals = (lhs == rhs);
/* 531 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(short lhs, short rhs) {
/* 542 */     if (!this.isEquals) {
/* 543 */       return this;
/*     */     }
/* 545 */     this.isEquals = (lhs == rhs);
/* 546 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(char lhs, char rhs) {
/* 557 */     if (!this.isEquals) {
/* 558 */       return this;
/*     */     }
/* 560 */     this.isEquals = (lhs == rhs);
/* 561 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(byte lhs, byte rhs) {
/* 572 */     if (!this.isEquals) {
/* 573 */       return this;
/*     */     }
/* 575 */     this.isEquals = (lhs == rhs);
/* 576 */     return this;
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
/*     */   public EqualsBuilder append(double lhs, double rhs) {
/* 593 */     if (!this.isEquals) {
/* 594 */       return this;
/*     */     }
/* 596 */     return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
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
/*     */   public EqualsBuilder append(float lhs, float rhs) {
/* 613 */     if (!this.isEquals) {
/* 614 */       return this;
/*     */     }
/* 616 */     return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(boolean lhs, boolean rhs) {
/* 627 */     if (!this.isEquals) {
/* 628 */       return this;
/*     */     }
/* 630 */     this.isEquals = (lhs == rhs);
/* 631 */     return this;
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
/*     */   public EqualsBuilder append(Object[] lhs, Object[] rhs) {
/* 645 */     if (!this.isEquals) {
/* 646 */       return this;
/*     */     }
/* 648 */     if (lhs == rhs) {
/* 649 */       return this;
/*     */     }
/* 651 */     if (lhs == null || rhs == null) {
/* 652 */       setEquals(false);
/* 653 */       return this;
/*     */     } 
/* 655 */     if (lhs.length != rhs.length) {
/* 656 */       setEquals(false);
/* 657 */       return this;
/*     */     } 
/* 659 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 660 */       append(lhs[i], rhs[i]);
/*     */     }
/* 662 */     return this;
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
/*     */   public EqualsBuilder append(long[] lhs, long[] rhs) {
/* 676 */     if (!this.isEquals) {
/* 677 */       return this;
/*     */     }
/* 679 */     if (lhs == rhs) {
/* 680 */       return this;
/*     */     }
/* 682 */     if (lhs == null || rhs == null) {
/* 683 */       setEquals(false);
/* 684 */       return this;
/*     */     } 
/* 686 */     if (lhs.length != rhs.length) {
/* 687 */       setEquals(false);
/* 688 */       return this;
/*     */     } 
/* 690 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 691 */       append(lhs[i], rhs[i]);
/*     */     }
/* 693 */     return this;
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
/*     */   public EqualsBuilder append(int[] lhs, int[] rhs) {
/* 707 */     if (!this.isEquals) {
/* 708 */       return this;
/*     */     }
/* 710 */     if (lhs == rhs) {
/* 711 */       return this;
/*     */     }
/* 713 */     if (lhs == null || rhs == null) {
/* 714 */       setEquals(false);
/* 715 */       return this;
/*     */     } 
/* 717 */     if (lhs.length != rhs.length) {
/* 718 */       setEquals(false);
/* 719 */       return this;
/*     */     } 
/* 721 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 722 */       append(lhs[i], rhs[i]);
/*     */     }
/* 724 */     return this;
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
/*     */   public EqualsBuilder append(short[] lhs, short[] rhs) {
/* 738 */     if (!this.isEquals) {
/* 739 */       return this;
/*     */     }
/* 741 */     if (lhs == rhs) {
/* 742 */       return this;
/*     */     }
/* 744 */     if (lhs == null || rhs == null) {
/* 745 */       setEquals(false);
/* 746 */       return this;
/*     */     } 
/* 748 */     if (lhs.length != rhs.length) {
/* 749 */       setEquals(false);
/* 750 */       return this;
/*     */     } 
/* 752 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 753 */       append(lhs[i], rhs[i]);
/*     */     }
/* 755 */     return this;
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
/*     */   public EqualsBuilder append(char[] lhs, char[] rhs) {
/* 769 */     if (!this.isEquals) {
/* 770 */       return this;
/*     */     }
/* 772 */     if (lhs == rhs) {
/* 773 */       return this;
/*     */     }
/* 775 */     if (lhs == null || rhs == null) {
/* 776 */       setEquals(false);
/* 777 */       return this;
/*     */     } 
/* 779 */     if (lhs.length != rhs.length) {
/* 780 */       setEquals(false);
/* 781 */       return this;
/*     */     } 
/* 783 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 784 */       append(lhs[i], rhs[i]);
/*     */     }
/* 786 */     return this;
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
/*     */   public EqualsBuilder append(byte[] lhs, byte[] rhs) {
/* 800 */     if (!this.isEquals) {
/* 801 */       return this;
/*     */     }
/* 803 */     if (lhs == rhs) {
/* 804 */       return this;
/*     */     }
/* 806 */     if (lhs == null || rhs == null) {
/* 807 */       setEquals(false);
/* 808 */       return this;
/*     */     } 
/* 810 */     if (lhs.length != rhs.length) {
/* 811 */       setEquals(false);
/* 812 */       return this;
/*     */     } 
/* 814 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 815 */       append(lhs[i], rhs[i]);
/*     */     }
/* 817 */     return this;
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
/*     */   public EqualsBuilder append(double[] lhs, double[] rhs) {
/* 831 */     if (!this.isEquals) {
/* 832 */       return this;
/*     */     }
/* 834 */     if (lhs == rhs) {
/* 835 */       return this;
/*     */     }
/* 837 */     if (lhs == null || rhs == null) {
/* 838 */       setEquals(false);
/* 839 */       return this;
/*     */     } 
/* 841 */     if (lhs.length != rhs.length) {
/* 842 */       setEquals(false);
/* 843 */       return this;
/*     */     } 
/* 845 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 846 */       append(lhs[i], rhs[i]);
/*     */     }
/* 848 */     return this;
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
/*     */   public EqualsBuilder append(float[] lhs, float[] rhs) {
/* 862 */     if (!this.isEquals) {
/* 863 */       return this;
/*     */     }
/* 865 */     if (lhs == rhs) {
/* 866 */       return this;
/*     */     }
/* 868 */     if (lhs == null || rhs == null) {
/* 869 */       setEquals(false);
/* 870 */       return this;
/*     */     } 
/* 872 */     if (lhs.length != rhs.length) {
/* 873 */       setEquals(false);
/* 874 */       return this;
/*     */     } 
/* 876 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 877 */       append(lhs[i], rhs[i]);
/*     */     }
/* 879 */     return this;
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
/*     */   public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
/* 893 */     if (!this.isEquals) {
/* 894 */       return this;
/*     */     }
/* 896 */     if (lhs == rhs) {
/* 897 */       return this;
/*     */     }
/* 899 */     if (lhs == null || rhs == null) {
/* 900 */       setEquals(false);
/* 901 */       return this;
/*     */     } 
/* 903 */     if (lhs.length != rhs.length) {
/* 904 */       setEquals(false);
/* 905 */       return this;
/*     */     } 
/* 907 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 908 */       append(lhs[i], rhs[i]);
/*     */     }
/* 910 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEquals() {
/* 920 */     return this.isEquals;
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
/*     */   public Boolean build() {
/* 934 */     return Boolean.valueOf(isEquals());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setEquals(boolean isEquals) {
/* 944 */     this.isEquals = isEquals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 952 */     this.isEquals = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\EqualsBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */