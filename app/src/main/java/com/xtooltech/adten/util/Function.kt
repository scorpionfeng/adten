package com.xtooltech.adten.util

import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.common.obd.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.experimental.and


const val DS_SUPPORT = 0x01 // ֧�ָ������

// ����������
private val supportDSItems = HashMap<String, DataStreamItem_DataType_STD>()
var isSupportAirFlow = false
var isSuppDataStream = false
private var dsSupportMap: MutableMap<Short, java.util.ArrayList<DataStreamItem_DataType_STD>> =
    HashMap()

fun produPid(data: ShortArray): List<Short> {
    val array = ArrayList<Short>()
    for (i in 0..31) {
        for (j in 1..8) {
            if (data[i] and 0x80 != 0.toShort()) {
                val temp = (i * 8 + j).toShort()
                if (temp.toInt() != 0x01 && temp.toInt() != 0x20 && temp.toInt() != 0x40
                    && temp < 0x4F
                ) {
                    array.add(temp)
                }
            }
            data[i] = (data[i].toInt() shl 1).toShort()
        }
    }
    return array
}

fun produFreezePid(data: ShortArray): List<Short> {
    val array = ArrayList<Short>()

    for (p in 0..31) {
        for (pj in 1..8) {
            if (data[p].toInt() and 0x80 != 0) {
                array.add((p * 8 + pj).toShort())
            }
            data[p] = (data.get(p).toInt() shl 1).toShort()
        }
    }
    return array
}


fun dataFlowKeyList(pidData: List<Short>): MutableList<DataStreamItem_DataType_STD> {
    var result = mutableListOf<DataStreamItem_DataType_STD>()
    var O2SLocId: Short = 0
    val pidLength: Int = pidData.size
    for (p in 0 until pidLength) {
        if (pidData[p] === 0x13.toShort()) {
            O2SLocId = 0x13
            break
        }
        if (pidData[p] === 0x1D.toShort()) {
            O2SLocId = 0x1D
            break
        }
    }
    // dsidArray init
    // dsidArray init
    val length2: Int = pidData.size
    val dsIDArray = Frame()
    for (p in 0 until length2) {
        val pid = (pidData[p] and 0xFF)
        if (pid >= 0x14 && pid <= 0x1B
            || pid >= 0x24 && pid <= 0x2B
            || pid >= 0x34 && pid <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x00,0x%02X", pid
                    )
                )
            } else if (O2SLocId.toInt() == 0x1D) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x02,0x%02X", pid
                    )
                )
            }
        } else {
            dsIDArray.add(
                String.format(
                    "0x00,0x00,0x00,0x00,0x00,0x%02X", pid
                )
            )
        }
        if (pid >= 0x14 && pid <= 0x1B
            || pid >= 0x24 && pid <= 0x2B
            || pid >= 0x34 && pid <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x01,0x%02X", pid
                    )
                )
            } else if (O2SLocId.toInt() == 0x1D) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x03,0x%02X", pid
                    )
                )
            }
        } else if (pid.toInt() == 0x03) {
            dsIDArray.add(
                String.format(
                    "0x00,0x00,0x00,0x00,0x01,0x%02X", pid
                )
            )
        } else if (pid.toInt() == 0x41) {
            for (n in 1..21) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x%02X,0x%02X", n + 0x70,
                        pid
                    )
                )
            }
        }
    }
    TimeUnit.SECONDS.sleep(1)
    // pidData初始化完成

    // pidData初始化完成
    for (a in 0 until dsIDArray.count()) {
        val dsIDDataArray = dsIDArray[a]
        val dsItem = DataStreamItem_DataType_STD(
            dsIDDataArray
        )
        dsItem.supportType = DS_SUPPORT
        val ds_file = DataBaseBin.searchDS(dsIDDataArray)
        if (ds_file != null) {
            dsItem.dsName = ds_file.dsName()
            val dsUnit = ds_file.dsUnit()
            dsItem.dsUnit = CurrentData.unitChoose(dsUnit)
            dsItem.dsCMD = ds_file.dsCommand()
            result.add(dsItem)
            result.add(dsItem)
            supportDSItems.put(dsIDDataArray.binaryToCommand(), dsItem)
        }
        if ("0x00,0x00,0x00,0x00,0x00,0x10".equals(
                dsIDDataArray.binaryToCommand(),
                ignoreCase = true
            )
        ) {
            isSupportAirFlow = true
        }
    }
    if (result.size > 0) {
        isSuppDataStream = true
    }

    return result
}


fun freezeKeyList(pidData: List<Short>): MutableList<Freeze_DataType_STD> {

    var freeseItem: MutableList<Freeze_DataType_STD> = mutableListOf()

    var O2SLocId: Short = 0
    for (o in 0 until pidData.size) {
        if (pidData[o] === 0x13.toShort()) {
            O2SLocId = 0x13
            break
        }
        if (pidData[o] === 0x1D.toShort()) {
            O2SLocId = 0x1D
            break
        }
    }

    val tempArray = java.util.ArrayList<String>()
    for (element in pidData) {
        val tempPidData = element
        if (tempPidData >= 0x14 && tempPidData <= 0x1B
            || tempPidData >= 0x24 && tempPidData <= 0x2B
            || tempPidData >= 0x34 && tempPidData <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x00,0x%02X", tempPidData))
            } else if (O2SLocId.toInt() == 0x1D) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x02,0x%02X", tempPidData))
            }
        } else {
            tempArray.add(String.format("0x00,0x00,0x00,0x00,0x00,0x%02X", tempPidData))
        }
        if (tempPidData >= 0x14 && tempPidData <= 0x1B
            || tempPidData >= 0x24 && tempPidData <= 0x2B
            || tempPidData >= 0x34 && tempPidData <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x01,0x%02X", tempPidData))
            }
            if (O2SLocId.toInt() == 0x1D) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x03,0x%02X", tempPidData))
            }
        } else if (tempPidData.toInt() == 0x03) {
            tempArray.add(String.format("0x00,0x00,0x00,0x00,0x01,0x%02X", tempPidData))
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
            freeseItem.add(freezeItem)
        }
    }

    return freeseItem

}


fun supportFreeze(): MutableList<ByteArray?> {
    var query = true
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(3)
        obdData[0] = 0x02
        obdData[1] = pid1
        obdData[2] = 0x00

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data =
            comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }
        data?.apply {

            takeIf { data?.size > 0 }?.apply {
                var item = data[0]
                datas.add(item)
                var flag = item?.get(3 + item[3])
                var result = flag?.and(0x01)
                if (result == 0x01.toByte()) {
                    pid1 = (pid1 + 0x20).toByte()
                } else {
                    query = false
                }
            }

        }
    }
    return datas
}

fun supportItem(flag: Int): MutableList<ByteArray?> {
    var query: Boolean = true
    var size = if (flag == 0) 2 else 376
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(size)
        obdData[0] = 0x01
        obdData[1] = pid1
        if (flag == 1) {
            obdData[2] = 0x00
        }

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data =
            comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }
        data?.apply {
            var item = data[0]
            datas.add(item)
            var flag = item?.get(3 + item[3])
            var result = flag?.and(0x01)
            if (result == 0x01.toByte()) {
                pid1 = (pid1 + 0x20).toByte()
            } else {
                query = false
            }
        }
    }
    return datas
}

fun supportItem2(flag: Int): MutableList<ByteArray?> {
    var query: Boolean = true
    var size = if (flag == 0) 2 else 3
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(size)
        obdData[0] = 0x02
        obdData[1] = pid1
        if (flag == 1) {
            obdData[2] = 0x00
        }

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data = comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }

        data?.apply {
            var item = data[0]
            datas.add(item)
            var flag = item?.get(3 + item[3]) //取有效数据的最后一位
            var result = flag?.and(0x01)
            if (result == 0x01.toByte()) {
                pid1 = (pid1 + 0x20).toByte()
            } else {
                query = false
            }
        }
    }
    return datas
}

fun hexString(data: Byte): String {
    return String.format("%02X", data)
}

fun mergePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset: Int
) {
    for (index in 0 until data?.size!!) {
        var inItem = data[index]
        if (inItem?.get(0 + offset) == 0x41.toByte()) {
            if (inItem.size > 2) {
                maskBuffer[index * 4 + 0] = inItem.get(2 + offset).toShort()
            }
            if (inItem.size > 3) {
                maskBuffer[index * 4 + 1] = inItem.get(3 + offset).toShort()
            }
            if (inItem.size > 4) {
                maskBuffer[index * 4 + 2] = inItem.get(4 + offset).toShort()
            }
            if (inItem.size > 5) {
                maskBuffer[index * 4 + 3] = (inItem.get(5 + offset).toShort() and 0xFE)
                if (inItem[5 + offset].toShort() and 0x01 === 0.toShort()) break
            }
        }
    }
}

fun mergeFreezePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset: Int
) {
    for (index in 0 until data?.size!!) {
        var inItem = data[index]
        if (inItem?.get(0 + offset) == 0x42.toByte()) {
            if (inItem.size > 3) {
                maskBuffer[index * 4 + 0] = inItem.get(3 + offset).toShort()
            }
            if (inItem.size > 4) {
                maskBuffer[index * 4 + 1] = inItem.get(4 + offset).toShort()
            }
            if (inItem.size > 5) {
                maskBuffer[index * 4 + 2] = inItem.get(5 + offset).toShort()
            }
            if (inItem.size > 6) {
                maskBuffer[index * 4 + 3] = inItem.get(6 + offset).toShort()
                if (inItem[6 + offset].toShort() and 0x01 === 0.toShort()) break
            }
        }
    }
}