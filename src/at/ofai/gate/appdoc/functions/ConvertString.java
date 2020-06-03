/*
 *  ConvertString.java
 *
 *  $Id: ConvertString.java 158 2013-05-10 14:16:37Z johann.petrak $
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
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.util.List;

/**
 *
 * @author Johann Petrak
 */
public class ConvertString implements TemplateMethodModel {
  // needs the following arguments (all strings)
  // the text to transform
  // the from markup format (currently: "plain")
  // the to markup format (currently: "html")

  public TemplateModel exec(List args) throws TemplateModelException {
    if (args.size() != 3) {
      throw new TemplateModelException("Need 3 args: text, fromformat, toformat");
    }
    String text = (String) args.get(0);
    String fromFormat = (String) args.get(1);
    String toFormat = (String) args.get(2);
    //System.out.println("ConvertString.exec called with "+fromFormat+"/"+toFormat+">>>"+text+"<<<");
    Converter converter = ConverterFactory.getConverter(fromFormat, toFormat);
    String converted = converter.convert(text);
    return new freemarker.template.SimpleScalar(converted);
  }
}
