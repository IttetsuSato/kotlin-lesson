package com.uzabase.corporateproduct.lesson.demo.validator


import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [WordOrEmailRequiredValidator::class])
annotation class WordOrEmailRequired(
    val message: String = "Either word or email is required",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
