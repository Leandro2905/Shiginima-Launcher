/*      */ package org.yaml.snakeyaml.scanner;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.error.Mark;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.tokens.AliasToken;
/*      */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEndToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentEndToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.KeyToken;
/*      */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*      */ import org.yaml.snakeyaml.tokens.TagToken;
/*      */ import org.yaml.snakeyaml.tokens.TagTuple;
/*      */ import org.yaml.snakeyaml.tokens.Token;
/*      */ import org.yaml.snakeyaml.tokens.ValueToken;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*      */ public final class ScannerImpl
/*      */   implements Scanner
/*      */ {
/*   87 */   private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
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
/*   98 */   public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
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
/*  114 */   public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<Character, Integer>();
/*      */   private final StreamReader reader;
/*      */   
/*      */   static {
/*  118 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
/*      */     
/*  120 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
/*      */     
/*  122 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
/*      */     
/*  124 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
/*      */     
/*  126 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
/*      */     
/*  128 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
/*      */     
/*  130 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
/*      */     
/*  132 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
/*      */     
/*  134 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
/*      */     
/*  136 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
/*      */     
/*  138 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*      */     
/*  140 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*      */     
/*  142 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
/*      */     
/*  144 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
/*      */     
/*  146 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
/*      */     
/*  148 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
/*      */ 
/*      */     
/*  151 */     ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
/*      */     
/*  153 */     ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
/*      */     
/*  155 */     ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean done = false;
/*      */ 
/*      */   
/*  163 */   private int flowLevel = 0;
/*      */ 
/*      */   
/*      */   private List<Token> tokens;
/*      */ 
/*      */   
/*  169 */   private int tokensTaken = 0;
/*      */ 
/*      */   
/*  172 */   private int indent = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayStack<Integer> indents;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowSimpleKey = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Integer, SimpleKey> possibleSimpleKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl(StreamReader reader) {
/*  213 */     this.reader = reader;
/*  214 */     this.tokens = new ArrayList<Token>(100);
/*  215 */     this.indents = new ArrayStack(10);
/*      */     
/*  217 */     this.possibleSimpleKeys = new LinkedHashMap<Integer, SimpleKey>();
/*  218 */     fetchStreamStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkToken(Token.ID... choices) {
/*  225 */     while (needMoreTokens()) {
/*  226 */       fetchMoreTokens();
/*      */     }
/*  228 */     if (!this.tokens.isEmpty()) {
/*  229 */       if (choices.length == 0) {
/*  230 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  234 */       Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
/*  235 */       for (int i = 0; i < choices.length; i++) {
/*  236 */         if (first == choices[i]) {
/*  237 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  241 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token peekToken() {
/*  248 */     while (needMoreTokens()) {
/*  249 */       fetchMoreTokens();
/*      */     }
/*  251 */     return this.tokens.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getToken() {
/*  258 */     if (!this.tokens.isEmpty()) {
/*  259 */       this.tokensTaken++;
/*  260 */       return this.tokens.remove(0);
/*      */     } 
/*  262 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreTokens() {
/*  271 */     if (this.done) {
/*  272 */       return false;
/*      */     }
/*      */     
/*  275 */     if (this.tokens.isEmpty()) {
/*  276 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  280 */     stalePossibleSimpleKeys();
/*  281 */     return (nextPossibleSimpleKey() == this.tokensTaken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchMoreTokens() {
/*  289 */     scanToNextToken();
/*      */     
/*  291 */     stalePossibleSimpleKeys();
/*      */ 
/*      */     
/*  294 */     unwindIndent(this.reader.getColumn());
/*      */ 
/*      */     
/*  297 */     char ch = this.reader.peek();
/*  298 */     switch (ch) {
/*      */       
/*      */       case '\000':
/*  301 */         fetchStreamEnd();
/*      */         return;
/*      */       
/*      */       case '%':
/*  305 */         if (checkDirective()) {
/*  306 */           fetchDirective();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case '-':
/*  312 */         if (checkDocumentStart()) {
/*  313 */           fetchDocumentStart();
/*      */           return;
/*      */         } 
/*  316 */         if (checkBlockEntry()) {
/*  317 */           fetchBlockEntry();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case '.':
/*  323 */         if (checkDocumentEnd()) {
/*  324 */           fetchDocumentEnd();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case '[':
/*  331 */         fetchFlowSequenceStart();
/*      */         return;
/*      */       
/*      */       case '{':
/*  335 */         fetchFlowMappingStart();
/*      */         return;
/*      */       
/*      */       case ']':
/*  339 */         fetchFlowSequenceEnd();
/*      */         return;
/*      */       
/*      */       case '}':
/*  343 */         fetchFlowMappingEnd();
/*      */         return;
/*      */       
/*      */       case ',':
/*  347 */         fetchFlowEntry();
/*      */         return;
/*      */ 
/*      */       
/*      */       case '?':
/*  352 */         if (checkKey()) {
/*  353 */           fetchKey();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case ':':
/*  359 */         if (checkValue()) {
/*  360 */           fetchValue();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case '*':
/*  366 */         fetchAlias();
/*      */         return;
/*      */       
/*      */       case '&':
/*  370 */         fetchAnchor();
/*      */         return;
/*      */       
/*      */       case '!':
/*  374 */         fetchTag();
/*      */         return;
/*      */       
/*      */       case '|':
/*  378 */         if (this.flowLevel == 0) {
/*  379 */           fetchLiteral();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case '>':
/*  385 */         if (this.flowLevel == 0) {
/*  386 */           fetchFolded();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case '\'':
/*  392 */         fetchSingle();
/*      */         return;
/*      */       
/*      */       case '"':
/*  396 */         fetchDouble();
/*      */         return;
/*      */     } 
/*      */     
/*  400 */     if (checkPlain()) {
/*  401 */       fetchPlain();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  407 */     String chRepresentation = String.valueOf(ch);
/*  408 */     for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
/*  409 */       String v = ESCAPE_REPLACEMENTS.get(s);
/*  410 */       if (v.equals(chRepresentation)) {
/*  411 */         chRepresentation = "\\" + s;
/*      */         break;
/*      */       } 
/*      */     } 
/*  415 */     if (ch == '\t')
/*  416 */       chRepresentation = chRepresentation + "(TAB)"; 
/*  417 */     String text = String.format("found character %s '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { Character.valueOf(ch), chRepresentation, chRepresentation });
/*      */ 
/*      */     
/*  420 */     throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
/*      */   }
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
/*      */   private int nextPossibleSimpleKey() {
/*  435 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  436 */       return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
/*      */     }
/*  438 */     return -1;
/*      */   }
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
/*      */   private void stalePossibleSimpleKeys() {
/*  452 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  453 */       Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
/*  454 */       while (iterator.hasNext()) {
/*  455 */         SimpleKey key = iterator.next();
/*  456 */         if (key.getLine() != this.reader.getLine() || this.reader.getIndex() - key.getIndex() > 1024) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  462 */           if (key.isRequired())
/*      */           {
/*      */             
/*  465 */             throw new ScannerException("while scanning a simple key", key.getMark(), "could not found expected ':'", this.reader.getMark());
/*      */           }
/*      */           
/*  468 */           iterator.remove();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
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
/*      */   private void savePossibleSimpleKey() {
/*  487 */     boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
/*      */     
/*  489 */     if (this.allowSimpleKey || !required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  499 */       if (this.allowSimpleKey) {
/*  500 */         removePossibleSimpleKey();
/*  501 */         int tokenNumber = this.tokensTaken + this.tokens.size();
/*  502 */         SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
/*      */         
/*  504 */         this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
/*      */       } 
/*      */       return;
/*      */     } 
/*      */     throw new YAMLException("A simple key is required only if it is the first token in the current line");
/*      */   }
/*      */   
/*      */   private void removePossibleSimpleKey() {
/*  512 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  513 */     if (key != null && key.isRequired()) {
/*  514 */       throw new ScannerException("while scanning a simple key", key.getMark(), "could not found expected ':'", this.reader.getMark());
/*      */     }
/*      */   }
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
/*      */   private void unwindIndent(int col) {
/*  544 */     if (this.flowLevel != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  549 */     while (this.indent > col) {
/*  550 */       Mark mark = this.reader.getMark();
/*  551 */       this.indent = ((Integer)this.indents.pop()).intValue();
/*  552 */       this.tokens.add(new BlockEndToken(mark, mark));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addIndent(int column) {
/*  560 */     if (this.indent < column) {
/*  561 */       this.indents.push(Integer.valueOf(this.indent));
/*  562 */       this.indent = column;
/*  563 */       return true;
/*      */     } 
/*  565 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchStreamStart() {
/*  576 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  579 */     StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
/*  580 */     this.tokens.add(streamStartToken);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fetchStreamEnd() {
/*  585 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  588 */     removePossibleSimpleKey();
/*  589 */     this.allowSimpleKey = false;
/*  590 */     this.possibleSimpleKeys.clear();
/*      */ 
/*      */     
/*  593 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  596 */     StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
/*  597 */     this.tokens.add(streamEndToken);
/*      */ 
/*      */     
/*  600 */     this.done = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDirective() {
/*  612 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  615 */     removePossibleSimpleKey();
/*  616 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  619 */     Token tok = scanDirective();
/*  620 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentStart() {
/*  627 */     fetchDocumentIndicator(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentEnd() {
/*  634 */     fetchDocumentIndicator(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentIndicator(boolean isDocumentStart) {
/*      */     DocumentEndToken documentEndToken;
/*  643 */     unwindIndent(-1);
/*      */ 
/*      */ 
/*      */     
/*  647 */     removePossibleSimpleKey();
/*  648 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  651 */     Mark startMark = this.reader.getMark();
/*  652 */     this.reader.forward(3);
/*  653 */     Mark endMark = this.reader.getMark();
/*      */     
/*  655 */     if (isDocumentStart) {
/*  656 */       DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
/*      */     } else {
/*  658 */       documentEndToken = new DocumentEndToken(startMark, endMark);
/*      */     } 
/*  660 */     this.tokens.add(documentEndToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceStart() {
/*  664 */     fetchFlowCollectionStart(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingStart() {
/*  668 */     fetchFlowCollectionStart(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionStart(boolean isMappingStart) {
/*      */     FlowSequenceStartToken flowSequenceStartToken;
/*  685 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  688 */     this.flowLevel++;
/*      */ 
/*      */     
/*  691 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  694 */     Mark startMark = this.reader.getMark();
/*  695 */     this.reader.forward(1);
/*  696 */     Mark endMark = this.reader.getMark();
/*      */     
/*  698 */     if (isMappingStart) {
/*  699 */       FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
/*      */     } else {
/*  701 */       flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
/*      */     } 
/*  703 */     this.tokens.add(flowSequenceStartToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceEnd() {
/*  707 */     fetchFlowCollectionEnd(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingEnd() {
/*  711 */     fetchFlowCollectionEnd(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionEnd(boolean isMappingEnd) {
/*      */     FlowSequenceEndToken flowSequenceEndToken;
/*  726 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  729 */     this.flowLevel--;
/*      */ 
/*      */     
/*  732 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  735 */     Mark startMark = this.reader.getMark();
/*  736 */     this.reader.forward();
/*  737 */     Mark endMark = this.reader.getMark();
/*      */     
/*  739 */     if (isMappingEnd) {
/*  740 */       FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
/*      */     } else {
/*  742 */       flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
/*      */     } 
/*  744 */     this.tokens.add(flowSequenceEndToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowEntry() {
/*  755 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  758 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  761 */     Mark startMark = this.reader.getMark();
/*  762 */     this.reader.forward();
/*  763 */     Mark endMark = this.reader.getMark();
/*  764 */     FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
/*  765 */     this.tokens.add(flowEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockEntry() {
/*  775 */     if (this.flowLevel == 0) {
/*      */       
/*  777 */       if (!this.allowSimpleKey) {
/*  778 */         throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  783 */       if (addIndent(this.reader.getColumn())) {
/*  784 */         Mark mark = this.reader.getMark();
/*  785 */         this.tokens.add(new BlockSequenceStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  792 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  795 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  798 */     Mark startMark = this.reader.getMark();
/*  799 */     this.reader.forward();
/*  800 */     Mark endMark = this.reader.getMark();
/*  801 */     BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
/*  802 */     this.tokens.add(blockEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchKey() {
/*  812 */     if (this.flowLevel == 0) {
/*      */       
/*  814 */       if (!this.allowSimpleKey) {
/*  815 */         throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/*  819 */       if (addIndent(this.reader.getColumn())) {
/*  820 */         Mark mark = this.reader.getMark();
/*  821 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */     
/*  825 */     this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */     
/*  828 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  831 */     Mark startMark = this.reader.getMark();
/*  832 */     this.reader.forward();
/*  833 */     Mark endMark = this.reader.getMark();
/*  834 */     KeyToken keyToken = new KeyToken(startMark, endMark);
/*  835 */     this.tokens.add(keyToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchValue() {
/*  845 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  846 */     if (key != null) {
/*      */       
/*  848 */       this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key.getMark()));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  853 */       if (this.flowLevel == 0 && 
/*  854 */         addIndent(key.getColumn())) {
/*  855 */         this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark()));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  860 */       this.allowSimpleKey = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  866 */       if (this.flowLevel == 0)
/*      */       {
/*      */ 
/*      */         
/*  870 */         if (!this.allowSimpleKey) {
/*  871 */           throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  879 */       if (this.flowLevel == 0 && 
/*  880 */         addIndent(this.reader.getColumn())) {
/*  881 */         Mark mark = this.reader.getMark();
/*  882 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  887 */       this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */       
/*  890 */       removePossibleSimpleKey();
/*      */     } 
/*      */     
/*  893 */     Mark startMark = this.reader.getMark();
/*  894 */     this.reader.forward();
/*  895 */     Mark endMark = this.reader.getMark();
/*  896 */     ValueToken valueToken = new ValueToken(startMark, endMark);
/*  897 */     this.tokens.add(valueToken);
/*      */   }
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
/*      */   private void fetchAlias() {
/*  912 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  915 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  918 */     Token tok = scanAnchor(false);
/*  919 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private void fetchAnchor() {
/*  933 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  936 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  939 */     Token tok = scanAnchor(true);
/*  940 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchTag() {
/*  950 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  953 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  956 */     Token tok = scanTag();
/*  957 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchLiteral() {
/*  968 */     fetchBlockScalar('|');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFolded() {
/*  978 */     fetchBlockScalar('>');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockScalar(char style) {
/*  990 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  993 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  996 */     Token tok = scanBlockScalar(style);
/*  997 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchSingle() {
/* 1004 */     fetchFlowScalar('\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDouble() {
/* 1011 */     fetchFlowScalar('"');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowScalar(char style) {
/* 1023 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1026 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1029 */     Token tok = scanFlowScalar(style);
/* 1030 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchPlain() {
/* 1038 */     savePossibleSimpleKey();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1046 */     Token tok = scanPlain();
/* 1047 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private boolean checkDirective() {
/* 1060 */     return (this.reader.getColumn() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentStart() {
/* 1069 */     if (this.reader.getColumn() == 0 && 
/* 1070 */       "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1071 */       return true;
/*      */     }
/*      */     
/* 1074 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentEnd() {
/* 1083 */     if (this.reader.getColumn() == 0 && 
/* 1084 */       "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1085 */       return true;
/*      */     }
/*      */     
/* 1088 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkBlockEntry() {
/* 1096 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkKey() {
/* 1104 */     if (this.flowLevel != 0) {
/* 1105 */       return true;
/*      */     }
/*      */     
/* 1108 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkValue() {
/* 1117 */     if (this.flowLevel != 0) {
/* 1118 */       return true;
/*      */     }
/*      */     
/* 1121 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
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
/*      */   private boolean checkPlain() {
/* 1145 */     char ch = this.reader.peek();
/*      */ 
/*      */     
/* 1148 */     return (Constant.NULL_BL_T_LINEBR.hasNo(ch, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1)) && (ch == '-' || (this.flowLevel == 0 && "?:".indexOf(ch) != -1))));
/*      */   }
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
/*      */   private void scanToNextToken() {
/* 1179 */     if (this.reader.getIndex() == 0 && this.reader.peek() == '﻿') {
/* 1180 */       this.reader.forward();
/*      */     }
/* 1182 */     boolean found = false;
/* 1183 */     while (!found) {
/* 1184 */       int ff = 0;
/*      */ 
/*      */       
/* 1187 */       while (this.reader.peek(ff) == ' ') {
/* 1188 */         ff++;
/*      */       }
/* 1190 */       if (ff > 0) {
/* 1191 */         this.reader.forward(ff);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1197 */       if (this.reader.peek() == '#') {
/* 1198 */         ff = 0;
/* 1199 */         while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1200 */           ff++;
/*      */         }
/* 1202 */         if (ff > 0) {
/* 1203 */           this.reader.forward(ff);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1208 */       if (scanLineBreak().length() != 0) {
/* 1209 */         if (this.flowLevel == 0)
/*      */         {
/*      */           
/* 1212 */           this.allowSimpleKey = true; } 
/*      */         continue;
/*      */       } 
/* 1215 */       found = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanDirective() {
/* 1223 */     Mark endMark, startMark = this.reader.getMark();
/*      */     
/* 1225 */     this.reader.forward();
/* 1226 */     String name = scanDirectiveName(startMark);
/* 1227 */     List<?> value = null;
/* 1228 */     if ("YAML".equals(name)) {
/* 1229 */       value = scanYamlDirectiveValue(startMark);
/* 1230 */       endMark = this.reader.getMark();
/* 1231 */     } else if ("TAG".equals(name)) {
/* 1232 */       value = scanTagDirectiveValue(startMark);
/* 1233 */       endMark = this.reader.getMark();
/*      */     } else {
/* 1235 */       endMark = this.reader.getMark();
/* 1236 */       int ff = 0;
/* 1237 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1238 */         ff++;
/*      */       }
/* 1240 */       if (ff > 0) {
/* 1241 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/* 1244 */     scanDirectiveIgnoredLine(startMark);
/* 1245 */     return (Token)new DirectiveToken(name, value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanDirectiveName(Mark startMark) {
/* 1256 */     int length = 0;
/*      */ 
/*      */ 
/*      */     
/* 1260 */     char ch = this.reader.peek(length);
/* 1261 */     while (Constant.ALPHA.has(ch)) {
/* 1262 */       length++;
/* 1263 */       ch = this.reader.peek(length);
/*      */     } 
/*      */     
/* 1266 */     if (length == 0) {
/* 1267 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1271 */     String value = this.reader.prefixForward(length);
/* 1272 */     ch = this.reader.peek();
/* 1273 */     if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
/* 1274 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1278 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Integer> scanYamlDirectiveValue(Mark startMark) {
/* 1283 */     while (this.reader.peek() == ' ') {
/* 1284 */       this.reader.forward();
/*      */     }
/* 1286 */     Integer major = scanYamlDirectiveNumber(startMark);
/* 1287 */     if (this.reader.peek() != '.') {
/* 1288 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1292 */     this.reader.forward();
/* 1293 */     Integer minor = scanYamlDirectiveNumber(startMark);
/* 1294 */     if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
/* 1295 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1299 */     List<Integer> result = new ArrayList<Integer>(2);
/* 1300 */     result.add(major);
/* 1301 */     result.add(minor);
/* 1302 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Integer scanYamlDirectiveNumber(Mark startMark) {
/* 1314 */     char ch = this.reader.peek();
/* 1315 */     if (!Character.isDigit(ch)) {
/* 1316 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */     
/* 1319 */     int length = 0;
/* 1320 */     while (Character.isDigit(this.reader.peek(length))) {
/* 1321 */       length++;
/*      */     }
/* 1323 */     Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
/* 1324 */     return value;
/*      */   }
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
/*      */   private List<String> scanTagDirectiveValue(Mark startMark) {
/* 1341 */     while (this.reader.peek() == ' ') {
/* 1342 */       this.reader.forward();
/*      */     }
/* 1344 */     String handle = scanTagDirectiveHandle(startMark);
/* 1345 */     while (this.reader.peek() == ' ') {
/* 1346 */       this.reader.forward();
/*      */     }
/* 1348 */     String prefix = scanTagDirectivePrefix(startMark);
/* 1349 */     List<String> result = new ArrayList<String>(2);
/* 1350 */     result.add(handle);
/* 1351 */     result.add(prefix);
/* 1352 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectiveHandle(Mark startMark) {
/* 1364 */     String value = scanTagHandle("directive", startMark);
/* 1365 */     char ch = this.reader.peek();
/* 1366 */     if (ch != ' ') {
/* 1367 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */     
/* 1370 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectivePrefix(Mark startMark) {
/* 1380 */     String value = scanTagUri("directive", startMark);
/* 1381 */     if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
/* 1382 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1386 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanDirectiveIgnoredLine(Mark startMark) {
/* 1391 */     int ff = 0;
/* 1392 */     while (this.reader.peek(ff) == ' ') {
/* 1393 */       ff++;
/*      */     }
/* 1395 */     if (ff > 0) {
/* 1396 */       this.reader.forward(ff);
/*      */     }
/* 1398 */     if (this.reader.peek() == '#') {
/* 1399 */       ff = 0;
/* 1400 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1401 */         ff++;
/*      */       }
/* 1403 */       this.reader.forward(ff);
/*      */     } 
/* 1405 */     char ch = this.reader.peek();
/* 1406 */     String lineBreak = scanLineBreak();
/* 1407 */     if (lineBreak.length() == 0 && ch != '\000') {
/* 1408 */       throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1412 */     return lineBreak;
/*      */   }
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
/*      */   private Token scanAnchor(boolean isAnchor) {
/*      */     AliasToken aliasToken;
/* 1428 */     Mark startMark = this.reader.getMark();
/* 1429 */     char indicator = this.reader.peek();
/* 1430 */     String name = (indicator == '*') ? "alias" : "anchor";
/* 1431 */     this.reader.forward();
/* 1432 */     int length = 0;
/* 1433 */     char ch = this.reader.peek(length);
/* 1434 */     while (Constant.ALPHA.has(ch)) {
/* 1435 */       length++;
/* 1436 */       ch = this.reader.peek(length);
/*      */     } 
/* 1438 */     if (length == 0) {
/* 1439 */       throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found but found " + ch, this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1443 */     String value = this.reader.prefixForward(length);
/* 1444 */     ch = this.reader.peek();
/* 1445 */     if (Constant.NULL_BL_T_LINEBR.hasNo(ch, "?:,]}%@`")) {
/* 1446 */       throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + ch + "(" + this.reader.peek() + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1450 */     Mark endMark = this.reader.getMark();
/*      */     
/* 1452 */     if (isAnchor) {
/* 1453 */       AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
/*      */     } else {
/* 1455 */       aliasToken = new AliasToken(value, startMark, endMark);
/*      */     } 
/* 1457 */     return (Token)aliasToken;
/*      */   }
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
/*      */   
/*      */   private Token scanTag() {
/* 1495 */     Mark startMark = this.reader.getMark();
/*      */ 
/*      */     
/* 1498 */     char ch = this.reader.peek(1);
/* 1499 */     String handle = null;
/* 1500 */     String suffix = null;
/*      */     
/* 1502 */     if (ch == '<') {
/*      */ 
/*      */       
/* 1505 */       this.reader.forward(2);
/* 1506 */       suffix = scanTagUri("tag", startMark);
/* 1507 */       if (this.reader.peek() != '>')
/*      */       {
/*      */         
/* 1510 */         throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + this.reader.peek() + "' (" + this.reader.peek() + ")", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/* 1514 */       this.reader.forward();
/* 1515 */     } else if (Constant.NULL_BL_T_LINEBR.has(ch)) {
/*      */ 
/*      */       
/* 1518 */       suffix = "!";
/* 1519 */       this.reader.forward();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1525 */       int length = 1;
/* 1526 */       boolean useHandle = false;
/* 1527 */       while (Constant.NULL_BL_LINEBR.hasNo(ch)) {
/* 1528 */         if (ch == '!') {
/* 1529 */           useHandle = true;
/*      */           break;
/*      */         } 
/* 1532 */         length++;
/* 1533 */         ch = this.reader.peek(length);
/*      */       } 
/* 1535 */       handle = "!";
/*      */ 
/*      */       
/* 1538 */       if (useHandle) {
/* 1539 */         handle = scanTagHandle("tag", startMark);
/*      */       } else {
/* 1541 */         handle = "!";
/* 1542 */         this.reader.forward();
/*      */       } 
/* 1544 */       suffix = scanTagUri("tag", startMark);
/*      */     } 
/* 1546 */     ch = this.reader.peek();
/*      */ 
/*      */     
/* 1549 */     if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
/* 1550 */       throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + ch + "' (" + ch + ")", this.reader.getMark());
/*      */     }
/*      */     
/* 1553 */     TagTuple value = new TagTuple(handle, suffix);
/* 1554 */     Mark endMark = this.reader.getMark();
/* 1555 */     return (Token)new TagToken(value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanBlockScalar(char style) {
/*      */     boolean folded;
/*      */     Mark mark1;
/* 1563 */     if (style == '>') {
/* 1564 */       folded = true;
/*      */     } else {
/* 1566 */       folded = false;
/*      */     } 
/* 1568 */     StringBuilder chunks = new StringBuilder();
/* 1569 */     Mark startMark = this.reader.getMark();
/*      */     
/* 1571 */     this.reader.forward();
/* 1572 */     Chomping chompi = scanBlockScalarIndicators(startMark);
/* 1573 */     int increment = chompi.getIncrement();
/* 1574 */     scanBlockScalarIgnoredLine(startMark);
/*      */ 
/*      */     
/* 1577 */     int minIndent = this.indent + 1;
/* 1578 */     if (minIndent < 1) {
/* 1579 */       minIndent = 1;
/*      */     }
/* 1581 */     String breaks = null;
/* 1582 */     int maxIndent = 0;
/* 1583 */     int indent = 0;
/*      */     
/* 1585 */     if (increment == -1) {
/* 1586 */       Object[] brme = scanBlockScalarIndentation();
/* 1587 */       breaks = (String)brme[0];
/* 1588 */       maxIndent = ((Integer)brme[1]).intValue();
/* 1589 */       mark1 = (Mark)brme[2];
/* 1590 */       indent = Math.max(minIndent, maxIndent);
/*      */     } else {
/* 1592 */       indent = minIndent + increment - 1;
/* 1593 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1594 */       breaks = (String)brme[0];
/* 1595 */       mark1 = (Mark)brme[1];
/*      */     } 
/*      */     
/* 1598 */     String lineBreak = "";
/*      */ 
/*      */     
/* 1601 */     while (this.reader.getColumn() == indent && this.reader.peek() != '\000') {
/* 1602 */       chunks.append(breaks);
/* 1603 */       boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
/* 1604 */       int length = 0;
/* 1605 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1606 */         length++;
/*      */       }
/* 1608 */       chunks.append(this.reader.prefixForward(length));
/* 1609 */       lineBreak = scanLineBreak();
/* 1610 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1611 */       breaks = (String)brme[0];
/* 1612 */       mark1 = (Mark)brme[1];
/* 1613 */       if (this.reader.getColumn() == indent && this.reader.peek() != '\000') {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1618 */         if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t".indexOf(this.reader.peek()) == -1) {
/*      */           
/* 1620 */           if (breaks.length() == 0)
/* 1621 */             chunks.append(" "); 
/*      */           continue;
/*      */         } 
/* 1624 */         chunks.append(lineBreak);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1633 */     if (chompi.chompTailIsNotFalse()) {
/* 1634 */       chunks.append(lineBreak);
/*      */     }
/* 1636 */     if (chompi.chompTailIsTrue()) {
/* 1637 */       chunks.append(breaks);
/*      */     }
/*      */     
/* 1640 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, mark1, style);
/*      */   }
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
/*      */   private Chomping scanBlockScalarIndicators(Mark startMark) {
/* 1660 */     Boolean chomping = null;
/* 1661 */     int increment = -1;
/* 1662 */     char ch = this.reader.peek();
/* 1663 */     if (ch == '-' || ch == '+') {
/* 1664 */       if (ch == '+') {
/* 1665 */         chomping = Boolean.TRUE;
/*      */       } else {
/* 1667 */         chomping = Boolean.FALSE;
/*      */       } 
/* 1669 */       this.reader.forward();
/* 1670 */       ch = this.reader.peek();
/* 1671 */       if (Character.isDigit(ch)) {
/* 1672 */         increment = Integer.parseInt(String.valueOf(ch));
/* 1673 */         if (increment == 0) {
/* 1674 */           throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
/*      */         }
/*      */ 
/*      */         
/* 1678 */         this.reader.forward();
/*      */       } 
/* 1680 */     } else if (Character.isDigit(ch)) {
/* 1681 */       increment = Integer.parseInt(String.valueOf(ch));
/* 1682 */       if (increment == 0) {
/* 1683 */         throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/* 1687 */       this.reader.forward();
/* 1688 */       ch = this.reader.peek();
/* 1689 */       if (ch == '-' || ch == '+') {
/* 1690 */         if (ch == '+') {
/* 1691 */           chomping = Boolean.TRUE;
/*      */         } else {
/* 1693 */           chomping = Boolean.FALSE;
/*      */         } 
/* 1695 */         this.reader.forward();
/*      */       } 
/*      */     } 
/* 1698 */     ch = this.reader.peek();
/* 1699 */     if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
/* 1700 */       throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + ch, this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1704 */     return new Chomping(chomping, increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanBlockScalarIgnoredLine(Mark startMark) {
/* 1713 */     int ff = 0;
/*      */     
/* 1715 */     while (this.reader.peek(ff) == ' ') {
/* 1716 */       ff++;
/*      */     }
/* 1718 */     if (ff > 0) {
/* 1719 */       this.reader.forward(ff);
/*      */     }
/*      */     
/* 1722 */     if (this.reader.peek() == '#') {
/* 1723 */       ff = 0;
/* 1724 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1725 */         ff++;
/*      */       }
/* 1727 */       if (ff > 0) {
/* 1728 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1733 */     char ch = this.reader.peek();
/* 1734 */     String lineBreak = scanLineBreak();
/* 1735 */     if (lineBreak.length() == 0 && ch != '\000') {
/* 1736 */       throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + ch, this.reader.getMark());
/*      */     }
/*      */     
/* 1739 */     return lineBreak;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarIndentation() {
/* 1751 */     StringBuilder chunks = new StringBuilder();
/* 1752 */     int maxIndent = 0;
/* 1753 */     Mark endMark = this.reader.getMark();
/*      */ 
/*      */ 
/*      */     
/* 1757 */     while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
/* 1758 */       if (this.reader.peek() != ' ') {
/*      */ 
/*      */         
/* 1761 */         chunks.append(scanLineBreak());
/* 1762 */         endMark = this.reader.getMark();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1767 */       this.reader.forward();
/* 1768 */       if (this.reader.getColumn() > maxIndent) {
/* 1769 */         maxIndent = this.reader.getColumn();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1774 */     return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
/*      */   }
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarBreaks(int indent) {
/* 1779 */     StringBuilder chunks = new StringBuilder();
/* 1780 */     Mark endMark = this.reader.getMark();
/* 1781 */     int ff = 0;
/* 1782 */     int col = this.reader.getColumn();
/*      */ 
/*      */     
/* 1785 */     while (col < indent && this.reader.peek(ff) == ' ') {
/* 1786 */       ff++;
/* 1787 */       col++;
/*      */     } 
/* 1789 */     if (ff > 0) {
/* 1790 */       this.reader.forward(ff);
/*      */     }
/*      */ 
/*      */     
/* 1794 */     String lineBreak = null;
/* 1795 */     while ((lineBreak = scanLineBreak()).length() != 0) {
/* 1796 */       chunks.append(lineBreak);
/* 1797 */       endMark = this.reader.getMark();
/*      */ 
/*      */       
/* 1800 */       ff = 0;
/* 1801 */       col = this.reader.getColumn();
/* 1802 */       while (col < indent && this.reader.peek(ff) == ' ') {
/* 1803 */         ff++;
/* 1804 */         col++;
/*      */       } 
/* 1806 */       if (ff > 0) {
/* 1807 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/*      */     
/* 1811 */     return new Object[] { chunks.toString(), endMark };
/*      */   }
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
/*      */   private Token scanFlowScalar(char style) {
/*      */     boolean _double;
/* 1834 */     if (style == '"') {
/* 1835 */       _double = true;
/*      */     } else {
/* 1837 */       _double = false;
/*      */     } 
/* 1839 */     StringBuilder chunks = new StringBuilder();
/* 1840 */     Mark startMark = this.reader.getMark();
/* 1841 */     char quote = this.reader.peek();
/* 1842 */     this.reader.forward();
/* 1843 */     chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/* 1844 */     while (this.reader.peek() != quote) {
/* 1845 */       chunks.append(scanFlowScalarSpaces(startMark));
/* 1846 */       chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/*      */     } 
/* 1848 */     this.reader.forward();
/* 1849 */     Mark endMark = this.reader.getMark();
/* 1850 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
/* 1858 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1862 */       int length = 0;
/* 1863 */       while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
/* 1864 */         length++;
/*      */       }
/* 1866 */       if (length != 0) {
/* 1867 */         chunks.append(this.reader.prefixForward(length));
/*      */       }
/*      */ 
/*      */       
/* 1871 */       char ch = this.reader.peek();
/* 1872 */       if (!doubleQuoted && ch == '\'' && this.reader.peek(1) == '\'') {
/* 1873 */         chunks.append("'");
/* 1874 */         this.reader.forward(2); continue;
/* 1875 */       }  if ((doubleQuoted && ch == '\'') || (!doubleQuoted && "\"\\".indexOf(ch) != -1)) {
/* 1876 */         chunks.append(ch);
/* 1877 */         this.reader.forward(); continue;
/* 1878 */       }  if (doubleQuoted && ch == '\\') {
/* 1879 */         this.reader.forward();
/* 1880 */         ch = this.reader.peek();
/* 1881 */         if (ESCAPE_REPLACEMENTS.containsKey(Character.valueOf(ch))) {
/*      */ 
/*      */ 
/*      */           
/* 1885 */           chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf(ch)));
/* 1886 */           this.reader.forward(); continue;
/* 1887 */         }  if (ESCAPE_CODES.containsKey(Character.valueOf(ch))) {
/*      */ 
/*      */           
/* 1890 */           length = ((Integer)ESCAPE_CODES.get(Character.valueOf(ch))).intValue();
/* 1891 */           this.reader.forward();
/* 1892 */           String hex = this.reader.prefix(length);
/* 1893 */           if (NOT_HEXA.matcher(hex).find()) {
/* 1894 */             throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader.getMark());
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1899 */           int decimal = Integer.parseInt(hex, 16);
/* 1900 */           String unicode = new String(Character.toChars(decimal));
/* 1901 */           chunks.append(unicode);
/* 1902 */           this.reader.forward(length); continue;
/* 1903 */         }  if (scanLineBreak().length() != 0) {
/* 1904 */           chunks.append(scanFlowScalarBreaks(startMark)); continue;
/*      */         } 
/* 1906 */         throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + ch + "(" + ch + ")", this.reader.getMark());
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1911 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarSpaces(Mark startMark) {
/* 1918 */     StringBuilder chunks = new StringBuilder();
/* 1919 */     int length = 0;
/*      */ 
/*      */     
/* 1922 */     while (" \t".indexOf(this.reader.peek(length)) != -1) {
/* 1923 */       length++;
/*      */     }
/* 1925 */     String whitespaces = this.reader.prefixForward(length);
/* 1926 */     char ch = this.reader.peek();
/* 1927 */     if (ch == '\000')
/*      */     {
/* 1929 */       throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1933 */     String lineBreak = scanLineBreak();
/* 1934 */     if (lineBreak.length() != 0) {
/* 1935 */       String breaks = scanFlowScalarBreaks(startMark);
/* 1936 */       if (!"\n".equals(lineBreak)) {
/* 1937 */         chunks.append(lineBreak);
/* 1938 */       } else if (breaks.length() == 0) {
/* 1939 */         chunks.append(" ");
/*      */       } 
/* 1941 */       chunks.append(breaks);
/*      */     } else {
/* 1943 */       chunks.append(whitespaces);
/*      */     } 
/* 1945 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanFlowScalarBreaks(Mark startMark) {
/* 1950 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1954 */       String prefix = this.reader.prefix(3);
/* 1955 */       if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))
/*      */       {
/* 1957 */         throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/* 1961 */       while (" \t".indexOf(this.reader.peek()) != -1) {
/* 1962 */         this.reader.forward();
/*      */       }
/*      */ 
/*      */       
/* 1966 */       String lineBreak = scanLineBreak();
/* 1967 */       if (lineBreak.length() != 0) {
/* 1968 */         chunks.append(lineBreak); continue;
/*      */       }  break;
/* 1970 */     }  return chunks.toString();
/*      */   }
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
/*      */   private Token scanPlain() {
/* 1987 */     StringBuilder chunks = new StringBuilder();
/* 1988 */     Mark startMark = this.reader.getMark();
/* 1989 */     Mark endMark = startMark;
/* 1990 */     int indent = this.indent + 1;
/* 1991 */     String spaces = "";
/*      */     do {
/*      */       char ch;
/* 1994 */       int length = 0;
/*      */       
/* 1996 */       if (this.reader.peek() == '#') {
/*      */         break;
/*      */       }
/*      */       while (true) {
/* 2000 */         ch = this.reader.peek(length);
/* 2001 */         if (Constant.NULL_BL_T_LINEBR.has(ch) || (this.flowLevel == 0 && ch == ':' && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(length + 1))) || (this.flowLevel != 0 && ",:?[]{}".indexOf(ch) != -1)) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2007 */         length++;
/*      */       } 
/*      */       
/* 2010 */       if (this.flowLevel != 0 && ch == ':' && Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length + 1), ",[]{}")) {
/*      */         
/* 2012 */         this.reader.forward(length);
/* 2013 */         throw new ScannerException("while scanning a plain scalar", startMark, "found unexpected ':'", this.reader.getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
/*      */       } 
/*      */ 
/*      */       
/* 2017 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 2020 */       this.allowSimpleKey = false;
/* 2021 */       chunks.append(spaces);
/* 2022 */       chunks.append(this.reader.prefixForward(length));
/* 2023 */       endMark = this.reader.getMark();
/* 2024 */       spaces = scanPlainSpaces();
/*      */     }
/* 2026 */     while (spaces.length() != 0 && this.reader.peek() != '#' && (this.flowLevel != 0 || this.reader.getColumn() >= indent));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2031 */     return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanPlainSpaces() {
/* 2039 */     int length = 0;
/* 2040 */     while (this.reader.peek(length) == ' ' || this.reader.peek(length) == '\t') {
/* 2041 */       length++;
/*      */     }
/* 2043 */     String whitespaces = this.reader.prefixForward(length);
/* 2044 */     String lineBreak = scanLineBreak();
/* 2045 */     if (lineBreak.length() != 0) {
/* 2046 */       this.allowSimpleKey = true;
/* 2047 */       String prefix = this.reader.prefix(3);
/* 2048 */       if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
/*      */       {
/* 2050 */         return "";
/*      */       }
/* 2052 */       StringBuilder breaks = new StringBuilder();
/*      */       while (true) {
/* 2054 */         while (this.reader.peek() == ' ') {
/* 2055 */           this.reader.forward();
/*      */         }
/* 2057 */         String lb = scanLineBreak();
/* 2058 */         if (lb.length() != 0) {
/* 2059 */           breaks.append(lb);
/* 2060 */           prefix = this.reader.prefix(3);
/* 2061 */           if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
/*      */           {
/* 2063 */             return "";
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2070 */       if (!"\n".equals(lineBreak))
/* 2071 */         return lineBreak + breaks; 
/* 2072 */       if (breaks.length() == 0) {
/* 2073 */         return " ";
/*      */       }
/* 2075 */       return breaks.toString();
/*      */     } 
/* 2077 */     return whitespaces;
/*      */   }
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
/*      */   private String scanTagHandle(String name, Mark startMark) {
/* 2103 */     char ch = this.reader.peek();
/* 2104 */     if (ch != '!') {
/* 2105 */       throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2111 */     int length = 1;
/* 2112 */     ch = this.reader.peek(length);
/* 2113 */     if (ch != ' ') {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2118 */       while (Constant.ALPHA.has(ch)) {
/* 2119 */         length++;
/* 2120 */         ch = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2125 */       if (ch != '!') {
/* 2126 */         this.reader.forward(length);
/* 2127 */         throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */       } 
/*      */       
/* 2130 */       length++;
/*      */     } 
/* 2132 */     String value = this.reader.prefixForward(length);
/* 2133 */     return value;
/*      */   }
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
/*      */   private String scanTagUri(String name, Mark startMark) {
/* 2154 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 2158 */     int length = 0;
/* 2159 */     char ch = this.reader.peek(length);
/* 2160 */     while (Constant.URI_CHARS.has(ch)) {
/* 2161 */       if (ch == '%') {
/* 2162 */         chunks.append(this.reader.prefixForward(length));
/* 2163 */         length = 0;
/* 2164 */         chunks.append(scanUriEscapes(name, startMark));
/*      */       } else {
/* 2166 */         length++;
/*      */       } 
/* 2168 */       ch = this.reader.peek(length);
/*      */     } 
/*      */ 
/*      */     
/* 2172 */     if (length != 0) {
/* 2173 */       chunks.append(this.reader.prefixForward(length));
/* 2174 */       length = 0;
/*      */     } 
/* 2176 */     if (chunks.length() == 0)
/*      */     {
/* 2178 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + ch + "(" + ch + ")", this.reader.getMark());
/*      */     }
/*      */     
/* 2181 */     return chunks.toString();
/*      */   }
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
/*      */   private String scanUriEscapes(String name, Mark startMark) {
/* 2198 */     int length = 1;
/* 2199 */     while (this.reader.peek(length * 3) == '%') {
/* 2200 */       length++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2206 */     Mark beginningMark = this.reader.getMark();
/* 2207 */     ByteBuffer buff = ByteBuffer.allocate(length);
/* 2208 */     while (this.reader.peek() == '%') {
/* 2209 */       this.reader.forward();
/*      */       try {
/* 2211 */         byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
/* 2212 */         buff.put(code);
/* 2213 */       } catch (NumberFormatException nfe) {
/* 2214 */         throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + this.reader.peek() + "(" + this.reader.peek() + ") and " + this.reader.peek(1) + "(" + this.reader.peek(1) + ")", this.reader.getMark());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2220 */       this.reader.forward(2);
/*      */     } 
/* 2222 */     buff.flip();
/*      */     try {
/* 2224 */       return UriEncoder.decode(buff);
/* 2225 */     } catch (CharacterCodingException e) {
/* 2226 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e.getMessage(), beginningMark);
/*      */     } 
/*      */   }
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
/*      */   private String scanLineBreak() {
/* 2249 */     char ch = this.reader.peek();
/* 2250 */     if (ch == '\r' || ch == '\n' || ch == '') {
/* 2251 */       if (ch == '\r' && '\n' == this.reader.peek(1)) {
/* 2252 */         this.reader.forward(2);
/*      */       } else {
/* 2254 */         this.reader.forward();
/*      */       } 
/* 2256 */       return "\n";
/* 2257 */     }  if (ch == ' ' || ch == ' ') {
/* 2258 */       this.reader.forward();
/* 2259 */       return String.valueOf(ch);
/*      */     } 
/* 2261 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Chomping
/*      */   {
/*      */     private final Boolean value;
/*      */     
/*      */     private final int increment;
/*      */     
/*      */     public Chomping(Boolean value, int increment) {
/* 2272 */       this.value = value;
/* 2273 */       this.increment = increment;
/*      */     }
/*      */     
/*      */     public boolean chompTailIsNotFalse() {
/* 2277 */       return (this.value == null || this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public boolean chompTailIsTrue() {
/* 2281 */       return (this.value != null && this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public int getIncrement() {
/* 2285 */       return this.increment;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */