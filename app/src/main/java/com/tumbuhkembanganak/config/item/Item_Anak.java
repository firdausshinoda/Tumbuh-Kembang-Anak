package com.tumbuhkembanganak.config.item;

public class Item_Anak {
    String id;
    String nama_anak;
    String sex;
    String tgl_lahir;

    // Empty constructor
    public Item_Anak(){

    }
    // constructor
    public Item_Anak(String id, String nama_anak, String sex, String tgl_lahir){
        this.id = id;
        this.nama_anak = nama_anak;
        this.sex = sex;
        this.tgl_lahir = tgl_lahir;
    }

    public String getId(){return this.id;}
    public String getNama_anak(){return this.nama_anak;}
    public String getSex(){return this.sex;}
    public String getTgl_lahir(){return this.tgl_lahir;}
    public void setId(String id){this.id= id;}
    public void setNama_anak(String nama_anak){this.nama_anak= nama_anak;}
    public void setSex(String sex){this.sex = sex;}
    public void setTgl_lahir(String tgl_lahir){this.tgl_lahir= tgl_lahir;}
}
