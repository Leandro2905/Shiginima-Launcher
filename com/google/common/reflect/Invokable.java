/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*     */   <M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member> Invokable(M member) {
/*  63 */     super(member);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Invokable<?, Object> from(Method method) {
/*  68 */     return new MethodInvokable(method);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
/*  73 */     return new ConstructorInvokable<T>(constructor);
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
/*     */   public final R invoke(@Nullable T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
/* 102 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeToken<? extends R> getReturnType() {
/* 109 */     return (TypeToken)TypeToken.of(getGenericReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<Parameter> getParameters() {
/* 118 */     Type[] parameterTypes = getGenericParameterTypes();
/* 119 */     Annotation[][] annotations = getParameterAnnotations();
/* 120 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 121 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 122 */       builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i]));
/*     */     }
/*     */     
/* 125 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
/* 130 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 131 */     for (Type type : getGenericExceptionTypes()) {
/*     */ 
/*     */       
/* 134 */       TypeToken<? extends Throwable> exceptionType = (TypeToken)TypeToken.of(type);
/*     */       
/* 136 */       builder.add(exceptionType);
/*     */     } 
/* 138 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
/* 148 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
/* 153 */     if (!returnType.isAssignableFrom(getReturnType())) {
/* 154 */       String str1 = String.valueOf(String.valueOf(getReturnType())), str2 = String.valueOf(String.valueOf(returnType)); throw new IllegalArgumentException((new StringBuilder(35 + str1.length() + str2.length())).append("Invokable is known to return ").append(str1).append(", not ").append(str2).toString());
/*     */     } 
/*     */ 
/*     */     
/* 158 */     Invokable<T, R1> specialized = this;
/* 159 */     return specialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Class<? super T> getDeclaringClass() {
/* 164 */     return (Class)super.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeToken<T> getOwnerType() {
/* 171 */     return TypeToken.of((Class)getDeclaringClass());
/*     */   }
/*     */   
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */   abstract Object invokeInternal(@Nullable Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   static class MethodInvokable<T> extends Invokable<T, Object> { final Method method;
/*     */     
/*     */     MethodInvokable(Method method) {
/* 191 */       super(method);
/* 192 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 197 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */     
/*     */     Type getGenericReturnType() {
/* 201 */       return this.method.getGenericReturnType();
/*     */     }
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 205 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 209 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 213 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 217 */       return (TypeVariable<?>[])this.method.getTypeParameters();
/*     */     }
/*     */     
/*     */     public final boolean isOverridable() {
/* 221 */       return (!isFinal() && !isPrivate() && !isStatic() && !Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 226 */       return this.method.isVarArgs();
/*     */     } }
/*     */ 
/*     */   
/*     */   static class ConstructorInvokable<T>
/*     */     extends Invokable<T, T> {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 235 */       super(constructor);
/* 236 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/*     */       try {
/* 242 */         return this.constructor.newInstance(args);
/* 243 */       } catch (InstantiationException e) {
/* 244 */         String str = String.valueOf(String.valueOf(this.constructor)); throw new RuntimeException((new StringBuilder(8 + str.length())).append(str).append(" failed.").toString(), e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 250 */       Class<?> declaringClass = getDeclaringClass();
/* 251 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])declaringClass.getTypeParameters();
/* 252 */       if (arrayOfTypeVariable.length > 0) {
/* 253 */         return Types.newParameterizedType(declaringClass, (Type[])arrayOfTypeVariable);
/*     */       }
/* 255 */       return declaringClass;
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 260 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 261 */       if (types.length > 0 && mayNeedHiddenThis()) {
/* 262 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 263 */         if (types.length == rawParamTypes.length && rawParamTypes[0] == getDeclaringClass().getEnclosingClass())
/*     */         {
/*     */           
/* 266 */           return Arrays.<Type>copyOfRange(types, 1, types.length);
/*     */         }
/*     */       } 
/* 269 */       return types;
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 273 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 277 */       return this.constructor.getParameterAnnotations();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 290 */       TypeVariable[] arrayOfTypeVariable1 = (TypeVariable[])getDeclaringClass().getTypeParameters();
/* 291 */       TypeVariable[] arrayOfTypeVariable2 = (TypeVariable[])this.constructor.getTypeParameters();
/* 292 */       TypeVariable[] arrayOfTypeVariable3 = new TypeVariable[arrayOfTypeVariable1.length + arrayOfTypeVariable2.length];
/*     */       
/* 294 */       System.arraycopy(arrayOfTypeVariable1, 0, arrayOfTypeVariable3, 0, arrayOfTypeVariable1.length);
/* 295 */       System.arraycopy(arrayOfTypeVariable2, 0, arrayOfTypeVariable3, arrayOfTypeVariable1.length, arrayOfTypeVariable2.length);
/*     */ 
/*     */ 
/*     */       
/* 299 */       return (TypeVariable<?>[])arrayOfTypeVariable3;
/*     */     }
/*     */     
/*     */     public final boolean isOverridable() {
/* 303 */       return false;
/*     */     }
/*     */     
/*     */     public final boolean isVarArgs() {
/* 307 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 311 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 312 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 314 */         return true;
/*     */       }
/* 316 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 317 */       if (enclosingMethod != null)
/*     */       {
/* 319 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       return (declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */