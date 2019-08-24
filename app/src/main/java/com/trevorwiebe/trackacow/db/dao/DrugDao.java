package com.trevorwiebe.trackacow.db.dao;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.trevorwiebe.trackacow.db.entities.DrugEntity;

import java.util.List;

@Dao
public interface DrugDao {

    @Insert
    void insertDrug(DrugEntity drugEntity);

    @Insert
    void insertListDrug(List<DrugEntity> drugEntities);

    @Query("SELECT * FROM Drug WHERE drugId = :id")
    DrugEntity getDrugById(String id);

    @Query("SELECT * FROM Drug")
    List<DrugEntity> getDrugList();

    @Query("UPDATE Drug SET defaultAmount = :defaultAmount, drugName = :drugName WHERE drugId = :drugId")
    void updateDrugById(int defaultAmount, String drugName, String drugId);

    @Query("DELETE FROM Drug")
    void deleteDrugTable();

    @Query("DELETE FROM Drug WHERE drugId = :drugId")
    void deleteDrugById(String drugId);

    @Update
    void updateDrug(DrugEntity drugEntity);

    @Delete
    void deleteDrug(DrugEntity drugEntity);

}
