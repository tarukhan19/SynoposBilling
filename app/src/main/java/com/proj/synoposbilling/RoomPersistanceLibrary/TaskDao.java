package com.proj.synoposbilling.RoomPersistanceLibrary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM KutumbDTO" )
    List<KutumbDTO> getAllProduct();

    @Query("SELECT * FROM KutumbDTO WHERE productid=:productid and id =:id" )
    public List<KutumbDTO> getDetail(String productid, String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(KutumbDTO task);

    @Delete
    void delete(KutumbDTO task);

    @Update
    void update(KutumbDTO task);

    @Query("delete from kutumbdto")
    void deleteAll();



}
