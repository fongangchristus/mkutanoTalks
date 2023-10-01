package org.it4innov.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "telegram")
public interface TelegramConfig {
    String token();
    String chatName();
}
