package com.mr_faton;

import com.mr_faton.core.GateWayInspector;
import com.mr_faton.gui.Tray;
import org.apache.log4j.Logger;

/**
 * Description
 *
 * @author root
 * @since 15.11.2015
 */
public class StartApp {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.StartApp");
    public static void main(String[] args) {
        GateWayInspector gateWayInspector = new GateWayInspector();
        Tray tray = new Tray();

        gateWayInspector.setTray(tray);
        tray.setGatewayInspector(gateWayInspector);

        try {
            tray.init();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
