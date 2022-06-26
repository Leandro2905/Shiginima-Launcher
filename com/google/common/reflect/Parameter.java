/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Parameter
/*     */   implements AnnotatedElement
/*     */ {
/*     */   private final Invokable<?, ?> declaration;
/*     */   private final int position;
/*     */   private final TypeToken<?> type;
/*     */   private final ImmutableList<Annotation> annotations;
/*     */   
/*     */   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations) {
/*  49 */     this.declaration = declaration;
/*  50 */     this.position = position;
/*  51 */     this.type = type;
/*  52 */     this.annotations = ImmutableList.copyOf((Object[])annotations);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeToken<?> getType() {
/*  57 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Invokable<?, ?> getDeclaringInvokable() {
/*  62 */     return this.declaration;
/*     */   }
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/*  66 */     return (getAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*  72 */     Preconditions.checkNotNull(annotationType);
/*  73 */     for (Annotation annotation : this.annotations) {
/*  74 */       if (annotationType.isInstance(annotation)) {
/*  75 */         return annotationType.cast(annotation);
/*     */       }
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public Annotation[] getAnnotations() {
/*  82 */     return getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
/*  90 */     return getDeclaredAnnotationsByType(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/*  98 */     return (Annotation[])this.annotations.toArray((Object[])new Annotation[this.annotations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
/* 107 */     Preconditions.checkNotNull(annotationType);
/* 108 */     return (A)FluentIterable.from((Iterable)this.annotations).filter(annotationType).first().orNull();
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
/*     */   public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
/* 120 */     return (A[])FluentIterable.from((Iterable)this.annotations).filter(annotationType).toArray(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 126 */     if (obj instanceof Parameter) {
/* 127 */       Parameter that = (Parameter)obj;
/* 128 */       return (this.position == that.position && this.declaration.equals(that.declaration));
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 134 */     return this.position;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 138 */     String str = String.valueOf(String.valueOf(this.type)); int i = this.position; return (new StringBuilder(15 + str.length())).append(str).append(" arg").append(i).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\Parameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */