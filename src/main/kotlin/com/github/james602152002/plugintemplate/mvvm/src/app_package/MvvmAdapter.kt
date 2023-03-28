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
    createXMLStr(packageName, className, path).save(
        directoryRes,
        "layout",
        "${layoutFileName}.xml"
    )

    val adapterClassName = "${className}sAdapter"

    createAdapterStr(
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
                    app:layout_constraintRight_toLeftOf="@+id/more"
                    app:layout_constraintTop_toTopOf="parent" />

                <com.bitzsoft.ailinkedlaw.widget.imageview.OperationImageView
                    android:id="@+id/more"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginEnd="@{adjModel.commonHMargin}"
                    android:background="@drawable/ripple_primary_oval"
                    android:onClick="@{()->model.showRecords()}"
                    app:layout_constraintLeft_toRightOf="@id/total_charge_amount"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toTopOf="@id/total_charge_amount"
                    app:srcCompat="@drawable/ic_more" />

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

private fun createAdapterStr(
    applicationPackageName: String,
    adapterClassName: String,
    className: String,
    path: String,
    layoutFileName: String,
) = """
package $applicationPackageName.adapter.${path}

import androidx.databinding.ViewDataBinding
import $applicationPackageName.R
import $applicationPackageName.adapter.base.ArchRecyclerAdapter
import $applicationPackageName.databinding.Card${className}sBinding
import $applicationPackageName.remote.attachment.RepoAttachmentViewModel
import $applicationPackageName.view.ui.base.MainBaseActivity
import $applicationPackageName.view_model.common.CommonDateTimePickerViewModel
import $applicationPackageName.view_model.${path}.${className}sViewModel
import com.bitzsoft.base.adapter.ArchViewHolder
import com.bitzsoft.model.response.${path}.Response${className}s
import com.bitzsoft.repo.delegate.RepoViewImplModel
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named
import java.text.DecimalFormat

class $adapterClassName(
    private val activity: MainBaseActivity,
    private val items: MutableList<Response${className}s>
) : ArchRecyclerAdapter<Card${className}sBinding>(activity, items) {

    lateinit var repo: RepoViewImplModel
    lateinit var attachModel: RepoAttachmentViewModel

    private val sauryKeyMap: HashMap<String?, String?> = activity.get(named("sauryKeyMap"))
    private val pickerModel: CommonDateTimePickerViewModel = activity.get()
    private val decimalFormat: DecimalFormat = activity.get()

    override fun layoutID(viewType: Int) = R.layout.$layoutFileName

    override fun initView(
        holder: ArchViewHolder<Card${className}sBinding>,
        position: Int
    ) {
        holder.dataBinding { binding ->
            binding.adjModel = adjModel
                    binding.pickerModel = pickerModel
                    binding.decimalFormat = decimalFormat
                    binding.sauryKeyMap = sauryKeyMap
                    binding.model = ${className}sViewModel(
                        activity,
                        items[position],
                        repo,
                        attachModel
                    )
        }
    }
}
""".trimIndent()