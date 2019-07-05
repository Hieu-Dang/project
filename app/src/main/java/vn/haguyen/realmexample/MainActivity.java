package vn.haguyen.realmexample;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
    RecyclerView recyclerView;
    Button btnShowList;
    Realm realm;
    Button btnHideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        btnShowList = (Button) findViewById(R.id.btnShowList);
        btnHideList = (Button) findViewById(R.id.btnHideList);


        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        //Khởi tạo OkHttpClient dể lấy dữ liệu
        OkHttpClient client = new OkHttpClient();
        //tạo request trên sever
        Request request = new Request.Builder().url("https://api.github.com/users").build();

        //Thực thi request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Network Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Lấy thông tin JSON trả về
                final String json = response.body().string();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String login = jsonObject.getString("login");
                                String url = jsonObject.getString("avatar_url");
                                String type = jsonObject.getString("type");
                                savedata(login, url, type);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        btnShowList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setupdata();
                            }
                        });

                        btnHideList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                recyclerView.setAdapter(null);

                            }
                        });
                    }
                });
            }

        });
    }

    private void savedata(final String login, final String url, final String type) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class);
                user.setAvatar_url(url);
                user.setLogin(login);
                user.setType(type);

            }
        });

    }

    private void setupdata() {
        RealmResults<User> data = realm.where(User.class).findAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter userAdapter = new UserAdapter(data, MainActivity.this);
        recyclerView.setAdapter(userAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
//                UserAdapter userAdapter = (UserAdapter) recyclerView.getAdapter();
                //    userAdapter.removeItem(position);
                DeleteData(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    public void DeleteData(final int position){
    final RealmResults<User> uses = realm.where(User.class).findAll();
    realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            int x = uses.size();
            Log.e("TAG","trong data con so phan tu la: " + x);
            User userde = uses.get(position);
            userde.deleteFromRealm();

        }
    });

}
public void Refesh(){
        
}
          /* protected void onDestroy() {
        super.onDestroy();
        realm.close();
        recyclerView.setAdapter(null);
    }*/

}
