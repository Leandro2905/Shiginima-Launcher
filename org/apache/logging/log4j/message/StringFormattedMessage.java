/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.IllegalFormatException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class StringFormattedMessage
/*     */   implements Message
/*     */ {
/*  33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   
/*     */   private static final int HASHVAL = 31;
/*     */   
/*     */   private String messagePattern;
/*     */   private transient Object[] argArray;
/*     */   private String[] stringArgs;
/*     */   private transient String formattedMessage;
/*     */   private transient Throwable throwable;
/*     */   
/*     */   public StringFormattedMessage(String messagePattern, Object... arguments) {
/*  46 */     this.messagePattern = messagePattern;
/*  47 */     this.argArray = arguments;
/*  48 */     if (arguments != null && arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable) {
/*  49 */       this.throwable = (Throwable)arguments[arguments.length - 1];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  59 */     if (this.formattedMessage == null) {
/*  60 */       this.formattedMessage = formatMessage(this.messagePattern, this.argArray);
/*     */     }
/*  62 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  71 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  80 */     if (this.argArray != null) {
/*  81 */       return this.argArray;
/*     */     }
/*  83 */     return (Object[])this.stringArgs;
/*     */   }
/*     */   
/*     */   protected String formatMessage(String msgPattern, Object... args) {
/*     */     try {
/*  88 */       return String.format(msgPattern, args);
/*  89 */     } catch (IllegalFormatException ife) {
/*  90 */       LOGGER.error("Unable to format msg: " + msgPattern, ife);
/*  91 */       return msgPattern;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  97 */     if (this == o) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (o == null || getClass() != o.getClass()) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     StringFormattedMessage that = (StringFormattedMessage)o;
/*     */     
/* 106 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     return Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 116 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return "StringFormatMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 128 */     out.defaultWriteObject();
/* 129 */     getFormattedMessage();
/* 130 */     out.writeUTF(this.formattedMessage);
/* 131 */     out.writeUTF(this.messagePattern);
/* 132 */     out.writeInt(this.argArray.length);
/* 133 */     this.stringArgs = new String[this.argArray.length];
/* 134 */     int i = 0;
/* 135 */     for (Object obj : this.argArray) {
/* 136 */       this.stringArgs[i] = obj.toString();
/* 137 */       i++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 142 */     in.defaultReadObject();
/* 143 */     this.formattedMessage = in.readUTF();
/* 144 */     this.messagePattern = in.readUTF();
/* 145 */     int length = in.readInt();
/* 146 */     this.stringArgs = new String[length];
/* 147 */     for (int i = 0; i < length; i++) {
/* 148 */       this.stringArgs[i] = in.readUTF();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 159 */     return this.throwable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\StringFormattedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */