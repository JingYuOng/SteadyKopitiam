package com.example.steadykopitiam.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.steadykopitiam.DBHelper
import com.example.steadykopitiam.UserRecord
import com.google.android.material.textfield.TextInputLayout
import android.widget.RadioButton
import android.widget.ArrayAdapter


class EditProfileActivity : AppCompatActivity() {
    lateinit var kopitiamDBHelper: DBHelper

    //User details
    var user = ArrayList<UserRecord>()
    private var userpassword : String? = ""
    private var useremail  : String? = ""
    var choice = arrayOf("High","Medium","Low")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.steadykopitiam.R.layout.activity_edit_profile)

        val btnUpdateProfile : Button = findViewById(com.example.steadykopitiam.R.id.btnUpdateProfile)
        val spinner = findViewById<Spinner>(com.example.steadykopitiam.R.id.profile_spinnerChoice)
        var genderStr : String = ""
        var dailyAct : String = ""

        val reg_input_layout_email: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_email)
        val register_email : EditText = findViewById(com.example.steadykopitiam.R.id.profile_email)
        val reg_input_layout_phoneNumber: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_phoneNumber)
        val register_phoneNumber : EditText = findViewById(com.example.steadykopitiam.R.id.profile_phoneNumber)
        val reg_input_layout_name: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_name)
        val register_name : EditText = findViewById(com.example.steadykopitiam.R.id.profile_name)
        val reg_input_layout_password: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_password)
        val register_password : EditText = findViewById(com.example.steadykopitiam.R.id.profile_password)
        val reg_input_layout_age: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_age)
        val register_age : EditText = findViewById(com.example.steadykopitiam.R.id.profile_age)
        val reg_input_layout_weight: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_weight)
        val register_weight : EditText = findViewById(com.example.steadykopitiam.R.id.profile_weight)
        val reg_input_layout_height: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_height)
        val register_height : EditText = findViewById(com.example.steadykopitiam.R.id.profile_height)
        val radio_group : RadioGroup = findViewById(com.example.steadykopitiam.R.id.profile_radio_group)


        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choice)
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    (parent.getChildAt(0) as TextView).setTextColor(Color.rgb(0,150,136))
                    (parent.getChildAt(0) as TextView).textSize = 18f
                    dailyAct = choice[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }


        // Get radio group selected item
        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                genderStr = radio.text.toString()
            })

        //Render user data
        fetchUser()
        //Text validation
        validation()

        //Update user data
        btnUpdateProfile.setOnClickListener {
            var valid : Boolean = true
            if(register_email.text.isEmpty()) {
                reg_input_layout_email.setError("Please enter your email.")
                valid = false
            }
            if(register_password.text.isEmpty()) {
                reg_input_layout_password.setError("Please enter your password.")
                valid = false
            }
            if (register_phoneNumber.text.isEmpty()) {
                reg_input_layout_phoneNumber.setError("Please enter your phone number.")
                valid = false
            }
            if (register_age.text.isEmpty()) {
                reg_input_layout_age.setError("Please enter your age.")
                valid = false
            }
            if (register_name.text.isEmpty()) {
                reg_input_layout_name.setError("Please enter your name.")
                valid = false
            }
            if (register_height.text.isEmpty()) {
                reg_input_layout_height.setError("Please enter your height.")
                valid = false
            }
            if (register_weight.text.isEmpty()) {
                reg_input_layout_weight.setError("Please enter your weight.")
                valid = false
            }
            if(valid) {
                var bool: Boolean = updateUser(dailyAct, genderStr)
                if(bool) {
//                    Toast.makeText(this, bool.toString(), Toast.LENGTH_LONG).show()
                    val myIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
            }

        }


    }

    fun updateUser(dailyAct:String, genderString: String):Boolean{
        val register_email : EditText = findViewById(com.example.steadykopitiam.R.id.profile_email)
        val register_phoneNumber : EditText = findViewById(com.example.steadykopitiam.R.id.profile_phoneNumber)
        val register_name : EditText = findViewById(com.example.steadykopitiam.R.id.profile_name)
        val register_password : EditText = findViewById(com.example.steadykopitiam.R.id.profile_password)
        val register_age : EditText = findViewById(com.example.steadykopitiam.R.id.profile_age)
        val register_weight : EditText = findViewById(com.example.steadykopitiam.R.id.profile_weight)
        val register_height : EditText = findViewById(com.example.steadykopitiam.R.id.profile_height)

        var username  = register_name.text.toString()
        var phoneNumber = register_phoneNumber.text.toString()
        var password  = register_password.text.toString()
        var email     = register_email.text.toString()
        var age       = register_age.text.toString()
        var height    = register_height.text.toString().toDouble()
        var weight    = register_weight.text.toString().toDouble()
        var bmi = (weight / (height*height))
        var calories : Int = 0
        var bmr = (10 * weight) + (6.25 * height) - (5 * age.toInt())
        var gender      : String = genderString
        var accountBalance : String = "20"
        var accountPoints : String = "0"


        if(dailyAct.equals("High")){
            calories = (bmr * 1.7).toInt()
        }else if(dailyAct.equals("Medium")){
            calories = (bmr * 1.5).toInt()
        }else{
            calories = (bmr * 1.2).toInt()
        }

        var carb = ( calories * 0.6 / 4 )
        var protein = (calories * 0.125 / 4 )
        var minerails = "3.4"
        var fibra = "30"
        var vitamins = "0.09"
        var fat = (calories * 0.275 / 9 )

        var result = kopitiamDBHelper.updateUser(
            UserRecord(username,gender,height,weight,bmi.toDouble(),age,email,accountBalance.toInt(),accountPoints.toInt(),carb.toInt(),calories,
                fat.toInt(),fibra.toInt(),minerails.toDouble(),vitamins.toDouble(),dailyAct,protein.toInt(),password,phoneNumber)
        )
//        Toast.makeText(this, "Added User : "+result, Toast.LENGTH_LONG).show()
        kopitiamDBHelper = DBHelper(this)

        //TODO: SetText and Set Index position of spinner
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

        return result
    }

    fun validation() {
        val reg_input_layout_email: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_email)
        val register_email : EditText = findViewById(com.example.steadykopitiam.R.id.profile_email)
        val reg_input_layout_phoneNumber: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_phoneNumber)
        val register_phoneNumber : EditText = findViewById(com.example.steadykopitiam.R.id.profile_phoneNumber)
        val reg_input_layout_name: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_name)
        val register_name : EditText = findViewById(com.example.steadykopitiam.R.id.profile_name)
        val reg_input_layout_password: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_password)
        val register_password : EditText = findViewById(com.example.steadykopitiam.R.id.profile_password)
        val reg_input_layout_age: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_age)
        val register_age : EditText = findViewById(com.example.steadykopitiam.R.id.profile_age)
        val reg_input_layout_weight: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_weight)
        val register_weight : EditText = findViewById(com.example.steadykopitiam.R.id.profile_weight)
        val reg_input_layout_height: TextInputLayout = findViewById(com.example.steadykopitiam.R.id.profile_input_layout_height)
        val register_height : EditText = findViewById(com.example.steadykopitiam.R.id.profile_height)
        val radio_group : RadioGroup = findViewById(com.example.steadykopitiam.R.id.profile_radio_group)

        register_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_email.setError("Please enter your email.")
                else
                    reg_input_layout_email.setError(null)
            }
        })

        register_phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_phoneNumber.setError("Please enter your phone number.")
                else
                    reg_input_layout_phoneNumber.setError(null)
            }
        })

        register_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_name.setError("Please enter your name.")
                else
                    reg_input_layout_name.setError(null)
            }
        })

        register_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_password.setError("Please enter your password.")
                else
                    reg_input_layout_password.setError(null)
            }
        })

        register_age.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_age.setError("Please enter your age.")
                else
                    reg_input_layout_age.setError(null)
            }
        })

        register_height.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_height.setError("Please enter your height.")
                else
                    reg_input_layout_height.setError(null)
            }
        })

        register_weight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_weight.setError("Please enter your weight.")
                else
                    reg_input_layout_weight.setError(null)
            }
        })

    }

    @SuppressLint("DefaultLocale")
    fun fetchUser() {
        val register_email : EditText = findViewById(com.example.steadykopitiam.R.id.profile_email)
        val register_phoneNumber : EditText = findViewById(com.example.steadykopitiam.R.id.profile_phoneNumber)
        val register_name : EditText = findViewById(com.example.steadykopitiam.R.id.profile_name)
        val register_password : EditText = findViewById(com.example.steadykopitiam.R.id.profile_password)
        val register_age : EditText = findViewById(com.example.steadykopitiam.R.id.profile_age)
        val register_weight : EditText = findViewById(com.example.steadykopitiam.R.id.profile_weight)
        val register_height : EditText = findViewById(com.example.steadykopitiam.R.id.profile_height)
        val radio_group : RadioGroup = findViewById(com.example.steadykopitiam.R.id.profile_radio_group)


        kopitiamDBHelper = DBHelper(this)

        //TODO: SetText and Set Index position of spinner
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

        if (!user.isEmpty()) {
            register_email.setText(user.get(0).email)
            register_password.setText(user.get(0).user_password)
            register_phoneNumber.setText(user.get(0).phoneNumber)
            register_name.setText(user.get(0).username)
            register_age.setText(user.get(0).age)
            register_height.setText(user.get(0).height.toString())
            register_weight.setText(user.get(0).weight.toString())

            var gender: String = user.get(0).gender
//            Toast.makeText(this, gender, Toast.LENGTH_SHORT).show()
            if (gender.equals("male")) {
                (radio_group.getChildAt(0) as RadioButton).isChecked = true
            } else {
                (radio_group.getChildAt(1) as RadioButton).isChecked = true
            }

            val spinner =
                findViewById<Spinner>(com.example.steadykopitiam.R.id.profile_spinnerChoice)
            val compareValue = user.get(0).user_dailyActivies
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choice)

            spinner.setAdapter(arrayAdapter)

//             Toast.makeText(this, choice.indexOf(compareValue).toString(), Toast.LENGTH_LONG).show()
             spinner.setSelection(choice.indexOf(compareValue),true)

        }

    }
}
