/*      */ package org.apache.commons.codec.language;
/*      */ 
/*      */ import java.util.Locale;
/*      */ import org.apache.commons.codec.EncoderException;
/*      */ import org.apache.commons.codec.StringEncoder;
/*      */ import org.apache.commons.codec.binary.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DoubleMetaphone
/*      */   implements StringEncoder
/*      */ {
/*      */   private static final String VOWELS = "AEIOUY";
/*   48 */   private static final String[] SILENT_START = new String[] { "GN", "KN", "PN", "WR", "PS" };
/*      */   
/*   50 */   private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[] { "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };
/*      */   
/*   52 */   private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[] { "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER" };
/*      */   
/*   54 */   private static final String[] L_T_K_S_N_M_B_Z = new String[] { "L", "T", "K", "S", "N", "M", "B", "Z" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   60 */   private int maxCodeLen = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String doubleMetaphone(String value) {
/*   76 */     return doubleMetaphone(value, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String doubleMetaphone(String value, boolean alternate) {
/*   87 */     value = cleanInput(value);
/*   88 */     if (value == null) {
/*   89 */       return null;
/*      */     }
/*      */     
/*   92 */     boolean slavoGermanic = isSlavoGermanic(value);
/*   93 */     int index = isSilentStart(value) ? 1 : 0;
/*      */     
/*   95 */     DoubleMetaphoneResult result = new DoubleMetaphoneResult(getMaxCodeLen());
/*      */     
/*   97 */     while (!result.isComplete() && index <= value.length() - 1) {
/*   98 */       switch (value.charAt(index)) {
/*      */         case 'A':
/*      */         case 'E':
/*      */         case 'I':
/*      */         case 'O':
/*      */         case 'U':
/*      */         case 'Y':
/*  105 */           index = handleAEIOUY(result, index);
/*      */           continue;
/*      */         case 'B':
/*  108 */           result.append('P');
/*  109 */           index = (charAt(value, index + 1) == 'B') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         
/*      */         case 'Ç':
/*  113 */           result.append('S');
/*  114 */           index++;
/*      */           continue;
/*      */         case 'C':
/*  117 */           index = handleC(value, result, index);
/*      */           continue;
/*      */         case 'D':
/*  120 */           index = handleD(value, result, index);
/*      */           continue;
/*      */         case 'F':
/*  123 */           result.append('F');
/*  124 */           index = (charAt(value, index + 1) == 'F') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         case 'G':
/*  127 */           index = handleG(value, result, index, slavoGermanic);
/*      */           continue;
/*      */         case 'H':
/*  130 */           index = handleH(value, result, index);
/*      */           continue;
/*      */         case 'J':
/*  133 */           index = handleJ(value, result, index, slavoGermanic);
/*      */           continue;
/*      */         case 'K':
/*  136 */           result.append('K');
/*  137 */           index = (charAt(value, index + 1) == 'K') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         case 'L':
/*  140 */           index = handleL(value, result, index);
/*      */           continue;
/*      */         case 'M':
/*  143 */           result.append('M');
/*  144 */           index = conditionM0(value, index) ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         case 'N':
/*  147 */           result.append('N');
/*  148 */           index = (charAt(value, index + 1) == 'N') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         
/*      */         case 'Ñ':
/*  152 */           result.append('N');
/*  153 */           index++;
/*      */           continue;
/*      */         case 'P':
/*  156 */           index = handleP(value, result, index);
/*      */           continue;
/*      */         case 'Q':
/*  159 */           result.append('K');
/*  160 */           index = (charAt(value, index + 1) == 'Q') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         case 'R':
/*  163 */           index = handleR(value, result, index, slavoGermanic);
/*      */           continue;
/*      */         case 'S':
/*  166 */           index = handleS(value, result, index, slavoGermanic);
/*      */           continue;
/*      */         case 'T':
/*  169 */           index = handleT(value, result, index);
/*      */           continue;
/*      */         case 'V':
/*  172 */           result.append('F');
/*  173 */           index = (charAt(value, index + 1) == 'V') ? (index + 2) : (index + 1);
/*      */           continue;
/*      */         case 'W':
/*  176 */           index = handleW(value, result, index);
/*      */           continue;
/*      */         case 'X':
/*  179 */           index = handleX(value, result, index);
/*      */           continue;
/*      */         case 'Z':
/*  182 */           index = handleZ(value, result, index, slavoGermanic);
/*      */           continue;
/*      */       } 
/*  185 */       index++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  190 */     return alternate ? result.getAlternate() : result.getPrimary();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object encode(Object obj) throws EncoderException {
/*  203 */     if (!(obj instanceof String)) {
/*  204 */       throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
/*      */     }
/*  206 */     return doubleMetaphone((String)obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String encode(String value) {
/*  217 */     return doubleMetaphone(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDoubleMetaphoneEqual(String value1, String value2) {
/*  231 */     return isDoubleMetaphoneEqual(value1, value2, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate) {
/*  245 */     return StringUtils.equals(doubleMetaphone(value1, alternate), doubleMetaphone(value2, alternate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCodeLen() {
/*  253 */     return this.maxCodeLen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxCodeLen(int maxCodeLen) {
/*  261 */     this.maxCodeLen = maxCodeLen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleAEIOUY(DoubleMetaphoneResult result, int index) {
/*  270 */     if (index == 0) {
/*  271 */       result.append('A');
/*      */     }
/*  273 */     return index + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleC(String value, DoubleMetaphoneResult result, int index) {
/*  280 */     if (conditionC0(value, index))
/*  281 */     { result.append('K');
/*  282 */       index += 2; }
/*  283 */     else if (index == 0 && contains(value, index, 6, new String[] { "CAESAR" }))
/*  284 */     { result.append('S');
/*  285 */       index += 2; }
/*  286 */     else if (contains(value, index, 2, new String[] { "CH" }))
/*  287 */     { index = handleCH(value, result, index); }
/*  288 */     else if (contains(value, index, 2, new String[] { "CZ" }) && !contains(value, index - 2, 4, new String[] { "WICZ" }))
/*      */     
/*      */     { 
/*  291 */       result.append('S', 'X');
/*  292 */       index += 2; }
/*  293 */     else if (contains(value, index + 1, 3, new String[] { "CIA" }))
/*      */     
/*  295 */     { result.append('X');
/*  296 */       index += 3; }
/*  297 */     else { if (contains(value, index, 2, new String[] { "CC" }) && (index != 1 || charAt(value, 0) != 'M'))
/*      */       {
/*      */         
/*  300 */         return handleCC(value, result, index); } 
/*  301 */       if (contains(value, index, 2, new String[] { "CK", "CG", "CQ" })) {
/*  302 */         result.append('K');
/*  303 */         index += 2;
/*  304 */       } else if (contains(value, index, 2, new String[] { "CI", "CE", "CY" })) {
/*      */         
/*  306 */         if (contains(value, index, 3, new String[] { "CIO", "CIE", "CIA" })) {
/*  307 */           result.append('S', 'X');
/*      */         } else {
/*  309 */           result.append('S');
/*      */         } 
/*  311 */         index += 2;
/*      */       } else {
/*  313 */         result.append('K');
/*  314 */         if (contains(value, index + 1, 2, new String[] { " C", " Q", " G" })) {
/*      */           
/*  316 */           index += 3;
/*  317 */         } else if (contains(value, index + 1, 1, new String[] { "C", "K", "Q" }) && !contains(value, index + 1, 2, new String[] { "CE", "CI" })) {
/*      */           
/*  319 */           index += 2;
/*      */         } else {
/*  321 */           index++;
/*      */         } 
/*      */       }  }
/*      */     
/*  325 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleCC(String value, DoubleMetaphoneResult result, int index) {
/*  332 */     if (contains(value, index + 2, 1, new String[] { "I", "E", "H" }) && !contains(value, index + 2, 2, new String[] { "HU" })) {
/*      */ 
/*      */       
/*  335 */       if ((index == 1 && charAt(value, index - 1) == 'A') || contains(value, index - 1, 5, new String[] { "UCCEE", "UCCES" })) {
/*      */ 
/*      */         
/*  338 */         result.append("KS");
/*      */       } else {
/*      */         
/*  341 */         result.append('X');
/*      */       } 
/*  343 */       index += 3;
/*      */     } else {
/*  345 */       result.append('K');
/*  346 */       index += 2;
/*      */     } 
/*      */     
/*  349 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleCH(String value, DoubleMetaphoneResult result, int index) {
/*  356 */     if (index > 0 && contains(value, index, 4, new String[] { "CHAE" })) {
/*  357 */       result.append('K', 'X');
/*  358 */       return index + 2;
/*  359 */     }  if (conditionCH0(value, index)) {
/*      */       
/*  361 */       result.append('K');
/*  362 */       return index + 2;
/*  363 */     }  if (conditionCH1(value, index)) {
/*      */       
/*  365 */       result.append('K');
/*  366 */       return index + 2;
/*      */     } 
/*  368 */     if (index > 0) {
/*  369 */       if (contains(value, 0, 2, new String[] { "MC" })) {
/*  370 */         result.append('K');
/*      */       } else {
/*  372 */         result.append('X', 'K');
/*      */       } 
/*      */     } else {
/*  375 */       result.append('X');
/*      */     } 
/*  377 */     return index + 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleD(String value, DoubleMetaphoneResult result, int index) {
/*  385 */     if (contains(value, index, 2, new String[] { "DG" })) {
/*      */       
/*  387 */       if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
/*  388 */         result.append('J');
/*  389 */         index += 3;
/*      */       } else {
/*      */         
/*  392 */         result.append("TK");
/*  393 */         index += 2;
/*      */       } 
/*  395 */     } else if (contains(value, index, 2, new String[] { "DT", "DD" })) {
/*  396 */       result.append('T');
/*  397 */       index += 2;
/*      */     } else {
/*  399 */       result.append('T');
/*  400 */       index++;
/*      */     } 
/*  402 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleG(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
/*  410 */     if (charAt(value, index + 1) == 'H') {
/*  411 */       index = handleGH(value, result, index);
/*  412 */     } else if (charAt(value, index + 1) == 'N') {
/*  413 */       if (index == 1 && isVowel(charAt(value, 0)) && !slavoGermanic) {
/*  414 */         result.append("KN", "N");
/*  415 */       } else if (!contains(value, index + 2, 2, new String[] { "EY" }) && charAt(value, index + 1) != 'Y' && !slavoGermanic) {
/*      */         
/*  417 */         result.append("N", "KN");
/*      */       } else {
/*  419 */         result.append("KN");
/*      */       } 
/*  421 */       index += 2;
/*  422 */     } else if (contains(value, index + 1, 2, new String[] { "LI" }) && !slavoGermanic) {
/*  423 */       result.append("KL", "L");
/*  424 */       index += 2;
/*  425 */     } else if (index == 0 && (charAt(value, index + 1) == 'Y' || contains(value, index + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
/*      */ 
/*      */ 
/*      */       
/*  429 */       result.append('K', 'J');
/*  430 */       index += 2;
/*  431 */     } else if ((contains(value, index + 1, 2, new String[] { "ER" }) || charAt(value, index + 1) == 'Y') && !contains(value, 0, 6, new String[] { "DANGER", "RANGER", "MANGER" }) && !contains(value, index - 1, 1, new String[] { "E", "I" }) && !contains(value, index - 1, 3, new String[] { "RGY", "OGY" })) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  437 */       result.append('K', 'J');
/*  438 */       index += 2;
/*  439 */     } else if (contains(value, index + 1, 1, new String[] { "E", "I", "Y" }) || contains(value, index - 1, 4, new String[] { "AGGI", "OGGI" })) {
/*      */ 
/*      */       
/*  442 */       if (contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" }) || contains(value, index + 1, 2, new String[] { "ET" })) {
/*      */ 
/*      */ 
/*      */         
/*  446 */         result.append('K');
/*  447 */       } else if (contains(value, index + 1, 3, new String[] { "IER" })) {
/*  448 */         result.append('J');
/*      */       } else {
/*  450 */         result.append('J', 'K');
/*      */       } 
/*  452 */       index += 2;
/*  453 */     } else if (charAt(value, index + 1) == 'G') {
/*  454 */       index += 2;
/*  455 */       result.append('K');
/*      */     } else {
/*  457 */       index++;
/*  458 */       result.append('K');
/*      */     } 
/*  460 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleGH(String value, DoubleMetaphoneResult result, int index) {
/*  467 */     if (index > 0 && !isVowel(charAt(value, index - 1))) {
/*  468 */       result.append('K');
/*  469 */       index += 2;
/*  470 */     } else if (index == 0) {
/*  471 */       if (charAt(value, index + 2) == 'I') {
/*  472 */         result.append('J');
/*      */       } else {
/*  474 */         result.append('K');
/*      */       } 
/*  476 */       index += 2;
/*  477 */     } else if ((index > 1 && contains(value, index - 2, 1, new String[] { "B", "H", "D" })) || (index > 2 && contains(value, index - 3, 1, new String[] { "B", "H", "D" })) || (index > 3 && contains(value, index - 4, 1, new String[] { "B", "H" }))) {
/*      */ 
/*      */ 
/*      */       
/*  481 */       index += 2;
/*      */     } else {
/*  483 */       if (index > 2 && charAt(value, index - 1) == 'U' && contains(value, index - 3, 1, new String[] { "C", "G", "L", "R", "T" })) {
/*      */ 
/*      */         
/*  486 */         result.append('F');
/*  487 */       } else if (index > 0 && charAt(value, index - 1) != 'I') {
/*  488 */         result.append('K');
/*      */       } 
/*  490 */       index += 2;
/*      */     } 
/*  492 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleH(String value, DoubleMetaphoneResult result, int index) {
/*  500 */     if ((index == 0 || isVowel(charAt(value, index - 1))) && isVowel(charAt(value, index + 1))) {
/*      */       
/*  502 */       result.append('H');
/*  503 */       index += 2;
/*      */     } else {
/*      */       
/*  506 */       index++;
/*      */     } 
/*  508 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleJ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
/*  516 */     if (contains(value, index, 4, new String[] { "JOSE" }) || contains(value, 0, 4, new String[] { "SAN " })) {
/*      */       
/*  518 */       if ((index == 0 && charAt(value, index + 4) == ' ') || value.length() == 4 || contains(value, 0, 4, new String[] { "SAN " })) {
/*      */         
/*  520 */         result.append('H');
/*      */       } else {
/*  522 */         result.append('J', 'H');
/*      */       } 
/*  524 */       index++;
/*      */     } else {
/*  526 */       if (index == 0 && !contains(value, index, 4, new String[] { "JOSE" })) {
/*  527 */         result.append('J', 'A');
/*  528 */       } else if (isVowel(charAt(value, index - 1)) && !slavoGermanic && (charAt(value, index + 1) == 'A' || charAt(value, index + 1) == 'O')) {
/*      */         
/*  530 */         result.append('J', 'H');
/*  531 */       } else if (index == value.length() - 1) {
/*  532 */         result.append('J', ' ');
/*  533 */       } else if (!contains(value, index + 1, 1, L_T_K_S_N_M_B_Z) && !contains(value, index - 1, 1, new String[] { "S", "K", "L" })) {
/*      */         
/*  535 */         result.append('J');
/*      */       } 
/*      */       
/*  538 */       if (charAt(value, index + 1) == 'J') {
/*  539 */         index += 2;
/*      */       } else {
/*  541 */         index++;
/*      */       } 
/*      */     } 
/*  544 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleL(String value, DoubleMetaphoneResult result, int index) {
/*  551 */     if (charAt(value, index + 1) == 'L') {
/*  552 */       if (conditionL0(value, index)) {
/*  553 */         result.appendPrimary('L');
/*      */       } else {
/*  555 */         result.append('L');
/*      */       } 
/*  557 */       index += 2;
/*      */     } else {
/*  559 */       index++;
/*  560 */       result.append('L');
/*      */     } 
/*  562 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleP(String value, DoubleMetaphoneResult result, int index) {
/*  569 */     if (charAt(value, index + 1) == 'H') {
/*  570 */       result.append('F');
/*  571 */       index += 2;
/*      */     } else {
/*  573 */       result.append('P');
/*  574 */       index = contains(value, index + 1, 1, new String[] { "P", "B" }) ? (index + 2) : (index + 1);
/*      */     } 
/*  576 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleR(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
/*  584 */     if (index == value.length() - 1 && !slavoGermanic && contains(value, index - 2, 2, new String[] { "IE" }) && !contains(value, index - 4, 2, new String[] { "ME", "MA" })) {
/*      */ 
/*      */       
/*  587 */       result.appendAlternate('R');
/*      */     } else {
/*  589 */       result.append('R');
/*      */     } 
/*  591 */     return (charAt(value, index + 1) == 'R') ? (index + 2) : (index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleS(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
/*  599 */     if (contains(value, index - 1, 3, new String[] { "ISL", "YSL" })) {
/*      */       
/*  601 */       index++;
/*  602 */     } else if (index == 0 && contains(value, index, 5, new String[] { "SUGAR" })) {
/*      */       
/*  604 */       result.append('X', 'S');
/*  605 */       index++;
/*  606 */     } else if (contains(value, index, 2, new String[] { "SH" })) {
/*  607 */       if (contains(value, index + 1, 4, new String[] { "HEIM", "HOEK", "HOLM", "HOLZ" })) {
/*      */         
/*  609 */         result.append('S');
/*      */       } else {
/*  611 */         result.append('X');
/*      */       } 
/*  613 */       index += 2;
/*  614 */     } else if (contains(value, index, 3, new String[] { "SIO", "SIA" }) || contains(value, index, 4, new String[] { "SIAN" })) {
/*      */       
/*  616 */       if (slavoGermanic) {
/*  617 */         result.append('S');
/*      */       } else {
/*  619 */         result.append('S', 'X');
/*      */       } 
/*  621 */       index += 3;
/*  622 */     } else if ((index == 0 && contains(value, index + 1, 1, new String[] { "M", "N", "L", "W" })) || contains(value, index + 1, 1, new String[] { "Z" })) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  628 */       result.append('S', 'X');
/*  629 */       index = contains(value, index + 1, 1, new String[] { "Z" }) ? (index + 2) : (index + 1);
/*  630 */     } else if (contains(value, index, 2, new String[] { "SC" })) {
/*  631 */       index = handleSC(value, result, index);
/*      */     } else {
/*  633 */       if (index == value.length() - 1 && contains(value, index - 2, 2, new String[] { "AI", "OI" })) {
/*      */         
/*  635 */         result.appendAlternate('S');
/*      */       } else {
/*  637 */         result.append('S');
/*      */       } 
/*  639 */       index = contains(value, index + 1, 1, new String[] { "S", "Z" }) ? (index + 2) : (index + 1);
/*      */     } 
/*  641 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleSC(String value, DoubleMetaphoneResult result, int index) {
/*  648 */     if (charAt(value, index + 2) == 'H') {
/*      */       
/*  650 */       if (contains(value, index + 3, 2, new String[] { "OO", "ER", "EN", "UY", "ED", "EM" })) {
/*      */         
/*  652 */         if (contains(value, index + 3, 2, new String[] { "ER", "EN" })) {
/*      */           
/*  654 */           result.append("X", "SK");
/*      */         } else {
/*  656 */           result.append("SK");
/*      */         }
/*      */       
/*  659 */       } else if (index == 0 && !isVowel(charAt(value, 3)) && charAt(value, 3) != 'W') {
/*  660 */         result.append('X', 'S');
/*      */       } else {
/*  662 */         result.append('X');
/*      */       }
/*      */     
/*  665 */     } else if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
/*  666 */       result.append('S');
/*      */     } else {
/*  668 */       result.append("SK");
/*      */     } 
/*  670 */     return index + 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleT(String value, DoubleMetaphoneResult result, int index) {
/*  677 */     if (contains(value, index, 4, new String[] { "TION" })) {
/*  678 */       result.append('X');
/*  679 */       index += 3;
/*  680 */     } else if (contains(value, index, 3, new String[] { "TIA", "TCH" })) {
/*  681 */       result.append('X');
/*  682 */       index += 3;
/*  683 */     } else if (contains(value, index, 2, new String[] { "TH" }) || contains(value, index, 3, new String[] { "TTH" })) {
/*  684 */       if (contains(value, index + 2, 2, new String[] { "OM", "AM" }) || contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" })) {
/*      */ 
/*      */ 
/*      */         
/*  688 */         result.append('T');
/*      */       } else {
/*  690 */         result.append('0', 'T');
/*      */       } 
/*  692 */       index += 2;
/*      */     } else {
/*  694 */       result.append('T');
/*  695 */       index = contains(value, index + 1, 1, new String[] { "T", "D" }) ? (index + 2) : (index + 1);
/*      */     } 
/*  697 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleW(String value, DoubleMetaphoneResult result, int index) {
/*  704 */     if (contains(value, index, 2, new String[] { "WR" })) {
/*      */       
/*  706 */       result.append('R');
/*  707 */       index += 2;
/*      */     }
/*  709 */     else if (index == 0 && (isVowel(charAt(value, index + 1)) || contains(value, index, 2, new String[] { "WH" }))) {
/*      */       
/*  711 */       if (isVowel(charAt(value, index + 1))) {
/*      */         
/*  713 */         result.append('A', 'F');
/*      */       } else {
/*      */         
/*  716 */         result.append('A');
/*      */       } 
/*  718 */       index++;
/*  719 */     } else if ((index == value.length() - 1 && isVowel(charAt(value, index - 1))) || contains(value, index - 1, 5, new String[] { "EWSKI", "EWSKY", "OWSKI", "OWSKY" }) || contains(value, 0, 3, new String[] { "SCH" })) {
/*      */ 
/*      */ 
/*      */       
/*  723 */       result.appendAlternate('F');
/*  724 */       index++;
/*  725 */     } else if (contains(value, index, 4, new String[] { "WICZ", "WITZ" })) {
/*      */       
/*  727 */       result.append("TS", "FX");
/*  728 */       index += 4;
/*      */     } else {
/*  730 */       index++;
/*      */     } 
/*      */     
/*  733 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleX(String value, DoubleMetaphoneResult result, int index) {
/*  740 */     if (index == 0) {
/*  741 */       result.append('S');
/*  742 */       index++;
/*      */     } else {
/*  744 */       if (index != value.length() - 1 || (!contains(value, index - 3, 3, new String[] { "IAU", "EAU" }) && !contains(value, index - 2, 2, new String[] { "AU", "OU"
/*      */           
/*      */           })))
/*      */       {
/*  748 */         result.append("KS");
/*      */       }
/*  750 */       index = contains(value, index + 1, 1, new String[] { "C", "X" }) ? (index + 2) : (index + 1);
/*      */     } 
/*  752 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleZ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
/*  760 */     if (charAt(value, index + 1) == 'H') {
/*      */       
/*  762 */       result.append('J');
/*  763 */       index += 2;
/*      */     } else {
/*  765 */       if (contains(value, index + 1, 2, new String[] { "ZO", "ZI", "ZA" }) || (slavoGermanic && index > 0 && charAt(value, index - 1) != 'T')) {
/*      */         
/*  767 */         result.append("S", "TS");
/*      */       } else {
/*  769 */         result.append('S');
/*      */       } 
/*  771 */       index = (charAt(value, index + 1) == 'Z') ? (index + 2) : (index + 1);
/*      */     } 
/*  773 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean conditionC0(String value, int index) {
/*  782 */     if (contains(value, index, 4, new String[] { "CHIA" }))
/*  783 */       return true; 
/*  784 */     if (index <= 1)
/*  785 */       return false; 
/*  786 */     if (isVowel(charAt(value, index - 2)))
/*  787 */       return false; 
/*  788 */     if (!contains(value, index - 1, 3, new String[] { "ACH" })) {
/*  789 */       return false;
/*      */     }
/*  791 */     char c = charAt(value, index + 2);
/*  792 */     return ((c != 'I' && c != 'E') || contains(value, index - 2, 6, new String[] { "BACHER", "MACHER" }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean conditionCH0(String value, int index) {
/*  801 */     if (index != 0)
/*  802 */       return false; 
/*  803 */     if (!contains(value, index + 1, 5, new String[] { "HARAC", "HARIS" }) && !contains(value, index + 1, 3, new String[] { "HOR", "HYM", "HIA", "HEM" }))
/*      */     {
/*  805 */       return false; } 
/*  806 */     if (contains(value, 0, 5, new String[] { "CHORE" })) {
/*  807 */       return false;
/*      */     }
/*  809 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean conditionCH1(String value, int index) {
/*  817 */     return (contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" }) || contains(value, index - 2, 6, new String[] { "ORCHES", "ARCHIT", "ORCHID" }) || contains(value, index + 2, 1, new String[] { "T", "S" }) || ((contains(value, index - 1, 1, new String[] { "A", "O", "U", "E" }) || index == 0) && (contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE) || index + 1 == value.length() - 1)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean conditionL0(String value, int index) {
/*  828 */     if (index == value.length() - 3 && contains(value, index - 1, 4, new String[] { "ILLO", "ILLA", "ALLE" }))
/*      */     {
/*  830 */       return true; } 
/*  831 */     if ((contains(value, value.length() - 2, 2, new String[] { "AS", "OS" }) || contains(value, value.length() - 1, 1, new String[] { "A", "O" })) && contains(value, index - 1, 4, new String[] { "ALLE"
/*      */         }))
/*      */     {
/*  834 */       return true;
/*      */     }
/*  836 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean conditionM0(String value, int index) {
/*  844 */     if (charAt(value, index + 1) == 'M') {
/*  845 */       return true;
/*      */     }
/*  847 */     return (contains(value, index - 1, 3, new String[] { "UMB" }) && (index + 1 == value.length() - 1 || contains(value, index + 2, 2, new String[] { "ER" })));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSlavoGermanic(String value) {
/*  858 */     return (value.indexOf('W') > -1 || value.indexOf('K') > -1 || value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isVowel(char ch) {
/*  866 */     return ("AEIOUY".indexOf(ch) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSilentStart(String value) {
/*  875 */     boolean result = false;
/*  876 */     for (String element : SILENT_START) {
/*  877 */       if (value.startsWith(element)) {
/*  878 */         result = true;
/*      */         break;
/*      */       } 
/*      */     } 
/*  882 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String cleanInput(String input) {
/*  889 */     if (input == null) {
/*  890 */       return null;
/*      */     }
/*  892 */     input = input.trim();
/*  893 */     if (input.length() == 0) {
/*  894 */       return null;
/*      */     }
/*  896 */     return input.toUpperCase(Locale.ENGLISH);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char charAt(String value, int index) {
/*  905 */     if (index < 0 || index >= value.length()) {
/*  906 */       return Character.MIN_VALUE;
/*      */     }
/*  908 */     return value.charAt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean contains(String value, int start, int length, String... criteria) {
/*  917 */     boolean result = false;
/*  918 */     if (start >= 0 && start + length <= value.length()) {
/*  919 */       String target = value.substring(start, start + length);
/*      */       
/*  921 */       for (String element : criteria) {
/*  922 */         if (target.equals(element)) {
/*  923 */           result = true;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  928 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class DoubleMetaphoneResult
/*      */   {
/*  938 */     private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
/*  939 */     private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
/*      */     private final int maxLength;
/*      */     
/*      */     public DoubleMetaphoneResult(int maxLength) {
/*  943 */       this.maxLength = maxLength;
/*      */     }
/*      */     
/*      */     public void append(char value) {
/*  947 */       appendPrimary(value);
/*  948 */       appendAlternate(value);
/*      */     }
/*      */     
/*      */     public void append(char primary, char alternate) {
/*  952 */       appendPrimary(primary);
/*  953 */       appendAlternate(alternate);
/*      */     }
/*      */     
/*      */     public void appendPrimary(char value) {
/*  957 */       if (this.primary.length() < this.maxLength) {
/*  958 */         this.primary.append(value);
/*      */       }
/*      */     }
/*      */     
/*      */     public void appendAlternate(char value) {
/*  963 */       if (this.alternate.length() < this.maxLength) {
/*  964 */         this.alternate.append(value);
/*      */       }
/*      */     }
/*      */     
/*      */     public void append(String value) {
/*  969 */       appendPrimary(value);
/*  970 */       appendAlternate(value);
/*      */     }
/*      */     
/*      */     public void append(String primary, String alternate) {
/*  974 */       appendPrimary(primary);
/*  975 */       appendAlternate(alternate);
/*      */     }
/*      */     
/*      */     public void appendPrimary(String value) {
/*  979 */       int addChars = this.maxLength - this.primary.length();
/*  980 */       if (value.length() <= addChars) {
/*  981 */         this.primary.append(value);
/*      */       } else {
/*  983 */         this.primary.append(value.substring(0, addChars));
/*      */       } 
/*      */     }
/*      */     
/*      */     public void appendAlternate(String value) {
/*  988 */       int addChars = this.maxLength - this.alternate.length();
/*  989 */       if (value.length() <= addChars) {
/*  990 */         this.alternate.append(value);
/*      */       } else {
/*  992 */         this.alternate.append(value.substring(0, addChars));
/*      */       } 
/*      */     }
/*      */     
/*      */     public String getPrimary() {
/*  997 */       return this.primary.toString();
/*      */     }
/*      */     
/*      */     public String getAlternate() {
/* 1001 */       return this.alternate.toString();
/*      */     }
/*      */     
/*      */     public boolean isComplete() {
/* 1005 */       return (this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\DoubleMetaphone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */