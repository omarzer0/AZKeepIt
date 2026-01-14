package az.zero.azkeepit.data.hashing

import az.zero.azkeepit.domain.hashing.PasswordHasher
import java.security.MessageDigest
import javax.inject.Inject

class SHA256PasswordHasher @Inject constructor() : PasswordHasher {
    override fun hash(password: String?): String? {
        if (password == null) return null
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(password.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }

    override fun verify(enteredPassword: String, storedHash: String): Boolean {
        return hash(enteredPassword) == storedHash
    }
}
