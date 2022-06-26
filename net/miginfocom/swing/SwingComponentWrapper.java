package net.miginfocom.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;

public class SwingComponentWrapper implements ComponentWrapper {
  private static boolean maxSet = false;
  
  private static boolean vp = true;
  
  private static final Color DB_COMP_OUTLINE = new Color(0, 0, 200);
  
  private final Component c;
  
  private int compType = -1;
  
  private Boolean bl = null;
  
  private boolean prefCalled = false;
  
  private static final IdentityHashMap<FontMetrics, Point2D.Float> FM_MAP = new IdentityHashMap<FontMetrics, Point2D.Float>(4);
  
  private static final Font SUBST_FONT = new Font("sansserif", 0, 11);
  
  private static Method BL_METHOD = null;
  
  private static Method BL_RES_METHOD = null;
  
  private static Method IMS_METHOD = null;
  
  public SwingComponentWrapper(Component paramComponent) {
    this.c = paramComponent;
  }
  
  public final int getBaseline(int paramInt1, int paramInt2) {
    if (BL_METHOD == null)
      return -1; 
    try {
      Object[] arrayOfObject = { Integer.valueOf((paramInt1 < 0) ? this.c.getWidth() : paramInt1), Integer.valueOf((paramInt2 < 0) ? this.c.getHeight() : paramInt2) };
      return ((Integer)BL_METHOD.invoke(this.c, arrayOfObject)).intValue();
    } catch (Exception exception) {
      return -1;
    } 
  }
  
  public final Object getComponent() {
    return this.c;
  }
  
  public final float getPixelUnitFactor(boolean paramBoolean) {
    Font font;
    FontMetrics fontMetrics;
    Point2D.Float float_;
    Float float_1;
    switch (PlatformDefaults.getLogicalPixelBase()) {
      case 100:
        font = this.c.getFont();
        fontMetrics = this.c.getFontMetrics((font != null) ? font : SUBST_FONT);
        float_ = FM_MAP.get(fontMetrics);
        if (float_ == null) {
          Rectangle2D rectangle2D = fontMetrics.getStringBounds("X", this.c.getGraphics());
          float_ = new Point2D.Float((float)rectangle2D.getWidth() / 6.0F, (float)rectangle2D.getHeight() / 13.277344F);
          FM_MAP.put(fontMetrics, float_);
        } 
        return paramBoolean ? float_.x : float_.y;
      case 101:
        float_1 = paramBoolean ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
        return (float_1 != null) ? float_1.floatValue() : ((paramBoolean ? getHorizontalScreenDPI() : getVerticalScreenDPI()) / PlatformDefaults.getDefaultDPI());
    } 
    return 1.0F;
  }
  
  public final int getX() {
    return this.c.getX();
  }
  
  public final int getY() {
    return this.c.getY();
  }
  
  public final int getHeight() {
    return this.c.getHeight();
  }
  
  public final int getWidth() {
    return this.c.getWidth();
  }
  
  public final int getScreenLocationX() {
    Point point = new Point();
    SwingUtilities.convertPointToScreen(point, this.c);
    return point.x;
  }
  
  public final int getScreenLocationY() {
    Point point = new Point();
    SwingUtilities.convertPointToScreen(point, this.c);
    return point.y;
  }
  
  public final int getMinimumHeight(int paramInt) {
    if (!this.prefCalled) {
      this.c.getPreferredSize();
      this.prefCalled = true;
    } 
    return (this.c.getMinimumSize()).height;
  }
  
  public final int getMinimumWidth(int paramInt) {
    if (!this.prefCalled) {
      this.c.getPreferredSize();
      this.prefCalled = true;
    } 
    return (this.c.getMinimumSize()).width;
  }
  
  public final int getPreferredHeight(int paramInt) {
    if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && paramInt != -1)
      this.c.setBounds(this.c.getX(), this.c.getY(), paramInt, 1); 
    return (this.c.getPreferredSize()).height;
  }
  
  public final int getPreferredWidth(int paramInt) {
    if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && paramInt != -1)
      this.c.setBounds(this.c.getX(), this.c.getY(), 1, paramInt); 
    return (this.c.getPreferredSize()).width;
  }
  
  public final int getMaximumHeight(int paramInt) {
    return !isMaxSet(this.c) ? 32767 : (this.c.getMaximumSize()).height;
  }
  
  public final int getMaximumWidth(int paramInt) {
    return !isMaxSet(this.c) ? 32767 : (this.c.getMaximumSize()).width;
  }
  
  private boolean isMaxSet(Component paramComponent) {
    if (IMS_METHOD != null)
      try {
        return ((Boolean)IMS_METHOD.invoke(paramComponent, (Object[])null)).booleanValue();
      } catch (Exception exception) {
        IMS_METHOD = null;
      }  
    return isMaxSizeSetOn1_4();
  }
  
  public final ContainerWrapper getParent() {
    Container container = this.c.getParent();
    return (container != null) ? new SwingContainerWrapper(container) : null;
  }
  
  public final int getHorizontalScreenDPI() {
    return PlatformDefaults.getDefaultDPI();
  }
  
  public final int getVerticalScreenDPI() {
    return PlatformDefaults.getDefaultDPI();
  }
  
  public final int getScreenWidth() {
    try {
      return (this.c.getToolkit().getScreenSize()).width;
    } catch (HeadlessException headlessException) {
      return 1024;
    } 
  }
  
  public final int getScreenHeight() {
    try {
      return (this.c.getToolkit().getScreenSize()).height;
    } catch (HeadlessException headlessException) {
      return 768;
    } 
  }
  
  public final boolean hasBaseline() {
    if (this.bl == null)
      try {
        if (BL_RES_METHOD == null || BL_RES_METHOD.invoke(this.c, new Object[0]).toString().equals("OTHER")) {
          this.bl = Boolean.FALSE;
        } else {
          Dimension dimension = this.c.getMinimumSize();
          this.bl = Boolean.valueOf((getBaseline(dimension.width, dimension.height) > -1));
        } 
      } catch (Throwable throwable) {
        this.bl = Boolean.FALSE;
      }  
    return this.bl.booleanValue();
  }
  
  public final String getLinkId() {
    return this.c.getName();
  }
  
  public final void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.c.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean isVisible() {
    return this.c.isVisible();
  }
  
  public final int[] getVisualPadding() {
    return (vp && this.c instanceof javax.swing.JTabbedPane && UIManager.getLookAndFeel().getClass().getName().endsWith("WindowsLookAndFeel")) ? new int[] { -1, 0, 2, 2 } : null;
  }
  
  public static boolean isMaxSizeSetOn1_4() {
    return maxSet;
  }
  
  public static void setMaxSizeSetOn1_4(boolean paramBoolean) {
    maxSet = paramBoolean;
  }
  
  public static boolean isVisualPaddingEnabled() {
    return vp;
  }
  
  public static void setVisualPaddingEnabled(boolean paramBoolean) {
    vp = paramBoolean;
  }
  
  public final void paintDebugOutline() {
    if (!this.c.isShowing())
      return; 
    Graphics2D graphics2D = (Graphics2D)this.c.getGraphics();
    if (graphics2D == null)
      return; 
    graphics2D.setPaint(DB_COMP_OUTLINE);
    graphics2D.setStroke(new BasicStroke(1.0F, 2, 0, 10.0F, new float[] { 2.0F, 4.0F }, 0.0F));
    graphics2D.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }
  
  public int getComponetType(boolean paramBoolean) {
    if (this.compType == -1)
      this.compType = checkType(paramBoolean); 
    return this.compType;
  }
  
  public int getLayoutHashCode() {
    Dimension dimension = this.c.getMaximumSize();
    int i = dimension.width + (dimension.height << 5);
    dimension = this.c.getPreferredSize();
    i += (dimension.width << 10) + (dimension.height << 15);
    dimension = this.c.getMinimumSize();
    i += (dimension.width << 20) + (dimension.height << 25);
    if (this.c.isVisible())
      i += 1324511; 
    String str = getLinkId();
    if (str != null)
      i += str.hashCode(); 
    return i;
  }
  
  private int checkType(boolean paramBoolean) {
    Component component = this.c;
    if (paramBoolean)
      if (component instanceof JScrollPane) {
        component = ((JScrollPane)component).getViewport().getView();
      } else if (component instanceof ScrollPane) {
        component = ((ScrollPane)component).getComponent(0);
      }  
    return (component instanceof javax.swing.JTextField || component instanceof java.awt.TextField) ? 3 : ((component instanceof javax.swing.JLabel || component instanceof java.awt.Label) ? 2 : ((component instanceof javax.swing.JToggleButton || component instanceof java.awt.Checkbox) ? 16 : ((component instanceof javax.swing.AbstractButton || component instanceof java.awt.Button) ? 5 : ((component instanceof javax.swing.JComboBox || component instanceof java.awt.Choice) ? 2 : ((component instanceof javax.swing.text.JTextComponent || component instanceof java.awt.TextComponent) ? 4 : ((component instanceof javax.swing.JPanel || component instanceof java.awt.Canvas) ? 10 : ((component instanceof javax.swing.JList || component instanceof java.awt.List) ? 6 : ((component instanceof javax.swing.JTable) ? 7 : ((component instanceof javax.swing.JSeparator) ? 18 : ((component instanceof javax.swing.JSpinner) ? 13 : ((component instanceof javax.swing.JProgressBar) ? 14 : ((component instanceof javax.swing.JSlider) ? 12 : ((component instanceof JScrollPane) ? 8 : ((component instanceof javax.swing.JScrollBar || component instanceof java.awt.Scrollbar) ? 17 : ((component instanceof Container) ? 1 : 0)))))))))))))));
  }
  
  public final int hashCode() {
    return getComponent().hashCode();
  }
  
  public final boolean equals(Object paramObject) {
    return !(paramObject instanceof ComponentWrapper) ? false : getComponent().equals(((ComponentWrapper)paramObject).getComponent());
  }
  
  static {
    try {
      IMS_METHOD = Component.class.getDeclaredMethod("isMaximumSizeSet", (Class[])null);
    } catch (Throwable throwable) {}
  }
  
  static {
    try {
      BL_METHOD = Component.class.getDeclaredMethod("getBaseline", new Class[] { int.class, int.class });
      BL_RES_METHOD = Component.class.getDeclaredMethod("getBaselineResizeBehavior", new Class[0]);
    } catch (Throwable throwable) {}
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\swing\SwingComponentWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */