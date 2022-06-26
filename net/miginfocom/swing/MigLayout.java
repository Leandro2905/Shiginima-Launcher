package net.miginfocom.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;

public final class MigLayout implements LayoutManager2, Externalizable {
  private final Map<Component, Object> scrConstrMap = new IdentityHashMap<Component, Object>(8);
  
  private Object layoutConstraints = "";
  
  private Object colConstraints = "";
  
  private Object rowConstraints = "";
  
  private transient ContainerWrapper cacheParentW = null;
  
  private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<ComponentWrapper, CC>(8);
  
  private transient Timer debugTimer = null;
  
  private transient LC lc = null;
  
  private transient AC colSpecs = null;
  
  private transient AC rowSpecs = null;
  
  private transient Grid grid = null;
  
  private transient int lastModCount = PlatformDefaults.getModCount();
  
  private transient int lastHash = -1;
  
  private transient Dimension lastInvalidSize = null;
  
  private transient boolean lastWasInvalid = false;
  
  private transient Dimension lastParentSize = null;
  
  private transient ArrayList<LayoutCallback> callbackList = null;
  
  private transient boolean dirty = true;
  
  private long lastSize = 0L;
  
  public MigLayout() {
    this("", "", "");
  }
  
  public MigLayout(String paramString) {
    this(paramString, "", "");
  }
  
  public MigLayout(String paramString1, String paramString2) {
    this(paramString1, paramString2, "");
  }
  
  public MigLayout(String paramString1, String paramString2, String paramString3) {
    setLayoutConstraints(paramString1);
    setColumnConstraints(paramString2);
    setRowConstraints(paramString3);
  }
  
  public MigLayout(LC paramLC) {
    this(paramLC, (AC)null, (AC)null);
  }
  
  public MigLayout(LC paramLC, AC paramAC) {
    this(paramLC, paramAC, (AC)null);
  }
  
  public MigLayout(LC paramLC, AC paramAC1, AC paramAC2) {
    setLayoutConstraints(paramLC);
    setColumnConstraints(paramAC1);
    setRowConstraints(paramAC2);
  }
  
  public Object getLayoutConstraints() {
    return this.layoutConstraints;
  }
  
  public void setLayoutConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.lc = ConstraintParser.parseLayoutConstraint((String)paramObject);
    } else if (paramObject instanceof LC) {
      this.lc = (LC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.layoutConstraints = paramObject;
    this.dirty = true;
  }
  
  public Object getColumnConstraints() {
    return this.colConstraints;
  }
  
  public void setColumnConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.colSpecs = ConstraintParser.parseColumnConstraints((String)paramObject);
    } else if (paramObject instanceof AC) {
      this.colSpecs = (AC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.colConstraints = paramObject;
    this.dirty = true;
  }
  
  public Object getRowConstraints() {
    return this.rowConstraints;
  }
  
  public void setRowConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.rowSpecs = ConstraintParser.parseRowConstraints((String)paramObject);
    } else if (paramObject instanceof AC) {
      this.rowSpecs = (AC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.rowConstraints = paramObject;
    this.dirty = true;
  }
  
  public Map<Component, Object> getConstraintMap() {
    return new IdentityHashMap<Component, Object>(this.scrConstrMap);
  }
  
  public void setConstraintMap(Map<Component, Object> paramMap) {
    this.scrConstrMap.clear();
    this.ccMap.clear();
    for (Map.Entry<Component, Object> entry : paramMap.entrySet())
      setComponentConstraintsImpl((Component)entry.getKey(), entry.getValue(), true); 
  }
  
  public Object getComponentConstraints(Component paramComponent) {
    synchronized (paramComponent.getParent().getTreeLock()) {
      return this.scrConstrMap.get(paramComponent);
    } 
  }
  
  public void setComponentConstraints(Component paramComponent, Object paramObject) {
    setComponentConstraintsImpl(paramComponent, paramObject, false);
  }
  
  private void setComponentConstraintsImpl(Component paramComponent, Object paramObject, boolean paramBoolean) {
    Container container = paramComponent.getParent();
    synchronized ((container != null) ? container.getTreeLock() : new Object()) {
      if (!paramBoolean && !this.scrConstrMap.containsKey(paramComponent))
        throw new IllegalArgumentException("Component must already be added to parent!"); 
      SwingComponentWrapper swingComponentWrapper = new SwingComponentWrapper(paramComponent);
      if (paramObject == null || paramObject instanceof String) {
        String str = ConstraintParser.prepare((String)paramObject);
        this.scrConstrMap.put(paramComponent, paramObject);
        this.ccMap.put(swingComponentWrapper, ConstraintParser.parseComponentConstraint(str));
      } else if (paramObject instanceof CC) {
        this.scrConstrMap.put(paramComponent, paramObject);
        this.ccMap.put(swingComponentWrapper, (CC)paramObject);
      } else {
        throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + paramObject.getClass().toString());
      } 
      this.dirty = true;
    } 
  }
  
  public boolean isManagingComponent(Component paramComponent) {
    return this.scrConstrMap.containsKey(paramComponent);
  }
  
  public void addLayoutCallback(LayoutCallback paramLayoutCallback) {
    if (paramLayoutCallback == null)
      throw new NullPointerException(); 
    if (this.callbackList == null)
      this.callbackList = new ArrayList<LayoutCallback>(1); 
    this.callbackList.add(paramLayoutCallback);
  }
  
  public void removeLayoutCallback(LayoutCallback paramLayoutCallback) {
    if (this.callbackList != null)
      this.callbackList.remove(paramLayoutCallback); 
  }
  
  private void setDebug(ComponentWrapper paramComponentWrapper, boolean paramBoolean) {
    if (paramBoolean && (this.debugTimer == null || this.debugTimer.getDelay() != getDebugMillis())) {
      if (this.debugTimer != null)
        this.debugTimer.stop(); 
      ContainerWrapper containerWrapper = paramComponentWrapper.getParent();
      final Component parent = (containerWrapper != null) ? (Component)containerWrapper.getComponent() : null;
      this.debugTimer = new Timer(getDebugMillis(), new MyDebugRepaintListener());
      if (component != null)
        SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                Container container = parent.getParent();
                if (container != null)
                  if (container instanceof JComponent) {
                    ((JComponent)container).revalidate();
                  } else {
                    parent.invalidate();
                    container.validate();
                  }  
              }
            }); 
      this.debugTimer.setInitialDelay(100);
      this.debugTimer.start();
    } else if (!paramBoolean && this.debugTimer != null) {
      this.debugTimer.stop();
      this.debugTimer = null;
    } 
  }
  
  private boolean getDebug() {
    return (this.debugTimer != null);
  }
  
  private int getDebugMillis() {
    int i = LayoutUtil.getGlobalDebugMillis();
    return (i > 0) ? i : this.lc.getDebugMillis();
  }
  
  private void checkCache(Container paramContainer) {
    if (paramContainer == null)
      return; 
    if (this.dirty)
      this.grid = null; 
    cleanConstraintMaps(paramContainer);
    int i = PlatformDefaults.getModCount();
    if (this.lastModCount != i) {
      this.grid = null;
      this.lastModCount = i;
    } 
    if (!paramContainer.isValid()) {
      if (!this.lastWasInvalid) {
        this.lastWasInvalid = true;
        int j = 0;
        boolean bool = false;
        for (ComponentWrapper componentWrapper : this.ccMap.keySet()) {
          Object object = componentWrapper.getComponent();
          if (object instanceof javax.swing.JTextArea || object instanceof javax.swing.JEditorPane)
            bool = true; 
          j ^= componentWrapper.getLayoutHashCode();
          j += 285134905;
        } 
        if (bool)
          resetLastInvalidOnParent(paramContainer); 
        if (j != this.lastHash) {
          this.grid = null;
          this.lastHash = j;
        } 
        Dimension dimension = paramContainer.getSize();
        if (this.lastInvalidSize == null || !this.lastInvalidSize.equals(dimension)) {
          if (this.grid != null)
            this.grid.invalidateContainerSize(); 
          this.lastInvalidSize = dimension;
        } 
      } 
    } else {
      this.lastWasInvalid = false;
    } 
    ContainerWrapper containerWrapper = checkParent(paramContainer);
    setDebug((ComponentWrapper)containerWrapper, (getDebugMillis() > 0));
    if (this.grid == null)
      this.grid = new Grid(containerWrapper, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList); 
    this.dirty = false;
  }
  
  private void cleanConstraintMaps(Container paramContainer) {
    HashSet hashSet = new HashSet(Arrays.asList((Object[])paramContainer.getComponents()));
    Iterator<Map.Entry> iterator = this.ccMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Component component = (Component)((ComponentWrapper)((Map.Entry)iterator.next()).getKey()).getComponent();
      if (!hashSet.contains(component)) {
        iterator.remove();
        this.scrConstrMap.remove(component);
      } 
    } 
  }
  
  private void resetLastInvalidOnParent(Container paramContainer) {
    while (paramContainer != null) {
      LayoutManager layoutManager = paramContainer.getLayout();
      if (layoutManager instanceof MigLayout)
        ((MigLayout)layoutManager).lastWasInvalid = false; 
      paramContainer = paramContainer.getParent();
    } 
  }
  
  private ContainerWrapper checkParent(Container paramContainer) {
    if (paramContainer == null)
      return null; 
    if (this.cacheParentW == null || this.cacheParentW.getComponent() != paramContainer)
      this.cacheParentW = new SwingContainerWrapper(paramContainer); 
    return this.cacheParentW;
  }
  
  public void layoutContainer(Container paramContainer) {
    synchronized (paramContainer.getTreeLock()) {
      checkCache(paramContainer);
      Insets insets = paramContainer.getInsets();
      int[] arrayOfInt = { insets.left, insets.top, paramContainer.getWidth() - insets.left - insets.right, paramContainer.getHeight() - insets.top - insets.bottom };
      if (this.grid.layout(arrayOfInt, this.lc.getAlignX(), this.lc.getAlignY(), getDebug(), true)) {
        this.grid = null;
        checkCache(paramContainer);
        this.grid.layout(arrayOfInt, this.lc.getAlignX(), this.lc.getAlignY(), getDebug(), false);
      } 
      long l = this.grid.getHeight()[1] + (this.grid.getWidth()[1] << 32L);
      if (this.lastSize != l) {
        this.lastSize = l;
        final ContainerWrapper containerWrapper = checkParent(paramContainer);
        Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)containerWrapper.getComponent());
        if (window != null)
          if (window.isVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    MigLayout.this.adjustWindowSize(containerWrapper);
                  }
                });
          } else {
            adjustWindowSize(containerWrapper);
          }  
      } 
      this.lastInvalidSize = null;
    } 
  }
  
  private void adjustWindowSize(ContainerWrapper paramContainerWrapper) {
    BoundSize boundSize1 = this.lc.getPackWidth();
    BoundSize boundSize2 = this.lc.getPackHeight();
    if (boundSize1 == null && boundSize2 == null)
      return; 
    Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)paramContainerWrapper.getComponent());
    if (window == null)
      return; 
    Dimension dimension = window.getPreferredSize();
    int i = constrain(checkParent(window), window.getWidth(), dimension.width, boundSize1);
    int j = constrain(checkParent(window), window.getHeight(), dimension.height, boundSize2);
    int k = Math.round(window.getX() - (i - window.getWidth()) * (1.0F - this.lc.getPackWidthAlign()));
    int m = Math.round(window.getY() - (j - window.getHeight()) * (1.0F - this.lc.getPackHeightAlign()));
    window.setBounds(k, m, i, j);
  }
  
  private int constrain(ContainerWrapper paramContainerWrapper, int paramInt1, int paramInt2, BoundSize paramBoundSize) {
    if (paramBoundSize == null)
      return paramInt1; 
    int i = paramInt1;
    UnitValue unitValue = paramBoundSize.getPreferred();
    if (unitValue != null)
      i = unitValue.getPixels(paramInt2, paramContainerWrapper, (ComponentWrapper)paramContainerWrapper); 
    i = paramBoundSize.constrain(i, paramInt2, paramContainerWrapper);
    return paramBoundSize.getGapPush() ? Math.max(paramInt1, i) : i;
  }
  
  public Dimension minimumLayoutSize(Container paramContainer) {
    synchronized (paramContainer.getTreeLock()) {
      return getSizeImpl(paramContainer, 0);
    } 
  }
  
  public Dimension preferredLayoutSize(Container paramContainer) {
    synchronized (paramContainer.getTreeLock()) {
      if (this.lastParentSize == null || !paramContainer.getSize().equals(this.lastParentSize))
        for (ComponentWrapper componentWrapper : this.ccMap.keySet()) {
          Component component = (Component)componentWrapper.getComponent();
          if (component instanceof javax.swing.JTextArea || component instanceof javax.swing.JEditorPane || (component instanceof JComponent && Boolean.TRUE.equals(((JComponent)component).getClientProperty("migLayout.dynamicAspectRatio")))) {
            layoutContainer(paramContainer);
            break;
          } 
        }  
      this.lastParentSize = paramContainer.getSize();
      return getSizeImpl(paramContainer, 1);
    } 
  }
  
  public Dimension maximumLayoutSize(Container paramContainer) {
    return new Dimension(32767, 32767);
  }
  
  private Dimension getSizeImpl(Container paramContainer, int paramInt) {
    checkCache(paramContainer);
    Insets insets = paramContainer.getInsets();
    int i = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getWidth() : null, paramInt) + insets.left + insets.right;
    int j = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getHeight() : null, paramInt) + insets.top + insets.bottom;
    return new Dimension(i, j);
  }
  
  public float getLayoutAlignmentX(Container paramContainer) {
    return (this.lc != null && this.lc.getAlignX() != null) ? this.lc.getAlignX().getPixels(1.0F, checkParent(paramContainer), null) : 0.0F;
  }
  
  public float getLayoutAlignmentY(Container paramContainer) {
    return (this.lc != null && this.lc.getAlignY() != null) ? this.lc.getAlignY().getPixels(1.0F, checkParent(paramContainer), null) : 0.0F;
  }
  
  public void addLayoutComponent(String paramString, Component paramComponent) {
    addLayoutComponent(paramComponent, paramString);
  }
  
  public void addLayoutComponent(Component paramComponent, Object paramObject) {
    synchronized (paramComponent.getParent().getTreeLock()) {
      setComponentConstraintsImpl(paramComponent, paramObject, true);
    } 
  }
  
  public void removeLayoutComponent(Component paramComponent) {
    synchronized (paramComponent.getParent().getTreeLock()) {
      this.scrConstrMap.remove(paramComponent);
      this.ccMap.remove(new SwingComponentWrapper(paramComponent));
    } 
  }
  
  public void invalidateLayout(Container paramContainer) {
    this.dirty = true;
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInput));
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (getClass() == MigLayout.class)
      LayoutUtil.writeAsXML(paramObjectOutput, this); 
  }
  
  private class MyDebugRepaintListener implements ActionListener {
    private MyDebugRepaintListener() {}
    
    public void actionPerformed(ActionEvent param1ActionEvent) {
      if (MigLayout.this.grid != null) {
        Component component = (Component)MigLayout.this.grid.getContainer().getComponent();
        if (component.isShowing()) {
          MigLayout.this.grid.paintDebug();
          return;
        } 
      } 
      MigLayout.this.debugTimer.stop();
      MigLayout.this.debugTimer = null;
    }
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\swing\MigLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */