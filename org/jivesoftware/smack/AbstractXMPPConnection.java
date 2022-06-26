/*      */ package org.jivesoftware.smack;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.jivesoftware.smack.compress.packet.Compress;
/*      */ import org.jivesoftware.smack.compression.XMPPInputOutputStream;
/*      */ import org.jivesoftware.smack.debugger.SmackDebugger;
/*      */ import org.jivesoftware.smack.filter.IQReplyFilter;
/*      */ import org.jivesoftware.smack.filter.StanzaFilter;
/*      */ import org.jivesoftware.smack.filter.StanzaIdFilter;
/*      */ import org.jivesoftware.smack.iqrequest.IQRequestHandler;
/*      */ import org.jivesoftware.smack.packet.Bind;
/*      */ import org.jivesoftware.smack.packet.ErrorIQ;
/*      */ import org.jivesoftware.smack.packet.ExtensionElement;
/*      */ import org.jivesoftware.smack.packet.IQ;
/*      */ import org.jivesoftware.smack.packet.Mechanisms;
/*      */ import org.jivesoftware.smack.packet.PlainStreamElement;
/*      */ import org.jivesoftware.smack.packet.Presence;
/*      */ import org.jivesoftware.smack.packet.Session;
/*      */ import org.jivesoftware.smack.packet.Stanza;
/*      */ import org.jivesoftware.smack.packet.StartTls;
/*      */ import org.jivesoftware.smack.packet.XMPPError;
/*      */ import org.jivesoftware.smack.parsing.ParsingExceptionCallback;
/*      */ import org.jivesoftware.smack.parsing.UnparsablePacket;
/*      */ import org.jivesoftware.smack.provider.ExtensionElementProvider;
/*      */ import org.jivesoftware.smack.provider.ProviderManager;
/*      */ import org.jivesoftware.smack.util.DNSUtil;
/*      */ import org.jivesoftware.smack.util.Objects;
/*      */ import org.jivesoftware.smack.util.PacketParserUtils;
/*      */ import org.jivesoftware.smack.util.ParserUtils;
/*      */ import org.jivesoftware.smack.util.SmackExecutorThreadFactory;
/*      */ import org.jivesoftware.smack.util.StringUtils;
/*      */ import org.jivesoftware.smack.util.dns.HostAddress;
/*      */ import org.jxmpp.jid.DomainBareJid;
/*      */ import org.jxmpp.jid.FullJid;
/*      */ import org.jxmpp.jid.Jid;
/*      */ import org.jxmpp.util.XmppStringUtils;
/*      */ import org.xmlpull.v1.XmlPullParser;
/*      */ import org.xmlpull.v1.XmlPullParserException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractXMPPConnection
/*      */   implements XMPPConnection
/*      */ {
/*   92 */   private static final Logger LOGGER = Logger.getLogger(AbstractXMPPConnection.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   private static final AtomicInteger connectionCounter = new AtomicInteger(0);
/*      */ 
/*      */   
/*      */   static {
/*  101 */     SmackConfiguration.getVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Collection<ConnectionCreationListener> getConnectionCreationListeners() {
/*  110 */     return XMPPConnectionRegistry.getConnectionCreationListeners();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  117 */   protected final Set<ConnectionListener> connectionListeners = new CopyOnWriteArraySet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private final Collection<PacketCollector> collectors = new ConcurrentLinkedQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  136 */   private final Map<StanzaListener, ListenerWrapper> syncRecvListeners = new LinkedHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   private final Map<StanzaListener, ListenerWrapper> asyncRecvListeners = new LinkedHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   private final Map<StanzaListener, ListenerWrapper> sendListeners = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  154 */   private final Map<StanzaListener, InterceptorWrapper> interceptors = new HashMap<>();
/*      */ 
/*      */   
/*  157 */   protected final Lock connectionLock = new ReentrantLock();
/*      */   
/*  159 */   protected final Map<String, ExtensionElement> streamFeatures = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FullJid user;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean connected = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String streamId;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  181 */   private long packetReplyTimeout = SmackConfiguration.getDefaultPacketReplyTimeout();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  186 */   protected SmackDebugger debugger = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Reader reader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Writer writer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  204 */   protected final SynchronizationPoint<Exception> lastFeaturesReceived = new SynchronizationPoint<>(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   protected final SynchronizationPoint<SmackException> saslFeatureReceived = new SynchronizationPoint<>(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  216 */   protected SASLAuthentication saslAuthentication = new SASLAuthentication(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  222 */   protected final int connectionCounterValue = connectionCounter.getAndIncrement();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ConnectionConfiguration config;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  232 */   private XMPPConnection.FromMode fromMode = XMPPConnection.FromMode.OMITTED;
/*      */   
/*      */   protected XMPPInputOutputStream compressionHandler;
/*      */   
/*  236 */   private ParsingExceptionCallback parsingExceptionCallback = SmackConfiguration.getDefaultParsingExceptionCallback();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  243 */   private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), (ThreadFactory)new SmackExecutorThreadFactory(this.connectionCounterValue, "Incoming Processor"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  249 */   private final ScheduledExecutorService removeCallbacksService = Executors.newSingleThreadScheduledExecutor((ThreadFactory)new SmackExecutorThreadFactory(this.connectionCounterValue, "Remove Callbacks"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  256 */   private final ExecutorService cachedExecutorService = Executors.newCachedThreadPool((ThreadFactory)new SmackExecutorThreadFactory(this.connectionCounterValue, "Cached Executor"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  270 */   private final ExecutorService singleThreadedExecutorService = Executors.newSingleThreadExecutor((ThreadFactory)new SmackExecutorThreadFactory(getConnectionCounter(), "Single Threaded Executor"));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String host;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int port;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean authenticated = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean wasAuthenticated = false;
/*      */ 
/*      */ 
/*      */   
/*  294 */   private final Map<String, IQRequestHandler> setIqRequestHandler = new HashMap<>();
/*  295 */   private final Map<String, IQRequestHandler> getIqRequestHandler = new HashMap<>();
/*      */   
/*      */   private String usedUsername;
/*      */   
/*      */   private String usedPassword;
/*      */   
/*      */   private String usedResource;
/*      */   
/*      */   private DomainBareJid serviceName;
/*      */   protected List<HostAddress> hostAddresses;
/*      */   
/*      */   protected ConnectionConfiguration getConfiguration() {
/*  307 */     return this.config;
/*      */   }
/*      */ 
/*      */   
/*      */   public DomainBareJid getServiceName() {
/*  312 */     if (this.serviceName != null) {
/*  313 */       return this.serviceName;
/*      */     }
/*  315 */     return this.config.getServiceName();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getHost() {
/*  320 */     return this.host;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPort() {
/*  325 */     return this.port;
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
/*      */   public synchronized AbstractXMPPConnection connect() throws SmackException, IOException, XMPPException, InterruptedException {
/*  355 */     throwAlreadyConnectedExceptionIfAppropriate();
/*      */ 
/*      */     
/*  358 */     this.saslAuthentication.init();
/*  359 */     this.saslFeatureReceived.init();
/*  360 */     this.lastFeaturesReceived.init();
/*  361 */     this.streamId = null;
/*      */ 
/*      */     
/*  364 */     connectInternal();
/*  365 */     return this;
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
/*      */   public synchronized void login() throws XMPPException, SmackException, IOException, InterruptedException {
/*  406 */     if (isAnonymous()) {
/*  407 */       throwNotConnectedExceptionIfAppropriate();
/*  408 */       throwAlreadyLoggedInExceptionIfAppropriate();
/*  409 */       loginAnonymously();
/*      */     }
/*      */     else {
/*      */       
/*  413 */       CharSequence username = (this.usedUsername != null) ? this.usedUsername : this.config.getUsername();
/*  414 */       String password = (this.usedPassword != null) ? this.usedPassword : this.config.getPassword();
/*  415 */       String resource = (this.usedResource != null) ? this.usedResource : this.config.getResource();
/*  416 */       login(username, password, resource);
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
/*      */   public synchronized void login(CharSequence username, String password) throws XMPPException, SmackException, IOException, InterruptedException {
/*  434 */     login(username, password, this.config.getResource());
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
/*      */   public synchronized void login(CharSequence username, String password, String resource) throws XMPPException, SmackException, IOException, InterruptedException {
/*  452 */     if (!this.config.allowNullOrEmptyUsername) {
/*  453 */       StringUtils.requireNotNullOrEmpty(username, "Username must not be null or empty");
/*      */     }
/*  455 */     throwNotConnectedExceptionIfAppropriate();
/*  456 */     throwAlreadyLoggedInExceptionIfAppropriate();
/*  457 */     this.usedUsername = (username != null) ? username.toString() : null;
/*  458 */     this.usedPassword = password;
/*  459 */     this.usedResource = resource;
/*  460 */     loginNonAnonymously(this.usedUsername, this.usedPassword, this.usedResource);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isConnected() {
/*  470 */     return this.connected;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAuthenticated() {
/*  475 */     return this.authenticated;
/*      */   }
/*      */ 
/*      */   
/*      */   public final FullJid getUser() {
/*  480 */     return this.user;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getStreamId() {
/*  485 */     if (!isConnected()) {
/*  486 */       return null;
/*      */     }
/*  488 */     return this.streamId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void bindResourceAndEstablishSession(String resource) throws XMPPException.XMPPErrorException, IOException, SmackException, InterruptedException {
/*  499 */     LOGGER.finer("Waiting for last features to be received before continuing with resource binding");
/*  500 */     this.lastFeaturesReceived.checkIfSuccessOrWait();
/*      */ 
/*      */     
/*  503 */     if (!hasFeature("bind", "urn:ietf:params:xml:ns:xmpp-bind"))
/*      */     {
/*      */       
/*  506 */       throw new SmackException.ResourceBindingNotOfferedException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  512 */     Bind bindResource = Bind.newSet(resource);
/*  513 */     PacketCollector packetCollector = createPacketCollectorAndSend((StanzaFilter)new StanzaIdFilter((Stanza)bindResource), (Stanza)bindResource);
/*  514 */     Bind response = packetCollector.<Bind>nextResultOrThrow();
/*      */ 
/*      */ 
/*      */     
/*  518 */     this.user = response.getJid();
/*  519 */     this.serviceName = this.user.asDomainBareJid();
/*      */     
/*  521 */     Session.Feature sessionFeature = getFeature("session", "urn:ietf:params:xml:ns:xmpp-session");
/*      */ 
/*      */     
/*  524 */     if (sessionFeature != null && !sessionFeature.isOptional() && !getConfiguration().isLegacySessionDisabled()) {
/*  525 */       Session session = new Session();
/*  526 */       packetCollector = createPacketCollectorAndSend((StanzaFilter)new StanzaIdFilter((Stanza)session), (Stanza)session);
/*  527 */       packetCollector.nextResultOrThrow();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void afterSuccessfulLogin(boolean resumed) throws SmackException.NotConnectedException, InterruptedException {
/*  533 */     this.authenticated = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  539 */     if (this.config.isDebuggerEnabled() && this.debugger != null) {
/*  540 */       this.debugger.userHasLogged(this.user);
/*      */     }
/*  542 */     callConnectionAuthenticatedListener(resumed);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  548 */     if (this.config.isSendPresence() && !resumed) {
/*  549 */       sendStanza((Stanza)new Presence(Presence.Type.available));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAnonymous() {
/*  555 */     return (this.config.getUsername() == null && this.usedUsername == null && !this.config.allowNullOrEmptyUsername);
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
/*      */   protected List<HostAddress> populateHostAddresses() {
/*  569 */     List<HostAddress> failedAddresses = new LinkedList<>();
/*      */     
/*  571 */     if (this.config.host != null) {
/*  572 */       this.hostAddresses = new ArrayList<>(1);
/*      */       
/*  574 */       HostAddress hostAddress = new HostAddress(this.config.host, this.config.port);
/*  575 */       this.hostAddresses.add(hostAddress);
/*      */     } else {
/*  577 */       this.hostAddresses = DNSUtil.resolveXMPPDomain(this.config.serviceName.toString(), failedAddresses);
/*      */     } 
/*      */ 
/*      */     
/*  581 */     assert !this.hostAddresses.isEmpty();
/*  582 */     return failedAddresses;
/*      */   }
/*      */   
/*      */   protected Lock getConnectionLock() {
/*  586 */     return this.connectionLock;
/*      */   }
/*      */   
/*      */   protected void throwNotConnectedExceptionIfAppropriate() throws SmackException.NotConnectedException {
/*  590 */     if (!isConnected()) {
/*  591 */       throw new SmackException.NotConnectedException();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void throwAlreadyConnectedExceptionIfAppropriate() throws SmackException.AlreadyConnectedException {
/*  596 */     if (isConnected()) {
/*  597 */       throw new SmackException.AlreadyConnectedException();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void throwAlreadyLoggedInExceptionIfAppropriate() throws SmackException.AlreadyLoggedInException {
/*  602 */     if (isAuthenticated()) {
/*  603 */       throw new SmackException.AlreadyLoggedInException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void sendPacket(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
/*  610 */     sendStanza(packet);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
/*  615 */     Objects.requireNonNull(packet, "Packet must not be null");
/*      */     
/*  617 */     throwNotConnectedExceptionIfAppropriate();
/*  618 */     switch (this.fromMode) {
/*      */       case sync:
/*  620 */         packet.setFrom((Jid)null);
/*      */         break;
/*      */       case async:
/*  623 */         packet.setFrom((Jid)getUser());
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  631 */     firePacketInterceptors(packet);
/*  632 */     sendStanzaInternal(packet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SASLAuthentication getSASLAuthentication() {
/*  643 */     return this.saslAuthentication;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void disconnect() {
/*      */     try {
/*  654 */       disconnect(new Presence(Presence.Type.unavailable));
/*      */     }
/*  656 */     catch (NotConnectedException e) {
/*  657 */       LOGGER.log(Level.FINEST, "Connection is already disconnected", e);
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
/*      */   public synchronized void disconnect(Presence unavailablePresence) throws SmackException.NotConnectedException {
/*      */     try {
/*  674 */       sendStanza((Stanza)unavailablePresence);
/*      */     }
/*  676 */     catch (InterruptedException e) {
/*  677 */       LOGGER.log(Level.FINE, "Was interrupted while sending unavailable presence. Continuing to disconnect the connection", e);
/*      */     } 
/*  679 */     shutdown();
/*  680 */     callConnectionClosedListener();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConnectionListener(ConnectionListener connectionListener) {
/*  690 */     if (connectionListener == null) {
/*      */       return;
/*      */     }
/*  693 */     this.connectionListeners.add(connectionListener);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeConnectionListener(ConnectionListener connectionListener) {
/*  698 */     this.connectionListeners.remove(connectionListener);
/*      */   }
/*      */ 
/*      */   
/*      */   public PacketCollector createPacketCollectorAndSend(IQ packet) throws SmackException.NotConnectedException, InterruptedException {
/*  703 */     IQReplyFilter iQReplyFilter = new IQReplyFilter(packet, this);
/*      */     
/*  705 */     PacketCollector packetCollector = createPacketCollectorAndSend((StanzaFilter)iQReplyFilter, (Stanza)packet);
/*  706 */     return packetCollector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PacketCollector createPacketCollectorAndSend(StanzaFilter packetFilter, Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
/*  713 */     PacketCollector packetCollector = createPacketCollector(packetFilter);
/*      */     
/*      */     try {
/*  716 */       sendStanza(packet);
/*      */     }
/*  718 */     catch (InterruptedException|NotConnectedException|RuntimeException e) {
/*  719 */       packetCollector.cancel();
/*  720 */       throw e;
/*      */     } 
/*  722 */     return packetCollector;
/*      */   }
/*      */ 
/*      */   
/*      */   public PacketCollector createPacketCollector(StanzaFilter packetFilter) {
/*  727 */     PacketCollector.Configuration configuration = PacketCollector.newConfiguration().setStanzaFilter(packetFilter);
/*  728 */     return createPacketCollector(configuration);
/*      */   }
/*      */ 
/*      */   
/*      */   public PacketCollector createPacketCollector(PacketCollector.Configuration configuration) {
/*  733 */     PacketCollector collector = new PacketCollector(this, configuration);
/*      */     
/*  735 */     this.collectors.add(collector);
/*  736 */     return collector;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePacketCollector(PacketCollector collector) {
/*  741 */     this.collectors.remove(collector);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void addPacketListener(StanzaListener packetListener, StanzaFilter packetFilter) {
/*  747 */     addAsyncStanzaListener(packetListener, packetFilter);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean removePacketListener(StanzaListener packetListener) {
/*  753 */     return removeAsyncStanzaListener(packetListener);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addSyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
/*  758 */     if (packetListener == null) {
/*  759 */       throw new NullPointerException("Packet listener is null.");
/*      */     }
/*  761 */     ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
/*  762 */     synchronized (this.syncRecvListeners) {
/*  763 */       this.syncRecvListeners.put(packetListener, wrapper);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeSyncStanzaListener(StanzaListener packetListener) {
/*  769 */     synchronized (this.syncRecvListeners) {
/*  770 */       return (this.syncRecvListeners.remove(packetListener) != null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAsyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
/*  776 */     if (packetListener == null) {
/*  777 */       throw new NullPointerException("Packet listener is null.");
/*      */     }
/*  779 */     ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
/*  780 */     synchronized (this.asyncRecvListeners) {
/*  781 */       this.asyncRecvListeners.put(packetListener, wrapper);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeAsyncStanzaListener(StanzaListener packetListener) {
/*  787 */     synchronized (this.asyncRecvListeners) {
/*  788 */       return (this.asyncRecvListeners.remove(packetListener) != null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPacketSendingListener(StanzaListener packetListener, StanzaFilter packetFilter) {
/*  794 */     if (packetListener == null) {
/*  795 */       throw new NullPointerException("Packet listener is null.");
/*      */     }
/*  797 */     ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
/*  798 */     synchronized (this.sendListeners) {
/*  799 */       this.sendListeners.put(packetListener, wrapper);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePacketSendingListener(StanzaListener packetListener) {
/*  805 */     synchronized (this.sendListeners) {
/*  806 */       this.sendListeners.remove(packetListener);
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
/*      */   protected void firePacketSendingListeners(final Stanza packet) {
/*  820 */     final List<StanzaListener> listenersToNotify = new LinkedList<>();
/*  821 */     synchronized (this.sendListeners) {
/*  822 */       for (ListenerWrapper listenerWrapper : this.sendListeners.values()) {
/*  823 */         if (listenerWrapper.filterMatches(packet)) {
/*  824 */           listenersToNotify.add(listenerWrapper.getListener());
/*      */         }
/*      */       } 
/*      */     } 
/*  828 */     if (listenersToNotify.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  832 */     asyncGo(new Runnable()
/*      */         {
/*      */           public void run() {
/*  835 */             for (StanzaListener listener : listenersToNotify) {
/*      */               try {
/*  837 */                 listener.processPacket(packet);
/*      */               }
/*  839 */               catch (Exception e) {
/*  840 */                 AbstractXMPPConnection.LOGGER.log(Level.WARNING, "Sending listener threw exception", e);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPacketInterceptor(StanzaListener packetInterceptor, StanzaFilter packetFilter) {
/*  850 */     if (packetInterceptor == null) {
/*  851 */       throw new NullPointerException("Packet interceptor is null.");
/*      */     }
/*  853 */     InterceptorWrapper interceptorWrapper = new InterceptorWrapper(packetInterceptor, packetFilter);
/*  854 */     synchronized (this.interceptors) {
/*  855 */       this.interceptors.put(packetInterceptor, interceptorWrapper);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePacketInterceptor(StanzaListener packetInterceptor) {
/*  861 */     synchronized (this.interceptors) {
/*  862 */       this.interceptors.remove(packetInterceptor);
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
/*      */   private void firePacketInterceptors(Stanza packet) {
/*  875 */     List<StanzaListener> interceptorsToInvoke = new LinkedList<>();
/*  876 */     synchronized (this.interceptors) {
/*  877 */       for (InterceptorWrapper interceptorWrapper : this.interceptors.values()) {
/*  878 */         if (interceptorWrapper.filterMatches(packet)) {
/*  879 */           interceptorsToInvoke.add(interceptorWrapper.getInterceptor());
/*      */         }
/*      */       } 
/*      */     } 
/*  883 */     for (StanzaListener interceptor : interceptorsToInvoke) {
/*      */       try {
/*  885 */         interceptor.processPacket(packet);
/*  886 */       } catch (Exception e) {
/*  887 */         LOGGER.log(Level.SEVERE, "Packet interceptor threw exception", e);
/*      */       } 
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
/*      */   protected void initDebugger() {
/*  900 */     if (this.reader == null || this.writer == null) {
/*  901 */       throw new NullPointerException("Reader or writer isn't initialized.");
/*      */     }
/*      */     
/*  904 */     if (this.config.isDebuggerEnabled()) {
/*  905 */       if (this.debugger == null) {
/*  906 */         this.debugger = SmackConfiguration.createDebugger(this, this.writer, this.reader);
/*      */       }
/*      */       
/*  909 */       if (this.debugger == null) {
/*  910 */         LOGGER.severe("Debugging enabled but could not find debugger class");
/*      */       } else {
/*      */         
/*  913 */         this.reader = this.debugger.newConnectionReader(this.reader);
/*  914 */         this.writer = this.debugger.newConnectionWriter(this.writer);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long getPacketReplyTimeout() {
/*  921 */     return this.packetReplyTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPacketReplyTimeout(long timeout) {
/*  926 */     this.packetReplyTimeout = timeout;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean replyToUnknownIqDefault = true;
/*      */   
/*      */   private boolean replyToUnkownIq;
/*      */   
/*      */   private long lastStanzaReceived;
/*      */ 
/*      */   
/*      */   public static void setReplyToUnknownIqDefault(boolean replyToUnkownIqDefault) {
/*  939 */     replyToUnknownIqDefault = replyToUnkownIqDefault;
/*      */   }
/*      */   protected AbstractXMPPConnection(ConnectionConfiguration configuration) {
/*  942 */     this.replyToUnkownIq = replyToUnknownIqDefault;
/*      */     this.config = configuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReplyToUnknownIq(boolean replyToUnknownIq) {
/*  952 */     this.replyToUnkownIq = replyToUnknownIq;
/*      */   }
/*      */   
/*      */   protected void parseAndProcessStanza(XmlPullParser parser) throws Exception {
/*  956 */     ParserUtils.assertAtStartTag(parser);
/*  957 */     int parserDepth = parser.getDepth();
/*  958 */     Stanza stanza = null;
/*      */     try {
/*  960 */       stanza = PacketParserUtils.parseStanza(parser);
/*      */     }
/*  962 */     catch (Exception e) {
/*  963 */       CharSequence content = PacketParserUtils.parseContentDepth(parser, parserDepth);
/*      */       
/*  965 */       UnparsablePacket message = new UnparsablePacket(content, e);
/*  966 */       ParsingExceptionCallback callback = getParsingExceptionCallback();
/*  967 */       if (callback != null) {
/*  968 */         callback.handleUnparsablePacket(message);
/*      */       }
/*      */     } 
/*  971 */     ParserUtils.assertAtEndTag(parser);
/*  972 */     if (stanza != null) {
/*  973 */       processPacket(stanza);
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
/*      */   protected void processPacket(Stanza packet) {
/*  985 */     assert packet != null;
/*  986 */     this.lastStanzaReceived = System.currentTimeMillis();
/*      */     
/*  988 */     this.executorService.submit(new ListenerNotification(packet));
/*      */   }
/*      */ 
/*      */   
/*      */   private class ListenerNotification
/*      */     implements Runnable
/*      */   {
/*      */     private final Stanza packet;
/*      */ 
/*      */     
/*      */     public ListenerNotification(Stanza packet) {
/*  999 */       this.packet = packet;
/*      */     }
/*      */     
/*      */     public void run() {
/* 1003 */       AbstractXMPPConnection.this.invokePacketCollectorsAndNotifyRecvListeners(this.packet);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void invokePacketCollectorsAndNotifyRecvListeners(final Stanza packet) {
/* 1014 */     if (packet instanceof IQ) {
/* 1015 */       String key; IQRequestHandler iqRequestHandler; ExecutorService executorService; final IQRequestHandler finalIqRequestHandler; final IQ iq = (IQ)packet;
/* 1016 */       IQ.Type type = iq.getType();
/* 1017 */       switch (type) {
/*      */         case sync:
/*      */         case async:
/* 1020 */           key = XmppStringUtils.generateKey(iq.getChildElementName(), iq.getChildElementNamespace());
/* 1021 */           iqRequestHandler = null;
/* 1022 */           switch (type) {
/*      */             case sync:
/* 1024 */               synchronized (this.setIqRequestHandler) {
/* 1025 */                 iqRequestHandler = this.setIqRequestHandler.get(key);
/*      */               } 
/*      */               break;
/*      */             case async:
/* 1029 */               synchronized (this.getIqRequestHandler) {
/* 1030 */                 iqRequestHandler = this.getIqRequestHandler.get(key);
/*      */               } 
/*      */               break;
/*      */             default:
/* 1034 */               throw new IllegalStateException("Should only encounter IQ type 'get' or 'set'");
/*      */           } 
/* 1036 */           if (iqRequestHandler == null) {
/* 1037 */             if (!this.replyToUnkownIq) {
/*      */               return;
/*      */             }
/*      */ 
/*      */             
/* 1042 */             ErrorIQ errorIQ = IQ.createErrorResponse(iq, new XMPPError(XMPPError.Condition.feature_not_implemented));
/*      */             
/*      */             try {
/* 1045 */               sendStanza((Stanza)errorIQ);
/*      */             }
/* 1047 */             catch (InterruptedException|NotConnectedException e) {
/* 1048 */               LOGGER.log(Level.WARNING, "Exception while sending error IQ to unkown IQ request", e);
/*      */             }  break;
/*      */           } 
/* 1051 */           executorService = null;
/* 1052 */           switch (iqRequestHandler.getMode()) {
/*      */             case sync:
/* 1054 */               executorService = this.singleThreadedExecutorService;
/*      */               break;
/*      */             case async:
/* 1057 */               executorService = this.cachedExecutorService;
/*      */               break;
/*      */           } 
/* 1060 */           finalIqRequestHandler = iqRequestHandler;
/* 1061 */           executorService.execute(new Runnable()
/*      */               {
/*      */                 public void run() {
/* 1064 */                   IQ response = finalIqRequestHandler.handleIQRequest(iq);
/* 1065 */                   if (response == null) {
/*      */                     return;
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   try {
/* 1074 */                     AbstractXMPPConnection.this.sendStanza((Stanza)response);
/*      */                   }
/* 1076 */                   catch (InterruptedException|NotConnectedException e) {
/* 1077 */                     AbstractXMPPConnection.LOGGER.log(Level.WARNING, "Exception while sending response to IQ request", e);
/*      */                   } 
/*      */                 }
/*      */               });
/*      */           return;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/* 1094 */     final Collection<StanzaListener> listenersToNotify = new LinkedList<>();
/* 1095 */     synchronized (this.asyncRecvListeners) {
/* 1096 */       for (ListenerWrapper listenerWrapper : this.asyncRecvListeners.values()) {
/* 1097 */         if (listenerWrapper.filterMatches(packet)) {
/* 1098 */           listenersToNotify.add(listenerWrapper.getListener());
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1103 */     for (StanzaListener listener : listenersToNotify) {
/* 1104 */       asyncGo(new Runnable()
/*      */           {
/*      */             public void run() {
/*      */               try {
/* 1108 */                 listener.processPacket(packet);
/* 1109 */               } catch (Exception e) {
/* 1110 */                 AbstractXMPPConnection.LOGGER.log(Level.SEVERE, "Exception in async packet listener", e);
/*      */               } 
/*      */             }
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/* 1117 */     for (PacketCollector collector : this.collectors) {
/* 1118 */       collector.processPacket(packet);
/*      */     }
/*      */ 
/*      */     
/* 1122 */     listenersToNotify.clear();
/* 1123 */     synchronized (this.syncRecvListeners) {
/* 1124 */       for (ListenerWrapper listenerWrapper : this.syncRecvListeners.values()) {
/* 1125 */         if (listenerWrapper.filterMatches(packet)) {
/* 1126 */           listenersToNotify.add(listenerWrapper.getListener());
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1133 */     this.singleThreadedExecutorService.execute(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1136 */             for (StanzaListener listener : listenersToNotify) {
/*      */               try {
/* 1138 */                 listener.processPacket(packet);
/* 1139 */               } catch (NotConnectedException e) {
/* 1140 */                 AbstractXMPPConnection.LOGGER.log(Level.WARNING, "Got not connected exception, aborting", e);
/*      */                 break;
/* 1142 */               } catch (Exception e) {
/* 1143 */                 AbstractXMPPConnection.LOGGER.log(Level.SEVERE, "Exception in packet listener", e);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setWasAuthenticated() {
/* 1158 */     if (!this.wasAuthenticated) {
/* 1159 */       this.wasAuthenticated = this.authenticated;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void callConnectionConnectedListener() {
/* 1164 */     for (ConnectionListener listener : this.connectionListeners) {
/* 1165 */       listener.connected(this);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void callConnectionAuthenticatedListener(boolean resumed) {
/* 1170 */     for (ConnectionListener listener : this.connectionListeners) {
/*      */       try {
/* 1172 */         listener.authenticated(this, resumed);
/* 1173 */       } catch (Exception e) {
/*      */ 
/*      */         
/* 1176 */         LOGGER.log(Level.SEVERE, "Exception in authenticated listener", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   void callConnectionClosedListener() {
/* 1182 */     for (ConnectionListener listener : this.connectionListeners) {
/*      */       try {
/* 1184 */         listener.connectionClosed();
/*      */       }
/* 1186 */       catch (Exception e) {
/*      */ 
/*      */         
/* 1189 */         LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void callConnectionClosedOnErrorListener(Exception e) {
/* 1195 */     LOGGER.log(Level.WARNING, "Connection closed with error", e);
/* 1196 */     for (ConnectionListener listener : this.connectionListeners) {
/*      */       try {
/* 1198 */         listener.connectionClosedOnError(e);
/*      */       }
/* 1200 */       catch (Exception e2) {
/*      */ 
/*      */         
/* 1203 */         LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void notifyReconnection() {
/* 1213 */     for (ConnectionListener listener : this.connectionListeners) {
/*      */       try {
/* 1215 */         listener.reconnectionSuccessful();
/*      */       }
/* 1217 */       catch (Exception e) {
/*      */ 
/*      */         
/* 1220 */         LOGGER.log(Level.WARNING, "notifyReconnection()", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class ListenerWrapper
/*      */   {
/*      */     private final StanzaListener packetListener;
/*      */ 
/*      */ 
/*      */     
/*      */     private final StanzaFilter packetFilter;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenerWrapper(StanzaListener packetListener, StanzaFilter packetFilter) {
/* 1240 */       this.packetListener = packetListener;
/* 1241 */       this.packetFilter = packetFilter;
/*      */     }
/*      */     
/*      */     public boolean filterMatches(Stanza packet) {
/* 1245 */       return (this.packetFilter == null || this.packetFilter.accept(packet));
/*      */     }
/*      */     
/*      */     public StanzaListener getListener() {
/* 1249 */       return this.packetListener;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class InterceptorWrapper
/*      */   {
/*      */     private final StanzaListener packetInterceptor;
/*      */ 
/*      */ 
/*      */     
/*      */     private final StanzaFilter packetFilter;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InterceptorWrapper(StanzaListener packetInterceptor, StanzaFilter packetFilter) {
/* 1268 */       this.packetInterceptor = packetInterceptor;
/* 1269 */       this.packetFilter = packetFilter;
/*      */     }
/*      */     
/*      */     public boolean filterMatches(Stanza packet) {
/* 1273 */       return (this.packetFilter == null || this.packetFilter.accept(packet));
/*      */     }
/*      */     
/*      */     public StanzaListener getInterceptor() {
/* 1277 */       return this.packetInterceptor;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getConnectionCounter() {
/* 1283 */     return this.connectionCounterValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFromMode(XMPPConnection.FromMode fromMode) {
/* 1288 */     this.fromMode = fromMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public XMPPConnection.FromMode getFromMode() {
/* 1293 */     return this.fromMode;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/* 1298 */     LOGGER.fine("finalizing XMPPConnection ( " + getConnectionCounter() + "): Shutting down executor services");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1306 */       this.executorService.shutdownNow();
/* 1307 */       this.cachedExecutorService.shutdown();
/* 1308 */       this.removeCallbacksService.shutdownNow();
/* 1309 */       this.singleThreadedExecutorService.shutdownNow();
/* 1310 */     } catch (Throwable t) {
/* 1311 */       LOGGER.log(Level.WARNING, "finalize() threw trhowable", t);
/*      */     } finally {
/*      */       
/* 1314 */       super.finalize();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void parseFeatures(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException, InterruptedException {
/* 1320 */     this.streamFeatures.clear();
/* 1321 */     int initialDepth = parser.getDepth();
/*      */     while (true) {
/* 1323 */       int eventType = parser.next();
/*      */       
/* 1325 */       if (eventType == 2 && parser.getDepth() == initialDepth + 1) {
/* 1326 */         StartTls startTls; Mechanisms mechanisms; Bind.Feature feature2; Session.Feature feature1; Compress.Feature feature; ExtensionElement extensionElement1; ExtensionElementProvider<ExtensionElement> provider; ExtensionElement streamFeature = null;
/* 1327 */         String name = parser.getName();
/* 1328 */         String namespace = parser.getNamespace();
/* 1329 */         switch (name) {
/*      */           case "starttls":
/* 1331 */             startTls = PacketParserUtils.parseStartTlsFeature(parser);
/*      */             break;
/*      */           case "mechanisms":
/* 1334 */             mechanisms = new Mechanisms(PacketParserUtils.parseMechanisms(parser));
/*      */             break;
/*      */           case "bind":
/* 1337 */             feature2 = Bind.Feature.INSTANCE;
/*      */             break;
/*      */           case "session":
/* 1340 */             feature1 = PacketParserUtils.parseSessionFeature(parser);
/*      */             break;
/*      */           case "compression":
/* 1343 */             feature = PacketParserUtils.parseCompressionFeature(parser);
/*      */             break;
/*      */           default:
/* 1346 */             provider = ProviderManager.getStreamFeatureProvider(name, namespace);
/* 1347 */             if (provider != null) {
/* 1348 */               extensionElement1 = (ExtensionElement)provider.parse(parser);
/*      */             }
/*      */             break;
/*      */         } 
/* 1352 */         if (extensionElement1 != null)
/* 1353 */           addStreamFeature(extensionElement1); 
/*      */         continue;
/*      */       } 
/* 1356 */       if (eventType == 3 && parser.getDepth() == initialDepth) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 1361 */     if (hasFeature("mechanisms", "urn:ietf:params:xml:ns:xmpp-sasl"))
/*      */     {
/* 1363 */       if (!hasFeature("starttls", "urn:ietf:params:xml:ns:xmpp-tls") || this.config.getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled)
/*      */       {
/* 1365 */         this.saslFeatureReceived.reportSuccess();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1371 */     if (hasFeature("bind", "urn:ietf:params:xml:ns:xmpp-bind") && (
/* 1372 */       !hasFeature("compression", "http://jabber.org/protocol/compress") || !this.config.isCompressionEnabled()))
/*      */     {
/*      */ 
/*      */       
/* 1376 */       this.lastFeaturesReceived.reportSuccess();
/*      */     }
/*      */     
/* 1379 */     afterFeaturesReceived();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void afterFeaturesReceived() throws SmackException.SecurityRequiredException, SmackException.NotConnectedException, InterruptedException {}
/*      */ 
/*      */ 
/*      */   
/*      */   public <F extends ExtensionElement> F getFeature(String element, String namespace) {
/* 1389 */     return (F)this.streamFeatures.get(XmppStringUtils.generateKey(element, namespace));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasFeature(String element, String namespace) {
/* 1394 */     return (getFeature(element, namespace) != null);
/*      */   }
/*      */   
/*      */   private void addStreamFeature(ExtensionElement feature) {
/* 1398 */     String key = XmppStringUtils.generateKey(feature.getElementName(), feature.getNamespace());
/* 1399 */     this.streamFeatures.put(key, feature);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback) throws SmackException.NotConnectedException, InterruptedException {
/* 1405 */     sendStanzaWithResponseCallback(stanza, replyFilter, callback, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback, ExceptionCallback exceptionCallback) throws SmackException.NotConnectedException, InterruptedException {
/* 1412 */     sendStanzaWithResponseCallback(stanza, replyFilter, callback, exceptionCallback, getPacketReplyTimeout());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStanzaWithResponseCallback(Stanza stanza, final StanzaFilter replyFilter, final StanzaListener callback, final ExceptionCallback exceptionCallback, long timeout) throws SmackException.NotConnectedException, InterruptedException {
/* 1420 */     Objects.requireNonNull(stanza, "stanza must not be null");
/*      */ 
/*      */     
/* 1423 */     Objects.requireNonNull(replyFilter, "replyFilter must not be null");
/* 1424 */     Objects.requireNonNull(callback, "callback must not be null");
/*      */     
/* 1426 */     final StanzaListener packetListener = new StanzaListener()
/*      */       {
/*      */         public void processPacket(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
/*      */           try {
/* 1430 */             XMPPException.XMPPErrorException.ifHasErrorThenThrow(packet);
/* 1431 */             callback.processPacket(packet);
/*      */           }
/* 1433 */           catch (XMPPErrorException e) {
/* 1434 */             if (exceptionCallback != null) {
/* 1435 */               exceptionCallback.processException(e);
/*      */             }
/*      */           } finally {
/*      */             
/* 1439 */             AbstractXMPPConnection.this.removeAsyncStanzaListener(this);
/*      */           } 
/*      */         }
/*      */       };
/* 1443 */     this.removeCallbacksService.schedule(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1446 */             boolean removed = AbstractXMPPConnection.this.removeAsyncStanzaListener(packetListener);
/*      */ 
/*      */             
/* 1449 */             if (removed && exceptionCallback != null) {
/* 1450 */               exceptionCallback.processException(SmackException.NoResponseException.newWith(AbstractXMPPConnection.this, replyFilter));
/*      */             }
/*      */           }
/*      */         }timeout, TimeUnit.MILLISECONDS);
/* 1454 */     addAsyncStanzaListener(packetListener, replyFilter);
/* 1455 */     sendStanza(stanza);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback) throws SmackException.NotConnectedException, InterruptedException {
/* 1461 */     sendIqWithResponseCallback(iqRequest, callback, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback) throws SmackException.NotConnectedException, InterruptedException {
/* 1467 */     sendIqWithResponseCallback(iqRequest, callback, exceptionCallback, getPacketReplyTimeout());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback, long timeout) throws SmackException.NotConnectedException, InterruptedException {
/* 1474 */     IQReplyFilter iQReplyFilter = new IQReplyFilter(iqRequest, this);
/* 1475 */     sendStanzaWithResponseCallback((Stanza)iqRequest, (StanzaFilter)iQReplyFilter, callback, exceptionCallback, timeout);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addOneTimeSyncCallback(final StanzaListener callback, StanzaFilter packetFilter) {
/* 1480 */     final StanzaListener packetListener = new StanzaListener()
/*      */       {
/*      */         public void processPacket(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
/*      */           try {
/* 1484 */             callback.processPacket(packet);
/*      */           } finally {
/* 1486 */             AbstractXMPPConnection.this.removeSyncStanzaListener(this);
/*      */           } 
/*      */         }
/*      */       };
/* 1490 */     addSyncStanzaListener(packetListener, packetFilter);
/* 1491 */     this.removeCallbacksService.schedule(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1494 */             AbstractXMPPConnection.this.removeSyncStanzaListener(packetListener);
/*      */           }
/*      */         },  getPacketReplyTimeout(), TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */   
/*      */   public IQRequestHandler registerIQRequestHandler(IQRequestHandler iqRequestHandler) {
/* 1501 */     String key = XmppStringUtils.generateKey(iqRequestHandler.getElement(), iqRequestHandler.getNamespace());
/* 1502 */     switch (iqRequestHandler.getType()) {
/*      */       case sync:
/* 1504 */         synchronized (this.setIqRequestHandler) {
/* 1505 */           return this.setIqRequestHandler.put(key, iqRequestHandler);
/*      */         } 
/*      */       case async:
/* 1508 */         synchronized (this.getIqRequestHandler) {
/* 1509 */           return this.getIqRequestHandler.put(key, iqRequestHandler);
/*      */         } 
/*      */     } 
/* 1512 */     throw new IllegalArgumentException("Only IQ type of 'get' and 'set' allowed");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final IQRequestHandler unregisterIQRequestHandler(IQRequestHandler iqRequestHandler) {
/* 1518 */     return unregisterIQRequestHandler(iqRequestHandler.getElement(), iqRequestHandler.getNamespace(), iqRequestHandler.getType());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public IQRequestHandler unregisterIQRequestHandler(String element, String namespace, IQ.Type type) {
/* 1524 */     String key = XmppStringUtils.generateKey(element, namespace);
/* 1525 */     switch (type) {
/*      */       case sync:
/* 1527 */         synchronized (this.setIqRequestHandler) {
/* 1528 */           return this.setIqRequestHandler.remove(key);
/*      */         } 
/*      */       case async:
/* 1531 */         synchronized (this.getIqRequestHandler) {
/* 1532 */           return this.getIqRequestHandler.remove(key);
/*      */         } 
/*      */     } 
/* 1535 */     throw new IllegalArgumentException("Only IQ type of 'get' and 'set' allowed");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastStanzaReceived() {
/* 1542 */     return this.lastStanzaReceived;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParsingExceptionCallback(ParsingExceptionCallback callback) {
/* 1552 */     this.parsingExceptionCallback = callback;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ParsingExceptionCallback getParsingExceptionCallback() {
/* 1561 */     return this.parsingExceptionCallback;
/*      */   }
/*      */   
/*      */   protected final void asyncGo(Runnable runnable) {
/* 1565 */     this.cachedExecutorService.execute(runnable);
/*      */   }
/*      */   
/*      */   protected final ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
/* 1569 */     return this.removeCallbacksService.schedule(runnable, delay, unit);
/*      */   }
/*      */   
/*      */   public abstract boolean isSecureConnection();
/*      */   
/*      */   protected abstract void sendStanzaInternal(Stanza paramStanza) throws SmackException.NotConnectedException, InterruptedException;
/*      */   
/*      */   public abstract void send(PlainStreamElement paramPlainStreamElement) throws SmackException.NotConnectedException, InterruptedException;
/*      */   
/*      */   public abstract boolean isUsingCompression();
/*      */   
/*      */   protected abstract void connectInternal() throws SmackException, IOException, XMPPException, InterruptedException;
/*      */   
/*      */   protected abstract void loginNonAnonymously(String paramString1, String paramString2, String paramString3) throws XMPPException, SmackException, IOException, InterruptedException;
/*      */   
/*      */   protected abstract void loginAnonymously() throws XMPPException, SmackException, IOException, InterruptedException;
/*      */   
/*      */   protected abstract void shutdown();
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\AbstractXMPPConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */