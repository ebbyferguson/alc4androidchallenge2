package com.fergispm.alc4android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle, txtDescription, txtPrice;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        }
        this.deal =deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_SHORT).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_SHORT).show();
                backToList();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    public void saveDeal(){
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());

        if (deal.getId() == null){
            mDatabaseReference.push().setValue(deal);
        }else {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }

    }

    private void backToList(){
        Intent iList = new Intent(this, ListActivity.class);
        startActivity(iList);
    }

    private void deleteDeal(){
        if (deal == null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
        }else {
            mDatabaseReference.child(deal.getId()).removeValue();
        }
    }

    private void clean(){
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }
}
