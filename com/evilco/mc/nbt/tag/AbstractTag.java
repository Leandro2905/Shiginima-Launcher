/*     */ package com.evilco.mc.nbt.tag;
/*     */ 
/*     */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*     */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTag
/*     */   implements ITag
/*     */ {
/*     */   protected String name;
/*  25 */   protected ITagContainer parent = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractTag(@Nonnull String name) {
/*  32 */     setName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractTag(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/*  43 */     Preconditions.checkNotNull(inputStream, "inputStream");
/*     */ 
/*     */     
/*  46 */     if (!anonymous) {
/*     */       
/*  48 */       int nameSize = inputStream.readShort();
/*  49 */       byte[] nameBytes = new byte[nameSize];
/*     */ 
/*     */       
/*  52 */       inputStream.readFully(nameBytes);
/*  53 */       setName(new String(nameBytes, STRING_CHARSET));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  62 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getNameBytes() {
/*  70 */     return this.name.getBytes(STRING_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ITagContainer getParent() {
/*  78 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract byte getTagID();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(@Nonnull String name) {
/*  93 */     Preconditions.checkNotNull(name, "name");
/*     */ 
/*     */     
/*  96 */     if (getParent() != null) getParent().removeTag(this);
/*     */ 
/*     */     
/*  99 */     this.name = name;
/*     */ 
/*     */     
/* 102 */     if (getParent() != null) {
/* 103 */       if (getParent() instanceof IAnonymousTagContainer) {
/* 104 */         ((IAnonymousTagContainer)getParent()).addTag(this);
/*     */       } else {
/* 106 */         ((INamedTagContainer)getParent()).setTag(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(@Nullable ITagContainer parent) {
/* 116 */     if (getParent() != null) getParent().removeTag(this);
/*     */ 
/*     */     
/* 119 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 128 */     if (!anonymous) {
/*     */       
/* 130 */       byte[] name = getNameBytes();
/*     */ 
/*     */       
/* 133 */       outputStream.writeShort(name.length);
/*     */ 
/*     */       
/* 136 */       outputStream.write(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\AbstractTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */