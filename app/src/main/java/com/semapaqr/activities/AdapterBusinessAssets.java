package com.semapaqr.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.semapaqr.R;


import java.util.ArrayList;

public class AdapterBusinessAssets extends RecyclerView.Adapter<AdapterBusinessAssets.HolderBusinessAssets> {

    private Context context;
    private ArrayList<ModelBusinessAssets> businessAssetsList;

    public AdapterBusinessAssets(Context context, ArrayList<ModelBusinessAssets> businessAssetsList) {
        this.context = context;
        this.businessAssetsList = businessAssetsList;
    }

    @NonNull
    @Override
    public HolderBusinessAssets onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_business_asset, parent, false);
        return new HolderBusinessAssets(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBusinessAssets holder, int position) {

        ModelBusinessAssets model = businessAssetsList.get(position);
        String id = model.getId();
        String codigo_activo = model.getCodigo_activo();
        String nombre_activo = model.getNombre_activo();
        String tipo_activo = model.getTipo_activo();
        String descripcion_activo = model.getDescripcion_activo();
        String sector_activo = model.getSector_activo();
        String encargado_activo = model.getEncargado_activo();
        String addedTime = model.getAddedTime();
        String updatedTime = model.getUpdatedTime();

        holder.codeBATv.setText(codigo_activo);
        holder.attendantTv.setText(encargado_activo);

        //handle item clicks (go to details records.activity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass record id to next activity to show details of that record
                Intent intent = new Intent(context, BusinessAssetDetailActivity.class);
                intent.putExtra("RECORD_BARCODE", codigo_activo);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return businessAssetsList.size(); //retorno del tamaño de la lista de activos
    }

    class HolderBusinessAssets extends RecyclerView.ViewHolder{

        TextView codeBATv;
        TextView attendantTv;
        public HolderBusinessAssets(@NonNull View itemView){
            super(itemView);

            //iniciando vistas de la card
            codeBATv = itemView.findViewById(R.id.codeBATv);
            attendantTv = itemView.findViewById(R.id.attendantTv);
        }
    }

}
