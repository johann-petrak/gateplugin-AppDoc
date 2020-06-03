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

import at.ofai.gate.appdoc.converters.ConverterFactory;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.util.List;

/**
 *
 * @author Johann Petrak
 */
public class IsConvertible implements TemplateMethodModel {
  // needs the following arguments (all strings)
  // from format name
  // to format name

  public TemplateModel exec(List args) throws TemplateModelException {
    if (args.size() != 2) {
      throw new TemplateModelException("Need 2 args: fromformat, toformat");
    }
    String fromFormat = (String) args.get(0);
    String toFormat = (String) args.get(1);
    boolean truefalse = ConverterFactory.isConvertible(fromFormat, toFormat);
    return new BooleanModel(truefalse,new DefaultObjectWrapper());
  }
}
