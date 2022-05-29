package com.gmail.reater.last.justjavanew;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int CUP_OF_TEA_PRICE = 5;
    public static final int WHIPPED_CREAM_PRICE = 1;
    public static final int CHOCOLATE_PRICE = 2;
    public static final int MIN_COUNT_CUP_OF_TEA = 1;
    public static final int MAX_COUNT_CUP_OF_TEA = 20;
    private int quantityOfTea = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {
        String summaryMessage = getSummaryMessage();
        composeEmail(null, summaryMessage);
    }

    @NonNull
    private String getSummaryMessage() {
        EditText nameEditText = findViewById(R.id.input_name);
        Editable name = nameEditText.getText();
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream);
        boolean isWhippedCreamChecked = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate);
        boolean isChocolateChecked = chocolateCheckBox.isChecked();
        int price = calculatePrice(quantityOfTea, isWhippedCreamChecked, isChocolateChecked);
        return getOrderSummaryMessage(name.toString(), isWhippedCreamChecked, isChocolateChecked, price);
    }

    public void submitInfo(View view) {
        String summaryMessage = getSummaryMessage();
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(summaryMessage);
    }

    public void submitPlus(View view) {
        if (quantityOfTea < MAX_COUNT_CUP_OF_TEA) {
            quantityOfTea++;
        } else {
            Toast.makeText(this, getString(R.string.max_count_cup_label), Toast.LENGTH_SHORT).show();
        }
        display(quantityOfTea);
    }

    public void submitMinus(View view) {
        if (quantityOfTea > MIN_COUNT_CUP_OF_TEA) {
            quantityOfTea--;
        } else {
            Toast.makeText(this, getString(R.string.min_count_cup_label), Toast.LENGTH_SHORT).show();
        }
        display(quantityOfTea);
    }

    private int calculatePrice(int quantity, boolean isWhippedCreamChecked, boolean isChocolateChecked) {
        int price = CUP_OF_TEA_PRICE;
        if (isWhippedCreamChecked) {
            price += WHIPPED_CREAM_PRICE;
        }
        if (isChocolateChecked) {
            price += CHOCOLATE_PRICE;
        }
        return quantity * price;
    }

    private String getOrderSummaryMessage(
            String name,
            boolean isWhippedCreamChecked,
            boolean isChocolateChecked,
            int totalPrice
    ) {
        return String.format(
                getString(R.string.order_summary_name, name.trim()) + "\n" +
                        getString(R.string.whipped_cream_label) + ": %b\n" +
                        getString(R.string.chocolate_label) + ": %b\n" +
                        getString(R.string.quantity_label) + ": %d\n" +
                        getString(R.string.total_price_label) + ": $%d \n" +
                        getString(R.string.thanks_label),
                isWhippedCreamChecked,
                isChocolateChecked,
                quantityOfTea,
                totalPrice
        );
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @SuppressLint("DefaultLocale")
    private void display(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format("%d", number));
    }
}