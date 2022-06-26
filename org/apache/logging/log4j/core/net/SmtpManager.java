/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.activation.DataSource;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.InternetHeaders;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import javax.mail.util.ByteArrayDataSource;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.util.CyclicBuffer;
/*     */ import org.apache.logging.log4j.core.util.NameUtil;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public class SmtpManager
/*     */   extends AbstractManager
/*     */ {
/*  54 */   private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory();
/*     */   
/*     */   private final Session session;
/*     */   
/*     */   private final CyclicBuffer<LogEvent> buffer;
/*     */   
/*     */   private volatile MimeMessage message;
/*     */   
/*     */   private final FactoryData data;
/*     */ 
/*     */   
/*     */   protected SmtpManager(String name, Session session, MimeMessage message, FactoryData data) {
/*  66 */     super(name);
/*  67 */     this.session = session;
/*  68 */     this.message = message;
/*  69 */     this.data = data;
/*  70 */     this.buffer = new CyclicBuffer(LogEvent.class, data.numElements);
/*     */   }
/*     */   
/*     */   public void add(LogEvent event) {
/*  74 */     this.buffer.add(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpManager getSMTPManager(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, String filterName, int numElements) {
/*  82 */     if (Strings.isEmpty(protocol)) {
/*  83 */       protocol = "smtp";
/*     */     }
/*     */     
/*  86 */     StringBuilder sb = new StringBuilder();
/*  87 */     if (to != null) {
/*  88 */       sb.append(to);
/*     */     }
/*  90 */     sb.append(':');
/*  91 */     if (cc != null) {
/*  92 */       sb.append(cc);
/*     */     }
/*  94 */     sb.append(':');
/*  95 */     if (bcc != null) {
/*  96 */       sb.append(bcc);
/*     */     }
/*  98 */     sb.append(':');
/*  99 */     if (from != null) {
/* 100 */       sb.append(from);
/*     */     }
/* 102 */     sb.append(':');
/* 103 */     if (replyTo != null) {
/* 104 */       sb.append(replyTo);
/*     */     }
/* 106 */     sb.append(':');
/* 107 */     if (subject != null) {
/* 108 */       sb.append(subject);
/*     */     }
/* 110 */     sb.append(':');
/* 111 */     sb.append(protocol).append(':').append(host).append(':').append("port").append(':');
/* 112 */     if (username != null) {
/* 113 */       sb.append(username);
/*     */     }
/* 115 */     sb.append(':');
/* 116 */     if (password != null) {
/* 117 */       sb.append(password);
/*     */     }
/* 119 */     sb.append(isDebug ? ":debug:" : "::");
/* 120 */     sb.append(filterName);
/*     */     
/* 122 */     String name = "SMTP:" + NameUtil.md5(sb.toString());
/*     */     
/* 124 */     return (SmtpManager)getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subject, protocol, host, port, username, password, isDebug, numElements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEvents(Layout<?> layout, LogEvent appendEvent) {
/* 134 */     if (this.message == null) {
/* 135 */       connect();
/*     */     }
/*     */     try {
/* 138 */       LogEvent[] priorEvents = (LogEvent[])this.buffer.removeAll();
/*     */ 
/*     */       
/* 141 */       byte[] rawBytes = formatContentToBytes(priorEvents, appendEvent, layout);
/*     */       
/* 143 */       String contentType = layout.getContentType();
/* 144 */       String encoding = getEncoding(rawBytes, contentType);
/* 145 */       byte[] encodedBytes = encodeContentToBytes(rawBytes, encoding);
/*     */       
/* 147 */       InternetHeaders headers = getHeaders(contentType, encoding);
/* 148 */       MimeMultipart mp = getMimeMultipart(encodedBytes, headers);
/*     */       
/* 150 */       sendMultipartMessage(this.message, mp);
/* 151 */     } catch (MessagingException e) {
/* 152 */       LOGGER.error("Error occurred while sending e-mail notification.", (Throwable)e);
/* 153 */       throw new LoggingException("Error occurred while sending email", e);
/* 154 */     } catch (IOException e) {
/* 155 */       LOGGER.error("Error occurred while sending e-mail notification.", e);
/* 156 */       throw new LoggingException("Error occurred while sending email", e);
/* 157 */     } catch (RuntimeException e) {
/* 158 */       LOGGER.error("Error occurred while sending e-mail notification.", e);
/* 159 */       throw new LoggingException("Error occurred while sending email", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] formatContentToBytes(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout) throws IOException {
/* 165 */     ByteArrayOutputStream raw = new ByteArrayOutputStream();
/* 166 */     writeContent(priorEvents, appendEvent, layout, raw);
/* 167 */     return raw.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeContent(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, ByteArrayOutputStream out) throws IOException {
/* 173 */     writeHeader(layout, out);
/* 174 */     writeBuffer(priorEvents, appendEvent, layout, out);
/* 175 */     writeFooter(layout, out);
/*     */   }
/*     */   
/*     */   protected void writeHeader(Layout<?> layout, OutputStream out) throws IOException {
/* 179 */     byte[] header = layout.getHeader();
/* 180 */     if (header != null) {
/* 181 */       out.write(header);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBuffer(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, OutputStream out) throws IOException {
/* 187 */     for (LogEvent priorEvent : priorEvents) {
/* 188 */       byte[] arrayOfByte = layout.toByteArray(priorEvent);
/* 189 */       out.write(arrayOfByte);
/*     */     } 
/*     */     
/* 192 */     byte[] bytes = layout.toByteArray(appendEvent);
/* 193 */     out.write(bytes);
/*     */   }
/*     */   
/*     */   protected void writeFooter(Layout<?> layout, OutputStream out) throws IOException {
/* 197 */     byte[] footer = layout.getFooter();
/* 198 */     if (footer != null) {
/* 199 */       out.write(footer);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getEncoding(byte[] rawBytes, String contentType) {
/* 204 */     ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(rawBytes, contentType);
/* 205 */     return MimeUtility.getEncoding((DataSource)byteArrayDataSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] encodeContentToBytes(byte[] rawBytes, String encoding) throws MessagingException, IOException {
/* 210 */     ByteArrayOutputStream encoded = new ByteArrayOutputStream();
/* 211 */     encodeContent(rawBytes, encoding, encoded);
/* 212 */     return encoded.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encodeContent(byte[] bytes, String encoding, ByteArrayOutputStream out) throws MessagingException, IOException {
/* 217 */     OutputStream encoder = MimeUtility.encode(out, encoding);
/* 218 */     encoder.write(bytes);
/* 219 */     encoder.close();
/*     */   }
/*     */   
/*     */   protected InternetHeaders getHeaders(String contentType, String encoding) {
/* 223 */     InternetHeaders headers = new InternetHeaders();
/* 224 */     headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
/* 225 */     headers.setHeader("Content-Transfer-Encoding", encoding);
/* 226 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MimeMultipart getMimeMultipart(byte[] encodedBytes, InternetHeaders headers) throws MessagingException {
/* 231 */     MimeMultipart mp = new MimeMultipart();
/* 232 */     MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
/* 233 */     mp.addBodyPart((BodyPart)part);
/* 234 */     return mp;
/*     */   }
/*     */   
/*     */   protected void sendMultipartMessage(MimeMessage message, MimeMultipart mp) throws MessagingException {
/* 238 */     synchronized (message) {
/* 239 */       message.setContent((Multipart)mp);
/* 240 */       message.setSentDate(new Date());
/* 241 */       Transport.send((Message)message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String to;
/*     */     
/*     */     private final String cc;
/*     */     
/*     */     private final String bcc;
/*     */     
/*     */     private final String from;
/*     */     private final String replyto;
/*     */     private final String subject;
/*     */     private final String protocol;
/*     */     private final String host;
/*     */     private final int port;
/*     */     private final String username;
/*     */     private final String password;
/*     */     private final boolean isDebug;
/*     */     private final int numElements;
/*     */     
/*     */     public FactoryData(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, int numElements) {
/* 266 */       this.to = to;
/* 267 */       this.cc = cc;
/* 268 */       this.bcc = bcc;
/* 269 */       this.from = from;
/* 270 */       this.replyto = replyTo;
/* 271 */       this.subject = subject;
/* 272 */       this.protocol = protocol;
/* 273 */       this.host = host;
/* 274 */       this.port = port;
/* 275 */       this.username = username;
/* 276 */       this.password = password;
/* 277 */       this.isDebug = isDebug;
/* 278 */       this.numElements = numElements;
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void connect() {
/* 283 */     if (this.message != null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 287 */       this.message = (new MimeMessageBuilder(this.session)).setFrom(this.data.from).setReplyTo(this.data.replyto).setRecipients(Message.RecipientType.TO, this.data.to).setRecipients(Message.RecipientType.CC, this.data.cc).setRecipients(Message.RecipientType.BCC, this.data.bcc).setSubject(this.data.subject).getMimeMessage();
/*     */     
/*     */     }
/* 290 */     catch (MessagingException e) {
/* 291 */       LOGGER.error("Could not set SmtpAppender message options.", (Throwable)e);
/* 292 */       this.message = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class SMTPManagerFactory
/*     */     implements ManagerFactory<SmtpManager, FactoryData>
/*     */   {
/*     */     private SMTPManagerFactory() {}
/*     */     
/*     */     public SmtpManager createManager(String name, SmtpManager.FactoryData data) {
/*     */       MimeMessage mimeMessage;
/* 303 */       String prefix = "mail." + data.protocol;
/*     */       
/* 305 */       Properties properties = PropertiesUtil.getSystemProperties();
/* 306 */       properties.put("mail.transport.protocol", data.protocol);
/* 307 */       if (properties.getProperty("mail.host") == null)
/*     */       {
/* 309 */         properties.put("mail.host", NetUtils.getLocalHostname());
/*     */       }
/*     */       
/* 312 */       if (null != data.host) {
/* 313 */         properties.put(prefix + ".host", data.host);
/*     */       }
/* 315 */       if (data.port > 0) {
/* 316 */         properties.put(prefix + ".port", String.valueOf(data.port));
/*     */       }
/*     */       
/* 319 */       Authenticator authenticator = buildAuthenticator(data.username, data.password);
/* 320 */       if (null != authenticator) {
/* 321 */         properties.put(prefix + ".auth", "true");
/*     */       }
/*     */       
/* 324 */       Session session = Session.getInstance(properties, authenticator);
/* 325 */       session.setProtocolForAddress("rfc822", data.protocol);
/* 326 */       session.setDebug(data.isDebug);
/*     */ 
/*     */       
/*     */       try {
/* 330 */         mimeMessage = (new MimeMessageBuilder(session)).setFrom(data.from).setReplyTo(data.replyto).setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc).setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject).getMimeMessage();
/*     */       
/*     */       }
/* 333 */       catch (MessagingException e) {
/* 334 */         SmtpManager.LOGGER.error("Could not set SmtpAppender message options.", (Throwable)e);
/* 335 */         mimeMessage = null;
/*     */       } 
/*     */       
/* 338 */       return new SmtpManager(name, session, mimeMessage, data);
/*     */     }
/*     */     
/*     */     private Authenticator buildAuthenticator(final String username, final String password) {
/* 342 */       if (null != password && null != username) {
/* 343 */         return new Authenticator() {
/* 344 */             private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
/*     */ 
/*     */ 
/*     */             
/*     */             protected PasswordAuthentication getPasswordAuthentication() {
/* 349 */               return this.passwordAuthentication;
/*     */             }
/*     */           };
/*     */       }
/* 353 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\SmtpManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */