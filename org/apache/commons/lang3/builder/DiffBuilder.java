/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiffBuilder
/*     */   implements Builder<DiffResult>
/*     */ {
/*     */   private final List<Diff<?>> diffs;
/*     */   private final boolean objectsTriviallyEqual;
/*     */   private final Object left;
/*     */   private final Object right;
/*     */   private final ToStringStyle style;
/*     */   
/*     */   public DiffBuilder(Object lhs, Object rhs, ToStringStyle style, boolean testTriviallyEqual) {
/* 106 */     if (lhs == null) {
/* 107 */       throw new IllegalArgumentException("lhs cannot be null");
/*     */     }
/* 109 */     if (rhs == null) {
/* 110 */       throw new IllegalArgumentException("rhs cannot be null");
/*     */     }
/*     */     
/* 113 */     this.diffs = new ArrayList<Diff<?>>();
/* 114 */     this.left = lhs;
/* 115 */     this.right = rhs;
/* 116 */     this.style = style;
/*     */ 
/*     */     
/* 119 */     this.objectsTriviallyEqual = (testTriviallyEqual && (lhs == rhs || lhs.equals(rhs)));
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
/*     */   public DiffBuilder(Object lhs, Object rhs, ToStringStyle style) {
/* 151 */     this(lhs, rhs, style, true);
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
/*     */   public DiffBuilder append(String fieldName, final boolean lhs, final boolean rhs) {
/* 171 */     if (fieldName == null) {
/* 172 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 175 */     if (this.objectsTriviallyEqual) {
/* 176 */       return this;
/*     */     }
/* 178 */     if (lhs != rhs) {
/* 179 */       this.diffs.add(new Diff<Boolean>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Boolean getLeft() {
/* 184 */               return Boolean.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean getRight() {
/* 189 */               return Boolean.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 193 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final boolean[] lhs, final boolean[] rhs) {
/* 213 */     if (fieldName == null) {
/* 214 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/* 216 */     if (this.objectsTriviallyEqual) {
/* 217 */       return this;
/*     */     }
/* 219 */     if (!Arrays.equals(lhs, rhs)) {
/* 220 */       this.diffs.add(new Diff<Boolean[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Boolean[] getLeft() {
/* 225 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean[] getRight() {
/* 230 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 234 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final byte lhs, final byte rhs) {
/* 254 */     if (fieldName == null) {
/* 255 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/* 257 */     if (this.objectsTriviallyEqual) {
/* 258 */       return this;
/*     */     }
/* 260 */     if (lhs != rhs) {
/* 261 */       this.diffs.add(new Diff<Byte>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Byte getLeft() {
/* 266 */               return Byte.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Byte getRight() {
/* 271 */               return Byte.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 275 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final byte[] lhs, final byte[] rhs) {
/* 295 */     if (fieldName == null) {
/* 296 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 299 */     if (this.objectsTriviallyEqual) {
/* 300 */       return this;
/*     */     }
/* 302 */     if (!Arrays.equals(lhs, rhs)) {
/* 303 */       this.diffs.add(new Diff<Byte[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Byte[] getLeft() {
/* 308 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Byte[] getRight() {
/* 313 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 317 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final char lhs, final char rhs) {
/* 337 */     if (fieldName == null) {
/* 338 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 341 */     if (this.objectsTriviallyEqual) {
/* 342 */       return this;
/*     */     }
/* 344 */     if (lhs != rhs) {
/* 345 */       this.diffs.add(new Diff<Character>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Character getLeft() {
/* 350 */               return Character.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Character getRight() {
/* 355 */               return Character.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 359 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final char[] lhs, final char[] rhs) {
/* 379 */     if (fieldName == null) {
/* 380 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 383 */     if (this.objectsTriviallyEqual) {
/* 384 */       return this;
/*     */     }
/* 386 */     if (!Arrays.equals(lhs, rhs)) {
/* 387 */       this.diffs.add(new Diff<Character[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Character[] getLeft() {
/* 392 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Character[] getRight() {
/* 397 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 401 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final double lhs, final double rhs) {
/* 421 */     if (fieldName == null) {
/* 422 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 425 */     if (this.objectsTriviallyEqual) {
/* 426 */       return this;
/*     */     }
/* 428 */     if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs)) {
/* 429 */       this.diffs.add(new Diff<Double>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Double getLeft() {
/* 434 */               return Double.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Double getRight() {
/* 439 */               return Double.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiffBuilder append(String fieldName, final double[] lhs, final double[] rhs) {
/* 463 */     if (fieldName == null) {
/* 464 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 467 */     if (this.objectsTriviallyEqual) {
/* 468 */       return this;
/*     */     }
/* 470 */     if (!Arrays.equals(lhs, rhs)) {
/* 471 */       this.diffs.add(new Diff<Double[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Double[] getLeft() {
/* 476 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Double[] getRight() {
/* 481 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 485 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final float lhs, final float rhs) {
/* 505 */     if (fieldName == null) {
/* 506 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 509 */     if (this.objectsTriviallyEqual) {
/* 510 */       return this;
/*     */     }
/* 512 */     if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs)) {
/* 513 */       this.diffs.add(new Diff<Float>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Float getLeft() {
/* 518 */               return Float.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Float getRight() {
/* 523 */               return Float.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 527 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final float[] lhs, final float[] rhs) {
/* 547 */     if (fieldName == null) {
/* 548 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 551 */     if (this.objectsTriviallyEqual) {
/* 552 */       return this;
/*     */     }
/* 554 */     if (!Arrays.equals(lhs, rhs)) {
/* 555 */       this.diffs.add(new Diff<Float[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Float[] getLeft() {
/* 560 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Float[] getRight() {
/* 565 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 569 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final int lhs, final int rhs) {
/* 589 */     if (fieldName == null) {
/* 590 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 593 */     if (this.objectsTriviallyEqual) {
/* 594 */       return this;
/*     */     }
/* 596 */     if (lhs != rhs) {
/* 597 */       this.diffs.add(new Diff<Integer>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Integer getLeft() {
/* 602 */               return Integer.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Integer getRight() {
/* 607 */               return Integer.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 611 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final int[] lhs, final int[] rhs) {
/* 631 */     if (fieldName == null) {
/* 632 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 635 */     if (this.objectsTriviallyEqual) {
/* 636 */       return this;
/*     */     }
/* 638 */     if (!Arrays.equals(lhs, rhs)) {
/* 639 */       this.diffs.add(new Diff<Integer[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Integer[] getLeft() {
/* 644 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Integer[] getRight() {
/* 649 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 653 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final long lhs, final long rhs) {
/* 673 */     if (fieldName == null) {
/* 674 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 677 */     if (this.objectsTriviallyEqual) {
/* 678 */       return this;
/*     */     }
/* 680 */     if (lhs != rhs) {
/* 681 */       this.diffs.add(new Diff<Long>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Long getLeft() {
/* 686 */               return Long.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Long getRight() {
/* 691 */               return Long.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 695 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final long[] lhs, final long[] rhs) {
/* 715 */     if (fieldName == null) {
/* 716 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 719 */     if (this.objectsTriviallyEqual) {
/* 720 */       return this;
/*     */     }
/* 722 */     if (!Arrays.equals(lhs, rhs)) {
/* 723 */       this.diffs.add(new Diff<Long[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Long[] getLeft() {
/* 728 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Long[] getRight() {
/* 733 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 737 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final short lhs, final short rhs) {
/* 757 */     if (fieldName == null) {
/* 758 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 761 */     if (this.objectsTriviallyEqual) {
/* 762 */       return this;
/*     */     }
/* 764 */     if (lhs != rhs) {
/* 765 */       this.diffs.add(new Diff<Short>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Short getLeft() {
/* 770 */               return Short.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Short getRight() {
/* 775 */               return Short.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 779 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final short[] lhs, final short[] rhs) {
/* 799 */     if (fieldName == null) {
/* 800 */       throw new IllegalArgumentException("Field name cannot be null");
/*     */     }
/*     */     
/* 803 */     if (this.objectsTriviallyEqual) {
/* 804 */       return this;
/*     */     }
/* 806 */     if (!Arrays.equals(lhs, rhs)) {
/* 807 */       this.diffs.add(new Diff<Short[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Short[] getLeft() {
/* 812 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Short[] getRight() {
/* 817 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 821 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final Object lhs, final Object rhs) {
/*     */     Object objectToTest;
/* 840 */     if (this.objectsTriviallyEqual) {
/* 841 */       return this;
/*     */     }
/* 843 */     if (lhs == rhs) {
/* 844 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 848 */     if (lhs != null) {
/* 849 */       objectToTest = lhs;
/*     */     } else {
/*     */       
/* 852 */       objectToTest = rhs;
/*     */     } 
/*     */     
/* 855 */     if (objectToTest.getClass().isArray()) {
/* 856 */       if (objectToTest instanceof boolean[]) {
/* 857 */         return append(fieldName, (boolean[])lhs, (boolean[])rhs);
/*     */       }
/* 859 */       if (objectToTest instanceof byte[]) {
/* 860 */         return append(fieldName, (byte[])lhs, (byte[])rhs);
/*     */       }
/* 862 */       if (objectToTest instanceof char[]) {
/* 863 */         return append(fieldName, (char[])lhs, (char[])rhs);
/*     */       }
/* 865 */       if (objectToTest instanceof double[]) {
/* 866 */         return append(fieldName, (double[])lhs, (double[])rhs);
/*     */       }
/* 868 */       if (objectToTest instanceof float[]) {
/* 869 */         return append(fieldName, (float[])lhs, (float[])rhs);
/*     */       }
/* 871 */       if (objectToTest instanceof int[]) {
/* 872 */         return append(fieldName, (int[])lhs, (int[])rhs);
/*     */       }
/* 874 */       if (objectToTest instanceof long[]) {
/* 875 */         return append(fieldName, (long[])lhs, (long[])rhs);
/*     */       }
/* 877 */       if (objectToTest instanceof short[]) {
/* 878 */         return append(fieldName, (short[])lhs, (short[])rhs);
/*     */       }
/*     */       
/* 881 */       return append(fieldName, (Object[])lhs, (Object[])rhs);
/*     */     } 
/*     */ 
/*     */     
/* 885 */     if (lhs != null && lhs.equals(rhs)) {
/* 886 */       return this;
/*     */     }
/*     */     
/* 889 */     this.diffs.add(new Diff(fieldName)
/*     */         {
/*     */           private static final long serialVersionUID = 1L;
/*     */           
/*     */           public Object getLeft() {
/* 894 */             return lhs;
/*     */           }
/*     */ 
/*     */           
/*     */           public Object getRight() {
/* 899 */             return rhs;
/*     */           }
/*     */         });
/*     */     
/* 903 */     return this;
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
/*     */   public DiffBuilder append(String fieldName, final Object[] lhs, final Object[] rhs) {
/* 921 */     if (this.objectsTriviallyEqual) {
/* 922 */       return this;
/*     */     }
/*     */     
/* 925 */     if (!Arrays.equals(lhs, rhs)) {
/* 926 */       this.diffs.add(new Diff<Object[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Object[] getLeft() {
/* 931 */               return lhs;
/*     */             }
/*     */ 
/*     */             
/*     */             public Object[] getRight() {
/* 936 */               return rhs;
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 941 */     return this;
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
/*     */   public DiffResult build() {
/* 955 */     return new DiffResult(this.left, this.right, this.diffs, this.style);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\DiffBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */