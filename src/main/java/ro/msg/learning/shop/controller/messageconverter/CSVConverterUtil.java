package ro.msg.learning.shop.controller.messageconverter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Component
public final class CSVConverterUtil {
    private static final CsvMapper mapper = new CsvMapper();

    private CSVConverterUtil() {

    }

    @SneakyThrows
    public static <T> List<T> fromCsv(Class<T> clazz, InputStream csvFile) {
        final var schema = mapper.schemaFor(clazz).withHeader();
        final MappingIterator<T> it = mapper.readerFor(clazz).with(schema).readValues(csvFile);
        return it.readAll();

    }

    @SneakyThrows
    public static <T> void toCsv(Class<T> clazz, List<T> data, OutputStream outputStream) {
        final var schema = mapper.schemaFor(clazz).withHeader();
        mapper.writer(schema).writeValues(outputStream).writeAll(data);
    }
}
