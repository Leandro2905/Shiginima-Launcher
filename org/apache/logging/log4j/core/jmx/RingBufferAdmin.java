/*    */ package org.apache.logging.log4j.core.jmx;
/*    */ 
/*    */ import com.lmax.disruptor.RingBuffer;
/*    */ import javax.management.ObjectName;
/*    */ import org.apache.logging.log4j.core.util.Assert;
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
/*    */ public class RingBufferAdmin
/*    */   implements RingBufferAdminMBean
/*    */ {
/*    */   private final RingBuffer<?> ringBuffer;
/*    */   private final ObjectName objectName;
/*    */   
/*    */   public static RingBufferAdmin forAsyncLogger(RingBuffer<?> ringBuffer, String contextName) {
/* 34 */     String ctxName = Server.escape(contextName);
/* 35 */     String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { ctxName });
/* 36 */     return new RingBufferAdmin(ringBuffer, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public static RingBufferAdmin forAsyncLoggerConfig(RingBuffer<?> ringBuffer, String contextName, String configName) {
/* 41 */     String ctxName = Server.escape(contextName);
/* 42 */     String cfgName = Server.escape(configName);
/* 43 */     String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { ctxName, cfgName });
/* 44 */     return new RingBufferAdmin(ringBuffer, name);
/*    */   }
/*    */   
/*    */   protected RingBufferAdmin(RingBuffer<?> ringBuffer, String mbeanName) {
/* 48 */     this.ringBuffer = (RingBuffer)Assert.requireNonNull(ringBuffer, "ringbuffer");
/*    */     try {
/* 50 */       this.objectName = new ObjectName(mbeanName);
/* 51 */     } catch (Exception e) {
/* 52 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long getBufferSize() {
/* 58 */     return this.ringBuffer.getBufferSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getRemainingCapacity() {
/* 63 */     return this.ringBuffer.remainingCapacity();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectName getObjectName() {
/* 74 */     return this.objectName;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\RingBufferAdmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */