/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class EndianUtils
/*     */ {
/*     */   public static short swapShort(short value) {
/*  56 */     return (short)(((value >> 0 & 0xFF) << 8) + ((value >> 8 & 0xFF) << 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int swapInteger(int value) {
/*  66 */     return ((value >> 0 & 0xFF) << 24) + ((value >> 8 & 0xFF) << 16) + ((value >> 16 & 0xFF) << 8) + ((value >> 24 & 0xFF) << 0);
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
/*     */   public static long swapLong(long value) {
/*  79 */     return ((value >> 0L & 0xFFL) << 56L) + ((value >> 8L & 0xFFL) << 48L) + ((value >> 16L & 0xFFL) << 40L) + ((value >> 24L & 0xFFL) << 32L) + ((value >> 32L & 0xFFL) << 24L) + ((value >> 40L & 0xFFL) << 16L) + ((value >> 48L & 0xFFL) << 8L) + ((value >> 56L & 0xFFL) << 0L);
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
/*     */   public static float swapFloat(float value) {
/*  96 */     return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double swapDouble(double value) {
/* 105 */     return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
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
/*     */   public static void writeSwappedShort(byte[] data, int offset, short value) {
/* 118 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 119 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short readSwappedShort(byte[] data, int offset) {
/* 130 */     return (short)(((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
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
/*     */   public static int readSwappedUnsignedShort(byte[] data, int offset) {
/* 143 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
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
/*     */   public static void writeSwappedInteger(byte[] data, int offset, int value) {
/* 155 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 156 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 157 */     data[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 158 */     data[offset + 3] = (byte)(value >> 24 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readSwappedInteger(byte[] data, int offset) {
/* 169 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
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
/*     */   public static long readSwappedUnsignedInteger(byte[] data, int offset) {
/* 184 */     long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16));
/*     */ 
/*     */ 
/*     */     
/* 188 */     long high = (data[offset + 3] & 0xFF);
/*     */     
/* 190 */     return (high << 24L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedLong(byte[] data, int offset, long value) {
/* 201 */     data[offset + 0] = (byte)(int)(value >> 0L & 0xFFL);
/* 202 */     data[offset + 1] = (byte)(int)(value >> 8L & 0xFFL);
/* 203 */     data[offset + 2] = (byte)(int)(value >> 16L & 0xFFL);
/* 204 */     data[offset + 3] = (byte)(int)(value >> 24L & 0xFFL);
/* 205 */     data[offset + 4] = (byte)(int)(value >> 32L & 0xFFL);
/* 206 */     data[offset + 5] = (byte)(int)(value >> 40L & 0xFFL);
/* 207 */     data[offset + 6] = (byte)(int)(value >> 48L & 0xFFL);
/* 208 */     data[offset + 7] = (byte)(int)(value >> 56L & 0xFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readSwappedLong(byte[] data, int offset) {
/* 219 */     long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     long high = (((data[offset + 4] & 0xFF) << 0) + ((data[offset + 5] & 0xFF) << 8) + ((data[offset + 6] & 0xFF) << 16) + ((data[offset + 7] & 0xFF) << 24));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     return (high << 32L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedFloat(byte[] data, int offset, float value) {
/* 240 */     writeSwappedInteger(data, offset, Float.floatToIntBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float readSwappedFloat(byte[] data, int offset) {
/* 251 */     return Float.intBitsToFloat(readSwappedInteger(data, offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedDouble(byte[] data, int offset, double value) {
/* 262 */     writeSwappedLong(data, offset, Double.doubleToLongBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double readSwappedDouble(byte[] data, int offset) {
/* 273 */     return Double.longBitsToDouble(readSwappedLong(data, offset));
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
/*     */   public static void writeSwappedShort(OutputStream output, short value) throws IOException {
/* 286 */     output.write((byte)(value >> 0 & 0xFF));
/* 287 */     output.write((byte)(value >> 8 & 0xFF));
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
/*     */   public static short readSwappedShort(InputStream input) throws IOException {
/* 300 */     return (short)(((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
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
/*     */   public static int readSwappedUnsignedShort(InputStream input) throws IOException {
/* 314 */     int value1 = read(input);
/* 315 */     int value2 = read(input);
/*     */     
/* 317 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
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
/*     */   public static void writeSwappedInteger(OutputStream output, int value) throws IOException {
/* 331 */     output.write((byte)(value >> 0 & 0xFF));
/* 332 */     output.write((byte)(value >> 8 & 0xFF));
/* 333 */     output.write((byte)(value >> 16 & 0xFF));
/* 334 */     output.write((byte)(value >> 24 & 0xFF));
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
/*     */   public static int readSwappedInteger(InputStream input) throws IOException {
/* 347 */     int value1 = read(input);
/* 348 */     int value2 = read(input);
/* 349 */     int value3 = read(input);
/* 350 */     int value4 = read(input);
/*     */     
/* 352 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
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
/*     */   public static long readSwappedUnsignedInteger(InputStream input) throws IOException {
/* 368 */     int value1 = read(input);
/* 369 */     int value2 = read(input);
/* 370 */     int value3 = read(input);
/* 371 */     int value4 = read(input);
/*     */     
/* 373 */     long low = (((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16));
/*     */ 
/*     */ 
/*     */     
/* 377 */     long high = (value4 & 0xFF);
/*     */     
/* 379 */     return (high << 24L) + (0xFFFFFFFFL & low);
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
/*     */   public static void writeSwappedLong(OutputStream output, long value) throws IOException {
/* 392 */     output.write((byte)(int)(value >> 0L & 0xFFL));
/* 393 */     output.write((byte)(int)(value >> 8L & 0xFFL));
/* 394 */     output.write((byte)(int)(value >> 16L & 0xFFL));
/* 395 */     output.write((byte)(int)(value >> 24L & 0xFFL));
/* 396 */     output.write((byte)(int)(value >> 32L & 0xFFL));
/* 397 */     output.write((byte)(int)(value >> 40L & 0xFFL));
/* 398 */     output.write((byte)(int)(value >> 48L & 0xFFL));
/* 399 */     output.write((byte)(int)(value >> 56L & 0xFFL));
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
/*     */   public static long readSwappedLong(InputStream input) throws IOException {
/* 412 */     byte[] bytes = new byte[8];
/* 413 */     for (int i = 0; i < 8; i++) {
/* 414 */       bytes[i] = (byte)read(input);
/*     */     }
/* 416 */     return readSwappedLong(bytes, 0);
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
/*     */   public static void writeSwappedFloat(OutputStream output, float value) throws IOException {
/* 429 */     writeSwappedInteger(output, Float.floatToIntBits(value));
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
/*     */   public static float readSwappedFloat(InputStream input) throws IOException {
/* 442 */     return Float.intBitsToFloat(readSwappedInteger(input));
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
/*     */   public static void writeSwappedDouble(OutputStream output, double value) throws IOException {
/* 455 */     writeSwappedLong(output, Double.doubleToLongBits(value));
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
/*     */   public static double readSwappedDouble(InputStream input) throws IOException {
/* 468 */     return Double.longBitsToDouble(readSwappedLong(input));
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
/*     */   private static int read(InputStream input) throws IOException {
/* 480 */     int value = input.read();
/*     */     
/* 482 */     if (-1 == value) {
/* 483 */       throw new EOFException("Unexpected EOF reached");
/*     */     }
/*     */     
/* 486 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\EndianUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */