package net.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.faceiraq.and_faceiraq.R;

import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherDecorator;

/**
 * Created by Paweł Sałata on 26.05.2017.
 * email: psalata9@gmail.com
 */

public class CardStackDecorator extends TabSwitcherDecorator {


    @NonNull
    @Override
    public View onInflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.opened_page_layout, parent, false);
    }

    @Override
    public void onShowTab(@NonNull Context context, @NonNull TabSwitcher tabSwitcher, @NonNull View view, @NonNull Tab tab, int index, int viewType, @Nullable Bundle savedInstanceState) {
        TextView textView = findViewById(R.id.cardTitle);
        textView.setText(tab.getTitle());
    }

    @Override
    public void onSaveInstanceState(@NonNull View view, @NonNull Tab tab, int index, int viewType, @NonNull Bundle outState) {
        super.onSaveInstanceState(view, tab, index, viewType, outState);
    }

    @Override
    public int getViewType(@NonNull Tab tab, int index) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
