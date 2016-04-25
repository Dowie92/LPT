package alexdissertation.lpt;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alex on 19/04/2016.
 */

public class LayerTitles {
    private static ArrayList<String> titlesArray= new ArrayList<String>();
    private static String TitleHomeConcat;
    private static String TitleSubLConcat;


    private static String HomeTitle;
    private static String HomeLayer;
    private static String HomePosition;

    private static String SubLTitle;
    private static String Layer;
    private static String Position;

    private String titleFullConcat = "";



    // could group these together to concat...then add to arraylist... one method for home activ.. another for subt run them
    //seperately to allow fo it to work more efficiently


       //Setters to set all of the values...
    public static void setHomeTitle(String HomeTitle){
        LayerTitles.HomeTitle = HomeTitle;
        Log.d ("LayerTitle", HomeTitle);
    }
    public static void setHomeLayer() {
        LayerTitles.HomeLayer = String.valueOf(1);
    }
    public static void setHomePosition(String Position){
        LayerTitles.HomePosition = Position;
    }

    public static void setSubTitle(String subLTitle){
        LayerTitles.SubLTitle=subLTitle;
        //titlesArray.add(title);
    }
    public static void setLayer(String Layer){
        LayerTitles.Layer=Layer;
        //titlesArray.add(Layer);
    }
    public static void setPosition(String Position){
        LayerTitles.Position = Position;
        //titlesArray.add(Position);
    }

    //Concats of the details of the layer path
    public String setHomeTitleConcat(){   //The Home activitys details
        //TitleHomeConcat = ht+l+p;
        getHomeTitle();
        getHomeLayer();
        getHomePosition();
        TitleHomeConcat = HomeTitle+HomeLayer+HomePosition;
        return  TitleHomeConcat;
    }
    public String setSubTitleConcat(){    //Sublayer details concat
        getSubTitle();
        getLayer();
        getPosition();
        TitleSubLConcat = SubLTitle+Layer+Position;
        return TitleSubLConcat;
    }

    //Claring the Array...
    public void arrayClear(){
        titlesArray.clear();
    }
    //adding the Home details to the array
    public void addHomeToArray(){
        titlesArray.clear();
        this.setHomeTitleConcat();
        getHomeTitleConcat();
        titlesArray.add(TitleHomeConcat);

    }
    public void addSubToArray(){
        this.setSubTitleConcat();
        getSubTitleConcat();
        int i = titlesArray.size();
        String iValue = String.valueOf(i);
        Log.d("Array Size", iValue);
        Log.d("Layer ", getLayer());
        Log.d("Layer -1", String.valueOf((Integer.parseInt(getLayer()))-1));
        if (i == ((Integer.parseInt(getLayer()))-1)){
            titlesArray.add(TitleSubLConcat);
        }
        else
            titlesArray.set((Integer.parseInt(getLayer())-1), TitleSubLConcat);
            //titlesArray.remove((Integer.parseInt(getLayer()))-1);
    }

    public String arrayPull(){
        int size = titlesArray.size();
        for (int i = 0; i < size; i++) {
            String str = titlesArray.get(i);
            String FullConc = titleFullConcat;
            Log.d("Full Conc1", FullConc);
            titleFullConcat= titleFullConcat+str;
            String Fullconc2 = titleFullConcat;
            Log.d("FullConc2", Fullconc2);
        }
        return titleFullConcat;
    }

    // Home layer details...
    public static String getHomeTitle(){
        return HomeTitle;
    }
    public static String getHomeLayer() {
        return HomeLayer;
    }
    public static String getHomePosition(){
        return HomePosition;
    }

    //Subtitle layers details
    public static String getSubTitle(){
        return SubLTitle;
    }
    public static String getLayer(){
        return Layer;
    }
    public static String getPosition(){
        return Position;
    }

    //Concat details
    public static String getHomeTitleConcat(){
        return TitleHomeConcat;
    }
    public static String getSubTitleConcat(){
        return TitleSubLConcat;
    }
}

