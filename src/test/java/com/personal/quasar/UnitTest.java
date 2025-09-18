package com.personal.quasar;

import com.personal.quasar.config.MongoConfig;
import com.personal.quasar.config.TomcatConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.lang.module.Configuration;

@SpringBootTest
//@ComponentScan(excludeFilters = @ComponentScan.Filter(classes = MongoConfig.class))
//@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
public class UnitTest {
}
