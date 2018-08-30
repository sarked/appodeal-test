package com.appodeal.test;

import android.os.AsyncTask;

class TimeTask extends AsyncTask<Void, Integer, String> {

    @Override
    protected String doInBackground(Void... voids) {
        int i=30;
        if (i>1){
            while(i>1){
                try{
                    Thread.sleep(1000);
                    i=i-1;
                    publishProgress(i);
                }catch (InterruptedException e){
                }
            }
        }
        return "end";
    }

    protected void onProgressUpdate(Integer... params){
        int val = params[0];
        MainActivity.self.btn.setText(val+"");
    }

    protected void onPostExecute(String res){
        MainActivity.self.onFinish();
    }
}