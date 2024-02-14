package boardproject.sns.util;

import java.util.Optional;

public class ClassUtils {

    public static <T> Optional<T> getSafeCastInstance(Object o, Class<T> clazz){
        //clazz가 null이 아니고 instnce라면 cast를 하고 아니면 empty 반환
        return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
    }

}
