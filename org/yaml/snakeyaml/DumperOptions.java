/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.emitter.ScalarAnalysis;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DumperOptions
/*     */ {
/*     */   public enum ScalarStyle
/*     */   {
/*  39 */     DOUBLE_QUOTED((String)Character.valueOf('"')), SINGLE_QUOTED((String)Character.valueOf('\'')), LITERAL((String)Character.valueOf('|')),
/*  40 */     FOLDED((String)Character.valueOf('>')), PLAIN(null);
/*     */     private Character styleChar;
/*     */     
/*     */     ScalarStyle(Character style) {
/*  44 */       this.styleChar = style;
/*     */     }
/*     */     
/*     */     public Character getChar() {
/*  48 */       return this.styleChar;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  53 */       return "Scalar style: '" + this.styleChar + "'";
/*     */     }
/*     */     
/*     */     public static ScalarStyle createStyle(Character style) {
/*  57 */       if (style == null) {
/*  58 */         return PLAIN;
/*     */       }
/*  60 */       switch (style.charValue()) {
/*     */         case '"':
/*  62 */           return DOUBLE_QUOTED;
/*     */         case '\'':
/*  64 */           return SINGLE_QUOTED;
/*     */         case '|':
/*  66 */           return LITERAL;
/*     */         case '>':
/*  68 */           return FOLDED;
/*     */       } 
/*  70 */       throw new YAMLException("Unknown scalar style character: " + style);
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
/*     */   public enum FlowStyle
/*     */   {
/*  85 */     FLOW((String)Boolean.TRUE), BLOCK((String)Boolean.FALSE), AUTO(null);
/*     */     
/*     */     private Boolean styleBoolean;
/*     */     
/*     */     FlowStyle(Boolean flowStyle) {
/*  90 */       this.styleBoolean = flowStyle;
/*     */     }
/*     */     
/*     */     public Boolean getStyleBoolean() {
/*  94 */       return this.styleBoolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  99 */       return "Flow style: '" + this.styleBoolean + "'";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum LineBreak
/*     */   {
/* 107 */     WIN("\r\n"), MAC("\r"), UNIX("\n");
/*     */     
/*     */     private String lineBreak;
/*     */     
/*     */     LineBreak(String lineBreak) {
/* 112 */       this.lineBreak = lineBreak;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 116 */       return this.lineBreak;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 121 */       return "Line break: " + name();
/*     */     }
/*     */     
/*     */     public static LineBreak getPlatformLineBreak() {
/* 125 */       String platformLineBreak = System.getProperty("line.separator");
/* 126 */       for (LineBreak lb : values()) {
/* 127 */         if (lb.lineBreak.equals(platformLineBreak)) {
/* 128 */           return lb;
/*     */         }
/*     */       } 
/* 131 */       return UNIX;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Version
/*     */   {
/* 139 */     V1_0((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }), V1_1((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(1) });
/*     */     
/*     */     private Integer[] version;
/*     */     
/*     */     Version(Integer[] version) {
/* 144 */       this.version = version;
/*     */     }
/*     */     
/*     */     public Integer[] getArray() {
/* 148 */       return this.version;
/*     */     }
/*     */     
/*     */     public String getRepresentation() {
/* 152 */       return this.version[0] + "." + this.version[1];
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 157 */       return "Version: " + getRepresentation();
/*     */     }
/*     */   }
/*     */   
/* 161 */   private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
/* 162 */   private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
/*     */   private boolean canonical = false;
/*     */   private boolean allowUnicode = true;
/*     */   private boolean allowReadOnlyProperties = false;
/* 166 */   private int indent = 2;
/* 167 */   private int bestWidth = 80;
/* 168 */   private LineBreak lineBreak = LineBreak.UNIX;
/*     */   private boolean explicitStart = false;
/*     */   private boolean explicitEnd = false;
/* 171 */   private TimeZone timeZone = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   private Tag explicitRoot = null;
/* 177 */   private Version version = null;
/* 178 */   private Map<String, String> tags = null;
/* 179 */   private Boolean prettyFlow = Boolean.valueOf(false);
/*     */   
/*     */   public boolean isAllowUnicode() {
/* 182 */     return this.allowUnicode;
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
/*     */   public void setAllowUnicode(boolean allowUnicode) {
/* 194 */     this.allowUnicode = allowUnicode;
/*     */   }
/*     */   
/*     */   public ScalarStyle getDefaultScalarStyle() {
/* 198 */     return this.defaultStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultScalarStyle(ScalarStyle defaultStyle) {
/* 209 */     if (defaultStyle == null) {
/* 210 */       throw new NullPointerException("Use ScalarStyle enum.");
/*     */     }
/* 212 */     this.defaultStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public void setIndent(int indent) {
/* 216 */     if (indent < 1) {
/* 217 */       throw new YAMLException("Indent must be at least 1");
/*     */     }
/* 219 */     if (indent > 10) {
/* 220 */       throw new YAMLException("Indent must be at most 10");
/*     */     }
/* 222 */     this.indent = indent;
/*     */   }
/*     */   
/*     */   public int getIndent() {
/* 226 */     return this.indent;
/*     */   }
/*     */   
/*     */   public void setVersion(Version version) {
/* 230 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/* 234 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanonical(boolean canonical) {
/* 244 */     this.canonical = canonical;
/*     */   }
/*     */   
/*     */   public boolean isCanonical() {
/* 248 */     return this.canonical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrettyFlow(boolean prettyFlow) {
/* 259 */     this.prettyFlow = Boolean.valueOf(prettyFlow);
/*     */   }
/*     */   
/*     */   public boolean isPrettyFlow() {
/* 263 */     return this.prettyFlow.booleanValue();
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
/*     */   public void setWidth(int bestWidth) {
/* 275 */     this.bestWidth = bestWidth;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 279 */     return this.bestWidth;
/*     */   }
/*     */   
/*     */   public LineBreak getLineBreak() {
/* 283 */     return this.lineBreak;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
/* 287 */     if (defaultFlowStyle == null) {
/* 288 */       throw new NullPointerException("Use FlowStyle enum.");
/*     */     }
/* 290 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public FlowStyle getDefaultFlowStyle() {
/* 294 */     return this.defaultFlowStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getExplicitRoot() {
/* 301 */     return this.explicitRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExplicitRoot(String expRoot) {
/* 311 */     setExplicitRoot(new Tag(expRoot));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExplicitRoot(Tag expRoot) {
/* 321 */     if (expRoot == null) {
/* 322 */       throw new NullPointerException("Root tag must be specified.");
/*     */     }
/* 324 */     this.explicitRoot = expRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineBreak(LineBreak lineBreak) {
/* 333 */     if (lineBreak == null) {
/* 334 */       throw new NullPointerException("Specify line break.");
/*     */     }
/* 336 */     this.lineBreak = lineBreak;
/*     */   }
/*     */   
/*     */   public boolean isExplicitStart() {
/* 340 */     return this.explicitStart;
/*     */   }
/*     */   
/*     */   public void setExplicitStart(boolean explicitStart) {
/* 344 */     this.explicitStart = explicitStart;
/*     */   }
/*     */   
/*     */   public boolean isExplicitEnd() {
/* 348 */     return this.explicitEnd;
/*     */   }
/*     */   
/*     */   public void setExplicitEnd(boolean explicitEnd) {
/* 352 */     this.explicitEnd = explicitEnd;
/*     */   }
/*     */   
/*     */   public Map<String, String> getTags() {
/* 356 */     return this.tags;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTags(Map<String, String> tags) {
/* 361 */     this.tags = tags;
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
/*     */   public ScalarStyle calculateScalarStyle(ScalarAnalysis analysis, ScalarStyle style) {
/* 375 */     return style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowReadOnlyProperties() {
/* 385 */     return this.allowReadOnlyProperties;
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
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
/* 397 */     this.allowReadOnlyProperties = allowReadOnlyProperties;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 401 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 409 */     this.timeZone = timeZone;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\DumperOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */