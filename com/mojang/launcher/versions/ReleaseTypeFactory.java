package com.mojang.launcher.versions;

public interface ReleaseTypeFactory<T extends ReleaseType> extends Iterable<T> {
  T getTypeByName(String paramString);
  
  T[] getAllTypes();
  
  Class<T> getTypeClass();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\versions\ReleaseTypeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */