package info.forallactivities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;


public class main extends AppCompatActivity implements View.OnClickListener {
    String constr = "jdbc:mysql://194.58.118.27:3306/workspace";
    String user = "zabbix";
    String pswd = "password";
    Button b;
    RecyclerView recycleV;
    EditText et_fname;
    EditText et_city;
    EditText et_email;
    EditText et_tel;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.b_submit);
        recycleV = (RecyclerView) findViewById(R.id.grid_v);
        b.setOnClickListener(this);
        main.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return main.context;
    }

    private final void focusOnView(final NestedScrollView scrV, final View view) {
        scrV.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrV.smoothScrollTo(0, view.getTop());
            }
        }, 100);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_submit:
                final String[] datatoreq = new String[4];
                et_fname = findViewById(R.id.fname_edit);
                et_city = findViewById(R.id.city_edit);
                et_email = findViewById(R.id.email_edit);
                et_tel = findViewById(R.id.tel_edit);
                datatoreq[0] = et_fname.getText().toString().toLowerCase();
                datatoreq[1] = et_city.getText().toString().toLowerCase();
                datatoreq[2] = et_email.getText().toString().toLowerCase();
                datatoreq[3] = et_tel.getText().toString();
                readfromDB readfromdb = new readfromDB();
                readfromdb.execute(datatoreq);

        }
    }

    @Override
    public void onDestroy() {
        System.out.println("Activity destroyed");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        System.out.println("Activity resumed");
        super.onResume();
    }

    @Override
    public void onStop() {
        System.out.println("Activity stopped");
        super.onStop();
    }

    class readfromDB extends AsyncTask<String, Void, ArrayList<AdapterHelper>> {
        Set<String> idsToNextSearch = new HashSet<>();

        protected ArrayList<AdapterHelper> doInBackground(String[] datatorec) {
            ArrayList<AdapterHelper> FromDB = new ArrayList<>();
            for (int p = 0; p < 4; p++) datatorec[p] = "'" + datatorec[p] + "%'";
            try {
                Connection con = DriverManager.getConnection(constr, user, pswd);
                Statement st = con.createStatement();
                Statement st2 = con.createStatement();
                Statement st3 = con.createStatement();

                ResultSet rs = st.executeQuery(String.format("select people.id from people left join telnumbers on telnumbers.id = people.id where lower(fname) like %s and lower(city) like %s and email like %s and telnumbers.tel like %s;", datatorec[0], datatorec[1], datatorec[2], datatorec[3]));

                while (rs.next()) idsToNextSearch.add(rs.getString(1));

                Iterator<String> idsIterator = idsToNextSearch.iterator();
                while (idsIterator.hasNext()) {
                    //System.out.print(idsIterator.next() + " ");
                    String idForNSearch = idsIterator.next();
                    String[] setofTels = new String[4];
                    String basicInfoFromDB[] = {"", "", "", ""};
                    int quantityOfTels = 0;

                    ResultSet rs2 = st2.executeQuery(String.format("select * from people where id = %s;", idForNSearch));
                    while (rs2.next()) {
                        for (int i = 1; i <= 4; ) {
                            basicInfoFromDB[i - 1] = rs2.getString(i);
                            i++;
                        }
                    }

                    ResultSet rs3 = st3.executeQuery(String.format("select tel from telnumbers where id = %s;", idForNSearch));
                    while (rs3.next()) {
                        setofTels[quantityOfTels] = rs3.getString(1);
                        quantityOfTels++;
                    }
                    FromDB.add(new AdapterHelper(basicInfoFromDB[0], basicInfoFromDB[1], basicInfoFromDB[2], basicInfoFromDB[3], setofTels, quantityOfTels));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FromDB;
        }

        protected void onPostExecute(ArrayList<AdapterHelper> fromDB) {
            System.out.println("Hello");
            try {
                final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                final Adapter tAdapter = new Adapter(fromDB);
                recycleV.setLayoutManager(llm);
                recycleV.setNestedScrollingEnabled(false);
                recycleV.setAdapter(tAdapter);
                TextView result = (TextView) findViewById(R.id.result);
                NestedScrollView scrollView = findViewById(R.id.scroll);
                focusOnView(scrollView, result);
                result.setText("Result(s)");

            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast toast = Toast.makeText(getApplicationContext(), "Successful connection to the database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
