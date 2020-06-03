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

package at.ofai.gate.appdoc;


import gate.*;
import gate.creole.*;
import gate.creole.metadata.*;
import java.awt.GridLayout;
import javax.swing.JPanel;

/** 
 * A visual resource that makes it easy to add or change the @comment, @author,
 * @version features used for generating the application documentation.
 * The actual generation is done with ApplicationDocController VR and its
 * associated ControllerPanel.
 *
 * @author Johann Petrak
 */
@CreoleResource(name = "AppDoc",
        guiType = GuiType.LARGE,
        resourceDisplayed = "gate.Executable",
        comment = "Edit the documentation for this executable resource")
public class AppDoc  extends AbstractVisualResource
  implements VisualResource {

  protected JPanel panel;
  protected Resource theTarget;

  @Override
  public void setTarget(Object target) {
    theTarget = (Resource)target;
    panel = new CommentPanel(this);
    this.add(panel);
    this.setLayout(new GridLayout(1,1));
  }

} // class AppDoc
