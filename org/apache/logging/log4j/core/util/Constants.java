/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Constants
/*    */ {
/*    */   public static final String LOG4J_LOG_EVENT_FACTORY = "Log4jLogEventFactory";
/*    */   public static final String LOG4J_CONTEXT_SELECTOR = "Log4jContextSelector";
/*    */   public static final String LOG4J_DEFAULT_STATUS_LEVEL = "Log4jDefaultStatusLevel";
/*    */   public static final String JNDI_CONTEXT_NAME = "java:comp/env/log4j/context-name";
/* 46 */   public static final String LINE_SEPARATOR = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
/*    */   public static final int MILLIS_IN_SECONDS = 1000;
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */