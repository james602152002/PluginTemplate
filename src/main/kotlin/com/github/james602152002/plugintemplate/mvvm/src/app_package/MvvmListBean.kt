package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createListBean(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val requestClassName = "Request${className}s"
    createRequestStr(
        packageName, requestClassName, path
    ).save(
        directorySrc, "request.${path}", requestClassName.asKt()
    )

    val responseClassName = "Response${className}s"
    createResponseStr(
        packageName, responseClassName, path
    ).save(
        directorySrc, "response.${path}", responseClassName.asKt()
    )
}

private fun createRequestStr(
    applicationPackageName: String,
    requestClassName: String,
    path: String,
) = """
package $applicationPackageName.request.${path}

import android.os.Parcelable
import com.bitzsoft.model.BuildConfig
import com.bitzsoft.model.request.common.RequestDateRangeInput
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class $requestClassName(
    @SerializedName("beginAmount")
    var beginAmount: Double? = null,
    @SerializedName("belongingToYear")
    var belongingToYear: String? = null,
    @SerializedName("chargeDateRange")
    var chargeDateRange: RequestDateRangeInput? = null,
    @SerializedName("chargeType")
    var chargeType: MutableList<String>? = null,
    @SerializedName("creatorUserIdList")
    var creatorUserIdList: MutableList<Int>? = null,
    @SerializedName("detailDateRange")
    var detailDateRange: RequestDateRangeInput? = null,
    @SerializedName("endAmount")
    var endAmount: Double? = null,
    @SerializedName("filter")
    var filter: String? = null,
    @SerializedName("operationTimeRange")
    var operationTimeRange: RequestDateRangeInput? = null,
    @SerializedName("organizationUnitId")
    var organizationUnitId: Int? = null,
    @SerializedName("pageNumber")
    var pageNumber: Int? = null,
    @SerializedName("pageSize")
    var pageSize: Int? = BuildConfig.pageSize,
    @SerializedName("statusList")
    var statusList: MutableList<String>? = null,
    @SerializedName("Type")
    var type: String? = null,
    @SerializedName("sorting")
    var sorting: String? = null
) : Parcelable
""".trimIndent()

private fun createResponseStr(
    applicationPackageName: String,
    requestClassName: String,
    path: String,
) = """
package $applicationPackageName.response.${path}


import com.bitzsoft.model.response.common.ResponseCommonList
import com.google.gson.annotations.SerializedName
import java.util.*

data class $requestClassName(
    @SerializedName("caseId")
    var caseId: String? = null,
    @SerializedName("caseName")
    var caseName: String? = null,
    @SerializedName("caseSerialId")
    var caseSerialId: String? = null,
    @SerializedName("chargeAmount")
    var chargeAmount: Double = 0.0,
    @SerializedName("chargeDate")
    var chargeDate: Date? = null,
    @SerializedName("operationTime")
    var operationTime: Date? = null,
    @SerializedName("chargeNo")
    var chargeNo: String? = null,
    @SerializedName("clientId")
    var clientId: String? = null,
    @SerializedName("clientName")
    var clientName: String? = null,
    @SerializedName("creationTime")
    var creationTime: Date? = null,
    @SerializedName("creatorUserId")
    var creatorUserId: String? = null,
    @SerializedName("userName", alternate = ["creatorUserName"])
    var creatorUserName: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("currencyText")
    var currencyText: String? = null,
    @SerializedName("groupType")
    var groupType: String? = null,
    @SerializedName("groupTypeName")
    var groupTypeName: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("remark")
    var remark: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("statusName")
    var statusName: String? = null
) : ResponseCommonList<$requestClassName>()
""".trimIndent()