/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.jivesoftware.smack.util.PacketUtil;
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
/*     */ public class AbstractError
/*     */ {
/*     */   private final String textNamespace;
/*     */   protected final Map<String, String> descriptiveTexts;
/*     */   private final List<ExtensionElement> extensions;
/*     */   
/*     */   protected AbstractError(Map<String, String> descriptiveTexts) {
/*  35 */     this(descriptiveTexts, null);
/*     */   }
/*     */   
/*     */   protected AbstractError(Map<String, String> descriptiveTexts, List<ExtensionElement> extensions) {
/*  39 */     this(descriptiveTexts, null, extensions);
/*     */   }
/*     */   
/*     */   protected AbstractError(Map<String, String> descriptiveTexts, String textNamespace, List<ExtensionElement> extensions) {
/*  43 */     if (descriptiveTexts != null) {
/*  44 */       this.descriptiveTexts = descriptiveTexts;
/*     */     } else {
/*  46 */       this.descriptiveTexts = Collections.emptyMap();
/*     */     } 
/*  48 */     this.textNamespace = textNamespace;
/*  49 */     if (extensions != null) {
/*  50 */       this.extensions = extensions;
/*     */     } else {
/*  52 */       this.extensions = Collections.emptyList();
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
/*     */   public String getDescriptiveText() {
/*  65 */     String defaultLocale = Locale.getDefault().getLanguage();
/*  66 */     String descriptiveText = getDescriptiveText(defaultLocale);
/*  67 */     if (descriptiveText == null) {
/*  68 */       descriptiveText = getDescriptiveText("");
/*     */     }
/*  70 */     return descriptiveText;
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
/*     */   public String getDescriptiveText(String xmllang) {
/*  83 */     return this.descriptiveTexts.get(xmllang);
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
/*     */   public <PE extends ExtensionElement> PE getExtension(String elementName, String namespace) {
/*  95 */     return (PE)PacketUtil.extensionElementFrom(this.extensions, elementName, namespace);
/*     */   }
/*     */   
/*     */   protected void addDescriptiveTextsAndExtensions(XmlStringBuilder xml) {
/*  99 */     for (Map.Entry<String, String> entry : this.descriptiveTexts.entrySet()) {
/* 100 */       String xmllang = entry.getKey();
/* 101 */       String text = entry.getValue();
/* 102 */       xml.halfOpenElement("text").xmlnsAttribute(this.textNamespace).xmllangAttribute(xmllang).rightAngleBracket();
/*     */       
/* 104 */       xml.escape(text);
/* 105 */       xml.closeElement("text");
/*     */     } 
/* 107 */     for (ExtensionElement packetExtension : this.extensions)
/* 108 */       xml.append(packetExtension.toXML()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\AbstractError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */