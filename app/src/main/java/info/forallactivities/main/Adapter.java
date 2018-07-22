package info.forallactivities.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        TextView tel1;
        TextView tel2;
        TextView tel3;
        PersonViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.grid_v);
            personId = (TextView)itemView.findViewById(R.id.idof);
            personName = (TextView)itemView.findViewById(R.id.fname);
            personEmail= (TextView)itemView.findViewById(R.id.email);
            personCity = (TextView)itemView.findViewById(R.id.city);
            tel1 = (TextView)itemView.findViewById(R.id.tel1);
            tel2 = (TextView)itemView.findViewById(R.id.tel2);
            tel3 = (TextView)itemView.findViewById(R.id.tel3);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            TextView id_tv = (TextView) view.findViewById(R.id.idof);
            Context context = main.getAppContext();
            Intent i = new Intent(context,more_info.class);
            i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            i.putExtra("id",id_tv.getText());
        }
    }
    List<AdapterHelper> persons;
    Adapter(List<AdapterHelper> persons){
        this.persons = persons;
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

    public static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personId.setText(persons.get(i).getA_idof());
        personViewHolder.personName.setText(persons.get(i).getA_f_name());
        personViewHolder.personCity.setText(persons.get(i).getA_city());
        personViewHolder.personEmail.setText(persons.get(i).getA_e_mail());
        more_info mif = new more_info();
        switch (persons.get(i).getS_of_atels()){
            case 1:

                personViewHolder.tel1.setText(persons.get(i).getA_telenums()[0]);
                break;

            case 2:
                personViewHolder.tel1.setText(persons.get(i).getA_telenums()[0]);
                personViewHolder.tel2.setText(persons.get(i).getA_telenums()[1]);
                break;

            case 3:
                personViewHolder.tel1.setText(persons.get(i).getA_telenums()[0]);
                personViewHolder.tel2.setText(persons.get(i).getA_telenums()[1]);
                personViewHolder.tel3.setText(persons.get(i).getA_telenums()[2]);
                break;
        }

        mif.stripUnderlines(personViewHolder.tel1);
        mif.stripUnderlines(personViewHolder.tel2);
        mif.stripUnderlines(personViewHolder.tel3);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}