package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;

public interface Parser {
  boolean checkEvent(Event.ID paramID);
  
  Event peekEvent();
  
  Event getEvent();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\parser\Parser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */