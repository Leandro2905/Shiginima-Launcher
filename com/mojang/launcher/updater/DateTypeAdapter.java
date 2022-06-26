/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTypeAdapter
/*    */   implements JsonSerializer<Date>, JsonDeserializer<Date>
/*    */ {
/* 26 */   private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
/* 27 */   private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 33 */     if (!(json instanceof JsonPrimitive)) {
/* 34 */       throw new JsonParseException("The date should be a string value");
/*    */     }
/* 36 */     Date date = deserializeToDate(json.getAsString());
/* 37 */     if (typeOfT == Date.class) {
/* 38 */       return date;
/*    */     }
/* 40 */     throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
/* 45 */     synchronized (this.enUsFormat) {
/*    */       
/* 47 */       return (JsonElement)new JsonPrimitive(serializeToString(src));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Date deserializeToDate(String string) {
/* 53 */     synchronized (this.enUsFormat) {
/*    */ 
/*    */ 
/*    */       
/* 57 */       return this.enUsFormat.parse(string);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String serializeToString(Date date) {
/* 84 */     synchronized (this.enUsFormat) {
/*    */       
/* 86 */       String result = this.iso8601Format.format(date);
/* 87 */       return result.substring(0, 22) + ":" + result.substring(22);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\DateTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */