/*     */ package org.apache.logging.log4j.core.appender.db.jpa;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.persistence.EntityTransaction;
/*     */ import javax.persistence.Persistence;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JpaDatabaseManager
/*     */   extends AbstractDatabaseManager
/*     */ {
/*  35 */   private static final JPADatabaseManagerFactory FACTORY = new JPADatabaseManagerFactory();
/*     */   
/*     */   private final String entityClassName;
/*     */   
/*     */   private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
/*     */   
/*     */   private final String persistenceUnitName;
/*     */   
/*     */   private EntityManagerFactory entityManagerFactory;
/*     */   
/*     */   private EntityManager entityManager;
/*     */   
/*     */   private EntityTransaction transaction;
/*     */   
/*     */   private JpaDatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/*  50 */     super(name, bufferSize);
/*  51 */     this.entityClassName = entityClass.getName();
/*  52 */     this.entityConstructor = entityConstructor;
/*  53 */     this.persistenceUnitName = persistenceUnitName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startupInternal() {
/*  58 */     this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void shutdownInternal() {
/*  63 */     if (this.entityManager != null || this.transaction != null) {
/*  64 */       commitAndClose();
/*     */     }
/*  66 */     if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
/*  67 */       this.entityManagerFactory.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/*     */     try {
/*  74 */       this.entityManager = this.entityManagerFactory.createEntityManager();
/*  75 */       this.transaction = this.entityManager.getTransaction();
/*  76 */       this.transaction.begin();
/*  77 */     } catch (Exception e) {
/*  78 */       throw new AppenderLoggingException("Cannot write logging event or flush buffer; manager cannot create EntityManager or transaction.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event) {
/*     */     AbstractLogEventWrapperEntity entity;
/*  86 */     if (!isRunning() || this.entityManagerFactory == null || this.entityManager == null || this.transaction == null)
/*     */     {
/*  88 */       throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  94 */       entity = this.entityConstructor.newInstance(new Object[] { event });
/*  95 */     } catch (Exception e) {
/*  96 */       throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", e);
/*     */     } 
/*     */     
/*     */     try {
/* 100 */       this.entityManager.persist(entity);
/* 101 */     } catch (Exception e) {
/* 102 */       if (this.transaction != null && this.transaction.isActive()) {
/* 103 */         this.transaction.rollback();
/* 104 */         this.transaction = null;
/*     */       } 
/* 106 */       throw new AppenderLoggingException("Failed to insert record for log event in JPA manager: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void commitAndClose() {
/*     */     try {
/* 114 */       if (this.transaction != null && this.transaction.isActive()) {
/* 115 */         this.transaction.commit();
/*     */       }
/* 117 */     } catch (Exception e) {
/* 118 */       if (this.transaction != null && this.transaction.isActive()) {
/* 119 */         this.transaction.rollback();
/*     */       }
/*     */     } finally {
/* 122 */       this.transaction = null;
/*     */       try {
/* 124 */         if (this.entityManager != null && this.entityManager.isOpen()) {
/* 125 */           this.entityManager.close();
/*     */         }
/* 127 */       } catch (Exception e) {
/* 128 */         LOGGER.warn("Failed to close entity manager while logging event or flushing buffer.", e);
/*     */       } finally {
/* 130 */         this.entityManager = null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static JpaDatabaseManager getJPADatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/* 153 */     return (JpaDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, entityClass, entityConstructor, persistenceUnitName), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
/*     */     
/*     */     private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
/*     */     
/*     */     private final String persistenceUnitName;
/*     */ 
/*     */     
/*     */     protected FactoryData(int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/* 169 */       super(bufferSize);
/*     */       
/* 171 */       this.entityClass = entityClass;
/* 172 */       this.entityConstructor = entityConstructor;
/* 173 */       this.persistenceUnitName = persistenceUnitName;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class JPADatabaseManagerFactory
/*     */     implements ManagerFactory<JpaDatabaseManager, FactoryData>
/*     */   {
/*     */     private JPADatabaseManagerFactory() {}
/*     */     
/*     */     public JpaDatabaseManager createManager(String name, JpaDatabaseManager.FactoryData data) {
/* 183 */       return new JpaDatabaseManager(name, data.getBufferSize(), data.entityClass, data.entityConstructor, data.persistenceUnitName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\JpaDatabaseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */