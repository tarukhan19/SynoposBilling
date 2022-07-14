package com.proj.synoposbilling.Interface;

import com.proj.synoposbilling.Model.SubCategoryDTO;

public interface SubcategoryAdpaterListener {
    void onProductSelected(SubCategoryDTO detailDTO);

    void filterProduct(String query) ;
}
