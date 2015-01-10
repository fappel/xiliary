package com.codeaffine.eclipse.swt.util;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.util.concurrent.RunInThread;
import com.codeaffine.test.util.util.concurrent.RunInThreadRule;

public class UIThreadSynchronizerTest {

  @Rule
  public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule
  public final RunInThreadRule runInThreadRule = new RunInThreadRule();

  private Display display;
  private Widget widget;
  private Runnable runnable;
  private UIThreadSynchronizer synchronizer;

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testSyncExecRunsCode() {
    synchronizer.syncExec( widget, runnable );

    verify( runnable ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testSyncExecAfterWidgetIsDisposedDoesNotRunCode() throws InterruptedException {
    widget.dispose();

    runInThread( new Runnable() {
      @Override
      public void run() {
        synchronizer.syncExec( widget, runnable );
      }
    } );

    verify( runnable, never() ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testSyncExecWithDisposedDisplayDoesNotRunCode() {
    display.dispose();

    synchronizer.asyncExec( widget, runnable );

    verify( runnable, never() ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testAsyncExecRunsCode() {
    synchronizer.asyncExec( widget, runnable );
    flushPendingEvents();

    verify( runnable ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testAsyncExecRunsCodeFromBackgroundThread() throws InterruptedException {
    runInThread( new Runnable() {
      @Override
      public void run() {
        synchronizer.asyncExec( widget, runnable );
      }
    } );
    flushPendingEvents();

    verify( runnable ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testAsyncExecAfterWidgetIsDisposedDoesNotRunCode() throws InterruptedException {
    runInThread( new Runnable() {
      @Override
      public void run() {
        synchronizer.asyncExec( widget, runnable );
      }
    } );
    widget.dispose();
    flushPendingEvents();

    verify( runnable, never() ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test
  public void testAsyncExecWithDisposedDisplayDoesNotRunCode() {
    display.dispose();

    synchronizer.asyncExec( widget, runnable );
    flushPendingEvents();

    verify( runnable, never() ).run();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @RunInThread
  @Test(expected=SWTException.class)
  public void testAsyncExecPropagatesWidgetDisposedExceptionInRunnable() {
    doThrow( new SWTException( SWT.ERROR_WIDGET_DISPOSED ) ).when( runnable ).run();

    synchronizer.asyncExec( widget, runnable );
    flushPendingEvents();
  }

  @Before
  public void setUp() {
    display = new Display();
    widget = new Shell( display );
    runnable = mock( Runnable.class );
    synchronizer = new UIThreadSynchronizer();
  }

  @After
  public void tearDown() {
    display.dispose();
  }

  private void flushPendingEvents() {
    while( !display.isDisposed() && display.readAndDispatch() ) {}
  }

  private static void runInThread( final Runnable runnable ) throws InterruptedException {
    ExceptionGuard exceptionGuard = new ExceptionGuard( runnable );
    Thread thread = new Thread( exceptionGuard );
    thread.setDaemon( true );
    thread.start();
    thread.join();
    exceptionGuard.handleException();
  }

  private static class ExceptionGuard implements Runnable {
    private final Runnable runnable;
    private volatile Throwable caughtException;

    ExceptionGuard( Runnable runnable ) {
      this.runnable = runnable;
    }

    @Override
    public void run() {
      try {
        runnable.run();
      } catch( Throwable thr ) {
        caughtException = thr;
      }
    }

    void handleException() {
      if( caughtException != null ) {
        throw new RuntimeException( "Caught exception in thread", caughtException );
      }
    }
  }

}
