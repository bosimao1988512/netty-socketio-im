package com.dcjt.dcjtim.bean;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置bean
 * Created by 唐文滔 on 2020/6/4
 */
@Configuration
@PropertySource(value = {"classpath:applicationDynamic.properties"})
//@PropertySource(value = {"file:src/main/resources/applicationDynamic.properties"})
public class BeanConf {
    @Value("${netty.socketio.port:8090}")
    private String socketPort;
    @Autowired
    RedisProperties redisProperties;
    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        //config.setCodec(new org.redisson.client.codec.StringCodec());
        config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setConnectionMinimumIdleSize(1).setConnectionPoolSize(3).setDatabase(redisProperties.getDatabase());
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    @Bean
    com.corundumstudio.socketio.Configuration configuration() {

        // socket配置
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setSoLinger(0);
        socketConfig.setReuseAddress(true);
        //socketConfig.setTcpNoDelay(true);

        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        RedissonStoreFactory redissonStoreFactory = new RedissonStoreFactory(redissonClient());
        configuration.setStoreFactory(redissonStoreFactory);
        configuration.setSocketConfig(socketConfig);
        configuration.setBossThreads(1);
        configuration.setWorkerThreads(3);
        configuration.setMaxFramePayloadLength(1024*1024);
        configuration.setPort(Integer.valueOf(socketPort));
        configuration.setPingInterval(30000);
        configuration.setPingTimeout(10000);
        configuration.setExceptionListener(new ExceptionListenerAdapter(){
            @Override
            public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
                ctx.close();
                return true;
            }
        });
        return configuration;
    }

    @Bean
    SocketIOServer socketIOServer() {
        return new SocketIOServer(configuration());
    }

    @Bean
    Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()-3);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(3);
        executor.setThreadNamePrefix("滔哥带你飞");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        //executor.execute(new Runnable{});
        return executor;
    }

    @Bean
    //@ConditionalOnClass(JavaTimeModule.class)
    Jackson2ObjectMapperBuilderCustomizer customizeLocalDateTimeFormat() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_DATE_TIME_FORMAT)));
            jacksonObjectMapperBuilder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_DATE_FORMAT)));
            jacksonObjectMapperBuilder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_TIME_FORMAT)));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_DATE_TIME_FORMAT)));
            jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_DATE_FORMAT)));
            jacksonObjectMapperBuilder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatetimeFormats.DEFAULT_TIME_FORMAT)));

        };
    }

}
