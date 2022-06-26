package net.miginfocom.layout;

import java.util.HashMap;

public final class PlatformDefaults {
  private static int DEF_H_UNIT = 1;
  
  private static int DEF_V_UNIT = 2;
  
  private static InCellGapProvider GAP_PROVIDER = null;
  
  private static volatile int MOD_COUNT = 0;
  
  private static final UnitValue LPX4 = new UnitValue(4.0F, 1, null);
  
  private static final UnitValue LPX6 = new UnitValue(6.0F, 1, null);
  
  private static final UnitValue LPX7 = new UnitValue(7.0F, 1, null);
  
  private static final UnitValue LPX9 = new UnitValue(9.0F, 1, null);
  
  private static final UnitValue LPX10 = new UnitValue(10.0F, 1, null);
  
  private static final UnitValue LPX11 = new UnitValue(11.0F, 1, null);
  
  private static final UnitValue LPX12 = new UnitValue(12.0F, 1, null);
  
  private static final UnitValue LPX14 = new UnitValue(14.0F, 1, null);
  
  private static final UnitValue LPX16 = new UnitValue(16.0F, 1, null);
  
  private static final UnitValue LPX18 = new UnitValue(18.0F, 1, null);
  
  private static final UnitValue LPX20 = new UnitValue(20.0F, 1, null);
  
  private static final UnitValue LPY4 = new UnitValue(4.0F, 2, null);
  
  private static final UnitValue LPY6 = new UnitValue(6.0F, 2, null);
  
  private static final UnitValue LPY7 = new UnitValue(7.0F, 2, null);
  
  private static final UnitValue LPY9 = new UnitValue(9.0F, 2, null);
  
  private static final UnitValue LPY10 = new UnitValue(10.0F, 2, null);
  
  private static final UnitValue LPY11 = new UnitValue(11.0F, 2, null);
  
  private static final UnitValue LPY12 = new UnitValue(12.0F, 2, null);
  
  private static final UnitValue LPY14 = new UnitValue(14.0F, 2, null);
  
  private static final UnitValue LPY16 = new UnitValue(16.0F, 2, null);
  
  private static final UnitValue LPY18 = new UnitValue(18.0F, 2, null);
  
  private static final UnitValue LPY20 = new UnitValue(20.0F, 2, null);
  
  public static final int WINDOWS_XP = 0;
  
  public static final int MAC_OSX = 1;
  
  public static final int GNOME = 2;
  
  private static int CUR_PLAF = 0;
  
  private static final UnitValue[] PANEL_INS = new UnitValue[4];
  
  private static final UnitValue[] DIALOG_INS = new UnitValue[4];
  
  private static String BUTTON_FORMAT = null;
  
  private static final HashMap<String, UnitValue> HOR_DEFS = new HashMap<String, UnitValue>(32);
  
  private static final HashMap<String, UnitValue> VER_DEFS = new HashMap<String, UnitValue>(32);
  
  private static BoundSize DEF_VGAP = null;
  
  private static BoundSize DEF_HGAP = null;
  
  static BoundSize RELATED_X = null;
  
  static BoundSize RELATED_Y = null;
  
  static BoundSize UNRELATED_X = null;
  
  static BoundSize UNRELATED_Y = null;
  
  private static UnitValue BUTT_WIDTH = null;
  
  private static Float horScale = null;
  
  private static Float verScale = null;
  
  public static final int BASE_FONT_SIZE = 100;
  
  public static final int BASE_SCALE_FACTOR = 101;
  
  public static final int BASE_REAL_PIXEL = 102;
  
  private static int LP_BASE = 101;
  
  private static Integer BASE_DPI_FORCED = null;
  
  private static int BASE_DPI = 96;
  
  private static boolean dra = true;
  
  public static int getCurrentPlatform() {
    String str = System.getProperty("os.name");
    return str.startsWith("Mac OS") ? 1 : (str.startsWith("Linux") ? 2 : 0);
  }
  
  public static void setPlatform(int paramInt) {
    switch (paramInt) {
      case 0:
        setRelatedGap(LPX4, LPY4);
        setUnrelatedGap(LPX7, LPY9);
        setParagraphGap(LPX14, LPY14);
        setIndentGap(LPX9, LPY9);
        setGridCellGap(LPX4, LPY4);
        setMinimumButtonWidth(new UnitValue(75.0F, 1, null));
        setButtonOrder("L_E+U+YNBXOCAH_R");
        setDialogInsets(LPY11, LPX11, LPY11, LPX11);
        setPanelInsets(LPY7, LPX7, LPY7, LPX7);
        break;
      case 1:
        setRelatedGap(LPX4, LPY4);
        setUnrelatedGap(LPX7, LPY9);
        setParagraphGap(LPX14, LPY14);
        setIndentGap(LPX10, LPY10);
        setGridCellGap(LPX4, LPY4);
        setMinimumButtonWidth(new UnitValue(68.0F, 1, null));
        setButtonOrder("L_HE+U+NYBXCOA_R");
        setDialogInsets(LPY14, LPX20, LPY20, LPX20);
        setPanelInsets(LPY16, LPX16, LPY16, LPX16);
        break;
      case 2:
        setRelatedGap(LPX6, LPY6);
        setUnrelatedGap(LPX12, LPY12);
        setParagraphGap(LPX18, LPY18);
        setIndentGap(LPX12, LPY12);
        setGridCellGap(LPX6, LPY6);
        setMinimumButtonWidth(new UnitValue(85.0F, 1, null));
        setButtonOrder("L_HE+UNYACBXIO_R");
        setDialogInsets(LPY12, LPX12, LPY12, LPX12);
        setPanelInsets(LPY6, LPX6, LPY6, LPX6);
        break;
      default:
        throw new IllegalArgumentException("Unknown platform: " + paramInt);
    } 
    CUR_PLAF = paramInt;
    BASE_DPI = (BASE_DPI_FORCED != null) ? BASE_DPI_FORCED.intValue() : getPlatformDPI(paramInt);
  }
  
  private static int getPlatformDPI(int paramInt) {
    switch (paramInt) {
      case 0:
      case 2:
        return 96;
      case 1:
        try {
          return (System.getProperty("java.version").compareTo("1.6") < 0) ? 72 : 96;
        } catch (Throwable throwable) {
          return 72;
        } 
    } 
    throw new IllegalArgumentException("Unknown platform: " + paramInt);
  }
  
  public static int getPlatform() {
    return CUR_PLAF;
  }
  
  public static int getDefaultDPI() {
    return BASE_DPI;
  }
  
  public static void setDefaultDPI(Integer paramInteger) {
    BASE_DPI = (paramInteger != null) ? paramInteger.intValue() : getPlatformDPI(CUR_PLAF);
    BASE_DPI_FORCED = paramInteger;
  }
  
  public static Float getHorizontalScaleFactor() {
    return horScale;
  }
  
  public static void setHorizontalScaleFactor(Float paramFloat) {
    if (!LayoutUtil.equals(horScale, paramFloat)) {
      horScale = paramFloat;
      MOD_COUNT++;
    } 
  }
  
  public static Float getVerticalScaleFactor() {
    return verScale;
  }
  
  public static void setVerticalScaleFactor(Float paramFloat) {
    if (!LayoutUtil.equals(verScale, paramFloat)) {
      verScale = paramFloat;
      MOD_COUNT++;
    } 
  }
  
  public static int getLogicalPixelBase() {
    return LP_BASE;
  }
  
  public static void setLogicalPixelBase(int paramInt) {
    if (LP_BASE != paramInt) {
      if (paramInt < 100 || paramInt > 101)
        throw new IllegalArgumentException("Unrecognized base: " + paramInt); 
      LP_BASE = paramInt;
      MOD_COUNT++;
    } 
  }
  
  public static void setRelatedGap(UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    setUnitValue(new String[] { "r", "rel", "related" }, paramUnitValue1, paramUnitValue2);
    RELATED_X = new BoundSize(paramUnitValue1, paramUnitValue1, null, "rel:rel");
    RELATED_Y = new BoundSize(paramUnitValue2, paramUnitValue2, null, "rel:rel");
  }
  
  public static void setUnrelatedGap(UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    setUnitValue(new String[] { "u", "unrel", "unrelated" }, paramUnitValue1, paramUnitValue2);
    UNRELATED_X = new BoundSize(paramUnitValue1, paramUnitValue1, null, "unrel:unrel");
    UNRELATED_Y = new BoundSize(paramUnitValue2, paramUnitValue2, null, "unrel:unrel");
  }
  
  public static void setParagraphGap(UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    setUnitValue(new String[] { "p", "para", "paragraph" }, paramUnitValue1, paramUnitValue2);
  }
  
  public static void setIndentGap(UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    setUnitValue(new String[] { "i", "ind", "indent" }, paramUnitValue1, paramUnitValue2);
  }
  
  public static void setGridCellGap(UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    if (paramUnitValue1 != null)
      DEF_HGAP = new BoundSize(paramUnitValue1, paramUnitValue1, null, null); 
    if (paramUnitValue2 != null)
      DEF_VGAP = new BoundSize(paramUnitValue2, paramUnitValue2, null, null); 
    MOD_COUNT++;
  }
  
  public static void setMinimumButtonWidth(UnitValue paramUnitValue) {
    BUTT_WIDTH = paramUnitValue;
    MOD_COUNT++;
  }
  
  public static UnitValue getMinimumButtonWidth() {
    return BUTT_WIDTH;
  }
  
  public static UnitValue getUnitValueX(String paramString) {
    return HOR_DEFS.get(paramString);
  }
  
  public static UnitValue getUnitValueY(String paramString) {
    return VER_DEFS.get(paramString);
  }
  
  public static final void setUnitValue(String[] paramArrayOfString, UnitValue paramUnitValue1, UnitValue paramUnitValue2) {
    for (String str1 : paramArrayOfString) {
      String str2 = str1.toLowerCase().trim();
      if (paramUnitValue1 != null)
        HOR_DEFS.put(str2, paramUnitValue1); 
      if (paramUnitValue2 != null)
        VER_DEFS.put(str2, paramUnitValue2); 
    } 
    MOD_COUNT++;
  }
  
  static int convertToPixels(float paramFloat1, String paramString, boolean paramBoolean, float paramFloat2, ContainerWrapper paramContainerWrapper, ComponentWrapper paramComponentWrapper) {
    UnitValue unitValue = (paramBoolean ? HOR_DEFS : VER_DEFS).get(paramString);
    return (unitValue != null) ? Math.round(paramFloat1 * unitValue.getPixels(paramFloat2, paramContainerWrapper, paramComponentWrapper)) : -87654312;
  }
  
  public static String getButtonOrder() {
    return BUTTON_FORMAT;
  }
  
  public static void setButtonOrder(String paramString) {
    BUTTON_FORMAT = paramString;
    MOD_COUNT++;
  }
  
  static String getTagForChar(char paramChar) {
    switch (paramChar) {
      case 'o':
        return "ok";
      case 'c':
        return "cancel";
      case 'h':
        return "help";
      case 'e':
        return "help2";
      case 'y':
        return "yes";
      case 'n':
        return "no";
      case 'a':
        return "apply";
      case 'x':
        return "next";
      case 'b':
        return "back";
      case 'i':
        return "finish";
      case 'l':
        return "left";
      case 'r':
        return "right";
      case 'u':
        return "other";
    } 
    return null;
  }
  
  public static BoundSize getGridGapX() {
    return DEF_HGAP;
  }
  
  public static BoundSize getGridGapY() {
    return DEF_VGAP;
  }
  
  public static UnitValue getDialogInsets(int paramInt) {
    return DIALOG_INS[paramInt];
  }
  
  public static void setDialogInsets(UnitValue paramUnitValue1, UnitValue paramUnitValue2, UnitValue paramUnitValue3, UnitValue paramUnitValue4) {
    if (paramUnitValue1 != null)
      DIALOG_INS[0] = paramUnitValue1; 
    if (paramUnitValue2 != null)
      DIALOG_INS[1] = paramUnitValue2; 
    if (paramUnitValue3 != null)
      DIALOG_INS[2] = paramUnitValue3; 
    if (paramUnitValue4 != null)
      DIALOG_INS[3] = paramUnitValue4; 
    MOD_COUNT++;
  }
  
  public static UnitValue getPanelInsets(int paramInt) {
    return PANEL_INS[paramInt];
  }
  
  public static void setPanelInsets(UnitValue paramUnitValue1, UnitValue paramUnitValue2, UnitValue paramUnitValue3, UnitValue paramUnitValue4) {
    if (paramUnitValue1 != null)
      PANEL_INS[0] = paramUnitValue1; 
    if (paramUnitValue2 != null)
      PANEL_INS[1] = paramUnitValue2; 
    if (paramUnitValue3 != null)
      PANEL_INS[2] = paramUnitValue3; 
    if (paramUnitValue4 != null)
      PANEL_INS[3] = paramUnitValue4; 
    MOD_COUNT++;
  }
  
  public static float getLabelAlignPercentage() {
    return (CUR_PLAF == 1) ? 1.0F : 0.0F;
  }
  
  static BoundSize getDefaultComponentGap(ComponentWrapper paramComponentWrapper1, ComponentWrapper paramComponentWrapper2, int paramInt, String paramString, boolean paramBoolean) {
    return (GAP_PROVIDER != null) ? GAP_PROVIDER.getDefaultGap(paramComponentWrapper1, paramComponentWrapper2, paramInt, paramString, paramBoolean) : ((paramComponentWrapper2 == null) ? null : ((paramInt == 2 || paramInt == 4) ? RELATED_X : RELATED_Y));
  }
  
  public static InCellGapProvider getGapProvider() {
    return GAP_PROVIDER;
  }
  
  public static void setGapProvider(InCellGapProvider paramInCellGapProvider) {
    GAP_PROVIDER = paramInCellGapProvider;
  }
  
  public static int getModCount() {
    return MOD_COUNT;
  }
  
  public void invalidate() {
    MOD_COUNT++;
  }
  
  public static int getDefaultHorizontalUnit() {
    return DEF_H_UNIT;
  }
  
  public static void setDefaultHorizontalUnit(int paramInt) {
    if (paramInt < 0 || paramInt > 27)
      throw new IllegalArgumentException("Illegal Unit: " + paramInt); 
    if (DEF_H_UNIT != paramInt) {
      DEF_H_UNIT = paramInt;
      MOD_COUNT++;
    } 
  }
  
  public static int getDefaultVerticalUnit() {
    return DEF_V_UNIT;
  }
  
  public static void setDefaultVerticalUnit(int paramInt) {
    if (paramInt < 0 || paramInt > 27)
      throw new IllegalArgumentException("Illegal Unit: " + paramInt); 
    if (DEF_V_UNIT != paramInt) {
      DEF_V_UNIT = paramInt;
      MOD_COUNT++;
    } 
  }
  
  public static boolean getDefaultRowAlignmentBaseline() {
    return dra;
  }
  
  public static void setDefaultRowAlignmentBaseline(boolean paramBoolean) {
    dra = paramBoolean;
  }
  
  static {
    setPlatform(getCurrentPlatform());
    MOD_COUNT = 0;
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\PlatformDefaults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */