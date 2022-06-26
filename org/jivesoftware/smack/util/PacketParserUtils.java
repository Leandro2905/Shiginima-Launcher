/*      */ package org.jivesoftware.smack.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.jivesoftware.smack.SmackException;
/*      */ import org.jivesoftware.smack.compress.packet.Compress;
/*      */ import org.jivesoftware.smack.packet.DefaultExtensionElement;
/*      */ import org.jivesoftware.smack.packet.EmptyResultIQ;
/*      */ import org.jivesoftware.smack.packet.ErrorIQ;
/*      */ import org.jivesoftware.smack.packet.ExtensionElement;
/*      */ import org.jivesoftware.smack.packet.IQ;
/*      */ import org.jivesoftware.smack.packet.Message;
/*      */ import org.jivesoftware.smack.packet.Presence;
/*      */ import org.jivesoftware.smack.packet.Session;
/*      */ import org.jivesoftware.smack.packet.Stanza;
/*      */ import org.jivesoftware.smack.packet.StartTls;
/*      */ import org.jivesoftware.smack.packet.StreamError;
/*      */ import org.jivesoftware.smack.packet.UnparsedIQ;
/*      */ import org.jivesoftware.smack.packet.XMPPError;
/*      */ import org.jivesoftware.smack.provider.ExtensionElementProvider;
/*      */ import org.jivesoftware.smack.provider.IQProvider;
/*      */ import org.jivesoftware.smack.provider.ProviderManager;
/*      */ import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
/*      */ import org.jxmpp.jid.Jid;
/*      */ import org.xmlpull.v1.XmlPullParser;
/*      */ import org.xmlpull.v1.XmlPullParserException;
/*      */ import org.xmlpull.v1.XmlPullParserFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PacketParserUtils
/*      */ {
/*   62 */   private static final Logger LOGGER = Logger.getLogger(PacketParserUtils.class.getName());
/*      */ 
/*      */   
/*      */   public static final String FEATURE_XML_ROUNDTRIP = "http://xmlpull.org/v1/doc/features.html#xml-roundtrip";
/*      */ 
/*      */   
/*      */   private static final XmlPullParserFactory XML_PULL_PARSER_FACTORY;
/*      */ 
/*      */   
/*      */   public static final boolean XML_PULL_PARSER_SUPPORTS_ROUNDTRIP;
/*      */ 
/*      */   
/*      */   static {
/*   75 */     boolean roundtrip = false;
/*      */     try {
/*   77 */       XML_PULL_PARSER_FACTORY = XmlPullParserFactory.newInstance();
/*   78 */       XmlPullParser xmlPullParser = XML_PULL_PARSER_FACTORY.newPullParser();
/*      */       try {
/*   80 */         xmlPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#xml-roundtrip", true);
/*      */         
/*   82 */         roundtrip = true;
/*   83 */       } catch (XmlPullParserException e) {
/*      */         
/*   85 */         LOGGER.log(Level.FINEST, "XmlPullParser does not support XML_ROUNDTRIP", (Throwable)e);
/*      */       }
/*      */     
/*   88 */     } catch (XmlPullParserException e) {
/*      */       
/*   90 */       throw new AssertionError(e);
/*      */     } 
/*   92 */     XML_PULL_PARSER_SUPPORTS_ROUNDTRIP = roundtrip;
/*      */   }
/*      */   
/*      */   public static XmlPullParser getParserFor(String stanza) throws XmlPullParserException, IOException {
/*   96 */     return getParserFor(new StringReader(stanza));
/*      */   }
/*      */   
/*      */   public static XmlPullParser getParserFor(Reader reader) throws XmlPullParserException, IOException {
/*  100 */     XmlPullParser parser = newXmppParser(reader);
/*      */     
/*  102 */     int event = parser.getEventType();
/*  103 */     while (event != 2) {
/*  104 */       if (event == 1) {
/*  105 */         throw new IllegalArgumentException("Document contains no start tag");
/*      */       }
/*  107 */       event = parser.next();
/*      */     } 
/*  109 */     return parser;
/*      */   }
/*      */ 
/*      */   
/*      */   public static XmlPullParser getParserFor(String stanza, String startTag) throws XmlPullParserException, IOException {
/*  114 */     XmlPullParser parser = getParserFor(stanza);
/*      */     
/*      */     while (true) {
/*  117 */       int event = parser.getEventType();
/*  118 */       String name = parser.getName();
/*  119 */       if (event == 2 && name.equals(startTag)) {
/*      */         break;
/*      */       }
/*  122 */       if (event == 1) {
/*  123 */         throw new IllegalArgumentException("Could not find start tag '" + startTag + "' in stanza: " + stanza);
/*      */       }
/*      */       
/*  126 */       parser.next();
/*      */     } 
/*      */     
/*  129 */     return parser;
/*      */   }
/*      */   
/*      */   public static Stanza parseStanza(String stanza) throws XmlPullParserException, IOException, SmackException {
/*  133 */     return parseStanza(getParserFor(stanza));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Stanza parseStanza(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  148 */     ParserUtils.assertAtStartTag(parser);
/*  149 */     String name = parser.getName();
/*  150 */     switch (name) {
/*      */       case "message":
/*  152 */         return (Stanza)parseMessage(parser);
/*      */       case "iq":
/*  154 */         return (Stanza)parseIQ(parser);
/*      */       case "presence":
/*  156 */         return (Stanza)parsePresence(parser);
/*      */     } 
/*  158 */     throw new IllegalArgumentException("Can only parse message, iq or presence, not " + name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static XmlPullParser newXmppParser() throws XmlPullParserException {
/*  175 */     XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
/*  176 */     parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
/*  177 */     if (XML_PULL_PARSER_SUPPORTS_ROUNDTRIP) {
/*      */       try {
/*  179 */         parser.setFeature("http://xmlpull.org/v1/doc/features.html#xml-roundtrip", true);
/*      */       }
/*  181 */       catch (XmlPullParserException e) {
/*  182 */         LOGGER.log(Level.SEVERE, "XmlPullParser does not support XML_ROUNDTRIP, although it was first determined to be supported", (Throwable)e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  187 */     return parser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static XmlPullParser newXmppParser(Reader reader) throws XmlPullParserException {
/*  204 */     XmlPullParser parser = newXmppParser();
/*  205 */     parser.setInput(reader);
/*  206 */     return parser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Message parseMessage(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  220 */     ParserUtils.assertAtStartTag(parser);
/*  221 */     assert parser.getName().equals("message");
/*      */     
/*  223 */     int initialDepth = parser.getDepth();
/*  224 */     Message message = new Message();
/*  225 */     message.setStanzaId(parser.getAttributeValue("", "id"));
/*  226 */     message.setTo(ParserUtils.getJidAttribute(parser, "to"));
/*  227 */     message.setFrom(ParserUtils.getJidAttribute(parser, "from"));
/*  228 */     String typeString = parser.getAttributeValue("", "type");
/*  229 */     if (typeString != null) {
/*  230 */       message.setType(Message.Type.fromString(typeString));
/*      */     }
/*  232 */     String language = getLanguageAttribute(parser);
/*      */ 
/*      */     
/*  235 */     String defaultLanguage = null;
/*  236 */     if (language != null && !"".equals(language.trim())) {
/*  237 */       message.setLanguage(language);
/*  238 */       defaultLanguage = language;
/*      */     } else {
/*      */       
/*  241 */       defaultLanguage = Stanza.getDefaultLanguage();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  247 */     String thread = null; while (true) {
/*      */       String elementName, namespace, xmlLangSubject, subject, xmlLang, body;
/*  249 */       int eventType = parser.next();
/*  250 */       switch (eventType) {
/*      */         case 2:
/*  252 */           elementName = parser.getName();
/*  253 */           namespace = parser.getNamespace();
/*  254 */           switch (elementName) {
/*      */             case "subject":
/*  256 */               xmlLangSubject = getLanguageAttribute(parser);
/*  257 */               if (xmlLangSubject == null) {
/*  258 */                 xmlLangSubject = defaultLanguage;
/*      */               }
/*      */               
/*  261 */               subject = parseElementText(parser);
/*      */               
/*  263 */               if (message.getSubject(xmlLangSubject) == null) {
/*  264 */                 message.addSubject(xmlLangSubject, subject);
/*      */               }
/*      */               continue;
/*      */             case "body":
/*  268 */               xmlLang = getLanguageAttribute(parser);
/*  269 */               if (xmlLang == null) {
/*  270 */                 xmlLang = defaultLanguage;
/*      */               }
/*      */               
/*  273 */               body = parseElementText(parser);
/*      */               
/*  275 */               if (message.getBody(xmlLang) == null) {
/*  276 */                 message.addBody(xmlLang, body);
/*      */               }
/*      */               continue;
/*      */             case "thread":
/*  280 */               if (thread == null) {
/*  281 */                 thread = parser.nextText();
/*      */               }
/*      */               continue;
/*      */             case "error":
/*  285 */               message.setError(parseError(parser));
/*      */               continue;
/*      */           } 
/*  288 */           addExtensionElement((Stanza)message, parser, elementName, namespace);
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/*  293 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/*  300 */     message.setThread(thread);
/*  301 */     return message;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String parseElementText(XmlPullParser parser) throws XmlPullParserException, IOException {
/*      */     String res;
/*  319 */     assert parser.getEventType() == 2;
/*      */     
/*  321 */     if (parser.isEmptyElementTag()) {
/*  322 */       res = "";
/*      */     }
/*      */     else {
/*      */       
/*  326 */       int event = parser.next();
/*  327 */       if (event != 4) {
/*  328 */         if (event == 3)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  334 */           return "";
/*      */         }
/*  336 */         throw new XmlPullParserException("Non-empty element tag not followed by text, while Mixed Content (XML 3.2.2) is disallowed");
/*      */       } 
/*      */ 
/*      */       
/*  340 */       res = parser.getText();
/*  341 */       event = parser.next();
/*  342 */       if (event != 3) {
/*  343 */         throw new XmlPullParserException("Non-empty element tag contains child-elements, while Mixed Content (XML 3.2.2) is disallowed");
/*      */       }
/*      */     } 
/*      */     
/*  347 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharSequence parseElement(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  363 */     return parseElement(parser, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharSequence parseElement(XmlPullParser parser, boolean fullNamespaces) throws XmlPullParserException, IOException {
/*  369 */     assert parser.getEventType() == 2;
/*  370 */     return parseContentDepth(parser, parser.getDepth(), fullNamespaces);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharSequence parseContent(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  391 */     assert parser.getEventType() == 2;
/*  392 */     if (parser.isEmptyElementTag()) {
/*  393 */       return "";
/*      */     }
/*      */     
/*  396 */     parser.next();
/*  397 */     return parseContentDepth(parser, parser.getDepth(), false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static CharSequence parseContentDepth(XmlPullParser parser, int depth) throws XmlPullParserException, IOException {
/*  402 */     return parseContentDepth(parser, depth, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharSequence parseContentDepth(XmlPullParser parser, int depth, boolean fullNamespaces) throws XmlPullParserException, IOException {
/*  429 */     if (parser.getFeature("http://xmlpull.org/v1/doc/features.html#xml-roundtrip")) {
/*  430 */       return parseContentDepthWithRoundtrip(parser, depth, fullNamespaces);
/*      */     }
/*  432 */     return parseContentDepthWithoutRoundtrip(parser, depth, fullNamespaces);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static CharSequence parseContentDepthWithoutRoundtrip(XmlPullParser parser, int depth, boolean fullNamespaces) throws XmlPullParserException, IOException {
/*  438 */     XmlStringBuilder xml = new XmlStringBuilder();
/*  439 */     int event = parser.getEventType();
/*  440 */     boolean isEmptyElement = false;
/*      */ 
/*      */ 
/*      */     
/*  444 */     String namespaceElement = null; while (true) {
/*      */       int i;
/*  446 */       switch (event) {
/*      */         case 2:
/*  448 */           xml.halfOpenElement(parser.getName());
/*  449 */           if (namespaceElement == null || fullNamespaces) {
/*  450 */             String namespace = parser.getNamespace();
/*  451 */             if (StringUtils.isNotEmpty(namespace)) {
/*  452 */               xml.attribute("xmlns", namespace);
/*  453 */               namespaceElement = parser.getName();
/*      */             } 
/*      */           } 
/*  456 */           for (i = 0; i < parser.getAttributeCount(); i++) {
/*  457 */             xml.attribute(parser.getAttributeName(i), parser.getAttributeValue(i));
/*      */           }
/*  459 */           if (parser.isEmptyElementTag()) {
/*  460 */             xml.closeEmptyElement();
/*  461 */             isEmptyElement = true;
/*      */             break;
/*      */           } 
/*  464 */           xml.rightAngleBracket();
/*      */           break;
/*      */         
/*      */         case 3:
/*  468 */           if (isEmptyElement) {
/*      */             
/*  470 */             isEmptyElement = false;
/*      */           } else {
/*      */             
/*  473 */             xml.closeElement(parser.getName());
/*      */           } 
/*  475 */           if (namespaceElement != null && namespaceElement.equals(parser.getName()))
/*      */           {
/*  477 */             namespaceElement = null;
/*      */           }
/*  479 */           if (parser.getDepth() <= depth) {
/*      */             break;
/*      */           }
/*      */           break;
/*      */         
/*      */         case 4:
/*  485 */           xml.append(parser.getText());
/*      */           break;
/*      */       } 
/*  488 */       event = parser.next();
/*      */     } 
/*  490 */     return xml;
/*      */   }
/*      */ 
/*      */   
/*      */   private static CharSequence parseContentDepthWithRoundtrip(XmlPullParser parser, int depth, boolean fullNamespaces) throws XmlPullParserException, IOException {
/*  495 */     StringBuilder sb = new StringBuilder();
/*  496 */     int event = parser.getEventType();
/*      */ 
/*      */     
/*      */     while (true) {
/*  500 */       if (event != 2 || !parser.isEmptyElementTag()) {
/*  501 */         sb.append(parser.getText());
/*      */       }
/*  503 */       if (event == 3 && parser.getDepth() <= depth) {
/*      */         break;
/*      */       }
/*  506 */       event = parser.next();
/*      */     } 
/*  508 */     return sb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Presence parsePresence(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  522 */     ParserUtils.assertAtStartTag(parser);
/*  523 */     int initialDepth = parser.getDepth();
/*      */     
/*  525 */     Presence.Type type = Presence.Type.available;
/*  526 */     String typeString = parser.getAttributeValue("", "type");
/*  527 */     if (typeString != null && !typeString.equals("")) {
/*  528 */       type = Presence.Type.fromString(typeString);
/*      */     }
/*  530 */     Presence presence = new Presence(type);
/*  531 */     presence.setTo(ParserUtils.getJidAttribute(parser, "to"));
/*  532 */     presence.setFrom(ParserUtils.getJidAttribute(parser, "from"));
/*  533 */     presence.setStanzaId(parser.getAttributeValue("", "id"));
/*      */     
/*  535 */     String language = getLanguageAttribute(parser);
/*  536 */     if (language != null && !"".equals(language.trim()))
/*  537 */       presence.setLanguage(language); 
/*      */     while (true) {
/*      */       String elementName, namespace;
/*      */       int priority;
/*      */       String modeText;
/*  542 */       int eventType = parser.next();
/*  543 */       switch (eventType) {
/*      */         case 2:
/*  545 */           elementName = parser.getName();
/*  546 */           namespace = parser.getNamespace();
/*  547 */           switch (elementName) {
/*      */             case "status":
/*  549 */               presence.setStatus(parser.nextText());
/*      */               break;
/*      */             case "priority":
/*  552 */               priority = Integer.parseInt(parser.nextText());
/*  553 */               presence.setPriority(priority);
/*      */               break;
/*      */             case "show":
/*  556 */               modeText = parser.nextText();
/*  557 */               if (StringUtils.isNotEmpty(modeText)) {
/*  558 */                 presence.setMode(Presence.Mode.fromString(modeText));
/*      */                 
/*      */                 break;
/*      */               } 
/*      */               
/*  563 */               LOGGER.warning("Empty or null mode text in presence show element form " + presence.getFrom() + " with id '" + presence.getStanzaId() + "' which is invalid according to RFC6121 4.7.2.1");
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case "error":
/*  571 */               presence.setError(parseError(parser));
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             default:
/*      */               try {
/*  578 */                 addExtensionElement((Stanza)presence, parser, elementName, namespace);
/*  579 */               } catch (Exception e) {
/*  580 */                 LOGGER.log(Level.WARNING, "Failed to parse extension packet in Presence packet.", e);
/*      */               } 
/*      */               break;
/*      */           } 
/*      */         case 3:
/*  585 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     
/*      */     } 
/*  591 */     return presence;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IQ parseIQ(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*      */     UnparsedIQ unparsedIQ;
/*      */     EmptyResultIQ emptyResultIQ;
/*  604 */     ParserUtils.assertAtStartTag(parser);
/*  605 */     int initialDepth = parser.getDepth();
/*  606 */     IQ iqPacket = null;
/*  607 */     XMPPError error = null;
/*      */     
/*  609 */     String id = parser.getAttributeValue("", "id");
/*  610 */     Jid to = ParserUtils.getJidAttribute(parser, "to");
/*  611 */     Jid from = ParserUtils.getJidAttribute(parser, "from");
/*  612 */     IQ.Type type = IQ.Type.fromString(parser.getAttributeValue("", "type")); while (true) {
/*      */       String elementName, namespace;
/*      */       IQProvider<IQ> provider;
/*  615 */       int eventType = parser.next();
/*      */       
/*  617 */       switch (eventType) {
/*      */         case 2:
/*  619 */           elementName = parser.getName();
/*  620 */           namespace = parser.getNamespace();
/*  621 */           switch (elementName) {
/*      */             case "error":
/*  623 */               error = parseError(parser);
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/*  628 */           provider = ProviderManager.getIQProvider(elementName, namespace);
/*  629 */           if (provider != null) {
/*  630 */             iqPacket = (IQ)provider.parse(parser);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */ 
/*      */           
/*  637 */           unparsedIQ = new UnparsedIQ(elementName, namespace, parseElement(parser));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/*  643 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/*  650 */     if (unparsedIQ == null) {
/*  651 */       ErrorIQ errorIQ; switch (type) {
/*      */         
/*      */         case error:
/*  654 */           errorIQ = new ErrorIQ(error);
/*      */           break;
/*      */         case result:
/*  657 */           emptyResultIQ = new EmptyResultIQ();
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/*  665 */     emptyResultIQ.setStanzaId(id);
/*  666 */     emptyResultIQ.setTo(to);
/*  667 */     emptyResultIQ.setFrom(from);
/*  668 */     emptyResultIQ.setType(type);
/*  669 */     emptyResultIQ.setError(error);
/*      */     
/*  671 */     return (IQ)emptyResultIQ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Collection<String> parseMechanisms(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  684 */     List<String> mechanisms = new ArrayList<>();
/*  685 */     boolean done = false;
/*  686 */     while (!done) {
/*  687 */       int eventType = parser.next();
/*      */       
/*  689 */       if (eventType == 2) {
/*  690 */         String elementName = parser.getName();
/*  691 */         if (elementName.equals("mechanism"))
/*  692 */           mechanisms.add(parser.nextText()); 
/*      */         continue;
/*      */       } 
/*  695 */       if (eventType == 3 && 
/*  696 */         parser.getName().equals("mechanisms")) {
/*  697 */         done = true;
/*      */       }
/*      */     } 
/*      */     
/*  701 */     return mechanisms;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Compress.Feature parseCompressionFeature(XmlPullParser parser) throws IOException, XmlPullParserException {
/*  713 */     assert parser.getEventType() == 2;
/*      */     
/*  715 */     int initialDepth = parser.getDepth();
/*  716 */     List<String> methods = new LinkedList<>(); while (true) {
/*      */       String name;
/*  718 */       int eventType = parser.next();
/*  719 */       switch (eventType) {
/*      */         case 2:
/*  721 */           name = parser.getName();
/*  722 */           switch (name) {
/*      */             case "method":
/*  724 */               methods.add(parser.nextText());
/*      */           } 
/*      */         
/*      */         
/*      */         case 3:
/*  729 */           name = parser.getName();
/*  730 */           switch (name) {
/*      */             case "compression":
/*  732 */               if (parser.getDepth() == initialDepth) {
/*      */                 break;
/*      */               }
/*      */           } 
/*      */       } 
/*      */     } 
/*  738 */     assert parser.getEventType() == 3;
/*  739 */     assert parser.getDepth() == initialDepth;
/*  740 */     return new Compress.Feature(methods);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, String> parseDescriptiveTexts(XmlPullParser parser, Map<String, String> descriptiveTexts) throws XmlPullParserException, IOException {
/*  745 */     if (descriptiveTexts == null) {
/*  746 */       descriptiveTexts = new HashMap<>();
/*      */     }
/*  748 */     String xmllang = getLanguageAttribute(parser);
/*  749 */     String text = parser.nextText();
/*  750 */     String previousValue = descriptiveTexts.put(xmllang, text);
/*  751 */     assert previousValue == null;
/*  752 */     return descriptiveTexts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SaslStreamElements.SASLFailure parseSASLFailure(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  764 */     int initialDepth = parser.getDepth();
/*  765 */     String condition = null;
/*  766 */     Map<String, String> descriptiveTexts = null; while (true) {
/*      */       String name;
/*  768 */       int eventType = parser.next();
/*  769 */       switch (eventType) {
/*      */         case 2:
/*  771 */           name = parser.getName();
/*  772 */           if (name.equals("text")) {
/*  773 */             descriptiveTexts = parseDescriptiveTexts(parser, descriptiveTexts);
/*      */             continue;
/*      */           } 
/*  776 */           assert condition == null;
/*  777 */           condition = parser.getName();
/*      */ 
/*      */         
/*      */         case 3:
/*  781 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     
/*      */     } 
/*  787 */     return new SaslStreamElements.SASLFailure(condition, descriptiveTexts);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StreamError parseStreamError(XmlPullParser parser) throws IOException, XmlPullParserException, SmackException {
/*  800 */     int initialDepth = parser.getDepth();
/*  801 */     List<ExtensionElement> extensions = new ArrayList<>();
/*  802 */     Map<String, String> descriptiveTexts = null;
/*  803 */     StreamError.Condition condition = null;
/*  804 */     String conditionText = null; while (true) {
/*      */       String name, namespace;
/*  806 */       int eventType = parser.next();
/*  807 */       switch (eventType) {
/*      */         case 2:
/*  809 */           name = parser.getName();
/*  810 */           namespace = parser.getNamespace();
/*  811 */           switch (namespace) {
/*      */             case "urn:ietf:params:xml:ns:xmpp-streams":
/*  813 */               switch (name) {
/*      */                 case "text":
/*  815 */                   descriptiveTexts = parseDescriptiveTexts(parser, descriptiveTexts);
/*      */                   continue;
/*      */               } 
/*      */ 
/*      */               
/*  820 */               condition = StreamError.Condition.fromString(name);
/*  821 */               if (!parser.isEmptyElementTag()) {
/*  822 */                 conditionText = parser.nextText();
/*      */               }
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/*  828 */           addExtensionElement(extensions, parser, name, namespace);
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/*  833 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     
/*      */     } 
/*  839 */     return new StreamError(condition, conditionText, descriptiveTexts, extensions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static XMPPError parseError(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  853 */     int initialDepth = parser.getDepth();
/*  854 */     Map<String, String> descriptiveTexts = null;
/*  855 */     XMPPError.Condition condition = null;
/*  856 */     String conditionText = null;
/*  857 */     List<ExtensionElement> extensions = new ArrayList<>();
/*      */ 
/*      */     
/*  860 */     XMPPError.Type errorType = XMPPError.Type.fromString(parser.getAttributeValue("", "type"));
/*  861 */     String errorGenerator = parser.getAttributeValue("", "by");
/*      */     while (true) {
/*      */       String name, namespace;
/*  864 */       int eventType = parser.next();
/*  865 */       switch (eventType) {
/*      */         case 2:
/*  867 */           name = parser.getName();
/*  868 */           namespace = parser.getNamespace();
/*  869 */           switch (namespace) {
/*      */             case "urn:ietf:params:xml:ns:xmpp-stanzas":
/*  871 */               switch (name) {
/*      */                 case "text":
/*  873 */                   descriptiveTexts = parseDescriptiveTexts(parser, descriptiveTexts);
/*      */                   continue;
/*      */               } 
/*  876 */               condition = XMPPError.Condition.fromString(name);
/*  877 */               if (!parser.isEmptyElementTag()) {
/*  878 */                 conditionText = parser.nextText();
/*      */               }
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/*  884 */           addExtensionElement(extensions, parser, name, namespace);
/*      */ 
/*      */         
/*      */         case 3:
/*  888 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     } 
/*  893 */     return new XMPPError(condition, conditionText, errorGenerator, errorType, descriptiveTexts, extensions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static ExtensionElement parsePacketExtension(String elementName, String namespace, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  903 */     return parseExtensionElement(elementName, namespace, parser);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ExtensionElement parseExtensionElement(String elementName, String namespace, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/*  917 */     ParserUtils.assertAtStartTag(parser);
/*      */     
/*  919 */     ExtensionElementProvider<ExtensionElement> provider = ProviderManager.getExtensionProvider(elementName, namespace);
/*  920 */     if (provider != null) {
/*  921 */       return (ExtensionElement)provider.parse(parser);
/*      */     }
/*      */     
/*  924 */     int initialDepth = parser.getDepth();
/*      */     
/*  926 */     DefaultExtensionElement extension = new DefaultExtensionElement(elementName, namespace); while (true) {
/*      */       String name;
/*  928 */       int eventType = parser.next();
/*  929 */       switch (eventType) {
/*      */         case 2:
/*  931 */           name = parser.getName();
/*      */           
/*  933 */           if (parser.isEmptyElementTag()) {
/*  934 */             extension.setValue(name, "");
/*      */             
/*      */             continue;
/*      */           } 
/*  938 */           eventType = parser.next();
/*  939 */           if (eventType == 4) {
/*  940 */             String value = parser.getText();
/*  941 */             extension.setValue(name, value);
/*      */           } 
/*      */ 
/*      */         
/*      */         case 3:
/*  946 */           if (parser.getDepth() == initialDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     } 
/*  951 */     return (ExtensionElement)extension;
/*      */   }
/*      */ 
/*      */   
/*      */   public static StartTls parseStartTlsFeature(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  956 */     assert parser.getEventType() == 2;
/*  957 */     assert parser.getNamespace().equals("urn:ietf:params:xml:ns:xmpp-tls");
/*  958 */     int initalDepth = parser.getDepth();
/*  959 */     boolean required = false; while (true) {
/*      */       String name;
/*  961 */       int event = parser.next();
/*  962 */       switch (event) {
/*      */         case 2:
/*  964 */           name = parser.getName();
/*  965 */           switch (name) {
/*      */             case "required":
/*  967 */               required = true;
/*      */           } 
/*      */         
/*      */         
/*      */         case 3:
/*  972 */           if (parser.getDepth() == initalDepth) {
/*      */             break;
/*      */           }
/*      */       } 
/*      */     } 
/*  977 */     assert parser.getEventType() == 3;
/*  978 */     return new StartTls(required);
/*      */   }
/*      */   
/*      */   public static Session.Feature parseSessionFeature(XmlPullParser parser) throws XmlPullParserException, IOException {
/*  982 */     ParserUtils.assertAtStartTag(parser);
/*  983 */     int initialDepth = parser.getDepth();
/*  984 */     boolean optional = false;
/*  985 */     if (!parser.isEmptyElementTag()) {
/*      */       while (true) {
/*  987 */         String name; int event = parser.next();
/*  988 */         switch (event) {
/*      */           case 2:
/*  990 */             name = parser.getName();
/*  991 */             switch (name) {
/*      */               case "optional":
/*  993 */                 optional = true;
/*      */             } 
/*      */           
/*      */           
/*      */           case 3:
/*  998 */             if (parser.getDepth() == initialDepth) {
/*      */               break;
/*      */             }
/*      */         } 
/*      */       } 
/*      */     }
/* 1004 */     return new Session.Feature(optional);
/*      */   }
/*      */   
/*      */   private static String getLanguageAttribute(XmlPullParser parser) {
/* 1008 */     for (int i = 0; i < parser.getAttributeCount(); i++) {
/* 1009 */       String attributeName = parser.getAttributeName(i);
/* 1010 */       if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(parser.getAttributePrefix(i))))
/*      */       {
/*      */         
/* 1013 */         return parser.getAttributeValue(i);
/*      */       }
/*      */     } 
/* 1016 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addPacketExtension(Stanza packet, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/* 1022 */     addExtensionElement(packet, parser);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addPacketExtension(Stanza packet, XmlPullParser parser, String elementName, String namespace) throws XmlPullParserException, IOException, SmackException {
/* 1028 */     addExtensionElement(packet, parser, elementName, namespace);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addPacketExtension(Collection<ExtensionElement> collection, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/* 1034 */     addExtensionElement(collection, parser, parser.getName(), parser.getNamespace());
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addPacketExtension(Collection<ExtensionElement> collection, XmlPullParser parser, String elementName, String namespace) throws XmlPullParserException, IOException, SmackException {
/* 1040 */     addExtensionElement(collection, parser, elementName, namespace);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addExtensionElement(Stanza packet, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/* 1046 */     ParserUtils.assertAtStartTag(parser);
/* 1047 */     addExtensionElement(packet, parser, parser.getName(), parser.getNamespace());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addExtensionElement(Stanza packet, XmlPullParser parser, String elementName, String namespace) throws XmlPullParserException, IOException, SmackException {
/* 1052 */     ExtensionElement packetExtension = parseExtensionElement(elementName, namespace, parser);
/* 1053 */     packet.addExtension(packetExtension);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addExtensionElement(Collection<ExtensionElement> collection, XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
/* 1059 */     addExtensionElement(collection, parser, parser.getName(), parser.getNamespace());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addExtensionElement(Collection<ExtensionElement> collection, XmlPullParser parser, String elementName, String namespace) throws XmlPullParserException, IOException, SmackException {
/* 1065 */     ExtensionElement packetExtension = parseExtensionElement(elementName, namespace, parser);
/* 1066 */     collection.add(packetExtension);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\PacketParserUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */