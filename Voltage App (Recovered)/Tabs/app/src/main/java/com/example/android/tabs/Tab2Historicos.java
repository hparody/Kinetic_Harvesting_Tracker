package com.example.android.tabs;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.android.tabs.entidades.HeartRate;
import com.example.android.tabs.Utilidades.Utilidades;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tab2Historicos extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {
    static final int DATE_ID1 = 0;
    static final int DATE_ID2 = 1;
    private static final String TAG = "Tab2Historicos";
    /* renamed from: C */
    Calendar f42C = Calendar.getInstance();
    Calendar C2 = Calendar.getInstance();
    ArrayList arrayHora = new ArrayList();
    ArrayList arrayTrama = new ArrayList();
    ConexionSQLiteHelper conn;
    EditText dateFrom;
    EditText dateTo;
    ImageButton deleteData;
    private OnDateSetListener getmDateSetListener1 = new C03228();
    Button graphButton;
    ListView listViewPersonas;
    ArrayList<HeartRate> listaHeartRate;
    ArrayList<String> listaInformacion;
    ArrayList<HeartRate> listaTrama;
    ArrayList<Double> listaTramaDouble;
    private TextView tvEnergy;
    private LineChart mChart;
    private LineChart mChart2;
    private OnDateSetListener mDateSetListener2 = new C03217();
    private double mEnergy;
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
    Button search;
    String textDateFROM;
    String textDateTO;
    String textTimeFrom;
    String textTimeTo;
    EditText timeFrom;
    EditText timeTo;

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$1 */
    class C03121 implements OnClickListener {
        C03121() {
        }

        public void onClick(View view) {
            Tab2Historicos.this.onCreateDialog(0).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$2 */
    class C03132 implements OnClickListener {
        C03132() {
        }

        public void onClick(View view) {
            Tab2Historicos.this.onCreateDialog(1).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$3 */
    class C03153 implements OnClickListener {

        /* renamed from: com.example.android.voltageapp.Tab2Historicos$3$1 */
        class C03141 implements OnTimeSetListener {
            C03141() {
            }

            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                EditText editText;
                StringBuilder stringBuilder;
                if (hourOfDay < 10) {
                    if (minutes < 10) {
                        editText = Tab2Historicos.this.timeFrom;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("0");
                        stringBuilder.append(hourOfDay);
                        stringBuilder.append(":0");
                        stringBuilder.append(minutes);
                        stringBuilder.append(":00");
                        editText.setText(stringBuilder.toString());
                        return;
                    }
                    editText = Tab2Historicos.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else if (minutes < 10) {
                    editText = Tab2Historicos.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":0");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else {
                    editText = Tab2Historicos.this.timeFrom;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                }
            }
        }

        C03153() {
        }

        public void onClick(View view) {
            new TimePickerDialog(Tab2Historicos.this.getActivity(), new C03141(), 0, 0, true).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$4 */
    class C03174 implements OnClickListener {

        /* renamed from: com.example.android.voltageapp.Tab2Historicos$4$1 */
        class C03161 implements OnTimeSetListener {
            C03161() {
            }

            public void onTimeSet(TimePicker timePicker, int hourOfDay2, int minutes2) {
                EditText editText;
                StringBuilder stringBuilder;
                if (hourOfDay2 < 10) {
                    if (minutes2 < 10) {
                        editText = Tab2Historicos.this.timeTo;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("0");
                        stringBuilder.append(hourOfDay2);
                        stringBuilder.append(":0");
                        stringBuilder.append(minutes2);
                        stringBuilder.append(":00");
                        editText.setText(stringBuilder.toString());
                        return;
                    }
                    editText = Tab2Historicos.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else if (minutes2 < 10) {
                    editText = Tab2Historicos.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":0");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                } else {
                    editText = Tab2Historicos.this.timeTo;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourOfDay2);
                    stringBuilder.append(":");
                    stringBuilder.append(minutes2);
                    stringBuilder.append(":00");
                    editText.setText(stringBuilder.toString());
                }
            }
        }

        C03174() {
        }

        public void onClick(View view) {
            new TimePickerDialog(Tab2Historicos.this.getActivity(), new C03161(), 0, 0, true).show();
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$6 */
    class C03206 implements OnClickListener {
        C03206() {
        }

        public void onClick(View view) {
            Tab2Historicos.this.startActivity(new Intent(Tab2Historicos.this.getActivity(), deleteData.class));
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$7 */
    class C03217 implements OnDateSetListener {
        C03217() {
        }

        public void onDateSet(DatePicker datePicker, int year2, int monthOfYear2, int dayOfMonth2) {
            Tab2Historicos.this.mYearTo = year2;
            Tab2Historicos.this.mMonthTo = monthOfYear2;
            Tab2Historicos.this.mDayTo = dayOfMonth2;
            Tab2Historicos.this.colocar_fecha2();
        }
    }

    /* renamed from: com.example.android.voltageapp.Tab2Historicos$8 */
    class C03228 implements OnDateSetListener {
        C03228() {
        }

        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Tab2Historicos.this.mYearFrom = year;
            Tab2Historicos.this.mMonthFrom = monthOfYear;
            Tab2Historicos.this.mDayFrom = dayOfMonth;
            Tab2Historicos.this.colocar_fecha1();
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        public String getFormattedValue(float value, AxisBase axis) {
            int intValue = (int) value;
            if (this.mValues.length <= intValue || intValue < 0) {
                return "";
            }
            return this.mValues[intValue];
        }
    }

    public void onChartGestureStart(MotionEvent me, ChartGesture lastPerformedGesture) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onChartGestureStart: X: ");
        stringBuilder.append(me.getX());
        stringBuilder.append("Y: ");
        stringBuilder.append(me.getY());
        Log.i(str, stringBuilder.toString());
    }

    public void onChartGestureEnd(MotionEvent me, ChartGesture lastPerformedGesture) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onChartGestureEnd: ");
        stringBuilder.append(lastPerformedGesture);
        Log.i(str, stringBuilder.toString());
    }

    public void onChartLongPressed(MotionEvent me) {
        Log.i(TAG, "onChartLongPressed: ");
    }

    public void onChartDoubleTapped(MotionEvent me) {
        Log.i(TAG, "onChartDoubleTapped");
    }

    public void onChartSingleTapped(MotionEvent me) {
        Log.i(TAG, "onChartSingleTapped");
    }

    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onChartFling: veloX: ");
        stringBuilder.append(velocityX);
        stringBuilder.append("valoY: ");
        stringBuilder.append(velocityY);
        Log.i(str, stringBuilder.toString());
    }

    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onChartScale: ScaleX: ");
        stringBuilder.append(scaleX);
        stringBuilder.append("ScaleY: ");
        stringBuilder.append(scaleY);
        Log.i(str, stringBuilder.toString());
    }

    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onChartTranslate: dX: ");
        stringBuilder.append(dX);
        stringBuilder.append("dY: ");
        stringBuilder.append(dY);
        Log.i(str, stringBuilder.toString());
    }

    public void onValueSelected(Entry e, Highlight h) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onValueSelected: ");
        stringBuilder.append(e.toString());
        Log.i(str, stringBuilder.toString());
    }

    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2historicos, container, false);
        this.mEnergy = 0;
        tvEnergy = rootView.findViewById(R.id.tv_energy);
        this.sMonthFrom = this.f42C.get(2);
        this.sDayFrom = this.f42C.get(5);
        this.sYearFrom = this.f42C.get(1);
        this.sMonthTo = this.C2.get(2);
        this.sDayTo = this.C2.get(5);
        this.sYearTo = this.C2.get(1);
        this.dateFrom = (EditText) rootView.findViewById(R.id.textViewFrom);
        this.dateFrom.setInputType(0);
        this.dateFrom.setOnClickListener(new C03121());
        this.dateTo = (EditText) rootView.findViewById(R.id.textViewTo);
        this.dateTo.setOnClickListener(new C03132());
        this.timeFrom = (EditText) rootView.findViewById(R.id.timeini);
        this.timeFrom.setOnClickListener(new C03153());
        this.timeTo = (EditText) rootView.findViewById(R.id.timeFin);
        this.timeTo.setOnClickListener(new C03174());
        // LineChart1
        this.mChart = (LineChart) rootView.findViewById(R.id.lineChart);
        this.mChart.setOnChartGestureListener(this);
        this.mChart.setOnChartValueSelectedListener(this);
        this.mChart.setDragEnabled(true);
        this.mChart.setScaleEnabled(true);
        this.mChart.setNoDataText("Seleccione un Intervalo de Tiempo");
        YAxis yAxis = this.mChart.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.setAxisMaximum(4.0f);
        yAxis.setAxisMinimum(0.0f);
        yAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        yAxis.setDrawLimitLinesBehindData(true);
        // LineChart2
        this.mChart2 = (LineChart) rootView.findViewById(R.id.lineChart2);
        this.mChart2.setOnChartGestureListener(this);
        this.mChart2.setOnChartValueSelectedListener(this);
        this.mChart2.setDragEnabled(true);
        this.mChart2.setScaleEnabled(true);
        this.mChart2.setNoDataText("Seleccione un Intervalo de Tiempo");
        YAxis yAxis2 = this.mChart.getAxisLeft();
        yAxis2.removeAllLimitLines();
        yAxis2.setAxisMaximum(30.0f);
        yAxis2.setAxisMinimum(0.0f);
        yAxis2.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        yAxis2.setDrawLimitLinesBehindData(true);
        //
        ArrayList<Entry> yvalues = new ArrayList();
        yvalues.add(new Entry(0.0f, 0.0f));
        List dataSets = new ArrayList();
        List dataSets2 = new ArrayList();
        LineDataSet set1 = new LineDataSet(yvalues, "Voltage [V]");
        LineDataSet set2 = new LineDataSet(yvalues, "Potencia [uW]");
        dataSets.add(set1);
        this.mChart.setData(new LineData(dataSets));
        this.mChart.getAxisRight().setEnabled(false);
        this.mChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        this.mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
        this.mChart.getDescription().setText("");
        dataSets2.add(set2);
        this.mChart2.setData(new LineData(dataSets2));
        this.mChart2.getAxisRight().setEnabled(false);
        this.mChart2.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        this.mChart2.getXAxis().setPosition(XAxisPosition.BOTTOM);
        this.mChart2.getDescription().setText("");
        set1.setColor(0);
        set1.setCircleColor(0);
        set1.setValueTextColor(0);
        set2.setColor(0);
        set2.setCircleColor(0);
        set2.setValueTextColor(0);
        this.search = (Button) rootView.findViewById(R.id.search);
        this.search.setOnClickListener(new OnClickListener() {

            /* renamed from: com.example.android.voltageapp.Tab2Historicos$5$1 */
            class C03181 implements OnItemClickListener {
                C03181() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    StringBuilder informacion = new StringBuilder();
                    informacion.append("id: ");
                    informacion.append(((HeartRate) Tab2Historicos.this.listaHeartRate.get(pos)).getId());
                    informacion.append("\n");
                    // String informacion = informacion.toString();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(informacion);
                    stringBuilder.append("Fecha: ");
                    stringBuilder.append(((HeartRate) Tab2Historicos.this.listaHeartRate.get(pos)).getFecha());
                    stringBuilder.append("\n");
                    // informacion = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(informacion);
                    stringBuilder.append("Hora: ");
                    stringBuilder.append(((HeartRate) Tab2Historicos.this.listaHeartRate.get(pos)).getHora());
                    stringBuilder.append("\n");
                    // informacion = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(informacion);
                    stringBuilder.append("Voltaje [V]: ");
                    stringBuilder.append(((HeartRate) Tab2Historicos.this.listaHeartRate.get(pos)).getTrama());
                    stringBuilder.append("(lpm)\n");
                    Toast.makeText(Tab2Historicos.this.getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    HeartRate user = (HeartRate) Tab2Historicos.this.listaHeartRate.get(pos);
                }
            }

            public void onClick(View view) {
                Log.w("INFO", "Searching Voltage");
                boolean z = true;
                Tab2Historicos.this.conn = new ConexionSQLiteHelper(Tab2Historicos.this.getActivity(), Utilidades.TABLA_HEARTRATE, null, 1);
                Tab2Historicos.this.listViewPersonas = (ListView) rootView.findViewById(R.id.listViewPersonas);
                Tab2Historicos.this.consultarListaPersonas();
                ArrayAdapter adaptador = new ArrayAdapter(Tab2Historicos.this.getActivity(), 17367043, Tab2Historicos.this.listaInformacion);
                adaptador.notifyDataSetChanged();
                Tab2Historicos.this.listViewPersonas.setAdapter(adaptador);
                for (int i = 0; i < Tab2Historicos.this.listaHeartRate.size(); i++) {
                    Tab2Historicos.this.arrayTrama.add(((HeartRate) Tab2Historicos.this.listaHeartRate.get(i)).getTrama());
                    Tab2Historicos.this.arrayHora.add(((HeartRate) Tab2Historicos.this.listaHeartRate.get(i)).getHora());
                }
                Tab2Historicos.this.listViewPersonas.setOnItemClickListener(new C03181());
                SQLiteDatabase db = Tab2Historicos.this.conn.getReadableDatabase();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("SELECT trama,hora FROM heartRate WHERE dateTime >= Datetime('");
                stringBuilder.append(Tab2Historicos.this.textDateFROM);
                stringBuilder.append(Tab2Historicos.this.textTimeFrom);
                stringBuilder.append("') AND ");
                stringBuilder.append(Utilidades.CAMPO_DATE_TIME);
                stringBuilder.append("<= Datetime('");
                stringBuilder.append(Tab2Historicos.this.textDateTO);
                stringBuilder.append(Tab2Historicos.this.textTimeTo);
                stringBuilder.append("')");
                Cursor cursor = db.rawQuery(stringBuilder.toString(), null);
                ArrayList<Entry> yvalues = new ArrayList();
                ArrayList<String> valuesList = new ArrayList();
                while (cursor.moveToNext()) {
                    yvalues.add(new Entry((float) cursor.getPosition(), (float) cursor.getDouble(0)));
                    valuesList.add(cursor.getString(1));
                }
                String[] values = (String[]) valuesList.toArray(new String[valuesList.size()]);
                XAxis xAxis = Tab2Historicos.this.mChart.getXAxis();
                int i2 = 0;
                while (i2 < values.length) {
                    List dataSets;
                    if (values[i2] != null) {
                        LineDataSet set1 = new LineDataSet(yvalues, "Voltage [V]");
                        set1.setFillAlpha(110);
                        set1.setColor(SupportMenu.CATEGORY_MASK);
                        set1.setLineWidth(2.0f);
                        YAxis yAxis = Tab2Historicos.this.mChart.getAxisLeft();
                        yAxis.setAxisMaximum(4.0f);
                        yAxis.setAxisMinimum(0.0f);
                        set1.setCircleColor(SupportMenu.CATEGORY_MASK);
                        set1.setValueTextSize(10.0f);
                        set1.setValueTextColor(SupportMenu.CATEGORY_MASK);
                        set1.setDrawFilled(true);
                        Drawable drawable = ContextCompat.getDrawable(Tab2Historicos.this.getActivity(), R.drawable.fade_red);
                        set1.setFillDrawable(drawable);
                        dataSets = new ArrayList();
                        dataSets.add(set1);
                        Tab2Historicos.this.mChart.setData(new LineData(dataSets));
                        xAxis.setGranularity(1.0f);
                        xAxis.setPosition(XAxisPosition.BOTTOM);
                        xAxis.setLabelRotationAngle(-45.0f);
                    } else {
                        YAxis yAxis2 = Tab2Historicos.this.mChart.getAxisLeft();
                        yAxis2.removeAllLimitLines();
                        yAxis2.setAxisMaximum(4.0f);
                        yAxis2.setAxisMinimum(0.0f);
                        yAxis2.enableGridDashedLine(10.0f, 10.0f, 0.0f);
                        yAxis2.setDrawLimitLinesBehindData(z);
                        ArrayList<Entry> yvaluesNull = new ArrayList();
                        yvalues.add(new Entry(0.0f, 60.0f));
                        dataSets = new ArrayList();
                        LineDataSet set12 = new LineDataSet(yvaluesNull, "Voltage [V]");
                        dataSets.add(set12);
                        Tab2Historicos.this.mChart.setData(new LineData(dataSets));
                        Tab2Historicos.this.mChart.getAxisRight().setEnabled(false);
                        set12.setColor(0);
                        set12.setCircleColor(0);
                        set12.setValueTextColor(0);
                    }
                    i2++;
                    z = true;
                }
                Tab2Historicos.this.mChart.notifyDataSetChanged();
                Tab2Historicos.this.mChart.invalidate();
                mChart.getDescription().setText("Generated Voltage");
                db.close();
                cursor.close();
                xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
                mChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                /////////////////////////////////////////////////////////////////////////////////
                Log.w("INFO", "Searching Power");
                boolean x = true;
                Tab2Historicos.this.conn = new ConexionSQLiteHelper(Tab2Historicos.this.getActivity(), Utilidades.TABLA_HEARTRATE, null, 1);
                Tab2Historicos.this.listViewPersonas = (ListView) rootView.findViewById(R.id.listViewPersonas);
                Tab2Historicos.this.consultarListaPersonas();
                ArrayAdapter adaptador2 = new ArrayAdapter(Tab2Historicos.this.getActivity(), 17367043, Tab2Historicos.this.listaInformacion);
                adaptador2.notifyDataSetChanged();
                Tab2Historicos.this.listViewPersonas.setAdapter(adaptador2);
                for (int i = 0; i < Tab2Historicos.this.listaHeartRate.size(); i++) {
                    Tab2Historicos.this.arrayTrama.add(((HeartRate) Tab2Historicos.this.listaHeartRate.get(i)).getTrama());
                    Tab2Historicos.this.arrayHora.add(((HeartRate) Tab2Historicos.this.listaHeartRate.get(i)).getHora());
                }
                Tab2Historicos.this.listViewPersonas.setOnItemClickListener(new C03181());
                SQLiteDatabase db2 = Tab2Historicos.this.conn.getReadableDatabase();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("SELECT potencia,hora,dateTime FROM heartRate WHERE dateTime >= Datetime('");
                stringBuilder2.append(Tab2Historicos.this.textDateFROM);
                stringBuilder2.append(Tab2Historicos.this.textTimeFrom);
                stringBuilder2.append("') AND ");
                stringBuilder2.append(Utilidades.CAMPO_DATE_TIME);
                stringBuilder2.append("<= Datetime('");
                stringBuilder2.append(Tab2Historicos.this.textDateTO);
                stringBuilder2.append(Tab2Historicos.this.textTimeTo);
                stringBuilder2.append("')");
                Cursor cursor2 = db2.rawQuery(stringBuilder2.toString(), null);
                ArrayList<Entry> yvalues2 = new ArrayList();
                ArrayList<String> valuesList2 = new ArrayList();
                ArrayList<String> yvalues2List = new ArrayList<>();
                ArrayList<String> dateTimeList = new ArrayList<>();
                while (cursor2.moveToNext()) {
                    yvalues2.add(new Entry((float) cursor2.getPosition(), (float) cursor2.getDouble(0)));
                    yvalues2List.add(cursor2.getString(0));
                    valuesList2.add(cursor2.getString(1));
                    dateTimeList.add(cursor2.getString(2));
                }
                String[] values2 = (String[]) valuesList2.toArray(new String[valuesList2.size()]);
                String[] yAxisValues = (String[]) yvalues2List.toArray(new String[yvalues2List.size()]);
                String[] dateTimeValues = (String[]) dateTimeList.toArray(new String[dateTimeList.size()]);
                XAxis xAxis2 = Tab2Historicos.this.mChart2.getXAxis();
                int i2_2 = 0; mEnergy = 0;
                while (i2_2 < values2.length) {
                    List dataSets2;
                    if (values2[i2_2] != null) {
                        LineDataSet set1_2 = new LineDataSet(yvalues2, "Power [uW]");
                        mChart2.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                        set1_2.setFillAlpha(110);
                        set1_2.setColor(R.color.colorAccent);
                        set1_2.setLineWidth(2.0f);
                        YAxis yAxis2 = Tab2Historicos.this.mChart2.getAxisLeft();
                        yAxis2.setAxisMaximum(30.0f);
                        yAxis2.setAxisMinimum(0.0f);
                        set1_2.setCircleColor(R.color.colorAccent);
                        set1_2.setValueTextSize(10.0f);
                        set1_2.setValueTextColor(R.color.colorAccent);
                        set1_2.setDrawFilled(true);
                        Drawable drawable2 = ContextCompat.getDrawable(Tab2Historicos.this.getActivity(), R.drawable.fade_blue);
                        set1_2.setFillDrawable(drawable2);
                        dataSets2 = new ArrayList();
                        dataSets2.add(set1_2);
                        Tab2Historicos.this.mChart2.setData(new LineData(dataSets2));
                        xAxis2.setGranularity(1.0f);
                        xAxis2.setPosition(XAxisPosition.BOTTOM);
                        xAxis2.setLabelRotationAngle(-45.0f);
                        if(i2_2 != 0){
                            String CurrentDate = dateTimeValues[i2_2];
                            String LastDate = dateTimeValues[i2_2-1];
                            SimpleDateFormat sdf_currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat sdf_LastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date_current = null;
                            Date date_last = null;
                            try {
                                date_current = sdf_currentDate.parse(CurrentDate);
                                date_last = sdf_LastDate.parse(LastDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long millisCurrent = date_current.getTime();
                            long millisLast = date_last.getTime();
                            double delta_time = (millisCurrent - millisLast)/1000;
                            double current_power = Double.parseDouble(yAxisValues[i2_2]);
                            double current_energy = delta_time*current_power;
                            mEnergy = mEnergy + current_energy;
                        }
                    } else {
                        YAxis yAxis2 = Tab2Historicos.this.mChart2.getAxisLeft();
                        yAxis2.removeAllLimitLines();
                        yAxis2.setAxisMaximum(30.0f);
                        yAxis2.setAxisMinimum(0.0f);
                        yAxis2.enableGridDashedLine(10.0f, 10.0f, 0.0f);
                        yAxis2.setDrawLimitLinesBehindData(x);
                        ArrayList<Entry> yvaluesNull2 = new ArrayList();
                        yvalues2.add(new Entry(0.0f, 60.0f));
                        dataSets2 = new ArrayList();
                        LineDataSet set12_2 = new LineDataSet(yvaluesNull2, "Power [uW]");
                        dataSets2.add(set12_2);
                        Tab2Historicos.this.mChart2.setData(new LineData(dataSets2));
                        Tab2Historicos.this.mChart2.getAxisRight().setEnabled(false);
                        set12_2.setColor(0);
                        set12_2.setCircleColor(0);
                        set12_2.setValueTextColor(0);
                    }
                    i2_2++;
                    x = true;
                }
                Tab2Historicos.this.mChart2.notifyDataSetChanged();
                Tab2Historicos.this.mChart2.invalidate();
                mChart2.getDescription().setText("Generated Power");
                db2.close();
                cursor2.close();
                xAxis2.setValueFormatter(new MyXAxisValueFormatter(values2));
                String partialEnergy = String.format("%.2f",mEnergy);
                String totalEnergy = "Energy [uJ] = \n" + partialEnergy;
                Log.w("ENERGY:", totalEnergy);
                tvEnergy.setText(totalEnergy);
            }
        });
        this.deleteData = (ImageButton) rootView.findViewById(R.id.delete);
        this.deleteData.setOnClickListener(new C03206());
        return rootView;
    }

    private ArrayList<HeartRate> obteneryvalues() {
        this.textDateTO = this.dateTo.getText().toString();
        this.textDateFROM = this.dateFrom.getText().toString();
        this.textTimeFrom = this.timeFrom.getText().toString();
        this.textTimeTo = this.timeTo.getText().toString();
        SQLiteDatabase db = this.conn.getReadableDatabase();
        ArrayList<HeartRate> yvalues = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT trama FROM heartRate WHERE dateTime >= Datetime('");
        stringBuilder.append(this.textDateFROM);
        stringBuilder.append(this.textTimeFrom);
        stringBuilder.append("') AND ");
        stringBuilder.append(Utilidades.CAMPO_DATE_TIME);
        stringBuilder.append("<= Datetime('");
        stringBuilder.append(this.textDateTO);
        stringBuilder.append(this.textTimeTo);
        stringBuilder.append("')");
        Cursor cursor = db.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                HeartRate heartRate = new HeartRate();
                heartRate.setTrama(cursor.getString(0));
                yvalues.add(heartRate);
            } finally {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            }
        }
        db.close();
        return yvalues;
    }

    private void consultarListaPersonas() {
        this.textDateTO = this.dateTo.getText().toString();
        this.textDateFROM = this.dateFrom.getText().toString();
        this.textTimeFrom = this.timeFrom.getText().toString();
        this.textTimeTo = this.timeTo.getText().toString();
        SQLiteDatabase db = this.conn.getReadableDatabase();
        this.listaHeartRate = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        stringBuilder.append("SELECT * FROM heartRate WHERE dateTime >= Datetime('");
        stringBuilder.append(this.textDateFROM);
        stringBuilder.append(this.textTimeFrom);
        stringBuilder.append("') AND ");
        stringBuilder.append(Utilidades.CAMPO_DATE_TIME);
        stringBuilder.append("<= Datetime('");
        stringBuilder.append(this.textDateTO);
        stringBuilder.append(this.textTimeTo);
        stringBuilder.append("')");
        Cursor cursor = db.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                HeartRate heartRate = new HeartRate();
                heartRate.setId(Integer.valueOf(cursor.getInt(0)));
                heartRate.setFecha(cursor.getString(1));
                heartRate.setHora(cursor.getString(2));
                heartRate.setTrama(cursor.getString(3));
                heartRate.setDateTime(cursor.getString(4));
                heartRate.setPotencia(cursor.getString(5));
                this.listaHeartRate.add(heartRate);
            } finally {
                if (!cursor.isClosed()) {
                    //cursor.close();
                }
            }
        }
        obtenerLista();
        cursor.close();
        db.close();
    }

    private void obtenerLista() {
        this.listaInformacion = new ArrayList();
        for (int i = 0; i < this.listaHeartRate.size(); i++) {
            ArrayList arrayList = this.listaInformacion;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(((HeartRate) this.listaHeartRate.get(i)).getFecha());
            stringBuilder.append(" - ");
            stringBuilder.append(((HeartRate) this.listaHeartRate.get(i)).getHora());
            stringBuilder.append(" - ");
            stringBuilder.append(((HeartRate) this.listaHeartRate.get(i)).getTrama());
            arrayList.add(stringBuilder.toString());
        }
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
                return new DatePickerDialog(getActivity(), this.getmDateSetListener1, this.sYearFrom, this.sMonthFrom, this.sDayFrom);
            case 1:
                return new DatePickerDialog(getActivity(), this.mDateSetListener2, this.sYearTo, this.sMonthTo, this.sDayTo);
            default:
                return null;
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }
}
