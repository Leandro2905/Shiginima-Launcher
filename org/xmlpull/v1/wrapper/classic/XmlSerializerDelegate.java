/*     */ package org.xmlpull.v1.wrapper.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import org.xmlpull.v1.XmlSerializer;
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
/*     */ public class XmlSerializerDelegate
/*     */   implements XmlSerializer
/*     */ {
/*     */   protected XmlSerializer xs;
/*     */   
/*     */   public XmlSerializerDelegate(XmlSerializer serializer) {
/*  24 */     this.xs = serializer;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  28 */     return this.xs.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix, String namespace) throws IOException, IllegalArgumentException, IllegalStateException {
/*  34 */     this.xs.setPrefix(prefix, namespace);
/*     */   }
/*     */   
/*     */   public void setOutput(OutputStream os, String encoding) throws IOException, IllegalArgumentException, IllegalStateException {
/*  38 */     this.xs.setOutput(os, encoding);
/*     */   }
/*     */   
/*     */   public void endDocument() throws IOException, IllegalArgumentException, IllegalStateException {
/*  42 */     this.xs.endDocument();
/*     */   }
/*     */   
/*     */   public void comment(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  46 */     this.xs.comment(text);
/*     */   }
/*     */   
/*     */   public int getDepth() {
/*  50 */     return this.xs.getDepth();
/*     */   }
/*     */   
/*     */   public void setProperty(String name, Object value) throws IllegalArgumentException, IllegalStateException {
/*  54 */     this.xs.setProperty(name, value);
/*     */   }
/*     */   
/*     */   public void cdsect(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  58 */     this.xs.cdsect(text);
/*     */   }
/*     */   
/*     */   public void setFeature(String name, boolean state) throws IllegalArgumentException, IllegalStateException {
/*  62 */     this.xs.setFeature(name, state);
/*     */   }
/*     */   
/*     */   public void entityRef(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  66 */     this.xs.entityRef(text);
/*     */   }
/*     */   
/*     */   public void processingInstruction(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  70 */     this.xs.processingInstruction(text);
/*     */   }
/*     */   
/*     */   public void setOutput(Writer writer) throws IOException, IllegalArgumentException, IllegalStateException {
/*  74 */     this.xs.setOutput(writer);
/*     */   }
/*     */   
/*     */   public void docdecl(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  78 */     this.xs.docdecl(text);
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  82 */     this.xs.flush();
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) {
/*  86 */     return this.xs.getProperty(name);
/*     */   }
/*     */   
/*     */   public XmlSerializer startTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
/*  90 */     return this.xs.startTag(namespace, name);
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  94 */     this.xs.ignorableWhitespace(text);
/*     */   }
/*     */   
/*     */   public XmlSerializer text(String text) throws IOException, IllegalArgumentException, IllegalStateException {
/*  98 */     return this.xs.text(text);
/*     */   }
/*     */   
/*     */   public boolean getFeature(String name) {
/* 102 */     return this.xs.getFeature(name);
/*     */   }
/*     */   
/*     */   public XmlSerializer attribute(String namespace, String name, String value) throws IOException, IllegalArgumentException, IllegalStateException {
/* 106 */     return this.xs.attribute(namespace, name, value);
/*     */   }
/*     */   
/*     */   public void startDocument(String encoding, Boolean standalone) throws IOException, IllegalArgumentException, IllegalStateException {
/* 110 */     this.xs.startDocument(encoding, standalone);
/*     */   }
/*     */   
/*     */   public String getPrefix(String namespace, boolean generatePrefix) throws IllegalArgumentException {
/* 114 */     return this.xs.getPrefix(namespace, generatePrefix);
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 118 */     return this.xs.getNamespace();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlSerializer endTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
/* 124 */     return this.xs.endTag(namespace, name);
/*     */   }
/*     */   
/*     */   public XmlSerializer text(char[] buf, int start, int len) throws IOException, IllegalArgumentException, IllegalStateException {
/* 128 */     return this.xs.text(buf, start, len);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\classic\XmlSerializerDelegate.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */