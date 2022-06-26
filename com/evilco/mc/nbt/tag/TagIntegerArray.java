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
/*    */ public class TagIntegerArray
/*    */   extends AbstractTag
/*    */ {
/*    */   protected int[] values;
/*    */   
/*    */   public TagIntegerArray(@Nonnull String name, @Nonnull int[] values) {
/* 27 */     super(name);
/* 28 */     setValues(values);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagIntegerArray(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/* 38 */     super(inputStream, anonymous);
/*    */ 
/*    */     
/* 41 */     int size = inputStream.readInt();
/*    */ 
/*    */     
/* 44 */     int[] data = new int[size];
/*    */ 
/*    */     
/* 47 */     for (int i = 0; i < size; i++) {
/* 48 */       data[i] = inputStream.readInt();
/*    */     }
/*    */ 
/*    */     
/* 52 */     this.values = data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getTagID() {
/* 60 */     return TagType.INTEGER_ARRAY.typeID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getValues() {
/* 68 */     return this.values;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValues(@Nonnull int[] i) {
/* 77 */     Preconditions.checkNotNull(i, "i");
/*    */ 
/*    */     
/* 80 */     this.values = i;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 88 */     super.write(outputStream, anonymous);
/*    */ 
/*    */     
/* 91 */     outputStream.writeInt(this.values.length);
/*    */ 
/*    */     
/* 94 */     for (int i : this.values)
/* 95 */       outputStream.writeInt(i); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagIntegerArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */