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

    private JLabel cheetahLabel;
    private JLabel selectedDownloadNameLabel;
 //   private JLabel sizeLabel;

    public StatusPanel() {

  //      setBorder(BorderFactory.createEtchedBorder());

        setLayout(new BorderLayout());

        cheetahLabel = new JLabel(defaultPreferencesBundle.getString("aboutPanel.program.name"));
        cheetahLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        selectedDownloadNameLabel = new JLabel();
      //  sizeLabel = new JLabel("dd");

        add(cheetahLabel, BorderLayout.EAST);
        add(selectedDownloadNameLabel, BorderLayout.WEST);
   //     add(sizeLabel, BorderLayout.CENTER);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 25, 0, 25);
    }

    public void setStatus(String selectedDownloadName) {
        selectedDownloadNameLabel.setText(selectedDownloadName);
    }

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
