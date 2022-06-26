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
/*    */ public class TagFloat
/*    */   extends AbstractTag
/*    */ {
/*    */   protected float value;
/*    */   
/*    */   public TagFloat(@Nonnull String name, float value) {
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
/*    */   public TagFloat(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 37 */     super(inputStream, anonymous);
/*    */     
/* 39 */     setValue(inputStream.readFloat());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 47 */     return TagType.FLOAT.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(float f) {
/* 63 */     this.value = f;
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
/* 74 */     outputStream.writeFloat(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */