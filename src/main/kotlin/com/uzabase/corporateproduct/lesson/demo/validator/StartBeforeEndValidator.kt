package com.uzabase.corporateproduct.lesson.demo.validator

import com.uzabase.corporateproduct.lesson.demo.controller.SearchRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StartBeforeEndValidator : ConstraintValidator<StartBeforeEnd, SearchRequest> {
    override fun isValid(value: SearchRequest?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false

        if (!value.word.isNullOrEmpty() && value.email.isNullOrEmpty()) return true
        if (value.word.isNullOrEmpty() && !value.email.isNullOrEmpty()) return true
        return false
    }
}