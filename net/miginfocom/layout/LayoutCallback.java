package net.miginfocom.layout;

public abstract class LayoutCallback {
  public UnitValue[] getPosition(ComponentWrapper paramComponentWrapper) {
    return null;
  }
  
  public BoundSize[] getSize(ComponentWrapper paramComponentWrapper) {
    return null;
  }
  
  public void correctBounds(ComponentWrapper paramComponentWrapper) {}
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\LayoutCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */