package org.tongwoo.core;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.tongwoo.util.Configuration;

import javax.jms.ConnectionFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableJms
public class ComsumeDataApplication {
    private static CountDownLatch LATCH = new CountDownLatch(1);

    public static void main(String...args) throws InterruptedException {
        SpringApplication app = new SpringApplication(ComsumeDataApplication.class);
        app.setWebEnvironment(false);
        app.setBannerMode(Banner.Mode.OFF);
        //CountDownLatch is synchronization thread util, just thread is LATCH.await so await only to this call LATCH.countDown function the thread will continue running
        ComsumeDataRunning.LATCH = LATCH;
        ComsumeDataRunning.StaticConsumer.LATCH = LATCH;
        ComsumeDataRunning.PositionConsumer.LATCH = LATCH;
        app.run(args);
        LATCH.await();
    }

    @Bean
    public MetricRegistry metrics() {
        return new MetricRegistry();
    }

    @Bean
    public ConsoleReporter consoleReporter(MetricRegistry metrics) {
        return ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
    	ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, Configuration.getValue("activemq.brokerUrl"));
//    	ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=10000");
        return connectionFactory;
    }

//    @Bean
//    public JmsTemplate jmsTemplate() throws JMSException {
//    	ConnectionFactory connectionFactory = connectionFactory();
//        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//        return jmsTemplate;
//    }

    @Bean(name="staticMessageListener")
    public DefaultMessageListenerContainer staticMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        defaultMessageListenerContainer.setDestination(new ActiveMQQueue(Configuration.getValue("static.messageListener.Class")));
        defaultMessageListenerContainer.setMessageListener(new ComsumeDataRunning.StaticConsumer());
        defaultMessageListenerContainer.setConcurrentConsumers(Configuration.getIntegerValue("static.messageListener.MaxConcurrency"));
//        defaultMessageListenerContainer.setConcurrency("2-9");
//        defaultMessageListenerContainer.setMaxConcurrentConsumers(15);
        return defaultMessageListenerContainer;
    }
    
    @Bean(name="positionMessageListener")
    public DefaultMessageListenerContainer positionMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
    	defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
    	defaultMessageListenerContainer.setDestination(new ActiveMQQueue(Configuration.getValue("position.messageListener.Class")));
    	defaultMessageListenerContainer.setMessageListener(new ComsumeDataRunning.PositionConsumer());
        defaultMessageListenerContainer.setConcurrentConsumers(Configuration.getIntegerValue("position.messageListener.MaxConcurrency"));
//        defaultMessageListenerContainer.setConcurrency("2-9");
//        defaultMessageListenerContainer.setMaxConcurrentConsumers(15);
    	return defaultMessageListenerContainer;
    }
 
}
