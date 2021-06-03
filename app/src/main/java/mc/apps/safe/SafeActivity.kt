package mc.apps.safe

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_safe.*
import mc.apps.safe.db.Dao
import mc.apps.safe.tools.AESCrypt
import mc.apps.safe.tools.FromDbAdapter
import mc.apps.safe.tools.McSwipeCallback


class SafeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : FromDbAdapter
    private val TAG = "tests"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe)
        setList()
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
            val url = dialogView.findViewById<EditText>(R.id.inputUrl).text.toString()

            if(name.isNotEmpty() && password.isNotEmpty() && login.isNotEmpty()) {
                val dao = Dao(SafeActivity@ this)
                var ok = dao.create(name, login, AESCrypt.encrypt(password), url)
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
    }
    override fun onBackPressed() {
        //super.onBackPressed()
        setList()
    }
    private fun setList() {

        recyclerView = findViewById(R.id.list)
        recyclerView!!.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        //val layoutManager = GridLayoutManager(this, 3)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        adapter = FromDbAdapter(
            this,
            object : FromDbAdapter.OnItemClickListener {
                override fun onItemClick(postion:Int, item: String) {
                   //Toast.makeText(this@SafeActivity, "Click on $item", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            this@SafeActivity, R.anim.layout_fall_down_animation
        )

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
                    "Copy",
                    View.OnClickListener {
                        Toast.makeText(applicationContext, "${AESCrypt.decrypt(item.password)} : password copied!", Toast.LENGTH_SHORT).show()
                        copyToClipboard(AESCrypt.decrypt(item.password))
                        setList()
                    })
                return AESCrypt.decrypt(item.password)
            }

            private fun copyToClipboard(text: String) {
                val clipboard : ClipboardManager  = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val  clip : ClipData = ClipData.newPlainText("Copied Password", text);
                clipboard.setPrimaryClip(clip)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val item = adapter.getData(position)

                if(direction==ItemTouchHelper.RIGHT) {
                    val dialog = BottomSheetDialog(this@SafeActivity)
                    val layout = layoutInflater.inflate(R.layout.bottom_actions_layout, null)
                    layout.findViewById<TextView>(R.id.txt_dialog_message).text = "${item.name} : ${item.url}"
                    dialog.setContentView(layout)
                    layout.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                        dialog.cancel()
                        setList()
                    }
                    layout.findViewById<ImageButton>(R.id.btn_delete).setOnClickListener {
                        showDeleteDialog(position)
                    }
                    dialog.show()
                }
            }

            private fun showDeleteDialog(position: Int) {
                val item = adapter.getData(position)
                val alertDialog: AlertDialog? = this@SafeActivity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setPositiveButton("Yes") { dialog, id ->
                            val dao = Dao(this@SafeActivity)
                            dao.delete(item)
                            toast("Item deleted!")
                            dialog.cancel()
                            setList()
                        }
                        setNegativeButton("No") { dialog, id ->
                            dialog.cancel()
                            setList()
                        }
                    }
                    builder.setMessage("Delete item : ${item.name}?")
                    builder.setTitle("Delete?")

                    builder.create()
                }
                alertDialog?.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun toast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //val inflater = menuInflater
        menuInflater.inflate(R.menu.menu_safe, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_action -> {
                openDialog()
                true
            }
            R.id.export_action -> {
                shareData()
                true
            }
            R.id.exit_action -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareData() {
        val dao = Dao(SafeActivity@this)
        val list = dao.all()
        val items = StringBuilder()
        list.forEach{
             //   item -> Log.i(TAG,"item : ${item.login} : ${item.password} [${AESCrypt.decrypt(item.password)}]")
            items.append("["+it.name+"]"+it.login+":"+it.password+"\n")
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, items.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share Passwords!")
        startActivity(shareIntent)
    }
}
