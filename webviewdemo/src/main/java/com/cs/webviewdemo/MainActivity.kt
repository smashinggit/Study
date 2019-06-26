package com.cs.webviewdemo

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.webkit.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //是否可以后退
        webView.canGoBack()
        //后退网页
        webView.goBack()
        //是否可以前进
        webView.canGoForward()
        //前进网页
        webView.goForward()

        //以当前的index为起始点前进或者后退到历史记录中指定的steps
        //如果steps为负数则为后退，正数则为前进
        //   webView.goBackOrForward(intsteps)


        /**
         * 清除缓存数据
         */
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        webView.clearCache(true)

        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory()

        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData()


        /**
         * WebSettings类
         * 作用：对WebView进行配置和管理
         */

        val webSettings = webView.settings

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.javaScriptEnabled = true

        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true       //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true  // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式


        /**
         * WebViewClient
         * 作用：处理各种通知 & 请求事件
         */
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                webView.loadUrl(url)

                // 步骤2：根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                val uri = Uri.parse(url)
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.scheme == "js") {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.authority == "webview") {

                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法")
                        // 可以在协议上带有参数并传递到Android上
                        val params = HashMap<String, String>()
                        val collection = uri.queryParameterNames;
                    }
                }
                return true
            }

            //作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            //作用：在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }


            //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
            //步骤2：将该html文件放置到代码根目录的assets文件夹下
            //步骤3：复写WebViewClient的onRecievedError方法
            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                super.onReceivedError(view, request, error)
            }

            //webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()    //表示等待证书响应
                // handler.cancel()      //表示挂起连接，为默认方式
                // handler.handleMessage(null)    //可做其他处理
            }
        }

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        /**
         * WebChromeClient
         * 作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
         */

        webView.webChromeClient =
                object : WebChromeClient() {

                    //得网页的加载进度并显示
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                    }

                    // 作用：支持javascript的警告框
                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult): Boolean {
                        AlertDialog.Builder(this@MainActivity)
                                .setTitle("JsAlert")
                                .setMessage(message)
                                .setPositiveButton("Ok") { _, _ ->
                                    result.confirm()
                                }
                                .setCancelable(false)
                                .show()
                        return true
                    }

                    //作用：支持javascript的确认框
                    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult): Boolean {
                        AlertDialog.Builder(this@MainActivity)
                                .setTitle("JsConfirm")
                                .setMessage(message)
                                .setPositiveButton("Ok") { _, _ ->
                                    result.confirm()
                                }
                                .setNegativeButton("cancle") { _, _ ->
                                    result.cancel()
                                }
                                .setCancelable(false)
                                .show()
                        // 返回布尔值：判断点击时确认还是取消
                        // true表示点击了确认；false表示点击了取消；
                        return true
                    }

                    override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                        return super.onJsPrompt(view, url, message, defaultValue, result)
                    }
                }


        /**
         * Android WebView与 JS 的交互方式
         */


        /**
         * 注意事项：如何避免WebView内存泄露？
         */

        // 1. 不在xml中定义 Webview ，而是在需要的时候在Activity中创建，并且Context使用 getApplicationgContext()
        // 2.  Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空


        /**
         *
         * 对于Android调用JS代码的方法有2种：
         *   通过WebView的loadUrl（）
         *   通过WebView的evaluateJavascript（）
         *
         *   对于JS调用Android代码的方法有3种：
         *
         *  通过WebView的addJavascriptInterface（）进行对象映射
         *  通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url
         *  通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
         */

        webView.loadUrl("file:///android_asset/javascript.html")
        // 注意调用的JS方法名要对应上

        /**
         * Android调用JS
         */
        // 1. 调用javascript的callJS()方法
//        btnInvok.setOnClickListener
//        {
//            webView.loadUrl("javascript:callJS()")  //JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
//        }
//        //2. 只需要将第一种方法的loadUrl()换成下面该方法即可
//        btnInvok.setOnClickListener
//        {
//            webView.evaluateJavascript("javascript:callJS()") { value ->
//                //此处为 js 返回的结果
//            }
//        }

        /**
         * JS通过WebView调用 Android 代码
         */

        //第一种(存在漏洞)
        webView.addJavascriptInterface(this, "test")

        //第二种
//       1. Android通过 WebViewClient 的回调方法shouldOverrideUrlLoading ()拦截 url
//       2.解析该 url 的协议
//       3.如果检测到是预先约定好的协议，就调用相应方法


        //第三种
       // 通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、
        // confirm()、prompt（） 消息


    }

    @JavascriptInterface
    fun hello(msg: String) {
        Toast.makeText(this, "JS调用了Android的hello方法", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory()

            rootLayout.removeView(webView)
            webView.destroy()
        }

        super.onDestroy()
    }

}
