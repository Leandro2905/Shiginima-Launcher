/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldUtils
/*     */ {
/*     */   public static Field getField(Class<?> cls, String fieldName) {
/*  63 */     Field field = getField(cls, fieldName, false);
/*  64 */     MemberUtils.setAccessibleWorkaround(field);
/*  65 */     return field;
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
/*     */   public static Field getField(Class<?> cls, String fieldName, boolean forceAccess) {
/*  86 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/*  87 */     Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
/*     */       try {
/* 105 */         Field field = acls.getDeclaredField(fieldName);
/*     */ 
/*     */         
/* 108 */         if (!Modifier.isPublic(field.getModifiers()))
/* 109 */         { if (forceAccess)
/* 110 */           { field.setAccessible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 115 */             return field; }  } else { return field; } 
/* 116 */       } catch (NoSuchFieldException ex) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     Field match = null;
/* 124 */     for (Class<?> class1 : (Iterable<Class<?>>)ClassUtils.getAllInterfaces(cls)) {
/*     */       try {
/* 126 */         Field test = class1.getField(fieldName);
/* 127 */         Validate.isTrue((match == null), "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", new Object[] { fieldName, cls });
/*     */         
/* 129 */         match = test;
/* 130 */       } catch (NoSuchFieldException ex) {}
/*     */     } 
/*     */ 
/*     */     
/* 134 */     return match;
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
/*     */   public static Field getDeclaredField(Class<?> cls, String fieldName) {
/* 149 */     return getDeclaredField(cls, fieldName, false);
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
/*     */   public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
/* 169 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 170 */     Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
/*     */     
/*     */     try {
/* 173 */       Field field = cls.getDeclaredField(fieldName);
/* 174 */       if (!MemberUtils.isAccessible(field)) {
/* 175 */         if (forceAccess) {
/* 176 */           field.setAccessible(true);
/*     */         } else {
/* 178 */           return null;
/*     */         } 
/*     */       }
/* 181 */       return field;
/* 182 */     } catch (NoSuchFieldException e) {
/*     */ 
/*     */       
/* 185 */       return null;
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
/*     */   public static Field[] getAllFields(Class<?> cls) {
/* 199 */     List<Field> allFieldsList = getAllFieldsList(cls);
/* 200 */     return allFieldsList.<Field>toArray(new Field[allFieldsList.size()]);
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
/*     */   public static List<Field> getAllFieldsList(Class<?> cls) {
/* 214 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 215 */     List<Field> allFields = new ArrayList<Field>();
/* 216 */     Class<?> currentClass = cls;
/* 217 */     while (currentClass != null) {
/* 218 */       Field[] declaredFields = currentClass.getDeclaredFields();
/* 219 */       for (Field field : declaredFields) {
/* 220 */         allFields.add(field);
/*     */       }
/* 222 */       currentClass = currentClass.getSuperclass();
/*     */     } 
/* 224 */     return allFields;
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
/*     */   public static Field[] getFieldsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 239 */     List<Field> annotatedFieldsList = getFieldsListWithAnnotation(cls, annotationCls);
/* 240 */     return annotatedFieldsList.<Field>toArray(new Field[annotatedFieldsList.size()]);
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
/*     */   public static List<Field> getFieldsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 255 */     Validate.isTrue((annotationCls != null), "The annotation class must not be null", new Object[0]);
/* 256 */     List<Field> allFields = getAllFieldsList(cls);
/* 257 */     List<Field> annotatedFields = new ArrayList<Field>();
/* 258 */     for (Field field : allFields) {
/* 259 */       if (field.getAnnotation(annotationCls) != null) {
/* 260 */         annotatedFields.add(field);
/*     */       }
/*     */     } 
/* 263 */     return annotatedFields;
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
/*     */   public static Object readStaticField(Field field) throws IllegalAccessException {
/* 278 */     return readStaticField(field, false);
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
/*     */   public static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
/* 296 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 297 */     Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", new Object[] { field.getName() });
/* 298 */     return readField(field, (Object)null, forceAccess);
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
/*     */   public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
/* 316 */     return readStaticField(cls, fieldName, false);
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
/*     */   public static Object readStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 338 */     Field field = getField(cls, fieldName, forceAccess);
/* 339 */     Validate.isTrue((field != null), "Cannot locate field '%s' on %s", new Object[] { fieldName, cls });
/*     */     
/* 341 */     return readStaticField(field, false);
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
/*     */   public static Object readDeclaredStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
/* 360 */     return readDeclaredStaticField(cls, fieldName, false);
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
/*     */   public static Object readDeclaredStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 382 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 383 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 385 */     return readStaticField(field, false);
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
/*     */   public static Object readField(Field field, Object target) throws IllegalAccessException {
/* 402 */     return readField(field, target, false);
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
/*     */   public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
/* 422 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 423 */     if (forceAccess && !field.isAccessible()) {
/* 424 */       field.setAccessible(true);
/*     */     } else {
/* 426 */       MemberUtils.setAccessibleWorkaround(field);
/*     */     } 
/* 428 */     return field.get(target);
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
/*     */   public static Object readField(Object target, String fieldName) throws IllegalAccessException {
/* 445 */     return readField(target, fieldName, false);
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
/*     */   public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 466 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 467 */     Class<?> cls = target.getClass();
/* 468 */     Field field = getField(cls, fieldName, forceAccess);
/* 469 */     Validate.isTrue((field != null), "Cannot locate field %s on %s", new Object[] { fieldName, cls });
/*     */     
/* 471 */     return readField(field, target, false);
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
/*     */   public static Object readDeclaredField(Object target, String fieldName) throws IllegalAccessException {
/* 488 */     return readDeclaredField(target, fieldName, false);
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
/*     */   public static Object readDeclaredField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 509 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 510 */     Class<?> cls = target.getClass();
/* 511 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 512 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls, fieldName });
/*     */     
/* 514 */     return readField(field, target, false);
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
/*     */   public static void writeStaticField(Field field, Object value) throws IllegalAccessException {
/* 530 */     writeStaticField(field, value, false);
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
/*     */   public static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException {
/* 550 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 551 */     Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", new Object[] { field.getDeclaringClass().getName(), field.getName() });
/*     */     
/* 553 */     writeField(field, (Object)null, value, forceAccess);
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
/*     */   public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
/* 572 */     writeStaticField(cls, fieldName, value, false);
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
/*     */   public static void writeStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 596 */     Field field = getField(cls, fieldName, forceAccess);
/* 597 */     Validate.isTrue((field != null), "Cannot locate field %s on %s", new Object[] { fieldName, cls });
/*     */     
/* 599 */     writeStaticField(field, value, false);
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
/*     */   public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
/* 618 */     writeDeclaredStaticField(cls, fieldName, value, false);
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
/*     */   public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 641 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 642 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 644 */     writeField(field, (Object)null, value, false);
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
/*     */   public static void writeField(Field field, Object target, Object value) throws IllegalAccessException {
/* 661 */     writeField(field, target, value, false);
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
/*     */   public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
/* 684 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 685 */     if (forceAccess && !field.isAccessible()) {
/* 686 */       field.setAccessible(true);
/*     */     } else {
/* 688 */       MemberUtils.setAccessibleWorkaround(field);
/*     */     } 
/* 690 */     field.set(target, value);
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
/*     */   public static void removeFinalModifier(Field field) {
/* 703 */     removeFinalModifier(field, true);
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
/*     */   public static void removeFinalModifier(Field field, boolean forceAccess) {
/* 720 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/*     */     
/*     */     try {
/* 723 */       if (Modifier.isFinal(field.getModifiers())) {
/*     */         
/* 725 */         Field modifiersField = Field.class.getDeclaredField("modifiers");
/* 726 */         boolean doForceAccess = (forceAccess && !modifiersField.isAccessible());
/* 727 */         if (doForceAccess) {
/* 728 */           modifiersField.setAccessible(true);
/*     */         }
/*     */         try {
/* 731 */           modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
/*     */         } finally {
/* 733 */           if (doForceAccess) {
/* 734 */             modifiersField.setAccessible(false);
/*     */           }
/*     */         } 
/*     */       } 
/* 738 */     } catch (NoSuchFieldException ignored) {
/*     */     
/* 740 */     } catch (IllegalAccessException ignored) {}
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
/*     */   public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
/* 761 */     writeField(target, fieldName, value, false);
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
/*     */   public static void writeField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 785 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 786 */     Class<?> cls = target.getClass();
/* 787 */     Field field = getField(cls, fieldName, forceAccess);
/* 788 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 790 */     writeField(field, target, value, false);
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
/*     */   public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException {
/* 809 */     writeDeclaredField(target, fieldName, value, false);
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
/*     */   public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 833 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 834 */     Class<?> cls = target.getClass();
/* 835 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 836 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 838 */     writeField(field, target, value, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\reflect\FieldUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */