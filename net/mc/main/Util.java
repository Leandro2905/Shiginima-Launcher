/*     */ package net.mc.main;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*  26 */   private static File workDir = null;
/*     */   
/*     */   public static File getWorkingDirectory() {
/*  29 */     if (workDir == null) {
/*  30 */       workDir = getWorkingDirectory("minecraft");
/*     */     }
/*  32 */     return workDir;
/*     */   }
/*     */   public static File getWorkingDirectory(String applicationName) {
/*     */     File workingDirectory;
/*  36 */     String applicationData, userHome = System.getProperty("user.home", ".");
/*     */     
/*  38 */     switch (getPlatform().ordinal()) {
/*     */       case 0:
/*     */       case 1:
/*  41 */         workingDirectory = new File(userHome, '.' + applicationName + '/');
/*     */         break;
/*     */       case 2:
/*  44 */         applicationData = System.getenv("APPDATA");
/*  45 */         if (applicationData != null) {
/*  46 */           workingDirectory = new File(applicationData, "." + applicationName + '/'); break;
/*     */         } 
/*  48 */         workingDirectory = new File(userHome, '.' + applicationName + '/');
/*     */         break;
/*     */       case 3:
/*  51 */         workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
/*     */         break;
/*     */       default:
/*  54 */         workingDirectory = new File(userHome, applicationName + '/'); break;
/*     */     } 
/*  56 */     if (!workingDirectory.exists() && !workingDirectory.mkdirs()) throw new RuntimeException("The working directory could not be created: " + workingDirectory); 
/*  57 */     return workingDirectory;
/*     */   }
/*     */   
/*     */   private static OS getPlatform() {
/*  61 */     String osName = System.getProperty("os.name").toLowerCase();
/*  62 */     if (osName.contains("win")) return OS.windows; 
/*  63 */     if (osName.contains("mac")) return OS.macos; 
/*  64 */     if (osName.contains("solaris")) return OS.solaris; 
/*  65 */     if (osName.contains("sunos")) return OS.solaris; 
/*  66 */     if (osName.contains("linux")) return OS.linux; 
/*  67 */     if (osName.contains("unix")) return OS.linux; 
/*  68 */     return OS.unknown;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String excutePost(String targetURL, String urlParameters) {
/*  74 */     HttpURLConnection connection = null;
/*     */     
/*     */     try {
/*  77 */       URL url = new URL(targetURL);
/*     */       
/*  79 */       connection = (HttpURLConnection)url.openConnection();
/*  80 */       connection.setRequestMethod("POST");
/*  81 */       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*     */       
/*  83 */       connection.setRequestProperty("Content-Length", Integer.toString((urlParameters.getBytes()).length));
/*  84 */       connection.setRequestProperty("Content-Language", "en-US");
/*     */       
/*  86 */       connection.setUseCaches(false);
/*  87 */       connection.setDoInput(true);
/*  88 */       connection.setDoOutput(true);
/*     */       
/*  90 */       connection.connect();
/*     */ 
/*     */       
/*  93 */       byte[] bytes = new byte[294];
/*  94 */       DataInputStream dis = new DataInputStream(Util.class.getResourceAsStream("minecraft.key"));
/*  95 */       dis.readFully(bytes);
/*  96 */       dis.close();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
/* 107 */       wr.writeBytes(urlParameters);
/* 108 */       wr.flush();
/* 109 */       wr.close();
/*     */       
/* 111 */       InputStream is = connection.getInputStream();
/* 112 */       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
/*     */       
/* 114 */       StringBuffer response = new StringBuffer();
/*     */       String line;
/* 116 */       while ((line = rd.readLine()) != null) {
/*     */         
/* 118 */         response.append(line);
/* 119 */         response.append('\r');
/*     */       } 
/* 121 */       rd.close();
/*     */       
/* 123 */       String str1 = response.toString();
/* 124 */       return str1;
/*     */     }
/* 126 */     catch (Exception e) {
/*     */       
/* 128 */       e.printStackTrace();
/* 129 */       return null;
/*     */     }
/*     */     finally {
/*     */       
/* 133 */       if (connection != null)
/* 134 */         connection.disconnect(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String str) {
/* 139 */     return (str == null || str.length() == 0);
/*     */   }
/*     */   
/*     */   public static void openLink(URI uri) {
/*     */     try {
/* 144 */       Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 145 */       o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { uri });
/* 146 */     } catch (Throwable e) {
/* 147 */       System.out.println("Failed to open link " + uri.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetVersion() {
/* 154 */     DataOutputStream dos = null;
/*     */     try {
/* 156 */       File dir = new File(getWorkingDirectory() + File.separator + "bin" + File.separator);
/* 157 */       File versionFile = new File(dir, "version");
/* 158 */       dos = new DataOutputStream(new FileOutputStream(versionFile));
/* 159 */       dos.writeUTF("0");
/* 160 */       dos.close();
/* 161 */     } catch (FileNotFoundException ex) {
/* 162 */       Logger.getLogger(Util.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 163 */     } catch (IOException ex) {
/* 164 */       Logger.getLogger(Util.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } finally {
/*     */       try {
/* 167 */         dos.close();
/* 168 */       } catch (IOException ex) {
/* 169 */         Logger.getLogger(Util.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getFakeLatestVersion() {
/*     */     
/* 176 */     try { File dir = new File(getWorkingDirectory() + File.separator + "bin" + File.separator);
/* 177 */       File file = new File(dir, "version");
/* 178 */       DataInputStream dis = new DataInputStream(new FileInputStream(file));
/* 179 */       String version = dis.readUTF();
/* 180 */       dis.close();
/* 181 */       if (version.equals("0")) {
/* 182 */         return "1285241960000";
/*     */       }
/* 184 */       return version; } catch (IOException iOException)
/*     */     
/* 186 */     { return "1285241960000"; }
/*     */   
/*     */   }
/*     */   
/*     */   private enum OS
/*     */   {
/* 192 */     linux, solaris, windows, macos, unknown;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\mc\main\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */