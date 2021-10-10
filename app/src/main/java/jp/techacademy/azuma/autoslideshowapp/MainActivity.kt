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
            //getContentsInfo()
        }

        val resolver = contentResolver

        cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //データの種類
            null,
            null,
            null,
            null
        )

        susumu_button.setOnClickListener {
            //getNextInfo()
            // timer.text = String.format("%.1f", mTimerSec)
            //indexからIDを取得、IDから画像のURI取得
            cursor!!.moveToNext()
            //val nextCusor = cursor.moveToNext()
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            ImageView.setImageURI(imageUri)
            //var next = cursor.moveToNext()
            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            Log.d("ANDROID_TETE","URI:" + imageUri.toString())
        }





        //start_stop.setOnClickListener(this)
        modoru_button.setOnClickListener {
            //getContentsInfo()
            //getNextInfo()
            // timer.text = String.format("%.1f", mTimerSec)
            //indexからIDを取得、IDから画像のURI取得
            cursor!!.moveToPrevious()
            //val nextCusor = cursor.moveToNext()
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            ImageView.setImageURI(imageUri)
            //var next = cursor.moveToNext()
            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            Log.d("ANDROID_TETE","URI:" + imageUri.toString())
        }

    }

/*
    private fun getNextInfo(){ //進むボタン用

            // timer.text = String.format("%.1f", mTimerSec)
            //indexからIDを取得、IDから画像のURI取得
            cursor.moveToNext()
            //val nextCusor = cursor.moveToNext()
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            ImageView.setImageURI(imageUri)
            //var next = cursor.moveToNext()
            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
            Log.d("ANDROID_TETE","URI:" + imageUri.toString())

        }
        cursor.close()
        //mTimer?.cancel()
    }


    private fun getContentsInfo(){  //再生ボタン用


        if(cursor!!.moveToFirst()){

                            // timer.text = String.format("%.1f", mTimerSec)
                            //indexからIDを取得、IDから画像のURI取得
                            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

                            //Log.d("ANDROID_TEST","URI:" + imageUri.toString())
                            ImageView.setImageURI(imageUri)
                            Log.d("ANDROID_TEST","URI:" + imageUri.toString())

        }
        cursor.close()
        //mTimer?.cancel()
    }

 */





    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String> ,grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSIONS_REQUEST_CODE ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //getContentsInfo()
                }
        }
    }




}