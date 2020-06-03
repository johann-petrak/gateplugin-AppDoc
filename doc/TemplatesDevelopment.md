# TemplatesDevelopment

<p>This plugin uses the Freemarker template engine (<a href="http://freemarker.sourceforge.net/">http://freemarker.sourceforge.net/</a>) and the
FMPP text file processor (<a href="http://fmpp.sourceforge.net/">http://fmpp.sourceforge.net/</a>) internally to generate the documentation from your pipeline file. To create or change template files, please refer to the documentation for Freemarker and FMPP:
  * Freemarker Manual: <a href="http://freemarker.sourceforge.net/docs/index.html">http://freemarker.sourceforge.net/docs/index.html</a>
  * FMPP: <a href="http://fmpp.sourceforge.net/manual.html">http://fmpp.sourceforge.net/manual.html</a></p>

<p><strong>Note:</strong> Currently, the included templates serve as rough examples
and the various conversions between formats are only partly or
not at all implemented. What exactly will be included here will
also depend on what users need and contribute (e.g. in the form
of enhanced or additional templates).</p>

<h2>How to create new templates</h2>

<p>The easiest way to create a new template is probably to copy one of the existing templates and modify it. Each "template" is really a directory with a number of files and subdirectories that are used in the template generation process. A template directory usually has the following structure and contains the following files:
  * in the root template directory, the file <code>config.fmpp</code>. This file contains part of the configuration information used in the template generation process (the other part being provided by the generating program at generation time).
  * A subdirectory <code>src</code> with one or more template files and zero or more additional files: each template file is "processed", i.e. template filling directives are executed and information from the application file is placed into the file before it is written to the output directory. Additional files are simply copied unchanged. This way it is possible to e.g. generate HTML files that include graphics, CSS, or Javascript files.</p>

<h2>Available Templates</h2>

<p>See TemplatesList</p>

<h2>Predefined functions</h2>

<p>(more TBD)</p>

<h3><code>hasCondition(prNode)</code></h3>

<p>returns a boolean indicating if there is information about a condition associated with the given PR node.</p>

<h3><code>convertString(theString,fromFormat,toFormat)</code></h3>

<p>returns the result of converting string from format fromFormat to format toFormat. If the conversion is not supported or not known, the original string is returned and a warning is written to standard output.</p>

<h3><code>isConvertible(fromFormat,toFormat)</code></h3>

<p>checks if the AppDoc plugin knows how to convert the fromFormat to toFormat and returns a boolean. Thus it can be used in a template like this:</p>

<blockquote>
  <p><code>&lt;#if isConvertible("plain text","msword")&gt;in your dreams&lt;#else&gt;of course not&lt;/#if&gt;</code></p>
</blockquote>

<h3><code>japeDoc(japeFileName,fromFormat,toFormat)</code></h3>

<p>Returns a list of hashes which describe all JapeDoc entries in the JAPE file. Each hash has the following keys:
  * name: the name of the JAPE component documented. It is derived from the fist non-empty line after the JapeDoc which must be of the form <code>type: name</code>, e.g. <code>Phase: myNicePhase1</code>. If the hash is for a FILE JapeDoc, i.e. a JapeDoc that starts with <code>/**FILE</code> or <code>//[FILE</code> (see {JapeDoc}), this is the name of the JAPE file.
  * type: the type of JAPE component document, e.g. "phase" or "rule" (see name). Always lowecased.
  * docstring: the actual content of the JapeDoc comment, with leading comment markers and one space removed. The format of this string is converted according to the fromFormat and toFormat specification.</p>

<p>To use this function use code like this:
<code>
&lt;#assign japeDocList=japeDoc("someFile.jape","html","html")&gt;
&lt;#list japeDocList as japeDocEntry&gt;
Documentation for ${japeDocEntry.type} ${japeDocEntry.name}:&lt;br&gt;
${japeDocEntry.docstring}&lt;p&gt;
</code></p>

<h2>Predefined directives</h2>

<p>(TBD)</p>
