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
/*     */ public abstract class IQ
/*     */   extends Stanza
/*     */ {
/*     */   public static final String IQ_ELEMENT = "iq";
/*     */   public static final String QUERY_ELEMENT = "query";
/*     */   private final String childElementName;
/*     */   private final String childElementNamespace;
/*  51 */   private Type type = Type.get;
/*     */   
/*     */   public IQ(IQ iq) {
/*  54 */     super(iq);
/*  55 */     this.type = iq.getType();
/*  56 */     this.childElementName = iq.childElementName;
/*  57 */     this.childElementNamespace = iq.childElementNamespace;
/*     */   }
/*     */   
/*     */   protected IQ(String childElementName) {
/*  61 */     this(childElementName, (String)null);
/*     */   }
/*     */   
/*     */   protected IQ(String childElementName, String childElementNamespace) {
/*  65 */     this.childElementName = childElementName;
/*  66 */     this.childElementNamespace = childElementNamespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  75 */     return this.type;
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
/*     */   public void setType(Type type) {
/*  88 */     this.type = (Type)Objects.requireNonNull(type, "type must not be null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestIQ() {
/*  98 */     switch (this.type) {
/*     */       case get:
/*     */       case set:
/* 101 */         return true;
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getChildElementName() {
/* 108 */     return this.childElementName;
/*     */   }
/*     */   
/*     */   public final String getChildElementNamespace() {
/* 112 */     return this.childElementNamespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public final XmlStringBuilder toXML() {
/* 117 */     XmlStringBuilder buf = new XmlStringBuilder();
/* 118 */     buf.halfOpenElement("iq");
/* 119 */     addCommonAttributes(buf);
/* 120 */     if (this.type == null) {
/* 121 */       buf.attribute("type", "get");
/*     */     } else {
/*     */       
/* 124 */       buf.attribute("type", this.type.toString());
/*     */     } 
/* 126 */     buf.rightAngleBracket();
/* 127 */     buf.append(getChildElementXML());
/* 128 */     buf.closeElement("iq");
/* 129 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XmlStringBuilder getChildElementXML() {
/* 139 */     XmlStringBuilder xml = new XmlStringBuilder();
/* 140 */     if (this.type == Type.error) {
/*     */       
/* 142 */       appendErrorIfExists(xml);
/*     */     }
/* 144 */     else if (this.childElementName != null) {
/*     */       
/* 146 */       IQChildElementXmlStringBuilder iqChildElement = getIQChildElementBuilder(new IQChildElementXmlStringBuilder(this));
/* 147 */       if (iqChildElement != null) {
/* 148 */         xml.append(iqChildElement);
/* 149 */         XmlStringBuilder extensionsXml = getExtensionsXML();
/* 150 */         if (iqChildElement.isEmptyElement) {
/* 151 */           if (extensionsXml.length() == 0) {
/* 152 */             xml.closeEmptyElement();
/* 153 */             return xml;
/*     */           } 
/* 155 */           xml.rightAngleBracket();
/*     */         } 
/*     */         
/* 158 */         xml.append(extensionsXml);
/* 159 */         xml.closeElement(iqChildElement.element);
/*     */       } 
/*     */     } 
/* 162 */     return xml;
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
/*     */   public static IQ createResultIQ(IQ request) {
/* 223 */     return new EmptyResultIQ(request);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ErrorIQ createErrorResponse(IQ request, XMPPError error) {
/* 245 */     if (request.getType() != Type.get && request.getType() != Type.set) {
/* 246 */       throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + request.toXML());
/*     */     }
/*     */     
/* 249 */     ErrorIQ result = new ErrorIQ(error);
/* 250 */     result.setStanzaId(request.getStanzaId());
/* 251 */     result.setFrom(request.getTo());
/* 252 */     result.setTo(request.getFrom());
/* 253 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder paramIQChildElementXmlStringBuilder);
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 264 */     get,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     set,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     result,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     error;
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
/* 292 */       return valueOf(string.toLowerCase(Locale.US));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IQChildElementXmlStringBuilder
/*     */     extends XmlStringBuilder {
/*     */     private final String element;
/*     */     private boolean isEmptyElement;
/*     */     
/*     */     private IQChildElementXmlStringBuilder(IQ iq) {
/* 302 */       this(iq.getChildElementName(), iq.getChildElementNamespace());
/*     */     }
/*     */     
/*     */     public IQChildElementXmlStringBuilder(ExtensionElement pe) {
/* 306 */       this(pe.getElementName(), pe.getNamespace());
/*     */     }
/*     */     
/*     */     private IQChildElementXmlStringBuilder(String element, String namespace) {
/* 310 */       prelude(element, namespace);
/* 311 */       this.element = element;
/*     */     }
/*     */     
/*     */     public void setEmptyElement() {
/* 315 */       this.isEmptyElement = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\IQ.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */