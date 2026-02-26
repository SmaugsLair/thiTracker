package com.smaugslair.thitracker.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanFinder implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        BeanFinder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> klass) {
        return applicationContext.getBean(klass);
    }

}
