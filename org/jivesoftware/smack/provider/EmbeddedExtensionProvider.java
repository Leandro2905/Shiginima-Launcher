/*     */ package org.jivesoftware.smack.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.jivesoftware.smack.SmackException;
/*     */ import org.jivesoftware.smack.packet.Element;
/*     */ import org.jivesoftware.smack.packet.ExtensionElement;
/*     */ import org.jivesoftware.smack.util.PacketParserUtils;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
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
/*     */ public abstract class EmbeddedExtensionProvider<PE extends ExtensionElement>
/*     */   extends ExtensionElementProvider<PE>
/*     */ {
/*     */   protected abstract PE createReturnExtension(String paramString1, String paramString2, Map<String, String> paramMap, List<? extends ExtensionElement> paramList);
/*     */   
/*     */   public final PE parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
/*     */     int event;
/*  89 */     String namespace = parser.getNamespace();
/*  90 */     String name = parser.getName();
/*  91 */     int attributeCount = parser.getAttributeCount();
/*  92 */     Map<String, String> attMap = new HashMap<>(attributeCount);
/*     */     
/*  94 */     for (int i = 0; i < attributeCount; i++) {
/*  95 */       attMap.put(parser.getAttributeName(i), parser.getAttributeValue(i));
/*     */     }
/*     */     
/*  98 */     List<ExtensionElement> extensions = new ArrayList<>();
/*     */     
/*     */     do {
/* 101 */       event = parser.next();
/*     */       
/* 103 */       if (event != 2)
/* 104 */         continue;  PacketParserUtils.addExtensionElement(extensions, parser);
/*     */     }
/* 106 */     while (event != 3 || parser.getDepth() != initialDepth);
/*     */     
/* 108 */     return createReturnExtension(name, namespace, attMap, extensions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\EmbeddedExtensionProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */