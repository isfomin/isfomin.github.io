package com.compassplus.pocketbank.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.TaskStackBuilder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.compassplus.pocketbank.BuildConfig;
import com.compassplus.pocketbank.R;
import com.compassplus.pocketbank.RxDataManager;
import com.compassplus.pocketbank.api.RequestHelper;
import com.compassplus.pocketbank.api.model.GetAcctsResponse;
import com.compassplus.pocketbank.api.model.LogonResponse;
import com.compassplus.pocketbank.api.model.PBRequest;
import com.compassplus.pocketbank.api.model.PBResponse;
import com.compassplus.pocketbank.api.model.PushHistoryCountResponse;
import com.compassplus.pocketbank.api.model.ZSBankNotifications;
import com.compassplus.pocketbank.api.model.ZSCard;
import com.compassplus.pocketbank.api.model.ZSGetCardResponse;
import com.compassplus.pocketbank.api.model.ZSPaymentTemplate;
import com.compassplus.pocketbank.api.model.ZSTransferHistoryRow;
import com.compassplus.pocketbank.api.model.config.Help;
import com.compassplus.pocketbank.common.ClientSessionManager;
import com.compassplus.pocketbank.common.TimeoutService;
import com.compassplus.pocketbank.common.ZSDashboardSingleton;
import com.compassplus.pocketbank.common.ZSInfoMessages;
import com.compassplus.pocketbank.common.utils.Utils;
import com.compassplus.pocketbank.common.widgets.ZSCustomToolbar;
import com.compassplus.pocketbank.config.EndpointPreferences;
import com.compassplus.pocketbank.config.PicassoConfig;
import com.compassplus.pocketbank.data.AppDataCache;
import com.compassplus.pocketbank.data.PreferenceUtils;
import com.compassplus.pocketbank.events.BadSessionEvent;
import com.compassplus.pocketbank.events.DynamicAuthEvent;
import com.compassplus.pocketbank.events.GetPanEvent;
import com.compassplus.pocketbank.events.LogonEvent;
import com.compassplus.pocketbank.events.RemoveEvent;
import com.compassplus.pocketbank.events.ResponseEvent;
import com.compassplus.pocketbank.events.RetryRequestEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.BadCertificateEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.BadInternetConnectionEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.PushCountEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.TimeoutEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.UnavailableVersionEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.ZSDynAuthEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.ZSLoadManagerEvent;
import com.compassplus.pocketbank.events.ZSKBEvents.ZSTechWorkEvent;
import com.compassplus.pocketbank.pushnotifications.NotificationUtils;
import com.compassplus.pocketbank.services.PocketBankService;
import com.compassplus.pocketbank.ui.ActivityRequestCodes;
import com.compassplus.pocketbank.ui.IntentExtras;
import com.compassplus.pocketbank.ui.auth.DynamicAuthCapActivity;
import com.compassplus.pocketbank.ui.auth.DynamicConfirmationActivity;
import com.compassplus.pocketbank.ui.common.WebViewActivity;
import com.compassplus.pocketbank.ui.dashboard.ZSBankNotificationsActivity;
import com.compassplus.pocketbank.ui.dialogs.AlertDialogFragment;
import com.compassplus.pocketbank.ui.dialogs.ProgressDialogFragment;
import com.compassplus.pocketbank.ui.login.LoginActivity;
import com.compassplus.pocketbank.ui.login.LoginCaptchaActivity;
import com.compassplus.pocketbank.ui.payments.ZSPaymentCompleteActivity;
import com.compassplus.pocketbank.ui.payments1.ScheduleCardSelectionActivity;
import com.compassplus.pocketbank.ui.transfers.ZSTransferCompleteActivity;
import com.crashlytics.android.Crashlytics;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.ThrowableFailureEvent;
import retrofit.RetrofitError;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.compassplus.pocketbank.api.model.PBResponse.FAULT_CODE_UNAVAILABLE_VERSION;
import static com.compassplus.pocketbank.api.model.PBResponse.FAULT_CODE_ZS_USER_BLOCKED;
import static com.compassplus.pocketbank.api.model.PBResponse.FAULT_CODE_ZS_USER_BLOCKED2;
import static com.compassplus.pocketbank.api.model.PBResponse.FAULT_UNKNOWN_DEVICE;

/**
 * Author: alex askerov
 * Date: 15/02/15
 */
public abstract class BaseActivity extends AppCompatActivity implements IntentExtras,
        ActivityRequestCodes {

    public static final int TIME_EXTENDED = 2134;
    public final static int TEMPLATE_TYPE_PAYMENT = 0;
    public final static int TEMPLATE_TYPE_TRANSFER = 1;
    private static final String TAG = "BaseActivity";
    private static final String TAG_PROGRESS_DIALOG = "ProgressDialogFragment";
    private static final String TAG_ERROR_DIALOG = "ErrorDialogFragment";
    private static final String DIALOG_ACTION_LOGOUT = "action_logout";
    private static final String DIALOG_ACTION_HANDLE_LOGOUT = "action_handle_logout";
    private static final String DIALOG_ACTION_DEVICE_BLOCKED = "action_device_blocked";
    private static final String DIALOG_ACCESS_SERVICE_BLOCKED = "access_service_blocked";
    private static final String DIALOG_ACTION_USER_BLOCKED = "action_user_blocked";
    private static final String DIALOG_ACTION_INVALID_AUTH = "action_invalid_auth";
    private static final String DIALOG_ACTION_NEED_RESET_PASSWORD = "action_need_reset_password";
    private static final String DIALOG_ACTION_BAD_SESSION = "action_bad_session";
    private static String UDID;
    private static String releaseVersion;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // Проверка интернет соединения
    private final Handler mHandler = new Handler();
    private final String ACTION_DELETE_TEMPLATE = "delete_template";
    private final int ACCT_UPDATE_TYPE_MANUAL = 0;
    private final int ACCT_UPDATE_TYPE_PAYMENT = 1;
    private final int ACCT_UPDATE_TYPE_TRANSFER = 2;
    public ZSInfoMessages infoMessages;
    public BaseActivity activity = this;
    public boolean flagLogout = false;
    protected PocketBankService pocketBankService;
    protected ZSCustomToolbar toolbar;
    private List<String> approvedNonAuthActivitiesList = new ArrayList<String>() {{
        add("LoginActivity");
        add("ZSGeoFilter");
        add("ZSBIActivity");
        add("ZSBICreateActivity");
        add("ZSBIDetailsActivity");
        add("ZSBalanceInfoDetailsActivity");
        add("ZSBalanceInfoCreateActivity");
        add("SingleNewsActivity");
        add("ZSCurrencyRatesActivity");
        add("ZSActivityTest");
        add("ZSFingerprintActivity");
        add("ZSDynAuthActivity");
        add("ZSGeoFilterActivity");
        add("ZSGeoFragmentActivity");
        add("ZSPushListActivity");
        add("SingleNewsActivity");
        add("ZSNewsActivity");
        add("ZSBICreateCompleteActivity");
        add("ZSBIListActivity");
    }};
    private ZSInfoMessages.ConfirmDialogListener infoMessagesListener;
    // Request Timeout
    private Handler timeoutHandler;
    private UserBlockedListener userBlockedListener;
    private boolean isAppClosed = false;
    private boolean timeout = false;
    private boolean techMessageReceived = false;
    private androidx.appcompat.app.AlertDialog badInternetDialog;
    private boolean badInternetDialogShown = false;
    private LinearLayout warningLayout;
    private TextView warningText;
    private Animation a, b;
    private ImageView warningIcon;
    private int notifCount;
    private boolean previousNetworkState = true;
    private String templateId = "0";
    private String templateSource = "";
    private List<BaseConfirmDialogListener> confirmListener = new ArrayList<>();
    // OverLay Loader
    private FrameLayout overlayLoader;
    private TextView overlayLoaderTextView;
    private int countOverlayLoader = 0;
    private CountDownTimer timeoutCountDownTimer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            showOverlayLoader(false);

        }
    };
    final Runnable mUpdateUI = new Runnable() {
        public void run() {
            isNetworkAvailable(false);
            mHandler.postDelayed(mUpdateUI, 2000);
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            PocketBankService.LocalBinder binder = (PocketBankService.LocalBinder) service;
            pocketBankService = binder.getService();
            if (!EventBus.getDefault().isRegistered(BaseActivity.this)) {
                EventBus.getDefault().registerSticky(BaseActivity.this);
            }
            onServiceConnect();

            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().get("activity") != null) {

                if (Objects.requireNonNull(getIntent().getExtras().get("activity")).toString().equals("LoginActivity") && AppDataCache.getInstance().isFirstLaunchApp()) {
//                    ((LoginActivity) BaseActivity.this).preLoader();
                    AppDataCache.getInstance().setFirstLaunchApp(false);
                }
                if (Objects.requireNonNull(getIntent().getExtras().get("activity")).toString().equals("MainActivity")) {
                    checkUnreadedNotifications();
//                    updatePushHistoryCount(false);
                }
                startTimerService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pocketBankService = null;
        }
    };
    /**
     * RxSubscription manager
     */

    private CompositeSubscription mSubscriptions;

    public static String getUDID() {
        return UDID;
    }

    public static String getReleaseVersion() {
        return releaseVersion;
    }

    public static void superMethod(String simpleParam, Callable<Void> methodParam) {

        //your logic code [...]

        //call methodParam
        try {
            methodParam.call();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Запрет на создание скриншотов экрана
//        if (!BuildConfig.DEBUG)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);

        // Ручная обработка все runtime исключений
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
//                //Catch your exception
//                ProcessPhoenix.triggerRebirth(getApplicationContext());
//            }
//        });

        // Анимация переходов activity
        overridePendingTransition(R.anim.activity_ltr, R.anim.activity_rtl);

        // Поддержка векторных изображений
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Инициализация диалогов
        initInfoMessages();

        // Установка портретной ориентации
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Получаем layout для activity
        setContentView(getLayoutId());

        // Инициализация загрузчика
        initOverlayLoader();

        // Менеджер подписок для Rx
        mSubscriptions = new CompositeSubscription();
        subscribe();

        toolbar = findViewById(R.id.base_activity_toolbar);
        toolbar.getMenuLogout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoMessages.createConfirmDialog("Вы уверены, что хотите выйти?", "ОК", "Отмена", DIALOG_ACTION_HANDLE_LOGOUT);
            }
        });

        initNotification();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setContentInsetsAbsolute(0, 0);
            if (getSupportActionBar() != null) {
                final Drawable upArrow = getResources().getDrawable(R.drawable.back);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }

        // Инициализация верхнего tab bar информирующего об интернет-соединении
        initErrorAttrs();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Intent intent = new Intent(this, PocketBankService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        if (PreferenceUtils.getRegFinished() && !isUserAuthorized()) {
            stopTimerService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flagLogout = false;
        mHandler.post(mUpdateUI);

        RxDataManager.getInstance().setActivity(this); //Добавляем в RX контекст текущей Activity
        isAppClosed = false;

        try {
            if (timeout) {
                timeoutLogout();
                flagLogout = true;
                timeout = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (PreferenceUtils.getRegFinished()) {
            if (!this.getClass().getSimpleName().equals("LoginActivity")) {
                if (isUserAuthorized()) {
                    if (AppDataCache.getInstance().getAccountList().size() == 0 || AppDataCache.getInstance().getVendorList().size() == 0) {
                        logout();
                        flagLogout = true;
                    }
                }
            }
        }

        UDID = android.provider.Settings.System.getString(super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            releaseVersion = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (!this.getClass().getSimpleName().equals("ZSPushListActivity") && !this.getClass().getSimpleName().equals("ZSSettingsActivity")) {
//            // Проверка состояния push уведомлений в рамках сессии (PRJZAPSIBP-2705)
//            if (AppDataCache.getInstance().APP_SESSION_PUSH_STATUS == AppDataCache.getInstance().PUSH_STATUS_UNDEFINED) {
//                AppDataCache.getInstance().APP_SESSION_PUSH_STATUS = NotificationUtils.isNotificationsEnabled(getApplicationContext()) ?
//                        AppDataCache.getInstance().PUSH_STATUS_ENABLED : AppDataCache.getInstance().PUSH_STATUS_DISABLED;
//            } else {
//                int pushStatus = NotificationUtils.isNotificationsEnabled(getApplicationContext()) ?
//                        AppDataCache.getInstance().PUSH_STATUS_ENABLED : AppDataCache.getInstance().PUSH_STATUS_DISABLED;
//                if (AppDataCache.getInstance().APP_SESSION_PUSH_STATUS != pushStatus) {
//                    AppDataCache.getInstance().APP_SESSION_PUSH_STATUS = NotificationUtils.isNotificationsEnabled(getApplicationContext()) ?
//                            AppDataCache.getInstance().PUSH_STATUS_ENABLED : AppDataCache.getInstance().PUSH_STATUS_DISABLED;
//                    try {
//                        sendPushToken();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

    }

    @Override
    protected void onStop() {
        if (isAuthRequired() && isUserAuthorized())
            ClientSessionManager.getInstance().onStop();
        if (pocketBankService != null) {
            unbindService(connection);
        }
        isAppClosed = true;
        super.onStop();
    }

    public void onEvent(TimeoutEvent event) {
        if (!RxDataManager.getInstance().stopCallServer) {
            if (event != null) {

                timeout = true;

                if (!isAppClosed) {
                    timeoutLogout();
                    timeout = false;
                }
            }
        }
    }

    public void onEvent(ZSTechWorkEvent event) {
        if (event != null && !techMessageReceived) {
            techMessageReceived = true;
            showOverlayLoader(false);

            infoMessages.createInfoDialog(!event.message.isEmpty() ? event.message :
                            "В настоящее время проводятся технические работы, вход в Мобильное приложение ограничен, попробуйте позднее. Приносим извинения за доставленные неудобства!", "OK", false,
                    new ZSInfoMessages.InfoDialogConfirmListener() {
                        @Override
                        public void onConfirm() {
                            finishAffinity();
                            System.exit(0);
                        }
                    });
        }
    }

    public void onEvent(BadSessionEvent event) {

        if (!this.getClass().getSimpleName().equals("LoginActivity")) {
            logout();
        } else {
            EventBus.getDefault().post(new TimeoutEvent());
        }

    }

    public void onEvent(BadCertificateEvent event) {

        // PRJZAPSIBP-1960

//        if (infoMessages != null) {
//            infoMessages.createErrorInfoDialogWithAction(event.getMessage(), "BAD_CERTIFICATE");
//        } else {
        EventBus.getDefault().post(new TimeoutEvent());
//        }
    }

    public void onEvent(UnavailableVersionEvent event) {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.PB_AlertDialog_Base);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.layout_zs_dialog, null);
                TextView tv = (TextView) dialogView.findViewById(R.id.dialog_textview);
                tv.setText("Уважаемый клиент!\n" +
                        "\n" +
                        "Мы внесли изменения для стабильной работы приложения. Просим Вас обновить версию приложения в Play Маркете для продолжения работы.");

                dialogBuilder.setView(dialogView);
                dialogBuilder
                        .setNegativeButton("ВЫХОД", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);

                            }
                        })
                        .setPositiveButton("ОБНОВИТЬ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        final String appPackageName = "com.inetbank.zapsibkombank"; // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }

                                    }
                                });

                            }
                        })
                        .setTitle("Внимание")
                        .setIcon(R.drawable.ic_error_dialog);


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                if (!isFinishing()) {
                    alertDialog.show();
                }
                Button b = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                if (b != null) {
                    b.setTextColor(getResources().getColor(R.color.text_color_secondary));
                }

                Button a = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                if (a != null) {
                    a.setTextColor(getResources().getColor(R.color.primary));
                }

            }
        });

    }

    private void initBadInternetDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.PB_AlertDialog_Base);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.layout_zs_dialog, null);
                TextView tv = (TextView) dialogView.findViewById(R.id.dialog_textview);
                tv.setText("Вероятно, соединение с Интернетом прервано");

                dialogBuilder.setView(dialogView);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        badInternetDialogShown = false;
                        dialog.dismiss();
                    }
                })
                        .setTitle("Ошибка")
                        .setIcon(R.drawable.ic_error_dialog);

                badInternetDialog = dialogBuilder.create();
                badInternetDialog.setCancelable(false);
                if (!isFinishing()) {
                    badInternetDialog.show();
                }
                Button a = badInternetDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                if (a != null) {
                    a.setTextColor(getResources().getColor(R.color.primary));
                }
            }
        });
    }

    public void onEvent(BadInternetConnectionEvent event) {

        if (badInternetDialog == null && !badInternetDialogShown) {
            badInternetDialogShown = true;
            initBadInternetDialog();
        }

    }

    public void startTimerService() {
        stopService(new Intent(this, TimeoutService.class));
        startService(new Intent(this, TimeoutService.class));
    }

    public void stopTimerService() {
        stopService(new Intent(this, TimeoutService.class));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        unsubscribe();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (overlayLoader.getVisibility() == View.VISIBLE) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateUI);
    }

    protected boolean isAuthRequired() {
        return true;
    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.progress_dialog), false);
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(getString(R.string.progress_dialog), cancelable);
    }

    public void showProgressDialog(final String message, final boolean cancelable) {

        try {
            if (!isAppClosed) {

                try {
                    getSupportFragmentManager().popBackStackImmediate();
                } catch (IllegalStateException ignored) {
                    // There's no way to avoid getting this if saveInstanceState has already been called.
                }

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (!isFinishing()) {
                            ProgressDialogFragment.newInstance(message, cancelable).show(getSupportFragmentManager(), TAG_PROGRESS_DIALOG);
                        }

                    }
                });

                timeoutHandler = new Handler(getMainLooper());
                timeoutHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                }, 120000);


            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    public void hideProgressDialog() {
        final ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_PROGRESS_DIALOG);

        if (timeoutHandler != null)
            timeoutHandler.removeCallbacksAndMessages(null);

        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
        }
    }

    public void showErrorDialog(final String message) {
        showOverlayLoader(false);
        if (message != null) {
            infoMessages.createErrorInfoDialog(message);
        }
    }

    public void showAlertDialog(final int id, final String message, final String title, final boolean showCancelButton) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                if (!isFinishing()) {
                    AlertDialogFragment.newInstance(id, message, title, showCancelButton).show(getSupportFragmentManager(), TAG_ERROR_DIALOG);
                }
            }
        });
    }

    @SuppressWarnings({"unused", "ThrowableResultOfMethodCallIgnored"})
    public void onEventMainThread(ThrowableFailureEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        hideProgressDialog();
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);

        showOverlayLoader(false);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3);
            swipeRefreshLayout.setRefreshing(false);
        }
        Throwable throwable = event.getThrowable();
        if (throwable != null) {

            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(throwable);
            } else {
                Log.e(TAG, throwable.getMessage(), throwable);
            }

            if (throwable instanceof RetrofitError) {
                handleRetrofitError((RetrofitError) throwable);
            } else {
                showErrorDialog(throwable.getMessage());
            }
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ResponseEvent responseEvent) {
        if (PreferenceUtils.getRegFinished()) {

            EventBus.getDefault().removeStickyEvent(responseEvent);
            if (handleResponse(responseEvent.getRequest(), responseEvent.getResponse())) {
                onSuccessResponseInEvent(responseEvent.getRequest(), responseEvent.getResponse());
            }
        }
    }

    protected void onSuccessResponseInEvent(PBRequest request, PBResponse response) {
        //implement in subclass if needed
    }

    protected void onServiceConnect() {
        if (isAuthRequired()) {
            if (isUserAuthorized()) {
                ClientSessionManager.getInstance().onStart();
            }
        } else {

            if (getIntent().getExtras() != null) {
                if (!approvedNonAuthActivitiesList.contains(this.getClass().getSimpleName())) {
                    if (!isFinishing()) logout();
                }
            }
        }
    }

    public void onBadSession() {
        hideProgressDialog();
        if (this instanceof LoginActivity) {
            ((LoginActivity) this).preLoader(null);
        }
    }

    /**
     * return false if it was a fault and it processed by this method.
     */
    public boolean handleResponse(PBRequest request, PBResponse response) {
        return handleResponse(request, response, true);
    }

    public boolean handleResponse(final PBResponse response) {

        switch (response.getFaultCode()) {
            case PBResponse.FAULT_CODE_UNKNOWN:
                return true;

            case PBResponse.FAULT_CODE_RESET_PIN:
                infoMessages.createErrorInfoDialogWithAction("Требуется восстановить пароль", DIALOG_ACTION_NEED_RESET_PASSWORD);
                return false;

            case PBResponse.FAULT_CODE_INCORRECT_LOGIN_OR_PASSWORD:
                infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACTION_INVALID_AUTH);
                return false;

            case PBResponse.FAULT_CODE_DEVICE_BLOCKED:
                if (PreferenceUtils.getRegFinished()) {
                    infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACTION_USER_BLOCKED);
                } else {
                    infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACTION_DEVICE_BLOCKED);
                }
                return false;

            case FAULT_CODE_ZS_USER_BLOCKED:
            case FAULT_CODE_ZS_USER_BLOCKED2:
            case PBResponse.FAULT_CODE_SMS_AUTH_USER_BLOCKED:
            case PBResponse.FAULT_CODE_USER_CHANGE_SIM_CARD:
            case PBResponse.FAULT_CODE_USER_SIM_CARD_BLOCKED:
                infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACTION_DEVICE_BLOCKED);
                return false;
            case PBResponse.FAULT_CODE_ACCESS_SERVICE_BLOCKED:
                infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACCESS_SERVICE_BLOCKED);
                return false;
            default:
                handleDefaultError(response);
                return false;
        }
    }

    public boolean handleResponse(PBRequest request, PBResponse response, boolean hideProgressOnSuccess) {

        if (!response.getFaultCode().equals("0") || hideProgressOnSuccess)
            hideProgressDialog();

        if (PBResponse.RESPONSE_CODE_CARDS_FOR_TRANSFER.equals(response.getResponseCode())) {
            startScheduleCardSelection(request, response);
            return false;
        }

        switch (response.getFaultCode()) {
            case PBResponse.FAULT_CODE_UNKNOWN:
                return true;
            case PBResponse.FAULT_CODE_SMS_AUTH:
            case PBResponse.FAULT_CODE_SMS_AUTH_INCORRECT_PASSWORD:
            case PBResponse.FAULT_CODE_SMS_AUTH_USER_BLOCKED:
                startDynamicAuth(request, response, response.getFaultCode());
                return false;

            case PBResponse.FAULT_CODE_CAP_AUTH:
            case PBResponse.FAULT_CODE_CAP_AUTH_NO_CHALLENGE:
                startCAPAuth(request, response);
                return false;

            case PBResponse.FAULT_CODE_CONFIRMATION:
                startDynamicConfirmationActivity(request, response);
                return false;

            case PBResponse.FAULT_CODE_CAPTCHA:
                startCaptchaActivity(request, response);
                return false;

            case PBResponse.FAULT_CODE_RESET_PIN:
                infoMessages.createErrorInfoDialogWithAction("Требуется восстановить пароль", DIALOG_ACTION_NEED_RESET_PASSWORD);
                return false;

            case PBResponse.FAULT_CODE_INCORRECT_LOGIN_OR_PASSWORD:
                infoMessages.createErrorInfoDialogWithAction(/*!PreferenceUtils.getRegFinished() ?*/ response.getMessage() /*: "Вы поменяли логин и/или пароль от Интернет-Банка, необходимо снова  авторизоваться по логину или номеру карты"*/, DIALOG_ACTION_INVALID_AUTH);
                return false;

            case PBResponse.FAULT_CODE_DEVICE_BLOCKED:
                infoMessages.createErrorInfoDialogWithAction("Устройство заблокировано Вами для входа в мобильное приложение. \n Пожалуйста, разблокируйте данное устройство в сервисе Интернет-Банк, раздел: \n Настройки > Общие", DIALOG_ACTION_USER_BLOCKED);
                return false;

            case FAULT_UNKNOWN_DEVICE:
                infoMessages.createErrorInfoDialogWithAction("Не удалось инициализировать устройство", DIALOG_ACTION_DEVICE_BLOCKED);
                return false;

            case FAULT_CODE_UNAVAILABLE_VERSION:
                infoMessages.createErrorInfoDialogWithAction("Уважаемый клиент!\n" +
                        "\n" +
                        "Мы внесли изменения для стабильной работы приложения. Просим Вас обновить версию приложения в Play Маркете для продолжения работы.", DIALOG_ACTION_DEVICE_BLOCKED);
                return false;

            case FAULT_CODE_ZS_USER_BLOCKED:
            case FAULT_CODE_ZS_USER_BLOCKED2:
            case PBResponse.FAULT_CODE_USER_CHANGE_SIM_CARD:
            case PBResponse.FAULT_CODE_USER_SIM_CARD_BLOCKED:
                infoMessages.createErrorInfoDialogWithAction(response.getMessage(), DIALOG_ACTION_DEVICE_BLOCKED);
                return false;

            default:
                handleDefaultError(response);
                return false;
        }
    }

    protected void handleDefaultError(PBResponse response) {
        hideProgressDialog();
        showErrorDialog(response.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_DYNAMIC_AUTH:
            case REQUEST_CODE_CAP_AUTH:
            case REQUEST_CODE_SCHEDULE_CARD:
            case REQUEST_CODE_DYNAMIC_CONFIRMATION:
            case REQUEST_CAPTCHA:
                if (resultCode == RESULT_OK && data != null) {
                    showProgressDialog();
                    PBRequest request = data.getParcelableExtra(EXTRA_REQUEST);
                    EventBus.getDefault().postSticky(new RetryRequestEvent(request));
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleRetrofitError(RetrofitError retrofitError) {
        showOverlayLoader(false);

        switch (retrofitError.getKind()) {
            case NETWORK:
                showErrorLayout("Нет доступа в Интернет");
            case HTTP:
                infoMessages.createErrorInfoDialog(getString(R.string.error_server));
                break;
            case CONVERSION:
                infoMessages.createErrorInfoDialog(getString(R.string.error_converter));
                break;
            default:
                infoMessages.createErrorInfoDialog(getString(R.string.error_unknown));
                break;
        }

    }

    private void initErrorAttrs() {
        warningLayout = findViewById(R.id.pager_warning_layout);
        warningText = findViewById(R.id.pager_warning_text);
        a = AnimationUtils.loadAnimation(this, R.anim.warning_slide_down);
        b = AnimationUtils.loadAnimation(this, R.anim.warning_slide_up);

        warningIcon = findViewById(R.id.pager_warning_icon);

        VectorDrawableCompat wIcon = VectorDrawableCompat.create(getResources(), R.drawable.ic_about_app, null);
        assert wIcon != null;
        wIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        warningIcon.setImageDrawable(wIcon);
    }

    private void showErrorLayout(String message) {
        if (warningLayout != null) {
            if (warningLayout.getVisibility() != View.VISIBLE) {
                warningText.setText(message);
                warningLayout.setVisibility(View.VISIBLE);
                warningLayout.setBackgroundColor(Color.parseColor("#eaf44336"));
                warningLayout.startAnimation(a);
            }
        }
    }

    private void hideErrorLayout(String message) {
        if (warningLayout != null) {
            if (warningLayout.getVisibility() != View.GONE) {
                warningText.setText(message);
                warningLayout.startAnimation(b);
                warningLayout.setBackgroundColor(Color.parseColor("#cc0b5917"));
                warningLayout.setVisibility(View.GONE);

                try {
                    if (this.getClass().getSimpleName().equals("LoginActivity")) {
                        ((LoginActivity) this).preLoader(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                updatePushHistoryCount();
                                return null;
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void onEventMainThread(RetryRequestEvent retryRequestEvent) {
        //System.out.println("BaseActivity onEventMainThread");
        PBRequest request = retryRequestEvent.getRequest();
        if (request.getAction().equals(RequestHelper.AUTH_GET_PAN_REQUEST)) {
            EventBus.getDefault().removeStickyEvent(retryRequestEvent);
            pocketBankService.login(request, "Login");
        } else if (request.getAction().equals(RequestHelper.LOGON_REQUEST)) {
            EventBus.getDefault().removeStickyEvent(retryRequestEvent);
            pocketBankService.logon(request);
        } else {
            EventBus.getDefault().removeStickyEvent(retryRequestEvent);
            pocketBankService.runPBRequest(request);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onUpPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preLogout() {
        LoginActivity.previousFragment = 0;
        ZSDashboardSingleton.getInstance().resetInstance();
        ZSLoadManagerEvent.getInstance().resetInstance();

        //Очищаем закешированные картинки из памяти
        PicassoConfig.clearPicassoMemoryCache();

        if ((!this.getClass().getSimpleName().equals("LoginActivity")
                || !PreferenceUtils.getRegFinished()) && pocketBankService != null) {
            pocketBankService.logout();
        }

        RxDataManager.resetInstance();
        RxDataManager.getInstance().setAuthKey("");
        EventBus.getDefault().removeAllStickyEvents();
        AppDataCache.getInstance().resetInstance();
    }

    private void timeoutLogout() {
        preLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void logout() {
        preLogout();
        showLoginActivity();
    }

    protected void handleLogout() {
        preLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("handle_logout", "true");
        TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (this.getClass().getSimpleName().equals("LoginActivity")) {
            EventBus.getDefault().post(new RemoveEvent());
            finish();
        }
        startActivity(intent);
    }

    protected void onUpPressed() {
        finish();
    }

    private void startDynamicAuth(PBRequest request, PBResponse response, String faultCode) {
        EventBus.getDefault().postSticky(new ZSDynAuthEvent());
        EventBus.getDefault().post(new DynamicAuthEvent(request, response, pocketBankService, faultCode));
    }

    private void startCAPAuth(PBRequest request, PBResponse response) {
        Intent intent = new Intent(this, DynamicAuthCapActivity.class);
        intent.putExtra(EXTRA_REQUEST, request);
        intent.putExtra(EXTRA_RESPONSE, response);
        startActivityForResult(intent, REQUEST_CODE_CAP_AUTH);
    }

    private void startDynamicConfirmationActivity(PBRequest request, PBResponse response) {
        Intent intent = new Intent(this, DynamicConfirmationActivity.class);
        intent.putExtra(EXTRA_REQUEST, request);
        intent.putExtra(EXTRA_RESPONSE, response);
        startActivityForResult(intent, REQUEST_CODE_DYNAMIC_CONFIRMATION);
    }

    protected void startScheduleCardSelection(PBRequest request, PBResponse response) {
        Intent intent = new Intent(this, ScheduleCardSelectionActivity.class);
        intent.putExtra(EXTRA_REQUEST, request);
        intent.putExtra(EXTRA_RESPONSE, response);
        startActivityForResult(intent, REQUEST_CODE_SCHEDULE_CARD);
    }

    protected void startCaptchaActivity(PBRequest request, PBResponse response) {
        Intent intent = new Intent(this, LoginCaptchaActivity.class);
        intent.putExtra(EXTRA_REQUEST, request);
        intent.putExtra(EXTRA_RESPONSE, response);
        startActivityForResult(intent, REQUEST_CAPTCHA);
    }

    public void startWebViewActivity(String url, String title) {
        if (TextUtils.isEmpty(url))
            return;

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, TextUtils.isEmpty(title) ? getTitle() : title);
        startActivity(intent);
    }

    public void showHelp(Help help) {
        if (help.isWebFile()) {
            String url = EndpointPreferences.getEndpoint().getTelebankEndpoint()
                    + "/" + help.getFile();
            startWebViewActivity(url, null);
        } else {
            showAlertDialog(0, help.getMessage(), null, false);
        }
    }

    public boolean isUserAuthorized() {

        GetPanEvent panEvent = EventBus.getDefault().getStickyEvent(GetPanEvent.class);
        LogonEvent logonEvent = AppDataCache.getInstance().getLogonEvent();

        return panEvent != null && panEvent.getPanResponse().isSuccess()
                & logonEvent != null && logonEvent.getLogonResponse().isSuccess();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimeoutService.updateTimer();
        if (!approvedNonAuthActivitiesList.contains(this.getClass().getSimpleName())) {
            if (!AppDataCache.getInstance().hasAccounts() || !AppDataCache.getInstance().hasVendors()) {
                logout();
            }
        }

        long userInteractionTime = new Date().getTime();

        // Detect Timeout 8 min
        if (AppDataCache.getInstance().getLastUserIteracton() != 0) {
            if (userInteractionTime - AppDataCache.getInstance().getLastUserIteracton() > 480000) {
                logout();
            } else {
                AppDataCache.getInstance().setLastUserIteracton(userInteractionTime);
            }
        } else {
            AppDataCache.getInstance().setLastUserIteracton(userInteractionTime);
        }
    }

    public void showToolbarAttrs(String title, Boolean notificationButton, Boolean logoutButton, Boolean searchButton, Boolean backButton, Boolean elevation, Boolean visible) {

        if (title == null) {
            toolbar.setToolbarTitle("");
            return;
        }

        if (!title.isEmpty())
            toolbar.setToolbarTitle(title);

        toolbar.getMenuNotificationCounterButton().setVisibility(notificationButton ? View.VISIBLE : View.GONE);
        toolbar.getMenuNotificationCount().setVisibility(Integer.valueOf(toolbar.getMenuNotificationCount().getText().toString()) > 0 ? View.VISIBLE : View.GONE);
        toolbar.getMenuLogout().setVisibility(logoutButton ? View.VISIBLE : View.GONE);
        toolbar.getMenuSearchView().setVisibility(searchButton ? View.VISIBLE : View.GONE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(backButton);
        getSupportActionBar().setHomeButtonEnabled(backButton);

        if (Utils.isPostLollipop()) {
            if (elevation)
                toolbar.setElevation(14);
            else
                toolbar.setElevation(0);
        }

        toolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void checkUnreadedNotifications() {
        RxDataManager.getInstance().observeZSBankNotificationsList().subscribe(new Subscriber<List<ZSBankNotifications>>() {
            @Override
            public void onCompleted() {
                notifCount = RxDataManager.getInstance().unreadedNotifications;
                setNotifCount(notifCount);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<ZSBankNotifications> templates) {

            }
        });
    }

    public void updatePushHistoryCount() {
        if (!PreferenceUtils.getZSMobileTemplate().getPhoneNumber().isEmpty()) {
            RxDataManager.getInstance().executeESBPushHistoryCount(PreferenceUtils.getZSMobileTemplate().getPhoneNumber())
                    .subscribe(new Subscriber<PushHistoryCountResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            EventBus.getDefault().post(new PushCountEvent());
                        }

                        @Override
                        public void onNext(PushHistoryCountResponse pushHistoryCountResponse) {
                            PreferenceUtils.saveZsPushCounter(pushHistoryCountResponse.getCount());
                            EventBus.getDefault().post(new PushCountEvent());
                        }
                    });
        }
    }

    private void initNotification() {
        toolbar.getMenuNotificationCounterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ZSBankNotificationsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void setNotifCount(int unreaded) {
        toolbar.setNotificationCount(unreaded);
    }

    public TextView getToolbarTitle() {
        return toolbar.getToolbarTitle();
    }

    public void setToolbarTitle(String title) {
        toolbar.setToolbarTitle(title);
    }

    public boolean isNetworkAvailable(boolean onlyCheck) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (onlyCheck) {
            return networkInfo != null && networkInfo.isConnected();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            hideErrorLayout("Интернет соединение восстановлено");

            if (overlayLoader != null && overlayLoader.getVisibility() == View.VISIBLE && !previousNetworkState) {
                showOverlayLoader(false);
            }

            previousNetworkState = true;

        } else {
            showErrorLayout("Нет доступа в Интернет");
            previousNetworkState = false;
        }
        return true;
    }

    public void removeExtras(String key) {
        getIntent().removeExtra(key);
    }

    public PocketBankService getPocketBankService() {
        return pocketBankService;
    }

    public SearchView getToolbarSearchView() {
        return toolbar.getMenuSearchView();
    }

    public void hideKeyboard(View view) {
        try {
            if (view != null && view.getWindowToken() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserBlockedListener(UserBlockedListener listener) {
        this.userBlockedListener = listener;
    }

    public void startDeleteTemplate(int type, final String templateId1) {
        showOverlayLoader(true);

        switch (type) {
            case TEMPLATE_TYPE_PAYMENT:
                RxDataManager.getInstance().observeZSPaymentTemplatesList().subscribe(new Subscriber<List<ZSPaymentTemplate>>() {
                    @Override
                    public void onCompleted() {
                        showOverlayLoader(false);
                        ZSLoadManagerEvent.getInstance().setNeedUpdatePaymentTemplates(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showOverlayLoader(false);
                    }

                    @Override
                    public void onNext(List<ZSPaymentTemplate> templates) {

                        for (ZSPaymentTemplate template : templates) {
                            if (template.getSeqNo() == Integer.valueOf(templateId1)) {
                                templateId = templateId1;
                                templateSource = template.getExtData().get(0).getSource();
                                infoMessages.createConfirmDialog("Вы действительно хотите удалить шаблон", "ОК", "ОТМЕНА", ACTION_DELETE_TEMPLATE);
                                break;
                            }
                        }
                    }
                });

                break;

            case TEMPLATE_TYPE_TRANSFER:
                break;
        }
    }

    public void setBaseConfirmListener(BaseConfirmDialogListener listener) {
        this.confirmListener.add(listener);
    }

    private void initInfoMessages() {
        infoMessagesListener = new ZSInfoMessages.ConfirmDialogListener() {
            @Override
            public void onConfirmDialogPositiveClick(String action) {

                if (!confirmListener.isEmpty()) {
                    for (BaseConfirmDialogListener listener : confirmListener) {
                        listener.onConfirmDialogPositiveClick(action);
                    }
                }

                switch (action) {
                    case DIALOG_ACTION_LOGOUT:
                        LoginActivity.previousFragment = 0;
                        logout();
                        break;

                    case DIALOG_ACTION_HANDLE_LOGOUT:
                        LoginActivity.previousFragment = 0;
                        handleLogout();
                        break;

                    case ACTION_DELETE_TEMPLATE:
                        showOverlayLoaderWithText("Удаление шаблона...");

                        RxDataManager.getInstance().observeZSDeleteTemplate(String.valueOf(templateId), templateSource).subscribe(new Subscriber<List<ZSCard>>() {
                            @Override
                            public void onCompleted() {
                                showOverlayLoader(false);
                                infoMessages.makeSuccessToast(getApplicationContext(), getLayoutInflater(), toolbar, "Удалено");
                                if (!RxDataManager.getInstance().getActivity().getClass().getSimpleName().equals("MainActivity")) {
                                    finish();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                showOverlayLoader(false);
                                infoMessages.createErrorInfoDialog(e.getMessage());
                            }

                            @Override
                            public void onNext(List<ZSCard> cards) {
                            }
                        });
                        break;

                }
            }

            @Override
            public void onConfirmDialogNegativeClick(String action) {

                if (!confirmListener.isEmpty()) {
                    for (BaseConfirmDialogListener listener : confirmListener) {
                        listener.onConfirmDialogNegativeClick(action);
                    }
                }

            }

            @Override
            public void onInfoDialogPositiveClick(String action) {

                if (!confirmListener.isEmpty()) {
                    for (BaseConfirmDialogListener listener : confirmListener) {
                        listener.onInfoDialogPositiveClick(action);
                    }
                }

                switch (action) {
                    case DIALOG_ACTION_NEED_RESET_PASSWORD:
                        userBlockedListener.onUserBlocked();
                        break;
                    case DIALOG_ACCESS_SERVICE_BLOCKED:
                    case DIALOG_ACTION_DEVICE_BLOCKED:
                        if (!isUserAuthorized() && userBlockedListener != null) {
                            userBlockedListener.onUserBlocked();
                        } else {
                            if (!PreferenceUtils.getRegFinished()) {
                                PreferenceUtils.setRegFinished(false);
                                PreferenceUtils.clearAllFingerprintPreferences();
                                PreferenceUtils.clearAllMobilePINPreferences();
                                logout();
                            }
                        }
                        break;
                    case DIALOG_ACTION_USER_BLOCKED:
                        if (!PreferenceUtils.getRegFinished()) {
                            userBlockedListener.onUserBlocked();
                        } else {
                            ProcessPhoenix.triggerRebirth(getApplicationContext());
                        }
                        break;
                    case DIALOG_ACTION_INVALID_AUTH:
                        userBlockedListener.onUserBlocked();
                        break;
                    case DIALOG_ACTION_BAD_SESSION:
                        userBlockedListener.onUserBlocked();
                        break;
                    case "BAD_SESSION":
                        EventBus.getDefault().post(new TimeoutEvent());
                        break;
                }
            }
        };
        infoMessages = new ZSInfoMessages(this, getApplicationContext(), infoMessagesListener);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("ZSErrorMessage") != null) {
                infoMessages.createErrorInfoDialog(Objects.requireNonNull(getIntent().getExtras().get("ZSErrorMessage")).toString());
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //При нажатии вне элемента убираем фокус

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().get("activity") != null) {
            if (Objects.requireNonNull(getIntent().getExtras().get("activity")).toString().equals("LoginActivity")) {
                //do nothing
            } else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View v = getCurrentFocus();
                    if (v instanceof EditText) {
                        Rect outRect = new Rect();
                        v.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            v.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
            }

        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void initOverlayLoader() {
        overlayLoader = new FrameLayout(this);
        overlayLoader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        overlayLoader.setBackgroundColor(Color.parseColor("#99ffffff"));

        LinearLayout ll = new LinearLayout(this);
        FrameLayout.LayoutParams lButtonParamsPBl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lButtonParamsPBl.gravity = Gravity.CENTER;
        ll.setLayoutParams(lButtonParamsPBl);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tw = new TextView(this);
        LinearLayout.LayoutParams lButtonParamsTW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lButtonParamsTW.gravity = Gravity.CENTER;
        tw.setGravity(Gravity.CENTER);
        tw.setTextSize(16);
        tw.setTypeface(Typeface.DEFAULT_BOLD);
        tw.setTextColor(getResources().getColor(R.color.primary));
        tw.setLayoutParams(lButtonParamsTW);
        tw.setText("Загрузка...");
        overlayLoaderTextView = tw;

        ProgressBar pb = new ProgressBar(this);
        LinearLayout.LayoutParams lButtonParamsPB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lButtonParamsPB.gravity = Gravity.CENTER;
        pb.setLayoutParams(lButtonParamsPB);

        ll.addView(tw);
        ll.addView(pb);

        overlayLoader.addView(ll);
        overlayLoader.setClickable(true);
        addContentView(overlayLoader, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        overlayLoader.setVisibility(View.GONE);
    }

    public void showOverlayLoader(final boolean show) {
        try {
            if (overlayLoader != null) {

                if (!show) {
                    countOverlayLoader--;
                    if (countOverlayLoader <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                overlayLoader.setVisibility(View.GONE);
                                overlayLoaderTextView.setText("Загрузка...");
                            }
                        });
                    }

                    if (timeoutCountDownTimer != null) {
                        timeoutCountDownTimer.cancel();
                    }

                } else {
                    countOverlayLoader++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            overlayLoader.setVisibility(View.VISIBLE);
                        }
                    });

                    if (timeoutCountDownTimer != null) {
                        timeoutCountDownTimer.cancel();
                    }

                    Objects.requireNonNull(timeoutCountDownTimer).start();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOverlayLoaderWithText(final String text) {
        overlayLoader.setVisibility(View.VISIBLE);
        overlayLoaderTextView.setText(text);

        if (timeoutCountDownTimer != null) {
            timeoutCountDownTimer.cancel();
        }

        Objects.requireNonNull(timeoutCountDownTimer).start();
    }

    public void updateAccountList() {
        showOverlayLoader(true);
        RxDataManager.getInstance().observeAccountsResponse().subscribe(new Subscriber<GetAcctsResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onNext(GetAcctsResponse acctsResponse) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });

                // Для обновления баланса в картах, на экранах, которые подписаны на изменение cardHash
                AppDataCache.getInstance().incrementCardHash();


            }
        });
    }

    public void updateAccountListWithAction(final Callable<Void> methodParam) {
        showOverlayLoader(true);
        RxDataManager.getInstance().observeAccountsResponse().subscribe(new Subscriber<GetAcctsResponse>() {
            @Override
            public void onCompleted() {
                try {
                    methodParam.call();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onNext(GetAcctsResponse acctsResponse) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });

                // Для обновления баланса в картах, на экранах, которые подписаны на изменение cardHash
                AppDataCache.getInstance().incrementCardHash();


            }
        });
    }

    public void updateCardList() {
        showOverlayLoader(true);
        RxDataManager.getInstance().observeZSCards().subscribe(new Subscriber<ZSGetCardResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onNext(ZSGetCardResponse cardResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                AppDataCache.getInstance().setCardResponse(cardResponse);
            }
        });
    }

    public void updateCardListWithAction(final Callable<Void> methodParam) {
        showOverlayLoader(true);
        RxDataManager.getInstance().observeZSCards().subscribe(new Subscriber<ZSGetCardResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onNext(ZSGetCardResponse cardResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                AppDataCache.getInstance().setCardResponse(cardResponse);

                try {
                    methodParam.call();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void updateAccountListTyped(final int type, final long approvalCode, final boolean isTemplate, final ZSTransferHistoryRow trHistory) {
        showOverlayLoaderWithText("Обновление списка счетов...");
        RxDataManager.getInstance().observeAccountsResponse().subscribe(new Subscriber<GetAcctsResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onNext(GetAcctsResponse acctsResponse) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOverlayLoader(false);

                        switch (type) {
                            case ACCT_UPDATE_TYPE_PAYMENT:

                                Intent intent = new Intent(getApplicationContext(), ZSPaymentCompleteActivity.class);
                                intent.putExtra("isTemplate", "" + isTemplate);
                                intent.putExtra("ApprovalCode", "" + approvalCode);
                                startActivity(intent);

                                ZSLoadManagerEvent.getInstance().setNeedUpdatePaymentHistory(true);
                                finish();
                                break;

                            case ACCT_UPDATE_TYPE_TRANSFER:
                                if (trHistory != null) {
                                    Intent intent2 = new Intent(getApplicationContext(), ZSTransferCompleteActivity.class);
                                    intent2.putExtra("transferData", (Parcelable) trHistory);
                                    intent2.putExtra("isTemplate", "" + isTemplate);
                                    startActivity(intent2);
                                }

                                ZSLoadManagerEvent.getInstance().setNeedUpdateTransferHistory(true);
                                finish();

                                break;
                        }
                    }
                });

                // Для обновления баланса в картах, на экранах, которые подписаны на изменение cardHash
                AppDataCache.getInstance().incrementCardHash();
            }
        });
    }

    public void startTemplateStatusScreen(int type, String templateSeqNo) {

        switch (type) {
            case TEMPLATE_TYPE_PAYMENT:
                Intent intent = new Intent(this, ZSTemplateSavedActivity.class);
                intent.putExtra("template_seqno", templateSeqNo);
                startActivity(intent);
                finish();

                break;

            case TEMPLATE_TYPE_TRANSFER:
                Intent intent2 = new Intent(getApplicationContext(), ZSTransferTemplateSavedActivity.class);
                intent2.putExtra("template_seqno", templateSeqNo);
                startActivity(intent2);
                finish();

                break;
        }

    }

    public void showZSErrorDialog(String message) {
        infoMessages.createErrorInfoDialog(message);
    }

    public void showZSInfoDialog(String message) {
        infoMessages.createInfoDialog(message);
    }

    private void subscribe() {

        for (Subscription subscription : createSubscriptions()) {
            mSubscriptions.add(subscription);
        }
    }

    public void unsubscribe() {
        if (mSubscriptions == null) {
            return;
        }
        mSubscriptions.unsubscribe();
        mSubscriptions.remove(mSubscriptions);
        mSubscriptions.clear();
        mSubscriptions = new CompositeSubscription();
    }

    public void showFPDialog() {
        infoMessages.createLoginFPDialog();
    }

    public void dismissFRPDialog() {
        infoMessages.hideFPDialog();
    }

    protected void addSubscription(Subscription subscription) {
        mSubscriptions.add(subscription);
    }

    protected List<Subscription> createSubscriptions() {
        return new ArrayList<>(0);
    }

    // Метод для тестовых целей
    // Вывод списка extras для activity
    public void printExtras(Intent i) {
        if (i != null) {
            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                // Вывод полного списка extras
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Log.e("ExtrasAcct", "[" + key + "=" + bundle.get(key) + "]");
                }
            }
        }
    }

    protected void sendPushToken() {
        LogonEvent logonEvent = AppDataCache.getInstance().getLogonEvent();
        if (logonEvent != null) {
            final LogonResponse logonResponse = logonEvent.getLogonResponse();

            String phone = logonResponse.getMessagingAddressForDynamic();
            RxDataManager.getInstance().executeESBPushToken(phone, NotificationUtils.isNotificationsEnabled(getApplicationContext()) ? "add" : "off").subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean aBoolean) {

                }
            });
        }
    }

    public interface UserBlockedListener {
        void onUserBlocked();
    }

    public interface BaseConfirmDialogListener {
        void onConfirmDialogPositiveClick(String action);

        void onConfirmDialogNegativeClick(String action);

        void onInfoDialogPositiveClick(String action);
    }

}
