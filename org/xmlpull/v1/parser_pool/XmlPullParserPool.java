/*    */ package org.xmlpull.v1.parser_pool;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.xmlpull.v1.XmlPullParser;
/*    */ import org.xmlpull.v1.XmlPullParserException;
/*    */ import org.xmlpull.v1.XmlPullParserFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlPullParserPool
/*    */ {
/* 20 */   protected List pool = new ArrayList();
/*    */   
/*    */   protected XmlPullParserFactory factory;
/*    */   
/*    */   public XmlPullParserPool() throws XmlPullParserException {
/* 25 */     this(XmlPullParserFactory.newInstance());
/*    */   }
/*    */   
/*    */   public XmlPullParserPool(XmlPullParserFactory factory) {
/* 29 */     if (factory == null) throw new IllegalArgumentException(); 
/* 30 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   protected XmlPullParser newParser() throws XmlPullParserException {
/* 34 */     return this.factory.newPullParser();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlPullParser getPullParserFromPool() throws XmlPullParserException {
/* 40 */     XmlPullParser pp = null;
/* 41 */     if (this.pool.size() > 0) {
/* 42 */       synchronized (this.pool) {
/* 43 */         if (this.pool.size() > 0) {
/* 44 */           pp = this.pool.remove(this.pool.size() - 1);
/*    */         }
/*    */       } 
/*    */     }
/* 48 */     if (pp == null) {
/* 49 */       pp = newParser();
/*    */     }
/*    */     
/* 52 */     return pp;
/*    */   }
/*    */   
/*    */   public void returnPullParserToPool(XmlPullParser pp) {
/* 56 */     if (pp == null) throw new IllegalArgumentException(); 
/* 57 */     synchronized (this.pool) {
/* 58 */       this.pool.add(pp);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 68 */     XmlPullParserPool pool = new XmlPullParserPool();
/* 69 */     XmlPullParser p1 = pool.getPullParserFromPool();
/* 70 */     pool.returnPullParserToPool(p1);
/* 71 */     XmlPullParser p2 = pool.getPullParserFromPool();
/*    */     
/* 73 */     if (p1 != p2) throw new RuntimeException(); 
/* 74 */     pool.returnPullParserToPool(p2);
/* 75 */     System.out.println(pool.getClass() + " OK");
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\parser_pool\XmlPullParserPool.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */