package com.example.android.voltageapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.voltageapp.entidades.Utilidades.Utilidades;

import java.util.Calendar;

public class deleteData extends AppCompatActivity {
    EditText dateFrom, timeFrom, dateTo, timeTo;
    private int mYearFrom, mMonthFrom, mDayFrom, sYearFrom, sMonthFrom, sDayFrom;
    private int mYearTo, mMonthTo, mDayTo, sYearTo, sMonthTo, sDayTo;
    static final int DATE_ID1 = 0;
    static final int DATE_ID2 = 1;
    Calendar C = Calendar.getInstance();
    Calendar C2 = Calendar.getInstance();
    Button deletedata;
    String textDateFROM,textTimeFrom,textDateTO,textTimeTo;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);

        //From Calendar
        sMonthFrom = C.get(Calendar.MONTH);
        sDayFrom = C.get(Calendar.DAY_OF_MONTH);
        sYearFrom = C.get(Calendar.YEAR);

        //To Calendar
        sMonthTo = C2.get(Calendar.MONTH);
        sDayTo = C2.get(Calendar.DAY_OF_MONTH);
        sYearTo = C2.get(Calendar.YEAR);


        dateFrom = (EditText) findViewById(R.id.dateFrom);
        timeFrom = (EditText) findViewById(R.id.timeFrom);
        dateTo = (EditText) findViewById(R.id.dateTo);
        timeTo=(EditText) findViewById(R.id.timeTo);
        dateFrom.setInputType(InputType.TYPE_NULL);
        dateTo.setInputType(InputType.TYPE_NULL);
        timeFrom.setInputType(InputType.TYPE_NULL);
        timeTo.setInputType(InputType.TYPE_NULL);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDialog(DATE_ID1).show();
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDialog(DATE_ID2).show();
            }
        });
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(deleteData.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay < 10) {
                            if (minutes < 10) {
                                timeFrom.setText("0" + hourOfDay + ":0" + minutes + ":00");
                            } else {
                                timeFrom.setText("0" + hourOfDay + ":" + minutes + ":00");
                            }
                        } else {
                            if (minutes < 10) {
                                timeFrom.setText(hourOfDay + ":0" + minutes + ":00");
                            } else {
                                timeFrom.setText(hourOfDay + ":" + minutes + ":00");
                            }
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(deleteData.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay2, int minutes2) {
                        if (hourOfDay2 < 10) {
                            if (minutes2 < 10) {
                                timeTo.setText("0" + hourOfDay2 + ":0" + minutes2 + ":00");
                            } else {
                                timeTo.setText("0" + hourOfDay2 + ":" + minutes2 + ":00");
                            }
                        } else {
                            if (minutes2 < 10) {
                                timeTo.setText(hourOfDay2 + ":0" + minutes2 + ":00");
                            } else {
                                timeTo.setText(hourOfDay2 + ":" + minutes2 + ":00");
                            }
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });

        deletedata = (Button) findViewById(R.id.deleteData);
        deletedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textDateFROM=dateFrom.getText().toString();
                textDateTO=dateTo.getText().toString();
                textTimeFrom=timeFrom.getText().toString();
                textTimeTo=timeTo.getText().toString();
                if (textDateFROM.length()!=0 && textDateTO.length()!=0 && textTimeFrom.length()!=0 && textTimeTo.length()!=0) {
                    conn = new ConexionSQLiteHelper(getApplicationContext(),
                            Utilidades.TABLA_HEARTRATE, null, 1);
                    SQLiteDatabase db = conn.getWritableDatabase();
//                 db.rawQuery("DELETE FROM "
//                        + Utilidades.TABLA_HEARTRATE + " WHERE " + Utilidades.CAMPO_DATE_TIME
//                        + " >= Datetime('" + textDateFROM + textTimeFrom + "') AND "
//                        + Utilidades.CAMPO_DATE_TIME + "<= Datetime('" + textDateTO
//                        + textTimeTo + "')", null);
                    String query = "DELETE FROM "+ Utilidades.TABLA_HEARTRATE + " WHERE " + Utilidades.CAMPO_DATE_TIME
                            + ">=Datetime('" + textDateFROM + textTimeFrom + "') AND "
                            + Utilidades.CAMPO_DATE_TIME + "<=Datetime('" + textDateTO
                            + textTimeTo + "')";
                    db.execSQL(query);
                    Toast.makeText(getApplicationContext(), "Data from " + textDateFROM + textTimeFrom + " to " + textDateTO + textTimeTo + " have been deleted",
                            Toast.LENGTH_SHORT).show();
                    db.close();
                }else{
                    Toast.makeText(getApplicationContext(), "Please set the time period",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void colocar_fecha1() {
        if (mMonthFrom + 1 <= 9) {
            dateFrom.setText(mYearFrom + "-0" + (mMonthFrom + 1) + "-" + mDayFrom + " ");
        } else {
            dateFrom.setText(mYearFrom + "-" + (mMonthFrom + 1) + "-" + mDayFrom + " ");
        }
    }

    private void colocar_fecha2() {
        if (mMonthTo + 1 <= 9) {
            dateTo.setText(mYearTo + "-0" + (mMonthTo + 1) + "-" + mDayTo + " ");
        } else {
            dateTo.setText(mYearTo + "-" + (mMonthTo + 1) + "-" + mDayTo + " ");
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year2, int monthOfYear2, int dayOfMonth2) {
                    mYearTo = year2;
                    mMonthTo = monthOfYear2;
                    mDayTo = dayOfMonth2;
                    colocar_fecha2();
                }
            };

    private DatePickerDialog.OnDateSetListener getmDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    mYearFrom = year;
                    mMonthFrom = monthOfYear;
                    mDayFrom = dayOfMonth;
                    colocar_fecha1();
                }
            };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID1:
                return new DatePickerDialog(deleteData.this, getmDateSetListener1, sYearFrom, sMonthFrom, sDayFrom);
            case DATE_ID2:
                return new DatePickerDialog(deleteData.this, mDateSetListener2, sYearTo, sMonthTo, sDayTo);
        }
        return null;
    }
}
