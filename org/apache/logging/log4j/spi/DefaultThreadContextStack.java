/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.logging.log4j.ThreadContext;
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
/*     */ public class DefaultThreadContextStack
/*     */   implements ThreadContextStack
/*     */ {
/*     */   private static final long serialVersionUID = 5050501L;
/*  36 */   private static final ThreadLocal<MutableThreadContextStack> stack = new ThreadLocal<MutableThreadContextStack>();
/*     */   
/*     */   private final boolean useStack;
/*     */   
/*     */   public DefaultThreadContextStack(boolean useStack) {
/*  41 */     this.useStack = useStack;
/*     */   }
/*     */   
/*     */   private MutableThreadContextStack getNonNullStackCopy() {
/*  45 */     MutableThreadContextStack values = stack.get();
/*  46 */     return (values == null) ? new MutableThreadContextStack() : (MutableThreadContextStack)values.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(String s) {
/*  51 */     if (!this.useStack) {
/*  52 */       return false;
/*     */     }
/*  54 */     MutableThreadContextStack copy = getNonNullStackCopy();
/*  55 */     copy.add(s);
/*  56 */     copy.freeze();
/*  57 */     stack.set(copy);
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends String> strings) {
/*  63 */     if (!this.useStack || strings.isEmpty()) {
/*  64 */       return false;
/*     */     }
/*  66 */     MutableThreadContextStack copy = getNonNullStackCopy();
/*  67 */     copy.addAll(strings);
/*  68 */     copy.freeze();
/*  69 */     stack.set(copy);
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> asList() {
/*  75 */     MutableThreadContextStack values = stack.get();
/*  76 */     if (values == null) {
/*  77 */       return Collections.emptyList();
/*     */     }
/*  79 */     return values.asList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  84 */     stack.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*  89 */     MutableThreadContextStack values = stack.get();
/*  90 */     return (values != null && values.contains(o));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/*  95 */     if (objects.isEmpty()) {
/*  96 */       return true;
/*     */     }
/*     */     
/*  99 */     MutableThreadContextStack values = stack.get();
/* 100 */     return (values != null && values.containsAll(objects));
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContextStack copy() {
/* 105 */     MutableThreadContextStack values = null;
/* 106 */     if (!this.useStack || (values = stack.get()) == null) {
/* 107 */       return new MutableThreadContextStack();
/*     */     }
/* 109 */     return values.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 114 */     if (this == obj) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (obj == null) {
/* 118 */       return false;
/*     */     }
/* 120 */     if (obj instanceof DefaultThreadContextStack) {
/* 121 */       DefaultThreadContextStack defaultThreadContextStack = (DefaultThreadContextStack)obj;
/* 122 */       if (this.useStack != defaultThreadContextStack.useStack) {
/* 123 */         return false;
/*     */       }
/*     */     } 
/* 126 */     if (!(obj instanceof ThreadContextStack)) {
/* 127 */       return false;
/*     */     }
/* 129 */     ThreadContextStack other = (ThreadContextStack)obj;
/* 130 */     MutableThreadContextStack values = stack.get();
/* 131 */     if (values == null) {
/* 132 */       return (other == null);
/*     */     }
/* 134 */     return values.equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 139 */     MutableThreadContextStack values = stack.get();
/* 140 */     return (values == null) ? 0 : values.getDepth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 145 */     MutableThreadContextStack values = stack.get();
/* 146 */     int prime = 31;
/* 147 */     int result = 1;
/*     */     
/* 149 */     result = 31 * result + ((values == null) ? 0 : values.hashCode());
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 155 */     MutableThreadContextStack values = stack.get();
/* 156 */     return (values == null || values.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 161 */     MutableThreadContextStack values = stack.get();
/* 162 */     if (values == null) {
/* 163 */       List<String> empty = Collections.emptyList();
/* 164 */       return empty.iterator();
/*     */     } 
/* 166 */     return values.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String peek() {
/* 171 */     MutableThreadContextStack values = stack.get();
/* 172 */     if (values == null || values.size() == 0) {
/* 173 */       return null;
/*     */     }
/* 175 */     return values.peek();
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/* 180 */     if (!this.useStack) {
/* 181 */       return "";
/*     */     }
/* 183 */     MutableThreadContextStack values = stack.get();
/* 184 */     if (values == null || values.size() == 0) {
/* 185 */       throw new NoSuchElementException("The ThreadContext stack is empty");
/*     */     }
/* 187 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 188 */     String result = copy.pop();
/* 189 */     copy.freeze();
/* 190 */     stack.set(copy);
/* 191 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String message) {
/* 196 */     if (!this.useStack) {
/*     */       return;
/*     */     }
/* 199 */     add(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 204 */     if (!this.useStack) {
/* 205 */       return false;
/*     */     }
/* 207 */     MutableThreadContextStack values = stack.get();
/* 208 */     if (values == null || values.size() == 0) {
/* 209 */       return false;
/*     */     }
/* 211 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 212 */     boolean result = copy.remove(o);
/* 213 */     copy.freeze();
/* 214 */     stack.set(copy);
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 220 */     if (!this.useStack || objects.isEmpty()) {
/* 221 */       return false;
/*     */     }
/* 223 */     MutableThreadContextStack values = stack.get();
/* 224 */     if (values == null || values.isEmpty()) {
/* 225 */       return false;
/*     */     }
/* 227 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 228 */     boolean result = copy.removeAll(objects);
/* 229 */     copy.freeze();
/* 230 */     stack.set(copy);
/* 231 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 236 */     if (!this.useStack || objects.isEmpty()) {
/* 237 */       return false;
/*     */     }
/* 239 */     MutableThreadContextStack values = stack.get();
/* 240 */     if (values == null || values.isEmpty()) {
/* 241 */       return false;
/*     */     }
/* 243 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 244 */     boolean result = copy.retainAll(objects);
/* 245 */     copy.freeze();
/* 246 */     stack.set(copy);
/* 247 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 252 */     MutableThreadContextStack values = stack.get();
/* 253 */     return (values == null) ? 0 : values.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 258 */     MutableThreadContextStack result = stack.get();
/* 259 */     if (result == null) {
/* 260 */       return (Object[])new String[0];
/*     */     }
/* 262 */     return result.toArray(new Object[result.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] ts) {
/* 267 */     MutableThreadContextStack result = stack.get();
/* 268 */     if (result == null) {
/* 269 */       if (ts.length > 0) {
/* 270 */         ts[0] = null;
/*     */       }
/* 272 */       return ts;
/*     */     } 
/* 274 */     return result.toArray(ts);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     MutableThreadContextStack values = stack.get();
/* 280 */     return (values == null) ? "[]" : values.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void trim(int depth) {
/* 285 */     if (depth < 0) {
/* 286 */       throw new IllegalArgumentException("Maximum stack depth cannot be negative");
/*     */     }
/* 288 */     MutableThreadContextStack values = stack.get();
/* 289 */     if (values == null) {
/*     */       return;
/*     */     }
/* 292 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 293 */     copy.trim(depth);
/* 294 */     copy.freeze();
/* 295 */     stack.set(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 303 */     return stack.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\DefaultThreadContextStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */