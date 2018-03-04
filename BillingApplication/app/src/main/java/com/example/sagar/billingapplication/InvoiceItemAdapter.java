package com.example.sagar.billingapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Sagar on 3/4/2018.
 */

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.ItemViewHolder>{

    private ArrayList<Product> Products;
    private LinkedHashMap<Product,Integer> ListofProducts;
    private Invoice CurrentInvoice;
    private Context context;
    private Product PresentProduct;

    public Invoice getInvoice(){
        return CurrentInvoice;
    }

    public void updateInvoice(Invoice i){
        this.CurrentInvoice = i;
        this.Products = new ArrayList<Product>(CurrentInvoice.getListOfProducts().keySet());
        this.ListofProducts = CurrentInvoice.getListOfProducts();
    }

    public InvoiceItemAdapter(Invoice CurrentInvoice, Context x){
        this.CurrentInvoice = CurrentInvoice;
        this.Products = new ArrayList<Product>(CurrentInvoice.getListOfProducts().keySet());
        this.ListofProducts = CurrentInvoice.getListOfProducts();
        this.context = x;
    }

    @Override
    public InvoiceItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.invoiceitemviewlayout,parent,false);
        return new InvoiceItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvoiceItemAdapter.ItemViewHolder holder, int position) {
        final Product p = Products.get(position);
        final int q = ListofProducts.get(p);
            holder.ProductName.setText(p.getName());
            holder.MRP.setText("MRP : " + p.getMRP());
            holder.Price.setText("Retail : " + p.getRetailRate() + "| Wholesale : " + p.getWholesaleRate());
            holder.Quantity.setText("Quantity : " + Integer.toString(q));
            PresentProduct = p;

        holder.linlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Delete Product ?");
                final AlertDialog dialog = alert.create();


                alert.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                CurrentInvoice.removeProduct(PresentProduct);
                                setfilter(new ArrayList<Product>(CurrentInvoice.getListOfProducts().keySet()));
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
        TextView Quantity;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ProductName = (TextView) itemView.findViewById(R.id.ProductName);
            MRP = (TextView) itemView.findViewById(R.id.MRP);
            Price = (TextView) itemView.findViewById(R.id.Price);
            linlayout = (LinearLayout) itemView.findViewById(R.id.itemviewlinearlayout);
            Quantity = (TextView) itemView.findViewById(R.id.Quantity);
        }
    }
}
