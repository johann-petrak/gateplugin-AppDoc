/*
 *  IsConvertible.java
 *
 *  $Id: ConvertString.java 58 2011-02-03 16:44:30Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */
package at.ofai.gate.appdoc.functions;

import at.ofai.gate.appdoc.converters.Converter;
import at.ofai.gate.appdoc.converters.ConverterFactory;
import at.ofai.gate.japedoc.JapeDocParser;
import freemarker.ext.beans.CollectionModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import gate.util.GateRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Johann Petrak
 */
public class JapeDoc implements TemplateMethodModel {
  // needs the following arguments (all strings)
  // - file path
  // - to format name

  public TemplateModel exec(List args) throws TemplateModelException {
    if (args.size() != 3) {
      throw new TemplateModelException("Need 3 args: japefilepath, fromformat, toformat, got "+args.size());
    }
    String filepath = (String) args.get(0);
    String fromFormat = (String) args.get(1);
    String toFormat = (String) args.get(2);

    Collection<Map<String,String>> listOfJapeDocs =
      new ArrayList<Map<String,String>>();

    //if the filepath is an empty string (which is the case if the calling
    // macro could not find a grammar URL), just return an empty list
    if(filepath.equals("")) {
      return new CollectionModel(listOfJapeDocs,new DefaultObjectWrapper());
    }

    JapeDocParser parser = new JapeDocParser();
    try {
      listOfJapeDocs = parser.parse(filepath);
    } catch(Exception ex) {
      throw new GateRuntimeException("Problem parsing JAPE File "+filepath,ex);
    }

    // convert the docstrings according to the required format conversion!
    Converter converter = ConverterFactory.getConverter(fromFormat, toFormat);
    for(Map<String,String> entry : listOfJapeDocs) {
      entry.put("docstring",converter.convert(entry.get("docstring")));
    }

    return new CollectionModel(listOfJapeDocs,new DefaultObjectWrapper());
  }
}
