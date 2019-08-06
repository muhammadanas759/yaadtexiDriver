package com.taxidriver.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taxidriver.app.ApiResponse.PastTripDetailResponse.User;
import com.taxidriver.app.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class DisplayProfile extends AppCompatActivity {
    private CircleImageView userImage;
    private TextView name,phone,email;
    private RatingBar rating ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        User user=null;
        if(getIntent()!=null){
            Bundle bundle=getIntent().getExtras().getBundle("data");
            user= (User) bundle.get("user");
        }

        userImage=findViewById(R.id.updateImage);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.updatenumber);
        email=findViewById(R.id.updateemail);
        rating=findViewById(R.id.rating);


        assert user != null;
        Picasso.get()
                .load("http://yaadtaxi.com/userprofilepics/" + user.getPicture())
                .placeholder(R.drawable.ic_dummy_user)
                .into(userImage);

        name.setText(user.getFirstName()+" "+user.getLastName());
        rating.setRating(Float.valueOf(user.getRating()));
        phone.setText(user.getMobile());

        email.setText(user.getEmail());


    }
}
