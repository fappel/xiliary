The batch file 'create-module.bat' can be used to create a new xiliary 'module'
comprised of a new feature and a new plug-in complemented with an appropriate
test-fragment.

To be able to use the 'create-module.bat' the archetypes located in this
project's subfolder must be installed. To do so run 'mvn install' for each
archetype's 'pom.xml'.

The batch process' working directory must point to the working directory of the
local xiliary git repository. You might use the 'Create Module' launch
configuration, which provides an appropriate configuration automatically.


Usage:

Follow the instructions and provide the following input on prompts:

1) 'plug-in id' determines project folder, bundle-symbolic-name, test-fragment-
    and feature-postfixes and the like.
    Example:    com.codeaffine.module
             -> com.codeaffine.module.test, com.codeaffine.module.feature
2) 'plug-in name' determines plug-in, fragment and feature names/labels
    Example:    Module 
             -> Module Tests, Module Feature
3) 'plug-in version' determines common version settings for plug-in, fragment
    and feature and according 'pom' files. Should be the same as the version of
    the parent 'pom' in the xiliary releng project.
    Example:    0.1.0
             -> 0.1.0.qualifier, 0.1.0-SNAPSHOT

Once the projects are created the parent 'pom' and the category.xml in the
xiliary releng project have to be amended to recognize the new module.  

Notes:

It might be possible to provide a single archetype instead of the batch
file. However I did not found ad-hoc an solution to create the flat directory
structure without a parent directory. Furthermore I do not know if it is
possible to use the partial archetypes standalone as it is possible know. 