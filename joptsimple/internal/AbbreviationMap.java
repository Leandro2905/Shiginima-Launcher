/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public class AbbreviationMap<V>
/*     */ {
/*     */   private String key;
/*     */   private V value;
/*  62 */   private final Map<Character, AbbreviationMap<V>> children = new TreeMap<Character, AbbreviationMap<V>>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int keysBeyond;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String aKey) {
/*  74 */     return (get(aKey) != null);
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
/*     */   public V get(String aKey) {
/*  87 */     char[] chars = charsOf(aKey);
/*     */     
/*  89 */     AbbreviationMap<V> child = this;
/*  90 */     for (char each : chars) {
/*  91 */       child = child.children.get(Character.valueOf(each));
/*  92 */       if (child == null) {
/*  93 */         return null;
/*     */       }
/*     */     } 
/*  96 */     return child.value;
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
/*     */   public void put(String aKey, V newValue) {
/* 109 */     if (newValue == null)
/* 110 */       throw new NullPointerException(); 
/* 111 */     if (aKey.length() == 0) {
/* 112 */       throw new IllegalArgumentException();
/*     */     }
/* 114 */     char[] chars = charsOf(aKey);
/* 115 */     add(chars, newValue, 0, chars.length);
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
/*     */   public void putAll(Iterable<String> keys, V newValue) {
/* 128 */     for (String each : keys)
/* 129 */       put(each, newValue); 
/*     */   }
/*     */   
/*     */   private boolean add(char[] chars, V newValue, int offset, int length) {
/* 133 */     if (offset == length) {
/* 134 */       this.value = newValue;
/* 135 */       boolean wasAlreadyAKey = (this.key != null);
/* 136 */       this.key = new String(chars);
/* 137 */       return !wasAlreadyAKey;
/*     */     } 
/*     */     
/* 140 */     char nextChar = chars[offset];
/* 141 */     AbbreviationMap<V> child = this.children.get(Character.valueOf(nextChar));
/* 142 */     if (child == null) {
/* 143 */       child = new AbbreviationMap();
/* 144 */       this.children.put(Character.valueOf(nextChar), child);
/*     */     } 
/*     */     
/* 147 */     boolean newKeyAdded = child.add(chars, newValue, offset + 1, length);
/*     */     
/* 149 */     if (newKeyAdded) {
/* 150 */       this.keysBeyond++;
/*     */     }
/* 152 */     if (this.key == null) {
/* 153 */       this.value = (this.keysBeyond > 1) ? null : newValue;
/*     */     }
/* 155 */     return newKeyAdded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String aKey) {
/* 166 */     if (aKey.length() == 0) {
/* 167 */       throw new IllegalArgumentException();
/*     */     }
/* 169 */     char[] keyChars = charsOf(aKey);
/* 170 */     remove(keyChars, 0, keyChars.length);
/*     */   }
/*     */   
/*     */   private boolean remove(char[] aKey, int offset, int length) {
/* 174 */     if (offset == length) {
/* 175 */       return removeAtEndOfKey();
/*     */     }
/* 177 */     char nextChar = aKey[offset];
/* 178 */     AbbreviationMap<V> child = this.children.get(Character.valueOf(nextChar));
/* 179 */     if (child == null || !child.remove(aKey, offset + 1, length)) {
/* 180 */       return false;
/*     */     }
/* 182 */     this.keysBeyond--;
/* 183 */     if (child.keysBeyond == 0)
/* 184 */       this.children.remove(Character.valueOf(nextChar)); 
/* 185 */     if (this.keysBeyond == 1 && this.key == null) {
/* 186 */       setValueToThatOfOnlyChild();
/*     */     }
/* 188 */     return true;
/*     */   }
/*     */   
/*     */   private void setValueToThatOfOnlyChild() {
/* 192 */     Map.Entry<Character, AbbreviationMap<V>> entry = this.children.entrySet().iterator().next();
/* 193 */     AbbreviationMap<V> onlyChild = entry.getValue();
/* 194 */     this.value = onlyChild.value;
/*     */   }
/*     */   
/*     */   private boolean removeAtEndOfKey() {
/* 198 */     if (this.key == null) {
/* 199 */       return false;
/*     */     }
/* 201 */     this.key = null;
/* 202 */     if (this.keysBeyond == 1) {
/* 203 */       setValueToThatOfOnlyChild();
/*     */     } else {
/* 205 */       this.value = null;
/*     */     } 
/* 207 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, V> toJavaUtilMap() {
/* 216 */     Map<String, V> mappings = new TreeMap<String, V>();
/* 217 */     addToMappings(mappings);
/* 218 */     return mappings;
/*     */   }
/*     */   
/*     */   private void addToMappings(Map<String, V> mappings) {
/* 222 */     if (this.key != null) {
/* 223 */       mappings.put(this.key, this.value);
/*     */     }
/* 225 */     for (AbbreviationMap<V> each : this.children.values())
/* 226 */       each.addToMappings(mappings); 
/*     */   }
/*     */   
/*     */   private static char[] charsOf(String aKey) {
/* 230 */     char[] chars = new char[aKey.length()];
/* 231 */     aKey.getChars(0, aKey.length(), chars, 0);
/* 232 */     return chars;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\AbbreviationMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */