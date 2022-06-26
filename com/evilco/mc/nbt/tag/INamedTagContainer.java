package com.evilco.mc.nbt.tag;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import java.util.Map;
import javax.annotation.Nonnull;

public interface INamedTagContainer extends ITagContainer {
  ITag getTag(@Nonnull String paramString);
  
  <T extends ITag> T getTag(String paramString, Class<T> paramClass) throws UnexpectedTagTypeException, TagNotFoundException;
  
  Map<String, ITag> getTags();
  
  void removeTag(@Nonnull String paramString);
  
  void setTag(@Nonnull ITag paramITag);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\INamedTagContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */