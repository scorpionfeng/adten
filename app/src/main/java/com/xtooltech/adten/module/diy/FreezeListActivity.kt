package com.xtooltech.adten.module.diy
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.common.obd.*
import com.xtooltech.adten.databinding.ActivityFreezeListBinding
import com.xtooltech.adten.databinding.ActivityFreezeListBindingImpl
import com.xtooltech.adten.util.PATH_DIY_FREEZE
import com.xtooltech.adten.util.UtilThread
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter
import java.util.*
import kotlin.experimental.and



class FreezeListViewModel : ViewModel() {



    companion object{
        private lateinit var instance: FreezeListViewModel

       fun setIns(ins:FreezeListViewModel){
           instance=ins
       }

        fun getIns():FreezeListViewModel{
            return instance
        }
    }

    var datas= mutableListOf<FlowItem>(
        FlowItem(0,"车速",false,"","KM"),
        FlowItem(1,"转速",false,"","RPM"),
        FlowItem(2,"MAF (空气质量浏览) 空气流速",false,"","KM"),
        FlowItem(3,"发动机冷却液温度",false,"",".C")
    )
}




@Route(path = PATH_DIY_FREEZE)
class FreezeListActivity : BaseVMActivity<ActivityFreezeListBinding, FreezeListViewModel>() {

    private lateinit  var  hud: KProgressHUD
    private lateinit var handler: Handler

    val adapter= UniversalAdapter(vm.datas,R.layout.item_flow,BR.model)


    private fun getData() {
        UtilThread.execute{
            var enterSucc = ObdManger.getIns().enter()
            printMessage("entersucc ?= $enterSucc")
            enterSucc?.trueLet {

                /** 读取冻结帧
                 * 0x02,0x02,0x00
                 * */
                val obdData= ByteArray(3)
                obdData[0]=0x02
                obdData[1]=0x02
                obdData[2]=0x00

                var comboCommand = ObdManger.getIns().comboCommand(obdData)
                var data = comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it,5000,10) }

                printMessage("freeze data >>>"+Utils.debugByteData(data?.get(0)))

                //60 0A 0B 41 6B 10 42 00 00 7E 58 00 00 D1

                var receiveData = data?.get(0)?.filterIndexed { index, _ -> index > ObdManger.getIns().computerOffset()-1 }

                val freezeSysIDRet =  ArrayList<Freeze_DataType_STD>()
                receiveData?.let {
                    if (it[0] == 0x42.toByte() && (it[3] !== 0.toByte() || it[4] !== 0.toByte())) {
                        val maskBuffer = ShortArray(32)
                        for (m in maskBuffer.indices) {
                            maskBuffer[m] = 0
                        }
                        for (i in 0..7) {
                            val obdData2= ByteArray(3)
                            obdData2[0]=0x02
                            obdData2[1]= (i*0x20.toByte()).toByte()
                            obdData2[2]=0x00
                            var comboCommand2 = ObdManger.getIns().comboCommand(obdData2)
                            var data2 = comboCommand2?.let {
                                ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 1)
                            }
                            var filterData2= data2?.get(0)?.filterIndexed{index,_->index>ObdManger.getIns().computerOffset()-1}

                            if(filterData2?.size!! >0){
                                if (filterData2[0] == 0x42.toByte()) {
                                    if (filterData2.size > 3) {
                                        maskBuffer[i * 4 + 0] = filterData2[3].toShort()
                                    }
                                    if (filterData2.size > 4) {
                                        maskBuffer[i * 4 + 1] = filterData2[4].toShort()
                                    }
                                    if (filterData2.size> 5) {
                                        maskBuffer[i * 4 + 2] = filterData2[5].toShort()
                                    }
                                    if (filterData2.size > 6) {
                                        maskBuffer[i * 4 + 3] = filterData2[6].toShort()
                                        if (filterData2[6] and 0x01.toByte() == 0.toByte()) {
                                            break
                                        }
                                    }
                                }
                            }
                        }


                        val pidData = DataArray()
                        for (p in 0..31) {
                            for (pj in 1..8) {
                                if (maskBuffer[p] and 0x80.toShort() != 0.toShort()) {
                                    pidData.add((p * 8 + pj).toShort())
                                }
                                maskBuffer[p] = (maskBuffer[p].toInt() shl 1).toShort()
                            }
                        }
                        var O2SLocId: Short = 0
                        for (o in 0 until pidData.length()) {
                            if (pidData.get(o) === 0x13.toShort()) {
                                O2SLocId = 0x13
                                break
                            }
                            if (pidData.get(o) === 0x1D.toShort()) {
                                O2SLocId = 0x1D
                                break
                            }
                        }
                        val tempArray =ArrayList<String>()
                        for (t in 0 until pidData.length()) {
                            val tempPidData: Short = pidData.get(t)
                            if (tempPidData >= 0x14 && tempPidData <= 0x1B
                                || tempPidData >= 0x24 && tempPidData <= 0x2B
                                || tempPidData >= 0x34 && tempPidData <= 0x3B
                            ) {
                                if (O2SLocId.toInt() == 0x13) {
                                    tempArray.add(
                                        String.format(
                                            "0x00,0x00,0x00,0x00,0x00,0x%02X",
                                            tempPidData
                                        )
                                    )
                                } else if (O2SLocId.toInt() == 0x1D) {
                                    tempArray.add(
                                        String.format(
                                            "0x00,0x00,0x00,0x00,0x02,0x%02X",
                                            tempPidData
                                        )
                                    )
                                }
                            } else {
                                tempArray.add(
                                    String.format(
                                        "0x00,0x00,0x00,0x00,0x00,0x%02X",
                                        tempPidData
                                    )
                                )
                            }
                            if (tempPidData >= 0x14 && tempPidData <= 0x1B
                                || tempPidData >= 0x24 && tempPidData <= 0x2B
                                || tempPidData >= 0x34 && tempPidData <= 0x3B
                            ) {
                                if (O2SLocId.toInt() == 0x13) {
                                    tempArray.add(
                                        String.format(
                                            "0x00,0x00,0x00,0x00,0x01,0x%02X",
                                            tempPidData
                                        )
                                    )
                                }
                                if (O2SLocId.toInt() == 0x1D) {
                                    tempArray.add(
                                        String.format(
                                            "0x00,0x00,0x00,0x00,0x03,0x%02X",
                                            tempPidData
                                        )
                                    )
                                }
                            } else if (tempPidData.toInt() == 0x03) {
                                tempArray.add(
                                    String.format(
                                        "0x00,0x00,0x00,0x00,0x01,0x%02X",
                                        tempPidData
                                    )
                                )
                            } else if (tempPidData.toInt() == 0x41) {
                                for (pid41 in 1..21) {
                                    tempArray.add(
                                        String.format(
                                            "0x00,0x00,0x00,0x00,0x%02X,0x%02X",
                                            pid41 + 0x70,
                                            tempPidData
                                        )
                                    )
                                }
                            }
                        }


                        for (ta in tempArray.indices) {
                            val ds: DS_File = DataBaseBin.searchDS(tempArray[ta])
                            if (ds.isSearch()) {
                                val searchData: DataArray = ds.dsCommand()
                                val cmdBuffer = ShortArray(3)
                                cmdBuffer[0] = 0x02
                                cmdBuffer[1] = searchData.get(1)
                                cmdBuffer[2] = 0x00
                                val unit: String = CurrentData.unitChoose(ds.dsUnit())
                                val freezeItem = Freeze_DataType_STD()
                                freezeItem.setFreezeID(tempArray[ta])
                                freezeItem.setFreezeName(ds.dsName())
                                freezeItem.setFreezeUnit(unit)
                                freezeItem.setFreezeCommand(DataArray(cmdBuffer, 3))
                                freezeSysIDRet.add(freezeItem)
                            }
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





    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.activity_freeze_list

    override fun getBindingId(): Int = BR.model


}
