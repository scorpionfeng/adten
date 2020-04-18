package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


class DestructCanStd :DestructBiz{

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String{
        var vinCode=""
        var vinList= mutableListOf<Byte>()
        var answer=false
        data.forEachIndexed loop@{index, bytes ->
            bytes?.apply {
                takeIf { index==0 }?.apply {
                    answer = bytes.contains(0x49.toByte())
                    takeIf { answer }?.apply {
                        vinList.addAll(bytes.slice(bytes.size-3 until bytes.size))
                    } ?:apply {
                        return@loop
                    }
                }
                takeIf { index>0 &&  answer }?.apply {
                    vinList.addAll(bytes.slice(4 until bytes.size))
                }
            }
        }

        takeIf { vinList.isNotEmpty() }?.apply {
            try {
                vinList.forEach {
                    vinCode += String.format("%c", it)
                }
            }catch (e:Exception){
                println("parse vin error")
            }

        }
        return vinCode

    }

    override fun parseTrobCode(cmd: Byte, data: List<ByteArray?>): Pair<Int, List<String>> {
        var isSingle=true
        var amount_code=0
        var index_flag=0
        var troCodeTextList= mutableListOf<String>()
        var troCodeDataList= mutableListOf<Byte>()
        data?.forEachIndexed loop@{
                index,item->
            item?.apply {

                takeIf { index == 0 }?.apply {
                    index_flag= item.indexOf(0x10)
                    takeIf { index_flag>-1 }?.let { isSingle=false }
                }

                takeIf { isSingle }?.apply {/** 单帧 */
                val sureAnsIndex = indexOf(cmd.plus(0x40.toByte()).toByte())
                    takeIf { sureAnsIndex == -1 }?.apply { return@loop }/** 非肯定应答 */
                    amount_code = this[sureAnsIndex + 1].toInt()/** 故障数 */
                    var elements = this.slice(sureAnsIndex+2 .. size-1) /** 取故障码数据及填充数据*/
                    troCodeDataList.addAll(elements)/** 添加到列表 */
                    return@loop
                } ?:apply {/** 长帧 */
                    takeIf { index==0 }?.apply {
                        index_flag=indexOf(0x10)
                        val index_sure = indexOf(cmd.plus(0x40.toByte()).toByte())
                        takeIf { index_sure == -1 }?.apply { return@loop }
                        amount_code=this[index_sure+1].toInt()
                        var flow_first = slice(index_sure + 2..size-1)
                        troCodeDataList.addAll(flow_first)

                    }?:apply {
                        var flow_else=slice(index_flag+1 .. size-1)
                        troCodeDataList.addAll(flow_else)
                    }
                }
            }
        }

        var codeArrays = troCodeDataList.toList().take(amount_code * 2)
        var tempCodeStr=""
        for( i in codeArrays.indices step 2){
            val codeHex=(codeArrays[i].toInt().shl(8).and(0xff)).or(codeArrays[i+1].toInt().and(0xff))

            try {
                tempCodeStr = when {
                    codeHex < 0x4000 -> { // P
                        String.format("P%04X", codeHex)
                    }
                    codeHex < 0x8000 -> { // C
                        String.format("C%04X", codeHex - 0x4000)
                    }
                    codeHex < 0xC000 -> { // B
                        String.format("B%04X", codeHex - 0x8000)
                    }
                    else -> { // U
                        String.format("U%04X", codeHex - 0xC000)
                    }
                }
                troCodeTextList.add(tempCodeStr)
            }catch (e: Exception){
                println("format exception")
            }
        }

        return Pair(amount_code, troCodeTextList)
    }


}