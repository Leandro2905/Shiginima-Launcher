/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ class AnnotatedSubscriberFinder
/*     */   implements SubscriberFindingStrategy
/*     */ {
/*  53 */   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>()
/*     */       {
/*     */ 
/*     */         
/*     */         public ImmutableList<Method> load(Class<?> concreteClass) throws Exception
/*     */         {
/*  59 */           return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(concreteClass);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object listener) {
/*  70 */     HashMultimap hashMultimap = HashMultimap.create();
/*  71 */     Class<?> clazz = listener.getClass();
/*  72 */     for (Method method : getAnnotatedMethods(clazz)) {
/*  73 */       Class<?>[] parameterTypes = method.getParameterTypes();
/*  74 */       Class<?> eventType = parameterTypes[0];
/*  75 */       EventSubscriber subscriber = makeSubscriber(listener, method);
/*  76 */       hashMultimap.put(eventType, subscriber);
/*     */     } 
/*  78 */     return (Multimap<Class<?>, EventSubscriber>)hashMultimap;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
/*     */     try {
/*  83 */       return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
/*  84 */     } catch (UncheckedExecutionException e) {
/*  85 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MethodIdentifier {
/*     */     private final String name;
/*     */     private final List<Class<?>> parameterTypes;
/*     */     
/*     */     MethodIdentifier(Method method) {
/*  94 */       this.name = method.getName();
/*  95 */       this.parameterTypes = Arrays.asList(method.getParameterTypes());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 100 */       return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 105 */       if (o instanceof MethodIdentifier) {
/* 106 */         MethodIdentifier ident = (MethodIdentifier)o;
/* 107 */         return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
/*     */       } 
/* 109 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
/* 114 */     Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
/* 115 */     Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
/* 116 */     for (Class<?> superClazz : supers) {
/* 117 */       for (Method superClazzMethod : superClazz.getMethods()) {
/* 118 */         if (superClazzMethod.isAnnotationPresent((Class)Subscribe.class) && !superClazzMethod.isBridge()) {
/*     */           
/* 120 */           Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
/* 121 */           if (parameterTypes.length != 1) {
/* 122 */             String str = String.valueOf(String.valueOf(superClazzMethod)); int i = parameterTypes.length; throw new IllegalArgumentException((new StringBuilder(128 + str.length())).append("Method ").append(str).append(" has @Subscribe annotation, but requires ").append(i).append(" arguments.  Event subscriber methods must require a single argument.").toString());
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 127 */           MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
/* 128 */           if (!identifiers.containsKey(ident)) {
/* 129 */             identifiers.put(ident, superClazzMethod);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 134 */     return ImmutableList.copyOf(identifiers.values());
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
/*     */   private static EventSubscriber makeSubscriber(Object listener, Method method) {
/*     */     EventSubscriber wrapper;
/* 150 */     if (methodIsDeclaredThreadSafe(method)) {
/* 151 */       wrapper = new EventSubscriber(listener, method);
/*     */     } else {
/* 153 */       wrapper = new SynchronizedEventSubscriber(listener, method);
/*     */     } 
/* 155 */     return wrapper;
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
/*     */   private static boolean methodIsDeclaredThreadSafe(Method method) {
/* 167 */     return (method.getAnnotation(AllowConcurrentEvents.class) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\eventbus\AnnotatedSubscriberFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */