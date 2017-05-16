package net.faceiraq.and_faceiraq.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.faceiraq.and_faceiraq.R;
import net.faceiraq.and_faceiraq.api.ApiCalls;
import net.faceiraq.and_faceiraq.api.ApiManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 19.04.2017.
 */

public class MainDialogFragment extends DialogFragment implements IMainDialogFragment{

    @Bind(R.id.dialog_recycler_view)
    RecyclerView mDialogRecyclerView;
//    @Bind(R.id.dialog_button)
//    Button mCancelButton;

    private ApiCalls api;


    public interface OnMainDialogActionsListener {
        public void onOpenedNewPage();
    }

    private MainDialogAdapter mDialogAdapter;
    private OnMainDialogActionsListener listener;
    private Activity mActivity;
    private int themeColour;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnMainDialogActionsListener) context;
            mActivity = (Activity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMainDialogActionsListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initWindowProperties();
        View view = inflater.inflate(R.layout.dialog_main, container, false);
        ButterKnife.bind(this, view);


        mDialogRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        changeButtonColor();
        mDialogAdapter = new MainDialogAdapter(listener, themeColour);
        mDialogAdapter.onViewAttached(this);
        mDialogRecyclerView.setAdapter(mDialogAdapter);
        initApi();
        return view;

    }

    private void initApi() {
        ApiManager.init(mActivity);
        api = ApiManager.get().getApi();
    }

    private void initWindowProperties() {
        Window window = getDialog().getWindow();

        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    private void changeButtonColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        themeColour = typedValue.data;
//        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.btn_round_grey);
//        drawable.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
//        mCancelButton.setBackground(drawable);
    }

//    @Override
//    public void allowPushService() {
//        mDialogAdapter.setSwitchSelection();
//        if (SharedPreferencesHelper.getChanged(mActivity)) {
//
//            PushDetails pushDetails = new PushDetails();
//            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//            String uuid = telephonyManager.getDeviceId();
//            pushDetails.setActive(SharedPreferencesHelper.getChecked(mActivity));
//            pushDetails.setUuid(uuid);
//            Call<PushResponse> call = api.allowPushService(pushDetails);
//            call.enqueue(new Callback<PushResponse>() {
//                @Override
//                public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {
//                    if (response.code() == 200) {
//                        Toast.makeText(mActivity, "ok", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(mActivity, "not good", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<PushResponse> call, Throwable t) {
//                    Toast.makeText(mActivity, "not ok", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        mDialogAdapter.onViewAttached(this);
//        allowPushService();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDialogAdapter.onViewDetached();
    }

    //    @OnClick(R.id.dialog_button)
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.dialog_button:
//                dismiss();
//                break;
//        }
//    }

    @Override
    public void onPageSelected() {
        mDialogAdapter.setSwitchSelection();
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDialogAdapter.setSwitchSelection();

    }
}
