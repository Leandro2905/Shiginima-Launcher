/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructuredDataMessage
/*     */   extends MapMessage
/*     */ {
/*     */   private static final long serialVersionUID = 1703221292892071920L;
/*     */   private static final int MAX_LENGTH = 32;
/*     */   private static final int HASHVAL = 31;
/*     */   private StructuredDataId id;
/*     */   private String message;
/*     */   private String type;
/*     */   
/*     */   public enum Format
/*     */   {
/*  45 */     XML,
/*     */     
/*  47 */     FULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(String id, String msg, String type) {
/*  57 */     this.id = new StructuredDataId(id, null, null);
/*  58 */     this.message = msg;
/*  59 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(String id, String msg, String type, Map<String, String> data) {
/*  70 */     super(data);
/*  71 */     this.id = new StructuredDataId(id, null, null);
/*  72 */     this.message = msg;
/*  73 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type) {
/*  83 */     this.id = id;
/*  84 */     this.message = msg;
/*  85 */     this.type = type;
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
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data) {
/*  97 */     super(data);
/*  98 */     this.id = id;
/*  99 */     this.message = msg;
/* 100 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StructuredDataMessage(StructuredDataMessage msg, Map<String, String> map) {
/* 110 */     super(map);
/* 111 */     this.id = msg.id;
/* 112 */     this.message = msg.message;
/* 113 */     this.type = msg.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructuredDataMessage() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/* 130 */     String[] formats = new String[(Format.values()).length];
/* 131 */     int i = 0;
/* 132 */     for (Format format : Format.values()) {
/* 133 */       formats[i++] = format.name();
/*     */     }
/* 135 */     return formats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId getId() {
/* 143 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(String id) {
/* 151 */     this.id = new StructuredDataId(id, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(StructuredDataId id) {
/* 159 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 167 */     return this.type;
/*     */   }
/*     */   
/*     */   protected void setType(String type) {
/* 171 */     if (type.length() > 32) {
/* 172 */       throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
/*     */     }
/* 174 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 183 */     return this.message;
/*     */   }
/*     */   
/*     */   protected void setMessageFormat(String msg) {
/* 187 */     this.message = msg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {
/* 193 */     validateKey(key);
/*     */   }
/*     */   
/*     */   private void validateKey(String key) {
/* 197 */     if (key.length() > 32) {
/* 198 */       throw new IllegalArgumentException("Structured data keys are limited to 32 characters. key: " + key);
/*     */     }
/* 200 */     char[] chars = key.toCharArray();
/* 201 */     for (char c : chars) {
/* 202 */       if (c < '!' || c > '~' || c == '=' || c == ']' || c == '"') {
/* 203 */         throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
/*     */       }
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
/*     */   public String asString() {
/* 216 */     return asString(Format.FULL, null);
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
/*     */   public String asString(String format) {
/*     */     try {
/* 229 */       return asString((Format)EnglishEnums.valueOf(Format.class, format), null);
/* 230 */     } catch (IllegalArgumentException ex) {
/* 231 */       return asString();
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
/*     */   
/*     */   public final String asString(Format format, StructuredDataId structuredDataId) {
/* 245 */     StringBuilder sb = new StringBuilder();
/* 246 */     boolean full = Format.FULL.equals(format);
/* 247 */     if (full) {
/* 248 */       String type = getType();
/* 249 */       if (type == null) {
/* 250 */         return sb.toString();
/*     */       }
/* 252 */       sb.append(getType()).append(' ');
/*     */     } 
/* 254 */     StructuredDataId id = getId();
/* 255 */     if (id != null) {
/* 256 */       id = id.makeId(structuredDataId);
/*     */     } else {
/* 258 */       id = structuredDataId;
/*     */     } 
/* 260 */     if (id == null || id.getName() == null) {
/* 261 */       return sb.toString();
/*     */     }
/* 263 */     sb.append('[');
/* 264 */     sb.append(id);
/* 265 */     sb.append(' ');
/* 266 */     appendMap(sb);
/* 267 */     sb.append(']');
/* 268 */     if (full) {
/* 269 */       String msg = getFormat();
/* 270 */       if (msg != null) {
/* 271 */         sb.append(' ').append(msg);
/*     */       }
/*     */     } 
/* 274 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 283 */     return asString(Format.FULL, (StructuredDataId)null);
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 297 */     if (formats != null && formats.length > 0) {
/* 298 */       for (String format : formats) {
/* 299 */         if (Format.XML.name().equalsIgnoreCase(format))
/* 300 */           return asXML(); 
/* 301 */         if (Format.FULL.name().equalsIgnoreCase(format)) {
/* 302 */           return asString(Format.FULL, (StructuredDataId)null);
/*     */         }
/*     */       } 
/* 305 */       return asString((Format)null, (StructuredDataId)null);
/*     */     } 
/* 307 */     return asString(Format.FULL, (StructuredDataId)null);
/*     */   }
/*     */   
/*     */   private String asXML() {
/* 311 */     StringBuilder sb = new StringBuilder();
/* 312 */     StructuredDataId id = getId();
/* 313 */     if (id == null || id.getName() == null || this.type == null) {
/* 314 */       return sb.toString();
/*     */     }
/* 316 */     sb.append("<StructuredData>\n");
/* 317 */     sb.append("<type>").append(this.type).append("</type>\n");
/* 318 */     sb.append("<id>").append(id).append("</id>\n");
/* 319 */     asXml(sb);
/* 320 */     sb.append("</StructuredData>\n");
/* 321 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return asString((Format)null, (StructuredDataId)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage newInstance(Map<String, String> map) {
/* 332 */     return new StructuredDataMessage(this, map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 337 */     if (this == o) {
/* 338 */       return true;
/*     */     }
/* 340 */     if (o == null || getClass() != o.getClass()) {
/* 341 */       return false;
/*     */     }
/*     */     
/* 344 */     StructuredDataMessage that = (StructuredDataMessage)o;
/*     */     
/* 346 */     if (!super.equals(o)) {
/* 347 */       return false;
/*     */     }
/* 349 */     if ((this.type != null) ? !this.type.equals(that.type) : (that.type != null)) {
/* 350 */       return false;
/*     */     }
/* 352 */     if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null)) {
/* 353 */       return false;
/*     */     }
/* 355 */     if ((this.message != null) ? !this.message.equals(that.message) : (that.message != null)) {
/* 356 */       return false;
/*     */     }
/*     */     
/* 359 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 364 */     int result = super.hashCode();
/* 365 */     result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
/* 366 */     result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
/* 367 */     result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
/* 368 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\StructuredDataMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */