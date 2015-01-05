package com.codeaffine.workflow.persistence;

import java.io.InputStream;
import java.io.OutputStream;

public interface Persistence {

  Object serialize( Memento memento );
  void serialize( Memento memento, OutputStream out );

  Memento deserialize( Object data );
  Memento deserialize( InputStream in );
}
