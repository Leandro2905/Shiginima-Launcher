/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JdbcDatabaseManager
/*     */   extends AbstractDatabaseManager
/*     */ {
/*  38 */   private static final JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManagerFactory();
/*     */   
/*     */   private final List<Column> columns;
/*     */   
/*     */   private final ConnectionSource connectionSource;
/*     */   
/*     */   private final String sqlStatement;
/*     */   private Connection connection;
/*     */   private PreparedStatement statement;
/*     */   
/*     */   private JdbcDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String sqlStatement, List<Column> columns) {
/*  49 */     super(name, bufferSize);
/*  50 */     this.connectionSource = connectionSource;
/*  51 */     this.sqlStatement = sqlStatement;
/*  52 */     this.columns = columns;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startupInternal() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutdownInternal() {
/*  62 */     if (this.connection != null || this.statement != null) {
/*  63 */       commitAndClose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/*     */     try {
/*  70 */       this.connection = this.connectionSource.getConnection();
/*  71 */       this.connection.setAutoCommit(false);
/*  72 */       this.statement = this.connection.prepareStatement(this.sqlStatement);
/*  73 */     } catch (SQLException e) {
/*  74 */       throw new AppenderLoggingException("Cannot write logging event or flush buffer; JDBC manager cannot connect to the database.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event) {
/*  82 */     StringReader reader = null;
/*     */     try {
/*  84 */       if (!isRunning() || this.connection == null || this.connection.isClosed() || this.statement == null || this.statement.isClosed())
/*     */       {
/*  86 */         throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
/*     */       }
/*     */ 
/*     */       
/*  90 */       int i = 1;
/*  91 */       for (Column column : this.columns) {
/*  92 */         if (column.isEventTimestamp) {
/*  93 */           this.statement.setTimestamp(i++, new Timestamp(event.getTimeMillis())); continue;
/*     */         } 
/*  95 */         if (column.isClob) {
/*  96 */           reader = new StringReader(column.layout.toSerializable(event));
/*  97 */           if (column.isUnicode) {
/*  98 */             this.statement.setNClob(i++, reader); continue;
/*     */           } 
/* 100 */           this.statement.setClob(i++, reader);
/*     */           continue;
/*     */         } 
/* 103 */         if (column.isUnicode) {
/* 104 */           this.statement.setNString(i++, column.layout.toSerializable(event)); continue;
/*     */         } 
/* 106 */         this.statement.setString(i++, column.layout.toSerializable(event));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (this.statement.executeUpdate() == 0) {
/* 113 */         throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
/*     */       }
/*     */     }
/* 116 */     catch (SQLException e) {
/* 117 */       throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
/*     */     } finally {
/*     */       
/* 120 */       Closer.closeSilently(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void commitAndClose() {
/*     */     try {
/* 127 */       if (this.connection != null && !this.connection.isClosed()) {
/* 128 */         this.connection.commit();
/*     */       }
/* 130 */     } catch (SQLException e) {
/* 131 */       throw new AppenderLoggingException("Failed to commit transaction logging event or flushing buffer.", e);
/*     */     } finally {
/*     */       try {
/* 134 */         Closer.close(this.statement);
/* 135 */       } catch (Exception e) {
/* 136 */         LOGGER.warn("Failed to close SQL statement logging event or flushing buffer.", e);
/*     */       } finally {
/* 138 */         this.statement = null;
/*     */       } 
/*     */       
/*     */       try {
/* 142 */         Closer.close(this.connection);
/* 143 */       } catch (Exception e) {
/* 144 */         LOGGER.warn("Failed to close database connection logging event or flushing buffer.", e);
/*     */       } finally {
/* 146 */         this.connection = null;
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
/*     */ 
/*     */   
/*     */   public static JdbcDatabaseManager getJDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs) {
/* 166 */     return (JdbcDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final ColumnConfig[] columnConfigs;
/*     */     
/*     */     private final ConnectionSource connectionSource;
/*     */     
/*     */     private final String tableName;
/*     */ 
/*     */     
/*     */     protected FactoryData(int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs) {
/* 181 */       super(bufferSize);
/* 182 */       this.connectionSource = connectionSource;
/* 183 */       this.tableName = tableName;
/* 184 */       this.columnConfigs = columnConfigs;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class JDBCDatabaseManagerFactory
/*     */     implements ManagerFactory<JdbcDatabaseManager, FactoryData>
/*     */   {
/*     */     private JDBCDatabaseManagerFactory() {}
/*     */     
/*     */     public JdbcDatabaseManager createManager(String name, JdbcDatabaseManager.FactoryData data) {
/* 194 */       StringBuilder columnPart = new StringBuilder();
/* 195 */       StringBuilder valuePart = new StringBuilder();
/* 196 */       List<JdbcDatabaseManager.Column> columns = new ArrayList<JdbcDatabaseManager.Column>();
/* 197 */       int i = 0;
/* 198 */       for (ColumnConfig config : data.columnConfigs) {
/* 199 */         if (i++ > 0) {
/* 200 */           columnPart.append(',');
/* 201 */           valuePart.append(',');
/*     */         } 
/*     */         
/* 204 */         columnPart.append(config.getColumnName());
/*     */         
/* 206 */         if (config.getLiteralValue() != null) {
/* 207 */           valuePart.append(config.getLiteralValue());
/*     */         } else {
/* 209 */           columns.add(new JdbcDatabaseManager.Column(config.getLayout(), config.isEventTimestamp(), config.isUnicode(), config.isClob()));
/*     */ 
/*     */           
/* 212 */           valuePart.append('?');
/*     */         } 
/*     */       } 
/*     */       
/* 216 */       String sqlStatement = "INSERT INTO " + data.tableName + " (" + columnPart + ") VALUES (" + valuePart + ')';
/*     */ 
/*     */       
/* 219 */       return new JdbcDatabaseManager(name, data.getBufferSize(), data.connectionSource, sqlStatement, columns);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Column
/*     */   {
/*     */     private final PatternLayout layout;
/*     */     
/*     */     private final boolean isEventTimestamp;
/*     */     
/*     */     private final boolean isUnicode;
/*     */     private final boolean isClob;
/*     */     
/*     */     private Column(PatternLayout layout, boolean isEventDate, boolean isUnicode, boolean isClob) {
/* 234 */       this.layout = layout;
/* 235 */       this.isEventTimestamp = isEventDate;
/* 236 */       this.isUnicode = isUnicode;
/* 237 */       this.isClob = isClob;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcDatabaseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */