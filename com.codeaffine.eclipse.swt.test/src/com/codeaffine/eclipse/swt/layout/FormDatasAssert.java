/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.layout;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Control;

public class FormDatasAssert extends AbstractAssert<FormDatasAssert, FormDatas> {

  private static final String PATTERN_ASSIGNED_TO
    = "Expected FormData <%s> to be assigned to given control but was <%s>.";
  private static final String PATTERN_ATTACHMENT_EXISTS = "Expected %s attachment to exist but was null.";
  private static final String PATTERN_SAME = "Expected %s to be <%s> but was <%s>.";

  public FormDatasAssert( FormDatas actual ) {
    super( actual, FormDatasAssert.class );
  }

  public static FormDatasAssert assertThat( FormDatas actual ) {
    return new FormDatasAssert( actual );
  }

  public FormDatasAssert isAssignedTo( Control control ) {
    isNotNull();
    if( actual.getFormData() == null ) {
      failWithMessage( "Expected actual FormData not to be null but it was." );
    }
    if( actual.getFormData() != control.getLayoutData() ) {
      failWithMessage( PATTERN_ASSIGNED_TO, actual.getFormData(), control.getLayoutData() );
    }
    return this;
  }

  public FormDatasAssert hasWidth( int width ) {
    isNotNull();
    isEquals( "width", width, actual.getFormData().width );
    return this;
  }

  public FormDatasAssert hasHeight( int height ) {
    isNotNull();
    isEquals( "height", height, actual.getFormData().height );
    return this;
  }

  public FormDatasAssert isAttachedToLeft( int numerator, int margin ) {
    isNotNull();
    verifyAttachment( actual.getFormData().left, "left", numerator, margin );
    return this;
  }

  public FormDatasAssert isAttachedToRight( int numerator, int margin ) {
    isNotNull();
    verifyAttachment( actual.getFormData().right, "right", numerator, margin );
    return this;
  }

  public FormDatasAssert isAttachedToTop( int numerator, int margin ) {
    isNotNull();
    verifyAttachment( actual.getFormData().top, "top", numerator, margin );
    return this;
  }

  public FormDatasAssert isAttachedToBottom( int numerator, int margin ) {
    isNotNull();
    verifyAttachment( actual.getFormData().bottom, "bottom", numerator, margin );
    return this;
  }

  public FormDatasAssert isAttachedAtLeftTo( Control control, int margin, int alignment ) {
    isNotNull();
    verifyAttachment( actual.getFormData().left, "left", control, margin, alignment );
    return this;
  }

  public FormDatasAssert isAttachedAtRightTo( Control control, int margin, int alignment ) {
    isNotNull();
    verifyAttachment( actual.getFormData().right, "right", control, margin, alignment );
    return this;
  }

  public FormDatasAssert isAttachedAtTopTo( Control control, int margin, int alignment ) {
    isNotNull();
    verifyAttachment( actual.getFormData().top, "top", control, margin, alignment );
    return this;
  }

  public FormDatasAssert isAttachedAtBottomTo( Control control, int margin, int alignment ) {
    isNotNull();
    verifyAttachment( actual.getFormData().bottom, "bottom", control, margin, alignment );
    return this;
  }

  private void attachmentExists( FormAttachment attachment, String direction ) {
    if( null == attachment ) {
      failWithMessage( PATTERN_ATTACHMENT_EXISTS, direction );
    }
  }

  private void verifyAttachment( FormAttachment attachment, String direction, int numerator, int margin ) {
    attachmentExists( attachment, direction );
    verifyAttachment( attachment, numerator, margin );
  }

  private void verifyAttachment( FormAttachment attachment , int numerator , int margin  ) {
    isEquals( "numerator", numerator, attachment.numerator );
    isEquals( "offset", margin, attachment.offset );
    isEquals( "aligment", 0, attachment.alignment );
    isSame( "control", null, attachment.control );
  }

  private void verifyAttachment(
    FormAttachment attachment, String direction, Control control, int margin, int alignment )
  {
    attachmentExists( attachment, direction );
    verifyAttachment( attachment, control, margin, alignment );
  }

  private void verifyAttachment( FormAttachment attachment , Control control , int margin , int alignment  ) {
    isSame( "control", control, attachment.control );
    isEquals( "offset", margin, attachment.offset );
    isEquals( "aligment", alignment, attachment.alignment );
    isEquals( "numerator", 0, attachment.numerator );
  }

  private void isSame( String name, Object first, Object second ) {
    if( first != second ) {
      failWithMessage( PATTERN_SAME, name, first, second );
    }
  }

  private void isEquals( String name, int first, int second ) {
    if( first != second ) {
      failWithMessage( PATTERN_SAME, name, first, second );
    }
  }
}