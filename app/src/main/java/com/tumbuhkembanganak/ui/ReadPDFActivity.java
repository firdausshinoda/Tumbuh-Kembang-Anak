package com.tumbuhkembanganak.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tumbuhkembanganak.databinding.ActivityReadPdfactivityBinding;

import com.tumbuhkembanganak.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadPDFActivity extends AppCompatActivity {

    private ActivityReadPdfactivityBinding binding;
    private Context context;
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    private int index = 0;

    private ImageView imv_pdf;
    private TextView tv_page_now;
    private TextView tv_page_total;
    private MaterialButton btn_next;
    private MaterialButton btn_prev;
    private String judul, file_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadPdfactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("File PDF");
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        context = this;

        Bundle b = getIntent().getExtras();
        judul = b.getString("judul");
        file_pdf = b.getString("file_pdf");

        initView();
        openRenderer();
    }

    private void initView() {
        imv_pdf = binding.contentPanel.imvPdf;
        tv_page_now = binding.contentPanel.tvPageNow;
        tv_page_total = binding.contentPanel.tvPageTotal;
        btn_next = binding.contentPanel.btnNext;
        btn_prev = binding.contentPanel.btnPrev;

        btn_next.setOnClickListener(v -> showPage(mCurrentPage.getIndex() + 1));
        btn_prev.setOnClickListener(v -> showPage(mCurrentPage.getIndex() - 1));
    }

    private void openRenderer() {
        String nama_file = file_pdf;
        File file = new File(getFilesDir(), nama_file);
        try
        {
            try {
                mPdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            } catch (IOException e) {
                e.printStackTrace();
            }
            showPage(index);

        } catch (Exception e) {
            Toast.makeText(this, "Eror saat membuka file.", Toast.LENGTH_SHORT).show();
            Log.e("tag", e.getMessage());
        }
    }

    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        imv_pdf.setImageBitmap(bitmap);
        updateUi();

        int page = index+1;

        if (page == 1 && mPdfRenderer.getPageCount() ==1){
            btn_next.setBackgroundColor(getResources().getColor(R.color.purplePrimary));
            btn_prev.setBackgroundColor(getResources().getColor(R.color.purplePrimary));
        } else if (page == 1 && mPdfRenderer.getPageCount() > 1){
            btn_next.setBackgroundColor(getResources().getColor(R.color.purpleSoft));
            btn_prev.setBackgroundColor(getResources().getColor(R.color.purplePrimary));
        } else if (page > 1 && page < mPdfRenderer.getPageCount()){
            btn_next.setBackgroundColor(getResources().getColor(R.color.purpleSoft));
            btn_prev.setBackgroundColor(getResources().getColor(R.color.purpleSoft));
        } else {
            btn_next.setBackgroundColor(getResources().getColor(R.color.purplePrimary));
            btn_prev.setBackgroundColor(getResources().getColor(R.color.purpleSoft));
        }
        Log.d("catatan",page+"------"+mPdfRenderer.getPageCount());
    }

    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();
        btn_prev.setEnabled(0 != index);
        btn_next.setEnabled(index + 1 < pageCount);

        tv_page_now.setText(String.valueOf(index + 1));
        tv_page_total.setText(String.valueOf(pageCount));
    }
}