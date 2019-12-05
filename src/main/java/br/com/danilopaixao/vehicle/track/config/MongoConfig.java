package br.com.danilopaixao.vehicle.track.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import br.com.danilopaixao.vehicle.track.config.converter.ZonedDateTimeReadConverter;
import br.com.danilopaixao.vehicle.track.config.converter.ZonedDateTimeWriteConverter;

@Configuration
public class MongoConfig{

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();


    public MongoCustomConversions customConversions() {
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }

}