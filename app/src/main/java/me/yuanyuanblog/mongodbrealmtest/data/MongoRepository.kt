package me.yuanyuanblog.mongodbrealmtest.data

import kotlinx.coroutines.flow.Flow
import me.yuanyuanblog.mongodbrealmtest.model.Person
import org.mongodb.kbson.ObjectId

interface MongoRepository {
    fun getData(): Flow<List<Person>>
    fun filterData(name: String): Flow<List<Person>>
    suspend fun insertPerson(person: Person)
    suspend fun updatePerson(person: Person)
    suspend fun deletePerson(id:ObjectId)
}