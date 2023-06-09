package hulio13.configuration;

import hulio13.configuration.json.JsonConfigurationFileReader;
import hulio13.configuration.reflections.AnnotationsConfiguratorService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class ConfigurationService {
    private static final Logger logger =
            LoggerFactory.getLogger(ConfigurationService.class);

    public static void configureWithAnnotations(String configPackage, String configFileName) {
        ConfigurationMap map;
        try {
            map = JsonConfigurationFileReader.readConfigurationFile(configFileName);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }

        addConfigurators(AnnotationsConfiguratorService.
                getConfiguredConfigurators(configPackage, map));
    }

    private static void addConfigurators(Object[] objects) {
        for (var obj : objects) {
            var clazz = obj.getClass();
            ConfiguratorRegistry.register(clazz, clazz.cast(obj));
        }
    }
}
