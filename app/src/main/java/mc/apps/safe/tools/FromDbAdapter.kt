package mc.apps.safe.tools

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mc.apps.safe.R
import mc.apps.safe.db.Dao
import mc.apps.safe.db.Item

class FromDbAdapter(val context: Context, private val listener: OnItemClickListener?) : RecyclerView.Adapter<FromDbAdapter.ViewHolder>() {
    private var dataset = listOf<Item>()

    init {
        refresh()
    }

    fun refresh(){
        this.dataset = read()
    }
    private fun read(): List<Item> {
        val dao = Dao(context)
        return dao.all()
    }

    interface OnItemClickListener {
        fun onItemClick(position:Int, item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "[${dataset[position].name}] ${dataset[position].login}"

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position, dataset[position].login)
        }
    }


    fun getData(position : Int): Item {
        return dataset[position]
    }

    override fun getItemCount(): Int {
      return dataset.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var title: TextView = itemView.findViewById(R.id.item_title)

      /* public void bind(final String item, final OnItemClickListener listener) {
          title.setText(item);
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                  listener.onItemClick(item);
              }
          });
      }*/
  }
}
