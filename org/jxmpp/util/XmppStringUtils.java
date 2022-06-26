/*     */ package org.jxmpp.util;
/*     */ 
/*     */ import org.jxmpp.util.cache.LruCache;
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
/*     */ public class XmppStringUtils
/*     */ {
/*     */   public static String parseLocalpart(String jid) {
/*  33 */     int atIndex = jid.indexOf('@');
/*  34 */     if (atIndex <= 0) {
/*  35 */       return "";
/*     */     }
/*  37 */     int slashIndex = jid.indexOf('/');
/*  38 */     if (slashIndex >= 0 && slashIndex < atIndex) {
/*  39 */       return "";
/*     */     }
/*  41 */     return jid.substring(0, atIndex);
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
/*     */   public static String parseDomain(String jid) {
/*  55 */     int atIndex = jid.indexOf('@');
/*     */     
/*  57 */     if (atIndex + 1 > jid.length()) {
/*  58 */       return "";
/*     */     }
/*  60 */     int slashIndex = jid.indexOf('/');
/*  61 */     if (slashIndex > 0) {
/*     */       
/*  63 */       if (slashIndex > atIndex) {
/*  64 */         return jid.substring(atIndex + 1, slashIndex);
/*     */       }
/*     */       
/*  67 */       return jid.substring(0, slashIndex);
/*     */     } 
/*     */     
/*  70 */     return jid.substring(atIndex + 1);
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
/*     */   public static String parseResource(String jid) {
/*  84 */     int slashIndex = jid.indexOf('/');
/*  85 */     if (slashIndex + 1 > jid.length() || slashIndex < 0) {
/*  86 */       return "";
/*     */     }
/*  88 */     return jid.substring(slashIndex + 1);
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
/*     */   @Deprecated
/*     */   public static String parseBareAddress(String jid) {
/* 104 */     return parseBareJid(jid);
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
/*     */   public static String parseBareJid(String jid) {
/* 117 */     int slashIndex = jid.indexOf('/');
/* 118 */     if (slashIndex < 0)
/* 119 */       return jid; 
/* 120 */     if (slashIndex == 0) {
/* 121 */       return "";
/*     */     }
/* 123 */     return jid.substring(0, slashIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullJID(String jid) {
/* 134 */     if (parseLocalpart(jid).length() <= 0 || parseDomain(jid).length() <= 0 || parseResource(jid).length() <= 0)
/*     */     {
/* 136 */       return false;
/*     */     }
/* 138 */     return true;
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
/*     */   public static boolean isBareJid(String jid) {
/* 154 */     return (parseLocalpart(jid).length() > 0 && parseDomain(jid).length() > 0 && parseResource(jid).length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 159 */   private static final LruCache<String, String> LOCALPART_ESACPE_CACHE = new LruCache(100);
/* 160 */   private static final LruCache<String, String> LOCALPART_UNESCAPE_CACHE = new LruCache(100);
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
/*     */   public static String escapeLocalpart(String localpart) {
/* 198 */     if (localpart == null) {
/* 199 */       return null;
/*     */     }
/* 201 */     String res = (String)LOCALPART_ESACPE_CACHE.get(localpart);
/* 202 */     if (res != null) {
/* 203 */       return res;
/*     */     }
/* 205 */     StringBuilder buf = new StringBuilder(localpart.length() + 8);
/* 206 */     for (int i = 0, n = localpart.length(); i < n; i++) {
/* 207 */       char c = localpart.charAt(i);
/* 208 */       switch (c) {
/*     */         case '"':
/* 210 */           buf.append("\\22");
/*     */           break;
/*     */         case '&':
/* 213 */           buf.append("\\26");
/*     */           break;
/*     */         case '\'':
/* 216 */           buf.append("\\27");
/*     */           break;
/*     */         case '/':
/* 219 */           buf.append("\\2f");
/*     */           break;
/*     */         case ':':
/* 222 */           buf.append("\\3a");
/*     */           break;
/*     */         case '<':
/* 225 */           buf.append("\\3c");
/*     */           break;
/*     */         case '>':
/* 228 */           buf.append("\\3e");
/*     */           break;
/*     */         case '@':
/* 231 */           buf.append("\\40");
/*     */           break;
/*     */         case '\\':
/* 234 */           buf.append("\\5c");
/*     */           break;
/*     */         default:
/* 237 */           if (Character.isWhitespace(c)) {
/* 238 */             buf.append("\\20"); break;
/*     */           } 
/* 240 */           buf.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 245 */     res = buf.toString();
/* 246 */     LOCALPART_ESACPE_CACHE.put(localpart, res);
/* 247 */     return res;
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
/*     */   public static String unescapeLocalpart(String localpart) {
/* 287 */     if (localpart == null) {
/* 288 */       return null;
/*     */     }
/* 290 */     String res = (String)LOCALPART_UNESCAPE_CACHE.get(localpart);
/* 291 */     if (res != null) {
/* 292 */       return res;
/*     */     }
/* 294 */     char[] localpartChars = localpart.toCharArray();
/* 295 */     StringBuilder buf = new StringBuilder(localpartChars.length);
/* 296 */     for (int i = 0, n = localpartChars.length; i < n; ) {
/*     */       
/* 298 */       char c = localpart.charAt(i);
/* 299 */       if (c == '\\' && i + 2 < n) {
/* 300 */         char c2 = localpartChars[i + 1];
/* 301 */         char c3 = localpartChars[i + 2];
/* 302 */         switch (c2) {
/*     */           case '2':
/* 304 */             switch (c3) {
/*     */               case '0':
/* 306 */                 buf.append(' ');
/* 307 */                 i += 2;
/*     */                 break;
/*     */               case '2':
/* 310 */                 buf.append('"');
/* 311 */                 i += 2;
/*     */                 break;
/*     */               case '6':
/* 314 */                 buf.append('&');
/* 315 */                 i += 2;
/*     */                 break;
/*     */               case '7':
/* 318 */                 buf.append('\'');
/* 319 */                 i += 2;
/*     */                 break;
/*     */               case 'f':
/* 322 */                 buf.append('/');
/* 323 */                 i += 2;
/*     */                 break;
/*     */             } 
/*     */           
/*     */           case '3':
/* 328 */             switch (c3) {
/*     */               case 'a':
/* 330 */                 buf.append(':');
/* 331 */                 i += 2;
/*     */                 break;
/*     */               case 'c':
/* 334 */                 buf.append('<');
/* 335 */                 i += 2;
/*     */                 break;
/*     */               case 'e':
/* 338 */                 buf.append('>');
/* 339 */                 i += 2;
/*     */                 break;
/*     */             } 
/*     */           
/*     */           case '4':
/* 344 */             if (c3 == '0') {
/* 345 */               buf.append("@");
/* 346 */               i += 2;
/*     */               break;
/*     */             } 
/*     */           
/*     */           case '5':
/* 351 */             if (c3 == 'c') {
/* 352 */               buf.append("\\");
/* 353 */               i += 2;
/*     */               break;
/*     */             } 
/*     */ 
/*     */           
/*     */           default:
/* 359 */             buf.append(c); break;
/*     */         }  i++;
/*     */       } 
/* 362 */     }  res = buf.toString();
/* 363 */     LOCALPART_UNESCAPE_CACHE.put(localpart, res);
/* 364 */     return res;
/*     */   }
/*     */   
/*     */   public static String completeJidFrom(CharSequence localpart, CharSequence domainpart) {
/* 368 */     return completeJidFrom((localpart != null) ? localpart.toString() : null, domainpart.toString());
/*     */   }
/*     */   
/*     */   public static String completeJidFrom(String localpart, String domainpart) {
/* 372 */     return completeJidFrom(localpart, domainpart, (String)null);
/*     */   }
/*     */   
/*     */   public static String completeJidFrom(CharSequence localpart, CharSequence domainpart, CharSequence resource) {
/* 376 */     return completeJidFrom((localpart != null) ? localpart.toString() : null, domainpart.toString(), (resource != null) ? resource.toString() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String completeJidFrom(String localpart, String domainpart, String resource) {
/* 381 */     if (domainpart == null) {
/* 382 */       throw new IllegalArgumentException("domainpart must not be null");
/*     */     }
/* 384 */     int localpartLength = (localpart != null) ? localpart.length() : 0;
/* 385 */     int domainpartLength = domainpart.length();
/* 386 */     int resourceLength = (resource != null) ? resource.length() : 0;
/* 387 */     int maxResLength = localpartLength + domainpartLength + resourceLength + 2;
/* 388 */     StringBuilder sb = new StringBuilder(maxResLength);
/* 389 */     if (localpartLength > 0) {
/* 390 */       sb.append(localpart).append('@');
/*     */     }
/* 392 */     sb.append(domainpart);
/* 393 */     if (resourceLength > 0) {
/* 394 */       sb.append('/').append(resource);
/*     */     }
/* 396 */     return sb.toString();
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
/*     */   public static String generateKey(String element, String namespace) {
/* 415 */     return element + '\t' + namespace;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmp\\util\XmppStringUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */