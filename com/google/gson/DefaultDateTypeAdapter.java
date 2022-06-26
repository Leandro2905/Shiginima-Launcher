/*     */ package com.google.gson;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.sql.Date;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DefaultDateTypeAdapter
/*     */   implements JsonSerializer<Date>, JsonDeserializer<Date>
/*     */ {
/*     */   private final DateFormat enUsFormat;
/*     */   private final DateFormat localFormat;
/*     */   private final DateFormat iso8601Format;
/*     */   
/*     */   DefaultDateTypeAdapter() {
/*  44 */     this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
/*     */   }
/*     */ 
/*     */   
/*     */   DefaultDateTypeAdapter(String datePattern) {
/*  49 */     this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
/*     */   }
/*     */   
/*     */   DefaultDateTypeAdapter(int style) {
/*  53 */     this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
/*     */   }
/*     */   
/*     */   public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
/*  57 */     this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
/*     */   }
/*     */ 
/*     */   
/*     */   DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
/*  62 */     this.enUsFormat = enUsFormat;
/*  63 */     this.localFormat = localFormat;
/*  64 */     this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
/*  65 */     this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
/*  71 */     synchronized (this.localFormat) {
/*  72 */       String dateFormatAsString = this.enUsFormat.format(src);
/*  73 */       return new JsonPrimitive(dateFormatAsString);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  79 */     if (!(json instanceof JsonPrimitive)) {
/*  80 */       throw new JsonParseException("The date should be a string value");
/*     */     }
/*  82 */     Date date = deserializeToDate(json);
/*  83 */     if (typeOfT == Date.class)
/*  84 */       return date; 
/*  85 */     if (typeOfT == Timestamp.class)
/*  86 */       return new Timestamp(date.getTime()); 
/*  87 */     if (typeOfT == Date.class) {
/*  88 */       return new Date(date.getTime());
/*     */     }
/*  90 */     throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
/*     */   }
/*     */ 
/*     */   
/*     */   private Date deserializeToDate(JsonElement json) {
/*  95 */     synchronized (this.localFormat) {
/*     */       
/*  97 */       return this.localFormat.parse(json.getAsString());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     StringBuilder sb = new StringBuilder();
/* 115 */     sb.append(DefaultDateTypeAdapter.class.getSimpleName());
/* 116 */     sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
/* 117 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\DefaultDateTypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */