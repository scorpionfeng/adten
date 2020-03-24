package com.xtooltech.adten.module.diy

import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFlowSmokeBinding
import com.xtooltech.adten.util.PATH_DIY_SMOKE
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter


class SmokeViewModel : ViewModel() {
    var datas = mutableListOf<FlowItem>(
//        FlowItem(0x01, "MIL灯(故障指示灯)状态",true,"","灯亮/灯灭"),
        FlowItem(0x03, "故障码", true, "", "个"),
        FlowItem(0x07, "未决故障码", true, "", "个"),
        FlowItem(0x0a, "永久故障码", true, "", "个")

//        FlowItem(0x4e,"失火监测",true,"","XX公里"),
//        FlowItem(0x1f,"燃油系统监测",true,"","XX小时/XX分钟/XX秒"),
//        FlowItem(0x1f,"综合成分监测",true,"","XX小时/XX分钟/XX秒"),
//        FlowItem(0x1f,"EGR和/或VVT系统",true,"","XX小时/XX分钟/XX秒")
    )
}


@Route(path = PATH_DIY_SMOKE)
class SmokeActivity : BaseVMActivity<ActivityFlowSmokeBinding, SmokeViewModel>() {

    val handler: Handler = Handler()
    lateinit var datas: MutableList<FlowItem>

    var show = true

    var enterSucc: Boolean? = false

    lateinit var adapter: UniversalAdapter<FlowItem>


    override fun initView() {

        datas = vm.datas

        adapter = UniversalAdapter(datas, R.layout.item_flow_detail, BR.model)
        vb.list.adapter = adapter
        vb.list.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClick { _, item, _ ->
            request_obd_state(item)
        }

       // handler.postDelayed(speedRunnable, 1000L)


    }

    private fun request_obd_state(item: FlowItem) {
        printMessage("request ${item.title}")
        Thread {

            val amount = ObdManger.getIns().readTrobleCodeAmount(item.kind)
                item.content = amount.toString()
                printMessage("kind=${item.kind}>" + amount)

                handler.post {
                    adapter.notifyDataSetChanged()
                }

        }.start()
    }


    private var speedRunnable = object : Runnable {


        override fun run() {
            Thread{

                datas.forEach {
                    val value = ObdManger.getIns().readTrobleCodeAmount(it.kind)
                    printMessage("kind= ${it.kind}>" + value)
                    it.content = value.toString()
                }

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }

            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        show = false
    }


    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_flow_smoke

    override fun getBindingId(): Int = BR.model
    fun smokeQuery(view: View) {
        Thread{
            var queryMilState = ObdManger.getIns().queryMilState()

            handler.post {
                toast("mil stat= "+if(queryMilState.first)"故障灯亮" else "故障灯灭" +" and 就绪状态是:${queryMilState.second}")
            }

        }.start()
    }

    fun clickClear(view: View) {
        Thread{

            val value = ObdManger.getIns().clearCode()

            handler.post {
                toast("清码= "+if(value) "成功"  else "失败")
            }

        }.start()
    }
}
