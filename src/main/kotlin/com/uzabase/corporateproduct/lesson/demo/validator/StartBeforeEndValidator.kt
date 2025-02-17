package com.uzabase.corporateproduct.lesson.demo.validator

import com.uzabase.corporateproduct.lesson.demo.controller.TimeRangeRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class StartBeforeEndValidator : ConstraintValidator<StartBeforeEnd, TimeRangeRequest> {
    override fun isValid(value: TimeRangeRequest?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false // null自体は許可しない
        return value.start < value.end // start が end より小さい場合に有効
    }
}