package com.mr_faton.core;

import com.mr_faton.core.util.SettingsManager;
import com.mr_faton.gui.Tray;
import org.apache.log4j.Logger;

import java.net.InetAddress;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 03.11.2015
 */
public class GateWayInspector {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.GateWayInspector");
    private Tray tray;
    private boolean enable = false;
    private String currentGateway = "";
    private String currentGatewayName = "";
    private int delay;


    public void start() {
        logger.info("App is start");
        enable = true;
        try {
            SettingsManager.load();
            delay = Integer.valueOf(SettingsManager.getSetup(SettingsManager.DELAY));
            while (enable) {
                for (int counter = 1; true; counter++) {
                    String selectedGateway = SettingsManager.getSetup(SettingsManager.GATEWAY_NAME_PATTERN + counter);
                    if (selectedGateway == null) break;
                    if (checkGateway(selectedGateway)) {
                        if (currentGateway.equals(selectedGateway)) {
                            break;
                        } else {
                            changeGateway(selectedGateway, counter);
                            break;
                        }
                    }
                }
                Thread.sleep(delay);
            }
        } catch (InterruptedException interruptedEx) {
            enable = false;
            logger.info("App is stop");
        } catch (Exception ex) {
            enable = false;
            logger.error(ex);
            tray.showErrorMessage("Ошибка!", "Возникла ошибка в работе приложения: \n" + ex.getMessage());
        }
    }


    public boolean getState() {
        return enable;
    }

    public String getStatus() {
        return "Состояние: " + (enable ? "работает" : "остановлен") + "\n" +
                "Текущий сервер: " + currentGatewayName + " (" + currentGateway + ")";
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }


    private boolean checkGateway(String gateway) throws Exception {
        InetAddress inetAddress = InetAddress.getByName(gateway);
        return inetAddress.isReachable(delay);
    }

    private void changeGateway(String gatewayAddress, int gatewayNumber) throws Exception {
        currentGateway = gatewayAddress;
        currentGatewayName = "Сервер_" + gatewayNumber;

        String[] commands = {
                "cmd.exe",
                "/c",
                "route delete 0.0.0.0 mask 0.0.0.0 && route add 0.0.0.0 mask 0.0.0.0 " + gatewayAddress};
        Process process = Runtime.getRuntime().exec(commands);
        process.waitFor();

        logger.info("Serer changed to " + currentGatewayName + " (" + currentGateway + ")");
        tray.showInfoMessage("Изменился сервер!", "Произошло переключение на:\n" +
                currentGatewayName + " (" + currentGateway + ")");
    }
}
