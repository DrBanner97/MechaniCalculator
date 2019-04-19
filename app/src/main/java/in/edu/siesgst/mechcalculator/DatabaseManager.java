package in.edu.siesgst.mechcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "mechanicalculator.db";
    private static final int DB_VERSION = 1;

    public static final String TAG = DatabaseManager.class.getSimpleName();



    private static final String CREATE_METAL_TABLE = " CREATE TABLE " + Constants.METAL_COST_TABLE + " ( " + Constants.METAL_ID + " VARCHAR DEFAULT NULL , " + Constants.METAL_NAME + " VARCHAR DEFAULT NULL , " +Constants.METAL_DENSITY + " VARCHAR DEFAULT NULL , " +
            Constants.METAL_COST +" VARCHAR DEFAULT '0' )";










    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_METAL_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertMetal(String id,String name,String cost,String density)
    {

        Log.d(TAG,"insert "+name+" "+cost);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.METAL_COST_TABLE + " WHERE " + Constants.METAL_NAME + " = '" + name + "'", null);
        int flag = 0;
        if (cursor.getCount() > 0)
        { flag = 1; }

        ContentValues values = new ContentValues();
        values.put(Constants.METAL_ID, id);
        values.put(Constants.METAL_NAME, name);
        values.put(Constants.METAL_COST, cost);
        values.put(Constants.METAL_DENSITY, density);





        if (flag == 0)
        {
            db.insert(Constants.METAL_COST_TABLE, null, values);

        }
        else
        {
            db.update(Constants.METAL_COST_TABLE, values, Constants.METAL_ID + " = '" + id + "'", null);
        }


        cursor.close();
    }


    public Cursor getMetalInfo(String metalName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.METAL_COST_TABLE + " WHERE " + Constants.METAL_NAME + " = '" + metalName + "'", null);

        return cursor;


    }


    public void initMetalTable(){


        insertMetal("0","Alloy Steel","60","8.05");
        insertMetal("1","Aluminum","70","2.7");
        insertMetal("2","Carbon Steel","59","7.85");
        insertMetal("3","Nickel Based Alloys","70","8.44");
        insertMetal("4","Stainless Steel","70","8");
        insertMetal("5","Titanium","70","4.506");
        insertMetal("6","Tool Steel","70","7.87");

    }
}
