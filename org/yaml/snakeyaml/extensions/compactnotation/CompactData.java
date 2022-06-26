/*    */ package org.yaml.snakeyaml.extensions.compactnotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompactData
/*    */ {
/*    */   private String prefix;
/* 25 */   private List<String> arguments = new ArrayList<String>();
/* 26 */   private Map<String, String> properties = new HashMap<String, String>();
/*    */   
/*    */   public CompactData(String prefix) {
/* 29 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public String getPrefix() {
/* 33 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public Map<String, String> getProperties() {
/* 37 */     return this.properties;
/*    */   }
/*    */   
/*    */   public List<String> getArguments() {
/* 41 */     return this.arguments;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "CompactData: " + this.prefix + " " + this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */