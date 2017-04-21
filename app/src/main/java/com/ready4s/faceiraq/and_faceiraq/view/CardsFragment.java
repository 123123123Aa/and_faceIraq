package com.ready4s.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutualmobile.cardstack.CardStackAdapter;
import com.mutualmobile.cardstack.CardStackLayout;
import com.ready4s.faceiraq.and_faceiraq.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsFragment extends Fragment {

    @Bind(R.id.cardStack)
    CardStackLayout cardStackLayout;


    public CardsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cards_fragment, container,  false);

        ButterKnife.bind(this, view);
        init();

        return view;
    }

    private void init() {
        cardStackLayout.setShowInitAnimation(true);
        cardStackLayout.setParallaxEnabled(true);
        cardStackLayout.setAdapter(new CardsAdapter(getActivity()));
    }

    private static class CardsAdapter extends CardStackAdapter {

        private List<String> cardsTitles;
        private LayoutInflater inflater;
        private Context context;

        public CardsAdapter(Context context) {
            super(context);
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.cardsTitles = new ArrayList<>();
            cardsTitles.add("Facebook");
            cardsTitles.add("FaceIraq");
            cardsTitles.add("Google");
            cardsTitles.add("TakNieNieIstotne");
        }

        @Override
        public View createView(int position, ViewGroup container) {
            CardView root = (CardView) inflater.inflate(R.layout.card_web_view, container, false);
            root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            TextView cardTitle = (TextView) root.findViewById(R.id.cardTitle);
            cardTitle.setText(cardsTitles.get(position));
            return root;
        }

        @Override
        public int getCount() {
            return cardsTitles.size();
        }
    }

}
