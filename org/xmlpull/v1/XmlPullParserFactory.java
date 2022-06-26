/*     */ package org.xmlpull.v1;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
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
/*     */ public class XmlPullParserFactory
/*     */ {
/*     */   static final Class referenceContextClass;
/*     */   public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
/*     */   private static final String RESOURCE_NAME = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory";
/*     */   protected Vector parserClasses;
/*     */   protected String classNamesLocation;
/*     */   protected Vector serializerClasses;
/*     */   
/*     */   static {
/*  40 */     XmlPullParserFactory f = new XmlPullParserFactory();
/*  41 */     referenceContextClass = f.getClass();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected Hashtable features = new Hashtable();
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
/*     */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/*  91 */     this.features.put(name, new Boolean(state));
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
/*     */   public boolean getFeature(String name) {
/* 105 */     Boolean value = (Boolean)this.features.get(name);
/* 106 */     return (value != null) ? value.booleanValue() : false;
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
/*     */   public void setNamespaceAware(boolean awareness) {
/* 119 */     this.features.put("http://xmlpull.org/v1/doc/features.html#process-namespaces", new Boolean(awareness));
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
/*     */   public boolean isNamespaceAware() {
/* 132 */     return getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
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
/*     */   public void setValidating(boolean validating) {
/* 146 */     this.features.put("http://xmlpull.org/v1/doc/features.html#validation", new Boolean(validating));
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
/*     */   public boolean isValidating() {
/* 158 */     return getFeature("http://xmlpull.org/v1/doc/features.html#validation");
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
/*     */   public XmlPullParser newPullParser() throws XmlPullParserException {
/* 172 */     if (this.parserClasses == null) throw new XmlPullParserException("Factory initialization was incomplete - has not tried " + this.classNamesLocation);
/*     */ 
/*     */     
/* 175 */     if (this.parserClasses.size() == 0) throw new XmlPullParserException("No valid parser classes found in " + this.classNamesLocation);
/*     */ 
/*     */     
/* 178 */     StringBuffer issues = new StringBuffer();
/*     */     
/* 180 */     for (int i = 0; i < this.parserClasses.size(); i++) {
/* 181 */       Class ppClass = (Class)this.parserClasses.elementAt(i);
/*     */       try {
/* 183 */         XmlPullParser pp = ppClass.newInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 188 */         for (Enumeration e = this.features.keys(); e.hasMoreElements(); ) {
/* 189 */           String key = e.nextElement();
/* 190 */           Boolean value = (Boolean)this.features.get(key);
/* 191 */           if (value != null && value.booleanValue()) {
/* 192 */             pp.setFeature(key, true);
/*     */           }
/*     */         } 
/* 195 */         return pp;
/*     */       }
/* 197 */       catch (Exception ex) {
/* 198 */         issues.append(ppClass.getName() + ": " + ex.toString() + "; ");
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     throw new XmlPullParserException("could not create parser: " + issues);
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
/*     */   public XmlSerializer newSerializer() throws XmlPullParserException {
/* 218 */     if (this.serializerClasses == null) {
/* 219 */       throw new XmlPullParserException("Factory initialization incomplete - has not tried " + this.classNamesLocation);
/*     */     }
/*     */     
/* 222 */     if (this.serializerClasses.size() == 0) {
/* 223 */       throw new XmlPullParserException("No valid serializer classes found in " + this.classNamesLocation);
/*     */     }
/*     */ 
/*     */     
/* 227 */     StringBuffer issues = new StringBuffer();
/*     */     
/* 229 */     for (int i = 0; i < this.serializerClasses.size(); i++) {
/* 230 */       Class ppClass = (Class)this.serializerClasses.elementAt(i);
/*     */       try {
/* 232 */         XmlSerializer ser = ppClass.newInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 241 */         return ser;
/*     */       }
/* 243 */       catch (Exception ex) {
/* 244 */         issues.append(ppClass.getName() + ": " + ex.toString() + "; ");
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     throw new XmlPullParserException("could not create serializer: " + issues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlPullParserFactory newInstance() throws XmlPullParserException {
/* 259 */     return newInstance(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlPullParserFactory newInstance(String classNames, Class context) throws XmlPullParserException {
/* 265 */     if (context == null)
/*     */     {
/*     */ 
/*     */       
/* 269 */       context = referenceContextClass;
/*     */     }
/*     */     
/* 272 */     String classNamesLocation = null;
/*     */     
/* 274 */     if (classNames == null || classNames.length() == 0 || "DEFAULT".equals(classNames)) {
/*     */       try {
/* 276 */         InputStream is = context.getResourceAsStream("/META-INF/services/org.xmlpull.v1.XmlPullParserFactory");
/*     */         
/* 278 */         if (is == null) throw new XmlPullParserException("resource not found: /META-INF/services/org.xmlpull.v1.XmlPullParserFactory make sure that parser implementing XmlPull API is available");
/*     */ 
/*     */         
/* 281 */         StringBuffer sb = new StringBuffer();
/*     */         
/*     */         while (true) {
/* 284 */           int ch = is.read();
/* 285 */           if (ch < 0)
/* 286 */             break;  if (ch > 32)
/* 287 */             sb.append((char)ch); 
/*     */         } 
/* 289 */         is.close();
/*     */         
/* 291 */         classNames = sb.toString();
/*     */       }
/* 293 */       catch (Exception e) {
/* 294 */         throw new XmlPullParserException(null, null, e);
/*     */       } 
/* 296 */       classNamesLocation = "resource /META-INF/services/org.xmlpull.v1.XmlPullParserFactory that contained '" + classNames + "'";
/*     */     } else {
/* 298 */       classNamesLocation = "parameter classNames to newInstance() that contained '" + classNames + "'";
/*     */     } 
/*     */ 
/*     */     
/* 302 */     XmlPullParserFactory factory = null;
/* 303 */     Vector parserClasses = new Vector();
/* 304 */     Vector serializerClasses = new Vector();
/* 305 */     int pos = 0;
/*     */     
/* 307 */     while (pos < classNames.length()) {
/* 308 */       int cut = classNames.indexOf(',', pos);
/*     */       
/* 310 */       if (cut == -1) cut = classNames.length(); 
/* 311 */       String name = classNames.substring(pos, cut);
/*     */       
/* 313 */       Class candidate = null;
/* 314 */       Object instance = null;
/*     */       
/*     */       try {
/* 317 */         candidate = Class.forName(name);
/*     */         
/* 319 */         instance = candidate.newInstance();
/*     */       }
/* 321 */       catch (Exception e) {}
/*     */       
/* 323 */       if (candidate != null) {
/* 324 */         boolean recognized = false;
/* 325 */         if (instance instanceof XmlPullParser) {
/* 326 */           parserClasses.addElement(candidate);
/* 327 */           recognized = true;
/*     */         } 
/* 329 */         if (instance instanceof XmlSerializer) {
/* 330 */           serializerClasses.addElement(candidate);
/* 331 */           recognized = true;
/*     */         } 
/* 333 */         if (instance instanceof XmlPullParserFactory) {
/* 334 */           if (factory == null) {
/* 335 */             factory = (XmlPullParserFactory)instance;
/*     */           }
/* 337 */           recognized = true;
/*     */         } 
/* 339 */         if (!recognized) {
/* 340 */           throw new XmlPullParserException("incompatible class: " + name);
/*     */         }
/*     */       } 
/* 343 */       pos = cut + 1;
/*     */     } 
/*     */     
/* 346 */     if (factory == null) {
/* 347 */       factory = new XmlPullParserFactory();
/*     */     }
/* 349 */     factory.parserClasses = parserClasses;
/* 350 */     factory.serializerClasses = serializerClasses;
/* 351 */     factory.classNamesLocation = classNamesLocation;
/* 352 */     return factory;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\XmlPullParserFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */