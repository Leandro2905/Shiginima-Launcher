/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  49 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  52 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  53 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  54 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  55 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  56 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  57 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  58 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  59 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  60 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  61 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  62 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  63 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  64 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  65 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  66 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  67 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flattenMapping(MappingNode node) {
/*  72 */     if (node.isMerged()) {
/*  73 */       node.setValue(mergeNode(node, true, new HashMap<Object, Integer>(), new ArrayList<NodeTuple>()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values) {
/*  94 */     List<NodeTuple> nodeValue = node.getValue();
/*     */     
/*  96 */     Collections.reverse(nodeValue);
/*  97 */     for (Iterator<NodeTuple> iter = nodeValue.iterator(); iter.hasNext(); ) {
/*  98 */       NodeTuple nodeTuple = iter.next();
/*  99 */       Node keyNode = nodeTuple.getKeyNode();
/* 100 */       Node valueNode = nodeTuple.getValueNode();
/* 101 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 102 */         MappingNode mn; SequenceNode sn; List<Node> vals; iter.remove();
/* 103 */         switch (valueNode.getNodeId()) {
/*     */           case mapping:
/* 105 */             mn = (MappingNode)valueNode;
/* 106 */             mergeNode(mn, false, key2index, values);
/*     */             continue;
/*     */           case sequence:
/* 109 */             sn = (SequenceNode)valueNode;
/* 110 */             vals = sn.getValue();
/* 111 */             for (Node subnode : vals) {
/* 112 */               if (!(subnode instanceof MappingNode)) {
/* 113 */                 throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 118 */               MappingNode mnode = (MappingNode)subnode;
/* 119 */               mergeNode(mnode, false, key2index, values);
/*     */             } 
/*     */             continue;
/*     */         } 
/* 123 */         throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), valueNode.getStartMark());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       Object key = constructObject(keyNode);
/* 131 */       if (!key2index.containsKey(key)) {
/* 132 */         values.add(nodeTuple);
/*     */         
/* 134 */         key2index.put(key, Integer.valueOf(values.size() - 1)); continue;
/* 135 */       }  if (isPreffered)
/*     */       {
/*     */         
/* 138 */         values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return values;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 146 */     flattenMapping(node);
/* 147 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 152 */     flattenMapping(node);
/* 153 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 158 */       SafeConstructor.this.constructScalar((ScalarNode)node);
/* 159 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 163 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap<String, Boolean>();
/*     */   static {
/* 165 */     BOOL_VALUES.put("yes", Boolean.TRUE);
/* 166 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 167 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 168 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 169 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 170 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 175 */       String val = (String)SafeConstructor.this.constructScalar((ScalarNode)node);
/* 176 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 182 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 183 */       int sign = 1;
/* 184 */       char first = value.charAt(0);
/* 185 */       if (first == '-') {
/* 186 */         sign = -1;
/* 187 */         value = value.substring(1);
/* 188 */       } else if (first == '+') {
/* 189 */         value = value.substring(1);
/*     */       } 
/* 191 */       int base = 10;
/* 192 */       if ("0".equals(value))
/* 193 */         return Integer.valueOf(0); 
/* 194 */       if (value.startsWith("0b"))
/* 195 */       { value = value.substring(2);
/* 196 */         base = 2; }
/* 197 */       else if (value.startsWith("0x"))
/* 198 */       { value = value.substring(2);
/* 199 */         base = 16; }
/* 200 */       else if (value.startsWith("0"))
/* 201 */       { value = value.substring(1);
/* 202 */         base = 8; }
/* 203 */       else { if (value.indexOf(':') != -1) {
/* 204 */           String[] digits = value.split(":");
/* 205 */           int bes = 1;
/* 206 */           int val = 0;
/* 207 */           for (int i = 0, j = digits.length; i < j; i++) {
/* 208 */             val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
/* 209 */             bes *= 60;
/*     */           } 
/* 211 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         } 
/* 213 */         return SafeConstructor.this.createNumber(sign, value, 10); }
/*     */       
/* 215 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/*     */   private Number createNumber(int sign, String number, int radix) {
/*     */     Number number1;
/* 221 */     if (sign < 0) {
/* 222 */       number = "-" + number;
/*     */     }
/*     */     try {
/* 225 */       number1 = Integer.valueOf(number, radix);
/* 226 */     } catch (NumberFormatException e) {
/*     */       try {
/* 228 */         number1 = Long.valueOf(number, radix);
/* 229 */       } catch (NumberFormatException e1) {
/* 230 */         number1 = new BigInteger(number, radix);
/*     */       } 
/*     */     } 
/* 233 */     return number1;
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 238 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 239 */       int sign = 1;
/* 240 */       char first = value.charAt(0);
/* 241 */       if (first == '-') {
/* 242 */         sign = -1;
/* 243 */         value = value.substring(1);
/* 244 */       } else if (first == '+') {
/* 245 */         value = value.substring(1);
/*     */       } 
/* 247 */       String valLower = value.toLowerCase();
/* 248 */       if (".inf".equals(valLower))
/* 249 */         return new Double((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); 
/* 250 */       if (".nan".equals(valLower))
/* 251 */         return new Double(Double.NaN); 
/* 252 */       if (value.indexOf(':') != -1) {
/* 253 */         String[] digits = value.split(":");
/* 254 */         int bes = 1;
/* 255 */         double val = 0.0D;
/* 256 */         for (int i = 0, j = digits.length; i < j; i++) {
/* 257 */           val += Double.parseDouble(digits[j - i - 1]) * bes;
/* 258 */           bes *= 60;
/*     */         } 
/* 260 */         return new Double(sign * val);
/*     */       } 
/* 262 */       Double d = Double.valueOf(value);
/* 263 */       return new Double(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlBinary
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 270 */       byte[] decoded = Base64Coder.decode(SafeConstructor.this.constructScalar((ScalarNode)node).toString().toCharArray());
/*     */       
/* 272 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlNumber
/*     */     extends AbstractConstruct {
/* 278 */     private final NumberFormat nf = NumberFormat.getInstance();
/*     */     
/*     */     public Object construct(Node node) {
/* 281 */       ScalarNode scalar = (ScalarNode)node;
/*     */       try {
/* 283 */         return this.nf.parse(scalar.getValue());
/* 284 */       } catch (ParseException e) {
/* 285 */         String lowerCaseValue = scalar.getValue().toLowerCase();
/* 286 */         if (lowerCaseValue.contains("inf") || lowerCaseValue.contains("nan"))
/*     */         {
/*     */           
/* 289 */           return ((Construct)SafeConstructor.this.yamlConstructors.get(Tag.FLOAT)).construct(node);
/*     */         }
/* 291 */         throw new IllegalArgumentException("Unable to parse as Number: " + scalar.getValue());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 298 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */   
/* 300 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp
/*     */     extends AbstractConstruct {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 307 */       return this.calendar;
/*     */     }
/*     */     public Object construct(Node node) {
/*     */       TimeZone timeZone;
/* 311 */       ScalarNode scalar = (ScalarNode)node;
/* 312 */       String nodeValue = scalar.getValue();
/* 313 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 314 */       if (match.matches()) {
/* 315 */         String str1 = match.group(1);
/* 316 */         String str2 = match.group(2);
/* 317 */         String str3 = match.group(3);
/* 318 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 319 */         this.calendar.clear();
/* 320 */         this.calendar.set(1, Integer.parseInt(str1));
/*     */         
/* 322 */         this.calendar.set(2, Integer.parseInt(str2) - 1);
/* 323 */         this.calendar.set(5, Integer.parseInt(str3));
/* 324 */         return this.calendar.getTime();
/*     */       } 
/* 326 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 327 */       if (!match.matches()) {
/* 328 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 330 */       String year_s = match.group(1);
/* 331 */       String month_s = match.group(2);
/* 332 */       String day_s = match.group(3);
/* 333 */       String hour_s = match.group(4);
/* 334 */       String min_s = match.group(5);
/*     */       
/* 336 */       String seconds = match.group(6);
/* 337 */       String millis = match.group(7);
/* 338 */       if (millis != null) {
/* 339 */         seconds = seconds + "." + millis;
/*     */       }
/* 341 */       double fractions = Double.parseDouble(seconds);
/* 342 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 343 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 345 */       String timezoneh_s = match.group(8);
/* 346 */       String timezonem_s = match.group(9);
/*     */       
/* 348 */       if (timezoneh_s != null) {
/* 349 */         String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
/* 350 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       } else {
/*     */         
/* 353 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       } 
/* 355 */       this.calendar = Calendar.getInstance(timeZone);
/* 356 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 358 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 359 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 360 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 361 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 362 */       this.calendar.set(13, sec_s);
/* 363 */       this.calendar.set(14, usec);
/* 364 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlOmap
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 373 */       Map<Object, Object> omap = new LinkedHashMap<Object, Object>();
/* 374 */       if (!(node instanceof SequenceNode)) {
/* 375 */         throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */ 
/*     */       
/* 379 */       SequenceNode snode = (SequenceNode)node;
/* 380 */       for (Node subnode : snode.getValue()) {
/* 381 */         if (!(subnode instanceof MappingNode)) {
/* 382 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 386 */         MappingNode mnode = (MappingNode)subnode;
/* 387 */         if (mnode.getValue().size() != 1) {
/* 388 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 392 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 393 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 394 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 395 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 396 */         omap.put(key, value);
/*     */       } 
/* 398 */       return omap;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlPairs
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 407 */       if (!(node instanceof SequenceNode)) {
/* 408 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */       
/* 411 */       SequenceNode snode = (SequenceNode)node;
/* 412 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 413 */       for (Node subnode : snode.getValue()) {
/* 414 */         if (!(subnode instanceof MappingNode)) {
/* 415 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 419 */         MappingNode mnode = (MappingNode)subnode;
/* 420 */         if (mnode.getValue().size() != 1) {
/* 421 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 425 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 426 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 427 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 428 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 429 */         pairs.add(new Object[] { key, value });
/*     */       } 
/* 431 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet implements Construct {
/*     */     public Object construct(Node node) {
/* 437 */       if (node.isTwoStepsConstruction()) {
/* 438 */         return SafeConstructor.this.createDefaultSet();
/*     */       }
/* 440 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 446 */       if (node.isTwoStepsConstruction()) {
/* 447 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 449 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 456 */       return SafeConstructor.this.constructScalar((ScalarNode)node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq implements Construct {
/*     */     public Object construct(Node node) {
/* 462 */       SequenceNode seqNode = (SequenceNode)node;
/* 463 */       if (node.isTwoStepsConstruction()) {
/* 464 */         return SafeConstructor.this.createDefaultList(seqNode.getValue().size());
/*     */       }
/* 466 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object data) {
/* 472 */       if (node.isTwoStepsConstruction()) {
/* 473 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else {
/* 475 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap implements Construct {
/*     */     public Object construct(Node node) {
/* 482 */       if (node.isTwoStepsConstruction()) {
/* 483 */         return SafeConstructor.this.createDefaultMap();
/*     */       }
/* 485 */       return SafeConstructor.this.constructMapping((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 491 */       if (node.isTwoStepsConstruction()) {
/* 492 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/*     */       } else {
/* 494 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 501 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node.getTag(), node.getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */