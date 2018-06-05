package com.ftec.constratints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueLoginValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {
    String message() default "Unique login constraint fails";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
