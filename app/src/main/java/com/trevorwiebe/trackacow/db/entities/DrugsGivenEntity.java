package com.trevorwiebe.trackacow.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "DrugsGiven")
public class DrugsGivenEntity implements Parcelable {

    public static final String DRUGS_GIVEN = "drugsGiven";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primaryKey")
    private int primaryKey;

    @ColumnInfo(name = "drugId")
    private String drugId;

    @ColumnInfo(name = "amountGiven")
    private int amountGiven;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "cowId")
    private String cowId;

    @ColumnInfo(name = "penId")
    private String penId;

    public DrugsGivenEntity(int primaryKey, String drugId, int amountGiven, long date, String cowId, String penId) {
        this.primaryKey = primaryKey;
        this.drugId = drugId;
        this.amountGiven = amountGiven;
        this.date = date;
        this.cowId = cowId;
        this.penId = penId;
    }

    @Ignore
    public DrugsGivenEntity(){}

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public int getAmountGiven() {
        return amountGiven;
    }

    public void setAmountGiven(int amountGiven) {
        this.amountGiven = amountGiven;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCowId() {
        return cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public String getPenId() {
        return penId;
    }

    public void setPenId(String penId) {
        this.penId = penId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.primaryKey);
        dest.writeString(this.drugId);
        dest.writeInt(this.amountGiven);
        dest.writeLong(this.date);
        dest.writeString(this.cowId);
        dest.writeString(this.penId);
    }

    protected DrugsGivenEntity(Parcel in) {
        this.primaryKey = in.readInt();
        this.drugId = in.readString();
        this.amountGiven = in.readInt();
        this.date = in.readLong();
        this.cowId = in.readString();
        this.penId = in.readString();
    }

    public static final Creator<DrugsGivenEntity> CREATOR = new Creator<DrugsGivenEntity>() {
        @Override
        public DrugsGivenEntity createFromParcel(Parcel source) {
            return new DrugsGivenEntity(source);
        }

        @Override
        public DrugsGivenEntity[] newArray(int size) {
            return new DrugsGivenEntity[size];
        }
    };
}