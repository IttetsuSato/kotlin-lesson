package com.uzabase.corporateproduct.lesson.demo.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailValidator(): ConstraintValidator<Email, String> {
    override fun isValid(email: String?, context: ConstraintValidatorContext?): Boolean {
        if(email == null) return true

        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        return regex.matches(email)
    }
}
