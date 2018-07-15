package com.example.kunalraghav.flickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class GetFlickrJsonData extends AsyncTask<String, Void, ArrayList<PhotoData>> implements RawJsonData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";
    private ArrayList<PhotoData> photoDataArrayList;
    private static final String baseURL="https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
    private String lang="en-us";
    private boolean MatchAll=true;
    private final OnDataAvailable Callback;
    private boolean runningOnSameThread = false;

    public GetFlickrJsonData( String lang, Boolean matchAll, OnDataAvailable callback) {
        Log.d(TAG, "GetFlickrJsonData: called");
        this.lang = lang;
        MatchAll = matchAll;
        Callback = callback;
    }

    interface OnDataAvailable{
        void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status);
    }

    @Override
    protected void onPostExecute(ArrayList<PhotoData> photoData) {
        Log.d(TAG, "onPostExecute: starts");
        if(Callback!=null){
            Callback.onDataAvailable(photoData,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }



    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria,MatchAll,lang);
        RawJsonData rawJsonData = new RawJsonData(this);
        rawJsonData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected ArrayList<PhotoData> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri =createUri(params[0],MatchAll,lang);
        RawJsonData jsonData = new RawJsonData(this);
        jsonData.runOnSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return photoDataArrayList;
    }

    private String createUri(String searchCriteria, Boolean matchAll, String lang){
        Log.d(TAG, "createUri: starts");
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL":"ANY")
                .appendQueryParameter("lang",lang).build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        if(status==DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete: starts status:" + status);
            photoDataArrayList = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsData = jsonObject.getJSONArray("items");
                for(int i=0; i< itemsData.length();i++){
                    JSONObject item = itemsData.getJSONObject(i);
                    String title = item.getString("title");
                    String author = item.getString("author");
                    String authorID = item.getString("author_id");
                    String published= item.getString("published");
                    String tags=item.getString("tags");
                    JSONObject media = item.getJSONObject("media");
                    String image = media.getString("m");
                    String dateTaken = item.getString("date_taken");
                    String link = item.getString("link");
                    String big_image_link =image.replaceFirst("_m","_b");
                PhotoData photo= new PhotoData(author,authorID,title,dateTaken,published,tags,big_image_link,image);
                photoDataArrayList.add(photo);
//                Log.d(TAG, "onDownloadComplete: \n"+ photo.toString());

                }
            }catch (JSONException e){
                Log.e(TAG, "onDownloadComplete: unable yo parse json: "+ e.getMessage());
            }

        }if (runningOnSameThread && Callback!=null){
            //error
            Log.d(TAG, "onDownloadComplete: running on same thread: "+ runningOnSameThread);
            Callback.onDataAvailable(photoDataArrayList,status);

        }
        else {
            //Download Failed
            Log.e(TAG, "onDownloadComplete: Download Failed with status: "+status);
        }
    }
}
