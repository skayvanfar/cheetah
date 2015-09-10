package gui;

import gui.listener.MainToolbarListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Saeed on 9/10/2015.
 */
public class StatusPanel extends JPanel implements ActionListener {

    // Add download text field.
 //   private JTextField addTextField;
 //   private JButton addButton;

 //   private MainToolbarListener mainToolbarListener;

    public StatusPanel() {

  //      setBorder(BorderFactory.createEtchedBorder());

  //      addButton = new JButton("New Download");
  //      addTextField = new JTextField(30);

  //      add(addButton);
 //       add(addTextField);
    }

  //  public void setToolbarListener(MainToolbarListener listener) {
 //       this.mainToolbarListener = mainToolbarListener;
 //   }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked= (JButton) e.getSource();

 //       if (clicked == addButton) {
 //           if (mainToolbarListener != null) {
 //               mainToolbarListener.newDownloadEventOccured();
 //           }
   //     } else if (clicked == refereshButton) {
   //         if (listener != null) {
  ///              listener.refereshEventOccured();
   //         }
        }
   //     actionAdd();

}
