/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamError
/*     */   extends AbstractError
/*     */   implements PlainStreamElement
/*     */ {
/*     */   public static final String ELEMENT = "stream:error";
/*     */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-streams";
/*     */   private final Condition condition;
/*     */   private final String conditionText;
/*     */   
/*     */   public StreamError(Condition condition, String conditionText, Map<String, String> descriptiveTexts, List<ExtensionElement> extensions) {
/* 108 */     super(descriptiveTexts, extensions);
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (StringUtils.isNullOrEmpty(conditionText)) {
/* 113 */       conditionText = null;
/*     */     }
/* 115 */     if (conditionText != null) {
/* 116 */       switch (condition) {
/*     */         case see_other_host:
/*     */           break;
/*     */         default:
/* 120 */           throw new IllegalArgumentException("The given condition '" + condition + "' can not contain a conditionText");
/*     */       } 
/*     */     
/*     */     }
/* 124 */     this.condition = condition;
/* 125 */     this.conditionText = conditionText;
/*     */   }
/*     */   
/*     */   public Condition getCondition() {
/* 129 */     return this.condition;
/*     */   }
/*     */   
/*     */   public String getConditionText() {
/* 133 */     return this.conditionText;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return toXML().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder toXML() {
/* 143 */     XmlStringBuilder xml = new XmlStringBuilder();
/* 144 */     xml.openElement("stream:error");
/* 145 */     xml.halfOpenElement(this.condition.toString()).xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-streams").closeEmptyElement();
/* 146 */     addDescriptiveTextsAndExtensions(xml);
/* 147 */     xml.closeElement("stream:error");
/* 148 */     return xml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Condition
/*     */   {
/* 156 */     bad_format,
/* 157 */     bad_namespace_prefix,
/* 158 */     conflict,
/* 159 */     connection_timeout,
/* 160 */     host_gone,
/* 161 */     host_unknown,
/* 162 */     improper_addressing,
/* 163 */     internal_server_error,
/* 164 */     invalid_from,
/* 165 */     invalid_namespace,
/* 166 */     invalid_xml,
/* 167 */     not_authorized,
/* 168 */     not_well_formed,
/* 169 */     policy_violation,
/* 170 */     remote_connection_failed,
/* 171 */     reset,
/* 172 */     resource_constraint,
/* 173 */     restricted_xml,
/* 174 */     see_other_host,
/* 175 */     system_shutdown,
/* 176 */     undeficed_condition,
/* 177 */     unsupported_encoding,
/* 178 */     unsupported_feature,
/* 179 */     unsupported_stanza_type,
/* 180 */     unsupported_version;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 184 */       return name().replace('_', '-');
/*     */     }
/*     */     
/*     */     public static Condition fromString(String string) {
/* 188 */       string = string.replace('-', '_');
/* 189 */       Condition condition = null;
/*     */       try {
/* 191 */         condition = valueOf(string);
/* 192 */       } catch (Exception e) {
/* 193 */         throw new IllegalStateException("Could not transform string '" + string + "' to XMPPErrorCondition", e);
/*     */       } 
/*     */       
/* 196 */       return condition;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\StreamError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */