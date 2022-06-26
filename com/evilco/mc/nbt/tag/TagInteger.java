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
/*    */ public class TagInteger
/*    */   extends AbstractTag
/*    */ {
/*    */   protected int value;
/*    */   
/*    */   public TagInteger(@Nonnull String name, int value) {
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
/*    */   public TagInteger(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 37 */     super(inputStream, anonymous);
/*    */     
/* 39 */     setValue(inputStream.readInt());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 47 */     return TagType.INTEGER.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(int i) {
/* 63 */     this.value = i;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 71 */     super.write(outputStream, anonymous);
/*    */ 
/*    */     
/* 74 */     outputStream.writeInt(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */