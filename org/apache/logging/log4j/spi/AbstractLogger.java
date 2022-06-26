/*      */ package org.apache.logging.log4j.spi;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.Marker;
/*      */ import org.apache.logging.log4j.MarkerManager;
/*      */ import org.apache.logging.log4j.message.Message;
/*      */ import org.apache.logging.log4j.message.MessageFactory;
/*      */ import org.apache.logging.log4j.message.ParameterizedMessageFactory;
/*      */ import org.apache.logging.log4j.message.StringFormattedMessage;
/*      */ import org.apache.logging.log4j.status.StatusLogger;
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
/*      */ public abstract class AbstractLogger
/*      */   implements ExtendedLogger, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 2L;
/*   40 */   public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   45 */   public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTRY").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   50 */   public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   55 */   public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   60 */   public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   65 */   public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   70 */   public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = (Class)ParameterizedMessageFactory.class;
/*      */   
/*   72 */   private static final String FQCN = AbstractLogger.class.getName();
/*      */ 
/*      */   
/*      */   private static final String THROWING = "throwing";
/*      */ 
/*      */   
/*      */   private static final String CATCHING = "catching";
/*      */ 
/*      */   
/*      */   private final String name;
/*      */   
/*      */   private final MessageFactory messageFactory;
/*      */ 
/*      */   
/*      */   public static void checkMessageFactory(ExtendedLogger logger, MessageFactory messageFactory) {
/*   87 */     String name = logger.getName();
/*   88 */     MessageFactory loggerMessageFactory = logger.getMessageFactory();
/*   89 */     if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
/*   90 */       StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", new Object[] { name, loggerMessageFactory, messageFactory });
/*      */ 
/*      */     
/*      */     }
/*   94 */     else if (messageFactory == null && !loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
/*   95 */       StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", new Object[] { name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS.getName() });
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
/*      */ 
/*      */   
/*      */   public AbstractLogger() {
/*  111 */     this.name = getClass().getName();
/*  112 */     this.messageFactory = createDefaultMessageFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name) {
/*  121 */     this.name = name;
/*  122 */     this.messageFactory = createDefaultMessageFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name, MessageFactory messageFactory) {
/*  132 */     this.name = name;
/*  133 */     this.messageFactory = (messageFactory == null) ? createDefaultMessageFactory() : messageFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void catching(Level level, Throwable t) {
/*  144 */     catching(FQCN, level, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void catching(String fqcn, Level level, Throwable t) {
/*  155 */     if (isEnabled(level, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  156 */       logMessage(fqcn, level, CATCHING_MARKER, catchingMsg(t), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void catching(Throwable t) {
/*  167 */     if (isEnabled(Level.ERROR, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  168 */       logMessage(FQCN, Level.ERROR, CATCHING_MARKER, catchingMsg(t), t);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Message catchingMsg(Throwable t) {
/*  173 */     return this.messageFactory.newMessage("catching");
/*      */   }
/*      */   
/*      */   private MessageFactory createDefaultMessageFactory() {
/*      */     try {
/*  178 */       return DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
/*  179 */     } catch (InstantiationException e) {
/*  180 */       throw new IllegalStateException(e);
/*  181 */     } catch (IllegalAccessException e) {
/*  182 */       throw new IllegalStateException(e);
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
/*      */   public void debug(Marker marker, Message msg) {
/*  194 */     logIfEnabled(FQCN, Level.DEBUG, marker, msg, (Throwable)null);
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
/*      */   public void debug(Marker marker, Message msg, Throwable t) {
/*  206 */     logIfEnabled(FQCN, Level.DEBUG, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Object message) {
/*  217 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
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
/*      */   public void debug(Marker marker, Object message, Throwable t) {
/*  230 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message) {
/*  241 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
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
/*      */   public void debug(Marker marker, String message, Object... params) {
/*  253 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, params);
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
/*      */   public void debug(Marker marker, String message, Throwable t) {
/*  266 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Message msg) {
/*  276 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Message msg, Throwable t) {
/*  287 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Object message) {
/*  297 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
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
/*      */   public void debug(Object message, Throwable t) {
/*  309 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message) {
/*  319 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object... params) {
/*  330 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, params);
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
/*      */   public void debug(String message, Throwable t) {
/*  342 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void entry() {
/*  350 */     entry(FQCN, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void entry(Object... params) {
/*  360 */     entry(FQCN, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entry(String fqcn, Object... params) {
/*  370 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  371 */       logIfEnabled(fqcn, Level.TRACE, ENTRY_MARKER, entryMsg(params.length, params), (Throwable)null);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Message entryMsg(int count, Object... params) {
/*  376 */     if (count == 0) {
/*  377 */       return this.messageFactory.newMessage("entry");
/*      */     }
/*  379 */     StringBuilder sb = new StringBuilder("entry params(");
/*  380 */     int i = 0;
/*  381 */     for (Object parm : params) {
/*  382 */       if (parm != null) {
/*  383 */         sb.append(parm.toString());
/*      */       } else {
/*  385 */         sb.append("null");
/*      */       } 
/*  387 */       if (++i < params.length) {
/*  388 */         sb.append(", ");
/*      */       }
/*      */     } 
/*  391 */     sb.append(')');
/*  392 */     return this.messageFactory.newMessage(sb.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Message msg) {
/*  403 */     logIfEnabled(FQCN, Level.ERROR, marker, msg, (Throwable)null);
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
/*      */   public void error(Marker marker, Message msg, Throwable t) {
/*  415 */     logIfEnabled(FQCN, Level.ERROR, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Object message) {
/*  426 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
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
/*      */   public void error(Marker marker, Object message, Throwable t) {
/*  439 */     logIfEnabled(FQCN, Level.ERROR, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message) {
/*  450 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
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
/*      */   public void error(Marker marker, String message, Object... params) {
/*  462 */     logIfEnabled(FQCN, Level.ERROR, marker, message, params);
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
/*      */   public void error(Marker marker, String message, Throwable t) {
/*  475 */     logIfEnabled(FQCN, Level.ERROR, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Message msg) {
/*  485 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Message msg, Throwable t) {
/*  496 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Object message) {
/*  506 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
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
/*      */   public void error(Object message, Throwable t) {
/*  518 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message) {
/*  528 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object... params) {
/*  539 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, params);
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
/*      */   public void error(String message, Throwable t) {
/*  551 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void exit() {
/*  559 */     exit(FQCN, (Object)null);
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
/*      */   public <R> R exit(R result) {
/*  571 */     return exit(FQCN, result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <R> R exit(String fqcn, R result) {
/*  582 */     if (isEnabled(Level.TRACE, EXIT_MARKER, (Object)null, (Throwable)null)) {
/*  583 */       logIfEnabled(fqcn, Level.TRACE, EXIT_MARKER, exitMsg(result), (Throwable)null);
/*      */     }
/*  585 */     return result;
/*      */   }
/*      */   
/*      */   protected Message exitMsg(Object result) {
/*  589 */     if (result == null) {
/*  590 */       return this.messageFactory.newMessage("exit");
/*      */     }
/*  592 */     return this.messageFactory.newMessage("exit with(" + result + ')');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Message msg) {
/*  603 */     logIfEnabled(FQCN, Level.FATAL, marker, msg, (Throwable)null);
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
/*      */   public void fatal(Marker marker, Message msg, Throwable t) {
/*  615 */     logIfEnabled(FQCN, Level.FATAL, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Object message) {
/*  626 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
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
/*      */   public void fatal(Marker marker, Object message, Throwable t) {
/*  639 */     logIfEnabled(FQCN, Level.FATAL, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message) {
/*  650 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
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
/*      */   public void fatal(Marker marker, String message, Object... params) {
/*  662 */     logIfEnabled(FQCN, Level.FATAL, marker, message, params);
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
/*      */   public void fatal(Marker marker, String message, Throwable t) {
/*  675 */     logIfEnabled(FQCN, Level.FATAL, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Message msg) {
/*  685 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Message msg, Throwable t) {
/*  696 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Object message) {
/*  706 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
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
/*      */   public void fatal(Object message, Throwable t) {
/*  718 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message) {
/*  728 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object... params) {
/*  739 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, params);
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
/*      */   public void fatal(String message, Throwable t) {
/*  751 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageFactory getMessageFactory() {
/*  761 */     return this.messageFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  771 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Message msg) {
/*  782 */     logIfEnabled(FQCN, Level.INFO, marker, msg, (Throwable)null);
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
/*      */   public void info(Marker marker, Message msg, Throwable t) {
/*  794 */     logIfEnabled(FQCN, Level.INFO, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Object message) {
/*  805 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
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
/*      */   public void info(Marker marker, Object message, Throwable t) {
/*  818 */     logIfEnabled(FQCN, Level.INFO, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message) {
/*  829 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
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
/*      */   public void info(Marker marker, String message, Object... params) {
/*  841 */     logIfEnabled(FQCN, Level.INFO, marker, message, params);
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
/*      */   public void info(Marker marker, String message, Throwable t) {
/*  854 */     logIfEnabled(FQCN, Level.INFO, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Message msg) {
/*  864 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Message msg, Throwable t) {
/*  875 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Object message) {
/*  885 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
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
/*      */   public void info(Object message, Throwable t) {
/*  897 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message) {
/*  907 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object... params) {
/*  918 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, params);
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
/*      */   public void info(String message, Throwable t) {
/*  930 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled() {
/*  940 */     return isEnabled(Level.DEBUG, (Marker)null, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled(Marker marker) {
/*  951 */     return isEnabled(Level.DEBUG, marker, (Object)null, (Throwable)null);
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
/*      */   public boolean isEnabled(Level level) {
/*  965 */     return isEnabled(level, (Marker)null, (Object)null, (Throwable)null);
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
/*      */   public boolean isEnabled(Level level, Marker marker) {
/*  980 */     return isEnabled(level, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isErrorEnabled() {
/*  991 */     return isEnabled(Level.ERROR, (Marker)null, (Object)null, (Throwable)null);
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
/*      */   public boolean isErrorEnabled(Marker marker) {
/* 1003 */     return isEnabled(Level.ERROR, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled() {
/* 1013 */     return isEnabled(Level.FATAL, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled(Marker marker) {
/* 1024 */     return isEnabled(Level.FATAL, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled() {
/* 1034 */     return isEnabled(Level.INFO, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled(Marker marker) {
/* 1045 */     return isEnabled(Level.INFO, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled() {
/* 1055 */     return isEnabled(Level.TRACE, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled(Marker marker) {
/* 1066 */     return isEnabled(Level.TRACE, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled() {
/* 1076 */     return isEnabled(Level.WARN, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled(Marker marker) {
/* 1087 */     return isEnabled(Level.WARN, marker, (Object)null, (Throwable)null);
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
/*      */   public void log(Level level, Marker marker, Message msg) {
/* 1099 */     logIfEnabled(FQCN, level, marker, msg, (Throwable)null);
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
/*      */   public void log(Level level, Marker marker, Message msg, Throwable t) {
/* 1112 */     logIfEnabled(FQCN, level, marker, msg, t);
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
/*      */   public void log(Level level, Marker marker, Object message) {
/* 1124 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
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
/*      */   public void log(Level level, Marker marker, Object message, Throwable t) {
/* 1138 */     if (isEnabled(level, marker, message, t)) {
/* 1139 */       logMessage(FQCN, level, marker, message, t);
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
/*      */   public void log(Level level, Marker marker, String message) {
/* 1152 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
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
/*      */   public void log(Level level, Marker marker, String message, Object... params) {
/* 1165 */     logIfEnabled(FQCN, level, marker, message, params);
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
/*      */   public void log(Level level, Marker marker, String message, Throwable t) {
/* 1179 */     logIfEnabled(FQCN, level, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Message msg) {
/* 1190 */     logIfEnabled(FQCN, level, (Marker)null, msg, (Throwable)null);
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
/*      */   public void log(Level level, Message msg, Throwable t) {
/* 1202 */     logIfEnabled(FQCN, level, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Object message) {
/* 1213 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
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
/*      */   public void log(Level level, Object message, Throwable t) {
/* 1226 */     logIfEnabled(FQCN, level, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message) {
/* 1237 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
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
/*      */   public void log(Level level, String message, Object... params) {
/* 1249 */     logIfEnabled(FQCN, level, (Marker)null, message, params);
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
/*      */   public void log(Level level, String message, Throwable t) {
/* 1262 */     logIfEnabled(FQCN, level, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Message msg, Throwable t) {
/* 1273 */     if (isEnabled(level, marker, msg, t)) {
/* 1274 */       logMessage(fqcn, level, marker, msg, t);
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
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable t) {
/* 1286 */     if (isEnabled(level, marker, message, t)) {
/* 1287 */       logMessage(fqcn, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
/* 1298 */     if (isEnabled(level, marker, message)) {
/* 1299 */       logMessage(fqcn, level, marker, message);
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
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 1311 */     if (isEnabled(level, marker, message, params)) {
/* 1312 */       logMessage(fqcn, level, marker, message, params);
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
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable t) {
/* 1324 */     if (isEnabled(level, marker, message, t)) {
/* 1325 */       logMessage(fqcn, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, Object message, Throwable t) {
/* 1331 */     logMessage(fqcn, level, marker, this.messageFactory.newMessage(message), t);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Throwable t) {
/* 1336 */     logMessage(fqcn, level, marker, this.messageFactory.newMessage(message), t);
/*      */   }
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message) {
/* 1340 */     Message msg = this.messageFactory.newMessage(message);
/* 1341 */     logMessage(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 1346 */     Message msg = this.messageFactory.newMessage(message, params);
/* 1347 */     logMessage(fqcn, level, marker, msg, msg.getThrowable());
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
/*      */   public void printf(Level level, Marker marker, String format, Object... params) {
/* 1360 */     if (isEnabled(level, marker, format, params)) {
/* 1361 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 1362 */       logMessage(FQCN, level, marker, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
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
/*      */   public void printf(Level level, String format, Object... params) {
/* 1375 */     if (isEnabled(level, (Marker)null, format, params)) {
/* 1376 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 1377 */       logMessage(FQCN, level, (Marker)null, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
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
/*      */   public <T extends Throwable> T throwing(T t) {
/* 1390 */     return throwing(FQCN, Level.ERROR, t);
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
/*      */   public <T extends Throwable> T throwing(Level level, T t) {
/* 1403 */     return throwing(FQCN, level, t);
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
/*      */   protected <T extends Throwable> T throwing(String fqcn, Level level, T t) {
/* 1415 */     if (isEnabled(level, THROWING_MARKER, (Object)null, (Throwable)null)) {
/* 1416 */       logMessage(fqcn, level, THROWING_MARKER, throwingMsg((Throwable)t), (Throwable)t);
/*      */     }
/* 1418 */     return t;
/*      */   }
/*      */   
/*      */   protected Message throwingMsg(Throwable t) {
/* 1422 */     return this.messageFactory.newMessage("throwing");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Message msg) {
/* 1433 */     logIfEnabled(FQCN, Level.TRACE, marker, msg, (Throwable)null);
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
/*      */   public void trace(Marker marker, Message msg, Throwable t) {
/* 1445 */     logIfEnabled(FQCN, Level.TRACE, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Object message) {
/* 1456 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
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
/*      */   public void trace(Marker marker, Object message, Throwable t) {
/* 1473 */     logIfEnabled(FQCN, Level.TRACE, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message) {
/* 1484 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
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
/*      */   public void trace(Marker marker, String message, Object... params) {
/* 1496 */     logIfEnabled(FQCN, Level.TRACE, marker, message, params);
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
/*      */   public void trace(Marker marker, String message, Throwable t) {
/* 1513 */     logIfEnabled(FQCN, Level.TRACE, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Message msg) {
/* 1523 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Message msg, Throwable t) {
/* 1534 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Object message) {
/* 1544 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
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
/*      */   public void trace(Object message, Throwable t) {
/* 1560 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message) {
/* 1570 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object... params) {
/* 1581 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, params);
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
/*      */   public void trace(String message, Throwable t) {
/* 1597 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Message msg) {
/* 1608 */     logIfEnabled(FQCN, Level.WARN, marker, msg, (Throwable)null);
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
/*      */   public void warn(Marker marker, Message msg, Throwable t) {
/* 1620 */     logIfEnabled(FQCN, Level.WARN, marker, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Object message) {
/* 1631 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
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
/*      */   public void warn(Marker marker, Object message, Throwable t) {
/* 1649 */     logIfEnabled(FQCN, Level.WARN, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message) {
/* 1660 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
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
/*      */   public void warn(Marker marker, String message, Object... params) {
/* 1672 */     logIfEnabled(FQCN, Level.WARN, marker, message, params);
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
/*      */   public void warn(Marker marker, String message, Throwable t) {
/* 1685 */     logIfEnabled(FQCN, Level.WARN, marker, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Message msg) {
/* 1695 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msg, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Message msg, Throwable t) {
/* 1706 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Object message) {
/* 1716 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
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
/*      */   public void warn(Object message, Throwable t) {
/* 1728 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message) {
/* 1738 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object... params) {
/* 1749 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, params);
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
/*      */   public void warn(String message, Throwable t) {
/* 1761 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, t);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\AbstractLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */