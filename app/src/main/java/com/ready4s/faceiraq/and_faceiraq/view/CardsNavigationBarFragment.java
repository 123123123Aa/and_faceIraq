package com.ready4s.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ready4s.faceiraq.and_faceiraq.R;

import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
* Created by Paweł Sałata on 21.04.2017.
* email: psalata9@gmail.com
*/

public class CardsNavigationBarFragment extends Fragment {

    public static final String TAG = "CardsNavigationBar";

    public CardsNavigationBarFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.cards_navigation_bar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

//        @OnClick({R.id.homeButton, R.id.previousPageButton, R.id.cancel_button, R.id.menuDotsButton})
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.homeButton:
//                    onNavigationBarActionListener.onCardsButtonPressed();
////                onNavigationBarActionListener.onHomeButtonPressed();
//                    break;
//                case R.id.previousPageButton:
//                    onNavigationBarActionListener.onPreviousPageButtonPressed();
//                    break;
//                case R.id.cardsCountButton:
//                    onNavigationBarActionListener.onCardsButtonPressed();
//                case R.id.cancel_button:
//                    mFocusSection.setVisibility(GONE);
//                    mAddressSection.setVisibility(View.VISIBLE);
//                    hideSoftKeyboard();
//                    break;
//                case R.id.menuDotsButton:
//                    MainDialog dialogFragment = new MainDialog();
//                    dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
//
//
//
//
//            }
//        }

}
