package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save
import toSnakeCase

fun createAdapter(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
    directoryRes: PsiDirectory,
) {
    //layout
    val layoutFileName = "Card${className}".toSnakeCase()
//    createXMLStr(packageName, className, path).save(
//        directoryRes,
//        "layout",
//        "${layoutFileName}.xml"
//    )

    val adapterClassName = "${className}sAdapter"

//    createAdapterStr(
//        packageName, adapterClassName, className, path, layoutFileName
//    ).save(
//        directorySrc, "adapter.${path}", adapterClassName.asKt()
//    )
    createFlexAdapterStr(
        packageName, adapterClassName, className, path, layoutFileName
    ).save(
        directorySrc, "adapter.${path}", adapterClassName.asKt()
    )
}

private fun createXMLStr(
    applicationPackageName: String,
    className: String,
    path: String,
) = """
    <?xml version="1.0" encoding="utf-8"?>
    <layout>

        <androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            style="@style/CommonCardStyle"
            android:onClick="@{()->model.onClick()}"
            android:tag="@{model.item}">

            <androidx.constraintlayout.widget.ConstraintLayout
                style="@style/CommonListCardConstraintStyle"
                android:paddingTop="@{adjModel.semiHMargin}"
                android:paddingBottom="@{adjModel.semiHMargin}">

                <com.bitzsoft.ailinkedlaw.widget.textview.DetailPagesTitleTextView
                    android:id="@+id/total_charge_amount"
                    centerLP="@{true}"
                    decimal="@{model.item.chargeAmount}"
                    format="@{decimalFormat}"
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"TotalChargeAmount"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toTopOf="@+id/currency"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toTopOf="parent" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ContentTextView
                    android:id="@+id/currency"
                    centerLP="@{true}"
                    content="@{BindingUtils.parseCharge(model.item.currencyText, model.item.currency)}"
                    keyDefault='@{"UnFilled"}'
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"Currency"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toTopOf="@+id/case_name"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toBottomOf="@id/total_charge_amount" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ThemeColorBodyTextView
                    android:id="@+id/case_name"
                    centerLP="@{true}"
                    content="@{model.item.caseName}"
                    keyDefault='@{"UnFilled"}'
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"CaseName"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    android:onClick="@{model::showCaseInfo}"
                    android:tag="@{model.item.caseId}"
                    app:layout_constraintBottom_toTopOf="@+id/case_serial_id"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toBottomOf="@id/currency" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ContentTextView
                    android:id="@+id/case_serial_id"
                    centerLP="@{true}"
                    content="@{model.item.caseSerialId}"
                    keyDefault='@{"UnFilled"}'
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"CaseNumber"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toTopOf="@+id/reimbursement_applicant"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toBottomOf="@id/case_name" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ContentTextView
                    android:id="@+id/reimbursement_applicant"
                    centerLP="@{true}"
                    content="@{model.item.creatorUserName}"
                    keyDefault='@{"UnFilled"}'
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"ReimbursementApplicant"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toTopOf="@+id/financial_payment_date"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toBottomOf="@id/case_serial_id" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ContentTextView
                    android:id="@+id/financial_payment_date"
                    centerLP="@{true}"
                    date="@{model.item.chargeDate}"
                    format="@{pickerModel.df}"
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"DateOfReimbursement"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toTopOf="@+id/operation_time"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toBottomOf="@id/reimbursement_applicant" />

                <com.bitzsoft.ailinkedlaw.widget.textview.ContentTextView
                    android:id="@+id/operation_time"
                    centerLP="@{true}"
                    date="@{model.item.operationTime}"
                    format="@{pickerModel.df}"
                    keyMap="@{sauryKeyMap}"
                    keyTitle='@{"FinancialPaymentDate"}'
                    android:layout_width="0px"
                    android:layout_height="wrap_content"
                    android:ellipsize="end"
                    android:maxLines="1"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toLeftOf="@+id/status"
                    app:layout_constraintTop_toBottomOf="@id/financial_payment_date" />

                <com.bitzsoft.ailinkedlaw.widget.textview.BodyTextView
                    android:id="@+id/status"
                    layout_constraintWidth_max="@{300}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginEnd="@{adjModel.commonHMargin}"
                    android:text="@{model.item.statusName}"
                    app:layout_constraintBottom_toBottomOf="@id/operation_time"
                    app:layout_constraintLeft_toRightOf="@id/operation_time"
                    app:layout_constraintRight_toRightOf="parent"
                    app:status="@{model.item.status}"
                    app:type='@{"commonPattern"}' />

            </androidx.constraintlayout.widget.ConstraintLayout>
        </androidx.cardview.widget.CardView>

        <data>

            <import type="com.bitzsoft.ailinkedlaw.util.BindingUtils" />

            <variable
                name="model"
                type="${applicationPackageName}.view_model.${path}.${className}sViewModel" />

            <variable
                name="adjModel"
                type="com.bitzsoft.ailinkedlaw.view_model.base.LayoutAdjustViewModel" />

            <variable
                name="pickerModel"
                type="com.bitzsoft.ailinkedlaw.view_model.common.CommonDateTimePickerViewModel" />

            <variable
                name="sauryKeyMap"
                type="java.util.HashMap&lt;String,String>" />

            <variable
                name="decimalFormat"
                type="java.text.DecimalFormat" />

        </data>

    </layout>
""".trimIndent()


private fun createFlexAdapterStr(
    applicationPackageName: String,
    adapterClassName: String,
    className: String,
    path: String,
    layoutFileName: String,
) = """
package $applicationPackageName.adapter.${path}

import android.os.Bundle
import $applicationPackageName.adapter.common.flex.CommonCellFlexAdapter
import $applicationPackageName.template.initAuditType
import $applicationPackageName.template.setAuditType
import $applicationPackageName.util.Utils
import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
import $applicationPackageName.view.ui.base.MainBaseActivity
import $applicationPackageName.view.ui.business_management.cases.ActivityCaseDetail
import $applicationPackageName.view.ui.${path}.Activity${Type.AUDIT.key}${className}s
import $applicationPackageName.view.ui.${path}.ActivityDetail${className}
import $applicationPackageName.view.ui.${path}.Activity${Type.MANAGE.key}${className}s
import $applicationPackageName.view.ui.${path}.Activity${Type.USER.key}${className}s
import $applicationPackageName.view_model.common.CommonDateTimePickerViewModel
import com.bitzsoft.base.util.Constants
import com.bitzsoft.base.adapter.ArchViewHolder
import com.bitzsoft.ailinkedlaw.model.ModelFlex
import com.bitzsoft.model.response.${path}.Response${className}s
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named
import java.text.DecimalFormat

class $adapterClassName(
    private val activity: MainBaseActivity,
    items: MutableList<Response${className}s>
) : CommonCellFlexAdapter<Response${className}s>(
    activity, items, isCard = true, onClick = { mItem ->
        val destBundle = Bundle()
        destBundle.putString("id", mItem.id)
        destBundle.setAuditType(
            when (activity) {
                is Activity${Type.USER.key}${className}s -> ${Type.USER.currentType}
                is Activity${Type.AUDIT.key}${className}s -> ${Type.AUDIT.currentType}
                is Activity${Type.MANAGE.key}${className}s -> ${Type.MANAGE.currentType}
                else -> activity.intent?.initAuditType() ?: ${Type.USER.currentType}
            }
        )

        Utils.startActivityByBundle(
            activity,
            ActivityDetail${className}::class.java,
            destBundle
        )
    }
) {
    private val pickerModel: CommonDateTimePickerViewModel = activity.get()
    private val decimalFormat: DecimalFormat = activity.get()

    override fun funFlexConv(model: Response${className}s): MutableList<ModelFlex> {
        return mutableListOf<ModelFlex>().apply {
//            this += ModelFlex(
//                keyTitle = "Pages.Customers.CaseConfirm",//立案客户
//                content = model.clientName,
//                isTitle = true,
//                singleLine = true,
//            )
//            this += ModelFlex(
//                status = model.caseSampleExecFlag,//状态
//                statusText = model.caseSampleExecFlagText,
//                statusType = Constants.STATUS_DEFAULT,
//            )
//            this += ModelFlex(
//                keyTitle = "SourceChannel",//来源渠道
//                content = model.pbSourceChannel?.replace("\n", ""),
//            )
//            this += ModelFlex(
//                keyTitle = "ContactDate",//联系日期
//                content = model.pbContactDate,
//                dateFormat = pickerModel.df
//            )
//            this += ModelFlex(
//                keyTitle = "ProfessionalField",//专业领域
//                content = model.pbAreasOfExpertise?.replace("\n", ""),
//            )
//            this += ModelFlex(
//                keyTitle = "DockingLawyers",//对接律师
//                content = model.pbDockingLawyersName,
//            )
//            this += ModelFlex(
//                keyTitle = "PaymentAmount",//收款金额
//                content = model.costLimit,
//                decimalFormat = decimalFormat
//            )
//            this += ModelFlex(
//                keyTitle = "demand",//需求
//                content = model.demandInfo,
//            )
//            this += ModelFlex(
//                keyTitle = "Remark",//备注
//                content = model.pbOther,
//            )
        }
    }
}
""".trimIndent()

//
//private fun createAdapterStr(
//    applicationPackageName: String,
//    adapterClassName: String,
//    className: String,
//    path: String,
//    layoutFileName: String,
//) = """
//package $applicationPackageName.adapter.${path}
//
//import androidx.databinding.ViewDataBinding
//import $applicationPackageName.R
//import $applicationPackageName.adapter.base.ArchRecyclerAdapter
//import $applicationPackageName.databinding.Card${className}sBinding
//import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
//import $applicationPackageName.view.ui.base.MainBaseActivity
//import $applicationPackageName.view_model.common.CommonDateTimePickerViewModel
//import $applicationPackageName.view_model.${path}.${className}sViewModel
//import com.bitzsoft.base.adapter.ArchViewHolder
//import com.bitzsoft.model.response.${path}.Response${className}s
//import com.bitzsoft.repo.delegate.RepoViewImplModel
//import org.koin.android.ext.android.get
//import org.koin.core.qualifier.named
//import java.text.DecimalFormat
//
//class $adapterClassName(
//    private val activity: MainBaseActivity,
//    private val items: MutableList<Response${className}s>
//) : ArchRecyclerAdapter<Card${className}sBinding>(activity, items) {
//
//    lateinit var repo: RepoViewImplModel
//    lateinit var attachModel: RepoAttachmentViewModel
//
//    private val sauryKeyMap: HashMap<String?, String?> = activity.get(named("sauryKeyMap"))
//    private val pickerModel: CommonDateTimePickerViewModel = activity.get()
//    private val decimalFormat: DecimalFormat = activity.get()
//
//    override fun layoutID(viewType: Int) = R.layout.$layoutFileName
//
//    override fun initView(
//        holder: ArchViewHolder<Card${className}sBinding>,
//        position: Int
//    ) {
//        holder.dataBinding { binding ->
//            binding.adjModel = adjModel
//                    binding.pickerModel = pickerModel
//                    binding.decimalFormat = decimalFormat
//                    binding.sauryKeyMap = sauryKeyMap
//                    binding.model = ${className}sViewModel(
//                        activity,
//                        items[position],
//                        repo,
//                        attachModel
//                    )
//        }
//    }
//}
//""".trimIndent()