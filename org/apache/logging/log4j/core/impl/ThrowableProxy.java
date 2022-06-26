/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrowableProxy
/*     */   implements Serializable
/*     */ {
/*     */   static class CacheEntry
/*     */   {
/*     */     private final ExtendedClassInfo element;
/*     */     private final ClassLoader loader;
/*     */     
/*     */     public CacheEntry(ExtendedClassInfo element, ClassLoader loader) {
/*  59 */       this.element = element;
/*  60 */       this.loader = loader;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PrivateSecurityManager
/*     */     extends SecurityManager {
/*     */     private PrivateSecurityManager() {}
/*     */     
/*     */     public Class<?>[] getClasses() {
/*  69 */       return getClassContext();
/*     */     }
/*     */   }
/*     */   
/*  73 */   private static final ThrowableProxy[] EMPTY_THROWABLE_PROXY_ARRAY = new ThrowableProxy[0];
/*     */   
/*     */   private static final char EOL = '\n';
/*     */   
/*  77 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final PrivateSecurityManager SECURITY_MANAGER;
/*     */   
/*     */   private static final long serialVersionUID = -2752771578252251910L;
/*     */   
/*     */   static {
/*  84 */     if (ReflectiveCallerClassUtility.isSupported()) {
/*  85 */       SECURITY_MANAGER = null;
/*     */     } else {
/*     */       PrivateSecurityManager privateSecurityManager;
/*     */       try {
/*  89 */         privateSecurityManager = new PrivateSecurityManager();
/*  90 */         if (privateSecurityManager.getClasses() == null) {
/*     */           
/*  92 */           privateSecurityManager = null;
/*  93 */           LOGGER.error("Unable to obtain call stack from security manager.");
/*     */         } 
/*  95 */       } catch (Exception e) {
/*  96 */         privateSecurityManager = null;
/*  97 */         LOGGER.debug("Unable to install security manager.", e);
/*     */       } 
/*  99 */       SECURITY_MANAGER = privateSecurityManager;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final ThrowableProxy causeProxy;
/*     */ 
/*     */   
/*     */   private int commonElementCount;
/*     */   
/*     */   private final ExtendedStackTraceElement[] extendedStackTrace;
/*     */   
/*     */   private final String localizedMessage;
/*     */   
/*     */   private final String message;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ThrowableProxy[] suppressedProxies;
/*     */   
/*     */   private final transient Throwable throwable;
/*     */ 
/*     */   
/*     */   private ThrowableProxy() {
/* 124 */     this.throwable = null;
/* 125 */     this.name = null;
/* 126 */     this.extendedStackTrace = null;
/* 127 */     this.causeProxy = null;
/* 128 */     this.message = null;
/* 129 */     this.localizedMessage = null;
/* 130 */     this.suppressedProxies = EMPTY_THROWABLE_PROXY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy(Throwable throwable) {
/* 140 */     this.throwable = throwable;
/* 141 */     this.name = throwable.getClass().getName();
/* 142 */     this.message = throwable.getMessage();
/* 143 */     this.localizedMessage = throwable.getLocalizedMessage();
/* 144 */     Map<String, CacheEntry> map = new HashMap<String, CacheEntry>();
/* 145 */     Stack<Class<?>> stack = getCurrentStack();
/* 146 */     this.extendedStackTrace = toExtendedStackTrace(stack, map, null, throwable.getStackTrace());
/* 147 */     Throwable throwableCause = throwable.getCause();
/* 148 */     this.causeProxy = (throwableCause == null) ? null : new ThrowableProxy(throwable, stack, map, throwableCause);
/* 149 */     this.suppressedProxies = toSuppressedProxies(throwable);
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
/*     */   private ThrowableProxy(Throwable parent, Stack<Class<?>> stack, Map<String, CacheEntry> map, Throwable cause) {
/* 166 */     this.throwable = cause;
/* 167 */     this.name = cause.getClass().getName();
/* 168 */     this.message = this.throwable.getMessage();
/* 169 */     this.localizedMessage = this.throwable.getLocalizedMessage();
/* 170 */     this.extendedStackTrace = toExtendedStackTrace(stack, map, parent.getStackTrace(), cause.getStackTrace());
/* 171 */     this.causeProxy = (cause.getCause() == null) ? null : new ThrowableProxy(parent, stack, map, cause.getCause());
/* 172 */     this.suppressedProxies = toSuppressedProxies(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 177 */     if (this == obj) {
/* 178 */       return true;
/*     */     }
/* 180 */     if (obj == null) {
/* 181 */       return false;
/*     */     }
/* 183 */     if (getClass() != obj.getClass()) {
/* 184 */       return false;
/*     */     }
/* 186 */     ThrowableProxy other = (ThrowableProxy)obj;
/* 187 */     if (this.causeProxy == null) {
/* 188 */       if (other.causeProxy != null) {
/* 189 */         return false;
/*     */       }
/* 191 */     } else if (!this.causeProxy.equals(other.causeProxy)) {
/* 192 */       return false;
/*     */     } 
/* 194 */     if (this.commonElementCount != other.commonElementCount) {
/* 195 */       return false;
/*     */     }
/* 197 */     if (this.name == null) {
/* 198 */       if (other.name != null) {
/* 199 */         return false;
/*     */       }
/* 201 */     } else if (!this.name.equals(other.name)) {
/* 202 */       return false;
/*     */     } 
/* 204 */     if (!Arrays.equals((Object[])this.extendedStackTrace, (Object[])other.extendedStackTrace)) {
/* 205 */       return false;
/*     */     }
/* 207 */     if (!Arrays.equals((Object[])this.suppressedProxies, (Object[])other.suppressedProxies)) {
/* 208 */       return false;
/*     */     }
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatCause(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages) {
/* 215 */     sb.append("Caused by: ").append(cause).append('\n');
/* 216 */     formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.extendedStackTrace, ignorePackages);
/*     */     
/* 218 */     if (cause.getCauseProxy() != null) {
/* 219 */       formatCause(sb, cause.causeProxy, ignorePackages);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatElements(StringBuilder sb, int commonCount, StackTraceElement[] causedTrace, ExtendedStackTraceElement[] extStackTrace, List<String> ignorePackages) {
/* 225 */     if (ignorePackages == null || ignorePackages.isEmpty()) {
/* 226 */       for (int i = 0; i < extStackTrace.length; i++) {
/* 227 */         formatEntry(causedTrace[i], extStackTrace[i], sb);
/*     */       }
/*     */     } else {
/* 230 */       int count = 0;
/* 231 */       for (int i = 0; i < extStackTrace.length; i++) {
/* 232 */         if (!ignoreElement(causedTrace[i], ignorePackages)) {
/* 233 */           if (count > 0) {
/* 234 */             if (count == 1) {
/* 235 */               sb.append("\t....\n");
/*     */             } else {
/* 237 */               sb.append("\t... suppressed ").append(count).append(" lines\n");
/*     */             } 
/* 239 */             count = 0;
/*     */           } 
/* 241 */           formatEntry(causedTrace[i], extStackTrace[i], sb);
/*     */         } else {
/* 243 */           count++;
/*     */         } 
/*     */       } 
/* 246 */       if (count > 0) {
/* 247 */         if (count == 1) {
/* 248 */           sb.append("\t...\n");
/*     */         } else {
/* 250 */           sb.append("\t... suppressed ").append(count).append(" lines\n");
/*     */         } 
/*     */       }
/*     */     } 
/* 254 */     if (commonCount != 0) {
/* 255 */       sb.append("\t... ").append(commonCount).append(" more").append('\n');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatEntry(StackTraceElement element, ExtendedStackTraceElement extStackTraceElement, StringBuilder sb) {
/* 261 */     sb.append("\tat ");
/* 262 */     sb.append(extStackTraceElement);
/* 263 */     sb.append('\n');
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause) {
/* 275 */     formatWrapper(sb, cause, null);
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> packages) {
/* 290 */     Throwable caused = (cause.getCauseProxy() != null) ? cause.getCauseProxy().getThrowable() : null;
/* 291 */     if (caused != null) {
/* 292 */       formatWrapper(sb, cause.causeProxy);
/* 293 */       sb.append("Wrapped by: ");
/*     */     } 
/* 295 */     sb.append(cause).append('\n');
/* 296 */     formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.extendedStackTrace, packages);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThrowableProxy getCauseProxy() {
/* 301 */     return this.causeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString() {
/* 310 */     return getCauseStackTraceAsString(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString(List<String> packages) {
/* 321 */     StringBuilder sb = new StringBuilder();
/* 322 */     if (this.causeProxy != null) {
/* 323 */       formatWrapper(sb, this.causeProxy);
/* 324 */       sb.append("Wrapped by: ");
/*     */     } 
/* 326 */     sb.append(toString());
/* 327 */     sb.append('\n');
/* 328 */     formatElements(sb, 0, this.throwable.getStackTrace(), this.extendedStackTrace, packages);
/* 329 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCommonElementCount() {
/* 339 */     return this.commonElementCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Stack<Class<?>> getCurrentStack() {
/* 349 */     if (ReflectiveCallerClassUtility.isSupported()) {
/* 350 */       Stack<Class<?>> classes = new Stack<Class<?>>();
/* 351 */       int index = 1;
/* 352 */       Class<?> clazz = ReflectiveCallerClassUtility.getCaller(index);
/* 353 */       while (clazz != null) {
/* 354 */         classes.push(clazz);
/* 355 */         clazz = ReflectiveCallerClassUtility.getCaller(++index);
/*     */       } 
/* 357 */       return classes;
/* 358 */     }  if (SECURITY_MANAGER != null) {
/* 359 */       Class<?>[] array = SECURITY_MANAGER.getClasses();
/* 360 */       Stack<Class<?>> classes = new Stack<Class<?>>();
/* 361 */       for (Class<?> clazz : array) {
/* 362 */         classes.push(clazz);
/*     */       }
/* 364 */       return classes;
/*     */     } 
/* 366 */     return new Stack<Class<?>>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement[] getExtendedStackTrace() {
/* 375 */     return this.extendedStackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString() {
/* 384 */     return getExtendedStackTraceAsString(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages) {
/* 395 */     StringBuilder sb = new StringBuilder(this.name);
/* 396 */     String msg = this.message;
/* 397 */     if (msg != null) {
/* 398 */       sb.append(": ").append(msg);
/*     */     }
/* 400 */     sb.append('\n');
/* 401 */     formatElements(sb, 0, this.throwable.getStackTrace(), this.extendedStackTrace, ignorePackages);
/* 402 */     if (this.causeProxy != null) {
/* 403 */       formatCause(sb, this.causeProxy, ignorePackages);
/*     */     }
/* 405 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/* 409 */     return this.localizedMessage;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 413 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 422 */     return this.name;
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStackTrace() {
/* 426 */     return (this.throwable == null) ? null : this.throwable.getStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy[] getSuppressedProxies() {
/* 435 */     return this.suppressedProxies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuppressedStackTrace() {
/* 444 */     ThrowableProxy[] suppressed = getSuppressedProxies();
/* 445 */     if (suppressed == null || suppressed.length == 0) {
/* 446 */       return "";
/*     */     }
/* 448 */     StringBuilder sb = new StringBuilder("Suppressed Stack Trace Elements:\n");
/* 449 */     for (ThrowableProxy proxy : suppressed) {
/* 450 */       sb.append(proxy.getExtendedStackTraceAsString());
/*     */     }
/* 452 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 461 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 466 */     int prime = 31;
/* 467 */     int result = 1;
/* 468 */     result = 31 * result + ((this.causeProxy == null) ? 0 : this.causeProxy.hashCode());
/* 469 */     result = 31 * result + this.commonElementCount;
/* 470 */     result = 31 * result + ((this.extendedStackTrace == null) ? 0 : Arrays.hashCode((Object[])this.extendedStackTrace));
/* 471 */     result = 31 * result + ((this.suppressedProxies == null) ? 0 : Arrays.hashCode((Object[])this.suppressedProxies));
/* 472 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/* 473 */     return result;
/*     */   }
/*     */   
/*     */   private boolean ignoreElement(StackTraceElement element, List<String> ignorePackages) {
/* 477 */     String className = element.getClassName();
/* 478 */     for (String pkg : ignorePackages) {
/* 479 */       if (className.startsWith(pkg)) {
/* 480 */         return true;
/*     */       }
/*     */     } 
/* 483 */     return false;
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
/*     */   private Class<?> loadClass(ClassLoader lastLoader, String className) {
/*     */     Class<?> clazz;
/* 498 */     if (lastLoader != null) {
/*     */       try {
/* 500 */         clazz = Loader.initializeClass(className, lastLoader);
/* 501 */         if (clazz != null) {
/* 502 */           return clazz;
/*     */         }
/* 504 */       } catch (Exception ignore) {}
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 509 */       clazz = Loader.loadClass(className);
/* 510 */     } catch (ClassNotFoundException ignored) {
/*     */       try {
/* 512 */         clazz = Loader.initializeClass(className, getClass().getClassLoader());
/* 513 */       } catch (ClassNotFoundException ignore) {
/* 514 */         return null;
/*     */       } 
/*     */     } 
/* 517 */     return clazz;
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
/*     */   private CacheEntry toCacheEntry(StackTraceElement stackTraceElement, Class<?> callerClass, boolean exact) {
/* 534 */     String location = "?";
/* 535 */     String version = "?";
/* 536 */     ClassLoader lastLoader = null;
/* 537 */     if (callerClass != null) {
/*     */       try {
/* 539 */         CodeSource source = callerClass.getProtectionDomain().getCodeSource();
/* 540 */         if (source != null) {
/* 541 */           URL locationURL = source.getLocation();
/* 542 */           if (locationURL != null) {
/* 543 */             String str = locationURL.toString().replace('\\', '/');
/* 544 */             int index = str.lastIndexOf("/");
/* 545 */             if (index >= 0 && index == str.length() - 1) {
/* 546 */               index = str.lastIndexOf("/", index - 1);
/* 547 */               location = str.substring(index + 1);
/*     */             } else {
/* 549 */               location = str.substring(index + 1);
/*     */             } 
/*     */           } 
/*     */         } 
/* 553 */       } catch (Exception ex) {}
/*     */ 
/*     */       
/* 556 */       Package pkg = callerClass.getPackage();
/* 557 */       if (pkg != null) {
/* 558 */         String ver = pkg.getImplementationVersion();
/* 559 */         if (ver != null) {
/* 560 */           version = ver;
/*     */         }
/*     */       } 
/* 563 */       lastLoader = callerClass.getClassLoader();
/*     */     } 
/* 565 */     return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
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
/*     */   ExtendedStackTraceElement[] toExtendedStackTrace(Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace) {
/*     */     int stackLength;
/* 584 */     if (rootTrace != null) {
/* 585 */       int rootIndex = rootTrace.length - 1;
/* 586 */       int stackIndex = stackTrace.length - 1;
/* 587 */       while (rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex])) {
/* 588 */         rootIndex--;
/* 589 */         stackIndex--;
/*     */       } 
/* 591 */       this.commonElementCount = stackTrace.length - 1 - stackIndex;
/* 592 */       stackLength = stackIndex + 1;
/*     */     } else {
/* 594 */       this.commonElementCount = 0;
/* 595 */       stackLength = stackTrace.length;
/*     */     } 
/* 597 */     ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
/* 598 */     Class<?> clazz = stack.isEmpty() ? null : stack.peek();
/* 599 */     ClassLoader lastLoader = null;
/* 600 */     for (int i = stackLength - 1; i >= 0; i--) {
/* 601 */       ExtendedClassInfo extClassInfo; StackTraceElement stackTraceElement = stackTrace[i];
/* 602 */       String className = stackTraceElement.getClassName();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 607 */       if (clazz != null && className.equals(clazz.getName())) {
/* 608 */         CacheEntry entry = toCacheEntry(stackTraceElement, clazz, true);
/* 609 */         extClassInfo = entry.element;
/* 610 */         lastLoader = entry.loader;
/* 611 */         stack.pop();
/* 612 */         clazz = stack.isEmpty() ? null : stack.peek();
/*     */       }
/* 614 */       else if (map.containsKey(className)) {
/* 615 */         CacheEntry entry = map.get(className);
/* 616 */         extClassInfo = entry.element;
/* 617 */         if (entry.loader != null) {
/* 618 */           lastLoader = entry.loader;
/*     */         }
/*     */       } else {
/* 621 */         CacheEntry entry = toCacheEntry(stackTraceElement, loadClass(lastLoader, className), false);
/*     */         
/* 623 */         extClassInfo = entry.element;
/* 624 */         map.put(stackTraceElement.toString(), entry);
/* 625 */         if (entry.loader != null) {
/* 626 */           lastLoader = entry.loader;
/*     */         }
/*     */       } 
/*     */       
/* 630 */       extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
/*     */     } 
/* 632 */     return extStackTrace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 637 */     String msg = this.message;
/* 638 */     return (msg != null) ? (this.name + ": " + msg) : this.name;
/*     */   }
/*     */   
/*     */   private ThrowableProxy[] toSuppressedProxies(Throwable thrown) {
/*     */     try {
/* 643 */       Throwable[] suppressed = Throwables.getSuppressed(thrown);
/* 644 */       if (suppressed == null) {
/* 645 */         return EMPTY_THROWABLE_PROXY_ARRAY;
/*     */       }
/* 647 */       ThrowableProxy[] proxies = new ThrowableProxy[suppressed.length];
/* 648 */       for (int i = 0; i < suppressed.length; i++) {
/* 649 */         proxies[i] = new ThrowableProxy(suppressed[i]);
/*     */       }
/* 651 */       return proxies;
/* 652 */     } catch (Exception e) {
/* 653 */       StatusLogger.getLogger().error(e);
/*     */       
/* 655 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\ThrowableProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */