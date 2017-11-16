package paynd.stripeandroidissue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceParams;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "### DEMO";
    private Stripe stripe = null;

    private void init() {
        stripe = new Stripe(this, BuildConfig.KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        init();
    }

    public void onPress(View view) {
        SourceParams sourceParams = SourceParams.createAlipaySingleUseParams(
                50,
                "EUR",
                "Mr. Sample",
                "mrpaynd@gmail.com",
                "example://stripe-redirect");
        stripe.createSource(sourceParams, new SourceCallback() {
            @Override
            public void onError(Exception error) {
                Log.d(TAG, "SourceCallback: error: " + error.toString());
            }

            @Override
            public void onSuccess(Source source) {
                if (Source.REDIRECT.equals(source.getFlow())) {
                    Log.d(TAG, "Source: " + source);
                    String redirectUrl = source.getRedirect().getUrl();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                    startActivity(browserIntent);
                } else {
                    Log.d(TAG, "onSuccess: No redirect!");
                }
            }
        });
    }
}
