package com.github.james602152002.plugintemplate.mvvm.src.app_package

import Type
import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createViewModel(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val viewModelClassName = "${className}sViewModel"
    createViewModelStr(
        packageName, viewModelClassName, className, path
    ).save(
        directorySrc, "view_model.${path}", viewModelClassName.asKt()
    )
}

private fun createViewModelStr(
    applicationPackageName: String,
    viewModelClassName: String,
    className: String,
    path: String,
) = """
package $applicationPackageName.view_model.${path}

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
import $applicationPackageName.remote.$path.Repo${className}
import $applicationPackageName.template.initAuditType
import $applicationPackageName.template.jumpToDetailPage
import $applicationPackageName.template.setAuditType
import $applicationPackageName.util.Utils
import $applicationPackageName.view.fragment.bottom_sheet.common.BottomSheetCommonList
import $applicationPackageName.view.ui.base.MainBaseActivity
import $applicationPackageName.view.ui.business_management.cases.ActivityCaseDetail
import $applicationPackageName.view.ui.${path}.Activity${Type.AUDIT.key}${className}s
import $applicationPackageName.view.ui.${path}.ActivityDetail${className}
import $applicationPackageName.view.ui.${path}.Activity${Type.MANAGE.key}${className}s
import $applicationPackageName.view.ui.${path}.Activity${Type.USER.key}${className}s
import com.bitzsoft.base.util.Constants
import com.bitzsoft.lifecycle.BaseLifeData
import com.bitzsoft.model.request.common.RequestCommonID
import com.bitzsoft.model.response.${path}.Response${className}s
import com.bitzsoft.repo.delegate.RepoViewImplModel
import java.lang.ref.WeakReference

class $viewModelClassName(
    mActivity: MainBaseActivity,
    val mItem: Response${className}s,
    private val mRepo: RepoViewImplModel,
    private val mAttachModel: RepoAttachmentViewModel
) : ViewModel() {

    private val refAct = WeakReference(mActivity)

    val item = BaseLifeData(mItem)

    fun onClick() {
        val destBundle = Bundle()
        destBundle.putString("id", mItem.id)
        destBundle.setAuditType(
            when (refAct.get()) {
                is Activity${Type.USER.key}${className}s -> ${Type.USER.currentType}
                is Activity${Type.AUDIT.key}${className}s -> ${Type.AUDIT.currentType}
                is Activity${Type.MANAGE.key}${className}s -> ${Type.MANAGE.currentType}
                else -> refAct.get()?.intent?.initAuditType() ?: ${Type.USER.currentType}
            }
        )

        Utils.startActivityByBundle(
            refAct.get(),
            ActivityDetail${className}::class.java,
            destBundle
        )
    }

    fun showCaseInfo(v: View) {
        v.jumpToDetailPage(ActivityCaseDetail::class.java)
    }
}
""".trimIndent()