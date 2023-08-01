package ro.msg.learning.shop.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("ro.msg.learning.shop.security")
@EnableConfigurationProperties
@Getter
@Setter
public class SecurityProperties {
    private AuthenticationType authenticationType;

}
