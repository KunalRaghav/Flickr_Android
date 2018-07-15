package com.example.kunalraghav.flickr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

enum DownloadStatus{IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class RawJsonData extends AsyncTask<String,Void, String>{
    private static final String TAG = "RawJsonData";
    private final OnDownloadComplete Callback;
    private DownloadStatus downloadStatus;

    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public RawJsonData(OnDownloadComplete callback) {
        downloadStatus = DownloadStatus.IDLE;
        Callback=callback;
    }

    void runOnSameThread(String s){
        Log.d(TAG, "runOnSameThread: starts");
//        onPostExecute(doInBackground(s));
        if(Callback!=null){
            Callback.onDownloadComplete(doInBackground(s),downloadStatus);
        }
        Log.d(TAG, "runOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d(TAG, "onPostExecute: Parameter provided: "+s);
        super.onPostExecute(s);
        if(Callback!=null) {
            Callback.onDownloadComplete(s, downloadStatus);
        }
        Log.d(TAG, "onPostExecute: COMPLETE");
    }





    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection feedloader= null;
        BufferedReader bufferedReader =null;
        if(strings==null){
            downloadStatus=DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try{
            downloadStatus=DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            feedloader=(HttpURLConnection)url.openConnection();
            feedloader.setRequestMethod("GET");
            feedloader.connect();
            int response = feedloader.getResponseCode();
            Log.d(TAG, "doInBackground: The response was: "+response);
            StringBuilder builder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(feedloader.getInputStream()));
            String line;
            while (null!=(line=bufferedReader.readLine())){
                builder.append(line).append("\n");
            }
            downloadStatus=DownloadStatus.OK;
            return builder.toString();
        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+ e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: I/O error "+ e.getMessage() );
        }catch (Exception e){
            Log.e(TAG,"doInBackground: Unknown error "+e.getMessage());
        }finally {
            if(feedloader!=null){
                feedloader.disconnect();
            }if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground:  error in closing stream "+e.getMessage() );
                }
            }
        }
        downloadStatus=DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

}
