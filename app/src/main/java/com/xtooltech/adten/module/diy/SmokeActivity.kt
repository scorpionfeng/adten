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
import com.xtooltech.adten.util.trueLet
import com.xtooltech.adtenx.util.parse2BizSingle
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter
import kotlin.experimental.and


var bin_zero:Byte=1//0000 0001
var bin_one:Byte=2//0000 0010
var bin_two:Byte=4//0000 0100
var bin_three:Byte=8//0000 1000
var bin_four:Byte=16//0001 0000
var bin_five:Byte=32//0010 0000
var bin_six:Byte=64//0100 0000
var bin_seven:Byte=128.toByte()//1000 0000

class SmokeViewModel : ViewModel() {
    var datas = mutableListOf<FlowItem>(
//        FlowItem(0x01, "MIL灯(故障指示灯)状态",true,"","灯亮/灯灭"),
        FlowItem(0x03, "故障码", true, "", "个"),
        FlowItem(0x07, "未决故障码", true, "", "个"),
        FlowItem(0x0a, "永久故障码", true, "", "个")
    )

    var dataReady= mutableListOf<ReadyItem>()
}
data class ReadyItem(var name:String,var title:String,var statu:String,var kind:Int)

@Route(path = PATH_DIY_SMOKE)
class SmokeActivity : BaseVMActivity<ActivityFlowSmokeBinding, SmokeViewModel>() {

    val handler: Handler = Handler()
    lateinit var datas: MutableList<FlowItem>
    lateinit var dataReady: MutableList<ReadyItem>

    var show = true

    var enterSucc: Boolean? = false

    lateinit var adapter: UniversalAdapter<FlowItem>
    lateinit var adapterReady: UniversalAdapter<ReadyItem>


    override fun initView() {

        datas = vm.datas
        dataReady=vm.dataReady

        adapter = UniversalAdapter(datas, R.layout.item_flow_detail, BR.model)
        vb.list.adapter = adapter
        vb.list.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClick { _, item, _ ->
            request_obd_state(item)
        }

        adapterReady = UniversalAdapter(dataReady, R.layout.item_freeze_ready, BR.modelReady)
        vb.ready.adapter = adapterReady
        vb.ready.layoutManager = LinearLayoutManager(this)

       // handler.postDelayed(speedRunnable, 1000L)


    }

    private fun request_obd_state(item: FlowItem) {
        printMessage("request ${item.title}")
        Thread {

            val amount = ObdManger.getIns().readTrobleCodeAmount(item.kind)
                item.content = amount.first.toString()
                printMessage("kind=${item.kind}>" + amount.first+"codes= "+amount.second)

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
                    printMessage("kind= ${it.kind}>" + value.first)
                    it.content = value.first.toString()
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

        Thread {

            var queryMilState = ObdManger.getIns().queryMilState()

            printMessage("故障灯状态和就绪状态 ${queryMilState.first}")

            var readStateData = queryMilState.second

            readStateData.isNotEmpty().trueLet {

                parseReadyState(readStateData)

                handler.post {
                    adapterReady.notifyDataSetChanged()
                    toast("mil stat= " + if (queryMilState.first) "故障灯亮" else "故障灯灭" + " and 就绪状态是:${queryMilState.second}")
                }

            }

        }.start()
    }

    private fun parseReadyState(rawData: List<Byte>) {

        var data= listOf(
            ReadyItem("MIS_SUP","失火状态 Misfire monitoring",(if(rawData[3].and(bin_zero)>0) "ON" else "OFF"),0),
            ReadyItem("FUEL_SUP","燃油监视 fuel system monitoring",(if(rawData[3].and(bin_one)>0) "ON" else "OFF"),0),
            ReadyItem("CCM_SUP","综合部件监视 CCM",(if(rawData[3].and(bin_two)>0) "ON" else "OFF"),0),
            ReadyItem("TypeIgn","点火类型",(if(rawData[3].and(bin_three)>0) "汽油" else "柴油"),0),
            ReadyItem("MIS_RDY","失火监视",(if(rawData[3].and(bin_four)>0) "ON" else "OFF"),0),
            ReadyItem("FUEL_RDY","燃油系统监视",(if(rawData[3].and(bin_five)>0) "ON" else "OFF"),0),
            ReadyItem("CCM_RDY","综合部件监视",(if(rawData[3].and(bin_six)>0) "ON" else "OFF"),0),
            ReadyItem("Reserved1","Reserved1",(if(rawData[3].and(bin_seven)>0) "ON" else "OFF"),0),
            ReadyItem("CAT_SUP","催化器监视",(if(rawData[4].and(bin_zero)>0) "ON" else "OFF"),1),
            ReadyItem("HCAT_SUP","加热式催化器监视",(if(rawData[4].and(bin_one)>0) "ON" else "OFF"),1),
            ReadyItem("EVAP_SUP","燃油蒸气系统监视",(if(rawData[4].and(bin_two)>0) "ON" else "OFF"),1),
            ReadyItem("AIR_SUP","二次空气喷射监视",(if(rawData[4].and(bin_three)>0) "ON" else "OFF"),1),
            ReadyItem("SIgn.Reserved1","二次空气喷射监视",(if(rawData[4].and(bin_four)>0) "ON" else "OFF"),1),
            ReadyItem("O2S_SUP","氧传感器监视",(if(rawData[4].and(bin_five)>0) "ON" else "OFF"),1),
            ReadyItem("HTR_SUP","氧传感器加热监视",(if(rawData[4].and(bin_six)>0) "ON" else "OFF"),1),
            ReadyItem("EGR_RDY","废气再循环监视",(if(rawData[4].and(bin_seven)>0) "ON" else "OFF"),1),
            ReadyItem("HCCATSUP","催化剂监视",(if(rawData[4].and(bin_zero)>0) "ON" else "OFF"),2),
            ReadyItem("NCAT_SUP","氮氧化物后处理监视",(if(rawData[4].and(bin_one)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved1","CIgn.Reserved1",(if(rawData[4].and(bin_two)>0) "ON" else "OFF"),2),
            ReadyItem("BP_SUP","增压系统监视",(if(rawData[4].and(bin_three)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved2","CIgn.Reserved2",(if(rawData[4].and(bin_four)>0) "ON" else "OFF"),2),
            ReadyItem("EGS_SUP","废气传感器监视",(if(rawData[4].and(bin_five)>0) "ON" else "OFF"),2),
            ReadyItem("PM_SUP","颗粒物补集器监视",(if(rawData[4].and(bin_six)>0) "ON" else "OFF"),2),
            ReadyItem("EGR_SUP","废气再循环监视",(if(rawData[4].and(bin_seven)>0) "ON" else "OFF"),2),
            ReadyItem("HCCATSUP","NMHC催化剂监视",(if(rawData[5].and(bin_zero)>0) "ON" else "OFF"),2),
            ReadyItem("NCAT_RDY","氮氧化物后处理监视",(if(rawData[5].and(bin_one)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved3","---",(if(rawData[5].and(bin_two)>0) "ON" else "OFF"),2),
            ReadyItem("BP_RDY","增压系统监视",(if(rawData[5].and(bin_three)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved4","---",(if(rawData[5].and(bin_four)>0) "ON" else "OFF"),2),
            ReadyItem("EGS_RDY","废气传感器监视",(if(rawData[5].and(bin_five)>0) "ON" else "OFF"),2),
            ReadyItem("PM_RDY","颗粒物补集器监视",(if(rawData[5].and(bin_six)>0) "ON" else "OFF"),2),
            ReadyItem("EGR_RDY","废气再循环监视",(if(rawData[5].and(bin_seven)>0) "ON" else "OFF"),2)
        )

        dataReady.addAll(data)


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
