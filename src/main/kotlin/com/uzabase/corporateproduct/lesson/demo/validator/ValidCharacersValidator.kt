package com.uzabase.corporateproduct.lesson.demo.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidCharactersValidator(): ConstraintValidator<ValidCharacters, String> {
    private lateinit var ngChars: String

    override fun initialize(constraintAnnotation: ValidCharacters?) {
        this.ngChars = constraintAnnotation?.ngChars ?: throw RuntimeException("No ngChars found on Annotation")
    }

    override fun isValid(word: String?, context: ConstraintValidatorContext?): Boolean {
        if(word == null) return true

        val ngCharArray = this.ngChars.toCharArray()
        for(c in ngCharArray) {
            if(word.contains(c, false)) return false
        }
        return true;
    }
}