package ro.msg.learning.shop.controller.messageconverter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CSVMessageConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {
    private static final MediaType CSV_TYPE = MediaType.valueOf("text/csv");

    private final Class<T> clazz;

    public CSVMessageConverter(Class<T> clazz) {
        super(CSV_TYPE);
        this.clazz = clazz;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected void writeInternal(List<T> ts, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        CSVConverterUtil.toCsv(clazz, ts, outputMessage.getBody());
    }

    @Override
    protected List<T> readInternal(Class<? extends List<T>> c, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return CSVConverterUtil.fromCsv(clazz, inputMessage.getBody());
    }


    @Override
    public List<T> read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return CSVConverterUtil.fromCsv(clazz, inputMessage.getBody());
    }
}
