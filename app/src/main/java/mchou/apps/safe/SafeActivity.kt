package mchou.apps.safe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mchou.apps.safe.tools.FromDbAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_safe.*
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import mchou.apps.codelabs.tools.Util
import mchou.apps.safe.db.Dao
import mchou.apps.safe.tools.AESCrypt
import mchou.apps.safe.tools.McSwipeCallback


class SafeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : FromDbAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe)

        setList()

      /*  val dao = Dao(SafeActivity@this)
        val list = dao.all()

        *//*dao.create("EDF: dady120@hotmail.fr",AESCrypt.encrypt("Adnyl_123"))
        dao.create("SFR: mehdi.chouarbi@sfr.fr",AESCrypt.encrypt("Adnyl_987"))*//*

        Log.i("tests","**********************************************************")
        list.forEach{
             item -> Log.i("tests","item : ${item.login} : ${item.password} [${AESCrypt.decrypt(item.password)}]")
        }
        Log.i("tests","**********************************************************")*/

        btn_add.setOnClickListener {
            /*snackbar_show(main_layout, "Hello!","Action!!",View.OnClickListener {
                Toast.makeText(this,"That's it!",Toast.LENGTH_SHORT).show()
            })*/
            openDialog()

          /*  Util.circleAppear(btn_menu_1)
            Util.circleAppear(txt_menu_1)*/
        }
        /*btn_menu_1.setOnClickListener {
            openDialog()
        }*/
    }

    private fun openDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Add Item..")
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher)
        alertDialogBuilder.setCancelable(false)

        val layoutInflater = LayoutInflater.from(this)
        val dialogView = layoutInflater.inflate(R.layout.input_layout, null)

        alertDialogBuilder.setView(dialogView)

        val dialog = alertDialogBuilder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.btn_cancel_item).setOnClickListener {
            dialog!!.cancel()
            setList()
        }
        dialogView.findViewById<Button>(R.id.btn_save_item).setOnClickListener {

            val name = dialogView.findViewById<EditText>(R.id.inputName).text.toString()
            val password = dialogView.findViewById<EditText>(R.id.inputPassword).text.toString()
            val login = dialogView.findViewById<EditText>(R.id.inputLogin).text.toString()

            if(name.isNotEmpty() && password.isNotEmpty() && login.isNotEmpty()) {
                val dao = Dao(SafeActivity@ this)
                var ok = dao.create(name, login, AESCrypt.encrypt(password), "")
                if (ok) {
                    toast("Item inserted! :)")
                    adapter.refresh()
                    adapter.notifyItemInserted(adapter.itemCount)
                    setList()
                }
            }
            //adapter.notifyDataSetChanged()
            dialog.cancel()
        }
    }

    private fun snackbar_show(parent: View, message: String, action: String, callback: View.OnClickListener?) {
        val snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG )
        snackbar.setAction(action, callback)
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()

        /*
            val snackbar = Snackbar
                .make(
                    main_layout,
                    "Removed from list!",
                    Snackbar.LENGTH_LONG
                )
            snackbar.setAction("UNDO") {
                *//*adapter.notifyDataSetChanged()
                                 recyclerView.scrollToPosition(position)*//*
                                Toast.makeText(this@SafeActivity, "Yes!!", Toast.LENGTH_SHORT).show()
                            }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
          */
    }

    private fun setList() {

        recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView!!.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        //val layoutManager = GridLayoutManager(this, 3)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        adapter = FromDbAdapter(
            this,
            null
            /*object : FromDbAdapter.OnItemClickListener {
                override fun onItemClick(postion:Int, item: String) {
                   // Toast.makeText(this@SafeActivity, "Click on $item", Toast.LENGTH_SHORT).show()
                }
            }*/
        )
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            this@SafeActivity, R.anim.layout_fall_down_animation
        )

        /*
        set swipe..
         */
        addSwipeToList()
    }

    private fun addSwipeToList() {
        /*val swipeController = SwipeToCallback(SafeActivity@this)
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(recyclerView)*/

        val swipeToDeleteCallback = object : McSwipeCallback(this) {
            override fun ItemClicked(position: Int) : String {
                val item = adapter.getData(position)
                snackbar_show(main_layout,
                    "Password : ${AESCrypt.decrypt(item.password)}",
                    "Close",
                    View.OnClickListener {
                        setList()
                    })

                return AESCrypt.decrypt(item.password)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val item = adapter.getData(position)

                if(direction==ItemTouchHelper.RIGHT) {
                    val dialog = BottomSheetDialog(this@SafeActivity)
                    val layout = layoutInflater.inflate(R.layout.bottom_dialog_layout, null)

                    layout.findViewById<TextView>(R.id.txt_dialog_message).text =
                        "Delete Item '${item.name}' from list?"
                    dialog.setContentView(layout)
                    layout.findViewById<Button>(R.id.btn_dialog_no).setOnClickListener {
                        dialog!!.cancel()
                        setList()
                    }
                    layout.findViewById<Button>(R.id.btn_dialog_yes).setOnClickListener {
                        val dao = Dao(this@SafeActivity)
                        dao.delete(item)
                        toast("Item deleted!")

                        dialog!!.cancel()
                        setList()
                    }
                    dialog.show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun toast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
