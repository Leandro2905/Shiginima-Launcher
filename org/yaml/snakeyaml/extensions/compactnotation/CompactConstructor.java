/*     */ package org.yaml.snakeyaml.extensions.compactnotation;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.constructor.Construct;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
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
/*     */ public class CompactConstructor
/*     */   extends Constructor
/*     */ {
/*  41 */   private static final Pattern GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");
/*     */   
/*  43 */   private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
/*  44 */   private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");
/*     */   
/*     */   private Construct compactConstruct;
/*     */   
/*     */   protected Object constructCompactFormat(ScalarNode node, CompactData data) {
/*     */     try {
/*  50 */       Object obj = createInstance(node, data);
/*  51 */       Map<String, Object> properties = new HashMap<String, Object>(data.getProperties());
/*  52 */       setProperties(obj, properties);
/*  53 */       return obj;
/*  54 */     } catch (Exception e) {
/*  55 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
/*  60 */     Class<?> clazz = getClassForName(data.getPrefix());
/*  61 */     Class<?>[] args = new Class[data.getArguments().size()];
/*  62 */     for (int i = 0; i < args.length; i++)
/*     */     {
/*  64 */       args[i] = String.class;
/*     */     }
/*  66 */     Constructor<?> c = clazz.getDeclaredConstructor(args);
/*  67 */     c.setAccessible(true);
/*  68 */     return c.newInstance(data.getArguments().toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
/*  73 */     if (data == null) {
/*  74 */       throw new NullPointerException("Data for Compact Object Notation cannot be null.");
/*     */     }
/*  76 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*  77 */       String key = entry.getKey();
/*  78 */       Property property = getPropertyUtils().getProperty(bean.getClass(), key);
/*     */       try {
/*  80 */         property.set(bean, entry.getValue());
/*  81 */       } catch (IllegalArgumentException e) {
/*  82 */         throw new YAMLException("Cannot set property='" + key + "' with value='" + data.get(key) + "' (" + data.get(key).getClass() + ") in " + bean);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompactData getCompactData(String scalar) {
/*  89 */     if (!scalar.endsWith(")")) {
/*  90 */       return null;
/*     */     }
/*  92 */     if (scalar.indexOf('(') < 0) {
/*  93 */       return null;
/*     */     }
/*  95 */     Matcher m = FIRST_PATTERN.matcher(scalar);
/*  96 */     if (m.matches()) {
/*  97 */       String tag = m.group(1).trim();
/*  98 */       String content = m.group(3);
/*  99 */       CompactData data = new CompactData(tag);
/* 100 */       if (content.length() == 0)
/* 101 */         return data; 
/* 102 */       String[] names = content.split("\\s*,\\s*");
/* 103 */       for (int i = 0; i < names.length; i++) {
/* 104 */         String section = names[i];
/* 105 */         if (section.indexOf('=') < 0) {
/* 106 */           data.getArguments().add(section);
/*     */         } else {
/* 108 */           Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
/* 109 */           if (sm.matches()) {
/* 110 */             String name = sm.group(1);
/* 111 */             String value = sm.group(2).trim();
/* 112 */             data.getProperties().put(name, value);
/*     */           } else {
/* 114 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/* 118 */       return data;
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   private Construct getCompactConstruct() {
/* 124 */     if (this.compactConstruct == null) {
/* 125 */       this.compactConstruct = createCompactConstruct();
/*     */     }
/* 127 */     return this.compactConstruct;
/*     */   }
/*     */   
/*     */   protected Construct createCompactConstruct() {
/* 131 */     return (Construct)new ConstructCompactObject();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Construct getConstructor(Node node) {
/* 136 */     if (node instanceof MappingNode) {
/* 137 */       MappingNode mnode = (MappingNode)node;
/* 138 */       List<NodeTuple> list = mnode.getValue();
/* 139 */       if (list.size() == 1) {
/* 140 */         NodeTuple tuple = list.get(0);
/* 141 */         Node key = tuple.getKeyNode();
/* 142 */         if (key instanceof ScalarNode) {
/* 143 */           ScalarNode scalar = (ScalarNode)key;
/* 144 */           if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 145 */             return getCompactConstruct();
/*     */           }
/*     */         } 
/*     */       } 
/* 149 */     } else if (node instanceof ScalarNode) {
/* 150 */       ScalarNode scalar = (ScalarNode)node;
/* 151 */       if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 152 */         return getCompactConstruct();
/*     */       }
/*     */     } 
/* 155 */     return super.getConstructor(node);
/*     */   }
/*     */   public class ConstructCompactObject extends Constructor.ConstructMapping { public ConstructCompactObject() {
/* 158 */       super(CompactConstructor.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 163 */       MappingNode mnode = (MappingNode)node;
/* 164 */       NodeTuple nodeTuple = mnode.getValue().iterator().next();
/*     */       
/* 166 */       Node valueNode = nodeTuple.getValueNode();
/*     */       
/* 168 */       if (valueNode instanceof MappingNode) {
/* 169 */         valueNode.setType(object.getClass());
/* 170 */         constructJavaBean2ndStep((MappingNode)valueNode, object);
/*     */       } else {
/*     */         
/* 173 */         CompactConstructor.this.applySequence(object, CompactConstructor.this.constructSequence((SequenceNode)valueNode));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object construct(Node node) {
/* 182 */       ScalarNode tmpNode = null;
/* 183 */       if (node instanceof MappingNode) {
/*     */         
/* 185 */         MappingNode mnode = (MappingNode)node;
/* 186 */         NodeTuple nodeTuple = mnode.getValue().iterator().next();
/* 187 */         node.setTwoStepsConstruction(true);
/* 188 */         tmpNode = (ScalarNode)nodeTuple.getKeyNode();
/*     */       } else {
/*     */         
/* 191 */         tmpNode = (ScalarNode)node;
/*     */       } 
/*     */       
/* 194 */       CompactData data = CompactConstructor.this.getCompactData(tmpNode.getValue());
/* 195 */       if (data == null) {
/* 196 */         return CompactConstructor.this.constructScalar(tmpNode);
/*     */       }
/* 198 */       return CompactConstructor.this.constructCompactFormat(tmpNode, data);
/*     */     } }
/*     */ 
/*     */   
/*     */   protected void applySequence(Object bean, List<?> value) {
/*     */     try {
/* 204 */       Property property = getPropertyUtils().getProperty(bean.getClass(), getSequencePropertyName(bean.getClass()));
/*     */       
/* 206 */       property.set(bean, value);
/* 207 */     } catch (Exception e) {
/* 208 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSequencePropertyName(Class<?> bean) throws IntrospectionException {
/* 219 */     Set<Property> properties = getPropertyUtils().getProperties(bean);
/* 220 */     for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext(); ) {
/* 221 */       Property property = iterator.next();
/* 222 */       if (!List.class.isAssignableFrom(property.getType())) {
/* 223 */         iterator.remove();
/*     */       }
/*     */     } 
/* 226 */     if (properties.size() == 0)
/* 227 */       throw new YAMLException("No list property found in " + bean); 
/* 228 */     if (properties.size() > 1) {
/* 229 */       throw new YAMLException("Many list properties found in " + bean + "; Please override getSequencePropertyName() to specify which property to use.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 234 */     return ((Property)properties.iterator().next()).getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */