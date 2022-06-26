/*     */ package org.yaml.snakeyaml.composer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.NodeEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Composer
/*     */ {
/*     */   private final Parser parser;
/*     */   private final Resolver resolver;
/*     */   private final Map<String, Node> anchors;
/*     */   private final Set<Node> recursiveNodes;
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver) {
/*  55 */     this.parser = parser;
/*  56 */     this.resolver = resolver;
/*  57 */     this.anchors = new HashMap<String, Node>();
/*  58 */     this.recursiveNodes = new HashSet<Node>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNode() {
/*  68 */     if (this.parser.checkEvent(Event.ID.StreamStart)) {
/*  69 */       this.parser.getEvent();
/*     */     }
/*     */     
/*  72 */     return !this.parser.checkEvent(Event.ID.StreamEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  83 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/*  84 */       return composeDocument();
/*     */     }
/*  86 */     return null;
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
/*     */   public Node getSingleNode() {
/* 101 */     this.parser.getEvent();
/*     */     
/* 103 */     Node document = null;
/* 104 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 105 */       document = composeDocument();
/*     */     }
/*     */     
/* 108 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 109 */       Event event = this.parser.getEvent();
/* 110 */       throw new ComposerException("expected a single document in the stream", document.getStartMark(), "but found another document", event.getStartMark());
/*     */     } 
/*     */ 
/*     */     
/* 114 */     this.parser.getEvent();
/* 115 */     return document;
/*     */   }
/*     */ 
/*     */   
/*     */   private Node composeDocument() {
/* 120 */     this.parser.getEvent();
/*     */     
/* 122 */     Node node = composeNode(null);
/*     */     
/* 124 */     this.parser.getEvent();
/* 125 */     this.anchors.clear();
/* 126 */     this.recursiveNodes.clear();
/* 127 */     return node;
/*     */   }
/*     */   
/*     */   private Node composeNode(Node parent) {
/* 131 */     this.recursiveNodes.add(parent);
/* 132 */     if (this.parser.checkEvent(Event.ID.Alias)) {
/* 133 */       AliasEvent aliasEvent = (AliasEvent)this.parser.getEvent();
/* 134 */       String str = aliasEvent.getAnchor();
/* 135 */       if (!this.anchors.containsKey(str)) {
/* 136 */         throw new ComposerException(null, null, "found undefined alias " + str, aliasEvent.getStartMark());
/*     */       }
/*     */       
/* 139 */       Node result = this.anchors.get(str);
/* 140 */       if (this.recursiveNodes.remove(result)) {
/* 141 */         result.setTwoStepsConstruction(true);
/*     */       }
/* 143 */       return result;
/*     */     } 
/* 145 */     NodeEvent event = (NodeEvent)this.parser.peekEvent();
/* 146 */     String anchor = null;
/* 147 */     anchor = event.getAnchor();
/*     */     
/* 149 */     Node node = null;
/* 150 */     if (this.parser.checkEvent(Event.ID.Scalar)) {
/* 151 */       node = composeScalarNode(anchor);
/* 152 */     } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
/* 153 */       node = composeSequenceNode(anchor);
/*     */     } else {
/* 155 */       node = composeMappingNode(anchor);
/*     */     } 
/* 157 */     this.recursiveNodes.remove(parent);
/* 158 */     return node;
/*     */   }
/*     */   private Node composeScalarNode(String anchor) {
/*     */     Tag nodeTag;
/* 162 */     ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
/* 163 */     String tag = ev.getTag();
/* 164 */     boolean resolved = false;
/*     */     
/* 166 */     if (tag == null || tag.equals("!")) {
/* 167 */       nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
/*     */       
/* 169 */       resolved = true;
/*     */     } else {
/* 171 */       nodeTag = new Tag(tag);
/*     */     } 
/* 173 */     ScalarNode scalarNode = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getStyle());
/*     */     
/* 175 */     if (anchor != null) {
/* 176 */       this.anchors.put(anchor, scalarNode);
/*     */     }
/* 178 */     return (Node)scalarNode;
/*     */   }
/*     */   private Node composeSequenceNode(String anchor) {
/*     */     Tag nodeTag;
/* 182 */     SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
/* 183 */     String tag = startEvent.getTag();
/*     */     
/* 185 */     boolean resolved = false;
/* 186 */     if (tag == null || tag.equals("!")) {
/* 187 */       nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
/* 188 */       resolved = true;
/*     */     } else {
/* 190 */       nodeTag = new Tag(tag);
/*     */     } 
/* 192 */     ArrayList<Node> children = new ArrayList<Node>();
/* 193 */     SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 195 */     if (anchor != null) {
/* 196 */       this.anchors.put(anchor, node);
/*     */     }
/* 198 */     int index = 0;
/* 199 */     while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
/* 200 */       children.add(composeNode((Node)node));
/* 201 */       index++;
/*     */     } 
/* 203 */     Event endEvent = this.parser.getEvent();
/* 204 */     node.setEndMark(endEvent.getEndMark());
/* 205 */     return (Node)node;
/*     */   }
/*     */   private Node composeMappingNode(String anchor) {
/*     */     Tag nodeTag;
/* 209 */     MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
/* 210 */     String tag = startEvent.getTag();
/*     */     
/* 212 */     boolean resolved = false;
/* 213 */     if (tag == null || tag.equals("!")) {
/* 214 */       nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
/* 215 */       resolved = true;
/*     */     } else {
/* 217 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 220 */     List<NodeTuple> children = new ArrayList<NodeTuple>();
/* 221 */     MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 223 */     if (anchor != null) {
/* 224 */       this.anchors.put(anchor, node);
/*     */     }
/* 226 */     while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
/* 227 */       Node itemKey = composeNode((Node)node);
/* 228 */       if (itemKey.getTag().equals(Tag.MERGE)) {
/* 229 */         node.setMerged(true);
/*     */       }
/* 231 */       Node itemValue = composeNode((Node)node);
/* 232 */       children.add(new NodeTuple(itemKey, itemValue));
/*     */     } 
/* 234 */     Event endEvent = this.parser.getEvent();
/* 235 */     node.setEndMark(endEvent.getEndMark());
/* 236 */     return (Node)node;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */