package gui;

import gui.listener.MainToolbarListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by Saeed on 9/10/2015.
 */
public class StatusPanel extends JPanel implements ActionListener {

    private ResourceBundle defaultPreferencesBundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    // Add download text field.
    private JLabel chitaLabel;
 //   private JButton addButton;

 //   private MainToolbarListener mainToolbarListener;

    public StatusPanel() {

  //      setBorder(BorderFactory.createEtchedBorder());

        chitaLabel = new JLabel(defaultPreferencesBundle.getString("aboutPanel.program.name"));
  //      addTextField = new JTextField(30);

        add(chitaLabel);
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
