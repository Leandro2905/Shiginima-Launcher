/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OptionalDataException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
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
/*     */ public class UdpSocketServer<T extends InputStream>
/*     */   extends AbstractSocketServer<T>
/*     */ {
/*     */   private final DatagramSocket datagramSocket;
/*     */   
/*     */   public static UdpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
/*  50 */     return new UdpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
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
/*     */   public static UdpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
/*  63 */     return new UdpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
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
/*     */   public static UdpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
/*  76 */     return new UdpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*     */     String line;
/*  88 */     if (args.length < 1 || args.length > 2) {
/*  89 */       System.err.println("Incorrect number of arguments");
/*  90 */       printUsage();
/*     */       return;
/*     */     } 
/*  93 */     int port = Integer.parseInt(args[0]);
/*  94 */     if (port <= 0 || port >= 65534) {
/*  95 */       System.err.println("Invalid port number");
/*  96 */       printUsage();
/*     */       return;
/*     */     } 
/*  99 */     if (args.length == 2 && args[1].length() > 0) {
/* 100 */       ConfigurationFactory.setConfigurationFactory((ConfigurationFactory)new AbstractSocketServer.ServerConfigurationFactory(args[1]));
/*     */     }
/* 102 */     UdpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(port);
/* 103 */     Thread server = new Thread(socketServer);
/* 104 */     server.start();
/* 105 */     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
/*     */     do {
/* 107 */       line = reader.readLine();
/* 108 */     } while (line != null && !line.equalsIgnoreCase("Quit") && !line.equalsIgnoreCase("Stop") && !line.equalsIgnoreCase("Exit"));
/*     */     
/* 110 */     socketServer.shutdown();
/* 111 */     server.join();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void printUsage() {
/* 118 */     System.out.println("Usage: ServerSocket port configFilePath");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final int maxBufferSize = 67584;
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
/*     */   public UdpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
/* 136 */     super(port, logEventInput);
/* 137 */     this.datagramSocket = new DatagramSocket(port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 145 */     while (isActive()) {
/* 146 */       if (this.datagramSocket.isClosed()) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 151 */         byte[] buf = new byte[67584];
/* 152 */         DatagramPacket packet = new DatagramPacket(buf, buf.length);
/* 153 */         this.datagramSocket.receive(packet);
/* 154 */         ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
/* 155 */         this.logEventInput.logEvents(this.logEventInput.wrapStream(bais), this);
/* 156 */       } catch (OptionalDataException e) {
/* 157 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 161 */         this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
/* 162 */       } catch (EOFException e) {
/* 163 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 167 */         this.logger.info("EOF encountered");
/* 168 */       } catch (IOException e) {
/* 169 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 173 */         this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 182 */     setActive(false);
/* 183 */     Thread.currentThread().interrupt();
/* 184 */     this.datagramSocket.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\UdpSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */