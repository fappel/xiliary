package com.codeaffine.eclipse.swt.widget.scrollable;

interface SizeObserver {
  default boolean mustLayoutAdapter() { return false; }
  default void update() {}
}