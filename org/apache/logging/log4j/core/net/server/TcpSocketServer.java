/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OptionalDataException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public class TcpSocketServer<T extends InputStream>
/*     */   extends AbstractSocketServer<T>
/*     */ {
/*     */   private class SocketHandler
/*     */     extends Thread
/*     */   {
/*     */     private final T inputStream;
/*     */     private volatile boolean shutdown = false;
/*     */     
/*     */     public SocketHandler(Socket socket) throws IOException {
/*  53 */       this.inputStream = TcpSocketServer.this.logEventInput.wrapStream(socket.getInputStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  58 */       boolean closed = false; try {
/*     */         while (true) {
/*     */           try {
/*  61 */             if (!this.shutdown) {
/*  62 */               TcpSocketServer.this.logEventInput.logEvents(this.inputStream, TcpSocketServer.this); continue;
/*     */             } 
/*  64 */           } catch (EOFException e) {
/*  65 */             closed = true;
/*  66 */           } catch (OptionalDataException e) {
/*  67 */             TcpSocketServer.this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
/*  68 */           } catch (IOException e) {
/*  69 */             TcpSocketServer.this.logger.error("IOException encountered while reading from socket", e);
/*     */           }  break;
/*  71 */         }  if (!closed) {
/*     */           try {
/*  73 */             this.inputStream.close();
/*  74 */           } catch (Exception ex) {}
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/*  79 */         TcpSocketServer.access$000(TcpSocketServer.this).remove(Long.valueOf(getId()));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void shutdown() {
/*  84 */       this.shutdown = true;
/*  85 */       interrupt();
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
/*     */   public static TcpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
/*  99 */     return new TcpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
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
/*     */   public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
/* 112 */     return new TcpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
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
/*     */   public static TcpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
/* 125 */     return new TcpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
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
/* 137 */     if (args.length < 1 || args.length > 2) {
/* 138 */       System.err.println("Incorrect number of arguments");
/* 139 */       printUsage();
/*     */       return;
/*     */     } 
/* 142 */     int port = Integer.parseInt(args[0]);
/* 143 */     if (port <= 0 || port >= 65534) {
/* 144 */       System.err.println("Invalid port number");
/* 145 */       printUsage();
/*     */       return;
/*     */     } 
/* 148 */     if (args.length == 2 && args[1].length() > 0) {
/* 149 */       ConfigurationFactory.setConfigurationFactory((ConfigurationFactory)new AbstractSocketServer.ServerConfigurationFactory(args[1]));
/*     */     }
/* 151 */     TcpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(port);
/* 152 */     Thread serverThread = new Thread(socketServer);
/* 153 */     serverThread.start();
/* 154 */     Charset enc = Charset.defaultCharset();
/* 155 */     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, enc));
/*     */     do {
/* 157 */       line = reader.readLine();
/* 158 */     } while (line != null && !line.equalsIgnoreCase("Quit") && !line.equalsIgnoreCase("Stop") && !line.equalsIgnoreCase("Exit"));
/*     */     
/* 160 */     socketServer.shutdown();
/* 161 */     serverThread.join();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void printUsage() {
/* 168 */     System.out.println("Usage: ServerSocket port configFilePath");
/*     */   }
/*     */   
/* 171 */   private final ConcurrentMap<Long, SocketHandler> handlers = new ConcurrentHashMap<Long, SocketHandler>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServerSocket serverSocket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
/* 186 */     this(port, logEventInput, new ServerSocket(port));
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
/*     */   public TcpSocketServer(int port, LogEventBridge<T> logEventInput, ServerSocket serverSocket) throws IOException {
/* 203 */     super(port, logEventInput);
/* 204 */     this.serverSocket = serverSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 212 */     while (isActive()) {
/* 213 */       if (this.serverSocket.isClosed()) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 218 */         Socket clientSocket = this.serverSocket.accept();
/* 219 */         clientSocket.setSoLinger(true, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 225 */         SocketHandler handler = new SocketHandler(clientSocket);
/* 226 */         this.handlers.put(Long.valueOf(handler.getId()), handler);
/* 227 */         handler.start();
/* 228 */       } catch (IOException ioe) {
/* 229 */         if (this.serverSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 233 */         System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
/* 234 */         ioe.printStackTrace();
/*     */       } 
/*     */     } 
/* 237 */     for (Map.Entry<Long, SocketHandler> entry : this.handlers.entrySet()) {
/* 238 */       SocketHandler handler = entry.getValue();
/* 239 */       handler.shutdown();
/*     */       try {
/* 241 */         handler.join();
/* 242 */       } catch (InterruptedException ie) {}
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
/*     */   public void shutdown() throws IOException {
/* 254 */     setActive(false);
/* 255 */     Thread.currentThread().interrupt();
/* 256 */     this.serverSocket.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\TcpSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */