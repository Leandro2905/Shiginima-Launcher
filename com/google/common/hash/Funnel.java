package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
public interface Funnel<T> extends Serializable {
  void funnel(T paramT, PrimitiveSink paramPrimitiveSink);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\Funnel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */