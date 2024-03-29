package com.odysseusinc.arachne.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "arachne")
public class ArachneConfiguration {
    @Value("${disabledModules:}")
    private List<String> disabledModules;

    public List<String> getDisabledModules() {
        return disabledModules;
    }

    public void setDisabledModules(List<String> disabledModules) {
        this.disabledModules = disabledModules;
    }

    public boolean isModuleDisabled(String module) {
        return this.disabledModules != null && this.disabledModules.contains(module);
    }
}
