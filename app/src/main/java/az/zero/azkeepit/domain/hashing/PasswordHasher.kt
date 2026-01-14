package az.zero.azkeepit.domain.hashing

interface PasswordHasher {
    fun hash(password: String?): String?
    fun verify(enteredPassword: String, storedHash: String): Boolean
}
