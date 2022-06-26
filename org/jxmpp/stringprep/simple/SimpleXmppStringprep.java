/*    */ package org.jxmpp.stringprep.simple;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.jxmpp.stringprep.XmppStringPrepUtil;
/*    */ import org.jxmpp.stringprep.XmppStringprep;
/*    */ import org.jxmpp.stringprep.XmppStringprepException;
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
/*    */ public class SimpleXmppStringprep
/*    */   implements XmppStringprep
/*    */ {
/*    */   private static SimpleXmppStringprep instance;
/*    */   
/*    */   public static void setup() {
/* 30 */     XmppStringPrepUtil.setXmppStringprep(getInstance());
/*    */   }
/*    */   
/*    */   public static SimpleXmppStringprep getInstance() {
/* 34 */     if (instance == null) {
/* 35 */       instance = new SimpleXmppStringprep();
/*    */     }
/* 37 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   private static final char[] LOCALPART_FURTHER_EXCLUDED_CHARACTERS = new char[] { '"', '&', '\'', '/', ',', '<', '>', '@', ' ' };
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
/*    */   public String localprep(String string) throws XmppStringprepException {
/* 62 */     string = simpleStringprep(string);
/* 63 */     for (char charFromString : string.toCharArray()) {
/* 64 */       for (char forbiddenChar : LOCALPART_FURTHER_EXCLUDED_CHARACTERS) {
/* 65 */         if (charFromString == forbiddenChar) {
/* 66 */           throw new XmppStringprepException(string, "Localpart must not contain '" + forbiddenChar + "'");
/*    */         }
/*    */       } 
/*    */     } 
/* 70 */     return string;
/*    */   }
/*    */ 
/*    */   
/*    */   public String domainprep(String string) throws XmppStringprepException {
/* 75 */     return simpleStringprep(string);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String resourceprep(String string) throws XmppStringprepException {
/* 85 */     return string;
/*    */   }
/*    */   
/*    */   private static String simpleStringprep(String string) {
/* 89 */     String res = string.toLowerCase(Locale.US);
/* 90 */     return res;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\stringprep\simple\SimpleXmppStringprep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */