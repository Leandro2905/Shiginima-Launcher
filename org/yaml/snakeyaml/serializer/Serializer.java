/*     */ package org.yaml.snakeyaml.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public final class Serializer
/*     */ {
/*     */   private final Emitable emitter;
/*     */   private final Resolver resolver;
/*     */   private boolean explicitStart;
/*     */   private boolean explicitEnd;
/*     */   private DumperOptions.Version useVersion;
/*     */   private Map<String, String> useTags;
/*     */   private Set<Node> serializedNodes;
/*     */   private Map<Node, String> anchors;
/*     */   private int lastAnchorId;
/*     */   private Boolean closed;
/*     */   private Tag explicitRoot;
/*     */   
/*     */   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
/*  65 */     this.emitter = emitter;
/*  66 */     this.resolver = resolver;
/*  67 */     this.explicitStart = opts.isExplicitStart();
/*  68 */     this.explicitEnd = opts.isExplicitEnd();
/*  69 */     if (opts.getVersion() != null) {
/*  70 */       this.useVersion = opts.getVersion();
/*     */     }
/*  72 */     this.useTags = opts.getTags();
/*  73 */     this.serializedNodes = new HashSet<Node>();
/*  74 */     this.anchors = new HashMap<Node, String>();
/*  75 */     this.lastAnchorId = 0;
/*  76 */     this.closed = null;
/*  77 */     this.explicitRoot = rootTag;
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/*  81 */     if (this.closed == null)
/*  82 */     { this.emitter.emit((Event)new StreamStartEvent(null, null));
/*  83 */       this.closed = Boolean.FALSE; }
/*  84 */     else { if (Boolean.TRUE.equals(this.closed)) {
/*  85 */         throw new SerializerException("serializer is closed");
/*     */       }
/*  87 */       throw new SerializerException("serializer is already opened"); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  92 */     if (this.closed == null)
/*  93 */       throw new SerializerException("serializer is not opened"); 
/*  94 */     if (!Boolean.TRUE.equals(this.closed)) {
/*  95 */       this.emitter.emit((Event)new StreamEndEvent(null, null));
/*  96 */       this.closed = Boolean.TRUE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serialize(Node node) throws IOException {
/* 101 */     if (this.closed == null)
/* 102 */       throw new SerializerException("serializer is not opened"); 
/* 103 */     if (this.closed.booleanValue()) {
/* 104 */       throw new SerializerException("serializer is closed");
/*     */     }
/* 106 */     this.emitter.emit((Event)new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
/*     */     
/* 108 */     anchorNode(node);
/* 109 */     if (this.explicitRoot != null) {
/* 110 */       node.setTag(this.explicitRoot);
/*     */     }
/* 112 */     serializeNode(node, null);
/* 113 */     this.emitter.emit((Event)new DocumentEndEvent(null, null, this.explicitEnd));
/* 114 */     this.serializedNodes.clear();
/* 115 */     this.anchors.clear();
/* 116 */     this.lastAnchorId = 0;
/*     */   }
/*     */   
/*     */   private void anchorNode(Node node) {
/* 120 */     if (node.getNodeId() == NodeId.anchor) {
/* 121 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 123 */     if (this.anchors.containsKey(node)) {
/* 124 */       String anchor = this.anchors.get(node);
/* 125 */       if (null == anchor) {
/* 126 */         anchor = generateAnchor();
/* 127 */         this.anchors.put(node, anchor);
/*     */       } 
/*     */     } else {
/* 130 */       SequenceNode seqNode; List<Node> list; MappingNode mnode; List<NodeTuple> map; this.anchors.put(node, null);
/* 131 */       switch (node.getNodeId()) {
/*     */         case sequence:
/* 133 */           seqNode = (SequenceNode)node;
/* 134 */           list = seqNode.getValue();
/* 135 */           for (Node item : list) {
/* 136 */             anchorNode(item);
/*     */           }
/*     */           break;
/*     */         case mapping:
/* 140 */           mnode = (MappingNode)node;
/* 141 */           map = mnode.getValue();
/* 142 */           for (NodeTuple object : map) {
/* 143 */             Node key = object.getKeyNode();
/* 144 */             Node value = object.getValueNode();
/* 145 */             anchorNode(key);
/* 146 */             anchorNode(value);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String generateAnchor() {
/* 154 */     this.lastAnchorId++;
/* 155 */     NumberFormat format = NumberFormat.getNumberInstance();
/* 156 */     format.setMinimumIntegerDigits(3);
/* 157 */     format.setMaximumFractionDigits(0);
/* 158 */     format.setGroupingUsed(false);
/* 159 */     String anchorId = format.format(this.lastAnchorId);
/* 160 */     return "id" + anchorId;
/*     */   }
/*     */   
/*     */   private void serializeNode(Node node, Node parent) throws IOException {
/* 164 */     if (node.getNodeId() == NodeId.anchor) {
/* 165 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 167 */     String tAlias = this.anchors.get(node);
/* 168 */     if (this.serializedNodes.contains(node)) {
/* 169 */       this.emitter.emit((Event)new AliasEvent(tAlias, null, null));
/*     */     } else {
/* 171 */       ScalarNode scalarNode; Tag detectedTag, defaultTag; ImplicitTuple tuple; ScalarEvent event; SequenceNode seqNode; boolean implicitS; int indexCounter; List<Node> list; this.serializedNodes.add(node);
/* 172 */       switch (node.getNodeId()) {
/*     */         case scalar:
/* 174 */           scalarNode = (ScalarNode)node;
/* 175 */           detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
/* 176 */           defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
/* 177 */           tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
/*     */           
/* 179 */           event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getStyle());
/*     */           
/* 181 */           this.emitter.emit((Event)event);
/*     */           return;
/*     */         case sequence:
/* 184 */           seqNode = (SequenceNode)node;
/* 185 */           implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
/*     */           
/* 187 */           this.emitter.emit((Event)new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
/*     */           
/* 189 */           indexCounter = 0;
/* 190 */           list = seqNode.getValue();
/* 191 */           for (Node item : list) {
/* 192 */             serializeNode(item, node);
/* 193 */             indexCounter++;
/*     */           } 
/* 195 */           this.emitter.emit((Event)new SequenceEndEvent(null, null));
/*     */           return;
/*     */       } 
/* 198 */       Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
/* 199 */       boolean implicitM = node.getTag().equals(implicitTag);
/* 200 */       this.emitter.emit((Event)new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node).getFlowStyle()));
/*     */       
/* 202 */       MappingNode mnode = (MappingNode)node;
/* 203 */       List<NodeTuple> map = mnode.getValue();
/* 204 */       for (NodeTuple row : map) {
/* 205 */         Node key = row.getKeyNode();
/* 206 */         Node value = row.getValueNode();
/* 207 */         serializeNode(key, (Node)mnode);
/* 208 */         serializeNode(value, (Node)mnode);
/*     */       } 
/* 210 */       this.emitter.emit((Event)new MappingEndEvent(null, null));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */