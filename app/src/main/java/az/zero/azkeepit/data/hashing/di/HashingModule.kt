package az.zero.azkeepit.data.hashing.di

import az.zero.azkeepit.data.hashing.SHA256PasswordHasher
import az.zero.azkeepit.domain.hashing.PasswordHasher
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HashingModule {

    @Binds
    @Reusable
    abstract fun bindPasswordHasher(
        impl: SHA256PasswordHasher
    ): PasswordHasher
}