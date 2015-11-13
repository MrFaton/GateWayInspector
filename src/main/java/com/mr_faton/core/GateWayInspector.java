package com.mr_faton.core;

import com.mr_faton.core.util.SettingsManager;
import com.mr_faton.gui.UserNotifier;
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
    private boolean enable = true;
    private String currentGateway;
    private GatewayType currentGatewayType;



    public void start() {
        enable = true;
        try {
            SettingsManager.load();

            String primaryG = SettingsManager.getPrimaryGateway();
            String secondaryG = SettingsManager.getSecondaryGateway();
            String thirdG = SettingsManager.getThirdGateway();
            String fourthG = SettingsManager.getFourthGateway();

            while (enable) {
                if (checkGateway(primaryG)) {
                    currentGateway = primaryG;
                    currentGatewayType = GatewayType.PRIMARY;
                    logger.info("Gateway switched to " + currentGatewayType.toString() + " - " + currentGateway);
                } else if (checkGateway(secondaryG)) {
                    currentGateway = secondaryG;
                    currentGatewayType = GatewayType.SECONDARY;
                    logger.info("Gateway switched to " + currentGatewayType.toString() + " - " + currentGateway);
                } else if (checkGateway(thirdG)) {
                    currentGateway = thirdG;
                    currentGatewayType = GatewayType.THIRD;
                    logger.info("Gateway switched to " + currentGatewayType.toString() + " - " + currentGateway);
                } else if (checkGateway(fourthG)) {
                    currentGateway = fourthG;
                    currentGatewayType = GatewayType.FOURTH;
                    logger.info("Gateway switched to " + currentGatewayType.toString() + " - " + currentGateway);
                } else {
                    currentGateway = null;
                    currentGatewayType = GatewayType.NULL;
                    logger.info("All Gateways are down!");
                }
                if(Thread.interrupted()) {
                    enable = false;
                    break;
                }
                Thread.sleep(SettingsManager.getDelay());
            }
        } catch (InterruptedException interruptedEx) {
            enable = false;
        } catch (Exception ex) {
            UserNotifier.warning(ex.getMessage());
            enable = false;
        }
    }

    public String getStatus() {
        return "Состояние: " +(enable ? "работает":"остановлен") + "\n" +
                "Тип Gateway: " + currentGatewayType + "\n" +
                "Адрес Gateway: " + currentGateway;
    }

    private boolean checkGateway(String gateway) throws Exception {
        InetAddress inetAddress = InetAddress.getByName(gateway);
        return inetAddress.isReachable(SettingsManager.getDelay());
    }
}
