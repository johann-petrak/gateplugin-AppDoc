# Use 

<h2>Overview</h2>

<p>The plugin really provides two separate kinds of functionality:
  * using one or more template files in a template directory and an application file (gapp, xgapp file), generate one or more documentation files. This works by filling the template with selected information from the application file to show details about the application and the processing resources included in the application. By convention, the preinstalled templates also use the features "@comment", "@author", and "@version" for additional documentation about the application and processing resources added by the user. The generation of documentation files can be initiated from a new visual resource tab ("AppDocGen") for applications in the GUI or by using a command line script.
  * A new visual resource tab ("AppDoc") for applications and processing resources in the GUI allows you to easily enter or modify author, version and documentation comments for each of these resources.</p>

<h2>GATE Developer GUI</h2>

<h3>1. Enable the plugin:</h3>

<ol>
<li>Go to File -&gt; Manage Creole Plugins</li>
<li>If the plugin archive was extracted in the GATE plugin directory you can skip this step. Otherwise click the "Add a Creole Repository" button and choose the directory where the AppDoc plugin is installed</li>
<li>The directory name appears in the list - check the "load now" button for that row and click OK</li>
</ol>

<h3>2. Load or create processing resources or pipelines</h3>

<p>For all processing resources and applications, a new additional visual resource tab "AppDoc" is now available. This tab is shown in the bottom of the main pane.</p>

<p>For applications (pipelines) another new visual resource tab "AppDocGen" is shown.</p>

<p><strong>Note:</strong> these tabs are only shown if the processing resource or
pipeline is created or loaded <strong>after</strong> the AppDoc plugin had been loaded.</p>

<p><img src="http://gateplugin-appdoc.googlecode.com/svn/wiki/gate-appdoc-tabs.png" alt="http://gateplugin-appdoc.googlecode.com/svn/wiki/gate-appdoc-tabs.png" title=""></p>

<p>In the AppDoc tab you can comfortably add or edit author, version and documentation comments for the resource. The information is immediately stored in the resource's features when the cursor is moved out of the editing area or another tab is shown.</p>

<p>The AppDocGen tab contains three buttons and a drop-down
combo-box:</p>

<ul>
<li>You can change the template directory to use a different set of templates and files for your generated documentation</li>
<li>You can change the directory where the generated documentation files are placed</li>
<li>With the "Save" button, you can save the current version of the application and initiate the documentation generation process</li>
<li>The comment format tells the documentation generator which format to expect for the comment field:

<ul><li>if HTML is selected and HTML files are generated, all HTML tags and entities will be placed into the output unchanged, allowing you to use HTML to format the documentation. To show "&gt;", "&lt;", or "&amp;" you have  to manually enter their corresponding HTML entities, e.g. "&lt;"</li>
<li>if "Plain text" is selected and HTML files are generated, empty lines are converted to paragraph breaks and the characters "&lt;". "&gt;" amd "&amp;" are converted into HTML entities.</li></ul></li>
</ul>

<h3>3. Generate the documentation file</h3>

<p>In the AppDocGen tab of the pipeline, use the "Save" button to store
the pipeline and generate the documentation file in the directory
shown as output directory.</p>

<p>If an error occurs, details will be shown in the message box. If the error occurs because of a bug in a file within a template directory that comes with the plugin, please <a href="http://code.google.com/p/gateplugin-appdoc/issues/entry">add an issue to the bug tracker</a>.</p>

<p>Alternately, you can just save the pipeline and use the command line tool (see below) for generating the documentation.</p>

<h2>Command Line</h2>

<p>NOTE: generating documentation from the command line only works for Linux for now!</p>

<p>You can find the script <code>appDoc.sh</code> in the <code>bin</code> subdirectory of the plugin directory. This script can be used to generate documentatio for an application file from  the command line.</p>

<p>Use the <code>-h</code> parameter to show usage information.</p>

<p>A typical way how to generate documentation for the application file <code>"mypipeline.gapp"</code> using the templates in directory <code>"&lt;myAppDocPath&gt;/templates/html_default"</code> and placing the output into directory <code>"./docdir"</code> would be:</p>

<blockquote>
  <p><code>&lt;myAppDocPath&gt;/bin/appDoc.sh -t &lt;myAppDocPath&gt;/templates/html_default -o ./docdir -g mypipeline.gapp</code></p>
</blockquote>
