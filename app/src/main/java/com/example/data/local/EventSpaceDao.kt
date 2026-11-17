package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EventSpaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventSpaceDao {

    @Query("SELECT * FROM event_spaces ORDER BY createdAt DESC")
    fun getAllSpaces(): Flow<List<EventSpaceEntity>>

    @Query("SELECT * FROM event_spaces WHERE id = :id")
    fun getSpaceById(id: Long): Flow<EventSpaceEntity?>

    @Query("SELECT * FROM event_spaces WHERE id = :id LIMIT 1")
    suspend fun getSpaceByIdOnce(id: Long): EventSpaceEntity?

    @Query("SELECT COUNT(*) FROM event_spaces")
    suspend fun countSpaces(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpace(space: EventSpaceEntity): Long

    @Update
    suspend fun updateSpace(space: EventSpaceEntity)

    @Delete
    suspend fun deleteSpace(space: EventSpaceEntity)

    @Query("UPDATE event_spaces SET isDisabled = :isDisabled WHERE id = :id")
    suspend fun updateDisabledStatus(id: Long, isDisabled: Boolean)
}
