package com.mr_faton.gui;

import com.mr_faton.core.GateWayInspector;
import com.mr_faton.core.util.SettingsManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Description
 *
 * @author root
 * @since 13.11.2015
 */
public class Tray {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.gui.Tray");
    private final String startStateItemLabel = "Старт";
    private final String stopStateItemLabel = "Стоп";
    private Thread executableThread;
    private TrayIcon trayIcon;
    private GateWayInspector gatewayInspector;
    private MenuItem stateItem;

    public void init() throws Exception{
        SettingsManager.load();

        if (!SystemTray.isSupported()) throw new Exception("Tray is not supported");
        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu popupMenu = new PopupMenu();

        stateItem = new MenuItem(stopStateItemLabel);
        stateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gatewayInspector.getState()) {
                    executableThread.interrupt();
                    stateItem.setLabel(startStateItemLabel);
                } else {
                    executableThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            gatewayInspector.start();
                        }
                    });
                    executableThread.start();
                    stateItem.setLabel(stopStateItemLabel);
                }
            }
        });

        MenuItem openFileSettingsItem = new MenuItem("Открыть файл с настройками");
        openFileSettingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProcessBuilder pb = new ProcessBuilder("Notepad.exe", SettingsManager.FILE_PATH);
                try {
                    pb.start();
                } catch (IOException ioEx) {
                    logger.error(ioEx);
                }
            }
        });



        MenuItem aboutItem = new MenuItem("О программе");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "<html><h1 style=\"text-align: center;\"><font color=\"#0000FF\">GatewayInspector by Mr_Faton</font></h1>\n" +
                        "<div align=\"center\"><font size=\"3\">(Контроль и переключение между Gateway серверами)</font></div>\n" +
                        "<hr>\n" +
                        "<ul>\n" +
                        "<li><font size=\"4\">Автор: Понарин Игорь Сергеевич</font></li>\n" +
                        "<li><font size=\"4\">Версия: 1.0</font></li>\n" +
                        "<li><font size=\"4\">Разработанно для АМСГ Харьков Аэропорт</font></li>\n" +
                        "</ul>\n" +
                        "<hr>\n" +
                        "<p><font size=\"3\">По всем вопросам обращайтесь по электронному адресу: <font color=\"#0000FF\">firefly90@inbox.ru</font></font></p>\n" +
                        "<p align=\"right\">&nbsp;</p>\n" +
                        "<p align=\"right\"><font size=\"2\">Copyright &copy; 15.11.2015 Понарин И.С.</font></p>\n</html>";
                JOptionPane.showMessageDialog(null, new JLabel(text), "Gateway Inspector by Mr_Faton",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        MenuItem helpItem = new MenuItem("О настройках");
        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "" +
                        "<html>" +
                        "<h2 align=\"center\"><font color=\"#0000FF\">Описание файла нстроек</font></h2>\n" +
                        "<ul>\n" +
                        "<li><font color=\"#0000FF\"><font color=\"#000000\">Параметр DELAY - задаётся в миллисекундах (1 сек = 1000 миллисекунд).<br>\n" +
                        "Этот параметр испльзуется для:<br>\n" +
                        "1) ожидания ответа от Gateway сервера, и если в течении этого времени сервер не ответил, он считается нерабочим.<br>\n" +
                        "2) если опрашиваемый сервер в порядке, от в таком случае это время используется для приостановления работы программы до следующей проверки.</font></font></li>\n" +
                        "<li><font color=\"#0000FF\"><font color=\"#000000\">Параметр GATEWAY_(номер) - эти параметры, их может быть любое количество, задают Gateway сервера, которые опрашивает программа и,<br>\n" +
                        "в случае выявления нерабочего сервера, переключается на тот рабочий сервер, который имеет наименьший порядковый номер.<br>\n" +
                        "Примеры: <font color=\"#0000FF\"><font color=\"#000000\">GATEWAY_1, <font color=\"#0000FF\"><font color=\"#000000\">GATEWAY_2, <font color=\"#0000FF\"><font color=\"#000000\">GATEWAY_3</font></font></font></font></font></font></font></font></li>\n" +
                        "</ul>\n" +
                        "</html>";
                JOptionPane.showMessageDialog(null, new JLabel(text), "Описание файла настроек",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });


        Menu menuHelp = new Menu("Помощь");
        menuHelp.add(helpItem);
        menuHelp.add(aboutItem);


        MenuItem exitItem = new MenuItem("Выход");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (executableThread != null) {
                    executableThread.interrupt();
                }
                System.exit(0);
            }
        });

        popupMenu.add(stateItem);
        popupMenu.add(openFileSettingsItem);
        popupMenu.add(menuHelp);
        popupMenu.add(exitItem);


        Image icon = new ImageIcon(getClass().getClassLoader().getResource("gateway_icon.png")).getImage();
        trayIcon = new TrayIcon(icon, "GatewayInspector", popupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfoMessage("Текущее состояние", gatewayInspector.getStatus());
            }
        });
        systemTray.add(trayIcon);

        executableThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gatewayInspector.start();
            }
        });
        executableThread.start();
    }

    public void setGatewayInspector(GateWayInspector gatewayInspector) {
        this.gatewayInspector = gatewayInspector;
    }

    public void showInfoMessage(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void showErrorMessage(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
    }
}
