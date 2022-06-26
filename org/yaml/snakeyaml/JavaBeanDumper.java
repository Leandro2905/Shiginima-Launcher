/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.representer.Representer;
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
/*     */ public class JavaBeanDumper
/*     */ {
/*     */   private boolean useGlobalTag;
/*     */   private DumperOptions.FlowStyle flowStyle;
/*     */   private DumperOptions options;
/*     */   private Representer representer;
/*     */   private final BeanAccess beanAccess;
/*     */   
/*     */   public JavaBeanDumper(boolean useGlobalTag, BeanAccess beanAccess) {
/*  45 */     this.useGlobalTag = useGlobalTag;
/*  46 */     this.beanAccess = beanAccess;
/*  47 */     this.flowStyle = DumperOptions.FlowStyle.BLOCK;
/*     */   }
/*     */   
/*     */   public JavaBeanDumper(boolean useGlobalTag) {
/*  51 */     this(useGlobalTag, BeanAccess.DEFAULT);
/*     */   }
/*     */   
/*     */   public JavaBeanDumper(BeanAccess beanAccess) {
/*  55 */     this(false, beanAccess);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaBeanDumper() {
/*  62 */     this(BeanAccess.DEFAULT);
/*     */   }
/*     */   
/*     */   public JavaBeanDumper(Representer representer, DumperOptions options) {
/*  66 */     if (representer == null) {
/*  67 */       throw new NullPointerException("Representer must be provided.");
/*     */     }
/*  69 */     if (options == null) {
/*  70 */       throw new NullPointerException("DumperOptions must be provided.");
/*     */     }
/*  72 */     this.options = options;
/*  73 */     this.representer = representer;
/*  74 */     this.beanAccess = null;
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
/*     */     DumperOptions doptions;
/*     */     Representer repr;
/*  88 */     if (this.options == null) {
/*  89 */       doptions = new DumperOptions();
/*  90 */       if (!this.useGlobalTag) {
/*  91 */         doptions.setExplicitRoot(Tag.MAP);
/*     */       }
/*  93 */       doptions.setDefaultFlowStyle(this.flowStyle);
/*     */     } else {
/*  95 */       doptions = this.options;
/*     */     } 
/*     */     
/*  98 */     if (this.representer == null) {
/*  99 */       repr = new Representer();
/* 100 */       repr.getPropertyUtils().setBeanAccess(this.beanAccess);
/*     */     } else {
/* 102 */       repr = this.representer;
/*     */     } 
/* 104 */     Yaml dumper = new Yaml(repr, doptions);
/* 105 */     dumper.dump(data, output);
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
/* 116 */     StringWriter buffer = new StringWriter();
/* 117 */     dump(data, buffer);
/* 118 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public boolean isUseGlobalTag() {
/* 122 */     return this.useGlobalTag;
/*     */   }
/*     */   
/*     */   public void setUseGlobalTag(boolean useGlobalTag) {
/* 126 */     this.useGlobalTag = useGlobalTag;
/*     */   }
/*     */   
/*     */   public DumperOptions.FlowStyle getFlowStyle() {
/* 130 */     return this.flowStyle;
/*     */   }
/*     */   
/*     */   public void setFlowStyle(DumperOptions.FlowStyle flowStyle) {
/* 134 */     this.flowStyle = flowStyle;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\JavaBeanDumper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */