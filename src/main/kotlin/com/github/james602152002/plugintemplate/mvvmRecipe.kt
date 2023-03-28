package com.github.james602152002.plugintemplate

import asKt
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.james602152002.plugintemplate.listeners.MyProjectManagerListener.Companion.projectInstance
import com.github.james602152002.plugintemplate.manager.PackageManager.packageName
import com.github.james602152002.plugintemplate.manager.Path
import com.github.james602152002.plugintemplate.manager.ProjectFileManager
import com.github.james602152002.plugintemplate.manager.addPackageName
import com.github.james602152002.plugintemplate.template.manifestTemplateXml
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager
import createActivity
import save

fun RecipeExecutor.mvvmRecipe(
    moduleData: ModuleTemplateData,
    pageName: String,
    packageRealName: String,
    needActivity: Boolean,
    activityLayoutName: String,
    generateActivityLayout: Boolean,
    activityPackageName: String,
    needFragment: Boolean,
    fragmentLayoutName: String,
    generateFragmentLayout: Boolean,
    fragmentPackageName: String,
    needRepository: Boolean,
    needViewModel: Boolean,
    repositoryPackageName: String,
    viewModelPackageName: String
) {
    val (projectData,_,_,manifestOut) = moduleData
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

//    if (needActivity) {
//        mergeXml(
//            manifestTemplateXml(packageRealName, activityPackageName, "${pageName}Activity"),
//            manifestOut.resolve("AndroidManifest.xml")
//        )
//    }

//    val dir = PsiManager.getInstance(project).findDirectory(virtSrc)
//    virtualFiles
    if (needActivity) {
        val layoutFileName = "Activity${pageName}"
       "".save(directoryRes, "layout", "${layoutFileName}.xml")
        createActivity(className = pageName, manifestOut = manifestOut, moduleData = moduleData)
            .save(directorySrc, packageName, "${pageName}Activity".asKt())
//        save(
//            mvvmActivityKt(
//                packageRealName,
//                pageName,
//                activityPackageName,
//                activityLayoutName,
//                needViewModel
//            ), srcOut.resolve("${pageName.toLowerCase()}/${pageName}Activity.${ktOrJavaExt}")
//        )
    }
}