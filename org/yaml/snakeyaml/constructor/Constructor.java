/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
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
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   private final Map<Tag, Class<? extends Object>> typeTags;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   
/*     */   public Constructor() {
/*  54 */     this(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot) {
/*  64 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
/*  71 */     if (theRoot == null) {
/*  72 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  74 */     return theRoot;
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  78 */     if (theRoot == null) {
/*  79 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/*  81 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/*  82 */     if (!Object.class.equals(theRoot.getType())) {
/*  83 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/*  85 */     this.typeTags = new HashMap<Tag, Class<? extends Object>>();
/*  86 */     this.typeDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
/*  87 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/*  88 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/*  89 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/*  90 */     addTypeDescription(theRoot);
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
/*     */   public Constructor(String theRoot) throws ClassNotFoundException {
/* 103 */     this((Class)Class.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 107 */     if (s == null) {
/* 108 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 110 */     if (s.trim().length() == 0) {
/* 111 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 113 */     return s;
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
/*     */   public TypeDescription addTypeDescription(TypeDescription definition) {
/* 127 */     if (definition == null) {
/* 128 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 130 */     Tag tag = definition.getTag();
/* 131 */     this.typeTags.put(tag, definition.getType());
/* 132 */     return this.typeDefinitions.put(definition.getType(), definition);
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
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 151 */       MappingNode mnode = (MappingNode)node;
/* 152 */       if (Properties.class.isAssignableFrom(node.getType())) {
/* 153 */         Properties properties = new Properties();
/* 154 */         if (!node.isTwoStepsConstruction()) {
/* 155 */           Constructor.this.constructMapping2ndStep(mnode, properties);
/*     */         } else {
/* 157 */           throw new YAMLException("Properties must not be recursive.");
/*     */         } 
/* 159 */         return properties;
/* 160 */       }  if (SortedMap.class.isAssignableFrom(node.getType())) {
/* 161 */         SortedMap<Object, Object> map = new TreeMap<Object, Object>();
/* 162 */         if (!node.isTwoStepsConstruction()) {
/* 163 */           Constructor.this.constructMapping2ndStep(mnode, map);
/*     */         }
/* 165 */         return map;
/* 166 */       }  if (Map.class.isAssignableFrom(node.getType())) {
/* 167 */         if (node.isTwoStepsConstruction()) {
/* 168 */           return Constructor.this.createDefaultMap();
/*     */         }
/* 170 */         return Constructor.this.constructMapping(mnode);
/*     */       } 
/* 172 */       if (SortedSet.class.isAssignableFrom(node.getType())) {
/* 173 */         SortedSet<Object> set = new TreeSet();
/*     */ 
/*     */         
/* 176 */         Constructor.this.constructSet2ndStep(mnode, set);
/*     */         
/* 178 */         return set;
/* 179 */       }  if (Collection.class.isAssignableFrom(node.getType())) {
/* 180 */         if (node.isTwoStepsConstruction()) {
/* 181 */           return Constructor.this.createDefaultSet();
/*     */         }
/* 183 */         return Constructor.this.constructSet(mnode);
/*     */       } 
/*     */       
/* 186 */       if (node.isTwoStepsConstruction()) {
/* 187 */         return createEmptyJavaBean(mnode);
/*     */       }
/* 189 */       return constructJavaBean2ndStep(mnode, createEmptyJavaBean(mnode));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 196 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 197 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/* 198 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 199 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 201 */         constructJavaBean2ndStep((MappingNode)node, object);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object createEmptyJavaBean(MappingNode node) {
/*     */       try {
/* 215 */         Constructor<?> c = node.getType().getDeclaredConstructor(new Class[0]);
/* 216 */         c.setAccessible(true);
/* 217 */         return c.newInstance(new Object[0]);
/* 218 */       } catch (Exception e) {
/* 219 */         throw new YAMLException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 224 */       Constructor.this.flattenMapping(node);
/* 225 */       Class<? extends Object> beanType = node.getType();
/* 226 */       List<NodeTuple> nodeValue = node.getValue();
/* 227 */       for (NodeTuple tuple : nodeValue) {
/*     */         ScalarNode keyNode;
/* 229 */         if (tuple.getKeyNode() instanceof ScalarNode) {
/*     */           
/* 231 */           keyNode = (ScalarNode)tuple.getKeyNode();
/*     */         } else {
/* 233 */           throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
/*     */         } 
/* 235 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 237 */         keyNode.setType(String.class);
/* 238 */         String key = (String)Constructor.this.constructObject((Node)keyNode);
/*     */         try {
/* 240 */           Property property = getProperty(beanType, key);
/* 241 */           valueNode.setType(property.getType());
/* 242 */           TypeDescription memberDescription = Constructor.this.typeDefinitions.get(beanType);
/* 243 */           boolean typeDetected = false;
/* 244 */           if (memberDescription != null) {
/* 245 */             SequenceNode snode; Class<? extends Object> memberType; MappingNode mnode; Class<? extends Object> keyType; switch (valueNode.getNodeId()) {
/*     */               case sequence:
/* 247 */                 snode = (SequenceNode)valueNode;
/* 248 */                 memberType = memberDescription.getListPropertyType(key);
/*     */                 
/* 250 */                 if (memberType != null) {
/* 251 */                   snode.setListType(memberType);
/* 252 */                   typeDetected = true; break;
/* 253 */                 }  if (property.getType().isArray()) {
/* 254 */                   snode.setListType(property.getType().getComponentType());
/* 255 */                   typeDetected = true;
/*     */                 } 
/*     */                 break;
/*     */               case mapping:
/* 259 */                 mnode = (MappingNode)valueNode;
/* 260 */                 keyType = memberDescription.getMapKeyType(key);
/* 261 */                 if (keyType != null) {
/* 262 */                   mnode.setTypes(keyType, memberDescription.getMapValueType(key));
/* 263 */                   typeDetected = true;
/*     */                 } 
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } 
/* 269 */           if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
/*     */             
/* 271 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 272 */             if (arguments != null && arguments.length > 0)
/*     */             {
/*     */               
/* 275 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 276 */                 Class<?> t = arguments[0];
/* 277 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 278 */                 snode.setListType(t);
/* 279 */               } else if (valueNode.getTag().equals(Tag.SET)) {
/* 280 */                 Class<?> t = arguments[0];
/* 281 */                 MappingNode mnode = (MappingNode)valueNode;
/* 282 */                 mnode.setOnlyKeyType(t);
/* 283 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 284 */               } else if (property.getType().isAssignableFrom(Map.class)) {
/* 285 */                 Class<?> ketType = arguments[0];
/* 286 */                 Class<?> valueType = arguments[1];
/* 287 */                 MappingNode mnode = (MappingNode)valueNode;
/* 288 */                 mnode.setTypes(ketType, valueType);
/* 289 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 297 */           Object value = Constructor.this.constructObject(valueNode);
/*     */           
/* 299 */           if ((property.getType() == float.class || property.getType() == Float.class) && 
/* 300 */             value instanceof Double) {
/* 301 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */ 
/*     */           
/* 305 */           property.set(object, value);
/* 306 */         } catch (Exception e) {
/* 307 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 312 */       return object;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException {
/* 317 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     private Construct getConstructor(Node node) {
/* 330 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 331 */       node.setType(cl);
/*     */       
/* 333 */       Construct constructor = Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 334 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/* 338 */       Object result = null;
/*     */       try {
/* 340 */         result = getConstructor(node).construct(node);
/* 341 */       } catch (ConstructorException e) {
/* 342 */         throw e;
/* 343 */       } catch (Exception e) {
/* 344 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/*     */       
/* 347 */       return result;
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 352 */         getConstructor(node).construct2ndStep(node, object);
/* 353 */       } catch (Exception e) {
/* 354 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node nnode) {
/*     */       Object result;
/* 367 */       ScalarNode node = (ScalarNode)nnode;
/* 368 */       Class<?> type = node.getType();
/*     */       
/* 370 */       if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class.isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class.isAssignableFrom(type) || Tag.BINARY.equals(node.getTag()) || Calendar.class.isAssignableFrom(type)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 376 */         result = constructStandardJavaInstance(type, node);
/*     */       } else {
/*     */         Object argument;
/* 379 */         Constructor[] arrayOfConstructor = (Constructor[])type.getConstructors();
/* 380 */         int oneArgCount = 0;
/* 381 */         Constructor<?> javaConstructor = null;
/* 382 */         for (Constructor<?> c : arrayOfConstructor) {
/* 383 */           if ((c.getParameterTypes()).length == 1) {
/* 384 */             oneArgCount++;
/* 385 */             javaConstructor = c;
/*     */           } 
/*     */         } 
/*     */         
/* 389 */         if (javaConstructor == null)
/* 390 */           throw new YAMLException("No single argument constructor found for " + type); 
/* 391 */         if (oneArgCount == 1) {
/* 392 */           argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */           
/* 401 */           argument = Constructor.this.constructScalar(node);
/*     */           try {
/* 403 */             javaConstructor = type.getConstructor(new Class[] { String.class });
/* 404 */           } catch (Exception e) {
/* 405 */             throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e.getMessage(), e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 411 */           result = javaConstructor.newInstance(new Object[] { argument });
/* 412 */         } catch (Exception e) {
/* 413 */           throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 418 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Object constructStandardJavaInstance(Class<String> type, ScalarNode node) {
/*     */       Object result;
/* 425 */       if (type == String.class) {
/* 426 */         Construct stringConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 427 */         result = stringConstructor.construct((Node)node);
/* 428 */       } else if (type == Boolean.class || type == boolean.class) {
/* 429 */         Construct boolConstructor = Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 430 */         result = boolConstructor.construct((Node)node);
/* 431 */       } else if (type == Character.class || type == char.class) {
/* 432 */         Construct charConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 433 */         String ch = (String)charConstructor.construct((Node)node);
/* 434 */         if (ch.length() == 0)
/* 435 */         { result = null; }
/* 436 */         else { if (ch.length() != 1) {
/* 437 */             throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
/*     */           }
/*     */           
/* 440 */           result = Character.valueOf(ch.charAt(0)); }
/*     */       
/* 442 */       } else if (Date.class.isAssignableFrom(type)) {
/* 443 */         Construct dateConstructor = Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 444 */         Date date = (Date)dateConstructor.construct((Node)node);
/* 445 */         if (type == Date.class) {
/* 446 */           result = date;
/*     */         } else {
/*     */           try {
/* 449 */             Constructor<?> constr = type.getConstructor(new Class[] { long.class });
/* 450 */             result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/* 451 */           } catch (Exception e) {
/* 452 */             throw new YAMLException("Cannot construct: '" + type + "'");
/*     */           } 
/*     */         } 
/* 455 */       } else if (type == Float.class || type == Double.class || type == float.class || type == double.class || type == BigDecimal.class) {
/*     */         
/* 457 */         if (type == BigDecimal.class) {
/* 458 */           result = new BigDecimal(node.getValue());
/*     */         } else {
/* 460 */           Construct doubleConstructor = Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 461 */           result = doubleConstructor.construct((Node)node);
/* 462 */           if (type == Float.class || type == float.class) {
/* 463 */             result = new Float(((Double)result).doubleValue());
/*     */           }
/*     */         } 
/* 466 */       } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == byte.class || type == short.class || type == int.class || type == long.class) {
/*     */ 
/*     */         
/* 469 */         Construct intConstructor = Constructor.this.yamlConstructors.get(Tag.INT);
/* 470 */         result = intConstructor.construct((Node)node);
/* 471 */         if (type == Byte.class || type == byte.class) {
/* 472 */           result = new Byte(result.toString());
/* 473 */         } else if (type == Short.class || type == short.class) {
/* 474 */           result = new Short(result.toString());
/* 475 */         } else if (type == Integer.class || type == int.class) {
/* 476 */           result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 477 */         } else if (type == Long.class || type == long.class) {
/* 478 */           result = new Long(result.toString());
/*     */         } else {
/*     */           
/* 481 */           result = new BigInteger(result.toString());
/*     */         } 
/* 483 */       } else if (Enum.class.isAssignableFrom(type)) {
/* 484 */         String enumValueName = node.getValue();
/*     */         try {
/* 486 */           result = Enum.valueOf(type, enumValueName);
/* 487 */         } catch (Exception ex) {
/* 488 */           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type.getName());
/*     */         }
/*     */       
/* 491 */       } else if (Calendar.class.isAssignableFrom(type)) {
/* 492 */         SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 493 */         contr.construct((Node)node);
/* 494 */         result = contr.getCalendar();
/* 495 */       } else if (Number.class.isAssignableFrom(type)) {
/* 496 */         SafeConstructor.ConstructYamlNumber contr = new SafeConstructor.ConstructYamlNumber(Constructor.this);
/* 497 */         result = contr.construct((Node)node);
/*     */       } else {
/* 499 */         throw new YAMLException("Unsupported class: " + type);
/*     */       } 
/* 501 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 512 */       SequenceNode snode = (SequenceNode)node;
/* 513 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 514 */         if (node.isTwoStepsConstruction()) {
/* 515 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 517 */         return Constructor.this.constructSet(snode);
/*     */       } 
/* 519 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 520 */         if (node.isTwoStepsConstruction()) {
/* 521 */           return Constructor.this.createDefaultList(snode.getValue().size());
/*     */         }
/* 523 */         return Constructor.this.constructSequence(snode);
/*     */       } 
/* 525 */       if (node.getType().isArray()) {
/* 526 */         if (node.isTwoStepsConstruction()) {
/* 527 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 529 */         return Constructor.this.constructArray(snode);
/*     */       } 
/*     */ 
/*     */       
/* 533 */       List<Constructor<?>> possibleConstructors = new ArrayList<Constructor<?>>(snode.getValue().size());
/*     */       
/* 535 */       for (Constructor<?> constructor : node.getType().getConstructors()) {
/*     */         
/* 537 */         if (snode.getValue().size() == (constructor.getParameterTypes()).length) {
/* 538 */           possibleConstructors.add(constructor);
/*     */         }
/*     */       } 
/* 541 */       if (!possibleConstructors.isEmpty()) {
/* 542 */         if (possibleConstructors.size() == 1) {
/* 543 */           Object[] arrayOfObject = new Object[snode.getValue().size()];
/* 544 */           Constructor<?> c = possibleConstructors.get(0);
/* 545 */           int i = 0;
/* 546 */           for (Node argumentNode : snode.getValue()) {
/* 547 */             Class<?> type = c.getParameterTypes()[i];
/*     */             
/* 549 */             argumentNode.setType(type);
/* 550 */             arrayOfObject[i++] = Constructor.this.constructObject(argumentNode);
/*     */           } 
/*     */           
/*     */           try {
/* 554 */             return c.newInstance(arrayOfObject);
/* 555 */           } catch (Exception e) {
/* 556 */             throw new YAMLException(e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 561 */         List<Object> argumentList = (List)Constructor.this.constructSequence(snode);
/* 562 */         Class<?>[] parameterTypes = new Class[argumentList.size()];
/* 563 */         int index = 0;
/* 564 */         for (Object parameter : argumentList) {
/* 565 */           parameterTypes[index] = parameter.getClass();
/* 566 */           index++;
/*     */         } 
/*     */         
/* 569 */         for (Constructor<?> c : possibleConstructors) {
/* 570 */           Class<?>[] argTypes = c.getParameterTypes();
/* 571 */           boolean foundConstructor = true;
/* 572 */           for (int i = 0; i < argTypes.length; i++) {
/* 573 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 574 */               foundConstructor = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 578 */           if (foundConstructor) {
/*     */             try {
/* 580 */               return c.newInstance(argumentList.toArray());
/* 581 */             } catch (Exception e) {
/* 582 */               throw new YAMLException(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 587 */       throw new YAMLException("No suitable constructor with " + String.valueOf(snode.getValue().size()) + " arguments found for " + node.getType());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
/* 595 */       if (!clazz.isPrimitive()) {
/* 596 */         return (Class)clazz;
/*     */       }
/* 598 */       if (clazz == int.class) {
/* 599 */         return (Class)Integer.class;
/*     */       }
/* 601 */       if (clazz == float.class) {
/* 602 */         return (Class)Float.class;
/*     */       }
/* 604 */       if (clazz == double.class) {
/* 605 */         return (Class)Double.class;
/*     */       }
/* 607 */       if (clazz == boolean.class) {
/* 608 */         return (Class)Boolean.class;
/*     */       }
/* 610 */       if (clazz == long.class) {
/* 611 */         return (Class)Long.class;
/*     */       }
/* 613 */       if (clazz == char.class) {
/* 614 */         return (Class)Character.class;
/*     */       }
/* 616 */       if (clazz == short.class) {
/* 617 */         return (Class)Short.class;
/*     */       }
/* 619 */       if (clazz == byte.class) {
/* 620 */         return (Class)Byte.class;
/*     */       }
/* 622 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 627 */       SequenceNode snode = (SequenceNode)node;
/* 628 */       if (List.class.isAssignableFrom(node.getType())) {
/* 629 */         List<Object> list = (List<Object>)object;
/* 630 */         Constructor.this.constructSequenceStep2(snode, list);
/* 631 */       } else if (node.getType().isArray()) {
/* 632 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 634 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 640 */     Class<? extends Object> classForTag = this.typeTags.get(node.getTag());
/* 641 */     if (classForTag == null) {
/* 642 */       Class<?> cl; String name = node.getTag().getClassName();
/*     */       
/*     */       try {
/* 645 */         cl = getClassForName(name);
/* 646 */       } catch (ClassNotFoundException e) {
/* 647 */         throw new YAMLException("Class not found: " + name);
/*     */       } 
/* 649 */       this.typeTags.put(node.getTag(), cl);
/* 650 */       return cl;
/*     */     } 
/* 652 */     return classForTag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 657 */     return Class.forName(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */