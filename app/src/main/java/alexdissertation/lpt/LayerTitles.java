package alexdissertation.lpt;

import android.os.Bundle;
import android.util.Log;

import java.lang.annotation.Target;
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
    private static String finalFileConcat;


    // could group these together to concat...then add to arraylist... one method for home activ.. another for subt run them
    //seperately to allow fo it to work more efficiently


       //Setters to set all of the values...
    public static void setHomeTitle(String HomeTitle){
        LayerTitles.HomeTitle = HomeTitle;
    }
    public static void setHomeLayer() {
        LayerTitles.HomeLayer = String.valueOf(1);
    }
    public static void setHomePosition(String Position){
        LayerTitles.HomePosition = Position;
    }

    public static void setSubTitle(String subLTitle){
        LayerTitles.SubLTitle=subLTitle;
    }
    public static void setLayer(String Layer){
        LayerTitles.Layer=Layer;
    }
    public static void setPosition(String Position){
        LayerTitles.Position = Position;
        //titlesArray.add(Position);
    }

    //Concats of the details of the layer path
    public static String setHomeTitleConcat(){//The Home activitys details
        getHomeTitle();
        getHomeLayer();
        getHomePosition();
        TitleHomeConcat = HomeTitle+HomeLayer+HomePosition;
        return  TitleHomeConcat;
    }
    public static String setSubTitleConcat(){    //Sublayer details concat
        getSubTitle();
        getLayer();
        getPosition();
        TitleSubLConcat = SubLTitle+Layer+Position;
        return TitleSubLConcat;
    }
    public void setTitleFullConcat(){
        arrayPull();
        finalFileConcat= titleFullConcat;
    }

    public void addHomeToArray(){
        titlesArray.clear();
        setHomeTitleConcat();
        getHomeTitleConcat();
        titlesArray.add(TitleHomeConcat);
    }
    public void addSubToArray(){
        setSubTitleConcat();
        getSubTitleConcat();
        titlesArray.add(TitleSubLConcat);
        int i = titlesArray.size();
        String iValue = String.valueOf(i);
        if (i == ((Integer.parseInt(getLayer())))){
            titlesArray.set(((Integer.parseInt(getLayer()))-1), TitleSubLConcat);
            String s= String.valueOf(Integer.parseInt(getLayer())-1);
            Log.d("LT-getL -1",s);
        }
        else if (i >((Integer.parseInt(getLayer())))) {
            titlesArray.subList(Integer.parseInt(getLayer()),i).clear();
            titlesArray.set(((Integer.parseInt(getLayer()))-1),TitleSubLConcat);
        }
        else titlesArray.add(TitleSubLConcat);

    }
    // for testing
    private static int arraySize;
    public static int getArraySize(){
        LayerTitles.arraySize = LayerTitles.titlesArray.size();
        return arraySize;
    }
    public void arrayDeleteLast(){
        titlesArray.remove(titlesArray.size()-1);
    }

    public String arrayPull(){
        int size = titlesArray.size();
        for (int i = 0; i < size; i++) {
            String str = titlesArray.get(i);
            titleFullConcat = titleFullConcat+str;
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

    //full concat
    public static String getFinalFileConcat(){
        return finalFileConcat;
    }

}

