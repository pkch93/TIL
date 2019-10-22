package reflection;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldReflectionTest {

    private static final List<String> personFieldNames = Arrays.asList("name", "age");
    private Object person;

    @BeforeEach
    void setUp() {
        person = new Person("name", 777);
    }

    /**
     * TODO: getDeclaredFields와 getFields의 차이
     */
    @Test
    void field_이름_뽑기() {

        Class<?> clazz = person.getClass();

        List<String> actual = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());

        for (int i = 0; i < personFieldNames.size(); i++) {
            assertThat(actual.get(i)).isEqualTo(personFieldNames.get(i));
        }
    }


}
