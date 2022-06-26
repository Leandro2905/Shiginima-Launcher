/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectionToStringBuilder
/*     */   extends ToStringBuilder
/*     */ {
/*     */   public static String toString(Object object) {
/* 118 */     return toString(object, (ToStringStyle)null, false, false, (Class<? super Object>)null);
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
/*     */   public static String toString(Object object, ToStringStyle style) {
/* 150 */     return toString(object, style, false, false, (Class<? super Object>)null);
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
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients) {
/* 188 */     return toString(object, style, outputTransients, false, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics) {
/* 234 */     return toString(object, style, outputTransients, outputStatics, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> String toString(T object, ToStringStyle style, boolean outputTransients, boolean outputStatics, Class<? super T> reflectUpToClass) {
/* 287 */     return (new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics)).toString();
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
/*     */   public static String toStringExclude(Object object, Collection<String> excludeFieldNames) {
/* 301 */     return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
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
/*     */   static String[] toNoNullStringArray(Collection<String> collection) {
/* 314 */     if (collection == null) {
/* 315 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     }
/* 317 */     return toNoNullStringArray(collection.toArray());
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
/*     */   static String[] toNoNullStringArray(Object[] array) {
/* 330 */     List<String> list = new ArrayList<String>(array.length);
/* 331 */     for (Object e : array) {
/* 332 */       if (e != null) {
/* 333 */         list.add(e.toString());
/*     */       }
/*     */     } 
/* 336 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*     */   public static String toStringExclude(Object object, String... excludeFieldNames) {
/* 350 */     return (new ReflectionToStringBuilder(object)).setExcludeFieldNames(excludeFieldNames).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean appendStatics = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean appendTransients = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] excludeFieldNames;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 373 */   private Class<?> upToClass = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder(Object object) {
/* 390 */     super(object);
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
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style) {
/* 410 */     super(object, style);
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
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
/* 436 */     super(object, style, buffer);
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
/*     */   public <T> ReflectionToStringBuilder(T object, ToStringStyle style, StringBuffer buffer, Class<? super T> reflectUpToClass, boolean outputTransients, boolean outputStatics) {
/* 461 */     super(object, style, buffer);
/* 462 */     setUpToClass(reflectUpToClass);
/* 463 */     setAppendTransients(outputTransients);
/* 464 */     setAppendStatics(outputStatics);
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
/*     */   protected boolean accept(Field field) {
/* 480 */     if (field.getName().indexOf('$') != -1)
/*     */     {
/* 482 */       return false;
/*     */     }
/* 484 */     if (Modifier.isTransient(field.getModifiers()) && !isAppendTransients())
/*     */     {
/* 486 */       return false;
/*     */     }
/* 488 */     if (Modifier.isStatic(field.getModifiers()) && !isAppendStatics())
/*     */     {
/* 490 */       return false;
/*     */     }
/* 492 */     if (this.excludeFieldNames != null && Arrays.binarySearch((Object[])this.excludeFieldNames, field.getName()) >= 0)
/*     */     {
/*     */       
/* 495 */       return false;
/*     */     }
/* 497 */     return true;
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
/*     */   protected void appendFieldsIn(Class<?> clazz) {
/* 514 */     if (clazz.isArray()) {
/* 515 */       reflectionAppendArray(getObject());
/*     */       return;
/*     */     } 
/* 518 */     Field[] fields = clazz.getDeclaredFields();
/* 519 */     AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 520 */     for (Field field : fields) {
/* 521 */       String fieldName = field.getName();
/* 522 */       if (accept(field)) {
/*     */         
/*     */         try {
/*     */           
/* 526 */           Object fieldValue = getValue(field);
/* 527 */           append(fieldName, fieldValue);
/* 528 */         } catch (IllegalAccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 533 */           throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludeFieldNames() {
/* 543 */     return (String[])this.excludeFieldNames.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getUpToClass() {
/* 554 */     return this.upToClass;
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
/*     */   protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 574 */     return field.get(getObject());
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
/*     */   public boolean isAppendStatics() {
/* 586 */     return this.appendStatics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppendTransients() {
/* 597 */     return this.appendTransients;
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
/*     */   public ReflectionToStringBuilder reflectionAppendArray(Object array) {
/* 610 */     getStyle().reflectionAppendArrayDetail(getStringBuffer(), null, array);
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
/*     */   public void setAppendStatics(boolean appendStatics) {
/* 624 */     this.appendStatics = appendStatics;
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
/*     */   public void setAppendTransients(boolean appendTransients) {
/* 636 */     this.appendTransients = appendTransients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder setExcludeFieldNames(String... excludeFieldNamesParam) {
/* 647 */     if (excludeFieldNamesParam == null) {
/* 648 */       this.excludeFieldNames = null;
/*     */     } else {
/*     */       
/* 651 */       this.excludeFieldNames = toNoNullStringArray((Object[])excludeFieldNamesParam);
/* 652 */       Arrays.sort((Object[])this.excludeFieldNames);
/*     */     } 
/* 654 */     return this;
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
/*     */   public void setUpToClass(Class<?> clazz) {
/* 666 */     if (clazz != null) {
/* 667 */       Object object = getObject();
/* 668 */       if (object != null && !clazz.isInstance(object)) {
/* 669 */         throw new IllegalArgumentException("Specified class is not a superclass of the object");
/*     */       }
/*     */     } 
/* 672 */     this.upToClass = clazz;
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
/*     */   public String toString() {
/* 684 */     if (getObject() == null) {
/* 685 */       return getStyle().getNullText();
/*     */     }
/* 687 */     Class<?> clazz = getObject().getClass();
/* 688 */     appendFieldsIn(clazz);
/* 689 */     while (clazz.getSuperclass() != null && clazz != getUpToClass()) {
/* 690 */       clazz = clazz.getSuperclass();
/* 691 */       appendFieldsIn(clazz);
/*     */     } 
/* 693 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\ReflectionToStringBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */