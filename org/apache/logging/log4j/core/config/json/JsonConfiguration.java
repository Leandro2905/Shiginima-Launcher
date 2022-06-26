/*     */ package org.apache.logging.log4j.core.config.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationMonitor;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.FileConfigurationMonitor;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ public class JsonConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*  49 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*  50 */   private final List<Status> status = new ArrayList<Status>();
/*     */   private JsonNode root;
/*     */   
/*     */   public JsonConfiguration(ConfigurationSource configSource) {
/*  54 */     super(configSource);
/*  55 */     File configFile = configSource.getFile();
/*     */     try {
/*     */       byte[] buffer;
/*  58 */       InputStream configStream = configSource.getInputStream();
/*     */       try {
/*  60 */         buffer = toByteArray(configStream);
/*     */       } finally {
/*  62 */         configStream.close();
/*     */       } 
/*  64 */       InputStream is = new ByteArrayInputStream(buffer);
/*  65 */       this.root = getObjectMapper().readTree(is);
/*  66 */       if (this.root.size() == 1) {
/*  67 */         for (JsonNode node : this.root) {
/*  68 */           this.root = node;
/*     */         }
/*     */       }
/*  71 */       processAttributes(this.rootNode, this.root);
/*  72 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*     */       
/*  74 */       for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
/*  75 */         String key = entry.getKey();
/*  76 */         String value = getStrSubstitutor().replace(entry.getValue());
/*  77 */         if ("status".equalsIgnoreCase(key)) {
/*  78 */           statusConfig.withStatus(value); continue;
/*  79 */         }  if ("dest".equalsIgnoreCase(key)) {
/*  80 */           statusConfig.withDestination(value); continue;
/*  81 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/*  82 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/*  83 */         }  if ("verbose".equalsIgnoreCase(entry.getKey())) {
/*  84 */           statusConfig.withVerbosity(value); continue;
/*  85 */         }  if ("packages".equalsIgnoreCase(key)) {
/*  86 */           String[] packages = value.split(Patterns.COMMA_SEPARATOR);
/*  87 */           for (String p : packages)
/*  88 */             PluginManager.addPackage(p);  continue;
/*     */         } 
/*  90 */         if ("name".equalsIgnoreCase(key)) {
/*  91 */           setName(value); continue;
/*  92 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/*  93 */           int interval = Integer.parseInt(value);
/*  94 */           if (interval > 0 && configFile != null)
/*  95 */             this.monitor = (ConfigurationMonitor)new FileConfigurationMonitor(this, configFile, this.listeners, interval);  continue;
/*     */         } 
/*  97 */         if ("advertiser".equalsIgnoreCase(key)) {
/*  98 */           createAdvertiser(value, configSource, buffer, "application/json");
/*     */         }
/*     */       } 
/* 101 */       statusConfig.initialize();
/* 102 */       if (getName() == null) {
/* 103 */         setName(configSource.getLocation());
/*     */       }
/* 105 */     } catch (Exception ex) {
/* 106 */       LOGGER.error("Error parsing {}", new Object[] { configSource.getLocation(), ex });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ObjectMapper getObjectMapper() {
/* 111 */     return (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 116 */     Iterator<Map.Entry<String, JsonNode>> iter = this.root.fields();
/* 117 */     List<Node> children = this.rootNode.getChildren();
/* 118 */     while (iter.hasNext()) {
/* 119 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 120 */       JsonNode n = entry.getValue();
/* 121 */       if (n.isObject()) {
/* 122 */         LOGGER.debug("Processing node for object {}", new Object[] { entry.getKey() });
/* 123 */         children.add(constructNode(entry.getKey(), this.rootNode, n)); continue;
/* 124 */       }  if (n.isArray()) {
/* 125 */         LOGGER.error("Arrays are not supported at the root configuration.");
/*     */       }
/*     */     } 
/* 128 */     LOGGER.debug("Completed parsing configuration");
/* 129 */     if (this.status.size() > 0) {
/* 130 */       for (Status s : this.status) {
/* 131 */         LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 139 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 140 */       if (source == null) {
/* 141 */         return null;
/*     */       }
/* 143 */       return (Configuration)new JsonConfiguration(source);
/* 144 */     } catch (IOException ex) {
/* 145 */       LOGGER.error("Cannot locate file {}", new Object[] { getConfigurationSource(), ex });
/*     */       
/* 147 */       return null;
/*     */     } 
/*     */   } private Node constructNode(String name, Node parent, JsonNode jsonNode) {
/*     */     String t;
/* 151 */     PluginType<?> type = this.pluginManager.getPluginType(name);
/* 152 */     Node node = new Node(parent, name, type);
/* 153 */     processAttributes(node, jsonNode);
/* 154 */     Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields();
/* 155 */     List<Node> children = node.getChildren();
/* 156 */     while (iter.hasNext()) {
/* 157 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 158 */       JsonNode n = entry.getValue();
/* 159 */       if (n.isArray() || n.isObject()) {
/* 160 */         if (type == null) {
/* 161 */           this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
/*     */         }
/* 163 */         if (n.isArray()) {
/* 164 */           LOGGER.debug("Processing node for array {}", new Object[] { entry.getKey() });
/* 165 */           for (int i = 0; i < n.size(); i++) {
/* 166 */             String pluginType = getType(n.get(i), entry.getKey());
/* 167 */             PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
/* 168 */             Node item = new Node(node, entry.getKey(), entryType);
/* 169 */             processAttributes(item, n.get(i));
/* 170 */             if (pluginType.equals(entry.getKey())) {
/* 171 */               LOGGER.debug("Processing {}[{}]", new Object[] { entry.getKey(), Integer.valueOf(i) });
/*     */             } else {
/* 173 */               LOGGER.debug("Processing {} {}[{}]", new Object[] { pluginType, entry.getKey(), Integer.valueOf(i) });
/*     */             } 
/* 175 */             Iterator<Map.Entry<String, JsonNode>> itemIter = n.get(i).fields();
/* 176 */             List<Node> itemChildren = item.getChildren();
/* 177 */             while (itemIter.hasNext()) {
/* 178 */               Map.Entry<String, JsonNode> itemEntry = itemIter.next();
/* 179 */               if (((JsonNode)itemEntry.getValue()).isObject()) {
/* 180 */                 LOGGER.debug("Processing node for object {}", new Object[] { itemEntry.getKey() });
/* 181 */                 itemChildren.add(constructNode(itemEntry.getKey(), item, itemEntry.getValue())); continue;
/* 182 */               }  if (((JsonNode)itemEntry.getValue()).isArray()) {
/* 183 */                 JsonNode array = itemEntry.getValue();
/* 184 */                 String entryName = itemEntry.getKey();
/* 185 */                 LOGGER.debug("Processing array for object {}", new Object[] { entryName });
/* 186 */                 for (int j = 0; j < array.size(); j++) {
/* 187 */                   itemChildren.add(constructNode(entryName, item, array.get(j)));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 192 */             children.add(item);
/*     */           }  continue;
/*     */         } 
/* 195 */         LOGGER.debug("Processing node for object {}", new Object[] { entry.getKey() });
/* 196 */         children.add(constructNode(entry.getKey(), node, n));
/*     */         continue;
/*     */       } 
/* 199 */       LOGGER.debug("Node {} is of type {}", new Object[] { entry.getKey(), n.getNodeType() });
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 204 */     if (type == null) {
/* 205 */       t = "null";
/*     */     } else {
/* 207 */       t = type.getElementName() + ':' + type.getPluginClass();
/*     */     } 
/*     */     
/* 210 */     String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
/*     */     
/* 212 */     LOGGER.debug("Returning {} with parent {} of type {}", new Object[] { node.getName(), p, t });
/* 213 */     return node;
/*     */   }
/*     */   
/*     */   private String getType(JsonNode node, String name) {
/* 217 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 218 */     while (iter.hasNext()) {
/* 219 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 220 */       if (((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 221 */         JsonNode n = entry.getValue();
/* 222 */         if (n.isValueNode()) {
/* 223 */           return n.asText();
/*     */         }
/*     */       } 
/*     */     } 
/* 227 */     return name;
/*     */   }
/*     */   
/*     */   private void processAttributes(Node parent, JsonNode node) {
/* 231 */     Map<String, String> attrs = parent.getAttributes();
/* 232 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 233 */     while (iter.hasNext()) {
/* 234 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 235 */       if (!((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 236 */         JsonNode n = entry.getValue();
/* 237 */         if (n.isValueNode()) {
/* 238 */           attrs.put(entry.getKey(), n.asText());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 246 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 253 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final JsonNode node;
/*     */     
/*     */     private final String name;
/*     */     private final JsonConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, JsonNode node, JsonConfiguration.ErrorType errorType) {
/* 265 */       this.name = name;
/* 266 */       this.node = node;
/* 267 */       this.errorType = errorType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\json\JsonConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */