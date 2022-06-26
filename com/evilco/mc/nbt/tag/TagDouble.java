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
/*    */ public class TagDouble
/*    */   extends AbstractTag
/*    */ {
/*    */   protected double value;
/*    */   
/*    */   public TagDouble(@Nonnull String name, double value) {
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
/*    */   public TagDouble(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 37 */     super(inputStream, anonymous);
/*    */     
/* 39 */     setValue(inputStream.readDouble());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 47 */     return TagType.DOUBLE.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(double d) {
/* 63 */     this.value = d;
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
/* 74 */     outputStream.writeDouble(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */