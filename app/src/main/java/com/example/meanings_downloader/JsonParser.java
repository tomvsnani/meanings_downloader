package com.example.meanings_downloader;

import android.os.Build;


import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Iterator;


public class JsonParser {

    static String example_String;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String[] parser(String sc) throws JSONException {
        StringBuilder definition = new StringBuilder();
        JSONArray jsonArray;
        JSONObject jsonObject;
        String str ;


        jsonArray = new JSONArray(sc);
        jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject json_meaning = jsonObject.getJSONObject("meaning");
        String sound=jsonObject.getString("phonetic");
        Iterator<String> iterator = json_meaning.keys();


        while (iterator.hasNext()) {
            String jsonArray_String =  iterator.next();
            if (json_meaning.get(jsonArray_String) instanceof JSONArray) {
                JSONArray jsonArray1 = (JSONArray) json_meaning.get(jsonArray_String);
                JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                if (!jsonObject1.isNull("definition"))
                    str = jsonObject1.getString("definition");
                else str = null;
                if (!jsonObject1.isNull("example"))

                    example_String = jsonObject1.getString("example");
                else

                    example_String = "Could not find an example .";
                assert str != null;
                String[] string = str.split(";");
                int i = 0;
                while (i < string.length) {

                    definition = definition.append(i + 1).append(". ").append(string[i]).append("<br/>");
                    i++;
                }
                if (!definition.toString().equals(""))

                    return    new String[]{definition.toString(), example_String, jsonArray_String,sound};


            }


        }

        return null;
    }

}
