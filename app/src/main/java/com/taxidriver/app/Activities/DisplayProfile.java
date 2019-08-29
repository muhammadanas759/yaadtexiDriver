package com.taxidriver.app.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    com.taxidriver.app.ApiResponse.UpcomingDetailResponse.User users;
    int no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
// my_child_toolbar is defined in the layout file


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Profile");
        User user=null;
        if(getIntent()!=null){
            Bundle bundle=getIntent().getExtras().getBundle("data");
            no=bundle.getInt("from");
            if (no==0)
            user= (User) bundle.get("user");

            else
                users= (com.taxidriver.app.ApiResponse.UpcomingDetailResponse.User) bundle.get("user");
        }

        userImage=findViewById(R.id.updateImage);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.updatenumber);
        email=findViewById(R.id.updateemail);
        rating=findViewById(R.id.rating);


        if  (user!=null) {
            Picasso.get()
                    .load("http://yaadtaxi.com/userprofilepics/" + user.getPicture())
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(userImage);

            name.setText(user.getFirstName() + " " + user.getLastName());
            rating.setRating(Float.valueOf(user.getRating()));
            phone.setText(user.getMobile());

            email.setText(user.getEmail());
        }else{
            Picasso.get()
                    .load("http://yaadtaxi.com/userprofilepics/" + users.getPicture())
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(userImage);

            name.setText(users.getFirstName() + " " + users.getLastName());
            rating.setRating(Float.valueOf(users.getRating()));
            phone.setText(users.getMobile());

            email.setText(users.getEmail());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            Log.e("value", "onOptionsItemSelected: "+item.getTitle());
                if (item.getItemId()==android.R.id.home) {
                  finish();
                }
            return true;
    }
}
