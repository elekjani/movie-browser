package hu.janos.elek.udacity.android.movies.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import hu.janos.elek.udacity.android.movies.R;

public class MovieDetailsActivity extends AppCompatActivity {

    private Toast mToast;
    DetailsAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        RecyclerView mNumbersList = (RecyclerView) findViewById(R.id.details);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(linearLayoutManager);

        mNumbersList.setHasFixedSize(true);

        mAdapter = new DetailsAdapter(getIntent());
        mNumbersList.setAdapter(mAdapter);
    }

    public void showError(int resId) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        mToast.show();
    }

    public void likeIt(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, "Liek It!", Toast.LENGTH_LONG);
        mToast.show();
    }


}
