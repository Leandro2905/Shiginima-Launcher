/*     */ package com.evilco.mc.nbt.tag;
/*     */ 
/*     */ import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
/*     */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*     */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TagList
/*     */   extends AbstractTag
/*     */   implements IAnonymousTagContainer
/*     */ {
/*     */   protected List<ITag> tagList;
/*     */   
/*     */   public TagList(@Nonnull String name) {
/*  29 */     super(name);
/*  30 */     this.tagList = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagList(@Nonnull String name, @Nonnull List<ITag> tagList) {
/*  39 */     super(name);
/*     */ 
/*     */     
/*  42 */     Preconditions.checkNotNull(tagList, "tagList");
/*     */ 
/*     */     
/*  45 */     this.tagList = tagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagList(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/*  55 */     super(inputStream, anonymous);
/*     */ 
/*     */     
/*  58 */     this.tagList = new ArrayList<>();
/*     */ 
/*     */     
/*  61 */     byte type = inputStream.readByte();
/*     */ 
/*     */     
/*  64 */     TagType tagType = TagType.valueOf(type);
/*     */ 
/*     */     
/*  67 */     int size = inputStream.readInt();
/*     */ 
/*     */     
/*  70 */     if (tagType == TagType.END) {
/*     */       return;
/*     */     }
/*  73 */     for (int i = 0; i < size; i++) {
/*  74 */       addTag(inputStream.readTag(tagType, true));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTag(@Nonnull ITag tag) {
/*  83 */     this.tagList.add(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ITag> getTags() {
/*  91 */     return (List<ITag>)(new ImmutableList.Builder()).addAll(this.tagList).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends ITag> List<T> getTags(Class<T> tagClass) throws UnexpectedTagTypeException {
/*  99 */     ImmutableList.Builder<T> builder = new ImmutableList.Builder();
/* 100 */     for (ITag tag : this.tagList) {
/* 101 */       if (!tagClass.isInstance(tag))
/* 102 */         throw new UnexpectedTagTypeException("The list entry should be of type " + tagClass
/*     */             
/* 104 */             .getSimpleName() + ", but is of type " + tag
/*     */             
/* 106 */             .getClass().getSimpleName()); 
/* 107 */       builder.add(tag);
/*     */     } 
/* 109 */     return (List<T>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getTagID() {
/* 117 */     return TagType.LIST.typeID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTag(@Nonnull ITag tag) {
/* 125 */     this.tagList.remove(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(int i, @Nonnull ITag tag) {
/* 133 */     this.tagList.set(i, tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 141 */     super.write(outputStream, anonymous);
/*     */ 
/*     */     
/* 144 */     outputStream.writeByte((this.tagList.size() > 0) ? ((ITag)this.tagList.get(0)).getTagID() : TagType.END.typeID);
/*     */ 
/*     */     
/* 147 */     outputStream.writeInt(this.tagList.size());
/*     */ 
/*     */     
/* 150 */     for (ITag tag : this.tagList)
/*     */     {
/* 152 */       tag.write(outputStream, true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */