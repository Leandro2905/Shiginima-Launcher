/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileTypeAdapter
/*    */   extends TypeAdapter<File>
/*    */ {
/*    */   public void write(JsonWriter out, File value) throws IOException {
/* 15 */     if (value == null) {
/* 16 */       out.nullValue();
/*    */     } else {
/* 18 */       out.value(value.getAbsolutePath());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public File read(JsonReader in) throws IOException {
/* 25 */     if (in.hasNext()) {
/*    */       
/* 27 */       String name = in.nextString();
/* 28 */       return (name != null) ? new File(name) : null;
/*    */     } 
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\FileTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */