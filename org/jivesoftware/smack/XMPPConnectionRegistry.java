/*    */ package org.jivesoftware.smack;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*    */ public class XMPPConnectionRegistry
/*    */ {
/* 29 */   private static final Set<ConnectionCreationListener> connectionEstablishedListeners = new CopyOnWriteArraySet<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
/* 40 */     connectionEstablishedListeners.add(connectionCreationListener);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void removeConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
/* 50 */     connectionEstablishedListeners.remove(connectionCreationListener);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Collection<ConnectionCreationListener> getConnectionCreationListeners() {
/* 61 */     return Collections.unmodifiableCollection(connectionEstablishedListeners);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\XMPPConnectionRegistry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */