/*    */ package com.evilco.mc.nbt.tag;
/*    */ 
/*    */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*    */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nonnull;
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
/*    */ public class TagByte
/*    */   extends AbstractTag
/*    */ {
/*    */   protected byte value;
/*    */   
/*    */   public TagByte(@Nonnull String name, byte value) {
/* 26 */     super(name);
/* 27 */     setValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagByte(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 37 */     super(inputStream, anonymous);
/*    */ 
/*    */     
/* 40 */     setValue(inputStream.readByte());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 48 */     return TagType.BYTE.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(byte b) {
/* 64 */     this.value = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 72 */     super.write(outputStream, anonymous);
/*    */ 
/*    */     
/* 75 */     outputStream.write(getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagByte.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */