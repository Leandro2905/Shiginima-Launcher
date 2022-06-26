/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextStack;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadContext
/*     */ {
/*     */   private static class EmptyThreadContextStack
/*     */     extends AbstractCollection<String>
/*     */     implements ThreadContextStack
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private EmptyThreadContextStack() {}
/*     */     
/*  55 */     private static final Iterator<String> EMPTY_ITERATOR = new ThreadContext.EmptyIterator<String>();
/*     */ 
/*     */     
/*     */     public String pop() {
/*  59 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String peek() {
/*  64 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(String message) {
/*  69 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDepth() {
/*  74 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> asList() {
/*  79 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void trim(int depth) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  90 */       return (o instanceof Collection && ((Collection)o).isEmpty());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  96 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack copy() {
/* 101 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 106 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(String e) {
/* 111 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 116 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends String> c) {
/* 121 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 126 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 131 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<String> iterator() {
/* 136 */       return EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 141 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 146 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class EmptyIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private EmptyIterator() {}
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 158 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 163 */       throw new NoSuchElementException("This is an empty iterator!");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static final Map<String, String> EMPTY_MAP = Collections.emptyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final ThreadContextStack EMPTY_STACK = new EmptyThreadContextStack();
/*     */   
/*     */   private static final String DISABLE_MAP = "disableThreadContextMap";
/*     */   
/*     */   private static final String DISABLE_STACK = "disableThreadContextStack";
/*     */   private static final String DISABLE_ALL = "disableThreadContext";
/*     */   private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
/*     */   private static boolean disableAll;
/*     */   private static boolean useMap;
/*     */   private static boolean useStack;
/*     */   private static ThreadContextMap contextMap;
/*     */   private static ThreadContextStack contextStack;
/* 196 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   static {
/* 199 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void init() {
/* 206 */     contextMap = null;
/* 207 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/* 208 */     disableAll = managerProps.getBooleanProperty("disableThreadContext");
/* 209 */     useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !disableAll);
/* 210 */     useMap = (!managerProps.getBooleanProperty("disableThreadContextMap") && !disableAll);
/*     */     
/* 212 */     contextStack = (ThreadContextStack)new DefaultThreadContextStack(useStack);
/* 213 */     String threadContextMapName = managerProps.getStringProperty("log4j2.threadContextMap");
/* 214 */     ClassLoader cl = ProviderUtil.findClassLoader();
/* 215 */     if (threadContextMapName != null) {
/*     */       try {
/* 217 */         Class<?> clazz = cl.loadClass(threadContextMapName);
/* 218 */         if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/* 219 */           contextMap = (ThreadContextMap)clazz.newInstance();
/*     */         }
/* 221 */       } catch (ClassNotFoundException cnfe) {
/* 222 */         LOGGER.error("Unable to locate configured ThreadContextMap {}", new Object[] { threadContextMapName });
/* 223 */       } catch (Exception ex) {
/* 224 */         LOGGER.error("Unable to create configured ThreadContextMap {}", new Object[] { threadContextMapName, ex });
/*     */       } 
/*     */     }
/* 227 */     if (contextMap == null && ProviderUtil.hasProviders()) {
/* 228 */       String factoryClassName = LogManager.getFactory().getClass().getName();
/* 229 */       for (Provider provider : ProviderUtil.getProviders()) {
/* 230 */         if (factoryClassName.equals(provider.getClassName())) {
/* 231 */           Class<? extends ThreadContextMap> clazz = provider.loadThreadContextMap();
/* 232 */           if (clazz != null) {
/*     */             try {
/* 234 */               contextMap = clazz.newInstance();
/*     */               break;
/* 236 */             } catch (Exception e) {
/* 237 */               LOGGER.error("Unable to locate or load configured ThreadContextMap {}", new Object[] { provider.getThreadContextMap(), e });
/*     */               
/* 239 */               contextMap = (ThreadContextMap)new DefaultThreadContextMap(useMap);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 245 */     if (contextMap == null) {
/* 246 */       contextMap = (ThreadContextMap)new DefaultThreadContextMap(useMap);
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
/*     */ 
/*     */   
/*     */   public static void put(String key, String value) {
/* 265 */     contextMap.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(String key) {
/* 276 */     return contextMap.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(String key) {
/* 284 */     contextMap.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearMap() {
/* 291 */     contextMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearAll() {
/* 298 */     clearMap();
/* 299 */     clearStack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsKey(String key) {
/* 308 */     return contextMap.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getContext() {
/* 316 */     return contextMap.getCopy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getImmutableContext() {
/* 324 */     Map<String, String> map = contextMap.getImmutableMapOrNull();
/* 325 */     return (map == null) ? EMPTY_MAP : map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty() {
/* 333 */     return contextMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearStack() {
/* 340 */     contextStack.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack cloneStack() {
/* 348 */     return contextStack.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack getImmutableStack() {
/* 356 */     ContextStack result = contextStack.getImmutableStackOrNull();
/* 357 */     return (result == null) ? (ContextStack)EMPTY_STACK : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setStack(Collection<String> stack) {
/* 365 */     if (stack.isEmpty() || !useStack) {
/*     */       return;
/*     */     }
/* 368 */     contextStack.clear();
/* 369 */     contextStack.addAll(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDepth() {
/* 379 */     return contextStack.getDepth();
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
/*     */   public static String pop() {
/* 391 */     return contextStack.pop();
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
/*     */   public static String peek() {
/* 404 */     return contextStack.peek();
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
/*     */   public static void push(String message) {
/* 416 */     contextStack.push(message);
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
/*     */   public static void push(String message, Object... args) {
/* 430 */     contextStack.push(ParameterizedMessage.format(message, args));
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
/*     */   public static void removeStack() {
/* 452 */     contextStack.clear();
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
/*     */   public static void trim(int depth) {
/* 485 */     contextStack.trim(depth);
/*     */   }
/*     */   
/*     */   public static interface ContextStack extends Serializable, Collection<String> {
/*     */     String pop();
/*     */     
/*     */     String peek();
/*     */     
/*     */     void push(String param1String);
/*     */     
/*     */     int getDepth();
/*     */     
/*     */     List<String> asList();
/*     */     
/*     */     void trim(int param1Int);
/*     */     
/*     */     ContextStack copy();
/*     */     
/*     */     ContextStack getImmutableStackOrNull();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\ThreadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */