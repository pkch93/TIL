package reflection;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassReflectionTest {

    private Object person;

    @BeforeEach
    public void setUp() throws Exception {
        person = new Person("pkch", 27);
    }

    /**
     * getName은 해당 객체의 class 이름을 String 형태로 알려준다.
     * 다만, 유의점은 클래스 이름만 보여주는 것이 아닌 해당 클래스가 소속한 패키지까지 모두 보여준다.
     */
    @Test
    public void getName() {
        Class<?> clazz = person.getClass();

        System.out.println(clazz.getName());
        assertThat(clazz.getName()).isEqualTo(Person.class.getName());
        assertThat(clazz).isEqualTo(Person.class);
    }

    @Test
    void getModifiers() {
        Class<?> clazz = person.getClass();

        int modifiers = clazz.getModifiers();

        assertThat(modifiers).isEqualTo(1);
    }

    @Test
    void Parameter() {
        Class<?> clazz = person.getClass();

        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod.getName());
            Parameter[] parameters = declaredMethod.getParameters();
            String[] parameterNames = new String[parameters.length];
            int i = 0;
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getType());
                parameterNames[i] = parameter.getName();
                i += 1;
            }

            Arrays.stream(parameterNames)
                    .forEach(System.out::println);
        }
    }
}
