/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.core.util.JndiCloser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiContextSelector
/*     */   implements NamedContextSelector
/*     */ {
/*  92 */   private static final LoggerContext CONTEXT = new LoggerContext("Default");
/*     */   
/*  94 */   private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap<String, LoggerContext>();
/*     */ 
/*     */   
/*  97 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 101 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 108 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 109 */     if (lc != null) {
/* 110 */       return lc;
/*     */     }
/*     */     
/* 113 */     String loggingContextName = null;
/*     */     
/* 115 */     Context ctx = null;
/*     */     try {
/* 117 */       ctx = new InitialContext();
/* 118 */       loggingContextName = (String)lookup(ctx, "java:comp/env/log4j/context-name");
/* 119 */     } catch (NamingException ne) {
/* 120 */       LOGGER.error("Unable to lookup java:comp/env/log4j/context-name", ne);
/*     */     } finally {
/* 122 */       JndiCloser.closeSilently(ctx);
/*     */     } 
/*     */     
/* 125 */     return (loggingContextName == null) ? CONTEXT : locateContext(loggingContextName, null, configLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext locateContext(String name, Object externalContext, URI configLocation) {
/* 130 */     if (name == null) {
/* 131 */       LOGGER.error("A context name is required to locate a LoggerContext");
/* 132 */       return null;
/*     */     } 
/* 134 */     if (!CONTEXT_MAP.containsKey(name)) {
/* 135 */       LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
/* 136 */       CONTEXT_MAP.putIfAbsent(name, ctx);
/*     */     } 
/* 138 */     return CONTEXT_MAP.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 144 */     for (Map.Entry<String, LoggerContext> entry : CONTEXT_MAP.entrySet()) {
/* 145 */       if (((LoggerContext)entry.getValue()).equals(context)) {
/* 146 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext removeContext(String name) {
/* 153 */     return CONTEXT_MAP.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/* 158 */     List<LoggerContext> list = new ArrayList<LoggerContext>(CONTEXT_MAP.values());
/* 159 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Object lookup(Context ctx, String name) throws NamingException {
/* 164 */     if (ctx == null) {
/* 165 */       return null;
/*     */     }
/*     */     try {
/* 168 */       return ctx.lookup(name);
/* 169 */     } catch (NameNotFoundException e) {
/* 170 */       LOGGER.error("Could not find name [" + name + "].");
/* 171 */       throw e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\selector\JndiContextSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */