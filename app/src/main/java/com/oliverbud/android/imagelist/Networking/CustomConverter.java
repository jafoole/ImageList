package com.oliverbud.android.imagelist.Networking;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by oliverbudiardjo on 8/3/15.
 */
public class CustomConverter implements Converter {

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        Log.d("ViceNewsConverter", "fromBody type: " + type.toString());
        NetworkResponseDataSub responseData = null;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String responseJson = fromStream(body.in());
            Object json = new JSONTokener(responseJson).nextValue();
            Log.d("ViceNewsConverter", "json class: " + json.getClass());
            if(json instanceof JSONObject){
                JSONObject jsonObject = new JSONObject(responseJson);
                Log.d("ViceNewsConverter", "jsonObject has responseData: " + jsonObject.has("responseData"));

            }else if (json instanceof JSONArray){
               Log.d("ViceNewsConverter", "isJSONArray");
            }
            Log.d("ViceNewsConverter", "responseJson: " + responseJson);


            responseData = gson.fromJson(responseJson, NetworkResponseDataSub.class);
        }
        catch (IOException e){
            Log.d("ViceNewsConverter", "IOException: " + e);

        }
        catch (JSONException j){
            Log.d("ViceNewsConverter", "JSONException: " + j);
        }
        return responseData;
    }

    public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }
}
