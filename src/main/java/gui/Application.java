/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package gui;

import javax.swing.*;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/10/2015
 */
public class Application {

    // Set application title.
    private final static String NAME_OF_CUI = "Cheetah";

    public static void main( String[] args ) {
        // enable anti-aliased text:
        System.setProperty("awt.useSystemAAFontSettings","lcd"); // on or lcd
        System.setProperty("swing.aatext", "true");
   //     System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
     //   System.setProperty("swing.crossplatformlaf", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
               // System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                System.setProperty("swing.crossplatformlaf", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                break;
            }
        }

       /* for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }*/

     //   Utils.setUIFont(new javax.swing.plaf.FontUIResource(Utils.createFont("/fonts/b_koodak.ttf")));
        //   Utils.setUIFont(new javax.swing.plaf.FontUIResource("/fonts/b_yekan.ttf",Font.BOLD,12));

        //   Locale locale1 = new Locale("fa", "IR", "WIN");
//setLocale(locale1);
        //     Properties systemProperties = System.getProperties();
        //     systemProperties.setProperty("http.proxyHost", "localhost");
        //      systemProperties.setProperty("http.proxyPort", "45");
        //    try {
        //         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        //             System.out.println(info.getName()); // Metal, Nimbus, CDE/Motif, Windows, Windows Classic, GTK+
        //             if ("Windows".equals(info.getName())) {

        //          } else if ("Nimbus".equals(info.getName())) {
        //              UIManager.setLookAndFeel(info.getClassName());
        //          }

        //          if ("GTK+".equals(info.getName())) {
        //               UIManager.setLookAndFeel(info.getClassName());
        //         }

        //        if ("Nimbus".equals(info.getName())) {
        //             UIManager.setLookAndFeel(info.getClassName());
        //         }
        //         }

        // setTheme(String themeName, String licenseKey, String logoString)
        //        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Red", "LPG", "Cheetah");

        // select Look and Feel
        //     UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
        //        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");


        //     UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        //    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");


        //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        //    } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
        //        e.printStackTrace();
        //    }


        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                 new DownloadManagerGUI(NAME_OF_CUI);
            }
        });

    }
}
