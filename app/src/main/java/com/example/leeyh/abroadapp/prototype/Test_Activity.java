//package com.example.leeyh.abroadapp.prototype;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.example.leeyh.abroadapp.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.net.URISyntaxException;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//
//import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_URL;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.NICKNAME;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.PASSWORD;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.PROFILE;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;
//
//public class Test_Activity extends AppCompatActivity {
//
//    ImageView test1;
//    ImageView test2;
//    ImageView test3;
//    ImageView test4;
//    ImageView test5;
//    Socket mSocket;
//    byte[] byteArray = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_);
//
////        final ProtoChatApplication application = (ProtoChatApplication) getApplication();
//
//        try {
//            IO.Options options = new IO.Options();
//            options.reconnection = true;
//            options.forceNew = true;
//            mSocket = IO.socket(REQUEST_URL, options);
//            mSocket.io().reconnection(true);
//            mSocket.connect();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//        test1 = findViewById(R.id.test_image_view);
//        test2 = findViewById(R.id.test_image_view2);
//        test3 = findViewById(R.id.test_image_view3);
//        test4 = findViewById(R.id.test_image_view4);
//        test5 = findViewById(R.id.test_image_view5);
//
//        Drawable test1Drawable = getResources().getDrawable(R.drawable.test2);
//        Bitmap test1Bitmap = ((BitmapDrawable) test1Drawable).getBitmap();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        test1Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        byteArray = outputStream.toByteArray();
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        test1.setImageBitmap(bitmap);
//
//        request();
//
//        mSocket.on("ttest", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run()         {
//                        Log.d("여기이이잉", "run: " + args);
//                        byte[] bytes = (byte[]) args[0];
//                        Log.d("여기이잉", "run: " + bytes);
//                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                        application.addBitmapToMemoryCache("test", bitmap1);
////                        test2.setImageBitmap(application.getBitmapFromMemoryCache("test"));
//                    }
//                });
//                                         }
//        });
//    }
//
//    public void request() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(USER_ID, "11");
//            jsonObject.put(NICKNAME, "11");
//            jsonObject.put(PASSWORD, "11");
//            jsonObject.put(PROFILE, byteArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mSocket.emit(SIGN_UP, jsonObject);
//    }
//}
