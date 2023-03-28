package com.github.james602152002.plugintemplate

import com.android.tools.idea.wizard.template.*

val MVVMTemplate
    get() = template {
        name = "MVVM Template"
        description = "一键创建 MVVM 单个页面所需要的全部组件"
        minApi = 19

        category = Category.Other
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.ActivityGallery,
            WizardUiContext.MenuEntry,
            WizardUiContext.NewProject,
            WizardUiContext.NewModule
        )

        val pageName = stringParameter {
            name = "Page Name"
            default = "Main"
            help = "请填写页面名,如填写 Main,会自动生成 MainActivity, MainViewModel 等文件"
            constraints = listOf(Constraint.NONEMPTY, Constraint.UNIQUE)
        }

        val packageName = stringParameter {
            name = "Root Package Name"
            default = "com.bitzsoft.ailinkedlaw"
            constraints = listOf(Constraint.PACKAGE)
            help = "请填写你的项目包名,请认真核实此包名是否是正确的项目包名,不能包含子包,正确的格式如:com.exanmple.app"
        }

//        //是否需要生成Activity
//        val needActivity = booleanParameter {
//            name = "Generate Activity"
//            default = true
//            help = "是否需要生成 Activity ? 不勾选则不生成"
//        }
        //文件分类 financial_management.charge_sz
        val path = stringParameter {
            name = "Path"
            default = "financial_management.charge_sz"
            help = "页面分类"
        }

        /**
         * ----------------------我的列表----------------------
         * */
        //是否创建我的列表页面
        val needUserAct = booleanParameter {
            name = "Generate User Activity"
            default = true
            help = "是否需要生成 Activity ? 不勾选则不生成"
        }

        //我的列表页面标题
        val keyUser = stringParameter {
            name = "Key User Title"
            default = "Pages.Financial.ChargesSZ"
            visible = { needUserAct.value }
            help = "我的列表页面标题"
        }

        //我的列表页面是否有创建
        val hasUserCreation = booleanParameter {
            name = "Has User Creation"
            default = true
            visible = { needUserAct.value }
            help = "是否有创建页面"
        }

        /**
         * ----------------------审批列表----------------------
         * */
        //是否创建审批列表页面
        val needAuditAct = booleanParameter {
            name = "Generate Audit Activity"
            default = true
            help = "是否需要生成 Activity ? 不勾选则不生成"
        }

        //审批列表页面标题
        val keyAudit = stringParameter {
            name = "Key Audit Title"
            default = "Pages.Financial.ChargesSZ.AuditCharges"
            visible = { needAuditAct.value }
            help = "审批列表页面标题"
        }

        //审批列表页面是否有创建
        val hasAuditCreation = booleanParameter {
            name = "Has Audit Creation"
            default = false
            visible = { needAuditAct.value }
            help = "是否有创建页面"
        }

        /**
         * ----------------------管理列表----------------------
         * */
        //是否创建管理列表页面
        val needManageAct = booleanParameter {
            name = "Generate Manage Activity"
            default = true
            help = "是否需要生成 Activity ? 不勾选则不生成"
        }

        //管理列表页面标题
        val keyManage = stringParameter {
            name = "Key Manage Title"
            default = "Pages.Financial.ChargesSZ.ManageCharges"
            visible = { needManageAct.value }
            help = "管理列表页面标题"
        }

        //管理列表页面是否有创建
        val hasManageCreation = booleanParameter {
            name = "Has Manage Creation"
            default = true
            visible = { needManageAct.value }
            help = "是否有创建页面"
        }

        /**
         * ----------------------列表内容----------------------
         * */
        //创建的key
        val keyCreation = stringParameter {
            name = "Permission Key Of Creation"
            default = "Pages.Financial.ChargesSZ.Create"
            help = "创建的key"
        }

        //状态筛选的key
        val keyStatusList = stringParameter {
            name = "StatusList Key"
            default = "statusList"
            help = "状态筛选的key"
        }

        //高级搜索的key
        val keyAdvanceSearch = stringParameter {
            name = "Advance Search Key"
            default = "menu_charge_sz"
            help = "状态筛选的key"
        }

        /**
         * ----------------------列表Fragment----------------------
         * */


//        //布局名
//        val activityLayoutName = stringParameter {
//            name = "Activity Layout Name"
//            default = "activity_main"
//            visible = { needActivity.value }
//            help = "Activity 创建之前需要填写 Activity 的布局名,若布局已创建就直接填写此布局名,若还没创建此布局,请勾选下面的单选框"
//            constraints = listOf(Constraint.LAYOUT, Constraint.NONEMPTY)
//            suggest = { "${activityToLayout(pageName.value.toUpperCase())}" }
//        }

//        //是否需要Activity的布局
//        val generateActivityLayout = booleanParameter {
//            name = "Generate Activity Layout"
//            default = true
//            visible = { needActivity.value }
//            help = "是否需要给 Activity 生成布局? 若勾选,则使用上面的布局名给此 Activity 创建默认的布局"
//        }
//
//        val activityPackageName = stringParameter {
//            name = "Activity Package Name"
//            default = "Activity Package Name"
//            visible = { needActivity.value }
//            help = "Activity 将被输出到此包下,请认真核实此包名是否是你需要输出的目标包名"
//            constraints = listOf(Constraint.PACKAGE)
//            suggest = { "${packageName.value}.${pageName.value.toLowerCase()}" }
//        }
//
//        //Fragment
//        //是否需要生成Fragment
//        val needFragment = booleanParameter {
//            name = "Generate Fragment"
//            default = false
//            help = "是否需要生成 Fragment ? 不勾选则不生成"
//        }
//
//        //布局名
//        val fragmentLayoutName = stringParameter {
//            name = "Fragment Layout Name"
//            default = "fragment_main"
//            visible = { needFragment.value }
//            help = "Fragment 创建之前需要填写 Fragment 的布局名,若布局已创建就直接填写此布局名,若还没创建此布局,请勾选下面的单选框"
//            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
//            suggest = { "${fragmentToLayout(pageName.value.toUpperCase())}" }
//        }
//
//        //是否需要Fragment的布局
//        val generateFragmentLayout = booleanParameter {
//            name = "Generate Fragment Layout"
//            default = true
//            visible = { needFragment.value }
//            help = "是否需要给 Fragment 生成布局? 若勾选,则使用上面的布局名给此 Fragment 创建默认的布局"
//        }
//
//        val fragmentPackageName = stringParameter {
//            name = "Fragment Package Name"
//            default = "function Package Name"
//            constraints = listOf(Constraint.PACKAGE)
//            visible = { needFragment.value }
//            help = "Fragment 将被输出到此包下,请认真核实此包名是否是你需要输出的目标包名"
//            suggest = { "${packageName.value}.${pageName.value.toLowerCase()}" }
//        }
//
//        val needRepository = booleanParameter {
//            name = "Generate Repository"
//            default = true
//            help = "是否需要生成 Repository ? 不勾选则不生成"
//        }
//
//        val repositoryPackageName = stringParameter {
//            name = "Repository Package Name"
//            default = "Repository Package Name"
//            constraints = listOf(Constraint.PACKAGE)
//            visible = { needRepository.value }
//            help = "Repository 将被输出到此包下,请认真核实此包名是否是你需要输出的目标包名"
//            suggest = { "${packageName.value}.${pageName.value.toLowerCase()}" }
//        }
//
//        val needViewModel = booleanParameter {
//            name = "Generate ViewModel"
//            default = true
//            help = "是否需要生成 ViewModel ? 不勾选则不生成"
//        }
//
//        val viewModelPackageName = stringParameter {
//            name = "ViewModel Package Name"
//            default = "ViewModel Package Name"
//            constraints = listOf(Constraint.PACKAGE)
//            visible = { needViewModel.value }
//            help = "ViewModel 将被输出到此包下,请认真核实此包名是否是你需要输出的目标包名"
//            suggest = { "${packageName.value}.${pageName.value.toLowerCase()}" }
//        }

        widgets(
            TextFieldWidget(pageName),
            PackageNameWidget(packageName),
            TextFieldWidget(path),
            CheckBoxWidget(needUserAct),
            TextFieldWidget(keyUser),
            CheckBoxWidget(hasUserCreation),
            CheckBoxWidget(needAuditAct),
            TextFieldWidget(keyAudit),
            CheckBoxWidget(hasAuditCreation),
            CheckBoxWidget(needManageAct),
            TextFieldWidget(keyManage),
            CheckBoxWidget(hasManageCreation),
            TextFieldWidget(keyCreation),
            TextFieldWidget(keyStatusList),
            TextFieldWidget(keyAdvanceSearch),
//            CheckBoxWidget(needActivity),
//            TextFieldWidget(activityLayoutName),
//            CheckBoxWidget(generateActivityLayout),
//            TextFieldWidget(activityPackageName),
//            CheckBoxWidget(needFragment),
//            TextFieldWidget(fragmentLayoutName),
//            CheckBoxWidget(generateFragmentLayout),
//            TextFieldWidget(fragmentPackageName),
//            CheckBoxWidget(needRepository),
//            TextFieldWidget(repositoryPackageName),
//            CheckBoxWidget(needViewModel),
//            TextFieldWidget(viewModelPackageName),
            LanguageWidget()
        )

//        thumb { File("template_blank_activity.png") }

        recipe = { data: TemplateData ->
            mvvmRecipe(
                data as ModuleTemplateData,
                pageName.value,
                packageName.value,
                path = path.value,
                needUserAct = needUserAct.value,
                keyUser = keyUser.value,
                hasUserCreation = hasUserCreation.value,
                needAuditAct = needAuditAct.value,
                keyAudit = keyAudit.value,
                hasAuditCreation = hasAuditCreation.value,
                needManageAct = needManageAct.value,
                keyManage = keyManage.value,
                hasManageCreation = hasManageCreation.value,
                keyCreation = keyCreation.value,
                keyStatusList = keyStatusList.value,
                keyAdvanceSearch = keyAdvanceSearch.value,
//                needActivity.value,
//                activityLayoutName.value,
//                generateActivityLayout.value,
//                activityPackageName.value,
//                needFragment.value,
//                fragmentLayoutName.value,
//                generateFragmentLayout.value,
//                fragmentPackageName.value,
//                needRepository.value,
//                needViewModel.value,
//                repositoryPackageName.value,
//                viewModelPackageName.value
            )
        }
    }