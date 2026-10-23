package com.tumbuhkembanganak.config.item;

public class Item_Catatan_Stt_Gizi {
    String id, id_anak, key_1, val_1, key_2, val_2, keterangan, val, tgl;

    // Empty constructor
    public Item_Catatan_Stt_Gizi(){

    }
    // constructor
    public Item_Catatan_Stt_Gizi(String id, String id_anak, String key_1, String val_1, String key_2, String val_2, String keterangan,
                                 String val, String tgl){
        this.id = id;
        this.id_anak= id_anak;
        this.key_1= key_1;
        this.val_1= val_1;
        this.key_2= key_2;
        this.val_2= val_2;
        this.keterangan= keterangan;
        this.val= val;
        this.tgl= tgl;
    }

    public String getId(){return this.id;}
    public String getId_anak(){return this.id_anak;}
    public String getKey_1(){return this.key_1;}
    public String getVal_1(){return this.val_1;}
    public String getKey_2(){return this.key_2;}
    public String getVal_2(){return this.val_2;}
    public String getKeterangan(){return this.keterangan;}
    public String getVal(){return this.val;}
    public String getTgl(){return this.tgl;}
    public void setId(String id){this.id= id;}
    public void setId_anak(String id_anak){this.id_anak= id_anak;}
    public void setKey_1(String key_1){this.key_1= key_1;}
    public void setVal_1(String val_1){this.val_1= val_1;}
    public void setKey_2(String key_2){this.key_2= key_2;}
    public void setVal_2(String val_2){this.val_2= val_2;}
    public void setKeterangan(String keterangan){this.keterangan= keterangan;}
    public void setVal(String val){this.val= val;}
    public void setTgl(String tgl){this.tgl= tgl;}
}
