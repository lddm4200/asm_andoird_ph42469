package com.example.asm_andoird_ph42469.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.asm_andoird_ph42469.Home;
import com.example.asm_andoird_ph42469.Modal.Response;
import com.example.asm_andoird_ph42469.R;
import com.example.asm_andoird_ph42469.Modal.SanPham;
import com.example.asm_andoird_ph42469.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.viewHolder> {
    private final Context context;
    private List<SanPham> list;
    Home home;


    public SanPhamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
        home = (Home) context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sanpham, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if (position >= 0 && position <= list.size()) {
            SanPham sv = list.get(position);

            holder.txtMaSV.setText("Tên sp: " + sv.getTen());
            holder.txtNameSV.setText("Giá: " + sv.getGia());
            holder.txtDiemTB.setText("Số lượng: " + sv.getSoLuong());

            Glide.with(holder.itemView.getContext())
                    .load(sv.getImage())
                    .into(holder.imgAvatar);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idStudent = sv.get_id();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Cảnh báo");
                    builder.setMessage("Bạn có muốn xóa không?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Call<Response<SanPham>> call = ApiService.apiService.deleteStudent(sv.get_id());
                            call.enqueue(new Callback<Response<SanPham>>() {
                                @Override
                                public void onResponse(Call<Response<SanPham>> call, retrofit2.Response<Response<SanPham>> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().getStatus() == 200){
                                            list.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<SanPham>> call, Throwable t) {
                                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    home.showDialog(context, sv, 0, list);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView txtNameSV, txtMaSV, txtDiemTB;
        ImageView imgAvatar;
        ImageButton btnUpdate, btnDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameSV = itemView.findViewById(R.id.txtNameSV);
            txtMaSV = itemView.findViewById(R.id.txtMaSV);
            txtDiemTB = itemView.findViewById(R.id.txtDiemTB);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


}
