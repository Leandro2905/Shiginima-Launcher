/*     */ package org.jivesoftware.smack.debugger;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.SmackConfiguration;
/*     */ import org.jivesoftware.smack.XMPPConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectionDebuggerFactory
/*     */   implements SmackDebuggerFactory
/*     */ {
/*  31 */   private static final Logger LOGGER = Logger.getLogger(ReflectionDebuggerFactory.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DEBUGGER_CLASS_PROPERTY_NAME = "smack.debuggerClass";
/*     */ 
/*     */   
/*  38 */   private static final String[] DEFAULT_DEBUGGERS = new String[] { "org.jivesoftware.smackx.debugger.EnhancedDebugger", "org.jivesoftware.smackx.debugger.android.AndroidDebugger", "org.jivesoftware.smack.debugger.ConsoleDebugger", "org.jivesoftware.smack.debugger.LiteDebugger", "org.jivesoftware.smack.debugger.JulDebugger" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDebuggerClass(Class<? extends SmackDebugger> debuggerClass) {
/*  51 */     if (debuggerClass == null) {
/*  52 */       System.clearProperty("smack.debuggerClass");
/*     */     } else {
/*  54 */       System.setProperty("smack.debuggerClass", debuggerClass.getCanonicalName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<SmackDebugger> getDebuggerClass() {
/*  64 */     String customDebuggerClassName = getCustomDebuggerClassName();
/*  65 */     if (customDebuggerClassName == null) {
/*  66 */       return getOneOfDefaultDebuggerClasses();
/*     */     }
/*     */     try {
/*  69 */       return (Class)Class.forName(customDebuggerClassName);
/*  70 */     } catch (Exception e) {
/*  71 */       LOGGER.log(Level.WARNING, "Unable to instantiate debugger class " + customDebuggerClassName, e);
/*     */ 
/*     */ 
/*     */       
/*  75 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public SmackDebugger create(XMPPConnection connection, Writer writer, Reader reader) throws IllegalArgumentException {
/*  80 */     Class<SmackDebugger> debuggerClass = getDebuggerClass();
/*  81 */     if (debuggerClass != null) {
/*     */       
/*     */       try {
/*  84 */         Constructor<SmackDebugger> constructor = debuggerClass.getConstructor(new Class[] { XMPPConnection.class, Writer.class, Reader.class });
/*     */         
/*  86 */         return constructor.newInstance(new Object[] { connection, writer, reader });
/*  87 */       } catch (Exception e) {
/*  88 */         throw new IllegalArgumentException("Can't initialize the configured debugger!", e);
/*     */       } 
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getCustomDebuggerClassName() {
/*     */     try {
/*  98 */       return System.getProperty("smack.debuggerClass");
/*  99 */     } catch (Throwable t) {
/*     */       
/* 101 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<SmackDebugger> getOneOfDefaultDebuggerClasses() {
/* 107 */     for (String debugger : DEFAULT_DEBUGGERS) {
/* 108 */       if (!SmackConfiguration.isDisabledSmackClass(debugger)) {
/*     */         
/*     */         try {
/*     */           
/* 112 */           return (Class)Class.forName(debugger);
/* 113 */         } catch (ClassNotFoundException cnfe) {
/* 114 */           LOGGER.fine("Did not find debugger class '" + debugger + "'");
/* 115 */         } catch (ClassCastException ex) {
/* 116 */           LOGGER.warning("Found debugger class that does not appears to implement SmackDebugger interface");
/* 117 */         } catch (Exception ex) {
/* 118 */           LOGGER.warning("Unable to instantiate either Smack debugger class");
/*     */         } 
/*     */       }
/*     */     } 
/* 122 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\ReflectionDebuggerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */