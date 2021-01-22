#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_weatherforecast_repository_ForecastRepository_getAppID(
        JNIEnv* env,
        jobject /* this */) {
    std::string app_id = "60c6fbeb4b93ac653c492ba806fc346d";
    return env->NewStringUTF(app_id.c_str());
}