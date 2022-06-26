package net.miginfocom.layout;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public final class UnitValue implements Serializable {
  private static final HashMap<String, Integer> UNIT_MAP = new HashMap<String, Integer>(32);
  
  private static final ArrayList<UnitConverter> CONVERTERS = new ArrayList<UnitConverter>();
  
  public static final int STATIC = 100;
  
  public static final int ADD = 101;
  
  public static final int SUB = 102;
  
  public static final int MUL = 103;
  
  public static final int DIV = 104;
  
  public static final int MIN = 105;
  
  public static final int MAX = 106;
  
  public static final int MID = 107;
  
  public static final int PIXEL = 0;
  
  public static final int LPX = 1;
  
  public static final int LPY = 2;
  
  public static final int MM = 3;
  
  public static final int CM = 4;
  
  public static final int INCH = 5;
  
  public static final int PERCENT = 6;
  
  public static final int PT = 7;
  
  public static final int SPX = 8;
  
  public static final int SPY = 9;
  
  public static final int ALIGN = 12;
  
  public static final int MIN_SIZE = 13;
  
  public static final int PREF_SIZE = 14;
  
  public static final int MAX_SIZE = 15;
  
  public static final int BUTTON = 16;
  
  public static final int LINK_X = 18;
  
  public static final int LINK_Y = 19;
  
  public static final int LINK_W = 20;
  
  public static final int LINK_H = 21;
  
  public static final int LINK_X2 = 22;
  
  public static final int LINK_Y2 = 23;
  
  public static final int LINK_XPOS = 24;
  
  public static final int LINK_YPOS = 25;
  
  public static final int LOOKUP = 26;
  
  public static final int LABEL_ALIGN = 27;
  
  private static final int IDENTITY = -1;
  
  static final UnitValue ZERO = new UnitValue(0.0F, null, 0, true, 100, null, null, "0px");
  
  static final UnitValue TOP = new UnitValue(0.0F, null, 6, false, 100, null, null, "top");
  
  static final UnitValue LEADING = new UnitValue(0.0F, null, 6, true, 100, null, null, "leading");
  
  static final UnitValue LEFT = new UnitValue(0.0F, null, 6, true, 100, null, null, "left");
  
  static final UnitValue CENTER = new UnitValue(50.0F, null, 6, true, 100, null, null, "center");
  
  static final UnitValue TRAILING = new UnitValue(100.0F, null, 6, true, 100, null, null, "trailing");
  
  static final UnitValue RIGHT = new UnitValue(100.0F, null, 6, true, 100, null, null, "right");
  
  static final UnitValue BOTTOM = new UnitValue(100.0F, null, 6, false, 100, null, null, "bottom");
  
  static final UnitValue LABEL = new UnitValue(0.0F, null, 27, false, 100, null, null, "label");
  
  static final UnitValue INF = new UnitValue(2097051.0F, null, 0, true, 100, null, null, "inf");
  
  static final UnitValue BASELINE_IDENTITY = new UnitValue(0.0F, null, -1, false, 100, null, null, "baseline");
  
  private final transient float value;
  
  private final transient int unit;
  
  private final transient int oper;
  
  private final transient String unitStr;
  
  private transient String linkId = null;
  
  private final transient boolean isHor;
  
  private final transient UnitValue[] subUnits;
  
  private static final float[] SCALE = new float[] { 25.4F, 2.54F, 1.0F, 0.0F, 72.0F };
  
  private static final long serialVersionUID = 1L;
  
  public UnitValue(float paramFloat) {
    this(paramFloat, null, 0, true, 100, null, null, paramFloat + "px");
  }
  
  public UnitValue(float paramFloat, int paramInt, String paramString) {
    this(paramFloat, null, paramInt, true, 100, null, null, paramString);
  }
  
  UnitValue(float paramFloat, String paramString1, boolean paramBoolean, int paramInt, String paramString2) {
    this(paramFloat, paramString1, -1, paramBoolean, paramInt, null, null, paramString2);
  }
  
  UnitValue(boolean paramBoolean, int paramInt, UnitValue paramUnitValue1, UnitValue paramUnitValue2, String paramString) {
    this(0.0F, "", -1, paramBoolean, paramInt, paramUnitValue1, paramUnitValue2, paramString);
    if (paramUnitValue1 == null || paramUnitValue2 == null)
      throw new IllegalArgumentException("Sub units is null!"); 
  }
  
  private UnitValue(float paramFloat, String paramString1, int paramInt1, boolean paramBoolean, int paramInt2, UnitValue paramUnitValue1, UnitValue paramUnitValue2, String paramString2) {
    if (paramInt2 < 100 || paramInt2 > 107)
      throw new IllegalArgumentException("Unknown Operation: " + paramInt2); 
    if (paramInt2 >= 101 && paramInt2 <= 107 && (paramUnitValue1 == null || paramUnitValue2 == null))
      throw new IllegalArgumentException(paramInt2 + " Operation may not have null sub-UnitValues."); 
    this.value = paramFloat;
    this.oper = paramInt2;
    this.isHor = paramBoolean;
    this.unitStr = paramString1;
    this.unit = (paramString1 != null) ? parseUnitString() : paramInt1;
    (new UnitValue[2])[0] = paramUnitValue1;
    (new UnitValue[2])[1] = paramUnitValue2;
    this.subUnits = (paramUnitValue1 != null && paramUnitValue2 != null) ? new UnitValue[2] : null;
    LayoutUtil.putCCString(this, paramString2);
  }
  
  public final int getPixels(float paramFloat, ContainerWrapper paramContainerWrapper, ComponentWrapper paramComponentWrapper) {
    return Math.round(getPixelsExact(paramFloat, paramContainerWrapper, paramComponentWrapper));
  }
  
  public final float getPixelsExact(float paramFloat, ContainerWrapper paramContainerWrapper, ComponentWrapper paramComponentWrapper) {
    if (paramContainerWrapper == null)
      return 1.0F; 
    if (this.oper == 100) {
      float f1;
      Float float_;
      Integer integer1;
      Integer integer2;
      Integer integer3;
      float f2;
      switch (this.unit) {
        case 0:
          return this.value;
        case 1:
        case 2:
          return paramContainerWrapper.getPixelUnitFactor((this.unit == 1)) * this.value;
        case 3:
        case 4:
        case 5:
        case 7:
          f1 = SCALE[this.unit - 3];
          float_ = this.isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
          if (float_ != null)
            f1 *= float_.floatValue(); 
          return (this.isHor ? paramContainerWrapper.getHorizontalScreenDPI() : paramContainerWrapper.getVerticalScreenDPI()) * this.value / f1;
        case 6:
          return this.value * paramFloat * 0.01F;
        case 8:
        case 9:
          return ((this.unit == 8) ? paramContainerWrapper.getScreenWidth() : paramContainerWrapper.getScreenHeight()) * this.value * 0.01F;
        case 12:
          integer1 = LinkHandler.getValue(paramContainerWrapper.getLayout(), "visual", this.isHor ? 0 : 1);
          integer2 = LinkHandler.getValue(paramContainerWrapper.getLayout(), "visual", this.isHor ? 2 : 3);
          return (integer1 == null || integer2 == null) ? 0.0F : (this.value * (Math.max(0, integer2.intValue()) - paramFloat) + integer1.intValue());
        case 13:
          return (paramComponentWrapper == null) ? 0.0F : (this.isHor ? paramComponentWrapper.getMinimumWidth(paramComponentWrapper.getHeight()) : paramComponentWrapper.getMinimumHeight(paramComponentWrapper.getWidth()));
        case 14:
          return (paramComponentWrapper == null) ? 0.0F : (this.isHor ? paramComponentWrapper.getPreferredWidth(paramComponentWrapper.getHeight()) : paramComponentWrapper.getPreferredHeight(paramComponentWrapper.getWidth()));
        case 15:
          return (paramComponentWrapper == null) ? 0.0F : (this.isHor ? paramComponentWrapper.getMaximumWidth(paramComponentWrapper.getHeight()) : paramComponentWrapper.getMaximumHeight(paramComponentWrapper.getWidth()));
        case 16:
          return PlatformDefaults.getMinimumButtonWidth().getPixels(paramFloat, paramContainerWrapper, paramComponentWrapper);
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
          integer3 = LinkHandler.getValue(paramContainerWrapper.getLayout(), getLinkTargetId(), this.unit - ((this.unit >= 24) ? 24 : 18));
          return (integer3 == null) ? 0.0F : ((this.unit == 24) ? (paramContainerWrapper.getScreenLocationX() + integer3.intValue()) : ((this.unit == 25) ? (paramContainerWrapper.getScreenLocationY() + integer3.intValue()) : integer3.intValue()));
        case 26:
          f2 = lookup(paramFloat, paramContainerWrapper, paramComponentWrapper);
          if (f2 != -8.7654312E7F)
            return f2; 
        case 27:
          return PlatformDefaults.getLabelAlignPercentage() * paramFloat;
      } 
      throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
    } 
    if (this.subUnits != null && this.subUnits.length == 2) {
      float f1 = this.subUnits[0].getPixelsExact(paramFloat, paramContainerWrapper, paramComponentWrapper);
      float f2 = this.subUnits[1].getPixelsExact(paramFloat, paramContainerWrapper, paramComponentWrapper);
      switch (this.oper) {
        case 101:
          return f1 + f2;
        case 102:
          return f1 - f2;
        case 103:
          return f1 * f2;
        case 104:
          return f1 / f2;
        case 105:
          return (f1 < f2) ? f1 : f2;
        case 106:
          return (f1 > f2) ? f1 : f2;
        case 107:
          return (f1 + f2) * 0.5F;
      } 
    } 
    throw new IllegalArgumentException("Internal: Unknown Oper: " + this.oper);
  }
  
  private float lookup(float paramFloat, ContainerWrapper paramContainerWrapper, ComponentWrapper paramComponentWrapper) {
    float f = -8.7654312E7F;
    for (int i = CONVERTERS.size() - 1; i >= 0; i--) {
      f = ((UnitConverter)CONVERTERS.get(i)).convertToPixels(this.value, this.unitStr, this.isHor, paramFloat, paramContainerWrapper, paramComponentWrapper);
      if (f != -8.7654312E7F)
        return f; 
    } 
    return PlatformDefaults.convertToPixels(this.value, this.unitStr, this.isHor, paramFloat, paramContainerWrapper, paramComponentWrapper);
  }
  
  private int parseUnitString() {
    int i = this.unitStr.length();
    if (i == 0)
      return this.isHor ? PlatformDefaults.getDefaultHorizontalUnit() : PlatformDefaults.getDefaultVerticalUnit(); 
    Integer integer = UNIT_MAP.get(this.unitStr);
    if (integer != null)
      return integer.intValue(); 
    if (this.unitStr.equals("lp"))
      return this.isHor ? 1 : 2; 
    if (this.unitStr.equals("sp"))
      return this.isHor ? 8 : 9; 
    if (lookup(0.0F, null, null) != -8.7654312E7F)
      return 26; 
    int j = this.unitStr.indexOf('.');
    if (j != -1) {
      this.linkId = this.unitStr.substring(0, j);
      String str = this.unitStr.substring(j + 1);
      if (str.equals("x"))
        return 18; 
      if (str.equals("y"))
        return 19; 
      if (str.equals("w") || str.equals("width"))
        return 20; 
      if (str.equals("h") || str.equals("height"))
        return 21; 
      if (str.equals("x2"))
        return 22; 
      if (str.equals("y2"))
        return 23; 
      if (str.equals("xpos"))
        return 24; 
      if (str.equals("ypos"))
        return 25; 
    } 
    throw new IllegalArgumentException("Unknown keyword: " + this.unitStr);
  }
  
  final boolean isLinked() {
    return (this.linkId != null);
  }
  
  final boolean isLinkedDeep() {
    if (this.subUnits == null)
      return (this.linkId != null); 
    for (UnitValue unitValue : this.subUnits) {
      if (unitValue.isLinkedDeep())
        return true; 
    } 
    return false;
  }
  
  final String getLinkTargetId() {
    return this.linkId;
  }
  
  final UnitValue getSubUnitValue(int paramInt) {
    return this.subUnits[paramInt];
  }
  
  final int getSubUnitCount() {
    return (this.subUnits != null) ? this.subUnits.length : 0;
  }
  
  public final UnitValue[] getSubUnits() {
    return (this.subUnits != null) ? (UnitValue[])this.subUnits.clone() : null;
  }
  
  public final int getUnit() {
    return this.unit;
  }
  
  public final String getUnitString() {
    return this.unitStr;
  }
  
  public final int getOperation() {
    return this.oper;
  }
  
  public final float getValue() {
    return this.value;
  }
  
  public final boolean isHorizontal() {
    return this.isHor;
  }
  
  public final String toString() {
    return getClass().getName() + ". Value=" + this.value + ", unit=" + this.unit + ", unitString: " + this.unitStr + ", oper=" + this.oper + ", isHor: " + this.isHor;
  }
  
  public final String getConstraintString() {
    return LayoutUtil.getCCString(this);
  }
  
  public final int hashCode() {
    return (int)(this.value * 12345.0F) + (this.oper >>> 5) + this.unit >>> 17;
  }
  
  public static synchronized void addGlobalUnitConverter(UnitConverter paramUnitConverter) {
    if (paramUnitConverter == null)
      throw new NullPointerException(); 
    CONVERTERS.add(paramUnitConverter);
  }
  
  public static synchronized boolean removeGlobalUnitConverter(UnitConverter paramUnitConverter) {
    return CONVERTERS.remove(paramUnitConverter);
  }
  
  public static synchronized UnitConverter[] getGlobalUnitConverters() {
    return CONVERTERS.<UnitConverter>toArray(new UnitConverter[CONVERTERS.size()]);
  }
  
  public static int getDefaultUnit() {
    return PlatformDefaults.getDefaultHorizontalUnit();
  }
  
  public static void setDefaultUnit(int paramInt) {
    PlatformDefaults.setDefaultHorizontalUnit(paramInt);
    PlatformDefaults.setDefaultVerticalUnit(paramInt);
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    if (getClass() == UnitValue.class)
      LayoutUtil.writeAsXML(paramObjectOutputStream, this); 
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInputStream));
  }
  
  static {
    if (LayoutUtil.HAS_BEANS)
      LayoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate() {
            protected Expression instantiate(Object param1Object, Encoder param1Encoder) {
              UnitValue unitValue = (UnitValue)param1Object;
              String str = unitValue.getConstraintString();
              if (str == null)
                throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil."); 
              return new Expression(param1Object, ConstraintParser.class, "parseUnitValueOrAlign", new Object[] { unitValue.getConstraintString(), unitValue.isHorizontal() ? Boolean.TRUE : Boolean.FALSE, null });
            }
          }); 
  }
  
  static {
    UNIT_MAP.put("px", Integer.valueOf(0));
    UNIT_MAP.put("lpx", Integer.valueOf(1));
    UNIT_MAP.put("lpy", Integer.valueOf(2));
    UNIT_MAP.put("%", Integer.valueOf(6));
    UNIT_MAP.put("cm", Integer.valueOf(4));
    UNIT_MAP.put("in", Integer.valueOf(5));
    UNIT_MAP.put("spx", Integer.valueOf(8));
    UNIT_MAP.put("spy", Integer.valueOf(9));
    UNIT_MAP.put("al", Integer.valueOf(12));
    UNIT_MAP.put("mm", Integer.valueOf(3));
    UNIT_MAP.put("pt", Integer.valueOf(7));
    UNIT_MAP.put("min", Integer.valueOf(13));
    UNIT_MAP.put("minimum", Integer.valueOf(13));
    UNIT_MAP.put("p", Integer.valueOf(14));
    UNIT_MAP.put("pref", Integer.valueOf(14));
    UNIT_MAP.put("max", Integer.valueOf(15));
    UNIT_MAP.put("maximum", Integer.valueOf(15));
    UNIT_MAP.put("button", Integer.valueOf(16));
    UNIT_MAP.put("label", Integer.valueOf(27));
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\UnitValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */