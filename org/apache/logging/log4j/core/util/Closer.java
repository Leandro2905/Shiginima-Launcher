/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.ServerSocket;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Closer
/*     */ {
/*     */   public static void closeSilently(Closeable closeable) {
/*     */     try {
/*  44 */       close(closeable);
/*  45 */     } catch (Exception ignored) {}
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
/*     */   public static void close(Closeable closeable) throws IOException {
/*  57 */     if (closeable != null) {
/*  58 */       closeable.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeSilently(ServerSocket serverSocket) {
/*     */     try {
/*  69 */       close(serverSocket);
/*  70 */     } catch (Exception ignored) {}
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
/*     */   public static void close(ServerSocket serverSocket) throws IOException {
/*  82 */     if (serverSocket != null) {
/*  83 */       serverSocket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeSilently(DatagramSocket datagramSocket) {
/*     */     try {
/*  94 */       close(datagramSocket);
/*  95 */     } catch (Exception ignored) {}
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
/*     */   public static void close(DatagramSocket datagramSocket) throws IOException {
/* 107 */     if (datagramSocket != null) {
/* 108 */       datagramSocket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeSilently(Statement statement) {
/*     */     try {
/* 120 */       close(statement);
/* 121 */     } catch (Exception ignored) {}
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
/*     */   public static void close(Statement statement) throws SQLException {
/* 133 */     if (statement != null) {
/* 134 */       statement.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeSilently(Connection connection) {
/*     */     try {
/* 146 */       close(connection);
/* 147 */     } catch (Exception ignored) {}
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
/*     */   public static void close(Connection connection) throws SQLException {
/* 159 */     if (connection != null)
/* 160 */       connection.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Closer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */