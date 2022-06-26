/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.util.JndiCloser;
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
/*    */ @Plugin(name = "jndi", category = "Lookup")
/*    */ public class JndiLookup
/*    */   implements StrLookup
/*    */ {
/*    */   static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";
/*    */   
/*    */   public String lookup(String key) {
/* 43 */     return lookup(null, key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 54 */     if (key == null) {
/* 55 */       return null;
/*    */     }
/*    */     
/* 58 */     Context ctx = null;
/*    */     try {
/* 60 */       ctx = new InitialContext();
/* 61 */       return (String)ctx.lookup(convertJndiName(key));
/* 62 */     } catch (NamingException e) {
/* 63 */       return null;
/*    */     } finally {
/* 65 */       JndiCloser.closeSilently(ctx);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String convertJndiName(String jndiName) {
/* 77 */     if (!jndiName.startsWith("java:comp/env/") && jndiName.indexOf(':') == -1) {
/* 78 */       jndiName = "java:comp/env/" + jndiName;
/*    */     }
/*    */     
/* 81 */     return jndiName;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\JndiLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */