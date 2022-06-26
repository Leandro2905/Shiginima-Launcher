/*    */ package org.jxmpp.stringprep;
/*    */ 
/*    */ import org.jxmpp.util.cache.Cache;
/*    */ import org.jxmpp.util.cache.LruCache;
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
/*    */ public class XmppStringPrepUtil
/*    */ {
/* 24 */   private static final Cache<String, String> NODEPREP_CACHE = (Cache<String, String>)new LruCache(100);
/* 25 */   private static final Cache<String, String> DOMAINPREP_CACHE = (Cache<String, String>)new LruCache(100);
/* 26 */   private static final Cache<String, String> RESOURCEPREP_CACHE = (Cache<String, String>)new LruCache(100);
/*    */   
/*    */   private static XmppStringprep xmppStringprep;
/*    */   
/*    */   public static void setXmppStringprep(XmppStringprep xmppStringprep) {
/* 31 */     XmppStringPrepUtil.xmppStringprep = xmppStringprep;
/*    */   }
/*    */   
/*    */   public static String localprep(String string) throws XmppStringprepException {
/* 35 */     if (xmppStringprep == null) {
/* 36 */       return string;
/*    */     }
/*    */     
/* 39 */     throwIfEmptyString(string);
/* 40 */     String res = (String)NODEPREP_CACHE.get(string);
/* 41 */     if (res != null) {
/* 42 */       return res;
/*    */     }
/* 44 */     res = xmppStringprep.localprep(string);
/* 45 */     NODEPREP_CACHE.put(string, res);
/* 46 */     return res;
/*    */   }
/*    */   
/*    */   public static String domainprep(String string) throws XmppStringprepException {
/* 50 */     if (xmppStringprep == null) {
/* 51 */       return string;
/*    */     }
/*    */     
/* 54 */     throwIfEmptyString(string);
/* 55 */     String res = (String)DOMAINPREP_CACHE.get(string);
/* 56 */     if (res != null) {
/* 57 */       return res;
/*    */     }
/* 59 */     res = xmppStringprep.domainprep(string);
/* 60 */     DOMAINPREP_CACHE.put(string, res);
/* 61 */     return res;
/*    */   }
/*    */   
/*    */   public static String resourceprep(String string) throws XmppStringprepException {
/* 65 */     if (xmppStringprep == null) {
/* 66 */       return string;
/*    */     }
/*    */     
/* 69 */     throwIfEmptyString(string);
/* 70 */     String res = (String)RESOURCEPREP_CACHE.get(string);
/* 71 */     if (res != null) {
/* 72 */       return res;
/*    */     }
/* 74 */     res = xmppStringprep.resourceprep(string);
/* 75 */     RESOURCEPREP_CACHE.put(string, res);
/* 76 */     return res;
/*    */   }
/*    */   
/*    */   public static void setMaxCacheSizes(int size) {
/* 80 */     NODEPREP_CACHE.setMaxCacheSize(size);
/* 81 */     DOMAINPREP_CACHE.setMaxCacheSize(size);
/* 82 */     RESOURCEPREP_CACHE.setMaxCacheSize(size);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void throwIfEmptyString(String string) throws XmppStringprepException {
/* 93 */     if (string.length() == 0)
/* 94 */       throw new XmppStringprepException(string, "Argument can't be the empty string"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\stringprep\XmppStringPrepUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */