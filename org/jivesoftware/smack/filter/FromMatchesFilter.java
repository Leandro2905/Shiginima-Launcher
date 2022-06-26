/*     */ package org.jivesoftware.smack.filter;
/*     */ 
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jxmpp.jid.Jid;
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
/*     */ public class FromMatchesFilter
/*     */   implements StanzaFilter
/*     */ {
/*     */   private final Jid address;
/*     */   private final boolean ignoreResourcepart;
/*     */   
/*     */   public FromMatchesFilter(Jid address, boolean ignoreResourcepart) {
/*  50 */     if (address != null && ignoreResourcepart) {
/*  51 */       this.address = address.withoutResource();
/*     */     } else {
/*     */       
/*  54 */       this.address = address;
/*     */     } 
/*  56 */     this.ignoreResourcepart = ignoreResourcepart;
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
/*     */   public static FromMatchesFilter create(Jid address) {
/*  68 */     return new FromMatchesFilter(address, address.hasNoResource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FromMatchesFilter createBare(Jid address) {
/*  79 */     address = (address == null) ? null : address;
/*  80 */     return new FromMatchesFilter(address, true);
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
/*     */   public static FromMatchesFilter createFull(Jid address) {
/*  92 */     return new FromMatchesFilter(address, false);
/*     */   }
/*     */   
/*     */   public boolean accept(Stanza packet) {
/*  96 */     Jid from = packet.getFrom();
/*  97 */     if (from == null) {
/*  98 */       return (this.address == null);
/*     */     }
/*     */     
/* 101 */     if (this.ignoreResourcepart) {
/* 102 */       from = from.withoutResource();
/*     */     }
/* 104 */     return from.equals((CharSequence)this.address);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 108 */     String matchMode = this.ignoreResourcepart ? "ignoreResourcepart" : "full";
/* 109 */     return getClass().getSimpleName() + " (" + matchMode + "): " + this.address;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\FromMatchesFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */