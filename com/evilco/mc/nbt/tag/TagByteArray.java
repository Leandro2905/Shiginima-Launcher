/*    */ package com.evilco.mc.nbt.tag;
/*    */ 
/*    */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*    */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ public class TagByteArray
/*    */   extends AbstractTag
/*    */ {
/*    */   protected byte[] value;
/*    */   
/*    */   public TagByteArray(@Nonnull String name, @Nonnull byte[] value) {
/* 27 */     super(name);
/* 28 */     setValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagByteArray(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 38 */     super(inputStream, anonymous);
/*    */ 
/*    */     
/* 41 */     int size = inputStream.readInt();
/*    */ 
/*    */     
/* 44 */     byte[] data = new byte[size];
/* 45 */     inputStream.readFully(data);
/*    */ 
/*    */     
/* 48 */     setValue(data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 56 */     return TagType.BYTE_ARRAY.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getValue() {
/* 64 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(@Nonnull byte[] b) {
/* 73 */     Preconditions.checkNotNull(b, "b");
/*    */ 
/*    */     
/* 76 */     this.value = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 84 */     super.write(outputStream, anonymous);
/*    */ 
/*    */     
/* 87 */     outputStream.writeInt(this.value.length);
/*    */ 
/*    */     
/* 90 */     outputStream.write(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */