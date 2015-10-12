package no.nb.microservices.streaminginfo.config;

import no.nb.commons.web.sso.feign.SsoFeignInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Created by andreasb on 12.10.15.
 */
@Profile({"docker", "prod"})
@Configuration
@Import(SsoFeignInterceptor.class)
public class FeignInterceptorConfig {
}
