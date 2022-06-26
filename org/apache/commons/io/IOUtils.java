/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.channels.Selector;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import org.apache.commons.io.output.ByteArrayOutputStream;
/*      */ import org.apache.commons.io.output.StringBuilderWriter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IOUtils
/*      */ {
/*      */   private static final int EOF = -1;
/*      */   public static final char DIR_SEPARATOR_UNIX = '/';
/*      */   public static final char DIR_SEPARATOR_WINDOWS = '\\';
/*  101 */   public static final char DIR_SEPARATOR = File.separatorChar;
/*      */   
/*      */   public static final String LINE_SEPARATOR_UNIX = "\n";
/*      */   
/*      */   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
/*      */   
/*      */   public static final String LINE_SEPARATOR;
/*      */   
/*      */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*      */   
/*      */   private static final int SKIP_BUFFER_SIZE = 2048;
/*      */   
/*      */   private static char[] SKIP_CHAR_BUFFER;
/*      */   private static byte[] SKIP_BYTE_BUFFER;
/*      */   
/*      */   static {
/*  117 */     StringBuilderWriter buf = new StringBuilderWriter(4);
/*  118 */     PrintWriter out = new PrintWriter((Writer)buf);
/*  119 */     out.println();
/*  120 */     LINE_SEPARATOR = buf.toString();
/*  121 */     out.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(URLConnection conn) {
/*  164 */     if (conn instanceof HttpURLConnection) {
/*  165 */       ((HttpURLConnection)conn).disconnect();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Reader input) {
/*  193 */     closeQuietly(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Writer output) {
/*  219 */     closeQuietly(output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(InputStream input) {
/*  246 */     closeQuietly(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(OutputStream output) {
/*  274 */     closeQuietly(output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Closeable closeable) {
/*      */     try {
/*  302 */       if (closeable != null) {
/*  303 */         closeable.close();
/*      */       }
/*  305 */     } catch (IOException ioe) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Socket sock) {
/*  334 */     if (sock != null) {
/*      */       try {
/*  336 */         sock.close();
/*  337 */       } catch (IOException ioe) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Selector selector) {
/*  367 */     if (selector != null) {
/*      */       try {
/*  369 */         selector.close();
/*  370 */       } catch (IOException ioe) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ServerSocket sock) {
/*  400 */     if (sock != null) {
/*      */       try {
/*  402 */         sock.close();
/*  403 */       } catch (IOException ioe) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/*  431 */     return ByteArrayOutputStream.toBufferedInputStream(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader toBufferedReader(Reader reader) {
/*  444 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input) throws IOException {
/*  461 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  462 */     copy(input, (OutputStream)output);
/*  463 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input, long size) throws IOException {
/*  484 */     if (size > 2147483647L) {
/*  485 */       throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
/*      */     }
/*      */     
/*  488 */     return toByteArray(input, (int)size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input, int size) throws IOException {
/*  504 */     if (size < 0) {
/*  505 */       throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
/*      */     }
/*      */     
/*  508 */     if (size == 0) {
/*  509 */       return new byte[0];
/*      */     }
/*      */     
/*  512 */     byte[] data = new byte[size];
/*  513 */     int offset = 0;
/*      */     
/*      */     int readed;
/*  516 */     while (offset < size && (readed = input.read(data, offset, size - offset)) != -1) {
/*  517 */       offset += readed;
/*      */     }
/*      */     
/*  520 */     if (offset != size) {
/*  521 */       throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
/*      */     }
/*      */     
/*  524 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input) throws IOException {
/*  540 */     return toByteArray(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
/*  558 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  559 */     copy(input, (OutputStream)output, encoding);
/*  560 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input, String encoding) throws IOException {
/*  584 */     return toByteArray(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static byte[] toByteArray(String input) throws IOException {
/*  601 */     return input.getBytes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URI uri) throws IOException {
/*  617 */     return toByteArray(uri.toURL());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URL url) throws IOException {
/*  633 */     URLConnection conn = url.openConnection();
/*      */     try {
/*  635 */       return toByteArray(conn);
/*      */     } finally {
/*  637 */       close(conn);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URLConnection urlConn) throws IOException {
/*  654 */     InputStream inputStream = urlConn.getInputStream();
/*      */     try {
/*  656 */       return toByteArray(inputStream);
/*      */     } finally {
/*  658 */       inputStream.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is) throws IOException {
/*  678 */     return toCharArray(is, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is, Charset encoding) throws IOException {
/*  697 */     CharArrayWriter output = new CharArrayWriter();
/*  698 */     copy(is, output, encoding);
/*  699 */     return output.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is, String encoding) throws IOException {
/*  723 */     return toCharArray(is, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(Reader input) throws IOException {
/*  739 */     CharArrayWriter sw = new CharArrayWriter();
/*  740 */     copy(input, sw);
/*  741 */     return sw.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input) throws IOException {
/*  759 */     return toString(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, Charset encoding) throws IOException {
/*  777 */     StringBuilderWriter sw = new StringBuilderWriter();
/*  778 */     copy(input, (Writer)sw, encoding);
/*  779 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, String encoding) throws IOException {
/*  803 */     return toString(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Reader input) throws IOException {
/*  818 */     StringBuilderWriter sw = new StringBuilderWriter();
/*  819 */     copy(input, (Writer)sw);
/*  820 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URI uri) throws IOException {
/*  833 */     return toString(uri, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URI uri, Charset encoding) throws IOException {
/*  848 */     return toString(uri.toURL(), Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URI uri, String encoding) throws IOException {
/*  866 */     return toString(uri, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URL url) throws IOException {
/*  879 */     return toString(url, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URL url, Charset encoding) throws IOException {
/*  894 */     InputStream inputStream = url.openStream();
/*      */     try {
/*  896 */       return toString(inputStream, encoding);
/*      */     } finally {
/*  898 */       inputStream.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URL url, String encoding) throws IOException {
/*  917 */     return toString(url, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toString(byte[] input) throws IOException {
/*  932 */     return new String(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] input, String encoding) throws IOException {
/*  949 */     return new String(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(InputStream input) throws IOException {
/*  968 */     return readLines(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
/*  986 */     InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(encoding));
/*  987 */     return readLines(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(InputStream input, String encoding) throws IOException {
/* 1011 */     return readLines(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(Reader input) throws IOException {
/* 1028 */     BufferedReader reader = toBufferedReader(input);
/* 1029 */     List<String> list = new ArrayList<String>();
/* 1030 */     String line = reader.readLine();
/* 1031 */     while (line != null) {
/* 1032 */       list.add(line);
/* 1033 */       line = reader.readLine();
/*      */     } 
/* 1035 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(Reader reader) {
/* 1068 */     return new LineIterator(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, Charset encoding) throws IOException {
/* 1102 */     return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
/* 1139 */     return lineIterator(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(CharSequence input) {
/* 1152 */     return toInputStream(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(CharSequence input, Charset encoding) {
/* 1165 */     return toInputStream(input.toString(), encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
/* 1185 */     return toInputStream(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input) {
/* 1198 */     return toInputStream(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input, Charset encoding) {
/* 1211 */     return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input, String encoding) throws IOException {
/* 1231 */     byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
/* 1232 */     return new ByteArrayInputStream(bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, OutputStream output) throws IOException {
/* 1249 */     if (data != null) {
/* 1250 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output) throws IOException {
/* 1268 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
/* 1286 */     if (data != null) {
/* 1287 */       output.write(new String(data, Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output, String encoding) throws IOException {
/* 1312 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, Writer output) throws IOException {
/* 1329 */     if (data != null) {
/* 1330 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output) throws IOException {
/* 1350 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
/* 1369 */     if (data != null) {
/* 1370 */       output.write((new String(data)).getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, String encoding) throws IOException {
/* 1397 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, Writer output) throws IOException {
/* 1412 */     if (data != null) {
/* 1413 */       write(data.toString(), output);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output) throws IOException {
/* 1432 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
/* 1449 */     if (data != null) {
/* 1450 */       write(data.toString(), output, encoding);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
/* 1474 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, Writer output) throws IOException {
/* 1489 */     if (data != null) {
/* 1490 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output) throws IOException {
/* 1509 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, Charset encoding) throws IOException {
/* 1526 */     if (data != null) {
/* 1527 */       output.write(data.getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, String encoding) throws IOException {
/* 1552 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, Writer output) throws IOException {
/* 1570 */     if (data != null) {
/* 1571 */       output.write(data.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output) throws IOException {
/* 1592 */     write(data, output, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
/* 1617 */     if (data != null) {
/* 1618 */       output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
/* 1638 */     writeLines(lines, lineEnding, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding) throws IOException {
/* 1656 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 1659 */     if (lineEnding == null) {
/* 1660 */       lineEnding = LINE_SEPARATOR;
/*      */     }
/* 1662 */     Charset cs = Charsets.toCharset(encoding);
/* 1663 */     for (Object line : lines) {
/* 1664 */       if (line != null) {
/* 1665 */         output.write(line.toString().getBytes(cs));
/*      */       }
/* 1667 */       output.write(lineEnding.getBytes(cs));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
/* 1692 */     writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
/* 1708 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 1711 */     if (lineEnding == null) {
/* 1712 */       lineEnding = LINE_SEPARATOR;
/*      */     }
/* 1714 */     for (Object line : lines) {
/* 1715 */       if (line != null) {
/* 1716 */         writer.write(line.toString());
/*      */       }
/* 1718 */       writer.write(lineEnding);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(InputStream input, OutputStream output) throws IOException {
/* 1744 */     long count = copyLarge(input, output);
/* 1745 */     if (count > 2147483647L) {
/* 1746 */       return -1;
/*      */     }
/* 1748 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
/* 1769 */     return copyLarge(input, output, new byte[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
/* 1790 */     long count = 0L;
/* 1791 */     int n = 0;
/* 1792 */     while (-1 != (n = input.read(buffer))) {
/* 1793 */       output.write(buffer, 0, n);
/* 1794 */       count += n;
/*      */     } 
/* 1796 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
/* 1820 */     return copyLarge(input, output, inputOffset, length, new byte[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
/* 1845 */     if (inputOffset > 0L) {
/* 1846 */       skipFully(input, inputOffset);
/*      */     }
/* 1848 */     if (length == 0L) {
/* 1849 */       return 0L;
/*      */     }
/* 1851 */     int bufferLength = buffer.length;
/* 1852 */     int bytesToRead = bufferLength;
/* 1853 */     if (length > 0L && length < bufferLength) {
/* 1854 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 1857 */     long totalRead = 0L; int read;
/* 1858 */     while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
/* 1859 */       output.write(buffer, 0, read);
/* 1860 */       totalRead += read;
/* 1861 */       if (length > 0L)
/*      */       {
/* 1863 */         bytesToRead = (int)Math.min(length - totalRead, bufferLength);
/*      */       }
/*      */     } 
/* 1866 */     return totalRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output) throws IOException {
/* 1886 */     copy(input, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
/* 1906 */     InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(encoding));
/* 1907 */     copy(in, output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output, String encoding) throws IOException {
/* 1933 */     copy(input, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(Reader input, Writer output) throws IOException {
/* 1957 */     long count = copyLarge(input, output);
/* 1958 */     if (count > 2147483647L) {
/* 1959 */       return -1;
/*      */     }
/* 1961 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output) throws IOException {
/* 1980 */     return copyLarge(input, output, new char[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
/* 1999 */     long count = 0L;
/* 2000 */     int n = 0;
/* 2001 */     while (-1 != (n = input.read(buffer))) {
/* 2002 */       output.write(buffer, 0, n);
/* 2003 */       count += n;
/*      */     } 
/* 2005 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
/* 2029 */     return copyLarge(input, output, inputOffset, length, new char[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
/* 2053 */     if (inputOffset > 0L) {
/* 2054 */       skipFully(input, inputOffset);
/*      */     }
/* 2056 */     if (length == 0L) {
/* 2057 */       return 0L;
/*      */     }
/* 2059 */     int bytesToRead = buffer.length;
/* 2060 */     if (length > 0L && length < buffer.length) {
/* 2061 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 2064 */     long totalRead = 0L; int read;
/* 2065 */     while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
/* 2066 */       output.write(buffer, 0, read);
/* 2067 */       totalRead += read;
/* 2068 */       if (length > 0L)
/*      */       {
/* 2070 */         bytesToRead = (int)Math.min(length - totalRead, buffer.length);
/*      */       }
/*      */     } 
/* 2073 */     return totalRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output) throws IOException {
/* 2097 */     copy(input, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output, Charset encoding) throws IOException {
/* 2124 */     OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(encoding));
/* 2125 */     copy(input, out);
/*      */ 
/*      */     
/* 2128 */     out.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
/* 2158 */     copy(input, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
/* 2179 */     if (!(input1 instanceof BufferedInputStream)) {
/* 2180 */       input1 = new BufferedInputStream(input1);
/*      */     }
/* 2182 */     if (!(input2 instanceof BufferedInputStream)) {
/* 2183 */       input2 = new BufferedInputStream(input2);
/*      */     }
/*      */     
/* 2186 */     int ch = input1.read();
/* 2187 */     while (-1 != ch) {
/* 2188 */       int i = input2.read();
/* 2189 */       if (ch != i) {
/* 2190 */         return false;
/*      */       }
/* 2192 */       ch = input1.read();
/*      */     } 
/*      */     
/* 2195 */     int ch2 = input2.read();
/* 2196 */     return (ch2 == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
/* 2217 */     input1 = toBufferedReader(input1);
/* 2218 */     input2 = toBufferedReader(input2);
/*      */     
/* 2220 */     int ch = input1.read();
/* 2221 */     while (-1 != ch) {
/* 2222 */       int i = input2.read();
/* 2223 */       if (ch != i) {
/* 2224 */         return false;
/*      */       }
/* 2226 */       ch = input1.read();
/*      */     } 
/*      */     
/* 2229 */     int ch2 = input2.read();
/* 2230 */     return (ch2 == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
/* 2249 */     BufferedReader br1 = toBufferedReader(input1);
/* 2250 */     BufferedReader br2 = toBufferedReader(input2);
/*      */     
/* 2252 */     String line1 = br1.readLine();
/* 2253 */     String line2 = br2.readLine();
/* 2254 */     while (line1 != null && line2 != null && line1.equals(line2)) {
/* 2255 */       line1 = br1.readLine();
/* 2256 */       line2 = br2.readLine();
/*      */     } 
/* 2258 */     return (line1 == null) ? ((line2 == null)) : line1.equals(line2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long skip(InputStream input, long toSkip) throws IOException {
/* 2278 */     if (toSkip < 0L) {
/* 2279 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2286 */     if (SKIP_BYTE_BUFFER == null) {
/* 2287 */       SKIP_BYTE_BUFFER = new byte[2048];
/*      */     }
/* 2289 */     long remain = toSkip;
/* 2290 */     while (remain > 0L) {
/* 2291 */       long n = input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
/* 2292 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2295 */       remain -= n;
/*      */     } 
/* 2297 */     return toSkip - remain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long skip(Reader input, long toSkip) throws IOException {
/* 2317 */     if (toSkip < 0L) {
/* 2318 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2325 */     if (SKIP_CHAR_BUFFER == null) {
/* 2326 */       SKIP_CHAR_BUFFER = new char[2048];
/*      */     }
/* 2328 */     long remain = toSkip;
/* 2329 */     while (remain > 0L) {
/* 2330 */       long n = input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, 2048L));
/* 2331 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2334 */       remain -= n;
/*      */     } 
/* 2336 */     return toSkip - remain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(InputStream input, long toSkip) throws IOException {
/* 2355 */     if (toSkip < 0L) {
/* 2356 */       throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
/*      */     }
/* 2358 */     long skipped = skip(input, toSkip);
/* 2359 */     if (skipped != toSkip) {
/* 2360 */       throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(Reader input, long toSkip) throws IOException {
/* 2380 */     long skipped = skip(input, toSkip);
/* 2381 */     if (skipped != toSkip) {
/* 2382 */       throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
/* 2402 */     if (length < 0) {
/* 2403 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 2405 */     int remaining = length;
/* 2406 */     while (remaining > 0) {
/* 2407 */       int location = length - remaining;
/* 2408 */       int count = input.read(buffer, offset + location, remaining);
/* 2409 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 2412 */       remaining -= count;
/*      */     } 
/* 2414 */     return length - remaining;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(Reader input, char[] buffer) throws IOException {
/* 2430 */     return read(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 2448 */     if (length < 0) {
/* 2449 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 2451 */     int remaining = length;
/* 2452 */     while (remaining > 0) {
/* 2453 */       int location = length - remaining;
/* 2454 */       int count = input.read(buffer, offset + location, remaining);
/* 2455 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 2458 */       remaining -= count;
/*      */     } 
/* 2460 */     return length - remaining;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(InputStream input, byte[] buffer) throws IOException {
/* 2476 */     return read(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
/* 2496 */     int actual = read(input, buffer, offset, length);
/* 2497 */     if (actual != length) {
/* 2498 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(Reader input, char[] buffer) throws IOException {
/* 2517 */     readFully(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 2537 */     int actual = read(input, buffer, offset, length);
/* 2538 */     if (actual != length) {
/* 2539 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(InputStream input, byte[] buffer) throws IOException {
/* 2558 */     readFully(input, buffer, 0, buffer.length);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\IOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */