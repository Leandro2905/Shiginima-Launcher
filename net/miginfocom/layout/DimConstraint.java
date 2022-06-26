package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

public final class DimConstraint implements Externalizable {
  final ResizeConstraint resize = new ResizeConstraint();
  
  private String sizeGroup = null;
  
  private BoundSize size = BoundSize.NULL_SIZE;
  
  private BoundSize gapBefore = null;
  
  private BoundSize gapAfter = null;
  
  private UnitValue align = null;
  
  private String endGroup = null;
  
  private boolean fill = false;
  
  private boolean noGrid = false;
  
  public int getGrowPriority() {
    return this.resize.growPrio;
  }
  
  public void setGrowPriority(int paramInt) {
    this.resize.growPrio = paramInt;
  }
  
  public Float getGrow() {
    return this.resize.grow;
  }
  
  public void setGrow(Float paramFloat) {
    this.resize.grow = paramFloat;
  }
  
  public int getShrinkPriority() {
    return this.resize.shrinkPrio;
  }
  
  public void setShrinkPriority(int paramInt) {
    this.resize.shrinkPrio = paramInt;
  }
  
  public Float getShrink() {
    return this.resize.shrink;
  }
  
  public void setShrink(Float paramFloat) {
    this.resize.shrink = paramFloat;
  }
  
  public UnitValue getAlignOrDefault(boolean paramBoolean) {
    return (this.align != null) ? this.align : (paramBoolean ? UnitValue.LEADING : ((this.fill || !PlatformDefaults.getDefaultRowAlignmentBaseline()) ? UnitValue.CENTER : UnitValue.BASELINE_IDENTITY));
  }
  
  public UnitValue getAlign() {
    return this.align;
  }
  
  public void setAlign(UnitValue paramUnitValue) {
    this.align = paramUnitValue;
  }
  
  public BoundSize getGapAfter() {
    return this.gapAfter;
  }
  
  public void setGapAfter(BoundSize paramBoundSize) {
    this.gapAfter = paramBoundSize;
  }
  
  boolean hasGapAfter() {
    return (this.gapAfter != null && !this.gapAfter.isUnset());
  }
  
  boolean isGapAfterPush() {
    return (this.gapAfter != null && this.gapAfter.getGapPush());
  }
  
  public BoundSize getGapBefore() {
    return this.gapBefore;
  }
  
  public void setGapBefore(BoundSize paramBoundSize) {
    this.gapBefore = paramBoundSize;
  }
  
  boolean hasGapBefore() {
    return (this.gapBefore != null && !this.gapBefore.isUnset());
  }
  
  boolean isGapBeforePush() {
    return (this.gapBefore != null && this.gapBefore.getGapPush());
  }
  
  public BoundSize getSize() {
    return this.size;
  }
  
  public void setSize(BoundSize paramBoundSize) {
    if (paramBoundSize != null)
      paramBoundSize.checkNotLinked(); 
    this.size = paramBoundSize;
  }
  
  public String getSizeGroup() {
    return this.sizeGroup;
  }
  
  public void setSizeGroup(String paramString) {
    this.sizeGroup = paramString;
  }
  
  public String getEndGroup() {
    return this.endGroup;
  }
  
  public void setEndGroup(String paramString) {
    this.endGroup = paramString;
  }
  
  public boolean isFill() {
    return this.fill;
  }
  
  public void setFill(boolean paramBoolean) {
    this.fill = paramBoolean;
  }
  
  public boolean isNoGrid() {
    return this.noGrid;
  }
  
  public void setNoGrid(boolean paramBoolean) {
    this.noGrid = paramBoolean;
  }
  
  int[] getRowGaps(ContainerWrapper paramContainerWrapper, BoundSize paramBoundSize, int paramInt, boolean paramBoolean) {
    BoundSize boundSize = paramBoolean ? this.gapBefore : this.gapAfter;
    if (boundSize == null || boundSize.isUnset())
      boundSize = paramBoundSize; 
    if (boundSize == null || boundSize.isUnset())
      return null; 
    int[] arrayOfInt = new int[3];
    for (byte b = 0; b <= 2; b++) {
      UnitValue unitValue = boundSize.getSize(b);
      arrayOfInt[b] = (unitValue != null) ? unitValue.getPixels(paramInt, paramContainerWrapper, null) : -2147471302;
    } 
    return arrayOfInt;
  }
  
  int[] getComponentGaps(ContainerWrapper paramContainerWrapper, ComponentWrapper paramComponentWrapper1, BoundSize paramBoundSize, ComponentWrapper paramComponentWrapper2, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    BoundSize boundSize = (paramInt2 < 2) ? this.gapBefore : this.gapAfter;
    boolean bool = (boundSize != null && boundSize.getGapPush()) ? true : false;
    if ((boundSize == null || boundSize.isUnset()) && (paramBoundSize == null || paramBoundSize.isUnset()) && paramComponentWrapper1 != null)
      boundSize = PlatformDefaults.getDefaultComponentGap(paramComponentWrapper1, paramComponentWrapper2, paramInt2 + 1, paramString, paramBoolean); 
    if (boundSize == null) {
      (new int[3])[0] = 0;
      (new int[3])[1] = 0;
      (new int[3])[2] = -2147471302;
      return bool ? new int[3] : null;
    } 
    int[] arrayOfInt = new int[3];
    for (byte b = 0; b <= 2; b++) {
      UnitValue unitValue = boundSize.getSize(b);
      arrayOfInt[b] = (unitValue != null) ? unitValue.getPixels(paramInt1, paramContainerWrapper, null) : -2147471302;
    } 
    return arrayOfInt;
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInput));
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (getClass() == DimConstraint.class)
      LayoutUtil.writeAsXML(paramObjectOutput, this); 
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\DimConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */