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

import gate.util.GateRuntimeException;

import java.util.EnumSet;

import com.admc.jcreole.JCreole;
import com.admc.jcreole.JCreolePrivilege;

/**
 * Converter for converting Creole wiki format  to HTML
 * 
 * @author Johann Petrak
 */
public class CreoleToHtmlConverter implements Converter {

  public String convert(String from) {
    // CreoleParser parser = new CreoleParser();
    if(from == null || from.isEmpty()) {
      // if the text is empty just return an empty string.
      // The JCreole.parseCreole method will throw an exception if it
      // has to parse the empty string!
      return "";
    }
    StringBuilder sb = new StringBuilder(from);
    JCreole jcreole = new JCreole();
    EnumSet<JCreolePrivilege> ps = jcreole.getPrivileges();
    ps.add(JCreolePrivilege.ABSLINK);
    jcreole.setPrivileges(ps);
    String to = "";
    try {
      //CreoleScanner scanner = CreoleScanner.newCreoleScanner(sb, true);
      //to = parser.parse(scanner).toString();
      to = jcreole.parseCreole(sb);
    } catch (Exception ex) {
      //System.out.println("This is the creole we got: >>>>\n"+sb+"\n<<<<");
      //System.out.println("This is the original string we got: >>>>\n"+from+"\n<<<<");
      throw new GateRuntimeException("Could not convert creole wiki format to html: "+from,ex);
    }
    return to;
  }

}
