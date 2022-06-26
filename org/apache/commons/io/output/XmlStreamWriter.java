/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.input.XmlStreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlStreamWriter
/*     */   extends Writer
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   private final OutputStream out;
/*     */   private final String defaultEncoding;
/*  47 */   private StringWriter xmlPrologWriter = new StringWriter(4096);
/*     */ 
/*     */ 
/*     */   
/*     */   private Writer writer;
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamWriter(OutputStream out) {
/*  60 */     this(out, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamWriter(OutputStream out, String defaultEncoding) {
/*  71 */     this.out = out;
/*  72 */     this.defaultEncoding = (defaultEncoding != null) ? defaultEncoding : "UTF-8";
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
/*     */   public XmlStreamWriter(File file) throws FileNotFoundException {
/*  84 */     this(file, (String)null);
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
/*     */   public XmlStreamWriter(File file, String defaultEncoding) throws FileNotFoundException {
/*  97 */     this(new FileOutputStream(file), defaultEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 106 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultEncoding() {
/* 115 */     return this.defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 125 */     if (this.writer == null) {
/* 126 */       this.encoding = this.defaultEncoding;
/* 127 */       this.writer = new OutputStreamWriter(this.out, this.encoding);
/* 128 */       this.writer.write(this.xmlPrologWriter.toString());
/*     */     } 
/* 130 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 140 */     if (this.writer != null) {
/* 141 */       this.writer.flush();
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
/*     */   private void detectEncoding(char[] cbuf, int off, int len) throws IOException {
/* 155 */     int size = len;
/* 156 */     StringBuffer xmlProlog = this.xmlPrologWriter.getBuffer();
/* 157 */     if (xmlProlog.length() + len > 4096) {
/* 158 */       size = 4096 - xmlProlog.length();
/*     */     }
/* 160 */     this.xmlPrologWriter.write(cbuf, off, size);
/*     */ 
/*     */     
/* 163 */     if (xmlProlog.length() >= 5) {
/* 164 */       if (xmlProlog.substring(0, 5).equals("<?xml")) {
/*     */         
/* 166 */         int xmlPrologEnd = xmlProlog.indexOf("?>");
/* 167 */         if (xmlPrologEnd > 0) {
/*     */           
/* 169 */           Matcher m = ENCODING_PATTERN.matcher(xmlProlog.substring(0, xmlPrologEnd));
/*     */           
/* 171 */           if (m.find()) {
/* 172 */             this.encoding = m.group(1).toUpperCase();
/* 173 */             this.encoding = this.encoding.substring(1, this.encoding.length() - 1);
/*     */           }
/*     */           else {
/*     */             
/* 177 */             this.encoding = this.defaultEncoding;
/*     */           }
/*     */         
/* 180 */         } else if (xmlProlog.length() >= 4096) {
/*     */ 
/*     */           
/* 183 */           this.encoding = this.defaultEncoding;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 188 */         this.encoding = this.defaultEncoding;
/*     */       } 
/* 190 */       if (this.encoding != null) {
/*     */         
/* 192 */         this.xmlPrologWriter = null;
/* 193 */         this.writer = new OutputStreamWriter(this.out, this.encoding);
/* 194 */         this.writer.write(xmlProlog.toString());
/* 195 */         if (len > size) {
/* 196 */           this.writer.write(cbuf, off + size, len - size);
/*     */         }
/*     */       } 
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 212 */     if (this.xmlPrologWriter != null) {
/* 213 */       detectEncoding(cbuf, off, len);
/*     */     } else {
/* 215 */       this.writer.write(cbuf, off, len);
/*     */     } 
/*     */   }
/*     */   
/* 219 */   static final Pattern ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\XmlStreamWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */