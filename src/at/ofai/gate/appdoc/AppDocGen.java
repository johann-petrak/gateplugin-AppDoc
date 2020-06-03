/*
 *  AppDocGen.java
 *
 *  $Id: AppDocGen.java 129 2011-05-11 14:11:24Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */
package at.ofai.gate.appdoc;

import gate.*;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.swing.XJFileChooser;
import gate.util.Files;
import gate.util.NameBearer;
import gate.util.persistence.PersistenceManager;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

// TODO: support nested controllers in the documentation!
// For starters, do not recurse but just treat them as any other ... user has
// to generate the doc for them seperately.

// TODO: add the rough scaffolds for processing JAPE sources and obtaining
// a hash of JAPE documentation for each JAPE gappFile referenced in the pipeline.
// The JAPE sources will have some special comments e.g. "//@[ .. //@]"
// as a comment for whatever follows -- for now just phase statements are
// supported, all other statements will have their comments ignored?
// After processing each JAPE gappFile, we put the comments into a hash or array
// and then provide that hash/array to the generator in a setting.
// The template is then free to fetch and show the source comments as part
// of the JAPE PR documentation (need to find a simple way how to identify
// a PR as being a JAPE, may be easier to do here and pass on a feature setting
// for the PR, e.g. %@japefile
// NOTE!! The creation of the hash should be possible from within the template
// by a user method -- this should work by passing on the path of the jape
// to the macro and getting back or setting the hash or similar.
// That way, the whole thing will also work in command-line mode!

// TODO: place the actual values for $gatehome und $relpath in the hash so that
// the generator can resolve those in the URL paths.

// TODO: make command line mode work

// Infos:
// http://fmpp.sourceforge.net/qtour.html
// http://freemarker.sourceforge.net/docs/index.html
// http://fmpp.sourceforge.net/settings.html
// http://fmpp.sourceforge.net/pphash.html
// http://fmpp.sourceforge.net/dataloader.html
// FMPP Java API: http://fmpp.sourceforge.net/api/index.html
// Freemarker API: http://freemarker.sourceforge.net/docs/api/index.html
// creating own directives, methods:
//   http://fmpp.sourceforge.net/freemarker/pgui_datamodel.html


/** 
 * A visual resource for creating the application documentation and for
 * configuring the generation process.
 *
 * @author Johann Petrak
 */
@CreoleResource(name = "AppDocGen",
        guiType = GuiType.LARGE,
        resourceDisplayed = "gate.Controller",
        comment = "Configure the documentation generation and generate documentation")
public class AppDocGen  extends AbstractVisualResource
  implements VisualResource {

  protected JPanel panel;
  protected Resource theTarget;

  @Override
  public void setTarget(Object target) {
    theTarget = (Resource)target;
     panel = new ControllerPanel(this);
    this.add(panel);
    this.setLayout(new GridLayout(1,1));
  }

  // TODO: replace any $relpath$ or $gatehome$ in a path stored in the
  // feature with the actual paths
  // PROBLEM: it seems this cannot be done -- no way to figure out the
  // path from which the application was loaded at this point
  String getTemplateDirName() {
    // returns the template directory: either as currently stored in the
    // feature map or the default template from the plugin path
    FeatureMap fm = theTarget.getFeatures();
    String templateDirName = (String)fm.get("%@templatedir");
    if(templateDirName == null) {
      ResourceData myResourceData =
          Gate.getCreoleRegister().get(this.getClass().getName());
      java.net.URL creoleXml = myResourceData.getXmlFileUrl();
      try {
        File pluginDir = new File(creoleXml.toURI()).getParentFile();
        pluginDir = new File(pluginDir,"templates");
        pluginDir = new File(pluginDir,"html_default");
        //templateDirName = pluginDir.getCanonicalPath();
        templateDirName = pluginDir.getAbsolutePath();
      //} catch (IOException ex) {
      //  templateDirName = "";
      } catch (URISyntaxException ex) {
        templateDirName = "";
      }
      if(templateDirName.equals("")) {
        //try {
          String defDir = System.getProperty("gate.user.filechooser.defaultdir");
          if(defDir == null || defDir.equals("")) {
            defDir = ".";
          }
          //templateDirName = new File(defDir).getCanonicalPath();
          templateDirName = new File(defDir).getAbsolutePath();
        //} catch (IOException ex) {
        //  templateDirName = "";
        //}
      }
    } else {
      templateDirName = makeAbsolutePath(templateDirName);
    }
    return templateDirName;
  }

  String getCommentFormat() {
    FeatureMap fm = theTarget.getFeatures();
    String format = (String)fm.get("%@commentFormat");
    if(format == null) {
      format = "Plain text";
    }
    return format;
  }

  // TODO: replace any $relpath$ or $gatehome$ in the stored path name
  // PROBLEM: it seems this cannot be done -- no way to figure out the
  // path from which the application was loaded at this point
  String getOutputDirName() {
    FeatureMap fm = theTarget.getFeatures();
    String outputDirName = (String)fm.get("%@outputdir");
    if(outputDirName == null) {
      // default for this is simply the current directory
      //try {
        String defDir = System.getProperty("gate.user.filechooser.defaultdir");
        if(defDir == null || defDir.equals("")) {
          defDir = ".";
        }
        //outputDirName = new File(defDir).getCanonicalPath();
        outputDirName = new File(defDir).getAbsolutePath();
      //} catch(IOException ex) {
      //  outputDirName = "";
      //}
    } else {
      outputDirName = makeAbsolutePath(outputDirName);
    }
    return outputDirName;
  }

  // if called with gappFilePathName != null, make the path
  // relative to the gappFilePathName
  void setTemplateDirName(String name, String gappFilePathName) {
    String forName = name;
    if(gappFilePathName != null) {
      forName = makeRelativePath(name,gappFilePathName);
    }
    theTarget.getFeatures().put("%@templatedir", forName);
  }

  // if called with gappFilePathName != null, make the path
  // relative to the gappFilePathName
  void setOutputDirName(String name, String gappFilePathName) {
    String forName = name;
    if(gappFilePathName != null) {
      forName = makeRelativePath(name,gappFilePathName);
    }
    theTarget.getFeatures().put("%@outputdir", forName);
  }

  protected String makeRelativePath(String dirName, String gappName) {
    String relPath = dirName;
    try {
      String gatePath = Gate.getGateHome().getCanonicalPath();
      String dirPath = new File(dirName).getCanonicalPath();
      URL dirURL = new File(dirName).getCanonicalFile().toURI().toURL();
      URL gappURL = new File(gappName).getCanonicalFile().toURI().toURL();
      URL gatehomeURL = Gate.getGateHome().getCanonicalFile().toURI().toURL();
      if(dirPath.startsWith(gatePath)) {
        relPath = "$gatehome$" + PersistenceManager.getRelativePath(gatehomeURL, dirURL);
      } else {
        relPath = "$relpath$" + PersistenceManager.getRelativePath(gappURL, dirURL);
      }
    } catch (IOException ex) {
      System.out.println("Cannot get canonical path, not relativizing: "+ex);
    }
    return relPath;
  }

  protected String makeAbsolutePath(String path) {
    String retPath = path;
    if(path.startsWith("$gatehome$")) {
      String gateHomePath = "";
      try {
        gateHomePath = Gate.getGateHome().getCanonicalPath();
      } catch (IOException ex) {
        gateHomePath = Gate.getGateHome().getAbsolutePath();
      }
      // to make this work when moving  between operating systems, go over
      // the URL
      try {
        URL gateHomeURL = new File(gateHomePath).toURI().toURL();
        URL finalURL = new URL(gateHomeURL,path.substring("$gatehome$".length()));
        retPath = Files.fileFromURL(finalURL).getAbsolutePath();
      } catch (MalformedURLException ex) {
        // fallback: replace using the file separator
        retPath = path.replaceFirst("\\$gatehome\\$", gateHomePath+File.separator);
      }
    } else if(path.startsWith("$relpath$")) {
      String relPath = getGappLoadPath();
      try {
        URL gateHomeURL = new File(relPath).toURI().toURL();
        URL finalURL = new URL(gateHomeURL,path.substring("$relpath$".length()));
        retPath = Files.fileFromURL(finalURL).getAbsolutePath();
      } catch (MalformedURLException ex) {
        retPath = path.replaceFirst("\\$relpath\\$", relPath);
      }
    }
    return retPath;
  }

  protected String getGappLoadPath() {
    NameBearer target = theTarget;
    Map<String,String> locations =
      Gate.getUserConfig().getMap(XJFileChooser.class.getName());
    String gappFileName = locations.get("application."+target.getName());
    String gappFilePath = "";
    if(gappFileName != null) {
      try {
        gappFilePath = new File(gappFileName).getCanonicalPath();
      } catch (IOException ex) {
        gappFilePath = new File(gappFileName).getAbsolutePath();
      }
    } else {
      try {
        gappFilePath = new File(".").getCanonicalPath();
      } catch (IOException ex) {
        gappFilePath = new File(".").getAbsolutePath();
      }
      System.err.println("AppDoc warning: Could not determine gapp file path for resolving $relpath$, using current dir "+gappFilePath);
    }
    return gappFilePath;
  }

} // class ApplicationDoc
