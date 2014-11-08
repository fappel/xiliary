package com.codeaffine.xiliary.alltest;

import org.junit.runner.RunWith;

import com.codeaffine.osgi.testuite.BundleTestSuite;
import com.codeaffine.osgi.testuite.BundleTestSuite.TestBundles;

@RunWith( BundleTestSuite.class )
@TestBundles( {
  "com.codeaffine.eclipse.core.runtime",
  "com.codeaffine.eclipse.core.runtime.test.util",
  "com.codeaffine.eclipse.swt",
  "com.codeaffine.eclipse.swt.test.util",
  "com.codeaffine.test.util"
} )
public class AllXiliaryTestSuite {
}
