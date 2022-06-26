/*    */ package com.evilco.mc.nbt.tag;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TagType
/*    */ {
/* 12 */   BYTE(1, (Class)TagByte.class),
/* 13 */   BYTE_ARRAY(7, (Class)TagByteArray.class),
/* 14 */   COMPOUND(10, (Class)TagCompound.class),
/* 15 */   DOUBLE(6, (Class)TagDouble.class),
/* 16 */   END(0, null),
/* 17 */   FLOAT(5, (Class)TagFloat.class),
/* 18 */   INTEGER(3, (Class)TagInteger.class),
/* 19 */   INTEGER_ARRAY(11, (Class)TagIntegerArray.class),
/* 20 */   LIST(9, (Class)TagList.class),
/* 21 */   LONG(4, (Class)TagLong.class),
/* 22 */   SHORT(2, (Class)TagShort.class),
/* 23 */   STRING(8, (Class)TagString.class);
/*    */   
/*    */   protected static final Map<Byte, TagType> typeMap;
/*    */   public final Class<? extends ITag> tagType;
/*    */   public final byte typeID;
/*    */   
/*    */   static {
/* 30 */     ImmutableMap.Builder<Byte, TagType> mapBuilder = new ImmutableMap.Builder();
/*    */ 
/*    */     
/* 33 */     for (TagType type : values()) {
/* 34 */       mapBuilder.put(Byte.valueOf(type.typeID), type);
/*    */     }
/*    */ 
/*    */     
/* 38 */     typeMap = (Map<Byte, TagType>)mapBuilder.build();
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
/*    */   TagType(int typeID, Class<? extends ITag> type) {
/* 62 */     this.typeID = (byte)typeID;
/* 63 */     this.tagType = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */