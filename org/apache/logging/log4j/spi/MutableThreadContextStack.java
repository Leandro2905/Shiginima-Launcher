/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutableThreadContextStack
/*     */   implements ThreadContextStack
/*     */ {
/*     */   private static final long serialVersionUID = 50505011L;
/*     */   private final List<String> list;
/*     */   private boolean frozen;
/*     */   
/*     */   public MutableThreadContextStack() {
/*  43 */     this(new ArrayList<String>());
/*     */   }
/*     */   
/*     */   public MutableThreadContextStack(List<String> list) {
/*  47 */     this.list = new ArrayList<String>(list);
/*     */   }
/*     */   
/*     */   private MutableThreadContextStack(MutableThreadContextStack stack) {
/*  51 */     this.list = new ArrayList<String>(stack.list);
/*     */   }
/*     */   
/*     */   private void checkInvariants() {
/*  55 */     if (this.frozen) {
/*  56 */       throw new UnsupportedOperationException("context stack has been frozen");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/*  62 */     checkInvariants();
/*  63 */     if (this.list.isEmpty()) {
/*  64 */       return null;
/*     */     }
/*  66 */     int last = this.list.size() - 1;
/*  67 */     String result = this.list.remove(last);
/*  68 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String peek() {
/*  73 */     if (this.list.isEmpty()) {
/*  74 */       return null;
/*     */     }
/*  76 */     int last = this.list.size() - 1;
/*  77 */     return this.list.get(last);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String message) {
/*  82 */     checkInvariants();
/*  83 */     this.list.add(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/*  88 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> asList() {
/*  93 */     return this.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void trim(int depth) {
/*  98 */     checkInvariants();
/*  99 */     if (depth < 0) {
/* 100 */       throw new IllegalArgumentException("Maximum stack depth cannot be negative");
/*     */     }
/* 102 */     if (this.list == null) {
/*     */       return;
/*     */     }
/* 105 */     List<String> copy = new ArrayList<String>(this.list.size());
/* 106 */     int count = Math.min(depth, this.list.size());
/* 107 */     for (int i = 0; i < count; i++) {
/* 108 */       copy.add(this.list.get(i));
/*     */     }
/* 110 */     this.list.clear();
/* 111 */     this.list.addAll(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContextStack copy() {
/* 116 */     return new MutableThreadContextStack(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 121 */     checkInvariants();
/* 122 */     this.list.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 127 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 132 */     return this.list.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 137 */     return this.list.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 142 */     return this.list.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 147 */     return this.list.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] ts) {
/* 152 */     return this.list.toArray(ts);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(String s) {
/* 157 */     checkInvariants();
/* 158 */     return this.list.add(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 163 */     checkInvariants();
/* 164 */     return this.list.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/* 169 */     return this.list.containsAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends String> strings) {
/* 174 */     checkInvariants();
/* 175 */     return this.list.addAll(strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 180 */     checkInvariants();
/* 181 */     return this.list.removeAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 186 */     checkInvariants();
/* 187 */     return this.list.retainAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 192 */     return String.valueOf(this.list);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     int prime = 31;
/* 198 */     int result = 1;
/* 199 */     result = 31 * result + ((this.list == null) ? 0 : this.list.hashCode());
/* 200 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 205 */     if (this == obj) {
/* 206 */       return true;
/*     */     }
/* 208 */     if (obj == null) {
/* 209 */       return false;
/*     */     }
/* 211 */     if (!(obj instanceof ThreadContextStack)) {
/* 212 */       return false;
/*     */     }
/* 214 */     ThreadContextStack other = (ThreadContextStack)obj;
/* 215 */     List<String> otherAsList = other.asList();
/* 216 */     if (this.list == null) {
/* 217 */       if (otherAsList != null) {
/* 218 */         return false;
/*     */       }
/* 220 */     } else if (!this.list.equals(otherAsList)) {
/* 221 */       return false;
/*     */     } 
/* 223 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 228 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 235 */     this.frozen = true;
/*     */   }
/*     */   
/*     */   public boolean isFrozen() {
/* 239 */     return this.frozen;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\MutableThreadContextStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */