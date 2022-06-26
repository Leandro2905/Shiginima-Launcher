/*     */ package org.jxmpp.jid.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.jxmpp.jid.BareJid;
/*     */ import org.jxmpp.jid.DomainFullJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.impl.JidCreate;
/*     */ import org.jxmpp.stringprep.XmppStringprepException;
/*     */ import org.jxmpp.util.XmppStringUtils;
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
/*     */ public class JidUtil
/*     */ {
/*     */   public static boolean isValidBareJid(CharSequence jid) {
/*     */     try {
/*  48 */       validateBareJid(jid);
/*  49 */     } catch (NotABareJidStringException|XmppStringprepException e) {
/*  50 */       return false;
/*     */     } 
/*  52 */     return true;
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
/*     */   public static BareJid validateBareJid(CharSequence jidcs) throws NotABareJidStringException, XmppStringprepException {
/*  72 */     String jid = jidcs.toString();
/*  73 */     int atIndex = jid.indexOf('@');
/*  74 */     if (atIndex == -1)
/*  75 */       throw new NotABareJidStringException("'" + jid + "' does not contain a '@' character"); 
/*  76 */     if (jid.indexOf('@', atIndex + 1) != -1) {
/*  77 */       throw new NotABareJidStringException("'" + jid + "' contains multiple '@' characters");
/*     */     }
/*  79 */     String localpart = XmppStringUtils.parseLocalpart(jid);
/*  80 */     if (localpart.length() == 0) {
/*  81 */       throw new NotABareJidStringException("'" + jid + "' has empty localpart");
/*     */     }
/*  83 */     String domainpart = XmppStringUtils.parseDomain(jid);
/*  84 */     if (domainpart.length() == 0) {
/*  85 */       throw new NotABareJidStringException("'" + jid + "' has empty domainpart");
/*     */     }
/*  87 */     return JidCreate.bareFrom(jid);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class NotABareJidStringException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = -1710386661031655082L;
/*     */     
/*     */     public NotABareJidStringException(String message) {
/*  97 */       super(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void filterBareJid(Collection<? extends Jid> in, Collection<BareJid> out) {
/* 102 */     for (Jid jid : in) {
/* 103 */       BareJid bareJid = jid.asBareJidIfPossible();
/* 104 */       if (bareJid != null) {
/* 105 */         out.add(bareJid);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<BareJid> filterBareJidSet(Collection<? extends Jid> input) {
/* 111 */     Set<BareJid> res = new HashSet<>(input.size());
/* 112 */     filterBareJid(input, res);
/* 113 */     return res;
/*     */   }
/*     */   
/*     */   public static List<BareJid> filterBareJidList(Collection<? extends Jid> input) {
/* 117 */     List<BareJid> res = new ArrayList<>(input.size());
/* 118 */     filterBareJid(input, res);
/* 119 */     return res;
/*     */   }
/*     */   
/*     */   public static void filterFullJid(Collection<? extends Jid> in, Collection<FullJid> out) {
/* 123 */     for (Jid jid : in) {
/* 124 */       FullJid fullJid = jid.asFullJidIfPossible();
/* 125 */       if (fullJid != null) {
/* 126 */         out.add(fullJid);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<FullJid> filterFullJidSet(Collection<? extends Jid> input) {
/* 132 */     Set<FullJid> res = new HashSet<>(input.size());
/* 133 */     filterFullJid(input, res);
/* 134 */     return res;
/*     */   }
/*     */   
/*     */   public static List<FullJid> filterFullJidList(Collection<? extends Jid> input) {
/* 138 */     List<FullJid> res = new ArrayList<>(input.size());
/* 139 */     filterFullJid(input, res);
/* 140 */     return res;
/*     */   }
/*     */   
/*     */   public static void filterDomainFullJid(Collection<? extends Jid> in, Collection<DomainFullJid> out) {
/* 144 */     for (Jid jid : in) {
/* 145 */       DomainFullJid domainFullJid = jid.asDomainFullJidIfPossible();
/* 146 */       if (domainFullJid != null) {
/* 147 */         out.add(domainFullJid);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<DomainFullJid> filterDomainFullJidSet(Collection<? extends Jid> input) {
/* 153 */     Set<DomainFullJid> res = new HashSet<>(input.size());
/* 154 */     filterDomainFullJid(input, res);
/* 155 */     return res;
/*     */   }
/*     */   
/*     */   public static List<DomainFullJid> filterDomainFullJidList(Collection<? extends Jid> input) {
/* 159 */     List<DomainFullJid> res = new ArrayList<>(input.size());
/* 160 */     filterDomainFullJid(input, res);
/* 161 */     return res;
/*     */   }
/*     */   
/*     */   public static Set<BareJid> bareJidSetFrom(Collection<CharSequence> jidStrings) {
/* 165 */     Set<BareJid> res = new HashSet<>(jidStrings.size());
/* 166 */     bareJidsFrom(jidStrings, res, null);
/* 167 */     return res;
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
/*     */   public static void bareJidsFrom(Collection<CharSequence> jidStrings, Collection<BareJid> output, List<XmppStringprepException> exceptions) {
/* 186 */     for (CharSequence jid : jidStrings) {
/*     */       try {
/* 188 */         BareJid bareJid = JidCreate.bareFrom(jid);
/* 189 */         output.add(bareJid);
/* 190 */       } catch (XmppStringprepException e) {
/* 191 */         if (exceptions != null) {
/* 192 */           exceptions.add(e); continue;
/*     */         } 
/* 194 */         throw new AssertionError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Jid> jidSetFrom(Collection<CharSequence> jidStrings) {
/* 201 */     Set<Jid> res = new HashSet<>(jidStrings.size());
/* 202 */     jidsFrom(jidStrings, res, null);
/* 203 */     return res;
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
/*     */   public static void jidsFrom(Collection<CharSequence> jidStrings, Collection<Jid> output, List<XmppStringprepException> exceptions) {
/* 222 */     for (CharSequence jidString : jidStrings) {
/*     */       try {
/* 224 */         Jid jid = JidCreate.from(jidString);
/* 225 */         output.add(jid);
/* 226 */       } catch (XmppStringprepException e) {
/* 227 */         if (exceptions != null) {
/* 228 */           exceptions.add(e); continue;
/*     */         } 
/* 230 */         throw new AssertionError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> toStringList(Collection<? extends Jid> jids) {
/* 237 */     List<String> res = new ArrayList<>(jids.size());
/* 238 */     toStrings(jids, res);
/* 239 */     return res;
/*     */   }
/*     */   
/*     */   public static Set<String> toStringSet(Collection<? extends Jid> jids) {
/* 243 */     Set<String> res = new HashSet<>(jids.size());
/* 244 */     toStrings(jids, res);
/* 245 */     return res;
/*     */   }
/*     */   
/*     */   public static void toStrings(Collection<? extends Jid> jids, Collection<String> jidStrings) {
/* 249 */     for (Jid jid : jids)
/* 250 */       jidStrings.add(jid.toString()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\ji\\util\JidUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */