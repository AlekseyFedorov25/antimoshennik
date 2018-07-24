package info.forallactivities.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.PersonViewHolder> {
    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView rv;
        TextView personId;
        TextView personName;
        TextView personEmail;
        TextView personCity;
        LinearLayout personTel_lay;

        PersonViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.grid_v);
            personId = (TextView) itemView.findViewById(R.id.idof);
            personName = (TextView) itemView.findViewById(R.id.fname);
            personEmail = (TextView) itemView.findViewById(R.id.email);
            personCity = (TextView) itemView.findViewById(R.id.city);
            personTel_lay = (LinearLayout) itemView.findViewById(R.id.tel_layout);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            TextView id_tv = (TextView) view.findViewById(R.id.idof);
            Context context = main.getAppContext();
            Intent i = new Intent(context.getApplicationContext(), more_info.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("id", id_tv.getText());
            context.startActivity(i);
        }
    }

    List<AdapterHelper> persons;

    Adapter(List<AdapterHelper> persons) {
        this.persons = persons;
    }

    public void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new Adapter.URLSpanNoUnderline(span.getURL());
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


    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tel_layout, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        Animation anim = AnimationUtils.loadAnimation(main.getAppContext(), R.anim.main_1);
        Animation anim2 = AnimationUtils.loadAnimation(main.getAppContext(), R.anim.main_2);

        personViewHolder.personId.setText(persons.get(i).getA_idof());
        personViewHolder.personName.setText(persons.get(i).getA_f_name());
        personViewHolder.personCity.setText(persons.get(i).getA_city());
        personViewHolder.personEmail.setText(persons.get(i).getA_e_mail());
        TextView[] tels = new TextView[5];
        for (int j = 0; j < persons.get(i).getS_of_atels(); j++) {
            tels[j] = new TextView(new ContextThemeWrapper(main.getAppContext(), R.style.Links), null, 0);
            tels[j].setText(persons.get(i).getA_telenums()[j]);
            Linkify.addLinks(tels[j], Linkify.PHONE_NUMBERS);
            stripUnderlines(tels[j]);
            personViewHolder.personTel_lay.addView(tels[j]);
            switch (j % 2) {
                case 1:
                    tels[j].startAnimation(anim);
                    break;
                case 0:
                    tels[j
                            ].startAnimation(anim2);
                    break;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}