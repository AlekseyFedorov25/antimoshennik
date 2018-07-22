package info.forallactivities.main;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.widget.TextViewCompat;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class more_info extends AppCompatActivity {
    String constr = "jdbc:mysql://194.58.118.27:3306/workspace";
    String user = "zabbix";
    String pswd = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        moreInfoFromDB mifdb = new moreInfoFromDB();
        mifdb.execute(id);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("I", "Back Pressed 1");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public static void addLinks(TextView textView, String linkThis, String toThis) {
        Pattern pattern = Pattern.compile(linkThis);
        String scheme = toThis;
        android.text.util.Linkify.addLinks(textView, pattern, scheme, new Linkify.MatchFilter() {
            @Override
            public boolean acceptMatch(CharSequence s, int start, int end) {
                return true;
            }
        }, new Linkify.TransformFilter() {

            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        });
    }

    class moreInfoFromDB extends AsyncTask<String, Void, AdapterHelper> {
        AdapterHelper adh_r;

        @Override
        protected AdapterHelper doInBackground(String... strings) {
            try {
                String[] basics = {"", "", "", ""};
                String[] tels = {"", "", "", ""};
                String[] links = {"", "", "", "", ""};

                int tels_c = 0;
                int links_c = 0;

                Connection con = DriverManager.getConnection(constr, user, pswd);
                Statement st = con.createStatement();
                Statement st2 = con.createStatement();
                Statement st3 = con.createStatement();

                ResultSet rsBasicInfo = st.executeQuery(String.format("select * from people where id = %s;", strings));
                ResultSet rsTels = st2.executeQuery(String.format("select tel from telnumbers where id = %s;", strings));
                ResultSet rsLinks = st3.executeQuery(String.format("select link from links where id = %s;", strings));

                while (rsBasicInfo.next()) {
                    for (int i = 1; i <= 4; i++) basics[i - 1] = rsBasicInfo.getString(i);
                }
                while (rsTels.next()) {
                    tels[tels_c] = rsTels.getString(1);
                    tels_c++;
                }
                while (rsLinks.next()) {
                    links[links_c] = rsLinks.getString(1);
                    links_c++;
                }
                adh_r = new AdapterHelper(basics[0], basics[1], basics[2], basics[3], tels, tels_c, links, links_c);
                return adh_r;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return adh_r;
        }

        @Override
        protected void onPostExecute(AdapterHelper adh) {

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_1);
            Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_2);

            TextView fname_m = findViewById(R.id.fname_m);
            TextView city_m = findViewById(R.id.city_m);
            TextView email_m = findViewById(R.id.email_m);
            TextView tel1_m = findViewById(R.id.tel1_m);
            TextView tel2_m = findViewById(R.id.tel2_m);
            TextView tel3_m = findViewById(R.id.tel3_m);
            LinearLayout link_lay = findViewById(R.id.link_lay);
            TextView[] links = new TextView[5];

            fname_m.setText(adh.getA_f_name());
            city_m.setText(adh.getA_city());
            email_m.setText(adh.getA_e_mail());

            switch (adh.getS_of_atels()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            for (int i = 0; i < adh.getS_of_alinks(); i++) {
                links[i] = new TextView(getApplicationContext());
                System.out.println(adh.getA_links()[i]);
                Pattern pattern = Pattern.compile("[\\/]");
                String toset = pattern.split(adh.getA_links()[i]).length > 1 ? pattern.split(adh.getA_links()[i])[0]+"..." : pattern.split(adh.getA_links()[i])[0];
                links[i].setText(toset);
                addLinks(links[i], toset, "http://"+adh.getA_links()[i]);
                TextViewCompat.setTextAppearance(links[i], R.style.Links);
                stripUnderlines(links[i]);
                link_lay.addView(links[i]);

                switch (i % 2) {
                    case 1:
                        links[i].startAnimation(anim);
                        break;
                    case 0:
                        links[i].startAnimation(anim2);
                        break;
                }

            }
            System.out.println("Hello and here is your info");
        }
    }
}
