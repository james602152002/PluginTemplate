package com.github.james602152002.plugintemplate.manager

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager

enum class Path(val type: Int) {
    APP(0), PRESENTATION(1), REPOSITORY(2), DATASOURCE(3), DOMAIN(4);

    companion object {
        private val map = values().associateBy(Path::type)
        fun from(type: Int) = map[type]
    }
}

class ProjectFileManager(
    private val project: Project,
    appPath: String = "app/src/main/java",
    presentationPath: String = "presentation/src/main/java",
    repositoryPath: String = "repository/src/main/java",
    datasourcePath: String = "datasource/src/main/java",
    domainPath: String = "domain/src/main/java",
) {

    private var modularized = false
    private val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots

    private val relativePaths = listOf(
        appPath, presentationPath, repositoryPath, datasourcePath, domainPath
    )

    private val resources = mutableListOf<Triple<Path, PsiDirectory?, String>>(
        Triple(Path.APP, null, appPath),
        Triple(Path.PRESENTATION, null, presentationPath),
        Triple(Path.REPOSITORY, null, repositoryPath),
        Triple(Path.DATASOURCE, null, datasourcePath),
        Triple(Path.DOMAIN, null, domainPath)
    )

    fun dirOf(path: Path) =
        if (modularized) resources[path.type].second!! else resources[0].second!!

    fun init(modularized: Boolean = false): Boolean {
        relativePaths.forEachIndexed { idx, path ->
            virtualFile(virtualFiles, path)?.let {
                resources[idx] =
                    resources[idx].copy(second = PsiManager.getInstance(project).findDirectory(it))
            }
        }
        resources.forEach { println(it.second) }
        if (modularized and resources.any { it.second == null }) return false
        return true
    }

    private fun virtualFile(virtualFiles: Array<VirtualFile>, relativePath: String) =
        virtualFiles.firstOrNull { it.path.contains(relativePath) }
}