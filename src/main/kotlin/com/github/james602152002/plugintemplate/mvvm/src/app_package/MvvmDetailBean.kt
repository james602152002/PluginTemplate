package com.github.james602152002.plugintemplate.mvvm.src.app_package

import asKt
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import save

fun createDetailBean(
    packageName: String = PackageManager.packageName,
    className: String,
    path: String,
    directorySrc: PsiDirectory,
) {
    val responseClassName = "Model${className}Info"
    createResponseStr(
        packageName, responseClassName, path
    ).save(
        directorySrc, "response.${path}", responseClassName.asKt()
    )
}

private fun createResponseStr(
    applicationPackageName: String,
    requestClassName: String,
    path: String,
) = """
package $applicationPackageName.response.${path}

import com.bitzsoft.model.response.common.ResponseCommon
import com.google.gson.annotations.SerializedName
import java.util.*

data class $requestClassName(
    @SerializedName("accountBank")
    var accountBank: String? = null,
    @SerializedName("accountName")
    var accountName: String? = null,
    @SerializedName("accountNumber")
    var accountNumber: String? = null,
    @SerializedName("auditFiles")
    var auditFiles: Any? = null,
    @SerializedName("caseId")
    var caseId: String? = null,
    @SerializedName("chargeAmount")
    var chargeAmount: Double? = null,
    @SerializedName("chargeCase")
    var chargeCase: Any? = null,
    @SerializedName("chargeCurrency")
    var chargeCurrency: String? = null,
    @SerializedName("chargeCurrencyName")
    var chargeCurrencyName: String? = null,
    @SerializedName("chargeDate")
    var chargeDate: Date? = null,
    @SerializedName("chargeNo")
    var chargeNo: String? = null,
    @SerializedName("chargeRate")
    var chargeRate: Double? = null,
    @SerializedName("chargeType")
    var chargeType: String? = null,
    @SerializedName("creationTime")
    var creationTime: Date? = null,
    @SerializedName("creatorUserId")
    var creatorUserId: Int? = null,
    @SerializedName("creatorUserName")
    var creatorUserName: String? = null,
    @SerializedName("groupType")
    var groupType: String? = null,
    @SerializedName("groupTypeName")
    var groupTypeName: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("isImportant")
    var isImportant: String? = null,
    @SerializedName("isImportantName")
    var isImportantName: String? = null,
    @SerializedName("leaderId")
    var leaderId: Int? = null,
    @SerializedName("leaderName")
    var leaderName: String? = null,
    @SerializedName("organizationUnitName")
    var organizationUnitName: String? = null,
    @SerializedName("remark")
    var remark: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("statusName")
    var statusName: String? = null,
    @SerializedName("userId")
    var userId: Int? = null,
    @SerializedName("userName")
    var userName: String? = null
) : ResponseCommon<$requestClassName>()
""".trimIndent()