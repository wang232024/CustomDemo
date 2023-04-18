增加模块LibA后，需对应修改模块build.gradle
plugins {
    //    id 'com.android.application'
    id 'com.android.library'
}

android {
    defaultConfig {
        // applicationId "com.example.tablayoutactivity"
    }
}

1. 修改 plugins.id
2. 去除子模块applicationId
