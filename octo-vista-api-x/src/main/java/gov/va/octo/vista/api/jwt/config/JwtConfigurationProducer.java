package gov.va.octo.vista.api.jwt.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.ws.rs.core.Configurable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read the <code>jwt.properties</code> file from the classpath and produce values that can be
 * injected with @{@link Configurable}.
 * <p>
 *
 * @author amccarty
 */
@ApplicationScoped
public class JwtConfigurationProducer {

    private static final Logger log = LoggerFactory.getLogger(JwtConfigurationProducer.class);
    private Properties properties;

    @PostConstruct
    public void init() {

        properties = new Properties();
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("jwt.properties");

        if (stream == null) {
            throw new RuntimeException("Cannot find jwt.properties configuration file.");
        }

        try {
            this.properties.load(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Configuration file cannot be loaded.");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                /* no op */
            }
        }
    }

    @Produces
    @JwtConfigurable
    public String produceString(InjectionPoint ip) {
        return getVal(getKey(ip));
    }

    @Produces
    @JwtConfigurable
    public Integer produceInteger(InjectionPoint ip) {
        String x = getVal(getKey(ip));
        if (x == null || !StringUtils.isNumeric(x)) {
            String err = "value: '" + x + "' is not a number -- defaulting to 0";
            log.error(err);
            throw new RuntimeException(err);
        }
        return Integer.valueOf(x);
    }

    @Produces
    @JwtConfigurable
    public Long produceLong(InjectionPoint ip) {
        String x = getVal(getKey(ip));
        if (x == null || !StringUtils.isNumeric(x)) {
            String err = "value: '" + x + "' is not a number -- defaulting to 0";
            log.error(err);
            throw new RuntimeException(err);
        }
        return Long.valueOf(x);
    }

    @Produces
    @JwtConfigurable
    public Boolean produceBoolean(InjectionPoint ip) {
        return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
    }

    private String getVal(String key) {
        if (!properties.containsKey(key)) {
            String err = "key: " + key + "  not found in jwt.properties";
            log.error(err);
            throw new RuntimeException(err);
        }
        return properties.getProperty(key);
    }

    private String getKey(InjectionPoint ip) {
        String key = ip.getAnnotated().getAnnotation(JwtConfigurable.class).value();
        return key;
    }
}
