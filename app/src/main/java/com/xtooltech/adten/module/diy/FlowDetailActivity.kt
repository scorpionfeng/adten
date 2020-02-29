package com.xtooltech.adten.module.diy

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adten.common.ble.BleManger
import com.xtooltech.adten.databinding.ActivityFlowDetailBinding
import com.xtooltech.adten.util.PATH_DIY_FLOW_DETAIL
import com.xtooltech.adten.util.UtilThread
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter
import kotlinx.android.synthetic.main.item_flow_detail.view.*


@Route(path = PATH_DIY_FLOW_DETAIL)
class FlowDetailActivity : BaseVMActivity<ActivityFlowDetailBinding, FlowListViewModel>() {

    val handler: Handler= Handler()
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


            if (datas[0].kind==0) {
               UtilThread.execute(speedRunnable)
            }


    }


    private var speedRunnable= object :Runnable {


        override fun run() {
            UtilThread.execute{

                datas.forEach {
                    when(it.kind){
                        0->{
                            val speedContent = BleManger.getIns().readSpeed()
                            datas[0].content=speedContent
                        }
                        1->{
                            val rpmContent =BleManger.getIns().readRpm()
                            datas[1].content=rpmContent
                        }
                        3->{
                            val temperContent =BleManger.getIns().readTemper()
                            datas[3].content=temperContent
                        }
                    }
                }

                runOnUiThread{
                    adapter.notifyDataSetChanged()
                    handler.postDelayed(this,500)

                }
            }
        }
    }


    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_flow_detail

    override fun getBindingId(): Int = BR.model
}
