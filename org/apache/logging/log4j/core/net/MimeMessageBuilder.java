/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import javax.mail.Address;
/*    */ import javax.mail.Message;
/*    */ import javax.mail.MessagingException;
/*    */ import javax.mail.Session;
/*    */ import javax.mail.internet.AddressException;
/*    */ import javax.mail.internet.InternetAddress;
/*    */ import javax.mail.internet.MimeMessage;
/*    */ import org.apache.logging.log4j.core.util.Charsets;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MimeMessageBuilder
/*    */ {
/*    */   private final MimeMessage message;
/*    */   
/*    */   public MimeMessageBuilder(Session session) {
/* 35 */     this.message = new MimeMessage(session);
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setFrom(String from) throws MessagingException {
/* 39 */     InternetAddress address = parseAddress(from);
/*    */     
/* 41 */     if (null != address) {
/* 42 */       this.message.setFrom((Address)address);
/*    */     } else {
/*    */       try {
/* 45 */         this.message.setFrom();
/* 46 */       } catch (Exception ex) {
/* 47 */         this.message.setFrom((Address)null);
/*    */       } 
/*    */     } 
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setReplyTo(String replyTo) throws MessagingException {
/* 54 */     InternetAddress[] addresses = parseAddresses(replyTo);
/*    */     
/* 56 */     if (null != addresses) {
/* 57 */       this.message.setReplyTo((Address[])addresses);
/*    */     }
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MimeMessageBuilder setRecipients(Message.RecipientType recipientType, String recipients) throws MessagingException {
/* 64 */     InternetAddress[] addresses = parseAddresses(recipients);
/*    */     
/* 66 */     if (null != addresses) {
/* 67 */       this.message.setRecipients(recipientType, (Address[])addresses);
/*    */     }
/* 69 */     return this;
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setSubject(String subject) throws MessagingException {
/* 73 */     if (subject != null) {
/* 74 */       this.message.setSubject(subject, Charsets.UTF_8.name());
/*    */     }
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public MimeMessage getMimeMessage() {
/* 80 */     return this.message;
/*    */   }
/*    */   
/*    */   private static InternetAddress parseAddress(String address) throws AddressException {
/* 84 */     return (address == null) ? null : new InternetAddress(address);
/*    */   }
/*    */   
/*    */   private static InternetAddress[] parseAddresses(String addresses) throws AddressException {
/* 88 */     return (addresses == null) ? null : InternetAddress.parse(addresses, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\MimeMessageBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */