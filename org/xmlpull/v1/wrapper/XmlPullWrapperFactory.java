/*     */ package org.xmlpull.v1.wrapper;
/*     */ 
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ import org.xmlpull.v1.wrapper.classic.StaticXmlPullParserWrapper;
/*     */ import org.xmlpull.v1.wrapper.classic.StaticXmlSerializerWrapper;
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
/*     */ public class XmlPullWrapperFactory
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   protected XmlPullParserFactory f;
/*     */   
/*     */   public static XmlPullWrapperFactory newInstance() throws XmlPullParserException {
/*  29 */     return new XmlPullWrapperFactory(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlPullWrapperFactory newInstance(XmlPullParserFactory factory) throws XmlPullParserException {
/*  35 */     return new XmlPullWrapperFactory(factory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlPullWrapperFactory newInstance(String classNames, Class context) throws XmlPullParserException {
/*  41 */     XmlPullParserFactory factory = XmlPullParserFactory.newInstance(classNames, context);
/*  42 */     return new XmlPullWrapperFactory(factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XmlPullWrapperFactory(XmlPullParserFactory factory) throws XmlPullParserException {
/*  49 */     if (factory != null) {
/*  50 */       this.f = factory;
/*     */     } else {
/*  52 */       this.f = XmlPullParserFactory.newInstance();
/*     */     } 
/*     */   }
/*     */   
/*     */   public XmlPullParserFactory getFactory() throws XmlPullParserException {
/*  57 */     return this.f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/*  63 */     this.f.setFeature(name, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) {
/*  68 */     return this.f.getFeature(name);
/*     */   }
/*     */   
/*     */   public void setNamespaceAware(boolean awareness) {
/*  72 */     this.f.setNamespaceAware(awareness);
/*     */   }
/*     */   
/*     */   public boolean isNamespaceAware() {
/*  76 */     return this.f.isNamespaceAware();
/*     */   }
/*     */   
/*     */   public void setValidating(boolean validating) {
/*  80 */     this.f.setValidating(validating);
/*     */   }
/*     */   
/*     */   public boolean isValidating() {
/*  84 */     return this.f.isValidating();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlPullParserWrapper newPullParserWrapper() throws XmlPullParserException {
/*  90 */     XmlPullParser pp = this.f.newPullParser();
/*     */ 
/*     */ 
/*     */     
/*  94 */     return (XmlPullParserWrapper)new StaticXmlPullParserWrapper(pp);
/*     */   }
/*     */   
/*     */   public XmlPullParserWrapper newPullParserWrapper(XmlPullParser pp) throws XmlPullParserException {
/*  98 */     return (XmlPullParserWrapper)new StaticXmlPullParserWrapper(pp);
/*     */   }
/*     */   
/*     */   public XmlSerializerWrapper newSerializerWrapper() throws XmlPullParserException {
/* 102 */     XmlSerializer xs = this.f.newSerializer();
/* 103 */     return (XmlSerializerWrapper)new StaticXmlSerializerWrapper(xs, this);
/*     */   }
/*     */   
/*     */   public XmlSerializerWrapper newSerializerWrapper(XmlSerializer xs) throws XmlPullParserException {
/* 107 */     return (XmlSerializerWrapper)new StaticXmlSerializerWrapper(xs, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\XmlPullWrapperFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */