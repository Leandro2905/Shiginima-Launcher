/*     */ package org.xmlpull.mxp1;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class MXParserNonValidating
/*     */   extends MXParserCachingStrings
/*     */ {
/*     */   private boolean processDocDecl;
/*     */   
/*     */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/*  36 */     if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
/*  37 */       if (this.eventType != 0) throw new XmlPullParserException("process DOCDECL feature can only be changed before parsing", this, null);
/*     */       
/*  39 */       this.processDocDecl = state;
/*  40 */       if (!state);
/*     */     }
/*     */     else {
/*     */       
/*  44 */       super.setFeature(name, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) {
/*  50 */     if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
/*  51 */       return this.processDocDecl;
/*     */     }
/*  53 */     return super.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char more() throws IOException, XmlPullParserException {
/*  60 */     return super.more();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] lookuEntityReplacement(int entitNameLen) throws XmlPullParserException, IOException {
/*  67 */     if (!this.allStringsInterned) {
/*  68 */       int hash = MXParser.fastHash(this.buf, this.posStart, this.posEnd - this.posStart);
/*     */       int i;
/*  70 */       label30: for (i = this.entityEnd - 1; i >= 0; i--) {
/*     */         
/*  72 */         if (hash == this.entityNameHash[i] && entitNameLen == (this.entityNameBuf[i]).length) {
/*  73 */           char[] entityBuf = this.entityNameBuf[i];
/*  74 */           for (int j = 0; j < entitNameLen; j++) {
/*     */             
/*  76 */             if (this.buf[this.posStart + j] != entityBuf[j])
/*     */               continue label30; 
/*  78 */           }  if (this.tokenize) this.text = this.entityReplacement[i]; 
/*  79 */           return this.entityReplacementBuf[i];
/*     */         } 
/*     */       } 
/*     */     } else {
/*  83 */       this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
/*  84 */       for (int i = this.entityEnd - 1; i >= 0; i--) {
/*     */ 
/*     */         
/*  87 */         if (this.entityRefName == this.entityName[i]) {
/*  88 */           if (this.tokenize) this.text = this.entityReplacement[i]; 
/*  89 */           return this.entityReplacementBuf[i];
/*     */         } 
/*     */       } 
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseDocdecl() throws XmlPullParserException, IOException {
/* 101 */     boolean oldTokenize = this.tokenize;
/*     */     
/*     */     try {
/* 104 */       char ch = more();
/* 105 */       if (ch != 'O') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 107 */       ch = more();
/* 108 */       if (ch != 'C') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 110 */       ch = more();
/* 111 */       if (ch != 'T') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 113 */       ch = more();
/* 114 */       if (ch != 'Y') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 116 */       ch = more();
/* 117 */       if (ch != 'P') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 119 */       ch = more();
/* 120 */       if (ch != 'E') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*     */       
/* 122 */       this.posStart = this.pos;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       ch = requireNextS();
/* 128 */       int nameStart = this.pos;
/* 129 */       ch = readName(ch);
/* 130 */       int nameEnd = this.pos;
/* 131 */       ch = skipS(ch);
/*     */       
/* 133 */       if (ch == 'S' || ch == 'P') {
/* 134 */         ch = processExternalId(ch);
/* 135 */         ch = skipS(ch);
/*     */       } 
/* 137 */       if (ch == '[') {
/* 138 */         processInternalSubset();
/*     */       }
/* 140 */       ch = skipS(ch);
/* 141 */       if (ch != '>') {
/* 142 */         throw new XmlPullParserException("expected > to finish <[DOCTYPE but got " + printable(ch), this, null);
/*     */       }
/*     */       
/* 145 */       this.posEnd = this.pos - 1;
/*     */     } finally {
/* 147 */       this.tokenize = oldTokenize;
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
/*     */   protected char processExternalId(char ch) throws XmlPullParserException, IOException {
/* 160 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processInternalSubset() throws XmlPullParserException, IOException {
/*     */     while (true) {
/* 171 */       char ch = more();
/* 172 */       if (ch == ']')
/* 173 */         break;  if (ch == '%') {
/* 174 */         processPEReference(); continue;
/* 175 */       }  if (isS(ch)) {
/* 176 */         ch = skipS(ch); continue;
/*     */       } 
/* 178 */       processMarkupDecl(ch);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processPEReference() throws XmlPullParserException, IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processMarkupDecl(char ch) throws XmlPullParserException, IOException {
/* 196 */     if (ch != '<') {
/* 197 */       throw new XmlPullParserException("expected < for markupdecl in DTD not " + printable(ch), this, null);
/*     */     }
/*     */     
/* 200 */     ch = more();
/* 201 */     if (ch == '?') {
/* 202 */       parsePI();
/* 203 */     } else if (ch == '!') {
/* 204 */       ch = more();
/* 205 */       if (ch == '-') {
/*     */         
/* 207 */         parseComment();
/*     */       } else {
/* 209 */         ch = more();
/* 210 */         if (ch == 'A') {
/* 211 */           processAttlistDecl(ch);
/* 212 */         } else if (ch == 'E') {
/* 213 */           ch = more();
/* 214 */           if (ch == 'L') {
/* 215 */             processElementDecl(ch);
/* 216 */           } else if (ch == 'N') {
/* 217 */             processEntityDecl(ch);
/*     */           } else {
/* 219 */             throw new XmlPullParserException("expected ELEMENT or ENTITY after <! in DTD not " + printable(ch), this, null);
/*     */           }
/*     */         
/*     */         }
/* 223 */         else if (ch == 'N') {
/* 224 */           processNotationDecl(ch);
/*     */         } else {
/* 226 */           throw new XmlPullParserException("expected markupdecl after <! in DTD not " + printable(ch), this, null);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 232 */       throw new XmlPullParserException("expected markupdecl in DTD not " + printable(ch), this, null);
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
/*     */   
/*     */   protected void processElementDecl(char ch) throws XmlPullParserException, IOException {
/* 251 */     ch = requireNextS();
/* 252 */     readName(ch);
/* 253 */     ch = requireNextS();
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
/*     */   protected void processAttlistDecl(char ch) throws XmlPullParserException, IOException {}
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
/*     */   protected void processEntityDecl(char ch) throws XmlPullParserException, IOException {}
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
/*     */   protected void processNotationDecl(char ch) throws XmlPullParserException, IOException {}
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
/*     */   protected char readName(char ch) throws XmlPullParserException, IOException {
/* 310 */     if (isNameStartChar(ch)) {
/* 311 */       throw new XmlPullParserException("XML name must start with name start character not " + printable(ch), this, null);
/*     */     }
/*     */     
/* 314 */     while (isNameChar(ch)) {
/* 315 */       ch = more();
/*     */     }
/* 317 */     return ch;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\mxp1\MXParserNonValidating.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */