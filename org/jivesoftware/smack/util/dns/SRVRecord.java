/*    */ package org.jivesoftware.smack.util.dns;
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
/*    */ public class SRVRecord
/*    */   extends HostAddress
/*    */   implements Comparable<SRVRecord>
/*    */ {
/*    */   private int weight;
/*    */   private int priority;
/*    */   
/*    */   public SRVRecord(String fqdn, int port, int priority, int weight) {
/* 40 */     super(fqdn, port);
/* 41 */     if (weight < 0 || weight > 65535) {
/* 42 */       throw new IllegalArgumentException("DNS SRV records weight must be a 16-bit unsiged integer (i.e. between 0-65535. Weight was: " + weight);
/*    */     }
/*    */ 
/*    */     
/* 46 */     if (priority < 0 || priority > 65535) {
/* 47 */       throw new IllegalArgumentException("DNS SRV records priority must be a 16-bit unsiged integer (i.e. between 0-65535. Priority was: " + priority);
/*    */     }
/*    */ 
/*    */     
/* 51 */     this.priority = priority;
/* 52 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPriority() {
/* 57 */     return this.priority;
/*    */   }
/*    */   
/*    */   public int getWeight() {
/* 61 */     return this.weight;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(SRVRecord other) {
/* 69 */     int res = other.priority - this.priority;
/* 70 */     if (res == 0) {
/* 71 */       res = this.weight - other.weight;
/*    */     }
/* 73 */     return res;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return super.toString() + " prio:" + this.priority + ":w:" + this.weight;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\dns\SRVRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */