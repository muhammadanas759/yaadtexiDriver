package com.taxidriver.app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;


import retrofit2.Call;
import retrofit2.Callback;

public class Wallet extends AppCompatActivity {

    User data;
    private TextView firstwallet, secondWallet, thirdwallet, totalAmount;
    private EditText tripamount;
    private Button addmoney;
    private Services mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        firstwallet = findViewById(R.id.walletfirst);
        secondWallet = findViewById(R.id.walletsecond);
        thirdwallet = findViewById(R.id.walletthird);
        totalAmount = findViewById(R.id.walletamount);
        tripamount = findViewById(R.id.wallettripamount);
        addmoney = findViewById(R.id.addmoney);
        mApi = Utils.getApiService();
        data = (User) LocalPersistence.readObjectFromFile(Wallet.this);

        firstwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripamount.setText("199");
            }
        });
        secondWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripamount.setText("599");
            }
        });
        thirdwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripamount.setText("1099");
            }
        });
//        addmoney.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!tripamount.getText().toString().isEmpty()) {
//                    mApi.AddMoney("Bearer " + data.getAccessToken(),
//                            tripamount.getText().toString(),
//                            "321321231321"
//                    ).enqueue(new Callback<WalletUserResponse>() {
//                        @Override
//                        public void onResponse(Call<WalletUserResponse> call, retrofit2.Response<WalletUserResponse> response) {
//                            if (response.isSuccessful()) {
//
//                                Toast.makeText(Wallet.this, "added successfully", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Toast.makeText(Wallet.this, "added ", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<WalletUserResponse> call, Throwable t) {
//                            Toast.makeText(Wallet.this, "failure", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(Wallet.this, "Enter amount first", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

}
