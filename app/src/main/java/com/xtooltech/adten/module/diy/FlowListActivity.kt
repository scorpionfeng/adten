package com.xtooltech.adten.module.diy

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFlowListBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter
import kotlin.experimental.and

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

    var datas= mutableListOf<FlowItem>(
//        FlowItem(0,"车速",false,"","KM"),
//        FlowItem(1,"转速",false,"","RPM"),
//        FlowItem(2,"MAF (空气质量浏览) 空气流速",false,"","KM"),
//        FlowItem(3,"发动机冷却液温度",false,"",".C")
    )
}


data class FlowItem(var kind:Byte,var title:String,var selected:Boolean,var content:String,var symbol:String)


@Route(path = PATH_DIY_FLOW)
class FlowListActivity : BaseVMActivity<ActivityFlowListBinding, FlowListViewModel>() {

    private lateinit  var  hud: KProgressHUD
    private lateinit var handler: Handler

    val adapter= UniversalAdapter(vm.datas,R.layout.item_flow,BR.model)


    var maskBuffer = ShortArray(32)

    private fun getData() {
        UtilThread.execute{
            var enterSucc = ObdManger.getIns().enter()
            printMessage("entersucc ?= $enterSucc")
            enterSucc?.trueLet {


                var datas: MutableList<ByteArray?> = supportItem(0)

                datas?.apply {

                    mergePid(datas, maskBuffer, ObdManger.getIns().computerOffset())
                    var produPid = produPid(maskBuffer)

                    produPid.forEachIndexed { index, sh ->
                        when(sh){
                            0x0c.toShort()->vm.datas.add(FlowItem(0x0c,"发动机转数",false,"","RPM "))
                            0x0d.toShort()->vm.datas.add(FlowItem(0x0d,"车速",false,"","km/h"))
                            0x03.toShort()->vm.datas.add(FlowItem(0x03,"燃油系统1状态",false,""," "))
                            0x04.toShort()->vm.datas.add(FlowItem(0x04,"负荷计算值",false,"","% "))
                            0x05.toShort()->vm.datas.add(FlowItem(0x05,"冷却液温度",false,"","°C "))
                            0x07.toShort()->vm.datas.add(FlowItem(0x07,"长期燃油修正(缸组1)",false,"","% "))
                            0x06.toShort()->vm.datas.add(FlowItem(0x06,"短期燃油修正(缸组1)",false,"","% "))
                            0x14.toShort()->vm.datas.add(FlowItem(0x14,"氧传感器输出电压(缸组1,传感器1)",false,"","v "))
                            0x15.toShort()->vm.datas.add(FlowItem(0x15,"氧传感器输出电压(缸组1,传感器2)",false,"","v "))
                            0x0e.toShort()->vm.datas.add(FlowItem(0x0e,"点火提前角",false,"","° "))
                            0x0f.toShort()->vm.datas.add(FlowItem(0x0f,"进气温度",false,"","°C "))
                            0x10.toShort()->vm.datas.add(FlowItem(0x10,"进气流量",false,"","g/s "))
                            0x11.toShort()->vm.datas.add(FlowItem(0x11,"节气门绝对位置",false,"","% "))
                            0x13.toShort()->vm.datas.add(FlowItem(0x13,"氧传感器位置",false,""," "))
                            0x1c.toShort()->vm.datas.add(FlowItem(0x1c,"车辆或发动机认证的OBD要求",false,""," "))
                            0x21.toShort()->vm.datas.add(FlowItem(0x21,"MIL(故障指示灯)点亮后的行驶距离",false,"","km "))
                        }
                    }
                }
            }

            handler.post{

                adapter.notifyDataSetChanged()
                toast("succe ? ="+enterSucc)
                hud.dismiss()

            }
        }
    }




    override fun initView() {

        handler=Handler(Looper.getMainLooper())

        hud= KProgressHUD(this).setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在加载...")
        hud.show()


        getData()



        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{bind,item,index->
            item.selected=!item.selected
            vm.datas[index].selected=item.selected
            bind.setVariable(BR.model,item)
        }


    }



    private fun mergePid(
        data: List<ByteArray?>?,
        maskBuffer: ShortArray
    ) {
        for (index in 0 until data?.size!!) {
            var inItem = data[index]
            if (inItem?.get(6) == 0x41.toByte()) {
                if (inItem.size > 2) {
                    maskBuffer[index * 4 + 0] = inItem.get(8).toShort()
                }
                if (inItem.size > 3) {
                    maskBuffer[index * 4 + 1] = inItem.get(9).toShort()
                }
                if (inItem.size > 4) {
                    maskBuffer[index * 4 + 2] = inItem.get(10).toShort()
                }
                if (inItem.size > 5) {
                    maskBuffer[index * 4 + 3] = (inItem.get(11).toShort() and 0xFE)
                    if (inItem[5].toShort() and 0x01 === 0.toShort()) break
                }
            }
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
