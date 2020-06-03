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
 * Converter that leaves the original string untouched
 * 
 * @author Johann Petrak
 */
public class IdentityConverter implements Converter {

  public String convert(String from) {
    return from;
  }

}
