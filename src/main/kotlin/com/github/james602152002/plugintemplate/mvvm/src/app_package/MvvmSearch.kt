package com.github.james602152002.plugintemplate.mvvm.src.app_package

import Type
import asKt
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.github.james602152002.plugintemplate.template.manifestTemplateXml
import com.intellij.psi.PsiDirectory
import save
import toSnakeCase
import java.io.File

fun RecipeExecutor.createSearch(
    packageName: String = PackageManager.packageName,
    className: String,
    manifestOut: File,
    path: String,
    directorySrc: PsiDirectory,
    directoryRes: PsiDirectory,
    keyKeywords: String,
) {
    val dir = "view.ui.search"
    val searchPath = path.split(".").first()
    val activityClassName = "ActivitySearch${className}s"
    mergeXml(
        manifestTemplateXml(
            dir = dir, path = searchPath, activityClassName = activityClassName
        ), manifestOut.resolve("AndroidManifest.xml")
    )

    //Activity
    createSearchActivity(
        packageName, activityClassName, className, path, searchPath, keyKeywords
    ).save(
        directorySrc, "${dir}.${searchPath}", activityClassName.asKt()
    )


    //layout
    val fragClassName = "FragmentSearch${className}"
    val layoutFileName = fragClassName.toSnakeCase()
    createXMLStr(packageName, className, searchPath).save(
        directoryRes, "layout", "${layoutFileName}.xml"
    )
    //Fragment
    createSearchFrag(
        packageName,
        fragClassName,
        className,
        path,
        searchPath,
        layoutFileName
    ).save(
        directorySrc, "view.fragment.search.${searchPath}", fragClassName.asKt()
    )
    val vmClassName = "Search${className}ViewModel"
    //viewModel
    createSearchVM(
        packageName,
        vmClassName,
        className,
        path,
        searchPath,
    ).save(
        directorySrc, "view_model.search.${searchPath}", vmClassName.asKt()
    )
}

private fun createSearchActivity(
    applicationPackageName: String,
    activityClassName: String,
    className: String,
    path: String,
    searchPath: String,
    keyKeywords: String,
) = """
package $applicationPackageName.view.ui.search.${searchPath}
  
import android.os.Bundle
import android.view.View
import $applicationPackageName.R
import $applicationPackageName.adapter.${path}.${className}sAdapter
import $applicationPackageName.databinding.ActivityCommonSearchBinding
import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
import $applicationPackageName.remote.${path}.Repo${className}
import $applicationPackageName.template.initAuditType
import $applicationPackageName.template.initRepoModel
import $applicationPackageName.view.fragment.search.BaseArchSearchFragment
import $applicationPackageName.view.fragment.search.${searchPath}.FragmentSearch${className}
import $applicationPackageName.view.ui.search.base.BaseArchSearchActivity
import $applicationPackageName.view_model.common.list.CommonListViewModel
import com.bitzsoft.base.helper.RefreshState
import com.bitzsoft.base.util.Constants
import com.bitzsoft.model.request.${path}.Request${className}s
import com.bitzsoft.model.response.${path}.Response${className}s
import com.bitzsoft.repo.view_model.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class $activityClassName :
    BaseArchSearchActivity<ActivityCommonSearchBinding, Request${className}s>(),
    View.OnClickListener {

    private val searchResults = mutableListOf<Response${className}s>()

    private val viewModel by lazy {
        CommonListViewModel<Response${className}s>(
            this,
            repo,
            RefreshState.REFRESH,
            0,
            null,
            ${className}sAdapter(this, searchResults)
        )
    }

    private val attachModel by viewModel<RepoAttachmentViewModel> {
        parametersOf(
            viewModel,
            repo
        )
    }

    private val repoModel: Repo${className} by initRepoModel {
        arrayOf(viewModel, repo)
    }

    override fun startBinding(binding: ActivityCommonSearchBinding) {
        val adapter = viewModel.adapter.get()
        binding.model = viewModel
    }

    override fun initView() {
        roomViewModel.updateKeywordsTitle(viewModel.sauryKeyMap, "$keyKeywords")
        viewModel.smartRefreshImplInit(this::fetchData)
    }

    override fun fetchData(refresh: Boolean) {
        when (intent.initAuditType()) {
            ${Type.MANAGE.currentType} -> repoModel.subscribeManagementList(
                refresh,
                request,
                searchResults
            )
            ${Type.AUDIT.currentType}-> repoModel.subscribeAuditList(
                refresh,
                request,
                searchResults
            )
            else -> repoModel.subscribeUserList(refresh, request, searchResults)
        }
    }

    override fun initFragment(impl: (viewModel: BaseViewModel, implFrag: () -> BaseArchSearchFragment<Request${className}s, *>, argInit: Bundle.() -> Unit, implKeywords: (keywords: String?) -> Unit) -> Unit) {
        impl(viewModel,
            { FragmentSearch$className() },
            { },
            { request.filter = it })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> goBack()
        }
    }
}
""".trimIndent()


private fun createXMLStr(
    applicationPackageName: String,
    className: String,
    searchPath: String,
) = """
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/card_view"
        style="@style/CommonCardStyle"
        searchCardLP="@{true}"
        android:clickable="true"
        android:descendantFocusability="afterDescendants"
        android:focusable="true"
        android:focusableInTouchMode="true">

        <androidx.constraintlayout.widget.ConstraintLayout
            style="@style/CommonListCardConstraintStyle"
            android:paddingTop="@{adjModel.commonHMargin}">

            <include
                android:id="@+id/category"
                layout="@layout/component_combobox_chips_selection"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone"
                app:adjModel="@{adjModel}"
                app:branchPermitSet="@{model.branchPermitSet}"
                app:chipsId="@{@id/recycler_view_category}"
                app:hintKey='@{"Type"}'
                app:items="@{model.categoryItems}"
                app:key='@{"category"}'
                app:layout_constraintBottom_toTopOf="@+id/reimburse_date"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:model="@{model}"
                app:sauryKeyMap="@{sauryKeyMap}"
                app:selectionIds="@={model.chargeTypes}"
                app:spanCnt="@{2}" />

            <include
                android:id="@+id/reimburse_date"
                layout="@layout/component_date_range_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone"
                app:adjModel="@{adjModel}"
                app:branchPermitSet="@{model.branchPermitSet}"
                app:keyPermission='@{"reimburse_date"}'
                app:keyTitle='@{"DateOfReimbursement"}'
                app:layout_constraintBottom_toTopOf="@+id/reimbursement_applicant"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/category"
                app:model="@{model}"
                app:pickerModel="@{pickerModel}"
                app:rangeInput="@={model.request.chargeDateRange}"
                app:sauryKeyMap="@{sauryKeyMap}" />

            <include
                android:id="@+id/reimbursement_applicant"
                layout="@layout/component_lawyer_chips_selection"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone"
                app:branchPermitSet="@{model.branchPermitSet}"
                app:hintKey='@{"ReimbursementApplicant"}'
                app:items="@{model.reimbursementApplicant}"
                app:key='@{"reimbursement_applicant"}'
                app:layout_constraintBottom_toTopOf="@+id/organization"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/reimburse_date"
                app:memberIdArr="@={model.request.creatorUserIdList}"
                app:model="@{model}"
                app:sauryKeyMap="@{sauryKeyMap}"
                app:selectLawyer="@{model::selectReimbursementApplicant}" />

            <com.james602152002.floatinglabelspinner.FloatingLabelSpinner
                android:id="@+id/organization"
                style="@style/BodyTextMustFillFLS"
                branchPermitSet="@{model.branchPermitSet}"
                centerLP="@{true}"
                changed="@{model.organizationChanged}"
                hintKey='@{"State"}'
                items="@{model.organizations}"
                keyMap="@{sauryKeyMap}"
                keyPermission='@{"organization"}'
                model="@{model}"
                position="@{model.organizationPos}"
                singleID="@={model.request.organizationUnitId}"
                android:visibility="gone"
                app:layout_constraintBottom_toTopOf="@+id/cost_amount_range"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/reimbursement_applicant" />

            <include
                android:id="@+id/cost_amount_range"
                layout="@layout/component_decimal_range_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone"
                app:adjModel="@{adjModel}"
                app:branchPermitSet="@{model.branchPermitSet}"
                app:end="@={model.request.beginAmount}"
                app:keyPermission='@{"component_over_due_days"}'
                app:keyTitle='@{"CostAmount"}'
                app:layout_constraintBottom_toTopOf="@+id/operation_time"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/organization"
                app:model="@{model}"
                app:sauryKeyMap="@{sauryKeyMap}"
                app:start="@={model.request.endAmount}" />

            <include
                android:id="@+id/operation_time"
                layout="@layout/component_date_range_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone"
                app:adjModel="@{adjModel}"
                app:branchPermitSet="@{model.branchPermitSet}"
                app:keyPermission='@{"operation_time"}'
                app:keyTitle='@{"FinancialPaymentDate"}'
                app:layout_constraintBottom_toTopOf="@+id/belonging_to_year"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/cost_amount_range"
                app:model="@{model}"
                app:pickerModel="@{pickerModel}"
                app:rangeInput="@={model.request.operationTimeRange}"
                app:sauryKeyMap="@{sauryKeyMap}" />

            <com.james602152002.floatinglabeledittext.FloatingLabelEditText
                android:id="@+id/belonging_to_year"
                style="@style/BodyTextFLE"
                branchPermitSet="@{model.branchPermitSet}"
                centerLP="@{true}"
                hintKey='@{"WithdrawalYear"}'
                keyMap="@{sauryKeyMap}"
                keyPermission='@{"belonging_to_year"}'
                model="@{model}"
                android:inputType="number"
                android:text="@={model.request.belongingToYear}"
                android:visibility="gone"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toBottomOf="@id/operation_time" />

        </androidx.constraintlayout.widget.ConstraintLayout>
    </androidx.cardview.widget.CardView>

    <data>

        <variable
            name="model"
            type="$applicationPackageName.view_model.search.$searchPath.Search${className}ViewModel" />

        <variable
            name="adjModel"
            type="com.bitzsoft.ailinkedlaw.view_model.base.LayoutAdjustViewModel" />

        <variable
            name="pickerModel"
            type="com.bitzsoft.ailinkedlaw.view_model.common.CommonDateTimePickerViewModel" />

        <variable
            name="sauryKeyMap"
            type="java.util.HashMap&lt;String,String>" />

    </data>
</layout>
""".trimIndent()

private fun createSearchFrag(
    applicationPackageName: String,
    searchClassName: String,
    className: String,
    path: String,
    searchPath: String,
    layoutFileName: String
) = """
package $applicationPackageName.view.fragment.search.${searchPath}

import $applicationPackageName.R
import $applicationPackageName.databinding.FragmentSearch${className}Binding
import $applicationPackageName.remote.common.RepoFLSOrganizationViewModel
import $applicationPackageName.template.initRepoModel
import $applicationPackageName.view.fragment.search.BaseArchSearchFragment
import $applicationPackageName.view_model.search.${searchPath}.Search${className}ViewModel
import com.bitzsoft.model.model.common.ModelFLSOrganizations
import com.bitzsoft.model.request.common.RequestCommonBooleanID
import com.bitzsoft.model.request.${path}.Request${className}s

class $searchClassName :
    BaseArchSearchFragment<Request${className}s, FragmentSearch${className}Binding>() {

    private val organizationModel: RepoFLSOrganizationViewModel by initRepoModel {
        arrayOf(
            viewModel,
            repo
        )
    }

    private val fragViewModel by lazy {
        Search${className}ViewModel(this, repo, request)
    }

    override fun layoutID() = R.layout.$layoutFileName

    override fun subscribe() {
        dataBinding {
            it.model = fragViewModel
            it.adjModel = adjModel
            it.pickerModel = pickerModel
            it.sauryKeyMap = viewModel.sauryKeyMap
        }
    }

    override fun fetchData() {
        if (fragViewModel.visible["organization"] == true) {
            val organizationObservables = mutableListOf(
                //组织机构
                ModelFLSOrganizations(
                    RequestCommonBooleanID(),
                    fragViewModel.organizations,
                    request.organizationUnitId,
                    fragViewModel::updateOrganizationSpinnerPos
                )
            )

            organizationModel.subscribe(organizationObservables)
        }
    }

    override fun startSearch() {
        listener.startSearch(request)
    }
}  
""".trimIndent()


private fun createSearchVM(
    applicationPackageName: String,
    vmClassName: String,
    className: String,
    path: String,
    searchPath: String,
) = """
package $applicationPackageName.view_model.search.${searchPath}

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import $applicationPackageName.template.clearSpace
import $applicationPackageName.template.form.formStatusConverter
import $applicationPackageName.template.form.initBranchForm
import $applicationPackageName.template.initAuditType
import $applicationPackageName.template.resultEmployees
import $applicationPackageName.template.selectMultiEmployee
import $applicationPackageName.view.fragment.base.BaseArchFragment
import com.bitzsoft.base.util.Constants
import com.bitzsoft.lifecycle.BaseLifeData
import com.bitzsoft.model.request.$path.Request${className}s
import com.bitzsoft.model.response.common.ResponseCommonComboBox
import com.bitzsoft.model.response.common.ResponseEmployeesItem
import com.bitzsoft.model.response.common.ResponseOrganizations
import com.bitzsoft.repo.delegate.RepoViewImplModel
import com.bitzsoft.repo.view_model.BaseViewModel
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.lang.ref.WeakReference

class $vmClassName(
    frag: BaseArchFragment<*>, repo: RepoViewImplModel, private val mRequest: Request${className}s
) : BaseViewModel(repo, null) {

    private val refContext = WeakReference(frag.requireContext())
    private val auditType = frag.arguments?.initAuditType()

    private val contractReimbursementApplicant: ActivityResultLauncher<Intent> =
        frag.get(named(Constants.contractFragCommon)) {
            parametersOf(
                frag, this::resultReimbursementApplicant
            )
        }

    val request = BaseLifeData(mRequest)

    //类型
    val categoryItems = BaseLifeData<MutableList<ResponseCommonComboBox>>()
    val chargeTypes = BaseLifeData(mRequest.chargeType?.joinToString { it }).apply {
        propertyChangedCallback(frag) {
            val result = get().clearSpace()
            mRequest.chargeType = when (result.isNullOrEmpty()) {
                true -> null
                else -> result.split(",").toMutableList()
            }
        }
    }

    //组织机构
    val organizations = mutableListOf<ResponseOrganizations>()
    val organizationPos = BaseLifeData<Int>()
    val organizationChanged = BaseLifeData(false)

    val reimbursementApplicant = BaseLifeData<MutableList<ResponseEmployeesItem>>()

    private val basePermitArr = arrayOf(
        "category",
        "reimburse_date",
        "reimbursement_applicant",
        "organization",
        "component_over_due_days",
        "operation_time",
        "belonging_to_year"
    )

    val branchPermitSet by initBranchForm(
        { frag.requireContext() }, basePermitArr = basePermitArr,
    )

    init {
        updateForm()
    }

    private fun updateForm() {
        refContext.get()?.let { context ->
            val baseVisArr = arrayOf("category", "reimburse_date", "reimbursement_applicant")
            val baseVisAdds = mutableListOf<String>()
            when (auditType) {
                Constants.TYPE_MANAGEMENT -> {
                    //组织机构、费用金额、财务打款日期
                    baseVisAdds += "organization"
                    baseVisAdds += "component_over_due_days"
                    baseVisAdds += "operation_time"
                    baseVisAdds += "belonging_to_year"
                }
                Constants.TYPE_AUDIT -> {

                }
                else -> {
                    //财务打款日期
                    baseVisAdds += "operation_time"
                }
            }

            updateVisibleGroup(
                formStatusConverter(
                    context, baseVisArr, baseAdds = baseVisAdds.toTypedArray()
                )
            )
        }
    }


    fun updateOrganizationSpinnerPos(pos: Int) {
        organizationChanged.set(true)
        organizationPos.set(pos)
    }


    fun selectReimbursementApplicant(v: View) {
        contractReimbursementApplicant.selectMultiEmployee(v, reimbursementApplicant)
    }

    private fun resultReimbursementApplicant(result: ActivityResult) {
        result.resultEmployees(reimbursementApplicant)
    }
}
""".trimIndent()