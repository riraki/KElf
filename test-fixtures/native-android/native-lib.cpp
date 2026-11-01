#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_riraki_nativetest_MainActivity_00024Companion_md5(JNIEnv* env, jobject thiz, jstring data) {
    // TODO: implement md5()
    jclass jMessageDigest = env->FindClass("java/security/MessageDigest");

    jmethodID jgetInstance = env->GetStaticMethodID(jMessageDigest, "getInstance",
        "(Ljava/lang/String;)Ljava/security/MessageDigest;");

    jobject jmessageDigest = env->CallStaticObjectMethod(jMessageDigest, jgetInstance,
        env->NewStringUTF("md5"));


    jclass jString = env->FindClass("java/lang/String");
    jmethodID jgetBytes = env->GetMethodID(jString, "getBytes", "()[B");
    env->DeleteLocalRef(jString);

    jobject data_bytes = env->CallObjectMethod(data, jgetBytes);

    jmethodID jupdate = env->GetMethodID(jMessageDigest, "update", "([B)V");
    env->CallVoidMethod(jmessageDigest, jupdate, data_bytes);
    env->DeleteLocalRef(data_bytes);

    jmethodID jdigest = env->GetMethodID(jMessageDigest, "digest", "()[B");
    env->DeleteLocalRef(jMessageDigest);

    jobject result_bytes = env->CallObjectMethod(jmessageDigest, jdigest);
    env->DeleteLocalRef(jmessageDigest);

    jclass jCompanion = env->FindClass("com/guozilu/class04application/MainActivity$Companion");
    jmethodID jbyteToHex = env->GetMethodID(jCompanion, "byteToHex", "([B)Ljava/lang/String;");
    env->DeleteLocalRef(jCompanion);
    jclass jMainActivity = env->FindClass("com/guozilu/class04application/MainActivity");
    jfieldID jINSTANCE = env->GetStaticFieldID(jMainActivity, "Companion",
        "Lcom/guozilu/class04application/MainActivity$Companion;");
    jobject jInstance = env->GetStaticObjectField(jMainActivity, jINSTANCE);
    env->DeleteLocalRef(jMainActivity);
    auto result = reinterpret_cast<jstring>(env->CallObjectMethod(jInstance, jbyteToHex, result_bytes));
    env->DeleteLocalRef(jInstance);
    env->DeleteLocalRef(result_bytes);
    return result;
}


