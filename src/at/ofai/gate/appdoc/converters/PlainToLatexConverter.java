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
 * Converter for plain text format to LaTeX.
 * <p>
 * NOTE: this is not implemented yet!!
 * 
 * @author johann
 */
public class PlainToLatexConverter implements Converter {

  public String convert(String from) {
    String text = from.replaceAll("%", "\\%");
    text = text.replaceAll("@", "\\verb!@!");
    text = text.replaceAll("_", "\\verb!_!");
    return text;
  }

}
