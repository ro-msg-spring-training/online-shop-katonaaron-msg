package ro.msg.learning.shop.controller.messageconverter;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CSVConverterUtilTest {

    private final Person person1 = new Person("John Smith", 21, "johnsmith@gmail.com");
    private final Person person2 = new Person("Maria Martin", 25, "mariamartin@gmail.com");

    private final List<Person> persons = List.of(person1, person2);
    private final String CSV_PERSON1 = """
            age,email,name
            21,johnsmith@gmail.com,"John Smith"
            """;
    private final String CSV_PERSON2 = """
            age,email,name
            25,mariamartin@gmail.com,"Maria Martin"
            """;

    private final String CSV_PERSONS = CSV_PERSON1 + CSV_PERSON2.substring(CSV_PERSON2.indexOf('\n') + 1);

    @Test
    void testFromCsv_one() {
        List<Person> results = CSVConverterUtil.fromCsv(Person.class, new ByteArrayInputStream(CSV_PERSON1.getBytes()));
        assertThat(results).singleElement().isEqualTo(person1);
    }

    @Test
    void testFromCsv_multiple() {
        List<Person> results = CSVConverterUtil.fromCsv(Person.class, new ByteArrayInputStream(CSV_PERSONS.getBytes()));
        assertThat(results).hasSize(2).hasSameElementsAs(persons);
    }

    @Test
    @SneakyThrows
    void testToCsv_one() {
        testListToCsv(Collections.singletonList(person1), CSV_PERSON1);
    }

    @Test
    @SneakyThrows
    void testToCsv_multiple() {
        testListToCsv(persons, CSV_PERSONS);
    }

    private void testListToCsv(List<Person> persons, String csv) throws IOException {
        try (final var baos = new ByteArrayOutputStream()) {
            CSVConverterUtil.toCsv(Person.class, persons, baos);
            final var text = baos.toString();
            assertThat(text).isEqualTo(csv);
        }
    }

    private record Person(
            String name,
            Integer age,
            String email) {
    }
}