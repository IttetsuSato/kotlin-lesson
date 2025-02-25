package com.uzabase.corporateproduct.lesson.demo.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailValidator::class])
annotation class Email(
    val message: String = "Invalid email format",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)