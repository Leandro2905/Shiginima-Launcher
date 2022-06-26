/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class FormattedMessage
/*     */   implements Message
/*     */ {
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   private static final int HASHVAL = 31;
/*     */   private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
/*  36 */   private static final Pattern MSG_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
/*     */   
/*     */   private String messagePattern;
/*     */   private transient Object[] argArray;
/*     */   private String[] stringArgs;
/*     */   private transient String formattedMessage;
/*     */   private final Throwable throwable;
/*     */   private Message message;
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
/*  46 */     this.messagePattern = messagePattern;
/*  47 */     this.argArray = arguments;
/*  48 */     this.throwable = throwable;
/*     */   }
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object[] arguments) {
/*  52 */     this.messagePattern = messagePattern;
/*  53 */     this.argArray = arguments;
/*  54 */     this.throwable = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object arg) {
/*  63 */     this.messagePattern = messagePattern;
/*  64 */     this.argArray = new Object[] { arg };
/*  65 */     this.throwable = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object arg1, Object arg2) {
/*  75 */     this(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  85 */     if (this.formattedMessage == null) {
/*  86 */       if (this.message == null) {
/*  87 */         this.message = getMessage(this.messagePattern, this.argArray, this.throwable);
/*     */       }
/*  89 */       this.formattedMessage = this.message.getFormattedMessage();
/*     */     } 
/*  91 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 100 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 109 */     if (this.argArray != null) {
/* 110 */       return this.argArray;
/*     */     }
/* 112 */     return (Object[])this.stringArgs;
/*     */   }
/*     */   
/*     */   protected Message getMessage(String msgPattern, Object[] args, Throwable throwable) {
/*     */     try {
/* 117 */       MessageFormat format = new MessageFormat(msgPattern);
/* 118 */       Format[] formats = format.getFormats();
/* 119 */       if (formats != null && formats.length > 0) {
/* 120 */         return new MessageFormatMessage(msgPattern, args);
/*     */       }
/* 122 */     } catch (Exception ex) {}
/*     */ 
/*     */     
/*     */     try {
/* 126 */       if (MSG_PATTERN.matcher(msgPattern).find()) {
/* 127 */         return new StringFormattedMessage(msgPattern, args);
/*     */       }
/* 129 */     } catch (Exception ex) {}
/*     */ 
/*     */     
/* 132 */     return new ParameterizedMessage(msgPattern, args, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 137 */     if (this == o) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (o == null || getClass() != o.getClass()) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     FormattedMessage that = (FormattedMessage)o;
/*     */     
/* 146 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 147 */       return false;
/*     */     }
/* 149 */     if (!Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs)) {
/* 150 */       return false;
/*     */     }
/*     */     
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 158 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 159 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return "FormattedMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 171 */     out.defaultWriteObject();
/* 172 */     getFormattedMessage();
/* 173 */     out.writeUTF(this.formattedMessage);
/* 174 */     out.writeUTF(this.messagePattern);
/* 175 */     out.writeInt(this.argArray.length);
/* 176 */     this.stringArgs = new String[this.argArray.length];
/* 177 */     int i = 0;
/* 178 */     for (Object obj : this.argArray) {
/* 179 */       this.stringArgs[i] = obj.toString();
/* 180 */       i++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 185 */     in.defaultReadObject();
/* 186 */     this.formattedMessage = in.readUTF();
/* 187 */     this.messagePattern = in.readUTF();
/* 188 */     int length = in.readInt();
/* 189 */     this.stringArgs = new String[length];
/* 190 */     for (int i = 0; i < length; i++) {
/* 191 */       this.stringArgs[i] = in.readUTF();
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
/* 202 */     if (this.throwable != null) {
/* 203 */       return this.throwable;
/*     */     }
/* 205 */     if (this.message == null) {
/* 206 */       this.message = getMessage(this.messagePattern, this.argArray, null);
/*     */     }
/* 208 */     return this.message.getThrowable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\FormattedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */