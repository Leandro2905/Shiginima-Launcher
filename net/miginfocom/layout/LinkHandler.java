package net.miginfocom.layout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public final class LinkHandler {
  public static final int X = 0;
  
  public static final int Y = 1;
  
  public static final int WIDTH = 2;
  
  public static final int HEIGHT = 3;
  
  public static final int X2 = 4;
  
  public static final int Y2 = 5;
  
  private static final ArrayList<WeakReference<Object>> LAYOUTS = new ArrayList<WeakReference<Object>>(4);
  
  private static final ArrayList<HashMap<String, int[]>> VALUES = new ArrayList<HashMap<String, int[]>>(4);
  
  private static final ArrayList<HashMap<String, int[]>> VALUES_TEMP = new ArrayList<HashMap<String, int[]>>(4);
  
  public static synchronized Integer getValue(Object paramObject, String paramString, int paramInt) {
    Integer integer = null;
    boolean bool = true;
    for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
      Object object = ((WeakReference<Object>)LAYOUTS.get(i)).get();
      if (integer == null && object == paramObject) {
        int[] arrayOfInt = (int[])((HashMap)VALUES_TEMP.get(i)).get(paramString);
        if (bool && arrayOfInt != null && arrayOfInt[paramInt] != -2147471302) {
          integer = Integer.valueOf(arrayOfInt[paramInt]);
        } else {
          arrayOfInt = (int[])((HashMap)VALUES.get(i)).get(paramString);
          integer = (arrayOfInt != null && arrayOfInt[paramInt] != -2147471302) ? Integer.valueOf(arrayOfInt[paramInt]) : null;
        } 
        bool = false;
      } 
      if (object == null) {
        LAYOUTS.remove(i);
        VALUES.remove(i);
        VALUES_TEMP.remove(i);
      } 
    } 
    return integer;
  }
  
  public static synchronized boolean setBounds(Object paramObject, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return setBounds(paramObject, paramString, paramInt1, paramInt2, paramInt3, paramInt4, false, false);
  }
  
  static synchronized boolean setBounds(Object paramObject, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) {
    for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
      Object object = ((WeakReference<Object>)LAYOUTS.get(i)).get();
      if (object == paramObject) {
        HashMap<String, int[]> hashMap1 = (paramBoolean1 ? VALUES_TEMP : VALUES).get(i);
        int[] arrayOfInt1 = (int[])hashMap1.get(paramString);
        if (arrayOfInt1 == null || arrayOfInt1[0] != paramInt1 || arrayOfInt1[1] != paramInt2 || arrayOfInt1[2] != paramInt3 || arrayOfInt1[3] != paramInt4) {
          if (arrayOfInt1 == null || !paramBoolean2) {
            hashMap1.put(paramString, new int[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4 });
            return true;
          } 
          boolean bool = false;
          if (paramInt1 != -2147471302) {
            if (arrayOfInt1[0] == -2147471302 || paramInt1 < arrayOfInt1[0]) {
              arrayOfInt1[0] = paramInt1;
              arrayOfInt1[2] = arrayOfInt1[4] - paramInt1;
              bool = true;
            } 
            if (paramInt3 != -2147471302) {
              int j = paramInt1 + paramInt3;
              if (arrayOfInt1[4] == -2147471302 || j > arrayOfInt1[4]) {
                arrayOfInt1[4] = j;
                arrayOfInt1[2] = j - arrayOfInt1[0];
                bool = true;
              } 
            } 
          } 
          if (paramInt2 != -2147471302) {
            if (arrayOfInt1[1] == -2147471302 || paramInt2 < arrayOfInt1[1]) {
              arrayOfInt1[1] = paramInt2;
              arrayOfInt1[3] = arrayOfInt1[5] - paramInt2;
              bool = true;
            } 
            if (paramInt4 != -2147471302) {
              int j = paramInt2 + paramInt4;
              if (arrayOfInt1[5] == -2147471302 || j > arrayOfInt1[5]) {
                arrayOfInt1[5] = j;
                arrayOfInt1[3] = j - arrayOfInt1[1];
                bool = true;
              } 
            } 
          } 
          return bool;
        } 
        return false;
      } 
    } 
    LAYOUTS.add(new WeakReference(paramObject));
    int[] arrayOfInt = { paramInt1, paramInt2, paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4 };
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(4);
    if (paramBoolean1)
      hashMap.put(paramString, arrayOfInt); 
    VALUES_TEMP.add(hashMap);
    hashMap = new HashMap<Object, Object>(4);
    if (!paramBoolean1)
      hashMap.put(paramString, arrayOfInt); 
    VALUES.add(hashMap);
    return true;
  }
  
  public static synchronized void clearWeakReferencesNow() {
    LAYOUTS.clear();
  }
  
  public static synchronized boolean clearBounds(Object paramObject, String paramString) {
    for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
      Object object = ((WeakReference<Object>)LAYOUTS.get(i)).get();
      if (object == paramObject)
        return (((HashMap)VALUES.get(i)).remove(paramString) != null); 
    } 
    return false;
  }
  
  static synchronized void clearTemporaryBounds(Object paramObject) {
    for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
      Object object = ((WeakReference<Object>)LAYOUTS.get(i)).get();
      if (object == paramObject) {
        ((HashMap)VALUES_TEMP.get(i)).clear();
        return;
      } 
    } 
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\LinkHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */