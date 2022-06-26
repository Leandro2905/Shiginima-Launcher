/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EventBus
/*     */ {
/* 121 */   private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Set<Class<?>>>()
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public Set<Class<?>> load(Class<?> concreteClass)
/*     */         {
/* 128 */           return TypeToken.of(concreteClass).getTypes().rawTypes();
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final SetMultimap<Class<?>, EventSubscriber> subscribersByType = (SetMultimap<Class<?>, EventSubscriber>)HashMultimap.create();
/*     */   
/* 140 */   private final ReadWriteLock subscribersByTypeLock = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private final SubscriberFindingStrategy finder = new AnnotatedSubscriberFinder();
/*     */ 
/*     */   
/* 150 */   private final ThreadLocal<Queue<EventWithSubscriber>> eventsToDispatch = new ThreadLocal<Queue<EventWithSubscriber>>()
/*     */     {
/*     */       protected Queue<EventBus.EventWithSubscriber> initialValue() {
/* 153 */         return new LinkedList<EventBus.EventWithSubscriber>();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 158 */   private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>()
/*     */     {
/*     */       protected Boolean initialValue() {
/* 161 */         return Boolean.valueOf(false);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private SubscriberExceptionHandler subscriberExceptionHandler;
/*     */ 
/*     */   
/*     */   public EventBus() {
/* 171 */     this("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(String identifier) {
/* 181 */     this(new LoggingSubscriberExceptionHandler(identifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(SubscriberExceptionHandler subscriberExceptionHandler) {
/* 191 */     this.subscriberExceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(subscriberExceptionHandler);
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
/*     */   public void register(Object object) {
/* 203 */     Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
/*     */     
/* 205 */     this.subscribersByTypeLock.writeLock().lock();
/*     */     try {
/* 207 */       this.subscribersByType.putAll(methodsInListener);
/*     */     } finally {
/* 209 */       this.subscribersByTypeLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Object object) {
/* 220 */     Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
/*     */     
/* 222 */     for (Map.Entry<Class<?>, Collection<EventSubscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<EventSubscriber>>>)methodsInListener.asMap().entrySet()) {
/* 223 */       Class<?> eventType = entry.getKey();
/* 224 */       Collection<EventSubscriber> eventMethodsInListener = entry.getValue();
/*     */       
/* 226 */       this.subscribersByTypeLock.writeLock().lock();
/*     */       try {
/* 228 */         Set<EventSubscriber> currentSubscribers = this.subscribersByType.get(eventType);
/* 229 */         if (!currentSubscribers.containsAll(eventMethodsInListener)) {
/* 230 */           String str = String.valueOf(String.valueOf(object)); throw new IllegalArgumentException((new StringBuilder(65 + str.length())).append("missing event subscriber for an annotated method. Is ").append(str).append(" registered?").toString());
/*     */         } 
/*     */         
/* 233 */         currentSubscribers.removeAll(eventMethodsInListener);
/*     */       } finally {
/* 235 */         this.subscribersByTypeLock.writeLock().unlock();
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
/*     */   public void post(Object event) {
/* 252 */     Set<Class<?>> dispatchTypes = flattenHierarchy(event.getClass());
/*     */     
/* 254 */     boolean dispatched = false;
/* 255 */     for (Class<?> eventType : dispatchTypes) {
/* 256 */       this.subscribersByTypeLock.readLock().lock();
/*     */       try {
/* 258 */         Set<EventSubscriber> wrappers = this.subscribersByType.get(eventType);
/*     */         
/* 260 */         if (!wrappers.isEmpty()) {
/* 261 */           dispatched = true;
/* 262 */           for (EventSubscriber wrapper : wrappers) {
/* 263 */             enqueueEvent(event, wrapper);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 267 */         this.subscribersByTypeLock.readLock().unlock();
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     if (!dispatched && !(event instanceof DeadEvent)) {
/* 272 */       post(new DeadEvent(this, event));
/*     */     }
/*     */     
/* 275 */     dispatchQueuedEvents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void enqueueEvent(Object event, EventSubscriber subscriber) {
/* 284 */     ((Queue<EventWithSubscriber>)this.eventsToDispatch.get()).offer(new EventWithSubscriber(event, subscriber));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void dispatchQueuedEvents() {
/* 295 */     if (((Boolean)this.isDispatching.get()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 299 */     this.isDispatching.set(Boolean.valueOf(true));
/*     */     try {
/* 301 */       Queue<EventWithSubscriber> events = this.eventsToDispatch.get();
/*     */       EventWithSubscriber eventWithSubscriber;
/* 303 */       while ((eventWithSubscriber = events.poll()) != null) {
/* 304 */         dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
/*     */       }
/*     */     } finally {
/* 307 */       this.isDispatching.remove();
/* 308 */       this.eventsToDispatch.remove();
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
/*     */   void dispatch(Object event, EventSubscriber wrapper) {
/*     */     try {
/* 322 */       wrapper.handleEvent(event);
/* 323 */     } catch (InvocationTargetException e) {
/*     */       try {
/* 325 */         this.subscriberExceptionHandler.handleException(e.getCause(), new SubscriberExceptionContext(this, event, wrapper.getSubscriber(), wrapper.getMethod()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 332 */       catch (Throwable t) {
/*     */         
/* 334 */         Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", new Object[] { t, e.getCause() }), t);
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
/*     */   @VisibleForTesting
/*     */   Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
/*     */     try {
/* 354 */       return (Set<Class<?>>)flattenHierarchyCache.getUnchecked(concreteClass);
/* 355 */     } catch (UncheckedExecutionException e) {
/* 356 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LoggingSubscriberExceptionHandler
/*     */     implements SubscriberExceptionHandler
/*     */   {
/*     */     private final Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LoggingSubscriberExceptionHandler(String identifier) {
/* 377 */       String str1 = String.valueOf(String.valueOf(EventBus.class.getName())), str2 = String.valueOf(String.valueOf(Preconditions.checkNotNull(identifier))); this.logger = Logger.getLogger((new StringBuilder(1 + str1.length() + str2.length())).append(str1).append(".").append(str2).toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void handleException(Throwable exception, SubscriberExceptionContext context) {
/* 384 */       String str1 = String.valueOf(String.valueOf(context.getSubscriber())), str2 = String.valueOf(String.valueOf(context.getSubscriberMethod())); this.logger.log(Level.SEVERE, (new StringBuilder(30 + str1.length() + str2.length())).append("Could not dispatch event: ").append(str1).append(" to ").append(str2).toString(), exception.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class EventWithSubscriber
/*     */   {
/*     */     final Object event;
/*     */     final EventSubscriber subscriber;
/*     */     
/*     */     public EventWithSubscriber(Object event, EventSubscriber subscriber) {
/* 395 */       this.event = Preconditions.checkNotNull(event);
/* 396 */       this.subscriber = (EventSubscriber)Preconditions.checkNotNull(subscriber);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\eventbus\EventBus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */