package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.valueOf;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

class PlatformTypeHelper {

  static PlatformType getCurrentType() {
    return valueOf( SWT.getPlatform().toUpperCase() );
  }

  static PlatformType[] getUnusedTypes() {
    List<PlatformType> all = asList( PlatformType.values() );
    List<PlatformType> list = new ArrayList<PlatformType>( all );
    list.remove( getCurrentType() );
    return list.toArray( new PlatformType[ list.size() ] );
  }
}