package com.rogergcc.ormroom.data.local.db;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by ROGERGCC on 18/02/2020.
 */
public class DateTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }
    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }
}