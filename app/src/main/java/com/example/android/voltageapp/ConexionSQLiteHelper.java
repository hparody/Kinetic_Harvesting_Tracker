package com.example.android.voltageapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.voltageapp.entidades.Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase dbHeartRate) {

        dbHeartRate.execSQL(Utilidades.CREAR_TABLA_HEARTRATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbHeartRate, int VersionAntigua, int VersionNueva) {
        dbHeartRate.execSQL("DROP TABLE IF EXISTS heartRate");
        onCreate(dbHeartRate);
    }
}
