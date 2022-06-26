/*     */ package org.apache.commons.lang3.event;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerSupport<L>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3593265990380473632L;
/*  80 */   private List<L> listeners = new CopyOnWriteArrayList<L>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient L proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient L[] prototypeArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> EventListenerSupport<T> create(Class<T> listenerInterface) {
/* 110 */     return new EventListenerSupport<T>(listenerInterface);
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
/*     */   public EventListenerSupport(Class<L> listenerInterface) {
/* 126 */     this(listenerInterface, Thread.currentThread().getContextClassLoader());
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
/*     */   public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader) {
/* 143 */     this();
/* 144 */     Validate.notNull(listenerInterface, "Listener interface cannot be null.", new Object[0]);
/* 145 */     Validate.notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
/* 146 */     Validate.isTrue(listenerInterface.isInterface(), "Class {0} is not an interface", new Object[] { listenerInterface.getName() });
/*     */     
/* 148 */     initializeTransientFields(listenerInterface, classLoader);
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
/*     */   public L fire() {
/* 167 */     return this.proxy;
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
/*     */   public void addListener(L listener) {
/* 183 */     Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
/* 184 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getListenerCount() {
/* 193 */     return this.listeners.size();
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
/*     */   public void removeListener(L listener) {
/* 205 */     Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
/* 206 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public L[] getListeners() {
/* 216 */     return this.listeners.toArray(this.prototypeArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
/* 225 */     ArrayList<L> serializableListeners = new ArrayList<L>();
/*     */ 
/*     */     
/* 228 */     ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
/* 229 */     for (L listener : this.listeners) {
/*     */       try {
/* 231 */         testObjectOutputStream.writeObject(listener);
/* 232 */         serializableListeners.add(listener);
/* 233 */       } catch (IOException exception) {
/*     */         
/* 235 */         testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     objectOutputStream.writeObject(serializableListeners.toArray(this.prototypeArray));
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
/*     */   private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
/* 254 */     L[] srcListeners = (L[])objectInputStream.readObject();
/*     */     
/* 256 */     this.listeners = new CopyOnWriteArrayList<L>(srcListeners);
/*     */ 
/*     */ 
/*     */     
/* 260 */     Class<L> listenerInterface = (Class)srcListeners.getClass().getComponentType();
/*     */     
/* 262 */     initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeTransientFields(Class<L> listenerInterface, ClassLoader classLoader) {
/* 273 */     L[] array = (L[])Array.newInstance(listenerInterface, 0);
/* 274 */     this.prototypeArray = array;
/* 275 */     createProxy(listenerInterface, classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createProxy(Class<L> listenerInterface, ClassLoader classLoader) {
/* 284 */     this.proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface }, createInvocationHandler()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InvocationHandler createInvocationHandler() {
/* 294 */     return new ProxyInvocationHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EventListenerSupport() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ProxyInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     public Object invoke(Object unusedProxy, Method method, Object[] args) throws Throwable {
/* 316 */       for (L listener : EventListenerSupport.this.listeners) {
/* 317 */         method.invoke(listener, args);
/*     */       }
/* 319 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\event\EventListenerSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */