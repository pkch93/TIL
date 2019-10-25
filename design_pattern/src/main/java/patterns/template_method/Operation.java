package patterns.template_method;

public interface Operation<T, R> {

    R operate(T former, T letter);
}
