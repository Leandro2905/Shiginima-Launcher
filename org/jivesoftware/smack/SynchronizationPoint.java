/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.packet.PlainStreamElement;
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jivesoftware.smack.packet.TopLevelStreamElement;
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
/*     */ public class SynchronizationPoint<E extends Exception>
/*     */ {
/*  33 */   private static final Logger LOGGER = Logger.getLogger(SynchronizationPoint.class.getName());
/*     */   
/*     */   private final AbstractXMPPConnection connection;
/*     */   
/*     */   private final Lock connectionLock;
/*     */   
/*     */   private final Condition condition;
/*     */   
/*     */   private State state;
/*     */   private E failureException;
/*     */   
/*     */   public SynchronizationPoint(AbstractXMPPConnection connection) {
/*  45 */     this.connection = connection;
/*  46 */     this.connectionLock = connection.getConnectionLock();
/*  47 */     this.condition = connection.getConnectionLock().newCondition();
/*  48 */     init();
/*     */   }
/*     */   
/*     */   public void init() {
/*  52 */     this.connectionLock.lock();
/*  53 */     this.state = State.Initial;
/*  54 */     this.failureException = null;
/*  55 */     this.connectionLock.unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendAndWaitForResponse(TopLevelStreamElement request) throws SmackException.NoResponseException, SmackException.NotConnectedException, InterruptedException {
/*  60 */     assert this.state == State.Initial;
/*  61 */     this.connectionLock.lock();
/*     */     try {
/*  63 */       if (request != null) {
/*  64 */         if (request instanceof Stanza) {
/*  65 */           this.connection.sendStanza((Stanza)request);
/*     */         }
/*  67 */         else if (request instanceof PlainStreamElement) {
/*  68 */           this.connection.send((PlainStreamElement)request);
/*     */         } else {
/*  70 */           throw new IllegalStateException("Unsupported element type");
/*     */         } 
/*  72 */         this.state = State.RequestSent;
/*     */       } 
/*  74 */       waitForConditionOrTimeout();
/*     */     } finally {
/*     */       
/*  77 */       this.connectionLock.unlock();
/*     */     } 
/*  79 */     checkForResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendAndWaitForResponseOrThrow(PlainStreamElement request) throws E, SmackException.NoResponseException, SmackException.NotConnectedException, InterruptedException {
/*  84 */     sendAndWaitForResponse((TopLevelStreamElement)request);
/*  85 */     switch (this.state) {
/*     */       case Failure:
/*  87 */         if (this.failureException != null) {
/*  88 */           throw this.failureException;
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkIfSuccessOrWaitOrThrow() throws SmackException.NoResponseException, E {
/*  97 */     checkIfSuccessOrWait();
/*  98 */     if (this.state == State.Failure) {
/*  99 */       throw this.failureException;
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkIfSuccessOrWait() throws SmackException.NoResponseException {
/* 104 */     this.connectionLock.lock();
/*     */     try {
/* 106 */       if (this.state == State.Success) {
/*     */         return;
/*     */       }
/*     */       
/* 110 */       waitForConditionOrTimeout();
/*     */     } finally {
/* 112 */       this.connectionLock.unlock();
/*     */     } 
/* 114 */     checkForResponse();
/*     */   }
/*     */   
/*     */   public void reportSuccess() {
/* 118 */     this.connectionLock.lock();
/*     */     try {
/* 120 */       this.state = State.Success;
/* 121 */       this.condition.signal();
/*     */     } finally {
/*     */       
/* 124 */       this.connectionLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reportFailure() {
/* 129 */     reportFailure(null);
/*     */   }
/*     */   
/*     */   public void reportFailure(E failureException) {
/* 133 */     this.connectionLock.lock();
/*     */     try {
/* 135 */       this.state = State.Failure;
/* 136 */       this.failureException = failureException;
/* 137 */       this.condition.signal();
/*     */     } finally {
/*     */       
/* 140 */       this.connectionLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean wasSuccessful() {
/* 145 */     this.connectionLock.lock();
/*     */     try {
/* 147 */       return (this.state == State.Success);
/*     */     } finally {
/*     */       
/* 150 */       this.connectionLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean requestSent() {
/* 155 */     this.connectionLock.lock();
/*     */     try {
/* 157 */       return (this.state == State.RequestSent);
/*     */     } finally {
/*     */       
/* 160 */       this.connectionLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void waitForConditionOrTimeout() {
/* 165 */     long remainingWait = TimeUnit.MILLISECONDS.toNanos(this.connection.getPacketReplyTimeout());
/* 166 */     while (this.state == State.RequestSent || this.state == State.Initial) {
/*     */       try {
/* 168 */         remainingWait = this.condition.awaitNanos(remainingWait);
/*     */         
/* 170 */         if (remainingWait <= 0L) {
/* 171 */           this.state = State.NoResponse;
/*     */           break;
/*     */         } 
/* 174 */       } catch (InterruptedException e) {
/*     */         
/* 176 */         LOGGER.log(Level.WARNING, "Thread interrupt while waiting for condition or timeout ignored", e);
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
/*     */   private void checkForResponse() throws SmackException.NoResponseException {
/* 189 */     switch (this.state) {
/*     */       case Initial:
/*     */       case NoResponse:
/*     */       case RequestSent:
/* 193 */         throw SmackException.NoResponseException.newWith(this.connection);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 201 */     Initial,
/* 202 */     RequestSent,
/* 203 */     NoResponse,
/* 204 */     Success,
/* 205 */     Failure;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\SynchronizationPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */