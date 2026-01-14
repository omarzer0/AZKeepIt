package az.zero.azkeepit.data.hashing

import android.util.Log
import az.zero.azkeepit.domain.hashing.PasswordHasher
import java.security.MessageDigest
import javax.inject.Inject


class SHA256PasswordHasher @Inject constructor() : PasswordHasher {
    override fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(password.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }

    override fun verify(enteredPassword: String, storedHash: String): Boolean {
        Log.d("SHA256PasswordHasher", "enteredPassword=$enteredPassword ")
        Log.d("SHA256PasswordHasher", "storedHash=$storedHash ")
        Log.d("SHA256PasswordHasher", "equal? ${enteredPassword == storedHash}")
        return hash(enteredPassword) == storedHash
    }
}
