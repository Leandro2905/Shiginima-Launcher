/*    */ package org.jivesoftware.smack.provider;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.jivesoftware.smack.SmackException;
/*    */ import org.jivesoftware.smack.packet.Element;
/*    */ import org.jivesoftware.smack.util.ParserUtils;
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
/*    */ 
/*    */ public abstract class Provider<E extends Element>
/*    */ {
/*    */   public final E parse(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/* 32 */     ParserUtils.assertAtStartTag(parser);
/*    */     
/* 34 */     int initialDepth = parser.getDepth();
/* 35 */     E e = parse(parser, initialDepth);
/*    */ 
/*    */     
/* 38 */     ParserUtils.forwardToEndTagOfDepth(parser, initialDepth);
/* 39 */     return e;
/*    */   }
/*    */   
/*    */   public abstract E parse(XmlPullParser paramXmlPullParser, int paramInt) throws XmlPullParserException, IOException, SmackException;
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\Provider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */