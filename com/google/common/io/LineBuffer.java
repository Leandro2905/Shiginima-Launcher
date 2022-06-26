/*     */ package com.google.common.io;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ abstract class LineBuffer
/*     */ {
/*  35 */   private StringBuilder line = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sawReturn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(char[] cbuf, int off, int len) throws IOException {
/*  51 */     int pos = off;
/*  52 */     if (this.sawReturn && len > 0)
/*     */     {
/*  54 */       if (finishLine((cbuf[pos] == '\n'))) {
/*  55 */         pos++;
/*     */       }
/*     */     }
/*     */     
/*  59 */     int start = pos;
/*  60 */     for (int end = off + len; pos < end; pos++) {
/*  61 */       switch (cbuf[pos]) {
/*     */         case '\r':
/*  63 */           this.line.append(cbuf, start, pos - start);
/*  64 */           this.sawReturn = true;
/*  65 */           if (pos + 1 < end && 
/*  66 */             finishLine((cbuf[pos + 1] == '\n'))) {
/*  67 */             pos++;
/*     */           }
/*     */           
/*  70 */           start = pos + 1;
/*     */           break;
/*     */         
/*     */         case '\n':
/*  74 */           this.line.append(cbuf, start, pos - start);
/*  75 */           finishLine(true);
/*  76 */           start = pos + 1;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/*  83 */     this.line.append(cbuf, start, off + len - start);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean finishLine(boolean sawNewline) throws IOException {
/*  88 */     handleLine(this.line.toString(), this.sawReturn ? (sawNewline ? "\r\n" : "\r") : (sawNewline ? "\n" : ""));
/*     */ 
/*     */     
/*  91 */     this.line = new StringBuilder();
/*  92 */     this.sawReturn = false;
/*  93 */     return sawNewline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finish() throws IOException {
/* 104 */     if (this.sawReturn || this.line.length() > 0)
/* 105 */       finishLine(false); 
/*     */   }
/*     */   
/*     */   protected abstract void handleLine(String paramString1, String paramString2) throws IOException;
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\LineBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */