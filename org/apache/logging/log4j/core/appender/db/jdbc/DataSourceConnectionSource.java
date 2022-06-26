/*    */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import javax.sql.DataSource;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ @Plugin(name = "DataSource", category = "Core", elementType = "connectionSource", printObject = true)
/*    */ public final class DataSourceConnectionSource
/*    */   implements ConnectionSource
/*    */ {
/* 38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final DataSource dataSource;
/*    */   private final String description;
/*    */   
/*    */   private DataSourceConnectionSource(String dataSourceName, DataSource dataSource) {
/* 44 */     this.dataSource = dataSource;
/* 45 */     this.description = "dataSource{ name=" + dataSourceName + ", value=" + dataSource + " }";
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection() throws SQLException {
/* 50 */     return this.dataSource.getConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return this.description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") String jndiName) {
/* 67 */     if (Strings.isEmpty(jndiName)) {
/* 68 */       LOGGER.error("No JNDI name provided.");
/* 69 */       return null;
/*    */     } 
/*    */     
/*    */     try {
/* 73 */       InitialContext context = new InitialContext();
/* 74 */       DataSource dataSource = (DataSource)context.lookup(jndiName);
/* 75 */       if (dataSource == null) {
/* 76 */         LOGGER.error("No data source found with JNDI name [" + jndiName + "].");
/* 77 */         return null;
/*    */       } 
/*    */       
/* 80 */       return new DataSourceConnectionSource(jndiName, dataSource);
/* 81 */     } catch (NamingException e) {
/* 82 */       LOGGER.error(e.getMessage(), e);
/* 83 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jdbc\DataSourceConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */