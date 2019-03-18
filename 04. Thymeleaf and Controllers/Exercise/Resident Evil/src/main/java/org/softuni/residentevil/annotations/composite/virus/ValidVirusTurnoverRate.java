package org.softuni.residentevil.annotations.composite.virus;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Turnover Rate – Number, between 0 and 100.
 */

@NotNull
@Min(ValidVirusTurnoverRate.MIN)
@Max(ValidVirusTurnoverRate.MAX)
@ReportAsSingleViolation
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidVirusTurnoverRate {

    int MIN = 0;
    int MAX = 100;

    String message() default "Turnover rate should be between 0 and 100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
