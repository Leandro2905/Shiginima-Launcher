/*     */ package org.xmlpull.v1.builder;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ import org.xmlpull.v1.builder.impl.XmlInfosetBuilderImpl;
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
/*     */ public abstract class XmlInfosetBuilder
/*     */ {
/*     */   protected XmlPullParserFactory factory;
/*     */   
/*     */   public static XmlInfosetBuilder newInstance() throws XmlBuilderException {
/*  51 */     XmlInfosetBuilderImpl xmlInfosetBuilderImpl = new XmlInfosetBuilderImpl();
/*     */     try {
/*  53 */       ((XmlInfosetBuilder)xmlInfosetBuilderImpl).factory = XmlPullParserFactory.newInstance(System.getProperty("org.xmlpull.v1.XmlPullParserFactory"), null);
/*     */       
/*  55 */       ((XmlInfosetBuilder)xmlInfosetBuilderImpl).factory.setNamespaceAware(true);
/*     */     }
/*  57 */     catch (XmlPullParserException ex) {
/*  58 */       throw new XmlBuilderException("could not create XmlPull factory:" + ex, ex);
/*     */     } 
/*  60 */     return (XmlInfosetBuilder)xmlInfosetBuilderImpl;
/*     */   }
/*     */ 
/*     */   
/*     */   public static XmlInfosetBuilder newInstance(XmlPullParserFactory factory) throws XmlBuilderException {
/*  65 */     if (factory == null) throw new IllegalArgumentException(); 
/*  66 */     XmlInfosetBuilderImpl xmlInfosetBuilderImpl = new XmlInfosetBuilderImpl();
/*  67 */     ((XmlInfosetBuilder)xmlInfosetBuilderImpl).factory = factory;
/*     */     
/*  69 */     ((XmlInfosetBuilder)xmlInfosetBuilderImpl).factory.setNamespaceAware(true);
/*     */ 
/*     */ 
/*     */     
/*  73 */     return (XmlInfosetBuilder)xmlInfosetBuilderImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlPullParserFactory getFactory() throws XmlBuilderException {
/*  80 */     return this.factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlDocument newDocument() throws XmlBuilderException {
/*  88 */     return newDocument(null, null, null);
/*     */   }
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
/*     */   public abstract XmlDocument newDocument(String paramString1, Boolean paramBoolean, String paramString2) throws XmlBuilderException;
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
/*     */   public abstract XmlElement newFragment(String paramString) throws XmlBuilderException;
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
/*     */   public abstract XmlElement newFragment(String paramString1, String paramString2) throws XmlBuilderException;
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
/*     */   public abstract XmlElement newFragment(XmlNamespace paramXmlNamespace, String paramString) throws XmlBuilderException;
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
/*     */   public abstract XmlNamespace newNamespace(String paramString) throws XmlBuilderException;
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
/*     */   public abstract XmlNamespace newNamespace(String paramString1, String paramString2) throws XmlBuilderException;
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
/*     */   public abstract XmlDocument parse(XmlPullParser paramXmlPullParser) throws XmlBuilderException;
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
/*     */   public abstract Object parseItem(XmlPullParser paramXmlPullParser) throws XmlBuilderException;
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
/*     */   public abstract XmlElement parseStartTag(XmlPullParser paramXmlPullParser) throws XmlBuilderException;
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
/*     */   public XmlDocument parseInputStream(InputStream is) throws XmlBuilderException {
/* 210 */     XmlPullParser pp = null;
/*     */     try {
/* 212 */       pp = this.factory.newPullParser();
/* 213 */       pp.setInput(is, null);
/*     */     }
/* 215 */     catch (XmlPullParserException e) {
/* 216 */       throw new XmlBuilderException("could not start parsing input stream", e);
/*     */     } 
/* 218 */     return parse(pp);
/*     */   }
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
/*     */   public XmlDocument parseInputStream(InputStream is, String encoding) throws XmlBuilderException {
/* 234 */     XmlPullParser pp = null;
/*     */     try {
/* 236 */       pp = this.factory.newPullParser();
/* 237 */       pp.setInput(is, encoding);
/*     */     }
/* 239 */     catch (XmlPullParserException e) {
/* 240 */       throw new XmlBuilderException("could not start parsing input stream (encoding=" + encoding + ")", e);
/*     */     } 
/* 242 */     return parse(pp);
/*     */   }
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
/*     */   public XmlDocument parseReader(Reader reader) throws XmlBuilderException {
/* 257 */     XmlPullParser pp = null;
/*     */     try {
/* 259 */       pp = this.factory.newPullParser();
/* 260 */       pp.setInput(reader);
/*     */     }
/* 262 */     catch (XmlPullParserException e) {
/* 263 */       throw new XmlBuilderException("could not start parsing input from reader", e);
/*     */     } 
/* 265 */     return parse(pp);
/*     */   }
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
/*     */   public abstract XmlDocument parseLocation(String paramString) throws XmlBuilderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract XmlElement parseFragment(XmlPullParser paramXmlPullParser) throws XmlBuilderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement parseFragmentFromInputStream(InputStream is) throws XmlBuilderException {
/* 298 */     XmlPullParser pp = null;
/*     */     try {
/* 300 */       pp = this.factory.newPullParser();
/* 301 */       pp.setInput(is, null);
/*     */       
/*     */       try {
/* 304 */         pp.nextTag();
/* 305 */       } catch (IOException e) {
/* 306 */         throw new XmlBuilderException("IO error when starting to parse input stream", e);
/*     */       }
/*     */     
/* 309 */     } catch (XmlPullParserException e) {
/* 310 */       throw new XmlBuilderException("could not start parsing input stream", e);
/*     */     } 
/* 312 */     return parseFragment(pp);
/*     */   }
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
/*     */   public XmlElement parseFragementFromInputStream(InputStream is, String encoding) throws XmlBuilderException {
/* 328 */     XmlPullParser pp = null;
/*     */     try {
/* 330 */       pp = this.factory.newPullParser();
/* 331 */       pp.setInput(is, encoding);
/*     */       
/*     */       try {
/* 334 */         pp.nextTag();
/* 335 */       } catch (IOException e) {
/* 336 */         throw new XmlBuilderException("IO error when starting to parse input stream (encoding=" + encoding + ")", e);
/*     */       }
/*     */     
/* 339 */     } catch (XmlPullParserException e) {
/* 340 */       throw new XmlBuilderException("could not start parsing input stream (encoding=" + encoding + ")", e);
/*     */     } 
/* 342 */     return parseFragment(pp);
/*     */   }
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
/*     */   public XmlElement parseFragmentFromReader(Reader reader) throws XmlBuilderException {
/* 357 */     XmlPullParser pp = null;
/*     */     try {
/* 359 */       pp = this.factory.newPullParser();
/* 360 */       pp.setInput(reader);
/*     */       
/*     */       try {
/* 363 */         pp.nextTag();
/* 364 */       } catch (IOException e) {
/* 365 */         throw new XmlBuilderException("IO error when starting to parse from reader", e);
/*     */       }
/*     */     
/* 368 */     } catch (XmlPullParserException e) {
/* 369 */       throw new XmlBuilderException("could not start parsing input from reader", e);
/*     */     } 
/* 371 */     return parseFragment(pp);
/*     */   }
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
/*     */   public void skipSubTree(XmlPullParser pp) throws XmlBuilderException {
/*     */     try {
/* 386 */       pp.require(2, null, null);
/* 387 */       int level = 1;
/* 388 */       while (level > 0) {
/* 389 */         int eventType = pp.next();
/* 390 */         if (eventType == 3) {
/* 391 */           level--; continue;
/* 392 */         }  if (eventType == 2) {
/* 393 */           level++;
/*     */         }
/*     */       } 
/* 396 */     } catch (XmlPullParserException e) {
/* 397 */       throw new XmlBuilderException("could not skip subtree", e);
/* 398 */     } catch (IOException e) {
/* 399 */       throw new XmlBuilderException("IO error when skipping subtree", e);
/*     */     } 
/*     */   }
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
/*     */   public abstract void serializeStartTag(XmlElement paramXmlElement, XmlSerializer paramXmlSerializer) throws XmlBuilderException;
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
/*     */   public abstract void serializeEndTag(XmlElement paramXmlElement, XmlSerializer paramXmlSerializer) throws XmlBuilderException;
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
/*     */   public abstract void serialize(Object paramObject, XmlSerializer paramXmlSerializer) throws XmlBuilderException;
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
/*     */   public abstract void serializeItem(Object paramObject, XmlSerializer paramXmlSerializer) throws XmlBuilderException;
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
/*     */   public void serializeToOutputStream(Object item, OutputStream os) throws XmlBuilderException {
/* 457 */     serializeToOutputStream(item, os, "UTF8");
/*     */   }
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
/*     */   public void serializeToOutputStream(Object item, OutputStream os, String encoding) throws XmlBuilderException {
/* 478 */     XmlSerializer ser = null;
/*     */     try {
/* 480 */       ser = this.factory.newSerializer();
/* 481 */       ser.setOutput(os, encoding);
/* 482 */     } catch (Exception e) {
/* 483 */       throw new XmlBuilderException("could not serialize node to output stream (encoding=" + encoding + ")", e);
/*     */     } 
/*     */     
/* 486 */     serialize(item, ser);
/*     */     try {
/* 488 */       ser.flush();
/* 489 */     } catch (IOException e) {
/* 490 */       throw new XmlBuilderException("could not flush output", e);
/*     */     } 
/*     */   }
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
/*     */   public void serializeToWriter(Object item, Writer writer) throws XmlBuilderException {
/* 508 */     XmlSerializer ser = null;
/*     */     try {
/* 510 */       ser = this.factory.newSerializer();
/* 511 */       ser.setOutput(writer);
/* 512 */     } catch (Exception e) {
/* 513 */       throw new XmlBuilderException("could not serialize node to writer", e);
/*     */     } 
/* 515 */     serialize(item, ser);
/*     */     try {
/* 517 */       ser.flush();
/* 518 */     } catch (IOException e) {
/* 519 */       throw new XmlBuilderException("could not flush output", e);
/*     */     } 
/*     */   }
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
/*     */   public String serializeToString(Object item) throws XmlBuilderException {
/* 537 */     StringWriter sw = new StringWriter();
/* 538 */     serializeToWriter(item, sw);
/* 539 */     return sw.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlInfosetBuilder.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */