package com.example.klaro.di


import com.example.klaro.domain.repository.interfaces.ImportRepository
import com.example.klaro.domain.repository.implementations.ImportRepositoryImpl
import com.example.klaro.domain.repository.implementations.LearningRepositoryImpl
import com.example.klaro.domain.repository.interfaces.LoginRepository
import com.example.klaro.domain.repository.implementations.LoginRepositoryImpl
import com.example.klaro.domain.repository.interfaces.MainRepository
import com.example.klaro.domain.repository.implementations.MainRepositoryImpl
import com.example.klaro.domain.repository.interfaces.RegistrationRepository
import com.example.klaro.domain.repository.implementations.RegistrationRepositoryImpl
import com.example.klaro.domain.repository.interfaces.LearningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
{

    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindRegistrationRepository(impl: RegistrationRepositoryImpl): RegistrationRepository

    @Binds
    @Singleton
    abstract fun provideImportRepository(impl: ImportRepositoryImpl): ImportRepository

    @Binds
    @Singleton
    abstract fun provideMainRepository(impl: MainRepositoryImpl): MainRepository

    @Binds
    @Singleton
    abstract fun provideLearningRepository(impl: LearningRepositoryImpl): LearningRepository
}

