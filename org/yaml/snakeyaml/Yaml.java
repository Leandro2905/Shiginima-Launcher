/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.emitter.Emitter;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.parser.ParserImpl;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
/*     */ import org.yaml.snakeyaml.serializer.Serializer;
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
/*     */ 
/*     */ public class Yaml
/*     */ {
/*     */   protected final Resolver resolver;
/*     */   private String name;
/*     */   protected BaseConstructor constructor;
/*     */   protected Representer representer;
/*     */   protected DumperOptions dumperOptions;
/*     */   
/*     */   public Yaml() {
/*  63 */     this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(LoaderOptions loaderOptions) {
/*  70 */     this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(DumperOptions dumperOptions) {
/*  80 */     this((BaseConstructor)new Constructor(), new Representer(), dumperOptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Representer representer) {
/*  91 */     this((BaseConstructor)new Constructor(), representer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor) {
/* 102 */     this(constructor, new Representer());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer) {
/* 115 */     this(constructor, representer, new DumperOptions());
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
/*     */   public Yaml(Representer representer, DumperOptions dumperOptions) {
/* 128 */     this((BaseConstructor)new Constructor(), representer, dumperOptions, new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions) {
/* 143 */     this(constructor, representer, dumperOptions, new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver) {
/* 161 */     if (!constructor.isExplicitPropertyUtils()) {
/* 162 */       constructor.setPropertyUtils(representer.getPropertyUtils());
/* 163 */     } else if (!representer.isExplicitPropertyUtils()) {
/* 164 */       representer.setPropertyUtils(constructor.getPropertyUtils());
/*     */     } 
/* 166 */     this.constructor = constructor;
/* 167 */     representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
/* 168 */     representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
/* 169 */     representer.getPropertyUtils().setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
/*     */     
/* 171 */     representer.setTimeZone(dumperOptions.getTimeZone());
/* 172 */     this.representer = representer;
/* 173 */     this.dumperOptions = dumperOptions;
/* 174 */     this.resolver = resolver;
/* 175 */     this.name = "Yaml:" + System.identityHashCode(this);
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
/*     */   
/*     */   public Yaml(BaseConstructor constructor, LoaderOptions loaderOptions, Representer representer, DumperOptions dumperOptions, Resolver resolver) {
/* 196 */     this(constructor, representer, dumperOptions, resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dump(Object data) {
/* 207 */     List<Object> list = new ArrayList(1);
/* 208 */     list.add(data);
/* 209 */     return dumpAll(list.iterator());
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
/*     */   public Node represent(Object data) {
/* 222 */     return this.representer.represent(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAll(Iterator<? extends Object> data) {
/* 233 */     StringWriter buffer = new StringWriter();
/* 234 */     dumpAll(data, buffer);
/* 235 */     return buffer.toString();
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
/*     */   public void dump(Object data, Writer output) {
/* 247 */     List<Object> list = new ArrayList(1);
/* 248 */     list.add(data);
/* 249 */     dumpAll(list.iterator(), output);
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
/*     */   public void dumpAll(Iterator<? extends Object> data, Writer output) {
/* 262 */     dumpAll(data, output, this.dumperOptions.getExplicitRoot());
/*     */   }
/*     */   
/*     */   private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
/* 266 */     Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, rootTag);
/*     */     
/*     */     try {
/* 269 */       serializer.open();
/* 270 */       while (data.hasNext()) {
/* 271 */         Node node = this.representer.represent(data.next());
/* 272 */         serializer.serialize(node);
/*     */       } 
/* 274 */       serializer.close();
/* 275 */     } catch (IOException e) {
/* 276 */       throw new YAMLException(e);
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
/*     */   
/*     */   public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle) {
/* 321 */     DumperOptions.FlowStyle oldStyle = this.representer.getDefaultFlowStyle();
/* 322 */     if (flowStyle != null) {
/* 323 */       this.representer.setDefaultFlowStyle(flowStyle);
/*     */     }
/* 325 */     List<Object> list = new ArrayList(1);
/* 326 */     list.add(data);
/* 327 */     StringWriter buffer = new StringWriter();
/* 328 */     dumpAll(list.iterator(), buffer, rootTag);
/* 329 */     this.representer.setDefaultFlowStyle(oldStyle);
/* 330 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAsMap(Object data) {
/* 353 */     return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
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
/*     */   public List<Event> serialize(Node data) {
/* 365 */     SilentEmitter emitter = new SilentEmitter();
/*     */     
/* 367 */     Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, this.dumperOptions.getExplicitRoot());
/*     */     
/*     */     try {
/* 370 */       serializer.open();
/* 371 */       serializer.serialize(data);
/* 372 */       serializer.close();
/* 373 */     } catch (IOException e) {
/* 374 */       throw new YAMLException(e);
/*     */     } 
/* 376 */     return emitter.getEvents();
/*     */   }
/*     */   
/*     */   private static class SilentEmitter implements Emitable {
/* 380 */     private List<Event> events = new ArrayList<Event>(100);
/*     */     
/*     */     public List<Event> getEvents() {
/* 383 */       return this.events;
/*     */     }
/*     */     
/*     */     public void emit(Event event) throws IOException {
/* 387 */       this.events.add(event);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SilentEmitter() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object load(String yaml) {
/* 400 */     return loadFromReader(new StreamReader(yaml), Object.class);
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
/*     */   public Object load(InputStream io) {
/* 412 */     return loadFromReader(new StreamReader((Reader)new UnicodeReader(io)), Object.class);
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
/*     */   public Object load(Reader io) {
/* 424 */     return loadFromReader(new StreamReader(io), Object.class);
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
/*     */   public <T> T loadAs(Reader io, Class<T> type) {
/* 441 */     return (T)loadFromReader(new StreamReader(io), type);
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
/*     */   public <T> T loadAs(String yaml, Class<T> type) {
/* 458 */     return (T)loadFromReader(new StreamReader(yaml), type);
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
/*     */   public <T> T loadAs(InputStream input, Class<T> type) {
/* 475 */     return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(input)), type);
/*     */   }
/*     */   
/*     */   private Object loadFromReader(StreamReader sreader, Class<?> type) {
/* 479 */     Composer composer = new Composer((Parser)new ParserImpl(sreader), this.resolver);
/* 480 */     this.constructor.setComposer(composer);
/* 481 */     return this.constructor.getSingleData(type);
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
/*     */   public Iterable<Object> loadAll(Reader yaml) {
/* 494 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 495 */     this.constructor.setComposer(composer);
/* 496 */     Iterator<Object> result = new Iterator() {
/*     */         public boolean hasNext() {
/* 498 */           return Yaml.this.constructor.checkData();
/*     */         }
/*     */         
/*     */         public Object next() {
/* 502 */           return Yaml.this.constructor.getData();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 506 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 509 */     return new YamlIterable(result);
/*     */   }
/*     */   
/*     */   private static class YamlIterable implements Iterable<Object> {
/*     */     private Iterator<Object> iterator;
/*     */     
/*     */     public YamlIterable(Iterator<Object> iterator) {
/* 516 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 520 */       return this.iterator;
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
/*     */   public Iterable<Object> loadAll(String yaml) {
/* 535 */     return loadAll(new StringReader(yaml));
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
/*     */   public Iterable<Object> loadAll(InputStream yaml) {
/* 548 */     return loadAll((Reader)new UnicodeReader(yaml));
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
/*     */   public Node compose(Reader yaml) {
/* 562 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 563 */     this.constructor.setComposer(composer);
/* 564 */     return composer.getSingleNode();
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
/*     */   public Iterable<Node> composeAll(Reader yaml) {
/* 577 */     final Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 578 */     this.constructor.setComposer(composer);
/* 579 */     Iterator<Node> result = new Iterator<Node>() {
/*     */         public boolean hasNext() {
/* 581 */           return composer.checkNode();
/*     */         }
/*     */         
/*     */         public Node next() {
/* 585 */           return composer.getNode();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 589 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 592 */     return new NodeIterable(result);
/*     */   }
/*     */   
/*     */   private static class NodeIterable implements Iterable<Node> {
/*     */     private Iterator<Node> iterator;
/*     */     
/*     */     public NodeIterable(Iterator<Node> iterator) {
/* 599 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Node> iterator() {
/* 603 */       return this.iterator;
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
/*     */   public void addImplicitResolver(String tag, Pattern regexp, String first) {
/* 622 */     addImplicitResolver(new Tag(tag), regexp, first);
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
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/* 638 */     this.resolver.addImplicitResolver(tag, regexp, first);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 643 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 654 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 664 */     this.name = name;
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
/*     */   public Iterable<Event> parse(Reader yaml) {
/* 676 */     final ParserImpl parser = new ParserImpl(new StreamReader(yaml));
/* 677 */     Iterator<Event> result = new Iterator<Event>() {
/*     */         public boolean hasNext() {
/* 679 */           return (parser.peekEvent() != null);
/*     */         }
/*     */         
/*     */         public Event next() {
/* 683 */           return parser.getEvent();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 687 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 690 */     return new EventIterable(result);
/*     */   }
/*     */   
/*     */   private static class EventIterable implements Iterable<Event> {
/*     */     private Iterator<Event> iterator;
/*     */     
/*     */     public EventIterable(Iterator<Event> iterator) {
/* 697 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Event> iterator() {
/* 701 */       return this.iterator;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 706 */     this.constructor.getPropertyUtils().setBeanAccess(beanAccess);
/* 707 */     this.representer.getPropertyUtils().setBeanAccess(beanAccess);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Loader loader) {
/* 715 */     this(loader, new Dumper(new DumperOptions()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Loader loader, Dumper dumper) {
/* 722 */     this(loader, dumper, new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Loader loader, Dumper dumper, Resolver resolver) {
/* 729 */     this(loader.constructor, dumper.representer, dumper.options, resolver);
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
/*     */   public Yaml(Dumper dumper) {
/* 741 */     this((BaseConstructor)new Constructor(), dumper.representer, dumper.options);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\Yaml.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */