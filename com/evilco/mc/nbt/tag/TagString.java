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
/*    */ public class TagString
/*    */   extends AbstractTag
/*    */ {
/*    */   protected String value;
/*    */   
/*    */   public TagString(@Nonnull String name, @Nonnull String value) {
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
/*    */   public TagString(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 37 */     super(inputStream, anonymous);
/*    */ 
/*    */     
/* 40 */     int size = inputStream.readShort();
/*    */ 
/*    */     
/* 43 */     byte[] data = new byte[size];
/* 44 */     inputStream.readFully(data);
/*    */ 
/*    */     
/* 47 */     setValue(new String(data, ITag.STRING_CHARSET));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 55 */     return TagType.STRING.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 63 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(@Nonnull String s) {
/* 71 */     this.value = s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 79 */     super.write(outputStream, anonymous);
/*    */ 
/*    */ 
/*    */     
/* 83 */     byte[] outputBytes = this.value.getBytes(ITag.STRING_CHARSET);
/* 84 */     outputStream.writeShort(outputBytes.length);
/*    */ 
/*    */     
/* 87 */     outputStream.write(outputBytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */