/*
 *  AppDoc.java
 *
 *  $Id: AppDoc.java 75 2011-02-04 15:23:36Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */

package at.ofai.gate.japedoc;

import gate.util.GateRuntimeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for properly detecting JapeDoc in a JAPE file.
 *
 * @author Johann Petrak
 */
public class JapeDocParser {
  // some precompiled regular expressions for parsing
  Pattern japeDocStartPattern1 = Pattern.compile("^\\s*/\\*\\*(FILE)?\\s*$");
  Pattern japeDocEndPattern1 = Pattern.compile("^\\s*\\*\\*?/\\s*$");
  Pattern japeDocInsidePattern1 = Pattern.compile("^\\s*\\* ?(.*)$");
  Pattern japeDocStartPattern2 = Pattern.compile("^\\s*//\\[(FILE)?\\s*$");
  Pattern japeDocEndPattern2 = Pattern.compile("^\\s*//\\]\\s*$");
  Pattern japeDocInsidePattern2 = Pattern.compile("^\\s*// ?(.*)$");
  Pattern japeDocTypePattern = Pattern.compile("^\\s*([a-zA-Z]+):\\s+([^\\s]+)\\s*$");
  Pattern japeDocEmptyLinePattern = Pattern.compile("^\\s*$");
  public Collection<Map<String,String>> parse(String pathname) {
    // if the pathname starts with "file:" it is really an URL, convert that
    // URL to a file
    File japeFile = null;
    japeFile = new File(pathname);
    FileInputStream fis;
    try {
      fis = new FileInputStream(japeFile);
    } catch (FileNotFoundException ex) {
      throw new GateRuntimeException("JapeDoc: JAPE file does not exist: "+pathname);
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String strLine;
    boolean readMore = true;
    Collection<Map<String,String>> result =
      new ArrayList<Map<String,String>>();
    boolean insideJapeDoc = false;  // indicates that we are inside a JapeDoc comment
    StringBuffer currentJapeDoc = null; // the current JapeDoc
    Map<String,String> currentEntry = null;
    boolean needType = false;  // indicates that after a jape doc, we still need the type
    int matchtype = 0;  // 0=no, 1=/** 2=//[
    int line = 0;
    while(readMore) {
      try {
        strLine = br.readLine();
      } catch (IOException ex) {
        System.err.println("Error reading JAPE file: "+pathname+" Reason: "+ex);
        strLine = null;
      }
      if(strLine == null) {
        readMore = false;
      } else {
        line++;
        if(japeDocEmptyLinePattern.matcher(strLine).matches()) {
          continue;
        }
        if(insideJapeDoc) {
          // process the jape doc and wait for closing comment line
          Matcher end;
          Matcher inside;
          if(matchtype == 1) {
            end = japeDocEndPattern1.matcher(strLine);
            inside = japeDocInsidePattern1.matcher(strLine);
          } else if(matchtype == 2) {
            end = japeDocEndPattern2.matcher(strLine);
            inside = japeDocInsidePattern2.matcher(strLine);
          } else {
            throw new GateRuntimeException("JapeDoc parsing error in file "+pathname+" line "+line);
          }
          if(end.matches()) {
            matchtype = 0;
            currentEntry.put("docstring", currentJapeDoc.toString());
            insideJapeDoc = false;
          } else if(inside.matches()) {
            String docLine = inside.group(1);
            currentJapeDoc.append(docLine);
          } else {
            throw new GateRuntimeException("JapeDoc format error in JAPE file "+pathname+": unclosed JavaDoc at line "+line);
          }
        } else {
          // wait for opening comment line
          Matcher start1 = japeDocStartPattern1.matcher(strLine);
          Matcher start2 = japeDocStartPattern2.matcher(strLine);
          String declaredType = null;
          if(start1.matches()) {
            matchtype = 1;
            needType = true;
            if(start1.group(1) != null) {
              declaredType = start1.group(1);
              needType = false;
            }
          } else if(start2.matches()) {
            matchtype = 2;
            needType = true;
            if(start2.group(1) != null) {
              declaredType = start2.group(1);
              needType = false;
            }
          }
          // if we got a match, create a new doc entry
          if(matchtype > 0) { // we got an opening comment line
            currentJapeDoc = new StringBuffer();
            currentEntry = new HashMap<String,String>();
            currentEntry.put("name", "");
            if(declaredType != null) {
              currentEntry.put("name", japeFile.getName());
              currentEntry.put("type", declaredType);
            } else {
              currentEntry.put("type", "");
            }
            currentEntry.put("format", "");
            currentEntry.put("docstring", "");
            result.add(currentEntry);
            insideJapeDoc = true;
            // if we have a FILE, set the type and set needType to false
          } else { // no start, but if we still need a type, try to find it
            if(needType) {
              Matcher type = japeDocTypePattern.matcher(strLine);
              if(type.matches()) {
                needType = false;
                currentEntry.put("type",type.group(1));
                currentEntry.put("name",type.group(2));
              } else {
                System.err.println("Could not find a type/name at line "+line);
                needType = false;
              }
            }
          }
        }
      }
    }
    try {
      br.close();
    } catch (IOException ex) {
      throw new GateRuntimeException("Error closing JAPE file: "+pathname,ex);
    }

    return result;
  }
}
