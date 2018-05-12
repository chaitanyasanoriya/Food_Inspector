package com.example.admin.androidcameraapitutorial;

import java.util.Random;

/**
 * Created by Wild Fire on 26-10-2017.
 */

public class RandomStringGenerator
{
    protected String getSaltString()
    {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18)
        { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
