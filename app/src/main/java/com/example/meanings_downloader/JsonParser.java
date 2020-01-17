package com.example.meanings_downloader;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.StringTokenizer;

public class JsonParser {

    static String example_String;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String parser(String sc) throws JSONException {
        String definition ="";
        Log.d("exampleokay", "hello");
        JSONArray jsonArray;
        JSONObject jsonObject;
        String str = null;

        jsonArray = new JSONArray(sc);
        jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject json_meaning = jsonObject.getJSONObject("meaning");
        Iterator<String> iterator = json_meaning.keys();


        while (iterator.hasNext()) {
            String jsonArray_String = (String) iterator.next();
            if (json_meaning.get(jsonArray_String) instanceof JSONArray) {
                JSONArray jsonArray1 = (JSONArray) json_meaning.get(jsonArray_String);
                JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                Iterator<String> iterator1 = jsonObject1.keys();


                    if (!jsonObject1.isNull("definition"))
                        str = jsonObject1.getString("definition");
                    else str=null;
                    if (!jsonObject1.isNull("example"))

                        example_String = jsonObject1.getString("example");
                    else

                        example_String ="Could not find an example .";

Log.d("stringfound","hey");

                StringTokenizer stringTokenizer = new StringTokenizer(str, ";");



                while (stringTokenizer.hasMoreTokens()) {

                    definition = definition+stringTokenizer.nextToken()+" .\n";

                }
               // Log.d("stringfoundd","meaning"+json_meaning.getString("meaning"));
               // if(!json_meaning.getString("meaning").equals(""))
                if(!definition.equals(""))
                    return definition;

               // return null;
            }


        }

        return null;
    }

    public static String example() {

        return example_String;

    }
}
