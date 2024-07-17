package mate.academy.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final String PATTERN_OF_ISBN13 = "^(?:ISBN(?:-13)?:? )?(?=[0-9]"
            + "{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]"
            + "?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null && Pattern.compile(PATTERN_OF_ISBN13).matcher(isbn).matches();
    }
}
