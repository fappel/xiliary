package com.codeaffine.eclipse.swt.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

public class FormDatas {

  static final int DENOMINATOR = 100;

  private final FormData formData;

  public static FormDatas attach( Control control ) {
    return new FormDatas( control );
  }

  public FormDatas toLeft() {
    return toLeft( 0 );
  }

  public FormDatas toLeft( int margin ) {
    formData.left = new FormAttachment( 0, margin );
    return this;
  }

  public FormDatas atLeftTo( Control control ) {
    return atLeftTo( control, 0 );
  }

  public FormDatas atLeftTo( Control control, int margin ) {
    return atLeftTo( control, margin, SWT.DEFAULT );
  }

  public FormDatas atLeftTo( Control control, int margin, int alignment ) {
    formData.left = new FormAttachment( control, margin, alignment );
    return this;
  }

  public FormDatas fromLeft( int numerator ) {
    return fromLeft( numerator, 0 );
  }

  public FormDatas fromLeft( int numerator, int margin ) {
    formData.left = new FormAttachment( numerator, margin );
    return this;
  }

  public FormDatas toRight() {
    return toRight( 0 );
  }

  public FormDatas toRight( int margin ) {
    formData.right = new FormAttachment( DENOMINATOR, -margin );
    return this;
  }

  public FormDatas atRightTo( Control control ) {
    atRightTo( control, 0 );
    return this;
  }

  public FormDatas atRightTo( Control control, int margin ) {
    return atRightTo( control, margin, SWT.DEFAULT );
  }

  public FormDatas atRightTo( Control control, int margin, int alignment ) {
    formData.right = new FormAttachment( control, -margin, alignment );
    return this;
  }

  public FormDatas fromRight( int numerator ) {
    return fromRight( numerator, 0 );
  }

  public FormDatas fromRight( int numerator, int margin ) {
    formData.right = new FormAttachment( DENOMINATOR - numerator, -margin );
    return this;
  }

  public FormDatas toTop() {
    return toTop( 0 );
  }

  public FormDatas toTop( int margin ) {
    formData.top = new FormAttachment( 0, margin );
    return this;
  }

  public FormDatas atTopTo( Control control ) {
    return atTopTo( control, 0 );
  }

  public FormDatas atTopTo( Control control, int margin ) {
    return atTopTo( control, margin, SWT.DEFAULT );
  }

  public FormDatas atTopTo( Control control, int margin, int alignment ) {
    formData.top = new FormAttachment( control, margin, alignment );
    return this;
  }

  public FormDatas fromTop( int numerator ) {
    return fromTop( numerator, 0 );
  }

  public FormDatas fromTop( int numerator, int margin ) {
    formData.top = new FormAttachment( numerator, margin );
    return this;
  }

  public FormDatas toBottom() {
    return toBottom( 0 );
  }

  public FormDatas toBottom( int margin ) {
    formData.bottom = new FormAttachment( DENOMINATOR, -margin );
    return this;
  }

  public FormDatas atBottomTo( Control control ) {
    return atBottomTo( control, 0 );
  }

  public FormDatas atBottomTo( Control control, int margin ) {
    return atBottomTo( control, margin, SWT.DEFAULT );
  }

  public FormDatas atBottomTo( Control control, int margin, int alignment ) {
    formData.bottom = new FormAttachment( control, -margin, alignment );
    return this;
  }

  public FormDatas fromBottom( int numerator ) {
    return fromBottom( numerator, 0 );
  }

  public FormDatas fromBottom( int numerator, int margin ) {
    formData.bottom = new FormAttachment( DENOMINATOR - numerator, -margin );
    return this;
  }

  public FormDatas withWidth( int width ) {
    formData.width = width;
    return this;
  }

  public FormDatas withHeight( int height ) {
    formData.height = height;
    return this;
  }

  public FormData getFormData() {
    return formData;
  }

  private FormDatas( Control control ) {
    formData = new FormData();
    control.setLayoutData( formData );
  }
}