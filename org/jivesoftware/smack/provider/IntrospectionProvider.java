/*     */ package org.jivesoftware.smack.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.jivesoftware.smack.SmackException;
/*     */ import org.jivesoftware.smack.packet.Element;
/*     */ import org.jivesoftware.smack.packet.ExtensionElement;
/*     */ import org.jivesoftware.smack.packet.IQ;
/*     */ import org.jivesoftware.smack.util.ParserUtils;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
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
/*     */ public class IntrospectionProvider
/*     */ {
/*     */   public static abstract class IQIntrospectionProvider<I extends IQ>
/*     */     extends IQProvider<I>
/*     */   {
/*     */     private final Class<I> elementClass;
/*     */     
/*     */     protected IQIntrospectionProvider(Class<I> elementClass) {
/*  37 */       this.elementClass = elementClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public I parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
/*     */       try {
/*  45 */         return (I)IntrospectionProvider.parseWithIntrospection(this.elementClass, parser, initialDepth);
/*     */       }
/*  47 */       catch (NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|ClassNotFoundException e) {
/*     */         
/*  49 */         throw new SmackException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class PacketExtensionIntrospectionProvider<PE extends ExtensionElement> extends ExtensionElementProvider<PE> {
/*     */     private final Class<PE> elementClass;
/*     */     
/*     */     protected PacketExtensionIntrospectionProvider(Class<PE> elementClass) {
/*  58 */       this.elementClass = elementClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PE parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
/*     */       try {
/*  66 */         return (PE)IntrospectionProvider.parseWithIntrospection(this.elementClass, parser, initialDepth);
/*     */       }
/*  68 */       catch (NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|ClassNotFoundException e) {
/*     */         
/*  70 */         throw new SmackException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object parseWithIntrospection(Class<?> objectClass, XmlPullParser parser, int initialDepth) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, XmlPullParserException, IOException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
/*  80 */     ParserUtils.assertAtStartTag(parser);
/*  81 */     Object object = objectClass.newInstance(); while (true) {
/*     */       String name, stringValue; Class<?> propertyType; Object value;
/*  83 */       int eventType = parser.next();
/*  84 */       switch (eventType) {
/*     */         case 2:
/*  86 */           name = parser.getName();
/*  87 */           stringValue = parser.nextText();
/*  88 */           propertyType = object.getClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[0]).getReturnType();
/*     */ 
/*     */ 
/*     */           
/*  92 */           value = decode(propertyType, stringValue);
/*     */           
/*  94 */           object.getClass().getMethod("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[] { propertyType }).invoke(object, new Object[] { value });
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 100 */           if (parser.getDepth() == initialDepth) {
/*     */             break;
/*     */           }
/*     */       } 
/*     */     
/*     */     } 
/* 106 */     ParserUtils.assertAtEndTag(parser);
/* 107 */     return object;
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
/*     */   private static Object decode(Class<?> type, String value) throws ClassNotFoundException {
/* 120 */     String name = type.getName();
/* 121 */     switch (name) {
/*     */       case "java.lang.String":
/* 123 */         return value;
/*     */       case "boolean":
/* 125 */         return Boolean.valueOf(value);
/*     */       case "int":
/* 127 */         return Integer.valueOf(value);
/*     */       case "long":
/* 129 */         return Long.valueOf(value);
/*     */       case "float":
/* 131 */         return Float.valueOf(value);
/*     */       case "double":
/* 133 */         return Double.valueOf(value);
/*     */       case "short":
/* 135 */         return Short.valueOf(value);
/*     */       case "byte":
/* 137 */         return Byte.valueOf(value);
/*     */       case "java.lang.Class":
/* 139 */         return Class.forName(value);
/*     */     } 
/* 141 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\IntrospectionProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */