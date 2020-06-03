# Changes

<h2>Changes for version 0.19</h2>

<ul>
<li>Compile with source/target 1.6</li>
</ul>

<h2>Changes for version 0.18</h2>

<ul>
<li>Correct bug when generating from creole input format and some comments were empty</li>
</ul>

<h2>Changes for version 0.17</h2>

<ul>
<li>The "Moinmoin" format has been renamed to the better name "creole" format. If your (x)gapp file contains "moinmoin" somewhere, best you change that to "creole".</li>
<li>Plain format should not support URLfication and a "\" at the end of a line will be converted to a deliberate newline</li>
<li>Several libraries have been upgraded to newer versions</li>
</ul>

<h2>Changes for version 0.16</h2>

<ul>
<li>Support the Scriptable Controller provided by the Groovy plugin</li>
</ul>

<h2>Changes for version 0.15</h2>

<ul>
<li>Support the new HELPURL feature of the Plugin Manager</li>
</ul>

<h2>Changes for version 0.14</h2>

<ul>
<li>Support the new creole.xml format for the plugin manager</li>
</ul>

<h2>Changes for version 0.13</h2>

<ul>
<li>add JapeBackRefs and JPDAPlus as recognized JAPE Transducers</li>
<li>improved the html_default template: URLs and file paths are clickable</li>
</ul>

<h2>Changes for version 0.12</h2>

<ul>
<li>Some smaller bugfixes</li>
<li>Merged the nicer looking HTML template from html_test into html_default</li>
</ul>

<h2>Changes for version 0.11</h2>

<ul>
<li>fixed a bug in the JapeDoc parser and added information about the JAPE file when an exception is thrown in the JapeDoc parser</li>
<li>added a much nicer looking template contributed by Mark Greenwood as <code>html_test</code> (for now)</li>
</ul>

<h2>Changes for version 0.10</h2>

<ul>
<li>fixed a bug that prevented the command line interface from working</li>
<li>added JapeDoc support. There is now a new predefined function for retrieving JapeDoc from the grammar file of a JAPE Transducer PR. The generated JapeDoc is included in the <code>html_default</code> example template.</li>
</ul>

<h2>Changes for version 0.9</h2>

<ul>
<li>the template directory and output directory paths are stored using the relative path markers $relpath$ and $gatehome$ so that moving between computers and installations will work more often.</li>
<li>the helpURL of processing resources is passed on to the documentation and is available with the template function <code>getHelpURL(featureMapNode)</code>. The emaple HTML template includes the help URL as a link.</li>
</ul>

<h2>Changes for version 0.8</h2>

<p>Remove some debugging messages, small bugs fixed.</p>

<h2>Changes for version 0.7</h2>

<ul>
<li>added basic templates for RTF and LaTeX output</li>
<li>added code for generating RTF output</li>
</ul>

<h2>Changes for version 0.6</h2>

<ul>
<li>re-factored classes for converting formats</li>
<li>added a converter for MoinMoin wiki syntax to HTML</li>
</ul>

<h2>Changes for version 0.5</h2>

<ul>
<li>Include information about the condition in the output if the application is a conditional controller or conditional serial analyser controller</li>
</ul>

<h2>Changes for version 0.4</h2>

<ul>
<li>make it work under Windows</li>
</ul>
