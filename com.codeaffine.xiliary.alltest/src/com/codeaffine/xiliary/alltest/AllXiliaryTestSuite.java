package com.codeaffine.xiliary.alltest;

import org.junit.runner.RunWith;

import com.codeaffine.osgi.testuite.BundleTestSuite;
import com.codeaffine.osgi.testuite.BundleTestSuite.TestBundles;

@RunWith( BundleTestSuite.class )
@TestBundles( {
  "com.codeaffine.eclipse.core.runtime"
} )
public class AllXiliaryTestSuite {
}
