/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.jivesoftware.smack.util.Objects;
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
/*     */ public final class Presence
/*     */   extends Stanza
/*     */   implements Cloneable
/*     */ {
/*     */   public static final String ELEMENT = "presence";
/*  62 */   private Type type = Type.available;
/*  63 */   private String status = null;
/*  64 */   private int priority = Integer.MIN_VALUE;
/*  65 */   private Mode mode = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Presence(Type type) {
/*  73 */     setType(type);
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
/*     */   public Presence(Type type, String status, int priority, Mode mode) {
/*  85 */     setType(type);
/*  86 */     setStatus(status);
/*  87 */     setPriority(priority);
/*  88 */     setMode(mode);
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
/*     */   public Presence(Presence other) {
/* 101 */     super(other);
/* 102 */     this.type = other.type;
/* 103 */     this.status = other.status;
/* 104 */     this.priority = other.priority;
/* 105 */     this.mode = other.mode;
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
/*     */   public boolean isAvailable() {
/* 120 */     return (this.type == Type.available);
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
/*     */   public boolean isAway() {
/* 134 */     return (this.type == Type.available && (this.mode == Mode.away || this.mode == Mode.xa || this.mode == Mode.dnd));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 143 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(Type type) {
/* 152 */     this.type = (Type)Objects.requireNonNull(type, "Type cannot be null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 163 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/* 173 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPriority() {
/* 182 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPriority(int priority) {
/* 192 */     if (priority < -128 || priority > 128) {
/* 193 */       throw new IllegalArgumentException("Priority value " + priority + " is not valid. Valid range is -128 through 128.");
/*     */     }
/*     */     
/* 196 */     this.priority = priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mode getMode() {
/* 205 */     if (this.mode == null) {
/* 206 */       return Mode.available;
/*     */     }
/* 208 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(Mode mode) {
/* 218 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder toXML() {
/* 223 */     XmlStringBuilder buf = new XmlStringBuilder();
/* 224 */     buf.halfOpenElement("presence");
/* 225 */     addCommonAttributes(buf);
/* 226 */     if (this.type != Type.available) {
/* 227 */       buf.attribute("type", this.type);
/*     */     }
/* 229 */     buf.rightAngleBracket();
/*     */     
/* 231 */     buf.optElement("status", this.status);
/* 232 */     if (this.priority != Integer.MIN_VALUE) {
/* 233 */       buf.element("priority", Integer.toString(this.priority));
/*     */     }
/* 235 */     if (this.mode != null && this.mode != Mode.available) {
/* 236 */       buf.element("show", this.mode);
/*     */     }
/* 238 */     buf.append(getExtensionsXML());
/*     */ 
/*     */     
/* 241 */     appendErrorIfExists(buf);
/*     */     
/* 243 */     buf.closeElement("presence");
/*     */     
/* 245 */     return buf;
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
/*     */   public Presence clone() {
/* 258 */     return new Presence(this);
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
/*     */   public enum Type
/*     */   {
/* 273 */     available,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     unavailable,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     subscribe,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     subscribed,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     unsubscribe,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     unsubscribed,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     error,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     probe;
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
/*     */     public static Type fromString(String string) {
/* 322 */       return valueOf(string.toLowerCase(Locale.US));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Mode
/*     */   {
/* 334 */     chat,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 339 */     available,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 344 */     away,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     xa,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     dnd;
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
/*     */     public static Mode fromString(String string) {
/* 366 */       return valueOf(string.toLowerCase(Locale.US));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Presence.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */