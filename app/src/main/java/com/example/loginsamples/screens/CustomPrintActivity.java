package com.example.loginsamples.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.loginsamples.database.AppDatabase;
import com.example.loginsamples.R;
import com.example.loginsamples.adapter.MyRecyclerViewAdapter;
import com.example.loginsamples.adapter.RvAdapterCallbacks;
import com.example.loginsamples.entity.ListItem;
import com.google.zxing.WriterException;
import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.GrayLevelEnum;
import com.nexgo.oaf.apiv3.device.printer.Printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomPrintActivity extends AppCompatActivity {

    @BindView(R.id.buttonPrint)
    Button buttonPrint;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ibAdd)
    ImageButton ibAdd;

    private MyRecyclerViewAdapter adapter;
    public static AppDatabase database;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_print);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "RoomExampleDb")
                .allowMainThreadQueries()
                .build();

        ButterKnife.bind(this);

        recyclerViewInit();
        refreshDataTable();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(findViewById(R.id.rvData));

    }

    // Adds new item in list on button click
    @OnClick(R.id.ibAdd)
    public void setBtnAdd() {
        adapter.getItems().add(ListItem.getGenereicItem());
        adapter.notifyDataSetChanged();
        //database.listItemDao().insertAll(lastItem);
        //refreshDataTable();
        adapter.rvData.smoothScrollToPosition(adapter.getItemCount() - 1);
    }


    @OnClick(R.id.buttonPrint)
    public void setBtnPrint() {
        try {
            ispisNaRacunuNexgo(getPrinter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btnSave)
    public void setBtnSave() {
        database.listItemDao().insertAll(adapter.getItems());
        adapter.notifyDataSetChanged();
        Toast.makeText(getBaseContext(), R.string.toast_save, Toast.LENGTH_SHORT).show();
    }


    public void ispisNaRacunuNexgo(final com.nexgo.oaf.apiv3.device.printer.Printer servis) {
        List<ListItem> list = adapter.getItems();
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).isPrintable() && list.get(j).isLined()) {
                servis.appendPrnStr(list.get(j).getItemText(), list.get(j).getFontSize(), AlignEnum.LEFT, list.get(j).isBold());
                servis.appendPrnStr("--------------------------------------", 20, AlignEnum.LEFT, false);
            } else if (list.get(j).isPrintable() && list.get(j).getItemText().equalsIgnoreCase("QR")) {
                // below line is for getting
                // the windowmanager service.
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                // initializing a variable for default display.
                Display display = manager.getDefaultDisplay();

                // creating a variable for point which
                // is to be displayed in QR Code.
                Point point = new Point();
                display.getSize(point);

                // getting width and
                // height of a point
                int width = point.x;
                int height = point.y;

                // generating dimension from width and height.
                int dimen = width < height ? width : height;
                dimen = dimen * 3 / 4;

                // setting this dimensions inside our qr code
                // encoder to generate our qr code.
                qrgEncoder = new QRGEncoder(list.get(j).getItemText().toString(), null, QRGContents.Type.TEXT, dimen);
                try {
                    // getting our qrcode in the form of bitmap.
                    bitmap = qrgEncoder.encodeAsBitmap();
                    // the bitmap is set inside our image
                    // view using .setimagebitmap method.
                    servis.appendImage(bitmap, AlignEnum.CENTER);
                } catch (WriterException e) {
                    // this method is called for
                    // exception handling.
                    Log.e("Tag", e.toString());
                }
            } else if (list.get(j).isPrintable()) {
                servis.appendPrnStr(list.get(j).getItemText(), list.get(j).getFontSize(), AlignEnum.LEFT, list.get(j).isBold());
            }
        }
        servis.startPrint(true, i -> Log.d("NEXGO PRINTER", "PRINT ENDED" + i));
    }

    public Printer printer;

    public void startNexgoPrinter() {
        printer = APIProxy.getDeviceEngine().getPrinter();
        printer.initPrinter();
        printer.setGray(GrayLevelEnum.LEVEL_0);
    }


    public Printer getPrinter() {
        if (printer == null) {
            startNexgoPrinter();
        }
        return printer;
    }


    public void refreshDataTable() {
        List<ListItem> listItems = database.listItemDao().getAll();
        adapter.setItems(listItems);
    }


    public void showDeleteItemDialog(ListItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_sentence);
        builder.setPositiveButton(R.string.confirm,
                (dialog, which) -> {
                    database.listItemDao().delete(item);
                    adapter.notifyDataSetChanged();
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Initialize adapter
     */
    public void recyclerViewInit() {
        adapter = new MyRecyclerViewAdapter(this, new RvAdapterCallbacks() {
            @Override
            public void onDeleteSelected(ListItem item) {
                showDeleteItemDialog(item);
            }
        });
        RecyclerView rvData = findViewById(R.id.rvData);
        adapter.rvData = rvData;
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider_in_a_row)));
        rvData.addItemDecoration(divider);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(adapter);
    }


    /**
     * Populates data
     */
    public ArrayList<ListItem> dataPopulation() {
        ArrayList<ListItem> data = new ArrayList<>();
        data.add(new ListItem("Prvi", true, false, false, 24));
        data.add(new ListItem("Drugi", true, false, false, 24));
        data.add(new ListItem("Treci", true, false, false, 24));
        data.add(new ListItem("Cetvrti", true, false, false, 24));
        data.add(new ListItem("Peti", true, false, false, 24));
        data.add(new ListItem("Šesti", true, false, false, 24));
        data.add(new ListItem("Sedmi", true, false, false, 24));
        data.add(new ListItem("Osmi", true, false, false, 24));
        data.add(new ListItem("Deveti", true, false, false, 24));
        return data;
    }

    //Drag and drop
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            adapter.getItems().get(fromPosition).setPosition(toPosition);
            adapter.getItems().get(toPosition).setPosition(fromPosition);
            Collections.swap(adapter.getItems(), fromPosition, toPosition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
            return false;
        }



        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}