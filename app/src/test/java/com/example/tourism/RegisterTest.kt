package com.example.tourism

import com.example.tourism.until.RegisterValidations
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RegisterTest {
    private lateinit var validator: RegisterValidations

    @Before
    fun setup() {
        validator = RegisterValidations()
    }
// test for email
    @Test
    fun emailFalseValue() {
        val validation = validator.emailIsValid("test-dd.com")

        Assert.assertEquals(false, validation)
    }

    @Test
    fun emailTrueValue() {
        val validation = validator.emailIsValid("test@test.com")

        Assert.assertEquals(true, validation)
    }

// test for password
    @Test
    fun passworFalseValue() {
        val validation = validator.passwordIsValid("69")

        Assert.assertEquals(false, validation)
    }


    @Test
    fun passworTrueValue() {
        val validation = validator.passwordIsValid("eagleAL@ibi1")

        Assert.assertEquals(true, validation)
    }

}