package com.example.realmpoc.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.realmpoc.R
import com.example.realmpoc.activity.DogListener
import com.example.realmpoc.entity.Dog

class DogsAdapter(
    private var dogList: ArrayList<Dog>,
    private var listener: DogListener
) : RecyclerView.Adapter<DogsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DogsAdapter.CardViewHolder {
        return CardViewHolder(
            listener,
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.dog_card,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(dogList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newDogList: ArrayList<Dog>) {
        dogList = newDogList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dogList.size

    inner class CardViewHolder(private val listener: DogListener, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dogName: TextView = itemView.findViewById(R.id.dog_name_card)
        private val dogAge: TextView = itemView.findViewById(R.id.dog_age_card)
        private val deleteButton: ImageView = itemView.findViewById(R.id.remove_dog)

        fun bind(dog: Dog) {
            dogName.text = itemView.context.getString(R.string.dog_name_card_string, dog.name)
            dogAge.text = itemView.context.getString(R.string.dog_age_card_string, dog.age.toString())
            deleteButton.setOnClickListener {
                listener.deleteDog(adapterPosition, dogList[adapterPosition].name)
            }
        }
    }
}
