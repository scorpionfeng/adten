package com.xtooltech.adten.module.diy

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adten.common.ble.BleManger
import com.xtooltech.adten.databinding.ActivityFlowListBinding
import com.xtooltech.adten.util.PATH_DIY
import com.xtooltech.adten.util.PATH_DIY_FLOW
import com.xtooltech.adten.util.PATH_DIY_FLOW_DETAIL
import com.xtooltech.adten.util.UtilThread
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter

class FlowListViewModel : ViewModel() {
     var datas= listOf<FlowItem>(
        FlowItem(0,"车速",false,"","KM"),
        FlowItem(1,"转速",false,"","RPM"),
        FlowItem(2,"MAF (空气质量浏览) 空气流速",false,"","KM"),
        FlowItem(3,"发动机冷却液温度",false,"",".C")
    )

    companion object{
        private lateinit var instance: FlowListViewModel

       fun setIns(ins:FlowListViewModel){
           instance=ins
       }

        fun getIns():FlowListViewModel{
            return instance
        }
    }
}


data class FlowItem(var kind:Int,var title:String,var selected:Boolean,var content:String,var symbol:String)


@Route(path = PATH_DIY_FLOW)
class FlowListActivity : BaseVMActivity<ActivityFlowListBinding, FlowListViewModel>() {

    private lateinit  var  hud: KProgressHUD
    private lateinit var handler: Handler

    val adapter= UniversalAdapter(vm.datas,R.layout.item_flow,BR.model)

    override fun initView() {

        handler=Handler(Looper.getMainLooper())

        hud= KProgressHUD(this).setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在加载...")
        hud.show()


        UtilThread.execute{
            var enterSucc = BleManger.getIns().enter()
            printMessage("entersucc ?= $enterSucc")

            handler.post{

                toast("succe? ="+enterSucc)
                hud.dismiss()

            }
        }

        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{bind,item,index->
            item.selected=!item.selected
            vm.datas[index].selected=item.selected
            bind.setVariable(BR.model,item)
        }


    }

    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.activity_flow_list

    override fun getBindingId(): Int = BR.model

    fun selectAll(view: View) {
        vm.datas.forEach {
            it.selected=true
        }
        adapter.notifyDataSetChanged()
    }

    fun nextStep(view: View) {

        FlowListViewModel.setIns(vm)
        ARouter.getInstance().build(PATH_DIY_FLOW_DETAIL).navigation()

    }
}
