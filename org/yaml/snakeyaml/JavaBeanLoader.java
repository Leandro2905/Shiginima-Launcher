/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ public class JavaBeanLoader<T>
/*     */ {
/*     */   private Yaml loader;
/*     */   
/*     */   public JavaBeanLoader(TypeDescription typeDescription) {
/*  42 */     this(typeDescription, BeanAccess.DEFAULT);
/*     */   }
/*     */   
/*     */   public JavaBeanLoader(TypeDescription typeDescription, BeanAccess beanAccess) {
/*  46 */     this(new LoaderOptions(typeDescription), beanAccess);
/*     */   }
/*     */   
/*     */   public JavaBeanLoader(LoaderOptions options, BeanAccess beanAccess) {
/*  50 */     if (options == null) {
/*  51 */       throw new NullPointerException("LoaderOptions must be provided.");
/*     */     }
/*  53 */     if (options.getRootTypeDescription() == null) {
/*  54 */       throw new NullPointerException("TypeDescription must be provided.");
/*     */     }
/*  56 */     Constructor constructor = new Constructor(options.getRootTypeDescription());
/*  57 */     this.loader = new Yaml((BaseConstructor)constructor, options, new Representer(), new DumperOptions(), new Resolver());
/*     */     
/*  59 */     this.loader.setBeanAccess(beanAccess);
/*     */   }
/*     */   
/*     */   public <S extends T> JavaBeanLoader(Class<S> clazz, BeanAccess beanAccess) {
/*  63 */     this(new TypeDescription((Class)clazz), beanAccess);
/*     */   }
/*     */   
/*     */   public <S extends T> JavaBeanLoader(Class<S> clazz) {
/*  67 */     this(clazz, BeanAccess.DEFAULT);
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
/*     */   public T load(String yaml) {
/*  80 */     return (T)this.loader.load(new StringReader(yaml));
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
/*     */   public T load(InputStream io) {
/*  93 */     return (T)this.loader.load((Reader)new UnicodeReader(io));
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
/*     */   public T load(Reader io) {
/* 106 */     return (T)this.loader.load(io);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\JavaBeanLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */