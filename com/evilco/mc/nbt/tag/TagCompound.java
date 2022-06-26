/*     */ package com.evilco.mc.nbt.tag;
/*     */ 
/*     */ import com.evilco.mc.nbt.error.TagNotFoundException;
/*     */ import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
/*     */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*     */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class TagCompound
/*     */   extends AbstractTag
/*     */   implements INamedTagContainer
/*     */ {
/*     */   protected Map<String, ITag> tags;
/*     */   
/*     */   public TagCompound(String name) {
/*  32 */     super(name);
/*     */     
/*  34 */     this.tags = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagCompound(@Nonnull NbtInputStream inputStream, boolean anonymous) throws IOException {
/*  43 */     super(inputStream, anonymous);
/*     */ 
/*     */     
/*  46 */     this.tags = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  51 */       byte type = inputStream.readByte();
/*     */ 
/*     */       
/*  54 */       TagType tagType = TagType.valueOf(type);
/*     */ 
/*     */       
/*  57 */       if (tagType == null) throw new IOException("Could not find a tag for type ID " + type + ".");
/*     */ 
/*     */       
/*  60 */       if (tagType == TagType.END) {
/*     */         break;
/*     */       }
/*  63 */       setTag(inputStream.readTag(tagType, false));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ITag getTag(@Nonnull String name) {
/*  73 */     Preconditions.checkNotNull(name, "name");
/*     */ 
/*     */     
/*  76 */     return this.tags.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends ITag> T getTag(String name, Class<T> tagClass) throws UnexpectedTagTypeException, TagNotFoundException {
/*  85 */     ITag tag = getTag(name);
/*     */     
/*  87 */     if (tag == null) {
/*  88 */       throw new TagNotFoundException("The compound tag is missing a " + name + " entry");
/*     */     }
/*  90 */     if (!tagClass.isInstance(tag)) {
/*  91 */       throw new UnexpectedTagTypeException("The compound entry " + name + " should be of type " + tagClass
/*  92 */           .getSimpleName() + ", but is of type " + tag
/*  93 */           .getClass().getSimpleName());
/*     */     }
/*  95 */     return (T)tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagCompound getCompound(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 106 */     return getTag(name, TagCompound.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInteger(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 117 */     return ((TagInteger)getTag(name, TagInteger.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getShort(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 128 */     return ((TagShort)getTag(name, TagShort.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getByte(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 139 */     return ((TagByte)getTag(name, TagByte.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLong(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 150 */     return ((TagLong)getTag(name, TagLong.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDouble(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 161 */     return ((TagDouble)getTag(name, TagDouble.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloat(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 172 */     return ((TagFloat)getTag(name, TagFloat.class)).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 183 */     return ((TagString)getTag(name, TagString.class)).getValue();
/*     */   }
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
/*     */   public <T extends ITag> List<T> getList(String name, Class<T> itemClass) throws UnexpectedTagTypeException, TagNotFoundException {
/* 197 */     return ((TagList)getTag(name, TagList.class)).getTags(itemClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIntegerArray(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 208 */     return ((TagIntegerArray)getTag(name, TagIntegerArray.class)).getValues();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getByteArray(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 219 */     return ((TagByteArray)getTag(name, TagByteArray.class)).getValue();
/*     */   }
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
/*     */   public String[] getStringArray(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 232 */     List<TagString> tags = getList(name, TagString.class);
/* 233 */     String[] array = new String[tags.size()];
/* 234 */     for (int i = 0; i < tags.size(); i++) {
/* 235 */       array[i] = ((TagString)tags.get(i)).getValue();
/*     */     }
/* 237 */     return array;
/*     */   }
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
/*     */   public double[] getDoubleArray(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 250 */     List<TagDouble> tags = getList(name, TagDouble.class);
/* 251 */     double[] array = new double[tags.size()];
/* 252 */     for (int i = 0; i < tags.size(); i++) {
/* 253 */       array[i] = ((TagDouble)tags.get(i)).getValue();
/*     */     }
/* 255 */     return array;
/*     */   }
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
/*     */   public float[] getFloatArray(String name) throws UnexpectedTagTypeException, TagNotFoundException {
/* 268 */     List<TagFloat> tags = getList(name, TagFloat.class);
/* 269 */     float[] array = new float[tags.size()];
/* 270 */     for (int i = 0; i < tags.size(); i++) {
/* 271 */       array[i] = ((TagFloat)tags.get(i)).getValue();
/*     */     }
/* 273 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ITag> getTags() {
/* 281 */     return (Map<String, ITag>)(new ImmutableMap.Builder()).putAll(this.tags).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTag(@Nonnull ITag tag) {
/* 290 */     Preconditions.checkNotNull(tag, "tag");
/*     */ 
/*     */     
/* 293 */     this.tags.remove(tag.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTag(@Nonnull String tag) {
/* 302 */     Preconditions.checkNotNull(tag, "tag");
/*     */ 
/*     */     
/* 305 */     this.tags.remove(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(@Nonnull ITag tag) {
/* 314 */     Preconditions.checkNotNull(tag, "tag");
/*     */ 
/*     */     
/* 317 */     if (this.tags.containsKey(tag)) ((ITag)this.tags.get(tag.getName())).setParent(null);
/*     */ 
/*     */     
/* 320 */     this.tags.put(tag.getName(), tag);
/*     */ 
/*     */     
/* 323 */     tag.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getTagID() {
/* 331 */     return TagType.COMPOUND.typeID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(NbtOutputStream outputStream, boolean anonymous) throws IOException {
/* 339 */     super.write(outputStream, anonymous);
/*     */ 
/*     */     
/* 342 */     for (Map.Entry<String, ITag> tagEntry : this.tags.entrySet()) {
/*     */       
/* 344 */       outputStream.writeByte(((ITag)tagEntry.getValue()).getTagID());
/*     */ 
/*     */       
/* 347 */       ((ITag)tagEntry.getValue()).write(outputStream, false);
/*     */     } 
/*     */ 
/*     */     
/* 351 */     outputStream.writeByte(TagType.END.typeID);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\TagCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */