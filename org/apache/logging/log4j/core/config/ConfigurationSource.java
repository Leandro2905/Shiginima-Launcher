/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationSource
/*     */ {
/*  34 */   public static final ConfigurationSource NULL_SOURCE = new ConfigurationSource(new byte[0]);
/*     */ 
/*     */   
/*     */   private final File file;
/*     */ 
/*     */   
/*     */   private final URL url;
/*     */   
/*     */   private final String location;
/*     */   
/*     */   private final InputStream stream;
/*     */   
/*     */   private final byte[] data;
/*     */ 
/*     */   
/*     */   private static byte[] toByteArray(InputStream inputStream) throws IOException {
/*  50 */     int buffSize = Math.max(4096, inputStream.available());
/*  51 */     ByteArrayOutputStream contents = new ByteArrayOutputStream(buffSize);
/*  52 */     byte[] buff = new byte[buffSize];
/*     */     
/*  54 */     int length = inputStream.read(buff);
/*  55 */     while (length > 0) {
/*  56 */       contents.write(buff, 0, length);
/*  57 */       length = inputStream.read(buff);
/*     */     } 
/*  59 */     return contents.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream) throws IOException {
/*  70 */     this(toByteArray(stream));
/*     */   }
/*     */   
/*     */   private ConfigurationSource(byte[] data) {
/*  74 */     this.data = (byte[])Assert.requireNonNull(data, "data is null");
/*  75 */     this.stream = new ByteArrayInputStream(data);
/*  76 */     this.file = null;
/*  77 */     this.url = null;
/*  78 */     this.location = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream, File file) {
/*  89 */     this.stream = (InputStream)Assert.requireNonNull(stream, "stream is null");
/*  90 */     this.file = (File)Assert.requireNonNull(file, "file is null");
/*  91 */     this.location = file.getAbsolutePath();
/*  92 */     this.url = null;
/*  93 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream, URL url) {
/* 104 */     this.stream = (InputStream)Assert.requireNonNull(stream, "stream is null");
/* 105 */     this.url = (URL)Assert.requireNonNull(url, "URL is null");
/* 106 */     this.location = url.toString();
/* 107 */     this.file = null;
/* 108 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 118 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 128 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 138 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/* 147 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource resetInputStream() throws IOException {
/* 157 */     if (this.file != null)
/* 158 */       return new ConfigurationSource(new FileInputStream(this.file), this.file); 
/* 159 */     if (this.url != null) {
/* 160 */       return new ConfigurationSource(this.url.openStream(), this.url);
/*     */     }
/* 162 */     return new ConfigurationSource(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     if (this.location != null) {
/* 169 */       return this.location;
/*     */     }
/* 171 */     if (this == NULL_SOURCE) {
/* 172 */       return "NULL_SOURCE";
/*     */     }
/* 174 */     int length = (this.data == null) ? -1 : this.data.length;
/* 175 */     return "stream (" + length + " bytes, unknown location)";
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\ConfigurationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */