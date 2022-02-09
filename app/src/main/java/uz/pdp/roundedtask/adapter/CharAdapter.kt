package uz.pdp.roundedtask.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.databinding.ItemCharBinding

class CharAdapter(private val listener: OnCharItemClickListener) :
    PagingDataAdapter<CharEntity, CharAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<CharEntity>() {
        override fun areItemsTheSame(oldItem: CharEntity, newItem: CharEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharEntity, newItem: CharEntity): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemBinding: ItemCharBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(result: CharEntity) {
            itemBinding.apply {
                Picasso.get().load(result.image).into(image)
                name.text = result.name
                status.text = result.status
                species.text = result.species
                location.text = result.location
                when (result.status) {
                    "Dead" -> {
                        statusColor.setCardBackgroundColor(Color.parseColor("#FFD63D2E"))
                    }
                    "Alive" -> {
                        statusColor.setCardBackgroundColor(Color.parseColor("#FF55CC44"))
                    }
                    else -> {
                        statusColor.setCardBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    }
                }

                itemView.setOnClickListener {
                    listener.itemClick("https://rickandmortyapi.com/")
                }
            }
        }
    }

    interface OnCharItemClickListener {
        fun itemClick(url: String)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCharBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}