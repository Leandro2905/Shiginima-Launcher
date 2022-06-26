package net.miginfocom.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ConstraintParser {
  public static LC parseLayoutConstraint(String paramString) {
    LC lC = new LC();
    if (paramString.length() == 0)
      return lC; 
    String[] arrayOfString = toTrimmedTokens(paramString, ',');
    for (byte b = 0; b < arrayOfString.length; b++) {
      String str = arrayOfString[b];
      if (str != null) {
        int i = str.length();
        if (i == 3 || i == 11) {
          if (str.equals("ltr") || str.equals("rtl") || str.equals("lefttoright") || str.equals("righttoleft")) {
            lC.setLeftToRight((str.charAt(0) == 'l') ? Boolean.TRUE : Boolean.FALSE);
            arrayOfString[b] = null;
          } 
          if (str.equals("ttb") || str.equals("btt") || str.equals("toptobottom") || str.equals("bottomtotop")) {
            lC.setTopToBottom((str.charAt(0) == 't'));
            arrayOfString[b] = null;
          } 
        } 
      } 
    } 
    for (String str : arrayOfString) {
      if (str == null || str.length() == 0)
        continue; 
      try {
        int i = -1;
        char c = str.charAt(0);
        if (c == 'w' || c == 'h') {
          i = startsWithLenient(str, "wrap", -1, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            lC.setWrapAfter((str1.length() != 0) ? Integer.parseInt(str1) : 0);
            continue;
          } 
          boolean bool = (c == 'w') ? true : false;
          if (bool && (str.startsWith("w ") || str.startsWith("width "))) {
            String str1 = str.substring((str.charAt(1) == ' ') ? 2 : 6).trim();
            lC.setWidth(parseBoundSize(str1, false, true));
            continue;
          } 
          if (!bool && (str.startsWith("h ") || str.startsWith("height "))) {
            String str1 = str.substring((str.charAt(1) == ' ') ? 2 : 7).trim();
            lC.setHeight(parseBoundSize(str1, false, false));
            continue;
          } 
          if (str.length() > 5) {
            String str1 = str.substring(5).trim();
            if (str.startsWith("wmin ")) {
              lC.minWidth(str1);
              continue;
            } 
            if (str.startsWith("wmax ")) {
              lC.maxWidth(str1);
              continue;
            } 
            if (str.startsWith("hmin ")) {
              lC.minHeight(str1);
              continue;
            } 
            if (str.startsWith("hmax ")) {
              lC.maxHeight(str1);
              continue;
            } 
          } 
          if (str.startsWith("hidemode ")) {
            lC.setHideMode(Integer.parseInt(str.substring(9)));
            continue;
          } 
        } 
        if (c == 'g') {
          if (str.startsWith("gapx ")) {
            lC.setGridGapX(parseBoundSize(str.substring(5).trim(), true, true));
            continue;
          } 
          if (str.startsWith("gapy ")) {
            lC.setGridGapY(parseBoundSize(str.substring(5).trim(), true, false));
            continue;
          } 
          if (str.startsWith("gap ")) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(4).trim(), ' ');
            lC.setGridGapX(parseBoundSize(arrayOfString1[0], true, true));
            lC.setGridGapY((arrayOfString1.length > 1) ? parseBoundSize(arrayOfString1[1], true, false) : lC.getGridGapX());
            continue;
          } 
        } 
        if (c == 'd') {
          i = startsWithLenient(str, "debug", 5, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            lC.setDebugMillis((str1.length() > 0) ? Integer.parseInt(str1) : 1000);
            continue;
          } 
        } 
        if (c == 'n') {
          if (str.equals("nogrid")) {
            lC.setNoGrid(true);
            continue;
          } 
          if (str.equals("nocache")) {
            lC.setNoCache(true);
            continue;
          } 
          if (str.equals("novisualpadding")) {
            lC.setVisualPadding(false);
            continue;
          } 
        } 
        if (c == 'f') {
          if (str.equals("fill") || str.equals("fillx") || str.equals("filly")) {
            lC.setFillX((str.length() == 4 || str.charAt(4) == 'x'));
            lC.setFillY((str.length() == 4 || str.charAt(4) == 'y'));
            continue;
          } 
          if (str.equals("flowy")) {
            lC.setFlowX(false);
            continue;
          } 
          if (str.equals("flowx")) {
            lC.setFlowX(true);
            continue;
          } 
        } 
        if (c == 'i') {
          i = startsWithLenient(str, "insets", 3, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            UnitValue[] arrayOfUnitValue = parseInsets(str1, true);
            LayoutUtil.putCCString(arrayOfUnitValue, str1);
            lC.setInsets(arrayOfUnitValue);
            continue;
          } 
        } 
        if (c == 'a') {
          i = startsWithLenient(str, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
          if (i > -1) {
            UnitValue unitValue1 = parseUnitValueOrAlign(str.substring(i).trim(), false, null);
            if (unitValue1 == UnitValue.BASELINE_IDENTITY)
              throw new IllegalArgumentException("'baseline' can not be used to align the whole component group."); 
            lC.setAlignY(unitValue1);
            continue;
          } 
          i = startsWithLenient(str, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
          if (i > -1) {
            lC.setAlignX(parseUnitValueOrAlign(str.substring(i).trim(), true, null));
            continue;
          } 
          i = startsWithLenient(str, "align", 2, true);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            lC.setAlignX(parseUnitValueOrAlign(arrayOfString1[0], true, null));
            if (arrayOfString1.length > 1)
              lC.setAlignY(parseUnitValueOrAlign(arrayOfString1[1], false, null)); 
            continue;
          } 
        } 
        if (c == 'p') {
          if (str.startsWith("packalign ")) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(10).trim(), ' ');
            lC.setPackWidthAlign((arrayOfString1[0].length() > 0) ? Float.parseFloat(arrayOfString1[0]) : 0.5F);
            if (arrayOfString1.length > 1)
              lC.setPackHeightAlign(Float.parseFloat(arrayOfString1[1])); 
            continue;
          } 
          if (str.startsWith("pack ") || str.equals("pack")) {
            String str1 = str.substring(4).trim();
            String[] arrayOfString1 = toTrimmedTokens((str1.length() > 0) ? str1 : "pref pref", ' ');
            lC.setPackWidth(parseBoundSize(arrayOfString1[0], false, true));
            if (arrayOfString1.length > 1)
              lC.setPackHeight(parseBoundSize(arrayOfString1[1], false, false)); 
            continue;
          } 
        } 
        if (lC.getAlignX() == null) {
          UnitValue unitValue1 = parseAlignKeywords(str, true);
          if (unitValue1 != null) {
            lC.setAlignX(unitValue1);
            continue;
          } 
        } 
        UnitValue unitValue = parseAlignKeywords(str, false);
        if (unitValue != null) {
          lC.setAlignY(unitValue);
        } else {
          throw new IllegalArgumentException("Unknown Constraint: '" + str + "'\n");
        } 
      } catch (Exception exception) {
        throw new IllegalArgumentException("Illegal Constraint: '" + str + "'\n" + exception.getMessage());
      } 
      continue;
    } 
    return lC;
  }
  
  public static AC parseRowConstraints(String paramString) {
    return parseAxisConstraint(paramString, false);
  }
  
  public static AC parseColumnConstraints(String paramString) {
    return parseAxisConstraint(paramString, true);
  }
  
  private static AC parseAxisConstraint(String paramString, boolean paramBoolean) {
    paramString = paramString.trim();
    if (paramString.length() == 0)
      return new AC(); 
    paramString = paramString.toLowerCase();
    ArrayList<String> arrayList = getRowColAndGapsTrimmed(paramString);
    BoundSize[] arrayOfBoundSize = new BoundSize[(arrayList.size() >> 1) + 1];
    byte b = 0;
    int i = arrayList.size();
    int j;
    for (j = 0; b < i; j++) {
      arrayOfBoundSize[j] = parseBoundSize(arrayList.get(b), true, paramBoolean);
      b += 2;
    } 
    DimConstraint[] arrayOfDimConstraint = new DimConstraint[arrayList.size() >> 1];
    i = 0;
    for (j = 0; i < arrayOfDimConstraint.length; j++) {
      if (j >= arrayOfBoundSize.length - 1)
        j = arrayOfBoundSize.length - 2; 
      arrayOfDimConstraint[i] = parseDimConstraint(arrayList.get((i << 1) + 1), arrayOfBoundSize[j], arrayOfBoundSize[j + 1], paramBoolean);
      i++;
    } 
    AC aC = new AC();
    aC.setConstaints(arrayOfDimConstraint);
    return aC;
  }
  
  private static DimConstraint parseDimConstraint(String paramString, BoundSize paramBoundSize1, BoundSize paramBoundSize2, boolean paramBoolean) {
    DimConstraint dimConstraint = new DimConstraint();
    dimConstraint.setGapBefore(paramBoundSize1);
    dimConstraint.setGapAfter(paramBoundSize2);
    String[] arrayOfString = toTrimmedTokens(paramString, ',');
    for (byte b = 0; b < arrayOfString.length; b++) {
      String str = arrayOfString[b];
      try {
        if (str.length() == 0)
          continue; 
        if (str.equals("fill")) {
          dimConstraint.setFill(true);
          continue;
        } 
        if (str.equals("nogrid")) {
          dimConstraint.setNoGrid(true);
          continue;
        } 
        int i = -1;
        char c = str.charAt(0);
        if (c == 's') {
          i = startsWithLenient(str, new String[] { "sizegroup", "sg" }, new int[] { 5, 2 }, true);
          if (i > -1) {
            dimConstraint.setSizeGroup(str.substring(i).trim());
            continue;
          } 
          i = startsWithLenient(str, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
          if (i > -1) {
            dimConstraint.setShrinkPriority(Integer.parseInt(str.substring(i).trim()));
            continue;
          } 
          i = startsWithLenient(str, "shrink", 6, true);
          if (i > -1) {
            dimConstraint.setShrink(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
        } 
        if (c == 'g') {
          i = startsWithLenient(str, new String[] { "growpriority", "gp" }, new int[] { 5, 2 }, true);
          if (i > -1) {
            dimConstraint.setGrowPriority(Integer.parseInt(str.substring(i).trim()));
            continue;
          } 
          i = startsWithLenient(str, "grow", 4, true);
          if (i > -1) {
            dimConstraint.setGrow(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
        } 
        if (c == 'a') {
          i = startsWithLenient(str, "align", 2, true);
          if (i > -1) {
            dimConstraint.setAlign(parseUnitValueOrAlign(str.substring(i).trim(), paramBoolean, null));
            continue;
          } 
        } 
        UnitValue unitValue = parseAlignKeywords(str, paramBoolean);
        if (unitValue != null) {
          dimConstraint.setAlign(unitValue);
        } else {
          dimConstraint.setSize(parseBoundSize(str, false, paramBoolean));
        } 
      } catch (Exception exception) {
        throw new IllegalArgumentException("Illegal contraint: '" + str + "'\n" + exception.getMessage());
      } 
      continue;
    } 
    return dimConstraint;
  }
  
  public static Map<ComponentWrapper, CC> parseComponentConstraints(Map<ComponentWrapper, String> paramMap) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    for (Map.Entry<ComponentWrapper, String> entry : paramMap.entrySet())
      hashMap.put(entry.getKey(), parseComponentConstraint((String)entry.getValue())); 
    return (Map)hashMap;
  }
  
  public static CC parseComponentConstraint(String paramString) {
    CC cC = new CC();
    if (paramString.length() == 0)
      return cC; 
    String[] arrayOfString = toTrimmedTokens(paramString, ',');
    for (String str : arrayOfString) {
      try {
        if (str.length() == 0)
          continue; 
        int i = -1;
        char c = str.charAt(0);
        if (c == 'n') {
          if (str.equals("north")) {
            cC.setDockSide(0);
            continue;
          } 
          if (str.equals("newline")) {
            cC.setNewline(true);
            continue;
          } 
          if (str.startsWith("newline ")) {
            String str1 = str.substring(7).trim();
            cC.setNewlineGapSize(parseBoundSize(str1, true, true));
            continue;
          } 
        } 
        if (c == 'f' && (str.equals("flowy") || str.equals("flowx"))) {
          cC.setFlowX((str.charAt(4) == 'x') ? Boolean.TRUE : Boolean.FALSE);
          continue;
        } 
        if (c == 's') {
          i = startsWithLenient(str, "skip", 4, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            cC.setSkip((str1.length() != 0) ? Integer.parseInt(str1) : 1);
            continue;
          } 
          i = startsWithLenient(str, "split", 5, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            cC.setSplit((str1.length() > 0) ? Integer.parseInt(str1) : 2097051);
            continue;
          } 
          if (str.equals("south")) {
            cC.setDockSide(2);
            continue;
          } 
          i = startsWithLenient(str, new String[] { "spany", "sy" }, new int[] { 5, 2 }, true);
          if (i > -1) {
            cC.setSpanY(parseSpan(str.substring(i).trim()));
            continue;
          } 
          i = startsWithLenient(str, new String[] { "spanx", "sx" }, new int[] { 5, 2 }, true);
          if (i > -1) {
            cC.setSpanX(parseSpan(str.substring(i).trim()));
            continue;
          } 
          i = startsWithLenient(str, "span", 4, true);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            cC.setSpanX((arrayOfString1[0].length() > 0) ? Integer.parseInt(arrayOfString1[0]) : 2097051);
            cC.setSpanY((arrayOfString1.length > 1) ? Integer.parseInt(arrayOfString1[1]) : 1);
            continue;
          } 
          i = startsWithLenient(str, "shrinkx", 7, true);
          if (i > -1) {
            cC.getHorizontal().setShrink(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "shrinky", 7, true);
          if (i > -1) {
            cC.getVertical().setShrink(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "shrink", 6, false);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            cC.getHorizontal().setShrink(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            if (arrayOfString1.length > 1)
              cC.getVertical().setShrink(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100)); 
            continue;
          } 
          i = startsWithLenient(str, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            if (str1.startsWith("x") || str1.startsWith("y")) {
              (str1.startsWith("x") ? cC.getHorizontal() : cC.getVertical()).setShrinkPriority(Integer.parseInt(str1.substring(2)));
            } else {
              String[] arrayOfString1 = toTrimmedTokens(str1, ' ');
              cC.getHorizontal().setShrinkPriority(Integer.parseInt(arrayOfString1[0]));
              if (arrayOfString1.length > 1)
                cC.getVertical().setShrinkPriority(Integer.parseInt(arrayOfString1[1])); 
            } 
            continue;
          } 
          i = startsWithLenient(str, new String[] { "sizegroupx", "sizegroupy", "sgx", "sgy" }, new int[] { 9, 9, 2, 2 }, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            char c1 = str.charAt(i - 1);
            if (c1 != 'y')
              cC.getHorizontal().setSizeGroup(str1); 
            if (c1 != 'x')
              cC.getVertical().setSizeGroup(str1); 
            continue;
          } 
        } 
        if (c == 'g') {
          i = startsWithLenient(str, "growx", 5, true);
          if (i > -1) {
            cC.getHorizontal().setGrow(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "growy", 5, true);
          if (i > -1) {
            cC.getVertical().setGrow(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "grow", 4, false);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            cC.getHorizontal().setGrow(parseFloat(arrayOfString1[0], ResizeConstraint.WEIGHT_100));
            cC.getVertical().setGrow(parseFloat((arrayOfString1.length > 1) ? arrayOfString1[1] : "", ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, new String[] { "growprio", "gp" }, new int[] { 8, 2 }, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            boolean bool = (str1.length() > 0) ? str1.charAt(0) : true;
            if (bool == 120 || bool == 121) {
              ((bool == 120) ? cC.getHorizontal() : cC.getVertical()).setGrowPriority(Integer.parseInt(str1.substring(2)));
            } else {
              String[] arrayOfString1 = toTrimmedTokens(str1, ' ');
              cC.getHorizontal().setGrowPriority(Integer.parseInt(arrayOfString1[0]));
              if (arrayOfString1.length > 1)
                cC.getVertical().setGrowPriority(Integer.parseInt(arrayOfString1[1])); 
            } 
            continue;
          } 
          if (str.startsWith("gap")) {
            BoundSize[] arrayOfBoundSize = parseGaps(str);
            if (arrayOfBoundSize[0] != null)
              cC.getVertical().setGapBefore(arrayOfBoundSize[0]); 
            if (arrayOfBoundSize[1] != null)
              cC.getHorizontal().setGapBefore(arrayOfBoundSize[1]); 
            if (arrayOfBoundSize[2] != null)
              cC.getVertical().setGapAfter(arrayOfBoundSize[2]); 
            if (arrayOfBoundSize[3] != null)
              cC.getHorizontal().setGapAfter(arrayOfBoundSize[3]); 
            continue;
          } 
        } 
        if (c == 'a') {
          i = startsWithLenient(str, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
          if (i > -1) {
            cC.getVertical().setAlign(parseUnitValueOrAlign(str.substring(i).trim(), false, null));
            continue;
          } 
          i = startsWithLenient(str, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
          if (i > -1) {
            cC.getHorizontal().setAlign(parseUnitValueOrAlign(str.substring(i).trim(), true, null));
            continue;
          } 
          i = startsWithLenient(str, "align", 2, true);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            cC.getHorizontal().setAlign(parseUnitValueOrAlign(arrayOfString1[0], true, null));
            if (arrayOfString1.length > 1)
              cC.getVertical().setAlign(parseUnitValueOrAlign(arrayOfString1[1], false, null)); 
            continue;
          } 
        } 
        if ((c == 'x' || c == 'y') && str.length() > 2) {
          char c1 = str.charAt(1);
          if (c1 == ' ' || (c1 == '2' && str.charAt(2) == ' ')) {
            if (cC.getPos() == null) {
              cC.setPos(new UnitValue[4]);
            } else if (!cC.isBoundsInGrid()) {
              throw new IllegalArgumentException("Cannot combine 'position' with 'x/y/x2/y2' keywords.");
            } 
            int j = ((c == 'x') ? 0 : 1) + ((c1 == '2') ? 2 : 0);
            UnitValue[] arrayOfUnitValue = cC.getPos();
            arrayOfUnitValue[j] = parseUnitValue(str.substring(2).trim(), null, (c == 'x'));
            cC.setPos(arrayOfUnitValue);
            cC.setBoundsInGrid(true);
            continue;
          } 
        } 
        if (c == 'c') {
          i = startsWithLenient(str, "cell", 4, true);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            if (arrayOfString1.length < 2)
              throw new IllegalArgumentException("At least two integers must follow " + str); 
            cC.setCellX(Integer.parseInt(arrayOfString1[0]));
            cC.setCellY(Integer.parseInt(arrayOfString1[1]));
            if (arrayOfString1.length > 2)
              cC.setSpanX(Integer.parseInt(arrayOfString1[2])); 
            if (arrayOfString1.length > 3)
              cC.setSpanY(Integer.parseInt(arrayOfString1[3])); 
            continue;
          } 
        } 
        if (c == 'p') {
          i = startsWithLenient(str, "pos", 3, true);
          if (i > -1) {
            if (cC.getPos() != null && cC.isBoundsInGrid())
              throw new IllegalArgumentException("Can not combine 'pos' with 'x/y/x2/y2' keywords."); 
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            UnitValue[] arrayOfUnitValue = new UnitValue[4];
            for (byte b = 0; b < arrayOfString1.length; b++)
              arrayOfUnitValue[b] = parseUnitValue(arrayOfString1[b], null, (b % 2 == 0)); 
            if ((arrayOfUnitValue[0] == null && arrayOfUnitValue[2] == null) || (arrayOfUnitValue[1] == null && arrayOfUnitValue[3] == null))
              throw new IllegalArgumentException("Both x and x2 or y and y2 can not be null!"); 
            cC.setPos(arrayOfUnitValue);
            cC.setBoundsInGrid(false);
            continue;
          } 
          i = startsWithLenient(str, "pad", 3, true);
          if (i > -1) {
            UnitValue[] arrayOfUnitValue = parseInsets(str.substring(i).trim(), false);
            cC.setPadding(new UnitValue[] { arrayOfUnitValue[0], (arrayOfUnitValue.length > 1) ? arrayOfUnitValue[1] : null, (arrayOfUnitValue.length > 2) ? arrayOfUnitValue[2] : null, (arrayOfUnitValue.length > 3) ? arrayOfUnitValue[3] : null });
            continue;
          } 
          i = startsWithLenient(str, "pushx", 5, true);
          if (i > -1) {
            cC.setPushX(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "pushy", 5, true);
          if (i > -1) {
            cC.setPushY(parseFloat(str.substring(i).trim(), ResizeConstraint.WEIGHT_100));
            continue;
          } 
          i = startsWithLenient(str, "push", 4, false);
          if (i > -1) {
            String[] arrayOfString1 = toTrimmedTokens(str.substring(i).trim(), ' ');
            cC.setPushX(parseFloat(arrayOfString1[0], ResizeConstraint.WEIGHT_100));
            cC.setPushY(parseFloat((arrayOfString1.length > 1) ? arrayOfString1[1] : "", ResizeConstraint.WEIGHT_100));
            continue;
          } 
        } 
        if (c == 't') {
          i = startsWithLenient(str, "tag", 3, true);
          if (i > -1) {
            cC.setTag(str.substring(i).trim());
            continue;
          } 
        } 
        if (c == 'w' || c == 'h') {
          if (str.equals("wrap")) {
            cC.setWrap(true);
            continue;
          } 
          if (str.startsWith("wrap ")) {
            String str1 = str.substring(5).trim();
            cC.setWrapGapSize(parseBoundSize(str1, true, true));
            continue;
          } 
          boolean bool = (c == 'w') ? true : false;
          if (bool && (str.startsWith("w ") || str.startsWith("width "))) {
            String str1 = str.substring((str.charAt(1) == ' ') ? 2 : 6).trim();
            cC.getHorizontal().setSize(parseBoundSize(str1, false, true));
            continue;
          } 
          if (!bool && (str.startsWith("h ") || str.startsWith("height "))) {
            String str1 = str.substring((str.charAt(1) == ' ') ? 2 : 7).trim();
            cC.getVertical().setSize(parseBoundSize(str1, false, false));
            continue;
          } 
          if (str.startsWith("wmin ") || str.startsWith("wmax ") || str.startsWith("hmin ") || str.startsWith("hmax ")) {
            String str1 = str.substring(5).trim();
            if (str1.length() > 0) {
              UnitValue unitValue1 = parseUnitValue(str1, null, bool);
              boolean bool1 = (str.charAt(3) == 'n') ? true : false;
              DimConstraint dimConstraint = bool ? cC.getHorizontal() : cC.getVertical();
              dimConstraint.setSize(new BoundSize(bool1 ? unitValue1 : dimConstraint.getSize().getMin(), dimConstraint.getSize().getPreferred(), bool1 ? dimConstraint.getSize().getMax() : unitValue1, str1));
              continue;
            } 
          } 
          if (str.equals("west")) {
            cC.setDockSide(1);
            continue;
          } 
          if (str.startsWith("hidemode ")) {
            cC.setHideMode(Integer.parseInt(str.substring(9)));
            continue;
          } 
        } 
        if (c == 'i' && str.startsWith("id ")) {
          cC.setId(str.substring(3).trim());
          int j = cC.getId().indexOf('.');
          if (j == 0 || j == cC.getId().length() - 1)
            throw new IllegalArgumentException("Dot must not be first or last!"); 
          continue;
        } 
        if (c == 'e') {
          if (str.equals("east")) {
            cC.setDockSide(3);
            continue;
          } 
          if (str.equals("external")) {
            cC.setExternal(true);
            continue;
          } 
          i = startsWithLenient(str, new String[] { "endgroupx", "endgroupy", "egx", "egy" }, new int[] { -1, -1, -1, -1 }, true);
          if (i > -1) {
            String str1 = str.substring(i).trim();
            char c1 = str.charAt(i - 1);
            DimConstraint dimConstraint = (c1 == 'x') ? cC.getHorizontal() : cC.getVertical();
            dimConstraint.setEndGroup(str1);
            continue;
          } 
        } 
        if (c == 'd') {
          if (str.equals("dock north")) {
            cC.setDockSide(0);
            continue;
          } 
          if (str.equals("dock west")) {
            cC.setDockSide(1);
            continue;
          } 
          if (str.equals("dock south")) {
            cC.setDockSide(2);
            continue;
          } 
          if (str.equals("dock east")) {
            cC.setDockSide(3);
            continue;
          } 
          if (str.equals("dock center")) {
            cC.getHorizontal().setGrow(Float.valueOf(100.0F));
            cC.getVertical().setGrow(Float.valueOf(100.0F));
            cC.setPushX(Float.valueOf(100.0F));
            cC.setPushY(Float.valueOf(100.0F));
            continue;
          } 
        } 
        UnitValue unitValue = parseAlignKeywords(str, true);
        if (unitValue != null) {
          cC.getHorizontal().setAlign(unitValue);
        } else {
          UnitValue unitValue1 = parseAlignKeywords(str, false);
          if (unitValue1 != null) {
            cC.getVertical().setAlign(unitValue1);
          } else {
            throw new IllegalArgumentException("Unknown keyword.");
          } 
        } 
      } catch (Exception exception) {
        throw new IllegalArgumentException("Illegal Constraint: '" + str + "'\n" + exception.getMessage());
      } 
      continue;
    } 
    return cC;
  }
  
  public static UnitValue[] parseInsets(String paramString, boolean paramBoolean) {
    if (paramString.length() == 0 || paramString.equals("dialog") || paramString.equals("panel")) {
      if (!paramBoolean)
        throw new IllegalAccessError("Insets now allowed: " + paramString + "\n"); 
      boolean bool = paramString.startsWith("p");
      UnitValue[] arrayOfUnitValue1 = new UnitValue[4];
      for (byte b1 = 0; b1 < 4; b1++)
        arrayOfUnitValue1[b1] = bool ? PlatformDefaults.getPanelInsets(b1) : PlatformDefaults.getDialogInsets(b1); 
      return arrayOfUnitValue1;
    } 
    String[] arrayOfString = toTrimmedTokens(paramString, ' ');
    UnitValue[] arrayOfUnitValue = new UnitValue[4];
    for (byte b = 0; b < 4; b++) {
      UnitValue unitValue = parseUnitValue(arrayOfString[(b < arrayOfString.length) ? b : (arrayOfString.length - 1)], UnitValue.ZERO, (b % 2 == 1));
      arrayOfUnitValue[b] = (unitValue != null) ? unitValue : PlatformDefaults.getPanelInsets(b);
    } 
    return arrayOfUnitValue;
  }
  
  private static BoundSize[] parseGaps(String paramString) {
    BoundSize[] arrayOfBoundSize = new BoundSize[4];
    int i = startsWithLenient(paramString, "gaptop", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[0] = parseBoundSize(paramString, true, false);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gapleft", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[1] = parseBoundSize(paramString, true, true);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gapbottom", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[2] = parseBoundSize(paramString, true, false);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gapright", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[3] = parseBoundSize(paramString, true, true);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gapbefore", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[1] = parseBoundSize(paramString, true, true);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gapafter", -1, true);
    if (i > -1) {
      paramString = paramString.substring(i).trim();
      arrayOfBoundSize[3] = parseBoundSize(paramString, true, true);
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, new String[] { "gapx", "gapy" }, (int[])null, true);
    if (i > -1) {
      boolean bool = (paramString.charAt(3) == 'x') ? true : false;
      String[] arrayOfString = toTrimmedTokens(paramString.substring(i).trim(), ' ');
      arrayOfBoundSize[bool ? 1 : 0] = parseBoundSize(arrayOfString[0], true, bool);
      if (arrayOfString.length > 1)
        arrayOfBoundSize[bool ? 3 : 2] = parseBoundSize(arrayOfString[1], true, !bool); 
      return arrayOfBoundSize;
    } 
    i = startsWithLenient(paramString, "gap ", 1, true);
    if (i > -1) {
      String[] arrayOfString = toTrimmedTokens(paramString.substring(i).trim(), ' ');
      arrayOfBoundSize[1] = parseBoundSize(arrayOfString[0], true, true);
      if (arrayOfString.length > 1) {
        arrayOfBoundSize[3] = parseBoundSize(arrayOfString[1], true, false);
        if (arrayOfString.length > 2) {
          arrayOfBoundSize[0] = parseBoundSize(arrayOfString[2], true, true);
          if (arrayOfString.length > 3)
            arrayOfBoundSize[2] = parseBoundSize(arrayOfString[3], true, false); 
        } 
      } 
      return arrayOfBoundSize;
    } 
    throw new IllegalArgumentException("Unknown Gap part: '" + paramString + "'");
  }
  
  private static int parseSpan(String paramString) {
    return (paramString.length() > 0) ? Integer.parseInt(paramString) : 2097051;
  }
  
  private static Float parseFloat(String paramString, Float paramFloat) {
    return (paramString.length() > 0) ? new Float(Float.parseFloat(paramString)) : paramFloat;
  }
  
  public static BoundSize parseBoundSize(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramString.length() == 0 || paramString.equals("null") || paramString.equals("n"))
      return null; 
    String str1 = paramString;
    boolean bool = false;
    if (paramString.endsWith("push")) {
      bool = true;
      int i = paramString.length();
      paramString = paramString.substring(0, i - (paramString.endsWith(":push") ? 5 : 4));
      if (paramString.length() == 0)
        return new BoundSize(null, null, null, true, str1); 
    } 
    String[] arrayOfString = toTrimmedTokens(paramString, ':');
    String str2 = arrayOfString[0];
    if (arrayOfString.length == 1) {
      boolean bool1 = str2.endsWith("!");
      if (bool1)
        str2 = str2.substring(0, str2.length() - 1); 
      UnitValue unitValue = parseUnitValue(str2, null, paramBoolean2);
      return new BoundSize((paramBoolean1 || bool1) ? unitValue : null, unitValue, bool1 ? unitValue : null, bool, str1);
    } 
    if (arrayOfString.length == 2)
      return new BoundSize(parseUnitValue(str2, null, paramBoolean2), parseUnitValue(arrayOfString[1], null, paramBoolean2), null, bool, str1); 
    if (arrayOfString.length == 3)
      return new BoundSize(parseUnitValue(str2, null, paramBoolean2), parseUnitValue(arrayOfString[1], null, paramBoolean2), parseUnitValue(arrayOfString[2], null, paramBoolean2), bool, str1); 
    throw new IllegalArgumentException("Min:Preferred:Max size section must contain 0, 1 or 2 colons. '" + str1 + "'");
  }
  
  public static UnitValue parseUnitValueOrAlign(String paramString, boolean paramBoolean, UnitValue paramUnitValue) {
    if (paramString.length() == 0)
      return paramUnitValue; 
    UnitValue unitValue = parseAlignKeywords(paramString, paramBoolean);
    return (unitValue != null) ? unitValue : parseUnitValue(paramString, paramUnitValue, paramBoolean);
  }
  
  public static UnitValue parseUnitValue(String paramString, boolean paramBoolean) {
    return parseUnitValue(paramString, null, paramBoolean);
  }
  
  private static UnitValue parseUnitValue(String paramString, UnitValue paramUnitValue, boolean paramBoolean) {
    if (paramString == null || paramString.length() == 0)
      return paramUnitValue; 
    String str = paramString;
    char c = paramString.charAt(0);
    if (c == '(' && paramString.charAt(paramString.length() - 1) == ')')
      paramString = paramString.substring(1, paramString.length() - 1); 
    if (c == 'n' && (paramString.equals("null") || paramString.equals("n")))
      return null; 
    if (c == 'i' && paramString.equals("inf"))
      return UnitValue.INF; 
    int i = getOper(paramString);
    boolean bool = (i == 101 || i == 102 || i == 103 || i == 104) ? true : false;
    if (i != 100) {
      String[] arrayOfString;
      if (!bool) {
        String str1 = paramString.substring(4, paramString.length() - 1).trim();
        arrayOfString = toTrimmedTokens(str1, ',');
        if (arrayOfString.length == 1)
          return parseUnitValue(str1, null, paramBoolean); 
      } else {
        byte b;
        if (i == 101) {
          b = 43;
        } else if (i == 102) {
          b = 45;
        } else if (i == 103) {
          b = 42;
        } else {
          b = 47;
        } 
        arrayOfString = toTrimmedTokens(paramString, b);
        if (arrayOfString.length > 2) {
          String str1 = arrayOfString[arrayOfString.length - 1];
          String str2 = paramString.substring(0, paramString.length() - str1.length() - 1);
          arrayOfString = new String[] { str2, str1 };
        } 
      } 
      if (arrayOfString.length != 2)
        throw new IllegalArgumentException("Malformed UnitValue: '" + paramString + "'"); 
      UnitValue unitValue1 = parseUnitValue(arrayOfString[0], null, paramBoolean);
      UnitValue unitValue2 = parseUnitValue(arrayOfString[1], null, paramBoolean);
      if (unitValue1 == null || unitValue2 == null)
        throw new IllegalArgumentException("Malformed UnitValue. Must be two sub-values: '" + paramString + "'"); 
      return new UnitValue(paramBoolean, i, unitValue1, unitValue2, str);
    } 
    try {
      String[] arrayOfString = getNumTextParts(paramString);
      float f = (arrayOfString[0].length() > 0) ? Float.parseFloat(arrayOfString[0]) : 1.0F;
      return new UnitValue(f, arrayOfString[1], paramBoolean, i, str);
    } catch (Exception exception) {
      throw new IllegalArgumentException("Malformed UnitValue: '" + paramString + "'");
    } 
  }
  
  static UnitValue parseAlignKeywords(String paramString, boolean paramBoolean) {
    if (startsWithLenient(paramString, "center", 1, false) != -1)
      return UnitValue.CENTER; 
    if (paramBoolean) {
      if (startsWithLenient(paramString, "left", 1, false) != -1)
        return UnitValue.LEFT; 
      if (startsWithLenient(paramString, "right", 1, false) != -1)
        return UnitValue.RIGHT; 
      if (startsWithLenient(paramString, "leading", 4, false) != -1)
        return UnitValue.LEADING; 
      if (startsWithLenient(paramString, "trailing", 5, false) != -1)
        return UnitValue.TRAILING; 
      if (startsWithLenient(paramString, "label", 5, false) != -1)
        return UnitValue.LABEL; 
    } else {
      if (startsWithLenient(paramString, "baseline", 4, false) != -1)
        return UnitValue.BASELINE_IDENTITY; 
      if (startsWithLenient(paramString, "top", 1, false) != -1)
        return UnitValue.TOP; 
      if (startsWithLenient(paramString, "bottom", 1, false) != -1)
        return UnitValue.BOTTOM; 
    } 
    return null;
  }
  
  private static String[] getNumTextParts(String paramString) {
    byte b = 0;
    int i = paramString.length();
    while (b < i) {
      char c = paramString.charAt(b);
      if (c == ' ')
        throw new IllegalArgumentException("Space in UnitValue: '" + paramString + "'"); 
      if ((c < '0' || c > '9') && c != '.' && c != '-')
        return new String[] { paramString.substring(0, b).trim(), paramString.substring(b).trim() }; 
      b++;
    } 
    return new String[] { paramString, "" };
  }
  
  private static int getOper(String paramString) {
    int i = paramString.length();
    if (i < 3)
      return 100; 
    if (i > 5 && paramString.charAt(3) == '(' && paramString.charAt(i - 1) == ')') {
      if (paramString.startsWith("min("))
        return 105; 
      if (paramString.startsWith("max("))
        return 106; 
      if (paramString.startsWith("mid("))
        return 107; 
    } 
    for (byte b = 0; b < 2; b++) {
      int j = i - 1;
      byte b1 = 0;
      while (j > 0) {
        char c = paramString.charAt(j);
        if (c == ')') {
          b1++;
        } else if (c == '(') {
          b1--;
        } else if (b1 == 0) {
          if (b == 0) {
            if (c == '+')
              return 101; 
            if (c == '-')
              return 102; 
          } else {
            if (c == '*')
              return 103; 
            if (c == '/')
              return 104; 
          } 
        } 
        j--;
      } 
    } 
    return 100;
  }
  
  private static int startsWithLenient(String paramString, String[] paramArrayOfString, int[] paramArrayOfint, boolean paramBoolean) {
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      boolean bool = (paramArrayOfint != null) ? paramArrayOfint[b] : true;
      int i = startsWithLenient(paramString, paramArrayOfString[b], bool, paramBoolean);
      if (i > -1)
        return i; 
    } 
    return -1;
  }
  
  private static int startsWithLenient(String paramString1, String paramString2, int paramInt, boolean paramBoolean) {
    if (paramString1.charAt(0) != paramString2.charAt(0))
      return -1; 
    if (paramInt == -1)
      paramInt = paramString2.length(); 
    int i = paramString1.length();
    if (i < paramInt)
      return -1; 
    int j = paramString2.length();
    byte b1 = 0;
    for (byte b2 = 0; b2 < j; b2++) {
      while (b1 < i && (paramString1.charAt(b1) == ' ' || paramString1.charAt(b1) == '_'))
        b1++; 
      if (b1 >= i || paramString1.charAt(b1) != paramString2.charAt(b2))
        return (b2 >= paramInt && (paramBoolean || b1 >= i) && (b1 >= i || paramString1.charAt(b1 - 1) == ' ')) ? b1 : -1; 
      b1++;
    } 
    return (b1 >= i || paramBoolean || paramString1.charAt(b1) == ' ') ? b1 : -1;
  }
  
  private static String[] toTrimmedTokens(String paramString, char paramChar) {
    byte b1 = 0;
    int i = paramString.length();
    boolean bool = (paramChar == ' ') ? true : false;
    byte b2 = 0;
    for (byte b3 = 0; b3 < i; b3++) {
      char c = paramString.charAt(b3);
      if (c == '(') {
        b2++;
      } else if (c == ')') {
        b2--;
      } else if (b2 == 0 && c == paramChar) {
        b1++;
        while (bool && b3 < i - 1 && paramString.charAt(b3 + 1) == ' ')
          b3++; 
      } 
      if (b2 < 0)
        throw new IllegalArgumentException("Unbalanced parentheses: '" + paramString + "'"); 
    } 
    if (b2 != 0)
      throw new IllegalArgumentException("Unbalanced parentheses: '" + paramString + "'"); 
    if (b1 == 0)
      return new String[] { paramString.trim() }; 
    String[] arrayOfString = new String[b1 + 1];
    int j = 0;
    byte b4 = 0;
    b2 = 0;
    for (byte b5 = 0; b5 < i; b5++) {
      char c = paramString.charAt(b5);
      if (c == '(') {
        b2++;
      } else if (c == ')') {
        b2--;
      } else if (b2 == 0 && c == paramChar) {
        arrayOfString[b4++] = paramString.substring(j, b5).trim();
        j = b5 + 1;
        while (bool && b5 < i - 1 && paramString.charAt(b5 + 1) == ' ')
          b5++; 
      } 
    } 
    arrayOfString[b4++] = paramString.substring(j, i).trim();
    return arrayOfString;
  }
  
  private static ArrayList<String> getRowColAndGapsTrimmed(String paramString) {
    if (paramString.indexOf('|') != -1)
      paramString = paramString.replaceAll("\\|", "]["); 
    ArrayList<String> arrayList = new ArrayList(Math.max(paramString.length() >> 3, 3));
    byte b1 = 0;
    byte b2 = 0;
    int i = 0;
    byte b3 = 0;
    int j = paramString.length();
    while (b3 < j) {
      char c = paramString.charAt(b3);
      if (c == '[') {
        b1++;
      } else if (c == ']') {
        b2++;
      } else {
        continue;
      } 
      if (b1 != b2 && b1 - 1 != b2)
        break; 
      arrayList.add(paramString.substring(i, b3).trim());
      i = b3 + 1;
      continue;
      b3++;
    } 
    if (b1 != b2)
      throw new IllegalArgumentException("'[' and ']' mismatch in row/column format string: " + paramString); 
    if (b1 == 0) {
      arrayList.add("");
      arrayList.add(paramString);
      arrayList.add("");
    } else if (arrayList.size() % 2 == 0) {
      arrayList.add(paramString.substring(i, paramString.length()));
    } 
    return arrayList;
  }
  
  public static String prepare(String paramString) {
    return (paramString != null) ? paramString.trim().toLowerCase() : "";
  }
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\miginfocom\layout\ConstraintParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */