package net.miginfocom.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;

public final class SwingContainerWrapper extends SwingComponentWrapper implements ContainerWrapper {
  private static final Color DB_CELL_OUTLINE = new Color(255, 0, 0);
  
  public SwingContainerWrapper(Container paramContainer) {
    super(paramContainer);
  }
  
  public ComponentWrapper[] getComponents() {
    Container container = (Container)getComponent();
    ComponentWrapper[] arrayOfComponentWrapper = new ComponentWrapper[container.getComponentCount()];
    for (byte b = 0; b < arrayOfComponentWrapper.length; b++)
      arrayOfComponentWrapper[b] = new SwingComponentWrapper(container.getComponent(b)); 
    return arrayOfComponentWrapper;
  }
  
  public int getComponentCount() {
    return ((Container)getComponent()).getComponentCount();
  }
  
  public Object getLayout() {
    return ((Container)getComponent()).getLayout();
  }
  
  public final boolean isLeftToRight() {
    return ((Container)getComponent()).getComponentOrientation().isLeftToRight();
  }
  
  public final void paintDebugCell(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    Component component = (Component)getComponent();
    if (!component.isShowing())
      return; 
    Graphics2D graphics2D = (Graphics2D)component.getGraphics();
    if (graphics2D == null)
      return; 
    graphics2D.setStroke(new BasicStroke(1.0F, 2, 0, 10.0F, new float[] { 2.0F, 3.0F }, 0.0F));
    graphics2D.setPaint(DB_CELL_OUTLINE);
    graphics2D.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
  }
  
  public int getComponetType(boolean paramBoolean) {
    return 1;
  }
  
  public int getLayoutHashCode() {
    long l = System.nanoTime();
    int i = super.getLayoutHashCode();
    if (isLeftToRight())
      i += 416343; 
    return 0;
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\swing\SwingContainerWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */