/*     */ package org.xmlpull.v1.dom2_builder;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
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
/*     */ public class DOM2XmlPullBuilder
/*     */ {
/*     */   protected Document newDoc() throws XmlPullParserException {
/*     */     try {
/*  55 */       DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
/*     */       
/*  57 */       DocumentBuilder builder = domFactory.newDocumentBuilder();
/*  58 */       DOMImplementation impl = builder.getDOMImplementation();
/*  59 */       return builder.newDocument();
/*  60 */     } catch (FactoryConfigurationError ex) {
/*  61 */       throw new XmlPullParserException("could not configure factory JAXP DocumentBuilderFactory: " + ex, null, ex);
/*     */     }
/*  63 */     catch (ParserConfigurationException ex) {
/*  64 */       throw new XmlPullParserException("could not configure parser JAXP DocumentBuilderFactory: " + ex, null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected XmlPullParser newParser() throws XmlPullParserException {
/*  70 */     XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/*  71 */     return factory.newPullParser();
/*     */   }
/*     */   
/*     */   public Element parse(Reader reader) throws XmlPullParserException, IOException {
/*  75 */     Document docFactory = newDoc();
/*  76 */     return parse(reader, docFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Element parse(Reader reader, Document docFactory) throws XmlPullParserException, IOException {
/*  82 */     XmlPullParser pp = newParser();
/*  83 */     pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
/*  84 */     pp.setInput(reader);
/*  85 */     pp.next();
/*  86 */     return parse(pp, docFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Element parse(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
/*  92 */     Element root = parseSubTree(pp, docFactory);
/*  93 */     return root;
/*     */   }
/*     */   
/*     */   public Element parseSubTree(XmlPullParser pp) throws XmlPullParserException, IOException {
/*  97 */     Document doc = newDoc();
/*  98 */     Element root = parseSubTree(pp, doc);
/*  99 */     return root;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Element parseSubTree(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
/* 105 */     BuildProcess process = new BuildProcess();
/* 106 */     return process.parseSubTree(pp, docFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   static class BuildProcess
/*     */   {
/*     */     private XmlPullParser pp;
/*     */     
/*     */     private Document docFactory;
/*     */     
/*     */     private boolean scanNamespaces = true;
/*     */ 
/*     */     
/*     */     public Element parseSubTree(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
/* 120 */       this.pp = pp;
/* 121 */       this.docFactory = docFactory;
/* 122 */       return parseSubTree();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Element parseSubTree() throws XmlPullParserException, IOException {
/* 128 */       this.pp.require(2, null, null);
/* 129 */       String name = this.pp.getName();
/* 130 */       String ns = this.pp.getNamespace();
/* 131 */       String prefix = this.pp.getPrefix();
/* 132 */       String qname = (prefix != null) ? (prefix + ":" + name) : name;
/* 133 */       Element parent = this.docFactory.createElementNS(ns, qname);
/*     */ 
/*     */       
/* 136 */       declareNamespaces(this.pp, parent);
/*     */ 
/*     */       
/* 139 */       for (int i = 0; i < this.pp.getAttributeCount(); i++) {
/*     */         
/* 141 */         String attrNs = this.pp.getAttributeNamespace(i);
/* 142 */         String attrName = this.pp.getAttributeName(i);
/* 143 */         String attrValue = this.pp.getAttributeValue(i);
/* 144 */         if (attrNs == null || attrNs.length() == 0) {
/* 145 */           parent.setAttribute(attrName, attrValue);
/*     */         } else {
/* 147 */           String attrPrefix = this.pp.getAttributePrefix(i);
/* 148 */           String attrQname = (attrPrefix != null) ? (attrPrefix + ":" + attrName) : attrName;
/* 149 */           parent.setAttributeNS(attrNs, attrQname, attrValue);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 154 */       while (this.pp.next() != 3) {
/* 155 */         if (this.pp.getEventType() == 2) {
/* 156 */           Element el = parseSubTree(this.pp, this.docFactory);
/* 157 */           parent.appendChild(el); continue;
/* 158 */         }  if (this.pp.getEventType() == 4) {
/* 159 */           String text = this.pp.getText();
/* 160 */           Text textEl = this.docFactory.createTextNode(text);
/* 161 */           parent.appendChild(textEl); continue;
/*     */         } 
/* 163 */         throw new XmlPullParserException("unexpected event " + XmlPullParser.TYPES[this.pp.getEventType()], this.pp, null);
/*     */       } 
/*     */ 
/*     */       
/* 167 */       this.pp.require(3, ns, name);
/* 168 */       return parent;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void declareNamespaces(XmlPullParser pp, Element parent) throws DOMException, XmlPullParserException {
/* 174 */       if (this.scanNamespaces)
/* 175 */       { this.scanNamespaces = false;
/* 176 */         int top = pp.getNamespaceCount(pp.getDepth()) - 1;
/*     */ 
/*     */         
/* 179 */         for (int i = top; i >= pp.getNamespaceCount(0); i--) {
/*     */ 
/*     */           
/* 182 */           String prefix = pp.getNamespacePrefix(i);
/* 183 */           int j = top; while (true) { if (j > i) {
/*     */               
/* 185 */               String prefixJ = pp.getNamespacePrefix(j);
/* 186 */               if ((prefix != null && prefix.equals(prefixJ)) || (prefix != null && prefix == prefixJ)) {
/*     */                 break;
/*     */               }
/*     */               
/*     */               j--;
/*     */               continue;
/*     */             } 
/* 193 */             declareOneNamespace(pp, i, parent); break; }
/*     */         
/*     */         }  }
/* 196 */       else { int i = pp.getNamespaceCount(pp.getDepth() - 1);
/* 197 */         for (; i < pp.getNamespaceCount(pp.getDepth()); 
/* 198 */           i++)
/*     */         {
/* 200 */           declareOneNamespace(pp, i, parent);
/*     */         } }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     private void declareOneNamespace(XmlPullParser pp, int i, Element parent) throws DOMException, XmlPullParserException {
/* 207 */       String xmlnsPrefix = pp.getNamespacePrefix(i);
/* 208 */       String xmlnsUri = pp.getNamespaceUri(i);
/* 209 */       String xmlnsDecl = (xmlnsPrefix != null) ? ("xmlns:" + xmlnsPrefix) : "xmlns";
/* 210 */       parent.setAttributeNS("http://www.w3.org/2000/xmlns/", xmlnsDecl, xmlnsUri);
/*     */     }
/*     */     
/*     */     private BuildProcess() {}
/*     */   }
/*     */   
/*     */   private static void assertEquals(String expected, String s) {
/* 217 */     if ((expected != null && !expected.equals(s)) || (expected == null && s == null))
/* 218 */       throw new RuntimeException("expected '" + expected + "' but got '" + s + "'"); 
/*     */   }
/*     */   
/*     */   private static void assertNotNull(Object o) {
/* 222 */     if (o == null)
/* 223 */       throw new RuntimeException("expected no null value"); 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {}
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\dom2_builder\DOM2XmlPullBuilder.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */