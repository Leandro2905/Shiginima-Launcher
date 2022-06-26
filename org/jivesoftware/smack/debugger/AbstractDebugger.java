/*     */ package org.jivesoftware.smack.debugger;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import org.jivesoftware.smack.ConnectionListener;
/*     */ import org.jivesoftware.smack.StanzaListener;
/*     */ import org.jivesoftware.smack.XMPPConnection;
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jivesoftware.smack.util.ObservableReader;
/*     */ import org.jivesoftware.smack.util.ObservableWriter;
/*     */ import org.jivesoftware.smack.util.ReaderListener;
/*     */ import org.jivesoftware.smack.util.WriterListener;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDebugger
/*     */   implements SmackDebugger
/*     */ {
/*     */   public static boolean printInterpreted = false;
/*     */   private final XMPPConnection connection;
/*     */   private final StanzaListener listener;
/*     */   private final ConnectionListener connListener;
/*     */   private final ReaderListener readerListener;
/*     */   private final WriterListener writerListener;
/*     */   private ObservableWriter writer;
/*     */   private ObservableReader reader;
/*     */   
/*     */   public AbstractDebugger(final XMPPConnection connection, Writer writer, Reader reader) {
/*  47 */     this.connection = connection;
/*     */ 
/*     */     
/*  50 */     this.reader = new ObservableReader(reader);
/*  51 */     this.readerListener = new ReaderListener() {
/*     */         public void read(String str) {
/*  53 */           AbstractDebugger.this.log("RECV (" + connection.getConnectionCounter() + "): " + str);
/*     */         }
/*     */       };
/*  56 */     this.reader.addReaderListener(this.readerListener);
/*     */ 
/*     */     
/*  59 */     this.writer = new ObservableWriter(writer);
/*  60 */     this.writerListener = new WriterListener() {
/*     */         public void write(String str) {
/*  62 */           AbstractDebugger.this.log("SENT (" + connection.getConnectionCounter() + "): " + str);
/*     */         }
/*     */       };
/*  65 */     this.writer.addWriterListener(this.writerListener);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.listener = new StanzaListener() {
/*     */         public void processPacket(Stanza packet) {
/*  72 */           if (AbstractDebugger.printInterpreted) {
/*  73 */             AbstractDebugger.this.log("RCV PKT (" + connection.getConnectionCounter() + "): " + packet.toXML());
/*     */           }
/*     */         }
/*     */       };
/*     */     
/*  78 */     this.connListener = new ConnectionListener() {
/*     */         public void connected(XMPPConnection connection) {
/*  80 */           AbstractDebugger.this.log("XMPPConnection connected (" + connection.getConnectionCounter() + ")");
/*     */         }
/*     */         
/*     */         public void authenticated(XMPPConnection connection, boolean resumed) {
/*  84 */           String logString = "XMPPConnection authenticated (" + connection.getConnectionCounter() + ")";
/*  85 */           if (resumed) {
/*  86 */             logString = logString + " and resumed";
/*     */           }
/*  88 */           AbstractDebugger.this.log(logString);
/*     */         }
/*     */         public void connectionClosed() {
/*  91 */           AbstractDebugger.this.log("XMPPConnection closed (" + connection.getConnectionCounter() + ")");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void connectionClosedOnError(Exception e) {
/*  98 */           AbstractDebugger.this.log("XMPPConnection closed due to an exception (" + connection.getConnectionCounter() + ")");
/*     */ 
/*     */ 
/*     */           
/* 102 */           e.printStackTrace();
/*     */         }
/*     */         public void reconnectionFailed(Exception e) {
/* 105 */           AbstractDebugger.this.log("Reconnection failed due to an exception (" + connection.getConnectionCounter() + ")");
/*     */ 
/*     */ 
/*     */           
/* 109 */           e.printStackTrace();
/*     */         }
/*     */         public void reconnectionSuccessful() {
/* 112 */           AbstractDebugger.this.log("XMPPConnection reconnected (" + connection.getConnectionCounter() + ")");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void reconnectingIn(int seconds) {
/* 118 */           AbstractDebugger.this.log("XMPPConnection (" + connection.getConnectionCounter() + ") will reconnect in " + seconds);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void log(String paramString);
/*     */ 
/*     */   
/*     */   public Reader newConnectionReader(Reader newReader) {
/* 129 */     this.reader.removeReaderListener(this.readerListener);
/* 130 */     ObservableReader debugReader = new ObservableReader(newReader);
/* 131 */     debugReader.addReaderListener(this.readerListener);
/* 132 */     this.reader = debugReader;
/* 133 */     return (Reader)this.reader;
/*     */   }
/*     */   
/*     */   public Writer newConnectionWriter(Writer newWriter) {
/* 137 */     this.writer.removeWriterListener(this.writerListener);
/* 138 */     ObservableWriter debugWriter = new ObservableWriter(newWriter);
/* 139 */     debugWriter.addWriterListener(this.writerListener);
/* 140 */     this.writer = debugWriter;
/* 141 */     return (Writer)this.writer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void userHasLogged(FullJid user) {
/* 146 */     String localpart = user.getLocalpart().toString();
/* 147 */     boolean isAnonymous = "".equals(localpart);
/* 148 */     String title = "User logged (" + this.connection.getConnectionCounter() + "): " + (isAnonymous ? "" : localpart) + "@" + this.connection.getServiceName() + ":" + this.connection.getPort();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     title = title + "/" + user.getResourcepart();
/* 156 */     log(title);
/*     */ 
/*     */     
/* 159 */     this.connection.addConnectionListener(this.connListener);
/*     */   }
/*     */   
/*     */   public Reader getReader() {
/* 163 */     return (Reader)this.reader;
/*     */   }
/*     */   
/*     */   public Writer getWriter() {
/* 167 */     return (Writer)this.writer;
/*     */   }
/*     */   
/*     */   public StanzaListener getReaderListener() {
/* 171 */     return this.listener;
/*     */   }
/*     */   
/*     */   public StanzaListener getWriterListener() {
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\AbstractDebugger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */