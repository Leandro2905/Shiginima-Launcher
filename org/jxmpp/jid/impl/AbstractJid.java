/*     */ package org.jxmpp.jid.impl;
/*     */ 
/*     */ import org.jxmpp.jid.BareJid;
/*     */ import org.jxmpp.jid.DomainFullJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.parts.Localpart;
/*     */ import org.jxmpp.jid.parts.Resourcepart;
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
/*     */ public abstract class AbstractJid
/*     */   implements Jid
/*     */ {
/*     */   public final boolean isBareOrFullJid() {
/*  33 */     return (isBareJid() || isFullJid());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isBareJid() {
/*  38 */     return this instanceof BareJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isFullJid() {
/*  43 */     return this instanceof FullJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDomainBareJid() {
/*  48 */     return this instanceof org.jxmpp.jid.DomainBareJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDomainFullJid() {
/*  53 */     return this instanceof DomainFullJid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasResource() {
/*  61 */     return this instanceof org.jxmpp.jid.JidWithResource;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasLocalpart() {
/*  66 */     return this instanceof org.jxmpp.jid.JidWithLocalpart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T extends Jid> T downcast() {
/*  72 */     return (T)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/*  77 */     return toString().length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  82 */     return toString().charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/*  87 */     return toString().subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart getResourceOrNull() {
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart getLocalpartOrNull() {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isParentOf(Jid jid) {
/* 102 */     FullJid fullJid = jid.asFullJidIfPossible();
/* 103 */     if (fullJid != null) {
/* 104 */       return isParentOf(fullJid);
/*     */     }
/* 106 */     BareJid bareJid = jid.asBareJidIfPossible();
/* 107 */     if (bareJid != null) {
/* 108 */       return isParentOf(bareJid);
/*     */     }
/* 110 */     DomainFullJid domainFullJid = jid.asDomainFullJidIfPossible();
/* 111 */     if (domainFullJid != null) {
/* 112 */       return isParentOf(domainFullJid);
/*     */     }
/*     */     
/* 115 */     return isParentOf(jid.asDomainBareJid());
/*     */   }
/*     */ 
/*     */   
/*     */   public final String asDomainBareJidString() {
/* 120 */     return asDomainBareJid().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 125 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object other) {
/* 130 */     if (other == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     if (this == other) {
/* 134 */       return true;
/*     */     }
/* 136 */     if (other instanceof CharSequence) {
/* 137 */       return equals((CharSequence)other);
/*     */     }
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(CharSequence charSequence) {
/* 144 */     return equals(charSequence.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(String string) {
/* 149 */     return toString().equals(string);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int compareTo(Jid other) {
/* 154 */     String otherString = other.toString();
/* 155 */     String myString = toString();
/* 156 */     return myString.compareTo(otherString);
/*     */   }
/*     */   
/*     */   public abstract boolean hasNoResource();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\AbstractJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */