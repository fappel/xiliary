Xiliary   [![Build Status](https://travis-ci.org/fappel/xiliary.svg?branch=master)](https://travis-ci.org/fappel/xiliary)
=======

####Introduction
Auxiliary libraries for Eclipse, JUnit and Java development in general.

The components are available from this p2 software repository: http://fappel.github.io/xiliary - it includes the FlatScrollBar and more...

<a href="http://with-eclipse.github.io/" target="_blank">
<img alt="with-Eclipse logo" src="http://with-eclipse.github.io/with-eclipse-0.jpg" />
</a>


####FlatScrollBar
<img src="http://www.codeaffine.com/wp-content/uploads/2015/01/style-scrollbar.png" width="425" height="207"/>
Drop in replacement for all your Eclipse 4.4+ based RCP apps table and tree **scrollbars** ...
as easy as this in two tiny steps:
 * add bundles **com.codeaffine.eclipse.swt.jar** and **com.codeaffine.eclipse.ui.swt.theme.jar** to your target platform and to your products feature.xml
 * merge this snippet with your RCP apps CSS:
```
Tree {
  flat-scroll-bar: true;
  flat-scroll-bar-thumb: #cdcdcd;
  flat-scroll-bar-page-increment: #f8f8f8;
  flat-scroll-bar-thumb-top-level: #454545;
  flat-scroll-bar-page-increment-top-level: #f8f8f8;
}

Table {
  flat-scroll-bar: true;
  flat-scroll-bar-thumb: #cdcdcd;
  flat-scroll-bar-page-increment: #f8f8f8;
  flat-scroll-bar-thumb-top-level: #454545;
  flat-scroll-bar-page-increment-top-level: #f8f8f8;
}
```
