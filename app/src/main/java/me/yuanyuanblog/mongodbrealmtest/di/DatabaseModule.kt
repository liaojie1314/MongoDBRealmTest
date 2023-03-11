package me.yuanyuanblog.mongodbrealmtest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import me.yuanyuanblog.mongodbrealmtest.data.MongoRepository
import me.yuanyuanblog.mongodbrealmtest.data.MongoRepositoryImpl
import me.yuanyuanblog.mongodbrealmtest.model.Address
import me.yuanyuanblog.mongodbrealmtest.model.Person
import me.yuanyuanblog.mongodbrealmtest.model.Pet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Person::class, Address::class, Pet::class
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideMongoRepository(realm: Realm): MongoRepository {
        return MongoRepositoryImpl(realm = realm)
    }
}