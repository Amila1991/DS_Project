package org.sem8.ds.client.controller;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author amila karunathilaka.
 */
public abstract class AbstractClientController {

    public AbstractClientController() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }
}
