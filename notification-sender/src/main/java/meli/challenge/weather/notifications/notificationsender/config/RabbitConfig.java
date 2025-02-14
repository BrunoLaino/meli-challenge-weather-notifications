package meli.challenge.weather.notifications.notificationsender.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_ENVIOS = "queue.envios";
    public static final String EXCHANGE_ENVIOS = "exchange.envios";

    public static final String DEAD_LETTER_QUEUE = "queue.envios.dead.letter";
    public static final String DEAD_LETTER_EXCHANGE = "exchange.envios.dead.letter";
    public static final String DEAD_LETTER_ROUTING_KEY = "routingKey.envios.dead.letter";

    @Bean
    public Queue enviosQueue() {
        return QueueBuilder.durable(QUEUE_ENVIOS)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange enviosExchange() {
        return new DirectExchange(EXCHANGE_ENVIOS);
    }

    @Bean
    public Binding enviosBinding(Queue enviosQueue, DirectExchange enviosExchange) {
        return BindingBuilder.bind(enviosQueue).to(enviosExchange).with(QUEUE_ENVIOS);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        // Permite que o conversor confie nos pacotes onde estão os DTOs
        typeMapper.setTrustedPackages(
                "meli.challenge.weather.notifications.scheduleworker.model.dto",
                "meli.challenge.weather.notifications.notificationsender.model.dto"
        );

        // Mapeamento explícito dos tipos enviados pelo produtor para as classes do consumidor
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(
                "meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage",
                meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO.class
        );
        idClassMapping.put(
                "meli.challenge.weather.notifications.scheduleworker.model.dto.CityWeatherForecast",
                meli.challenge.weather.notifications.notificationsender.model.dto.CityWeatherForecastDTO.class
        );
        idClassMapping.put(
                "meli.challenge.weather.notifications.scheduleworker.model.dto.DeadLetterRecordDTO",
                meli.challenge.weather.notifications.notificationsender.model.dto.CityWeatherForecastDTO.class
        );
        typeMapper.setIdClassMapping(idClassMapping);

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }
}
