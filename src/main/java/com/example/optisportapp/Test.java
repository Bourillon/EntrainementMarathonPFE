package com.example.optisportapp;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Test
{
    public static void main(String[] args) throws IOException {
        ClassLoader loader = Test.class.getClassLoader();
        System.out.print("\n");
        System.out.print(loader.getResource("model.pmml"));

        System.out.print("\n");
    }
}