package ro.msg.learning.shop.controller.messageconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.msg.learning.shop.dto.StockDto;

import java.util.List;

@Configuration
public class MessageConverterConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new CSVMessageConverter<>(StockDto.class));
    }

}
