/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
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
/*     */ public class MapMessage
/*     */   implements MultiformatMessage
/*     */ {
/*     */   private static final long serialVersionUID = -5031471831131487120L;
/*     */   private final SortedMap<String, String> data;
/*     */   
/*     */   public enum MapFormat
/*     */   {
/*  37 */     XML,
/*     */     
/*  39 */     JSON,
/*     */     
/*  41 */     JAVA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage() {
/*  52 */     this.data = new TreeMap<String, String>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage(Map<String, String> map) {
/*  60 */     this.data = (map instanceof SortedMap) ? (SortedMap<String, String>)map : new TreeMap<String, String>(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/*  65 */     String[] formats = new String[(MapFormat.values()).length];
/*  66 */     int i = 0;
/*  67 */     for (MapFormat format : MapFormat.values()) {
/*  68 */       formats[i++] = format.name();
/*     */     }
/*  70 */     return formats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  79 */     return this.data.values().toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  88 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getData() {
/*  96 */     return Collections.unmodifiableMap(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 103 */     this.data.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 112 */     if (value == null) {
/* 113 */       throw new IllegalArgumentException("No value provided for key " + key);
/*     */     }
/* 115 */     validate(key, value);
/* 116 */     this.data.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> map) {
/* 128 */     this.data.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 137 */     return this.data.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remove(String key) {
/* 146 */     return this.data.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString() {
/* 155 */     return asString((MapFormat)null);
/*     */   }
/*     */   
/*     */   public String asString(String format) {
/*     */     try {
/* 160 */       return asString((MapFormat)EnglishEnums.valueOf(MapFormat.class, format));
/* 161 */     } catch (IllegalArgumentException ex) {
/* 162 */       return asString();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String asString(MapFormat format) {
/* 172 */     StringBuilder sb = new StringBuilder();
/* 173 */     if (format == null)
/* 174 */     { appendMap(sb); }
/*     */     else
/* 176 */     { switch (format)
/*     */       { case XML:
/* 178 */           asXml(sb);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 194 */           return sb.toString();case JSON: asJson(sb); return sb.toString();case JAVA: asJava(sb); return sb.toString(); }  appendMap(sb); }  return sb.toString();
/*     */   }
/*     */   
/*     */   public void asXml(StringBuilder sb) {
/* 198 */     sb.append("<Map>\n");
/* 199 */     for (Map.Entry<String, String> entry : this.data.entrySet()) {
/* 200 */       sb.append("  <Entry key=\"").append(entry.getKey()).append("\">").append(entry.getValue()).append("</Entry>\n");
/*     */     }
/*     */     
/* 203 */     sb.append("</Map>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 212 */     return asString();
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 225 */     if (formats == null || formats.length == 0) {
/* 226 */       return asString();
/*     */     }
/* 228 */     for (String format : formats) {
/* 229 */       for (MapFormat mapFormat : MapFormat.values()) {
/* 230 */         if (mapFormat.name().equalsIgnoreCase(format)) {
/* 231 */           return asString(mapFormat);
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     return asString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendMap(StringBuilder sb) {
/* 240 */     boolean first = true;
/* 241 */     for (Map.Entry<String, String> entry : this.data.entrySet()) {
/* 242 */       if (!first) {
/* 243 */         sb.append(' ');
/*     */       }
/* 245 */       first = false;
/* 246 */       sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void asJson(StringBuilder sb) {
/* 251 */     boolean first = true;
/* 252 */     sb.append('{');
/* 253 */     for (Map.Entry<String, String> entry : this.data.entrySet()) {
/* 254 */       if (!first) {
/* 255 */         sb.append(", ");
/*     */       }
/* 257 */       first = false;
/* 258 */       sb.append('"').append(entry.getKey()).append("\":");
/* 259 */       sb.append('"').append(entry.getValue()).append('"');
/*     */     } 
/* 261 */     sb.append('}');
/*     */   }
/*     */ 
/*     */   
/*     */   protected void asJava(StringBuilder sb) {
/* 266 */     boolean first = true;
/* 267 */     sb.append('{');
/* 268 */     for (Map.Entry<String, String> entry : this.data.entrySet()) {
/* 269 */       if (!first) {
/* 270 */         sb.append(", ");
/*     */       }
/* 272 */       first = false;
/* 273 */       sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
/*     */     } 
/* 275 */     sb.append('}');
/*     */   }
/*     */   
/*     */   public MapMessage newInstance(Map<String, String> map) {
/* 279 */     return new MapMessage(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 284 */     return asString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 289 */     if (this == o) {
/* 290 */       return true;
/*     */     }
/* 292 */     if (o == null || getClass() != o.getClass()) {
/* 293 */       return false;
/*     */     }
/*     */     
/* 296 */     MapMessage that = (MapMessage)o;
/*     */     
/* 298 */     return this.data.equals(that.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 303 */     return this.data.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 313 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\MapMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */