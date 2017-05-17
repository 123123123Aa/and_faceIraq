package net.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import net.faceiraq.and_faceiraq.model.utils.PageUrlValidator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsFragment extends Fragment {

    public static final String TAG = "CardsFragment";


    @Bind(R.id.cardStack)
    CardStackLayout cardStackLayout;

    private OpenedPagesDAO openedPagesDAO;
    private List<OpenedPageModel> openedPages;
    private CardsAdapter adapter;

    public CardsFragment() {
        openedPagesDAO = new OpenedPagesDAO();
        openedPages = openedPagesDAO.getOpenedPages();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cards_fragment, container, false);
        ButterKnife.bind(this, view);
        init();

        return view;
    }

    private void init() {
        adapter = new CardsAdapter(getActivity(), openedPages);
        cardStackLayout.setShowInitAnimation(true);
        cardStackLayout.setParallaxEnabled(true);
        cardStackLayout.setAdapter(adapter);
    }

    public void openNewPage(long cardID) {
        OpenedPageModel pageModel = new OpenedPageModel();
        pageModel.setId(cardID);
        pageModel.setUrl(getResources().getString(R.string.HOME_PAGE_ADDRESS));
        openedPages.add(pageModel);
        adapter = new CardsAdapter(getActivity(), openedPages);
        cardStackLayout.removeAdapter();
        cardStackLayout.setAdapter(adapter);
    }

    private void removePageAt(int position) {
        ((CardsActivity)getActivity()).onCardDeleted(openedPages.get(position).getId());
        openedPages.remove(position);
        adapter = new CardsAdapter(getActivity(), openedPages);
        cardStackLayout.removeAdapter();
        cardStackLayout.setAdapter(adapter);
    }

    private class CardsAdapter extends CardStackAdapter {

        private List<OpenedPageModel> openedPages;
        private LayoutInflater inflater;
        private Context context;

        public CardsAdapter(Context context, List<OpenedPageModel> openedPages) {
            super(context);
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.openedPages = new ArrayList<>(openedPages);
        }

        @Override
        public View createView(final int position, ViewGroup container) {
            CardView root = (CardView) inflater.inflate(R.layout.card_web_view, container, false);
            root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            TextView cardTitle = (TextView) root.findViewById(R.id.cardTitle);
            String cardRawUrl = PageUrlValidator.getRawUrl(openedPages.get(position).getUrl());
            cardTitle.setText(cardRawUrl);
            ImageView imageView = (ImageView) root.findViewById(R.id.cardContent);
            RelativeLayout mTitleSectionRl = (RelativeLayout) root.findViewById(R.id.card_title_section);
            mTitleSectionRl.setBackgroundColor(getThemeColour());
            byte[] screenshotByteArray = openedPages.get(position).getScreenshot();
            Glide.with(context)
                    .load(screenshotByteArray)
                    .centerCrop()
                    .into(imageView);

            RelativeLayout deleteIcon = (RelativeLayout) root.findViewById(R.id.deleteIconSection);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (openedPages.size() > 1) {
                        removePageAt(position);
                    }
                }
            });
            return root;
        }

        @Override
        public int getCount() {
            return openedPages.size();
        }

        private int getThemeColour() {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int themeColour = typedValue.data;
            return themeColour;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            int position = getSelectedCardPosition();
            SharedPreferencesHelper.setCardNumber(
                    context,
                    openedPages.get(position).getId());
            SharedPreferencesHelper.setCardUrl(
                    context,
                    openedPages.get(position).getUrl());
            Log.d(TAG,
                    "onClick: position=" + position +
                    ", id=" + openedPages.get(position).getId() +
                    ", url=" + openedPages.get(position).getUrl());
            ((CardsActivity)context).onCardSelected();
        }
    }

}
