package sk.loffay.wandera;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * @author Pavol Loffay
 */
public class AppConfiguration extends Configuration {

    @NotEmpty
    private String template;

    @NotEmpty
    private String environment;

    @NotEmpty
    private String notificationFile;


    @JsonProperty
    public String getEnvironment() {
        return environment;
    }

    @JsonProperty
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getNotificationFile() {
        return notificationFile;
    }

    @JsonProperty
    public void setNotificationFile(String notificationFile) {
        this.notificationFile = notificationFile;
    }
}
