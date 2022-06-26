/*    */ package net.minecraft.launcher;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Enumeration;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
/*    */ 
/*    */ public class PackageEditor
/*    */ {
/*    */   public static void main(String[] args) throws IOException {
/* 14 */     unpackJar("./src/dest", "./src/a.jar");
/*    */   }
/*    */   
/*    */   public static void unpackJar(String destinationDir, String jarPath) throws IOException {
/* 18 */     File file = new File(jarPath);
/* 19 */     JarFile jar = new JarFile(file);
/*    */     Enumeration<JarEntry> enums;
/* 21 */     for (enums = jar.entries(); enums.hasMoreElements(); ) {
/* 22 */       JarEntry entry = enums.nextElement();
/*    */       
/* 24 */       String fileName = destinationDir + File.separator + entry.getName();
/* 25 */       File f = new File(fileName);
/*    */       
/* 27 */       if (fileName.endsWith("/")) {
/* 28 */         f.mkdirs();
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 33 */     for (enums = jar.entries(); enums.hasMoreElements(); ) {
/* 34 */       JarEntry entry = enums.nextElement();
/*    */       
/* 36 */       String fileName = destinationDir + File.separator + entry.getName();
/* 37 */       File f = new File(fileName);
/*    */       
/* 39 */       if (!fileName.endsWith("/")) {
/* 40 */         InputStream is = jar.getInputStream(entry);
/* 41 */         FileOutputStream fos = new FileOutputStream(f);
/*    */         
/* 43 */         while (is.available() > 0) {
/* 44 */           fos.write(is.read());
/*    */         }
/*    */         
/* 47 */         fos.close();
/* 48 */         is.close();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\PackageEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */