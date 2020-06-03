# Troubleshooting

<h2>The AppDoc and AppDoc Tabs are now shown</h2>

<ul>
<li>Make sure that the plugin is loaded <strong>before</strong> the processing resource or the application (pipeline) is created or loaded.</li>
<li>Make sure there was no error message when loading the plugin. If you get something like "Bad version number in .class file" you are probably trying to use this with a Java version older than 1.6. Java 6 (1.6) is the minimum requirement!</li>
</ul>
