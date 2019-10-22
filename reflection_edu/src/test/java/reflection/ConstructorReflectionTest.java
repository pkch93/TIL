package reflection;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorReflectionTest {

    private Class<?> clazz;
    private List<String> personFieldNames = Arrays.asList("name", "age");

    @BeforeEach
    void setUp() {
        clazz = Person.class;
    }

    @Test
    void constructor_정보_뽑기() throws NoSuchMethodException {
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);

        Parameter[] parameters = constructor.getParameters();
        Arrays.stream(parameters)
                .map(Parameter::getName)
                .forEach(System.out::println);
    }

    @Test
    void 기본_생성자로_필드값_주입() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = clazz.getConstructor();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "pkch");
        map.put("age", 27);

        Object newObj = constructor.newInstance();
        Field[] fields = newObj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            for (String fieldName : personFieldNames) {
                if (fieldName.equals(field.getName())) {
                    field.set(newObj, map.get(fieldName));
                }
            }
        }

        Person actual = (Person) newObj;

        assertThat(actual.getName()).isEqualTo("pkch");
        assertThat(actual.getAge()).isEqualTo(27);
    }
}
