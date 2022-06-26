/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.packet.StreamError;
/*     */ import org.jivesoftware.smack.util.Async;
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
/*     */ public class ReconnectionManager
/*     */ {
/*  44 */   private static final Logger LOGGER = Logger.getLogger(ReconnectionManager.class.getName());
/*     */   
/*  46 */   private static final Map<AbstractXMPPConnection, ReconnectionManager> INSTANCES = new WeakHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized ReconnectionManager getInstanceFor(AbstractXMPPConnection connection) {
/*  55 */     ReconnectionManager reconnectionManager = INSTANCES.get(connection);
/*  56 */     if (reconnectionManager == null) {
/*  57 */       reconnectionManager = new ReconnectionManager(connection);
/*  58 */       INSTANCES.put(connection, reconnectionManager);
/*     */     } 
/*  60 */     return reconnectionManager;
/*     */   }
/*     */   
/*     */   static {
/*  64 */     XMPPConnectionRegistry.addConnectionCreationListener(new ConnectionCreationListener() {
/*     */           public void connectionCreated(XMPPConnection connection) {
/*  66 */             if (connection instanceof AbstractXMPPConnection) {
/*  67 */               ReconnectionManager.getInstanceFor((AbstractXMPPConnection)connection);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean enabledPerDefault = false;
/*     */ 
/*     */   
/*     */   private final WeakReference<AbstractXMPPConnection> weakRefConnection;
/*     */ 
/*     */   
/*     */   public static void setEnabledPerDefault(boolean enabled) {
/*  82 */     enabledPerDefault = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getEnabledPerDefault() {
/*  91 */     return enabledPerDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  96 */   private final int randomBase = (new Random()).nextInt(13) + 2;
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
/*     */   private final Runnable reconnectionRunnable;
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
/*     */   private boolean automaticReconnectEnabled = false;
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
/*     */   boolean done = false;
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
/*     */   private Thread reconnectionThread;
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
/*     */   private final ConnectionListener connectionListener;
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
/*     */   public synchronized void enableAutomaticReconnection() {
/* 194 */     if (this.automaticReconnectEnabled) {
/*     */       return;
/*     */     }
/* 197 */     XMPPConnection connection = this.weakRefConnection.get();
/* 198 */     if (connection == null) {
/* 199 */       throw new IllegalStateException("Connection instance no longer available");
/*     */     }
/* 201 */     connection.addConnectionListener(this.connectionListener);
/* 202 */     this.automaticReconnectEnabled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void disableAutomaticReconnection() {
/* 209 */     if (!this.automaticReconnectEnabled) {
/*     */       return;
/*     */     }
/* 212 */     XMPPConnection connection = this.weakRefConnection.get();
/* 213 */     if (connection == null) {
/* 214 */       throw new IllegalStateException("Connection instance no longer available");
/*     */     }
/* 216 */     connection.removeConnectionListener(this.connectionListener);
/* 217 */     this.automaticReconnectEnabled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutomaticReconnectEnabled() {
/* 227 */     return this.automaticReconnectEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isReconnectionPossible(XMPPConnection connection) {
/* 236 */     return (!this.done && !connection.isConnected() && isAutomaticReconnectEnabled());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void reconnect() {
/* 245 */     XMPPConnection connection = this.weakRefConnection.get();
/* 246 */     if (connection == null) {
/* 247 */       LOGGER.fine("Connection is null, will not reconnect");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 253 */     if (this.reconnectionThread != null && this.reconnectionThread.isAlive()) {
/*     */       return;
/*     */     }
/* 256 */     this.reconnectionThread = Async.go(this.reconnectionRunnable, "Smack Reconnection Manager (" + connection.getConnectionCounter() + ')');
/*     */   }
/*     */   
/*     */   private ReconnectionManager(AbstractXMPPConnection connection) {
/* 260 */     this.connectionListener = new AbstractConnectionListener()
/*     */       {
/*     */         public void connectionClosed()
/*     */         {
/* 264 */           ReconnectionManager.this.done = true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void authenticated(XMPPConnection connection, boolean resumed) {
/* 269 */           ReconnectionManager.this.done = false;
/*     */         }
/*     */ 
/*     */         
/*     */         public void connectionClosedOnError(Exception e) {
/* 274 */           ReconnectionManager.this.done = false;
/* 275 */           if (!ReconnectionManager.this.isAutomaticReconnectEnabled()) {
/*     */             return;
/*     */           }
/* 278 */           if (e instanceof XMPPException.StreamErrorException) {
/* 279 */             XMPPException.StreamErrorException xmppEx = (XMPPException.StreamErrorException)e;
/* 280 */             StreamError error = xmppEx.getStreamError();
/*     */             
/* 282 */             if (StreamError.Condition.conflict == error.getCondition()) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           
/* 287 */           ReconnectionManager.this.reconnect();
/*     */         }
/*     */       };
/*     */     this.weakRefConnection = new WeakReference<>(connection);
/*     */     this.reconnectionRunnable = new Thread() {
/*     */         private int attempts = 0;
/*     */         
/*     */         private int timeDelay() {
/*     */           this.attempts++;
/*     */           if (this.attempts > 13)
/*     */             return ReconnectionManager.this.randomBase * 6 * 5; 
/*     */           if (this.attempts > 7)
/*     */             return ReconnectionManager.this.randomBase * 6; 
/*     */           return ReconnectionManager.this.randomBase;
/*     */         }
/*     */         
/*     */         public void run() {
/*     */           AbstractXMPPConnection connection = ReconnectionManager.this.weakRefConnection.get();
/*     */           if (connection == null)
/*     */             return; 
/*     */           while (ReconnectionManager.this.isReconnectionPossible(connection)) {
/*     */             int remainingSeconds = timeDelay();
/*     */             while (ReconnectionManager.this.isReconnectionPossible(connection) && remainingSeconds > 0) {
/*     */               try {
/*     */                 Thread.sleep(1000L);
/*     */                 remainingSeconds--;
/*     */                 for (ConnectionListener listener : connection.connectionListeners)
/*     */                   listener.reconnectingIn(remainingSeconds); 
/*     */               } catch (InterruptedException e) {
/*     */                 ReconnectionManager.LOGGER.log(Level.FINE, "waiting for reconnection interrupted", e);
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */             for (ConnectionListener listener : connection.connectionListeners)
/*     */               listener.reconnectingIn(0); 
/*     */             try {
/*     */               if (ReconnectionManager.this.isReconnectionPossible(connection))
/*     */                 connection.connect(); 
/*     */             } catch (Exception e) {
/*     */               for (ConnectionListener listener : connection.connectionListeners)
/*     */                 listener.reconnectionFailed(e); 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */     if (getEnabledPerDefault())
/*     */       enableAutomaticReconnection(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\ReconnectionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */