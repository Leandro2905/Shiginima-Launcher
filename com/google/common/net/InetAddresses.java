/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.hash.Hashing;
/*      */ import com.google.common.io.ByteStreams;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Arrays;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public final class InetAddresses
/*      */ {
/*      */   private static final int IPV4_PART_COUNT = 4;
/*      */   private static final int IPV6_PART_COUNT = 8;
/*  117 */   private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
/*  118 */   private static final Inet4Address ANY4 = (Inet4Address)forString("0.0.0.0");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Inet4Address getInet4Address(byte[] bytes) {
/*  130 */     Preconditions.checkArgument((bytes.length == 4), "Byte array has invalid length for an IPv4 address: %s != 4.", new Object[] { Integer.valueOf(bytes.length) });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  135 */     return (Inet4Address)bytesToInetAddress(bytes);
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
/*      */   public static InetAddress forString(String ipString) {
/*  149 */     byte[] addr = ipStringToBytes(ipString);
/*      */ 
/*      */     
/*  152 */     if (addr == null) {
/*  153 */       throw new IllegalArgumentException(String.format("'%s' is not an IP string literal.", new Object[] { ipString }));
/*      */     }
/*      */ 
/*      */     
/*  157 */     return bytesToInetAddress(addr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInetAddress(String ipString) {
/*  168 */     return (ipStringToBytes(ipString) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] ipStringToBytes(String ipString) {
/*  173 */     boolean hasColon = false;
/*  174 */     boolean hasDot = false;
/*  175 */     for (int i = 0; i < ipString.length(); i++) {
/*  176 */       char c = ipString.charAt(i);
/*  177 */       if (c == '.') {
/*  178 */         hasDot = true;
/*  179 */       } else if (c == ':') {
/*  180 */         if (hasDot) {
/*  181 */           return null;
/*      */         }
/*  183 */         hasColon = true;
/*  184 */       } else if (Character.digit(c, 16) == -1) {
/*  185 */         return null;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  190 */     if (hasColon) {
/*  191 */       if (hasDot) {
/*  192 */         ipString = convertDottedQuadToHex(ipString);
/*  193 */         if (ipString == null) {
/*  194 */           return null;
/*      */         }
/*      */       } 
/*  197 */       return textToNumericFormatV6(ipString);
/*  198 */     }  if (hasDot) {
/*  199 */       return textToNumericFormatV4(ipString);
/*      */     }
/*  201 */     return null;
/*      */   }
/*      */   
/*      */   private static byte[] textToNumericFormatV4(String ipString) {
/*  205 */     String[] address = ipString.split("\\.", 5);
/*  206 */     if (address.length != 4) {
/*  207 */       return null;
/*      */     }
/*      */     
/*  210 */     byte[] bytes = new byte[4];
/*      */     try {
/*  212 */       for (int i = 0; i < bytes.length; i++) {
/*  213 */         bytes[i] = parseOctet(address[i]);
/*      */       }
/*  215 */     } catch (NumberFormatException ex) {
/*  216 */       return null;
/*      */     } 
/*      */     
/*  219 */     return bytes;
/*      */   }
/*      */   
/*      */   private static byte[] textToNumericFormatV6(String ipString) {
/*      */     int partsHi, partsLo;
/*  224 */     String[] parts = ipString.split(":", 10);
/*  225 */     if (parts.length < 3 || parts.length > 9) {
/*  226 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  231 */     int skipIndex = -1;
/*  232 */     for (int i = 1; i < parts.length - 1; i++) {
/*  233 */       if (parts[i].length() == 0) {
/*  234 */         if (skipIndex >= 0) {
/*  235 */           return null;
/*      */         }
/*  237 */         skipIndex = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  243 */     if (skipIndex >= 0) {
/*      */       
/*  245 */       partsHi = skipIndex;
/*  246 */       partsLo = parts.length - skipIndex - 1;
/*  247 */       if (parts[0].length() == 0 && --partsHi != 0) {
/*  248 */         return null;
/*      */       }
/*  250 */       if (parts[parts.length - 1].length() == 0 && --partsLo != 0) {
/*  251 */         return null;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  256 */       partsHi = parts.length;
/*  257 */       partsLo = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  262 */     int partsSkipped = 8 - partsHi + partsLo;
/*  263 */     if ((skipIndex >= 0) ? (partsSkipped >= 1) : (partsSkipped == 0)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  268 */       ByteBuffer rawBytes = ByteBuffer.allocate(16); try {
/*      */         int j;
/*  270 */         for (j = 0; j < partsHi; j++) {
/*  271 */           rawBytes.putShort(parseHextet(parts[j]));
/*      */         }
/*  273 */         for (j = 0; j < partsSkipped; j++) {
/*  274 */           rawBytes.putShort((short)0);
/*      */         }
/*  276 */         for (j = partsLo; j > 0; j--) {
/*  277 */           rawBytes.putShort(parseHextet(parts[parts.length - j]));
/*      */         }
/*  279 */       } catch (NumberFormatException ex) {
/*  280 */         return null;
/*      */       } 
/*  282 */       return rawBytes.array();
/*      */     } 
/*      */     return null;
/*      */   } private static String convertDottedQuadToHex(String ipString) {
/*  286 */     int lastColon = ipString.lastIndexOf(':');
/*  287 */     String initialPart = ipString.substring(0, lastColon + 1);
/*  288 */     String dottedQuad = ipString.substring(lastColon + 1);
/*  289 */     byte[] quad = textToNumericFormatV4(dottedQuad);
/*  290 */     if (quad == null) {
/*  291 */       return null;
/*      */     }
/*  293 */     String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | quad[1] & 0xFF);
/*  294 */     String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | quad[3] & 0xFF);
/*  295 */     String str1 = String.valueOf(String.valueOf(initialPart)), str2 = String.valueOf(String.valueOf(penultimate)), str3 = String.valueOf(String.valueOf(ultimate)); return (new StringBuilder(1 + str1.length() + str2.length() + str3.length())).append(str1).append(str2).append(":").append(str3).toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte parseOctet(String ipPart) {
/*  300 */     int octet = Integer.parseInt(ipPart);
/*      */ 
/*      */     
/*  303 */     if (octet > 255 || (ipPart.startsWith("0") && ipPart.length() > 1)) {
/*  304 */       throw new NumberFormatException();
/*      */     }
/*  306 */     return (byte)octet;
/*      */   }
/*      */ 
/*      */   
/*      */   private static short parseHextet(String ipPart) {
/*  311 */     int hextet = Integer.parseInt(ipPart, 16);
/*  312 */     if (hextet > 65535) {
/*  313 */       throw new NumberFormatException();
/*      */     }
/*  315 */     return (short)hextet;
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
/*      */   private static InetAddress bytesToInetAddress(byte[] addr) {
/*      */     try {
/*  331 */       return InetAddress.getByAddress(addr);
/*  332 */     } catch (UnknownHostException e) {
/*  333 */       throw new AssertionError(e);
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
/*      */   public static String toAddrString(InetAddress ip) {
/*  355 */     Preconditions.checkNotNull(ip);
/*  356 */     if (ip instanceof Inet4Address)
/*      */     {
/*  358 */       return ip.getHostAddress();
/*      */     }
/*  360 */     Preconditions.checkArgument(ip instanceof Inet6Address);
/*  361 */     byte[] bytes = ip.getAddress();
/*  362 */     int[] hextets = new int[8];
/*  363 */     for (int i = 0; i < hextets.length; i++) {
/*  364 */       hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
/*      */     }
/*      */     
/*  367 */     compressLongestRunOfZeroes(hextets);
/*  368 */     return hextetsToIPv6String(hextets);
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
/*      */   private static void compressLongestRunOfZeroes(int[] hextets) {
/*  381 */     int bestRunStart = -1;
/*  382 */     int bestRunLength = -1;
/*  383 */     int runStart = -1;
/*  384 */     for (int i = 0; i < hextets.length + 1; i++) {
/*  385 */       if (i < hextets.length && hextets[i] == 0) {
/*  386 */         if (runStart < 0) {
/*  387 */           runStart = i;
/*      */         }
/*  389 */       } else if (runStart >= 0) {
/*  390 */         int runLength = i - runStart;
/*  391 */         if (runLength > bestRunLength) {
/*  392 */           bestRunStart = runStart;
/*  393 */           bestRunLength = runLength;
/*      */         } 
/*  395 */         runStart = -1;
/*      */       } 
/*      */     } 
/*  398 */     if (bestRunLength >= 2) {
/*  399 */       Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
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
/*      */   private static String hextetsToIPv6String(int[] hextets) {
/*  418 */     StringBuilder buf = new StringBuilder(39);
/*  419 */     boolean lastWasNumber = false;
/*  420 */     for (int i = 0; i < hextets.length; i++) {
/*  421 */       boolean thisIsNumber = (hextets[i] >= 0);
/*  422 */       if (thisIsNumber) {
/*  423 */         if (lastWasNumber) {
/*  424 */           buf.append(':');
/*      */         }
/*  426 */         buf.append(Integer.toHexString(hextets[i]));
/*      */       }
/*  428 */       else if (i == 0 || lastWasNumber) {
/*  429 */         buf.append("::");
/*      */       } 
/*      */       
/*  432 */       lastWasNumber = thisIsNumber;
/*      */     } 
/*  434 */     return buf.toString();
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
/*      */   public static String toUriString(InetAddress ip) {
/*  463 */     if (ip instanceof Inet6Address) {
/*  464 */       String str = String.valueOf(String.valueOf(toAddrString(ip))); return (new StringBuilder(2 + str.length())).append("[").append(str).append("]").toString();
/*      */     } 
/*  466 */     return toAddrString(ip);
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
/*      */   public static InetAddress forUriString(String hostAddr) {
/*      */     String ipString;
/*      */     int expectBytes;
/*  485 */     Preconditions.checkNotNull(hostAddr);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  490 */     if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
/*  491 */       ipString = hostAddr.substring(1, hostAddr.length() - 1);
/*  492 */       expectBytes = 16;
/*      */     } else {
/*  494 */       ipString = hostAddr;
/*  495 */       expectBytes = 4;
/*      */     } 
/*      */ 
/*      */     
/*  499 */     byte[] addr = ipStringToBytes(ipString);
/*  500 */     if (addr == null || addr.length != expectBytes) {
/*  501 */       throw new IllegalArgumentException(String.format("Not a valid URI IP literal: '%s'", new Object[] { hostAddr }));
/*      */     }
/*      */ 
/*      */     
/*  505 */     return bytesToInetAddress(addr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUriInetAddress(String ipString) {
/*      */     try {
/*  517 */       forUriString(ipString);
/*  518 */       return true;
/*  519 */     } catch (IllegalArgumentException e) {
/*  520 */       return false;
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
/*      */   public static boolean isCompatIPv4Address(Inet6Address ip) {
/*  549 */     if (!ip.isIPv4CompatibleAddress()) {
/*  550 */       return false;
/*      */     }
/*      */     
/*  553 */     byte[] bytes = ip.getAddress();
/*  554 */     if (bytes[12] == 0 && bytes[13] == 0 && bytes[14] == 0 && (bytes[15] == 0 || bytes[15] == 1))
/*      */     {
/*  556 */       return false;
/*      */     }
/*      */     
/*  559 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getCompatIPv4Address(Inet6Address ip) {
/*  570 */     Preconditions.checkArgument(isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", new Object[] { toAddrString(ip) });
/*      */ 
/*      */     
/*  573 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean is6to4Address(Inet6Address ip) {
/*  591 */     byte[] bytes = ip.getAddress();
/*  592 */     return (bytes[0] == 32 && bytes[1] == 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address get6to4IPv4Address(Inet6Address ip) {
/*  603 */     Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", new Object[] { toAddrString(ip) });
/*      */ 
/*      */     
/*  606 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static final class TeredoInfo
/*      */   {
/*      */     private final Inet4Address server;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final Inet4Address client;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int port;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int flags;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TeredoInfo(@Nullable Inet4Address server, @Nullable Inet4Address client, int port, int flags) {
/*  644 */       Preconditions.checkArgument((port >= 0 && port <= 65535), "port '%s' is out of range (0 <= port <= 0xffff)", new Object[] { Integer.valueOf(port) });
/*      */       
/*  646 */       Preconditions.checkArgument((flags >= 0 && flags <= 65535), "flags '%s' is out of range (0 <= flags <= 0xffff)", new Object[] { Integer.valueOf(flags) });
/*      */ 
/*      */       
/*  649 */       this.server = (Inet4Address)MoreObjects.firstNonNull(server, InetAddresses.ANY4);
/*  650 */       this.client = (Inet4Address)MoreObjects.firstNonNull(client, InetAddresses.ANY4);
/*  651 */       this.port = port;
/*  652 */       this.flags = flags;
/*      */     }
/*      */     
/*      */     public Inet4Address getServer() {
/*  656 */       return this.server;
/*      */     }
/*      */     
/*      */     public Inet4Address getClient() {
/*  660 */       return this.client;
/*      */     }
/*      */     
/*      */     public int getPort() {
/*  664 */       return this.port;
/*      */     }
/*      */     
/*      */     public int getFlags() {
/*  668 */       return this.flags;
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
/*      */   public static boolean isTeredoAddress(Inet6Address ip) {
/*  681 */     byte[] bytes = ip.getAddress();
/*  682 */     return (bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0);
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
/*      */   public static TeredoInfo getTeredoInfo(Inet6Address ip) {
/*  694 */     Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", new Object[] { toAddrString(ip) });
/*      */ 
/*      */     
/*  697 */     byte[] bytes = ip.getAddress();
/*  698 */     Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
/*      */     
/*  700 */     int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
/*      */ 
/*      */     
/*  703 */     int port = (ByteStreams.newDataInput(bytes, 10).readShort() ^ 0xFFFFFFFF) & 0xFFFF;
/*      */     
/*  705 */     byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
/*  706 */     for (int i = 0; i < clientBytes.length; i++)
/*      */     {
/*  708 */       clientBytes[i] = (byte)(clientBytes[i] ^ 0xFFFFFFFF);
/*      */     }
/*  710 */     Inet4Address client = getInet4Address(clientBytes);
/*      */     
/*  712 */     return new TeredoInfo(server, client, port, flags);
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
/*      */   public static boolean isIsatapAddress(Inet6Address ip) {
/*  734 */     if (isTeredoAddress(ip)) {
/*  735 */       return false;
/*      */     }
/*      */     
/*  738 */     byte[] bytes = ip.getAddress();
/*      */     
/*  740 */     if ((bytes[8] | 0x3) != 3)
/*      */     {
/*      */ 
/*      */       
/*  744 */       return false;
/*      */     }
/*      */     
/*  747 */     return (bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2);
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
/*      */   public static Inet4Address getIsatapIPv4Address(Inet6Address ip) {
/*  759 */     Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", new Object[] { toAddrString(ip) });
/*      */ 
/*      */     
/*  762 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  778 */     return (isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip));
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
/*      */   public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  795 */     if (isCompatIPv4Address(ip)) {
/*  796 */       return getCompatIPv4Address(ip);
/*      */     }
/*      */     
/*  799 */     if (is6to4Address(ip)) {
/*  800 */       return get6to4IPv4Address(ip);
/*      */     }
/*      */     
/*  803 */     if (isTeredoAddress(ip)) {
/*  804 */       return getTeredoInfo(ip).getClient();
/*      */     }
/*      */     
/*  807 */     throw new IllegalArgumentException(String.format("'%s' has no embedded IPv4 address.", new Object[] { toAddrString(ip) }));
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
/*      */   public static boolean isMappedIPv4Address(String ipString) {
/*  834 */     byte[] bytes = ipStringToBytes(ipString);
/*  835 */     if (bytes != null && bytes.length == 16) {
/*  836 */       int i; for (i = 0; i < 10; i++) {
/*  837 */         if (bytes[i] != 0) {
/*  838 */           return false;
/*      */         }
/*      */       } 
/*  841 */       for (i = 10; i < 12; i++) {
/*  842 */         if (bytes[i] != -1) {
/*  843 */           return false;
/*      */         }
/*      */       } 
/*  846 */       return true;
/*      */     } 
/*  848 */     return false;
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
/*      */   public static Inet4Address getCoercedIPv4Address(InetAddress ip) {
/*  872 */     if (ip instanceof Inet4Address) {
/*  873 */       return (Inet4Address)ip;
/*      */     }
/*      */ 
/*      */     
/*  877 */     byte[] bytes = ip.getAddress();
/*  878 */     boolean leadingBytesOfZero = true;
/*  879 */     for (int i = 0; i < 15; i++) {
/*  880 */       if (bytes[i] != 0) {
/*  881 */         leadingBytesOfZero = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  885 */     if (leadingBytesOfZero && bytes[15] == 1)
/*  886 */       return LOOPBACK4; 
/*  887 */     if (leadingBytesOfZero && bytes[15] == 0) {
/*  888 */       return ANY4;
/*      */     }
/*      */     
/*  891 */     Inet6Address ip6 = (Inet6Address)ip;
/*  892 */     long addressAsLong = 0L;
/*  893 */     if (hasEmbeddedIPv4ClientAddress(ip6)) {
/*  894 */       addressAsLong = getEmbeddedIPv4ClientAddress(ip6).hashCode();
/*      */     }
/*      */     else {
/*      */       
/*  898 */       addressAsLong = ByteBuffer.wrap(ip6.getAddress(), 0, 8).getLong();
/*      */     } 
/*      */ 
/*      */     
/*  902 */     int coercedHash = Hashing.murmur3_32().hashLong(addressAsLong).asInt();
/*      */ 
/*      */     
/*  905 */     coercedHash |= 0xE0000000;
/*      */ 
/*      */ 
/*      */     
/*  909 */     if (coercedHash == -1) {
/*  910 */       coercedHash = -2;
/*      */     }
/*      */     
/*  913 */     return getInet4Address(Ints.toByteArray(coercedHash));
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
/*      */   public static int coerceToInteger(InetAddress ip) {
/*  938 */     return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address fromInteger(int address) {
/*  949 */     return getInet4Address(Ints.toByteArray(address));
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
/*      */   public static InetAddress fromLittleEndianByteArray(byte[] addr) throws UnknownHostException {
/*  964 */     byte[] reversed = new byte[addr.length];
/*  965 */     for (int i = 0; i < addr.length; i++) {
/*  966 */       reversed[i] = addr[addr.length - i - 1];
/*      */     }
/*  968 */     return InetAddress.getByAddress(reversed);
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
/*      */   public static InetAddress decrement(InetAddress address) {
/*  981 */     byte[] addr = address.getAddress();
/*  982 */     int i = addr.length - 1;
/*  983 */     while (i >= 0 && addr[i] == 0) {
/*  984 */       addr[i] = -1;
/*  985 */       i--;
/*      */     } 
/*      */     
/*  988 */     Preconditions.checkArgument((i >= 0), "Decrementing %s would wrap.", new Object[] { address });
/*      */     
/*  990 */     addr[i] = (byte)(addr[i] - 1);
/*  991 */     return bytesToInetAddress(addr);
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
/*      */   public static InetAddress increment(InetAddress address) {
/* 1004 */     byte[] addr = address.getAddress();
/* 1005 */     int i = addr.length - 1;
/* 1006 */     while (i >= 0 && addr[i] == -1) {
/* 1007 */       addr[i] = 0;
/* 1008 */       i--;
/*      */     } 
/*      */     
/* 1011 */     Preconditions.checkArgument((i >= 0), "Incrementing %s would wrap.", new Object[] { address });
/*      */     
/* 1013 */     addr[i] = (byte)(addr[i] + 1);
/* 1014 */     return bytesToInetAddress(addr);
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
/*      */   public static boolean isMaximum(InetAddress address) {
/* 1026 */     byte[] addr = address.getAddress();
/* 1027 */     for (int i = 0; i < addr.length; i++) {
/* 1028 */       if (addr[i] != -1) {
/* 1029 */         return false;
/*      */       }
/*      */     } 
/* 1032 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\net\InetAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */