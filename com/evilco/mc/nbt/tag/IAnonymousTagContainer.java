package com.evilco.mc.nbt.tag;

import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import java.util.List;
import javax.annotation.Nonnull;

public interface IAnonymousTagContainer extends ITagContainer {
  void addTag(@Nonnull ITag paramITag);
  
  List<ITag> getTags();
  
  <T extends ITag> List<T> getTags(Class<T> paramClass) throws UnexpectedTagTypeException;
  
  void setTag(int paramInt, @Nonnull ITag paramITag);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\tag\IAnonymousTagContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */