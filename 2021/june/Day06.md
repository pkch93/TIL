# 2021.06.06 TIL - React Native App Icon 반영

## Android

안드로이드는 `android/app/src/main/res`에서 `mipmap-*` 폴더에 있는 앱 아이콘을 변경하면 된다. 이름을 바꾸고 싶다면 res 폴더 내의 `AndroidMenifest.xml`에서 수정이 필요하다.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.dessertsclient">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
      android:name=".MainApplication"
      android:label="@string/app_name"
      android:icon="@mipmap/ic_launcher"
      android:roundIcon="@mipmap/ic_launcher_round"
      android:allowBackup="false"
      android:theme="@style/AppTheme">
      <activity
        android:name=".MainActivity"
        android:label="@string/app_name"
        android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
        android:launchMode="singleTask"
        android:windowSoftInputMode="adjustResize">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
      </activity>
      <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />
    </application>

</manifest>
```

위에서 application의 `android:icon`과 `android:roundIcon`을 수정하면 된다.

## iOS

iOS는 XCode 실행이 필요하다.

XCode에서 프로젝트를 불러온 뒤 `images.xcassets`를 열면 각 사이즈에 맞게 앱 아이콘을 설정할 수 있는 뷰를 볼 수 있다.

![](https://user-images.githubusercontent.com/30178507/121031028-f6adfa80-c7e4-11eb-89aa-374c4b8adb61.png)

여기서 사이즈에 맞는 이미지를 각각 넣어주면 된다.

### 참고

app icons generator: [https://appicon.co/](https://appicon.co/)
