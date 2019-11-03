package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.ArrayList

class FoodItemActivity : AppCompatActivity() {

    var navigationPosition: Int = 0

    //Steady picks
    private var steadyPicksRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodHorizontal>? = null
    private var adapter: AdapterFoodViewHorizontal? = null
    //TODO: Update foodList
    private val myImageList = intArrayOf(R.drawable.chicken_rice, R.drawable.char_siew_rice, R.drawable.fishball_noodle_dry, R.drawable.minced_pork_noodle, R.drawable.duck_rice, R.drawable.kway_chap, R.drawable.lor_mee, R.drawable.fried_rice, R.drawable.fried_carrot_cake)
    private val myImageNameList = arrayOf("Chicken rice", "Char siew rice", "Fishball noodle(Dry)", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")
    private val myImageDescriptionList = arrayOf(" Chicken, roasted, with skin, served with rice and chilli sauce.", "Pork barbequed in sweet sauce, served with rice and cucumber.", "Yellow noodles with fish ball and chye sim, served with chili sauce.", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_item)
        initView()

        //TODO: update stall name
        val stallName = "Stall name"
        setTitle(stallName)

        //TODO: Retrieve JSON and extract specific food item details

        //TODO: Render the details into Nutrition display table

        //Steady Picks
        steadyPicksRecyclerView = findViewById(R.id.SteadyPicksRecyclerView)

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = AdapterFoodViewHorizontal(applicationContext, imageModelArrayList!!)
        steadyPicksRecyclerView!!.adapter = adapter
        steadyPicksRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        steadyPicksRecyclerView!!.addOnItemTouchListener(
            FoodItemActivity.RecyclerTouchListener(
                applicationContext,
                steadyPicksRecyclerView!!,
                object : FoodItemActivity.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        //TODO: Start new Activity here
                        Toast.makeText(
                            applicationContext,
                            imageModelArrayList!![position].getNames(),
                            Toast.LENGTH_SHORT
                        ).show()

                        //Start new Activity
                        //TODO: Pass stall name to QR Activity
                        val myIntent = Intent(applicationContext, QRActivity::class.java)
                        startActivity(myIntent)
                        finish()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

        //TODO: Retrieve available coins for redemption

        //TODO: Update discounted price in button.Text



        //TODO: Purchase food
        val btnPurchase : Button = findViewById(R.id.btnPurchase)
        btnPurchase.setOnClickListener {
            //TODO: Validate wallet amount, else prompt user to top-up
            //TODO: Persist purchase order in DB
            //TODO: Store 10% rebate of spending amount in wallet

            //TODO - BONUS feature: Send sms for confirmation of order made
            //Direct user to last order
            val myIntent = Intent(applicationContext, QRActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    private fun populateList(): ArrayList<ModelFoodHorizontal> {

        val list = ArrayList<ModelFoodHorizontal>()
        for (i in myImageList.indices) {
            val imageModel = ModelFoodHorizontal()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            imageModel.setDescriptions(myImageDescriptionList[i])
            list.add(imageModel)
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: FoodItemActivity.ClickListener?) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = R.id.nav_home
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Home"

        navigationView.setNavigationItemSelectedListener  { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    toolbar.title = "Home"
                    navigationPosition = R.id.nav_home
                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
                R.id.nav_purchases -> {
                    toolbar.title = "Purchases"
                    navigationPosition = R.id.nav_purchases
                }
                R.id.nav_profile -> {
                    toolbar.title = "Profile"
                    navigationPosition = R.id.nav_profile
                }
                R.id.nav_wallet -> {
                    toolbar.title = "Wallet"
                    navigationPosition = R.id.nav_wallet
                    val myIntent = Intent(this, WalletActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
                R.id.nav_about -> {
                    toolbar.title = "About"
                    navigationPosition = R.id.nav_about
                }
            }
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()
            true
        }

        //Change navigation header information
        changeNavigationHeaderInfo()
    }

    private fun changeNavigationHeaderInfo() {
        val headerView = navigationView.getHeaderView(0)
        headerView.username.text = "lokeshdesai@android4dev.com"
    }

    private fun setUpDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == R.id.nav_home) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.nav_home
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Home"
        }
    }
}