package com.xtooltech.adten.module.diy

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFlowListBinding
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.adtenx.util.toObdIndex
import com.xtooltech.adtenx.util.toObdPid
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter

class FlowListViewModel : ViewModel() {



    companion object{
        private lateinit var instance: FlowListViewModel

       fun setIns(ins:FlowListViewModel){
           instance=ins
       }

        fun getIns():FlowListViewModel{
            return instance
        }
    }

    var datas= mutableListOf<ObdItem>(
    )
}


data class FlowItem(var kind:Byte,var title:String,var selected:Boolean,var content:String,var symbol:String)

@Route(path = PATH_DIY_FLOW)
class FlowListActivity : BaseVMActivity<ActivityFlowListBinding, FlowListViewModel>() {

    private lateinit  var  hud: KProgressHUD
    private lateinit var handler: Handler
    val adapter= UniversalAdapter(vm.datas,R.layout.item_flow,BR.model)

    private fun getData() {
        Thread {
            val list = ObdManger.getIns().queryFlowListItem()
            list.apply {
                vm.datas.addAll(list.toMutableList())
            }
            Log.i("Communication","数据流共${vm.datas.size}条")

            handler.post {

                adapter.notifyDataSetChanged()
                hud.dismiss()
            }
        }.start()
    }


    override fun initView() {

        handler=Handler(Looper.getMainLooper())

        hud= KProgressHUD(this).setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在加载...")
        if (!hud.isShowing) {
            hud.show()
        }


        getData()

        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{bind,item,index->
            readItem(item)
        }
    }

    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.activity_flow_list

    override fun getBindingId(): Int = BR.model

    fun nextStep(view: View) {
        FlowListViewModel.setIns(vm)
        ARouter.getInstance().build(PATH_DIY_FLOW_DETAIL).navigation()

    }

    fun readItem(item:ObdItem){

        Thread{

            val value = ObdManger.getIns().readFlowItem(item)
            item.content=value

            runOnUiThread {
                adapter.notifyDataSetChanged()
                toast("${item.index} value= "+item.content)
            }

        }.start()
    }

    fun readFlow(view: View) {
        Thread {
            vm.datas.forEach {

                val value = ObdManger.getIns().readFlowItem(it)
                it.content=value

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    toast("${it.index} value= "+it.content)
                }

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }.start()
    }
}
