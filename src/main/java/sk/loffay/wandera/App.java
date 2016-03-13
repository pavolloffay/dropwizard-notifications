package sk.loffay.wandera;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import sk.loffay.wandera.auth.SimpleAuthenticator;
import sk.loffay.wandera.dao.CsvNotificationStorage;
import sk.loffay.wandera.dao.InMemoryUserStorage;
import sk.loffay.wandera.dao.NotificationStorage;
import sk.loffay.wandera.dao.UserStorage;
import sk.loffay.wandera.exception.mapper.EntityNotFoundExceptionMapper;
import sk.loffay.wandera.model.User;
import sk.loffay.wandera.rest.RestNotifications;
import sk.loffay.wandera.rest.RestPing;

/**
 * @author Pavol Loffay
 */
public class App extends Application<AppConfiguration> {


    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
    }

    @Override
    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        // test template
        checkTemplateHealth(environment.healthChecks(), configuration);

        // json
        configureObjectMapper(environment.getObjectMapper());

        // auth
        setUpAuthentication(environment.jersey());

        // rest configuration
        addRestServices(environment.jersey(), configuration);
        addExceptionMappers(environment.jersey());
    }

    @Override
    public String getName() {
        return "WanderaNotificationService";
    }

    private void setUpAuthentication(JerseyEnvironment jerseyEnvironment) {
        final UserStorage userStorage = new InMemoryUserStorage();
        jerseyEnvironment.register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new SimpleAuthenticator(userStorage))
                        .buildAuthFilter()));
        jerseyEnvironment.register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private void addRestServices(JerseyEnvironment jerseyEnvironment, AppConfiguration configuration) {
        final RestPing restPing = new RestPing(configuration.getTemplate(), configuration.getEnvironment());
        jerseyEnvironment.register(restPing);

        NotificationStorage notificationStorage = new CsvNotificationStorage(configuration.getNotificationFile());
        final RestNotifications restNotifications = new RestNotifications(notificationStorage);
        jerseyEnvironment.register(restNotifications);
    }

    private void addExceptionMappers(JerseyEnvironment jerseyEnvironment) {
        jerseyEnvironment.register(new EntityNotFoundExceptionMapper());
    }

    private void checkTemplateHealth(HealthCheckRegistry healthCheckRegistry, AppConfiguration configuration) {
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        healthCheckRegistry.register("template", healthCheck);
    }

    public static void configureObjectMapper(ObjectMapper mapper) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY); // skip false :(
        mapper.enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
