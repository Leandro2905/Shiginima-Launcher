package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

public final class LC implements Externalizable {
  private int wrapAfter = 2097051;
  
  private Boolean leftToRight = null;
  
  private UnitValue[] insets = null;
  
  private UnitValue alignX = null;
  
  private UnitValue alignY = null;
  
  private BoundSize gridGapX = null;
  
  private BoundSize gridGapY = null;
  
  private BoundSize width = BoundSize.NULL_SIZE;
  
  private BoundSize height = BoundSize.NULL_SIZE;
  
  private BoundSize packW = BoundSize.NULL_SIZE;
  
  private BoundSize packH = BoundSize.NULL_SIZE;
  
  private float pwAlign = 0.5F;
  
  private float phAlign = 1.0F;
  
  private int debugMillis = 0;
  
  private int hideMode = 0;
  
  private boolean noCache = false;
  
  private boolean flowX = true;
  
  private boolean fillX = false;
  
  private boolean fillY = false;
  
  private boolean topToBottom = true;
  
  private boolean noGrid = false;
  
  private boolean visualPadding = true;
  
  public boolean isNoCache() {
    return this.noCache;
  }
  
  public void setNoCache(boolean paramBoolean) {
    this.noCache = paramBoolean;
  }
  
  public final UnitValue getAlignX() {
    return this.alignX;
  }
  
  public final void setAlignX(UnitValue paramUnitValue) {
    this.alignX = paramUnitValue;
  }
  
  public final UnitValue getAlignY() {
    return this.alignY;
  }
  
  public final void setAlignY(UnitValue paramUnitValue) {
    this.alignY = paramUnitValue;
  }
  
  public final int getDebugMillis() {
    return this.debugMillis;
  }
  
  public final void setDebugMillis(int paramInt) {
    this.debugMillis = paramInt;
  }
  
  public final boolean isFillX() {
    return this.fillX;
  }
  
  public final void setFillX(boolean paramBoolean) {
    this.fillX = paramBoolean;
  }
  
  public final boolean isFillY() {
    return this.fillY;
  }
  
  public final void setFillY(boolean paramBoolean) {
    this.fillY = paramBoolean;
  }
  
  public final boolean isFlowX() {
    return this.flowX;
  }
  
  public final void setFlowX(boolean paramBoolean) {
    this.flowX = paramBoolean;
  }
  
  public final BoundSize getGridGapX() {
    return this.gridGapX;
  }
  
  public final void setGridGapX(BoundSize paramBoundSize) {
    this.gridGapX = paramBoundSize;
  }
  
  public final BoundSize getGridGapY() {
    return this.gridGapY;
  }
  
  public final void setGridGapY(BoundSize paramBoundSize) {
    this.gridGapY = paramBoundSize;
  }
  
  public final int getHideMode() {
    return this.hideMode;
  }
  
  public final void setHideMode(int paramInt) {
    if (paramInt < 0 || paramInt > 3)
      throw new IllegalArgumentException("Wrong hideMode: " + paramInt); 
    this.hideMode = paramInt;
  }
  
  public final UnitValue[] getInsets() {
    (new UnitValue[4])[0] = this.insets[0];
    (new UnitValue[4])[1] = this.insets[1];
    (new UnitValue[4])[2] = this.insets[2];
    (new UnitValue[4])[3] = this.insets[3];
    return (this.insets != null) ? new UnitValue[4] : null;
  }
  
  public final void setInsets(UnitValue[] paramArrayOfUnitValue) {
    (new UnitValue[4])[0] = paramArrayOfUnitValue[0];
    (new UnitValue[4])[1] = paramArrayOfUnitValue[1];
    (new UnitValue[4])[2] = paramArrayOfUnitValue[2];
    (new UnitValue[4])[3] = paramArrayOfUnitValue[3];
    this.insets = (paramArrayOfUnitValue != null) ? new UnitValue[4] : null;
  }
  
  public final Boolean getLeftToRight() {
    return this.leftToRight;
  }
  
  public final void setLeftToRight(Boolean paramBoolean) {
    this.leftToRight = paramBoolean;
  }
  
  public final boolean isNoGrid() {
    return this.noGrid;
  }
  
  public final void setNoGrid(boolean paramBoolean) {
    this.noGrid = paramBoolean;
  }
  
  public final boolean isTopToBottom() {
    return this.topToBottom;
  }
  
  public final void setTopToBottom(boolean paramBoolean) {
    this.topToBottom = paramBoolean;
  }
  
  public final boolean isVisualPadding() {
    return this.visualPadding;
  }
  
  public final void setVisualPadding(boolean paramBoolean) {
    this.visualPadding = paramBoolean;
  }
  
  public final int getWrapAfter() {
    return this.wrapAfter;
  }
  
  public final void setWrapAfter(int paramInt) {
    this.wrapAfter = paramInt;
  }
  
  public final BoundSize getPackWidth() {
    return this.packW;
  }
  
  public final void setPackWidth(BoundSize paramBoundSize) {
    this.packW = (paramBoundSize != null) ? paramBoundSize : BoundSize.NULL_SIZE;
  }
  
  public final BoundSize getPackHeight() {
    return this.packH;
  }
  
  public final void setPackHeight(BoundSize paramBoundSize) {
    this.packH = (paramBoundSize != null) ? paramBoundSize : BoundSize.NULL_SIZE;
  }
  
  public final float getPackHeightAlign() {
    return this.phAlign;
  }
  
  public final void setPackHeightAlign(float paramFloat) {
    this.phAlign = Math.max(0.0F, Math.min(1.0F, paramFloat));
  }
  
  public final float getPackWidthAlign() {
    return this.pwAlign;
  }
  
  public final void setPackWidthAlign(float paramFloat) {
    this.pwAlign = Math.max(0.0F, Math.min(1.0F, paramFloat));
  }
  
  public final BoundSize getWidth() {
    return this.width;
  }
  
  public final void setWidth(BoundSize paramBoundSize) {
    this.width = (paramBoundSize != null) ? paramBoundSize : BoundSize.NULL_SIZE;
  }
  
  public final BoundSize getHeight() {
    return this.height;
  }
  
  public final void setHeight(BoundSize paramBoundSize) {
    this.height = (paramBoundSize != null) ? paramBoundSize : BoundSize.NULL_SIZE;
  }
  
  public final LC pack() {
    return pack("pref", "pref");
  }
  
  public final LC pack(String paramString1, String paramString2) {
    setPackWidth((paramString1 != null) ? ConstraintParser.parseBoundSize(paramString1, false, false) : BoundSize.NULL_SIZE);
    setPackHeight((paramString2 != null) ? ConstraintParser.parseBoundSize(paramString2, false, false) : BoundSize.NULL_SIZE);
    return this;
  }
  
  public final LC packAlign(float paramFloat1, float paramFloat2) {
    setPackWidthAlign(paramFloat1);
    setPackHeightAlign(paramFloat2);
    return this;
  }
  
  public final LC wrap() {
    setWrapAfter(0);
    return this;
  }
  
  public final LC wrapAfter(int paramInt) {
    setWrapAfter(paramInt);
    return this;
  }
  
  public final LC noCache() {
    setNoCache(true);
    return this;
  }
  
  public final LC flowY() {
    setFlowX(false);
    return this;
  }
  
  public final LC flowX() {
    setFlowX(true);
    return this;
  }
  
  public final LC fill() {
    setFillX(true);
    setFillY(true);
    return this;
  }
  
  public final LC fillX() {
    setFillX(true);
    return this;
  }
  
  public final LC fillY() {
    setFillY(true);
    return this;
  }
  
  public final LC leftToRight(boolean paramBoolean) {
    setLeftToRight(paramBoolean ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }
  
  public final LC rightToLeft() {
    setLeftToRight(Boolean.FALSE);
    return this;
  }
  
  public final LC bottomToTop() {
    setTopToBottom(false);
    return this;
  }
  
  public final LC topToBottom() {
    setTopToBottom(true);
    return this;
  }
  
  public final LC noGrid() {
    setNoGrid(true);
    return this;
  }
  
  public final LC noVisualPadding() {
    setVisualPadding(false);
    return this;
  }
  
  public final LC insetsAll(String paramString) {
    UnitValue unitValue1 = ConstraintParser.parseUnitValue(paramString, true);
    UnitValue unitValue2 = ConstraintParser.parseUnitValue(paramString, false);
    this.insets = new UnitValue[] { unitValue2, unitValue1, unitValue2, unitValue1 };
    return this;
  }
  
  public final LC insets(String paramString) {
    this.insets = ConstraintParser.parseInsets(paramString, true);
    return this;
  }
  
  public final LC insets(String paramString1, String paramString2, String paramString3, String paramString4) {
    this.insets = new UnitValue[] { ConstraintParser.parseUnitValue(paramString1, false), ConstraintParser.parseUnitValue(paramString2, true), ConstraintParser.parseUnitValue(paramString3, false), ConstraintParser.parseUnitValue(paramString4, true) };
    return this;
  }
  
  public final LC alignX(String paramString) {
    setAlignX(ConstraintParser.parseUnitValueOrAlign(paramString, true, null));
    return this;
  }
  
  public final LC alignY(String paramString) {
    setAlignY(ConstraintParser.parseUnitValueOrAlign(paramString, false, null));
    return this;
  }
  
  public final LC align(String paramString1, String paramString2) {
    if (paramString1 != null)
      alignX(paramString1); 
    if (paramString2 != null)
      alignY(paramString2); 
    return this;
  }
  
  public final LC gridGapX(String paramString) {
    setGridGapX(ConstraintParser.parseBoundSize(paramString, true, true));
    return this;
  }
  
  public final LC gridGapY(String paramString) {
    setGridGapY(ConstraintParser.parseBoundSize(paramString, true, false));
    return this;
  }
  
  public final LC gridGap(String paramString1, String paramString2) {
    if (paramString1 != null)
      gridGapX(paramString1); 
    if (paramString2 != null)
      gridGapY(paramString2); 
    return this;
  }
  
  public final LC debug(int paramInt) {
    setDebugMillis(paramInt);
    return this;
  }
  
  public final LC hideMode(int paramInt) {
    setHideMode(paramInt);
    return this;
  }
  
  public final LC minWidth(String paramString) {
    setWidth(LayoutUtil.derive(getWidth(), ConstraintParser.parseUnitValue(paramString, true), null, null));
    return this;
  }
  
  public final LC width(String paramString) {
    setWidth(ConstraintParser.parseBoundSize(paramString, false, true));
    return this;
  }
  
  public final LC maxWidth(String paramString) {
    setWidth(LayoutUtil.derive(getWidth(), null, null, ConstraintParser.parseUnitValue(paramString, true)));
    return this;
  }
  
  public final LC minHeight(String paramString) {
    setHeight(LayoutUtil.derive(getHeight(), ConstraintParser.parseUnitValue(paramString, false), null, null));
    return this;
  }
  
  public final LC height(String paramString) {
    setHeight(ConstraintParser.parseBoundSize(paramString, false, false));
    return this;
  }
  
  public final LC maxHeight(String paramString) {
    setHeight(LayoutUtil.derive(getHeight(), null, null, ConstraintParser.parseUnitValue(paramString, false)));
    return this;
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInput));
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (getClass() == LC.class)
      LayoutUtil.writeAsXML(paramObjectOutput, this); 
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\LC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */