/*    */ package net.minecraft.launcher.game;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Shiginima
/*    */ {
/*    */   public static void main(String[] args) {
/* 12 */     System.out.println("TEST123123123123");
/* 13 */     String className = args[0];
/* 14 */     System.out.println("GameStarter running! " + className);
/*    */     
/* 16 */     String[] passedArgs = new String[args.length - 1];
/* 17 */     for (int i = 1; i < args.length; i++) {
/* 18 */       passedArgs[i - 1] = args[i];
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 23 */       Class<?> game = Class.forName(className);
/* 24 */       Method main = game.getMethod("main", new Class[] { String.class });
/* 25 */       main.invoke(null, new Object[] { passedArgs });
/*    */     }
/* 27 */     catch (Exception e) {
/*    */       
/* 29 */       System.out.println("Error while starting game:");
/* 30 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\game\Shiginima.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */