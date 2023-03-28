package com.github.james602152002.plugintemplate.template


fun manifestTemplateXml(path: String, activityClassName: String) = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools">
    <application>
           <activity
            android:name=".view.ui.${path}.${activityClassName}"
            android:theme="@style/CommonTheme" />
    </application>
</manifest>
"""