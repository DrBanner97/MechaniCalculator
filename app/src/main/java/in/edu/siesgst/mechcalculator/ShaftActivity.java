package in.edu.siesgst.mechcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShaftActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView solveButton;

    TextView d1_placeholder,d2_placeholder,d3_placeholder,d4_placeholder,l1_placeholder,l2_placeholder,l3_placeholder,l4_placeholder;
    EditText d1_edit_text, d2_edit_text, d3_edit_text, d4_edit_text, l1_edit_text, l2_edit_text, l3_edit_text, l4_edit_text,costRate,burningMassPercent;
    Spinner shaftMaterialSpinner,steps;

    Cursor currentMetalCursor = null;

    List<EditText> d_values,l_values;
    List<TextView> d1l1_dialog_values;

    public static final String TAG = ShaftActivity.class.getSimpleName();

    DatabaseManager database;
    SessionManager session;

    int currentSteps;
    AlertDialog.Builder builder;
    AlertDialog d;

    View dialogView;

    TextView header,material,rate,d1l1Dialog,d2l2dialog,d3l3dialog,d4l4dialog,volumeText,density,forgingWeightText,burningMassPercentDialog,
            burnedForgingWeight,rawMaterialCost,forgingCost,normalisingCost,proofMachiningCost,inspectionCost,miscellaneousCost,totalCost,
            interestRate,grandTotalCost;

    MaterialButton save;


    public void showFields(int n){
        currentSteps = n;
        switch (n){
            case 1:
                d1_placeholder.setVisibility(View.VISIBLE);
                d1_edit_text.setVisibility(View.VISIBLE);
                l1_placeholder.setVisibility(View.VISIBLE);
                l1_edit_text.setVisibility(View.VISIBLE);

                d2_placeholder.setVisibility(View.GONE);
                d2_edit_text.setVisibility(View.GONE);
                l2_placeholder.setVisibility(View.GONE);
                l2_edit_text.setVisibility(View.GONE);

                d3_placeholder.setVisibility(View.GONE);
                d3_edit_text.setVisibility(View.GONE);
                l3_placeholder.setVisibility(View.GONE);
                l3_edit_text.setVisibility(View.GONE);


                d4_placeholder.setVisibility(View.GONE);
                d4_edit_text.setVisibility(View.GONE);
                l4_placeholder.setVisibility(View.GONE);
                l4_edit_text.setVisibility(View.GONE);



                break;
            case 2:
                d1_placeholder.setVisibility(View.VISIBLE);
                d1_edit_text.setVisibility(View.VISIBLE);
                l1_placeholder.setVisibility(View.VISIBLE);
                l1_edit_text.setVisibility(View.VISIBLE);

                d2_placeholder.setVisibility(View.VISIBLE);
                d2_edit_text.setVisibility(View.VISIBLE);
                l2_placeholder.setVisibility(View.VISIBLE);
                l2_edit_text.setVisibility(View.VISIBLE);

                d3_placeholder.setVisibility(View.GONE);
                d3_edit_text.setVisibility(View.GONE);
                l3_placeholder.setVisibility(View.GONE);
                l3_edit_text.setVisibility(View.GONE);

                d4_placeholder.setVisibility(View.GONE);
                d4_edit_text.setVisibility(View.GONE);
                l4_placeholder.setVisibility(View.GONE);
                l4_edit_text.setVisibility(View.GONE);


                break;
            case 3:
                d1_placeholder.setVisibility(View.VISIBLE);
                d1_edit_text.setVisibility(View.VISIBLE);
                l1_placeholder.setVisibility(View.VISIBLE);
                l1_edit_text.setVisibility(View.VISIBLE);

                d2_placeholder.setVisibility(View.VISIBLE);
                d2_edit_text.setVisibility(View.VISIBLE);
                l2_placeholder.setVisibility(View.VISIBLE);
                l2_edit_text.setVisibility(View.VISIBLE);

                d3_placeholder.setVisibility(View.VISIBLE);
                d3_edit_text.setVisibility(View.VISIBLE);
                l3_placeholder.setVisibility(View.VISIBLE);
                l3_edit_text.setVisibility(View.VISIBLE);

                d4_placeholder.setVisibility(View.GONE);
                d4_edit_text.setVisibility(View.GONE);
                l4_placeholder.setVisibility(View.GONE);
                l4_edit_text.setVisibility(View.GONE);


                break;
            case 4:
                d1_placeholder.setVisibility(View.VISIBLE);
                d1_edit_text.setVisibility(View.VISIBLE);
                l1_placeholder.setVisibility(View.VISIBLE);
                l1_edit_text.setVisibility(View.VISIBLE);

                d2_placeholder.setVisibility(View.VISIBLE);
                d2_edit_text.setVisibility(View.VISIBLE);
                l2_placeholder.setVisibility(View.VISIBLE);
                l2_edit_text.setVisibility(View.VISIBLE);

                d3_placeholder.setVisibility(View.VISIBLE);
                d3_edit_text.setVisibility(View.VISIBLE);
                l3_placeholder.setVisibility(View.VISIBLE);
                l3_edit_text.setVisibility(View.VISIBLE);

                d4_placeholder.setVisibility(View.VISIBLE);
                d4_edit_text.setVisibility(View.VISIBLE);
                l4_placeholder.setVisibility(View.VISIBLE);
                l4_edit_text.setVisibility(View.VISIBLE);

                break;
        }



    }



    public void showDialogFields(int n){
        switch (n){
            case 1:
                d2l2dialog.setVisibility(View.GONE);
                d3l3dialog.setVisibility(View.GONE);
                d4l4dialog.setVisibility(View.GONE);
                break;
            case 2:
                d2l2dialog.setVisibility(View.VISIBLE);
                d3l3dialog.setVisibility(View.GONE);
                d4l4dialog.setVisibility(View.GONE);
                break;
            case 3:
                d2l2dialog.setVisibility(View.VISIBLE);
                d3l3dialog.setVisibility(View.VISIBLE);
                d4l4dialog.setVisibility(View.GONE);
                break;
            case 4:
                d2l2dialog.setVisibility(View.VISIBLE);
                d3l3dialog.setVisibility(View.VISIBLE);
                d4l4dialog.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shaft);

        toolbar = findViewById(R.id.shaft_activity_toolbar);
        setSupportActionBar(toolbar);
        database = new DatabaseManager(ShaftActivity.this);
        session = new SessionManager(ShaftActivity.this);

        d1_placeholder = findViewById(R.id.d1_placeholder);
        d2_placeholder = findViewById(R.id.d2_placeholder);
        d3_placeholder = findViewById(R.id.d3_placeholder);
        d4_placeholder = findViewById(R.id.d4_placeholder);

        l1_placeholder = findViewById(R.id.l1_placeholder);
        l2_placeholder = findViewById(R.id.l2_placeholder);
        l3_placeholder = findViewById(R.id.l3_placeholder);
        l4_placeholder = findViewById(R.id.l4_placeholder);

        d1_edit_text = findViewById(R.id.d1_edit_text);
        d2_edit_text = findViewById(R.id.d2_edit_text);
        d3_edit_text = findViewById(R.id.d3_edit_text);
        d4_edit_text = findViewById(R.id.d4_edit_text);

        l1_edit_text = findViewById(R.id.l1_edit_text);
        l2_edit_text = findViewById(R.id.l2_edit_text);
        l3_edit_text = findViewById(R.id.l3_edit_text);
        l4_edit_text = findViewById(R.id.l4_edit_text);

        shaftMaterialSpinner = findViewById(R.id.shaft_material_spinner);
        costRate = findViewById(R.id.shaft_cost_rate_edit_text);
        burningMassPercent = findViewById(R.id.shaft_burning_mass_percentage_edit_text);
        steps = findViewById(R.id.steps_spinner);

        d_values = new ArrayList<>();
        d_values.add(d1_edit_text);
        d_values.add(d2_edit_text);
        d_values.add(d3_edit_text);
        d_values.add(d4_edit_text);

        l_values = new ArrayList<>();
        l_values.add(l1_edit_text);
        l_values.add(l2_edit_text);
        l_values.add(l3_edit_text);
        l_values.add(l4_edit_text);

        d1l1_dialog_values = new ArrayList<>();



        solveButton = findViewById(R.id.solve_shaft_text);

        String[] arraySpinner = new String[] {
                "Alloy Steel",
                "Aluminum",
                "Carbon Steel",
                "Nickel Based Alloys",
                "Stainless Steel",
                "Titanium",
                "Tool Steel"
        };

        String[] stepsSpinnerArray = new String[] {
                "1",
                "2",
                "3",
                "4"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        shaftMaterialSpinner.setAdapter(adapter);

        shaftMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                Log.d(TAG,"selected="+selectedItem);
                currentMetalCursor = database.getMetalInfo(selectedItem);
                currentMetalCursor.moveToFirst();
                costRate.setText(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_COST)));
                burningMassPercent.setText(session.getShaftBurningMassPercentage()+"");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> stepsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stepsSpinnerArray);
        stepsAdapter.setDropDownViewResource(R.layout.spinner_item);
        steps.setAdapter(stepsAdapter);

        steps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                showFields(Integer.parseInt(selectedItem));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ShaftActivity.this);
                if(costRate.getText().toString().trim().length()==0)
                    costRate.setText(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_COST)));
                if(burningMassPercent.getText().toString().length()==0)
                    burningMassPercent.setText(session.getShaftBurningMassPercentage()+"");

                int flag =0;

                for(int i=0;i<currentSteps;i++){
                    if(d_values.get(i).getText().toString().length()==0 || l_values.get(i).getText().toString().length()==0 ){
                        flag = 1;
                        d_values.get(i).setError("Missing Field");
                        l_values.get(i).setError("Missing Field");
                    }

                }

                if(flag ==0) {

                    if (currentMetalCursor != null) {
                        database.insertMetal(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_ID)),
                                currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_NAME)),
                                costRate.getText().toString(),
                                currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY)));

                        session.setShaftBurningMassPercentage(burningMassPercent.getText().toString());
                    }

                    calculate();
                }
            }
        });




    }


    public void calculate(){

        double volume=0;

        builder = new AlertDialog.Builder(ShaftActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView= inflater.inflate(R.layout.shaft_calculation_dialog, null);
        builder.setView(dialogView);


        material = dialogView.findViewById(R.id.material_text);
        rate = dialogView.findViewById(R.id.rate_text);
        d1l1Dialog = dialogView.findViewById(R.id.d1_l1_text);
        d2l2dialog = dialogView.findViewById(R.id.d2_l2_text);
        d3l3dialog = dialogView.findViewById(R.id.d3_l3_text);
        d4l4dialog = dialogView.findViewById(R.id.d4_l4_text);

        d1l1_dialog_values.add(d1l1Dialog);
        d1l1_dialog_values.add(d2l2dialog);
        d1l1_dialog_values.add(d3l3dialog);
        d1l1_dialog_values.add(d4l4dialog);


        volumeText = dialogView.findViewById(R.id.volume_text);
        density = dialogView.findViewById(R.id.density_text);

        forgingWeightText = dialogView.findViewById(R.id.forging_weight);
        burningMassPercentDialog = dialogView.findViewById(R.id.burning_mass_percent);
        burnedForgingWeight = dialogView.findViewById(R.id.burned_forging_weight);


        rawMaterialCost = dialogView.findViewById(R.id.raw_material_cost);
        forgingCost = dialogView.findViewById(R.id.forging_cost);
        normalisingCost = dialogView.findViewById(R.id.normalising_cost);
        proofMachiningCost = dialogView.findViewById(R.id.proof_machining_cost);
        inspectionCost = dialogView.findViewById(R.id.inspection_cost);
        miscellaneousCost = dialogView.findViewById(R.id.miscellaneous_cost);

        totalCost = dialogView.findViewById(R.id.total_cost);
        interestRate = dialogView.findViewById(R.id.interest_rate);

        grandTotalCost = dialogView.findViewById(R.id.grand_total_cost);


        header = dialogView.findViewById(R.id.calculation_header);


        save= dialogView.findViewById(R.id.material_icon_button);


        showDialogFields(currentSteps);

        for(int i=0;i<currentSteps;i++){
            volume+= Math.pow(Double.parseDouble(d_values.get(i).getText().toString()),2) *Double.parseDouble(l_values.get(i).getText().toString());
        }
        volume*=((double) Math.PI/4);



        header.setText("Calculation Details");
        material.setText("Material: "+shaftMaterialSpinner.getSelectedItem().toString());
        rate.setText("Rate: Rs "+costRate.getText().toString()+" per kg");

        d1l1Dialog.setText("D1: "+d1_edit_text.getText().toString()+" mm  L1: "+l1_edit_text.getText().toString()+" mm");
        d2l2dialog.setText("D2: "+d2_edit_text.getText().toString()+" mm  L2: "+l2_edit_text.getText().toString()+" mm");
        d3l3dialog.setText("D3: "+d3_edit_text.getText().toString()+" mm  L3: "+l3_edit_text.getText().toString()+" mm");
        d4l4dialog.setText("D4: "+d4_edit_text.getText().toString()+" mm  L4: "+l4_edit_text.getText().toString()+" mm");

        Double forgingWeight = volume* Double.parseDouble(currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY)));
        forgingWeight=(double)forgingWeight/1E6;


        volumeText.setText("Volume: "+round((volume/1E3),2)+" cubic cm");
        density.setText("Density: "+currentMetalCursor.getString(currentMetalCursor.getColumnIndex(Constants.METAL_DENSITY))+" gram per cubic cm");
        forgingWeightText.setText("Forging Weight: "+round(forgingWeight,2)+" kg");
        burningMassPercentDialog.setText("Burning Mass Percentage: "+session.getShaftBurningMassPercentage()+"%");
        double burnedForgingWeightValue = forgingWeight*(1+((double)session.getShaftBurningMassPercentage()/100));
        burnedForgingWeight.setText("Burned Forging Weight: "+round(burnedForgingWeightValue,2));



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
        if (ContextCompat.checkSelfPermission(ShaftActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            handlePermission();

        } else {
            // Permission has already been granted
            try
            {

                Date c = Calendar.getInstance().getTime();
                BaseFont urName = BaseFont.createFont("assets/fonts/Roboto-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);


                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
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


                for(int i=0;i<currentSteps;i++){
                    normalChunk = new Chunk(d1l1_dialog_values.get(i).getText().toString(), normalTextFont);
                    normalPara = new Paragraph(normalChunk);
                    document.add(normalPara);


                }

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

                Snackbar.make(findViewById(R.id.shaft_activity_container), "Saved as PDF", Snackbar.LENGTH_LONG)
//                        .setAction("Open", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                File file = new File(Environment.getExternalStorageDirectory() +"/"+ file_name+".pdf");
//                                Intent target = new Intent(Intent.ACTION_VIEW);
//                                target.setDataAndType(Uri.fromFile(file),"application/pdf");
//                                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                                Intent intent = Intent.createChooser(target, "Open");
//                                try {
//                                    startActivity(intent);
//                                } catch (ActivityNotFoundException e) {
//                                    // Instruct the user to install a PDF reader here, or something
//                                }
//
//                                // Respond to the click, such as by undoing the modification that caused
//                                // this message to be displayed
//                            }
//                        })
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(ShaftActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(ShaftActivity.this,
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
