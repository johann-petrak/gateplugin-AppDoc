# Formats

<p>The AppDoc plugin allows you to specify in the AppDocGen tab what kind of format the text in the application and PR comments has. This setting is only relevant for converting markup you include into the comments or special formatting conventions to what is needed in the template that generates the final output.</p>

<p>For example, if the template generates a HTML page as the final output and you specify that HTML is to be used for comment markup, all the HTML markup in the comment texts will be passed unchanged to the final HTML documentation page. On the other hand, if you tell AppDoc that your comment texts are in "Plain text" format, any embedded HTML markup will show up as normal text in the generated HTML page, while empty lines will be converted to <code>&lt;p&gt;</code> in the output.</p>

<p>The AppDoc plugin does not limit what kind of format will be generated through a template in any way. However, only a limited set of conversions from markup in the comments to some kind of output format is supported. Templates should check if the conversion(s) needed for creating the final output are all supported before file generation is actual started (see predefined function <code>checkFormatConversion(fromFormat,toFormat)</code>
TemplatesDevelopment)</p>

<h2>Plain Text</h2>

<p>If a documentation comment is in plain text, the following conversion will take place when converted to HTML:
  * a new line is converted to a new paragraph
  * URLs are converted to links
  * a double backslash (\) at the end of a line is converted to a deliberate new line</p>

<h2>HTML</h2>

<p>HTML is copied over to HTML output format as is. Conversion to LaTeX is not supported.</p>

<h2>Creole</h2>

<p>Creole is a leightweight Wiki Markup language and its syntax is described here: <a href="http://wikicreole.org/wiki/Creole1.0">http://wikicreole.org/wiki/Creole1.0</a> It can only be converted to HTML.</p>
