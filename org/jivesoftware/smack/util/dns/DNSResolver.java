package org.jivesoftware.smack.util.dns;

import java.util.List;

public interface DNSResolver {
  List<SRVRecord> lookupSRVRecords(String paramString) throws Exception;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\dns\DNSResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */