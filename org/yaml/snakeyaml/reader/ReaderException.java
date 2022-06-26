/*    */ package org.yaml.snakeyaml.reader;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.YAMLException;
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
/*    */ public class ReaderException
/*    */   extends YAMLException
/*    */ {
/*    */   private static final long serialVersionUID = 8710781187529689083L;
/*    */   private final String name;
/*    */   private final char character;
/*    */   private final int position;
/*    */   
/*    */   public ReaderException(String name, int position, char character, String message) {
/* 27 */     super(message);
/* 28 */     this.name = name;
/* 29 */     this.character = character;
/* 30 */     this.position = position;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public char getCharacter() {
/* 38 */     return this.character;
/*    */   }
/*    */   
/*    */   public int getPosition() {
/* 42 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return "unacceptable character '" + this.character + "' (0x" + Integer.toHexString(this.character).toUpperCase() + ") " + getMessage() + "\nin \"" + this.name + "\", position " + this.position;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\reader\ReaderException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */