package in.edu.siesgst.mechcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private final static String LOG = SessionManager.class.getSimpleName();

    private static final String PREF_NAME = "MechaniCalculator";
    private static final int PRIVATE_MODE = 0;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;






    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void addLaborCostRate(String rate){
        editor.putString(Constants.LABOR_COST_RATE,rate).apply();
    }

    public Double getLaborCostRate(){
        return Double.parseDouble(sharedPreferences.getString(Constants.LABOR_COST_RATE,"0.0"));
    }

    public double getForginCostFactor() {
        return Double.parseDouble(sharedPreferences.getString(Constants.FORGING_COST_FACTOR,"0.0"));
    }

    public void setForginCostFactor(String forginCostFactor) {
        editor.putString(Constants.FORGING_COST_FACTOR,forginCostFactor).apply();

    }

    public double getNormalisingCostFactor() {
        Log.d(LOG,"getNormalisingValue="+sharedPreferences.getString(Constants.NORMALISING_COST_FACTOR,"0.0"));
        return Double.parseDouble(sharedPreferences.getString(Constants.NORMALISING_COST_FACTOR,"0.0"));

    }

    public void setNormalisingCostFactor(String normalisingCostFactor) {
        Log.d(LOG,"setNormalisingValue="+normalisingCostFactor);
        editor.putString(Constants.NORMALISING_COST_FACTOR,normalisingCostFactor).apply();
        getNormalisingCostFactor();

    }

    public double getProofMachiningCostFactor() {
        return Double.parseDouble(sharedPreferences.getString(Constants.PROOF_MACHINING_COST_FACTOR,"0.0"));
    }

    public void setProofMachiningCostFactor(String proofMachiningCostFactor) {
        editor.putString(Constants.PROOF_MACHINING_COST_FACTOR,proofMachiningCostFactor).apply();
    }

    public double getInspectionCostFactor() {
        return Double.parseDouble(sharedPreferences.getString(Constants.INSPECTION_COST_FACTOR,"0.0"));
    }

    public void setInspectionCostFactor(String inspectionCostFactor) {
        editor.putString(Constants.INSPECTION_COST_FACTOR,inspectionCostFactor).apply();
    }

    public double getMiscellaneousCostFactor() {
        return Double.parseDouble(sharedPreferences.getString(Constants.MISCELLANEOUS_COST_FACTOR,"0.0"));
    }

    public void setMiscellaneousCostFactor(String miscellaneousCostFactor) {
        editor.putString(Constants.MISCELLANEOUS_COST_FACTOR,miscellaneousCostFactor).apply();
    }

    public int getBlockBurningMassPercentage() {
        return Integer.parseInt(sharedPreferences.getString(Constants.BLOCK_BURNING_MASS_PERCENT,"0"));
    }

    public void setBlockBurningMassPercentage(String blockBurningMassPercentage) {
        editor.putString(Constants.BLOCK_BURNING_MASS_PERCENT,blockBurningMassPercentage).apply();
    }
    public int getGearBurningMassPercentage() {
        return Integer.parseInt(sharedPreferences.getString(Constants.GEAR_BURNING_MASS_PERCENT,"0"));
    }

    public void setGearBurningMassPercentage(String gearBurningMassPercentage) {
        editor.putString(Constants.GEAR_BURNING_MASS_PERCENT,gearBurningMassPercentage).apply();
    }

    public int getShaftBurningMassPercentage() {
        return Integer.parseInt(sharedPreferences.getString(Constants.SHAFT_BURNING_MASS_PERCENT,"0"));
    }

    public void setShaftBurningMassPercentage(String shaftBurningMassPercentage) {
        editor.putString(Constants.SHAFT_BURNING_MASS_PERCENT,shaftBurningMassPercentage).apply();
    }
}
