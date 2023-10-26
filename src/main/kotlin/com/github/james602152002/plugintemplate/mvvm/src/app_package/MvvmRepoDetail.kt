package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createRepoDetail(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val repoClassName = "Repo${className}Detail"
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

import $applicationPackageName.view_model.common.list.CommonListViewModel
import $applicationPackageName.view_model.common.work_flow.CommonWorkFlowViewModel
import com.bitzsoft.base.util.Constants
import com.bitzsoft.model.request.common.RequestCommonID
import com.bitzsoft.model.request.common.workflow.RequestCommonProcess
import com.bitzsoft.model.response.common.ResponseAction
import com.bitzsoft.repo.delegate.RepoViewImplModel
import com.bitzsoft.repo.view_model.BaseRepoViewModel
import com.bitzsoft.repo.view_model.BaseViewModel

class $repoClassName(
    private val model: BaseViewModel,
    private val repo: RepoViewImplModel
) : BaseRepoViewModel() {

    fun subscribeDetail(
        auditType: String,
        workFlowModel: CommonWorkFlowViewModel,
        request: RequestCommonID,
        actionImpl: (MutableList<ResponseAction>) -> Unit
    ) {
        val api = repo.service
        jobInfo(
            model, api, request,
            implInfo = {
                model.subscribeOnUI(this, { api.fetch${className}Info(request) }) { response ->
                    response.result?.let { result ->
                        model.updateViewModel(result)
                    }
                }
            },
            implWorkflow = { workFlowModel.updateWorkFlow(it) },
            apiActions = {
                when (auditType) {
                    //管理
                    Constants.TYPE_MANAGEMENT -> api.fetch${className}Actions(request)
                    //审核
                    Constants.TYPE_AUDIT -> api.fetchAudit${className}Actions(request)
                    //我的
                    else -> api.fetchUser${className}Actions(request)
                }
            },
            actionImpl = actionImpl
        )
    }

    fun fetchAuditActions(requestProcess: RequestCommonProcess) {
        val api = repo.service
        jobAuditAction(model) { api.fetchAudit${className}Actions(RequestCommonID(id = requestProcess.id)) }
    }

    fun subscribeProcess(request: RequestCommonProcess) {
        val api = repo.service
        jobProcess(model, request, Constants.P_TYPE_PROCESS) { api.fetchProcess${className}(request) }
    }

    fun subscribeDelete(request: RequestCommonID) {
        val api = repo.service
        jobDelete(model, request.id) { api.fetchDelete${className}(request) }
    }
}
""".trimIndent()