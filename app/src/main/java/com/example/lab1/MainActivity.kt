package com.example.lab1

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ініціалізуємо чекбокси які використовуються
        val checkBoxPizzaTypeMozzarella = findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Mozzarella)
        val checkBoxPizzaTypeHawaiian = findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Hawaiian)
        val checkBoxPizzaTypePepperoni = findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Pepperoni)

        val checkBoxPizzaSize25 = findViewById<CheckBox>(R.id.checkBox_Pizza_Size_25)
        val checkBoxPizzaSize35 = findViewById<CheckBox>(R.id.checkBox_Pizza_Size_35)
        val checkBoxPizzaSize45 = findViewById<CheckBox>(R.id.checkBox_Pizza_Size_45)

        val checkBoxPizzaCount1 = findViewById<CheckBox>(R.id.checkBox_Pizza_Count_1)
        val checkBoxPizzaCount2 = findViewById<CheckBox>(R.id.checkBox_Pizza_Count_2)
        val checkBoxPizzaCount3 = findViewById<CheckBox>(R.id.checkBox_Pizza_Count_3)

        // створюємо групи чекбоксів, щоб можна було вибрати
        // тільки один параметер певного типу
        val checkBoxesPizzaType = listOf(checkBoxPizzaTypeMozzarella, checkBoxPizzaTypeHawaiian, checkBoxPizzaTypePepperoni)
        val checkBoxesPizzaSize = listOf(checkBoxPizzaSize25, checkBoxPizzaSize35, checkBoxPizzaSize45)
        val checkBoxesPizzaCount = listOf(checkBoxPizzaCount1, checkBoxPizzaCount2, checkBoxPizzaCount3)

        val allCheckBoxes = AllCheckBoxes(checkBoxesPizzaType, checkBoxesPizzaSize, checkBoxesPizzaCount)

        // реакція на натискання одного з чекбоксів
        clickCheckBox(checkBoxesPizzaType)
        clickCheckBox(checkBoxesPizzaSize)
        clickCheckBox(checkBoxesPizzaCount)

        // ініціалізація кнопки для підтвердження замовлення
        val buttonConfirm = findViewById<Button>(R.id.button_Confirm)

        // реакція на натискання кнопки
        clickButton(buttonConfirm, allCheckBoxes)

    }
    // якщо натиснути на один з чекбоксів, помітки на інших чекбоксах знімаються
    private fun clickCheckBox(checkBoxes: List<CheckBox>){
        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBoxes.forEach { it.isChecked = false }

                if (isChecked) {
                    buttonView.isChecked = true
                }
            }
        }
    }

    // для створення вікна повідомлення про статус замовлення
    private fun showPopupWindow(view: View, popup: View, closeButtonId: Int) {

        val popupWindow = PopupWindow(
            popup,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val alertCloseButton = popup.findViewById<Button>(closeButtonId)

        alertCloseButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun clickButton(buttonConfirm: Button, allCheckBoxes: AllCheckBoxes){
        buttonConfirm.setOnClickListener {
            // шукаємо вибрані дані
            val selectedCheckBoxPizzaType = allCheckBoxes.checkBoxesPizzaType.find { checkBox -> checkBox.isChecked }
            val selectedCheckBoxPizzaSize = allCheckBoxes.checkBoxesPizzaSize.find { checkBox -> checkBox.isChecked }
            val selectedCheckBoxPizzaCount = allCheckBoxes.checkBoxesPizzaCount.find { checkBox -> checkBox.isChecked }

            // якщо не всі параметри вибрані виводимо повідомлення попередження
            if (selectedCheckBoxPizzaType == null || selectedCheckBoxPizzaSize == null || selectedCheckBoxPizzaCount == null){
                val inflater = LayoutInflater.from(this)
                val viewAlert = inflater.inflate(R.layout.activity_alert, null)

                showPopupWindow(it, viewAlert, R.id.Alert_Close_Button)

                // якщо все вибрано виводимо підтвердження з даними замовлення
            } else {
                val inflater = LayoutInflater.from(this)
                val viewSuccess = inflater.inflate(R.layout.activity_success, null)

                val successType = viewSuccess.findViewById<TextView>(R.id.Success_Type)
                val successSize = viewSuccess.findViewById<TextView>(R.id.Success_Size)
                val successCount = viewSuccess.findViewById<TextView>(R.id.Success_Count)

                successType.text = getString(R.string.success_type, selectedCheckBoxPizzaType.text.toString())
                successSize.text = getString(R.string.success_size, selectedCheckBoxPizzaSize.text.toString())
                successCount.text = getString(R.string.success_count, selectedCheckBoxPizzaCount.text.toString())

                showPopupWindow(it, viewSuccess, R.id.Success_Close_Button)
            }
        }
    }

    data class AllCheckBoxes(
        val checkBoxesPizzaType: List<CheckBox>,
        val checkBoxesPizzaSize: List<CheckBox>,
        val checkBoxesPizzaCount: List<CheckBox>
    )
}