# JapeDoc

<p>For documenting JAPE files, a similar approach is used as for JavaDoc: everything in a special kind of comment is taken to be the documentation for the stuff that follows. For JapeDoc, two different special comment formats are possible:</p>

<ol>
<li><strong>similar to JavaDoc:</strong> such somments have to start with <code>/**</code>  and end with <code>*/</code> or <code>**/</code> and must have lines starting with a star and followed by a single space in between.</li>
<li><strong>using single line comments:</strong> such comments have to start with <code>//[</code> and end with <code>//]</code> and must have lines starting with a double slash followed by a single space in between.</li>
</ol>

<p>Here is an example for format 1:
<code>
/**
 * this is my documentation for the phase
 */
Phase: myPhase1
</code></p>

<p>And here the same in format 2:
<code>
//[
// this is my documentation for the phase
//]
Phase: myPhase1
</code></p>

<p>With both formats, the comment starter can be immediately followed by "FILE". In that case the comment is for the whole JAPE file and need not be followed by a phase or rule (and any phase or rule following it is ignored). Here is an example using format 2:
<code>
//[FILE
// This is the documentation for the whole JAPE file
//]
</code></p>
