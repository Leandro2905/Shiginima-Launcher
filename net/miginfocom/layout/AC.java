package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;

public final class AC implements Externalizable {
  private final ArrayList<DimConstraint> cList = new ArrayList<DimConstraint>(8);
  
  private transient int curIx = 0;
  
  public AC() {
    this.cList.add(new DimConstraint());
  }
  
  public final DimConstraint[] getConstaints() {
    return this.cList.<DimConstraint>toArray(new DimConstraint[this.cList.size()]);
  }
  
  public final void setConstaints(DimConstraint[] paramArrayOfDimConstraint) {
    if (paramArrayOfDimConstraint == null || paramArrayOfDimConstraint.length < 1)
      paramArrayOfDimConstraint = new DimConstraint[] { new DimConstraint() }; 
    this.cList.clear();
    this.cList.ensureCapacity(paramArrayOfDimConstraint.length);
    for (DimConstraint dimConstraint : paramArrayOfDimConstraint)
      this.cList.add(dimConstraint); 
  }
  
  public int getCount() {
    return this.cList.size();
  }
  
  public final AC count(int paramInt) {
    makeSize(paramInt);
    return this;
  }
  
  public final AC noGrid() {
    return noGrid(new int[] { this.curIx });
  }
  
  public final AC noGrid(int... paramVarArgs) {
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setNoGrid(true);
    } 
    return this;
  }
  
  public final AC index(int paramInt) {
    makeSize(paramInt);
    this.curIx = paramInt;
    return this;
  }
  
  public final AC fill() {
    return fill(new int[] { this.curIx });
  }
  
  public final AC fill(int... paramVarArgs) {
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setFill(true);
    } 
    return this;
  }
  
  public final AC sizeGroup() {
    return sizeGroup("", new int[] { this.curIx });
  }
  
  public final AC sizeGroup(String paramString) {
    return sizeGroup(paramString, new int[] { this.curIx });
  }
  
  public final AC sizeGroup(String paramString, int... paramVarArgs) {
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setSizeGroup(paramString);
    } 
    return this;
  }
  
  public final AC size(String paramString) {
    return size(paramString, new int[] { this.curIx });
  }
  
  public final AC size(String paramString, int... paramVarArgs) {
    BoundSize boundSize = ConstraintParser.parseBoundSize(paramString, false, true);
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setSize(boundSize);
    } 
    return this;
  }
  
  public final AC gap() {
    this.curIx++;
    return this;
  }
  
  public final AC gap(String paramString) {
    return gap(paramString, new int[] { this.curIx++ });
  }
  
  public final AC gap(String paramString, int... paramVarArgs) {
    BoundSize boundSize = (paramString != null) ? ConstraintParser.parseBoundSize(paramString, true, true) : null;
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      if (boundSize != null)
        ((DimConstraint)this.cList.get(j)).setGapAfter(boundSize); 
    } 
    return this;
  }
  
  public final AC align(String paramString) {
    return align(paramString, new int[] { this.curIx });
  }
  
  public final AC align(String paramString, int... paramVarArgs) {
    UnitValue unitValue = ConstraintParser.parseAlignKeywords(paramString, true);
    if (unitValue == null)
      unitValue = ConstraintParser.parseAlignKeywords(paramString, false); 
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setAlign(unitValue);
    } 
    return this;
  }
  
  public final AC growPrio(int paramInt) {
    return growPrio(paramInt, new int[] { this.curIx });
  }
  
  public final AC growPrio(int paramInt, int... paramVarArgs) {
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setGrowPriority(paramInt);
    } 
    return this;
  }
  
  public final AC grow() {
    return grow(1.0F, new int[] { this.curIx });
  }
  
  public final AC grow(float paramFloat) {
    return grow(paramFloat, new int[] { this.curIx });
  }
  
  public final AC grow(float paramFloat, int... paramVarArgs) {
    Float float_ = new Float(paramFloat);
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setGrow(float_);
    } 
    return this;
  }
  
  public final AC shrinkPrio(int paramInt) {
    return shrinkPrio(paramInt, new int[] { this.curIx });
  }
  
  public final AC shrinkPrio(int paramInt, int... paramVarArgs) {
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setShrinkPriority(paramInt);
    } 
    return this;
  }
  
  public final AC shrink() {
    return shrink(100.0F, new int[] { this.curIx });
  }
  
  public final AC shrink(float paramFloat) {
    return shrink(paramFloat, new int[] { this.curIx });
  }
  
  public final AC shrink(float paramFloat, int... paramVarArgs) {
    Float float_ = new Float(paramFloat);
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      int j = paramVarArgs[i];
      makeSize(j);
      ((DimConstraint)this.cList.get(j)).setShrink(float_);
    } 
    return this;
  }
  
  public final AC shrinkWeight(float paramFloat) {
    return shrink(paramFloat);
  }
  
  public final AC shrinkWeight(float paramFloat, int... paramVarArgs) {
    return shrink(paramFloat, paramVarArgs);
  }
  
  private void makeSize(int paramInt) {
    if (this.cList.size() <= paramInt) {
      this.cList.ensureCapacity(paramInt);
      for (int i = this.cList.size(); i <= paramInt; i++)
        this.cList.add(new DimConstraint()); 
    } 
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInput));
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (getClass() == AC.class)
      LayoutUtil.writeAsXML(paramObjectOutput, this); 
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\AC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */