/*      */ package com.google.common.reflect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ForwardingSet;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.primitives.Primitives;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public abstract class TypeToken<T>
/*      */   extends TypeCapture<T>
/*      */   implements Serializable
/*      */ {
/*      */   private final Type runtimeType;
/*      */   private transient TypeResolver typeResolver;
/*      */   
/*      */   protected TypeToken() {
/*  113 */     this.runtimeType = capture();
/*  114 */     Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", new Object[] { this.runtimeType });
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
/*      */   protected TypeToken(Class<?> declaringClass) {
/*  140 */     Type captured = capture();
/*  141 */     if (captured instanceof Class) {
/*  142 */       this.runtimeType = captured;
/*      */     } else {
/*  144 */       this.runtimeType = (of((Class)declaringClass).resolveType(captured)).runtimeType;
/*      */     } 
/*      */   }
/*      */   
/*      */   private TypeToken(Type type) {
/*  149 */     this.runtimeType = (Type)Preconditions.checkNotNull(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> TypeToken<T> of(Class<T> type) {
/*  154 */     return new SimpleTypeToken<T>(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static TypeToken<?> of(Type type) {
/*  159 */     return new SimpleTypeToken(type);
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
/*      */   public final Class<? super T> getRawType() {
/*  177 */     Class<?> rawType = getRawType(this.runtimeType);
/*      */     
/*  179 */     Class<? super T> result = (Class)rawType;
/*  180 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
/*  190 */     ImmutableSet<Class<? super T>> result = (ImmutableSet)getRawTypes(this.runtimeType);
/*  191 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  196 */     return this.runtimeType;
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
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
/*  215 */     TypeResolver resolver = (new TypeResolver()).where((Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  220 */     return new SimpleTypeToken<T>(resolver.resolveType(this.runtimeType));
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
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
/*  239 */     return where(typeParam, of(typeArg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> resolveType(Type type) {
/*  250 */     Preconditions.checkNotNull(type);
/*  251 */     TypeResolver resolver = this.typeResolver;
/*  252 */     if (resolver == null) {
/*  253 */       resolver = this.typeResolver = TypeResolver.accordingTo(this.runtimeType);
/*      */     }
/*  255 */     return of(resolver.resolveType(type));
/*      */   }
/*      */   
/*      */   private Type[] resolveInPlace(Type[] types) {
/*  259 */     for (int i = 0; i < types.length; i++) {
/*  260 */       types[i] = resolveType(types[i]).getType();
/*      */     }
/*  262 */     return types;
/*      */   }
/*      */   
/*      */   private TypeToken<?> resolveSupertype(Type type) {
/*  266 */     TypeToken<?> supertype = resolveType(type);
/*      */     
/*  268 */     supertype.typeResolver = this.typeResolver;
/*  269 */     return supertype;
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
/*      */   @Nullable
/*      */   final TypeToken<? super T> getGenericSuperclass() {
/*  287 */     if (this.runtimeType instanceof TypeVariable)
/*      */     {
/*  289 */       return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
/*      */     }
/*  291 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  293 */       return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
/*      */     }
/*  295 */     Type superclass = getRawType().getGenericSuperclass();
/*  296 */     if (superclass == null) {
/*  297 */       return null;
/*      */     }
/*      */     
/*  300 */     TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
/*  301 */     return superToken;
/*      */   }
/*      */   @Nullable
/*      */   private TypeToken<? super T> boundAsSuperclass(Type bound) {
/*  305 */     TypeToken<?> token = of(bound);
/*  306 */     if (token.getRawType().isInterface()) {
/*  307 */       return null;
/*      */     }
/*      */     
/*  310 */     TypeToken<? super T> superclass = (TypeToken)token;
/*  311 */     return superclass;
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
/*      */   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
/*  327 */     if (this.runtimeType instanceof TypeVariable) {
/*  328 */       return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  330 */     if (this.runtimeType instanceof WildcardType) {
/*  331 */       return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  333 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  334 */     for (Type interfaceType : getRawType().getGenericInterfaces()) {
/*      */       
/*  336 */       TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
/*      */       
/*  338 */       builder.add(resolvedInterface);
/*      */     } 
/*  340 */     return builder.build();
/*      */   }
/*      */   
/*      */   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
/*  344 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  345 */     for (Type bound : bounds) {
/*      */       
/*  347 */       TypeToken<? super T> boundType = (TypeToken)of(bound);
/*  348 */       if (boundType.getRawType().isInterface()) {
/*  349 */         builder.add(boundType);
/*      */       }
/*      */     } 
/*  352 */     return builder.build();
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
/*      */   public final TypeSet getTypes() {
/*  367 */     return new TypeSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
/*  376 */     Preconditions.checkArgument(superclass.isAssignableFrom(getRawType()), "%s is not a super class of %s", new Object[] { superclass, this });
/*      */     
/*  378 */     if (this.runtimeType instanceof TypeVariable) {
/*  379 */       return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  381 */     if (this.runtimeType instanceof WildcardType) {
/*  382 */       return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  384 */     if (superclass.isArray()) {
/*  385 */       return getArraySupertype(superclass);
/*      */     }
/*      */     
/*  388 */     TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
/*      */     
/*  390 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
/*  399 */     Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", new Object[] { this });
/*      */     
/*  401 */     if (this.runtimeType instanceof WildcardType) {
/*  402 */       return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
/*      */     }
/*  404 */     Preconditions.checkArgument(getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", new Object[] { subclass, this });
/*      */ 
/*      */     
/*  407 */     if (isArray()) {
/*  408 */       return getArraySubtype(subclass);
/*      */     }
/*      */     
/*  411 */     TypeToken<? extends T> subtype = (TypeToken)of(resolveTypeArgsForSubclass(subclass));
/*      */     
/*  413 */     return subtype;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAssignableFrom(TypeToken<?> type) {
/*  418 */     return isAssignableFrom(type.runtimeType);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAssignableFrom(Type type) {
/*  423 */     return isAssignable((Type)Preconditions.checkNotNull(type), this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isArray() {
/*  431 */     return (getComponentType() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrimitive() {
/*  440 */     return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> wrap() {
/*  450 */     if (isPrimitive()) {
/*      */       
/*  452 */       Class<T> type = (Class<T>)this.runtimeType;
/*  453 */       return of(Primitives.wrap(type));
/*      */     } 
/*  455 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isWrapper() {
/*  459 */     return Primitives.allWrapperTypes().contains(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> unwrap() {
/*  469 */     if (isWrapper()) {
/*      */       
/*  471 */       Class<T> type = (Class<T>)this.runtimeType;
/*  472 */       return of(Primitives.unwrap(type));
/*      */     } 
/*  474 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final TypeToken<?> getComponentType() {
/*  482 */     Type componentType = Types.getComponentType(this.runtimeType);
/*  483 */     if (componentType == null) {
/*  484 */       return null;
/*      */     }
/*  486 */     return of(componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, Object> method(Method method) {
/*  495 */     Preconditions.checkArgument(of(method.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", new Object[] { method, this });
/*      */     
/*  497 */     return new Invokable.MethodInvokable<T>(method) {
/*      */         Type getGenericReturnType() {
/*  499 */           return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
/*      */         }
/*      */         Type[] getGenericParameterTypes() {
/*  502 */           return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */         Type[] getGenericExceptionTypes() {
/*  505 */           return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */         public TypeToken<T> getOwnerType() {
/*  508 */           return TypeToken.this;
/*      */         }
/*      */         public String toString() {
/*  511 */           String str1 = String.valueOf(String.valueOf(getOwnerType())), str2 = String.valueOf(String.valueOf(super.toString())); return (new StringBuilder(1 + str1.length() + str2.length())).append(str1).append(".").append(str2).toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, T> constructor(Constructor<?> constructor) {
/*  522 */     Preconditions.checkArgument((constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", new Object[] { constructor, getRawType() });
/*      */     
/*  524 */     return new Invokable.ConstructorInvokable<T>(constructor) {
/*      */         Type getGenericReturnType() {
/*  526 */           return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
/*      */         }
/*      */         Type[] getGenericParameterTypes() {
/*  529 */           return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */         Type[] getGenericExceptionTypes() {
/*  532 */           return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */         public TypeToken<T> getOwnerType() {
/*  535 */           return TypeToken.this;
/*      */         }
/*      */         public String toString() {
/*  538 */           String str1 = String.valueOf(String.valueOf(getOwnerType())), str2 = String.valueOf(String.valueOf(Joiner.on(", ").join((Object[])getGenericParameterTypes()))); return (new StringBuilder(2 + str1.length() + str2.length())).append(str1).append("(").append(str2).append(")").toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class TypeSet
/*      */     extends ForwardingSet<TypeToken<? super T>>
/*      */     implements Serializable
/*      */   {
/*      */     private transient ImmutableSet<TypeToken<? super T>> types;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     public TypeSet interfaces() {
/*  555 */       return new TypeToken.InterfaceSet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeSet classes() {
/*  560 */       return new TypeToken.ClassSet();
/*      */     }
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  564 */       ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
/*  565 */       if (filteredTypes == null) {
/*      */ 
/*      */         
/*  568 */         ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
/*      */         
/*  570 */         return (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*      */ 
/*      */       
/*  574 */       return (Set<TypeToken<? super T>>)filteredTypes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  582 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getImmediateRawTypes());
/*      */       
/*  584 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class InterfaceSet
/*      */     extends TypeSet
/*      */   {
/*      */     private final transient TypeToken<T>.TypeSet allTypes;
/*      */     private transient ImmutableSet<TypeToken<? super T>> interfaces;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     InterfaceSet(TypeToken<T>.TypeSet allTypes) {
/*  596 */       this.allTypes = allTypes;
/*      */     }
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  600 */       ImmutableSet<TypeToken<? super T>> result = this.interfaces;
/*  601 */       if (result == null) {
/*  602 */         return (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet());
/*      */       }
/*      */ 
/*      */       
/*  606 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  611 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  617 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getImmediateRawTypes());
/*      */       
/*  619 */       return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes).filter(new Predicate<Class<?>>()
/*      */           {
/*      */             public boolean apply(Class<?> type) {
/*  622 */               return type.isInterface();
/*      */             }
/*      */           }).toSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  629 */       throw new UnsupportedOperationException("interfaces().classes() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  633 */       return TypeToken.this.getTypes().interfaces();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ClassSet extends TypeSet {
/*      */     private transient ImmutableSet<TypeToken<? super T>> classes;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private ClassSet() {}
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  644 */       ImmutableSet<TypeToken<? super T>> result = this.classes;
/*  645 */       if (result == null) {
/*      */         
/*  647 */         ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
/*      */         
/*  649 */         return (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*      */ 
/*      */       
/*  653 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  658 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  664 */       ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getImmediateRawTypes());
/*      */       
/*  666 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  670 */       throw new UnsupportedOperationException("classes().interfaces() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  674 */       return TypeToken.this.getTypes().classes();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private enum TypeFilter
/*      */     implements Predicate<TypeToken<?>>
/*      */   {
/*  682 */     IGNORE_TYPE_VARIABLE_OR_WILDCARD {
/*      */       public boolean apply(TypeToken<?> type) {
/*  684 */         return (!(type.runtimeType instanceof TypeVariable) && !(type.runtimeType instanceof WildcardType));
/*      */       }
/*      */     },
/*      */     
/*  688 */     INTERFACE_ONLY {
/*      */       public boolean apply(TypeToken<?> type) {
/*  690 */         return type.getRawType().isInterface();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@Nullable Object o) {
/*  699 */     if (o instanceof TypeToken) {
/*  700 */       TypeToken<?> that = (TypeToken)o;
/*  701 */       return this.runtimeType.equals(that.runtimeType);
/*      */     } 
/*  703 */     return false;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  707 */     return this.runtimeType.hashCode();
/*      */   }
/*      */   
/*      */   public String toString() {
/*  711 */     return Types.toString(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  718 */     return of((new TypeResolver()).resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final TypeToken<T> rejectTypeVariables() {
/*  726 */     (new TypeVisitor() {
/*      */         void visitTypeVariable(TypeVariable<?> type) {
/*  728 */           String str = String.valueOf(String.valueOf(TypeToken.this.runtimeType)); throw new IllegalArgumentException((new StringBuilder(58 + str.length())).append(str).append("contains a type variable and is not safe for the operation").toString());
/*      */         }
/*      */         
/*      */         void visitWildcardType(WildcardType type) {
/*  732 */           visit(type.getLowerBounds());
/*  733 */           visit(type.getUpperBounds());
/*      */         }
/*      */         void visitParameterizedType(ParameterizedType type) {
/*  736 */           visit(type.getActualTypeArguments());
/*  737 */           visit(new Type[] { type.getOwnerType() });
/*      */         }
/*      */         void visitGenericArrayType(GenericArrayType type) {
/*  740 */           visit(new Type[] { type.getGenericComponentType() });
/*      */         }
/*      */       }).visit(new Type[] { this.runtimeType });
/*  743 */     return this;
/*      */   }
/*      */   
/*      */   private static boolean isAssignable(Type from, Type to) {
/*  747 */     if (to.equals(from)) {
/*  748 */       return true;
/*      */     }
/*  750 */     if (to instanceof WildcardType) {
/*  751 */       return isAssignableToWildcardType(from, (WildcardType)to);
/*      */     }
/*      */ 
/*      */     
/*  755 */     if (from instanceof TypeVariable) {
/*  756 */       return isAssignableFromAny(((TypeVariable)from).getBounds(), to);
/*      */     }
/*      */ 
/*      */     
/*  760 */     if (from instanceof WildcardType) {
/*  761 */       return isAssignableFromAny(((WildcardType)from).getUpperBounds(), to);
/*      */     }
/*  763 */     if (from instanceof GenericArrayType) {
/*  764 */       return isAssignableFromGenericArrayType((GenericArrayType)from, to);
/*      */     }
/*      */     
/*  767 */     if (to instanceof Class)
/*  768 */       return isAssignableToClass(from, (Class)to); 
/*  769 */     if (to instanceof ParameterizedType)
/*  770 */       return isAssignableToParameterizedType(from, (ParameterizedType)to); 
/*  771 */     if (to instanceof GenericArrayType) {
/*  772 */       return isAssignableToGenericArrayType(from, (GenericArrayType)to);
/*      */     }
/*  774 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isAssignableFromAny(Type[] fromTypes, Type to) {
/*  779 */     for (Type from : fromTypes) {
/*  780 */       if (isAssignable(from, to)) {
/*  781 */         return true;
/*      */       }
/*      */     } 
/*  784 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean isAssignableToClass(Type from, Class<?> to) {
/*  788 */     return to.isAssignableFrom(getRawType(from));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isAssignableToWildcardType(Type from, WildcardType to) {
/*  798 */     return (isAssignable(from, supertypeBound(to)) && isAssignableBySubtypeBound(from, to));
/*      */   }
/*      */   
/*      */   private static boolean isAssignableBySubtypeBound(Type from, WildcardType to) {
/*  802 */     Type toSubtypeBound = subtypeBound(to);
/*  803 */     if (toSubtypeBound == null) {
/*  804 */       return true;
/*      */     }
/*  806 */     Type fromSubtypeBound = subtypeBound(from);
/*  807 */     if (fromSubtypeBound == null) {
/*  808 */       return false;
/*      */     }
/*  810 */     return isAssignable(toSubtypeBound, fromSubtypeBound);
/*      */   }
/*      */   
/*      */   private static boolean isAssignableToParameterizedType(Type from, ParameterizedType to) {
/*  814 */     Class<?> matchedClass = getRawType(to);
/*  815 */     if (!matchedClass.isAssignableFrom(getRawType(from))) {
/*  816 */       return false;
/*      */     }
/*  818 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
/*  819 */     Type[] toTypeArgs = to.getActualTypeArguments();
/*  820 */     TypeToken<?> fromTypeToken = of(from);
/*  821 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  829 */       Type fromTypeArg = (fromTypeToken.resolveType(arrayOfTypeVariable[i])).runtimeType;
/*  830 */       if (!matchTypeArgument(fromTypeArg, toTypeArgs[i])) {
/*  831 */         return false;
/*      */       }
/*      */     } 
/*  834 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isAssignableToGenericArrayType(Type from, GenericArrayType to) {
/*  838 */     if (from instanceof Class) {
/*  839 */       Class<?> fromClass = (Class)from;
/*  840 */       if (!fromClass.isArray()) {
/*  841 */         return false;
/*      */       }
/*  843 */       return isAssignable(fromClass.getComponentType(), to.getGenericComponentType());
/*  844 */     }  if (from instanceof GenericArrayType) {
/*  845 */       GenericArrayType fromArrayType = (GenericArrayType)from;
/*  846 */       return isAssignable(fromArrayType.getGenericComponentType(), to.getGenericComponentType());
/*      */     } 
/*  848 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isAssignableFromGenericArrayType(GenericArrayType from, Type to) {
/*  853 */     if (to instanceof Class) {
/*  854 */       Class<?> toClass = (Class)to;
/*  855 */       if (!toClass.isArray()) {
/*  856 */         return (toClass == Object.class);
/*      */       }
/*  858 */       return isAssignable(from.getGenericComponentType(), toClass.getComponentType());
/*  859 */     }  if (to instanceof GenericArrayType) {
/*  860 */       GenericArrayType toArrayType = (GenericArrayType)to;
/*  861 */       return isAssignable(from.getGenericComponentType(), toArrayType.getGenericComponentType());
/*      */     } 
/*  863 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean matchTypeArgument(Type from, Type to) {
/*  868 */     if (from.equals(to)) {
/*  869 */       return true;
/*      */     }
/*  871 */     if (to instanceof WildcardType) {
/*  872 */       return isAssignableToWildcardType(from, (WildcardType)to);
/*      */     }
/*  874 */     return false;
/*      */   }
/*      */   
/*      */   private static Type supertypeBound(Type type) {
/*  878 */     if (type instanceof WildcardType) {
/*  879 */       return supertypeBound((WildcardType)type);
/*      */     }
/*  881 */     return type;
/*      */   }
/*      */   
/*      */   private static Type supertypeBound(WildcardType type) {
/*  885 */     Type[] upperBounds = type.getUpperBounds();
/*  886 */     if (upperBounds.length == 1)
/*  887 */       return supertypeBound(upperBounds[0]); 
/*  888 */     if (upperBounds.length == 0) {
/*  889 */       return Object.class;
/*      */     }
/*  891 */     String str = String.valueOf(String.valueOf(type)); throw new AssertionError((new StringBuilder(59 + str.length())).append("There should be at most one upper bound for wildcard type: ").append(str).toString());
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Type subtypeBound(Type type) {
/*  897 */     if (type instanceof WildcardType) {
/*  898 */       return subtypeBound((WildcardType)type);
/*      */     }
/*  900 */     return type;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private static Type subtypeBound(WildcardType type) {
/*  905 */     Type[] lowerBounds = type.getLowerBounds();
/*  906 */     if (lowerBounds.length == 1)
/*  907 */       return subtypeBound(lowerBounds[0]); 
/*  908 */     if (lowerBounds.length == 0) {
/*  909 */       return null;
/*      */     }
/*  911 */     String str = String.valueOf(String.valueOf(type)); throw new AssertionError((new StringBuilder(46 + str.length())).append("Wildcard should have at most one lower bound: ").append(str).toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static Class<?> getRawType(Type type) {
/*  918 */     return (Class)getRawTypes(type).iterator().next();
/*      */   }
/*      */   @VisibleForTesting
/*      */   static ImmutableSet<Class<?>> getRawTypes(Type type) {
/*  922 */     Preconditions.checkNotNull(type);
/*  923 */     final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
/*  924 */     (new TypeVisitor() {
/*      */         void visitTypeVariable(TypeVariable<?> t) {
/*  926 */           visit(t.getBounds());
/*      */         }
/*      */         void visitWildcardType(WildcardType t) {
/*  929 */           visit(t.getUpperBounds());
/*      */         }
/*      */         void visitParameterizedType(ParameterizedType t) {
/*  932 */           builder.add(t.getRawType());
/*      */         }
/*      */         void visitClass(Class<?> t) {
/*  935 */           builder.add(t);
/*      */         }
/*      */         void visitGenericArrayType(GenericArrayType t) {
/*  938 */           builder.add(Types.getArrayClass(TypeToken.getRawType(t.getGenericComponentType())));
/*      */         }
/*      */       }).visit(new Type[] { type });
/*      */     
/*  942 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
/*  953 */     if (cls.isArray()) {
/*  954 */       Type arrayOfGenericType = Types.newArrayType((toGenericType((Class)cls.getComponentType())).runtimeType);
/*      */ 
/*      */ 
/*      */       
/*  958 */       TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
/*  959 */       return result;
/*      */     } 
/*  961 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*  962 */     if (arrayOfTypeVariable.length > 0) {
/*      */       
/*  964 */       TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedType(cls, (Type[])arrayOfTypeVariable));
/*      */       
/*  966 */       return type;
/*      */     } 
/*  968 */     return of(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
/*  974 */     for (Type upperBound : upperBounds) {
/*      */       
/*  976 */       TypeToken<? super T> bound = (TypeToken)of(upperBound);
/*  977 */       if (of(supertype).isAssignableFrom(bound)) {
/*      */         
/*  979 */         TypeToken<? super T> result = bound.getSupertype(supertype);
/*  980 */         return result;
/*      */       } 
/*      */     } 
/*  983 */     String str1 = String.valueOf(String.valueOf(supertype)), str2 = String.valueOf(String.valueOf(this)); throw new IllegalArgumentException((new StringBuilder(23 + str1.length() + str2.length())).append(str1).append(" isn't a super type of ").append(str2).toString());
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
/*  987 */     Type[] arr$ = lowerBounds; int len$ = arr$.length, i$ = 0; if (i$ < len$) { Type lowerBound = arr$[i$];
/*      */       
/*  989 */       TypeToken<? extends T> bound = (TypeToken)of(lowerBound);
/*      */       
/*  991 */       return bound.getSubtype(subclass); }
/*      */     
/*  993 */     String str1 = String.valueOf(String.valueOf(subclass)), str2 = String.valueOf(String.valueOf(this)); throw new IllegalArgumentException((new StringBuilder(21 + str1.length() + str2.length())).append(str1).append(" isn't a subclass of ").append(str2).toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
/* 1000 */     TypeToken<?> componentType = (TypeToken)Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", new Object[] { supertype, this });
/*      */ 
/*      */ 
/*      */     
/* 1004 */     TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
/*      */     
/* 1006 */     TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
/*      */ 
/*      */     
/* 1009 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
/* 1014 */     TypeToken<?> componentSubtype = getComponentType().getSubtype(subclass.getComponentType());
/*      */ 
/*      */     
/* 1017 */     TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
/*      */ 
/*      */     
/* 1020 */     return result;
/*      */   }
/*      */   
/*      */   private Type resolveTypeArgsForSubclass(Class<?> subclass) {
/* 1024 */     if (this.runtimeType instanceof Class)
/*      */     {
/* 1026 */       return subclass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1035 */     TypeToken<?> genericSubtype = toGenericType(subclass);
/*      */     
/* 1037 */     Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
/*      */ 
/*      */     
/* 1040 */     return (new TypeResolver()).where(supertypeWithArgsFromSubtype, this.runtimeType).resolveType(genericSubtype.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type newArrayClassOrGenericArrayType(Type componentType) {
/* 1050 */     return Types.JavaVersion.JAVA7.newArrayType(componentType);
/*      */   }
/*      */   
/*      */   private static final class SimpleTypeToken<T> extends TypeToken<T> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SimpleTypeToken(Type type) {
/* 1056 */       super(type);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class TypeCollector<K>
/*      */   {
/*      */     private TypeCollector() {}
/*      */ 
/*      */ 
/*      */     
/* 1069 */     static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>()
/*      */       {
/*      */         Class<?> getRawType(TypeToken<?> type) {
/* 1072 */           return type.getRawType();
/*      */         }
/*      */         
/*      */         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
/* 1076 */           return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
/*      */         }
/*      */         
/*      */         @Nullable
/*      */         TypeToken<?> getSuperclass(TypeToken<?> type) {
/* 1081 */           return type.getGenericSuperclass();
/*      */         }
/*      */       };
/*      */     
/* 1085 */     static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>()
/*      */       {
/*      */         Class<?> getRawType(Class<?> type) {
/* 1088 */           return type;
/*      */         }
/*      */         
/*      */         Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
/* 1092 */           return Arrays.asList(type.getInterfaces());
/*      */         }
/*      */         
/*      */         @Nullable
/*      */         Class<?> getSuperclass(Class<?> type) {
/* 1097 */           return type.getSuperclass();
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*      */     final TypeCollector<K> classesOnly() {
/* 1103 */       return new ForwardingTypeCollector<K>(this) {
/*      */           Iterable<? extends K> getInterfaces(K type) {
/* 1105 */             return (Iterable<? extends K>)ImmutableSet.of();
/*      */           }
/*      */           ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1108 */             ImmutableList.Builder<K> builder = ImmutableList.builder();
/* 1109 */             for (K type : types) {
/* 1110 */               if (!getRawType(type).isInterface()) {
/* 1111 */                 builder.add(type);
/*      */               }
/*      */             } 
/* 1114 */             return super.collectTypes((Iterable<? extends K>)builder.build());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     final ImmutableList<K> collectTypes(K type) {
/* 1120 */       return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1125 */       Map<K, Integer> map = Maps.newHashMap();
/* 1126 */       for (K type : types) {
/* 1127 */         collectTypes(type, map);
/*      */       }
/* 1129 */       return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
/*      */     }
/*      */ 
/*      */     
/*      */     private int collectTypes(K type, Map<? super K, Integer> map) {
/* 1134 */       Integer existing = map.get(this);
/* 1135 */       if (existing != null)
/*      */       {
/* 1137 */         return existing.intValue();
/*      */       }
/* 1139 */       int aboveMe = getRawType(type).isInterface() ? 1 : 0;
/*      */ 
/*      */       
/* 1142 */       for (K interfaceType : getInterfaces(type)) {
/* 1143 */         aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
/*      */       }
/* 1145 */       K superclass = getSuperclass(type);
/* 1146 */       if (superclass != null) {
/* 1147 */         aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1154 */       map.put(type, Integer.valueOf(aboveMe + 1));
/* 1155 */       return aboveMe + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
/* 1160 */       Ordering<K> keyOrdering = new Ordering<K>() {
/*      */           public int compare(K left, K right) {
/* 1162 */             return valueComparator.compare(map.get(left), map.get(right));
/*      */           }
/*      */         };
/* 1165 */       return keyOrdering.immutableSortedCopy(map.keySet());
/*      */     }
/*      */     
/*      */     abstract Class<?> getRawType(K param1K);
/*      */     
/*      */     abstract Iterable<? extends K> getInterfaces(K param1K);
/*      */     
/*      */     @Nullable
/*      */     abstract K getSuperclass(K param1K);
/*      */     
/*      */     private static class ForwardingTypeCollector<K> extends TypeCollector<K> {
/*      */       ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
/* 1177 */         this.delegate = delegate;
/*      */       }
/*      */       private final TypeToken.TypeCollector<K> delegate;
/*      */       Class<?> getRawType(K type) {
/* 1181 */         return this.delegate.getRawType(type);
/*      */       }
/*      */       
/*      */       Iterable<? extends K> getInterfaces(K type) {
/* 1185 */         return this.delegate.getInterfaces(type);
/*      */       }
/*      */       
/*      */       K getSuperclass(K type) {
/* 1189 */         return this.delegate.getSuperclass(type);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\TypeToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */