package com.github.james602152002.plugintemplate

import Type
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.james602152002.plugintemplate.listeners.MyProjectManagerListener.Companion.projectInstance
import com.github.james602152002.plugintemplate.manager.ProjectFileManager
import com.github.james602152002.plugintemplate.manager.addPackageName
import com.github.james602152002.plugintemplate.mvvm.src.app_package.*
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager
import createListActivity

fun RecipeExecutor.mvvmRecipe(
    moduleData: ModuleTemplateData,
    pageName: String,
    packageRealName: String,
    path: String,
    needUserAct: Boolean,
    keyUser: String,
    hasUserCreation: Boolean,
    needAuditAct: Boolean,
    keyAudit: String,
    hasAuditCreation: Boolean,
    needManageAct: Boolean,
    keyManage: String,
    hasManageCreation: Boolean,
    keyCreation: String,
    keyStatusList: String,
    keyAdvanceSearch: String,
    keyKeywords: String,
    keyDetail: String,
) {
    val (projectData, _, _, manifestOut) = moduleData
    val project = projectInstance ?: return

    addAllKotlinDependencies(moduleData)
    addPackageName(packageRealName, projectData.applicationPackage.toString())

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.path.contains("src") }
    val virtRes = virtualFiles.first { it.path.contains("res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    val pfm = ProjectFileManager(project)
    if (pfm.init().not()) return

    /**
     * -----------------列表頁-----------------
     * */
    if (needUserAct) {
        createListActivity(
            packageName = packageRealName,
            className = pageName,
            manifestOut = manifestOut,
            path = path,
            directorySrc = directorySrc,
            processType = Type.USER,
            keyTitle = keyUser,
            hasCreation = hasUserCreation,
            keyCreation = keyCreation,
            keyStatusList = keyStatusList,
            keyAdvanceSearch = keyAdvanceSearch
        )
    }

    if (needAuditAct) {
        createListActivity(
            packageName = packageRealName,
            className = pageName,
            manifestOut = manifestOut,
            path = path,
            directorySrc = directorySrc,
            processType = Type.AUDIT,
            keyTitle = keyAudit,
            hasCreation = hasAuditCreation,
            keyCreation = keyCreation,
            keyStatusList = keyStatusList,
            keyAdvanceSearch = keyAdvanceSearch
        )
    }


    if (needManageAct) {
        createListActivity(
            packageName = packageRealName,
            className = pageName,
            manifestOut = manifestOut,
            path = path,
            directorySrc = directorySrc,
            processType = Type.MANAGE,
            keyTitle = keyManage,
            hasCreation = hasManageCreation,
            keyCreation = keyCreation,
            keyStatusList = keyStatusList,
            keyAdvanceSearch = keyAdvanceSearch
        )
    }


    /**
     * -----------------列表fragment、高級搜索-----------------
     * */

    if (needUserAct || needAuditAct || needManageAct) {
        //fragment
        createListFragment(
            packageName = packageRealName,
            className = pageName,
            path = path,
            directorySrc = directorySrc,
            keyStatusList = keyStatusList,
        )
        //請求
        createRepo(
            packageName = packageRealName,
            className = pageName,
            path = path,
            directorySrc = directorySrc,
        )
        //diffUtil
        createDiffUtil(
            packageName = packageRealName,
            className = pageName,
            path = path,
            directorySrc = directorySrc,
        )
        //适配器
        createAdapter(
            packageName = packageRealName,
            className = pageName,
            path = path,
            directorySrc = directorySrc,
            directoryRes = directoryRes
        )
//        //适配器viewModel
//        createViewModel(
//            packageName = packageRealName,
//            className = pageName,
//            path = path,
//            directorySrc = directorySrc,
//        )
        //列表javabean
        createListBean(
            packageName = packageRealName,
            className = pageName,
            path = path,
            directorySrc = directorySrc,
        )
        //高级搜索
        createSearch(
            packageName = packageRealName,
            className = pageName,
            manifestOut = manifestOut,
            path = path,
            directorySrc = directorySrc,
            directoryRes = directoryRes,
            keyKeywords = keyKeywords,
        )
    }
    /**
     * -----------------詳情-----------------
     * */
    createDetailActivity(
        packageName = packageRealName,
        className = pageName,
        manifestOut = manifestOut,
        path = path,
        directorySrc = directorySrc,
        directoryRes = directoryRes,
        keyTitle = keyDetail,
        keyCreation = keyCreation,
    )

    createDetailBean(
        packageName = packageRealName,
        className = pageName,
        path = path,
        directorySrc = directorySrc,
    )

    createRepoDetail(
        packageName = packageRealName,
        className = pageName,
        path = path,
        directorySrc = directorySrc,
    )
//    if (needActivity) {
//        mergeXml(
//            manifestTemplateXml(packageRealName, activityPackageName, "${pageName}Activity"),
//            manifestOut.resolve("AndroidManifest.xml")
//        )
//    }
//
////    val dir = PsiManager.getInstance(project).findDirectory(virtSrc)
////    virtualFiles
//    if (needActivity) {
////        val layoutFileName = "Activity${pageName}"
////       "".save(directoryRes, "layout", "${layoutFileName}.xml")
//        createActivity(className = pageName, manifestOut = manifestOut,  path= path,directorySrc = directorySrc)
////            .save(directorySrc, packageName, "${pageName}Activity".asKt())
//            .save(
//                directorySrc,
//                "view.ui.financial_management.charge_sz",
//                "${pageName}Activity".asKt()
//            )
////        save(
////            mvvmActivityKt(
////                packageRealName,
////                pageName,
////                activityPackageName,
////                activityLayoutName,
////                needViewModel
////            ), srcOut.resolve("${pageName.toLowerCase()}/${pageName}Activity.${ktOrJavaExt}")
////        )
//    }
//    if (generateActivityLayout) {
//
//    }
}