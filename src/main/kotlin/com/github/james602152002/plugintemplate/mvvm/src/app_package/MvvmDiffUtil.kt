package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createDiffUtil(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val diffUtilClassName = "Diff${className}CBU"
    createDiffUtilStr(
        packageName, diffUtilClassName, className, path
    ).save(
        directorySrc, "util.diffutil.${path}", diffUtilClassName.asKt()
    )
}

private fun createDiffUtilStr(
    applicationPackageName: String, repoClassName: String, className: String, path: String
) = """
    package $applicationPackageName.util.diffutil.${path}

    import com.bitzsoft.base.util.diff_util.BaseDiffUtil
    import com.bitzsoft.model.response.${path}.Response${className}s

    class $repoClassName(
        oldData: MutableList<Response${className}s>,
        newData: MutableList<Response${className}s>
    ) : BaseDiffUtil<Response${className}s>(oldData, newData) {

    override var implItemSame: (Response${className}s.(newItem: Response${className}s) -> Boolean)? =
        { newItem ->
           id == newItem.id
        }

    override var implContentSame: (Response${className}s.(newItem: Response${className}s) -> Boolean)? =
        { newItem ->
             chargeAmount == newItem.chargeAmount &&
                            currencyText == newItem.currencyText &&
                            currency == newItem.currency &&
                            caseName == newItem.caseName &&
                            caseSerialId == newItem.caseSerialId &&
                            creatorUserName == newItem.creatorUserName &&
                            chargeDate == newItem.chargeDate &&
                            operationTime == newItem.operationTime &&
                            statusName == newItem.statusName &&
                            status == newItem.status
        }
}
""".trimIndent()