package com.example.android.voltageapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.voltageapp.entidades.HeartRate;
import com.example.android.voltageapp.entidades.Utilidades.Utilidades;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Tab2Historicos extends Fragment implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private double mEnergy;
    private double rawEnergy;
    private double mHarvestedEnergy;
    TextView tvEnergy;
    TextView tvHarvestedEnergy;

    EditText dateTo;
    EditText dateFrom;
    EditText timeFrom;
    EditText timeTo;
    Button search, graphButton;
    private int mYearFrom, mMonthFrom, mDayFrom, sYearFrom, sMonthFrom, sDayFrom;
    private int mYearTo, mMonthTo, mDayTo, sYearTo, sMonthTo, sDayTo;
    static final int DATE_ID1 = 0;
    static final int DATE_ID2 = 1;

    Calendar C = Calendar.getInstance();
    Calendar C2 = Calendar.getInstance();

    //    LineGraphSeries<DataPoint> series;
//    GraphView graph;
    ArrayList arrayHora = new ArrayList<String>();
    ArrayList arrayTrama = new ArrayList<String>();

    String textDateTO;
    String textDateFROM;
    String textTimeFrom;
    String textTimeTo;

    // Ola ola k tal
    // declaracion de los arraylists
    ListView listViewPersonas;
    ArrayList<String> listaInformacion;
    ArrayList<HeartRate> listaHeartRate;
    ArrayList<HeartRate> listaTrama;
    ArrayList<Double> listaTramaDouble;
//    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    Float capacitor = Float.parseFloat("10");

    //Here starts the LineChart Code
    ConexionSQLiteHelper conn;

    ImageButton deleteData;

    private static final String TAG = "Tab2Historicos";
    private LineChart mChart;

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureStart: X: "+me.getX() + "Y: " +me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureEnd: " + lastPerformedGesture);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i(TAG,"onChartLongPressed: ");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i(TAG, "onChartDoubleTapped");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i(TAG, "onChartSingleTapped");

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i(TAG, "onChartFling: veloX: " + velocityX + "valoY: "+velocityY);

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i(TAG, "onChartScale: ScaleX: " +scaleX + "ScaleY: " + scaleY);

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i(TAG, "onChartTranslate: dX: "+dX + "dY: " + dY);

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "onValueSelected: " + e.toString());

    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected");

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2historicos, container, false);

        tvHarvestedEnergy = (TextView) rootView.findViewById(R.id.tv_harvested_energy);

        //From Calendar
        sMonthFrom = C.get(Calendar.MONTH);
        sDayFrom = C.get(Calendar.DAY_OF_MONTH);
        sYearFrom = C.get(Calendar.YEAR);

        //To Calendar
        sMonthTo = C2.get(Calendar.MONTH);
        sDayTo = C2.get(Calendar.DAY_OF_MONTH);
        sYearTo = C2.get(Calendar.YEAR);

        //From Date EditText onClick
        dateFrom = (EditText) rootView.findViewById(R.id.textViewFrom);
        dateFrom.setInputType(InputType.TYPE_NULL);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDialog(DATE_ID1).show();
            }
        });

        // To Date EditText OnClick
        dateTo = (EditText) rootView.findViewById(R.id.textViewTo);
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDialog(DATE_ID2).show();
            }
        });



        //Time From
        timeFrom = (EditText) rootView.findViewById(R.id.timeini);
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

        //Time To
        timeTo = (EditText) rootView.findViewById(R.id.timeFin);
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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



        //GRAFICA 1, PARAMETROS INICIALES PREVIO A BÚSQUEDA - VOLTAJE
        mChart = (LineChart) rootView.findViewById(R.id.lineChart);
        mChart.setOnChartGestureListener(Tab2Historicos.this);
        mChart.setOnChartValueSelectedListener(Tab2Historicos.this);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setNoDataText("Seleccione un Intervalo de Tiempo");
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.setAxisMaximum(3.3f);
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f,10f,0f);
        yAxis.setDrawLimitLinesBehindData(true);
        ArrayList<Entry> yvalues = new ArrayList<>();
        yvalues.add(new Entry(0,5f));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet set1 = new LineDataSet(yvalues, "Generated Voltage [V]");
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.getAxisRight().setEnabled(false);
        set1.setColor(Color.TRANSPARENT);
        set1.setCircleColor(Color.TRANSPARENT);
        set1.setValueTextColor(Color.TRANSPARENT);
        mChart.getDescription().setText("Generated Voltage [V]");

        search = rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conn = new ConexionSQLiteHelper(getActivity(), "heartRate", null, 1);

                listViewPersonas = (ListView) rootView.findViewById(R.id.listViewPersonas);


                databaseQuery();

                ArrayAdapter adaptador = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listaInformacion);
                adaptador.notifyDataSetChanged();
                listViewPersonas.setAdapter(adaptador);
                for (int i = 0; i < listaHeartRate.size(); i++) {
                    arrayTrama.add(listaHeartRate.get(i).getTrama());
                    arrayHora.add(listaHeartRate.get(i).getHora());
                }

                listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        String informacion = "id: " + listaHeartRate.get(pos).getId() + "\n";
                        informacion += "Fecha: " + listaHeartRate.get(pos).getFecha() + "\n";
                        informacion += "Hora: " + listaHeartRate.get(pos).getHora() + "\n";
                        informacion += "Heart Rate: " + listaHeartRate.get(pos).getTrama() + "(lpm)" + "\n";
                        Toast.makeText(getActivity(), informacion, Toast.LENGTH_SHORT).show();
                        HeartRate user = listaHeartRate.get(pos);
                    }
                });

                Log.w("INFO", "Fecha: "+textDateFROM);
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_TRAMA + "," + Utilidades.CAMPO_HORA + "," +
                        Utilidades.CAMPO_POTENCIA + "," + Utilidades.CAMPO_DATE_TIME + " FROM " + Utilidades.TABLA_HEARTRATE +
                        " WHERE " + Utilidades.CAMPO_DATE_TIME + " >= Datetime('" + textDateFROM + textTimeFrom + "') AND " +
                        Utilidades.CAMPO_DATE_TIME + "<= Datetime('" + textDateTO + textTimeTo + "')", null);
                ArrayList<Entry> yvalues = new ArrayList<>();
                ArrayList<String> valuesList = new ArrayList<>();
                ArrayList<Entry> yvalues2 = new ArrayList();
                ArrayList<String> yvaluesString = new ArrayList();
                ArrayList<String> yvalues2String = new ArrayList();
                ArrayList<String> valuesList2 = new ArrayList();
                ArrayList<String> dateTime = new ArrayList();


                while (cursor.moveToNext()) {
                    yvalues.add(new Entry(cursor.getPosition(), cursor.getFloat(0)));
                    valuesList.add(cursor.getString(1));
                    yvaluesString.add(cursor.getString(0));
                    yvalues2.add(new Entry(cursor.getPosition(), cursor.getFloat(2)));
                    yvalues2String.add(cursor.getString(2));
                    valuesList2.add(cursor.getString(1));
                    dateTime.add(cursor.getString(3));
                }
//                Log.w(TAG, "Array de potencia"+yvalues2);

                String[] values = valuesList.toArray(new String[valuesList.size()]);
                String[] values2 = valuesList2.toArray(new String[valuesList.size()]);
                String[] yAxisValues = yvalues2String.toArray(new String[yvalues2String.size()]);
                String[] yAxisVoltage = yvaluesString.toArray(new String[yvaluesString.size()]);
                String[] dateTimeValues = dateTime.toArray(new String[dateTime.size()]);
                Float[] yVoltageFloat = convertStringArraytoFloatArray(yAxisVoltage);
                Float[] yPowerFloat = convertStringArraytoFloatArray(yAxisValues);
                Float maxVoltage;

                if (!yvaluesString.isEmpty()) {
                    maxVoltage = Float.parseFloat(Collections.max(yvaluesString)) + Float.parseFloat("0.1");
                } else {
                    maxVoltage = Float.parseFloat("3");
                }

                XAxis xAxis = mChart.getXAxis();
                int i1 = 0; mEnergy = 0; rawEnergy = 0;
                mHarvestedEnergy = 0;
                Log.w("INFO",yvaluesString.toString() );
                Float pastVoltage;
                Float currentVoltage;
                while (i1 < values.length) {
                    boolean z = true;
                    if (values[i1] != null) {
                        //GRAFICA 1
                        LineDataSet set1 = new LineDataSet(yvalues, "Generated Voltage [V]");
                        set1.setFillAlpha(110);
                        set1.setColor(Color.RED);
                        set1.setLineWidth(2f);
                        YAxis yAxis = mChart.getAxisLeft();
                        yAxis.setAxisMaximum(maxVoltage);
                        yAxis.setAxisMinimum(0.0f);
                        set1.setCircleColor(Color.RED);
                        set1.setValueTextSize(10f);
                        set1.setValueTextColor(Color.RED);
                        set1.setDrawFilled(true);
                        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
                        set1.setFillDrawable(drawable);
                        mChart.setVisibleXRangeMaximum(8);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);
                        LineData data = new LineData(dataSets);
                        mChart.setData(data);
                        mChart.getDescription().setEnabled(true);
                        xAxis.setGranularity(1);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelRotationAngle(-45.0f);
                        if(i1 == 0){
                            pastVoltage = Float.parseFloat("0");
                        }else{
                            pastVoltage = yVoltageFloat[i1-1];
                        }
                        currentVoltage = yVoltageFloat[i1];
                        rawEnergy = 0.5*capacitor*(Math.pow(currentVoltage, 2) - Math.pow(pastVoltage,2 ));
                        if(rawEnergy < 0){
                            if(currentVoltage > Float.parseFloat("3.75")){
                                Log.w("INFO","mEnergy: "+mEnergy);
                                mEnergy = mEnergy + Math.abs(rawEnergy);
                            }
                        }else{
                            mHarvestedEnergy = mHarvestedEnergy + rawEnergy;
                        }
                    } else {
                        YAxis yAxis = mChart.getAxisLeft();
                        yAxis.removeAllLimitLines();
                        yAxis.setAxisMaximum(3.3f);
                        yAxis.setAxisMinimum(0.0f);
                        yAxis.enableGridDashedLine(10f, 10f, 0f);
                        yAxis.setDrawLimitLinesBehindData(true);
                        ArrayList<Entry> yvaluesNull = new ArrayList<>();
                        yvalues.add(new Entry(0, 5.0f));
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        LineDataSet set1 = new LineDataSet(yvaluesNull, "Generated Voltage [V]");
                        dataSets.add(set1);
                        LineData data = new LineData(dataSets);
                        mChart.setData(data);
                        mChart.getAxisRight().setEnabled(false);
                        set1.setColor(Color.TRANSPARENT);
                        set1.setCircleColor(Color.TRANSPARENT);
                        set1.setValueTextColor(Color.TRANSPARENT);
                    }
                    i1++;
                    z = true;
                }
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

                if (!yvaluesString.isEmpty()){
                    mChart.getDescription().setText("Max Voltage [V] = " + String.valueOf(maxVoltage));
                }else {
                    mChart.getDescription().setText("Generated Voltage [V]");
                }
                String partialEnergy = String.format("%.2f", mEnergy);
                String totalEnergy = "Transfered Energy [uJ]: " + partialEnergy;
                Log.w("ENERGY:", totalEnergy);
                tvEnergy = (TextView) rootView.findViewById(R.id.tv_energy);
                tvEnergy.setText(totalEnergy);
                String partialHarvestedEnergy = String.format("%.2f", mHarvestedEnergy);
                String totalHarvestedEnergy = "Harvested Energy [uJ]: " + partialHarvestedEnergy;
                tvHarvestedEnergy.setText(String.valueOf(totalHarvestedEnergy));
                db.close();
                cursor.close();
            }
        });

        deleteData = (ImageButton) rootView.findViewById(R.id.delete);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), deleteData.class);
                startActivity(intent);
            }
        });
        return rootView;

    }
    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values){
            this.mValues= values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int intValue = (int) value;
            if (mValues.length>intValue && intValue>=0){
                return mValues[intValue];
            }else{
                return "";
            }

        }

    }


    private ArrayList<HeartRate> obteneryvalues() {

        textDateTO = dateTo.getText().toString();
        textDateFROM = dateFrom.getText().toString();
        textTimeFrom = timeFrom.getText().toString();
        textTimeTo = timeTo.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();

        HeartRate heartRate = null;
        ArrayList<HeartRate> yvalues = new ArrayList<HeartRate>();
        //select * from usuarios
        Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_TRAMA+" FROM "
                + Utilidades.TABLA_HEARTRATE + " WHERE " + Utilidades.CAMPO_DATE_TIME
                + " >= Datetime('" + textDateFROM + textTimeFrom + "') AND "
                + Utilidades.CAMPO_DATE_TIME + "<= Datetime('" + textDateTO
                + textTimeTo + "')", null);

        try {


            while (cursor.moveToNext()) {
                heartRate = new HeartRate();
                heartRate.setTrama(cursor.getString(0));
                yvalues.add(heartRate);

            }
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        db.close();
        return yvalues;
    }



    private void databaseQuery() {

        textDateTO = dateTo.getText().toString();
        textDateFROM = dateFrom.getText().toString();
        textTimeFrom = timeFrom.getText().toString();
        textTimeTo = timeTo.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();

        HeartRate heartRate = null;
        listaHeartRate = new ArrayList<HeartRate>();
        //select * from usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_HEARTRATE + " WHERE " + Utilidades.CAMPO_DATE_TIME + " >= Datetime('" + textDateFROM + textTimeFrom + "') AND " + Utilidades.CAMPO_DATE_TIME + "<= Datetime('" + textDateTO + textTimeTo + "')", null);

        try {
            while (cursor.moveToNext()) {
                heartRate = new HeartRate();
                heartRate.setId(cursor.getInt(0));
                heartRate.setFecha(cursor.getString(1));
                heartRate.setHora(cursor.getString(2));
                heartRate.setTrama(cursor.getString(3));
                heartRate.setDateTime(cursor.getString(4));
                listaHeartRate.add(heartRate);
            }
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        obtenerLista();
        db.close();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();

        for (int i = 0; i < listaHeartRate.size(); i++) {
            listaInformacion.add(listaHeartRate.get(i).getFecha() + " - "
                    + listaHeartRate.get(i).getHora() + " - "
                    + listaHeartRate.get(i).getTrama());
        }

    }

    private void colocar_fecha1() {
        String dayFrom = "";
        Log.w("INFO","mDayFrom: "+mDayFrom);
        if(mDayFrom < 10){
            dayFrom = "0"+String.valueOf(mDayFrom);
        }else{
            dayFrom = String.valueOf(mDayFrom);
        }
        if (mMonthFrom + 1 <= 9) {
            dateFrom.setText(mYearFrom + "-0" + (mMonthFrom + 1) + "-" + dayFrom + " ");
        } else {
            Log.w("INFO","dayFrom: "+dayFrom);
            dateFrom.setText(mYearFrom + "-" + (mMonthFrom + 1) + "-" + dayFrom + " ");
        }
    }

    private void colocar_fecha2() {
        String dayTo = "";
        if(mDayTo < 10){
            dayTo = "0"+String.valueOf(mDayTo);
        }else{
            dayTo = String.valueOf(mDayTo);
        }
        if (mMonthTo + 1 <= 9) {
            dateTo.setText(mYearTo + "-0" + (mMonthTo + 1) + "-" + dayTo + " ");
        } else {
            dateTo.setText(mYearTo + "-" + (mMonthTo + 1) + "-" + dayTo + " ");
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
                return new DatePickerDialog(getActivity(), getmDateSetListener1, sYearFrom, sMonthFrom, sDayFrom);
            case DATE_ID2:
                return new DatePickerDialog(getActivity(), mDateSetListener2, sYearTo, sMonthTo, sDayTo);
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public Float[] convertStringArraytoFloatArray(String[] num) {
        if (num != null) {

            Float floatarray[] = new Float[num.length];

            for (int i = 0; i <num.length; i++) {
                floatarray[i] = Float.parseFloat(num[i]);
            }
            return floatarray;
        }else {
            return null;
        }
    }

}

