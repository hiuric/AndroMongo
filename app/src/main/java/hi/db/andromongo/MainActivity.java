package hi.db.andromongo;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import hi.db.hiMonWorkerSample;
import hi.db.hiMongo;
class MongoCaller extends AsyncTask<String, Void, String>{
   private MainActivity mActivity;
   public MongoCaller(Activity activity) {mActivity =(MainActivity) activity; }
   @Override
   protected String doInBackground(String... prm_) {
      hiMonWorkerSample.COM _com=new hiMonWorkerSample.COM(prm_[0],8010,15);
      hiMongo.Collection coll = hiMongo.use("db01",_com).in("coll_01");
      if( prm_[1].equals("last_records") ){
         coll.find().sort("{_id:-1}").forOne(Fi->{
            Fi.deleteMany("{date:{$lt:{$calc:'#CUR.date-5000'}}}");
            });
         }
      else if( prm_[1].equals("drop") )  coll.drop();
      else if( prm_[1].equals("insert") )coll.insertOne(prm_[2]);
      StringBuilder _sb =new StringBuilder();
      coll.find("{}","{_id:0}").forEachJson(Rj->_sb.append(Rj+"\n"));
      _com.close();
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
      findViewById(R.id.insert).setOnClickListener((_View_) ->{
         MongoCaller _mongoCaller = new MongoCaller(this);
         String _host=((EditText)findViewById(R.id.host)).getText().toString();
         _mongoCaller.execute(_host,"insert"
                             ,"{type:'X',value:"+(++count)+",date:"+hiMongo.date()+"}");
         });
      findViewById(R.id.last_records).setOnClickListener((_View_) ->{
         MongoCaller _mongoCaller = new MongoCaller(this);
         String _host=((EditText)findViewById(R.id.host)).getText().toString();
         _mongoCaller.execute(_host,"last_records");
         });
      findViewById(R.id.drop).setOnClickListener((_View_) ->{
         MongoCaller _mongoCaller = new MongoCaller(this);
         String _host=((EditText)findViewById(R.id.host)).getText().toString();
         _mongoCaller.execute(_host,"drop");
         });
      }
   void dispResult(String result_){
      ((TextView)findViewById(R.id.textView_1)).setText(result_);
      }
   }