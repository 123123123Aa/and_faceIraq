package net.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.faceiraq.and_faceiraq.R;

import net.faceiraq.and_faceiraq.dialog.MainDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
* Created by Paweł Sałata on 21.04.2017.
* email: psalata9@gmail.com
*/

public class CardsNavigationBarFragment extends Fragment {

    public interface OnCardsNavigationBarActions {
        void onNewPageButtonPressed();
        void onHomeButtonPressed();
        void onCardsButtonPressed();
        int getCardsAmount();
    }

    @Bind(R.id.cardsCountButton)
    TextView cardsCountButton;

    OnCardsNavigationBarActions onCardsNavigationBarActions;

    public static final String TAG = "CardsNavigationBar";

    public CardsNavigationBarFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        try {
            onCardsNavigationBarActions = (OnCardsNavigationBarActions) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCardsNavigationBarActions");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCardsCount();
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

    @OnClick({R.id.homeButton, R.id.previousPageButton, R.id.menuDotsButtonExtended, R.id.cardsCountButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                onCardsNavigationBarActions.onHomeButtonPressed();
                break;
            case R.id.previousPageButton:
                onCardsNavigationBarActions.onNewPageButtonPressed();
                break;
            case R.id.cardsCountButton:
                onCardsNavigationBarActions.onCardsButtonPressed();
            case R.id.menuDotsButtonExtended:
                MainDialogFragment dialogFragment = new MainDialogFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
        }
    }


    public void updateCardCount(int cardsCount) {
        if(cardsCount != 0 && cardsCountButton != null) {
            cardsCountButton.setText(String.valueOf(cardsCount));
        }
    }

    private void updateCardsCount() {
        if(onCardsNavigationBarActions.getCardsAmount() != 0) {
            cardsCountButton.setText(String.valueOf(onCardsNavigationBarActions.getCardsAmount()));
        }
    }

}
