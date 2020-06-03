/*
 *  DocGenerator.java
 *
 *  $Id: DocGenerator.java 157 2013-05-10 14:11:21Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */
package at.ofai.gate.appdoc;

import fmpp.ProcessingException;
import fmpp.progresslisteners.ConsoleProgressListener;
import fmpp.setting.SettingException;
import fmpp.setting.Settings;
import gate.Gate;
import gate.util.GateException;
import gate.util.GateRuntimeException;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Johann Petrak
 */
public class DocGenerator {

  public static void generateDoc(
    File gappFile,
    String templateDirName,
    String outputDirName)
    throws SettingException, ProcessingException {
    String fn = "";
    if(!Gate.isInitialised()) {
      throw new GateRuntimeException("GATE not initialized when generateDoc is called!");
    }
    fn = gappFile.getAbsolutePath();
    String gappFileName = fn;
    String gatehomeTmp;
    gatehomeTmp = Gate.getGateHome().getAbsolutePath();
    final String gatehome = gatehomeTmp;
    String relpathTmp;
    relpathTmp = gappFile.getParentFile().getAbsolutePath();
    final String relpath = relpathTmp;
    File templateDir = new File(templateDirName);
    System.out.println("AppDoc: using template directory " + templateDir);
    Settings settings = new fmpp.setting.Settings(templateDir);
    settings.define("outFileExtension", Settings.TYPE_STRING, false, true);
    settings.load(new File(templateDir, "config.fmpp"));
    // the directory that will contain all output files
    System.out.println("AppDoc: using output directory " + outputDirName);
    settings.setWithString("outputRoot", outputDirName);
    String extension = (String) settings.get("outFileExtension");
    if (extension == null) {  // if somebody
      throw new GateRuntimeException("Parameter outfileExtension not set in the config file!");
    }
    String docFileName = gappFile.getName() + ".doc" + settings.get("outFileExtension");
    System.out.println("AppDoc: generating documentation root file: " + docFileName);

    String gappFileNameSlashes = gappFileName;
    String docFileNameSlashes = docFileName;
    String gatehomeSlashes = gatehome;
    String relpathSlashes = relpath;
    // TODO: this is rather a hack. We should figure out how to construct
    // the settings datastructure directly instead of supplying a string
    // where we have to struggle to get the file path names right.
    if(File.separator.equals("\\")) {
      gappFileNameSlashes = gappFileName.replaceAll("\\\\", "/");
      docFileNameSlashes = docFileName.replaceAll("\\\\","/");
      gatehomeSlashes = gatehome.replaceAll("\\\\", "/");
      relpathSlashes = relpath.replaceAll("\\\\", "/");
    }
    String settingsString = 
      "{doc: xml(\"" + gappFileNameSlashes + "\")\n"
      + "docFile: \"" + docFileNameSlashes + "\"\n"
      + "gatehome: \"" + gatehomeSlashes + "\"\n"
      + "relpath: \"" + relpathSlashes + "\"\n"
      + "}";
    //System.out.println("Settings string is: "+settingsString);
    settings.setWithString("data",settingsString);
    settings.addProgressListener(new ConsoleProgressListener());
    settings.execute();
  } // generateDoc

  public static void main(String[] args) {
    Options options = new Options();
    options.addOption("g","gappfile",true,"The gapp file to document");
    options.addOption("t","templatedir",true,"Template directory name");
    options.addOption("o","outputdir",true,"Output directory name (default: current)");
    options.addOption("f","commentformat",true,"Comment format: plain, creole, or html(default)");
    options.addOption("h","help",false,"Show detailed usage help");
    
    CommandLineParser parser = new PosixParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      System.err.println("Error parsing the command line arguments: "+ex);
      System.exit(1);
    }

    if(cmd.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "appDoc", options );
      System.exit(0);
    }

    String gappFileName = cmd.getOptionValue("g");
    if(gappFileName == null) {
      System.err.println("Error: the g/gappfile option must be supplied");
      System.exit(1);
    }

    String templateDirName = cmd.getOptionValue("t");
    if(templateDirName == null) {
      System.err.println("Error: the t/templatefile option must be supplied");
      System.exit(1);
    }

    String outputDirName = cmd.getOptionValue("o");
    if(outputDirName == null) {
      System.err.println("Output directory set to current directory");
      outputDirName = ".";
    }

    String commentFormat = cmd.getOptionValue("f","html");
    if(!commentFormat.equals("plain") && !commentFormat.equals("html") && !commentFormat.equals("creole")) {
      System.err.println("Comment format must be 'plain', 'html', or 'creole'");
      System.exit(1);
    }

    if(commentFormat.equals("plain")) {
      commentFormat = "plain text";
    }

    try {
      Gate.init();
    } catch (GateException ex) {
      System.err.println("Error initializing GATE: "+ex);
      ex.printStackTrace(System.err);
      System.exit(1);
    }

    File gappFile = null;
    gappFile = new File(gappFileName).getAbsoluteFile();
    templateDirName = new File(templateDirName).getAbsolutePath();
    outputDirName = new File(outputDirName).getAbsolutePath();
    //System.out.println("Trying to generate for "+gappFile+","+templateDirName+","+outputDirName);
    try {
      generateDoc(gappFile, templateDirName, outputDirName);
    } catch (Exception ex) {
      System.err.println("Error generating the documentation: "+ex);
      ex.printStackTrace(System.err);
      System.exit(1);
    }    
  } // main
}
