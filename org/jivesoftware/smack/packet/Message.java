/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.jivesoftware.smack.util.XmlStringBuilder;
/*     */ import org.jxmpp.jid.Jid;
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
/*     */ 
/*     */ public final class Message
/*     */   extends Stanza
/*     */   implements Cloneable
/*     */ {
/*     */   public static final String ELEMENT = "message";
/*     */   public static final String BODY = "body";
/*     */   private Type type;
/*  60 */   private String thread = null;
/*     */   
/*  62 */   private final Set<Subject> subjects = new HashSet<>();
/*  63 */   private final Set<Body> bodies = new HashSet<>();
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
/*     */   public Message(Jid to) {
/*  77 */     setTo(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(Jid to, Type type) {
/*  87 */     this(to);
/*  88 */     setType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(Jid to, String body) {
/*  98 */     this(to);
/*  99 */     setBody(body);
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
/*     */   public Message(Message other) {
/* 112 */     super(other);
/* 113 */     this.type = other.type;
/* 114 */     this.thread = other.thread;
/* 115 */     this.subjects.addAll(other.subjects);
/* 116 */     this.bodies.addAll(other.bodies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 126 */     if (this.type == null) {
/* 127 */       return Type.normal;
/*     */     }
/* 129 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(Type type) {
/* 138 */     this.type = type;
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
/*     */   public String getSubject() {
/* 152 */     return getSubject((String)null);
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
/*     */   public String getSubject(String language) {
/* 164 */     Subject subject = getMessageSubject(language);
/* 165 */     return (subject == null) ? null : subject.subject;
/*     */   }
/*     */   
/*     */   private Subject getMessageSubject(String language) {
/* 169 */     language = determineLanguage(language);
/* 170 */     for (Subject subject : this.subjects) {
/* 171 */       if (language.equals(subject.language)) {
/* 172 */         return subject;
/*     */       }
/*     */     } 
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Subject> getSubjects() {
/* 185 */     return Collections.unmodifiableSet(this.subjects);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/* 195 */     if (subject == null) {
/* 196 */       removeSubject("");
/*     */       return;
/*     */     } 
/* 199 */     addSubject((String)null, subject);
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
/*     */   public Subject addSubject(String language, String subject) {
/* 211 */     language = determineLanguage(language);
/* 212 */     Subject messageSubject = new Subject(language, subject);
/* 213 */     this.subjects.add(messageSubject);
/* 214 */     return messageSubject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeSubject(String language) {
/* 224 */     language = determineLanguage(language);
/* 225 */     for (Subject subject : this.subjects) {
/* 226 */       if (language.equals(subject.language)) {
/* 227 */         return this.subjects.remove(subject);
/*     */       }
/*     */     } 
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeSubject(Subject subject) {
/* 240 */     return this.subjects.remove(subject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getSubjectLanguages() {
/* 249 */     Subject defaultSubject = getMessageSubject((String)null);
/* 250 */     List<String> languages = new ArrayList<>();
/* 251 */     for (Subject subject : this.subjects) {
/* 252 */       if (!subject.equals(defaultSubject)) {
/* 253 */         languages.add(subject.language);
/*     */       }
/*     */     } 
/* 256 */     return Collections.unmodifiableList(languages);
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
/*     */   public String getBody() {
/* 270 */     return getBody((String)null);
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
/*     */   public String getBody(String language) {
/* 283 */     Body body = getMessageBody(language);
/* 284 */     return (body == null) ? null : body.message;
/*     */   }
/*     */   
/*     */   private Body getMessageBody(String language) {
/* 288 */     language = determineLanguage(language);
/* 289 */     for (Body body : this.bodies) {
/* 290 */       if (language.equals(body.language)) {
/* 291 */         return body;
/*     */       }
/*     */     } 
/* 294 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Body> getBodies() {
/* 305 */     return Collections.unmodifiableSet(this.bodies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBody(String body) {
/* 314 */     if (body == null) {
/* 315 */       removeBody("");
/*     */       return;
/*     */     } 
/* 318 */     addBody((String)null, body);
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
/*     */   public Body addBody(String language, String body) {
/* 331 */     language = determineLanguage(language);
/* 332 */     Body messageBody = new Body(language, body);
/* 333 */     this.bodies.add(messageBody);
/* 334 */     return messageBody;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeBody(String language) {
/* 344 */     language = determineLanguage(language);
/* 345 */     for (Body body : this.bodies) {
/* 346 */       if (language.equals(body.language)) {
/* 347 */         return this.bodies.remove(body);
/*     */       }
/*     */     } 
/* 350 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeBody(Body body) {
/* 361 */     return this.bodies.remove(body);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getBodyLanguages() {
/* 371 */     Body defaultBody = getMessageBody((String)null);
/* 372 */     List<String> languages = new ArrayList<>();
/* 373 */     for (Body body : this.bodies) {
/* 374 */       if (!body.equals(defaultBody)) {
/* 375 */         languages.add(body.language);
/*     */       }
/*     */     } 
/* 378 */     return Collections.unmodifiableList(languages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThread() {
/* 388 */     return this.thread;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThread(String thread) {
/* 398 */     this.thread = thread;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String determineLanguage(String language) {
/* 404 */     language = "".equals(language) ? null : language;
/*     */ 
/*     */     
/* 407 */     if (language == null && this.language != null) {
/* 408 */       return this.language;
/*     */     }
/* 410 */     if (language == null) {
/* 411 */       return getDefaultLanguage();
/*     */     }
/*     */     
/* 414 */     return language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder toXML() {
/* 421 */     XmlStringBuilder buf = new XmlStringBuilder();
/* 422 */     buf.halfOpenElement("message");
/* 423 */     addCommonAttributes(buf);
/* 424 */     buf.optAttribute("type", this.type);
/* 425 */     buf.rightAngleBracket();
/*     */ 
/*     */     
/* 428 */     Subject defaultSubject = getMessageSubject((String)null);
/* 429 */     if (defaultSubject != null) {
/* 430 */       buf.element("subject", defaultSubject.subject);
/*     */     }
/*     */     
/* 433 */     for (Subject subject : getSubjects()) {
/*     */       
/* 435 */       if (subject.equals(defaultSubject))
/*     */         continue; 
/* 437 */       buf.halfOpenElement("subject").xmllangAttribute(subject.language).rightAngleBracket();
/* 438 */       buf.escape(subject.subject);
/* 439 */       buf.closeElement("subject");
/*     */     } 
/*     */     
/* 442 */     Body defaultBody = getMessageBody((String)null);
/* 443 */     if (defaultBody != null) {
/* 444 */       buf.element("body", defaultBody.message);
/*     */     }
/*     */     
/* 447 */     for (Body body : getBodies()) {
/*     */       
/* 449 */       if (body.equals(defaultBody))
/*     */         continue; 
/* 451 */       buf.halfOpenElement("body").xmllangAttribute(body.getLanguage()).rightAngleBracket();
/* 452 */       buf.escape(body.getMessage());
/* 453 */       buf.closeElement("body");
/*     */     } 
/* 455 */     buf.optElement("thread", this.thread);
/*     */     
/* 457 */     if (this.type == Type.error) {
/* 458 */       appendErrorIfExists(buf);
/*     */     }
/*     */     
/* 461 */     buf.append(getExtensionsXML());
/* 462 */     buf.closeElement("message");
/* 463 */     return buf;
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
/*     */   public Message clone() {
/* 476 */     return new Message(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message() {}
/*     */   
/*     */   public static class Subject
/*     */   {
/*     */     private final String subject;
/*     */     private final String language;
/*     */     
/*     */     private Subject(String language, String subject) {
/* 488 */       if (language == null) {
/* 489 */         throw new NullPointerException("Language cannot be null.");
/*     */       }
/* 491 */       if (subject == null) {
/* 492 */         throw new NullPointerException("Subject cannot be null.");
/*     */       }
/* 494 */       this.language = language;
/* 495 */       this.subject = subject;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLanguage() {
/* 504 */       return this.language;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSubject() {
/* 513 */       return this.subject;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 518 */       int prime = 31;
/* 519 */       int result = 1;
/* 520 */       result = 31 * result + this.language.hashCode();
/* 521 */       result = 31 * result + this.subject.hashCode();
/* 522 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 526 */       if (this == obj) {
/* 527 */         return true;
/*     */       }
/* 529 */       if (obj == null) {
/* 530 */         return false;
/*     */       }
/* 532 */       if (getClass() != obj.getClass()) {
/* 533 */         return false;
/*     */       }
/* 535 */       Subject other = (Subject)obj;
/*     */       
/* 537 */       return (this.language.equals(other.language) && this.subject.equals(other.subject));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Body
/*     */   {
/*     */     private final String message;
/*     */     
/*     */     private final String language;
/*     */ 
/*     */     
/*     */     private Body(String language, String message) {
/* 551 */       if (language == null) {
/* 552 */         throw new NullPointerException("Language cannot be null.");
/*     */       }
/* 554 */       if (message == null) {
/* 555 */         throw new NullPointerException("Message cannot be null.");
/*     */       }
/* 557 */       this.language = language;
/* 558 */       this.message = message;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLanguage() {
/* 567 */       return this.language;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 576 */       return this.message;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 580 */       int prime = 31;
/* 581 */       int result = 1;
/* 582 */       result = 31 * result + this.language.hashCode();
/* 583 */       result = 31 * result + this.message.hashCode();
/* 584 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 588 */       if (this == obj) {
/* 589 */         return true;
/*     */       }
/* 591 */       if (obj == null) {
/* 592 */         return false;
/*     */       }
/* 594 */       if (getClass() != obj.getClass()) {
/* 595 */         return false;
/*     */       }
/* 597 */       Body other = (Body)obj;
/*     */       
/* 599 */       return (this.language.equals(other.language) && this.message.equals(other.message));
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
/*     */   public enum Type
/*     */   {
/* 612 */     normal,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 617 */     chat,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 622 */     groupchat,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 627 */     headline,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 632 */     error;
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
/*     */     public static Type fromString(String string) {
/* 644 */       return valueOf(string.toLowerCase(Locale.US));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Message.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */