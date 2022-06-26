/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Element
/*     */   extends AccessibleObject
/*     */   implements Member
/*     */ {
/*     */   private final AccessibleObject accessibleObject;
/*     */   private final Member member;
/*     */   
/*     */   <M extends AccessibleObject & Member> Element(M member) {
/*  43 */     Preconditions.checkNotNull(member);
/*  44 */     this.accessibleObject = (AccessibleObject)member;
/*  45 */     this.member = (Member)member;
/*     */   }
/*     */   
/*     */   public TypeToken<?> getOwnerType() {
/*  49 */     return TypeToken.of(getDeclaringClass());
/*     */   }
/*     */   
/*     */   public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/*  53 */     return this.accessibleObject.isAnnotationPresent(annotationClass);
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
/*  57 */     return this.accessibleObject.getAnnotation(annotationClass);
/*     */   }
/*     */   
/*     */   public final Annotation[] getAnnotations() {
/*  61 */     return this.accessibleObject.getAnnotations();
/*     */   }
/*     */   
/*     */   public final Annotation[] getDeclaredAnnotations() {
/*  65 */     return this.accessibleObject.getDeclaredAnnotations();
/*     */   }
/*     */   
/*     */   public final void setAccessible(boolean flag) throws SecurityException {
/*  69 */     this.accessibleObject.setAccessible(flag);
/*     */   }
/*     */   
/*     */   public final boolean isAccessible() {
/*  73 */     return this.accessibleObject.isAccessible();
/*     */   }
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/*  77 */     return this.member.getDeclaringClass();
/*     */   }
/*     */   
/*     */   public final String getName() {
/*  81 */     return this.member.getName();
/*     */   }
/*     */   
/*     */   public final int getModifiers() {
/*  85 */     return this.member.getModifiers();
/*     */   }
/*     */   
/*     */   public final boolean isSynthetic() {
/*  89 */     return this.member.isSynthetic();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPublic() {
/*  94 */     return Modifier.isPublic(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isProtected() {
/*  99 */     return Modifier.isProtected(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPackagePrivate() {
/* 104 */     return (!isPrivate() && !isPublic() && !isProtected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPrivate() {
/* 109 */     return Modifier.isPrivate(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStatic() {
/* 114 */     return Modifier.isStatic(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isFinal() {
/* 125 */     return Modifier.isFinal(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAbstract() {
/* 130 */     return Modifier.isAbstract(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNative() {
/* 135 */     return Modifier.isNative(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSynchronized() {
/* 140 */     return Modifier.isSynchronized(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isVolatile() {
/* 145 */     return Modifier.isVolatile(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isTransient() {
/* 150 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 154 */     if (obj instanceof Element) {
/* 155 */       Element that = (Element)obj;
/* 156 */       return (getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member));
/*     */     } 
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 162 */     return this.member.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 166 */     return this.member.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\Element.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */