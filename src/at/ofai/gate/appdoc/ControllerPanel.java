/*
 *  ControllerPanel.java
 *
 *  $Id: ControllerPanel.java 146 2013-05-03 15:42:01Z johann.petrak $
 *
 *  Copyright Austrian Research Institute for Artificial Intelligence (OFAI)
 *  http://www.ofai.at
 *
 *  Licensed under the GNU General Public License Version 2
 *
 */

package at.ofai.gate.appdoc;

import gate.Controller;
import gate.CorpusController;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.ResourceData;
import gate.gui.MainFrame;
import gate.swing.XJFileChooser;
import gate.util.Err;
import gate.util.ExtensionFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// TODO: save/restore the comment format setting in feature %@commentFormat
// TODO: factor out the code to actually generate the doc into a separate
// class which also has a main method so it can be used from the command line.
// TODO: factor all methods that define methods to be used in templates and
// add the more generic or useful methods currently defined in the template

/**
 * The panel that shows the buttons to set the template directory, output
 * directory and to generate the documentation and any additional controls
 * to change the configuration (at the moment, to select the format of
 * the comment).
 * 
 * @author Johann Petrak
 */
public class ControllerPanel extends JPanel {

    /** Creates new form ControllerPanel */
    AppDocGen owner;
    Resource theTarget;
    public ControllerPanel(AppDocGen theOwner) {
        owner = theOwner;
        theTarget = theOwner.theTarget;
        initComponents();
        jLabelOutputDirPath.setText(theOwner.getOutputDirName());
        jLabelTemplateDirPath.setText(theOwner.getTemplateDirName());
        // activate the buttons:
        jButtonSave.addActionListener(new SaveActionListener());
        jButtonOutputDir.addActionListener(new OutputDirActionListener());
        jButtonTemplateDir.addActionListener(new TemplateDirActionListener());
        jComboBox1.addActionListener(new CommentFormatActionListener());
        // map the text shown in the combo box to a short name
        formatMappings = new HashMap<String,String>();
        formatMappings.put("Plain text", "plain");
        formatMappings.put("plain", "Plain text");
        formatMappings.put("HTML", "html");
        formatMappings.put("html", "HTML");
        formatMappings.put("Creole Wiki Markup", "creole");
        formatMappings.put("creole","Creole Wiki Markup");
        setFormat(theOwner.getCommentFormat());
    }


  void saveControllerAndGenerateDoc() {
      final XJFileChooser fileChooser = MainFrame.getFileChooser();
      ExtensionFileFilter filter = new ExtensionFileFilter(
        "GATE Application files", "gapp");
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setDialogTitle("Select a file where to save the application "
        + ((theTarget instanceof CorpusController
           && ((CorpusController)theTarget).getCorpus() != null) ?
        "WITH" : "WITHOUT") + " corpus.");
      fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      fileChooser.setResource("application." + theTarget.getName());

      if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        final File gappFile = fileChooser.getSelectedFile();
        final JPanel parentPanel = this;
        Runnable runnable = new Runnable() {
          public void run() {
            try {
              // Relativize the directory paths with relation to the gappFile path
              // before storing
              owner.setTemplateDirName(owner.getTemplateDirName(), gappFile.getAbsolutePath());
              owner.setOutputDirName(owner.getOutputDirName(), gappFile.getAbsolutePath());

              Iterator it = ((Controller)theTarget).getPRs().iterator();
              while(it.hasNext()) {
                ProcessingResource pr = (ProcessingResource)it.next();
                String resourceClassName = pr.getClass().getName();
                ResourceData rd = Gate.getCreoleRegister().get(resourceClassName);
                String helpURL = rd.getHelpURL();
                if(helpURL != null) {
                  if(!helpURL.startsWith("http://") && !helpURL.startsWith("file:")) {
                    helpURL = "http://gate.ac.uk/userguide/" + helpURL;
                  }
                  pr.getFeatures().put("%@helpURL", helpURL);
                }
              }
              Map<String, String> locations = fileChooser.getLocations();
              // When saving an application state, use paths relative to
              // GATE HOME for resources inside GATE HOME and warn about them.
              gate.util.persistence.PersistenceManager
                .saveObjectToFile(theTarget, gappFile, true, true);
              // save also the location of the application as last application
              locations.put("lastapplication", gappFile.getCanonicalPath());
              // add this application to the list of recent applications
              String list = locations.get("applications");
              if (list == null) { list = ""; }
              list = list.replaceFirst("\\Q"+theTarget.getName()+"\\E(;|$)", "");
              list = theTarget.getName() + ";" + list;
              locations.put("applications", list);
              fileChooser.setLocations(locations);
              // after the pipeline has been saved, generate the documentation
              DocGenerator.generateDoc(gappFile, owner.getTemplateDirName(), owner.getOutputDirName());
            }
            catch(Exception e) {
              JOptionPane.showMessageDialog(parentPanel, "Error!\n"
                + e.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
              e.printStackTrace(Err.getPrintWriter());
            }
          }
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
      }
    }

  private static HashMap<String,String> formatMappings;

  public String getFormat() {
    return formatMappings.get((String)jComboBox1.getSelectedItem());
  }

  public void setFormat(String item) {
    String mapped = formatMappings.get(item);
    if(mapped == null) {
      mapped = formatMappings.get("plain");
    }
    jComboBox1.setSelectedItem(mapped);
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jButton1 = new javax.swing.JButton();
    jButtonTemplateDir = new javax.swing.JButton();
    jLabelTemplateDirText = new javax.swing.JLabel();
    jLabelTemplateDirPath = new javax.swing.JLabel();
    jButtonOutputDir = new javax.swing.JButton();
    jLabelOutputDirText = new javax.swing.JLabel();
    jLabelOutputDirPath = new javax.swing.JLabel();
    jButtonSave = new javax.swing.JButton();
    jLabelSave = new javax.swing.JLabel();
    jLabelSpacer = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox();

    jButton1.setText("jButton1");

    jButtonTemplateDir.setText("Change");

    jLabelTemplateDirText.setText("Template Directory:");

    jButtonOutputDir.setText("Change");

    jLabelOutputDirText.setText("Output Directory:");

    jButtonSave.setText("Save");

    jLabelSave.setText("and generate the documentation");

    jLabel1.setText("Comment format:");

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Plain text", "HTML", "Creole Wiki Markup" }));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabelSpacer, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButtonTemplateDir)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabelTemplateDirPath, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
              .addComponent(jLabelTemplateDirText)))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButtonOutputDir)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabelOutputDirPath, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
              .addComponent(jLabelOutputDirText)))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButtonSave)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabelSave))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonOutputDir, jButtonSave, jButtonTemplateDir});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonTemplateDir)
          .addComponent(jLabelTemplateDirText))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabelTemplateDirPath)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonOutputDir)
          .addComponent(jLabelOutputDirText))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabelOutputDirPath)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonSave)
          .addComponent(jLabelSave))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabelSpacer)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(75, Short.MAX_VALUE))
    );

    layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabelTemplateDirPath, jLabelTemplateDirText});

    layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabelOutputDirPath, jLabelOutputDirText});

    layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabelSave, jLabelSpacer});

  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButtonOutputDir;
  private javax.swing.JButton jButtonSave;
  private javax.swing.JButton jButtonTemplateDir;
  private javax.swing.JComboBox jComboBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabelOutputDirPath;
  private javax.swing.JLabel jLabelOutputDirText;
  private javax.swing.JLabel jLabelSave;
  private javax.swing.JLabel jLabelSpacer;
  private javax.swing.JLabel jLabelTemplateDirPath;
  private javax.swing.JLabel jLabelTemplateDirText;
  // End of variables declaration//GEN-END:variables

  protected class CommentFormatActionListener implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      owner.theTarget.getFeatures().put("%@commentFormat", getFormat()); 
    }
  }



  protected class SaveActionListener implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      //System.out.println("Save button pressed");
      // initiate saving the gapp. If not cancelled, after the gap
      // was saved, get the full path of the gapp file and use that
      // to config the documentation generation.
      saveControllerAndGenerateDoc();
    }
  }

  protected class TemplateDirActionListener implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      //System.out.println("Template button pressed");
      // show the filepicker: if a new dir is picked, use
      // owner.setTemplateDirName to update it, also update the
      // dir shown
      // first get canonical name
      final XJFileChooser fileChooser = MainFrame.getFileChooser();
      String knownLocation = fileChooser.getLocationForResource("application.templatedir." + owner.theTarget.getName());
      if(knownLocation == null) {
        // TODO: can this property be null or blank?
        fileChooser.setCurrentDirectory(new File(owner.getTemplateDirName()));
      }
      fileChooser.setDialogTitle("Chose the template directory");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setResource("application.templatedir." + owner.theTarget.getName());

      if(fileChooser.showDialog(owner,"Choose Directory") == JFileChooser.APPROVE_OPTION) {
        try {
          final File file = fileChooser.getSelectedFile();
          String templateDir = file.getCanonicalPath();
          owner.setTemplateDirName(templateDir,null);
          jLabelTemplateDirPath.setText(templateDir);
        } catch (IOException ex) {
          System.err.println("Could not set template directory: "+ex);
        }
      }
    }
  }

  protected class OutputDirActionListener implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      //System.out.println("OutputDir button pressed");
      // show the filepicker: if new dir is picked, use owner.setOutputDir
      // to save it, also change the dir shown
      // First get canonical name
      final XJFileChooser fileChooser = MainFrame.getFileChooser();
      String knownLocation = fileChooser.getLocationForResource("application.outputdir." + owner.theTarget.getName());
      if(knownLocation == null) {
        // TODO: can this property be null or blank?
        fileChooser.setCurrentDirectory(new File(owner.getOutputDirName()));
      }
      fileChooser.setDialogTitle("Chose the output directory");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setResource("application.outputdir." + owner.theTarget.getName());

      if(fileChooser.showDialog(owner,"Choose Directory") == JFileChooser.APPROVE_OPTION) {
        try {
          final File file = fileChooser.getSelectedFile();
          String outputDir = file.getCanonicalPath();
          owner.setOutputDirName(outputDir,null);
          jLabelOutputDirPath.setText(outputDir);
        } catch (IOException ex) {
          System.err.println("Could not set output directory: "+ex);
        }
      }
    }
  }

}
