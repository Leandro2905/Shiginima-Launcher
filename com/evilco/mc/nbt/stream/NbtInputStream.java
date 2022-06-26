/*    */ package com.evilco.mc.nbt.stream;
/*    */ 
/*    */ import com.evilco.mc.nbt.tag.ITag;
/*    */ import com.evilco.mc.nbt.tag.TagType;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NbtInputStream
/*    */   extends DataInputStream
/*    */ {
/*    */   public NbtInputStream(InputStream in) {
/* 22 */     super(in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ITag readTag() throws IOException {
/* 32 */     byte type = readByte();
/*    */ 
/*    */     
/* 35 */     TagType tagType = TagType.valueOf(type);
/*    */ 
/*    */     
/* 38 */     if (tagType == null) throw new IOException("Invalid NBT tag: Found unknown tag type " + type + ".");
/*    */ 
/*    */     
/* 41 */     if (tagType == TagType.END) return null;
/*    */ 
/*    */     
/* 44 */     return readTag(tagType, false);
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
/*    */   public ITag readTag(TagType type, boolean anonymous) throws IOException {
/* 56 */     Constructor<? extends ITag> constructor = null;
/*    */     
/*    */     try {
/* 59 */       constructor = type.tagType.getConstructor(new Class[] { NbtInputStream.class, boolean.class });
/* 60 */     } catch (NoSuchMethodException ex) {
/* 61 */       throw new IOException("Invalid NBT implementation state: Type " + type.tagType.getName() + " has no de-serialization constructor.");
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 66 */       return constructor.newInstance(new Object[] { this, Boolean.valueOf(anonymous) });
/* 67 */     } catch (Exception ex) {
/* 68 */       throw new IOException("Invalid NBT implementation state: Type " + type.tagType.getName() + " in (" + getClass().getName() + ") has no valid constructor: " + ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\stream\NbtInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */