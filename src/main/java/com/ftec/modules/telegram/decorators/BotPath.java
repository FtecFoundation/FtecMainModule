package com.ftec.modules.telegram.decorators;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BotPath {
    String[] value();
}
