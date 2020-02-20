package com.example.unlockme;

import java.util.ArrayList;

public interface FetchDataCallbackInterface {
    // method called when server's data get fetched
    public void fetchDataCallback(ArrayList<String> result);
}
