package com.oliverbud.android.imagelist.Networking;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
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
        NetworkResponseData responseData = null;
        Gson gson = new Gson();
        try {
            responseData = gson.fromJson(new BufferedReader(new InputStreamReader(body.in())), NetworkResponseData.class);
        }
        catch (IOException e){

        }
        return responseData;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }
}
