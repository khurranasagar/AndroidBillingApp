package com.example.sagar.billingapplication;

/**
 * Created by Sagar on 2/3/2018.
 */

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 2/3/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Product> Products;
    public Invoice CurrentInvoice;
    public Context context;
    public String quantity;

    public ItemAdapter(ArrayList<Product> P, Invoice CurrentInvoice, Context x){
        this.Products = P;
        this.CurrentInvoice = CurrentInvoice;
        this.context = x;
    }

    public Invoice getInvoice(){
        return CurrentInvoice;
    }

    public void updateInvoice(Invoice i){
        this.CurrentInvoice = i;
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemviewlayout,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Product p = Products.get(position);
        holder.ProductName.setText(p.getName());
        holder.MRP.setText("MRP : " + p.getMRP());
        holder.Price.setText("Retail : " + p.getRetailRate() + "| Wholesale : " + p.getWholesaleRate());
        holder.linlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Quantity");
                alert.setMessage("Enter Quantity");
                final AlertDialog dialog = alert.create();
                //CurrentInvoice.addProduct(p, 5);
                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        input.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                input.requestFocus();
                alert.setView(input);


                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                quantity = input.getText().toString();
                                int q = Integer.parseInt(quantity);
                                CurrentInvoice.addProduct(p,q);
                            }
                        });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();

                // show it
                alertDialog.show();

            }
        });
    }

    public void setfilter(List<Product> FilteredProducts){

        Products  = new ArrayList<Product>();
        Products.addAll(FilteredProducts);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return Products.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView ProductName;
        TextView MRP;
        TextView Price;
        LinearLayout linlayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ProductName = (TextView) itemView.findViewById(R.id.ProductName);
            MRP = (TextView) itemView.findViewById(R.id.MRP);
            Price = (TextView) itemView.findViewById(R.id.Price);
            linlayout = (LinearLayout) itemView.findViewById(R.id.itemviewlinearlayout);
        }
    }


}
