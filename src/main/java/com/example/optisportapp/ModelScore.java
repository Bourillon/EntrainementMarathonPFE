package com.example.optisportapp;

import android.content.Context;
import android.content.res.AssetManager;

import org.pmml4s.model.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ModelScore {

    Model model;

    public ModelScore(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream targetStream = assetManager.open("model_test.pmml");
        model = Model.fromInputStream(targetStream);
    }

    public Double getRegressionValue() {
        Map<String, Double> values = new HashMap<>();
        values.put("age", 0.0199132141783263);
        values.put("sex", 0.0506801187398187);
        values.put("bmi", 0.104808689473925);
        values.put("bp", 0.0700725447072635);
        values.put("s1", -0.0359677812752396);
        values.put("s2", -0.0266789028311707);
        values.put("s3", -0.0249926566315915);
        values.put("s4", -0.00259226199818282);
        values.put("s5", 0.00371173823343597);
        values.put("s6", 0.0403433716478807);
        /*
        values.put("NB_marathon", 1.1);
        values.put("Gender", 1.1);
        values.put("Taille", 170.0);
        values.put("Poids", 70.0);
        values.put("IMC", 24.0);
        values.put("Age", 20.0);
        values.put("NB_Training", 3.0);
        values.put("Fractionné", 1.0);
        values.put("SL", 1.0);
        values.put("Denivelé", 1.0);
        values.put("Duree_preparation", 1.0);
        values.put("RF", 1.0);
        values.put("Activities", 1.0);
        values.put("Experience", 1.0);
         */
        Object[] valuesMap = Arrays.stream(model.inputNames())
                .map(values::get)
                .toArray();
        Object[] result = model.predict(valuesMap);
        return (Double) result[0];
    }
}