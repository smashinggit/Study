//
// Created by Lenovo on 2018/10/30.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_cmos_ndkdemo_JniTools_getStringFromNDK(
        JNIEnv *env, jobject /* this */) {
    std::string hello = "从C代码中读取的内容";
    return env->NewStringUTF(hello.c_str());
}