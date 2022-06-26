/*    */ package org.jivesoftware.smack.provider;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.jivesoftware.smack.SmackException;
/*    */ import org.jivesoftware.smack.packet.Bind;
/*    */ import org.jivesoftware.smack.packet.Element;
/*    */ import org.jxmpp.jid.FullJid;
/*    */ import org.jxmpp.jid.impl.JidCreate;
/*    */ import org.xmlpull.v1.XmlPullParser;
/*    */ import org.xmlpull.v1.XmlPullParserException;
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
/*    */ public class BindIQProvider
/*    */   extends IQProvider<Bind>
/*    */ {
/*    */   public Bind parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
/* 34 */     Bind bind = null; while (true) {
/*    */       String name; FullJid fullJid;
/* 36 */       int eventType = parser.next();
/* 37 */       switch (eventType) {
/*    */         case 2:
/* 39 */           name = parser.getName();
/* 40 */           switch (name) {
/*    */             case "resource":
/* 42 */               bind = Bind.newSet(parser.nextText());
/*    */             
/*    */             case "jid":
/* 45 */               fullJid = JidCreate.fullFrom(parser.nextText());
/* 46 */               bind = Bind.newResult(fullJid);
/*    */           } 
/*    */         
/*    */         
/*    */         case 3:
/* 51 */           if (parser.getDepth() == initialDepth) {
/*    */             break;
/*    */           }
/*    */       } 
/*    */     
/*    */     } 
/* 57 */     return bind;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\BindIQProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */