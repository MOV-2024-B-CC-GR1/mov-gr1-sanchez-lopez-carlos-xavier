package com.example.deber.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.R

class ConcesionariaAdapter(private val concesionarias: List<Concesionaria>) :
    RecyclerView.Adapter<ConcesionariaAdapter.ConcesionariaViewHolder>() {

    class ConcesionariaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombre)
        val direccionTextView: TextView = itemView.findViewById(R.id.textViewDireccion)
        val fechaFundacionTextView: TextView = itemView.findViewById(R.id.textViewFechaFundacion)
        val abiertaTextView: TextView = itemView.findViewById(R.id.textViewAbierta)
        val numeroAutosTextView: TextView = itemView.findViewById(R.id.textViewNumeroAutos)
        val latitudTextView: TextView = itemView.findViewById(R.id.textViewLatitud)
        val longitudTextView: TextView = itemView.findViewById(R.id.textViewLongitud)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcesionariaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_concesionaria, parent, false)
        return ConcesionariaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConcesionariaViewHolder, position: Int) {
        val concesionaria = concesionarias[position]
        holder.nombreTextView.text = concesionaria.nombre
        holder.direccionTextView.text = "Dirección: " + concesionaria.direccion
        holder.fechaFundacionTextView.text = "Fecha de Fundación: " + concesionaria.fechaFundacion
        holder.abiertaTextView.text = if (concesionaria.abierta) "Abierta" else "Cerrada"
        holder.numeroAutosTextView.text = "Número de autos: ${concesionaria.numeroAutos}"
        holder.latitudTextView.text = "Latitud: ${concesionaria.latitud}"
        holder.longitudTextView.text = "Longitud: ${concesionaria.longitud}"

    }

    override fun getItemCount(): Int {
        return concesionarias.size
    }
}