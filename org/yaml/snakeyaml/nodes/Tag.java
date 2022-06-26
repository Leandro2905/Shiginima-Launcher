/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.sql.Date;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.UriEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Tag
/*     */   implements Comparable<Tag>
/*     */ {
/*     */   public static final String PREFIX = "tag:yaml.org,2002:";
/*  33 */   public static final Tag YAML = new Tag("tag:yaml.org,2002:yaml");
/*  34 */   public static final Tag MERGE = new Tag("tag:yaml.org,2002:merge");
/*  35 */   public static final Tag SET = new Tag("tag:yaml.org,2002:set");
/*  36 */   public static final Tag PAIRS = new Tag("tag:yaml.org,2002:pairs");
/*  37 */   public static final Tag OMAP = new Tag("tag:yaml.org,2002:omap");
/*  38 */   public static final Tag BINARY = new Tag("tag:yaml.org,2002:binary");
/*  39 */   public static final Tag INT = new Tag("tag:yaml.org,2002:int");
/*  40 */   public static final Tag FLOAT = new Tag("tag:yaml.org,2002:float");
/*  41 */   public static final Tag TIMESTAMP = new Tag("tag:yaml.org,2002:timestamp");
/*  42 */   public static final Tag BOOL = new Tag("tag:yaml.org,2002:bool");
/*  43 */   public static final Tag NULL = new Tag("tag:yaml.org,2002:null");
/*  44 */   public static final Tag STR = new Tag("tag:yaml.org,2002:str");
/*  45 */   public static final Tag SEQ = new Tag("tag:yaml.org,2002:seq");
/*  46 */   public static final Tag MAP = new Tag("tag:yaml.org,2002:map");
/*     */ 
/*     */   
/*  49 */   public static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP = new HashMap<Tag, Set<Class<?>>>(); static {
/*  50 */     Set<Class<?>> floatSet = new HashSet<Class<?>>();
/*  51 */     floatSet.add(Double.class);
/*  52 */     floatSet.add(Float.class);
/*  53 */     floatSet.add(BigDecimal.class);
/*  54 */     COMPATIBILITY_MAP.put(FLOAT, floatSet);
/*     */     
/*  56 */     Set<Class<?>> intSet = new HashSet<Class<?>>();
/*  57 */     intSet.add(Integer.class);
/*  58 */     intSet.add(Long.class);
/*  59 */     intSet.add(BigInteger.class);
/*  60 */     COMPATIBILITY_MAP.put(INT, intSet);
/*     */     
/*  62 */     Set<Class<?>> timestampSet = new HashSet<Class<?>>();
/*  63 */     timestampSet.add(Date.class);
/*  64 */     timestampSet.add(Date.class);
/*  65 */     timestampSet.add(Timestamp.class);
/*  66 */     COMPATIBILITY_MAP.put(TIMESTAMP, timestampSet);
/*     */   }
/*     */   
/*     */   private final String value;
/*     */   private boolean secondary = false;
/*     */   
/*     */   public Tag(String tag) {
/*  73 */     if (tag == null)
/*  74 */       throw new NullPointerException("Tag must be provided."); 
/*  75 */     if (tag.length() == 0)
/*  76 */       throw new IllegalArgumentException("Tag must not be empty."); 
/*  77 */     if (tag.trim().length() != tag.length()) {
/*  78 */       throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
/*     */     }
/*  80 */     this.value = UriEncoder.encode(tag);
/*  81 */     this.secondary = !tag.startsWith("tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   public Tag(Class<? extends Object> clazz) {
/*  85 */     if (clazz == null) {
/*  86 */       throw new NullPointerException("Class for tag must be provided.");
/*     */     }
/*  88 */     this.value = "tag:yaml.org,2002:" + UriEncoder.encode(clazz.getName());
/*     */   }
/*     */   
/*     */   public Tag(URI uri) {
/*  92 */     if (uri == null) {
/*  93 */       throw new NullPointerException("URI for tag must be provided.");
/*     */     }
/*  95 */     this.value = uri.toASCIIString();
/*     */   }
/*     */   
/*     */   public boolean isSecondary() {
/*  99 */     return this.secondary;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 103 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean startsWith(String prefix) {
/* 107 */     return this.value.startsWith(prefix);
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 111 */     if (!this.value.startsWith("tag:yaml.org,2002:")) {
/* 112 */       throw new YAMLException("Invalid tag: " + this.value);
/*     */     }
/* 114 */     return UriEncoder.decode(this.value.substring("tag:yaml.org,2002:".length()));
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 118 */     return this.value.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 128 */     if (obj instanceof Tag)
/* 129 */       return this.value.equals(((Tag)obj).getValue()); 
/* 130 */     if (obj instanceof String && 
/* 131 */       this.value.equals(obj.toString())) {
/*     */       
/* 133 */       System.err.println("Comparing Tag and String is deprecated.");
/* 134 */       return true;
/*     */     } 
/*     */     
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return this.value.hashCode();
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
/*     */   public boolean isCompatible(Class<?> clazz) {
/* 155 */     Set<Class<?>> set = COMPATIBILITY_MAP.get(this);
/* 156 */     if (set != null) {
/* 157 */       return set.contains(clazz);
/*     */     }
/* 159 */     return false;
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
/*     */   public boolean matches(Class<? extends Object> clazz) {
/* 171 */     return this.value.equals("tag:yaml.org,2002:" + clazz.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(Tag o) {
/* 175 */     return this.value.compareTo(o.getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\nodes\Tag.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */