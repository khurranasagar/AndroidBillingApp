package com.example.sagar.billingapplication;

import android.content.ClipData;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import static android.support.v4.view.MenuItemCompat.*;
import static android.support.v4.view.MenuItemCompat.getActionView;

public class ItemSearch extends AppCompatActivity {

    public ArrayList<Product> Products;

    public String CustName, CustAddr, CustPhone;
    public Invoice PresentInvoice;
    char type;
    public RecyclerView ItemList;
    public RecyclerView InvoiceList;
    public ItemAdapter adapter;
    public InvoiceItemAdapter Invoiceadapter;
    private List<Product> CurrentProducts;
    private int position;
    public SlidingUpPanelLayout SlideupPanel;
    private DecimalFormat df = new DecimalFormat("#####0.00");
    private TextView CustNameText;
    private TextView InvoiceTotalText;



    public void updateProducts() {
        //Products.clear();
        try {
            BufferedReader filein = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.items)));
            String sCurrentLine;
            sCurrentLine = filein.readLine();       // Skip Header
            while ((sCurrentLine = filein.readLine()) != null) {
                String s[] = sCurrentLine.split(",");
                Product p = new Product(s[0], Double.parseDouble(s[2]), Double.parseDouble(s[3]), Double.parseDouble(s[1]));
                Products.add(p);
            }
            filein.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        CustName = (String) getIntent().getStringExtra("CustomerName");
        if(CustName.equals("")){
            CustName = "Cash";
        }
        CustPhone = (String) getIntent().getStringExtra("CustomerPhone");
        CustAddr = (String) getIntent().getStringExtra("CustomerAddress");
        type = (char) getIntent().getCharExtra("Type", 'W');

        CustNameText = findViewById(R.id.CustNameText);
        InvoiceTotalText = findViewById(R.id.InvoiceTotalText);

        CustNameText.setText(CustName);

        SlideupPanel = (SlidingUpPanelLayout) findViewById(R.id.Slideup);

        PresentInvoice = new Invoice(CustName, CustPhone, CustAddr, type);
        InvoiceTotalText.setText(Double.toString(PresentInvoice.getTotal()));
        Products = new ArrayList<Product>();
        updateProducts();
        ItemList = (RecyclerView) findViewById(R.id.InvoiceItemSearchList);
        ItemList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(Products,PresentInvoice,this);
        ItemList.setAdapter(adapter);

        InvoiceList = (RecyclerView) findViewById(R.id.InvoiceList);
        InvoiceList.setLayoutManager(new LinearLayoutManager(this));
        Invoiceadapter = new InvoiceItemAdapter(PresentInvoice,this);
        InvoiceList.setAdapter(Invoiceadapter);

        SlideupPanel.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                InvoiceTotalText.setText(Double.toString(PresentInvoice.getTotal()));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    PresentInvoice = adapter.getInvoice();
                    Invoiceadapter.updateInvoice(PresentInvoice);
                    Invoiceadapter.setfilter(new ArrayList<Product>(PresentInvoice.getListOfProducts().keySet()));
                }
                if(newState == SlidingUpPanelLayout.PanelState.HIDDEN) {
                    PresentInvoice = Invoiceadapter.getInvoice();
                    adapter.updateInvoice(PresentInvoice);
                    Invoiceadapter.setfilter(new ArrayList<Product>(PresentInvoice.getListOfProducts().keySet()));
                }
            }
        });

        SlideupPanel.setPanelHeight(148);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PresentInvoice.setPaidAmount(0);
//                PresentInvoice.setNetAmount(PresentInvoice.getTotal() - PresentInvoice.getPaidAmount());
//                String outputFile = ("C:/Users/Sagar/Downloads/Invoices/Invoice " + PresentInvoice.getCustomerName() + ".pdf");
//
//                Document document = new Document(PageSize.A5);
//                try {
//                    @SuppressWarnings("unused")
//                    PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(outputFile));
////			writer.setPageEvent(new PDFPageEventAddInvoiceName());
//                    document.setMargins(20f,20f,10f,10f);
//                    document.open();
//                    PdfPCell cell;
//                    PdfPTable table;
//
//                    Paragraph p = new Paragraph("Rough Estimate", FontFactory.getFont("Arial",14, Font.BOLD));
//                    p.setAlignment(Element.ALIGN_CENTER);
//                    document.add(p);
//
//                    table = new PdfPTable(3);
//                    cell = new PdfPCell(new Paragraph("Name",FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Paragraph(PresentInvoice.getCustomerName(),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(2);
//                    table.addCell(cell);
//                    table.setSpacingBefore(5);
//
//                    cell = new PdfPCell(new Paragraph("Date & Time",FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(PresentInvoice.getDate(),FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(PresentInvoice.getTime(),FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Contact",FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(PresentInvoice.getContact(),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(2);
//                    table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Address",FontFactory.getFont("Arial",11)));
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(PresentInvoice.getAddress(),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(2);
//                    table.addCell(cell);
//                    table.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    document.add(table);
//
//
//
//
//                    table = new PdfPTable(17);
//                    table.setSpacingBefore(5);
//                    table.setSpacingAfter(7);
//                    table.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    table.setWidthPercentage(100);
//
//                    cell = new PdfPCell(new Phrase("Qty",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(1);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("Description of Goods",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(9);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("MRP",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(2);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("Rate",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(2);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("Price",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(3);
//                    table.addCell(cell);
//
//                    Map<Product,Integer> Products = PresentInvoice.getListOfProducts();
//                    int i = 0;
//                    for(Map.Entry<Product, Integer> entry : Products.entrySet()){
//                        if(PresentInvoice.getType() == 'W'){
//
////					cell = new PdfPCell(new Phrase(Integer.toString(i+1),FontFactory.getFont("Arial",11)));
////			        cell.setColspan(2);
////			        table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(Integer.toString(entry.getValue()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(1);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(entry.getKey().getName(),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(9);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getMRP()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(2);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getWholesaleRate()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(2);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getWholesaleRate()* entry.getValue()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(3);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//
//                        }else if(PresentInvoice.getType() == 'R'){
//
////					cell = new PdfPCell(new Phrase(Integer.toString(i+1),FontFactory.getFont("Arial",11)));
////			        cell.setColspan(2);
////			        table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(Integer.toString(entry.getValue()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(1);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(entry.getKey().getName(),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(9);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getMRP()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(2);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getRetailRate()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(2);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//                            cell = new PdfPCell(new Phrase(df.format(entry.getKey().getRetailRate()* entry.getValue()),FontFactory.getFont("Arial",11)));
//                            cell.setColspan(3);
//                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            table.addCell(cell);
//
//                        }
//                        i++;
//                    }
//
//                    cell = new PdfPCell(new Phrase("Grand Total",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(14);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(df.format(PresentInvoice.getTotal()),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(3);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Advance",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(14);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(df.format(PresentInvoice.getPaidAmount()),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(3);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//
//                    if(PresentInvoice.getRoundOff() > 0){
//                        cell = new PdfPCell(new Phrase("Round Off",FontFactory.getFont("Arial",11)));
//                        cell.setColspan(14);
//                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("+ " + df.format(PresentInvoice.getRoundOff()),FontFactory.getFont("Arial",11)));
//                        cell.setColspan(3);
//                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        table.addCell(cell);
//                    }else if(PresentInvoice.getRoundOff() < 0){
//                        cell = new PdfPCell(new Phrase("Round Off",FontFactory.getFont("Arial",11)));
//                        cell.setColspan(14);
//                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("- " + df.format(-1 * (PresentInvoice.getRoundOff())),FontFactory.getFont("Arial",11)));
//                        cell.setColspan(3);
//                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        table.addCell(cell);
//                    }
//
//                    cell = new PdfPCell(new Phrase("Net Amount",FontFactory.getFont("Arial",12,Font.BOLD)));
//                    cell.setColspan(14);
//                    cell.setRowspan(2);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(df.format(PresentInvoice.getNetAmount()),FontFactory.getFont("Arial",12,Font.BOLD)));
//                    cell.setColspan(3);
//                    cell.setRowspan(2);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);
//
//                    document.add(table);
//                    Phrase noitems = new Phrase("Total Items",FontFactory.getFont("Arial",11,Font.BOLD));
//                    table = new PdfPTable(5);
//                    cell = new PdfPCell(noitems);
//                    cell.setColspan(1);
//                    table.addCell(cell);
//                    cell = new PdfPCell((new Phrase(((Integer)i).toString(),FontFactory.getFont("Arial",11,Font.BOLD))));
//                    cell.setColspan(4);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Paragraph("Amount",FontFactory.getFont("Arial",11)));
//                    cell.setColspan(1);
//                    cell.setRowspan(2);
//                    table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(PresentInvoice.getWordTotal().toUpperCase(),FontFactory.getFont("Arial",11)));
//                    cell.setColspan(4);
//                    cell.setRowspan(2);
//                    table.addCell(cell);
////			cell = new PdfPCell(new Paragraph("Remarks",FontFactory.getFont("Arial",11)));
////			cell.setColspan(1);
////			cell.setRowspan(2);
////			table.addCell(cell);
////			cell = new PdfPCell(new Phrase(remarks,FontFactory.getFont("Arial",11)));
////			cell.setColspan(4);
////			cell.setRowspan(2);
////			table.addCell(cell);
//                    table.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    table.setWidthPercentage(100);
//                    document.add(table);
//
//
//                    document.close();
//
//                }catch (FileNotFoundException | DocumentException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//            }
//            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchfile, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Product> FilteredList = filter(newText);
                adapter.setfilter(FilteredList);
                return false;
            }
        });
        return true;
    }

    public List<Product> filter(String query) {

        List<Product> SearchItemResultsList = new ArrayList<Product>();
        int i = 0;
        String[] s = query.split(" ");
        ArrayList<String> search = new ArrayList<String>(Arrays.asList(s));
        ArrayList<String> Pro;
        int k = 0, l = 0;
        int flag = 0;
        //SearchItemResultsList.clear();
        for (Product p : Products) {
            if (p.getName().toLowerCase().startsWith(query)) {
                if (!SearchItemResultsList.contains(p))
                    SearchItemResultsList.add(p);
            }
        }
        for (Product p : Products) {
            Pro = new ArrayList<String>(Arrays.asList(p.getName().toLowerCase().split(" ")));
            for (String s1 : search) {
                for (l = 0; l < Pro.size(); l++) {
                    if (Pro.get(l).equals(s1)) {
                        if (!SearchItemResultsList.contains(p))
                            SearchItemResultsList.add(p);
                    }
                }
            }
        }
        for (Product p : Products) {
            Pro = new ArrayList<String>(Arrays.asList(p.getName().toLowerCase().split(" ")));
            for (String s1 : search) {
                flag = 0;
                for (l = 0; l < Pro.size(); l++) {
                    if (!Pro.get(l).startsWith(s1)) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    if (!SearchItemResultsList.contains(p))
                        SearchItemResultsList.add(p);
                }
            }
        }
        for (Product p1 : Products) {
            flag = 0;
            for (String s1 : search) {
                if (!p1.getName().toLowerCase().contains(s1)) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                if (!SearchItemResultsList.contains(p1))
                    SearchItemResultsList.add(p1);
            }
        }
        for (Product p : Products) {
            Pro = new ArrayList<String>(Arrays.asList(p.getName().split(" ")));
            flag = 0;
            for (l = 0; l < Pro.size(); l++) {
                for (k = 0; k < search.size(); k++) {
                    if (Pro.get(l).toLowerCase().startsWith(search.get(k).toLowerCase())) {
                        flag = 1;
                    }
                }
            }
            if (flag == 1) {
                if (!SearchItemResultsList.contains(p))
                    SearchItemResultsList.add(p);
            }
        }

        return SearchItemResultsList;

    }

    //for changing the text color of searchview
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

}
