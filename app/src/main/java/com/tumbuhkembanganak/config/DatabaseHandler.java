package com.tumbuhkembanganak.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tumbuh_kembang_anak";
    private static final String TABLE_ANAK = "anak";
    private static final String TABLE_FORM_KSP = "form_ksp";
    private static final String TABLE_DATA_GIZI = "data_gizi";
    private static final String TABLE_CATATAN = "catatan";
    private static final String TABLE_CATATAN_STT_GIZI = "catatan_stt_gizi";
    private SQLiteDatabase _db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ANAK = "CREATE TABLE " + TABLE_ANAK+ "("
                + Config.TAG_ID + " INTEGER PRIMARY KEY," + Config.TAG_NAMA_ANAK+ " TEXT,"
                + Config.TAG_SEX + " TEXT," + Config.TAG_TGL_LAHIR+ " TEXT )";
        String CREATE_TABLE_FORM_KSP = "CREATE TABLE " + TABLE_FORM_KSP+ "("
                + Config.TAG_ID + " INTEGER PRIMARY KEY,"
                + Config.TAG_PERTANYAAN+ " TEXT,"
                + Config.TAG_PERKEMBANGAN+ " TEXT,"
                + Config.TAG_GAMBAR + " TEXT,"
                + Config.TAG_USIA+ " TEXT )";
        String CREATE_TABLE_DATA_GIZI = "CREATE TABLE " + TABLE_DATA_GIZI+ "("
                + Config.TAG_ID + " INTEGER PRIMARY KEY,"
                + Config.TAG_TIPE_1+ " TEXT,"
                + Config.TAG_TIPE_2+ " TEXT,"
                + Config.TAG_VALUE+ " TEXT,"
                + Config.TAG_SEX + " TEXT,"
                + Config.TAG_MIN_3_SD + " TEXT,"
                + Config.TAG_MIN_2_SD + " TEXT,"
                + Config.TAG_MIN_1_SD + " TEXT,"
                + Config.TAG_MEDIAN + " TEXT,"
                + Config.TAG_1_SD + " TEXT,"
                + Config.TAG_2_SD + " TEXT,"
                + Config.TAG_3_SD + " TEXT,"
                + Config.TAG_USIA+ " TEXT )";
        String CREATE_TABLE_CATATAN = "CREATE TABLE " + TABLE_CATATAN+ "("
                + Config.TAG_ID + " INTEGER PRIMARY KEY," + Config.TAG_JUDUL+ " TEXT,"
                + Config.TAG_ISI+ " TEXT," + Config.TAG_TGL+ " TEXT )";
        String CREATE_TABLE_CATATAN_STT_GIZI = "CREATE TABLE " + TABLE_CATATAN_STT_GIZI+ "("
                + Config.TAG_ID + " INTEGER PRIMARY KEY," + Config.TAG_ID_ANAK+ " TEXT,"
                + Config.TAG_KEY_1+ " TEXT," + Config.TAG_VALUE_1+ " TEXT,"
                + Config.TAG_KEY_2+ " TEXT," + Config.TAG_VALUE_2+ " TEXT,"
                + Config.TAG_KETERANGAN+ " TEXT,"+ Config.TAG_TGL+ " TEXT,"+ Config.TAG_VALUE+ " TEXT )";
        db.execSQL(CREATE_TABLE_ANAK);
        db.execSQL(CREATE_TABLE_FORM_KSP);
        db.execSQL(CREATE_TABLE_DATA_GIZI);
        db.execSQL(CREATE_TABLE_CATATAN);
        db.execSQL(CREATE_TABLE_CATATAN_STT_GIZI);
        _db = db;

        insertFirstDataKSP();
        insertFirstDataGizi();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANAK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_KSP);
        onCreate(db);
    }

    public void insertDataGizi(String tipe1,String tipe2,String value,String sex,String min_3,String min_2,String min_1,String median, String sd_1, String sd_2, String sd_3, String usia) {
        String query = "insert into "+TABLE_DATA_GIZI+" ("+Config.TAG_TIPE_1+","+ Config.TAG_TIPE_2+","+Config.TAG_VALUE+","+Config.TAG_SEX+","+
                Config.TAG_MIN_3_SD+","+Config.TAG_MIN_2_SD+","+Config.TAG_MIN_1_SD+","+Config.TAG_MEDIAN+","+
                Config.TAG_1_SD+","+Config.TAG_2_SD+","+Config.TAG_3_SD+","+Config.TAG_USIA+")"+
                " values('"+tipe1+"','"+tipe2+"','"+value+"','"+sex+"','"+min_3+"','"+min_2+"','"+min_1+"','"+median+"','"+sd_1+"','"+sd_2+"','"+sd_1+"','"+usia+"');";
        _db.execSQL(query);
    }

    public void insertFormKSP(String pertanyaan, String gambar, String perkembangan, String usia) {
        String query = "insert into "+TABLE_FORM_KSP+" ("+Config.TAG_PERTANYAAN+","+ Config.TAG_PERKEMBANGAN+","+Config.TAG_GAMBAR+","+Config.TAG_USIA+")"+
                " values('"+pertanyaan+"','"+perkembangan+"','"+gambar+"','"+usia+"');";
        _db.execSQL(query);
    }

    public void insertAnak(String nama, String sex, String tgl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.TAG_NAMA_ANAK, nama);
        values.put(Config.TAG_SEX, sex);
        values.put(Config.TAG_TGL_LAHIR, tgl);
        db.insert(TABLE_ANAK, null, values);
        db.close();
    }

    public boolean insertCatatan(String judul, String isi, String tgl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.TAG_JUDUL, judul);
        values.put(Config.TAG_ISI, isi);
        values.put(Config.TAG_TGL, tgl);
        db.insert(TABLE_CATATAN, null, values);
        return true;
    }

    public boolean updateCatatan(String id, String judul, String isi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ID, id);
        contentValues.put(Config.TAG_JUDUL, judul);
        contentValues.put(Config.TAG_ISI, isi);
        db.update(TABLE_CATATAN, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public ArrayList<HashMap<String, String>> getAllCatatan() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_CATATAN;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_JUDUL, cursor.getString(1));
                map.put(Config.TAG_ISI, cursor.getString(2));
                map.put(Config.TAG_TGL, cursor.getString(3));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getAllCatatanSttGizi(String id_anak) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_CATATAN_STT_GIZI+ " WHERE "+Config.TAG_ID_ANAK+" = "+id_anak+ " ORDER BY "
                +Config.TAG_ID+ " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_ID_ANAK, cursor.getString(1));
                map.put(Config.TAG_KEY_1, cursor.getString(2));
                map.put(Config.TAG_VALUE_1, cursor.getString(3));
                map.put(Config.TAG_KEY_2, cursor.getString(4));
                map.put(Config.TAG_VALUE_2, cursor.getString(5));
                map.put(Config.TAG_KETERANGAN, cursor.getString(6));
                map.put(Config.TAG_VALUE, cursor.getString(7));
                map.put(Config.TAG_TGL, cursor.getString(8));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public boolean deleteCatatan(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATATAN, Config.TAG_ID+ " = ?", new String[] {  id });
        return true;
    }

    public boolean insertCatatanSttGizi(String id_anak, String key_1, String value_1, String key_2, String value_2,
                                        String keterangan, String value, String tgl) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("catatan",id_anak+"/"+key_1+"/"+value_1+"/"+key_2+"/"+value_2+"/"+keterangan+"/"+value+"/"+tgl);
        ContentValues values = new ContentValues();
        values.put(Config.TAG_ID_ANAK, id_anak);
        values.put(Config.TAG_KEY_1, key_1);
        values.put(Config.TAG_VALUE_1, value_1);
        values.put(Config.TAG_KEY_2, key_2);
        values.put(Config.TAG_VALUE_2, value_2);
        values.put(Config.TAG_KETERANGAN, keterangan);
        values.put(Config.TAG_VALUE, value);
        values.put(Config.TAG_TGL, tgl);
        db.insert(TABLE_CATATAN_STT_GIZI, null, values);
        return true;
    }

    public boolean deleteCatatanSttGiziId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATATAN_STT_GIZI, Config.TAG_ID+ " = ?", new String[] {  id });
        return true;
    }

    public boolean deleteCatatanSttGiziIdAnak(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATATAN_STT_GIZI, Config.TAG_ID_ANAK+ " = ?", new String[] {  id });
        return true;
    }

    public ArrayList<HashMap<String, String>> getDataGizi(String tipe1, String tipe2, String sex, String value,String umur) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery;
        if (umur.isEmpty()){
            selectQuery = "SELECT  * FROM "+TABLE_DATA_GIZI+ " WHERE "+Config.TAG_TIPE_1+" = '"+tipe1+"' AND "+
                    Config.TAG_TIPE_2+" = '"+tipe2+"' AND "+Config.TAG_SEX+" = '"+sex+"' AND "+Config.TAG_VALUE+" = '"+value+"'";
        } else {
            selectQuery = "SELECT  * FROM "+TABLE_DATA_GIZI+ " WHERE "+Config.TAG_TIPE_1+" = '"+tipe1+"' AND "+
                    Config.TAG_TIPE_2+" = '"+tipe2+"' AND "+Config.TAG_SEX+" = '"+sex+"' AND "+Config.TAG_VALUE+" = '"+value+"' AND "+Config.TAG_USIA+" = '"+umur+"'";
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_TIPE_1, cursor.getString(1));
                map.put(Config.TAG_TIPE_2, cursor.getString(2));
                map.put(Config.TAG_VALUE, cursor.getString(3));
                map.put(Config.TAG_SEX, cursor.getString(4));
                map.put(Config.TAG_MIN_3_SD, cursor.getString(5));
                map.put(Config.TAG_MIN_2_SD, cursor.getString(6));
                map.put(Config.TAG_MIN_1_SD, cursor.getString(7));
                map.put(Config.TAG_MEDIAN, cursor.getString(8));
                map.put(Config.TAG_1_SD, cursor.getString(9));
                map.put(Config.TAG_2_SD, cursor.getString(10));
                map.put(Config.TAG_3_SD, cursor.getString(11));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getValueDataGizi(String tipe1, String tipe2, String sex) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_DATA_GIZI+ " WHERE "+Config.TAG_TIPE_1+" = '"+tipe1+"' AND "+
                Config.TAG_TIPE_2+" = '"+tipe2+"' AND "+ Config.TAG_SEX+" = '"+sex+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_TIPE_1, cursor.getString(1));
                map.put(Config.TAG_TIPE_2, cursor.getString(2));
                map.put(Config.TAG_VALUE, cursor.getString(3));
                map.put(Config.TAG_SEX, cursor.getString(4));
                map.put(Config.TAG_MIN_3_SD, cursor.getString(5));
                map.put(Config.TAG_MIN_2_SD, cursor.getString(6));
                map.put(Config.TAG_MIN_1_SD, cursor.getString(7));
                map.put(Config.TAG_MEDIAN, cursor.getString(8));
                map.put(Config.TAG_1_SD, cursor.getString(9));
                map.put(Config.TAG_2_SD, cursor.getString(10));
                map.put(Config.TAG_3_SD, cursor.getString(11));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getAllFormKSP(String umur) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_FORM_KSP+ " WHERE "+Config.TAG_USIA+" = "+umur;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_PERTANYAAN, cursor.getString(1));
                map.put(Config.TAG_PERKEMBANGAN, cursor.getString(2));
                map.put(Config.TAG_GAMBAR, cursor.getString(3));
                map.put(Config.TAG_USIA, cursor.getString(4));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getAllKSP() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_FORM_KSP;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_PERTANYAAN, cursor.getString(1));
                map.put(Config.TAG_PERKEMBANGAN, cursor.getString(2));
                map.put(Config.TAG_GAMBAR, cursor.getString(3));
                map.put(Config.TAG_USIA, cursor.getString(4));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getAllAnak() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_ANAK;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Config.TAG_ID, cursor.getString(0));
                map.put(Config.TAG_NAMA_ANAK, cursor.getString(1));
                map.put(Config.TAG_SEX, cursor.getString(2));
                map.put(Config.TAG_TGL_LAHIR, cursor.getString(3));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public void deleteAnak(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANAK, Config.TAG_ID+ " = ?", new String[] {  id });
        db.close();
    }

    public boolean updateAnak(String id, String nama, String sex, String tgl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ID, id);
        contentValues.put(Config.TAG_NAMA_ANAK, nama);
        contentValues.put(Config.TAG_SEX, sex);
        contentValues.put(Config.TAG_TGL_LAHIR, tgl);
        db.update(TABLE_ANAK, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    private void insertFirstDataGizi(){
        //umur __ berat badan __ laki-laki 0 - 60 bulan
        insertDataGizi("umur","berat_badan","0","Laki - Laki","2.1","2.5","2.9","3.3","3.9","4.4","5.0","0-60");
        insertDataGizi("umur","berat_badan","1","Laki - Laki","2.9","3.4","3.9","4.5","5.1","5.8","6.6","0-60");
        insertDataGizi("umur","berat_badan","2","Laki - Laki","3.8","4.3","4.9","5.6","6.3","7.1","8.0","0-60");
        insertDataGizi("umur","berat_badan","3","Laki - Laki","4.4","5.0","5.7","6.4","7.2","8.0","9.0","0-60");
        insertDataGizi("umur","berat_badan","4","Laki - Laki","4.9","5.6","6.2","7.0","7.8","8.7","9.7","0-60");
        insertDataGizi("umur","berat_badan","5","Laki - Laki","5.3","6.0","6.7","7.5","8.4","9.3","10.4","0-60");
        insertDataGizi("umur","berat_badan","6","Laki - Laki","5.7","6.4","7.1","7.9","8.8","9.8","10.9","0-60");
        insertDataGizi("umur","berat_badan","7","Laki - Laki","5.9","6.7","7.4","8.3","9.2","10.3","11.3","0-60");
        insertDataGizi("umur","berat_badan","8","Laki - Laki","6.2","6.9","7.7","8.6","9.6","10.7","11.7","0-60");
        insertDataGizi("umur","berat_badan","9","Laki - Laki","6.4","7.1","8.0","8.9","9.9","11.0","12.0","0-60");
        insertDataGizi("umur","berat_badan","10","Laki - Laki","6.6","7.4","8.2","9.2","10.2","11.4","12.3","0-60");
        insertDataGizi("umur","berat_badan","11","Laki - Laki","6.8","7.6","8.4","9.4","10.5","11.7","13.7","0-60");
        insertDataGizi("umur","berat_badan","12","Laki - Laki","6.9","7.7","8.6","9.6","10.8","12.0","13.0","0-60");
        insertDataGizi("umur","berat_badan","13","Laki - Laki","7.1","7.9","8.8","9.9","11.0","12.3","13.7","0-60");
        insertDataGizi("umur","berat_badan","14","Laki - Laki","7.2","8.1","9.0","10.1","11.3","12.6","14.0","0-60");
        insertDataGizi("umur","berat_badan","15","Laki - Laki","7.4","8.3","9.2","10.3","11.5","12.8","14.3","0-60");
        insertDataGizi("umur","berat_badan","16","Laki - Laki","7.5","8.4","9.4","10.5","11.7","13.1","14.6","0-60");
        insertDataGizi("umur","berat_badan","17","Laki - Laki","7.7","8.6","9.6","10.7","12.0","13.5","14.9","0-60");
        insertDataGizi("umur","berat_badan","18","Laki - Laki","7.8","8.8","9.8","10.9","12.2","13.7","15.3","0-60");
        insertDataGizi("umur","berat_badan","19","Laki - Laki","8.0","8.9","10.0","11.1","12.5","13.9","15.6","0-60");
        insertDataGizi("umur","berat_badan","20","Laki - Laki","8.1","9.1","10.1","11.3","12.7","14.2","15.9","0-60");
        insertDataGizi("umur","berat_badan","21","Laki - Laki","8.2","9.2","10.3","11.5","12.9","14.5","16.2","0-60");
        insertDataGizi("umur","berat_badan","22","Laki - Laki","8.4","9.4","10.5","11.8","13.2","14.7","16.5","0-60");
        insertDataGizi("umur","berat_badan","23","Laki - Laki","8.5","9.5","10.7","12.0","13.4","15.0","16.8","0-60");
        insertDataGizi("umur","berat_badan","24","Laki - Laki","8.6","9.7","10.8","12.2","13.6","15.3","17.1","0-60");
        insertDataGizi("umur","berat_badan","25","Laki - Laki","8.8","9.8","11.0","12.4","13.9","15.5","17.5","0-60");
        insertDataGizi("umur","berat_badan","26","Laki - Laki","8.9","10.0","11.2","12.5","14.1","15.8","17.8","0-60");
        insertDataGizi("umur","berat_badan","27","Laki - Laki","9.0","10.1","11.3","12.7","14.3","16.1","18.1","0-60");
        insertDataGizi("umur","berat_badan","28","Laki - Laki","9.1","10.2","11.5","12.9","14.5","16.3","18.4","0-60");
        insertDataGizi("umur","berat_badan","29","Laki - Laki","9.2","10.4","11.7","13.1","14.8","16.6","18.7","0-60");
        insertDataGizi("umur","berat_badan","30","Laki - Laki","9.4","10.5","11.8","13.3","15.0","16.9","19.0","0-60");
        insertDataGizi("umur","berat_badan","31","Laki - Laki","9.5","10.7","12.0","13.5","15.2","17.1","19.3","0-60");
        insertDataGizi("umur","berat_badan","32","Laki - Laki","9.6","10.8","12.1","13.7","15.4","17.3","19.6","0-60");
        insertDataGizi("umur","berat_badan","33","Laki - Laki","9.7","10.9","12.3","13.8","15.6","17.6","19.9","0-60");
        insertDataGizi("umur","berat_badan","34","Laki - Laki","9.8","11.0","12.4","14.0","15.8","17.8","20.2","0-60");
        insertDataGizi("umur","berat_badan","35","Laki - Laki","9.9","11.2","12.6","14.2","16.0","18.1","20.4","0-60");
        insertDataGizi("umur","berat_badan","36","Laki - Laki","10.0","11.3","12.7","14.3","16.2","18.3","20.7","0-60");
        insertDataGizi("umur","berat_badan","37","Laki - Laki","10.1","11.4","12.9","14.5","16.4","18.6","21.0","0-60");
        insertDataGizi("umur","berat_badan","38","Laki - Laki","10.2","11.5","13.0","14.7","16.6","18.8","21.3","0-60");
        insertDataGizi("umur","berat_badan","39","Laki - Laki","10.3","11.6","13.1","14.8","16.8","19.0","21.6","0-60");
        insertDataGizi("umur","berat_badan","40","Laki - Laki","10.4","11.8","13.3","15.0","17.0","19.3","21.9","0-60");
        insertDataGizi("umur","berat_badan","41","Laki - Laki","10.5","11.9","13.4","15.2","17.2","19.5","22.1","0-60");
        insertDataGizi("umur","berat_badan","42","Laki - Laki","10.6","12.0","13.6","15.3","17.4","19.7","22.4","0-60");
        insertDataGizi("umur","berat_badan","43","Laki - Laki","10.7","12.1","13.7","15.5","17.6","20.0","22.7","0-60");
        insertDataGizi("umur","berat_badan","44","Laki - Laki","10.8","12.2","13.8","15.7","17.8","20.2","23.0","0-60");
        insertDataGizi("umur","berat_badan","45","Laki - Laki","10.9","12.4","14.0","15.8","18.0","20.5","23.3","0-60");
        insertDataGizi("umur","berat_badan","46","Laki - Laki","1.0","12.5","14.1","16.0","18.2","20.7","23.6","0-60");
        insertDataGizi("umur","berat_badan","47","Laki - Laki","11.1","12.6","14.3","16.2","18.4","20.9","23.9","0-60");
        insertDataGizi("umur","berat_badan","48","Laki - Laki","11.2","12.7","14.4","16.3","18.6","21.2","24.2","0-60");
        insertDataGizi("umur","berat_badan","49","Laki - Laki","11.3","12.8","14.5","16.5","18.8","21.4","24.5","0-60");
        insertDataGizi("umur","berat_badan","50","Laki - Laki","11.4","12.9","14.7","16.7","19.0","21.7","24.8","0-60");
        insertDataGizi("umur","berat_badan","51","Laki - Laki","11.5","13.1","14.8","16.8","19.2","21.9","25.1","0-60");
        insertDataGizi("umur","berat_badan","52","Laki - Laki","11.6","13.2","15.0","17.0","19.4","22.2","25.4","0-60");
        insertDataGizi("umur","berat_badan","53","Laki - Laki","11.7","13.3","15.1","17.2","19.6","22.4","25.7","0-60");
        insertDataGizi("umur","berat_badan","54","Laki - Laki","11.8","13.4","15.2","17.3","19.8","22.7","26.0","0-60");
        insertDataGizi("umur","berat_badan","55","Laki - Laki","11.9","13.5","15.4","17.5","20.0","22.9","26.3","0-60");
        insertDataGizi("umur","berat_badan","56","Laki - Laki","12.0","13.6","15.5","17.7","20.2","23.2","26.6","0-60");
        insertDataGizi("umur","berat_badan","57","Laki - Laki","12.1","13.7","15.6","17.8","20.4","23.4","26.9","0-60");
        insertDataGizi("umur","berat_badan","58","Laki - Laki","12.2","13.8","15.8","18.0","20.6","23.7","27.2","0-60");
        insertDataGizi("umur","berat_badan","59","Laki - Laki","12.3","14.0","15.9","18.2","20.8","23.9","27.6","0-60");
        insertDataGizi("umur","berat_badan","60","Laki - Laki","12.4","14.1","16.0","18.3","21.0","24.2","27.9","0-60");

        //umur __ panjang badan __ laki-laki 0-24 bulan
        insertDataGizi("umur","panjang_badan","0","Laki - Laki","44.2","46.1","48.0","49.9","51.8","53.7","55.6","0-24");
        insertDataGizi("umur","panjang_badan","1","Laki - Laki","48.9","50.8","52.8","54.7","56.7","58.6","60.6","0-24");
        insertDataGizi("umur","panjang_badan","2","Laki - Laki","52.4","54.4","56.4","58.4","50.4","62.4","64.4","0-24");
        insertDataGizi("umur","panjang_badan","3","Laki - Laki","55.3","57.3","59.4","61.4","63.5","65.5","67.6","0-24");
        insertDataGizi("umur","panjang_badan","4","Laki - Laki","57.6","59.7","61.8","63.9","66.0","68.0","70.1","0-24");
        insertDataGizi("umur","panjang_badan","5","Laki - Laki","59.6","61.7","63.8","65.9","68.0","70.1","72.2","0-24");
        insertDataGizi("umur","panjang_badan","6","Laki - Laki","61.2","63.3","65.5","67.6","69.8","71.9","74.0","0-24");
        insertDataGizi("umur","panjang_badan","7","Laki - Laki","62.7","64.8","67.0","69.2","71.3","73.5","75.7","0-24");
        insertDataGizi("umur","panjang_badan","8","Laki - Laki","64.0","66.2","68.4","70.6","72.8","75.0","77.2","0-24");
        insertDataGizi("umur","panjang_badan","9","Laki - Laki","65.2","67.5","69.7","72.0","74.2","76.5","78.7","0-24");
        insertDataGizi("umur","panjang_badan","10","Laki - Laki","66.4","68.7","71.0","73.3","75.6","77.9","80.1","0-24");
        insertDataGizi("umur","panjang_badan","11","Laki - Laki","67.6","69.9","72.2","74.5","76.9","79.2","81.5","0-24");
        insertDataGizi("umur","panjang_badan","12","Laki - Laki","68.6","71.0","73.4","75.7","78.1","80.5","82.9","0-24");
        insertDataGizi("umur","panjang_badan","13","Laki - Laki","69.6","72.1","74.5","76.9","79.3","81.8","84.2","0-24");
        insertDataGizi("umur","panjang_badan","14","Laki - Laki","70.6","73.1","75.6","78.0","80.5","83.0","85.5","0-24");
        insertDataGizi("umur","panjang_badan","15","Laki - Laki","71.6","74.1","76.6","79.1","81.7","84.2","86.7","0-24");
        insertDataGizi("umur","panjang_badan","16","Laki - Laki","72.5","75.0","77.6","80.2","82.8","85.4","88.0","0-24");
        insertDataGizi("umur","panjang_badan","17","Laki - Laki","73.3","76.0","78.6","81.2","83.9","86.5","89.2","0-24");
        insertDataGizi("umur","panjang_badan","18","Laki - Laki","74.2","76.9","79.6","82.3","85.0","87.7","90.4","0-24");
        insertDataGizi("umur","panjang_badan","19","Laki - Laki","75.0","77.7","80.5","83.2","86.0","88.8","91.5","0-24");
        insertDataGizi("umur","panjang_badan","20","Laki - Laki","75.8","78.6","81.4","84.2","87.0","89.8","92.6","0-24");
        insertDataGizi("umur","panjang_badan","21","Laki - Laki","76.5","79.4","82.3","85.1","88.0","90.9","93.8","0-24");
        insertDataGizi("umur","panjang_badan","22","Laki - Laki","77.2","80.2","83.1","86.0","89.0","91.9","94.9","0-24");
        insertDataGizi("umur","panjang_badan","23","Laki - Laki","78.0","81.0","83.9","86.9","89.9","92.9","95.9","0-24");
        insertDataGizi("umur","panjang_badan","24","Laki - Laki","78.7","81.7","84.8","87.8","90.9","93.9","97.0","0-24");

        //umur __ tinggi badan __ laki-laki 24-60 bulan
        insertDataGizi("umur","tinggi_badan","24","Laki - Laki","78.0","8.0","84.1","8.1","90.2","93.2","96.3","24-60");
        insertDataGizi("umur","tinggi_badan","25","Laki - Laki","78.6","81.7","84.9","8.0","91.1","94.2","97.3","24-60");
        insertDataGizi("umur","tinggi_badan","26","Laki - Laki","79.3","81.5","85.6","8.8","92.0","95.2","98.3","24-60");
        insertDataGizi("umur","tinggi_badan","27","Laki - Laki","79.9","82.1","86.4","8.6","92.9","96.1","99.3","24-60");
        insertDataGizi("umur","tinggi_badan","28","Laki - Laki","80.5","83.8","87.1","90.4","93.7","97.0","100.3","24-60");
        insertDataGizi("umur","tinggi_badan","29","Laki - Laki","81.1","83.5","87.8","91.2","94.5","97.9","101.2","24-60");
        insertDataGizi("umur","tinggi_badan","30","Laki - Laki","81.7","84.1","88.5","91.9","95.3","98.7","102.1","24-60");
        insertDataGizi("umur","tinggi_badan","31","Laki - Laki","82.3","85.7","89.2","92.7","96.1","99.6","103.0","24-60");
        insertDataGizi("umur","tinggi_badan","32","Laki - Laki","82.8","85.4","89.9","93.4","96.9","100.4","103.9","24-60");
        insertDataGizi("umur","tinggi_badan","33","Laki - Laki","83.4","86.9","90.5","94.1","97.6","101.2","104.8","24-60");
        insertDataGizi("umur","tinggi_badan","34","Laki - Laki","83.9","86.5","91.1","94.8","98.4","102.0","105.6","24-60");
        insertDataGizi("umur","tinggi_badan","35","Laki - Laki","84.4","87.1","91.8","95.4","99.1","102.7","106.4","24-60");
        insertDataGizi("umur","tinggi_badan","36","Laki - Laki","85.0","88.7","92.4","96.1","99.8","103.5","107.2","24-60");
        insertDataGizi("umur","tinggi_badan","37","Laki - Laki","85.5","88.2","93.0","96.7","100.5","104.2","108.0","24-60");
        insertDataGizi("umur","tinggi_badan","38","Laki - Laki","86.0","89.8","93.6","97.4","101.2","105.0","108.8","24-60");
        insertDataGizi("umur","tinggi_badan","39","Laki - Laki","86.5","90.3","94.2","98.0","101.8","105.7","109.5","24-60");
        insertDataGizi("umur","tinggi_badan","40","Laki - Laki","87.0","90.9","94.7","98.6","102.5","106.4","110.3","24-60");
        insertDataGizi("umur","tinggi_badan","41","Laki - Laki","87.5","91.4","95.3","99.2","103.2","107.1","111.0","24-60");
        insertDataGizi("umur","tinggi_badan","42","Laki - Laki","88.0","91.9","95.9","99.9","103.8","107.8","111.7","24-60");
        insertDataGizi("umur","tinggi_badan","43","Laki - Laki","88.4","92.4","96.4","100.4","104.5","108.5","112.5","24-60");
        insertDataGizi("umur","tinggi_badan","44","Laki - Laki","88.9","93.0","97.0","101.0","105.1","109.1","113.2","24-60");
        insertDataGizi("umur","tinggi_badan","45","Laki - Laki","89.4","93.5","97.5","101.6","105.7","109.8","113.9","24-60");
        insertDataGizi("umur","tinggi_badan","46","Laki - Laki","89.8","94.0","98.1","102.2","106.3","110.4","114.6","24-60");
        insertDataGizi("umur","tinggi_badan","47","Laki - Laki","90.3","94.4","98.6","102.8","106.9","111.1","115.2","24-60");
        insertDataGizi("umur","tinggi_badan","48","Laki - Laki","90.7","94.9","99.1","103.3","107.5","111.7","115.9","24-60");
        insertDataGizi("umur","tinggi_badan","49","Laki - Laki","91.2","95.4","99.7","103.9","108.1","112.4","116.6","24-60");
        insertDataGizi("umur","tinggi_badan","50","Laki - Laki","92.6","95.9","100.2","104.4","108.7","113.0","117.3","24-60");
        insertDataGizi("umur","tinggi_badan","51","Laki - Laki","92.1","96.4","100.7","105.0","109.3","113.6","117.9","24-60");
        insertDataGizi("umur","tinggi_badan","52","Laki - Laki","93.5","96.9","101.2","105.6","109.9","114.2","118.6","24-60");
        insertDataGizi("umur","tinggi_badan","53","Laki - Laki","93.0","97.4","101.7","106.1","110.5","114.9","119.2","24-60");
        insertDataGizi("umur","tinggi_badan","54","Laki - Laki","93.4","97.8","102.3","106.7","111.1","115.5","119.9","24-60");
        insertDataGizi("umur","tinggi_badan","55","Laki - Laki","94.9","98.3","102.8","107.2","111.7","116.1","120.6","24-60");
        insertDataGizi("umur","tinggi_badan","56","Laki - Laki","94.3","98.8","103.3","107.8","112.3","116.7","121.2","24-60");
        insertDataGizi("umur","tinggi_badan","57","Laki - Laki","94.7","99.3","103.8","108.3","112.8","117.4","121.9","24-60");
        insertDataGizi("umur","tinggi_badan","58","Laki - Laki","95.2","99.7","104.3","108.9","113.4","118.0","122.6","24-60");
        insertDataGizi("umur","tinggi_badan","59","Laki - Laki","95.6","10.2","104.8","109.4","114.0","118.6","123.2","24-60");
        insertDataGizi("umur","tinggi_badan","60","Laki - Laki","96.1","10.7","105.3","110.0","114.6","119.2","123.9","24-60");

        //panjang badan __ berat badan __ laki-laki 0-24 bulan
        insertDataGizi("panjang_badan","berat_badan","45.0","Laki - Laki","1.9","2.0","2.2","2.4","2.7","3.0","3.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","45.5","Laki - Laki","1.9","2.1","2.3","2.5","2.8","3.1","3.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","46.0","Laki - Laki","2.0","2.2","2.4","2.6","2.9","3.1","3.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","46.5","Laki - Laki","2.1","2.3","2.5","2.7","3.0","3.2","3.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","47.0","Laki - Laki","2.1","2.3","2.5","2.8","3.0","3.3","3.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","47.5","Laki - Laki","2.2","2.4","2.6","2.9","3.1","3.4","3.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","48.0","Laki - Laki","2.3","2.5","2.7","2.9","3.2","3.6","3.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","48.5","Laki - Laki","2.3","2.6","2.8","3.0","3.3","3.7","4.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","49.0","Laki - Laki","2.4","2.6","2.9","3.1","3.4","3.8","4.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","49.5","Laki - Laki","2.5","2.7","3.0","3.2","3.5","3.9","4.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","50.0","Laki - Laki","2.6","2.8","3.0","3.3","3.6","4.0","4.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","50.5","Laki - Laki","2.7","2.9","3.1","3.4","3.8","4.1","4.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","51.0","Laki - Laki","2.7","3.0","3.2","3.5","3.9","4.2","4.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","51.5","Laki - Laki","2.8","3.1","3.3","3.6","4.0","4.4","4.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","52.0","Laki - Laki","2.9","3.2","3.5","3.8","4.1","4.5","5.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","52.5","Laki - Laki","3.0","3.3","3.6","3.9","4.2","4.6","5.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","53.0","Laki - Laki","3.1","3.4","3.7","4.0","4.4","4.8","5.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","53.5","Laki - Laki","3.2","3.5","3.8","4.1","4.5","4.9","5.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","54.0","Laki - Laki","3.3","3.6","3.9","4.3","4.7","5.1","5.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","54.5","Laki - Laki","3.4","3.7","4.0","4.4","4.8","5.3","5.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","55.0","Laki - Laki","3.6","3.8","4.2","4.5","5.0","5.4","6.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","55.5","Laki - Laki","3.7","4.0","4.3","4.7","5.1","5.6","6.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","56.0","Laki - Laki","3.8","4.1","4.4","4.8","5.3","5.7","6.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","56.5","Laki - Laki","3.9","4.2","4.6","5.0","5.4","5.8","6.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","57.0","Laki - Laki","4.0","4.3","4.7","5.1","5.6","6.1","6.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","57.5","Laki - Laki","4.1","4.5","4.9","5.3","5.7","6.3","6.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","58.0","Laki - Laki","4.3","4.6","5.0","5.4","5.9","6.4","7.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","58.5","Laki - Laki","4.4","4.7","5.1","5.6","6.1","6.6","7.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","59.0","Laki - Laki","4.5","4.8","5.3","5.7","6.2","6.8","7.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","59.5","Laki - Laki","4.6","5.0","5.4","5.9","6.4","7.0","7.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","60.0","Laki - Laki","4.7","5.1","5.5","6.0","6.5","7.1","7.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","60.5","Laki - Laki","4.8","5.2","5.6","6.1","6.7","7.3","8.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","61.0","Laki - Laki","4.9","5.3","5.8","6.3","6.8","7.4","8.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","61.5","Laki - Laki","5.0","5.4","5.9","6.4","7.0","7.6","8.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","62.0","Laki - Laki","5.1","5.6","6.0","6.5","7.1","7.7","8.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","62.5","Laki - Laki","5.2","5.7","6.1","6.7","7.2","7.9","8.6","0-24");

        insertDataGizi("panjang_badan","berat_badan","63.0","Laki - Laki","5.3","5.8","6.2","6.8","7.4","8.0","8.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","63.5","Laki - Laki","5.4","5.9","6.4","6.9","7.5","8.2","8.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","64.0","Laki - Laki","5.5","6.0","6.5","7.0","7.6","8.3","9.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","64.5","Laki - Laki","5.6","6.1","6.6","7.1","7.8","8.5","9.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","65.0","Laki - Laki","5.7","6.2","6.7","7.3","7.9","8.6","9.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","65.5","Laki - Laki","5.8","6.3","6.8","7.4","8.0","8.7","9.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","66.0","Laki - Laki","5.9","6.4","6.9","7.5","8.2","8.9","9.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","66.5","Laki - Laki","6.0","6.5","7.0","7.6","8.3","9.0","9.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","67.0","Laki - Laki","6.1","6.6","7.1","7.7","8.4","92","10.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","67.5","Laki - Laki","6.2","6.7","7.2","7.9","8.5","9.3","10.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","68.0","Laki - Laki","6.3","6.8","7.3","8.0","8.7","9.4","10.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","68.5","Laki - Laki","6.4","6.9","7.5","8.1","8.8","9.6","10.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","69.0","Laki - Laki","6.5","7.0","7.6","8.2","8.9","9.7","10.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","69.5","Laki - Laki","6.6","7.1","7.7","8.3","9.0","9.8","10.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","70.0","Laki - Laki","6.6","7.2","7.8","8.4","9.2","10.0","10.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","70.5","Laki - Laki","6.7","7.3","7.9","8.5","9.3","10.1","11.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","71.0","Laki - Laki","6.8","7.4","8.0","8.6","9.4","10.2","11.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","71.5","Laki - Laki","6.9","7.5","8.1","8.8","9.5","10.4","11.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","72.0","Laki - Laki","7.0","7.6","8.2","8.9","9.6","10.5","11.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","72.5","Laki - Laki","7.1","7.6","8.3","9.0","9.8","10.6","11.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","73.0","Laki - Laki","7.2","7.7","8.4","9.1","9.9","10.8","11.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","73.5","Laki - Laki","7.2","7.8","8.5","9.2","10.0","10.9","11.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","74.0","Laki - Laki","7.3","7.9","8.6","9.3","10.1","11.0","12.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","74.5","Laki - Laki","7.4","8.0","8.7","9.4","10.2","11.2","12.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","75.0","Laki - Laki","7.5","8.1","8.8","9.5","10.3","11.3","12.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","75.5","Laki - Laki","7.6","8.2","8.8","9.6","10.4","11.4","12.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","76.0","Laki - Laki","7.6","8.3","8.9","9.7","10.6","11.5","12.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","76.5","Laki - Laki","7.7","8.3","9.0","9.8","10.7","11.6","12.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","77.0","Laki - Laki","7.8","8.4","9.1","9.9","10.8","11.7","12.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","77.5","Laki - Laki","7.9","8.5","9.2","10.0","10.9","11.9","13.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","78.0","Laki - Laki","7.9","8.6","9.3","10.1","11.0","12.0","13.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","78.5","Laki - Laki","8.0","8.7","9.4","10.2","11.1","12.1","13.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","79.0","Laki - Laki","8.1","8.7","9.5","10.3","11.2","12.2","13.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","79.5","Laki - Laki","8.2","8.8","9.5","10.4","11.3","12.3","13.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","80.0","Laki - Laki","8.2","8.9","9.6","10.4","11.4","12.4","13.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","80.5","Laki - Laki","8.3","9.0","9.7","10.5","11.5","12.5","13.7","0-24");

        insertDataGizi("panjang_badan","berat_badan","81.0","Laki - Laki","8.4","9.1","9.8","10.6","11.6","12.6","13.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","81.5","Laki - Laki","8.5","9.1","9.9","10.7","11.7","12.7","13.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","82.0","Laki - Laki","8.5","9.2","10.0","10.8","11.8","12.8","14.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","82.5","Laki - Laki","8.6","9.3","10.1","10.9","11.9","13.0","14.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","83.0","Laki - Laki","8.7","9.4","10.2","11.0","12.0","13.1","14.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","83.5","Laki - Laki","8.8","9.5","10.3","11.2","12.1","13.2","14.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","84.0","Laki - Laki","8.9","9.6","10.4","11.3","12.2","13.3","14.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","84.5","Laki - Laki","9.0","9.7","10.5","11.4","12.4","13.5","14.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","85.0","Laki - Laki","9.1","9.8","10.6","11.5","12.5","13.6","14.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","85.5","Laki - Laki","9.2","9.9","10.7","11.6","12.6","13.7","15.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","86.0","Laki - Laki","9.3","10.0","10.8","11.7","12.8","13.9","15.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","86.5","Laki - Laki","9.4","10.1","11.0","11.9","12.9","14.0","15.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","87.0","Laki - Laki","9.5","10.2","11.1","12.0","13.0","14.2","15.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","87.5","Laki - Laki","9.6","10.4","11.2","12.1","13.2","14.3","15.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","88.0","Laki - Laki","9.7","10.5","11.3","12.2","13.3","14.5","15.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","88.5","Laki - Laki","9.8","10.6","11.4","12.4","13.4","14.6","15.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","89.0","Laki - Laki","9.9","10.7","11.5","12.5","13.5","14.7","16.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","89.5","Laki - Laki","10.0","10.8","11.6","12.6","13.7","14.9","16.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","90.0","Laki - Laki","10.1","10.9","11.8","12.7","13.8","15.0","16.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","90.5","Laki - Laki","10.2","11.0","11.9","12.8","13.9","15.1","16.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","91.0","Laki - Laki","10.3","11.1","12.0","13.0","14.1","15.3","16.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","91.5","Laki - Laki","10.4","11.2","12.1","13.1","14.2","15.4","16.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","92.0","Laki - Laki","10.5","11.3","12.2","13.2","14.3","15.6","17.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","92.5","Laki - Laki","10.6","11.4","12.3","13.3","14.4","15.7","17.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","93.0","Laki - Laki","10.7","11.5","12.4","13.4","14.6","15.8","17.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","93.5","Laki - Laki","10.7","11.6","12.5","13.5","14.7","16.0","17.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","94.0","Laki - Laki","10.8","11.7","12.6","13.7","14.8","16.1","17.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","94.5","Laki - Laki","10.9","11.8","12.7","13.8","14.9","16.3","17.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","95.0","Laki - Laki","11.0","11.9","12.8","13.9","15.1","16.4","17.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","95.5","Laki - Laki","11.1","12.0","12.9","14.0","15.2","16.5","18.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","96.0","Laki - Laki","11.2","12.1","13.1","14.1","15.3","16.7","18.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","96.5","Laki - Laki","11.3","12.2","13.2","14.3","15.5","16.8","18.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","97.0","Laki - Laki","11.4","12.3","13.3","14.4","15.6","17.0","18.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","97.5","Laki - Laki","11.5","12.4","13.4","14.5","15.7","17.1","18.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","98.0","Laki - Laki","11.6","12.5","13.5","14.6","15.9","17.3","18.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","98.5","Laki - Laki","11.7","12.6","13.6","14.8","16.0","17.5","19.1","0-24");

        insertDataGizi("panjang_badan","berat_badan","99.0","Laki - Laki","11.8","12.7","13.7","14.9","16.2","17.6","19.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","99.5","Laki - Laki","11.9","12.9","13.9","15.0","16.3","17.8","19.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","100.0","Laki - Laki","12.0","12.0","14.0","15.2","16.5","18.0","19.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","100.5","Laki - Laki","12.1","13.1","14.1","15.3","16.6","18.1","19.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","101.0","Laki - Laki","12.2","13.2","14.2","15.4","16.8","18.3","20.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","101.5","Laki - Laki","12.3","13.4","14.4","15.6","16.9","18.5","20.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","102.0","Laki - Laki","12.4","13.5","14.5","15.7","17.1","18.7","20.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","102.5","Laki - Laki","12.5","13.6","14.6","15.9","17.3","18.8","20.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","103.0","Laki - Laki","12.6","13.8","14.8","16.0","17.4","19.0","20.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","103.5","Laki - Laki","12.7","13.9","14.9","16.2","17.6","19.2","21.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","104.0","Laki - Laki","12.8","13.0","15.0","16.3","17.8","19.4","21.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","104.5","Laki - Laki","12.9","14.2","15.2","16.5","17.9","19.6","21.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","105.0","Laki - Laki","13.0","14.3","15.3","16.6","18.1","19.8","21.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","105.5","Laki - Laki","13.2","14.4","15.4","16.8","18.3","20.0","21.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","106.0","Laki - Laki","13.3","14.6","15.6","16.9","18.5","20.2","22.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","106.5","Laki - Laki","13.4","14.7","15.7","17.1","18.6","20.4","22.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","107.0","Laki - Laki","13.5","14.9","15.9","17.3","18.8","20.6","22.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","107.5","Laki - Laki","13.6","14.0","16.0","17.4","19.0","20.8","22.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","108.0","Laki - Laki","13.7","14.2","16.2","17.6","19.2","21.0","23.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","108.5","Laki - Laki","13.8","15.3","16.3","17.8","19.4","21.2","23.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","109.0","Laki - Laki","14.0","15.5","16.5","17.9","19.6","21.4","23.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","109.5","Laki - Laki","14.1","15.6","16.6","18.1","19.8","21.7","23.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","110.0","Laki - Laki","14.2","15.8","16.8","18.3","20.0","21.9","24.1","0-24");

        //tinggi badan __ berat badan __ laki-laki 24 - 60 bulan
        insertDataGizi("tinggi_badan","berat_badan","65.0","Laki - Laki","5.9","6.3","6.9","7.4","8.1","8.8","9.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","65.5","Laki - Laki","6.0","6.4","7.0","7.6","8.2","8.9","9.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","66.0","Laki - Laki","6.1","6.5","7.1","7.7","8.3","9.1","9.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","66.5","Laki - Laki","6.1","6.6","7.2","7.8","8.5","9.2","10.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","67.0","Laki - Laki","6.2","6.7","7.3","7.9","8.6","9.4","10.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","67.5","Laki - Laki","6.3","6.8","7.4","8.0","8.7","9.5","10.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","68.0","Laki - Laki","6.4","6.9","7.5","8.1","8.8","9.6","10.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","68.5","Laki - Laki","6.5","7.0","7.6","8.2","9.0","9.8","10.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","69.0","Laki - Laki","6.6","7.1","7.7","8.4","9.1","9.9","10.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","69.5","Laki - Laki","6.7","7.2","7.8","8.5","9.2","10.0","11.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","70.0","Laki - Laki","6.8","7.3","7.9","8.6","9.3","10.2","11.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","70.5","Laki - Laki","6.9","7.4","8.0","8.7","9.5","10.3","11.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","71.0","Laki - Laki","6.9","7.5","8.1","8.8","9.6","10.4","11.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","71.5","Laki - Laki","7.0","7.6","8.2","8.9","9.7","10.6","11.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","72.0","Laki - Laki","7.1","7.7","8.3","9.0","9.8","10.7","11.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","72.5","Laki - Laki","7.2","7.8","8.4","9.1","9.9","10.8","11.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","73.0","Laki - Laki","7.3","7.9","8.5","9.2","10.0","11.0","12.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","73.5","Laki - Laki","7.4","7.9","8.6","9.3","10.2","11.1","12.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","74.0","Laki - Laki","7.4","8.0","8.7","9.4","10.3","11.2","12.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","74.5","Laki - Laki","7.5","8.1","8.8","9.5","10.4","11.3","12.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","75.0","Laki - Laki","7.6","8.2","8.9","9.6","10.5","11.4","12.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","75.5","Laki - Laki","7.7","8.3","9.0","9.7","10.6","11.6","12.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","76.0","Laki - Laki","7.7","8.4","9.1","9.8","10.7","11.7","12.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","76.5","Laki - Laki","7.8","8.5","9.2","9.9","10.8","11.8","12.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","77.0","Laki - Laki","7.9","8.5","9.2","10.0","10.9","11.9","13.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","77.5","Laki - Laki","8.0","8.6","9.3","10.1","11.0","12.0","13.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","78.0","Laki - Laki","8.0","8.7","9.4","10.2","11.1","12.1","13.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","78.5","Laki - Laki","8.1","8.8","9.5","10.3","11.2","12.2","13.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","79.0","Laki - Laki","8.2","8.8","9.6","10.4","11.3","12.3","13.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","79.5","Laki - Laki","8.3","8.9","9.7","10.5","11.4","12.4","13.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","80.0","Laki - Laki","8.3","9.0","9.7","10.6","11.5","12.6","13.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","80.5","Laki - Laki","8.4","9.1","9.8","10.7","11.6","12.7","13.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","81.0","Laki - Laki","8.5","9.2","9.9","10.8","11.7","12.8","14.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","81.5","Laki - Laki","8.6","9.3","10.0","10.9","11.8","12.9","14.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","82.0","Laki - Laki","8.7","9.3","10.1","11.0","11.9","13.0","14.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","82.5","Laki - Laki","8.7","9.4","10.2","11.1","12.1","13.1","14.4","24-60");

        insertDataGizi("tinggi_badan","berat_badan","83.0","Laki - Laki","8.8","9.5","10.3","11.2","12.2","13.3","14.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","83.5","Laki - Laki","8.9","9.6","10.4","11.3","12.3","13.4","14.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","84.0","Laki - Laki","9.0","9.7","10.5","11.4","12.4","13.5","14.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","84.5","Laki - Laki","9.1","9.9","10.7","11.5","12.5","13.7","14.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","85.0","Laki - Laki","9.2","10.0","10.8","11.7","12.7","13.8","15.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","85.5","Laki - Laki","9.3","10.1","10.9","11.8","12.8","13.9","15.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","86.0","Laki - Laki","9.4","10.2","11.0","11.9","12.9","14.1","15.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","86.5","Laki - Laki","9.5","10.3","11.1","12.0","13.1","14.2","15.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","87.0","Laki - Laki","9.6","10.4","11.2","12.2","13.2","14.4","15.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","87.5","Laki - Laki","9.7","10.5","11.3","12.3","13.3","14.5","15.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","88.0","Laki - Laki","9.8","10.6","11.5","12.4","13.5","14.7","16.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","88.5","Laki - Laki","9.9","10.7","11.6","12.5","13.6","14.8","16.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","89.0","Laki - Laki","10.0","10.8","11.7","12.6","13.7","14.9","16.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","89.5","Laki - Laki","10.1","10.9","11.8","12.8","13.9","15.1","16.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","90.0","Laki - Laki","10.2","11.0","11.9","12.9","14.0","15.2","16.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","90.5","Laki - Laki","10.3","11.1","12.0","13.0","14.1","15.3","16.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","91.0","Laki - Laki","10.4","11.2","12.1","13.1","14.2","15.5","16.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","91.5","Laki - Laki","10.5","11.3","12.2","13.2","14.4","15.6","17.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","92.0","Laki - Laki","10.6","11.4","12.3","13.4","14.5","15.8","17.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","92.5","Laki - Laki","10.7","11.5","12.4","13.5","14.6","15.9","17.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","93.0","Laki - Laki","10.8","11.6","12.6","13.6","14.7","16.0","17.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","93.5","Laki - Laki","10.9","11.7","12.7","13.7","14.9","16.2","17.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","94.0","Laki - Laki","11.0","11.8","12.8","13.8","15.0","16.3","17.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","94.5","Laki - Laki","11.1","11.9","12.9","13.9","15.1","16.5","17.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","95.0","Laki - Laki","11.1","12.0","13.0","14.1","15.3","16.6","18.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","95.5","Laki - Laki","11.2","12.1","13.1","14.2","15.4","16.7","18.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","96.0","Laki - Laki","11.3","12.2","13.2","14.3","15.5","16.9","18.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","96.5","Laki - Laki","11.4","12.3","13.3","14.4","15.7","17.0","18.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","97.0","Laki - Laki","11.5","12.4","13.4","14.6","15.8","17.2","18.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","97.5","Laki - Laki","11.6","12.5","13.6","14.7","15.9","17.4","18.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","98.0","Laki - Laki","11.7","12.6","13.7","14.8","16.1","17.5","19.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","98.5","Laki - Laki","11.8","12.8","13.8","14.9","16.2","17.7","19.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","99.0","Laki - Laki","11.9","12.9","13.9","15.1","16.4","17.9","19.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","99.5","Laki - Laki","12.0","13.0","14.0","15.2","16.5","18.0","19.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","100.0","Laki - Laki","12.1","13.1","14.2","15.4","16.7","18.2","19.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","100.5","Laki - Laki","12.2","13.2","14.3","15.5","16.9","18.4","20.1","24-60");

        insertDataGizi("tinggi_badan","berat_badan","101.0","Laki - Laki","12.3","13.3","14.4","15.6","17.0","18.5","20.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","101.5","Laki - Laki","12.4","13.4","14.5","15.8","17.2","18.7","20.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","102.0","Laki - Laki","12.5","13.6","14.7","15.9","17.3","18.9","20.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","102.5","Laki - Laki","12.6","13.7","14.8","16.1","17.5","19.1","20.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","103.0","Laki - Laki","12.8","13.8","14.9","16.2","17.7","19.3","21.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","103.5","Laki - Laki","12.9","13.9","15.1","16.4","17.8","19.5","21.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","104.0","Laki - Laki","13.0","14.0","15.2","16.5","18.0","19.7","21.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","104.5","Laki - Laki","13.1","14.2","15.4","16.7","18.2","19.9","21.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","105.0","Laki - Laki","13.2","14.3","15.5","16.8","18.4","20.1","22.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","105.5","Laki - Laki","13.3","14.4","15.6","17.0","18.5","20.3","22.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","106.0","Laki - Laki","13.4","14.5","15.8","17.2","18.7","20.5","22.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","106.5","Laki - Laki","13.5","14.7","15.9","17.3","18.9","20.7","22.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","107.0","Laki - Laki","13.7","14.8","16.1","17.5","19.1","20.9","22.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","107.5","Laki - Laki","13.8","14.9","16.2","17.7","19.3","21.1","23.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","108.0","Laki - Laki","13.9","15.1","16.4","17.8","19.5","21.3","23.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","108.5","Laki - Laki","14.0","15.2","16.5","18.0","19.7","21.5","23.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","109.0","Laki - Laki","14.1","15.3","16.7","18.2","19.8","21.8","23.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","109.5","Laki - Laki","14.3","15.5","16.8","18.3","20.0","22.0","24.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","110.0","Laki - Laki","14.4","15.6","17.0","18.5","20.2","22.2","24.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","110.5","Laki - Laki","14.5","15.8","17.1","18.7","20.4","22.4","24.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","111.0","Laki - Laki","14.6","15.9","17.3","18.9","20.7","22.7","25.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","111.5","Laki - Laki","14.8","16.0","17.5","19.1","20.9","22.9","25.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","112.0","Laki - Laki","14.9","16.2","17.6","19.2","21.1","23.1","25.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","112.5","Laki - Laki","15.0","16.3","17.8","19.4","21.3","23.4","25.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","113.0","Laki - Laki","15.2","16.5","18.0","19.6","21.5","23.6","26.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","113.5","Laki - Laki","15.3","16.6","18.1","19.8","21.7","23.9","26.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","114.0","Laki - Laki","15.4","16.8","18.3","20.0","21.9","24.1","26.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","114.5","Laki - Laki","15.6","16.9","18.5","20.2","22.1","24.4","26.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","115.0","Laki - Laki","15.7","17.1","18.6","20.4","22.4","24.6","27.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","115.5","Laki - Laki","15.8","17.2","18.8","20.6","22.6","24.9","27.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","116.0","Laki - Laki","16.0","17.4","19.0","20.8","22.8","25.1","27.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","116.5","Laki - Laki","16.1","17.5","19.2","21.0","23.0","25.4","28.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","117.0","Laki - Laki","16.2","17.7","19.3","21.2","23.3","25.6","28.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","117.5","Laki - Laki","16.4","17.9","19.5","21.4","23.5","25.9","28.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","118.0","Laki - Laki","16.5","18.0","19.7","21.6","23.7","26.1","28.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","118.5","Laki - Laki","16.7","18.2","19.9","21.8","23.9","26.4","29.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","119.0","Laki - Laki","16.8","18.3","20.0","22.0","24.1","26.6","29.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","119.5","Laki - Laki","16.9","18.5","20.2","22.2","24.4","26.9","29.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","120.0","Laki - Laki","17.1","18.6","20.4","22.4","24.6","27.2","30.1","24-60");

        //umur __ index massa tubuh __ laki-laki 0-24 bulan
        insertDataGizi("umur","index_massa_tubuh","0","Laki - Laki","10.2","11.1","12.2","13.4","14.8","16.3","18.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","1","Laki - Laki","11.3","12.4","13.6","14.9","16.3","17.8","19.4","0-24");
        insertDataGizi("umur","index_massa_tubuh","2","Laki - Laki","12.5","13.7","15.0","16.3","17.8","19.4","21.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","3","Laki - Laki","13.1","14.3","15.5","16.9","18.4","20.0","21.8","0-24");
        insertDataGizi("umur","index_massa_tubuh","4","Laki - Laki","13.4","14.5","15.8","17.2","18.7","20.3","22.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","5","Laki - Laki","13.5","14.7","15.9","17.3","18.8","20.5","22.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","6","Laki - Laki","13.6","14.7","16.0","17.3","18.8","20.5","22.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","7","Laki - Laki","13.7","14.8","16.0","17.3","18.8","20.5","22.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","8","Laki - Laki","13.6","14.7","15.9","17.3","18.7","20.4","22.2","0-24");
        insertDataGizi("umur","index_massa_tubuh","9","Laki - Laki","13.6","14.7","15.8","17.2","18.6","20.3","22.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","10","Laki - Laki","13.5","14.6","15.7","17.0","18.5","20.1","22.0","0-24");
        insertDataGizi("umur","index_massa_tubuh","11","Laki - Laki","13.4","14.5","15.6","16.9","18.4","20.0","21.9","0-24");
        insertDataGizi("umur","index_massa_tubuh","12","Laki - Laki","13.4","14.4","15.5","16.8","18.2","19.8","21.7","0-24");
        insertDataGizi("umur","index_massa_tubuh","13","Laki - Laki","13.3","14.3","15.4","16.7","18.1","19.7","21.6","0-24");
        insertDataGizi("umur","index_massa_tubuh","14","Laki - Laki","13.2","14.2","15.3","16.6","18.0","19.5","21.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","15","Laki - Laki","13.1","14.1","15.2","16.4","17.8","19.4","21.2","0-24");
        insertDataGizi("umur","index_massa_tubuh","16","Laki - Laki","13.1","14.0","15.1","16.3","17.7","19.3","21.0","0-24");
        insertDataGizi("umur","index_massa_tubuh","17","Laki - Laki","13.0","13.9","15.0","16.2","17.6","19.1","20.9","0-24");
        insertDataGizi("umur","index_massa_tubuh","18","Laki - Laki","12.9","13.9","14.9","16.1","17.5","19.0","20.8","0-24");
        insertDataGizi("umur","index_massa_tubuh","19","Laki - Laki","12.9","13.8","14.9","16.1","17.4","18.9","20.7","0-24");
        insertDataGizi("umur","index_massa_tubuh","20","Laki - Laki","12.8","13.7","14.8","16.0","17.3","18.8","20.6","0-24");
        insertDataGizi("umur","index_massa_tubuh","21","Laki - Laki","12.8","13.7","14.7","15.9","17.2","18.7","20.5","0-24");
        insertDataGizi("umur","index_massa_tubuh","22","Laki - Laki","12.7","13.6","14.7","15.8","17.2","18.7","20.4","0-24");
        insertDataGizi("umur","index_massa_tubuh","23","Laki - Laki","12.7","13.6","14.6","15.8","17.1","18.6","20.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","24","Laki - Laki","12.7","13.6","14.6","15.7","17.0","18.5","20.3","0-24");

        //24 - 60 bulan
        insertDataGizi("umur","index_massa_tubuh","24","Laki - Laki","12.9","13.8","14.8","16.0","17.3","18.9","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","25","Laki - Laki","12.8","13.8","14.8","16.0","17.3","18.8","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","26","Laki - Laki","12.8","13.7","14.8","15.9","17.3","18.8","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","27","Laki - Laki","12.7","13.7","14.7","15.9","17.2","18.7","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","28","Laki - Laki","12.7","13.6","14.7","15.9","17.2","18.7","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","29","Laki - Laki","12.7","13.6","14.7","15.8","17.1","18.6","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","30","Laki - Laki","12.6","13.6","14.6","15.8","17.1","18.6","20.2","24-60");
        insertDataGizi("umur","index_massa_tubuh","31","Laki - Laki","12.6","13.5","14.6","15.8","17.1","18.5","20.2","24-60");
        insertDataGizi("umur","index_massa_tubuh","32","Laki - Laki","12.5","13.5","14.6","15.7","17.0","18.5","20.1","24-60");
        insertDataGizi("umur","index_massa_tubuh","33","Laki - Laki","12.5","13.5","14.5","15.7","17.0","18.5","20.1","24-60");
        insertDataGizi("umur","index_massa_tubuh","34","Laki - Laki","12.5","13.4","14.5","15.7","17.0","18.4","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","35","Laki - Laki","12.4","13.4","14.5","15.6","16.9","18.4","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","36","Laki - Laki","12.4","13.4","14.4","15.6","16.9","18.4","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","37","Laki - Laki","12.4","13.3","14.4","15.6","16.9","18.3","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","38","Laki - Laki","12.3","13.3","14.4","15.5","16.8","18.3","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","39","Laki - Laki","12.3","13.3","14.3","15.5","16.8","18.3","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","40","Laki - Laki","12.3","13.2","14.3","15.5","16.8","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","41","Laki - Laki","12.2","13.2","14.3","15.5","16.8","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","42","Laki - Laki","12.2","13.2","14.3","15.4","16.8","18.2","19.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","43","Laki - Laki","12.2","13.2","14.2","15.4","16.7","18.2","19.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","44","Laki - Laki","12.2","13.1","14.2","15.4","16.7","18.2","19.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","45","Laki - Laki","12.2","13.1","14.2","15.4","16.7","18.2","19.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","46","Laki - Laki","12.1","13.1","14.2","15.4","16.7","18.2","19.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","47","Laki - Laki","12.1","13.1","14.2","15.3","16.7","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","48","Laki - Laki","12.1","13.1","14.1","15.3","16.7","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","49","Laki - Laki","12.1","13.0","14.1","15.3","16.7","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","50","Laki - Laki","12.1","13.0","14.1","15.3","16.7","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","51","Laki - Laki","12.1","13.0","14.1","15.3","16.6","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","52","Laki - Laki","12.0","13.0","14.1","15.3","16.6","18.2","19.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","53","Laki - Laki","12.0","13.0","14.1","15.3","16.6","18.2","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","54","Laki - Laki","12.0","13.0","14.0","15.3","16.6","18.2","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","55","Laki - Laki","12.0","13.0","14.0","15.2","16.6","18.2","20.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","56","Laki - Laki","12.0","12.9","14.0","15.2","16.6","18.2","20.1","24-60");
        insertDataGizi("umur","index_massa_tubuh","57","Laki - Laki","12.0","12.9","14.0","15.2","16.6","18.2","20.1","24-60");
        insertDataGizi("umur","index_massa_tubuh","58","Laki - Laki","12.0","12.9","14.0","15.2","16.6","18.3","20.2","24-60");
        insertDataGizi("umur","index_massa_tubuh","59","Laki - Laki","12.0","12.9","14.0","15.2","16.6","18.3","20.2","24-60");
        insertDataGizi("umur","index_massa_tubuh","60","Laki - Laki","12.0","12.9","14.0","15.2","16.6","18.3","20.3","24-60");

        //umur __ berat badan __ perempuan 0 - 60 bulan
        insertDataGizi("umur","berat_badan","0","Perempuan","2.0","2.4","2.8","3.2","3.7","4.2","4.8","0-60");
        insertDataGizi("umur","berat_badan","1","Perempuan","2.7","3.2","3.6","4.2","4.8","5.5","6.2","0-60");
        insertDataGizi("umur","berat_badan","2","Perempuan","3.4","3.9","4.5","5.1","5.8","6.6","7.5","0-60");
        insertDataGizi("umur","berat_badan","3","Perempuan","4.0","4.5","5.2","5.8","6.6","7.5","8.5","0-60");
        insertDataGizi("umur","berat_badan","4","Perempuan","4.4","5.0","5.7","6.4","7.3","8.2","9.3","0-60");
        insertDataGizi("umur","berat_badan","5","Perempuan","4.8","5.4","6.1","6.9","7.8","8.8","10.0","0-60");
        insertDataGizi("umur","berat_badan","6","Perempuan","5.1","5.7","6.5","7.3","8.3","9.3","10.6","0-60");
        insertDataGizi("umur","berat_badan","7","Perempuan","5.3","6.0","6.8","7.6","8.6","9.8","11.1","0-60");
        insertDataGizi("umur","berat_badan","8","Perempuan","5.6","6.3","7.0","7.9","9.0","10.2","11.6","0-60");
        insertDataGizi("umur","berat_badan","9","Perempuan","5.8","6.5","7.3","8.3","9.3","10.5","12.0","0-60");
        insertDataGizi("umur","berat_badan","10","Perempuan","5.9","6.7","7.5","8.5","9.6","10.9","12.4","0-60");
        insertDataGizi("umur","berat_badan","11","Perempuan","6.1","6.9","7.7","8.7","9.9","11.2","12.8","0-60");
        insertDataGizi("umur","berat_badan","12","Perempuan","6.3","7.0","7.9","8.9","10.1","11.5","13.1","0-60");
        insertDataGizi("umur","berat_badan","13","Perempuan","6.4","7.2","8.1","9.2","10.4","11.8","13.5","0-60");
        insertDataGizi("umur","berat_badan","14","Perempuan","6.6","7.4","8.3","9.4","10.6","12.1","13.8","0-60");
        insertDataGizi("umur","berat_badan","15","Perempuan","6.7","7.6","8.5","9.6","10.9","12.4","14.1","0-60");
        insertDataGizi("umur","berat_badan","16","Perempuan","6.9","7.7","8.7","9.8","11.1","12.6","14.5","0-60");
        insertDataGizi("umur","berat_badan","17","Perempuan","7.0","7.9","8.9","10.0","11.4","12.9","14.8","0-60");
        insertDataGizi("umur","berat_badan","18","Perempuan","7.2","8.1","9.1","10.2","11.6","13.2","15.1","0-60");
        insertDataGizi("umur","berat_badan","19","Perempuan","7.3","8.2","9.2","10.4","11.8","13.5","15.4","0-60");
        insertDataGizi("umur","berat_badan","20","Perempuan","7.5","8.4","9.4","10.6","12.1","13.7","15.7","0-60");
        insertDataGizi("umur","berat_badan","21","Perempuan","7.6","8.6","9.6","10.9","12.3","14.0","16.0","0-60");
        insertDataGizi("umur","berat_badan","22","Perempuan","7.8","8.7","9.8","11.1","12.5","14.3","16.4","0-60");
        insertDataGizi("umur","berat_badan","23","Perempuan","7.9","8.9","10.0","11.3","12.8","14.6","16.7","0-60");
        insertDataGizi("umur","berat_badan","24","Perempuan","8.1","9.0","10.2","11.5","13.0","14.8","17.0","0-60");
        insertDataGizi("umur","berat_badan","25","Perempuan","8.2","9.2","10.3","11.7","13.3","15.1","17.3","0-60");
        insertDataGizi("umur","berat_badan","26","Perempuan","8.4","9.4","10.5","11.9","13.5","15.4","17.7","0-60");
        insertDataGizi("umur","berat_badan","27","Perempuan","8.5","9.5","10.7","12.1","13.7","15.7","18.0","0-60");
        insertDataGizi("umur","berat_badan","28","Perempuan","8.6","9.7","10.9","12.3","14.0","16.0","18.3","0-60");
        insertDataGizi("umur","berat_badan","29","Perempuan","8.8","9.8","11.1","12.5","14.2","16.2","18.7","0-60");
        insertDataGizi("umur","berat_badan","30","Perempuan","8.9","10.0","11.2","12.7","14.4","16.5","19.0","0-60");
        insertDataGizi("umur","berat_badan","31","Perempuan","9.0","10.1","11.4","12.9","14.7","16.8","19.3","0-60");
        insertDataGizi("umur","berat_badan","32","Perempuan","9.1","10.3","11.6","13.1","14.9","17.1","19.6","0-60");
        insertDataGizi("umur","berat_badan","33","Perempuan","9.3","10.4","11.7","13.3","15.1","17.3","20.0","0-60");
        insertDataGizi("umur","berat_badan","34","Perempuan","9.4","10.5","11.9","13.5","15.4","17.6","20.3","0-60");
        insertDataGizi("umur","berat_badan","35","Perempuan","9.5","10.7","12.0","13.7","15.6","17.9","20.6","0-60");

        insertDataGizi("umur","berat_badan","36","Perempuan","9.6","10.8","12.2","13.9","15.8","18.1","20.9","0-60");
        insertDataGizi("umur","berat_badan","37","Perempuan","9.7","10.9","12.4","14.0","16.0","18.4","21.3","0-60");
        insertDataGizi("umur","berat_badan","38","Perempuan","9.8","11.1","12.5","14.2","16.3","18.7","21.6","0-60");
        insertDataGizi("umur","berat_badan","39","Perempuan","9.9","11.2","12.7","14.4","16.5","19.0","22.0","0-60");
        insertDataGizi("umur","berat_badan","40","Perempuan","10.1","11.3","12.8","14.6","16.7","19.2","22.3","0-60");
        insertDataGizi("umur","berat_badan","41","Perempuan","10.2","11.5","13.0","14.8","16.9","19.5","22.7","0-60");
        insertDataGizi("umur","berat_badan","42","Perempuan","10.3","11.6","13.1","15.0","17.2","19.8","23.0","0-60");
        insertDataGizi("umur","berat_badan","43","Perempuan","10.4","11.7","13.3","15.2","17.4","20.1","23.4","0-60");
        insertDataGizi("umur","berat_badan","44","Perempuan","10.5","11.8","13.4","15.3","17.6","20.4","23.7","0-60");
        insertDataGizi("umur","berat_badan","45","Perempuan","10.6","12.0","13.6","15.5","17.8","20.7","24.1","0-60");
        insertDataGizi("umur","berat_badan","46","Perempuan","10.7","12.1","13.7","15.7","18.1","20.9","24.5","0-60");
        insertDataGizi("umur","berat_badan","47","Perempuan","10.8","12.2","13.9","15.9","18.3","21.2","24.8","0-60");
        insertDataGizi("umur","berat_badan","48","Perempuan","10.9","12.3","14.0","16.1","18.5","21.5","25.2","0-60");
        insertDataGizi("umur","berat_badan","49","Perempuan","11.0","12.4","14.2","16.3","18.8","21.8","25.5","0-60");
        insertDataGizi("umur","berat_badan","50","Perempuan","11.1","12.6","14.3","16.4","19.0","22.1","25.9","0-60");
        insertDataGizi("umur","berat_badan","51","Perempuan","11.2","12.7","14.5","16.6","19.2","22.4","26.3","0-60");
        insertDataGizi("umur","berat_badan","52","Perempuan","11.3","12.8","14.6","16.8","19.4","22.6","26.6","0-60");
        insertDataGizi("umur","berat_badan","53","Perempuan","11.4","12.9","14.8","17.0","19.7","22.9","27.0","0-60");
        insertDataGizi("umur","berat_badan","54","Perempuan","11.5","13.0","14.9","17.2","19.9","23.2","27.4","0-60");
        insertDataGizi("umur","berat_badan","55","Perempuan","11.6","13.2","15.1","17.3","20.1","23.5","27.7","0-60");
        insertDataGizi("umur","berat_badan","56","Perempuan","11.7","13.3","15.2","17.5","20.3","23.8","28.1","0-60");
        insertDataGizi("umur","berat_badan","57","Perempuan","11.8","13.4","15.3","17.7","20.6","24.1","28.5","0-60");
        insertDataGizi("umur","berat_badan","58","Perempuan","11.9","13.5","15.5","17.9","20.8","24.4","28.8","0-60");
        insertDataGizi("umur","berat_badan","59","Perempuan","12.0","13.6","15.6","18.0","20.0","24.6","29.2","0-60");
        insertDataGizi("umur","berat_badan","60","Perempuan","12.1","13.7","15.8","18.2","20.2","24.9","29.5","0-60");

        //umur __ panjang badan __ perempuan 0 - 24 bulan
        insertDataGizi("umur","panjang_badan","0","Perempuan","43.6","45.4","47.3","49.1","51.0","52.9","54.7","0-24");
        insertDataGizi("umur","panjang_badan","1","Perempuan","47.8","49.8","51.7","53.7","55.6","57.6","59.5","0-24");
        insertDataGizi("umur","panjang_badan","2","Perempuan","51.0","53.0","55.0","57.1","59.1","61.1","63.2","0-24");
        insertDataGizi("umur","panjang_badan","3","Perempuan","53.5","55.6","57.7","59.8","61.9","64.0","66.1","0-24");
        insertDataGizi("umur","panjang_badan","4","Perempuan","55.6","57.8","59.9","63.1","64.3","66.4","68.6","0-24");
        insertDataGizi("umur","panjang_badan","5","Perempuan","57.4","59.6","61.8","65.0","66.2","68.5","70.7","0-24");
        insertDataGizi("umur","panjang_badan","6","Perempuan","58.9","61.2","63.5","66.7","68.0","70.3","72.5","0-24");
        insertDataGizi("umur","panjang_badan","7","Perempuan","60.3","62.7","65.0","67.3","69.6","71.9","74.2","0-24");
        insertDataGizi("umur","panjang_badan","8","Perempuan","61.7","64.0","66.4","68.7","71.1","73.5","75.8","0-24");
        insertDataGizi("umur","panjang_badan","9","Perempuan","62.9","65.3","67.7","70.1","72.6","75.0","77.4","0-24");
        insertDataGizi("umur","panjang_badan","10","Perempuan","64.1","66.5","69.0","71.5","73.9","76.4","78.9","0-24");
        insertDataGizi("umur","panjang_badan","11","Perempuan","65.2","67.7","70.3","72.8","75.3","77.8","80.3","0-24");
        insertDataGizi("umur","panjang_badan","12","Perempuan","66.3","68.9","71.4","74.0","76.6","79.2","81.7","0-24");
        insertDataGizi("umur","panjang_badan","13","Perempuan","67.3","70.0","72.6","75.2","77.8","80.5","83.1","0-24");
        insertDataGizi("umur","panjang_badan","14","Perempuan","68.3","71.0","73.7","76.4","79.1","81.7","84.4","0-24");
        insertDataGizi("umur","panjang_badan","15","Perempuan","69.3","72.0","74.8","77.5","80.2","83.0","85.7","0-24");
        insertDataGizi("umur","panjang_badan","16","Perempuan","70.2","73.0","75.8","78.6","81.4","84.2","87.0","0-24");
        insertDataGizi("umur","panjang_badan","17","Perempuan","71.1","74.0","76.8","79.7","82.5","85.4","88.2","0-24");
        insertDataGizi("umur","panjang_badan","18","Perempuan","72.0","74.9","77.8","80.7","83.6","86.5","89.4","0-24");
        insertDataGizi("umur","panjang_badan","19","Perempuan","72.8","75.8","78.8","81.7","84.7","87.6","90.6","0-24");
        insertDataGizi("umur","panjang_badan","20","Perempuan","73.7","76.7","79.7","82.7","85.7","88.7","91.7","0-24");
        insertDataGizi("umur","panjang_badan","21","Perempuan","74.5","77.5","80.6","83.7","86.7","89.8","92.9","0-24");
        insertDataGizi("umur","panjang_badan","22","Perempuan","75.2","78.4","82.5","84.6","87.7","90.8","94.0","0-24");
        insertDataGizi("umur","panjang_badan","23","Perempuan","76.0","79.2","83.3","85.5","88.7","91.9","95.0","0-24");
        insertDataGizi("umur","panjang_badan","24","Perempuan","76.7","80.0","84.2","86.4","89.6","92.9","96.1","0-24");

        //umur __ tinggi badan __ perempuan 24-60 bulan
        insertDataGizi("umur","tinggi_badan","24","Perempuan","76.0","79.3","82.5","85.7","88.9","92.2","95.4","24-60");
        insertDataGizi("umur","tinggi_badan","25","Perempuan","76.8","80.0","83.3","86.6","89.9","93.1","96.4","24-60");
        insertDataGizi("umur","tinggi_badan","26","Perempuan","77.5","80.8","84.1","87.4","90.8","94.1","97.4","24-60");
        insertDataGizi("umur","tinggi_badan","27","Perempuan","78.1","81.5","84.9","88.3","91.7","95.0","98.4","24-60");
        insertDataGizi("umur","tinggi_badan","28","Perempuan","78.8","82.2","85.7","89.1","92.5","96.0","99.4","24-60");
        insertDataGizi("umur","tinggi_badan","29","Perempuan","79.5","82.9","86.4","89.9","93.4","96.9","19.3","24-60");
        insertDataGizi("umur","tinggi_badan","30","Perempuan","80.1","83.6","87.1","90.7","94.2","97.7","19.3","24-60");
        insertDataGizi("umur","tinggi_badan","31","Perempuan","80.7","84.3","87.9","91.4","95.0","98.6","19.2","24-60");
        insertDataGizi("umur","tinggi_badan","32","Perempuan","81.3","84.9","88.6","92.2","95.8","99.4","19.1","24-60");
        insertDataGizi("umur","tinggi_badan","33","Perempuan","81.9","85.6","89.3","92.9","96.6","100.3","19.9","24-60");
        insertDataGizi("umur","tinggi_badan","34","Perempuan","82.5","86.2","89.9","93.6","97.4","101.1","19.8","24-60");
        insertDataGizi("umur","tinggi_badan","35","Perempuan","83.1","86.8","90.6","94.4","98.1","101.9","19.6","24-60");
        insertDataGizi("umur","tinggi_badan","36","Perempuan","83.6","87.4","91.2","95.1","98.9","102.7","19.5","24-60");
        insertDataGizi("umur","tinggi_badan","37","Perempuan","84.2","88.0","91.9","95.7","99.6","103.4","19.3","24-60");
        insertDataGizi("umur","tinggi_badan","38","Perempuan","84.7","88.6","92.5","96.4","100.3","104.2","19.1","24-60");
        insertDataGizi("umur","tinggi_badan","39","Perempuan","85.3","89.2","93.1","97.1","101.0","105.0","19.9","24-60");
        insertDataGizi("umur","tinggi_badan","40","Perempuan","85.8","99.8","93.8","97.7","101.7","105.7","19.7","24-60");
        insertDataGizi("umur","tinggi_badan","41","Perempuan","86.3","90.4","94.4","98.4","102.4","106.4","110.5","24-60");
        insertDataGizi("umur","tinggi_badan","42","Perempuan","86.8","90.9","95.0","99.0","103.1","107.2","111.2","24-60");
        insertDataGizi("umur","tinggi_badan","43","Perempuan","87.4","91.5","95.6","99.7","103.8","107.9","112.0","24-60");
        insertDataGizi("umur","tinggi_badan","44","Perempuan","87.9","92.0","96.2","100.3","104.5","108.6","112.7","24-60");
        insertDataGizi("umur","tinggi_badan","45","Perempuan","88.4","92.5","96.7","100.9","105.1","109.3","113.5","24-60");
        insertDataGizi("umur","tinggi_badan","46","Perempuan","88.9","93.1","97.3","101.5","105.8","110.0","114.2","24-60");
        insertDataGizi("umur","tinggi_badan","47","Perempuan","89.3","93.6","97.9","102.1","106.4","110.7","114.9","24-60");
        insertDataGizi("umur","tinggi_badan","48","Perempuan","89.8","94.1","98.4","102.7","107.0","111.3","115.7","24-60");
        insertDataGizi("umur","tinggi_badan","49","Perempuan","90.3","94.6","99.0","103.3","107.7","112.0","116.4","24-60");
        insertDataGizi("umur","tinggi_badan","50","Perempuan","90.7","95.1","99.5","103.9","108.3","112.7","117.1","24-60");
        insertDataGizi("umur","tinggi_badan","51","Perempuan","91.2","95.6","100.1","104.5","108.9","113.3","117.7","24-60");
        insertDataGizi("umur","tinggi_badan","52","Perempuan","91.7","96.1","100.6","105.0","109.5","114.0","118.4","24-60");
        insertDataGizi("umur","tinggi_badan","53","Perempuan","92.1","96.6","101.1","105.6","110.1","114.6","119.1","24-60");
        insertDataGizi("umur","tinggi_badan","54","Perempuan","92.6","97.1","101.6","106.2","110.7","115.2","119.8","24-60");
        insertDataGizi("umur","tinggi_badan","55","Perempuan","93.0","97.6","102.2","106.7","111.3","115.9","120.4","24-60");
        insertDataGizi("umur","tinggi_badan","56","Perempuan","93.4","98.1","102.7","107.3","111.9","116.5","121.1","24-60");
        insertDataGizi("umur","tinggi_badan","57","Perempuan","93.9","98.5","103.2","107.8","112.5","117.1","121.8","24-60");
        insertDataGizi("umur","tinggi_badan","58","Perempuan","94.3","99.0","103.7","108.4","113.0","117.7","122.4","24-60");
        insertDataGizi("umur","tinggi_badan","59","Perempuan","94.7","99.5","104.2","108.9","113.6","118.3","123.1","24-60");
        insertDataGizi("umur","tinggi_badan","60","Perempuan","95.2","99.9","104.7","109.4","114.2","118.9","123.7","24-60");

        //panjang badan __ berat badan __ perempuan 0 -24 bula
        insertDataGizi("panjang_badan","berat_badan","45.0","Perempuan","1.9","2.1","2.3","2.5","2.7","3.0","3.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","45.5","Perempuan","2.0","2.1","2.3","2.5","2.8","3.1","3.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","46.0","Perempuan","2.0","2.2","2.4","2.6","2.9","3.2","3.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","46.5","Perempuan","2.1","2.3","2.5","2.7","3.0","3.3","3.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","47.0","Perempuan","2.2","2.4","2.6","2.8","3.1","3.4","3.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","47.5","Perempuan","2.2","2.4","2.6","2.9","3.2","3.5","3.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","48.0","Perempuan","2.3","2.5","2.7","3.0","3.3","3.6","4.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","48.5","Perempuan","2.4","2.6","2.8","3.1","3.4","3.7","4.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","49.0","Perempuan","2.4","2.6","2.9","3.2","3.5","3.8","4.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","49.5","Perempuan","2.5","2.7","3.0","3.3","3.6","3.9","4.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","50.0","Perempuan","2.6","2.8","3.1","3.4","3.7","4.0","4.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","50.5","Perempuan","2.7","2.9","3.2","3.5","3.8","4.2","4.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","51.0","Perempuan","2.8","3.0","3.3","3.6","3.9","4.3","4.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","51.5","Perempuan","2.8","3.1","3.4","3.7","4.0","4.4","4.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","52.0","Perempuan","2.9","3.2","3.5","3.8","4.2","4.6","5.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","52.5","Perempuan","3.0","3.3","3.6","3.9","4.3","4.7","5.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","53.0","Perempuan","3.1","3.4","3.7","4.0","4.4","4.9","5.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","53.5","Perempuan","3.2","3.5","3.8","4.2","4.6","5.0","5.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","54.0","Perempuan","3.3","3.6","3.9","4.3","4.7","5.2","5.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","54.5","Perempuan","3.4","3.7","4.0","4.4","4.8","5.3","5.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","55.0","Perempuan","3.5","3.8","4.2","4.5","5.0","5.5","6.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","55.5","Perempuan","3.6","3.9","4.3","4.7","5.1","5.7","6.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","56.0","Perempuan","3.7","4.0","4.4","4.8","5.3","5.8","6.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","56.5","Perempuan","3.8","4.1","4.5","5.0","5.4","6.0","6.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","57.0","Perempuan","3.9","4.3","4.6","5.1","5.6","6.1","6.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","57.5","Perempuan","4.0","4.4","4.8","5.2","5.7","6.3","7.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","58.0","Perempuan","4.1","4.5","4.9","5.4","5.9","6.5","7.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","58.5","Perempuan","4.2","4.6","5.0","5.5","6.0","6.6","7.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","59.0","Perempuan","4.3","4.7","5.1","5.6","6.2","6.8","7.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","59.5","Perempuan","4.4","4.8","5.3","5.7","6.3","6.9","7.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","60.0","Perempuan","4.5","4.9","5.4","5.9","6.4","7.1","7.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","60.5","Perempuan","4.6","5.0","5.5","6.0","6.6","7.3","8.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","61.0","Perempuan","4.7","5.1","5.6","6.1","6.7","7.4","8.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","61.5","Perempuan","4.8","5.2","5.7","6.3","6.9","7.6","8.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","62.0","Perempuan","4.9","5.3","5.8","6.4","7.0","7.7","8.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","62.5","Perempuan","5.0","5.4","5.9","6.5","7.1","7.8","8.7","0-24");

        insertDataGizi("panjang_badan","berat_badan","63.0","Perempuan","5.1","5.5","6.0","6.6","7.3","8.0","8.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","63.5","Perempuan","5.2","5.6","6.2","6.7","7.4","8.1","9.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","64.0","Perempuan","5.3","5.7","6.3","6.9","7.5","8.3","9.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","64.5","Perempuan","5.4","5.8","6.4","7.0","7.6","8.4","9.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","65.0","Perempuan","5.5","5.9","6.5","7.1","7.8","8.6","9.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","65.5","Perempuan","5.5","6.0","6.6","7.2","7.9","8.7","9.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","66.0","Perempuan","5.6","6.1","6.7","7.3","8.0","8.8","9.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","66.5","Perempuan","5.7","6.2","6.8","7.4","8.1","9.0","9.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","67.0","Perempuan","5.8","6.3","6.9","7.5","8.3","9.1","10.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","67.5","Perempuan","5.9","6.4","7.0","7.6","8.4","9.2","10.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","68.0","Perempuan","6.0","6.5","7.1","7.7","8.5","9.4","10.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","68.5","Perempuan","6.1","6.6","7.2","7.9","8.6","9.5","10.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","69.0","Perempuan","6.1","6.7","7.3","8.0","8.7","9.6","10.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","69.5","Perempuan","6.2","6.8","7.4","8.1","8.8","9.7","10.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","70.0","Perempuan","6.3","6.9","7.5","8.2","9.0","9.9","10.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","70.5","Perempuan","6.4","6.9","7.6","8.3","9.1","10.0","12.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","71.0","Perempuan","6.5","7.0","7.7","8.4","9.2","10.1","12.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","71.5","Perempuan","6.5","7.1","7.7","8.5","9.3","10.2","12.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","72.0","Perempuan","6.6","7.2","7.8","8.6","9.4","10.3","12.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","72.5","Perempuan","6.7","7.3","7.9","8.7","9.5","10.5","12.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","73.0","Perempuan","6.8","7.4","8.0","8.8","9.6","10.6","12.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","73.5","Perempuan","6.9","7.4","8.1","8.9","9.7","10.7","12.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","74.0","Perempuan","6.9","7.5","8.2","9.0","9.8","10.8","12.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","74.5","Perempuan","7.0","7.6","8.3","9.1","9.9","10.9","13.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","75.0","Perempuan","7.1","7.7","8.4","9.1","10.0","11.0","13.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","75.5","Perempuan","7.1","7.8","8.5","9.2","10.1","11.1","13.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","76.0","Perempuan","7.2","7.8","8.5","9.3","10.2","11.2","13.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","76.5","Perempuan","7.3","7.9","8.6","9.4","10.3","11.4","13.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","77.0","Perempuan","7.4","8.0","8.7","9.5","10.4","11.5","13.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","77.5","Perempuan","7.4","8.1","8.8","9.6","10.5","11.6","13.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","78.0","Perempuan","7.5","8.2","8.9","9.7","10.6","11.7","13.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","78.5","Perempuan","7.6","8.2","9.0","9.8","10.7","11.8","14.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","79.0","Perempuan","7.7","8.3","9.1","9.9","10.8","11.9","14.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","79.5","Perempuan","7.7","8.4","9.1","10.0","10.9","12.0","14.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","80.0","Perempuan","7.8","8.5","9.2","10.1","11.0","12.1","14.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","80.5","Perempuan","7.9","8.6","9.3","10.2","11.2","12.3","14.5","0-24");

        insertDataGizi("panjang_badan","berat_badan","81.0","Perempuan","8.0","8.7","9.4","10.3","11.3","12.4","13.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","81.5","Perempuan","8.1","8.8","9.5","10.4","11.4","12.5","13.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","82.0","Perempuan","8.1","8.8","9.6","10.5","11.5","12.6","13.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","82.5","Perempuan","8.2","8.9","9.7","10.6","11.6","12.8","14.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","83.0","Perempuan","8.3","9.0","9.8","10.7","11.8","12.9","14.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","83.5","Perempuan","8.4","9.1","9.9","10.9","11.9","13.1","14.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","84.0","Perempuan","8.5","9.2","10.1","11.0","12.0","13.2","14.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","84.5","Perempuan","8.6","9.3","10.2","11.1","12.1","13.3","14.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","85.0","Perempuan","8.7","9.4","10.3","11.2","12.3","13.5","14.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","85.5","Perempuan","8.8","9.5","10.4","11.3","12.4","13.6","15.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","86.0","Perempuan","8.9","9.7","10.5","11.5","12.6","13.8","15.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","86.5","Perempuan","9.0","9.8","10.6","11.6","12.7","13.9","15.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","87.0","Perempuan","9.1","9.9","10.7","11.7","12.8","14.1","15.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","87.5","Perempuan","9.2","10.0","10.9","11.8","13.0","14.2","15.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","88.0","Perempuan","9.3","10.1","11.0","12.0","13.1","14.4","15.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","88.5","Perempuan","9.4","10.2","11.1","12.1","13.2","14.5","16.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","89.0","Perempuan","9.5","10.3","11.2","12.2","13.4","14.7","16.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","89.5","Perempuan","9.6","10.4","11.3","12.3","13.5","14.8","16.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","90.0","Perempuan","9.7","10.5","11.4","12.5","13.7","15.0","16.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","90.5","Perempuan","9.8","10.6","11.5","12.6","13.8","15.1","16.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","91.0","Perempuan","9.9","10.7","11.7","12.7","13.9","15.3","16.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","91.5","Perempuan","10.0","10.8","11.8","12.8","14.1","15.5","17.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","92.0","Perempuan","10.1","10.9","11.9","13.0","14.2","15.6","17.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","92.5","Perempuan","10.1","11.0","12.0","13.1","14.3","15.8","17.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","93.0","Perempuan","10.2","11.1","12.1","13.2","14.5","15.9","17.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","93.5","Perempuan","10.3","11.2","12.2","13.3","14.6","16.1","17.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","94.0","Perempuan","10.4","11.3","12.3","13.5","14.7","16.2","17.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","94.5","Perempuan","10.5","11.4","12.4","13.6","14.9","16.4","18.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","95.0","Perempuan","10.6","11.5","12.6","13.7","15.0","16.5","18.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","95.5","Perempuan","10.7","11.6","12.7","13.8","15.2","16.7","18.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","96.0","Perempuan","10.8","11.7","12.8","14.0","15.3","16.8","18.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","96.5","Perempuan","10.9","11.8","12.9","14.1","15.4","17.0","18.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","97.0","Perempuan","11.0","12.0","13.0","14.2","15.6","17.1","18.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","97.5","Perempuan","11.1","12.1","13.1","14.4","15.7","17.3","19.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","98.0","Perempuan","11.2","12.2","13.3","14.5","15.9","17.5","19.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","98.5","Perempuan","11.3","12.3","13.4","14.6","16.0","17.6","19.5","0-24");

        insertDataGizi("panjang_badan","berat_badan","99.0","Perempuan","11.4","12.4","13.5","14.8","16.2","17.8","19.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","99.5","Perempuan","11.5","12.5","13.6","14.9","16.3","18.0","19.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","100.0","Perempuan","11.6","12.6","13.7","15.0","16.5","18.1","20.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","100.5","Perempuan","11.7","12.7","13.9","15.2","16.6","18.3","20.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","101.0","Perempuan","11.8","12.8","14.0","15.3","16.8","18.5","20.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","101.5","Perempuan","11.9","13.0","14.1","15.5","17.0","18.7","20.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","102.0","Perempuan","12.0","13.1","14.3","15.6","17.1","18.9","20.8","0-24");
        insertDataGizi("panjang_badan","berat_badan","102.5","Perempuan","12.1","13.2","14.4","15.8","17.3","19.0","21.0","0-24");
        insertDataGizi("panjang_badan","berat_badan","103.0","Perempuan","12.3","13.3","14.5","15.9","17.5","19.2","21.3","0-24");
        insertDataGizi("panjang_badan","berat_badan","103.5","Perempuan","12.4","13.5","14.7","16.1","17.6","19.4","21.5","0-24");
        insertDataGizi("panjang_badan","berat_badan","104.0","Perempuan","12.5","13.6","14.8","16.2","17.8","19.6","21.7","0-24");
        insertDataGizi("panjang_badan","berat_badan","104.5","Perempuan","12.6","13.7","15.0","16.4","18.0","19.8","21.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","105.0","Perempuan","12.7","13.8","15.1","16.5","18.2","20.0","22.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","105.5","Perempuan","12.8","14.0","15.3","16.7","18.4","20.2","22.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","106.0","Perempuan","13.0","14.1","15.4","16.9","18.5","20.5","22.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","106.5","Perempuan","13.1","14.3","15.6","17.1","18.7","20.7","22.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","107.0","Perempuan","13.2","14.4","15.7","17.2","18.9","20.9","23.1","0-24");
        insertDataGizi("panjang_badan","berat_badan","107.5","Perempuan","13.3","14.5","15.9","17.4","19.1","21.1","23.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","108.0","Perempuan","13.5","14.7","16.0","17.6","19.3","21.3","23.6","0-24");
        insertDataGizi("panjang_badan","berat_badan","108.5","Perempuan","13.6","14.8","16.2","17.8","19.5","21.6","23.9","0-24");
        insertDataGizi("panjang_badan","berat_badan","109.0","Perempuan","13.7","15.0","16.4","18.0","19.7","21.8","24.2","0-24");
        insertDataGizi("panjang_badan","berat_badan","109.5","Perempuan","13.9","15.1","16.5","18.1","20.0","22.0","24.4","0-24");
        insertDataGizi("panjang_badan","berat_badan","110.0","Perempuan","14.0","15.3","16.7","18.3","20.2","22.3","24.7","0-24");

        //tinggi badan __ berat badan __ perempuan 24 - 60 bulan
        insertDataGizi("tinggi_badan","berat_badan","65.0","Perempuan","5.6","6.1","6.6","7.2","7.9","8.7","9.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","65.5","Perempuan","5.7","6.2","6.7","7.4","8.1","8.9","9.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","66.0","Perempuan","5.8","6.3","6.8","7.5","8.2","9.0","10.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","66.5","Perempuan","5.8","6.4","6.9","7.6","8.3","9.1","10.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","67.0","Perempuan","5.9","6.4","7.0","7.7","8.4","9.3","10.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","67.5","Perempuan","6.0","6.5","7.1","7.8","8.5","9.4","10.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","68.0","Perempuan","6.1","6.6","7.2","7.9","8.7","9.5","10.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","68.5","Perempuan","6.2","6.7","7.3","8.0","8.8","9.7","10.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","69.0","Perempuan","6.3","6.8","7.4","8.1","8.9","9.8","10.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","69.5","Perempuan","6.3","6.9","7.5","8.2","9.0","9.9","10.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","70.0","Perempuan","6.4","7.0","7.6","8.3","9.1","10.0","11.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","70.5","Perempuan","6.5","7.1","7.7","8.4","9.2","10.1","11.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","71.0","Perempuan","6.6","7.1","7.8","8.5","9.3","10.3","11.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","71.5","Perempuan","6.7","7.2","7.9","8.6","9.4","10.4","11.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","72.0","Perempuan","6.7","7.3","8.0","8.7","9.5","10.5","11.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","72.5","Perempuan","6.8","7.4","8.1","8.8","9.7","10.6","11.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","73.0","Perempuan","6.9","7.5","8.1","8.9","9.8","10.7","11.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","73.5","Perempuan","7.0","7.6","8.2","9.0","9.9","10.8","12.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","74.0","Perempuan","7.0","7.6","8.3","9.1","00.0","11.0","12.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","74.5","Perempuan","7.1","7.7","8.4","9.2","00.1","11.1","12.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","75.0","Perempuan","7.2","7.8","8.5","9.3","00.2","11.2","12.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","75.5","Perempuan","7.2","7.9","8.6","9.4","00.3","11.3","12.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","76.0","Perempuan","7.3","8.0","8.7","9.5","00.4","11.4","12.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","76.5","Perempuan","7.4","8.0","8.7","9.6","00.5","11.5","12.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","77.0","Perempuan","7.5","8.1","8.8","9.6","00.6","11.6","12.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","77.5","Perempuan","7.5","8.2","8.9","9.7","00.7","11.7","12.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","78.0","Perempuan","7.6","8.3","9.0","9.8","00.8","11.8","13.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","78.5","Perempuan","7.7","8.4","9.1","9.9","00.9","12.0","13.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","79.0","Perempuan","7.8","8.4","9.2","10.0","11.0","12.1","13.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","79.5","Perempuan","7.8","8.5","9.3","10.1","11.1","12.2","13.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","80.0","Perempuan","7.9","8.6","9.4","10.2","11.2","12.3","13.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","80.5","Perempuan","8.0","8.7","9.5","10.3","11.3","12.4","13.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","81.0","Perempuan","8.1","8.8","9.6","10.4","11.4","12.6","13.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","81.5","Perempuan","8.2","8.9","9.7","10.6","11.6","12.7","14.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","82.0","Perempuan","8.3","9.0","9.8","10.7","11.7","12.8","14.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","82.5","Perempuan","8.4","9.1","9.9","10.8","11.8","13.0","14.3","24-60");

        insertDataGizi("tinggi_badan","berat_badan","83.0","Perempuan","8.5","9.2","10.0","10.9","11.9","13.1","14.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","83.5","Perempuan","8.5","9.3","10.1","11.0","12.1","13.3","14.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","84.0","Perempuan","8.6","9.4","10.2","11.1","12.2","13.4","14.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","84.5","Perempuan","8.7","9.5","10.3","11.3","12.3","13.5","14.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","85.0","Perempuan","8.8","9.6","10.4","11.4","12.5","13.7","15.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","85.5","Perempuan","8.9","9.7","10.6","11.5","12.6","13.8","15.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","86.0","Perempuan","9.0","9.8","10.7","11.6","12.7","14.0","15.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","86.5","Perempuan","9.1","9.9","10.8","11.8","12.9","14.2","15.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","87.0","Perempuan","9.2","10.0","10.9","11.9","13.0","14.3","15.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","87.5","Perempuan","9.3","10.1","11.0","12.0","13.2","14.5","15.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","88.0","Perempuan","9.4","10.2","11.1","12.1","13.3","14.6","16.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","88.5","Perempuan","9.5","10.3","11.2","12.3","13.4","14.8","16.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","89.0","Perempuan","9.6","10.4","11.4","12.4","13.6","14.9","16.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","89.5","Perempuan","9.7","10.5","11.5","12.5","13.7","15.1","16.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","90.0","Perempuan","9.8","10.6","11.6","12.6","13.8","15.2","16.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","90.5","Perempuan","9.9","10.7","11.7","12.8","14.0","15.4","16.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","91.0","Perempuan","10.0","10.9","11.8","12.9","14.1","15.5","17.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","91.5","Perempuan","10.1","11.0","11.9","13.0","14.3","15.7","17.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","92.0","Perempuan","10.2","11.1","12.0","13.1","14.4","15.8","17.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","92.5","Perempuan","10.3","11.2","12.1","13.3","14.5","16.0","17.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","93.0","Perempuan","10.4","11.3","12.3","13.4","14.7","16.1","17.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","93.5","Perempuan","10.5","11.4","12.4","13.5","14.8","16.3","17.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","94.0","Perempuan","10.6","11.5","12.5","13.6","14.9","16.4","18.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","94.5","Perempuan","10.7","11.6","12.6","13.8","15.1","16.6","18.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","95.0","Perempuan","10.8","11.7","12.7","13.9","15.2","16.7","18.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","95.5","Perempuan","10.8","11.8","12.8","14.0","15.4","16.9","18.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","96.0","Perempuan","10.9","11.9","12.9","14.1","15.5","17.0","18.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","96.5","Perempuan","11.0","12.0","13.1","14.3","15.6","17.2","19.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","97.0","Perempuan","11.1","12.1","13.2","14.4","15.8","17.4","19.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","97.5","Perempuan","11.2","12.2","13.3","14.5","15.9","17.5","19.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","98.0","Perempuan","11.3","12.3","13.4","14.7","16.1","17.7","19.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","98.5","Perempuan","11.4","12.4","13.5","14.8","16.2","17.9","19.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","99.0","Perempuan","11.5","12.5","13.7","14.9","16.4","18.0","19.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","99.5","Perempuan","11.6","12.7","13.8","15.1","16.5","18.2","20.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","100.0","Perempuan","11.7","12.8","13.9","15.2","16.7","18.4","20.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","100.5","Perempuan","11.9","12.9","14.1","15.4","16.9","18.6","20.5","24-60");

        insertDataGizi("tinggi_badan","berat_badan","101.0","Perempuan","12.0","13.0","14.2","15.5","17.0","18.7","20.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","101.5","Perempuan","12.1","13.1","14.3","15.7","17.2","18.9","20.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","102.0","Perempuan","12.2","13.3","14.5","15.8","17.4","19.1","21.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","102.5","Perempuan","12.3","13.4","14.6","16.0","17.5","19.3","21.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","103.0","Perempuan","12.4","13.5","14.7","16.1","17.7","19.5","21.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","103.5","Perempuan","12.5","13.6","14.9","16.3","17.9","19.7","21.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","104.0","Perempuan","12.6","13.8","15.0","16.4","18.1","19.9","22.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","104.5","Perempuan","12.8","13.9","15.2","16.6","18.2","20.1","22.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","105.0","Perempuan","12.9","14.0","15.3","16.8","18.4","20.3","22.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","105.5","Perempuan","13.0","14.2","15.5","16.9","18.6","20.5","22.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","106.0","Perempuan","13.1","14.3","15.6","17.1","18.8","20.8","23.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","106.5","Perempuan","13.3","14.5","15.8","17.3","19.0","21.0","23.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","107.0","Perempuan","13.4","14.6","15.9","17.5","19.2","21.2","23.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","107.5","Perempuan","13.5","14.7","16.1","17.7","19.4","21.4","23.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","108.0","Perempuan","13.7","14.9","16.3","17.8","19.6","21.7","24.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","108.5","Perempuan","13.8","15.0","16.4","18.0","19.8","21.9","24.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","109.0","Perempuan","13.9","15.2","16.6","18.2","20.0","22.1","24.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","109.5","Perempuan","14.1","15.4","16.8","18.4","20.3","22.4","24.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","110.0","Perempuan","14.2","15.5","17.0","18.6","20.5","22.6","25.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","110.5","Perempuan","14.4","15.7","17.1","18.8","20.7","22.9","25.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","111.0","Perempuan","14.5","15.8","17.3","19.0","20.9","23.1","25.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","111.5","Perempuan","14.7","16.0","17.5","19.2","21.2","23.4","26.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","112.0","Perempuan","14.8","16.2","17.7","19.4","21.4","23.6","26.2","24-60");
        insertDataGizi("tinggi_badan","berat_badan","112.5","Perempuan","15.0","16.3","17.9","19.6","21.6","23.9","26.5","24-60");
        insertDataGizi("tinggi_badan","berat_badan","113.0","Perempuan","15.1","16.5","18.0","19.8","21.8","24.3","26.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","113.5","Perempuan","15.3","16.7","18.2","20.0","22.1","24.4","27.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","114.0","Perempuan","15.4","16.8","18.4","20.2","22.3","24.7","27.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","114.5","Perempuan","15.6","17.0","18.6","20.5","22.6","25.0","27.8","24-60");
        insertDataGizi("tinggi_badan","berat_badan","115.0","Perempuan","15.7","17.2","18.8","20.7","22.8","25.2","28.1","24-60");
        insertDataGizi("tinggi_badan","berat_badan","115.5","Perempuan","15.9","17.3","19.0","20.9","23.0","25.5","28.4","24-60");
        insertDataGizi("tinggi_badan","berat_badan","116.0","Perempuan","16.0","17.5","19.2","21.1","23.3","25.8","28.7","24-60");
        insertDataGizi("tinggi_badan","berat_badan","116.5","Perempuan","16.2","17.7","19.4","21.3","23.5","26.1","29.0","24-60");
        insertDataGizi("tinggi_badan","berat_badan","117.0","Perempuan","16.3","17.8","19.6","21.5","23.8","26.3","29.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","117.5","Perempuan","16.5","18.0","19.8","21.7","24.0","26.6","29.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","118.0","Perempuan","16.6","18.2","19.9","22.0","24.2","26.9","29.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","118.5","Perempuan","16.8","18.4","20.1","22.2","24.5","27.2","30.3","24-60");
        insertDataGizi("tinggi_badan","berat_badan","119.0","Perempuan","16.9","18.5","20.3","22.4","24.7","27.4","30.6","24-60");
        insertDataGizi("tinggi_badan","berat_badan","119.5","Perempuan","17.1","18.7","20.5","22.6","25.0","27.7","30.9","24-60");
        insertDataGizi("tinggi_badan","berat_badan","120.0","Perempuan","17.3","18.9","20.7","22.8","25.3","28.0","31.2","24-60");

        //umur __ index massa tubuh __ perempuan 0 - 24 bulan
        insertDataGizi("umur","index_massa_tubuh","1","Perempuan","10.8","12.0","13.2","14.6","16.0","17.5","19.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","2","Perempuan","11.8","13.0","14.3","15.8","17.3","19.0","20.7","0-24");
        insertDataGizi("umur","index_massa_tubuh","3","Perempuan","12.4","13.6","14.9","16.4","17.9","19.7","21.5","0-24");
        insertDataGizi("umur","index_massa_tubuh","4","Perempuan","12.7","13.9","15.2","16.7","18.3","20.0","22.0","0-24");
        insertDataGizi("umur","index_massa_tubuh","5","Perempuan","12.9","14.1","15.4","16.8","18.4","20.2","22.2","0-24");
        insertDataGizi("umur","index_massa_tubuh","6","Perempuan","13.0","14.1","15.5","16.9","18.5","20.3","22.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","7","Perempuan","13.0","14.2","15.5","16.9","18.5","20.3","22.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","8","Perempuan","13.0","14.1","15.4","16.8","18.4","20.2","22.2","0-24");
        insertDataGizi("umur","index_massa_tubuh","9","Perempuan","12.9","14.1","15.3","16.7","18.3","20.1","22.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","10","Perempuan","12.9","14.0","15.2","16.6","18.2","19.9","21.9","0-24");
        insertDataGizi("umur","index_massa_tubuh","11","Perempuan","12.8","13.9","15.1","16.5","18.0","19.8","21.8","0-24");
        insertDataGizi("umur","index_massa_tubuh","12","Perempuan","12.7","13.8","15.0","16.4","17.9","19.6","21.6","0-24");
        insertDataGizi("umur","index_massa_tubuh","13","Perempuan","12.6","13.7","14.9","16.2","17.7","19.5","21.4","0-24");
        insertDataGizi("umur","index_massa_tubuh","14","Perempuan","12.6","13.6","14.8","16.1","17.6","19.3","21.3","0-24");
        insertDataGizi("umur","index_massa_tubuh","15","Perempuan","12.5","13.5","14.7","16.0","17.5","19.2","21.1","0-24");
        insertDataGizi("umur","index_massa_tubuh","16","Perempuan","12.4","13.5","14.6","15.9","17.4","19.1","21.0","0-24");
        insertDataGizi("umur","index_massa_tubuh","17","Perempuan","12.4","13.4","14.5","15.8","17.3","18.9","20.9","0-24");
        insertDataGizi("umur","index_massa_tubuh","18","Perempuan","12.3","13.3","14.4","15.7","17.2","18.8","20.8","0-24");
        insertDataGizi("umur","index_massa_tubuh","19","Perempuan","12.3","13.3","14.4","15.7","17.1","18.8","20.7","0-24");
        insertDataGizi("umur","index_massa_tubuh","20","Perempuan","12.2","13.2","14.3","15.6","17.0","18.7","20.6","0-24");
        insertDataGizi("umur","index_massa_tubuh","21","Perempuan","12.2","13.2","14.3","15.5","17.0","18.6","20.5","0-24");
        insertDataGizi("umur","index_massa_tubuh","22","Perempuan","12.2","13.1","14.2","15.5","16.9","18.5","20.4","0-24");
        insertDataGizi("umur","index_massa_tubuh","23","Perempuan","12.2","13.1","14.2","15.4","16.9","18.5","20.4","0-24");
        insertDataGizi("umur","index_massa_tubuh","24","Perempuan","12.1","13.1","14.2","15.4","16.8","18.4","20.3","0-24");

        //umur __ index massa tubuh __ perempuan 24-60 bulan
        insertDataGizi("umur","index_massa_tubuh","24","Perempuan","12.4","13.3","14.4","15.7","17.1","18.7","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","25","Perempuan","12.4","13.3","14.4","15.7","17.1","18.7","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","26","Perempuan","12.3","13.3","14.4","15.6","17.0","18.7","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","27","Perempuan","12.3","13.3","14.4","15.6","17.0","18.6","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","28","Perempuan","12.3","13.3","14.3","15.6","17.0","18.6","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","29","Perempuan","12.3","13.2","14.3","15.6","17.0","18.6","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","30","Perempuan","12.3","13.2","14.3","15.5","16.9","18.5","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","31","Perempuan","12.2","13.2","14.3","15.5","16.9","18.5","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","32","Perempuan","12.2","13.2","14.3","15.5","16.9","18.5","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","33","Perempuan","12.2","13.1","14.2","15.5","16.9","18.5","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","34","Perempuan","12.2","13.1","14.2","15.4","16.8","18.5","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","35","Perempuan","12.1","13.1","14.2","15.4","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","36","Perempuan","12.1","13.1","14.2","15.4","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","37","Perempuan","12.1","13.1","14.1","15.4","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","38","Perempuan","12.1","13.0","14.1","15.4","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","39","Perempuan","12.0","13.0","14.1","15.3","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","40","Perempuan","12.0","13.0","14.1","15.3","16.8","18.4","20.3","24-60");
        insertDataGizi("umur","index_massa_tubuh","41","Perempuan","12.0","13.0","14.1","15.3","16.8","18.4","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","42","Perempuan","12.0","12.9","14.0","15.3","16.8","18.4","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","43","Perempuan","11.9","12.9","14.0","15.3","16.8","18.4","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","44","Perempuan","11.9","12.9","14.0","15.3","16.8","18.5","20.4","24-60");
        insertDataGizi("umur","index_massa_tubuh","45","Perempuan","11.9","12.9","14.0","15.3","16.8","18.5","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","46","Perempuan","11.9","12.9","14.0","15.3","16.8","18.5","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","47","Perempuan","11.8","12.8","14.0","15.3","16.8","18.5","20.5","24-60");
        insertDataGizi("umur","index_massa_tubuh","48","Perempuan","11.8","12.8","14.0","15.3","16.8","18.5","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","49","Perempuan","11.8","12.8","13.9","15.3","16.8","18.5","20.6","24-60");
        insertDataGizi("umur","index_massa_tubuh","50","Perempuan","11.8","12.8","13.9","15.3","16.8","18.6","20.7","24-60");
        insertDataGizi("umur","index_massa_tubuh","51","Perempuan","11.8","12.8","13.9","15.3","16.8","18.6","20.7","24-60");
        insertDataGizi("umur","index_massa_tubuh","52","Perempuan","11.7","12.8","13.9","15.3","16.8","18.6","20.7","24-60");
        insertDataGizi("umur","index_massa_tubuh","53","Perempuan","11.7","12.7","13.9","15.3","16.8","18.6","20.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","54","Perempuan","11.7","12.7","13.9","15.3","16.8","18.7","20.8","24-60");
        insertDataGizi("umur","index_massa_tubuh","55","Perempuan","11.7","12.7","13.9","15.3","16.8","18.7","20.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","56","Perempuan","11.7","12.7","13.9","15.3","16.8","18.7","20.9","24-60");
        insertDataGizi("umur","index_massa_tubuh","57","Perempuan","11.7","12.7","13.9","15.3","16.9","18.7","21.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","58","Perempuan","11.7","12.7","13.9","15.3","16.9","18.8","21.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","59","Perempuan","11.6","12.7","13.9","15.3","16.9","18.8","21.0","24-60");
        insertDataGizi("umur","index_massa_tubuh","60","Perempuan","11.6","12.7","13.9","15.3","16.9","18.8","21.1","24-60");
    }

    private void insertFirstDataKSP(){
        //usia 3
        insertFormKSP("Pada waktu bayi telentang, apakah masing-masing lengan dan tungkai bergerak dengan mudah? Jawab " +
                        "TIDAK bila salah satu atau kedua tungkai atau lengan bayi bergerak tak terarah/tak terkendali.",
                "", "Gerak Kasar","3");
        insertFormKSP("Pada waktu bayi telentang apakah ia melihat dan menatap wajah anda?",
                "","Sosialisasi dan kemandirian","3");
        insertFormKSP("Apakah bayi anda dapat mengeluarkan suara - suara lain (ngoceh), disamping menangis?",
                "","Bicara dan Bahasa","3");
        insertFormKSP("Pada waktu bayi telentang, apakah ia dapat mengikuti gerakan anda dengan menggerakan kepalanya " +
                        "dari kanan/kiri ke tengok?",
                "img_1.png","Gerak Halus","3");
        insertFormKSP("Pada waktu bayi telentang, apakah ia dapat mengikuti gerakan anda dengan menggerakan kepalanya " +
                        "satu sisi hampir sampai pada sisi yang lain?",
                "img_2.png","Gerak Halus","3");
        insertFormKSP("Pada waktu anda mengajak bayi berbicara dan tersenyum, apakah ia tersenyum kembali kepada anda?",
                "","Sosialisasi dan Kemandirian","3");
        insertFormKSP("Pada waktu bayi telungkup di alas yang datar, apakah ia dapat mengangkat kepalanya seperti pada " +
                        "gambar ini?",
                "img_3.png","Gerak Kasar","3");
        insertFormKSP("Pada waktu bayi telungkup dialas yang datar, apakah ia dapat mengangkat kepalanya sehingga " +
                        "membentuk dusut 45 derajat seperti pada gambar?",
                "img_4.png","Gerak Kasar","3");
        insertFormKSP("Pada waktu bayi telungkup dialas yang datar, apakah ia dapat mengangkat kepalanya dengan tegak " +
                        "seperti pada gambar?",
                "img_5.png","Gerak Kasar","3");
        insertFormKSP("Apakah bayi suka tertawa keras walau tidak digelitik atau diraba-raba?",
                "","Bicara dan Bahasa","3");

        //usia 6
        insertFormKSP("Pada bayi telentang, apakah ia dapat mengikuti gerakan anda dengan menggerakan kepala sepenuhnya dari satu " +
                        "satu sisi ke sisi yang lain?",
                "img_6.png","Gerak Halus","6");
        insertFormKSP("Dapatkah bayi anda mempertahankan posisi kepala dalam keadaan tegak dan stabil? Jawab TIDAK bila kepala " +
                        "bayi cenderung jatuh ke kanan/kiri atau ke dadanya",
                "","Gerak Kasar","6");
        insertFormKSP("Sentuhkan pensil di punggung tangan atau ujung jari bayi (Jangan meletakkan diatas telapak tangan bayi). " +
                        "Apakah bayi dapat menggemgam pensil itu selama beberapa detik?",
                "img_7.png","Gerak Halus","6");
        insertFormKSP("Ketika bayi telungkup dialas datar, apakah ia dapat mengangkat dada dengan kedua lengannya sebagai penyangga " +
                        "seperti pada gambar?",
                "img_8.png","Gerak Kasar","6");
        insertFormKSP("Pernahkah bayi mengeluarkan suara gembura bernada tinggi atau memekik tetapi bukan menangis?",
                "","Bicara dan Bahasa","6");
        insertFormKSP("Pernahkan bayi berbalik paling sedikit dua kali, dari telentang ke telungkup atau sebaliknya?",
                "","Gerak Kasar","6");
        insertFormKSP("Pernahkan anda melihat bayi tersenyum ketika melihat mainan yang lucu, gambar aau binatang peliharaan " +
                        "pada saat ia bermain sendiri?",
                "","Sosialisasi dan Kemandirian","6");
        insertFormKSP("Dapatkan bayi mengarahkan matanya pada benda kecil sebesar kacang, kismis atau uang logam? Jawab TIDAK jika " +
                        "ia tidak dapat mengarahkan matanya.",
                "","Gerak Halus","6");
        insertFormKSP("Dapatkan bayi meraih mainan yang diletakkan agak jauh namun masih berada dalam jangkauan tangannya?",
                "","Gerak Halus","6");
        insertFormKSP("Pada posisi bayi telentang, pegang kedua tangannya lalu tarik perlahan - lahan ke posisi duduk. Dapatkah bayi " +
                        "mempertahankan lehernya secara kaku seperti gambar disebelah kiri? Jawab TIDAK bila kepala bayi jatuh kembali seperti " +
                        "gambar sebelah kanan.",
                "img_9.png","Gerak Kasar","6");

        //usia 9
        insertFormKSP("Pada posisi bayi telentang, pegang kedua tangannya lalu tarik perlahan - lahan ke posisi clucluk. " +
                        "Dapatkah bayi mempertahankan leherlah secara kaku seperti gambar di sebelah kiri? Jawab TIDAK bila kepada bayi " +
                        "jatuh kembali seperti pada gambar sebelah kanan.",
                "img_9.png","Gerak Kasar","9");
        insertFormKSP("Pernahkah anda melihat bayi memindahkan mainan atau kue kering dari satu tangan ke tangan yang lain? " +
                        "Benda? benda panjang seperti sendok atau kerincingan bertangkai tidak ikut dinilai.",
                "","Gerak Halus","9");
        insertFormKSP("Tarik perhatian bayi dengan memperlihatkan selendang, sapu tangan atau serbet, kemudian jatuhkan ke " +
                        "lantai. Apakah bayi mencoba mencarinya? Misalnya mencari dibawah meja atau di belakang kursi?",
                "","Gerak Halus","9");
        insertFormKSP("Apakah bayi dapat memungut dua benda seperti mainan/kue kering, dan masing - masing tangan memegang satu " +
                        "benda pada saat yang sama? Jawab TIDAK bila bayi tidak pernah melakukan perbuatan ini.",
                "","Gerak Halus","9");
        insertFormKSP("Jika anda mengangkat bayi melalui ketiaknya ke posisi berdiri, dapatkah ia menyangga sebagian berat badan " +
                        "dengan kedua kakinya? Jawab YA bila ia mencoba berdiri dan sebagian berat badan tertumpu pada kedua kakinya.",
                "","Gerak Kasar","9");
        insertFormKSP("Dapatkah bayi memungut dengan tangannya benda - benda kecil seperti kismis, kacang? Kacang potongan biskuit, " +
                        "dengan gerakan miring atau menggerapai seperti pada gambar?",
                "","Gerak Halus","9");
        insertFormKSP("Tanpa disangga oleh bantal, kursi atau dinding, dapatkah bayi duduk sendiri selama 60 detik?",
                "img_10.png","Gerak Kasar","9");
        insertFormKSP("Apakah bayi dapat makan kue kering sendiri?",
                "img_11.png","Sosialisasi dan Kemandirian","9");
        insertFormKSP("Pada waktu bayi bermain sendiri dan anda diam - diam datang berdiri dibelakangnnya, apakah ia menengok " +
                        "ke belakang seperti mendengar kedatangan anda? Suara keras tidak ikut dihitung. Jawab YA hanya jika anda melihat reaksinya " +
                        "terhadap suara yang perlahan atau bisikan.",
                "","Bicara dan Bahasa","9");
        insertFormKSP("Letakkan suatu mainan yang diinginkannya diluar jangkauan bayi, apakah ia mencoba mendapatkannya dengan " +
                        "mengulurkan lengan atau badannya?",
                "","Sosialisasi dan Kemandirian","9");

        //usia 12
        insertFormKSP("Jika anda bersembunyi dibelakang sesuatu/dipojok, kemudian muncul dan menghilang secara berulang - ulang " +
                        "dihadapan anak, apakah ia mencari anda atau mengharapkan anda muncul kembali?",
                "","Sosialisasi dan Kemandirian","12");
        insertFormKSP("Letakkan pensil ditelapak tangan bayi. Coba ambil pensil tersebut dengan perlahan - lahan. Sulitkah anda " +
                        "mendapatkan pensil itu kembali?",
                "","Gerak Halus","12");
        insertFormKSP("Apakah anak anda dapat berdiri selama 30 detik atau lebih dengan berpegangan pada kursi/meja?",
                "","Gerak Kasar","12");
        insertFormKSP("Apakah anak anda dapat mengatakan 2 suku kata yang sama misalnya, \"mama-mama\",\"da-da\" atau \"pa-pa\". " +
                        "Jawab YA bila ia mengeluarkan salah satu suara tadi.",
                "","Bicara dan Bahasa","12");
        insertFormKSP("Apakah anak anda dapat mengangkat badannya ke posisi berdiri tanpa bantuan anda?",
                "","Gerak Kasar","12");
        insertFormKSP("Apakah anak anda dapat membedakan anda dengan prang yang belum ia kenal? Ia akan menunjukkan sikap " +
                        "malu- malu atau ragu - ragu pada saat permulaan bertemu dengan orang yang belum dikenalnya.",
                "","Sosialisasi dan Kemandirian","12");
        insertFormKSP("Apakah anak dapat mengambil benda kecil seperti kacang atau kismis, dengan meremas diantara ibu jari " +
                        "dan jarinya seperti pada gambar?",
                "img_12.png","Gerak Halus","12");
        insertFormKSP("Apakah anak dapat duduk sendiri tanpa bantuan?",
                "","Gerak Kasar","12");
        insertFormKSP("Sebut 2-3 kata yang dapat ditiru oleh anak (tidak perlu kata - kata yang lengkap). Apakah ia mencoba " +
                        "meniru menyebutkan kata - kata tadi?",
                "","Bicara dan Bahasa","12");
        insertFormKSP("Tanpa bantuan, apakah anak dapat mempertemukan dua kubus keci yang pegang? Kerincingan bertangkai dan tutup " +
                        "panel tidak dinilai.",
                "","Gerak Halus","12");

        //usia 15
        insertFormKSP("Tanpa bantuan, apakah anak dapat mempertemukan dua kubus kecil yang ia pegang? Kerincingan bertangkai dan tutup, " +
                        "panci tidak ikut dinilai.",
                "","Gerak Halus","15");
        insertFormKSP("Apakah anak dapat jalan sendiri atau jalan dengan berpegangan?",
                "","Gerak Kasar","15");
        insertFormKSP("Tanpa bantuan, apakah anak dapat bertepuk tangan atau melambai - lambai? Jawab TIDAK bila ia membutuh kemandirian " +
                        "kaq bantuan?",
                "","Sosialisasi dan Kemandirian","15");
        insertFormKSP("Apakah anak dapat mengatakan \"papa\" ketika ia memanggil/melihat ayahnya, atau mengatakan \"mama\" " +
                        "jika melihat ibunya? Jawab YA bila anak mengatakan salah satu diantaranya.",
                "","Bicara dan Bahasa","15");
        insertFormKSP("Dapatkan anak berdiri sendiri tanpa berpegangan selama kira - kira 15 detik?",
                "","Gerak Kasar","15");
        insertFormKSP("Dapatkah anak berdiri sendiri tanpa berpegangan selama 30 detik atau lebih?",
                "","Gerak Kasar","15");
        insertFormKSP("Tanpa berpegangan atau menyentuh lantai, apakah anak dapat membungkuk untuk memungut mainan dilantai " +
                        "dan kemudian berdiri kembali?",
                "","Gerak Kasar","15");
        insertFormKSP("Apakah anak anda dapat menunjukan apa yang diinginkannya tanpa menangis atau merengek? Jawab YA bila ia " +
                        "menunjuk, menarik atau mengeluarkan suara yang menyenangkan.",
                "","Sosialisasi dan Kemandirian","15");
        insertFormKSP("Apakah anak dapat berjalan disepanjang ruangan tanpa jatuh atau terhuyung - huyung?",
                "","Gerak Kasar","15");
        insertFormKSP("Apakah anak dapat mengambil benda kecil seperti kacang, kismis, atau potongan biskuit dengan menggunakan " +
                        "ibu jari seperti pada gambar ini.",
                "img_12.png","Gerak Halus","15");

        //usia 18
        insertFormKSP("Tanpa bantuan, apakah anak dapat berteput tangan atau melambai - lambai? Jawab TIDAK bila ia membutuhkan bantuan.",
                "","Sosialisasi dan Kemandirian","18");
        insertFormKSP("Apakah anak dapat mengatakan \"papa\" ketika ia memanggil/melihat ayahnya, atau mengatakan \"mama\" jika " +
                        "memanggil/melihat ibunya?",
                "","Bicara dan Bahasa","18");
        insertFormKSP("Apakah anak dapat berdiri sendiri tanpa berpegangan selama kira - kira 5 detik?",
                "","Gerak Kasar","18");
        insertFormKSP("Apakah anak dapat berdiri sendiri tanpa berpegangan selama 30 detik atau lebih?",
                "","Gerak Kasar","18");
        insertFormKSP("Tanpa berpegangan atau menyentuh lantai, pakah anak dapat membungkuk untuk memungut mainan dilantai dan " +
                        "kemudian berdiri kembali?",
                "","Gerak Kasar","18");
        insertFormKSP("Apakah anak dapat menunjukan apa yang diinginkannya tanpa menangis atau merengek? Jawab YA bila ia menunjuk, " +
                        "menarik atau mengeluarkan suara yang menyenangkan.",
                "","Sosialisasi dan Kemandirian","18");
        insertFormKSP("Apakah anak dapat berjalan disepanjang ruangan tanpa jatuh atau terhuyung - huyung?",
                "","Gerak Kasar","18");
        insertFormKSP("Apakah anak dapat mengambil benda kecil seperti kacang, kismis, atau potongan biskuit dengan menggunakan ibu " +
                        "jari dan jari telunjuk seperti pada gambar?",
                "img_12.png","Gerak Halus","18");
        insertFormKSP("Jika anda menggelindingkan bola ke anak, apakah ia menggelindingkan/melemparkan kembali bola pada anda?",
                "","Gerak Halus, Sosialisasi dan Kemandirian","18");
        insertFormKSP("Apakah anak dapat memegang sendiri cangkir/gelas dan minum dari tempat tersebut tanpa tumpah?",
                "","Sosialisasi dan Kemandirian","18");

        //usia 21
        insertFormKSP("Tanpa berpegangan atau menyentuh lantai, apakah anak dapat membungkuk untuk memungut mainan dilantai " +
                        "dan kemudian berdiri kembali?",
                "","Gerak Kasar","21");
        insertFormKSP("Apakah anak dapat menunjukkan apa yang diinginkannya tanpa menangis atau merengek? Jawab YA bila ia " +
                        "menunjuk, menarik atau mengeluarkan suara yang menyenangkan.",
                "","Sosialisasi dan Kemandirian","21");
        insertFormKSP("Apakah anak dapat berjalan disepanjang ruangan tanpa jatuh atau terhuyung - huyung?",
                "","Gerak Kasar","21");
        insertFormKSP("Apakah anak dapat mengambil benda kecil seperti kacang, kismis, atau potongan biskuit dengan " +
                        "menggunakan ibu jari dan jari telunjuk seperti pada gambar?",
                "img_12.png","Gerak Halus","21");
        insertFormKSP("Jika anda menggelinding bola ke anak, apakah ia menggelinding/melemparkan kembali bola pada anda?",
                "","Gerak Halus","21");
        insertFormKSP("Apakah anak dapat memegang sendiri cangkir/gelas dan minum dari tempat tersebut tanpa tumpah?",
                "","Sosialisasi dan Kemandirian","21");
        insertFormKSP("Jika anda sedang melakukan pekerjaan rumah angga, apakah anak meniru apa yang anda lakukan?",
                "","Sosialisasi dan Kemandirian","21");
        insertFormKSP("Apakah anak dapat meletakkan satu kubus diatas gelak halus ya tida kubus yang lain tanpa menjatuhkan " +
                        "kubus itu? Kubus yang digunakan ukuran 2.5 - 5.0 cm.",
                "","Gerak Halus","21");
        insertFormKSP("Apakah anak dapat mengucapkan paling sedikit 3 kata yang mempunyai arti selain \"papa\" dan " +
                        "\"mama\"?",
                "","Bicara dan Bahasa","21");
        insertFormKSP("Apakah anak dapat berjalan mundur 5 langkah atau lebih tanpa kehilangan keseimbangan? (Anda mungkin " +
                        "dapat melihatnya ketika anak menarik mainannya).",
                "","Gerak Kasar","21");

        //usia 24
        insertFormKSP("Jika anda sedang melakukan pekerjaan rumah tangga, apakah anak meniru apa yang anda lakukan?",
                "","Sosialisasi dan Kemandirian","24");
        insertFormKSP("Apakah anak anda dapat meletakkan 1 buah kubus diatas kubus yang lain tanpa menjatuhkan kubus itu? " +
                        "Kubus yang digunakan ukuran 2.5 - 5 cm.",
                "","Gerak Halus","24");
        insertFormKSP("Apakah anak dapat mengucapkan paling sedikit 3 kata yang mempunyai arti selain \"papa\" dan \"mama\"?",
                "","Bicara dan Bahasa","24");
        insertFormKSP("Apakah anak dapat berjalan mundur 5 langkah atau lebih tanpa kehilangan keseimbangan? (Anda mungkin dapat " +
                        "melihatnya ketika anak menarik mainannya).",
                "","Gerak Kasar","24");
        insertFormKSP("Dapatkah anak melepas pakaiannya seperti baju, rok, atau celananya? (Topi dan kaos kaki tidak ikut dinilai).",
                "","Gerak Halus, Sosisalisasi dan Kemandirian","24");
        insertFormKSP("Dapatkah anak berjalan naik tangga sendiri? Jawab YA jika ia naik tangga dengan posisi tegak atau berpegangan " +
                        "pada dinding atau pegangan tangga. Jawab TIDAK jika ia naik tangga dengan merangkak atau anda tidak membolehkan anak naik tangga " +
                        "atau anak harus berpegangan pada seseorang.",
                "","Gerak Kasar, Sosialisasi dan Kemandirian","24");
        insertFormKSP("Tanpa bimbingan, petunjuk atau bantuan anda, dapatkah anak menunjuk dengan benar paling sedikit satu bagian badannya " +
                        "(rambut, mata, hitung, mulut, atau bagian badan yang lain)?",
                "","Bicara dan Bahasa","24");
        insertFormKSP("Dapatkah anak makan nasi sendiri tanpa banyak tumpah?",
                "","Sosialisasi dan Kemandirian","24");
        insertFormKSP("Dapatkah anak membantu memungut mainannya sendiri atau membantu mengangkat piring jika diminta?",
                "","Bicara dan Bahasa","24");
        insertFormKSP("Dapatkah anak menendang bola kecil (sebesar bola tenis) kedepan tanpa berpengaruh pada apapun? " +
                        "Mendorong tidak ikut dinilai.",
                "","Gerak Kasar","24");

        //usia 30
        insertFormKSP("Dapatkah anak melepas pakaiannya seperti baju, rok atau celananya? (topi dan kaos kaki tidak ikut" +
                        "dinilai).",
                "","Sosialisasi dan Kemandirian","30");
        insertFormKSP("Dapatkah anak berjalan naik tangga sendiri? Jawab YA jika ia naik tanggan dengan posisi tegak atau " +
                        "berpegangan pada dinding atau pegangan tangga. Jawab TIDAK jika ia naik tanggan dengan merangkak atau anda " +
                        "tidak membolehkan anak naik tangga atau anak harus berpegangan pada seseorang.",
                "","Gerak Kasar","30");
        insertFormKSP("Tanpa bimbingan, petunjuk atau bantuan anda, dapatkah anak menunjuk dengan benar paling sedikit " +
                        "satu bagian badannya (rambut, mata, hidung, mulut, atau bagian badan yang lain)?",
                "","Bicara dan Bahasa","30");
        insertFormKSP("Dapatkah anak makan nasi sendiri tanpa banyak tumpah?",
                "","Sosialisasi dan Kemandirian","30");
        insertFormKSP("Dapatkah anak membantu memungut mainannya sendiri atau membantu mengangkat piring jika mininta?",
                "","Bicara dan Bahasa","30");
        insertFormKSP("Dapatkah anak menendang bola kecil (sebesar bola tenis) Gerak kasar kedepan tanpa berpegangan pada apapun? " +
                        "Mendorong tidak ikut dinilai.",
                "","Gerak Kasar","30");
        insertFormKSP("Bila diberi pensil, apakah anak mencoret - coret kertas tanpa bantuan/petunjuk?",
                "","Gerak Halus","30");
        insertFormKSP("Dapatkah anak meletakkan 4 buah kubus satu persatu diatas kubus yang lain tanpa menjatuhkan kubus itu? " +
                        "Kubus yang digunakan ukuran 2.5 - 5 cm.",
                "","Gerak Halus","30");
        insertFormKSP("Dapatkah anak menggunakan 2 kata pada saat berbicara seperti \"minta minum\", \"mau tidur\"? " +
                        "\"terimakasih\" dan \"dadah\" tidak ikut dinilai.",
                "","Bicara dan Bahasa","30");
        insertFormKSP("Dapatkah anak dapat menyebut 2 diantara gambar - gambar ini tanpa bantuan?",
                "img_13.png","Bicara dan Bahasa","30");

        //usia 36
        insertFormKSP("Bila diberi pensil, apakah anak mencoret - coret kertas tanpa bantuan/petunjuk?",
                "","Gerak Halus","36");
        insertFormKSP("Dapatkah anak meletakkan 4 buah kubus satu persatu diatas kubus yang lain tanpa menjatuhkan kubus itu? " +
                        "Kubus yang digunakan ukuran 2.5 - 5 cm.",
                "","Gerak Halus","36");
        insertFormKSP("Dapatkah anak menggunakan 2 kata pada saat berbicara seperti \"minum minum\", \"mau tidur\"? " +
                        "\"terimakasih\" dan \"dadah\" tidak ikut dinilai.",
                "","Bicara dan Bahasa","36");
        insertFormKSP("Apakah anak dapat menyebut 2 diantara gambar - gambar ini tanpa bantuan?",
                "img_13.png","Bicara dan Bahasa","36");
        insertFormKSP("Dapatkah anak melempar bola lurus ke arah perut atau dada anda dari jarak 1,5 meter?",
                "","Gerak Kasar","36");
        insertFormKSP("Ikuti perintah ini dengan seksama, Jangan memberi isyarat dengan telunjuk atau mata pada saat memberikan perintah " +
                        "berikut ini, \"Letakkan kertas ini dilantai\", \"Letakkan kertas ini dikurisi\", \"Berikan kertas ini kepada ibu\". " +
                        "Dapatkah anak melaksanakan ketiga perintah tadi?",
                "","Bicara dan Bahasa","36");
        insertFormKSP("Buat garis lurus kebawah sepanjang sekurang - kurangnya 2.5 cm. Suruh anak menggambar garis lain di " +
                        "samping garis tersebut.?",
                "img_14.png","Gerak Halus","36");
        insertFormKSP("Letakkan selembar kertas seukuran buku dilantai. Apakah anak dpat melompati bagian kertas dengan mengangkat " +
                        "kedua kakinya secara bersamaan tanpa didahului lari?",
                "","Gerak Kasar","36");
        insertFormKSP("Dapatkah anak mengenakan sepatunya sendiri?",
                "","Sosialisasi dan Kemandirian","36");
        insertFormKSP("Dapatkah anak mengayuh sepeda roda tiga sejauh sedikit 3 meter?",
                "","Gerak Kasar","36");

        //usia 42
        insertFormKSP("Dapatkah anak mengenakan sepatunya sendiri?",
                "","Sosialisasi dan Kemandirian","42");
        insertFormKSP("Dapatkah anak mengayuh sepera roda tiga sejauh setdikitnya 3 meter?",
                "","Gerak Kasar","42");
        insertFormKSP("Setelah makan, apakah anak mencuci dan mengeringkan tangannya dengan baik sehingga anda tidak perlu " +
                        "mengulanginya?",
                "","Sosialisasi dan Kemandirian","42");
        insertFormKSP("Suruh anak berdiri satu kaki tanpa berpegangan. Jika perlu ditunjukkan caranya dan beri anak anda " +
                        "kesempatan melakukannya 3 kali. Dapatkah ia mempertahankan keseimbangan dalam waktu 2 detik atau lebih?",
                "","Gerak Kasar","42");
        insertFormKSP("Letakkan selembar kertas seukuran buku ini dilantai. Apakah anak dapat melompati panjang kertas " +
                        "ini dengan mengangkat kedua kakinya secara bersamaan tanpa didahului lari?",
                "","Gerak Kasar","42");
        insertFormKSP("Jangan membantu anak dan jangan menyebut lingkaran. Suruh anak menggambar seperti contoh menggambar " +
                        "lingkaran?",
                "img_15.png","Gerak Halus","42");
        insertFormKSP("Dapatkah anak meletakkan 8 buah kubus satu persatu diatas yang lain tanpa menjatuhkan kubus tersebut? " +
                        "Kubus yang digunakan ukuran 2.5 - 5 cm.",
                "","Gerak Halus","42");
        insertFormKSP("Apakah anak dapat bermain petak umpet, ular naga atau permainan lain dimana ia ikut bermain dan " +
                        "mengikuti aturan bermain?",
                "","Sosialisasi dan Kemandirian","42");
        insertFormKSP("Dapatkah anak mengenakan celana panjang, kemeja, baju atau kaos kaki tanpa dibantu? (Tidak termasuk " +
                        "memasang kancing, gesper atau ikat pinggang).",
                "", "Sosialisasi dan Kemandirian","42");

        //usia 48
        insertFormKSP("Dapatkah anak mengayuh sepeda roda tiga sejauh sedikitnya 3 meter?",
                "","Gerak Kasar","48");
        insertFormKSP("Setelah makan, apakah anak mencuci dan mengeringkan tangannya dengan baik sehingga anda tidak " +
                        "perlu mengulanginya?",
                "","Sosialisasi dan Kemandirian","48");
        insertFormKSP("Suruh anak berdiri satu kaki tanpa berpegangan. Jika perlu tunjukkan caranya dan beri anak anda " +
                        "kesempatan melakukannya 3 kali. Dapatkah ia mempertahankan keseimbangan dalam waktu 2 detik atau lebih?",
                "","Gerak Kasar","48");
        insertFormKSP("Letakkan selembar kertas seukuran buku ini di lantai. Apakah anak dapat melompati panjang kertas ini " +
                        "dengan mengangkat kedua kakinya secara bersamaan tanpa didahului lari?",
                "","Gerak Kasar","48");
        insertFormKSP("Jangan membantu anak dan jangan menyebut lingkaran. Suruh anak menggambar seperti contoh ini di kertas " +
                        "kosong yang tersedia. Dapatkah anak menggambar lingkaran?",
                "img_15.png","Gerak Halus","48");
        insertFormKSP("Dapatkah anak meletakkan 8 buah kubus satu persatu di atas yang lain tanpa menjatuhkan kubus tersebut?\n" +
                        "Kubus yang digunakan ukuran 2.5 – 5 cm",
                "","Gerak Halus","48");
        insertFormKSP("Apakah anak dapat bermain petak umpet, ular naga atau permainan lain dimana ia ikut bermain dan mengikuti " +
                        "aturan bermain?",
                "","Sosialisasi dan Kemandirian","48");
        insertFormKSP("Dapatkah anak mengenakan celana panjang, kemeja, baju atau kaos kaki tanpa di bantu? (Tidak termasuk memasang " +
                        "kancing, gesper atau ikat pinggang)",
                "","Sosialisasi dan Kemandirian","48");
        insertFormKSP("Dapatkah anak menyebutkan nama lengkapnya tanpa dibantu? Jawab TIDAK jika ia hanya menyebutkan sebagian namanya " +
                        "atau ucapannya sulit dimengerti.",
                "","Bicara dan Bahasa","48");

        //usia 54
        insertFormKSP("Dapatkah anak meletakkan 8 buah kubus satu persatu diatas yang lain tanpa menjatuhkan kubus tersebut? " +
                        "Kuburs yang digunakan ukuran 2.5 - 5 cm.",
                "","Gerak Halus","54");
        insertFormKSP("Apakah anak dapat bermain petak umpet, ular naga atau permainan lain dimana ia ikut bermain dan mengikuti " +
                        "aturan bermain?",
                "","sosialisasi dan Kemandirian","54");
        insertFormKSP("Dapatkah anak mengenakan celana panjang, kemeja, baju atau kaos kaki tanpa dibantu? (Tidak termasuk memasang " +
                        "kancing, gesper atau ikat pinggang).",
                "","Sosialisasi dan Kemandirian","54");
        insertFormKSP("Dapatkah anak menyebutkan nama lengkapnya tanpa dibantu? Jawab TIDAK jika ia hanya menyebutkan namanya atau " +
                        "ucapannya sulit dimengerti.",
                "","Bicara dan Bahasa","54");
        insertFormKSP("Isilah titik - titik dibawah ini dengan jawaban anak. Jangan membantu kecuali mengulangi pertanyaan. " +
                        "\"Apa yang kamu lakukan jika kamu kedinginan?\" \n \"Apa yang kamu lakukan jika kamu lapar?\" \n " +
                        "\"Apa yang kamu lakukan jika kamu lelah?\" \n Jawaban YA bila anak menjawab ke 3 pertanyaan tadi dengan benar, " +
                        "bukan dengan gerakan atau isyarat. Jika kedinginan, jawaban yang benar adalah\"menggigil\", \"pakai mantel\" " +
                        "atau \"masuk kedalam rumah\". \n Jika lapar, jawaban yang benar adalah \"makan\" \n Jika lelah, jawaban yang benar " +
                        "adalah \"mengantuk\",\"tidur\",\"berbaring atau tidur - tiduran\", \"istirahat\" atau \"diam sejenak\"",
                "","Bicara dan Bahasa","54");
        insertFormKSP("Apakah anak dapat mengancing bajunya atau pakaian boneka?",
                "","Sosialisasi dan Kemandirian","54");
        insertFormKSP("Suruh anak berdiri satu kaki tanpa berpegangan. Jika perlu tunjukkan caranya dan beri anak anda kesempatan " +
                        "melakukannya 3 kali. Dapatkah ia mempertahankan keseimbangan dalam waktu 6 detik atau lebih?",
                "","Gerak Kasar","54");
        insertFormKSP("Jangan mengoreksi/membantu anak. Jangan menyebut kata \"lebih panjang\". Perlihatkan gambar kedua garis ini " +
                        "pada anak. Tanyakan, \"Mana garis yang lebih panjang?\" \nMinta anak menunjuk garis yang lebih oanjang. Setelah anak " +
                        "menunjuk, putar gambar ini dan ulangi pertanyaan tersebut. Setalah anak menunjuk, putar kembali lagi dan ulangi pertanyaan " +
                        "tadi. Apakah anak dapat menunjuk garis yang lebih panjang sebanyak 3 kali dengan benar?",
                "img_16.png","Gerak Halus","54");
        insertFormKSP("Jangan membantu anak dan jangan memberitahu nama gambar ini, suruh anak menggambar seperti contoh " +
                        "ini dikertas kosong yang tersedia. Berikan 3 kali kesempatan. Apakah anak dapat menggambar seperti contoh ini?",
                "img_17.png","Gerak Halus","54");
        insertFormKSP("Ikuti perintah ini dengan seksama. Jangan memberi isyarat dengan telunjuk atau mata pada saat memberikan " +
                        "perintah berikut ini, \n\"Letakkan kertas ini diatas lantai\" \n\"Letakkan kertas ini dibawah kursi\" \n" +
                        "\"Letakkan kertas ini didepan kamu\" \n\"Letakkan kertas ini dibelakang kamu\" \nJawab YA hanya jika anak mengerti " +
                        "arti \"diatas\", \"dibawah\", \"didepan\" dan \"dibelakang\"",
                "","Bicara dan Bahasa","54");

        //usia 60
        insertFormKSP("Isi titik - titik dibawah ini dengan jawaban anak. Jangan membantu kecuali mengulangi pertanyaan. \n" +
                        "\"Apa yang kamu lakukan jika kamu kedinginan?\" \n\"Apakah yang kamu lakukan jika kamu lapar?\" \n" +
                        "\"Apa yang kamu lakukan jika kamu lelah?\" \nJawab YA bila anak menjawab ke 3 pertanyaan tadi dengan benar, " +
                        "bukan dengan gerakan atau isyarat. Jika kedinginan, jawaban yang benar adalah \"menggigil\", \"pakai mantel\", " +
                        "atau \"masuk kedalam kamar\". \nJika lapar jawaban yang benar adalah \"makan\"\nJika lelah, jawaban yang " +
                        "benar adalah \"mengantuk\", \"tidur\", \"berbaring/tidur - tiduran\", \"istirahat\" atau \"diam sejenak\".",
                "","Bicara dan Bahasa","60");
        insertFormKSP("Apakah anak dapat mengancingkan bajunya atau pakaian bonekas?",
                "","Sosialisasi dan Kemandirian","60");
        insertFormKSP("Suruh anak berdiri satu kaki tanpa berpegangan. Jika perlu tunjukkan caranya dan beri anak anda " +
                        "kesempatan melakukannya 3 kali. Dapatkah ia mempertahankan keseimbangan dalam waktu 6 detik atau lebih?",
                "","Gerak Kasar","60");
        insertFormKSP("Jangan mengoreksi atau membantu anak. Jangan menyebut kata \"lebih panjnag\". Perlihatkan gambar kedua " +
                        "garis ini pada anak. Tanyakan, \"Mana garis yang lebih panjang?\" \nMinta anak menunjuk garis yang lebih panjang. " +
                        "Setelah anak menunjuk, putar lembar ini dan ulangi pertanyaan tersebut. Setalah anak menunjuk, putar lembar ini " +
                        "lagi dan ulangi pertanyan tadi. Apakah anak dapat menunjukkan garis yang lebih panjang sebanyak 3 kali dengan benar?",
                "img_16.png","Gerak Halus","60");
        insertFormKSP("Jangan membantu anak dan jangan memberitahu nama gambar ini, suruh anak menggambar seperti contoh " +
                        "ini dikertas kosong yang tersedia. Berikan 3 kali kesempatan. Apakah anak dapat menggambar seperti contoh ini?",
                "img_17.png","Gerak Halus","60");
        insertFormKSP("Ikuti perintah ini dengan seksama. Jangan memberi isyarat dengan telunjuk atau mada pada saat memberikan " +
                        "perintah berikut ini. \n\"Letakkan kertas ini diatas lantai\". \n\"Letakkan kertas ini dibawah kursi\". " +
                        "\n\"Letakkan kertas ini didepan kamu\". \n\"Letakkan kertas ini dibelakang kamu\".\nJawab YA hanya jika anak mengaerti " +
                        "arti \"diatas\", \"dibawah\", \"didepan\", \"dibelakang\"",
                "","Bicara dan Bahasa","60");
        insertFormKSP("Apakah anak bereaksi dengan tenang dan tidak rewel (tanpa menangis atau menggelayut pada anda) pada " +
                        "saat anda meninggalkannya?",
                "","Sosialisasi dan Kemandirian","60");
        insertFormKSP("Jangan menunjuk, membantu atau membetulkan, katakan pada anak, \"Tunjukkan segi empat merah\" " +
                        "\"Tunjukan segi empat kuning\", \"Tunjukkan segi empat biru\", \"Tunjukkan segi empat hijau\". Dapatkah anak " +
                        "menunjuk ke empat warna ini dengan benar?",
                "img_18.png","Bicara dan Bahasa","60");
        insertFormKSP("Suruh anak melompat dengan satu kaki beberapa kali tanpa berpegangan (lompatan dengan kaki tidak ikut dinilai). " +
                        "Apakah ia dapat melompat 2-3 kali dengan satu kaki?",
                "","Gerak Kasar","60");
        insertFormKSP("Dapatkah anak sepenuhnya berpakaian sendiri tanpa bantuan?",
                "","Sosialisasi dan Kemandirian","60");

        //usia 66
        insertFormKSP("Jangan membantu anak dan jangan memberitahu nama gambar ini, suruh anak menggabar seperti contoh ini dikertas " +
                        "kosong yang tersedia. Berikan 3 kali kesempatan. Apakah anak dapat menggambar seperti pada contoh ini?",
                "img_17.png","Gerak Halus","66");
        insertFormKSP("Ikuti perintah ini dengan seksama. Jangan memberi isyarat dengan telunjuk atau mada pada saat memberikan " +
                        "perintah berikut ini. \n\"Letakkan kertas ini diatas lantai\". \n\"Letakkan kertas ini dibawah kursi\". " +
                        "\n\"Letakkan kertas ini didepan kamu\". \n\"Letakkan kertas ini dibelakang kamu\".\nJawab YA hanya jika anak mengaerti " +
                        "arti \"diatas\", \"dibawah\", \"didepan\", \"dibelakang\"",
                "","Bicara dan Bahasa","66");
        insertFormKSP("Apakah anak bereaksi dengan tenang dan tidak rewel (tanpa menangis atau menggelayut pada anda) pada " +
                        "saat anda meninggalkannya?",
                "","Sosialisasi dan Kemandirian","66");
        insertFormKSP("Jangan menunjuk, membantu atau membetulkan, katakan pada anak, \"Tunjukkan segi empat merah\" " +
                        "\"Tunjukan segi empat kuning\", \"Tunjukkan segi empat biru\", \"Tunjukkan segi empat hijau\". Dapatkah anak " +
                        "menunjuk ke empat warna ini dengan benar?",
                "img_18.png","Bicara dan Bahasa","66");
        insertFormKSP("Suruh anak melompat dengan satu kaki beberapa kali tanpa berpegangan (lompatan dengan kaki tidak ikut dinilai). " +
                        "Apakah ia dapat melompat 2-3 kali dengan satu kaki?",
                "","Gerak Kasar","66");
        insertFormKSP("Dapatkah anak sepenuhnya berpakaian sendiri tanpa bantuan?",
                "","Sosialisasi dan Kemandirian","66");
        insertFormKSP("Suruh anak menggambar ditempat kosong yang tersedia. Katakan padanya, \"Buatla gambar orang\". Jangan " +
                        "memberi perintah lebih dari itu. Jangan bertanya/mengingatkan anak bila ada bagian yang belum tergambar. Dalam " +
                        "memberi nilai, hitunglah berapa bagian tubuh yang tergambar. Untuk bagian tubu yang berpasangan seperti mata, telinga, " +
                        "lengan dan kaki, setiap pasang dinilai satu bagian. Dapatkah anak menggambar sedikit 3 bagian tubuh?",
                "","Gerak Halus","66");
        insertFormKSP("Pada gambar orang yang dibuat nomor 7, dapatkah anak menggambar sedikitnya 6 bagian tubuh?",
                "","Gerak Halus","66");
        insertFormKSP("Tulis apa yang dikatakan anak pada alimat - kalimat yang belum selesai ini, jangan membantu kecuali mengulang " +
                        "pertanyaan,\n\"Jika kuda besar maka tikus....\"\n\"Jika api panas maka es....\"\n" +
                        "\"Jika ibu seorang wanita maka ayah seorang....\"\nApakah anak menjawab dengan benar (tikus kecil, es dingin,ayang " +
                        "seorang pria)?",
                "","Bicara dan Bahasa","66");
        insertFormKSP("Apakah anak dapat menangkap bola kecil sebear bola tenis/bola kasti hanya dengan menggunakan kedua tangannya? " +
                        "(Bola besar tidak ikut dinilai)",
                "","Gerak Kasar","66");

        //usia 72
        insertFormKSP("Jangan menunjuk, membantu atau membetulkan, katakan pada anak, \"Tunjukkan segi empat merah\" " +
                        "\"Tunjukan segi empat kuning\", \"Tunjukkan segi empat biru\", \"Tunjukkan segi empat hijau\". Dapatkah anak " +
                        "menunjuk ke empat warna ini dengan benar?",
                "img_18.png","Bicara dan Bahasa","72");
        insertFormKSP("Suruh anak melompat dengan satu kaki beberapa kali tanpa berpegangan (lompatan dengan kaki tidak ikut dinilai). " +
                        "Apakah ia dapat melompat 2-3 kali dengan satu kaki?",
                "","Gerak Kasar","72");
        insertFormKSP("Dapatkah anak sepenuhnya berpakaian sendiri tanpa bantuan?",
                "","Sosialisasi dan Kemandirian","72");
        insertFormKSP("Suruh anak menggambar ditempat kosong yang tersedia. Katakan padanya, \"Buatla gambar orang\". Jangan " +
                        "memberi perintah lebih dari itu. Jangan bertanya/mengingatkan anak bila ada bagian yang belum tergambar. Dalam " +
                        "memberi nilai, hitunglah berapa bagian tubuh yang tergambar. Untuk bagian tubu yang berpasangan seperti mata, telinga, " +
                        "lengan dan kaki, setiap pasang dinilai satu bagian. Dapatkah anak menggambar sedikit 3 bagian tubuh?",
                "","Gerak Halus","72");
        insertFormKSP("Pada gambar orang yang dibuat nomor 7, dapatkah anak menggambar sedikitnya 6 bagian tubuh?",
                "","Gerak Halus","72");
        insertFormKSP("Tulis apa yang dikatakan anak pada alimat - kalimat yang belum selesai ini, jangan membantu kecuali mengulang " +
                        "pertanyaan,\n\"Jika kuda besar maka tikus....\"\n\"Jika api panas maka es....\"\n" +
                        "\"Jika ibu seorang wanita maka ayah seorang....\"\nApakah anak menjawab dengan benar (tikus kecil, es dingin,ayang " +
                        "seorang pria)?",
                "","Sosialisasi dan Kemandirian","72");
        insertFormKSP("Apakah anak dapat menangkap bola kecil sebear bola tenis/bola kasti hanya dengan menggunakan kedua tangannya? " +
                        "(Bola besar tidak ikut dinilai)",
                "","Gerak Kasar","72");
        insertFormKSP("Suruh anak berdiri satu kaki tanpa berpegangan. Jika perlu ditunjukkan caranya dan beri anak anda " +
                        "kesempatan melakukannya 3 kali. Dapatkah ia mempertahankan keseimbangan dalam waktu 11 detik atau lebih?",
                "","Gerak Kasar","72");
        insertFormKSP("Jangan membantu anak dan jangan memberitahu nama gambar ini. Suruh anak menggambar seperti contoh " +
                        "ini dikertas kosong yang tersedia. Berikan 3 kali kesempatan. Apakah anak dapat menggambar seperti contoh ini?",
                "img_19.png","Gerak Halus","72");
        insertFormKSP("Isilah titik - titik dibawah ini dengan jawaban anak. Jangan membantu kecuali mengulangi pertanyaan sampai 3 " +
                        "kali bila anak menanyakannya.\n\"Sendok dibuat dari apa?\"\n\"Sepatu dibuat dari apa?\"\n" +
                        "\"Pintu dibuat dari apa?\"\nApakah anak anda menjawab ke 3 pertanyaan diatas dengan benar? Sendok dibuat dari besi, " +
                        "baja, plastik, kayu.\nSepatu dibuat dari kulit, karet, kain, plastik, kayu.\nPintu dibuat dari kayu, besi, kaca.",
                "","Bicara dan Bahasa","72");
    }
}
