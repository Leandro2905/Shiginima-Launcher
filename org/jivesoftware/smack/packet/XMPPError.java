/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
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
/*     */ public class XMPPError
/*     */   extends AbstractError
/*     */ {
/*     */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-stanzas";
/*     */   public static final String ERROR = "error";
/*  67 */   private static final Logger LOGGER = Logger.getLogger(XMPPError.class.getName());
/*  68 */   private static final Map<Condition, Type> CONDITION_TO_TYPE = new HashMap<>();
/*     */   
/*     */   static {
/*  71 */     CONDITION_TO_TYPE.put(Condition.bad_request, Type.MODIFY);
/*  72 */     CONDITION_TO_TYPE.put(Condition.conflict, Type.CANCEL);
/*  73 */     CONDITION_TO_TYPE.put(Condition.feature_not_implemented, Type.CANCEL);
/*  74 */     CONDITION_TO_TYPE.put(Condition.forbidden, Type.AUTH);
/*  75 */     CONDITION_TO_TYPE.put(Condition.gone, Type.CANCEL);
/*  76 */     CONDITION_TO_TYPE.put(Condition.internal_server_error, Type.CANCEL);
/*  77 */     CONDITION_TO_TYPE.put(Condition.item_not_found, Type.CANCEL);
/*  78 */     CONDITION_TO_TYPE.put(Condition.jid_malformed, Type.MODIFY);
/*  79 */     CONDITION_TO_TYPE.put(Condition.not_acceptable, Type.MODIFY);
/*  80 */     CONDITION_TO_TYPE.put(Condition.not_allowed, Type.CANCEL);
/*  81 */     CONDITION_TO_TYPE.put(Condition.not_authorized, Type.AUTH);
/*  82 */     CONDITION_TO_TYPE.put(Condition.policy_violation, Type.MODIFY);
/*  83 */     CONDITION_TO_TYPE.put(Condition.recipient_unavailable, Type.WAIT);
/*  84 */     CONDITION_TO_TYPE.put(Condition.redirect, Type.MODIFY);
/*  85 */     CONDITION_TO_TYPE.put(Condition.registration_required, Type.AUTH);
/*  86 */     CONDITION_TO_TYPE.put(Condition.remote_server_not_found, Type.CANCEL);
/*  87 */     CONDITION_TO_TYPE.put(Condition.remote_server_timeout, Type.WAIT);
/*  88 */     CONDITION_TO_TYPE.put(Condition.resource_constraint, Type.WAIT);
/*  89 */     CONDITION_TO_TYPE.put(Condition.service_unavailable, Type.WAIT);
/*  90 */     CONDITION_TO_TYPE.put(Condition.subscription_required, Type.WAIT);
/*  91 */     CONDITION_TO_TYPE.put(Condition.unexpected_request, Type.MODIFY);
/*     */   }
/*     */   private final Condition condition;
/*     */   private final String conditionText;
/*     */   private final String errorGenerator;
/*     */   private final Type type;
/*     */   
/*     */   public XMPPError(Condition condition) {
/*  99 */     this(condition, null, null, null, null, null);
/*     */   }
/*     */   
/*     */   public XMPPError(Condition condition, ExtensionElement applicationSpecificCondition) {
/* 103 */     this(condition, null, null, null, null, Arrays.asList(new ExtensionElement[] { applicationSpecificCondition }));
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
/*     */   public XMPPError(Condition condition, String conditionText, String errorGenerator, Type type, Map<String, String> descriptiveTexts, List<ExtensionElement> extensions) {
/* 119 */     super(descriptiveTexts, "urn:ietf:params:xml:ns:xmpp-stanzas", extensions);
/* 120 */     this.condition = condition;
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (StringUtils.isNullOrEmpty(conditionText)) {
/* 125 */       conditionText = null;
/*     */     }
/* 127 */     if (conditionText != null) {
/* 128 */       switch (condition) {
/*     */         case gone:
/*     */         case redirect:
/*     */           break;
/*     */         default:
/* 133 */           throw new IllegalArgumentException("Condition text can only be set with condtion types 'gone' and 'redirect', not " + condition);
/*     */       } 
/*     */ 
/*     */     
/*     */     }
/* 138 */     this.conditionText = conditionText;
/* 139 */     this.errorGenerator = errorGenerator;
/* 140 */     if (type == null) {
/* 141 */       Type determinedType = CONDITION_TO_TYPE.get(condition);
/* 142 */       if (determinedType == null) {
/* 143 */         LOGGER.warning("Could not determine type for condition: " + condition);
/* 144 */         determinedType = Type.CANCEL;
/*     */       } 
/* 146 */       this.type = determinedType;
/*     */     } else {
/* 148 */       this.type = type;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Condition getCondition() {
/* 158 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 167 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getErrorGenerator() {
/* 171 */     return this.errorGenerator;
/*     */   }
/*     */   
/*     */   public String getConditionText() {
/* 175 */     return this.conditionText;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 180 */     StringBuilder sb = new StringBuilder("XMPPError: ");
/* 181 */     sb.append(this.condition.toString()).append(" - ").append(this.type.toString());
/* 182 */     if (this.errorGenerator != null) {
/* 183 */       sb.append(". Generated by ").append(this.errorGenerator);
/*     */     }
/* 185 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStringBuilder toXML() {
/* 194 */     XmlStringBuilder xml = new XmlStringBuilder();
/* 195 */     xml.halfOpenElement("error");
/* 196 */     xml.attribute("type", this.type.toString());
/* 197 */     xml.optAttribute("by", this.errorGenerator);
/* 198 */     xml.rightAngleBracket();
/*     */     
/* 200 */     xml.halfOpenElement(this.condition.toString());
/* 201 */     xml.xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-stanzas");
/* 202 */     if (this.conditionText != null) {
/* 203 */       xml.rightAngleBracket();
/* 204 */       xml.escape(this.conditionText);
/* 205 */       xml.closeElement(this.condition.toString());
/*     */     } else {
/*     */       
/* 208 */       xml.closeEmptyElement();
/*     */     } 
/*     */     
/* 211 */     addDescriptiveTextsAndExtensions(xml);
/*     */     
/* 213 */     xml.closeElement("error");
/* 214 */     return xml;
/*     */   }
/*     */   
/*     */   public static XMPPError from(Condition condition, String descriptiveText) {
/* 218 */     Map<String, String> descriptiveTexts = new HashMap<>();
/* 219 */     descriptiveTexts.put("en", descriptiveText);
/* 220 */     return new XMPPError(condition, null, null, null, descriptiveTexts, null);
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
/* 235 */     WAIT,
/* 236 */     CANCEL,
/* 237 */     MODIFY,
/* 238 */     AUTH,
/* 239 */     CONTINUE;
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 244 */       return name().toLowerCase();
/*     */     }
/*     */ 
/*     */     
/*     */     public static Type fromString(String string) {
/* 249 */       string = string.toUpperCase();
/* 250 */       return valueOf(string);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Condition {
/* 255 */     bad_request,
/* 256 */     conflict,
/* 257 */     feature_not_implemented,
/* 258 */     forbidden,
/* 259 */     gone,
/* 260 */     internal_server_error,
/* 261 */     item_not_found,
/* 262 */     jid_malformed,
/* 263 */     not_acceptable,
/* 264 */     not_allowed,
/* 265 */     not_authorized,
/* 266 */     policy_violation,
/* 267 */     recipient_unavailable,
/* 268 */     redirect,
/* 269 */     registration_required,
/* 270 */     remote_server_not_found,
/* 271 */     remote_server_timeout,
/* 272 */     resource_constraint,
/* 273 */     service_unavailable,
/* 274 */     subscription_required,
/* 275 */     undefined_condition,
/* 276 */     unexpected_request;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 280 */       return name().replace('_', '-');
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static Condition fromString(String string) {
/* 286 */       if ("xml-not-well-formed".equals(string)) {
/* 287 */         string = "not-well-formed";
/*     */       }
/* 289 */       string = string.replace('-', '_');
/* 290 */       Condition condition = null;
/*     */       try {
/* 292 */         condition = valueOf(string);
/* 293 */       } catch (Exception e) {
/* 294 */         throw new IllegalStateException("Could not transform string '" + string + "' to XMPPErrorCondition", e);
/*     */       } 
/* 296 */       return condition;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\XMPPError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */