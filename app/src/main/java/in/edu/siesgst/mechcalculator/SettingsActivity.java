package in.edu.siesgst.mechcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText forging, normalise,proof,inspection,misc;
    SessionManager session;
    TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.settings_activity_toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(SettingsActivity.this);

        forging = findViewById(R.id.forging_factor_edit_text);
        normalise = findViewById(R.id.normalising_factor_edit_text);
        proof = findViewById(R.id.proofing_factor_edit_text);
        inspection = findViewById(R.id.insp_factor_edit_text);
        misc = findViewById(R.id.misc_factor_edit_text);
        saveButton = findViewById(R.id.save_button);



        forging.setText(session.getForginCostFactor()+"");
        normalise.setText(session.getNormalisingCostFactor()+"");
        proof.setText(session.getProofMachiningCostFactor()+"");
        inspection.setText(session.getInspectionCostFactor()+"");
        misc.setText(session.getMiscellaneousCostFactor()+"");


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(forging.getText().toString().length()!=0)
                    session.setForginCostFactor(forging.getText().toString());
                if(normalise.getText().toString().length()!=0)
                    session.setNormalisingCostFactor(normalise.getText().toString());
                if(proof.getText().toString().length()!=0)
                    session.setProofMachiningCostFactor(proof.getText().toString());
                if(inspection.getText().toString().length()!=0)
                    session.setInspectionCostFactor(inspection.getText().toString());
                if(misc.getText().toString().length()!=0)
                    session.setMiscellaneousCostFactor(misc.getText().toString());


                Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
//                finish();
            }
        });





    }

}
