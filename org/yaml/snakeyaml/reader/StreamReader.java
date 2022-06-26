/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.scanner.Constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamReader
/*     */ {
/*  32 */   public static final Pattern NON_PRINTABLE = Pattern.compile("[^\t\n\r -~ -퟿-�]");
/*     */   
/*     */   private String name;
/*     */   private final Reader stream;
/*  36 */   private int pointer = 0;
/*     */   private boolean eof = true;
/*     */   private String buffer;
/*  39 */   private int index = 0;
/*  40 */   private int line = 0;
/*  41 */   private int column = 0;
/*     */   private char[] data;
/*     */   
/*     */   public StreamReader(String stream) {
/*  45 */     this.name = "'string'";
/*  46 */     this.buffer = "";
/*  47 */     checkPrintable(stream);
/*  48 */     this.buffer = stream + "\000";
/*  49 */     this.stream = null;
/*  50 */     this.eof = true;
/*  51 */     this.data = null;
/*     */   }
/*     */   
/*     */   public StreamReader(Reader reader) {
/*  55 */     this.name = "'reader'";
/*  56 */     this.buffer = "";
/*  57 */     this.stream = reader;
/*  58 */     this.eof = false;
/*  59 */     this.data = new char[1024];
/*  60 */     update();
/*     */   }
/*     */   
/*     */   void checkPrintable(CharSequence data) {
/*  64 */     Matcher em = NON_PRINTABLE.matcher(data);
/*  65 */     if (em.find()) {
/*  66 */       int position = this.index + this.buffer.length() - this.pointer + em.start();
/*  67 */       throw new ReaderException(this.name, position, em.group().charAt(0), "special characters are not allowed");
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
/*     */   void checkPrintable(char[] chars, int begin, int end) {
/*  85 */     for (int i = begin; i < end; ) {
/*  86 */       char c = chars[i];
/*     */       
/*  88 */       if (isPrintable(c)) {
/*     */         i++;
/*     */         continue;
/*     */       } 
/*  92 */       int position = this.index + this.buffer.length() - this.pointer + i;
/*  93 */       throw new ReaderException(this.name, position, c, "special characters are not allowed");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(char c) {
/*  98 */     return ((c >= ' ' && c <= '~') || c == '\n' || c == '\r' || c == '\t' || c == '' || (c >= ' ' && c <= '퟿') || (c >= '' && c <= '�'));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getMark() {
/* 104 */     return new Mark(this.name, this.index, this.line, this.column, this.buffer, this.pointer);
/*     */   }
/*     */   
/*     */   public void forward() {
/* 108 */     forward(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forward(int length) {
/* 117 */     if (this.pointer + length + 1 >= this.buffer.length()) {
/* 118 */       update();
/*     */     }
/* 120 */     char ch = Character.MIN_VALUE;
/* 121 */     for (int i = 0; i < length; i++) {
/* 122 */       ch = this.buffer.charAt(this.pointer);
/* 123 */       this.pointer++;
/* 124 */       this.index++;
/* 125 */       if (Constant.LINEBR.has(ch) || (ch == '\r' && this.buffer.charAt(this.pointer) != '\n')) {
/* 126 */         this.line++;
/* 127 */         this.column = 0;
/* 128 */       } else if (ch != '﻿') {
/* 129 */         this.column++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public char peek() {
/* 135 */     return this.buffer.charAt(this.pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char peek(int index) {
/* 145 */     if (this.pointer + index + 1 > this.buffer.length()) {
/* 146 */       update();
/*     */     }
/* 148 */     return this.buffer.charAt(this.pointer + index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefix(int length) {
/* 158 */     if (this.pointer + length >= this.buffer.length()) {
/* 159 */       update();
/*     */     }
/* 161 */     if (this.pointer + length > this.buffer.length()) {
/* 162 */       return this.buffer.substring(this.pointer);
/*     */     }
/* 164 */     return this.buffer.substring(this.pointer, this.pointer + length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefixForward(int length) {
/* 171 */     String prefix = prefix(length);
/* 172 */     this.pointer += length;
/* 173 */     this.index += length;
/*     */     
/* 175 */     this.column += length;
/* 176 */     return prefix;
/*     */   }
/*     */   
/*     */   private void update() {
/* 180 */     if (!this.eof) {
/* 181 */       this.buffer = this.buffer.substring(this.pointer);
/* 182 */       this.pointer = 0;
/*     */       try {
/* 184 */         int converted = this.stream.read(this.data);
/* 185 */         if (converted > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 192 */           checkPrintable(this.data, 0, converted);
/* 193 */           this.buffer = (new StringBuilder(this.buffer.length() + converted)).append(this.buffer).append(this.data, 0, converted).toString();
/*     */         } else {
/*     */           
/* 196 */           this.eof = true;
/* 197 */           this.buffer += "\000";
/*     */         } 
/* 199 */       } catch (IOException ioe) {
/* 200 */         throw new YAMLException(ioe);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getColumn() {
/* 206 */     return this.column;
/*     */   }
/*     */   
/*     */   public Charset getEncoding() {
/* 210 */     return Charset.forName(((UnicodeReader)this.stream).getEncoding());
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 214 */     return this.index;
/*     */   }
/*     */   
/*     */   public int getLine() {
/* 218 */     return this.line;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\reader\StreamReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */