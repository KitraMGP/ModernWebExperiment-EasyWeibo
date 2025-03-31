package kitra.easyweibo.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface PostRestController {
    /**
     * Alias for {@link Controller#value}.
     */
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
