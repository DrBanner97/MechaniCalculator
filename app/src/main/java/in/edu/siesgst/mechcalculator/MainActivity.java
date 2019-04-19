package in.edu.siesgst.mechcalculator;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout blockLayout,gearLayout,shaftLayout;
    DatabaseManager db;
    SessionManager session;

    ImageView settings;

    public static final String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blockLayout = findViewById(R.id.block_layout);
        gearLayout = findViewById(R.id.gear_layout);
        settings= findViewById(R.id.settings);
        shaftLayout = findViewById(R.id.shaft_layout);
        db = new DatabaseManager(MainActivity.this);
        session = new SessionManager(MainActivity.this);
        db.initMetalTable();
        initSessionValues();


        blockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BlockActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });
        gearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GearActivity.class));

            }
        });
        shaftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShaftActivity.class));
            }
        });

    }


    public void initSessionValues(){


        if(session.getBlockBurningMassPercentage()==0)
            session.setBlockBurningMassPercentage("15");
        if(session.getGearBurningMassPercentage()==0)
            session.setGearBurningMassPercentage("20");
        if(session.getShaftBurningMassPercentage()==0)
            session.setShaftBurningMassPercentage("15");


        if(session.getForginCostFactor()==0.0)
            session.setForginCostFactor("25");
        if(session.getNormalisingCostFactor()==0.0)
            session.setNormalisingCostFactor("3");
        if(session.getProofMachiningCostFactor()==0.0)
            session.setProofMachiningCostFactor("3");
        if(session.getInspectionCostFactor()==0.0)
            session.setInspectionCostFactor("2");
        if(session.getMiscellaneousCostFactor()==0.0)
            session.setMiscellaneousCostFactor("3");
    }
}
