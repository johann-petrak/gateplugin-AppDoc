/*
 *  AppDoc.java
 *
 *  $Id: AppDoc.java 12 2011-01-28 16:31:56Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */

package at.ofai.gate.appdoc.converters;

/**
 * Converter for plain text format to HTML
 * 
 * @author Johann Petrak
 */
public class PlainToHtmlConverter implements Converter {
  static final String urlRegexpString = "\\b((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";
  public String convert(String from) {
    String text = from.replaceAll("&", "&amp;");
    text = text.replaceAll("<", "&lt;");
    text = text.replaceAll(">", "&gt;");
    text = text.replaceAll("(?m)\\\\\\\\\\s*\\n","<br/>\n"); // \\ followed by optional whitespace
    text = text.replaceAll("(?m)^\\s*\\n", "\n<p>\n");
    text = text.replaceAll(urlRegexpString,"<a href=\"$1\">$1</a>");
    return text;
  }

}
