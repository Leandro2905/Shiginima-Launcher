/*     */ package org.apache.logging.log4j.core.config.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
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
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class XmlConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*     */   private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*  65 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */   
/*     */   private static final String LOG4J_XSD = "Log4j-config.xsd";
/*  68 */   private final List<Status> status = new ArrayList<Status>();
/*     */ 
/*     */   
/*     */   private Element rootElement;
/*     */ 
/*     */   
/*     */   private boolean strict;
/*     */   
/*     */   private String schema;
/*     */ 
/*     */   
/*     */   static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
/*  80 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  81 */     factory.setNamespaceAware(true);
/*  82 */     enableXInclude(factory);
/*  83 */     return factory.newDocumentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void enableXInclude(DocumentBuilderFactory factory) {
/*     */     try {
/*  95 */       factory.setXIncludeAware(true);
/*  96 */     } catch (UnsupportedOperationException e) {
/*  97 */       LOGGER.warn("The DocumentBuilderFactory does not support XInclude: {}", new Object[] { factory, e });
/*  98 */     } catch (AbstractMethodError err) {
/*  99 */       LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: {}", new Object[] { factory, err });
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 104 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 105 */     } catch (ParserConfigurationException e) {
/* 106 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}].", new Object[] { factory, "http://apache.org/xml/features/xinclude/fixup-base-uris", e });
/*     */     }
/* 108 */     catch (AbstractMethodError err) {
/* 109 */       LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: {}", new Object[] { factory, err });
/*     */     } 
/*     */     try {
/* 112 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/* 113 */     } catch (ParserConfigurationException e) {
/* 114 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}].", new Object[] { factory, "http://apache.org/xml/features/xinclude/fixup-language", e });
/*     */     }
/* 116 */     catch (AbstractMethodError err) {
/* 117 */       LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: {}", new Object[] { factory, err });
/*     */     } 
/*     */   }
/*     */   
/*     */   public XmlConfiguration(ConfigurationSource configSource) {
/* 122 */     super(configSource);
/* 123 */     File configFile = configSource.getFile();
/* 124 */     byte[] buffer = null;
/*     */     
/*     */     try {
/* 127 */       InputStream configStream = configSource.getInputStream();
/*     */       try {
/* 129 */         buffer = toByteArray(configStream);
/*     */       } finally {
/* 131 */         configStream.close();
/*     */       } 
/* 133 */       InputSource source = new InputSource(new ByteArrayInputStream(buffer));
/* 134 */       source.setSystemId(configSource.getLocation());
/* 135 */       Document document = newDocumentBuilder().parse(source);
/* 136 */       this.rootElement = document.getDocumentElement();
/* 137 */       Map<String, String> attrs = processAttributes(this.rootNode, this.rootElement);
/* 138 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*     */       
/* 140 */       for (Map.Entry<String, String> entry : attrs.entrySet()) {
/* 141 */         String key = entry.getKey();
/* 142 */         String value = getStrSubstitutor().replace(entry.getValue());
/* 143 */         if ("status".equalsIgnoreCase(key)) {
/* 144 */           statusConfig.withStatus(value); continue;
/* 145 */         }  if ("dest".equalsIgnoreCase(key)) {
/* 146 */           statusConfig.withDestination(value); continue;
/* 147 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/* 148 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/* 149 */         }  if ("verbose".equalsIgnoreCase(key)) {
/* 150 */           statusConfig.withVerbosity(value); continue;
/* 151 */         }  if ("packages".equalsIgnoreCase(key)) {
/* 152 */           String[] packages = value.split(Patterns.COMMA_SEPARATOR);
/* 153 */           for (String p : packages)
/* 154 */             PluginManager.addPackage(p);  continue;
/*     */         } 
/* 156 */         if ("name".equalsIgnoreCase(key)) {
/* 157 */           setName(value); continue;
/* 158 */         }  if ("strict".equalsIgnoreCase(key)) {
/* 159 */           this.strict = Boolean.parseBoolean(value); continue;
/* 160 */         }  if ("schema".equalsIgnoreCase(key)) {
/* 161 */           this.schema = value; continue;
/* 162 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/* 163 */           int interval = Integer.parseInt(value);
/* 164 */           if (interval > 0 && configFile != null)
/* 165 */             this.monitor = (ConfigurationMonitor)new FileConfigurationMonitor(this, configFile, this.listeners, interval);  continue;
/*     */         } 
/* 167 */         if ("advertiser".equalsIgnoreCase(key)) {
/* 168 */           createAdvertiser(value, configSource, buffer, "text/xml");
/*     */         }
/*     */       } 
/* 171 */       statusConfig.initialize();
/* 172 */     } catch (SAXException domEx) {
/* 173 */       LOGGER.error("Error parsing " + configSource.getLocation(), domEx);
/* 174 */     } catch (IOException ioe) {
/* 175 */       LOGGER.error("Error parsing " + configSource.getLocation(), ioe);
/* 176 */     } catch (ParserConfigurationException pex) {
/* 177 */       LOGGER.error("Error parsing " + configSource.getLocation(), pex);
/*     */     } 
/* 179 */     if (this.strict && this.schema != null && buffer != null) {
/* 180 */       InputStream is = null;
/*     */       try {
/* 182 */         is = Loader.getResourceAsStream(this.schema, XmlConfiguration.class.getClassLoader());
/* 183 */       } catch (Exception ex) {
/* 184 */         LOGGER.error("Unable to access schema {}", new Object[] { this.schema, ex });
/*     */       } 
/* 186 */       if (is != null) {
/* 187 */         Source src = new StreamSource(is, "Log4j-config.xsd");
/* 188 */         SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 189 */         Schema schema = null;
/*     */         try {
/* 191 */           schema = factory.newSchema(src);
/* 192 */         } catch (SAXException ex) {
/* 193 */           LOGGER.error("Error parsing Log4j schema", ex);
/*     */         } 
/* 195 */         if (schema != null) {
/* 196 */           Validator validator = schema.newValidator();
/*     */           try {
/* 198 */             validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
/* 199 */           } catch (IOException ioe) {
/* 200 */             LOGGER.error("Error reading configuration for validation", ioe);
/* 201 */           } catch (SAXException ex) {
/* 202 */             LOGGER.error("Error validating configuration", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     if (getName() == null) {
/* 209 */       setName(configSource.getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 215 */     if (this.rootElement == null) {
/* 216 */       LOGGER.error("No logging configuration");
/*     */       return;
/*     */     } 
/* 219 */     constructHierarchy(this.rootNode, this.rootElement);
/* 220 */     if (this.status.size() > 0) {
/* 221 */       for (Status s : this.status) {
/* 222 */         LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
/*     */       }
/*     */       return;
/*     */     } 
/* 226 */     this.rootElement = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 232 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 233 */       if (source == null) {
/* 234 */         return null;
/*     */       }
/* 236 */       XmlConfiguration config = new XmlConfiguration(source);
/* 237 */       return (config.rootElement == null) ? null : (Configuration)config;
/* 238 */     } catch (IOException ex) {
/* 239 */       LOGGER.error("Cannot locate file " + getConfigurationSource(), ex);
/*     */       
/* 241 */       return null;
/*     */     } 
/*     */   }
/*     */   private void constructHierarchy(Node node, Element element) {
/* 245 */     processAttributes(node, element);
/* 246 */     StringBuilder buffer = new StringBuilder();
/* 247 */     NodeList list = element.getChildNodes();
/* 248 */     List<Node> children = node.getChildren();
/* 249 */     for (int i = 0; i < list.getLength(); i++) {
/* 250 */       Node w3cNode = list.item(i);
/* 251 */       if (w3cNode instanceof Element) {
/* 252 */         Element child = (Element)w3cNode;
/* 253 */         String name = getType(child);
/* 254 */         PluginType<?> type = this.pluginManager.getPluginType(name);
/* 255 */         Node childNode = new Node(node, name, type);
/* 256 */         constructHierarchy(childNode, child);
/* 257 */         if (type == null) {
/* 258 */           String value = childNode.getValue();
/* 259 */           if (!childNode.hasChildren() && value != null) {
/* 260 */             node.getAttributes().put(name, value);
/*     */           } else {
/* 262 */             this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
/*     */           } 
/*     */         } else {
/* 265 */           children.add(childNode);
/*     */         } 
/* 267 */       } else if (w3cNode instanceof Text) {
/* 268 */         Text data = (Text)w3cNode;
/* 269 */         buffer.append(data.getData());
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     String text = buffer.toString().trim();
/* 274 */     if (text.length() > 0 || (!node.hasChildren() && !node.isRoot())) {
/* 275 */       node.setValue(text);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getType(Element element) {
/* 280 */     if (this.strict) {
/* 281 */       NamedNodeMap attrs = element.getAttributes();
/* 282 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 283 */         Node w3cNode = attrs.item(i);
/* 284 */         if (w3cNode instanceof Attr) {
/* 285 */           Attr attr = (Attr)w3cNode;
/* 286 */           if (attr.getName().equalsIgnoreCase("type")) {
/* 287 */             String type = attr.getValue();
/* 288 */             attrs.removeNamedItem(attr.getName());
/* 289 */             return type;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 294 */     return element.getTagName();
/*     */   }
/*     */   
/*     */   private Map<String, String> processAttributes(Node node, Element element) {
/* 298 */     NamedNodeMap attrs = element.getAttributes();
/* 299 */     Map<String, String> attributes = node.getAttributes();
/*     */     
/* 301 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 302 */       Node w3cNode = attrs.item(i);
/* 303 */       if (w3cNode instanceof Attr) {
/* 304 */         Attr attr = (Attr)w3cNode;
/* 305 */         if (!attr.getName().equals("xml:base"))
/*     */         {
/*     */           
/* 308 */           attributes.put(attr.getName(), attr.getValue()); } 
/*     */       } 
/*     */     } 
/* 311 */     return attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 316 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 323 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final Element element;
/*     */     
/*     */     private final String name;
/*     */     private final XmlConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, Element element, XmlConfiguration.ErrorType errorType) {
/* 335 */       this.name = name;
/* 336 */       this.element = element;
/* 337 */       this.errorType = errorType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\xml\XmlConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */