package com.example.deber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Auto

class AutoAdapter(
    private val autos: List<Auto>,
    private val onModificarClickListener: (Auto) -> Unit,
    private val onEliminarClickListener: (Auto) -> Unit
) : RecyclerView.Adapter<AutoAdapter.AutoViewHolder>() {

    class AutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marcaTextView: TextView = itemView.findViewById(R.id.textViewMarca)
        val modeloTextView: TextView = itemView.findViewById(R.id.textViewModelo)
        val anioTextView: TextView = itemView.findViewById(R.id.textViewAnio)
        val precioTextView: TextView = itemView.findViewById(R.id.textViewPrecio)
        val disponibleTextView: TextView = itemView.findViewById(R.id.textViewDisponible)
        val btnModificar: Button = itemView.findViewById(R.id.btnModificar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_auto, parent, false)
        return AutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
        val auto = autos[position]
        holder.marcaTextView.text = "Marca: ${auto.marca}"
        holder.modeloTextView.text = "Modelo: ${auto.modelo}"
        holder.anioTextView.text = "Año: ${auto.anio}"
        holder.precioTextView.text = "Precio: $${auto.precio}"
        holder.disponibleTextView.text = if (auto.disponible) "Disponible" else "No disponible"

        // Configurar el listener para el botón "Modificar"
        holder.btnModificar.setOnClickListener {
            onModificarClickListener(auto)
        }

        // Configurar el listener para el botón "Eliminar"
        holder.btnEliminar.setOnClickListener {
            onEliminarClickListener(auto)
        }
    }

    override fun getItemCount(): Int {
        return autos.size
    }
}