import com.intellij.notification.NotificationGroupManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinLanguage

fun String.asKt() = "${this.capitalize()}.kt"

val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

// 获取通知组管理器
//private val manager = NotificationGroupManager.getInstance();

// 获取注册的通知组
//private val balloon = manager.getNotificationGroup("helloword.notification.balloon")


fun String.toSnakeCase() = camelRegex.replace(this) { "_${it.value}" }.toLowerCase()

fun String.save(srcDir: PsiDirectory, subDirPath: String, fileName: String) {
    try {

        val destDir = subDirPath.split(".").toDir(srcDir)
        println("-------------")
        println(destDir)
//    val balloonNotification =
//        balloon.createNotification(srcDir, NotificationType.INFORMATION)
        // 将通知放入通知总线
//    Notifications.Bus.notify(balloonNotification);
        val psiFile = PsiFileFactory.getInstance(srcDir.project)
            .createFileFromText(fileName, KotlinLanguage.INSTANCE, this)
        destDir.add(psiFile)
    } catch (exc: Exception) {
        println(exc)
        exc.printStackTrace()
    }
}

fun List<String>.toDir(srcDir: PsiDirectory): PsiDirectory {
    var result = srcDir
    forEach {
        result = result.findSubdirectory(it) ?: result.createSubdirectory(it)
    }
    return result
}