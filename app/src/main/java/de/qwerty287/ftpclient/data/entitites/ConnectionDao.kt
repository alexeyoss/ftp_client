package de.qwerty287.ftpclient.data.entitites

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class ConnectionDao {

    @Query("SELECT * FROM connections")
    abstract fun getAll(): LiveData<List<Connection>>

    @Query("SELECT * FROM connections WHERE _id = :id LIMIT 1")
    abstract suspend fun get(id: Long): Connection?

    @Query("SELECT * FROM connections")
    abstract suspend fun getListOfAll(): List<Connection>

    @Query("SELECT COUNT(title) FROM connections WHERE title LIKE :title")
    abstract suspend fun countRows(title: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(connection: Connection): Long

    @Delete
    abstract suspend fun delete(connection: Connection)

    @Update
    abstract suspend fun update(connection: Connection)

    // TODO refactor
    suspend fun copyElem(connection: Connection) {
        val searchPattern = connection.title.filter { it.isLetter() }

        val entitiesAmount = countRows(searchPattern) + 1
        insert(connection.apply {
            this.title += entitiesAmount.toString()
        })
    }
}
