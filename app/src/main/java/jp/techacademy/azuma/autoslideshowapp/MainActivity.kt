package jp.techacademy.azuma.autoslideshowapp

import android.Manifest
import android.content.ContentResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Handler
import android.view.View

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100 //

    //画像の情報を取得
    var cursor: Cursor? = null
    //再生・停止ボタン用
    var button_flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mTimer: Timer? = null //※質問　コロンの意味
        var mTimerSec = 0.0
        var mHandler = Handler() //

        //6.0以降の場合
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //許可されている
                //getContentsInfo()
            } else {
                //許可されてないのでダイアログ表示
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSIONS_REQUEST_CODE) //ここを質問、何をしているのか　配列？
            }
        } else {
            //getContentsInfo()　ここは質問、アンドロイドのバージョンが6未満だった場合は何をする？
        }

        /*ここから進むボタン、戻るボタン関連スタート*/
        val resolver = contentResolver
        cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //データの種類
            null,
            null,
            null,
            null
        )

        start_stop.setOnClickListener {
            //if(button_flag == 0) {
                if (mTimer == null) {
                    button_flag = 1
                    start_stop.text = "停止"
                    //タイマーの作成
                    mTimer = Timer()
                    //タイマーの始動
                    mTimer!!.schedule(object : TimerTask() {
                        override fun run() {
                            mTimerSec += 1
                            mHandler.post {
                                //timer.text = String.format("%.1f", mTimerSec)
                                if (cursor!!.moveToNext()) {
                                    //do{
                                    //indexからIDを取り出し、そのIDから画像のURIを取得する
                                    val fieldIndex =
                                        cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                    val id = cursor!!.getLong(fieldIndex)
                                    val imageUri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )

                                    Log.d("ANDROID", "URI:" + imageUri.toString())
                                    ImageView.setImageURI(imageUri)
                                    //} while (cursor!!.moveToNext())
                                } else {
                                    cursor!!.moveToPrevious()
                                }
                                //cursor!!.close()
                            }
                        }
                    }, 2000, 2000) //最初に始動させるまで100ミリ秒、ループの間隔を100ミリ秒に設定
                } else {
                        mTimer!!.cancel()
                        button_flag = 0
                        start_stop.text = "再生"
                        mTimer = null
                }
                Log.d("ANDROID", "URI:" + button_flag.toString())
        }

        susumu_button.setOnClickListener {
            //if(cursor!!.isLast()==false) {
            if(mTimer==null) {
                if (cursor!!.moveToNext()) {
                    //indexからIDを取得、IDから画像のURI取得
                    //cursor!!.moveToNext()
                    //val nextCusor = cursor.moveToNext()
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
                    ImageView.setImageURI(imageUri)
                    //var next = cursor.moveToNext()
                    Log.d("ANDROID_TETE1", "URI:" + imageUri.toString())
                } else {
                    //次がなかったら最初に戻る
                    cursor!!.moveToFirst()
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    ImageView.setImageURI(imageUri)
                    Log.d("ANDROID_TETE2", "URI:" + imageUri.toString())
                }
            }
        }

        modoru_button.setOnClickListener {
            if(mTimer==null) {
                if (cursor!!.moveToPrevious()) {
                    //indexからIDを取得、IDから画像のURI取得
                    //cursor!!.moveToPrevious()
                    //val nextCusor = cursor.moveToNext()
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
                    ImageView.setImageURI(imageUri)
                    //var next = cursor.moveToNext()
                    Log.d("ANDROID_TETE1", "URI:" + imageUri.toString())
                } else {
                    //前がなかったら最後に戻る
                    cursor!!.moveToLast()
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    ImageView.setImageURI(imageUri)
                    Log.d("ANDROID_TETE2", "URI:" + imageUri.toString())
                }
            }
        }

    }

    /*ここまで進むボタン、戻るボタン関連エンド*/




    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String> ,grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSIONS_REQUEST_CODE ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //getContentsInfo()
                } else {
                    Log.d("ANDROID","許可されなかった")
                }
        }
    }




}