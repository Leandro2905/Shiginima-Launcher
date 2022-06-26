/*     */ package org.xmlpull.v1.wrapper.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
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
/*     */ public class XmlPullParserDelegate
/*     */   implements XmlPullParser
/*     */ {
/*     */   protected XmlPullParser pp;
/*     */   
/*     */   public XmlPullParserDelegate(XmlPullParser pp) {
/*  25 */     this.pp = pp;
/*     */   }
/*     */   
/*     */   public String getText() {
/*  29 */     return this.pp.getText();
/*     */   }
/*     */   
/*     */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/*  33 */     this.pp.setFeature(name, state);
/*     */   }
/*     */   
/*     */   public char[] getTextCharacters(int[] holderForStartAndLength) {
/*  37 */     return this.pp.getTextCharacters(holderForStartAndLength);
/*     */   }
/*     */   
/*     */   public int getColumnNumber() {
/*  41 */     return this.pp.getColumnNumber();
/*     */   }
/*     */   
/*     */   public int getNamespaceCount(int depth) throws XmlPullParserException {
/*  45 */     return this.pp.getNamespaceCount(depth);
/*     */   }
/*     */   
/*     */   public String getNamespacePrefix(int pos) throws XmlPullParserException {
/*  49 */     return this.pp.getNamespacePrefix(pos);
/*     */   }
/*     */   
/*     */   public String getAttributeName(int index) {
/*  53 */     return this.pp.getAttributeName(index);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.pp.getName();
/*     */   }
/*     */   
/*     */   public boolean getFeature(String name) {
/*  61 */     return this.pp.getFeature(name);
/*     */   }
/*     */   
/*     */   public String getInputEncoding() {
/*  65 */     return this.pp.getInputEncoding();
/*     */   }
/*     */   
/*     */   public String getAttributeValue(int index) {
/*  69 */     return this.pp.getAttributeValue(index);
/*     */   }
/*     */   
/*     */   public String getNamespace(String prefix) {
/*  73 */     return this.pp.getNamespace(prefix);
/*     */   }
/*     */   
/*     */   public void setInput(Reader in) throws XmlPullParserException {
/*  77 */     this.pp.setInput(in);
/*     */   }
/*     */   
/*     */   public int getLineNumber() {
/*  81 */     return this.pp.getLineNumber();
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) {
/*  85 */     return this.pp.getProperty(name);
/*     */   }
/*     */   
/*     */   public boolean isEmptyElementTag() throws XmlPullParserException {
/*  89 */     return this.pp.isEmptyElementTag();
/*     */   }
/*     */   
/*     */   public boolean isAttributeDefault(int index) {
/*  93 */     return this.pp.isAttributeDefault(index);
/*     */   }
/*     */   
/*     */   public String getNamespaceUri(int pos) throws XmlPullParserException {
/*  97 */     return this.pp.getNamespaceUri(pos);
/*     */   }
/*     */   
/*     */   public int next() throws XmlPullParserException, IOException {
/* 101 */     return this.pp.next();
/*     */   }
/*     */   
/*     */   public int nextToken() throws XmlPullParserException, IOException {
/* 105 */     return this.pp.nextToken();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
/* 112 */     this.pp.defineEntityReplacementText(entityName, replacementText);
/*     */   }
/*     */   
/*     */   public int getAttributeCount() {
/* 116 */     return this.pp.getAttributeCount();
/*     */   }
/*     */   
/*     */   public boolean isWhitespace() throws XmlPullParserException {
/* 120 */     return this.pp.isWhitespace();
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/* 124 */     return this.pp.getPrefix();
/*     */   }
/*     */   
/*     */   public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
/* 128 */     this.pp.require(type, namespace, name);
/*     */   }
/*     */   
/*     */   public String nextText() throws XmlPullParserException, IOException {
/* 132 */     return this.pp.nextText();
/*     */   }
/*     */   
/*     */   public String getAttributeType(int index) {
/* 136 */     return this.pp.getAttributeType(index);
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 140 */     return this.pp.getDepth();
/*     */   }
/*     */   
/*     */   public int nextTag() throws XmlPullParserException, IOException {
/* 144 */     return this.pp.nextTag();
/*     */   }
/*     */   
/*     */   public int getEventType() throws XmlPullParserException {
/* 148 */     return this.pp.getEventType();
/*     */   }
/*     */   
/*     */   public String getAttributePrefix(int index) {
/* 152 */     return this.pp.getAttributePrefix(index);
/*     */   }
/*     */   
/*     */   public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
/* 156 */     this.pp.setInput(inputStream, inputEncoding);
/*     */   }
/*     */   
/*     */   public String getAttributeValue(String namespace, String name) {
/* 160 */     return this.pp.getAttributeValue(namespace, name);
/*     */   }
/*     */   
/*     */   public void setProperty(String name, Object value) throws XmlPullParserException {
/* 164 */     this.pp.setProperty(name, value);
/*     */   }
/*     */   
/*     */   public String getPositionDescription() {
/* 168 */     return this.pp.getPositionDescription();
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 172 */     return this.pp.getNamespace();
/*     */   }
/*     */   
/*     */   public String getAttributeNamespace(int index) {
/* 176 */     return this.pp.getAttributeNamespace(index);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\classic\XmlPullParserDelegate.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */