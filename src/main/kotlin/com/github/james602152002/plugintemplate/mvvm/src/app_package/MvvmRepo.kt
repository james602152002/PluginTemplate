package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createRepo(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val repoClassName = "Repo${className}"
    createRepoStr(
        packageName, repoClassName, className, path
    ).save(
        directorySrc, "remote.${path}", repoClassName.asKt()
    )
}

private fun createRepoStr(
    applicationPackageName: String,
    repoClassName: String,
    className: String,
    path: String
) = """
    package $applicationPackageName.remote.${path}

    import android.content.Intent
    import $applicationPackageName.adapter.common.CommonListFVAdapter
    import $applicationPackageName.template.initTab
    import $applicationPackageName.template.updateData
    import $applicationPackageName.util.diffutil.${path}.Diff${className}CBU
    import $applicationPackageName.view_model.common.CommonTabViewModel
    import com.bitzsoft.model.request.${path}.Request${className}s
    import com.bitzsoft.model.response.${path}.Response${className}s
    import com.bitzsoft.repo.delegate.RepoViewImplModel
    import com.bitzsoft.repo.view_model.BaseRepoViewModel
    import com.bitzsoft.repo.view_model.BaseViewModel

    class $repoClassName(
        private val model: BaseViewModel,
        private val repo: RepoViewImplModel
    ) : BaseRepoViewModel() {

        fun subscribeUserTab(
            tabModel: CommonTabViewModel,
            adapter: CommonListFVAdapter<*>,
            queryName: String,
            intent: Intent?
        ) {
            val api = repo.service
            jobTab(model) {
                model.initTab(
                    tabModel,
                    adapter,
                    queryName,
                    intent
                ) { api.fetchUser${className}States() }
            }
        }

        fun subscribeAuditTab(
            tabModel: CommonTabViewModel,
            adapter: CommonListFVAdapter<*>,
            queryName: String,
            intent: Intent?
        ) {
            val api = repo.service
            jobTab(model) {
                model.initTab(
                    tabModel,
                    adapter,
                    queryName,
                    intent
                ) { api.fetchAudit${className}States() }
            }
        }

        fun subscribeManageTab(
            tabModel: CommonTabViewModel,
            adapter: CommonListFVAdapter<*>,
            queryName: String,
            intent: Intent?
        ) {
            val api = repo.service
            jobTab(model) {
                model.initTab(
                    tabModel,
                    adapter,
                    queryName,
                    intent
                ) { api.fetch${className}States() }
            }
        }

        /**
         * @param refresh true -> 刷新 false -> 加载
         * */
        fun subscribeUserList(
            refresh: Boolean,
            request: Request${className}s,
            items: MutableList<Response${className}s>
        ) {
            val api = repo.service
            jobList(model) {
                val oldData = items.toMutableList()
                updateData(refresh, model, request, items,
                    { api.fetchUser${className}s(request) },
                    { Diff${className}CBU(oldData, items) })
            }
        }

        /**
         * @param refresh true -> 刷新 false -> 加载
         * */
        fun subscribeAuditList(
            refresh: Boolean,
            request: Request${className}s,
            items: MutableList<Response${className}s>
        ) {
            val api = repo.service
            jobList(model) {
                val oldData = items.toMutableList()
                updateData(refresh, model, request, items,
                    { api.fetchAudit${className}s(request) },
                    { Diff${className}CBU(oldData, items) })
            }
        }

        /**
         * @param refresh true -> 刷新 false -> 加载
         * */
        fun subscribeManagementList(
            refresh: Boolean,
            request: Request${className}s,
            items: MutableList<Response${className}s>
        ) {
            val api = repo.service
            jobList(model) {
                val oldData = items.toMutableList()
                updateData(refresh, model, request, items,
                    { api.fetch${className}s(request) },
                    { Diff${className}CBU(oldData, items) }
                )
            }
        }
    }
""".trimIndent()