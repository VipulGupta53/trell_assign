package com.example.trell_assign


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var ffmpeg: FFmpeg? = null
var inputFileAbsolutePath = ""
    var outputFileAbsolutePath = ""

    var command = arrayOf(
        "-y",
        "-i",
        inputFileAbsolutePath,
        "-s",
        "160x120",
        "-r",
        "25",
        "-vcodec",
        "mpeg4",
        "-b:v",
        "150k",
        "-b:a",
        "48000",
        "-ac",
        "2",
        "-ar",
        "22050",
        outputFileAbsolutePath
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        select.setOnClickListener {
            chooseVideoFromGallary()
        }

        compress.setOnClickListener {
            loadFFMpegBinary()
            inputFileAbsolutePath   =  fv.display.toString()
            execCommand(command)
            cv.setVideoPath(outputFileAbsolutePath)
            cv.requestFocus()
            cv.start()

        }
    }

    fun chooseVideoFromGallary() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       // intent.putExtra("path"  , Intent.ACTION_GET_CONTENT)


        startActivityForResult(intent , 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }


            if (data != null) {
                var  contentURI: Uri? = data.data

                fv.setVideoURI(contentURI)
                fv.requestFocus()
                fv.start()

        }
    }
    private fun loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {

                ffmpeg = FFmpeg.getInstance(this)
            }
            ffmpeg!!.loadBinary(object : LoadBinaryResponseHandler() {
                override fun onFailure() {

                }

                override fun onSuccess() {

                }
            })
        } catch (e: FFmpegNotSupportedException) {

        } catch (e: Exception) {

        }
    }
    fun execCommand(cmds: Array<String>) {
        try {

            ffmpeg!!.execute(cmds, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}
                override fun onProgress(message: String) {
                }
                override fun onFailure(message: String) {

                }
                override fun onSuccess(message: String) {

                }

                override fun onFinish() {}
            })
        } catch (e: FFmpegCommandAlreadyRunningException) {
            e.printStackTrace()
        }
    }

}
