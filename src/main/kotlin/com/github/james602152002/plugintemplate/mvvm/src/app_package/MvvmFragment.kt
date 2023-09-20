package com.github.james602152002.plugintemplate.mvvm.src.app_package

import Type
import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createListFragment(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
    keyStatusList: String,
) {
    val fragmentClassName = "Fragment${className}s"
    createListFragmentStr(
        packageName, fragmentClassName, className, path, keyStatusList
    ).save(
        directorySrc, "view.fragment.${path}", fragmentClassName.asKt()
    )
}

private fun createListFragmentStr(
    applicationPackageName: String,
    fragmentClassName: String,
    className: String,
    path: String,
    keyStatusList: String,
) = """
    package $applicationPackageName.view.fragment.${path}

    import $applicationPackageName.adapter.${path}.${className}sAdapter
    import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
    import $applicationPackageName.remote.${path}.Repo${className}
    import $applicationPackageName.template.initRepoModel
    import $applicationPackageName.view.fragment.base.BaseArchListFragment
    import $applicationPackageName.view.ui.base.MainBaseActivity
    import $applicationPackageName.view.ui.${path}.Activity${Type.AUDIT.key}${className}s
    import $applicationPackageName.view.ui.${path}.Activity${Type.MANAGE.key}${className}s
    import $applicationPackageName.view.ui.${path}.Activity${Type.USER.key}${className}s
    import com.bitzsoft.model.request.${path}.Request${className}s
    import com.bitzsoft.model.response.${path}.Response${className}s
    import org.koin.androidx.viewmodel.ext.android.activityViewModel
    import org.koin.core.parameter.parametersOf

    class $fragmentClassName :
        BaseArchListFragment<Request${className}s, Response${className}s>() {

        override val request = Request${className}s(sorting = "creationTime desc")

        private val repoModel: Repo${className} by initRepoModel { arrayOf(viewModel, repo) }
        private val attachModel by activityViewModel<RepoAttachmentViewModel> {
            parametersOf(
                viewModel,
                repo
            )
        }

        override fun adapter() = ${className}sAdapter(requireActivity() as MainBaseActivity, items)

        override fun subscribe() {
            val adapter = viewModel.adapter.get()
            if (adapter is ${className}sAdapter) {
                adapter.repo = repo
                adapter.attachModel = attachModel
            }
            super.subscribe()
        }

        override fun fetchData(refresh: Boolean) {
            request.$keyStatusList = arguments?.getStringArrayList("$keyStatusList")
            when (activity) {
                is ActivityUser${className}s -> repoModel.subscribeUserList(refresh, request, items)
                is ActivityAudit${className}s -> repoModel.subscribeAuditList(
                    refresh,
                    request,
                    items
                )
                is ActivityManage${className}s -> repoModel.subscribeManagementList(
                    refresh,
                    request,
                    items
                )
            }
        }
    }
""".trimIndent()