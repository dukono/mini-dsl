package dukono.minidsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(value = DslApiGenerators.class)
public @interface DslApiGenerator {

	Class<?> filterClass() default Void.class;

	String prefixName() default "";

	String removeFromPrefixName() default "";

	String packagePath() default "";

	String classNameSuffix() default "RequestApi";

	Class<? extends Dto> dto();

}
