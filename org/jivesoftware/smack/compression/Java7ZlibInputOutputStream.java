/*     */ package org.jivesoftware.smack.compression;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Java7ZlibInputOutputStream
/*     */   extends XMPPInputOutputStream
/*     */ {
/*     */   private static final Method method;
/*     */   
/*     */   static {
/*  55 */     Method m = null;
/*     */     
/*  57 */     try { m = Deflater.class.getMethod("deflate", new Class[] { byte[].class, int.class, int.class, int.class }); }
/*  58 */     catch (SecurityException e) {  }
/*  59 */     catch (NoSuchMethodException e) {}
/*     */     
/*  61 */     method = m;
/*  62 */   } private static final boolean supported = (method != null); private static final int compressionLevel = -1; private static final int SYNC_FLUSH_INT = 2;
/*     */   private static final int FULL_FLUSH_INT = 3;
/*     */   
/*     */   public Java7ZlibInputOutputStream() {
/*  66 */     super("zlib");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSupported() {
/*  71 */     return supported;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream(InputStream inputStream) {
/*  76 */     return new InflaterInputStream(inputStream, new Inflater(), 512)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public int available() throws IOException
/*     */         {
/*  96 */           if (this.inf.needsInput()) {
/*  97 */             return 0;
/*     */           }
/*  99 */           return super.available();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream(OutputStream outputStream) {
/*     */     final int flushMethodInt;
/* 107 */     if (flushMethod == XMPPInputOutputStream.FlushMethod.SYNC_FLUSH) {
/* 108 */       flushMethodInt = 2;
/*     */     } else {
/* 110 */       flushMethodInt = 3;
/*     */     } 
/* 112 */     return new DeflaterOutputStream(outputStream, new Deflater(-1))
/*     */       {
/*     */         public void flush() throws IOException {
/* 115 */           if (!Java7ZlibInputOutputStream.supported) {
/* 116 */             super.flush();
/*     */             return;
/*     */           } 
/*     */           try {
/*     */             int count;
/* 121 */             while ((count = ((Integer)Java7ZlibInputOutputStream.method.invoke(this.def, new Object[] { this.buf, Integer.valueOf(0), Integer.valueOf(this.buf.length), Integer.valueOf(this.val$flushMethodInt) })).intValue()) != 0) {
/* 122 */               this.out.write(this.buf, 0, count);
/*     */             }
/* 124 */           } catch (IllegalArgumentException e) {
/* 125 */             throw new IOException("Can't flush");
/* 126 */           } catch (IllegalAccessException e) {
/* 127 */             throw new IOException("Can't flush");
/* 128 */           } catch (InvocationTargetException e) {
/* 129 */             throw new IOException("Can't flush");
/*     */           } 
/* 131 */           super.flush();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\compression\Java7ZlibInputOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */