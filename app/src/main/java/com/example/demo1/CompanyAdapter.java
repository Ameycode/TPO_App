package com.example.demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CompanyAdapter extends ArrayAdapter<Company> {

    private Context mContext;
    private ArrayList<Company> companyList;

    public CompanyAdapter(@NonNull Context context, ArrayList<Company> list) {
        super(context, 0, list);
        mContext = context;
        companyList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.company_list_item, parent, false);
        }

        Company currentCompany = companyList.get(position);

        TextView name = listItem.findViewById(R.id.company_name);
        name.setText(currentCompany.getCompanyName());

        TextView criteria = listItem.findViewById(R.id.company_criteria);
        criteria.setText(currentCompany.getCompanyCriteria());

        TextView description = listItem.findViewById(R.id.company_description);
        description.setText(currentCompany.getDescription());

        TextView higherPackage = listItem.findViewById(R.id.company_higher_package);
        higherPackage.setText(currentCompany.getHigherPackage());

        TextView averagePackage = listItem.findViewById(R.id.company_average_package);
        averagePackage.setText(currentCompany.getAveragePackage());

        TextView lowerPackage = listItem.findViewById(R.id.company_lower_package);
        lowerPackage.setText(currentCompany.getLowerPackage());

        TextView department = listItem.findViewById(R.id.company_depertment);
        department.setText(currentCompany.getDepartment());

        ImageView imageView = listItem.findViewById(R.id.company_image);
        if (currentCompany.getImageUrl() != null && !currentCompany.getImageUrl().isEmpty()) {
            Glide.with(mContext).load(currentCompany.getImageUrl()).into(imageView);
        }

        return listItem;
    }
}
