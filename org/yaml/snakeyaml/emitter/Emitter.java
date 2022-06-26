/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
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
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*   64 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
/*      */   
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   68 */   private static final char[] SPACE = new char[] { ' ' };
/*      */   
/*      */   static {
/*   71 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(false), "0");
/*   72 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   73 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   74 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   75 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   76 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   77 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   78 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   79 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*   80 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*   81 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*   82 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*   83 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*   84 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*   85 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */   }
/*      */   
/*   88 */   private static final Map<String, String> DEFAULT_TAG_PREFIXES = new LinkedHashMap<String, String>();
/*      */   static {
/*   90 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*   91 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final Writer stream;
/*      */ 
/*      */   
/*      */   private final ArrayStack<EmitterState> states;
/*      */ 
/*      */   
/*      */   private EmitterState state;
/*      */ 
/*      */   
/*      */   private final Queue<Event> events;
/*      */ 
/*      */   
/*      */   private Event event;
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private Boolean canonical;
/*      */   
/*      */   private Boolean prettyFlow;
/*      */   
/*      */   private boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private char[] bestLineBreak;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private Character style;
/*      */   
/*      */   private DumperOptions options;
/*      */ 
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts) {
/*  156 */     this.stream = stream;
/*      */ 
/*      */     
/*  159 */     this.states = new ArrayStack(100);
/*  160 */     this.state = new ExpectStreamStart();
/*      */     
/*  162 */     this.events = new ArrayBlockingQueue<Event>(100);
/*  163 */     this.event = null;
/*      */     
/*  165 */     this.indents = new ArrayStack(10);
/*  166 */     this.indent = null;
/*      */     
/*  168 */     this.flowLevel = 0;
/*      */     
/*  170 */     this.mappingContext = false;
/*  171 */     this.simpleKeyContext = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     this.column = 0;
/*  180 */     this.whitespace = true;
/*  181 */     this.indention = true;
/*      */ 
/*      */     
/*  184 */     this.openEnded = false;
/*      */ 
/*      */     
/*  187 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  188 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  189 */     this.allowUnicode = opts.isAllowUnicode();
/*  190 */     this.bestIndent = 2;
/*  191 */     if (opts.getIndent() > 1 && opts.getIndent() < 10) {
/*  192 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  194 */     this.bestWidth = 80;
/*  195 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  196 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  198 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*      */ 
/*      */     
/*  201 */     this.tagPrefixes = new LinkedHashMap<String, String>();
/*      */ 
/*      */     
/*  204 */     this.preparedAnchor = null;
/*  205 */     this.preparedTag = null;
/*      */ 
/*      */     
/*  208 */     this.analysis = null;
/*  209 */     this.style = null;
/*  210 */     this.options = opts;
/*      */   }
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  214 */     this.events.add(event);
/*  215 */     while (!needMoreEvents()) {
/*  216 */       this.event = this.events.poll();
/*  217 */       this.state.expect();
/*  218 */       this.event = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreEvents() {
/*  225 */     if (this.events.isEmpty()) {
/*  226 */       return true;
/*      */     }
/*  228 */     Event event = this.events.peek();
/*  229 */     if (event instanceof DocumentStartEvent)
/*  230 */       return needEvents(1); 
/*  231 */     if (event instanceof SequenceStartEvent)
/*  232 */       return needEvents(2); 
/*  233 */     if (event instanceof MappingStartEvent) {
/*  234 */       return needEvents(3);
/*      */     }
/*  236 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean needEvents(int count) {
/*  241 */     int level = 0;
/*  242 */     Iterator<Event> iter = this.events.iterator();
/*  243 */     iter.next();
/*  244 */     while (iter.hasNext()) {
/*  245 */       Event event = iter.next();
/*  246 */       if (event instanceof DocumentStartEvent || event instanceof CollectionStartEvent) {
/*  247 */         level++;
/*  248 */       } else if (event instanceof DocumentEndEvent || event instanceof org.yaml.snakeyaml.events.CollectionEndEvent) {
/*  249 */         level--;
/*  250 */       } else if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  251 */         level = -1;
/*      */       } 
/*  253 */       if (level < 0) {
/*  254 */         return false;
/*      */       }
/*      */     } 
/*  257 */     return (this.events.size() < count + 1);
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  261 */     this.indents.push(this.indent);
/*  262 */     if (this.indent == null) {
/*  263 */       if (flow) {
/*  264 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  266 */         this.indent = Integer.valueOf(0);
/*      */       } 
/*  268 */     } else if (!indentless) {
/*  269 */       Emitter emitter = this; emitter.indent = Integer.valueOf(emitter.indent.intValue() + this.bestIndent);
/*      */     } 
/*      */   }
/*      */   
/*      */   private class ExpectStreamStart
/*      */     implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  279 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamStartEvent) {
/*  280 */         Emitter.this.writeStreamStart();
/*  281 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart();
/*      */       } else {
/*  283 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectNothing implements EmitterState {
/*      */     public void expect() throws IOException {
/*  290 */       throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
/*      */     }
/*      */     
/*      */     private ExpectNothing() {} }
/*      */   
/*      */   private class ExpectFirstDocumentStart implements EmitterState { private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  298 */       (new Emitter.ExpectDocumentStart(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectDocumentStart implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  306 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  310 */       if (Emitter.this.event instanceof DocumentStartEvent) {
/*  311 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  312 */         if ((ev.getVersion() != null || ev.getTags() != null) && Emitter.this.openEnded) {
/*  313 */           Emitter.this.writeIndicator("...", true, false, false);
/*  314 */           Emitter.this.writeIndent();
/*      */         } 
/*  316 */         if (ev.getVersion() != null) {
/*  317 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  318 */           Emitter.this.writeVersionDirective(versionText);
/*      */         } 
/*  320 */         Emitter.this.tagPrefixes = (Map)new LinkedHashMap<Object, Object>(Emitter.DEFAULT_TAG_PREFIXES);
/*  321 */         if (ev.getTags() != null) {
/*  322 */           Set<String> handles = new TreeSet<String>(ev.getTags().keySet());
/*  323 */           for (String handle : handles) {
/*  324 */             String prefix = (String)ev.getTags().get(handle);
/*  325 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  326 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  327 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  328 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           } 
/*      */         } 
/*  331 */         boolean implicit = (this.first && !ev.getExplicit() && !Emitter.this.canonical.booleanValue() && ev.getVersion() == null && (ev.getTags() == null || ev.getTags().isEmpty()) && !Emitter.this.checkEmptyDocument());
/*      */ 
/*      */ 
/*      */         
/*  335 */         if (!implicit) {
/*  336 */           Emitter.this.writeIndent();
/*  337 */           Emitter.this.writeIndicator("---", true, false, false);
/*  338 */           if (Emitter.this.canonical.booleanValue()) {
/*  339 */             Emitter.this.writeIndent();
/*      */           }
/*      */         } 
/*  342 */         Emitter.this.state = new Emitter.ExpectDocumentRoot();
/*  343 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  349 */         Emitter.this.writeStreamEnd();
/*  350 */         Emitter.this.state = new Emitter.ExpectNothing();
/*      */       } else {
/*  352 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  359 */       if (Emitter.this.event instanceof DocumentEndEvent) {
/*  360 */         Emitter.this.writeIndent();
/*  361 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  362 */           Emitter.this.writeIndicator("...", true, false, false);
/*  363 */           Emitter.this.writeIndent();
/*      */         } 
/*  365 */         Emitter.this.flushStream();
/*  366 */         Emitter.this.state = new Emitter.ExpectDocumentStart(false);
/*      */       } else {
/*  368 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  375 */       Emitter.this.states.push(new Emitter.ExpectDocumentEnd());
/*  376 */       Emitter.this.expectNode(true, false, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey) throws IOException {
/*  383 */     this.rootContext = root;
/*  384 */     this.mappingContext = mapping;
/*  385 */     this.simpleKeyContext = simpleKey;
/*  386 */     if (this.event instanceof org.yaml.snakeyaml.events.AliasEvent) {
/*  387 */       expectAlias();
/*  388 */     } else if (this.event instanceof ScalarEvent || this.event instanceof CollectionStartEvent) {
/*  389 */       processAnchor("&");
/*  390 */       processTag();
/*  391 */       if (this.event instanceof ScalarEvent) {
/*  392 */         expectScalar();
/*  393 */       } else if (this.event instanceof SequenceStartEvent) {
/*  394 */         if (this.flowLevel != 0 || this.canonical.booleanValue() || ((SequenceStartEvent)this.event).getFlowStyle().booleanValue() || checkEmptySequence()) {
/*      */           
/*  396 */           expectFlowSequence();
/*      */         } else {
/*  398 */           expectBlockSequence();
/*      */         }
/*      */       
/*  401 */       } else if (this.flowLevel != 0 || this.canonical.booleanValue() || ((MappingStartEvent)this.event).getFlowStyle().booleanValue() || checkEmptyMapping()) {
/*      */         
/*  403 */         expectFlowMapping();
/*      */       } else {
/*  405 */         expectBlockMapping();
/*      */       } 
/*      */     } else {
/*      */       
/*  409 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  414 */     if (((NodeEvent)this.event).getAnchor() == null) {
/*  415 */       throw new EmitterException("anchor is not specified for alias");
/*      */     }
/*  417 */     processAnchor("*");
/*  418 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  422 */     increaseIndent(true, false);
/*  423 */     processScalar();
/*  424 */     this.indent = (Integer)this.indents.pop();
/*  425 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowSequence() throws IOException {
/*  431 */     writeIndicator("[", true, true, false);
/*  432 */     this.flowLevel++;
/*  433 */     increaseIndent(true, false);
/*  434 */     if (this.prettyFlow.booleanValue()) {
/*  435 */       writeIndent();
/*      */     }
/*  437 */     this.state = new ExpectFirstFlowSequenceItem();
/*      */   }
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  442 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  443 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  444 */         Emitter.this.flowLevel--;
/*  445 */         Emitter.this.writeIndicator("]", false, false, false);
/*  446 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  448 */         if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  449 */           Emitter.this.writeIndent();
/*      */         }
/*  451 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem());
/*  452 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  459 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  460 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  461 */         Emitter.this.flowLevel--;
/*  462 */         if (Emitter.this.canonical.booleanValue()) {
/*  463 */           Emitter.this.writeIndicator(",", false, false, false);
/*  464 */           Emitter.this.writeIndent();
/*      */         } 
/*  466 */         Emitter.this.writeIndicator("]", false, false, false);
/*  467 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  468 */           Emitter.this.writeIndent();
/*      */         }
/*  470 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  472 */         Emitter.this.writeIndicator(",", false, false, false);
/*  473 */         if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  474 */           Emitter.this.writeIndent();
/*      */         }
/*  476 */         Emitter.this.states.push(new ExpectFlowSequenceItem());
/*  477 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowMapping() throws IOException {
/*  485 */     writeIndicator("{", true, true, false);
/*  486 */     this.flowLevel++;
/*  487 */     increaseIndent(true, false);
/*  488 */     if (this.prettyFlow.booleanValue()) {
/*  489 */       writeIndent();
/*      */     }
/*  491 */     this.state = new ExpectFirstFlowMappingKey();
/*      */   }
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  496 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  497 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  498 */         Emitter.this.flowLevel--;
/*  499 */         Emitter.this.writeIndicator("}", false, false, false);
/*  500 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  502 */         if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  503 */           Emitter.this.writeIndent();
/*      */         }
/*  505 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  506 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  507 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  509 */           Emitter.this.writeIndicator("?", true, false, false);
/*  510 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  511 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  519 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  520 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  521 */         Emitter.this.flowLevel--;
/*  522 */         if (Emitter.this.canonical.booleanValue()) {
/*  523 */           Emitter.this.writeIndicator(",", false, false, false);
/*  524 */           Emitter.this.writeIndent();
/*      */         } 
/*  526 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  527 */           Emitter.this.writeIndent();
/*      */         }
/*  529 */         Emitter.this.writeIndicator("}", false, false, false);
/*  530 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  532 */         Emitter.this.writeIndicator(",", false, false, false);
/*  533 */         if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  534 */           Emitter.this.writeIndent();
/*      */         }
/*  536 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  537 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  538 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  540 */           Emitter.this.writeIndicator("?", true, false, false);
/*  541 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  542 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  550 */       Emitter.this.writeIndicator(":", false, false, false);
/*  551 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  552 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  558 */       if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  559 */         Emitter.this.writeIndent();
/*      */       }
/*  561 */       Emitter.this.writeIndicator(":", true, false, false);
/*  562 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  563 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectBlockSequence() throws IOException {
/*  570 */     boolean indentless = (this.mappingContext && !this.indention);
/*  571 */     increaseIndent(false, indentless);
/*  572 */     this.state = new ExpectFirstBlockSequenceItem();
/*      */   }
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  577 */       (new Emitter.ExpectBlockSequenceItem(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockSequenceItem implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  585 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  589 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  590 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  591 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  593 */         Emitter.this.writeIndent();
/*  594 */         Emitter.this.writeIndicator("-", true, false, true);
/*  595 */         Emitter.this.states.push(new ExpectBlockSequenceItem(false));
/*  596 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void expectBlockMapping() throws IOException {
/*  603 */     increaseIndent(false, false);
/*  604 */     this.state = new ExpectFirstBlockMappingKey();
/*      */   }
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  609 */       (new Emitter.ExpectBlockMappingKey(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockMappingKey implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  617 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  621 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  622 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  623 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  625 */         Emitter.this.writeIndent();
/*  626 */         if (Emitter.this.checkSimpleKey()) {
/*  627 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue());
/*  628 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  630 */           Emitter.this.writeIndicator("?", true, false, true);
/*  631 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue());
/*  632 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  640 */       Emitter.this.writeIndicator(":", false, false, false);
/*  641 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  642 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  648 */       Emitter.this.writeIndent();
/*  649 */       Emitter.this.writeIndicator(":", true, false, true);
/*  650 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  651 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkEmptySequence() {
/*  658 */     return (this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.SequenceEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  662 */     return (this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.MappingEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  666 */     if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
/*  667 */       return false;
/*      */     }
/*  669 */     Event event = this.events.peek();
/*  670 */     if (event instanceof ScalarEvent) {
/*  671 */       ScalarEvent e = (ScalarEvent)event;
/*  672 */       return (e.getAnchor() == null && e.getTag() == null && e.getImplicit() != null && e.getValue().length() == 0);
/*      */     } 
/*      */     
/*  675 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  679 */     int length = 0;
/*  680 */     if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
/*  681 */       if (this.preparedAnchor == null) {
/*  682 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  684 */       length += this.preparedAnchor.length();
/*      */     } 
/*  686 */     String tag = null;
/*  687 */     if (this.event instanceof ScalarEvent) {
/*  688 */       tag = ((ScalarEvent)this.event).getTag();
/*  689 */     } else if (this.event instanceof CollectionStartEvent) {
/*  690 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     } 
/*  692 */     if (tag != null) {
/*  693 */       if (this.preparedTag == null) {
/*  694 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  696 */       length += this.preparedTag.length();
/*      */     } 
/*  698 */     if (this.event instanceof ScalarEvent) {
/*  699 */       if (this.analysis == null) {
/*  700 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  702 */       length += this.analysis.scalar.length();
/*      */     } 
/*  704 */     return (length < 128 && (this.event instanceof org.yaml.snakeyaml.events.AliasEvent || (this.event instanceof ScalarEvent && !this.analysis.empty && !this.analysis.multiline) || checkEmptySequence() || checkEmptyMapping()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAnchor(String indicator) throws IOException {
/*  712 */     NodeEvent ev = (NodeEvent)this.event;
/*  713 */     if (ev.getAnchor() == null) {
/*  714 */       this.preparedAnchor = null;
/*      */       return;
/*      */     } 
/*  717 */     if (this.preparedAnchor == null) {
/*  718 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  720 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  721 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  725 */     String tag = null;
/*  726 */     if (this.event instanceof ScalarEvent) {
/*  727 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  728 */       tag = ev.getTag();
/*  729 */       if (this.style == null) {
/*  730 */         this.style = chooseScalarStyle();
/*      */       }
/*  732 */       if ((!this.canonical.booleanValue() || tag == null) && ((this.style == null && ev.getImplicit().canOmitTagInPlainScalar()) || (this.style != null && ev.getImplicit().canOmitTagInNonPlainScalar()))) {
/*      */ 
/*      */         
/*  735 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*  738 */       if (ev.getImplicit().canOmitTagInPlainScalar() && tag == null) {
/*  739 */         tag = "!";
/*  740 */         this.preparedTag = null;
/*      */       } 
/*      */     } else {
/*  743 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  744 */       tag = ev.getTag();
/*  745 */       if ((!this.canonical.booleanValue() || tag == null) && ev.getImplicit()) {
/*  746 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*      */     } 
/*  750 */     if (tag == null) {
/*  751 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  753 */     if (this.preparedTag == null) {
/*  754 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  756 */     writeIndicator(this.preparedTag, true, false, false);
/*  757 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private Character chooseScalarStyle() {
/*  761 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  762 */     if (this.analysis == null) {
/*  763 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  765 */     if ((ev.getStyle() != null && ev.getStyle().charValue() == '"') || this.canonical.booleanValue()) {
/*  766 */       return Character.valueOf('"');
/*      */     }
/*  768 */     if (ev.getStyle() == null && ev.getImplicit().canOmitTagInPlainScalar() && (
/*  769 */       !this.simpleKeyContext || (!this.analysis.empty && !this.analysis.multiline)) && ((this.flowLevel != 0 && this.analysis.allowFlowPlain) || (this.flowLevel == 0 && this.analysis.allowBlockPlain)))
/*      */     {
/*  771 */       return null;
/*      */     }
/*      */     
/*  774 */     if (ev.getStyle() != null && (ev.getStyle().charValue() == '|' || ev.getStyle().charValue() == '>') && 
/*  775 */       this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.allowBlock) {
/*  776 */       return ev.getStyle();
/*      */     }
/*      */     
/*  779 */     if ((ev.getStyle() == null || ev.getStyle().charValue() == '\'') && 
/*  780 */       this.analysis.allowSingleQuoted && (!this.simpleKeyContext || !this.analysis.multiline)) {
/*  781 */       return Character.valueOf('\'');
/*      */     }
/*      */     
/*  784 */     return Character.valueOf('"');
/*      */   }
/*      */ 
/*      */   
/*      */   private void processScalar() throws IOException {
/*  789 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  790 */     if (this.analysis == null) {
/*  791 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  793 */     if (this.style == null) {
/*  794 */       this.style = chooseScalarStyle();
/*      */     }
/*      */     
/*  797 */     this.style = this.options.calculateScalarStyle(this.analysis, DumperOptions.ScalarStyle.createStyle(this.style)).getChar();
/*  798 */     boolean split = !this.simpleKeyContext;
/*  799 */     if (this.style == null) {
/*  800 */       writePlain(this.analysis.scalar, split);
/*      */     } else {
/*  802 */       switch (this.style.charValue()) {
/*      */         case '"':
/*  804 */           writeDoubleQuoted(this.analysis.scalar, split);
/*      */           break;
/*      */         case '\'':
/*  807 */           writeSingleQuoted(this.analysis.scalar, split);
/*      */           break;
/*      */         case '>':
/*  810 */           writeFolded(this.analysis.scalar);
/*      */           break;
/*      */         case '|':
/*  813 */           writeLiteral(this.analysis.scalar);
/*      */           break;
/*      */         default:
/*  816 */           throw new YAMLException("Unexpected style: " + this.style);
/*      */       } 
/*      */     } 
/*  819 */     this.analysis = null;
/*  820 */     this.style = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String prepareVersion(DumperOptions.Version version) {
/*  826 */     if (version.getArray()[0].intValue() != 1) {
/*  827 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/*  829 */     return version.getRepresentation();
/*      */   }
/*      */   
/*  832 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/*  835 */     if (handle.length() == 0)
/*  836 */       throw new EmitterException("tag handle must not be empty"); 
/*  837 */     if (handle.charAt(0) != '!' || handle.charAt(handle.length() - 1) != '!')
/*  838 */       throw new EmitterException("tag handle must start and end with '!': " + handle); 
/*  839 */     if (!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
/*  840 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/*  842 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/*  846 */     if (prefix.length() == 0) {
/*  847 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/*  849 */     StringBuilder chunks = new StringBuilder();
/*  850 */     int start = 0;
/*  851 */     int end = 0;
/*  852 */     if (prefix.charAt(0) == '!') {
/*  853 */       end = 1;
/*      */     }
/*  855 */     while (end < prefix.length()) {
/*  856 */       end++;
/*      */     }
/*  858 */     if (start < end) {
/*  859 */       chunks.append(prefix.substring(start, end));
/*      */     }
/*  861 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/*  865 */     if (tag.length() == 0) {
/*  866 */       throw new EmitterException("tag must not be empty");
/*      */     }
/*  868 */     if ("!".equals(tag)) {
/*  869 */       return tag;
/*      */     }
/*  871 */     String handle = null;
/*  872 */     String suffix = tag;
/*      */     
/*  874 */     for (String prefix : this.tagPrefixes.keySet()) {
/*  875 */       if (tag.startsWith(prefix) && ("!".equals(prefix) || prefix.length() < tag.length())) {
/*  876 */         handle = prefix;
/*      */       }
/*      */     } 
/*  879 */     if (handle != null) {
/*  880 */       suffix = tag.substring(handle.length());
/*  881 */       handle = this.tagPrefixes.get(handle);
/*      */     } 
/*      */     
/*  884 */     int end = suffix.length();
/*  885 */     String suffixText = (end > 0) ? suffix.substring(0, end) : "";
/*      */     
/*  887 */     if (handle != null) {
/*  888 */       return handle + suffixText;
/*      */     }
/*  890 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*  893 */   private static final Pattern ANCHOR_FORMAT = Pattern.compile("^[-_\\w]*$");
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/*  896 */     if (anchor.length() == 0) {
/*  897 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/*  899 */     if (!ANCHOR_FORMAT.matcher(anchor).matches()) {
/*  900 */       throw new EmitterException("invalid character in the anchor: " + anchor);
/*      */     }
/*  902 */     return anchor;
/*      */   }
/*      */ 
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar) {
/*  907 */     if (scalar.length() == 0) {
/*  908 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/*  911 */     boolean blockIndicators = false;
/*  912 */     boolean flowIndicators = false;
/*  913 */     boolean lineBreaks = false;
/*  914 */     boolean specialCharacters = false;
/*      */ 
/*      */     
/*  917 */     boolean leadingSpace = false;
/*  918 */     boolean leadingBreak = false;
/*  919 */     boolean trailingSpace = false;
/*  920 */     boolean trailingBreak = false;
/*  921 */     boolean breakSpace = false;
/*  922 */     boolean spaceBreak = false;
/*      */ 
/*      */     
/*  925 */     if (scalar.startsWith("---") || scalar.startsWith("...")) {
/*  926 */       blockIndicators = true;
/*  927 */       flowIndicators = true;
/*      */     } 
/*      */     
/*  930 */     boolean preceededByWhitespace = true;
/*  931 */     boolean followedByWhitespace = (scalar.length() == 1 || Constant.NULL_BL_T_LINEBR.has(scalar.charAt(1)));
/*      */ 
/*      */     
/*  934 */     boolean previousSpace = false;
/*      */ 
/*      */     
/*  937 */     boolean previousBreak = false;
/*      */     
/*  939 */     int index = 0;
/*      */     
/*  941 */     while (index < scalar.length()) {
/*  942 */       char ch = scalar.charAt(index);
/*      */       
/*  944 */       if (index == 0) {
/*      */         
/*  946 */         if ("#,[]{}&*!|>'\"%@`".indexOf(ch) != -1) {
/*  947 */           flowIndicators = true;
/*  948 */           blockIndicators = true;
/*      */         } 
/*  950 */         if (ch == '?' || ch == ':') {
/*  951 */           flowIndicators = true;
/*  952 */           if (followedByWhitespace) {
/*  953 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  956 */         if (ch == '-' && followedByWhitespace) {
/*  957 */           flowIndicators = true;
/*  958 */           blockIndicators = true;
/*      */         } 
/*      */       } else {
/*      */         
/*  962 */         if (",?[]{}".indexOf(ch) != -1) {
/*  963 */           flowIndicators = true;
/*      */         }
/*  965 */         if (ch == ':') {
/*  966 */           flowIndicators = true;
/*  967 */           if (followedByWhitespace) {
/*  968 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  971 */         if (ch == '#' && preceededByWhitespace) {
/*  972 */           flowIndicators = true;
/*  973 */           blockIndicators = true;
/*      */         } 
/*      */       } 
/*      */       
/*  977 */       boolean isLineBreak = Constant.LINEBR.has(ch);
/*  978 */       if (isLineBreak) {
/*  979 */         lineBreaks = true;
/*      */       }
/*  981 */       if (ch != '\n' && (' ' > ch || ch > '~')) {
/*  982 */         if ((ch == '' || (' ' <= ch && ch <= '퟿') || ('' <= ch && ch <= '�')) && ch != '﻿') {
/*      */ 
/*      */           
/*  985 */           if (!this.allowUnicode) {
/*  986 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/*  989 */           specialCharacters = true;
/*      */         } 
/*      */       }
/*      */       
/*  993 */       if (ch == ' ') {
/*  994 */         if (index == 0) {
/*  995 */           leadingSpace = true;
/*      */         }
/*  997 */         if (index == scalar.length() - 1) {
/*  998 */           trailingSpace = true;
/*      */         }
/* 1000 */         if (previousBreak) {
/* 1001 */           breakSpace = true;
/*      */         }
/* 1003 */         previousSpace = true;
/* 1004 */         previousBreak = false;
/* 1005 */       } else if (isLineBreak) {
/* 1006 */         if (index == 0) {
/* 1007 */           leadingBreak = true;
/*      */         }
/* 1009 */         if (index == scalar.length() - 1) {
/* 1010 */           trailingBreak = true;
/*      */         }
/* 1012 */         if (previousSpace) {
/* 1013 */           spaceBreak = true;
/*      */         }
/* 1015 */         previousSpace = false;
/* 1016 */         previousBreak = true;
/*      */       } else {
/* 1018 */         previousSpace = false;
/* 1019 */         previousBreak = false;
/*      */       } 
/*      */ 
/*      */       
/* 1023 */       index++;
/* 1024 */       preceededByWhitespace = (Constant.NULL_BL_T.has(ch) || isLineBreak);
/* 1025 */       followedByWhitespace = (index + 1 >= scalar.length() || Constant.NULL_BL_T.has(scalar.charAt(index + 1)) || isLineBreak);
/*      */     } 
/*      */ 
/*      */     
/* 1029 */     boolean allowFlowPlain = true;
/* 1030 */     boolean allowBlockPlain = true;
/* 1031 */     boolean allowSingleQuoted = true;
/* 1032 */     boolean allowBlock = true;
/*      */     
/* 1034 */     if (leadingSpace || leadingBreak || trailingSpace || trailingBreak) {
/* 1035 */       allowFlowPlain = allowBlockPlain = false;
/*      */     }
/*      */     
/* 1038 */     if (trailingSpace) {
/* 1039 */       allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1043 */     if (breakSpace) {
/* 1044 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
/*      */     }
/*      */ 
/*      */     
/* 1048 */     if (spaceBreak || specialCharacters) {
/* 1049 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1053 */     if (lineBreaks) {
/* 1054 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1057 */     if (flowIndicators) {
/* 1058 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1061 */     if (blockIndicators) {
/* 1062 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1065 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void flushStream() throws IOException {
/* 1072 */     this.stream.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeStreamStart() {}
/*      */ 
/*      */   
/*      */   void writeStreamEnd() throws IOException {
/* 1080 */     flushStream();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException {
/* 1085 */     if (!this.whitespace && needWhitespace) {
/* 1086 */       this.column++;
/* 1087 */       this.stream.write(SPACE);
/*      */     } 
/* 1089 */     this.whitespace = whitespace;
/* 1090 */     this.indention = (this.indention && indentation);
/* 1091 */     this.column += indicator.length();
/* 1092 */     this.openEnded = false;
/* 1093 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException {
/*      */     int indent;
/* 1098 */     if (this.indent != null) {
/* 1099 */       indent = this.indent.intValue();
/*      */     } else {
/* 1101 */       indent = 0;
/*      */     } 
/*      */     
/* 1104 */     if (!this.indention || this.column > indent || (this.column == indent && !this.whitespace)) {
/* 1105 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1108 */     if (this.column < indent) {
/* 1109 */       this.whitespace = true;
/* 1110 */       char[] data = new char[indent - this.column];
/* 1111 */       for (int i = 0; i < data.length; i++) {
/* 1112 */         data[i] = ' ';
/*      */       }
/* 1114 */       this.column = indent;
/* 1115 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1120 */     this.whitespace = true;
/* 1121 */     this.indention = true;
/* 1122 */     this.column = 0;
/* 1123 */     if (data == null) {
/* 1124 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1126 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1131 */     this.stream.write("%YAML ");
/* 1132 */     this.stream.write(versionText);
/* 1133 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText) throws IOException {
/* 1139 */     this.stream.write("%TAG ");
/* 1140 */     this.stream.write(handleText);
/* 1141 */     this.stream.write(SPACE);
/* 1142 */     this.stream.write(prefixText);
/* 1143 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException {
/* 1148 */     writeIndicator("'", true, false, false);
/* 1149 */     boolean spaces = false;
/* 1150 */     boolean breaks = false;
/* 1151 */     int start = 0, end = 0;
/*      */     
/* 1153 */     while (end <= text.length()) {
/* 1154 */       char ch = Character.MIN_VALUE;
/* 1155 */       if (end < text.length()) {
/* 1156 */         ch = text.charAt(end);
/*      */       }
/* 1158 */       if (spaces) {
/* 1159 */         if (ch == '\000' || ch != ' ') {
/* 1160 */           if (start + 1 == end && this.column > this.bestWidth && split && start != 0 && end != text.length()) {
/*      */             
/* 1162 */             writeIndent();
/*      */           } else {
/* 1164 */             int len = end - start;
/* 1165 */             this.column += len;
/* 1166 */             this.stream.write(text, start, len);
/*      */           } 
/* 1168 */           start = end;
/*      */         } 
/* 1170 */       } else if (breaks) {
/* 1171 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1172 */           if (text.charAt(start) == '\n') {
/* 1173 */             writeLineBreak(null);
/*      */           }
/* 1175 */           String data = text.substring(start, end);
/* 1176 */           for (char br : data.toCharArray()) {
/* 1177 */             if (br == '\n') {
/* 1178 */               writeLineBreak(null);
/*      */             } else {
/* 1180 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1183 */           writeIndent();
/* 1184 */           start = end;
/*      */         }
/*      */       
/* 1187 */       } else if (Constant.LINEBR.has(ch, "\000 '") && 
/* 1188 */         start < end) {
/* 1189 */         int len = end - start;
/* 1190 */         this.column += len;
/* 1191 */         this.stream.write(text, start, len);
/* 1192 */         start = end;
/*      */       } 
/*      */ 
/*      */       
/* 1196 */       if (ch == '\'') {
/* 1197 */         this.column += 2;
/* 1198 */         this.stream.write("''");
/* 1199 */         start = end + 1;
/*      */       } 
/* 1201 */       if (ch != '\000') {
/* 1202 */         spaces = (ch == ' ');
/* 1203 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1205 */       end++;
/*      */     } 
/* 1207 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1211 */     writeIndicator("\"", true, false, false);
/* 1212 */     int start = 0;
/* 1213 */     int end = 0;
/* 1214 */     while (end <= text.length()) {
/* 1215 */       Character ch = null;
/* 1216 */       if (end < text.length()) {
/* 1217 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1219 */       if (ch == null || "\"\\  ﻿".indexOf(ch.charValue()) != -1 || ' ' > ch.charValue() || ch.charValue() > '~') {
/*      */         
/* 1221 */         if (start < end) {
/* 1222 */           int len = end - start;
/* 1223 */           this.column += len;
/* 1224 */           this.stream.write(text, start, len);
/* 1225 */           start = end;
/*      */         } 
/* 1227 */         if (ch != null) {
/*      */           String data;
/* 1229 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1230 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch);
/* 1231 */           } else if (!this.allowUnicode || !StreamReader.isPrintable(ch.charValue())) {
/*      */ 
/*      */             
/* 1234 */             if (ch.charValue() <= 'ÿ') {
/* 1235 */               String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1236 */               data = "\\x" + s.substring(s.length() - 2);
/* 1237 */             } else if (ch.charValue() >= '?' && ch.charValue() <= '?') {
/* 1238 */               if (end + 1 < text.length()) {
/* 1239 */                 Character ch2 = Character.valueOf(text.charAt(++end));
/* 1240 */                 String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
/* 1241 */                 data = "\\U" + s.substring(s.length() - 8);
/*      */               } else {
/* 1243 */                 String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1244 */                 data = "\\u" + s.substring(s.length() - 4);
/*      */               } 
/*      */             } else {
/* 1247 */               String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1248 */               data = "\\u" + s.substring(s.length() - 4);
/*      */             } 
/*      */           } else {
/* 1251 */             data = String.valueOf(ch);
/*      */           } 
/* 1253 */           this.column += data.length();
/* 1254 */           this.stream.write(data);
/* 1255 */           start = end + 1;
/*      */         } 
/*      */       } 
/* 1258 */       if (0 < end && end < text.length() - 1 && (ch.charValue() == ' ' || start >= end) && this.column + end - start > this.bestWidth && split) {
/*      */         String data;
/*      */         
/* 1261 */         if (start >= end) {
/* 1262 */           data = "\\";
/*      */         } else {
/* 1264 */           data = text.substring(start, end) + "\\";
/*      */         } 
/* 1266 */         if (start < end) {
/* 1267 */           start = end;
/*      */         }
/* 1269 */         this.column += data.length();
/* 1270 */         this.stream.write(data);
/* 1271 */         writeIndent();
/* 1272 */         this.whitespace = false;
/* 1273 */         this.indention = false;
/* 1274 */         if (text.charAt(start) == ' ') {
/* 1275 */           data = "\\";
/* 1276 */           this.column += data.length();
/* 1277 */           this.stream.write(data);
/*      */         } 
/*      */       } 
/* 1280 */       end++;
/*      */     } 
/* 1282 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1286 */     StringBuilder hints = new StringBuilder();
/* 1287 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1288 */       hints.append(this.bestIndent);
/*      */     }
/* 1290 */     char ch1 = text.charAt(text.length() - 1);
/* 1291 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1292 */       hints.append("-");
/* 1293 */     } else if (text.length() == 1 || Constant.LINEBR.has(text.charAt(text.length() - 2))) {
/* 1294 */       hints.append("+");
/*      */     } 
/* 1296 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text) throws IOException {
/* 1300 */     String hints = determineBlockHints(text);
/* 1301 */     writeIndicator(">" + hints, true, false, false);
/* 1302 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1303 */       this.openEnded = true;
/*      */     }
/* 1305 */     writeLineBreak(null);
/* 1306 */     boolean leadingSpace = true;
/* 1307 */     boolean spaces = false;
/* 1308 */     boolean breaks = true;
/* 1309 */     int start = 0, end = 0;
/* 1310 */     while (end <= text.length()) {
/* 1311 */       char ch = Character.MIN_VALUE;
/* 1312 */       if (end < text.length()) {
/* 1313 */         ch = text.charAt(end);
/*      */       }
/* 1315 */       if (breaks) {
/* 1316 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1317 */           if (!leadingSpace && ch != '\000' && ch != ' ' && text.charAt(start) == '\n') {
/* 1318 */             writeLineBreak(null);
/*      */           }
/* 1320 */           leadingSpace = (ch == ' ');
/* 1321 */           String data = text.substring(start, end);
/* 1322 */           for (char br : data.toCharArray()) {
/* 1323 */             if (br == '\n') {
/* 1324 */               writeLineBreak(null);
/*      */             } else {
/* 1326 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1329 */           if (ch != '\000') {
/* 1330 */             writeIndent();
/*      */           }
/* 1332 */           start = end;
/*      */         } 
/* 1334 */       } else if (spaces) {
/* 1335 */         if (ch != ' ') {
/* 1336 */           if (start + 1 == end && this.column > this.bestWidth) {
/* 1337 */             writeIndent();
/*      */           } else {
/* 1339 */             int len = end - start;
/* 1340 */             this.column += len;
/* 1341 */             this.stream.write(text, start, len);
/*      */           } 
/* 1343 */           start = end;
/*      */         }
/*      */       
/* 1346 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1347 */         int len = end - start;
/* 1348 */         this.column += len;
/* 1349 */         this.stream.write(text, start, len);
/* 1350 */         if (ch == '\000') {
/* 1351 */           writeLineBreak(null);
/*      */         }
/* 1353 */         start = end;
/*      */       } 
/*      */       
/* 1356 */       if (ch != '\000') {
/* 1357 */         breaks = Constant.LINEBR.has(ch);
/* 1358 */         spaces = (ch == ' ');
/*      */       } 
/* 1360 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1365 */     String hints = determineBlockHints(text);
/* 1366 */     writeIndicator("|" + hints, true, false, false);
/* 1367 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1368 */       this.openEnded = true;
/*      */     }
/* 1370 */     writeLineBreak(null);
/* 1371 */     boolean breaks = true;
/* 1372 */     int start = 0, end = 0;
/* 1373 */     while (end <= text.length()) {
/* 1374 */       char ch = Character.MIN_VALUE;
/* 1375 */       if (end < text.length()) {
/* 1376 */         ch = text.charAt(end);
/*      */       }
/* 1378 */       if (breaks) {
/* 1379 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1380 */           String data = text.substring(start, end);
/* 1381 */           for (char br : data.toCharArray()) {
/* 1382 */             if (br == '\n') {
/* 1383 */               writeLineBreak(null);
/*      */             } else {
/* 1385 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1388 */           if (ch != '\000') {
/* 1389 */             writeIndent();
/*      */           }
/* 1391 */           start = end;
/*      */         }
/*      */       
/* 1394 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1395 */         this.stream.write(text, start, end - start);
/* 1396 */         if (ch == '\000') {
/* 1397 */           writeLineBreak(null);
/*      */         }
/* 1399 */         start = end;
/*      */       } 
/*      */       
/* 1402 */       if (ch != '\000') {
/* 1403 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1405 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1410 */     if (this.rootContext) {
/* 1411 */       this.openEnded = true;
/*      */     }
/* 1413 */     if (text.length() == 0) {
/*      */       return;
/*      */     }
/* 1416 */     if (!this.whitespace) {
/* 1417 */       this.column++;
/* 1418 */       this.stream.write(SPACE);
/*      */     } 
/* 1420 */     this.whitespace = false;
/* 1421 */     this.indention = false;
/* 1422 */     boolean spaces = false;
/* 1423 */     boolean breaks = false;
/* 1424 */     int start = 0, end = 0;
/* 1425 */     while (end <= text.length()) {
/* 1426 */       char ch = Character.MIN_VALUE;
/* 1427 */       if (end < text.length()) {
/* 1428 */         ch = text.charAt(end);
/*      */       }
/* 1430 */       if (spaces) {
/* 1431 */         if (ch != ' ') {
/* 1432 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1433 */             writeIndent();
/* 1434 */             this.whitespace = false;
/* 1435 */             this.indention = false;
/*      */           } else {
/* 1437 */             int len = end - start;
/* 1438 */             this.column += len;
/* 1439 */             this.stream.write(text, start, len);
/*      */           } 
/* 1441 */           start = end;
/*      */         } 
/* 1443 */       } else if (breaks) {
/* 1444 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1445 */           if (text.charAt(start) == '\n') {
/* 1446 */             writeLineBreak(null);
/*      */           }
/* 1448 */           String data = text.substring(start, end);
/* 1449 */           for (char br : data.toCharArray()) {
/* 1450 */             if (br == '\n') {
/* 1451 */               writeLineBreak(null);
/*      */             } else {
/* 1453 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1456 */           writeIndent();
/* 1457 */           this.whitespace = false;
/* 1458 */           this.indention = false;
/* 1459 */           start = end;
/*      */         }
/*      */       
/* 1462 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1463 */         int len = end - start;
/* 1464 */         this.column += len;
/* 1465 */         this.stream.write(text, start, len);
/* 1466 */         start = end;
/*      */       } 
/*      */       
/* 1469 */       if (ch != '\000') {
/* 1470 */         spaces = (ch == ' ');
/* 1471 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1473 */       end++;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */