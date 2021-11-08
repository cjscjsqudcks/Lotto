package com.example.lotto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val clearBtn: Button by lazy {
        findViewById<Button>(R.id.clearbtn)
    }
    private val addBtn: Button by lazy {
        findViewById<Button>(R.id.addbtn)
    }
    private val randBtn: Button by lazy {
        findViewById<Button>(R.id.randbtn)
    }
    private val numPick: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numpick)
    }
    private val historyBtn:Button by lazy{
        findViewById<Button>(R.id.historybtn)
    }

    private val numTxList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.numtx1),
            findViewById<TextView>(R.id.numtx2),
            findViewById<TextView>(R.id.numtx3),
            findViewById<TextView>(R.id.numtx4),
            findViewById<TextView>(R.id.numtx5),
            findViewById<TextView>(R.id.numtx6)
        )
    }
    private var did = false

    private val numSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initNumber()
        initAddbtn()
        initClearbtn()
        initHistoryBtn()
    }

    private fun initClearbtn(){
        clearBtn.setOnClickListener {
            numSet.clear()
            numTxList.forEach{
                it.text=""
                it.background=null
            }
            did=false
        }
    }

    private fun initAddbtn() {
        addBtn.setOnClickListener {
            if (did) {
                Toast.makeText(this, "초기화 후 실행 부탁합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (numSet.size >= 6) {
                Toast.makeText(this, " 번호는 6개까지만 선택 가능합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (numSet.contains(numPick.value)) {
                Toast.makeText(this, " 이미 선택한 번호 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val tx=numTxList[numSet.size]//선택한 숫자의 개수만큼에 해당하는 번재의 textview 뽑아옴

            tx.text=numPick.value.toString()//향제 넘버픽의 숫자를 갖고 옴
            setNumberBackground(numPick.value,tx)

            numSet.add(numPick.value)//배열에도 선택된 번호를 저장
        }
    }

    private fun initHistoryBtn(){
        historyBtn.setOnClickListener {
            val i= Intent(this,HistoryActivity::class.java)
            startActivity(i)
        }
    }

    private fun initNumber() {
        randBtn.setOnClickListener {
            val list = getRandNum()
            did=true
            list.forEachIndexed{index,number->
                val tx=numTxList[index]
                tx.text=number.toString()
                setNumberBackground(number,tx)
            }

        }
    }

    private fun setNumberBackground (number:Int, tx: TextView){
        when(number) {
            in 1..10->tx.background=getDrawable(R.drawable.circle_yellow)
            in 11..20->tx.background=getDrawable(R.drawable.circle_blue)
            in 21..30->tx.background=getDrawable(R.drawable.circle_red)
            in 31..40->tx.background=getDrawable(R.drawable.circle_gray)
            else->tx.background=getDrawable(R.drawable.circle_green)

        }

    }

    private fun getRandNum(): List<Int> {
        val list= mutableListOf<Array<Int>>()

        val numList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if(numSet.contains(i)){//기존 번호 배열에 i가 있을 경우
                    continue
                }
                this.add(i)
            }
        }
        numList.shuffle()//섞기

        val newList =numSet.toList()+ numList.subList(0, 6-numSet.size)//기존 리스트를 추출하여 합침
        return newList.sorted()//정렬
    }
}