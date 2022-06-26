/*      */ package com.google.gson.stream;
/*      */ 
/*      */ import com.google.gson.internal.JsonReaderInternalAccess;
/*      */ import com.google.gson.internal.bind.JsonTreeReader;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
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
/*      */ public class JsonReader
/*      */   implements Closeable
/*      */ {
/*  192 */   private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
/*      */   
/*      */   private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
/*      */   
/*      */   private static final int PEEKED_NONE = 0;
/*      */   
/*      */   private static final int PEEKED_BEGIN_OBJECT = 1;
/*      */   
/*      */   private static final int PEEKED_END_OBJECT = 2;
/*      */   
/*      */   private static final int PEEKED_BEGIN_ARRAY = 3;
/*      */   
/*      */   private static final int PEEKED_END_ARRAY = 4;
/*      */   
/*      */   private static final int PEEKED_TRUE = 5;
/*      */   
/*      */   private static final int PEEKED_FALSE = 6;
/*      */   
/*      */   private static final int PEEKED_NULL = 7;
/*      */   
/*      */   private static final int PEEKED_SINGLE_QUOTED = 8;
/*      */   
/*      */   private static final int PEEKED_DOUBLE_QUOTED = 9;
/*      */   
/*      */   private static final int PEEKED_UNQUOTED = 10;
/*      */   
/*      */   private static final int PEEKED_BUFFERED = 11;
/*      */   
/*      */   private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
/*      */   
/*      */   private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
/*      */   
/*      */   private static final int PEEKED_UNQUOTED_NAME = 14;
/*      */   private static final int PEEKED_LONG = 15;
/*      */   private static final int PEEKED_NUMBER = 16;
/*      */   private static final int PEEKED_EOF = 17;
/*      */   private static final int NUMBER_CHAR_NONE = 0;
/*      */   private static final int NUMBER_CHAR_SIGN = 1;
/*      */   private static final int NUMBER_CHAR_DIGIT = 2;
/*      */   private static final int NUMBER_CHAR_DECIMAL = 3;
/*      */   private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
/*      */   private static final int NUMBER_CHAR_EXP_E = 5;
/*      */   private static final int NUMBER_CHAR_EXP_SIGN = 6;
/*      */   private static final int NUMBER_CHAR_EXP_DIGIT = 7;
/*      */   private final Reader in;
/*      */   private boolean lenient = false;
/*  238 */   private final char[] buffer = new char[1024];
/*  239 */   private int pos = 0;
/*  240 */   private int limit = 0;
/*      */   
/*  242 */   private int lineNumber = 0;
/*  243 */   private int lineStart = 0;
/*      */   
/*  245 */   private int peeked = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long peekedLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int peekedNumberLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String peekedString;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  269 */   private int[] stack = new int[32];
/*  270 */   private int stackSize = 0;
/*      */   public JsonReader(Reader in) {
/*  272 */     this.stack[this.stackSize++] = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  279 */     if (in == null) {
/*  280 */       throw new NullPointerException("in == null");
/*      */     }
/*  282 */     this.in = in;
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
/*      */   public final void setLenient(boolean lenient) {
/*  315 */     this.lenient = lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLenient() {
/*  322 */     return this.lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginArray() throws IOException {
/*  330 */     int p = this.peeked;
/*  331 */     if (p == 0) {
/*  332 */       p = doPeek();
/*      */     }
/*  334 */     if (p == 3) {
/*  335 */       push(1);
/*  336 */       this.peeked = 0;
/*      */     } else {
/*  338 */       throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endArray() throws IOException {
/*  348 */     int p = this.peeked;
/*  349 */     if (p == 0) {
/*  350 */       p = doPeek();
/*      */     }
/*  352 */     if (p == 4) {
/*  353 */       this.stackSize--;
/*  354 */       this.peeked = 0;
/*      */     } else {
/*  356 */       throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginObject() throws IOException {
/*  366 */     int p = this.peeked;
/*  367 */     if (p == 0) {
/*  368 */       p = doPeek();
/*      */     }
/*  370 */     if (p == 1) {
/*  371 */       push(3);
/*  372 */       this.peeked = 0;
/*      */     } else {
/*  374 */       throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endObject() throws IOException {
/*  384 */     int p = this.peeked;
/*  385 */     if (p == 0) {
/*  386 */       p = doPeek();
/*      */     }
/*  388 */     if (p == 2) {
/*  389 */       this.stackSize--;
/*  390 */       this.peeked = 0;
/*      */     } else {
/*  392 */       throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNext() throws IOException {
/*  401 */     int p = this.peeked;
/*  402 */     if (p == 0) {
/*  403 */       p = doPeek();
/*      */     }
/*  405 */     return (p != 2 && p != 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonToken peek() throws IOException {
/*  412 */     int p = this.peeked;
/*  413 */     if (p == 0) {
/*  414 */       p = doPeek();
/*      */     }
/*      */     
/*  417 */     switch (p) {
/*      */       case 1:
/*  419 */         return JsonToken.BEGIN_OBJECT;
/*      */       case 2:
/*  421 */         return JsonToken.END_OBJECT;
/*      */       case 3:
/*  423 */         return JsonToken.BEGIN_ARRAY;
/*      */       case 4:
/*  425 */         return JsonToken.END_ARRAY;
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*  429 */         return JsonToken.NAME;
/*      */       case 5:
/*      */       case 6:
/*  432 */         return JsonToken.BOOLEAN;
/*      */       case 7:
/*  434 */         return JsonToken.NULL;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*  439 */         return JsonToken.STRING;
/*      */       case 15:
/*      */       case 16:
/*  442 */         return JsonToken.NUMBER;
/*      */       case 17:
/*  444 */         return JsonToken.END_DOCUMENT;
/*      */     } 
/*  446 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   
/*      */   private int doPeek() throws IOException {
/*  451 */     int peekStack = this.stack[this.stackSize - 1];
/*  452 */     if (peekStack == 1)
/*  453 */     { this.stack[this.stackSize - 1] = 2; }
/*  454 */     else if (peekStack == 2)
/*      */     
/*  456 */     { int i = nextNonWhitespace(true);
/*  457 */       switch (i) {
/*      */         case 93:
/*  459 */           return this.peeked = 4;
/*      */         case 59:
/*  461 */           checkLenient(); break;
/*      */         case 44:
/*      */           break;
/*      */         default:
/*  465 */           throw syntaxError("Unterminated array");
/*      */       }  }
/*  467 */     else { if (peekStack == 3 || peekStack == 5) {
/*  468 */         this.stack[this.stackSize - 1] = 4;
/*      */         
/*  470 */         if (peekStack == 5) {
/*  471 */           int j = nextNonWhitespace(true);
/*  472 */           switch (j) {
/*      */             case 125:
/*  474 */               return this.peeked = 2;
/*      */             case 59:
/*  476 */               checkLenient(); break;
/*      */             case 44:
/*      */               break;
/*      */             default:
/*  480 */               throw syntaxError("Unterminated object");
/*      */           } 
/*      */         } 
/*  483 */         int i = nextNonWhitespace(true);
/*  484 */         switch (i) {
/*      */           case 34:
/*  486 */             return this.peeked = 13;
/*      */           case 39:
/*  488 */             checkLenient();
/*  489 */             return this.peeked = 12;
/*      */           case 125:
/*  491 */             if (peekStack != 5) {
/*  492 */               return this.peeked = 2;
/*      */             }
/*  494 */             throw syntaxError("Expected name");
/*      */         } 
/*      */         
/*  497 */         checkLenient();
/*  498 */         this.pos--;
/*  499 */         if (isLiteral((char)i)) {
/*  500 */           return this.peeked = 14;
/*      */         }
/*  502 */         throw syntaxError("Expected name");
/*      */       } 
/*      */       
/*  505 */       if (peekStack == 4) {
/*  506 */         this.stack[this.stackSize - 1] = 5;
/*      */         
/*  508 */         int i = nextNonWhitespace(true);
/*  509 */         switch (i) {
/*      */           case 58:
/*      */             break;
/*      */           case 61:
/*  513 */             checkLenient();
/*  514 */             if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
/*  515 */               this.pos++;
/*      */             }
/*      */             break;
/*      */           default:
/*  519 */             throw syntaxError("Expected ':'");
/*      */         } 
/*  521 */       } else if (peekStack == 6) {
/*  522 */         if (this.lenient) {
/*  523 */           consumeNonExecutePrefix();
/*      */         }
/*  525 */         this.stack[this.stackSize - 1] = 7;
/*  526 */       } else if (peekStack == 7) {
/*  527 */         int i = nextNonWhitespace(false);
/*  528 */         if (i == -1) {
/*  529 */           return this.peeked = 17;
/*      */         }
/*  531 */         checkLenient();
/*  532 */         this.pos--;
/*      */       }
/*  534 */       else if (peekStack == 8) {
/*  535 */         throw new IllegalStateException("JsonReader is closed");
/*      */       }  }
/*      */     
/*  538 */     int c = nextNonWhitespace(true);
/*  539 */     switch (c) {
/*      */       case 93:
/*  541 */         if (peekStack == 1) {
/*  542 */           return this.peeked = 4;
/*      */         }
/*      */ 
/*      */       
/*      */       case 44:
/*      */       case 59:
/*  548 */         if (peekStack == 1 || peekStack == 2) {
/*  549 */           checkLenient();
/*  550 */           this.pos--;
/*  551 */           return this.peeked = 7;
/*      */         } 
/*  553 */         throw syntaxError("Unexpected value");
/*      */       
/*      */       case 39:
/*  556 */         checkLenient();
/*  557 */         return this.peeked = 8;
/*      */       case 34:
/*  559 */         if (this.stackSize == 1) {
/*  560 */           checkLenient();
/*      */         }
/*  562 */         return this.peeked = 9;
/*      */       case 91:
/*  564 */         return this.peeked = 3;
/*      */       case 123:
/*  566 */         return this.peeked = 1;
/*      */     } 
/*  568 */     this.pos--;
/*      */ 
/*      */     
/*  571 */     if (this.stackSize == 1) {
/*  572 */       checkLenient();
/*      */     }
/*      */     
/*  575 */     int result = peekKeyword();
/*  576 */     if (result != 0) {
/*  577 */       return result;
/*      */     }
/*      */     
/*  580 */     result = peekNumber();
/*  581 */     if (result != 0) {
/*  582 */       return result;
/*      */     }
/*      */     
/*  585 */     if (!isLiteral(this.buffer[this.pos])) {
/*  586 */       throw syntaxError("Expected value");
/*      */     }
/*      */     
/*  589 */     checkLenient();
/*  590 */     return this.peeked = 10;
/*      */   }
/*      */   private int peekKeyword() throws IOException {
/*      */     String keyword, keywordUpper;
/*      */     int peeking;
/*  595 */     char c = this.buffer[this.pos];
/*      */ 
/*      */ 
/*      */     
/*  599 */     if (c == 't' || c == 'T') {
/*  600 */       keyword = "true";
/*  601 */       keywordUpper = "TRUE";
/*  602 */       peeking = 5;
/*  603 */     } else if (c == 'f' || c == 'F') {
/*  604 */       keyword = "false";
/*  605 */       keywordUpper = "FALSE";
/*  606 */       peeking = 6;
/*  607 */     } else if (c == 'n' || c == 'N') {
/*  608 */       keyword = "null";
/*  609 */       keywordUpper = "NULL";
/*  610 */       peeking = 7;
/*      */     } else {
/*  612 */       return 0;
/*      */     } 
/*      */ 
/*      */     
/*  616 */     int length = keyword.length();
/*  617 */     for (int i = 1; i < length; i++) {
/*  618 */       if (this.pos + i >= this.limit && !fillBuffer(i + 1)) {
/*  619 */         return 0;
/*      */       }
/*  621 */       c = this.buffer[this.pos + i];
/*  622 */       if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
/*  623 */         return 0;
/*      */       }
/*      */     } 
/*      */     
/*  627 */     if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length]))
/*      */     {
/*  629 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  633 */     this.pos += length;
/*  634 */     return this.peeked = peeking;
/*      */   }
/*      */   
/*      */   private int peekNumber() throws IOException {
/*      */     int j;
/*  639 */     char[] buffer = this.buffer;
/*  640 */     int p = this.pos;
/*  641 */     int l = this.limit;
/*      */     
/*  643 */     long value = 0L;
/*  644 */     boolean negative = false;
/*  645 */     boolean fitsInLong = true;
/*  646 */     int last = 0;
/*      */     
/*  648 */     int i = 0;
/*      */ 
/*      */     
/*  651 */     for (;; i++) {
/*  652 */       if (p + i == l) {
/*  653 */         if (i == buffer.length)
/*      */         {
/*      */           
/*  656 */           return 0;
/*      */         }
/*  658 */         if (!fillBuffer(i + 1)) {
/*      */           break;
/*      */         }
/*  661 */         p = this.pos;
/*  662 */         l = this.limit;
/*      */       } 
/*      */       
/*  665 */       char c = buffer[p + i];
/*  666 */       switch (c) {
/*      */         case '-':
/*  668 */           if (last == 0) {
/*  669 */             negative = true;
/*  670 */             last = 1; break;
/*      */           } 
/*  672 */           if (last == 5) {
/*  673 */             last = 6;
/*      */             break;
/*      */           } 
/*  676 */           return 0;
/*      */         
/*      */         case '+':
/*  679 */           if (last == 5) {
/*  680 */             last = 6;
/*      */             break;
/*      */           } 
/*  683 */           return 0;
/*      */         
/*      */         case 'E':
/*      */         case 'e':
/*  687 */           if (last == 2 || last == 4) {
/*  688 */             last = 5;
/*      */             break;
/*      */           } 
/*  691 */           return 0;
/*      */         
/*      */         case '.':
/*  694 */           if (last == 2) {
/*  695 */             last = 3;
/*      */             break;
/*      */           } 
/*  698 */           return 0;
/*      */         
/*      */         default:
/*  701 */           if (c < '0' || c > '9') {
/*  702 */             if (!isLiteral(c)) {
/*      */               break;
/*      */             }
/*  705 */             return 0;
/*      */           } 
/*  707 */           if (last == 1 || last == 0) {
/*  708 */             value = -(c - 48);
/*  709 */             last = 2; break;
/*  710 */           }  if (last == 2) {
/*  711 */             if (value == 0L) {
/*  712 */               return 0;
/*      */             }
/*  714 */             long newValue = value * 10L - (c - 48);
/*  715 */             j = fitsInLong & ((value > -922337203685477580L || (value == -922337203685477580L && newValue < value)) ? 1 : 0);
/*      */             
/*  717 */             value = newValue; break;
/*  718 */           }  if (last == 3) {
/*  719 */             last = 4; break;
/*  720 */           }  if (last == 5 || last == 6) {
/*  721 */             last = 7;
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  727 */     if (last == 2 && j != 0 && (value != Long.MIN_VALUE || negative)) {
/*  728 */       this.peekedLong = negative ? value : -value;
/*  729 */       this.pos += i;
/*  730 */       return this.peeked = 15;
/*  731 */     }  if (last == 2 || last == 4 || last == 7) {
/*      */       
/*  733 */       this.peekedNumberLength = i;
/*  734 */       return this.peeked = 16;
/*      */     } 
/*  736 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isLiteral(char c) throws IOException {
/*  741 */     switch (c) {
/*      */       case '#':
/*      */       case '/':
/*      */       case ';':
/*      */       case '=':
/*      */       case '\\':
/*  747 */         checkLenient();
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*      */       case ' ':
/*      */       case ',':
/*      */       case ':':
/*      */       case '[':
/*      */       case ']':
/*      */       case '{':
/*      */       case '}':
/*  759 */         return false;
/*      */     } 
/*  761 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextName() throws IOException {
/*      */     String result;
/*  773 */     int p = this.peeked;
/*  774 */     if (p == 0) {
/*  775 */       p = doPeek();
/*      */     }
/*      */     
/*  778 */     if (p == 14) {
/*  779 */       result = nextUnquotedValue();
/*  780 */     } else if (p == 12) {
/*  781 */       result = nextQuotedValue('\'');
/*  782 */     } else if (p == 13) {
/*  783 */       result = nextQuotedValue('"');
/*      */     } else {
/*  785 */       throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */     
/*  788 */     this.peeked = 0;
/*  789 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextString() throws IOException {
/*      */     String result;
/*  801 */     int p = this.peeked;
/*  802 */     if (p == 0) {
/*  803 */       p = doPeek();
/*      */     }
/*      */     
/*  806 */     if (p == 10) {
/*  807 */       result = nextUnquotedValue();
/*  808 */     } else if (p == 8) {
/*  809 */       result = nextQuotedValue('\'');
/*  810 */     } else if (p == 9) {
/*  811 */       result = nextQuotedValue('"');
/*  812 */     } else if (p == 11) {
/*  813 */       result = this.peekedString;
/*  814 */       this.peekedString = null;
/*  815 */     } else if (p == 15) {
/*  816 */       result = Long.toString(this.peekedLong);
/*  817 */     } else if (p == 16) {
/*  818 */       result = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  819 */       this.pos += this.peekedNumberLength;
/*      */     } else {
/*  821 */       throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */     
/*  824 */     this.peeked = 0;
/*  825 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nextBoolean() throws IOException {
/*  836 */     int p = this.peeked;
/*  837 */     if (p == 0) {
/*  838 */       p = doPeek();
/*      */     }
/*  840 */     if (p == 5) {
/*  841 */       this.peeked = 0;
/*  842 */       return true;
/*  843 */     }  if (p == 6) {
/*  844 */       this.peeked = 0;
/*  845 */       return false;
/*      */     } 
/*  847 */     throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
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
/*      */   public void nextNull() throws IOException {
/*  859 */     int p = this.peeked;
/*  860 */     if (p == 0) {
/*  861 */       p = doPeek();
/*      */     }
/*  863 */     if (p == 7) {
/*  864 */       this.peeked = 0;
/*      */     } else {
/*  866 */       throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
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
/*      */   public double nextDouble() throws IOException {
/*  881 */     int p = this.peeked;
/*  882 */     if (p == 0) {
/*  883 */       p = doPeek();
/*      */     }
/*      */     
/*  886 */     if (p == 15) {
/*  887 */       this.peeked = 0;
/*  888 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  891 */     if (p == 16) {
/*  892 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  893 */       this.pos += this.peekedNumberLength;
/*  894 */     } else if (p == 8 || p == 9) {
/*  895 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*  896 */     } else if (p == 10) {
/*  897 */       this.peekedString = nextUnquotedValue();
/*  898 */     } else if (p != 11) {
/*  899 */       throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */ 
/*      */     
/*  903 */     this.peeked = 11;
/*  904 */     double result = Double.parseDouble(this.peekedString);
/*  905 */     if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
/*  906 */       throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     }
/*      */     
/*  909 */     this.peekedString = null;
/*  910 */     this.peeked = 0;
/*  911 */     return result;
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
/*      */   public long nextLong() throws IOException {
/*  925 */     int p = this.peeked;
/*  926 */     if (p == 0) {
/*  927 */       p = doPeek();
/*      */     }
/*      */     
/*  930 */     if (p == 15) {
/*  931 */       this.peeked = 0;
/*  932 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  935 */     if (p == 16) {
/*  936 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  937 */       this.pos += this.peekedNumberLength;
/*  938 */     } else if (p == 8 || p == 9) {
/*  939 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       try {
/*  941 */         long l = Long.parseLong(this.peekedString);
/*  942 */         this.peeked = 0;
/*  943 */         return l;
/*  944 */       } catch (NumberFormatException ignored) {}
/*      */     }
/*      */     else {
/*      */       
/*  948 */       throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */ 
/*      */     
/*  952 */     this.peeked = 11;
/*  953 */     double asDouble = Double.parseDouble(this.peekedString);
/*  954 */     long result = (long)asDouble;
/*  955 */     if (result != asDouble) {
/*  956 */       throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     }
/*      */     
/*  959 */     this.peekedString = null;
/*  960 */     this.peeked = 0;
/*  961 */     return result;
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
/*      */   private String nextQuotedValue(char quote) throws IOException {
/*  976 */     char[] buffer = this.buffer;
/*  977 */     StringBuilder builder = new StringBuilder();
/*      */     while (true) {
/*  979 */       int p = this.pos;
/*  980 */       int l = this.limit;
/*      */       
/*  982 */       int start = p;
/*  983 */       while (p < l) {
/*  984 */         int c = buffer[p++];
/*      */         
/*  986 */         if (c == quote) {
/*  987 */           this.pos = p;
/*  988 */           builder.append(buffer, start, p - start - 1);
/*  989 */           return builder.toString();
/*  990 */         }  if (c == 92) {
/*  991 */           this.pos = p;
/*  992 */           builder.append(buffer, start, p - start - 1);
/*  993 */           builder.append(readEscapeCharacter());
/*  994 */           p = this.pos;
/*  995 */           l = this.limit;
/*  996 */           start = p; continue;
/*  997 */         }  if (c == 10) {
/*  998 */           this.lineNumber++;
/*  999 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/*      */       
/* 1003 */       builder.append(buffer, start, p - start);
/* 1004 */       this.pos = p;
/* 1005 */       if (!fillBuffer(1)) {
/* 1006 */         throw syntaxError("Unterminated string");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String nextUnquotedValue() throws IOException {
/*      */     String result;
/* 1016 */     StringBuilder builder = null;
/* 1017 */     int i = 0;
/*      */ 
/*      */     
/*      */     label34: while (true) {
/* 1021 */       for (; this.pos + i < this.limit; i++)
/* 1022 */       { switch (this.buffer[this.pos + i])
/*      */         { case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1028 */             checkLenient(); break label34;
/*      */           case '\t': break label34;
/*      */           case '\n': break label34;
/*      */           case '\f': break label34;
/*      */           case '\r': break label34;
/*      */           case ' ': break label34;
/*      */           case ',':
/*      */             break label34;
/*      */           case ':':
/*      */             break label34;
/*      */           case '[':
/*      */             break label34;
/*      */           case ']':
/*      */             break label34;
/*      */           case '{':
/*      */             break label34;
/*      */           case '}':
/* 1045 */             break label34; }  }  if (i < this.buffer.length) {
/* 1046 */         if (fillBuffer(i + 1)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1054 */       if (builder == null) {
/* 1055 */         builder = new StringBuilder();
/*      */       }
/* 1057 */       builder.append(this.buffer, this.pos, i);
/* 1058 */       this.pos += i;
/* 1059 */       i = 0;
/* 1060 */       if (!fillBuffer(1)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1066 */     if (builder == null) {
/* 1067 */       result = new String(this.buffer, this.pos, i);
/*      */     } else {
/* 1069 */       builder.append(this.buffer, this.pos, i);
/* 1070 */       result = builder.toString();
/*      */     } 
/* 1072 */     this.pos += i;
/* 1073 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private void skipQuotedValue(char quote) throws IOException {
/* 1078 */     char[] buffer = this.buffer;
/*      */     while (true) {
/* 1080 */       int p = this.pos;
/* 1081 */       int l = this.limit;
/*      */       
/* 1083 */       while (p < l) {
/* 1084 */         int c = buffer[p++];
/* 1085 */         if (c == quote) {
/* 1086 */           this.pos = p; return;
/*      */         } 
/* 1088 */         if (c == 92) {
/* 1089 */           this.pos = p;
/* 1090 */           readEscapeCharacter();
/* 1091 */           p = this.pos;
/* 1092 */           l = this.limit; continue;
/* 1093 */         }  if (c == 10) {
/* 1094 */           this.lineNumber++;
/* 1095 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/* 1098 */       this.pos = p;
/* 1099 */       if (!fillBuffer(1))
/* 1100 */         throw syntaxError("Unterminated string"); 
/*      */     } 
/*      */   }
/*      */   private void skipUnquotedValue() throws IOException {
/*      */     do {
/* 1105 */       int i = 0;
/* 1106 */       for (; this.pos + i < this.limit; i++) {
/* 1107 */         switch (this.buffer[this.pos + i]) {
/*      */           case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1113 */             checkLenient();
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\f':
/*      */           case '\r':
/*      */           case ' ':
/*      */           case ',':
/*      */           case ':':
/*      */           case '[':
/*      */           case ']':
/*      */           case '{':
/*      */           case '}':
/* 1125 */             this.pos += i;
/*      */             return;
/*      */         } 
/*      */       } 
/* 1129 */       this.pos += i;
/* 1130 */     } while (fillBuffer(1));
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
/*      */   public int nextInt() throws IOException {
/* 1144 */     int p = this.peeked;
/* 1145 */     if (p == 0) {
/* 1146 */       p = doPeek();
/*      */     }
/*      */ 
/*      */     
/* 1150 */     if (p == 15) {
/* 1151 */       int result = (int)this.peekedLong;
/* 1152 */       if (this.peekedLong != result) {
/* 1153 */         throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */       }
/*      */       
/* 1156 */       this.peeked = 0;
/* 1157 */       return result;
/*      */     } 
/*      */     
/* 1160 */     if (p == 16) {
/* 1161 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/* 1162 */       this.pos += this.peekedNumberLength;
/* 1163 */     } else if (p == 8 || p == 9) {
/* 1164 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       try {
/* 1166 */         int result = Integer.parseInt(this.peekedString);
/* 1167 */         this.peeked = 0;
/* 1168 */         return result;
/* 1169 */       } catch (NumberFormatException ignored) {}
/*      */     }
/*      */     else {
/*      */       
/* 1173 */       throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     } 
/*      */ 
/*      */     
/* 1177 */     this.peeked = 11;
/* 1178 */     double asDouble = Double.parseDouble(this.peekedString);
/* 1179 */     int i = (int)asDouble;
/* 1180 */     if (i != asDouble) {
/* 1181 */       throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     }
/*      */     
/* 1184 */     this.peekedString = null;
/* 1185 */     this.peeked = 0;
/* 1186 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/* 1193 */     this.peeked = 0;
/* 1194 */     this.stack[0] = 8;
/* 1195 */     this.stackSize = 1;
/* 1196 */     this.in.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void skipValue() throws IOException {
/* 1205 */     int count = 0;
/*      */     do {
/* 1207 */       int p = this.peeked;
/* 1208 */       if (p == 0) {
/* 1209 */         p = doPeek();
/*      */       }
/*      */       
/* 1212 */       if (p == 3) {
/* 1213 */         push(1);
/* 1214 */         count++;
/* 1215 */       } else if (p == 1) {
/* 1216 */         push(3);
/* 1217 */         count++;
/* 1218 */       } else if (p == 4) {
/* 1219 */         this.stackSize--;
/* 1220 */         count--;
/* 1221 */       } else if (p == 2) {
/* 1222 */         this.stackSize--;
/* 1223 */         count--;
/* 1224 */       } else if (p == 14 || p == 10) {
/* 1225 */         skipUnquotedValue();
/* 1226 */       } else if (p == 8 || p == 12) {
/* 1227 */         skipQuotedValue('\'');
/* 1228 */       } else if (p == 9 || p == 13) {
/* 1229 */         skipQuotedValue('"');
/* 1230 */       } else if (p == 16) {
/* 1231 */         this.pos += this.peekedNumberLength;
/*      */       } 
/* 1233 */       this.peeked = 0;
/* 1234 */     } while (count != 0);
/*      */   }
/*      */   
/*      */   private void push(int newTop) {
/* 1238 */     if (this.stackSize == this.stack.length) {
/* 1239 */       int[] newStack = new int[this.stackSize * 2];
/* 1240 */       System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
/* 1241 */       this.stack = newStack;
/*      */     } 
/* 1243 */     this.stack[this.stackSize++] = newTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fillBuffer(int minimum) throws IOException {
/* 1252 */     char[] buffer = this.buffer;
/* 1253 */     this.lineStart -= this.pos;
/* 1254 */     if (this.limit != this.pos) {
/* 1255 */       this.limit -= this.pos;
/* 1256 */       System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
/*      */     } else {
/* 1258 */       this.limit = 0;
/*      */     } 
/*      */     
/* 1261 */     this.pos = 0;
/*      */     int total;
/* 1263 */     while ((total = this.in.read(buffer, this.limit, buffer.length - this.limit)) != -1) {
/* 1264 */       this.limit += total;
/*      */ 
/*      */       
/* 1267 */       if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == '﻿') {
/* 1268 */         this.pos++;
/* 1269 */         this.lineStart++;
/* 1270 */         minimum++;
/*      */       } 
/*      */       
/* 1273 */       if (this.limit >= minimum) {
/* 1274 */         return true;
/*      */       }
/*      */     } 
/* 1277 */     return false;
/*      */   }
/*      */   
/*      */   private int getLineNumber() {
/* 1281 */     return this.lineNumber + 1;
/*      */   }
/*      */   
/*      */   private int getColumnNumber() {
/* 1285 */     return this.pos - this.lineStart + 1;
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
/*      */   private int nextNonWhitespace(boolean throwOnEof) throws IOException {
/* 1303 */     char[] buffer = this.buffer;
/* 1304 */     int p = this.pos;
/* 1305 */     int l = this.limit;
/*      */     while (true) {
/* 1307 */       if (p == l) {
/* 1308 */         this.pos = p;
/* 1309 */         if (!fillBuffer(1)) {
/*      */           break;
/*      */         }
/* 1312 */         p = this.pos;
/* 1313 */         l = this.limit;
/*      */       } 
/*      */       
/* 1316 */       int c = buffer[p++];
/* 1317 */       if (c == 10) {
/* 1318 */         this.lineNumber++;
/* 1319 */         this.lineStart = p; continue;
/*      */       } 
/* 1321 */       if (c == 32 || c == 13 || c == 9) {
/*      */         continue;
/*      */       }
/*      */       
/* 1325 */       if (c == 47) {
/* 1326 */         this.pos = p;
/* 1327 */         if (p == l) {
/* 1328 */           this.pos--;
/* 1329 */           boolean charsLoaded = fillBuffer(2);
/* 1330 */           this.pos++;
/* 1331 */           if (!charsLoaded) {
/* 1332 */             return c;
/*      */           }
/*      */         } 
/*      */         
/* 1336 */         checkLenient();
/* 1337 */         char peek = buffer[this.pos];
/* 1338 */         switch (peek) {
/*      */           
/*      */           case '*':
/* 1341 */             this.pos++;
/* 1342 */             if (!skipTo("*/")) {
/* 1343 */               throw syntaxError("Unterminated comment");
/*      */             }
/* 1345 */             p = this.pos + 2;
/* 1346 */             l = this.limit;
/*      */             continue;
/*      */ 
/*      */           
/*      */           case '/':
/* 1351 */             this.pos++;
/* 1352 */             skipToEndOfLine();
/* 1353 */             p = this.pos;
/* 1354 */             l = this.limit;
/*      */             continue;
/*      */         } 
/*      */         
/* 1358 */         return c;
/*      */       } 
/* 1360 */       if (c == 35) {
/* 1361 */         this.pos = p;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1367 */         checkLenient();
/* 1368 */         skipToEndOfLine();
/* 1369 */         p = this.pos;
/* 1370 */         l = this.limit; continue;
/*      */       } 
/* 1372 */       this.pos = p;
/* 1373 */       return c;
/*      */     } 
/*      */     
/* 1376 */     if (throwOnEof) {
/* 1377 */       throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     }
/*      */     
/* 1380 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkLenient() throws IOException {
/* 1385 */     if (!this.lenient) {
/* 1386 */       throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipToEndOfLine() throws IOException {
/* 1396 */     while (this.pos < this.limit || fillBuffer(1)) {
/* 1397 */       char c = this.buffer[this.pos++];
/* 1398 */       if (c == '\n') {
/* 1399 */         this.lineNumber++;
/* 1400 */         this.lineStart = this.pos; break;
/*      */       } 
/* 1402 */       if (c == '\r') {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipTo(String toFind) throws IOException {
/* 1413 */     for (; this.pos + toFind.length() <= this.limit || fillBuffer(toFind.length()); this.pos++) {
/* 1414 */       if (this.buffer[this.pos] == '\n') {
/* 1415 */         this.lineNumber++;
/* 1416 */         this.lineStart = this.pos + 1;
/*      */       } else {
/*      */         
/* 1419 */         int c = 0; while (true) { if (c < toFind.length()) {
/* 1420 */             if (this.buffer[this.pos + c] != toFind.charAt(c))
/*      */               break;  c++;
/*      */             continue;
/*      */           } 
/* 1424 */           return true; } 
/*      */       } 
/* 1426 */     }  return false;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1430 */     return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
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
/*      */   private char readEscapeCharacter() throws IOException {
/*      */     char result;
/*      */     int i, end;
/* 1444 */     if (this.pos == this.limit && !fillBuffer(1)) {
/* 1445 */       throw syntaxError("Unterminated escape sequence");
/*      */     }
/*      */     
/* 1448 */     char escaped = this.buffer[this.pos++];
/* 1449 */     switch (escaped) {
/*      */       case 'u':
/* 1451 */         if (this.pos + 4 > this.limit && !fillBuffer(4)) {
/* 1452 */           throw syntaxError("Unterminated escape sequence");
/*      */         }
/*      */         
/* 1455 */         result = Character.MIN_VALUE;
/* 1456 */         for (i = this.pos, end = i + 4; i < end; i++) {
/* 1457 */           char c = this.buffer[i];
/* 1458 */           result = (char)(result << 4);
/* 1459 */           if (c >= '0' && c <= '9') {
/* 1460 */             result = (char)(result + c - 48);
/* 1461 */           } else if (c >= 'a' && c <= 'f') {
/* 1462 */             result = (char)(result + c - 97 + 10);
/* 1463 */           } else if (c >= 'A' && c <= 'F') {
/* 1464 */             result = (char)(result + c - 65 + 10);
/*      */           } else {
/* 1466 */             throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
/*      */           } 
/*      */         } 
/* 1469 */         this.pos += 4;
/* 1470 */         return result;
/*      */       
/*      */       case 't':
/* 1473 */         return '\t';
/*      */       
/*      */       case 'b':
/* 1476 */         return '\b';
/*      */       
/*      */       case 'n':
/* 1479 */         return '\n';
/*      */       
/*      */       case 'r':
/* 1482 */         return '\r';
/*      */       
/*      */       case 'f':
/* 1485 */         return '\f';
/*      */       
/*      */       case '\n':
/* 1488 */         this.lineNumber++;
/* 1489 */         this.lineStart = this.pos;
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1496 */     return escaped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IOException syntaxError(String message) throws IOException {
/* 1505 */     throw new MalformedJsonException(message + " at line " + getLineNumber() + " column " + getColumnNumber());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeNonExecutePrefix() throws IOException {
/* 1514 */     nextNonWhitespace(true);
/* 1515 */     this.pos--;
/*      */     
/* 1517 */     if (this.pos + NON_EXECUTE_PREFIX.length > this.limit && !fillBuffer(NON_EXECUTE_PREFIX.length)) {
/*      */       return;
/*      */     }
/*      */     
/* 1521 */     for (int i = 0; i < NON_EXECUTE_PREFIX.length; i++) {
/* 1522 */       if (this.buffer[this.pos + i] != NON_EXECUTE_PREFIX[i]) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1528 */     this.pos += NON_EXECUTE_PREFIX.length;
/*      */   }
/*      */   
/*      */   static {
/* 1532 */     JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
/*      */         public void promoteNameToValue(JsonReader reader) throws IOException {
/* 1534 */           if (reader instanceof JsonTreeReader) {
/* 1535 */             ((JsonTreeReader)reader).promoteNameToValue();
/*      */             return;
/*      */           } 
/* 1538 */           int p = reader.peeked;
/* 1539 */           if (p == 0) {
/* 1540 */             p = reader.doPeek();
/*      */           }
/* 1542 */           if (p == 13) {
/* 1543 */             reader.peeked = 9;
/* 1544 */           } else if (p == 12) {
/* 1545 */             reader.peeked = 8;
/* 1546 */           } else if (p == 14) {
/* 1547 */             reader.peeked = 10;
/*      */           } else {
/* 1549 */             throw new IllegalStateException("Expected a name but was " + reader.peek() + " " + " at line " + reader.getLineNumber() + " column " + reader.getColumnNumber());
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\stream\JsonReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */