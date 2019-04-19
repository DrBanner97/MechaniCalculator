package in.edu.siesgst.mechcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GearActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner gearMaterialSpinner;
    DatabaseManager database;
    EditText costRate,burningMassPercent,length,outer_diam,inner_diam;
    SessionManager session;
    TextView solveButton;
    AlertDialog d;


    Cursor currentMetalCursor = null;

    public static final String TAG = GearActivity.class.getSimpleName();


    TextView header,material,rate,lengthDialog,breadthDialog,heightDialog,volumeText,density,forgingWeightText,burningMassPercentDialog,
            burnedForgingWeight,rawMaterialCost,forgingCost,normalisingCost,proofMachiningCost,inspectionCost,miscellaneousCost,totalCost,
            interestRate,grandTotalCost;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear);

        toolbar = findViewById(R.id.gear_activity_toolbar);
        setSupportActionBar(toolbar);
        gearMaterialSpinner = findViewById(R.id.gear_material_spinner);
        costRate = findViewById(R.id.gear_cost_rate_edit_text);
        burningMassPercent = findViewById(R.id.gear_burning_mass_percentage_edit_text);
        length = findViewById(R.id.gear_length_edit_text);
        outer_diam = findViewById(R.id.gear_outer_diameter_edit_text);
        inner_diam = findViewById(R.id.gear_inner_diameter_edit_text);
        solveButton = findViewById(R.id.solve_gear_text);

        database = new DatabaseManager(GearActivity.this);
        session = new SessionManager(GearActivity.this);



        String[] arraySpinner = new String[] {
                "Alloy Steel",
                "Aluminum",
                "Carbon Steel",
                "Nickel Based Alloys",
                "Stainless Steel",
                "Titanium",
                "Tool Steel"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        gearMaterialSpinner.setAdapter(adapter);

        gearMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                Log.d(TAG,"selected="+selectedItem);
                currentMetalCursor = database.getMetalInfo(selectedItem);
                currentMetalCursor.moveToFirst();
                costRate.setText(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_COST)));
                burningMassPercent.setText(session.getGearBurningMassPercentage()+"");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(costRate.getText().toString().trim().length()==0)
                    costRate.setText(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_COST)));
                if(burningMassPercent.getText().toString().length()==0)
                    burningMassPercent.setText(session.getGearBurningMassPercentage()+"");
                if(outer_diam.getText().toString().trim().length()==0 || inner_diam.getText().toString().trim().length()==0 || length.getText().toString().trim().length()==0 ){

                    if (outer_diam.getText().toString().trim().length()==0)
                        outer_diam.setError("Missing field");
                    if (inner_diam.getText().toString().trim().length()==0)
                        inner_diam.setError("Missing field");
                    if (length.getText().toString().trim().length()==0)
                        length.setError("Missing field");


                }
                else{

                    hideSoftKeyboard(GearActivity.this);
                    if(currentMetalCursor!=null){
                        database.insertMetal(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_ID)),
                                currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_NAME)),
                                costRate.getText().toString(),
                                currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY)));

                        session.setGearBurningMassPercentage(burningMassPercent.getText().toString());
                    }
                    calculate();


                }


            }
        });
    }


    public void calculate(){

        final double volume = (double)(Math.PI/4) * (Math.pow(Double.parseDouble(outer_diam.getText().toString()),2) - Math.pow(Double.parseDouble(inner_diam.getText().toString()),2) ) * Double.parseDouble(length.getText().toString());
        final double massValue = Double.parseDouble(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY))) * volume;
        final double forgingWeight = massValue/(1E6);

        Log.d(TAG,"val = "+(Math.pow(Double.parseDouble(outer_diam.getText().toString()),2) - Math.pow(Double.parseDouble(inner_diam.getText().toString()),2) ));

        AlertDialog.Builder builder = new AlertDialog.Builder(GearActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.calculation_dialog, null);
        builder.setView(view);


        header = view.findViewById(R.id.calculation_header);
        material = view.findViewById(R.id.material_text);
        rate = view.findViewById(R.id.rate_text);
        lengthDialog = view.findViewById(R.id.length_text);
        breadthDialog = view.findViewById(R.id.breadth_text);
        heightDialog = view.findViewById(R.id.height_text);
        volumeText = view.findViewById(R.id.volume_text);
        density = view.findViewById(R.id.density_text);

        forgingWeightText = view.findViewById(R.id.forging_weight);
        burningMassPercentDialog = view.findViewById(R.id.burning_mass_percent);
        burnedForgingWeight = view.findViewById(R.id.burned_forging_weight);


        rawMaterialCost = view.findViewById(R.id.raw_material_cost);
        forgingCost = view.findViewById(R.id.forging_cost);
        normalisingCost = view.findViewById(R.id.normalising_cost);
        proofMachiningCost = view.findViewById(R.id.proof_machining_cost);
        inspectionCost = view.findViewById(R.id.inspection_cost);
        miscellaneousCost = view.findViewById(R.id.miscellaneous_cost);

        totalCost = view.findViewById(R.id.total_cost);
        interestRate = view.findViewById(R.id.interest_rate);

        grandTotalCost = view.findViewById(R.id.grand_total_cost);

        final MaterialButton save = view.findViewById(R.id.material_icon_button);

        header.setText("Calculation Details");
        material.setText("Material:"+gearMaterialSpinner.getSelectedItem().toString());
        rate.setText("Rate: Rs "+costRate.getText().toString()+" per kg");
        lengthDialog.setText("Length: "+length.getText().toString()+" mm");
        breadthDialog.setText("Outer Diameter: "+outer_diam.getText().toString()+" mm");
        heightDialog.setText("Inner Diameter: "+inner_diam.getText().toString()+" mm");
        volumeText.setText("Volume: "+round((volume/1E3),2)+" cubic cm");
        density.setText("Density: "+currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY))+" gram per cubic cm");
        forgingWeightText.setText("Forging Weight: "+round(forgingWeight,2)+" kg");

        burningMassPercentDialog.setText("Burning Mass Percentage: "+session.getGearBurningMassPercentage()+"%");

        double burnedForgingWeightValue = forgingWeight*(1+((double)session.getGearBurningMassPercentage()/100));
        burnedForgingWeight.setText("Burned Forging Weight: "+round(burnedForgingWeightValue,2)+" kg");
        Double totalSum= 0.0;

        rawMaterialCost.setText("Raw Material Cost: Rs "+round(burnedForgingWeightValue*Double.parseDouble(costRate.getText().toString()),2));
        totalSum+=(burnedForgingWeightValue*Double.parseDouble(costRate.getText().toString()));
        forgingCost.setText("Forging Cost: Rs "+round(forgingWeight*session.getForginCostFactor(),2));
        totalSum+=forgingWeight*session.getForginCostFactor();
        normalisingCost.setText("Normalising Cost: Rs "+round(forgingWeight*session.getNormalisingCostFactor(),2));
        totalSum+=forgingWeight*session.getNormalisingCostFactor();
        proofMachiningCost.setText("Proof Machining Cost: Rs "+round(burnedForgingWeightValue*session.getProofMachiningCostFactor(),2));
        totalSum+=burnedForgingWeightValue*session.getProofMachiningCostFactor();
        miscellaneousCost.setText("Miscellaneous Cost: Rs "+round(burnedForgingWeightValue*session.getMiscellaneousCostFactor(),2));
        totalSum+=burnedForgingWeightValue*session.getMiscellaneousCostFactor();
        inspectionCost.setText("Inspection Cost: Rs "+round(burnedForgingWeightValue*session.getInspectionCostFactor(),2));
        totalSum+=burnedForgingWeightValue*session.getInspectionCostFactor();


        totalCost.setText("Total Cost: Rs "+round(totalSum,2));
        interestRate.setText("Interest Rate: 5%");
        grandTotalCost.setText("Grand Total: Rs "+round(totalSum*1.05,2));





        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAsPdf();
            }
        });


        d = builder.show();

    }


    public void saveAsPdf(){
        if (ContextCompat.checkSelfPermission(GearActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            handlePermission();

        } else {
            // Permission has already been granted
            try
            {

                Date c = Calendar.getInstance().getTime();
                BaseFont urName = BaseFont.createFont("assets/fonts/Roboto-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);


                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH_mm_ss");
                String formattedDate = df.format(c);

                final String file_name="Calculated_"+formattedDate;

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory() + "/"+file_name+".pdf"));
                document.open();

                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

                Font headerFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
                Chunk headerChunk = new Chunk("Calculation Details", headerFont);
                Paragraph headerpara = new Paragraph(headerChunk);
                headerpara.setAlignment(Element.ALIGN_CENTER);
                document.add(headerpara);

                Font normalTextFont = new Font(urName, 12.0f, Font.NORMAL, BaseColor.BLACK);
                Chunk normalChunk = new Chunk(material.getText().toString(), normalTextFont);
                Paragraph normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(rate.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(lengthDialog.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(breadthDialog.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(heightDialog.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(volumeText.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(density.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(forgingWeightText.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(burningMassPercentDialog.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(burnedForgingWeight.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);


                document.add(new Paragraph(""));
                document.add(new Chunk(lineSeparator));
                document.add(new Paragraph(""));

                normalChunk = new Chunk(rawMaterialCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(forgingCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(normalisingCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(proofMachiningCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(inspectionCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(miscellaneousCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);


                document.add(new Paragraph(""));
                document.add(new Chunk(lineSeparator));
                document.add(new Paragraph(""));

                normalChunk = new Chunk(totalCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                normalChunk = new Chunk(interestRate.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);

                document.add(new Paragraph(""));
                document.add(new Chunk(lineSeparator));
                document.add(new Paragraph(""));

                normalChunk = new Chunk(grandTotalCost.getText().toString(), normalTextFont);
                normalPara = new Paragraph(normalChunk);
                document.add(normalPara);











                document.close();
                Log.d("OK", "done");



                d.dismiss();

                Snackbar.make(findViewById(R.id.gear_activity_container), "Saved as PDF", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                File file = new File(Environment.getExternalStorageDirectory() +"/"+ file_name+".pdf");

                                Uri photoURI = FileProvider.getUriForFile(GearActivity.this, GearActivity.this.getApplicationContext().getPackageName() + ".my.package.name.provider", file);

                                Intent target = new Intent(Intent.ACTION_VIEW);
                                target.setDataAndType(photoURI,"application/pdf");
                                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                                Intent intent = Intent.createChooser(target, "Open");
                                try {
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    // Instruct the user to install a PDF reader here, or something
                                }

                                // Respond to the click, such as by undoing the modification that caused
                                // this message to be displayed
                            }
                        })
                        .show();
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (DocumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }



    }



    public void handlePermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(GearActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(GearActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() !=null && inputMethodManager!=null)
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
