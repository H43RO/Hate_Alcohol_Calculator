package com.haerokim.hate_alcohol

import android.app.ProgressDialog.show
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_hate_calculator.count_member
import kotlinx.android.synthetic.main.activity_normal_caculator.*

class NormalCaculatorActivity : AppCompatActivity() {
    var people = 2
    var total: Int = 1000

    //SnackBar ActionButton Listener - Saving result at MainActivity

    inner class SaveButtonListener(val result : String ) : View.OnClickListener{
        override fun onClick(v: View?) {
            val intent = Intent(this@NormalCaculatorActivity, MainActivity::class.java)
            intent.putExtra("result", result)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_caculator)


        //술자리에 참여한 사람이 몇명인지 얻기 위한 SeekBar

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, count: Int, p2: Boolean) {
                count_member.setText(count.toString()+"명")
                people = count
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        //총 술값(영수증에 찍힌 금액)을 얻기 위한 EditText Listener

        total_sum_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                if (total_sum_edit_text.text?.length != 0) { //강제종료 현상 방지
                    total = Integer.parseInt(total_sum_edit_text.text.toString())
                    total /= people
                    sum_text_view.text = total.toString() + "원"
                } else {
                    total_sum_edit_text.setError("금액을 입력해주세요")
                }
        })

        //ClipBoard에 결과값을 복사

        var result: String
        var clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clipData: ClipData

        sum_text_view.setOnClickListener {
            if(sum_text_view.text.toString() == "0원"){
                total_sum_edit_text.setError("금액을 입력해주세요")
                Toast.makeText(
                    this,
                    "정보를 모두 입력해주세요",
                    Toast.LENGTH_SHORT
                ).show()

            }else{
                result = sum_text_view.text.toString()

                clipData = ClipData.newPlainText("TOTAL", result)
                clipboardManager.primaryClip = clipData

                Snackbar.make(normal_layout, "복사되었습니다!", Snackbar.LENGTH_LONG)
                    .setAction("저장", SaveButtonListener(result))
                    .setActionTextColor(Color.parseColor("#ffffff"))
                    .show()
            }
        }
    }
}
