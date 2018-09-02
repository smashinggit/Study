package com.test.netdemo

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.test.netdemo.intercepter.LoggingInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    val NETWORK_EXECUTOR = Executors.newSingleThreadExecutor()
    val DECODE_EXECUTOR = Executors.newSingleThreadExecutor()
    val BACKGROUND_BASE_URL = "http://www.gravatar.com/avatar/4df6f4fe5976df17deeea19443d4429d?s="

    val mClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            asynchronousGet()    //get下载文件
        }

        btn2.setOnClickListener {
            postString()    //post上传字符串
        }

        btn3.setOnClickListener {
            postStream()    //post上传流
        }

        btn4.setOnClickListener {
            postFile()    //post上传文件
        }

        btn5.setOnClickListener {
            postFormParam()    //post提交表单数据
        }

        btn6.setOnClickListener {
            postMultipart()    //POST提交Multipart数据
        }
    }


    //POST提交Multipart数据
    private fun postMultipart() {
        val file = File(Environment.getExternalStorageDirectory().absolutePath, "test.txt")
        log("file路径 ${file.absolutePath}")
        try {
            if (!file.exists())
                file.createNewFile()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "test.txt",
                        RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
                .build()

        val request = Request.Builder()
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build()

        mClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        e?.printStackTrace()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        log("${response?.body()?.string()}")
                    }
                })

    }

    //post提交表单数据
    private fun postFormParam() {
        val formBody = FormBody.Builder()
                .add("search", "Jurassic Park")
                .build()

        val request = Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build()

        mClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        e?.printStackTrace()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        log("${response?.body()?.string()}")
                    }
                })
    }


    //post上传文件
    private fun postFile() {

        val file = File(Environment.getExternalStorageDirectory().absolutePath, "test.txt")
        log("file路径 ${file.absolutePath}")
        try {
            if (!file.exists())
                file.createNewFile()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val request = Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
                .build()

        mClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        e?.printStackTrace()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        log("${response?.body()?.string()}")
                    }
                })
    }

    //post上传流
    private fun postStream() {
        val requestBody = object : RequestBody() {
            override fun contentType(): MediaType? {
                return MediaType.parse("text/x-markdown; charset=utf-8")
            }

            override fun writeTo(sink: BufferedSink?) {
                sink?.writeUtf8("Numbers\n")
                sink?.writeUtf8("-------\n")
                for (i in 2..997) {
                    sink?.writeUtf8(String.format(" * %s = %s\n", i, factor(i)))
                }
            }

            private fun factor(n: Int): String {
                for (i in 2 until n) {
                    val x = n / i
                    if (x * i == n) return factor(x) + " × " + i
                }
                return Integer.toString(n)
            }
        }


        val request = Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build()

        mClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        e?.printStackTrace()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        log("${response?.body()?.string()}")
                    }
                })

    }

    //post上传字符串
    private fun postString() {
        val postBody = (""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n")

        val request = Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), postBody))
                .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response) {

                log("${response.body()?.string()}")
            }
        })


    }

    //异步下载文件
    private fun asynchronousGet() {

        val request = Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Headers.java")   //如果已经存在同名的header，此方法会替换掉
                .addHeader("Accept", "application/json; q=0.5") //如果已经存在同名的header，此方法会额外增加
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response) {

                val headers = response.headers()
                for (i in 0 until headers.size()) {
                    log("header：    ${headers.name(i)} ：${headers.get(headers.name(i))} ")
                }

                log(" ${response.body()?.string()}")
            }
        })
    }


    fun log(s: String) {
        Log.e("tag", s)
    }

}
