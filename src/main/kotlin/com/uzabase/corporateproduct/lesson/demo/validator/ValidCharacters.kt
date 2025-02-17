package com.uzabase.corporateproduct.lesson.demo.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidCharactersValidator::class])
annotation class ValidCharacters(
    val message: String = "Contains invalid characters: {ngChars}",
    val ngChars: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)