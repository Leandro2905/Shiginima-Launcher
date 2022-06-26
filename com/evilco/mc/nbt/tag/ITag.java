/*    */ package com.evilco.mc.nbt.tag;
/*    */ 
/*    */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ITag
/*    */ {
/* 19 */   public static final Charset STRING_CHARSET = Charset.forName("UTF-8");
/*    */   
/*    */   String getName();
/*    */   
/*    */   byte[] getNameBytes();
/*    */   
/*    */   ITagContainer getParent();
/*    */   
/*    */   byte getTagID();
/*    */   
/*    */   void setName(@Nonnull String paramString);
/*    */   
/*    */   void setParent(@Nullable ITagContainer paramITagContainer);
/*    */   
/*    */   void write(NbtOutputStream paramNbtOutputStream, boolean paramBoolean) throws IOException;
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\ITag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */