package net.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mutualmobile.cardstack.CardStackAdapter;
import com.mutualmobile.cardstack.CardStackLayout;

import net.faceiraq.and_faceiraq.R;
import net.faceiraq.and_faceiraq.controller.CardsActivity;
import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import net.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import net.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import net.faceiraq.and_faceiraq.utils.PageUrlValidator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.mrapp.android.tabswitcher.Animation;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherListener;

import static net.faceiraq.and_faceiraq.R.id.cardStack;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsFragment extends Fragment implements TabSwitcherListener {

    public static final String TAG = "CardsFragment";

    private OpenedPagesDAO openedPagesDAO;
    private List<OpenedPageModel> openedPages;
    private Context context;
    TabSwitcher cardStack;


    public CardsFragment() {
        openedPagesDAO = new OpenedPagesDAO();
        openedPages = openedPagesDAO.getOpenedPages();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cards_fragment, container, false);
        ButterKnife.bind(this, view);
        cardStack = (TabSwitcher) view.findViewById(R.id.cardStack);
        init();

//        setPadding();
        return view;
    }

    private void setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(cardStack, new OnApplyWindowInsetsListener() {

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                cardStack.setPadding(insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                return insets;
            }

        });
    }

    private void init() {
        cardStack.setDecorator(new CardStackDecorator());
        cardStack.addListener(this);
        Log.d(TAG, "init: openedPagesCount=" + openedPages.size() );
        for (int i = 0; i < openedPages.size(); i++) {
            Tab tab = new Tab("title" + i);
            tab.setCloseable(true);
            tab.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            cardStack.addTab(tab);
        }
    }

    public void openNewPage(long cardID) {
        Tab tab = new Tab(String.valueOf(cardID));
        cardStack.addTab(tab);
    }

    @Override
    public void onSwitcherShown(@NonNull TabSwitcher tabSwitcher) {

    }

    @Override
    public void onSwitcherHidden(@NonNull TabSwitcher tabSwitcher) {

    }

    @Override
    public void onSelectionChanged(@NonNull TabSwitcher tabSwitcher, int selectedTabIndex, @Nullable Tab selectedTab) {

    }

    @Override
    public void onTabAdded(@NonNull TabSwitcher tabSwitcher, int index, @NonNull Tab tab, @NonNull Animation animation) {

    }

    @Override
    public void onTabRemoved(@NonNull TabSwitcher tabSwitcher, int index, @NonNull Tab tab, @NonNull Animation animation) {

    }

    @Override
    public void onAllTabsRemoved(@NonNull TabSwitcher tabSwitcher, @NonNull Tab[] tabs, @NonNull Animation animation) {

    }
}
