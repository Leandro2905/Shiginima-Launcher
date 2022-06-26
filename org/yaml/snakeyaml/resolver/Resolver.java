/*     */ package org.yaml.snakeyaml.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public class Resolver
/*     */ {
/*  32 */   public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static final Pattern FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
/*     */   
/*  41 */   public static final Pattern INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
/*     */   
/*  43 */   public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
/*  44 */   public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
/*  45 */   public static final Pattern EMPTY = Pattern.compile("^$");
/*  46 */   public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
/*     */   
/*  48 */   public static final Pattern VALUE = Pattern.compile("^(?:=)$");
/*  49 */   public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
/*     */   
/*  51 */   protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<Character, List<ResolverTuple>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resolver(boolean respectDefaultImplicitScalars) {
/*  61 */     if (respectDefaultImplicitScalars) {
/*  62 */       addImplicitResolvers();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addImplicitResolvers() {
/*  67 */     addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     addImplicitResolver(Tag.INT, INT, "-+0123456789");
/*  74 */     addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
/*  75 */     addImplicitResolver(Tag.MERGE, MERGE, "<");
/*  76 */     addImplicitResolver(Tag.NULL, NULL, "~nN\000");
/*  77 */     addImplicitResolver(Tag.NULL, EMPTY, null);
/*  78 */     addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     addImplicitResolver(Tag.YAML, YAML, "!&*");
/*     */   }
/*     */   
/*     */   public Resolver() {
/*  87 */     this(true);
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/*  91 */     if (first == null) {
/*  92 */       List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
/*  93 */       if (curr == null) {
/*  94 */         curr = new ArrayList<ResolverTuple>();
/*  95 */         this.yamlImplicitResolvers.put(null, curr);
/*     */       } 
/*  97 */       curr.add(new ResolverTuple(tag, regexp));
/*     */     } else {
/*  99 */       char[] chrs = first.toCharArray();
/* 100 */       for (int i = 0, j = chrs.length; i < j; i++) {
/* 101 */         Character theC = Character.valueOf(chrs[i]);
/* 102 */         if (theC.charValue() == '\000')
/*     */         {
/* 104 */           theC = null;
/*     */         }
/* 106 */         List<ResolverTuple> curr = this.yamlImplicitResolvers.get(theC);
/* 107 */         if (curr == null) {
/* 108 */           curr = new ArrayList<ResolverTuple>();
/* 109 */           this.yamlImplicitResolvers.put(theC, curr);
/*     */         } 
/* 111 */         curr.add(new ResolverTuple(tag, regexp));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag resolve(NodeId kind, String value, boolean implicit) {
/* 117 */     if (kind == NodeId.scalar && implicit) {
/* 118 */       List<ResolverTuple> resolvers = null;
/* 119 */       if (value.length() == 0) {
/* 120 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(false));
/*     */       } else {
/* 122 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
/*     */       } 
/* 124 */       if (resolvers != null) {
/* 125 */         for (ResolverTuple v : resolvers) {
/* 126 */           Tag tag = v.getTag();
/* 127 */           Pattern regexp = v.getRegexp();
/* 128 */           if (regexp.matcher(value).matches()) {
/* 129 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/* 133 */       if (this.yamlImplicitResolvers.containsKey(null)) {
/* 134 */         for (ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
/* 135 */           Tag tag = v.getTag();
/* 136 */           Pattern regexp = v.getRegexp();
/* 137 */           if (regexp.matcher(value).matches()) {
/* 138 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 143 */     switch (kind) {
/*     */       case scalar:
/* 145 */         return Tag.STR;
/*     */       case sequence:
/* 147 */         return Tag.SEQ;
/*     */     } 
/* 149 */     return Tag.MAP;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */