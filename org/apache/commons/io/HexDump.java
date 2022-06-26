/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HexDump
/*     */ {
/*     */   public static void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
/*  76 */     if (index < 0 || index >= data.length) {
/*  77 */       throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (stream == null) {
/*  82 */       throw new IllegalArgumentException("cannot write to nullstream");
/*     */     }
/*  84 */     long display_offset = offset + index;
/*  85 */     StringBuilder buffer = new StringBuilder(74);
/*     */     
/*  87 */     for (int j = index; j < data.length; j += 16) {
/*  88 */       int chars_read = data.length - j;
/*     */       
/*  90 */       if (chars_read > 16) {
/*  91 */         chars_read = 16;
/*     */       }
/*  93 */       dump(buffer, display_offset).append(' '); int k;
/*  94 */       for (k = 0; k < 16; k++) {
/*  95 */         if (k < chars_read) {
/*  96 */           dump(buffer, data[k + j]);
/*     */         } else {
/*  98 */           buffer.append("  ");
/*     */         } 
/* 100 */         buffer.append(' ');
/*     */       } 
/* 102 */       for (k = 0; k < chars_read; k++) {
/* 103 */         if (data[k + j] >= 32 && data[k + j] < Byte.MAX_VALUE) {
/* 104 */           buffer.append((char)data[k + j]);
/*     */         } else {
/* 106 */           buffer.append('.');
/*     */         } 
/*     */       } 
/* 109 */       buffer.append(EOL);
/* 110 */       stream.write(buffer.toString().getBytes());
/* 111 */       stream.flush();
/* 112 */       buffer.setLength(0);
/* 113 */       display_offset += chars_read;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final String EOL = System.getProperty("line.separator");
/*     */   
/* 122 */   private static final char[] _hexcodes = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static final int[] _shifts = new int[] { 28, 24, 20, 16, 12, 8, 4, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder dump(StringBuilder _lbuffer, long value) {
/* 140 */     for (int j = 0; j < 8; j++) {
/* 141 */       _lbuffer.append(_hexcodes[(int)(value >> _shifts[j]) & 0xF]);
/*     */     }
/*     */     
/* 144 */     return _lbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder dump(StringBuilder _cbuffer, byte value) {
/* 155 */     for (int j = 0; j < 2; j++) {
/* 156 */       _cbuffer.append(_hexcodes[value >> _shifts[j + 6] & 0xF]);
/*     */     }
/* 158 */     return _cbuffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\HexDump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */