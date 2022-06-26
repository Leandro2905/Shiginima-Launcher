/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.jivesoftware.smack.filter.StanzaFilter;
/*     */ import org.jivesoftware.smack.packet.Stanza;
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
/*     */ public class PacketCollector
/*     */ {
/*     */   private final StanzaFilter packetFilter;
/*     */   private final ArrayBlockingQueue<Stanza> resultQueue;
/*     */   private final PacketCollector collectorToReset;
/*     */   private final XMPPConnection connection;
/*     */   private boolean cancelled = false;
/*     */   private volatile long waitStart;
/*     */   
/*     */   protected PacketCollector(XMPPConnection connection, Configuration configuration) {
/*  65 */     this.connection = connection;
/*  66 */     this.packetFilter = configuration.packetFilter;
/*  67 */     this.resultQueue = new ArrayBlockingQueue<>(configuration.size);
/*  68 */     this.collectorToReset = configuration.collectorToReset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  78 */     if (!this.cancelled) {
/*  79 */       this.cancelled = true;
/*  80 */       this.connection.removePacketCollector(this);
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
/*     */   @Deprecated
/*     */   public StanzaFilter getPacketFilter() {
/*  93 */     return getStanzaFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StanzaFilter getStanzaFilter() {
/* 103 */     return this.packetFilter;
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
/*     */   public <P extends Stanza> P pollResult() {
/* 116 */     return (P)this.resultQueue.poll();
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
/*     */   public <P extends Stanza> P pollResultOrThrow() throws XMPPException.XMPPErrorException {
/* 131 */     P result = pollResult();
/* 132 */     if (result != null) {
/* 133 */       XMPPException.XMPPErrorException.ifHasErrorThenThrow((Stanza)result);
/*     */     }
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <P extends Stanza> P nextResultBlockForever() throws InterruptedException {
/*     */     Stanza stanza;
/* 147 */     throwIfCancelled();
/* 148 */     P res = null;
/* 149 */     while (res == null) {
/* 150 */       stanza = this.resultQueue.take();
/*     */     }
/* 152 */     return (P)stanza;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <P extends Stanza> P nextResult() throws InterruptedException {
/* 163 */     return nextResult(this.connection.getPacketReplyTimeout());
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
/*     */   public <P extends Stanza> P nextResult(long timeout) throws InterruptedException {
/* 179 */     throwIfCancelled();
/* 180 */     P res = null;
/* 181 */     long remainingWait = timeout;
/* 182 */     this.waitStart = System.currentTimeMillis();
/*     */     while (true) {
/* 184 */       Stanza stanza = this.resultQueue.poll(remainingWait, TimeUnit.MILLISECONDS);
/* 185 */       if (stanza != null) {
/* 186 */         return (P)stanza;
/*     */       }
/* 188 */       remainingWait = timeout - System.currentTimeMillis() - this.waitStart;
/* 189 */       if (remainingWait <= 0L) {
/* 190 */         return null;
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
/*     */   public <P extends Stanza> P nextResultOrThrow() throws SmackException.NoResponseException, XMPPException.XMPPErrorException, InterruptedException {
/* 204 */     return nextResultOrThrow(this.connection.getPacketReplyTimeout());
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
/*     */   public <P extends Stanza> P nextResultOrThrow(long timeout) throws SmackException.NoResponseException, XMPPException.XMPPErrorException, InterruptedException {
/* 218 */     P result = nextResult(timeout);
/* 219 */     cancel();
/* 220 */     if (result == null) {
/* 221 */       throw SmackException.NoResponseException.newWith(this.connection, this);
/*     */     }
/*     */     
/* 224 */     XMPPException.XMPPErrorException.ifHasErrorThenThrow((Stanza)result);
/*     */     
/* 226 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCollectedCount() {
/* 236 */     return this.resultQueue.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processPacket(Stanza packet) {
/* 246 */     if (this.packetFilter == null || this.packetFilter.accept(packet)) {
/* 247 */       while (!this.resultQueue.offer(packet))
/*     */       {
/* 249 */         this.resultQueue.poll();
/*     */       }
/* 251 */       if (this.collectorToReset != null) {
/* 252 */         this.collectorToReset.waitStart = System.currentTimeMillis();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private final void throwIfCancelled() {
/* 258 */     if (this.cancelled) {
/* 259 */       throw new IllegalStateException("Packet collector already cancelled");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Configuration newConfiguration() {
/* 269 */     return new Configuration();
/*     */   }
/*     */   
/*     */   public static class Configuration {
/*     */     private StanzaFilter packetFilter;
/* 274 */     private int size = SmackConfiguration.getPacketCollectorSize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PacketCollector collectorToReset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Configuration setPacketFilter(StanzaFilter packetFilter) {
/* 290 */       return setStanzaFilter(packetFilter);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration setStanzaFilter(StanzaFilter stanzaFilter) {
/* 301 */       this.packetFilter = stanzaFilter;
/* 302 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration setSize(int size) {
/* 313 */       this.size = size;
/* 314 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration setCollectorToReset(PacketCollector collector) {
/* 325 */       this.collectorToReset = collector;
/* 326 */       return this;
/*     */     }
/*     */     
/*     */     private Configuration() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\PacketCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */