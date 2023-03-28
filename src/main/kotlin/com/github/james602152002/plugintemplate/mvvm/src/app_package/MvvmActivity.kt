import com.android.tools.idea.wizard.template.RecipeExecutor
import com.github.james602152002.plugintemplate.manager.PackageManager
import com.github.james602152002.plugintemplate.template.manifestTemplateXml
import com.intellij.psi.PsiDirectory
import java.io.File

enum class Type(val key: String, val currentType: String) {
    USER("User", "Constants.TYPE_PERSON"),
    AUDIT("Audit", "Constants.TYPE_AUDIT"),
    MANAGE("Manage", "Constants.TYPE_MANAGEMENT")
}

fun RecipeExecutor.createListActivity(
    packageName: String = PackageManager.packageName,
//    applicationPackageName: String = PackageManager.applicationPackageName,
    className: String,
    manifestOut: File,
    path: String,
    directorySrc: PsiDirectory,
    processType: Type,
    keyTitle: String,
    hasCreation: Boolean,
    keyCreation: String,
    keyStatusList: String,
    keyAdvanceSearch: String,
) {
    val activityClassName = "Activity${processType.key}${className}s"
//    val layoutFileName = "Activity${className}"
    mergeXml(
        manifestTemplateXml(
            path = path,
            activityClassName = activityClassName
        ),
        manifestOut.resolve("AndroidManifest.xml")
    )
//    generateSimpleLayout(moduleData, activityClassName, layoutFileName.toSnakeCase())
    createListActivity(
        packageName,
        activityClassName,
        className,
        path,
        processType,
        keyTitle,
        hasCreation,
        "\"" + keyCreation + "\"",
        keyStatusList,
        keyAdvanceSearch,
    ).save(
        directorySrc,
        "view.ui.${path}",
        activityClassName.asKt()
    )
}

private fun createListActivity(
    applicationPackageName: String,
    activityClassName: String,
    className: String,
    path: String,
    processType: Type,
    keyTitle: String,
    hasCreation: Boolean,
    keyCreation: String,
    keyStatusList: String,
    keyAdvanceSearch: String,
) = """
  package $applicationPackageName.view.ui.${path}
  
  import android.view.View
  import $applicationPackageName.remote.${path}.Repo${className}
  import $applicationPackageName.template.initRepoModel
  import $applicationPackageName.template.view.getPopupWindow
  import $applicationPackageName.view.fragment.${path}.Fragment${className}
  import $applicationPackageName.view.ui.base.BaseArchPageTSCActivity
  import $applicationPackageName.view.ui.search.${path}.ActivitySearch${className}
  import com.bitzsoft.base.util.Constants
  import com.bitzsoft.model.request.${path}.Request${className}s
  import com.bitzsoft.repo.delegate.RepoViewImplModel
  import org.koin.androidx.viewmodel.ext.android.viewModel
  
  class $activityClassName : BaseArchPageTSCActivity<Request${className}>(),
    View.OnClickListener {

    override var titleKey: String? = "$keyTitle"

    override fun statusListKey() = "$keyStatusList"

    override fun keyCreation() = ${if (hasCreation) keyCreation else null}

    override fun keyFilter() = "$keyAdvanceSearch"

    override fun implCreationPage() = ${if (hasCreation) "ActivityCreate${className}::class.java" else null}

    override fun implSearch(): Pair<String, Class<*>> =
        Pair(${processType.currentType}, ActivitySearch${className}::class.java)

    private val repo: RepoViewImplModel by viewModel()
    private val repoModel: Repo${className} by initRepoModel { arrayOf(viewModel, repo) }

    override fun frag(pos: Int) = Fragment${className}()

    override fun fetchTab() {
        repoModel.subscribe${processType.key}Tab(tabModel, adapter, statusListKey, intent)
    }

    override fun implShowFilter(request: Request${className}s, v: View, key: String, position: Int) {
        getPopupWindow(viewModel, v, key, request.sorting) {
            request.sorting = it
            adapter.refresh(position)
        }
    }
}
""".trimIndent()