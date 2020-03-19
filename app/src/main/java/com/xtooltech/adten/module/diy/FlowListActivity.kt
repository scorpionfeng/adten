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
import com.xtooltech.adten.common.obd.DataStreamItem_DataType_STD
import com.xtooltech.adten.common.obd.Freeze_DataType_STD
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

    var datas= mutableListOf<DataStreamItem_DataType_STD>(
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


                var datas: MutableList<ByteArray?> = supportItem()

                datas?.apply {

                    mergePid(datas, maskBuffer, ObdManger.getIns().computerOffset())
                    var produPid = produPid(maskBuffer)
                    var freezeKeyList = dataFlowKeyList(produPid)
                    freezeKeyList?.apply {
                        vm.datas.addAll(freezeKeyList)

                        runOnUiThread{
                            adapter.notifyDataSetChanged()
                            hud.dismiss()
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

    fun readItem(item:DataStreamItem_DataType_STD){

        Thread{

            val value = ObdManger.getIns()
                .readFlowState(item.dsCMD.array, item.dsID.binaryToCommand())
            item.dsValue = value

            runOnUiThread {
                adapter.notifyDataSetChanged()
                toast("${item.dsID.binaryToCommand()} value= "+item.dsValue)
            }

        }.start()




    }

    fun readFlow(view: View) {
        UtilThread.execute {
            vm.datas.forEach {
                val value = ObdManger.getIns()
                    .readFlowState(it.dsCMD.array, it.dsID.binaryToCommand())
                it.dsValue = value

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
//                val value = ObdManger.getIns()
//                    .readFlowState(listOf(0x01,0x0D), vm.datas[3].dsID.binaryToCommand())
//                     vm.datas[3].dsValue = value
//
//            printMessage("value= "+value)

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
//                val value = ObdManger.getIns()
//                    .readFlowState(vm.datas[67].dsCMD.array, vm.datas[67].dsID.binaryToCommand())
//            vm.datas[67].dsValue = value
//
//                runOnUiThread {
//                    adapter.notifyDataSetChanged()
//                }

        }
    }
}
