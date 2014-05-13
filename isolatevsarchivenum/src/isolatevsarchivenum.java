/**
 * Created by malcolm on 5/4/14.
 */

import com.oracle.javafx.jmx.json.JSONReader;
import processing.data.JSONArray;
import processing.data.JSONObject;
import sun.jvm.hotspot.asm.sparc.SPARCTrapInstruction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.*;

import processing.core.*;
import processing.*;
import processing.core.PApplet.*;


public class isolatevsarchivenum
{
    public static void main(String[] args) throws IOException
    {

        // prefix "acc" "accessionVsIsolateNum" file
        String fileToParse2 = "/Users/malcolm/Documents/Cluster/salmonella_meta/isolateNum_vs_accessionNum.txt";
        BufferedReader accReader = null;

        //Input file which needs to be parsed
        String fileToParse1 = "/Users/malcolm/Documents/Cluster/salmonella_meta/Table_S2.txt";
        BufferedReader tableReader = null;
        int numRows=150; //hack
        final int accColNum=2;
        final String DELIMITER = " ";
        final String DELIMITER2= "\t";
        Strain[] strainObj=new Strain[numRows];

        try
        {
            tableReader = new BufferedReader(new FileReader(fileToParse1));
            String line   = "";
            String accline= "";
            int row=0;
            while ((line = tableReader.readLine()) != null)
            {
                int col = 0;
                strainObj[row] = new Strain();
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                for (String token : tokens) {

                    switch(col) {

                        case 0: strainObj[row].setIsolate(token);
                                    accReader = new BufferedReader(new FileReader(fileToParse2));
                                    while ((accline = accReader.readLine()) != null){
                                        String[] acctokens = accline.split(DELIMITER2);
                                        if(acctokens[0].equals(token)) {
                                            strainObj[row].setaccNum(acctokens[accColNum]);
                                            strainObj[row].setsecondIsolate(acctokens[0]);
                                        }
                                    }
                                break;
                        case 1: strainObj[row].setIsolated_from(token);
                                break;
                        case 2: strainObj[row].setAMR_phenotypic_profile(token);
                                break;
                        case 3: strainObj[row].setYear_of_isolation(token);
                                break;
                        case 4: strainObj[row].setResistance_determinants_identified(token);
                                break;
                    }
                    //System.out.print(token);
                    col++;

                }
                //System.out.println();
                row++;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                tableReader.close();
                accReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


////////// JSON READER
//////////////////////////////////////////////////////////////////////////////

     //   JsonReader reader = Json.createReader(new FileReader("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1.json"));
  //    JsonWriter writer = Json.createWriter(new FileWriter("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1_out.json"));


///////////////////////////////////////////
        //JSONObject json;
        processing.core.PApplet myApplet = new PApplet(); //= processing.core.PApplet.loadJSONObject("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1.json");
        JSONArray myValues = myApplet.loadJSONArray("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1.json");

        jsonModify(myValues,strainObj);

        //JSONObject name = json.getJSONObject("children");
        //JSONObject children = json.getJSONObject("children");
        // JSONObject children= mapArray.getJSONObject(1);

       /* JSONArray mapArray = json.getJSONArray("children");
         String test1 = mapArray[1].getString(1);
         */

        //processing.data.JSONObject
        //json. ("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1.json");
        //processing.data.
        //json = processing.core.PApplet.loadJSONObject("/Users/malcolm/Documents/Cluster/salmonella_meta/testJson1.json");

        //int id = json.getInt("id");
        System.out.println();
/////////////////////////////////////////////////////////////////////////////
    }

/// Method for recursive callback of the JSON tree structure
    public static void jsonModify(JSONArray values, Strain[] strains){
        for (int i = 0; i < values.size(); i++) {
            JSONObject entry = values.getJSONObject(i);
            String name = entry.getString("name");
            double length = entry.getDouble("length");
            System.out.println(name + ", " + length + ", ");


            //for(Strain strain : strains) //*WOULDNT WORK FOR SOME REASON?
           for(int j=0; j<strains.length; j++)
            {
                Strain strain = strains[j];

                if( strain == null)
                    throw new RuntimeException("Strain is null");
                else
                    System.out.println("get " + strain.getIsolate());

                if (strain.getIsolate().startsWith(name)) {
                    entry.setString("Isolate", strain.getIsolate());
                    entry.setString("Isolated_from", strain.getIsolated_from());
                    entry.setString("AMR_phenotypic_profile", strain.getAMR_phenotypic_profile());
                    entry.setString("Year_of_isolation", strain.getYear_of_isolation());
                    entry.setString("Resistance_determinants_identified", strain.getResistance_determinants_identified());
                    entry.setString("accNum", strain.getaccNum()); // this is the File Name!
                    entry.setString("secondIsolate", strain.getsecondIsolate());
                }
            }



            if(entry.hasKey("children")) {
                JSONArray newValues = entry.getJSONArray("children");
                jsonModify(newValues,strains);
            }
        }
    }






}





class Strain {
    private String Isolate;
    //public void Strain(String a){this.Isolate=a;}
    //public String[] Header;
    private String Isolated_from;
    private String AMR_phenotypic_profile;
    private String Year_of_isolation;
    private String Resistance_determinants_identified;
    private String accNum; // this is the File Name!
    private String secondIsolate;


    public void setIsolate(String a){this.Isolate=a;}
    public String getIsolate(){return this.Isolate;}

    public void setIsolated_from(String a){this.Isolated_from=a;}
    public String getIsolated_from(){return this.Isolated_from;}

    public void setAMR_phenotypic_profile(String a){this.AMR_phenotypic_profile=a;}
    public String getAMR_phenotypic_profile(){return this.AMR_phenotypic_profile;}

    public void setYear_of_isolation(String a){this.Year_of_isolation=a;}
    public String getYear_of_isolation(){return this.Year_of_isolation;}

    public void setResistance_determinants_identified(String a){this.Resistance_determinants_identified=a;}
    public String getResistance_determinants_identified(){return this.Resistance_determinants_identified;}


    public void setaccNum(String a){this.accNum=a;}
    public String getaccNum(){return this.accNum;}

    public void setsecondIsolate(String a){this.secondIsolate=a;}
    public String getsecondIsolate(){return this.secondIsolate;}
}



/*
int arrayLength=150;
//Set the delimiter used in file
tableScanner.useDelimiter(" ");

        //Get all tokens and store them in some data structure
        //I am just printing them
        strain[] a = new strain[arrayLength]; //hack
        int test1=0;
        while (tableScanner.hasNext())
        {
        String temp = tableScanner.next();
        //System.out.print(tableScanner.next() + "|");
        System.out.print(temp + "|");
        // tableScanner.
        //System.out.println(test1++);
        if(temp.contains("\n")){
        System.out.println(test1++);
        }


        }

        //Do not forget to close the scanner
        tableScanner.close();
        */