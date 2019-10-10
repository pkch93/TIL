package objectmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sample.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectMapperTest {
    private static final String TEST_RESOURCE = "src/test/resources";
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void person_json_파일_Person_Object로_파싱() throws IOException {
        Person person = objectMapper.readValue(new File(TEST_RESOURCE + "/person.json"), Person.class);

        assertThat(person.getName()).isEqualTo("pkch");
        assertThat(person.getAge()).isEqualTo(27);
    }

    @Test
    void person_Object를_json_String으로_파싱() throws IOException {
        Person person = new Person("pkch", 27);

        objectMapper.writeValue(new File(TEST_RESOURCE + "/write.json"), person);
        String personJsonString = objectMapper.writeValueAsString(person);
        byte[] personJsonBytes = objectMapper.writeValueAsBytes(person);
    }

    @Test
    void jsonNode() throws IOException {
        Person person = new Person("pkch", 27);

        JsonNode jsonNode = objectMapper.readTree(new File(TEST_RESOURCE + "/person.json"));
        assertThat(jsonNode.get("name").asText()).isEqualTo("pkch");
        assertThat(jsonNode.get("age").asInt()).isEqualTo(27);
    }

    @Test
    void 잘못된_필드를_가진_json_파일_파싱시_예외() {
        assertThrows(UnrecognizedPropertyException.class, () -> objectMapper.readValue(new File(TEST_RESOURCE + "/invalid_person.json"), Person.class));
    }

    @Test
    void json_array를_java_List로_파싱() throws IOException {
        List<Person> persons = objectMapper.readValue(new File(TEST_RESOURCE + "/list.json"), new TypeReference<List<Person>>(){});

        assertThat(persons.size()).isEqualTo(2);
    }
}
