/*      */ package org.apache.commons.lang3.reflect;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.GenericDeclaration;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.commons.lang3.builder.Builder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TypeUtils
/*      */ {
/*      */   public static class WildcardTypeBuilder
/*      */     implements Builder<WildcardType>
/*      */   {
/*      */     private Type[] upperBounds;
/*      */     private Type[] lowerBounds;
/*      */     
/*      */     private WildcardTypeBuilder() {}
/*      */     
/*      */     public WildcardTypeBuilder withUpperBounds(Type... bounds) {
/*   69 */       this.upperBounds = bounds;
/*   70 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardTypeBuilder withLowerBounds(Type... bounds) {
/*   79 */       this.lowerBounds = bounds;
/*   80 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardType build() {
/*   88 */       return new TypeUtils.WildcardTypeImpl(this.upperBounds, this.lowerBounds);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class GenericArrayTypeImpl
/*      */     implements GenericArrayType
/*      */   {
/*      */     private final Type componentType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private GenericArrayTypeImpl(Type componentType) {
/*  104 */       this.componentType = componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getGenericComponentType() {
/*  112 */       return this.componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  120 */       return TypeUtils.toString(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  128 */       return (obj == this || (obj instanceof GenericArrayType && TypeUtils.equals(this, (GenericArrayType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  136 */       int result = 1072;
/*  137 */       result |= this.componentType.hashCode();
/*  138 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ParameterizedTypeImpl
/*      */     implements ParameterizedType
/*      */   {
/*      */     private final Class<?> raw;
/*      */ 
/*      */     
/*      */     private final Type useOwner;
/*      */ 
/*      */     
/*      */     private final Type[] typeArguments;
/*      */ 
/*      */ 
/*      */     
/*      */     private ParameterizedTypeImpl(Class<?> raw, Type useOwner, Type[] typeArguments) {
/*  158 */       this.raw = raw;
/*  159 */       this.useOwner = useOwner;
/*  160 */       this.typeArguments = typeArguments;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getRawType() {
/*  168 */       return this.raw;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getOwnerType() {
/*  176 */       return this.useOwner;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/*  184 */       return (Type[])this.typeArguments.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  192 */       return TypeUtils.toString(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  200 */       return (obj == this || (obj instanceof ParameterizedType && TypeUtils.equals(this, (ParameterizedType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  209 */       int result = 1136;
/*  210 */       result |= this.raw.hashCode();
/*  211 */       result <<= 4;
/*  212 */       result |= ObjectUtils.hashCode(this.useOwner);
/*  213 */       result <<= 8;
/*  214 */       result |= Arrays.hashCode((Object[])this.typeArguments);
/*  215 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class WildcardTypeImpl
/*      */     implements WildcardType
/*      */   {
/*  224 */     private static final Type[] EMPTY_BOUNDS = new Type[0];
/*      */ 
/*      */     
/*      */     private final Type[] upperBounds;
/*      */ 
/*      */     
/*      */     private final Type[] lowerBounds;
/*      */ 
/*      */ 
/*      */     
/*      */     private WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
/*  235 */       this.upperBounds = (Type[])ObjectUtils.defaultIfNull(upperBounds, EMPTY_BOUNDS);
/*  236 */       this.lowerBounds = (Type[])ObjectUtils.defaultIfNull(lowerBounds, EMPTY_BOUNDS);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getUpperBounds() {
/*  244 */       return (Type[])this.upperBounds.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getLowerBounds() {
/*  252 */       return (Type[])this.lowerBounds.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  260 */       return TypeUtils.toString(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  268 */       return (obj == this || (obj instanceof WildcardType && TypeUtils.equals(this, (WildcardType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  276 */       int result = 18688;
/*  277 */       result |= Arrays.hashCode((Object[])this.upperBounds);
/*  278 */       result <<= 8;
/*  279 */       result |= Arrays.hashCode((Object[])this.lowerBounds);
/*  280 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  288 */   public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(new Type[] { Object.class }).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Type type, Type toType) {
/*  312 */     return isAssignable(type, toType, (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static boolean isAssignable(Type type, Type toType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  326 */     if (toType == null || toType instanceof Class) {
/*  327 */       return isAssignable(type, (Class)toType);
/*      */     }
/*      */     
/*  330 */     if (toType instanceof ParameterizedType) {
/*  331 */       return isAssignable(type, (ParameterizedType)toType, typeVarAssigns);
/*      */     }
/*      */     
/*  334 */     if (toType instanceof GenericArrayType) {
/*  335 */       return isAssignable(type, (GenericArrayType)toType, typeVarAssigns);
/*      */     }
/*      */     
/*  338 */     if (toType instanceof WildcardType) {
/*  339 */       return isAssignable(type, (WildcardType)toType, typeVarAssigns);
/*      */     }
/*      */     
/*  342 */     if (toType instanceof TypeVariable) {
/*  343 */       return isAssignable(type, (TypeVariable)toType, typeVarAssigns);
/*      */     }
/*      */     
/*  346 */     throw new IllegalStateException("found an unhandled type: " + toType);
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
/*      */   private static boolean isAssignable(Type type, Class<?> toClass) {
/*  358 */     if (type == null)
/*      */     {
/*  360 */       return (toClass == null || !toClass.isPrimitive());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  365 */     if (toClass == null) {
/*  366 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  370 */     if (toClass.equals(type)) {
/*  371 */       return true;
/*      */     }
/*      */     
/*  374 */     if (type instanceof Class)
/*      */     {
/*  376 */       return ClassUtils.isAssignable((Class)type, toClass);
/*      */     }
/*      */     
/*  379 */     if (type instanceof ParameterizedType)
/*      */     {
/*  381 */       return isAssignable(getRawType((ParameterizedType)type), toClass);
/*      */     }
/*      */ 
/*      */     
/*  385 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */       
/*  388 */       for (Type bound : ((TypeVariable)type).getBounds()) {
/*  389 */         if (isAssignable(bound, toClass)) {
/*  390 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  394 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  399 */     if (type instanceof GenericArrayType) {
/*  400 */       return (toClass.equals(Object.class) || (toClass.isArray() && isAssignable(((GenericArrayType)type).getGenericComponentType(), toClass.getComponentType())));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  408 */     if (type instanceof WildcardType) {
/*  409 */       return false;
/*      */     }
/*      */     
/*  412 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static boolean isAssignable(Type type, ParameterizedType toParameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  426 */     if (type == null) {
/*  427 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  432 */     if (toParameterizedType == null) {
/*  433 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  437 */     if (toParameterizedType.equals(type)) {
/*  438 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  442 */     Class<?> toClass = getRawType(toParameterizedType);
/*      */ 
/*      */     
/*  445 */     Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, (Map<TypeVariable<?>, Type>)null);
/*      */ 
/*      */     
/*  448 */     if (fromTypeVarAssigns == null) {
/*  449 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  455 */     if (fromTypeVarAssigns.isEmpty()) {
/*  456 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  460 */     Map<TypeVariable<?>, Type> toTypeVarAssigns = getTypeArguments(toParameterizedType, toClass, typeVarAssigns);
/*      */ 
/*      */ 
/*      */     
/*  464 */     for (TypeVariable<?> var : toTypeVarAssigns.keySet()) {
/*  465 */       Type toTypeArg = unrollVariableAssignments(var, toTypeVarAssigns);
/*  466 */       Type fromTypeArg = unrollVariableAssignments(var, fromTypeVarAssigns);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  471 */       if (fromTypeArg != null && !toTypeArg.equals(fromTypeArg) && (!(toTypeArg instanceof WildcardType) || !isAssignable(fromTypeArg, toTypeArg, typeVarAssigns)))
/*      */       {
/*      */ 
/*      */         
/*  475 */         return false;
/*      */       }
/*      */     } 
/*  478 */     return true;
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
/*      */   private static Type unrollVariableAssignments(TypeVariable<?> var, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*      */     Type result;
/*      */     while (true) {
/*  492 */       result = typeVarAssigns.get(var);
/*  493 */       if (result instanceof TypeVariable && !result.equals(var)) {
/*  494 */         var = (TypeVariable)result;
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  499 */     return result;
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
/*      */   private static boolean isAssignable(Type type, GenericArrayType toGenericArrayType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  514 */     if (type == null) {
/*  515 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  520 */     if (toGenericArrayType == null) {
/*  521 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  525 */     if (toGenericArrayType.equals(type)) {
/*  526 */       return true;
/*      */     }
/*      */     
/*  529 */     Type toComponentType = toGenericArrayType.getGenericComponentType();
/*      */     
/*  531 */     if (type instanceof Class) {
/*  532 */       Class<?> cls = (Class)type;
/*      */ 
/*      */       
/*  535 */       return (cls.isArray() && isAssignable(cls.getComponentType(), toComponentType, typeVarAssigns));
/*      */     } 
/*      */ 
/*      */     
/*  539 */     if (type instanceof GenericArrayType)
/*      */     {
/*  541 */       return isAssignable(((GenericArrayType)type).getGenericComponentType(), toComponentType, typeVarAssigns);
/*      */     }
/*      */ 
/*      */     
/*  545 */     if (type instanceof WildcardType) {
/*      */       
/*  547 */       for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
/*  548 */         if (isAssignable(bound, toGenericArrayType)) {
/*  549 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  553 */       return false;
/*      */     } 
/*      */     
/*  556 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */       
/*  559 */       for (Type bound : getImplicitBounds((TypeVariable)type)) {
/*  560 */         if (isAssignable(bound, toGenericArrayType)) {
/*  561 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  565 */       return false;
/*      */     } 
/*      */     
/*  568 */     if (type instanceof ParameterizedType)
/*      */     {
/*      */ 
/*      */       
/*  572 */       return false;
/*      */     }
/*      */     
/*  575 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static boolean isAssignable(Type type, WildcardType toWildcardType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  590 */     if (type == null) {
/*  591 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  596 */     if (toWildcardType == null) {
/*  597 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  601 */     if (toWildcardType.equals(type)) {
/*  602 */       return true;
/*      */     }
/*      */     
/*  605 */     Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
/*  606 */     Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);
/*      */     
/*  608 */     if (type instanceof WildcardType) {
/*  609 */       WildcardType wildcardType = (WildcardType)type;
/*  610 */       Type[] upperBounds = getImplicitUpperBounds(wildcardType);
/*  611 */       Type[] lowerBounds = getImplicitLowerBounds(wildcardType);
/*      */       
/*  613 */       for (Type toBound : toUpperBounds) {
/*      */ 
/*      */         
/*  616 */         toBound = substituteTypeVariables(toBound, typeVarAssigns);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  621 */         for (Type bound : upperBounds) {
/*  622 */           if (!isAssignable(bound, toBound, typeVarAssigns)) {
/*  623 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  628 */       for (Type toBound : toLowerBounds) {
/*      */ 
/*      */         
/*  631 */         toBound = substituteTypeVariables(toBound, typeVarAssigns);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  636 */         for (Type bound : lowerBounds) {
/*  637 */           if (!isAssignable(toBound, bound, typeVarAssigns)) {
/*  638 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*  642 */       return true;
/*      */     } 
/*      */     
/*  645 */     for (Type toBound : toUpperBounds) {
/*      */ 
/*      */       
/*  648 */       if (!isAssignable(type, substituteTypeVariables(toBound, typeVarAssigns), typeVarAssigns))
/*      */       {
/*  650 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  654 */     for (Type toBound : toLowerBounds) {
/*      */ 
/*      */       
/*  657 */       if (!isAssignable(substituteTypeVariables(toBound, typeVarAssigns), type, typeVarAssigns))
/*      */       {
/*  659 */         return false;
/*      */       }
/*      */     } 
/*  662 */     return true;
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
/*      */   private static boolean isAssignable(Type type, TypeVariable<?> toTypeVariable, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  677 */     if (type == null) {
/*  678 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  683 */     if (toTypeVariable == null) {
/*  684 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  688 */     if (toTypeVariable.equals(type)) {
/*  689 */       return true;
/*      */     }
/*      */     
/*  692 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */ 
/*      */       
/*  696 */       Type[] bounds = getImplicitBounds((TypeVariable)type);
/*      */       
/*  698 */       for (Type bound : bounds) {
/*  699 */         if (isAssignable(bound, toTypeVariable, typeVarAssigns)) {
/*  700 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  705 */     if (type instanceof Class || type instanceof ParameterizedType || type instanceof GenericArrayType || type instanceof WildcardType)
/*      */     {
/*  707 */       return false;
/*      */     }
/*      */     
/*  710 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*  722 */     if (type instanceof TypeVariable && typeVarAssigns != null) {
/*  723 */       Type replacementType = typeVarAssigns.get(type);
/*      */       
/*  725 */       if (replacementType == null) {
/*  726 */         throw new IllegalArgumentException("missing assignment type for type variable " + type);
/*      */       }
/*      */       
/*  729 */       return replacementType;
/*      */     } 
/*  731 */     return type;
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
/*      */   public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType type) {
/*  748 */     return getTypeArguments(type, getRawType(type), (Map<TypeVariable<?>, Type>)null);
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
/*      */   public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass) {
/*  784 */     return getTypeArguments(type, toClass, (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*  797 */     if (type instanceof Class) {
/*  798 */       return getTypeArguments((Class)type, toClass, subtypeVarAssigns);
/*      */     }
/*      */     
/*  801 */     if (type instanceof ParameterizedType) {
/*  802 */       return getTypeArguments((ParameterizedType)type, toClass, subtypeVarAssigns);
/*      */     }
/*      */     
/*  805 */     if (type instanceof GenericArrayType) {
/*  806 */       return getTypeArguments(((GenericArrayType)type).getGenericComponentType(), toClass.isArray() ? toClass.getComponentType() : toClass, subtypeVarAssigns);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  812 */     if (type instanceof WildcardType) {
/*  813 */       for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
/*      */         
/*  815 */         if (isAssignable(bound, toClass)) {
/*  816 */           return getTypeArguments(bound, toClass, subtypeVarAssigns);
/*      */         }
/*      */       } 
/*      */       
/*  820 */       return null;
/*      */     } 
/*      */     
/*  823 */     if (type instanceof TypeVariable) {
/*  824 */       for (Type bound : getImplicitBounds((TypeVariable)type)) {
/*      */         
/*  826 */         if (isAssignable(bound, toClass)) {
/*  827 */           return getTypeArguments(bound, toClass, subtypeVarAssigns);
/*      */         }
/*      */       } 
/*      */       
/*  831 */       return null;
/*      */     } 
/*  833 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*      */     Map<TypeVariable<?>, Type> typeVarAssigns;
/*  847 */     Class<?> cls = getRawType(parameterizedType);
/*      */ 
/*      */     
/*  850 */     if (!isAssignable(cls, toClass)) {
/*  851 */       return null;
/*      */     }
/*      */     
/*  854 */     Type ownerType = parameterizedType.getOwnerType();
/*      */ 
/*      */     
/*  857 */     if (ownerType instanceof ParameterizedType) {
/*      */       
/*  859 */       ParameterizedType parameterizedOwnerType = (ParameterizedType)ownerType;
/*  860 */       typeVarAssigns = getTypeArguments(parameterizedOwnerType, getRawType(parameterizedOwnerType), subtypeVarAssigns);
/*      */     }
/*      */     else {
/*      */       
/*  864 */       typeVarAssigns = (subtypeVarAssigns == null) ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(subtypeVarAssigns);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  869 */     Type[] typeArgs = parameterizedType.getActualTypeArguments();
/*      */     
/*  871 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */     
/*  874 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  875 */       Type typeArg = typeArgs[i];
/*  876 */       typeVarAssigns.put(arrayOfTypeVariable[i], typeVarAssigns.containsKey(typeArg) ? typeVarAssigns.get(typeArg) : typeArg);
/*      */     } 
/*      */ 
/*      */     
/*  880 */     if (toClass.equals(cls))
/*      */     {
/*  882 */       return typeVarAssigns;
/*      */     }
/*      */ 
/*      */     
/*  886 */     return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*  900 */     if (!isAssignable(cls, toClass)) {
/*  901 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  905 */     if (cls.isPrimitive()) {
/*      */       
/*  907 */       if (toClass.isPrimitive())
/*      */       {
/*      */         
/*  910 */         return new HashMap<TypeVariable<?>, Type>();
/*      */       }
/*      */ 
/*      */       
/*  914 */       cls = ClassUtils.primitiveToWrapper(cls);
/*      */     } 
/*      */ 
/*      */     
/*  918 */     HashMap<TypeVariable<?>, Type> typeVarAssigns = (subtypeVarAssigns == null) ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(subtypeVarAssigns);
/*      */ 
/*      */ 
/*      */     
/*  922 */     if (toClass.equals(cls)) {
/*  923 */       return typeVarAssigns;
/*      */     }
/*      */ 
/*      */     
/*  927 */     return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
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
/*      */   public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> cls, ParameterizedType superType) {
/*  959 */     Validate.notNull(cls, "cls is null", new Object[0]);
/*  960 */     Validate.notNull(superType, "superType is null", new Object[0]);
/*      */     
/*  962 */     Class<?> superClass = getRawType(superType);
/*      */ 
/*      */     
/*  965 */     if (!isAssignable(cls, superClass)) {
/*  966 */       return null;
/*      */     }
/*      */     
/*  969 */     if (cls.equals(superClass)) {
/*  970 */       return getTypeArguments(superType, superClass, (Map<TypeVariable<?>, Type>)null);
/*      */     }
/*      */ 
/*      */     
/*  974 */     Type midType = getClosestParentType(cls, superClass);
/*      */ 
/*      */     
/*  977 */     if (midType instanceof Class) {
/*  978 */       return determineTypeArguments((Class)midType, superType);
/*      */     }
/*      */     
/*  981 */     ParameterizedType midParameterizedType = (ParameterizedType)midType;
/*  982 */     Class<?> midClass = getRawType(midParameterizedType);
/*      */ 
/*      */     
/*  985 */     Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(midClass, superType);
/*      */     
/*  987 */     mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);
/*      */     
/*  989 */     return typeVarAssigns;
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
/*      */   private static <T> void mapTypeVariablesToArguments(Class<T> cls, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1003 */     Type ownerType = parameterizedType.getOwnerType();
/*      */     
/* 1005 */     if (ownerType instanceof ParameterizedType)
/*      */     {
/* 1007 */       mapTypeVariablesToArguments(cls, (ParameterizedType)ownerType, typeVarAssigns);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1014 */     Type[] typeArgs = parameterizedType.getActualTypeArguments();
/*      */ 
/*      */ 
/*      */     
/* 1018 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])getRawType(parameterizedType).getTypeParameters();
/*      */ 
/*      */     
/* 1021 */     List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());
/*      */ 
/*      */     
/* 1024 */     for (int i = 0; i < typeArgs.length; i++) {
/* 1025 */       TypeVariable<?> typeVar = arrayOfTypeVariable[i];
/* 1026 */       Type typeArg = typeArgs[i];
/*      */ 
/*      */       
/* 1029 */       if (typeVarList.contains(typeArg) && typeVarAssigns.containsKey(typeVar))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1034 */         typeVarAssigns.put((TypeVariable)typeArg, typeVarAssigns.get(typeVar));
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
/*      */   private static Type getClosestParentType(Class<?> cls, Class<?> superClass) {
/* 1049 */     if (superClass.isInterface()) {
/*      */       
/* 1051 */       Type[] interfaceTypes = cls.getGenericInterfaces();
/*      */       
/* 1053 */       Type genericInterface = null;
/*      */ 
/*      */       
/* 1056 */       for (Type midType : interfaceTypes) {
/* 1057 */         Class<?> midClass = null;
/*      */         
/* 1059 */         if (midType instanceof ParameterizedType) {
/* 1060 */           midClass = getRawType((ParameterizedType)midType);
/* 1061 */         } else if (midType instanceof Class) {
/* 1062 */           midClass = (Class)midType;
/*      */         } else {
/* 1064 */           throw new IllegalStateException("Unexpected generic interface type found: " + midType);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1070 */         if (isAssignable(midClass, superClass) && isAssignable(genericInterface, midClass))
/*      */         {
/* 1072 */           genericInterface = midType;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1077 */       if (genericInterface != null) {
/* 1078 */         return genericInterface;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1084 */     return cls.getGenericSuperclass();
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
/*      */   public static boolean isInstance(Object value, Type type) {
/* 1096 */     if (type == null) {
/* 1097 */       return false;
/*      */     }
/*      */     
/* 1100 */     return (value == null) ? ((!(type instanceof Class) || !((Class)type).isPrimitive())) : isAssignable(value.getClass(), type, (Map<TypeVariable<?>, Type>)null);
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
/*      */   public static Type[] normalizeUpperBounds(Type[] bounds) {
/* 1126 */     Validate.notNull(bounds, "null value specified for bounds array", new Object[0]);
/*      */     
/* 1128 */     if (bounds.length < 2) {
/* 1129 */       return bounds;
/*      */     }
/*      */     
/* 1132 */     Set<Type> types = new HashSet<Type>(bounds.length);
/*      */     
/* 1134 */     for (Type type1 : bounds) {
/* 1135 */       boolean subtypeFound = false;
/*      */       
/* 1137 */       for (Type type2 : bounds) {
/* 1138 */         if (type1 != type2 && isAssignable(type2, type1, (Map<TypeVariable<?>, Type>)null)) {
/* 1139 */           subtypeFound = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1144 */       if (!subtypeFound) {
/* 1145 */         types.add(type1);
/*      */       }
/*      */     } 
/*      */     
/* 1149 */     return types.<Type>toArray(new Type[types.size()]);
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
/*      */   public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
/* 1162 */     Validate.notNull(typeVariable, "typeVariable is null", new Object[0]);
/* 1163 */     Type[] bounds = typeVariable.getBounds();
/*      */     
/* 1165 */     (new Type[1])[0] = Object.class; return (bounds.length == 0) ? new Type[1] : normalizeUpperBounds(bounds);
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
/*      */   public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
/* 1179 */     Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
/* 1180 */     Type[] bounds = wildcardType.getUpperBounds();
/*      */     
/* 1182 */     (new Type[1])[0] = Object.class; return (bounds.length == 0) ? new Type[1] : normalizeUpperBounds(bounds);
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
/*      */   public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
/* 1195 */     Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
/* 1196 */     Type[] bounds = wildcardType.getLowerBounds();
/*      */     
/* 1198 */     (new Type[1])[0] = null; return (bounds.length == 0) ? new Type[1] : bounds;
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
/*      */   public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1215 */     Validate.notNull(typeVarAssigns, "typeVarAssigns is null", new Object[0]);
/*      */ 
/*      */     
/* 1218 */     for (Map.Entry<TypeVariable<?>, Type> entry : typeVarAssigns.entrySet()) {
/* 1219 */       TypeVariable<?> typeVar = entry.getKey();
/* 1220 */       Type type = entry.getValue();
/*      */       
/* 1222 */       for (Type bound : getImplicitBounds(typeVar)) {
/* 1223 */         if (!isAssignable(type, substituteTypeVariables(bound, typeVarAssigns), typeVarAssigns))
/*      */         {
/* 1225 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/* 1229 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Class<?> getRawType(ParameterizedType parameterizedType) {
/* 1240 */     Type rawType = parameterizedType.getRawType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1247 */     if (!(rawType instanceof Class)) {
/* 1248 */       throw new IllegalStateException("Wait... What!? Type of rawType: " + rawType);
/*      */     }
/*      */     
/* 1251 */     return (Class)rawType;
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
/*      */   public static Class<?> getRawType(Type type, Type assigningType) {
/* 1267 */     if (type instanceof Class)
/*      */     {
/* 1269 */       return (Class)type;
/*      */     }
/*      */     
/* 1272 */     if (type instanceof ParameterizedType)
/*      */     {
/* 1274 */       return getRawType((ParameterizedType)type);
/*      */     }
/*      */     
/* 1277 */     if (type instanceof TypeVariable) {
/* 1278 */       if (assigningType == null) {
/* 1279 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1283 */       Object genericDeclaration = ((TypeVariable)type).getGenericDeclaration();
/*      */ 
/*      */ 
/*      */       
/* 1287 */       if (!(genericDeclaration instanceof Class)) {
/* 1288 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1293 */       Map<TypeVariable<?>, Type> typeVarAssigns = getTypeArguments(assigningType, (Class)genericDeclaration);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1298 */       if (typeVarAssigns == null) {
/* 1299 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1303 */       Type typeArgument = typeVarAssigns.get(type);
/*      */       
/* 1305 */       if (typeArgument == null) {
/* 1306 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1310 */       return getRawType(typeArgument, assigningType);
/*      */     } 
/*      */     
/* 1313 */     if (type instanceof GenericArrayType) {
/*      */       
/* 1315 */       Class<?> rawComponentType = getRawType(((GenericArrayType)type).getGenericComponentType(), assigningType);
/*      */ 
/*      */ 
/*      */       
/* 1319 */       return Array.newInstance(rawComponentType, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/* 1323 */     if (type instanceof WildcardType) {
/* 1324 */       return null;
/*      */     }
/*      */     
/* 1327 */     throw new IllegalArgumentException("unknown type: " + type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isArrayType(Type type) {
/* 1336 */     return (type instanceof GenericArrayType || (type instanceof Class && ((Class)type).isArray()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type getArrayComponentType(Type type) {
/* 1345 */     if (type instanceof Class) {
/* 1346 */       Class<?> clazz = (Class)type;
/* 1347 */       return clazz.isArray() ? clazz.getComponentType() : null;
/*      */     } 
/* 1349 */     if (type instanceof GenericArrayType) {
/* 1350 */       return ((GenericArrayType)type).getGenericComponentType();
/*      */     }
/* 1352 */     return null;
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
/*      */   public static Type unrollVariables(Map<TypeVariable<?>, Type> typeArguments, Type type) {
/* 1364 */     if (typeArguments == null) {
/* 1365 */       typeArguments = Collections.emptyMap();
/*      */     }
/* 1367 */     if (containsTypeVariables(type)) {
/* 1368 */       if (type instanceof TypeVariable) {
/* 1369 */         return unrollVariables(typeArguments, typeArguments.get(type));
/*      */       }
/* 1371 */       if (type instanceof ParameterizedType) {
/* 1372 */         Map<TypeVariable<?>, Type> parameterizedTypeArguments; ParameterizedType p = (ParameterizedType)type;
/*      */         
/* 1374 */         if (p.getOwnerType() == null) {
/* 1375 */           parameterizedTypeArguments = typeArguments;
/*      */         } else {
/* 1377 */           parameterizedTypeArguments = new HashMap<TypeVariable<?>, Type>(typeArguments);
/* 1378 */           parameterizedTypeArguments.putAll(getTypeArguments(p));
/*      */         } 
/* 1380 */         Type[] args = p.getActualTypeArguments();
/* 1381 */         for (int i = 0; i < args.length; i++) {
/* 1382 */           Type unrolled = unrollVariables(parameterizedTypeArguments, args[i]);
/* 1383 */           if (unrolled != null) {
/* 1384 */             args[i] = unrolled;
/*      */           }
/*      */         } 
/* 1387 */         return parameterizeWithOwner(p.getOwnerType(), (Class)p.getRawType(), args);
/*      */       } 
/* 1389 */       if (type instanceof WildcardType) {
/* 1390 */         WildcardType wild = (WildcardType)type;
/* 1391 */         return wildcardType().withUpperBounds(unrollBounds(typeArguments, wild.getUpperBounds())).withLowerBounds(unrollBounds(typeArguments, wild.getLowerBounds())).build();
/*      */       } 
/*      */     } 
/*      */     
/* 1395 */     return type;
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
/*      */   private static Type[] unrollBounds(Map<TypeVariable<?>, Type> typeArguments, Type[] bounds) {
/* 1407 */     Type[] result = bounds;
/* 1408 */     int i = 0;
/* 1409 */     for (; i < result.length; i++) {
/* 1410 */       Type unrolled = unrollVariables(typeArguments, result[i]);
/* 1411 */       if (unrolled == null) {
/* 1412 */         result = (Type[])ArrayUtils.remove((Object[])result, i--);
/*      */       } else {
/* 1414 */         result[i] = unrolled;
/*      */       } 
/*      */     } 
/* 1417 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsTypeVariables(Type type) {
/* 1428 */     if (type instanceof TypeVariable) {
/* 1429 */       return true;
/*      */     }
/* 1431 */     if (type instanceof Class) {
/* 1432 */       return ((((Class)type).getTypeParameters()).length > 0);
/*      */     }
/* 1434 */     if (type instanceof ParameterizedType) {
/* 1435 */       for (Type arg : ((ParameterizedType)type).getActualTypeArguments()) {
/* 1436 */         if (containsTypeVariables(arg)) {
/* 1437 */           return true;
/*      */         }
/*      */       } 
/* 1440 */       return false;
/*      */     } 
/* 1442 */     if (type instanceof WildcardType) {
/* 1443 */       WildcardType wild = (WildcardType)type;
/* 1444 */       return (containsTypeVariables(getImplicitLowerBounds(wild)[0]) || containsTypeVariables(getImplicitUpperBounds(wild)[0]));
/*      */     } 
/*      */     
/* 1447 */     return false;
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
/*      */   public static final ParameterizedType parameterize(Class<?> raw, Type... typeArguments) {
/* 1459 */     return parameterizeWithOwner((Type)null, raw, typeArguments);
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
/*      */   public static final ParameterizedType parameterize(Class<?> raw, Map<TypeVariable<?>, Type> typeArgMappings) {
/* 1472 */     Validate.notNull(raw, "raw class is null", new Object[0]);
/* 1473 */     Validate.notNull(typeArgMappings, "typeArgMappings is null", new Object[0]);
/* 1474 */     return parameterizeWithOwner((Type)null, raw, extractTypeArgumentsFrom(typeArgMappings, (TypeVariable<?>[])raw.getTypeParameters()));
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
/*      */   public static final ParameterizedType parameterizeWithOwner(Type owner, Class<?> raw, Type... typeArguments) {
/*      */     Type useOwner;
/* 1489 */     Validate.notNull(raw, "raw class is null", new Object[0]);
/*      */     
/* 1491 */     if (raw.getEnclosingClass() == null) {
/* 1492 */       Validate.isTrue((owner == null), "no owner allowed for top-level %s", new Object[] { raw });
/* 1493 */       useOwner = null;
/* 1494 */     } else if (owner == null) {
/* 1495 */       useOwner = raw.getEnclosingClass();
/*      */     } else {
/* 1497 */       Validate.isTrue(isAssignable(owner, raw.getEnclosingClass()), "%s is invalid owner type for parameterized %s", new Object[] { owner, raw });
/*      */       
/* 1499 */       useOwner = owner;
/*      */     } 
/* 1501 */     Validate.noNullElements((Object[])typeArguments, "null type argument at index %s", new Object[0]);
/* 1502 */     Validate.isTrue(((raw.getTypeParameters()).length == typeArguments.length), "invalid number of type parameters specified: expected %s, got %s", new Object[] { Integer.valueOf((raw.getTypeParameters()).length), Integer.valueOf(typeArguments.length) });
/*      */ 
/*      */ 
/*      */     
/* 1506 */     return new ParameterizedTypeImpl(raw, useOwner, typeArguments);
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
/*      */   public static final ParameterizedType parameterizeWithOwner(Type owner, Class<?> raw, Map<TypeVariable<?>, Type> typeArgMappings) {
/* 1520 */     Validate.notNull(raw, "raw class is null", new Object[0]);
/* 1521 */     Validate.notNull(typeArgMappings, "typeArgMappings is null", new Object[0]);
/* 1522 */     return parameterizeWithOwner(owner, raw, extractTypeArgumentsFrom(typeArgMappings, (TypeVariable<?>[])raw.getTypeParameters()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type[] extractTypeArgumentsFrom(Map<TypeVariable<?>, Type> mappings, TypeVariable<?>[] variables) {
/* 1532 */     Type[] result = new Type[variables.length];
/* 1533 */     int index = 0;
/* 1534 */     for (TypeVariable<?> var : variables) {
/* 1535 */       Validate.isTrue(mappings.containsKey(var), "missing argument mapping for %s", new Object[] { toString(var) });
/* 1536 */       result[index++] = mappings.get(var);
/*      */     } 
/* 1538 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static WildcardTypeBuilder wildcardType() {
/* 1547 */     return new WildcardTypeBuilder();
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
/*      */   public static GenericArrayType genericArrayType(Type componentType) {
/* 1559 */     return new GenericArrayTypeImpl((Type)Validate.notNull(componentType, "componentType is null", new Object[0]));
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
/*      */   public static boolean equals(Type t1, Type t2) {
/* 1572 */     if (ObjectUtils.equals(t1, t2)) {
/* 1573 */       return true;
/*      */     }
/* 1575 */     if (t1 instanceof ParameterizedType) {
/* 1576 */       return equals((ParameterizedType)t1, t2);
/*      */     }
/* 1578 */     if (t1 instanceof GenericArrayType) {
/* 1579 */       return equals((GenericArrayType)t1, t2);
/*      */     }
/* 1581 */     if (t1 instanceof WildcardType) {
/* 1582 */       return equals((WildcardType)t1, t2);
/*      */     }
/* 1584 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean equals(ParameterizedType p, Type t) {
/* 1595 */     if (t instanceof ParameterizedType) {
/* 1596 */       ParameterizedType other = (ParameterizedType)t;
/* 1597 */       if (equals(p.getRawType(), other.getRawType()) && equals(p.getOwnerType(), other.getOwnerType())) {
/* 1598 */         return equals(p.getActualTypeArguments(), other.getActualTypeArguments());
/*      */       }
/*      */     } 
/* 1601 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean equals(GenericArrayType a, Type t) {
/* 1612 */     return (t instanceof GenericArrayType && equals(a.getGenericComponentType(), ((GenericArrayType)t).getGenericComponentType()));
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
/*      */   private static boolean equals(WildcardType w, Type t) {
/* 1624 */     if (t instanceof WildcardType) {
/* 1625 */       WildcardType other = (WildcardType)t;
/* 1626 */       return (equals(getImplicitLowerBounds(w), getImplicitLowerBounds(other)) && equals(getImplicitUpperBounds(w), getImplicitUpperBounds(other)));
/*      */     } 
/*      */     
/* 1629 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean equals(Type[] t1, Type[] t2) {
/* 1640 */     if (t1.length == t2.length) {
/* 1641 */       for (int i = 0; i < t1.length; i++) {
/* 1642 */         if (!equals(t1[i], t2[i])) {
/* 1643 */           return false;
/*      */         }
/*      */       } 
/* 1646 */       return true;
/*      */     } 
/* 1648 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Type type) {
/* 1659 */     Validate.notNull(type);
/* 1660 */     if (type instanceof Class) {
/* 1661 */       return classToString((Class)type);
/*      */     }
/* 1663 */     if (type instanceof ParameterizedType) {
/* 1664 */       return parameterizedTypeToString((ParameterizedType)type);
/*      */     }
/* 1666 */     if (type instanceof WildcardType) {
/* 1667 */       return wildcardTypeToString((WildcardType)type);
/*      */     }
/* 1669 */     if (type instanceof TypeVariable) {
/* 1670 */       return typeVariableToString((TypeVariable)type);
/*      */     }
/* 1672 */     if (type instanceof GenericArrayType) {
/* 1673 */       return genericArrayTypeToString((GenericArrayType)type);
/*      */     }
/* 1675 */     throw new IllegalArgumentException(ObjectUtils.identityToString(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toLongString(TypeVariable<?> var) {
/* 1686 */     Validate.notNull(var, "var is null", new Object[0]);
/* 1687 */     StringBuilder buf = new StringBuilder();
/* 1688 */     GenericDeclaration d = (GenericDeclaration)var.getGenericDeclaration();
/* 1689 */     if (d instanceof Class) {
/* 1690 */       Class<?> c = (Class)d;
/*      */       while (true) {
/* 1692 */         if (c.getEnclosingClass() == null) {
/* 1693 */           buf.insert(0, c.getName());
/*      */           break;
/*      */         } 
/* 1696 */         buf.insert(0, c.getSimpleName()).insert(0, '.');
/* 1697 */         c = c.getEnclosingClass();
/*      */       } 
/* 1699 */     } else if (d instanceof Type) {
/* 1700 */       buf.append(toString((Type)d));
/*      */     } else {
/* 1702 */       buf.append(d);
/*      */     } 
/* 1704 */     return buf.append(':').append(typeVariableToString(var)).toString();
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
/*      */   public static <T> Typed<T> wrap(final Type type) {
/* 1716 */     return new Typed<T>()
/*      */       {
/*      */         public Type getType() {
/* 1719 */           return type;
/*      */         }
/*      */       };
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
/*      */   public static <T> Typed<T> wrap(Class<T> type) {
/* 1733 */     return wrap(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String classToString(Class<?> c) {
/* 1743 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1745 */     if (c.getEnclosingClass() != null) {
/* 1746 */       buf.append(classToString(c.getEnclosingClass())).append('.').append(c.getSimpleName());
/*      */     } else {
/* 1748 */       buf.append(c.getName());
/*      */     } 
/* 1750 */     if ((c.getTypeParameters()).length > 0) {
/* 1751 */       buf.append('<');
/* 1752 */       appendAllTo(buf, ", ", (Type[])c.getTypeParameters());
/* 1753 */       buf.append('>');
/*      */     } 
/* 1755 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String typeVariableToString(TypeVariable<?> v) {
/* 1765 */     StringBuilder buf = new StringBuilder(v.getName());
/* 1766 */     Type[] bounds = v.getBounds();
/* 1767 */     if (bounds.length > 0 && (bounds.length != 1 || !Object.class.equals(bounds[0]))) {
/* 1768 */       buf.append(" extends ");
/* 1769 */       appendAllTo(buf, " & ", v.getBounds());
/*      */     } 
/* 1771 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String parameterizedTypeToString(ParameterizedType p) {
/* 1781 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1783 */     Type useOwner = p.getOwnerType();
/* 1784 */     Class<?> raw = (Class)p.getRawType();
/* 1785 */     Type[] typeArguments = p.getActualTypeArguments();
/* 1786 */     if (useOwner == null) {
/* 1787 */       buf.append(raw.getName());
/*      */     } else {
/* 1789 */       if (useOwner instanceof Class) {
/* 1790 */         buf.append(((Class)useOwner).getName());
/*      */       } else {
/* 1792 */         buf.append(useOwner.toString());
/*      */       } 
/* 1794 */       buf.append('.').append(raw.getSimpleName());
/*      */     } 
/*      */     
/* 1797 */     appendAllTo(buf.append('<'), ", ", typeArguments).append('>');
/* 1798 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String wildcardTypeToString(WildcardType w) {
/* 1808 */     StringBuilder buf = (new StringBuilder()).append('?');
/* 1809 */     Type[] lowerBounds = w.getLowerBounds();
/* 1810 */     Type[] upperBounds = w.getUpperBounds();
/* 1811 */     if (lowerBounds.length > 1 || (lowerBounds.length == 1 && lowerBounds[0] != null)) {
/* 1812 */       appendAllTo(buf.append(" super "), " & ", lowerBounds);
/* 1813 */     } else if (upperBounds.length > 1 || (upperBounds.length == 1 && !Object.class.equals(upperBounds[0]))) {
/* 1814 */       appendAllTo(buf.append(" extends "), " & ", upperBounds);
/*      */     } 
/* 1816 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String genericArrayTypeToString(GenericArrayType g) {
/* 1826 */     return String.format("%s[]", new Object[] { toString(g.getGenericComponentType()) });
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
/*      */   private static StringBuilder appendAllTo(StringBuilder buf, String sep, Type... types) {
/* 1838 */     Validate.notEmpty(Validate.noNullElements((Object[])types));
/* 1839 */     if (types.length > 0) {
/* 1840 */       buf.append(toString(types[0]));
/* 1841 */       for (int i = 1; i < types.length; i++) {
/* 1842 */         buf.append(sep).append(toString(types[i]));
/*      */       }
/*      */     } 
/* 1845 */     return buf;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\reflect\TypeUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */