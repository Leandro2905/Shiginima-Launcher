/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructuredDataId
/*     */   implements Serializable
/*     */ {
/*     */   private static final String AT = "@";
/*  31 */   public static final StructuredDataId TIME_QUALITY = new StructuredDataId("timeQuality", null, new String[] { "tzKnown", "isSynced", "syncAccuracy" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public static final StructuredDataId ORIGIN = new StructuredDataId("origin", null, new String[] { "ip", "enterpriseId", "software", "swVersion" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final StructuredDataId META = new StructuredDataId("meta", null, new String[] { "sequenceId", "sysUpTime", "language" });
/*     */   
/*     */   public static final int RESERVED = -1;
/*     */   
/*     */   private static final long serialVersionUID = 9031746276396249990L;
/*     */   
/*     */   private static final int MAX_LENGTH = 32;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int enterpriseNumber;
/*     */   
/*     */   private final String[] required;
/*     */   
/*     */   private final String[] optional;
/*     */ 
/*     */   
/*     */   protected StructuredDataId(String name, String[] required, String[] optional) {
/*  61 */     int index = -1;
/*  62 */     if (name != null) {
/*  63 */       if (name.length() > 32) {
/*  64 */         throw new IllegalArgumentException(String.format("Length of id %s exceeds maximum of %d characters", new Object[] { name, Integer.valueOf(32) }));
/*     */       }
/*     */       
/*  67 */       index = name.indexOf("@");
/*     */     } 
/*     */     
/*  70 */     if (index > 0) {
/*  71 */       this.name = name.substring(0, index);
/*  72 */       this.enterpriseNumber = Integer.parseInt(name.substring(index + 1));
/*     */     } else {
/*  74 */       this.name = name;
/*  75 */       this.enterpriseNumber = -1;
/*     */     } 
/*  77 */     this.required = required;
/*  78 */     this.optional = optional;
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
/*     */   public StructuredDataId(String name, int enterpriseNumber, String[] required, String[] optional) {
/*  91 */     if (name == null) {
/*  92 */       throw new IllegalArgumentException("No structured id name was supplied");
/*     */     }
/*  94 */     if (name.contains("@")) {
/*  95 */       throw new IllegalArgumentException("Structured id name cannot contain an '@'");
/*     */     }
/*  97 */     if (enterpriseNumber <= 0) {
/*  98 */       throw new IllegalArgumentException("No enterprise number was supplied");
/*     */     }
/* 100 */     this.name = name;
/* 101 */     this.enterpriseNumber = enterpriseNumber;
/* 102 */     String id = (enterpriseNumber < 0) ? name : (name + "@" + enterpriseNumber);
/* 103 */     if (id.length() > 32) {
/* 104 */       throw new IllegalArgumentException("Length of id exceeds maximum of 32 characters: " + id);
/*     */     }
/* 106 */     this.required = required;
/* 107 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(StructuredDataId id) {
/* 116 */     if (id == null) {
/* 117 */       return this;
/*     */     }
/* 119 */     return makeId(id.getName(), id.getEnterpriseNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(String defaultId, int enterpriseNumber) {
/*     */     String id;
/*     */     String[] req;
/*     */     String[] opt;
/* 132 */     if (enterpriseNumber <= 0) {
/* 133 */       return this;
/*     */     }
/* 135 */     if (this.name != null) {
/* 136 */       id = this.name;
/* 137 */       req = this.required;
/* 138 */       opt = this.optional;
/*     */     } else {
/* 140 */       id = defaultId;
/* 141 */       req = null;
/* 142 */       opt = null;
/*     */     } 
/*     */     
/* 145 */     return new StructuredDataId(id, enterpriseNumber, req, opt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRequired() {
/* 153 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getOptional() {
/* 161 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 169 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEnterpriseNumber() {
/* 177 */     return this.enterpriseNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReserved() {
/* 185 */     return (this.enterpriseNumber <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     return isReserved() ? this.name : (this.name + "@" + this.enterpriseNumber);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\StructuredDataId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */