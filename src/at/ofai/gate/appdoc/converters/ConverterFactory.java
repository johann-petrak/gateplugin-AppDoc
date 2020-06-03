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
 * This factory class can be used to get a converter object for converting
 * between two formats.
 *
 * @author Johann Petrak
 */
public class ConverterFactory {
  /**
   * Return a converter object for conversions between the two given classes.
   * This will always return an object, for unsupported or unknown format
   * combinations, the identity converter is returned and a warning is written
   * to standard output.
   * 
   * @param fromFormat
   * @param toFormat
   * @return
   */
  public static Converter getConverter(String fromFormat, String toFormat) {
    Converter c = getConverterImpl(fromFormat, toFormat);
    if(c == null) {
      System.err.println("Conversion from "+fromFormat+" to "+toFormat+
        " not known or not supported, no conversion done");
      return new IdentityConverter();
    } else {
      return c;
    }
    

  }

  // returns a converter object or null if conversion is not supported or not known.
  private static Converter getConverterImpl(String fromFormat, String toFormat) {
    String from = fromFormat.trim().toLowerCase();
    String to   = toFormat.trim().toLowerCase();
    if(from.equals(to)) {
      //System.out.println("Same input output format, using identity: "+fromFormat+"/"+toFormat);
      return new IdentityConverter();
    }
    String fromto = fromFormat.toLowerCase() + "_" + toFormat.toLowerCase();
    if(fromto.equals("plain_html")) {
      //System.out.println("Converting fomr plain to HTML");
      return new PlainToHtmlConverter();
    }
    if(fromto.equals("plain_latex")) {
      //System.out.println("Converting from plain to latex");
      return new PlainToLatexConverter();
    }
    if(fromto.equals("plain_rtf")) {
      return new PlainToRtfConverter();
    }
    if(fromto.equals("creole_html")) {
      return new CreoleToHtmlConverter();
    }
    // This conversion is not known or not supported. For now we
    // just issue a warning message and return the text unchanged.
    System.err.println("Conversion from "+fromFormat+" to "+toFormat+
      " not known or not supported, no conversion done");
    return null;
  }

  public static boolean isConvertible(String fromFormat, String toFormat) {
    if(getConverterImpl(fromFormat,toFormat) == null) {
      return false;
    } else {
      return true;
    }
  }
}
