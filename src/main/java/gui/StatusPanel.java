package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by Saeed on 9/10/2015.
 */
class StatusPanel extends JPanel implements ActionListener {

    private final ResourceBundle defaultPreferencesBundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    // Add download text field.
    private JLabel cheetahLabel;
 //   private JButton addButton;

 //   private MainToolbarListener mainToolbarListener;

    public StatusPanel() {

  //      setBorder(BorderFactory.createEtchedBorder());

   //     setLayout(new FlowLayout(FlowLayout.RIGHT));

        cheetahLabel = new JLabel(defaultPreferencesBundle.getString("aboutPanel.program.name"));
        cheetahLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
  //      addTextField = new JTextField(30);

        add(cheetahLabel);
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
