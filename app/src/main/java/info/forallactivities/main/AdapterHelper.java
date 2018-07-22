package info.forallactivities.main;


import java.util.ArrayList;

public class AdapterHelper {
    private String a_idof;
    private String a_f_name;
    private String a_city;
    private String a_e_mail;
    private String[] a_tels;
    private int s_of_atels;
    private String[] a_links;
    private int s_of_alinks;

    public AdapterHelper(String a_idof, String a_f_name, String a_city, String a_e_mail, String[] a_tels, int s_of_atels) {
        this.a_idof = a_idof;
        this.a_f_name = a_f_name;
        this.a_city = a_city;
        this.a_e_mail = a_e_mail;
        this.a_tels = a_tels;
        this.s_of_atels = s_of_atels;
    }

    public AdapterHelper(String a_idof, String a_f_name, String a_city, String a_e_mail, String[] a_tels, int s_of_atels, String[] a_links, int s_of_alinks) {
        this.a_idof = a_idof;
        this.a_f_name = a_f_name;
        this.a_city = a_city;
        this.a_e_mail = a_e_mail;
        this.a_tels = a_tels;
        this.s_of_atels = s_of_atels;
        this.a_links = a_links;
        this.s_of_alinks = s_of_alinks;
    }

    public String getA_f_name() { return a_f_name; }

    public String getA_city() { return a_city; }

    public String getA_e_mail() { return a_e_mail; }

    public String[] getA_telenums() { return a_tels; }

    public int getS_of_atels() { return s_of_atels; }

    public String getA_idof() { return a_idof; }

    public String[] getA_links() { return a_links; }

    public int getS_of_alinks() { return s_of_alinks; }
}
