/*    */ package org.jivesoftware.smack.compress.packet;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.FullStreamElement;
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
/*    */ public class Compressed
/*    */   extends FullStreamElement
/*    */ {
/*    */   public static final String ELEMENT = "compressed";
/*    */   public static final String NAMESPACE = "http://jabber.org/protocol/compress";
/* 26 */   public static final Compressed INSTANCE = new Compressed();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getElementName() {
/* 33 */     return "compressed";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespace() {
/* 38 */     return "http://jabber.org/protocol/compress";
/*    */   }
/*    */ 
/*    */   
/*    */   public String toXML() {
/* 43 */     return "<compressed xmlns='http://jabber.org/protocol/compress'/>";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\compress\packet\Compressed.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */