package com.ready4s.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutualmobile.cardstack.CardStackAdapter;
import com.mutualmobile.cardstack.CardStackLayout;
import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.controller.CardsActivity;
import com.ready4s.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsFragment extends Fragment {

    public static final String TAG = "CardsFragment";


    @Bind(R.id.cardStack)
    RecyclerView cardStackLayout;
//    CardStackLayout cardStackLayout;
    private static final int VERTICAL_MARGIN = -1000;

    private OpenedPagesDAO openedPagesDAO;
    private List<OpenedPageModel> openedPages;
    private CardStackRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public CardsFragment() {
        openedPagesDAO = new OpenedPagesDAO();
        openedPages = openedPagesDAO.getOpenedPages();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutManager = new LinearLayoutManager(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cards_recycler_fragment, container, false);
        ButterKnife.bind(this, view);

//        init();
        init();


        return view;
    }

    private void init() {
        cardStackLayout.setLayoutManager(layoutManager);
        adapter = new CardStackRecyclerAdapter(openedPages, getContext());
        cardStackLayout.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_MARGIN));
        cardStackLayout.setAdapter(adapter);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = verticalSpaceHeight;
            }
        }
    }

//    private void init() {
//        adapter = new CardsAdapter(getActivity(), openedPages);
//        cardStackLayout.setShowInitAnimation(true);
//        cardStackLayout.setParallaxEnabled(true);
//        cardStackLayout.setAdapter(adapter);
//    }
//
//    public void openNewPage(long cardID) {
//        OpenedPageModel pageModel = new OpenedPageModel();
//        pageModel.setId(cardID);
//        pageModel.setUrl(getResources().getString(R.string.HOME_PAGE_ADDRESS));
//        openedPages.add(pageModel);
//        adapter = new CardsAdapter(getActivity(), openedPages);
//        cardStackLayout.removeAdapter();
//        cardStackLayout.setAdapter(adapter);
//    }

    private void removePageAt(int position) {
        ((CardsActivity)getActivity()).onCardDeleted(openedPages.get(position).getId());
//        adapter = new CardsAdapter(getActivity(), openedPages);
//        cardStackLayout.removeAdapter();
//        cardStackLayout.setAdapter(adapter);
//        adapter.removeViewAt(position);
    }

    private class CardsAdapter extends CardStackAdapter {

        private List<OpenedPageModel> openedPages;
        private LayoutInflater inflater;
        private Context context;
        private ViewGroup container;

        public CardsAdapter(Context context, List<OpenedPageModel> openedPages) {
            super(context);
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.openedPages = new ArrayList<>(openedPages);
        }

        @Override
        public View createView(final int position, ViewGroup container) {
            this.container = container;
            CardView root = (CardView) inflater.inflate(R.layout.card_web_view, container, false);
            root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            TextView cardTitle = (TextView) root.findViewById(R.id.cardTitle);
            cardTitle.setText(openedPages.get(position).getTitle());
            ImageView imageView = (ImageView) root.findViewById(R.id.cardContent);
            byte[] screenshotByteArray = openedPages.get(position).getScreenshot();
            try {
                imageView.setImageBitmap(ImageUtil.convertToBitmap(screenshotByteArray));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageView deleteIcon = (ImageView) root.findViewById(R.id.deleteIcon);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePageAt(position);
                }
            });
            return root;
        }


//        public void removeViewAt(final int position) {
//            float nextViewY = container.getChildAt(position).getY();
//            container.removeViewAt(position);
//            for (int i = position; i < getCount(); i++) {
//                float tmp = container.getChildAt(i).getY();
//                container.getChildAt(i).setY(nextViewY);
//                nextViewY = tmp;
//            }
//        }

        public void removeViewAt(final int position) {
            container.removeViewAt(position);
            openedPages.remove(position);
            resetCards();
        }

        @Override
        public int getCount() {
            return openedPages.size();
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
