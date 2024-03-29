package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.github.james602152002.plugintemplate.template.manifestTemplateXml
import com.intellij.psi.PsiDirectory
import save
import toSnakeCase
import java.io.File

fun RecipeExecutor.createDetailActivity(
    packageName: String = PackageManager.packageName,
    className: String,
    manifestOut: File,
    path: String,
    directorySrc: PsiDirectory,
    directoryRes: PsiDirectory,
    keyTitle: String,
    keyCreation: String,
) {
    val dir = "view.ui"
    val activityClassName = "ActivityDetail${className}"
//    val layoutFileName = "Activity${className}"
    mergeXml(
        manifestTemplateXml(
            dir = dir,
            path = path,
            activityClassName = activityClassName
        ),
        manifestOut.resolve("AndroidManifest.xml")
    )

    val layoutFileName = activityClassName.toSnakeCase()
    createXMLStr(packageName, path, keyTitle, className).save(
        directoryRes, "layout", "${layoutFileName}.xml"
    )

    createDetailActivity(
        packageName,
        activityClassName,
        className,
        path,
        keyCreation,
        layoutFileName,
    ).save(
        directorySrc,
        "${dir}.${path}",
        activityClassName.asKt()
    )

    val vmClassName = "${className}DetailViewModel"
    createVM(
        packageName,
        vmClassName,
        className,
        path,
    ).save(
        directorySrc,
        "view_model.${path}",
        vmClassName.asKt()
    )
}

private fun createDetailActivity(
    applicationPackageName: String,
    activityClassName: String,
    className: String,
    path: String,
    keyCreation: String,
    layoutFileName: String
) = """
  package $applicationPackageName.view.ui.${path}
  
  import android.content.Intent
  import android.os.Bundle
  import android.view.View
  import $applicationPackageName.R
  import $applicationPackageName.databinding.${activityClassName}Binding
  import $applicationPackageName.decoration.common.CommonDividerItemDecoration
  import $applicationPackageName.remote.$path.Repo${className}Detail
  import $applicationPackageName.template.initAuditType
  import $applicationPackageName.template.initNotificationID
  import $applicationPackageName.template.initRepoModel
  import $applicationPackageName.template.operator.contains
  import $applicationPackageName.template.operator.toIgnoreCaseRegex
  import $applicationPackageName.template.model.actionBranchHashSet
  import $applicationPackageName.util.Utils
  import $applicationPackageName.view.fragment.bottom_sheet.common.BottomSheetCommonAction
  import $applicationPackageName.view.ui.base.BaseArchActivity
  import $applicationPackageName.view.ui.common.ActivityCommonWorkFlowList
  import $applicationPackageName.view_model.common.CommonDateTimePickerViewModel
  import $applicationPackageName.view_model.common.CommonDetailProcessViewModel
  import $applicationPackageName.view_model.common.contract.ViewModelContractProcess
  import $applicationPackageName.view_model.common.list.CommonListViewModel
  import $applicationPackageName.view_model.common.work_flow.CommonWorkFlowViewModel
  import $applicationPackageName.view_model.$path.${className}DetailViewModel
  import com.bitzsoft.base.helper.RefreshState
  import com.bitzsoft.base.template.commonCanEdit
  import com.bitzsoft.model.request.common.RequestCommonID
  import com.bitzsoft.model.request.common.workflow.RequestCommonProcess
  import com.bitzsoft.model.response.common.ResponseAction
  import com.bitzsoft.repo.delegate.RepoViewImplModel
  import org.koin.androidx.viewmodel.ext.android.viewModel
  import org.koin.core.parameter.parametersOf

  class $activityClassName : BaseArchActivity<${activityClassName}Binding>(),
      View.OnClickListener {


      private val actions = mutableListOf<ResponseAction>()
      private val actionMap by actionBranchHashSet(
          "edit", "delete", "redo"
      ) { "$keyCreation" }

      private val id by lazy { intent.initNotificationID() }
      private val request by lazy { RequestCommonID(id = id) }

      private val repo: RepoViewImplModel by viewModel()
      private val viewModel by lazy {
          ${className}DetailViewModel(this, repo, RefreshState.NORMAL)
      }

      private val pickerViewModel: CommonDateTimePickerViewModel by viewModel()

      private val workFlowModel by lazy {
          CommonWorkFlowViewModel(mAct = this, implId = { request.id })
      }

      private val vmContractProcess = ViewModelContractProcess(this)
      private val processModel by lazy {
          CommonDetailProcessViewModel(this, repo, vmContractProcess) { _, _, processContract ->
//              processContract.launch(Intent(
//                  this, ActivityProcess${className}::class.java
//              ).apply {
//                  putExtra("id", id)
//              })
          }
      }

      private val repoModel: Repo${className}Detail by initRepoModel { arrayOf(viewModel, repo) }

      override fun layoutID() = R.layout.$layoutFileName

      override fun subscribe() {
          dataBinding {
              it.adjModel = adjModel
              it.model = viewModel
              it.picker = pickerViewModel
              it.workFlowModel = workFlowModel
              it.processModel = processModel
          }
      }

      override fun initView() {
          viewModel.smartRefreshImplInit { fetchData() }
      }

      private fun fetchData() {
          repoModel.subscribeDetail(
              intent.initAuditType(),
              workFlowModel,
              request,
              this::updateActions
          )
      }

      private fun updateActions(actions: MutableList<ResponseAction>?) {
          this.actions.apply {
              clear()
              actions?.let { addAll(it) }
          }

          adjModel.editVis.set(actions.commonCanEdit(actionMap))
          processModel.updateViewModel(actions)
      }

      override fun onResume() {
          super.onResume()
          viewModel.updateRefreshState(RefreshState.REFRESH)
      }

      override fun onClick(v: View) {
          when (v.id) {
              R.id.back -> goBack()
              R.id.edit -> {
                  BottomSheetCommonAction().show(supportFragmentManager, actions, actionMap) {
                      when (it.name) {
                          //编辑
                          in "edit".toIgnoreCaseRegex() -> {
                              viewModel.startEdit()
                          }
                          //删除
                          in "delete".toIgnoreCaseRegex() -> {
                              showDialog(R.string.Delete) {
                                  repoModel.subscribeDelete(request)
                              }
                          }
                          //重新提交
                          in "redo".toIgnoreCaseRegex() -> {
                              showDialog(R.string.Redo) {
                                  repoModel.subscribeProcess(
                                      RequestCommonProcess(
                                          id = id, condition = it.condition, eventName = it.eventName
                                      )
                                  )
                              }
                          }
                      }
                  }
              }
          }
      }
  }
""".trimIndent()

private fun createXMLStr(
    applicationPackageName: String,
    path: String,
    keyTitle: String,
    className: String,
) = """
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/content_view"
        style="@style/CommonContentViewStyle"
        startConstraint="@={model.startConstraintImpl}"
        transitionConstraint="@{nestedConstraint}"
        transitionExcludeTargets="@{BindingUtils.toIntArray(@id/recycler_view)}">

        <com.facebook.drawee.view.SimpleDraweeView style="@style/CommonCollapsingFrescoBG" />

        <com.google.android.material.appbar.AppBarLayout style="@style/CommonAppBarStyle">

            <com.google.android.material.appbar.CollapsingToolbarLayout
                android:id="@+id/collapsing_toolbar_layout"
                style="@style/CommonCollapsingToolBarStyle">

                <include
                    layout="@layout/common_back_toolbar"
                    app:adjModel="@{adjModel}" />

                <androidx.constraintlayout.widget.ConstraintLayout style="@style/CommonToolBarConstraintLayoutStyle">

                    <com.bitzsoft.ailinkedlaw.widget.textview.ExpandTitleTextView
                        android:id="@+id/expand_title"
                        style="@style/CommonExpandedTitleStyle"
                        keyMap="@{model.sauryKeyMap}"
                        textKey='@{"$keyTitle"}'
                        app:left_icon_id="@+id/back" />

                    <com.bitzsoft.ailinkedlaw.widget.imageview.ExpandToolBarImageView
                        android:id="@+id/edit"
                        style="@style/CommonToolBarIconStyle"
                        visible="@{adjModel.editVis}"
                        android:onClick="@{adjModel::onClick}"
                        app:can_scroll_toolbar="true"
                        app:layout_constraintRight_toRightOf="parent"
                        app:layout_constraintTop_toTopOf="@null"
                        app:srcCompat="@drawable/ic_edit"
                        app:widget_position="most_right" />

                </androidx.constraintlayout.widget.ConstraintLayout>

            </com.google.android.material.appbar.CollapsingToolbarLayout>

        </com.google.android.material.appbar.AppBarLayout>

        <com.scwang.smart.refresh.layout.SmartRefreshLayout
            android:id="@+id/smart_refresh_layout"
            e="@{model.errorData}"
            smartRefreshImpl="@{model.refreshImplField}"
            snack="@{model.snackContentID}"
            snackCallBack="@{model.snackCallBack}"
            state="@={model.refreshState}"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/common_background_color"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            app:srlEnableLoadMore="false"
            app:srlEnableScrollContentWhenRefreshed="false">

            <androidx.core.widget.NestedScrollView
                android:id="@+id/scroll_view"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

                <androidx.constraintlayout.widget.ConstraintLayout
                    android:id="@+id/nested_constraint"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:descendantFocusability="blocksDescendants"
                    android:paddingTop="@{adjModel.semiVMargin}"
                    android:paddingBottom="@{adjModel.semiVMargin}">

                    <include
                        android:id="@+id/card_basic_info"
                        layout="@layout/component_title_and_flex"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        app:adjModel="@{adjModel}"
                        app:branchPermitSet="@{model.branchPermitSet}"
                        app:cardId="@{@id/card_basic_info}"
                        app:flexInfo="@{model.flexBasicInfo}"
                        app:keyPermission='@{model.keyPermissionBasicInfo}'
                        app:keyTitle='@{"BasicInformation"}'
                        app:model="@{model}"
                        app:resBT="@{@id/card_remark}"
                        app:titleId="@{@id/title_basic_info}" />

                    <include
                        android:id="@+id/card_remark"
                        layout="@layout/component_common_remark"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        app:branchPermitSet="@{model.branchPermitSet}"
                        app:cardCenterLP="@{true}"
                        app:content="@{model.item.remark}"
                        app:keyPermission='@{"remark"}'
                        app:keyTitle='@{"Remark"}'
                        app:layout_constraintBottom_toTopOf="@+id/title_common_approval_records"
                        app:layout_constraintLeft_toLeftOf="parent"
                        app:layout_constraintRight_toRightOf="parent"
                        app:layout_constraintTop_toBottomOf="@id/card_basic_info"
                        app:sauryKeyMap="@{model.sauryKeyMap}"
                        app:visible="@{model.item != null}" />

                    <include
                        layout="@layout/component_common_approval_records"
                        app:adjModel="@{adjModel}"
                        app:model="@{workFlowModel}"
                        app:picker="@{picker}"
                        app:sauryKeyMap="@{model.sauryKeyMap}"
                        app:topResId="@{@id/card_remark}" />

                </androidx.constraintlayout.widget.ConstraintLayout>
            </androidx.core.widget.NestedScrollView>

        </com.scwang.smart.refresh.layout.SmartRefreshLayout>

        <include
            android:id="@+id/process_bottom_sheet"
            layout="@layout/component_bottom_sheet_process"
            app:adjModel="@{adjModel}"
            app:anchorId="@{@id/smart_refresh_layout}"
            app:model="@{processModel}" />

    </androidx.coordinatorlayout.widget.CoordinatorLayout>

    <data>

        <import type="com.bitzsoft.ailinkedlaw.util.BindingUtils" />

        <variable
            name="model"
            type="$applicationPackageName.view_model.$path.${className}DetailViewModel" />

        <variable
            name="workFlowModel"
            type="com.bitzsoft.ailinkedlaw.view_model.common.work_flow.CommonWorkFlowViewModel" />

        <variable
            name="adjModel"
            type="com.bitzsoft.ailinkedlaw.view_model.base.LayoutAdjustViewModel" />

        <variable
            name="picker"
            type="com.bitzsoft.ailinkedlaw.view_model.common.CommonDateTimePickerViewModel" />

        <variable
            name="processModel"
            type="com.bitzsoft.ailinkedlaw.view_model.common.CommonDetailProcessViewModel" />

    </data>

</layout>
""".trimIndent()


private fun createVM(
    applicationPackageName: String,
    vmClassName: String,
    className: String,
    path: String,
) = """
package $applicationPackageName.view_model.${path}
  
import android.app.Activity
import android.os.Bundle
import com.bitzsoft.ailinkedlaw.delegates.human_resources.DelegateDepart
import $applicationPackageName.util.Utils
import $applicationPackageName.view.ui.base.MainBaseActivity
import $applicationPackageName.view.ui.$path.ActivityCreate$className
import com.bitzsoft.base.helper.RefreshState
import com.bitzsoft.lifecycle.BaseLifeData
import com.bitzsoft.model.model.$path.Model${className}Info
import com.bitzsoft.ailinkedlaw.model.ModelFlex
import com.bitzsoft.repo.delegate.RepoViewImplModel
import com.bitzsoft.repo.view_model.BaseViewModel
import java.lang.ref.WeakReference

class $vmClassName(
    mActivity: MainBaseActivity, repo: RepoViewImplModel,
    refreshState: RefreshState
) : BaseViewModel(repo, refreshState) {

    private val refAct = WeakReference(mActivity)
    val item = BaseLifeData<Model${className}Info>().apply {
        propertyChangedCallback(mActivity) { data ->
            updateBasicInfo(data)
        }
    }
    
    val flexBasicInfo = BaseLifeData<MutableList<ModelFlex<Any>>()
    val branchPermitSet by DelegateDepart.permitOnBoarding { mActivity }
    val keyPermissionBasicInfo = DelegateDepart.keyPermission

    val snackCallBack: (Any?) -> Unit = {
        when (it) {
            "SuccessfullyDeleted" -> {
                mActivity.setResult(Activity.RESULT_OK)
                mActivity.goBack()
            }
            else -> {
                updateRefreshState(RefreshState.REFRESH)
            }
        }
    }

    override fun updateViewModel(response: Any?) {
        when (response) {
            is Model${className}Info -> {
                item.set(response)
            }
        }
    }
    
    private fun updateBasicInfo(response: Model${className}Info?) {
        response ?: return
        flexBasicInfo.set(mutableListOf<ModelFlex<Any>>().apply {

        })
        refAct.get()?.let { DelegateDepart.updateForm(this, it, response) }
        startConstraint()
    }

    fun startEdit() {
        refAct.get()?.apply {
            val destBundle = Bundle()
            destBundle.putString("id", item.get()?.id)
//            Utils.startActivityByBundle(this, ActivityCreate$className::class.java, destBundle)
        }
    }
}
""".trimIndent()