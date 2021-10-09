package jp.techacademy.azuma.autoslideshowapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Handler

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100 //※質問 紫色になる意味

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //6.0以降の場合
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //許可されている
                getContentsInfo()
            } else {
                //許可されてないのでダイアログ表示
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSIONS_REQUEST_CODE) //ここを質問、何をしているのか　配列？
            }
        } else {
            getContentsInfo()
        }

        start_button.setOnClickListener {
            getContentsInfo()
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String> ,grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSIONS_REQUEST_CODE ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getContentsInfo()
                }
        }
    }


    private fun getContentsInfo(){
        var mTimer: Timer? = null //※質問　コロンの意味
        var mTimerSec = 0.0
        var mHandler = Handler() //※質問　ハンドラーの上に線が引かれる
        //画像の情報を取得
        val resolver = contentResolver //色が紫になるのはどういう意味なのか？
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //データの種類
            null,
            null,
            null,
            null
        )


        if(cursor!!.moveToFirst()){

            if (mTimer == null) {
                //タイマーの作成
                mTimer = Timer()
                //タイマーの始動
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mTimerSec += 0.1
                        mHandler.post {
                            // timer.text = String.format("%.1f", mTimerSec)
                            //indexからIDを取得、IDから画像のURI取得
                            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

                            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
                            ImageView.setImageURI(imageUri)
                            Log.d("ANDROID_TEST","URI:" + imageUri.toString())
                        } //どこでループさせているのかを質問する
                    }
                }, 100, 100) //最初に始動させるまで100ミリ秒、ループの間隔を100ミリ秒に設定
            }
        }
        cursor.close()
        mTimer?.cancel()
    }




}