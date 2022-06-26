/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
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
/*     */ 
/*     */ 
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*     */   public Representer() {
/*  43 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean implements Represent {
/*     */     public Node representData(Object data) {
/*     */       try {
/*  49 */         return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
/*  50 */       } catch (IntrospectionException e) {
/*  51 */         throw new YAMLException(e);
/*     */       } 
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
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/*  71 */     List<NodeTuple> value = new ArrayList<NodeTuple>(properties.size());
/*     */     
/*  73 */     Tag customTag = this.classTags.get(javaBean.getClass());
/*  74 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/*  76 */     MappingNode node = new MappingNode(tag, value, null);
/*  77 */     this.representedObjects.put(javaBean, node);
/*  78 */     boolean bestStyle = true;
/*  79 */     for (Property property : properties) {
/*  80 */       Object memberValue = property.get(javaBean);
/*  81 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/*     */       
/*  83 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/*     */       
/*  85 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/*  88 */       if (((ScalarNode)tuple.getKeyNode()).getStyle() != null) {
/*  89 */         bestStyle = false;
/*     */       }
/*  91 */       Node nodeValue = tuple.getValueNode();
/*  92 */       if (!(nodeValue instanceof ScalarNode) || ((ScalarNode)nodeValue).getStyle() != null) {
/*  93 */         bestStyle = false;
/*     */       }
/*  95 */       value.add(tuple);
/*     */     } 
/*  97 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/*  98 */       node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */     } else {
/* 100 */       node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */     } 
/* 102 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 121 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 123 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 125 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 127 */     if (propertyValue != null && !hasAlias) {
/* 128 */       NodeId nodeId = nodeValue.getNodeId();
/* 129 */       if (customTag == null) {
/* 130 */         if (nodeId == NodeId.scalar) {
/* 131 */           if (propertyValue instanceof Enum) {
/* 132 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/* 135 */           if (nodeId == NodeId.mapping && 
/* 136 */             property.getType() == propertyValue.getClass() && 
/* 137 */             !(propertyValue instanceof java.util.Map) && 
/* 138 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 139 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 144 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return new NodeTuple((Node)nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 166 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 171 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 172 */     if (arguments != null) {
/* 173 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         Iterable<Object> memberList;
/* 175 */         Class<? extends Object> t = (Class)arguments[0];
/* 176 */         SequenceNode snode = (SequenceNode)node;
/*     */         
/* 178 */         if (object.getClass().isArray()) {
/* 179 */           memberList = Arrays.asList((Object[])object);
/*     */         } else {
/*     */           
/* 182 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 184 */         Iterator<Object> iter = memberList.iterator();
/* 185 */         for (Node childNode : snode.getValue()) {
/* 186 */           Object member = iter.next();
/* 187 */           if (member != null && 
/* 188 */             t.equals(member.getClass()) && 
/* 189 */             childNode.getNodeId() == NodeId.mapping) {
/* 190 */             childNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 194 */       } else if (object instanceof Set) {
/* 195 */         Class<?> t = arguments[0];
/* 196 */         MappingNode mnode = (MappingNode)node;
/* 197 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 198 */         Set<?> set = (Set)object;
/* 199 */         for (Object member : set) {
/* 200 */           NodeTuple tuple = iter.next();
/* 201 */           Node keyNode = tuple.getKeyNode();
/* 202 */           if (t.equals(member.getClass()) && 
/* 203 */             keyNode.getNodeId() == NodeId.mapping) {
/* 204 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 208 */       } else if (object instanceof java.util.Map) {
/* 209 */         Class<?> keyType = arguments[0];
/* 210 */         Class<?> valueType = arguments[1];
/* 211 */         MappingNode mnode = (MappingNode)node;
/* 212 */         for (NodeTuple tuple : mnode.getValue()) {
/* 213 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 214 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 224 */     Tag tag = node.getTag();
/* 225 */     if (tag.matches(type)) {
/* 226 */       if (Enum.class.isAssignableFrom(type)) {
/* 227 */         node.setTag(Tag.STR);
/*     */       } else {
/* 229 */         node.setTag(Tag.MAP);
/*     */       } 
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
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) throws IntrospectionException {
/* 244 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */