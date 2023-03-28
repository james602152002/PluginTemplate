package com.github.james602152002.plugintemplate.template


fun manifestTemplateXml(dir: String, path: String, activityClassName: String) = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools">
    <application>
           <activity
            android:name=".${dir}.${path}.${activityClassName}"
            android:launchMode="singleTask"
            android:theme="@style/CommonTheme" />
    </application>
</manifest>
"""