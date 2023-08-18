package com.example.subject6

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageFrame : ImageView = findViewById(R.id.cat)
        val postButton : Button = findViewById(R.id.post_btn)
        val resetButton : Button = findViewById(R.id.reset_btn)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // UI処理をするためにHandlerクラス生成
        val handler = Handler(Looper.getMainLooper())

        /* bitmap1は元画像 */
        /* cute_catはactivity_main.xmlのImageViewのsrcに書いてあるid */
        var bitmap1 : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat)

        /* bitmap2は表示中のBitmap */
        /* まずbitmapインスタンスを生成 */
        var bitmap2 : Bitmap = Bitmap.createBitmap(bitmap1)

        postButton.setOnClickListener {
            var bitmap3 : Bitmap = Bitmap.createBitmap(bitmap2.width, bitmap2.height, Bitmap.Config.ARGB_8888)
            monoBitmap(bitmap3, bitmap2)
            imageFrame.setImageBitmap(bitmap3)
            bitmap2 = bitmap3

            thread {
                var i = 0
                while (i < 100) {
                    i++
                    // 50ミリ秒間待機
                    // これを設置する理由は、ないと一瞬で終わっちゃうから...
                    SystemClock.sleep(50)
                    // メインスレッドに接続してからUI処理
                    handler.post {
                        // progressで現在の進捗率を更新
                        progressBar.progress = i
                    }
                }
                handler.post {
                }
            }
        }

        resetButton.setOnClickListener {
            imageFrame.setImageBitmap(bitmap1)
            bitmap2 = bitmap1
        }

        // ツールバーをアクションバーとしてセット
        val Toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(Toolbar)
        // タイトルを設定
        supportActionBar!!.setTitle("Exercise6_Thread")
    }
    private fun monoBitmap(outBitmap : Bitmap, inBitmap : Bitmap) {
        val width : Int = outBitmap.width
        val height : Int = outBitmap.height

        var color : Int
        var mono : Int

        for(j in 0..height - 1 step 1) {
            for (i in 0..width - 1 step 1) {
                color = inBitmap.getPixel(i, j)
                mono = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3
                outBitmap.setPixel(i, j, Color.rgb(mono, mono, mono))
            }
        }
    }
}