package com.mr_faton.core.util;

import com.mr_faton.core.GatewayType;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.gui.UserNotifier;

import java.io.*;
import java.util.Properties;

/**
 * Description
 *
 * @author root
 * @since 11.11.2015
 */
public class SettingsManager {
    private static final String PATH = System.getProperty("user.dir") + "\\settings.properties";
    private static final String DELAY = "DELAY";
    private static final String PRIMARY_GATEWAY = GatewayType.PRIMARY.toString();
    private static final String SECONDARY_GATEWAY = GatewayType.SECONDARY.toString();
    private static final String THIRD_GATEWAY = GatewayType.THIRD.toString();
    private static final String FOURTH_GATEWAY = GatewayType.FOURTH.toString();
    private static final Properties SETTINGS_MANAGER = new Properties();

    public static void load() throws IOException {
        File settingsFile = new File(PATH);
        if (!settingsFile.exists()) {
            SETTINGS_MANAGER.put(DELAY, 15_000);
            SETTINGS_MANAGER.put(PRIMARY_GATEWAY, "192.168.101.1");
            SETTINGS_MANAGER.put(SECONDARY_GATEWAY, "192.168.101.2");
            SETTINGS_MANAGER.put(THIRD_GATEWAY, "192.168.101.3");
            SETTINGS_MANAGER.put(FOURTH_GATEWAY, "192.168.101.4");

            SETTINGS_MANAGER.store(new FileOutputStream(settingsFile),
                    "Всего доступно 4 GATEWAY сервера. Время задержки задаётся в миллисекундах 1сек = 1000 миллисек");

            throw new FileNotFoundException(
                    "Похоже что файл с настройками не найден.<br/>" +
                    "По адресу '" + PATH + "' будт создан стандартный файл с настройкам.<br/>" +
                    "Пожалуйста, обновите настройки в этом файле.");
        }
        SETTINGS_MANAGER.load(new FileInputStream(settingsFile));
    }

    public static int getDelay() throws NoSuchEntityException {
        String delayStr = SETTINGS_MANAGER.getProperty(DELAY);
        if (delayStr == null)
            throw new NoSuchEntityException("Параметр '" + DELAY + "' не существует в фале настроек или не указан");
        return Integer.valueOf(delayStr);
    }

    public static String getPrimaryGateway() throws NoSuchEntityException {
        String gateway = SETTINGS_MANAGER.getProperty(PRIMARY_GATEWAY);
        if (gateway == null)
            throw new NoSuchEntityException(
                    "Параметр '" + PRIMARY_GATEWAY  + " не существует в фале настроек или не указан");
        return gateway;
    }
    public static String getSecondaryGateway() {
        return SETTINGS_MANAGER.getProperty(SECONDARY_GATEWAY);
    }
    public static String getThirdGateway() {
        return SETTINGS_MANAGER.getProperty(THIRD_GATEWAY);
    }
    public static String getFourthGateway() {
        return SETTINGS_MANAGER.getProperty(FOURTH_GATEWAY);
    }
}
