package com.codeaffine.xiliary.alltest;

import org.junit.runner.RunWith;

import com.codeaffine.osgi.testuite.BundleTestSuite;
import com.codeaffine.osgi.testuite.BundleTestSuite.ClassnameFilters;
import com.codeaffine.osgi.testuite.BundleTestSuite.TestBundles;

@RunWith( BundleTestSuite.class )
@TestBundles( {
  "com.codeaffine.eclipse.ui",
  "com.codeaffine.eclipse.ui.swt.theme",
  "com.codeaffine.eclipse.core.runtime",
  "com.codeaffine.eclipse.core.runtime.test.util",
  "com.codeaffine.workflow",
  "com.codeaffine.osgi.test.util",
  "com.codeaffine.test.util"
} )
@ClassnameFilters( {
  ".*PDETest"
} )
public class AllXiliaryPDETestSuite {}