package com.smaugslair.thitracker.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

//As is Spring will look for thi.properties on the classpath
@ConfigurationProperties("thi")
public class ThiProperties {

    private String appUrl = "urlnotloaded";
    private String resetEndpoint = "resetpassword";

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getResetEndpoint() {
        return resetEndpoint;
    }

    public void setResetEndpoint(String resetEndpoint) {
        this.resetEndpoint = resetEndpoint;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThiProperties.class.getSimpleName() + "[", "]")
                .add("appUrl='" + appUrl + "'")
                .add("resetEndpoint='" + resetEndpoint + "'")
                .toString();
    }
}
