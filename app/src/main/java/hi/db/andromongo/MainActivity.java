package hi.db.andromongo;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hi.db.hiMonWorkerSample;
import hi.db.hiMongo;
import otsu.hiNote.*;

import static hi.db.hiMonWorkerSample.*;
class MongoCaller extends AsyncTask<String, Void, String>{
   private MainActivity mActivity;
   public MongoCaller(Activity activity) {mActivity =(MainActivity) activity; }
   @Override
   protected String doInBackground(String... params_) {
      StringBuilder         _sb =new StringBuilder();
      hiMonWorkerSample.COM _com=new hiMonWorkerSample.COM(params_[0],8010,15);
      hiMongo.DB db = hiMongo.use("db01",_com);
      if( params_[1].equals("drop") ){
         db.in("coll_01").drop();
         }
      else if( params_[1].equals("insert") ){
         db.in("coll_01")
           .insertOne(params_[2])
           .find("{}","{_id:0}")
           .sort("{_id:-1}")
           .limit(4)
           .getJsonList(hiU.REVERSE)
           .forEach(Rj->_sb.append(Rj+"\n"))
           ;
         }
      hiU.close(_com);
      return _sb.toString();
      }
   @Override
   protected void onPostExecute(String result_) {
      mActivity.dispResult(result_);
      }
   }
public class MainActivity extends AppCompatActivity{
   int count;
   @Override
   protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      findViewById(R.id.drop).setOnClickListener((_View_) ->{
         MongoCaller _mongoCaller = new MongoCaller(this);
         String _host=((EditText)findViewById(R.id.host)).getText().toString();
         _mongoCaller.execute(_host,"drop");
         });
      findViewById(R.id.insert).setOnClickListener((_View_) ->{
         MongoCaller _mongoCaller = new MongoCaller(this);
         String _host=((EditText)findViewById(R.id.host)).getText().toString();
         _mongoCaller.execute(_host,"insert"
                             ,"{type:'X',value:"+(++count)+",date:"+hiMongo.date()+"}");
         });
      }
   void dispResult(String result_){
      ((TextView)findViewById(R.id.textView_1)).setText(result_);
      }
   }