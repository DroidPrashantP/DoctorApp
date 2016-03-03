package com.midoconline.app.ui.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.midoconline.app.R;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.ui.activities.util.ErrorDialogFragment;
import com.midoconline.app.ui.interfaces.PaymentForm;
import com.midoconline.app.ui.interfaces.TokenList;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class PaymentActivity extends FragmentActivity {

    /*
     * Change this to your publishable key.
     *
     * You can get your key here: https://manage.stripe.com/account/apikeys
     */
//    public static final String PUBLISHABLE_KEY = "sk_test_0mwlAmQVrlBfQJ5sHlgmXQQy";  // test key
    public static final String PUBLISHABLE_KEY = "pk_test_rtY8DogK3CGi5lBmPTZVA7AZ"; // publishable test key
//    public static final String PUBLISHABLE_KEY = "sk_live_LQlW1Ljf8PpqTPGeLtPveDvs"; //  live key
//    public static final String PUBLISHABLE_KEY = "pk_live_cM3pk51kT0hLVYa0SIBGB8BB";  // live publishable key

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

    }

    public void saveCreditCard(PaymentForm form) {

        Card card = new Card(
                form.getCardNumber(),
                form.getExpMonth(),
                form.getExpYear(),
                form.getCvc());

        boolean validation = card.validateCard();
        if (validation) {
            startProgress();
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            getTokenList().addToList(token);
                            finishProgress();
                        }

                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    private void startProgress() {
        Utils.showProgress(this);
    }

    private void finishProgress() {
        Utils.closeProgress();
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }

    private TokenList getTokenList() {
        return (TokenList) (getSupportFragmentManager().findFragmentById(R.id.token_list));
    }
}