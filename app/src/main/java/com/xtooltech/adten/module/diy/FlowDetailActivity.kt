package com.xtooltech.adten.module.diy

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adten.databinding.ActivityFlowDetailBinding
import com.xtooltech.adten.util.PATH_DIY_FLOW_DETAIL
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter


@Route(path = PATH_DIY_FLOW_DETAIL)
class FlowDetailActivity : BaseVMActivity<ActivityFlowDetailBinding, FlowListViewModel>() {

    val destVm=FlowListViewModel.getIns()

    val datas=destVm.datas.filter { it.selected }

    lateinit var  adapter:UniversalAdapter<FlowItem>

    override fun bindActivity() {

        vb.setVariable(bindId,destVm)
        vb.lifecycleOwner=this
    }


    override fun initView() {


        datas.forEach{
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter=UniversalAdapter(datas,R.layout.item_flow_detail,BR.model)
        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)


    }

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_flow_detail

    override fun getBindingId(): Int = BR.model
}
