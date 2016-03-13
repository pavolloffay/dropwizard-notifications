package sk.loffay.wandera;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author Pavol Loffay
 */
public class TemplateHealthCheck extends HealthCheck {
    private final String environment;


    public TemplateHealthCheck(String environment) {
        this.environment = environment;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(environment, "DEV");
        if (!saying.contains("DEV")) {
            return Result.unhealthy("template doesn't include a environment");
        }
        return Result.healthy();
    }
}
