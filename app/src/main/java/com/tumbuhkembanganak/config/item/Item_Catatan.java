package com.tumbuhkembanganak.config.item;

public class Item_Catatan {
    String id;
    String judul;
    String isi;
    String tgl;

    // Empty constructor
    public Item_Catatan(){

    }
    // constructor
    public Item_Catatan(String id, String judul, String isi, String tgl){
        this.id = id;
        this.judul= judul;
        this.isi = isi;
        this.tgl= tgl;
    }

    public String getId(){return this.id;}
    public String getJudul(){return this.judul;}
    public String getIsi(){return this.isi;}
    public String getTgl(){return this.tgl;}
    public void setId(String id){this.id= id;}
    public void setJudul(String judul){this.judul= judul;}
    public void setIsi(String isi){this.isi = isi;}
    public void setTgl(String tgl){this.tgl= tgl;}
}
