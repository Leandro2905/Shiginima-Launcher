/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jivesoftware.smack.packet.StreamError;
/*     */ import org.jivesoftware.smack.packet.XMPPError;
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
/*     */ public abstract class XMPPException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 6881651633890968625L;
/*     */   
/*     */   protected XMPPException() {}
/*     */   
/*     */   protected XMPPException(String message) {
/*  55 */     super(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMPPException(String message, Throwable wrappedThrowable) {
/*  66 */     super(message, wrappedThrowable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class XMPPErrorException
/*     */     extends XMPPException
/*     */   {
/*     */     private static final long serialVersionUID = 212790389529249604L;
/*     */ 
/*     */     
/*     */     private final XMPPError error;
/*     */ 
/*     */ 
/*     */     
/*     */     public XMPPErrorException(XMPPError error) {
/*  83 */       this.error = error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public XMPPErrorException(String message, XMPPError error, Throwable wrappedThrowable) {
/*  95 */       super(message, wrappedThrowable);
/*  96 */       this.error = error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public XMPPErrorException(String message, XMPPError error) {
/* 107 */       super(message);
/* 108 */       this.error = error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public XMPPError getXMPPError() {
/* 118 */       return this.error;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 123 */       String superMessage = super.getMessage();
/* 124 */       if (superMessage != null) {
/* 125 */         return superMessage;
/*     */       }
/*     */       
/* 128 */       return this.error.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public static void ifHasErrorThenThrow(Stanza packet) throws XMPPErrorException {
/* 133 */       XMPPError xmppError = packet.getError();
/* 134 */       if (xmppError != null) {
/* 135 */         throw new XMPPErrorException(xmppError);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StreamErrorException
/*     */     extends XMPPException
/*     */   {
/*     */     private static final long serialVersionUID = 3400556867134848886L;
/*     */ 
/*     */ 
/*     */     
/*     */     private final StreamError streamError;
/*     */ 
/*     */ 
/*     */     
/*     */     public StreamErrorException(StreamError streamError) {
/* 155 */       super(streamError.getCondition().toString() + " You can read more about the meaning of this stream error at http://xmpp.org/rfcs/rfc6120.html#streams-error-conditions\n" + streamError.toString());
/*     */ 
/*     */       
/* 158 */       this.streamError = streamError;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StreamError getStreamError() {
/* 168 */       return this.streamError;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\XMPPException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */