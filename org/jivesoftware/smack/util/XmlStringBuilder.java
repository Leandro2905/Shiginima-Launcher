/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import org.jivesoftware.smack.packet.Element;
/*     */ import org.jivesoftware.smack.packet.ExtensionElement;
/*     */ import org.jivesoftware.smack.packet.NamedElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlStringBuilder
/*     */   implements Appendable, CharSequence
/*     */ {
/*  26 */   public static final String RIGHT_ANGLE_BRACKET = Character.toString('>');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   private final LazyStringBuilder sb = new LazyStringBuilder();
/*     */ 
/*     */   
/*     */   public XmlStringBuilder(ExtensionElement pe) {
/*  35 */     this();
/*  36 */     prelude(pe);
/*     */   }
/*     */   
/*     */   public XmlStringBuilder(NamedElement e) {
/*  40 */     this();
/*  41 */     halfOpenElement(e.getElementName());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder escapedElement(String name, String escapedContent) {
/*  45 */     assert escapedContent != null;
/*  46 */     openElement(name);
/*  47 */     append(escapedContent);
/*  48 */     closeElement(name);
/*  49 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder element(String name, String content) {
/*  59 */     assert content != null;
/*  60 */     openElement(name);
/*  61 */     escape(content);
/*  62 */     closeElement(name);
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder element(String name, CharSequence content) {
/*  73 */     return element(name, content.toString());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder element(String name, Enum<?> content) {
/*  77 */     assert content != null;
/*  78 */     element(name, content.name());
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder element(Element element) {
/*  83 */     assert element != null;
/*  84 */     return append(element.toXML());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optElement(String name, String content) {
/*  88 */     if (content != null) {
/*  89 */       element(name, content);
/*     */     }
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optElement(String name, CharSequence content) {
/*  95 */     if (content != null) {
/*  96 */       element(name, content.toString());
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optElement(Element element) {
/* 102 */     if (element != null) {
/* 103 */       append(element.toXML());
/*     */     }
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optElement(String name, Enum<?> content) {
/* 109 */     if (content != null) {
/* 110 */       element(name, content);
/*     */     }
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optIntElement(String name, int value) {
/* 116 */     if (value >= 0) {
/* 117 */       element(name, String.valueOf(value));
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder halfOpenElement(String name) {
/* 123 */     assert StringUtils.isNotEmpty(name);
/* 124 */     this.sb.append('<').append(name);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder halfOpenElement(NamedElement namedElement) {
/* 129 */     return halfOpenElement(namedElement.getElementName());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder openElement(String name) {
/* 133 */     halfOpenElement(name).rightAngleBracket();
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder closeElement(String name) {
/* 138 */     this.sb.append("</").append(name);
/* 139 */     rightAngleBracket();
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder closeElement(NamedElement e) {
/* 144 */     closeElement(e.getElementName());
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder closeEmptyElement() {
/* 149 */     this.sb.append("/>");
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder rightAngleBracket() {
/* 159 */     this.sb.append(RIGHT_ANGLE_BRACKET);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public XmlStringBuilder rightAngelBracket() {
/* 170 */     return rightAngleBracket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder attribute(String name, String value) {
/* 181 */     assert value != null;
/* 182 */     this.sb.append(' ').append(name).append("='");
/* 183 */     escape(value);
/* 184 */     this.sb.append('\'');
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder attribute(String name, CharSequence value) {
/* 189 */     return attribute(name, value.toString());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder attribute(String name, Enum<?> value) {
/* 193 */     assert value != null;
/* 194 */     attribute(name, value.name());
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder attribute(String name, int value) {
/* 199 */     assert name != null;
/* 200 */     return attribute(name, String.valueOf(value));
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optAttribute(String name, String value) {
/* 204 */     if (value != null) {
/* 205 */       attribute(name, value);
/*     */     }
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optAttribute(String name, CharSequence value) {
/* 211 */     if (value != null) {
/* 212 */       attribute(name, value.toString());
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optAttribute(String name, Enum<?> value) {
/* 218 */     if (value != null) {
/* 219 */       attribute(name, value.toString());
/*     */     }
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder optIntAttribute(String name, int value) {
/* 232 */     if (value >= 0) {
/* 233 */       attribute(name, Integer.toString(value));
/*     */     }
/* 235 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder optLongAttribute(String name, Long value) {
/* 246 */     if (value != null && value.longValue() >= 0L) {
/* 247 */       attribute(name, Long.toString(value.longValue()));
/*     */     }
/* 249 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optBooleanAttribute(String name, boolean bool) {
/* 253 */     if (bool) {
/* 254 */       this.sb.append(' ').append(name).append("='true'");
/*     */     }
/* 256 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder xmlnsAttribute(String value) {
/* 260 */     optAttribute("xmlns", value);
/* 261 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder xmllangAttribute(String value) {
/* 265 */     optAttribute("xml:lang", value);
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder escape(String text) {
/* 270 */     assert text != null;
/* 271 */     this.sb.append(StringUtils.escapeForXML(text));
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder escape(CharSequence text) {
/* 276 */     return escape(text.toString());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder prelude(ExtensionElement pe) {
/* 280 */     return prelude(pe.getElementName(), pe.getNamespace());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder prelude(String elementName, String namespace) {
/* 284 */     halfOpenElement(elementName);
/* 285 */     xmlnsAttribute(namespace);
/* 286 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optAppend(CharSequence csq) {
/* 290 */     if (csq != null) {
/* 291 */       append(csq);
/*     */     }
/* 293 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder optAppend(Element element) {
/* 297 */     if (element != null) {
/* 298 */       append(element.toXML());
/*     */     }
/* 300 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder append(XmlStringBuilder xsb) {
/* 304 */     assert xsb != null;
/* 305 */     this.sb.append(xsb.sb);
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder append(Collection<? extends Element> elements) {
/* 310 */     for (Element element : elements) {
/* 311 */       append(element.toXML());
/*     */     }
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder emptyElement(Enum<?> element) {
/* 317 */     return emptyElement(element.name());
/*     */   }
/*     */   
/*     */   public XmlStringBuilder emptyElement(String element) {
/* 321 */     halfOpenElement(element);
/* 322 */     return closeEmptyElement();
/*     */   }
/*     */   
/*     */   public XmlStringBuilder condEmptyElement(boolean condition, String element) {
/* 326 */     if (condition) {
/* 327 */       emptyElement(element);
/*     */     }
/* 329 */     return this;
/*     */   }
/*     */   
/*     */   public XmlStringBuilder condAttribute(boolean condition, String name, String value) {
/* 333 */     if (condition) {
/* 334 */       attribute(name, value);
/*     */     }
/* 336 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder append(CharSequence csq) {
/* 341 */     assert csq != null;
/* 342 */     this.sb.append(csq);
/* 343 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder append(CharSequence csq, int start, int end) {
/* 348 */     assert csq != null;
/* 349 */     this.sb.append(csq, start, end);
/* 350 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder append(char c) {
/* 355 */     this.sb.append(c);
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 361 */     return this.sb.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 366 */     return this.sb.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 371 */     return this.sb.subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 376 */     return this.sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 381 */     if (!(other instanceof CharSequence)) {
/* 382 */       return false;
/*     */     }
/* 384 */     CharSequence otherCharSequenceBuilder = (CharSequence)other;
/* 385 */     return toString().equals(otherCharSequenceBuilder.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 390 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public XmlStringBuilder() {}
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\XmlStringBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */