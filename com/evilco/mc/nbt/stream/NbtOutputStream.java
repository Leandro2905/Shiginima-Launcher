/*    */ package com.evilco.mc.nbt.stream;
/*    */ 
/*    */ import com.evilco.mc.nbt.tag.ITag;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NbtOutputStream
/*    */   extends DataOutputStream
/*    */ {
/*    */   public NbtOutputStream(OutputStream out) {
/* 20 */     super(out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(ITag tag) throws IOException {
/* 30 */     writeByte(tag.getTagID());
/*    */ 
/*    */     
/* 33 */     tag.write(this, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\stream\NbtOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */