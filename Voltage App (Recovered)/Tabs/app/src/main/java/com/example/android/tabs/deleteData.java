package com.example.android.tabs;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tabs.Utilidades.Utilidades;

import java.util.Calendar;

public class deleteData extends AppCompatActivity {
    static final int DATE_ID1 = 0;
    static final int DATE_ID2 = 1;
    /* renamed from: C */
    Calendar f43C = Calendar.getInstance();
    Calendar C2 = Calendar.getInstance();
    ConexionSQLiteHelper conn;
    EditText dateFrom;
    EditText dateTo;
    Button deletedata;
    private OnDateSetListener getmDateSetListener1 = new C03317();
    private OnDateSetListener mDateSetListener2 = new C03306();
    private int mDayFrom;
    private int mDayTo;
    private int mMonthFrom;
    private int mMonthTo;
    private int mYearFrom;
    private int mYearTo;
    private int sDayFrom;
    private int sDayTo;
    private int sMonthFrom;
    private int sMonthTo;
    private int sYearFrom;
    private int sYearTo;
    String textDateFROM;
    String textDateTO;
    String textTimeFrom;
    String textTimeTo;
    EditText timeFrom;
    EditText timeTo;

    /* renamed from: com.example.android.voltageapp.deleteData$1 */
    class C03231 implements OnClickListener {
        C03231() {
        }

        public void onClick(View view) {
            deleteData.this.onCreateDialog(0).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$2 */
    class C03242 implements OnClickListener {
        C03242() {
        }

        public void onClick(View view) {
            deleteData.this.onCreateDialog(1).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$3 */
    class C03263 implements OnClickListener {

        /* renamed from: com.example.android.voltageapp.deleteData$3$1 */
        class C03251 implements OnTimeSetListener {
            C03251() {
            }

            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                EditText editText;
                StringBuilder stringBuilder;
                if (hourOfDay < 10) {
                    if (minutes < 10) {
                        editText = deleteData.this.timeFrom;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("0");
                        stringBuilder.append(hourOfDay);
                        stringBuilder.append(":0");
                        stringBuilder.append(minutes);
                        stringBuilder.append(":00");
                        editText.setText(stringBuilder.toString());
                        return;
                    }
                    editText = deleteData.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else if (minutes < 10) {
                    editText = deleteData.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":0");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else {
                    editText = deleteData.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                }
            }
        }

        C03263() {
        }

        public void onClick(View view) {
            new TimePickerDialog(deleteData.this, new C03251(), 0, 0, true).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$4 */
    class C03284 implements OnClickListener {

        /* renamed from: com.example.android.voltageapp.deleteData$4$1 */
        class C03271 implements OnTimeSetListener {
            C03271() {
            }

            public void onTimeSet(TimePicker timePicker, int hourOfDay2, int minutes2) {
                EditText editText;
                StringBuilder stringBuilder;
                if (hourOfDay2 < 10) {
                    if (minutes2 < 10) {
                        editText = deleteData.this.timeTo;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("0");
                        stringBuilder.append(hourOfDay2);
                        stringBuilder.append(":0");
                        stringBuilder.append(minutes2);
                        stringBuilder.append(":00");
                        editText.setText(stringBuilder.toString());
                        return;
                    }
                    editText = deleteData.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else if (minutes2 < 10) {
                    editText = deleteData.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":0");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else {
                    editText = deleteData.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                }
            }
        }

        C03284() {
        }

        public void onClick(View view) {
            new TimePickerDialog(deleteData.this, new C03271(), 0, 0, true).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$5 */
    class C03295 implements OnClickListener {
        C03295() {
        }

        public void onClick(View view) {
            deleteData.this.textDateFROM = deleteData.this.dateFrom.getText().toString();
            deleteData.this.textDateTO = deleteData.this.dateTo.getText().toString();
            deleteData.this.textTimeFrom = deleteData.this.timeFrom.getText().toString();
            deleteData.this.textTimeTo = deleteData.this.timeTo.getText().toString();
            if (deleteData.this.textDateFROM.length() == 0 || deleteData.this.textDateTO.length() == 0 || deleteData.this.textTimeFrom.length() == 0 || deleteData.this.textTimeTo.length() == 0) {
                Toast.makeText(deleteData.this.getApplicationContext(), "Please set the time period", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteData.this.conn = new ConexionSQLiteHelper(deleteData.this.getApplicationContext(), Utilidades.TABLA_HEARTRATE, null, 1);
            SQLiteDatabase db = deleteData.this.conn.getWritableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM heartRate WHERE dateTime>=Datetime('");
            query.append(deleteData.this.textDateFROM);
            query.append(deleteData.this.textTimeFrom);
            query.append("') AND ");
            query.append(Utilidades.CAMPO_DATE_TIME);
            query.append("<=Datetime('");
            query.append(deleteData.this.textDateTO);
            query.append(deleteData.this.textTimeTo);
            query.append("')");
            db.execSQL(query.toString());
            Context applicationContext = deleteData.this.getApplicationContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Data from ");
            stringBuilder.append(deleteData.this.textDateFROM);
            stringBuilder.append(deleteData.this.textTimeFrom);
            stringBuilder.append(" to ");
            stringBuilder.append(deleteData.this.textDateTO);
            stringBuilder.append(deleteData.this.textTimeTo);
            stringBuilder.append(" have been deleted");
            Toast.makeText(applicationContext, stringBuilder.toString(), 0).show();
            db.close();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$6 */
    class C03306 implements OnDateSetListener {
        C03306() {
        }

        public void onDateSet(DatePicker datePicker, int year2, int monthOfYear2, int dayOfMonth2) {
            deleteData.this.mYearTo = year2;
            deleteData.this.mMonthTo = monthOfYear2;
            deleteData.this.mDayTo = dayOfMonth2;
            deleteData.this.colocar_fecha2();
        }
    }

    /* renamed from: com.example.android.voltageapp.deleteData$7 */
    class C03317 implements OnDateSetListener {
        C03317() {
        }

        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            deleteData.this.mYearFrom = year;
            deleteData.this.mMonthFrom = monthOfYear;
            deleteData.this.mDayFrom = dayOfMonth;
            deleteData.this.colocar_fecha1();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_delete_data);
        this.sMonthFrom = this.f43C.get(2);
        this.sDayFrom = this.f43C.get(5);
        this.sYearFrom = this.f43C.get(1);
        this.sMonthTo = this.C2.get(2);
        this.sDayTo = this.C2.get(5);
        this.sYearTo = this.C2.get(1);
        this.dateFrom = (EditText) findViewById(R.id.dateFrom);
        this.timeFrom = (EditText) findViewById(R.id.timeFrom);
        this.dateTo = (EditText) findViewById(R.id.dateTo);
        this.timeTo = (EditText) findViewById(R.id.timeTo);
        this.dateFrom.setInputType(0);
        this.dateTo.setInputType(0);
        this.timeFrom.setInputType(0);
        this.timeTo.setInputType(0);
        this.dateFrom.setOnClickListener(new C03231());
        this.dateTo.setOnClickListener(new C03242());
        this.timeFrom.setOnClickListener(new C03263());
        this.timeTo.setOnClickListener(new C03284());
        this.deletedata = (Button) findViewById(R.id.deleteData);
        this.deletedata.setOnClickListener(new C03295());
    }

    private void colocar_fecha1() {
        EditText editText = this.dateFrom;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.mMonthFrom + 1 <= 9) {
            stringBuilder.append(this.mYearFrom);
            stringBuilder.append("-0");
            stringBuilder.append(this.mMonthFrom + 1);
            stringBuilder.append("-");
            stringBuilder.append(this.mDayFrom);
            stringBuilder.append(" ");
            editText.setText(stringBuilder.toString());
            return;
        }
        editText = this.dateFrom;
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.mYearFrom);
        stringBuilder.append("-");
        stringBuilder.append(this.mMonthFrom + 1);
        stringBuilder.append("-");
        stringBuilder.append(this.mDayFrom);
        stringBuilder.append(" ");
        editText.setText(stringBuilder.toString());
    }

    private void colocar_fecha2() {
        EditText editText = this.dateTo;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.mMonthTo + 1 <= 9) {
            stringBuilder.append(this.mYearTo);
            stringBuilder.append("-0");
            stringBuilder.append(this.mMonthTo + 1);
            stringBuilder.append("-");
            stringBuilder.append(this.mDayTo);
            stringBuilder.append(" ");
            editText.setText(stringBuilder.toString());
            return;
        }
        editText = this.dateTo;
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.mYearTo);
        stringBuilder.append("-");
        stringBuilder.append(this.mMonthTo + 1);
        stringBuilder.append("-");
        stringBuilder.append(this.mDayTo);
        stringBuilder.append(" ");
        editText.setText(stringBuilder.toString());
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, this.getmDateSetListener1, this.sYearFrom, this.sMonthFrom, this.sDayFrom);
            case 1:
                return new DatePickerDialog(this, this.mDateSetListener2, this.sYearTo, this.sMonthTo, this.sDayTo);
            default:
                return null;
        }
    }
}
