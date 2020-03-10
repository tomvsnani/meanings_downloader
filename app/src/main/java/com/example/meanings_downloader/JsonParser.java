package com.example.meanings_downloader;

import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Iterator;


public class JsonParser {




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String[] parser(String sc) throws JSONException {
        StringBuilder definition = new StringBuilder();
       String example_String="";
        JSONArray jsonArray;
        JSONObject jsonObject;
        String str ;
        String jsonArray_String ="";


        jsonArray = new JSONArray(sc);
        jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject json_meaning = jsonObject.getJSONObject("meaning");
        String sound=jsonObject.getString("phonetic");
        Iterator<String> iterator = json_meaning.keys();


        while (iterator.hasNext() && !jsonObject.isNull("meaning")) {
            jsonArray_String =  iterator.next();
            if (json_meaning.get(jsonArray_String) instanceof JSONArray) {
                JSONArray jsonArray1 = (JSONArray) json_meaning.get(jsonArray_String);
                JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                if (!jsonObject1.isNull("definition"))
                    str = jsonObject1.getString("definition");
                else str = "";
                if (!jsonObject1.isNull("example") && !jsonObject1.getString("example").equals("") )

                    example_String = jsonObject1.getString("example");
                else

                    example_String = "Could not find an example .";

                String[] string = str.split(";");
                int i = 0;
              if(string.length>1){


                      definition = definition.append(string[0]);


              }
              else
              {
                 for(int j=0;j<string.length;j++){
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                         definition = definition.append(j + 1).append(". ").append(String.join("\n",string[i]));
                     }
                 }
              }



                   Log.d("definition",definition.toString());


            }


        }


        return    new String[]{definition.toString(), example_String, jsonArray_String,sound};

    }

}
