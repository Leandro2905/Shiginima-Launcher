/*    */ package com.mojang.util;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UUIDTypeAdapter
/*    */   extends TypeAdapter<UUID>
/*    */ {
/*    */   public void write(JsonWriter out, UUID value) throws IOException {
/* 15 */     out.value(fromUUID(value));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public UUID read(JsonReader in) throws IOException {
/* 21 */     return fromString(in.nextString());
/*    */   }
/*    */ 
/*    */   
/*    */   public static String fromUUID(UUID value) {
/* 26 */     return value.toString().replace("-", "");
/*    */   }
/*    */ 
/*    */   
/*    */   public static UUID fromString(String input) {
/* 31 */     return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojan\\util\UUIDTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */