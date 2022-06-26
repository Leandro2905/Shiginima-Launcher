/*     */ package org.jivesoftware.smack.sasl.packet;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.jivesoftware.smack.packet.AbstractError;
/*     */ import org.jivesoftware.smack.packet.PlainStreamElement;
/*     */ import org.jivesoftware.smack.sasl.SASLError;
/*     */ import org.jivesoftware.smack.util.Objects;
/*     */ import org.jivesoftware.smack.util.StringUtils;
/*     */ import org.jivesoftware.smack.util.XmlStringBuilder;
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
/*     */ public class SaslStreamElements
/*     */ {
/*     */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-sasl";
/*     */   
/*     */   public static class AuthMechanism
/*     */     implements PlainStreamElement
/*     */   {
/*     */     public static final String ELEMENT = "auth";
/*     */     private final String mechanism;
/*     */     private final String authenticationText;
/*     */     
/*     */     public AuthMechanism(String mechanism, String authenticationText) {
/*  41 */       this.mechanism = (String)Objects.requireNonNull(mechanism, "SASL mechanism shouldn't be null.");
/*  42 */       this.authenticationText = (String)StringUtils.requireNotNullOrEmpty(authenticationText, "SASL authenticationText must not be null or empty (RFC6120 6.4.2)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public XmlStringBuilder toXML() {
/*  48 */       XmlStringBuilder xml = new XmlStringBuilder();
/*  49 */       xml.halfOpenElement("auth").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").attribute("mechanism", this.mechanism).rightAngleBracket();
/*  50 */       xml.optAppend(this.authenticationText);
/*  51 */       xml.closeElement("auth");
/*  52 */       return xml;
/*     */     }
/*     */     
/*     */     public String getMechanism() {
/*  56 */       return this.mechanism;
/*     */     }
/*     */     
/*     */     public String getAuthenticationText() {
/*  60 */       return this.authenticationText;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Challenge
/*     */     implements PlainStreamElement
/*     */   {
/*     */     public static final String ELEMENT = "challenge";
/*     */     
/*     */     private final String data;
/*     */     
/*     */     public Challenge(String data) {
/*  73 */       this.data = StringUtils.returnIfNotEmptyTrimmed(data);
/*     */     }
/*     */ 
/*     */     
/*     */     public XmlStringBuilder toXML() {
/*  78 */       XmlStringBuilder xml = (new XmlStringBuilder()).halfOpenElement("challenge").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
/*     */       
/*  80 */       xml.optAppend(this.data);
/*  81 */       xml.closeElement("challenge");
/*  82 */       return xml;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Response
/*     */     implements PlainStreamElement
/*     */   {
/*     */     public static final String ELEMENT = "response";
/*     */     
/*     */     private final String authenticationText;
/*     */     
/*     */     public Response() {
/*  95 */       this.authenticationText = null;
/*     */     }
/*     */     
/*     */     public Response(String authenticationText) {
/*  99 */       this.authenticationText = StringUtils.returnIfNotEmptyTrimmed(authenticationText);
/*     */     }
/*     */ 
/*     */     
/*     */     public XmlStringBuilder toXML() {
/* 104 */       XmlStringBuilder xml = new XmlStringBuilder();
/* 105 */       xml.halfOpenElement("response").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
/* 106 */       xml.optAppend(this.authenticationText);
/* 107 */       xml.closeElement("response");
/* 108 */       return xml;
/*     */     }
/*     */     
/*     */     public String getAuthenticationText() {
/* 112 */       return this.authenticationText;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Success
/*     */     implements PlainStreamElement
/*     */   {
/*     */     public static final String ELEMENT = "success";
/*     */ 
/*     */ 
/*     */     
/*     */     private final String data;
/*     */ 
/*     */ 
/*     */     
/*     */     public Success(String data) {
/* 131 */       this.data = StringUtils.returnIfNotEmptyTrimmed(data);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getData() {
/* 140 */       return this.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public XmlStringBuilder toXML() {
/* 145 */       XmlStringBuilder xml = new XmlStringBuilder();
/* 146 */       xml.halfOpenElement("success").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
/* 147 */       xml.optAppend(this.data);
/* 148 */       xml.closeElement("success");
/* 149 */       return xml;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SASLFailure
/*     */     extends AbstractError
/*     */     implements PlainStreamElement
/*     */   {
/*     */     public static final String ELEMENT = "failure";
/*     */     
/*     */     private final SASLError saslError;
/*     */     private final String saslErrorString;
/*     */     
/*     */     public SASLFailure(String saslError) {
/* 164 */       this(saslError, null);
/*     */     }
/*     */     
/*     */     public SASLFailure(String saslError, Map<String, String> descriptiveTexts) {
/* 168 */       super(descriptiveTexts);
/* 169 */       SASLError error = SASLError.fromString(saslError);
/* 170 */       if (error == null) {
/*     */ 
/*     */         
/* 173 */         this.saslError = SASLError.not_authorized;
/*     */       } else {
/*     */         
/* 176 */         this.saslError = error;
/*     */       } 
/* 178 */       this.saslErrorString = saslError;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SASLError getSASLError() {
/* 187 */       return this.saslError;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSASLErrorString() {
/* 194 */       return this.saslErrorString;
/*     */     }
/*     */ 
/*     */     
/*     */     public XmlStringBuilder toXML() {
/* 199 */       XmlStringBuilder xml = new XmlStringBuilder();
/* 200 */       xml.halfOpenElement("failure").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
/* 201 */       xml.emptyElement(this.saslErrorString);
/* 202 */       addDescriptiveTextsAndExtensions(xml);
/* 203 */       xml.closeElement("failure");
/* 204 */       return xml;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 209 */       return toXML().toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\packet\SaslStreamElements.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */