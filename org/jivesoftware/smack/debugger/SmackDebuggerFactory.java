package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import org.jivesoftware.smack.XMPPConnection;

public interface SmackDebuggerFactory {
  SmackDebugger create(XMPPConnection paramXMPPConnection, Writer paramWriter, Reader paramReader) throws IllegalArgumentException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\SmackDebuggerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */